package com.redlimerl.detailab;

import com.redlimerl.detailab.render.ArmorBarRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import java.util.Map;

@Mod.EventBusSubscriber(modid = DetailArmorBar.MOD_ID, bus = Bus.FORGE, value = Dist.CLIENT)
public class PlayerEvents {

    @SubscribeEvent
    public static void onArmorRender(RenderGameOverlayEvent.PreLayer event) {
        if (event.getOverlay() == ForgeIngameGui.ARMOR_LEVEL_ELEMENT) {
            event.setCanceled(true);
            var instance = Minecraft.getInstance();
            if (instance.player != null && instance.gameMode != null && instance.gameMode.getPlayerMode().isSurvival()) {
                ArmorBarRenderer.INSTANCE.render(event.getMatrixStack(), instance.player);
            }
        }
    }

    @SubscribeEvent
    public static void onExpPick(PlayerXpEvent.PickupXp event) {
        Map.Entry<EquipmentSlot, ItemStack> entry = EnchantmentHelper.getRandomItemWith(Enchantments.MENDING, event.getPlayer(), ItemStack::isDamaged);
        if (entry != null) ArmorBarRenderer.LAST_MENDING = DetailArmorBar.getTicks();
    }
}
