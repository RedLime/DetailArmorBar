package com.redlimerl.detailab.api.render;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ArmorBarRenderManager extends BarRenderManager {

    private final Identifier texture;
    private final int textureWidth;
    private final int textureHeight;
    private final TextureOffset textureOffsetFull;
    private final TextureOffset textureOffsetHalf;
    private final TextureOffset textureOffsetOutline;
    private final TextureOffset textureOffsetOutlineHalf;
    private final Color color;

    public ArmorBarRenderManager(Identifier texture, int textureWidth, int textureHeight, TextureOffset textureOffsetFull, TextureOffset textureOffsetHalf,
                                 TextureOffset textureOffsetOutline, TextureOffset textureOffsetOutlineHalf) {
        this(texture, textureWidth, textureHeight, textureOffsetFull, textureOffsetHalf, textureOffsetOutline, textureOffsetOutlineHalf, Color.WHITE);
    }

    public ArmorBarRenderManager(Identifier texture, int textureWidth, int textureHeight, TextureOffset textureOffsetFull, TextureOffset textureOffsetHalf,
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


    @Override
    public @NotNull Identifier getTexture() {
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

    @Override
    public @NotNull TextureOffset getTextureOffsetFull() {
        return this.textureOffsetFull;
    }

    @Override
    public @NotNull TextureOffset getTextureOffsetHalf() {
        return this.textureOffsetHalf;
    }

    @Override
    public @NotNull TextureOffset getTextureOffsetOutline() {
        return this.textureOffsetOutline;
    }

    @Override
    public @NotNull TextureOffset getTextureOffsetOutlineHalf() {
        return this.textureOffsetOutlineHalf;
    }

    @Override
    public boolean isShown() {
        return false;
    }

    @Override
    public @NotNull Color getColor() {
        return this.color;
    }
}
