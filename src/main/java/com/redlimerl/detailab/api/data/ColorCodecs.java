package com.redlimerl.detailab.api.data;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.util.dynamic.Codecs;

import java.awt.*;
import java.util.List;

public final class ColorCodecs {

    public static final Codec<Color> HEX_COLOR_CODEC = Codec.STRING.xmap(
            hexCode -> new Color(Integer.parseInt(hexCode, 16)),
            col -> Integer.toHexString(col.getRGB())
    );

    public static final Codec<Color> RGB_COLOR_CODEC = Codec.list(Codecs.NONNEGATIVE_INT).xmap(
            ints -> new Color(ints.get(0), ints.get(1), ints.get(2)),
            col -> List.of(col.getRed(), col.getGreen(), col.getBlue())
    );

    public static final Codec<Color> COLOR_CODEC = Codec.either(HEX_COLOR_CODEC, RGB_COLOR_CODEC).xmap(
            either -> either.map(c -> c, c -> c),
            Either::right // Default to rgb array codec for encoding
    );
}
