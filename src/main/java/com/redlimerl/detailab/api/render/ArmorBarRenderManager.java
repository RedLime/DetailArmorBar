package com.redlimerl.detailab.api.render;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.redlimerl.detailab.data.ArmorBarCodecs;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.Optional;

public class ArmorBarRenderManager extends BarRenderManager {

    public static final Codec<ArmorBarRenderManager> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("texture").forGetter(ArmorBarRenderManager::getTexture),
            Codecs.POSITIVE_INT.fieldOf("texture_width").forGetter(ArmorBarRenderManager::getTextureWidth),
            Codecs.POSITIVE_INT.fieldOf("texture_height").forGetter(ArmorBarRenderManager::getTextureHeight),
            ArmorBarCodecs.TEXTURE_OFFSETS.fieldOf("offsets").forGetter(ArmorBarCodecs::encodeTextureOffsetes),
            ArmorBarCodecs.COLOR_CODEC.optionalFieldOf("color").forGetter(m -> Optional.of(m.color))
    ).apply(instance, (id, width, height, offsets, color) ->
            new ArmorBarRenderManager(id, width, height, offsets.get("full"), offsets.get("half"),
                    offsets.get("outline"), offsets.get("outline_half"), color.orElse(Color.WHITE))
    ));

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
