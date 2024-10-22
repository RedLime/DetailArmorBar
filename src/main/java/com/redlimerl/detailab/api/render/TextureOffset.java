package com.redlimerl.detailab.api.render;

import com.mojang.serialization.Codec;
import net.minecraft.util.dynamic.Codecs;
import java.util.List;

public class TextureOffset {
    public static final Codec<TextureOffset> CODEC = Codecs.NON_NEGATIVE_INT.listOf(2, 2)
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
