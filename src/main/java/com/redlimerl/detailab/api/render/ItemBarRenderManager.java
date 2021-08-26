package com.redlimerl.detailab.api.render;

import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.awt.*;

public class ItemBarRenderManager extends BarRenderManager {

    private final ResourceLocation texture;
    private final int textureWidth;
    private final int textureHeight;
    private final TextureOffset textureOffsetFull;
    private final TextureOffset textureOffsetOutline;
    private final Color color;
    private final boolean isShown;

    public ItemBarRenderManager(ResourceLocation texture, int textureWidth, int textureHeight, TextureOffset textureOffsetFull,
                                 TextureOffset textureOffsetOutline, boolean isShown) {
        this(texture, textureWidth, textureHeight, textureOffsetFull, textureOffsetOutline, isShown, Color.WHITE);
    }

    public ItemBarRenderManager(ResourceLocation texture, int textureWidth, int textureHeight, TextureOffset textureOffsetFull,
                                 TextureOffset textureOffsetOutline, boolean isShown, Color color) {
        this.texture = texture;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.textureOffsetFull = textureOffsetFull;
        this.textureOffsetOutline = textureOffsetOutline;
        this.isShown = isShown;
        this.color = color;
    }


    @Nonnull
    @Override
    public ResourceLocation getTexture() {
        return this.texture;
    }

    @Override
    public int getTextureWidth() {
        return this.textureWidth;
    }

    @Override
    public int getTextureHeight() {
        return this.textureHeight;
    }

    @Nonnull
    @Override
    public TextureOffset getTextureOffsetFull() {
        return this.textureOffsetFull;
    }

    @Nonnull
    @Override
    public TextureOffset getTextureOffsetHalf() {
        return this.textureOffsetFull;
    }

    @Nonnull
    @Override
    public TextureOffset getTextureOffsetOutline() {
        return this.textureOffsetOutline;
    }

    @Nonnull
    @Override
    public TextureOffset getTextureOffsetOutlineHalf() {
        return this.textureOffsetOutline;
    }

    @Override
    public boolean isShown() {
        return !this.isShown;
    }

    @Nonnull
    @Override
    public Color getColor() {
        return this.color;
    }
}
