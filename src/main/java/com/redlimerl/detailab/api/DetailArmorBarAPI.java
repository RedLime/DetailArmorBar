package com.redlimerl.detailab.api;

import com.redlimerl.detailab.api.data.ArmorBarLoader;
import com.redlimerl.detailab.api.render.CustomArmorBar;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Map;

public class DetailArmorBarAPI {
    public static ArmorBarBuilder customArmorBarBuilder() {
        return new ArmorBarBuilder();
    }
    public static ItemBarBuilder customItemBarBuilder() {
        return new ItemBarBuilder();
    }

    public static final ArmorBarLoader LOADER = new ArmorBarLoader();
    static HashMap<ArmorItem, CustomArmorBar> staticArmorList = new HashMap<>();
    static HashMap<Item, CustomArmorBar> itemList = new HashMap<>();

    public static Map<ArmorItem, CustomArmorBar> getArmorBarList() {
        return LOADER.getArmorList();
    }
    public static Map<Item, CustomArmorBar> getItemBarList() {
        return itemList;
    }

    public static Map<ArmorItem, CustomArmorBar> getStaticArmorBarList() {
        return staticArmorList;
    }
    public static Map<Item, CustomArmorBar> getStaticItemBarList() {
        return itemList;
    }
}
