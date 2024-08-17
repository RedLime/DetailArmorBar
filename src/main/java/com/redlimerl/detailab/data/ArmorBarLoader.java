package com.redlimerl.detailab.data;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.redlimerl.detailab.DetailArmorBar;
import com.redlimerl.detailab.api.DetailArmorBarAPI;
import com.redlimerl.detailab.api.render.ArmorBarRenderManager;
import com.redlimerl.detailab.api.render.CustomArmorBar;
import com.redlimerl.detailab.api.render.ItemBarRenderManager;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.util.Map;
import java.util.Optional;

public class ArmorBarLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private Map<ArmorItem, CustomArmorBar> armorList;
    private Map<Item, CustomArmorBar> itemList;

    public ArmorBarLoader() {
        super(GSON, "armor_bar");
    }

    public Map<ArmorItem, CustomArmorBar> getArmorList() {
        return armorList;
    }

    public Map<Item, CustomArmorBar> getItemList() {
        return itemList;
    }

    @Override
    public Identifier getFabricId() {
        return Identifier.of(DetailArmorBar.MOD_ID, "armor");
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        ImmutableMap.Builder<ArmorItem, CustomArmorBar> armorBuilder =
                ImmutableMap.<ArmorItem, CustomArmorBar>builder().putAll(DetailArmorBarAPI.getStaticArmorBarList());

        ImmutableMap.Builder<Item, CustomArmorBar> itemBarBuilder =
                ImmutableMap.<Item, CustomArmorBar>builder().putAll(DetailArmorBarAPI.getStaticItemBarList());

        prepared.forEach((id, json) -> {
            try {
                JsonObject root = json.getAsJsonObject();
                JsonElement itemsJson = root.get("items");
                JsonElement rendererJson = root.get("render");
                String type = root.get("type").getAsString();

                if(!itemsJson.isJsonArray()) {
                    DetailArmorBar.LOGGER.error("Missing or broken item list in armor definition {}", id);
                    return;
                } else if(!rendererJson.isJsonObject()) {
                    DetailArmorBar.LOGGER.error("Missing or broken renderer in armor definition {}", id);
                    return;
                }

                if(type.equals("armor")) {
                    Optional<ArmorItem[]> items = Codec.list(Identifier.CODEC)
                            .decode(JsonOps.INSTANCE, itemsJson)
                            .resultOrPartial(err -> DetailArmorBar.LOGGER.error("Invalid items in armor definition [{}]: {}", id, err))
                            .map(pair -> pair.getFirst().stream()
                                    .map(Registries.ITEM::get)
                                    .filter(this::filterAndLogArmor)
                                    .toArray(ArmorItem[]::new));

                    Optional<ArmorBarRenderManager> renderer = ArmorBarRenderManager.CODEC.decode(JsonOps.INSTANCE, rendererJson)
                            .resultOrPartial(err -> DetailArmorBar.LOGGER.error("Failed to load render manager [{}]: {}", id, err))
                            .map(Pair::getFirst);

                    // TODO: Rendering predicates and stuff
                    Optional<CustomArmorBar> barOpt = renderer.map(r -> new CustomArmorBar(stack -> r));

                    items.ifPresentOrElse(list -> barOpt.ifPresentOrElse(bar -> {
                                for (ArmorItem item : list) {
                                    armorBuilder.put(item, bar);
                                }
                            }, () -> DetailArmorBar.LOGGER.error("Armor definition {} is missing a renderer!", id)),
                            () -> DetailArmorBar.LOGGER.error("Invalid or empty item list in armor definition {}", id));

                } else if (type.equals("item")) {
                    Optional<Item[]> items = Codec.list(Identifier.CODEC)
                            .decode(JsonOps.INSTANCE, itemsJson)
                            .resultOrPartial(err -> DetailArmorBar.LOGGER.error("Invalid items in item definition [{}]: {}", id, err))
                            .map(pair -> pair.getFirst().stream()
                                    .map(Registries.ITEM::get)
                                    .toArray(Item[]::new));

                    Optional<ItemBarRenderManager> renderer = ItemBarRenderManager.CODEC.decode(JsonOps.INSTANCE, rendererJson)
                            .resultOrPartial(err -> DetailArmorBar.LOGGER.error("Failed to load item render manager [{}]: {}", id, err))
                            .map(Pair::getFirst);

                    // TODO: Rendering predicates and stuff
                    Optional<CustomArmorBar> barOpt = renderer.map(r -> new CustomArmorBar(stack -> r));

                    items.ifPresentOrElse(list -> barOpt.ifPresentOrElse(bar -> {
                                for (Item item : list) {
                                    itemBarBuilder.put(item, bar);
                                }
                            }, () -> DetailArmorBar.LOGGER.error("Item definition {} is missing a renderer!", id)),
                            () -> DetailArmorBar.LOGGER.error("Invalid or empty item list in item definition {}", id));

                } else {
                    throw new IllegalArgumentException("Invalid type " + type);
                }
            } catch (Exception ex) {
                DetailArmorBar.LOGGER.error("Failed to load armor definition [{}]: {}", id, ex);
            }

        });

        armorList = armorBuilder.build();
        itemList = itemBarBuilder.build();
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
