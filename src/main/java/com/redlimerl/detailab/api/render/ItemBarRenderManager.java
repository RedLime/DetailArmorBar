package com.redlimerl.detailab.api.render;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.redlimerl.detailab.data.ArmorBarCodecs;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ItemBarRenderManager implements BarRenderManager {

    public static final Codec<ItemBarRenderManager> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Texture.CODEC.fieldOf("main").forGetter(ItemBarRenderManager::getTextureFull),
            Texture.CODEC.fieldOf("outline").forGetter(ItemBarRenderManager::getTextureOutline),
            Codec.BOOL.optionalFieldOf("is_shown", true).forGetter(i -> i.isShown),
            ArmorBarCodecs.COLOR_CODEC.optionalFieldOf("color", Color.WHITE).forGetter(ItemBarRenderManager::getColor)
    ).apply(instance, ItemBarRenderManager::new));

    private final Texture textureFull;
    private final Texture textureOutline;
    private final Color color;
    private final boolean isShown;

    public ItemBarRenderManager(Identifier texture, int textureWidth, int textureHeight, TextureOffset textureOffsetFull,
                                 TextureOffset textureOffsetOutline, boolean isShown) {
        this(texture, textureWidth, textureHeight, textureOffsetFull, textureOffsetOutline, isShown, Color.WHITE);
    }

    public ItemBarRenderManager(Identifier texture, int textureWidth, int textureHeight, TextureOffset textureOffsetFull,
                                 TextureOffset textureOffsetOutline, boolean isShown, Color color) {
        this(new Texture(texture, textureWidth, textureHeight, textureOffsetFull), new Texture(texture, textureWidth, textureHeight, textureOffsetOutline),
                isShown, color);
    }

    public ItemBarRenderManager(Texture full, Texture outline, boolean isShown) {
        this(full, outline, isShown, Color.WHITE);
    }

    public ItemBarRenderManager(Texture full, Texture outline, boolean isShown, Color color) {
        this.textureFull = full;
        this.textureOutline = outline;
        this.isShown = isShown;
        this.color = color;
    }

    @Deprecated
    public @NotNull Identifier getTexture() {
        return this.textureFull.location();
    }

    @Deprecated
    public int getTextureWidth() {
        return this.textureFull.width();
    }

    @Deprecated
    public int getTextureHeight() {
        return this.textureFull.height();
    }

    @Override
    public @NotNull Texture getTextureFull() {
        return this.textureFull;
    }

    @Override
    public @NotNull Texture getTextureHalf() {
        return this.textureFull;
    }

    @Override
    public @NotNull Texture getTextureOutline() {
        return this.textureOutline;
    }

    @Override
    public @NotNull Texture getTextureOutlineHalf() {
        return this.textureOutline;
    }

    @Override
    public boolean isHidden() {
        return !this.isShown;
    }

    @Override
    public @NotNull Color getColor() {
        return this.color;
    }
}
