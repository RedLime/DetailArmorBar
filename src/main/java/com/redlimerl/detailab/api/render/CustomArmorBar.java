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
        if (renderInfo.isShown()) return;

        RenderSystem.setShaderTexture(0, renderInfo.getTexture());

        if (isHalf) {
            InGameDrawer.drawTexture(context, xPos, yPos, renderInfo.getTextureOffsetHalf().x, renderInfo.getTextureOffsetHalf().y,
                    renderInfo.getTextureWidth(), renderInfo.getTextureHeight(), renderInfo.getColor(), isMirror);
        } else {
            InGameDrawer.drawTexture(context, xPos, yPos, renderInfo.getTextureOffsetFull().x, renderInfo.getTextureOffsetFull().y,
                    renderInfo.getTextureWidth(), renderInfo.getTextureHeight(), renderInfo.getColor(), false);
        }
    }

    public void drawOutLine(ItemStack itemStack, DrawContext context, int xPos, int yPos, boolean isHalf, boolean isMirror, Color color) {
        BarRenderManager renderInfo = predicate.apply(itemStack);
        if (renderInfo.isShown()) return;

        RenderSystem.setShaderTexture(0, renderInfo.getTexture());

        TextureOffset offset = isHalf ? renderInfo.getTextureOffsetOutlineHalf() : renderInfo.getTextureOffsetOutline();

        if (isHalf) {
            if (renderInfo instanceof ItemBarRenderManager) {
                InGameDrawer.drawTexture(context, xPos + 4, yPos, offset.x + 4, offset.y, 5, 9,
                        renderInfo.getTextureWidth(), renderInfo.getTextureHeight(), color, false);
            } else {
                InGameDrawer.drawTexture(context, xPos, yPos, offset.x, offset.y,
                        renderInfo.getTextureWidth(), renderInfo.getTextureHeight(), color, isMirror);
            }
        } else {
            InGameDrawer.drawTexture(context, xPos, yPos, offset.x, offset.y,
                    renderInfo.getTextureWidth(), renderInfo.getTextureHeight(), color, false);
        }
    }
}
