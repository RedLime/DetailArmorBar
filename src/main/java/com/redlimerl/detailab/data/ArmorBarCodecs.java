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

    public static final Codec<Color> NUM_COLOR_CODEC = Codec.INT.xmap(
            Color::new,
            Color::getRGB
    );

    public static final Codec<Color> HEX_COLOR_CODEC = Codec.STRING.xmap(
            hexCode -> new Color(Integer.parseInt(hexCode, 16)),
            col -> Integer.toHexString(col.getRGB())
    );

    public static final Codec<Color> RGB_COLOR_CODEC = Codecs.NONNEGATIVE_INT.listOf(3, 3).xmap(
            ints -> new Color(ints.get(0), ints.get(1), ints.get(2)),
            col -> List.of(col.getRed(), col.getGreen(), col.getBlue())
    );

    public static final Codec<Color> COLOR_CODEC = Codec.lazyInitialized(() ->
            Codec.withAlternative(NUM_COLOR_CODEC, Codec.withAlternative(RGB_COLOR_CODEC, HEX_COLOR_CODEC))
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
