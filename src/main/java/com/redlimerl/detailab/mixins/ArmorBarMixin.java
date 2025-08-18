package com.redlimerl.detailab.mixins;
import com.mojang.blaze3d.vertex.PoseStack;
import com.redlimerl.detailab.render.ArmorBarRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(Gui.class)
public abstract class ArmorBarMixin {
    @Shadow @Final protected Minecraft minecraft;
    @Shadow protected abstract Player getCameraPlayer();
    @Redirect(
            method = "renderPlayerHealth",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getArmorValue()I"
            )
    )
    private int disableVanillaArmorBar(Player player) {
        return 0;
    }
    @Inject(
            method = "renderPlayerHealth",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getArmorValue()I")
    )
    private void renderCustomArmorBar(GuiGraphics guiGraphics, CallbackInfo ci) {
        if (this.minecraft.screen instanceof ReceivingLevelScreen) return;
        Player player = this.getCameraPlayer();
        if (player == null || player.isRemoved()) return;
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        try {
            ArmorBarRenderer.INSTANCE.render(poseStack, player);
        } finally {
            poseStack.popPose();
        }
    }
}