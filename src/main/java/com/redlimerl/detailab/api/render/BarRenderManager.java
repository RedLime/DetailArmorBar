package com.redlimerl.detailab.api.render;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public abstract class BarRenderManager {

    @NotNull
    public abstract Identifier getTexture();

    public abstract int getTextureWidth();

    public abstract int getTextureHeight();

    @NotNull
    public abstract TextureOffset getTextureOffsetFull();

    @NotNull
    public abstract TextureOffset getTextureOffsetHalf();

    @NotNull
    public abstract TextureOffset getTextureOffsetOutline();

    @NotNull
    public abstract TextureOffset getTextureOffsetOutlineHalf();

    public abstract boolean isShown();

    @NotNull
    public abstract Color getColor();
}
