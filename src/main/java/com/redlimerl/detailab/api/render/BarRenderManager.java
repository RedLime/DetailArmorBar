package com.redlimerl.detailab.api.render;


import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.awt.*;

public abstract class BarRenderManager {

    @Nonnull
    public abstract ResourceLocation getTexture();

    public abstract int getTextureWidth();

    public abstract int getTextureHeight();

    @Nonnull
    public abstract TextureOffset getTextureOffsetFull();

    @Nonnull
    public abstract TextureOffset getTextureOffsetHalf();

    @Nonnull
    public abstract TextureOffset getTextureOffsetOutline();

    @Nonnull
    public abstract TextureOffset getTextureOffsetOutlineHalf();

    public abstract boolean isShown();

    @Nonnull
    public abstract Color getColor();
}
