package com.redlimerl.detailab.api.render;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.redlimerl.detailab.ClientInitializer;
import com.redlimerl.detailab.DetailArmorBar;
import com.redlimerl.detailab.render.InGameDrawer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterial;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CustomArmorBar {

    // 覆盖层纹理位置|trim overlay location
    private static final ResourceLocation OVERLAY_TEXTURE =
            new ResourceLocation(DetailArmorBar.MOD_ID, "textures/armor_trim.png");

    // 彩色纹饰纹理缓存|cache color
    private static final Map<ResourceLocation, ResourceLocation> COLORED_TRIM_CACHE = new HashMap<>();
    private static final Map<ResourceLocation, Color[]> PALETTE_CACHE = new HashMap<>();

    public static CustomArmorBar DEFAULT = new CustomArmorBar(itemStack -> new ArmorBarRenderManager(ClientInitializer.GUI_ARMOR_BAR, 128, 128,
            new TextureOffset(63, 9 + DetailArmorBar.isVanillaTexture()), new TextureOffset(54, 9 + DetailArmorBar.isVanillaTexture()),
            new TextureOffset(9, 0), new TextureOffset(27, 0)));

    public static CustomArmorBar EMPTY = new CustomArmorBar(itemStack -> {
        if (DetailArmorBar.getConfig().getOptions().toggleEmptyBar) {
            return new ArmorBarRenderManager(ClientInitializer.GUI_ARMOR_BAR, 128, 128,
                    new TextureOffset(45, 0), new TextureOffset(45, 0), new TextureOffset(9, 0), new TextureOffset(27, 0));
        } else {
            return new ArmorBarRenderManager(
                    ClientInitializer.GUI_ARMOR_BAR, 128, 128,
                    new TextureOffset(0, 0), new TextureOffset(0, 0), new TextureOffset(0, 0), new TextureOffset(0, 0));
        }
    });

    private final Function<ItemStack, ? extends BarRenderManager> predicate;

    public CustomArmorBar(Function<ItemStack, ? extends BarRenderManager> predicate) {
        this.predicate = predicate;
    }

    public void draw(ItemStack itemStack, PoseStack matrices, int xPos, int yPos, boolean isHalf, boolean isMirror) {
        BarRenderManager renderInfo = predicate.apply(itemStack);
        if (renderInfo.isShown()) return;

        RenderSystem.setShaderTexture(0, renderInfo.getTexture());

        if (isHalf) {
            InGameDrawer.drawTexture(matrices, xPos, yPos, renderInfo.getTextureOffsetHalf().x(), renderInfo.getTextureOffsetHalf().y(),
                    renderInfo.getTextureWidth(), renderInfo.getTextureHeight(), renderInfo.getColor(), isMirror);
        } else {
            InGameDrawer.drawTexture(matrices, xPos, yPos, renderInfo.getTextureOffsetFull().x(), renderInfo.getTextureOffsetFull().y(),
                    renderInfo.getTextureWidth(), renderInfo.getTextureHeight(), renderInfo.getColor(), false);
        }

        IntegratedServer server = Minecraft.getInstance().getSingleplayerServer();
        // 只在有纹饰时渲染覆盖层
        if (server != null && ArmorTrim.getTrim(server.registryAccess(), itemStack).isPresent()) {
            TrimMaterial material = ArmorTrim.getTrim(server.registryAccess(), itemStack).get().material().get();
            renderTrimOverlay(material, matrices, xPos, yPos, isHalf, isMirror, itemStack.getItem() instanceof ElytraItem);
        }
    }
    // 渲染纹饰覆盖层
    private void renderTrimOverlay(TrimMaterial material, PoseStack matrices,
                                   int xPos, int yPos, boolean isHalf,
                                   boolean isMirror, boolean isElytra) {
        // 获取纹饰材料的ID和名称|get trim material's location and name
        ResourceLocation location = new ResourceLocation(material.assetName());
        String materialName = location.getPath().substring(location.getPath().lastIndexOf('/') + 1);

        // 获取或生成着色后的覆盖层纹理|get colored trim overlay
        ResourceLocation coloredOverlay = getColoredOverlayTexture(location, materialName);
        if (coloredOverlay == null) return;

        // 设置着色器纹理|set shader texture
        RenderSystem.setShaderTexture(0, coloredOverlay);

        // 获取覆盖层纹理的尺寸|set overlay texture's size
        int textureWidth = 18;
        int textureHeight = 18;

        // 渲染覆盖层|rendering trim overlay
        if (isElytra){
            InGameDrawer.drawTexture(matrices, xPos, yPos, 0, 9,
                    textureWidth, textureHeight, Color.WHITE, false);
        }
        else {
            if (isHalf) {
                InGameDrawer.drawTexture(matrices, xPos, yPos, 9, 0,
                        textureWidth, textureHeight, Color.WHITE, isMirror);
            } else {
                InGameDrawer.drawTexture(matrices, xPos, yPos, 0, 0,
                        textureWidth, textureHeight, Color.WHITE, false);
            }
        }
    }

    // 获取着色后的覆盖层纹理|get colored trim overlay
    private ResourceLocation getColoredOverlayTexture(ResourceLocation location, String materialName) {
        if (COLORED_TRIM_CACHE.containsKey(location)) {
            return COLORED_TRIM_CACHE.get(location);
        }

        // 加载调色板|load trim palette
        Color[] palette = loadPaletteColors(location, materialName);
        if (palette == null) {
            DetailArmorBar.LOGGER.error("Failed to load palette for material: {}", location);
            return null;
        }

        try {
            // 加载覆盖层灰度纹理|load trim overlay
            Resource resource = Minecraft.getInstance().getResourceManager().getResource(OVERLAY_TEXTURE).orElse(null);
            if (resource == null) {
                DetailArmorBar.LOGGER.error("Overlay texture not found: {}", OVERLAY_TEXTURE);
                return null;
            }
            NativeImage overlayImage;
            try (InputStream stream = resource.open()) {
                overlayImage = NativeImage.read(stream);
            }
            int width = overlayImage.getWidth();
            int height = overlayImage.getHeight();
            NativeImage coloredImage = new NativeImage(width, height, false);

            // 应用调色板着色|apply shader
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int argb = overlayImage.getPixelRGBA(x, y);
                    int newColor = getNewColor(argb, palette);
                    coloredImage.setPixelRGBA(x, y, newColor);
                }
            }

            // 创建动态纹理|coloring trim overlay
            DynamicTexture dynamicTexture = new DynamicTexture(coloredImage);
            ResourceLocation textureId = new ResourceLocation(
                    DetailArmorBar.MOD_ID,
                    "colored_trim_" + location.getNamespace() + "_" + location.getPath().replace('/', '_')
            );
            Minecraft.getInstance().getTextureManager().register(textureId, dynamicTexture);
            COLORED_TRIM_CACHE.put(location, textureId);
            overlayImage.close();

            return textureId;
        } catch (IOException e) {
            DetailArmorBar.LOGGER.error("Failed to generate colored trim texture", e);
            return null;
        }
    }
    private static int getNewColor(int argb, Color[] palette) {
        int a = FastColor.ABGR32.alpha(argb);
        int r = FastColor.ABGR32.red(argb);
        int g = FastColor.ABGR32.green(argb);
        int b = FastColor.ABGR32.blue(argb);

        // 精确匹配特定灰度值|overlay color->palette color
        int gray = (r + g + b) / 3;
        int index;
        switch (gray) {
            case 0x00: index = 7; break;   // 0
            case 0x20: index = 6; break;   // 32
            case 0x40: index = 5; break;   // 64
            case 0x60: index = 4; break;   // 96
            case 0x80: index = 3; break;   // 128
            case 0xA0: index = 2; break;   // 160
            case 0xC0: index = 1; break;   // 192
            case 0xE0: index = 0; break;   // 224
            default:
                return 0;
        }

        // 获取调色板颜色|get palette color
        Color paletteColor = palette[index];

        // 组合新颜色|put new color
        return FastColor.ABGR32.color(
                a, // 保留原始透明度
                paletteColor.getBlue(),
                paletteColor.getGreen(),
                paletteColor.getRed()
        );
    }

    // 加载调色板颜色|load palette
    private Color[] loadPaletteColors(ResourceLocation materialId, String materialName) {
        if (PALETTE_CACHE.containsKey(materialId)) {
            return PALETTE_CACHE.get(materialId);
        }

        // 调色板纹理|trim palette's location
        ResourceLocation paletteTexture = new ResourceLocation(
                materialId.getNamespace(),
                "textures/trims/color_palettes/" + materialName + ".png"
        );

        Color[] palette = new Color[8];
        try {
            Resource resource = Minecraft.getInstance().getResourceManager().getResource(paletteTexture).orElse(null);
            if (resource == null) {
                DetailArmorBar.LOGGER.error("Palette texture not found: {}", paletteTexture);
                return null;
            }

            // 加载调色板|load palette
            NativeImage paletteImage;
            try (InputStream stream = resource.open()) {
                paletteImage = NativeImage.read(stream);
            }

            if (paletteImage.getWidth() < 8 || paletteImage.getHeight() < 1) {
                DetailArmorBar.LOGGER.error("Invalid palette texture size: {}x{}",
                        paletteImage.getWidth(), paletteImage.getHeight());
                paletteImage.close();
                return null;
            }

            for (int i = 0; i < 8; i++) {
                int rgb = paletteImage.getPixelRGBA(i, 0);
                palette[i] = new Color(
                        FastColor.ABGR32.red(rgb),
                        FastColor.ABGR32.green(rgb),
                        FastColor.ABGR32.blue(rgb)
                );
            }

            // 缓存调色板|cache palette
            PALETTE_CACHE.put(materialId, palette);
            paletteImage.close();
            return palette;
        } catch (IOException e) {
            DetailArmorBar.LOGGER.error("Failed to load palette texture", e);
            return null;
        }
    }

    public void drawOutLine(ItemStack itemStack, PoseStack matrices, int xPos, int yPos, boolean isHalf, boolean isMirror, Color color) {
        BarRenderManager renderInfo = predicate.apply(itemStack);
        if (renderInfo.isShown()) return;

        RenderSystem.setShaderTexture(0, renderInfo.getTexture());

        if (isHalf) {
            if (renderInfo instanceof ItemBarRenderManager) {
                InGameDrawer.drawTexture(matrices, xPos + 4, yPos, renderInfo.getTextureOffsetOutlineHalf().x() + 4, renderInfo.getTextureOffsetOutlineHalf().y(), 5, 9,
                        renderInfo.getTextureWidth(), renderInfo.getTextureHeight(), color, false);
            } else {
                InGameDrawer.drawTexture(matrices, xPos, yPos, renderInfo.getTextureOffsetOutlineHalf().x(), renderInfo.getTextureOffsetOutlineHalf().y(),
                        renderInfo.getTextureWidth(), renderInfo.getTextureHeight(), color, isMirror);
            }
        } else {
            InGameDrawer.drawTexture(matrices, xPos, yPos, renderInfo.getTextureOffsetOutline().x(), renderInfo.getTextureOffsetOutline().y(),
                    renderInfo.getTextureWidth(), renderInfo.getTextureHeight(), color, false);
        }
    }
}
