package com.redlimerl.detailab.mixins;

import com.redlimerl.detailab.DetailArmorBar;
import com.redlimerl.detailab.render.ArmorBarRenderer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

@Mixin(value = ExperienceOrbEntity.class)
public class ExperienceOrbEntityMixin {

    @Redirect(method = "repairPlayerGears(Lnet/minecraft/entity/player/PlayerEntity;I)I",
            at = @At(value = "INVOKE", target = "Ljava/util/Map$Entry;getValue()Ljava/lang/Object;"))
    private Object repairPlayerGears(Map.Entry<EquipmentSlot, ItemStack> entry) {
        ItemStack itemStack = entry.getValue();
        EquipmentSlot key = entry.getKey();
        if (key.getType() == EquipmentSlot.Type.ARMOR)
            ArmorBarRenderer.Companion.setLAST_MENDING(DetailArmorBar.INSTANCE.getTicks());
        return itemStack;
    }
}
