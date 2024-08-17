package com.redlimerl.detailab.api.render;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.text.Style;
import net.minecraft.util.dynamic.Codecs;

import java.util.List;

public class TextureOffset {
    public static final Codec<TextureOffset> CODEC = Codec.list(Codecs.NONNEGATIVE_INT)
            .xmap(ints -> new TextureOffset(ints.get(0), ints.get(1)),
                    texoff -> List.of(texoff.x, texoff.y)
            );

    public final int x;
    public final int y;

    public TextureOffset(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
