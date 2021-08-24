package com.redlimerl.detailab.api.render;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ItemBarRenderManager extends BarRenderManager {

    private final Identifier texture;
    private final int textureWidth;
    private final int textureHeight;
    private final TextureOffset textureOffsetFull;
    private final TextureOffset textureOffsetOutline;
    private final Color color;
    private final boolean isShown;

    public ItemBarRenderManager(Identifier texture, int textureWidth, int textureHeight, TextureOffset textureOffsetFull,
                                 TextureOffset textureOffsetOutline, boolean isShown) {
        this(texture, textureWidth, textureHeight, textureOffsetFull, textureOffsetOutline, isShown, Color.WHITE);
    }

    public ItemBarRenderManager(Identifier texture, int textureWidth, int textureHeight, TextureOffset textureOffsetFull,
                                 TextureOffset textureOffsetOutline, boolean isShown, Color color) {
        this.texture = texture;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.textureOffsetFull = textureOffsetFull;
        this.textureOffsetOutline = textureOffsetOutline;
        this.isShown = isShown;
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
        return this.textureOffsetFull;
    }

    @Override
    public @NotNull TextureOffset getTextureOffsetOutline() {
        return this.textureOffsetOutline;
    }

    @Override
    public @NotNull TextureOffset getTextureOffsetOutlineHalf() {
        return this.textureOffsetOutline;
    }

    @Override
    public boolean isShown() {
        return !this.isShown;
    }

    @Override
    public @NotNull Color getColor() {
        return this.color;
    }
}
