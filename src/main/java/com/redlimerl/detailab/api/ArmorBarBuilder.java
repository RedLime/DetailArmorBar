package com.redlimerl.detailab.api;

import com.redlimerl.detailab.DetailArmorBar;
import com.redlimerl.detailab.api.render.ArmorBarRenderManager;
import com.redlimerl.detailab.api.render.CustomArmorBar;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Level;

import java.util.function.Function;

public class ArmorBarBuilder {
    private ArmorItem[] armor;
    private Function<ItemStack, ArmorBarRenderManager> predicate;

    /**
     * Specifies the {@link ArmorItem} on which the armor bar will appear when equipped.
     * @throws IllegalStateException [armorItem] aren't {@link ArmorItem}.
     */
    public ArmorBarBuilder armor(ArmorItem... armorItem) {
        try {
            armor = armorItem;
            return this;
        } catch (Exception e) {
            throw new IllegalStateException("This is not ArmorItem");
        }
    }

    /**
     * Specifies the render options for the Armor Bar.
     * @see ArmorBarRenderManager
     */
    public ArmorBarBuilder render(Function<ItemStack, ArmorBarRenderManager> renderManager) {
        predicate = renderManager;
        return this;
    }

    /**
     * Registers the Custom Armor Bar so that it can be displayed.
     * @throws IllegalStateException Not all items have been initialized. check out {@link #armor(ArmorItem...)}, {@link #render(Function)}
     */
    public void register() {
        try {
            CustomArmorBar armorBar = new CustomArmorBar(predicate);
            for (ArmorItem armorItem : armor) {
                DetailArmorBarAPI.armorList.put(armorItem, armorBar);
            }
            if (armor.length != 0 && ForgeRegistries.ITEMS.getKey(armor[0]) != null) {
                DetailArmorBar.LOGGER.log(Level.INFO, "Successfully registered '" + ForgeRegistries.ITEMS.getKey(armor[0]).toString() + (armor.length > 1 ? "' and "+(armor.length-1)+" more items" : "'") + "!");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Not all items have been initialized");
        }
    }
}
