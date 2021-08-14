package com.redlimerl.detailab.render

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.render.*
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Matrix4f
import java.awt.Color

object InGameDrawer {
    fun drawTexture(matrices: MatrixStack, x: Int, y: Int, u: Int, v: Int, width: Int, height: Int, color: Color = Color.WHITE, mirror: Boolean = false) {
        RenderSystem.setShaderColor(color.red/255f, color.green/255f, color.blue/255f, color.alpha/100f)
        drawTexture(matrices, x, y, u.toFloat(), v.toFloat(), 9, 9, width, height, mirror)
    }

    fun drawTexture(matrices: MatrixStack, x: Int, y: Int, u: Int, v: Int, color: Color, mirror: Boolean = false) {
        RenderSystem.setShaderColor(color.red/255f, color.green/255f, color.blue/255f, color.alpha/100f)
        drawTexture(matrices, x, y, u.toFloat(), v.toFloat(), 9, 9, 128, 128, mirror)
    }

    fun drawTexture(matrices: MatrixStack, x: Int, y: Int, u: Int, v: Int, mirror: Boolean = false) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        drawTexture(matrices, x, y, u.toFloat(), v.toFloat(), 9, 9, 128, 128, mirror)
    }

    private fun drawTexture(matrices: MatrixStack, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, textureWidth: Int, textureHeight: Int, mirror: Boolean) {
        drawTexture(matrices, x, y, width, height, u, v, width, height, textureWidth, textureHeight, mirror)
    }

    private fun drawTexture(matrices: MatrixStack, x: Int, y: Int, width: Int, height: Int, u: Float, v: Float, regionWidth: Int, regionHeight: Int, textureWidth: Int, textureHeight: Int, mirror: Boolean) {
        drawTexture(matrices, x, x + width, y, y + height, 0, regionWidth, regionHeight, u, v, textureWidth, textureHeight, mirror)
    }

    private fun drawTexture(matrices: MatrixStack, x0: Int, y0: Int, x1: Int, y1: Int, z: Int, regionWidth: Int, regionHeight: Int, u: Float, v: Float, textureWidth: Int, textureHeight: Int, mirror: Boolean) {
        drawTexturedQuad(matrices.peek().model, x0, y0, x1, y1, z,
            (u + 0.0f) / textureWidth.toFloat(),
            (u + regionWidth.toFloat()) / textureWidth.toFloat(),
            (v + 0.0f) / textureHeight.toFloat(),
            (v + regionHeight.toFloat()) / textureHeight.toFloat(),
            mirror
        )
    }

    private fun drawTexturedQuad(matrices: Matrix4f, x0: Int, x1: Int, y0: Int, y1: Int, z: Int, u0: Float, u1: Float, v0: Float, v1: Float, mirror: Boolean) {
        RenderSystem.setShader { GameRenderer.getPositionTexShader() }
        val bufferBuilder = Tessellator.getInstance().buffer
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE)
        if (mirror) {
            bufferBuilder.vertex(matrices, x0.toFloat(), y1.toFloat(), z.toFloat()).texture(u1, v1).next()
            bufferBuilder.vertex(matrices, x1.toFloat(), y1.toFloat(), z.toFloat()).texture(u0, v1).next()
            bufferBuilder.vertex(matrices, x1.toFloat(), y0.toFloat(), z.toFloat()).texture(u0, v0).next()
            bufferBuilder.vertex(matrices, x0.toFloat(), y0.toFloat(), z.toFloat()).texture(u1, v0).next()
        } else {
            bufferBuilder.vertex(matrices, x0.toFloat(), y1.toFloat(), z.toFloat()).texture(u0, v1).next()
            bufferBuilder.vertex(matrices, x1.toFloat(), y1.toFloat(), z.toFloat()).texture(u1, v1).next()
            bufferBuilder.vertex(matrices, x1.toFloat(), y0.toFloat(), z.toFloat()).texture(u1, v0).next()
            bufferBuilder.vertex(matrices, x0.toFloat(), y0.toFloat(), z.toFloat()).texture(u0, v0).next()
        }
        bufferBuilder.end()
        BufferRenderer.draw(bufferBuilder)
    }
}