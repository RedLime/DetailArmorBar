package com.redlimerl.detailab.api.render;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.redlimerl.detailab.data.ArmorBarCodecs;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;

public class ArmorBarRenderManager implements BarRenderManager {

    public static final Codec<ArmorBarRenderManager> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Texture.CODEC.fieldOf("full").forGetter(ArmorBarRenderManager::getTextureFull),
            Texture.CODEC.fieldOf("half").forGetter(ArmorBarRenderManager::getTextureHalf),
            Texture.CODEC.fieldOf("outline").forGetter(ArmorBarRenderManager::getTextureOutline),
            Texture.CODEC.fieldOf("outline_half").forGetter(ArmorBarRenderManager::getTextureOutlineHalf),
            ArmorBarCodecs.COLOR_CODEC.optionalFieldOf("color", Color.WHITE).forGetter(ArmorBarRenderManager::getColor)
    ).apply(instance, ArmorBarRenderManager::new));

    private final Texture textureFull;
    private final Texture textureHalf;
    private final Texture textureOutline;
    private final Texture textureOutlineHalf;
    private final Color color;

    public ArmorBarRenderManager(Identifier texture, int textureWidth, int textureHeight, TextureOffset textureOffsetFull, TextureOffset textureOffsetHalf,
                                 TextureOffset textureOffsetOutline, TextureOffset textureOffsetOutlineHalf) {
        this(texture, textureWidth, textureHeight, textureOffsetFull, textureOffsetHalf, textureOffsetOutline, textureOffsetOutlineHalf, Color.WHITE);
    }

    public ArmorBarRenderManager(Identifier texture, int textureWidth, int textureHeight, TextureOffset textureOffsetFull, TextureOffset textureOffsetHalf,
                                 TextureOffset textureOffsetOutline, TextureOffset textureOffsetOutlineHalf, Color color) {
        this(new Texture(texture, textureWidth, textureHeight, textureOffsetFull),
                new Texture(texture, textureWidth, textureHeight ,textureOffsetHalf),
                new Texture(texture, textureWidth, textureHeight, textureOffsetOutline),
                new Texture(texture, textureWidth, textureHeight, textureOffsetOutlineHalf),
                color);
    }

    public ArmorBarRenderManager(Texture full, Texture half, Texture outline, Texture outlineHalf) {
        this(full, half, outline, outlineHalf, Color.WHITE);
    }

    public ArmorBarRenderManager(Texture full, Texture half, Texture outline, Texture outlineHalf, Color color) {
        this.textureFull = full;
        this.textureHalf = half;
        this.textureOutline = outline;
        this.textureOutlineHalf = outlineHalf;
        this.color = color;
    }

    @Deprecated
    public @NotNull Identifier getTexture() {
        return textureFull.location();
    }

    @Deprecated
    public int getTextureWidth() {
        return textureFull.width();
    }

    @Deprecated
    public int getTextureHeight() {
        return textureFull.height();
    }

    @Override
    public @NotNull Texture getTextureFull() {
        return this.textureFull;
    }

    @Override
    public @NotNull Texture getTextureHalf() {
        return this.textureHalf;
    }

    @Override
    public @NotNull Texture getTextureOutline() {
        return this.textureOutline;
    }

    @Override
    public @NotNull Texture getTextureOutlineHalf() {
        return this.textureOutlineHalf;
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public @NotNull Color getColor() {
        return this.color;
    }


}
