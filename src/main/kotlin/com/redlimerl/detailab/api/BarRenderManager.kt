package com.redlimerl.detailab.api

import net.minecraft.util.Identifier
import java.awt.Color


class TextureOffset(val x: Int, val y: Int)

interface BarRenderManager {
    val texture: Identifier
    val textureWidth: Int
    val textureHeight: Int
    val textureOffsetFull: TextureOffset
    val textureOffsetHalf: TextureOffset
    val textureOffsetOutline: TextureOffset
    val textureOffsetOutlineHalf: TextureOffset
    val isShown: Boolean
    val color: Color
}

/**
 * Render options for Armor Bar.
 * @param texture Texture Identifier where the Armor Bar
 * @param textureWidth Texture's width
 * @param textureHeight Texture's height
 * @param textureOffsetFull Full Armor Bar texture position
 * @param textureOffsetHalf Half Armor Bar texture position
 * @param textureOffsetOutline Full Armor Bar Outline texture position
 * @param textureOffsetOutlineHalf Half Armor Bar Outline texture position
 * @param color If you want, change the color of the texture. But outline is not change. Default is [Color.WHITE]
 * @see TextureOffset
 */
class ArmorBarRenderManager(
    override val texture: Identifier, override val textureWidth: Int, override val textureHeight: Int,
    override val textureOffsetFull: TextureOffset, override val textureOffsetHalf: TextureOffset,
    override val textureOffsetOutline: TextureOffset, override val textureOffsetOutlineHalf: TextureOffset, override val color: Color = Color.WHITE
) : BarRenderManager {

    override val isShown: Boolean
        get() = true

}


/**
 * Render options for Wearable Item Armor Bar.
 * @param texture Texture Identifier where the Armor Bar
 * @param textureWidth Texture's width
 * @param textureHeight Texture's height
 * @param textureOffsetFull Full Armor Bar texture position
 * @param textureOffsetOutline Full Armor Bar Outline texture position
 * @param isShown Whether the Item Bar is visible.
 * @param color If you want, change the color of the texture. But outline is not change. Default is [Color.WHITE]
 * @see TextureOffset
 */
class ItemBarRenderManager(
    override val texture: Identifier, override val textureWidth: Int, override val textureHeight: Int,
    override val textureOffsetFull: TextureOffset, override val textureOffsetOutline: TextureOffset,
    override val isShown: Boolean, override val color: Color = Color.WHITE
) : BarRenderManager {

    override val textureOffsetHalf: TextureOffset
        get() = textureOffsetFull
    override val textureOffsetOutlineHalf: TextureOffset
        get() = textureOffsetOutline

}