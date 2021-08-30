package com.redlimerl.detailab.api.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.redlimerl.detailab.DetailArmorBar;
import com.redlimerl.detailab.render.InGameDrawer;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.function.Function;

public class CustomArmorBar {

    public static CustomArmorBar DEFAULT = new CustomArmorBar(itemStack -> new ArmorBarRenderManager(DetailArmorBar.GUI_ARMOR_BAR, 128, 128,
            new TextureOffset(63, 9), new TextureOffset(54, 9), new TextureOffset(27, 0), new TextureOffset(9, 0)));
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

    public void draw(ItemStack itemStack, MatrixStack matrices, int xPos, int yPos, boolean isHalf, boolean isMirror) {
        BarRenderManager renderInfo = predicate.apply(itemStack);
        if (renderInfo.isShown()) return;

        Minecraft.getInstance().getTextureManager().bind(renderInfo.getTexture());

        if (isHalf) {
            InGameDrawer.drawTexture(matrices, xPos, yPos, renderInfo.getTextureOffsetHalf().x, renderInfo.getTextureOffsetHalf().y,
                    renderInfo.getTextureWidth(), renderInfo.getTextureHeight(), renderInfo.getColor(), isMirror);
        } else {
            InGameDrawer.drawTexture(matrices, xPos, yPos, renderInfo.getTextureOffsetFull().x, renderInfo.getTextureOffsetFull().y,
                    renderInfo.getTextureWidth(), renderInfo.getTextureHeight(), renderInfo.getColor(), false);
        }
    }

    public void drawOutLine(ItemStack itemStack, MatrixStack matrices, int xPos, int yPos, boolean isHalf, boolean isMirror, Color color) {
        BarRenderManager renderInfo = predicate.apply(itemStack);
        if (renderInfo.isShown()) return;

        Minecraft.getInstance().getTextureManager().bind(renderInfo.getTexture());

        if (isHalf) {
            if (renderInfo instanceof ItemBarRenderManager) {
                InGameDrawer.drawTexture(matrices, xPos + 4, yPos, renderInfo.getTextureOffsetOutlineHalf().x + 4, renderInfo.getTextureOffsetOutlineHalf().y, 5, 9,
                        renderInfo.getTextureWidth(), renderInfo.getTextureHeight(), color, false);
            } else {
                InGameDrawer.drawTexture(matrices, xPos, yPos, renderInfo.getTextureOffsetOutlineHalf().x, renderInfo.getTextureOffsetOutlineHalf().y,
                        renderInfo.getTextureWidth(), renderInfo.getTextureHeight(), color, isMirror);
            }
        } else {
            InGameDrawer.drawTexture(matrices, xPos, yPos, renderInfo.getTextureOffsetOutline().x, renderInfo.getTextureOffsetOutline().y,
                    renderInfo.getTextureWidth(), renderInfo.getTextureHeight(), color, false);
        }
    }
}
