package com.redlimerl.detailab.item

import com.mojang.blaze3d.systems.RenderSystem
import com.redlimerl.detailab.DetailArmorBar
import com.redlimerl.detailab.DetailArmorBar.GUI_ARMOR_BAR
import com.redlimerl.detailab.api.ArmorBarRenderManager
import com.redlimerl.detailab.api.BarRenderManager
import com.redlimerl.detailab.api.TextureOffset
import com.redlimerl.detailab.render.InGameDrawer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import java.awt.Color

data class CustomArmorBar(val predicate: (ItemStack) -> BarRenderManager) {
    companion object {
        var prevTexture: Identifier? = null
        val DEFAULT = CustomArmorBar {
            return@CustomArmorBar ArmorBarRenderManager(GUI_ARMOR_BAR, 128, 128,
                TextureOffset(63, 9), TextureOffset(54, 9), TextureOffset(27, 0), TextureOffset(9, 0))
        }
        val EMPTY = CustomArmorBar {
            if (DetailArmorBar.getConfig().options?.toggleEmptyBar == true) {
                return@CustomArmorBar ArmorBarRenderManager(GUI_ARMOR_BAR, 128, 128,
                    TextureOffset(45, 0), TextureOffset(45, 0), TextureOffset(9, 0), TextureOffset(27, 0))
            } else {
                return@CustomArmorBar ArmorBarRenderManager(
                    GUI_ARMOR_BAR, 128, 128,
                    TextureOffset(0, 0), TextureOffset(0, 0), TextureOffset(0, 0), TextureOffset(0, 0)
                )
            }
        }
    }

    fun draw(itemStack: ItemStack, matrices: MatrixStack, xPos: Int, yPos: Int, isHalf: Boolean, isMirror: Boolean) {
        val renderInfo = predicate(itemStack)
        if (!renderInfo.isShown) return

        RenderSystem.setShaderTexture(0, renderInfo.texture)
        if (prevTexture != renderInfo.texture || prevTexture == null) {
            prevTexture = renderInfo.texture
            RenderSystem.setShaderTexture(0, renderInfo.texture)
        }

        if (isHalf) {
            InGameDrawer.drawTexture(matrices, xPos, yPos, renderInfo.textureOffsetHalf.x, renderInfo.textureOffsetHalf.y, renderInfo.color, isMirror)
        } else {
            InGameDrawer.drawTexture(matrices, xPos, yPos, renderInfo.textureOffsetFull.x, renderInfo.textureOffsetFull.y, renderInfo.color)
        }
    }

    fun drawOutLine(itemStack: ItemStack, matrices: MatrixStack, xPos: Int, yPos: Int, isHalf: Boolean, isMirror: Boolean, color: Color) {
        val renderInfo = predicate(itemStack)
        if (!renderInfo.isShown) return

        RenderSystem.setShaderTexture(0, renderInfo.texture)
        if (prevTexture != renderInfo.texture || prevTexture == null) {
            prevTexture = renderInfo.texture
            RenderSystem.setShaderTexture(0, renderInfo.texture)
        }

        if (isHalf) {
            InGameDrawer.drawTexture(matrices, xPos, yPos, renderInfo.textureOffsetOutlineHalf.x, renderInfo.textureOffsetOutlineHalf.y, color, isMirror)
        } else {
            InGameDrawer.drawTexture(matrices, xPos, yPos, renderInfo.textureOffsetOutline.x, renderInfo.textureOffsetOutline.y, color)
        }
    }
}