package com.redlimerl.detailab.mixins;

import com.redlimerl.detailab.DetailArmorBar;
import com.redlimerl.detailab.render.ArmorBarRenderer;
import net.minecraft.enchantment.ThornsEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ThornsEnchantment.class)
public class ThornsEnchantmentMixin {

    @Redirect(method = "onUserDamaged(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/Entity;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private boolean attackerDamage(Entity attacker, DamageSource source, float damage) {
        if (damage > 0.2f) ArmorBarRenderer.Companion.setLAST_THORNS(DetailArmorBar.INSTANCE.getTicks());
        return attacker.damage(source, damage);
    }
}
