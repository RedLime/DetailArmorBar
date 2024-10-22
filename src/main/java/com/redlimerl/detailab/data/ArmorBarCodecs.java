package com.redlimerl.detailab.data;

import com.mojang.serialization.Codec;
import net.minecraft.util.dynamic.Codecs;

import java.awt.*;
import java.util.List;

public final class ArmorBarCodecs {

    public static final Codec<Color> NUM_COLOR_CODEC = Codec.INT.xmap(
            Color::new,
            Color::getRGB
    );

    public static final Codec<Color> HEX_COLOR_CODEC = Codec.STRING.xmap(
            hexCode -> new Color(Integer.parseInt(hexCode, 16)),
            col -> Integer.toHexString(col.getRGB())
    );

    public static final Codec<Color> RGB_COLOR_CODEC = Codecs.NON_NEGATIVE_INT.listOf(3, 3).xmap(
            ints -> new Color(ints.get(0), ints.get(1), ints.get(2)),
            col -> List.of(col.getRed(), col.getGreen(), col.getBlue())
    );

    public static final Codec<Color> COLOR_CODEC = Codec.lazyInitialized(() ->
            Codec.withAlternative(NUM_COLOR_CODEC, Codec.withAlternative(RGB_COLOR_CODEC, HEX_COLOR_CODEC))
    );


}
