package com.redlimerl.detailab.data;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;
import com.redlimerl.detailab.api.render.BarRenderManager;
import com.redlimerl.detailab.api.render.ItemBarRenderManager;
import com.redlimerl.detailab.api.render.TextureOffset;
import net.minecraft.util.dynamic.Codecs;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public final class ArmorBarCodecs {

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

    public static final Codec<Map<String, TextureOffset>> TEXTURE_OFFSETS =
            Codec.simpleMap(Codec.STRING, TextureOffset.CODEC, Keyable.forStrings(ArmorBarCodecs::texOffsetKeyables)).codec();


    private static Stream<String> texOffsetKeyables() {
        return Stream.of("full", "half", "outline", "outline_half");
    }

    public static Map<String, TextureOffset> encodeTextureOffsetes(BarRenderManager manager) {
        ImmutableMap.Builder<String, TextureOffset> builder = ImmutableMap.builder();
        builder.put("full", manager.getTextureOffsetFull());
        builder.put("outline", manager.getTextureOffsetOutline());
        if(!(manager instanceof ItemBarRenderManager)) {
            builder.put("half", manager.getTextureOffsetHalf());
            builder.put("outline_half", manager.getTextureOffsetOutlineHalf());
        }
        return builder.build();
    }
}
