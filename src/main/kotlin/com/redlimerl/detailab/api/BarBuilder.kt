package com.redlimerl.detailab.api

import com.redlimerl.detailab.DetailArmorBar
import com.redlimerl.detailab.item.CustomArmorBar
import com.redlimerl.detailab.item.CustomArmors
import net.minecraft.item.ArmorItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Wearable
import net.minecraft.util.registry.Registry
import org.apache.logging.log4j.Level


class ArmorBarBuilder {
    private lateinit var armor: Array<out ArmorItem>
    private lateinit var predicate: (ItemStack) -> ArmorBarRenderManager

    /**
     * Specifies the [ArmorItem] on which the armor bar will appear when equipped.
     * @throws IllegalStateException [armorItem] aren't [ArmorItem].
     */
    fun armor(vararg armorItem: Item): ArmorBarBuilder {
        try {
            armor = armorItem.map { item: Item -> item as ArmorItem }.toTypedArray()
            return this
        } catch (e: Exception) {
            throw IllegalStateException("This is not ArmorItem")
        }
    }

    /**
     * Specifies the render options for the Armor Bar.
     * @See ArmorBarRenderManager
     */
    fun render(renderManager: (ItemStack) -> ArmorBarRenderManager): ArmorBarBuilder {
        predicate = renderManager
        return this
    }

    /**
     * Registers the Custom Armor Bar so that it can be displayed.
     * @throws IllegalStateException Not all items have been initialized. check out [armor], [render].
     */
    fun register() {
        try {
            armor.forEach {
                CustomArmors.armorList[it] = CustomArmorBar(predicate)
                DetailArmorBar.LOGGER.log(Level.INFO, "Successfully registered '${Registry.ITEM.getId(it)}'!")
            }
        } catch (e: UninitializedPropertyAccessException) {
            throw IllegalStateException("Not all items have been initialized")
        }
    }
}

class ItemBarBuilder {
    private lateinit var item: Item
    private lateinit var predicate: (ItemStack) -> ItemBarRenderManager


    /**
     * Specifies the [Item] on which the armor bar will appear when equipped.
     * @throws IllegalStateException If [item] is not Wearable, an error occurs.
     */
    fun item(item: Item): ItemBarBuilder {
        if (item !is Wearable) throw IllegalStateException("It isn't Wearable Item")
        this.item = item
        return this
    }


    /**
     * Specifies the render options for the Armor Bar.
     * @See ItemBarRenderManager
     */
    fun render(renderManager: (ItemStack) -> ItemBarRenderManager): ItemBarBuilder {
        predicate = renderManager
        return this
    }

    /**
     * Registers the Custom Armor Bar so that it can be displayed.
     * @throws IllegalStateException Not all items have been initialized. check out [item], [render].
     * @throws IllegalStateException If [item] is not Wearable, an error occurs.
     */
    fun register() {
        try {
            if (item !is Wearable) throw IllegalStateException("It isn't Wearable Item")
            CustomArmors.itemList[item] = CustomArmorBar(predicate)
            DetailArmorBar.LOGGER.log(Level.INFO, "Successfully registered '${Registry.ITEM.getId(item)}'!")
        } catch (e: UninitializedPropertyAccessException) {
            throw IllegalStateException("Not all items have been initialized")
        }
    }
}