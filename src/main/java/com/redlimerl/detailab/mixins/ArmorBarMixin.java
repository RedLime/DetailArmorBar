//package com.redlimerl.detailab.mixins;
//
//import com.redlimerl.detailab.render.ArmorBarRenderer;
//import net.minecraft.world.entity.player.Player;
//
//@Mixin(InGameHud.class)
//public class ArmorBarMixin extends DrawableHelper {
//
//    @Shadow
//    private native Player getCameraPlayer();
//
//    @Inject(method = "renderStatusBars(Lnet/minecraft/client/util/math/MatrixStack;)V",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V"))
//    private void renderArmorOverlay(MatrixStack matrices, CallbackInfo info) {
//        ArmorBarRenderer.INSTANCE.render(matrices, getCameraPlayer());
//    }
//
//    @Redirect(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getArmor()I"))
//    private int injected(Player playerEntity) {
//        return 0;
//    }
//}
