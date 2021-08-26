package com.redlimerl.detailab;

import com.redlimerl.detailab.render.ArmorBarRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.logging.log4j.Level;

import java.util.Map;

@Mod.EventBusSubscriber(modid = DetailArmorBar.MOD_ID, bus = Bus.FORGE, value = Dist.CLIENT)
public class PlayerEvents {

    @SubscribeEvent
    public static void onArmorRender(RenderGameOverlayEvent.PreLayer event) {
        if (event.getOverlay() == ForgeIngameGui.ARMOR_LEVEL_ELEMENT) {
            event.setCanceled(true);
            if (Minecraft.getInstance().player != null) {
                ArmorBarRenderer.INSTANCE.render(event.getMatrixStack(), Minecraft.getInstance().player);
            }
        }
    }

    @SubscribeEvent
    public static void onExpPick(PlayerXpEvent.PickupXp event) {
        Map.Entry<EquipmentSlot, ItemStack> entry = EnchantmentHelper.getRandomItemWith(Enchantments.MENDING, event.getPlayer(), ItemStack::isDamaged);
        if (entry != null) ArmorBarRenderer.LAST_MENDING = DetailArmorBar.getTicks();
    }
}
