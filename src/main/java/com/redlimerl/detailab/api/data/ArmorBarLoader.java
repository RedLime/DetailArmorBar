package com.redlimerl.detailab.api.data;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.redlimerl.detailab.DetailArmorBar;
import com.redlimerl.detailab.api.DetailArmorBarAPI;
import com.redlimerl.detailab.api.render.ArmorBarRenderManager;
import com.redlimerl.detailab.api.render.CustomArmorBar;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ArmorBarLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public Map<ArmorItem, CustomArmorBar> armorList;

    public ArmorBarLoader() {
        super(GSON, "armor_bar");
    }

    @Override
    public Identifier getFabricId() {
        return Identifier.of(DetailArmorBar.MOD_ID, "armor");
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        ImmutableMap.Builder<ArmorItem, CustomArmorBar> armorBuilder =
                ImmutableMap.<ArmorItem, CustomArmorBar>builder().putAll(DetailArmorBarAPI.getStaticArmorBarList());

        prepared.forEach((id, json) -> {
            try {
                JsonObject root = json.getAsJsonObject();
                JsonElement itemsJson = root.get("armor");
                JsonElement rendererJson = root.get("renderer");

                Optional<ArmorItem[]> items = Codec.list(Identifier.CODEC)
                        .decode(JsonOps.INSTANCE, itemsJson)
                        .resultOrPartial(s -> DetailArmorBar.LOGGER.error("Invalid items in armor definition [{}] : {}", id, s))
                        .map(pair -> pair.getFirst().stream()
                                .map(Registries.ITEM::get)
                                .filter(this::filterAndLogArmor)
                                .toArray(ArmorItem[]::new));

                Optional<ArmorBarRenderManager> renderer = ArmorBarRenderManager.CODEC.decode(JsonOps.INSTANCE, rendererJson)
                        .resultOrPartial(err -> DetailArmorBar.LOGGER.error("Failed to load armor renderer [{}] : {}", id, err))
                        .map(Pair::getFirst);

                // TODO: Rendering predicates and stuff
                Optional<CustomArmorBar> barOpt = renderer.map(r -> new CustomArmorBar(stack -> r));

                items.ifPresent(list -> barOpt.ifPresent(bar -> {
                    for (ArmorItem item : list) {
                        armorBuilder.put(item, bar);
                    }
                }));
            } catch (Exception ex) {
                DetailArmorBar.LOGGER.error("Failed to load armor definition [{}]: {}", id, ex);
            }

        });

        armorList = armorBuilder.build();
    }

    private boolean filterAndLogArmor(Item input) {
        if(input instanceof ArmorItem) {
            return true;
        } else {
            DetailArmorBar.LOGGER.warn("Non-armor item in armor bar manager. Ignoring.");
            return false;
        }
    }
}
