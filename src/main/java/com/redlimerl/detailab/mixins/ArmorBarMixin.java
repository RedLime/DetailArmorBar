package com.redlimerl.detailab.mixins;

import com.redlimerl.detailab.render.ArmorBarRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class ArmorBarMixin {

    @Shadow
    private native PlayerEntity getCameraPlayer();

    @Inject(method = "renderStatusBars",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V"))
    private void renderArmorOverlay(DrawContext context, CallbackInfo ci) {
        ArmorBarRenderer.INSTANCE.render(context, getCameraPlayer());
    }

    @Redirect(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getArmor()I"))
    private int injected(PlayerEntity playerEntity) {
        return 0;
    }
}
