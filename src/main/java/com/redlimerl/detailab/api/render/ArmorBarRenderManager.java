package com.redlimerl.detailab.api.render;


import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.awt.*;

public class ArmorBarRenderManager extends BarRenderManager {

    private final ResourceLocation texture;
    private final int textureWidth;
    private final int textureHeight;
    private final TextureOffset textureOffsetFull;
    private final TextureOffset textureOffsetHalf;
    private final TextureOffset textureOffsetOutline;
    private final TextureOffset textureOffsetOutlineHalf;
    private final Color color;

    public ArmorBarRenderManager(ResourceLocation texture, int textureWidth, int textureHeight, TextureOffset textureOffsetFull, TextureOffset textureOffsetHalf,
                                 TextureOffset textureOffsetOutline, TextureOffset textureOffsetOutlineHalf) {
        this(texture, textureWidth, textureHeight, textureOffsetFull, textureOffsetHalf, textureOffsetOutline, textureOffsetOutlineHalf, Color.WHITE);
    }

    public ArmorBarRenderManager(ResourceLocation texture, int textureWidth, int textureHeight, TextureOffset textureOffsetFull, TextureOffset textureOffsetHalf,
                                 TextureOffset textureOffsetOutline, TextureOffset textureOffsetOutlineHalf, Color color) {
        this.texture = texture;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.textureOffsetFull = textureOffsetFull;
        this.textureOffsetHalf = textureOffsetHalf;
        this.textureOffsetOutline = textureOffsetOutline;
        this.textureOffsetOutlineHalf = textureOffsetOutlineHalf;
        this.color = color;
    }


    @Override @Nonnull
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

    @Override @Nonnull
    public TextureOffset getTextureOffsetFull() {
        return this.textureOffsetFull;
    }

    @Override @Nonnull
    public TextureOffset getTextureOffsetHalf() {
        return this.textureOffsetHalf;
    }

    @Override @Nonnull
    public TextureOffset getTextureOffsetOutline() {
        return this.textureOffsetOutline;
    }

    @Override @Nonnull
    public TextureOffset getTextureOffsetOutlineHalf() {
        return this.textureOffsetOutlineHalf;
    }

    @Override
    public boolean isShown() {
        return false;
    }

    @Override @Nonnull
    public Color getColor() {
        return this.color;
    }
}
