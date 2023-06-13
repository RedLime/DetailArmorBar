package com.redlimerl.detailab.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import org.joml.Matrix4f;

import java.awt.*;

@SuppressWarnings({"SuspiciousNameCombination", "SameParameterValue"})
public class InGameDrawer {

    public static void drawTexture(DrawContext context, int x, int y, int u, int v, Color color, boolean mirror) {
        drawTexture(context, x, y, u, v, 128, 128, color, mirror);
    }

    public static void drawTexture(DrawContext context, int x, int y, int u, int v, int width, int height, Color color, boolean mirror) {
        drawTexture(context, x, y, (float) u, (float) v, 9, 9, width, height, color, mirror);
    }

    public static void drawTexture(DrawContext context, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, Color color, boolean mirror) {
        RenderSystem.setShaderColor(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, color.getAlpha()/100f);
        drawTexture(context, x, y, width, height, u, v, width, height, textureWidth, textureHeight, mirror);
    }

    public static void drawTexture(DrawContext context, int x, int y, int width, int height, float u, float v, int regionWidth, int regionHeight, int textureWidth, int textureHeight, boolean mirror) {
        drawTexture(context, x, x + width, y, y + height, 0, regionWidth, regionHeight, u, v, textureWidth, textureHeight, mirror);
    }

    private static void drawTexture(DrawContext context, int x0, int y0, int x1, int y1, int z, int regionWidth, int regionHeight, float u, float v, int textureWidth, int textureHeight, boolean mirror) {
        drawTexturedQuad(context.getMatrices().peek().getPositionMatrix(), x0, y0, x1, y1, z, (u + 0.0F) / (float)textureWidth, (u + (float)regionWidth) / (float)textureWidth, (v + 0.0F) / (float)textureHeight, (v + (float)regionHeight) / (float)textureHeight, mirror);
    }

    private static void drawTexturedQuad(Matrix4f matrices, int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1, boolean mirror) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);

        if (mirror) {
            bufferBuilder.vertex(matrices, (float)x0, (float)y1, (float)z).texture(u1, v1).next();
            bufferBuilder.vertex(matrices, (float)x1, (float)y1, (float)z).texture(u0, v1).next();
            bufferBuilder.vertex(matrices, (float)x1, (float)y0, (float)z).texture(u0, v0).next();
            bufferBuilder.vertex(matrices, (float)x0, (float)y0, (float)z).texture(u1, v0).next();
        } else {
            bufferBuilder.vertex(matrices, (float)x0, (float)y1, (float)z).texture(u0, v1).next();
            bufferBuilder.vertex(matrices, (float)x1, (float)y1, (float)z).texture(u1, v1).next();
            bufferBuilder.vertex(matrices, (float)x1, (float)y0, (float)z).texture(u1, v0).next();
            bufferBuilder.vertex(matrices, (float)x0, (float)y0, (float)z).texture(u0, v0).next();
        }
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }
}
