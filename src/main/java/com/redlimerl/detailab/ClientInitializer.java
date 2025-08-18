package com.redlimerl.detailab;

import com.redlimerl.detailab.api.DetailArmorBarAPI;
import com.redlimerl.detailab.api.render.ArmorBarRenderManager;
import com.redlimerl.detailab.api.render.ItemBarRenderManager;
import com.redlimerl.detailab.api.render.TextureOffset;
import com.redlimerl.detailab.config.DABForgeConfig;
import com.redlimerl.detailab.screen.OptionsScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.awt.*;

public class ClientInitializer {
    public final static ResourceLocation GUI_ARMOR_BAR = new ResourceLocation(DetailArmorBar.MOD_ID, "textures/armor_bar.png");
    private final static String[] compatibilityMods = { "healthoverlay" };
    private final static TextureOffset outline = new TextureOffset(9, 0);
    private final static TextureOffset outlineHalf = new TextureOffset(27, 0);

    public static void onClientSetup() {
        initializeModFeatures();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, DABForgeConfig.SPEC);
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((mc, screen) -> new OptionsScreen(screen)));
    }

    private static void initializeModFeatures() {
        customArmorBar(new ArmorItem[]{(ArmorItem) Items.NETHERITE_HELMET, (ArmorItem) Items.NETHERITE_LEGGINGS, (ArmorItem) Items.NETHERITE_CHESTPLATE, (ArmorItem) Items.NETHERITE_BOOTS}, 9, 0);
        customArmorBar(new ArmorItem[]{(ArmorItem) Items.DIAMOND_HELMET, (ArmorItem) Items.DIAMOND_LEGGINGS, (ArmorItem) Items.DIAMOND_CHESTPLATE, (ArmorItem) Items.DIAMOND_BOOTS}, 27, 18);
        customArmorBar(new ArmorItem[]{(ArmorItem) Items.TURTLE_HELMET}, 45, 36);
        customArmorBar(new ArmorItem[]{(ArmorItem) Items.IRON_HELMET, (ArmorItem) Items.IRON_LEGGINGS, (ArmorItem) Items.IRON_CHESTPLATE, (ArmorItem) Items.IRON_BOOTS}, 63, 54);
        customArmorBar(new ArmorItem[]{(ArmorItem) Items.CHAINMAIL_HELMET, (ArmorItem) Items.CHAINMAIL_LEGGINGS, (ArmorItem) Items.CHAINMAIL_CHESTPLATE, (ArmorItem) Items.CHAINMAIL_BOOTS}, 81, 72);
        customArmorBar(new ArmorItem[]{(ArmorItem) Items.GOLDEN_HELMET, (ArmorItem) Items.GOLDEN_LEGGINGS, (ArmorItem) Items.GOLDEN_CHESTPLATE, (ArmorItem) Items.GOLDEN_BOOTS}, 99, 90);
        customArmorBar(new ArmorItem[]{(ArmorItem) Items.LEATHER_HELMET, (ArmorItem) Items.LEATHER_LEGGINGS, (ArmorItem) Items.LEATHER_CHESTPLATE, (ArmorItem) Items.LEATHER_BOOTS}, 117, 108);
        DetailArmorBarAPI.customItemBarBuilder().item(Items.ELYTRA)
                .render((ItemStack itemStack) -> {
                    if (itemStack.getItem() instanceof DyeableLeatherItem) {
                        int colorValue = getElytraColor(itemStack);
                        return new ItemBarRenderManager(GUI_ARMOR_BAR, 128, 128,
                                new TextureOffset(36, 0), new TextureOffset(54, 0), true, new Color(colorValue));}
                    return new ItemBarRenderManager(GUI_ARMOR_BAR, 128, 128,
                            new TextureOffset(36, 0), new TextureOffset(54, 0), true, Color.WHITE);
                }).register();
        for (String compatibilityMod : compatibilityMods) {
            if (ModList.get().getModObjectById(compatibilityMod).isPresent()) {
                DetailArmorBar.getConfig().getOptions().toggleCompatibleHeartMod = true;
            }
        }
    }

    //代码优化||code optimization
    private static void customArmorBar(ArmorItem[] armors, int x_full, int x_half) {
        DetailArmorBarAPI.customArmorBarBuilder().armor(armors)
                .render(itemStack -> {
                    if (itemStack.getItem() instanceof DyeableLeatherItem dyeableItem) {
                        int colorValue = dyeableItem.getColor(itemStack);
                        return createRenderManager(x_full, x_half, new Color(colorValue));
                    }
                    return createRenderManager(x_full, x_half, Color.WHITE);
                }).register();
    }
    private static ArmorBarRenderManager createRenderManager(
            int x_full, int x_half, Color color
    ) {
        int textureOffset = 9 + DetailArmorBar.isVanillaTexture();
        return new ArmorBarRenderManager(
                GUI_ARMOR_BAR, 128, 128,
                new TextureOffset(x_full, textureOffset),
                new TextureOffset(x_half, textureOffset),
                outline, outlineHalf,
                color
        );
    }

    //兼容鞘翅纹饰的鞘翅染色功能||combine elytra trim's elytra dying function
    private static int getElytraColor(ItemStack pStack) {
        CompoundTag $$1 = pStack.getTagElement("display");
        return $$1 != null && $$1.contains("color", 99) ? $$1.getInt("color") : 8421504;
    }
}
