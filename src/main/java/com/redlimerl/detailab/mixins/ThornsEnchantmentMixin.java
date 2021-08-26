//package com.redlimerl.detailab.mixins;
//
//import com.redlimerl.detailab.DetailArmorBar;
//import com.redlimerl.detailab.render.ArmorBarRenderer;
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.item.enchantment.ThornsEnchantment;
//
//@Mixin(ThornsEnchantment.class)
//public class ThornsEnchantmentMixin {
//
//    @Redirect(method = "onUserDamaged(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/Entity;I)V",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
//    private boolean attackerDamage(Entity attacker, DamageSource source, float damage) {
//        if (damage > 0.2f) ArmorBarRenderer.LAST_THORNS = DetailArmorBar.getTicks();
//        return attacker.hurt(source, damage);
//    }
//}
