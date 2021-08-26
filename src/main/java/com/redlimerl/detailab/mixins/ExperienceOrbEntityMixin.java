//package com.redlimerl.detailab.mixins;
//
//import com.redlimerl.detailab.DetailArmorBar;
//import com.redlimerl.detailab.render.ArmorBarRenderer;
//import net.minecraft.world.entity.EquipmentSlot;
//import net.minecraft.world.item.ItemStack;
//
//import java.util.Map;
//
//@Mixin(value = ExperienceOrbEntity.class)
//public class ExperienceOrbEntityMixin {
//
//    @Redirect(method = "repairPlayerGears(Lnet/minecraft/entity/player/PlayerEntity;I)I",
//            at = @At(value = "INVOKE", target = "Ljava/util/Map$Entry;getValue()Ljava/lang/Object;"))
//    private Object repairPlayerGears(Map.Entry<EquipmentSlot, ItemStack> entry) {
//        ItemStack itemStack = entry.getValue();
//        EquipmentSlot key = entry.getKey();
//        if (key.getType() == EquipmentSlot.Type.ARMOR)
//            ArmorBarRenderer.LAST_MENDING = DetailArmorBar.getTicks();
//        return itemStack;
//    }
//}
