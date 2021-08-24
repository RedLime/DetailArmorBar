package com.redlimerl.detailab.api;

import com.redlimerl.detailab.DetailArmorBar;
import com.redlimerl.detailab.api.render.CustomArmorBar;
import com.redlimerl.detailab.api.render.ItemBarRenderManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;

import java.util.function.Function;

public class ItemBarBuilder {
    private Item item;
    private Function<ItemStack, ItemBarRenderManager> predicate;


    /**
     * Specifies the {@link Item} on which the armor bar will appear when equipped.
     * @throws IllegalStateException If [item] is not Wearable, an error occurs.
     */
    public ItemBarBuilder item(Item item) {
        this.item = item;
        return this;
    }


    /**
     * Specifies the render options for the Armor Bar.
     * @see ItemBarRenderManager
     */
    public ItemBarBuilder render(Function<ItemStack, ItemBarRenderManager> renderManager) {
        predicate = renderManager;
        return this;
    }

    /**
     * Registers the Custom Armor Bar so that it can be displayed.
     * @throws IllegalStateException Not all items have been initialized. check out item, render.
     * @throws IllegalStateException If {@link #item} is not Wearable, an error occurs.
     */
    public void register() {
        try {
            DetailArmorBarAPI.itemList.put(item, new CustomArmorBar(predicate));
            DetailArmorBar.LOGGER.log(Level.INFO, "Successfully registered '"+ Registry.ITEM.getId(item) + "'!");
        } catch (Exception e) {
            throw new IllegalStateException("Not all items have been initialized");
        }
    }
}
