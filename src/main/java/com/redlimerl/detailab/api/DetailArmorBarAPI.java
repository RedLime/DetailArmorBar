package com.redlimerl.detailab.api;

import com.redlimerl.detailab.api.render.CustomArmorBar;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public class DetailArmorBarAPI {
    public static ArmorBarBuilder customArmorBarBuilder() {
        return new ArmorBarBuilder();
    }
    public static ItemBarBuilder customItemBarBuilder() {
        return new ItemBarBuilder();
    }

    static HashMap<ArmorItem, CustomArmorBar> armorList = new HashMap<>();
    static HashMap<Item, CustomArmorBar> itemList = new HashMap<>();

    public static Map<ArmorItem, CustomArmorBar> getArmorBarList() {
        return armorList;
    }
    public static Map<Item, CustomArmorBar> getItemBarList() {
        return itemList;
    }
}
