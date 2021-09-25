package com.redlimerl.detailab.events;

import com.redlimerl.detailab.DetailArmorBar;
import com.redlimerl.detailab.render.ArmorBarRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import java.util.Map;

@Mod.EventBusSubscriber(modid = DetailArmorBar.MOD_ID, bus = Bus.FORGE, value = Dist.CLIENT)
public class PlayerEvents {

    @SubscribeEvent
    public static void onArmorRender(RenderGameOverlayEvent event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ARMOR) {
            event.setCanceled(true);
            Minecraft instance = Minecraft.getInstance();
            if (instance.player != null && instance.gameMode != null && instance.gameMode.getPlayerMode().isSurvival()) {
                ArmorBarRenderer.INSTANCE.render(event.getMatrixStack(), instance.player);
            }
        }
    }

    @SubscribeEvent
    public static void onExpPick(PlayerXpEvent.PickupXp event) {
        Map.Entry<EquipmentSlotType, ItemStack> entry = EnchantmentHelper.getRandomItemWith(Enchantments.MENDING, event.getPlayer(), ItemStack::isDamaged);
        if (entry != null) ArmorBarRenderer.LAST_MENDING = DetailArmorBar.getTicks();
    }
}
