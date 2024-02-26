package com.redlimerl.detailab.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;

import java.awt.*;

@SuppressWarnings({"SuspiciousNameCombination", "SameParameterValue"})
public class InGameDrawer {

    public static void drawTexture(PoseStack matrices, int x, int y, int u, int v, Color color, boolean mirror) {
        drawTexture(matrices, x, y, u, v, 128, 128, color, mirror);
    }

    public static void drawTexture(PoseStack matrices, int x, int y, int u, int v, int width, int height, Color color, boolean mirror) {
        drawTexture(matrices, x, y, u, v, 9, 9, width, height, color, mirror);
    }

    public static void drawTexture(PoseStack matrices, int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight, Color color, boolean mirror) {
        RenderSystem.setShaderColor(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, color.getAlpha()/100f);
        drawTexture(matrices, x, y, width, height, (float) u, (float) v, width, height, textureWidth, textureHeight, mirror);
    }

    public static void drawTexture(PoseStack matrices, int x, int y, int width, int height, float u, float v, int regionWidth, int regionHeight, int textureWidth, int textureHeight, boolean mirror) {
        drawTexture(matrices, x, x + width, y, y + height, 0, regionWidth, regionHeight, u, v, textureWidth, textureHeight, mirror);
    }

    private static void drawTexture(PoseStack matrices, int x0, int y0, int x1, int y1, int z, int regionWidth, int regionHeight, float u, float v, int textureWidth, int textureHeight, boolean mirror) {
        drawTexturedQuad(matrices.last().pose(), x0, y0, x1, y1, z, (u + 0.0F) / (float)textureWidth, (u + (float)regionWidth) / (float)textureWidth, (v + 0.0F) / (float)textureHeight, (v + (float)regionHeight) / (float)textureHeight, mirror);
    }

    private static void drawTexturedQuad(Matrix4f matrices, int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1, boolean mirror) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        if (mirror) {
            bufferBuilder.vertex(matrices, (float)x0, (float)y1, (float)z).uv(u1, v1).endVertex();
            bufferBuilder.vertex(matrices, (float)x1, (float)y1, (float)z).uv(u0, v1).endVertex();
            bufferBuilder.vertex(matrices, (float)x1, (float)y0, (float)z).uv(u0, v0).endVertex();
            bufferBuilder.vertex(matrices, (float)x0, (float)y0, (float)z).uv(u1, v0).endVertex();
        } else {
            bufferBuilder.vertex(matrices, (float)x0, (float)y1, (float)z).uv(u0, v1).endVertex();
            bufferBuilder.vertex(matrices, (float)x1, (float)y1, (float)z).uv(u1, v1).endVertex();
            bufferBuilder.vertex(matrices, (float)x1, (float)y0, (float)z).uv(u1, v0).endVertex();
            bufferBuilder.vertex(matrices, (float)x0, (float)y0, (float)z).uv(u0, v0).endVertex();
        }
        BufferUploader.drawWithShader(bufferBuilder.end());
    }
}
