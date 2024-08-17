package com.redlimerl.detailab.api.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.redlimerl.detailab.DetailArmorBar;
import com.redlimerl.detailab.render.InGameDrawer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.function.Function;

public class CustomArmorBar {

    public static CustomArmorBar DEFAULT = new CustomArmorBar(itemStack -> new ArmorBarRenderManager(DetailArmorBar.GUI_ARMOR_BAR, 128, 128,
            new TextureOffset(63, 9 + DetailArmorBar.isVanillaTexture()), new TextureOffset(54, 9 + DetailArmorBar.isVanillaTexture()),
            new TextureOffset(9, 0), new TextureOffset(27, 0)));

    public static CustomArmorBar EMPTY = new CustomArmorBar(itemStack -> {
        if (DetailArmorBar.getConfig().getOptions().toggleEmptyBar) {
            return new ArmorBarRenderManager(DetailArmorBar.GUI_ARMOR_BAR, 128, 128,
                    new TextureOffset(45, 0), new TextureOffset(45, 0), new TextureOffset(9, 0), new TextureOffset(27, 0));
        } else {
            return new ArmorBarRenderManager(
                    DetailArmorBar.GUI_ARMOR_BAR, 128, 128,
                    new TextureOffset(0, 0), new TextureOffset(0, 0), new TextureOffset(0, 0), new TextureOffset(0, 0));
        }
    });

    private final Function<ItemStack, ? extends BarRenderManager> predicate;

    public CustomArmorBar(Function<ItemStack, ? extends BarRenderManager> predicate) {
        this.predicate = predicate;
    }

    public void draw(ItemStack itemStack, DrawContext context, int xPos, int yPos, boolean isHalf, boolean isMirror) {
        BarRenderManager renderInfo = predicate.apply(itemStack);
        if (renderInfo.isHidden()) return;

        if (isHalf) {
            BarRenderManager.Texture textureInfo = renderInfo.getTextureHalf();
            RenderSystem.setShaderTexture(0, textureInfo.location());
            InGameDrawer.drawTexture(DetailArmorBar.GUI_ARMOR_BAR, context, xPos, yPos, textureInfo.offset().x, textureInfo.offset().y,
                    textureInfo.width(), textureInfo.height(), renderInfo.getColor(), isMirror);
        } else {
            BarRenderManager.Texture textureInfo = renderInfo.getTextureFull();
            RenderSystem.setShaderTexture(0, textureInfo.location());
            InGameDrawer.drawTexture(DetailArmorBar.GUI_ARMOR_BAR, context, xPos, yPos, textureInfo.offset().x, textureInfo.offset().y,
                    textureInfo.width(), textureInfo.height(), renderInfo.getColor(), false);
        }
    }

    public void drawOutLine(ItemStack itemStack, DrawContext context, int xPos, int yPos, boolean isHalf, boolean isMirror, Color color) {
        BarRenderManager renderInfo = predicate.apply(itemStack);
        if (renderInfo.isHidden()) return;

        BarRenderManager.Texture textureInfo = isHalf ? renderInfo.getTextureOutlineHalf() : renderInfo.getTextureOutline();
        TextureOffset offset = textureInfo.offset();
        RenderSystem.setShaderTexture(0, textureInfo.location());

        if (isHalf) {
            if (renderInfo instanceof ItemBarRenderManager) {
                InGameDrawer.drawTexture(DetailArmorBar.GUI_ARMOR_BAR, context, xPos + 4, yPos, offset.x + 4, offset.y, 5, 9,
                        textureInfo.width(), textureInfo.height(), color, false);
            } else {
                InGameDrawer.drawTexture(DetailArmorBar.GUI_ARMOR_BAR, context, xPos, yPos, offset.x, offset.y,
                        textureInfo.width(), textureInfo.height(), color, isMirror);
            }
        } else {
            InGameDrawer.drawTexture(DetailArmorBar.GUI_ARMOR_BAR, context, xPos, yPos, offset.x, offset.y,
                    textureInfo.width(), textureInfo.height(), color, false);
        }
    }
}
