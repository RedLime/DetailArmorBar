@file:Suppress("SameParameterValue")

package com.redlimerl.detailab

import com.mojang.blaze3d.systems.RenderSystem
import com.redlimerl.detailab.DetailArmorBar.getConfig
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.hud.InGameHud
import net.minecraft.client.render.*
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.enchantment.*
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.*
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Matrix4f
import net.minecraft.util.registry.Registry
import org.apache.commons.lang3.mutable.MutableInt
import java.awt.Color
import kotlin.math.ceil
import kotlin.math.roundToInt

@Environment(EnvType.CLIENT)
class ArmorBarRenderer {

    data class LevelData(var total: Int, var count: Int)

    companion object {
        val INSTANCE = ArmorBarRenderer()
        val GUI_ARMOR_BAR = Identifier(DetailArmorBar.MOD_ID, "textures/armor_bar.png")
        var LAST_THORNS = 0L
        var LAST_MENDING = 0L

        private fun getAnimationSpeed(): Int {
            return (30*when(getConfig().options?.effectSpeed) {
                EffectSpeedType.VERY_FAST -> 0.5f
                EffectSpeedType.FAST -> 0.75f
                EffectSpeedType.SLOW -> 1.25f
                EffectSpeedType.VERY_SLOW -> 1.5f
                else -> 1.0f
            }).toInt()
        }

        private fun getProtectColor(g: Int, p: Int, e: Int, f: Int, a: Int): Color {
            val speed = getAnimationSpeed()
            val alpha = when (getConfig().options?.effectType) {
                ProtectionEffectType.AURA -> {
                    80
                }
                ProtectionEffectType.OUTLINE -> {
                    val time = DetailArmorBar.getTicks()
                    when {
                        time % (speed*4) < (speed*2) -> 0
                        time % (speed*2) < speed -> (MathHelper.lerp((time % speed) / (speed.toFloat()-1f), 0f, 0.65f)*255).roundToInt()
                        else -> (MathHelper.lerp((time % speed) / (speed.toFloat()-1f), 0.65f, 0f)*255).roundToInt()
                    }
                }
                else -> {
                    0
                }
            }

            return when {
                g > 0 -> Color(153, 255, 255, alpha)
                p > 0 -> Color(153, 153, 255, alpha)
                e > 0 -> Color(255, 255, 0, alpha)
                f > 0 -> Color(210, 56, 0, alpha)
                a > 0 -> Color(255, 255, 255, alpha)
                else -> Color.WHITE
            }
        }

        private fun getProtectColor(s: IntArray): Color {
            return getProtectColor(s[0], s[1], s[2], s[3], s[4])
        }

        private fun getLowDurabilityColor(): Color {
            val speed = getAnimationSpeed()
            val time = DetailArmorBar.getTicks()
            val alpha = when {
                time % (speed*4) >= (speed*2) -> 0
                time % (speed*2) < speed -> (MathHelper.lerp((time % speed) / (speed.toFloat()-1f), 0f, 0.65f)*255).roundToInt()
                else -> (MathHelper.lerp((time % speed) / (speed.toFloat()-1f), 0.65f, 0f)*255).roundToInt()
            }

            return Color(255, 25, 25, alpha)
        }

        private fun getThornColor(): Color {
            val time = DetailArmorBar.getTicks() - LAST_THORNS
            return when {
                getConfig().options?.effectThorn == AnimationType.STATIC -> {
                    Color.WHITE
                }
                time > 19 -> {
                    Color.WHITE
                }
                else -> {
                    val cc = (MathHelper.lerp((time % 20) / 19f, 0f, 1f)*255).roundToInt()
                    Color(255, cc, cc)
                }
            }
        }

        private fun getProtectLevel(equipment: Iterable<ItemStack>, type: ProtectionEnchantment.Type): LevelData {
            val mutableInt = MutableInt()
            var count = 0
            for (itemStack in equipment) {
                if (!itemStack.isEmpty) {
                    val nbtList = itemStack.enchantments
                    for (i in nbtList.indices) {
                        val nbtCompound = nbtList.getCompound(i)
                        Registry.ENCHANTMENT.getOrEmpty(EnchantmentHelper.getIdFromNbt(nbtCompound))
                            .ifPresent { enchantment: Enchantment? ->
                                if (enchantment is ProtectionEnchantment) {
                                    if (enchantment.protectionType == type) {
                                        mutableInt.add(EnchantmentHelper.getLevelFromNbt(nbtCompound))
                                        count++
                                    }
                                }
                            }
                    }
                }
            }
            return LevelData(mutableInt.toInt(), count)
        }

        private fun getThorns(equipment: Iterable<ItemStack>): LevelData {
            val mutableInt = MutableInt()
            var count = 0
            for (itemStack in equipment) {
                if (!itemStack.isEmpty) {
                    val nbtList = itemStack.enchantments
                    for (i in nbtList.indices) {
                        val nbtCompound = nbtList.getCompound(i)
                        Registry.ENCHANTMENT.getOrEmpty(EnchantmentHelper.getIdFromNbt(nbtCompound))
                            .ifPresent { enchantment: Enchantment? ->
                                if (enchantment is ThornsEnchantment) {
                                    val level = EnchantmentHelper.getLevelFromNbt(nbtCompound)
                                    mutableInt.add(if (level == 1) 1 else (level*2)-1)
                                    count++
                                }
                            }
                    }
                }
            }
            return LevelData(mutableInt.toInt(), count)
        }

        private fun getArmorMaterials(equipment: Iterable<ItemStack>): Map<Int, ArmorMaterial> {
            var armorPoints = 0
            val armorMaterial = hashMapOf<Int, ArmorMaterial>()

            for (itemStack in equipment) {
                if (!itemStack.isEmpty) {
                    if (itemStack.item is ArmorItem) {
                        val armor = itemStack.item as ArmorItem
                        repeat(armor.protection) {
                            armorMaterial[armorPoints + it] = armor.material
                        }
                        armorPoints += armor.protection
                    }
                }
            }
            return armorMaterial
        }

        private fun isElytra(equipment: Iterable<ItemStack>): Boolean {
            for (itemStack in equipment) {
                if (!itemStack.isEmpty) {
                    if (itemStack.item is ElytraItem) {
                        return true
                    }
                }
            }
            return false
        }

        private fun getLowDurability(equipment: Iterable<ItemStack>): Int {
            var count = 0
            for (itemStack in equipment) {
                if (!itemStack.isEmpty) {
                    if (itemStack.item is ArmorItem) {
                        if ((itemStack.damage.toFloat() / itemStack.maxDamage * 100f) > 92f) {
                            count += (itemStack.item as ArmorItem).protection
                        }
                    }
                }
            }
            return count
        }

        private fun getArmorLayerX(material: ArmorMaterial): Int {
            val ne = getConfig().options?.toggleNetherites == true
            val ae = getConfig().options?.toggleArmorTypes == true
            return if (ne && material == ArmorMaterials.NETHERITE) {
                0
            } else if (ae) {
                when(material) {
                    ArmorMaterials.CHAIN -> 72
                    ArmorMaterials.DIAMOND -> 18
                    ArmorMaterials.GOLD -> 90
                    ArmorMaterials.LEATHER -> 108
                    ArmorMaterials.TURTLE -> 36
                    else -> 54
                }
            } else {
                54
            }
        }
    }


    private val client: MinecraftClient = MinecraftClient.getInstance()
    private val hud: InGameHud = client.inGameHud


    fun render(matrices: MatrixStack, player: PlayerEntity) {
        client.profiler.swap("armor")

        val generic = getProtectLevel(player.armorItems, ProtectionEnchantment.Type.ALL)
        val projectile = getProtectLevel(player.armorItems, ProtectionEnchantment.Type.PROJECTILE)
        val explosive = getProtectLevel(player.armorItems, ProtectionEnchantment.Type.EXPLOSION)
        val fire = getProtectLevel(player.armorItems, ProtectionEnchantment.Type.FIRE)
        val protectArr = intArrayOf(generic.total + generic.count, projectile.total, explosive.total, fire.total, 0)
        val armorMaterials = getArmorMaterials(player.armorItems.reversed())
        val thorns = getThorns(player.armorItems)

        val playerHealth = MathHelper.ceil(player.health)
        val playerArmor = player.armor
        val totalEnchants = protectArr.sum()
        val maxHealth = player.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH).toFloat().coerceAtLeast(playerHealth.toFloat())
        val absorptionHealth = MathHelper.ceil(player.absorptionAmount)
        val resultHealth = MathHelper.ceil((maxHealth + absorptionHealth.toFloat()) / 20.0f)
        val screenWidth = client.window.scaledWidth / 2 - 91
        val screenHeight = client.window.scaledHeight - 39
        val yPos = screenHeight - (resultHealth - 1) * (10 - (resultHealth - 2)).coerceAtLeast(3) - 10


        RenderSystem.enableBlend()
        RenderSystem.setShader(GameRenderer::getPositionTexShader)
        RenderSystem.setShaderTexture(0, GUI_ARMOR_BAR)

        //Default
        for (count in 0..9) {
            if (playerArmor > 0) {
                val xPos = screenWidth + count * 8

                if (count * 2 + 1 < playerArmor) {
                    val am1 = armorMaterials.getOrDefault(count * 2, ArmorMaterials.IRON)
                    val am2 = armorMaterials.getOrDefault(count * 2 + 1, ArmorMaterials.IRON)
                    if (am1 == am2) {
                        drawTexture(matrices, xPos, yPos, getArmorLayerX(am1)+9, 9)
                    } else {
                        drawTexture(matrices, xPos, yPos, getArmorLayerX(am1), 9)
                        drawTexture(matrices, xPos, yPos, getArmorLayerX(am2), 9, true)
                    }
                }
                if (count * 2 + 1 == playerArmor) {
                    drawTexture(matrices, xPos, yPos, 45, 0)
                    val am = armorMaterials.getOrDefault(count * 2, ArmorMaterials.IRON)
                    drawTexture(matrices, xPos, yPos, getArmorLayerX(am), 9)
                }
                if (count * 2 + 1 > playerArmor) {
                    if (count == 9 && isElytra(player.armorItems))
                        drawTexture(matrices, xPos, yPos, 36, 0)
                    else
                        drawTexture(matrices, xPos, yPos, 45, 0)
                }
            }
        }


        //Armor Enchantments
        if (getConfig().options?.toggleEnchants == true) {
            for (count in 0..9) {
                if (totalEnchants == 0 || count * 2 + 1 > totalEnchants) break

                val xPos = screenWidth + count * 8
                if (count * 2 + 1 < totalEnchants) {
                    var min = -1
                    var max = -1
                    for (pw in 0..4) {
                        if (min == -1 && protectArr[pw] > 1) {
                            min = pw
                            break
                        } else if (min == -1 && protectArr[pw] == 1) {
                            min = pw
                        } else if (min != -1 && max == -1 && protectArr[pw] >= 1) max = pw
                    }
                    if (min != -1 && max != -1) {
                        drawEnchantTexture(matrices, xPos, yPos, getProtectColor(protectArr), 2)
                        protectArr[min] = 0
                        drawEnchantTexture(matrices, xPos, yPos, getProtectColor(protectArr), 1)
                        protectArr[max] -= 1
                    } else {
                        drawEnchantTexture(matrices, xPos, yPos, getProtectColor(protectArr))
                        protectArr[min] -= 2
                    }
                }
                if (count * 2 + 1 == totalEnchants) {
                    drawEnchantTexture(matrices, xPos, yPos, getProtectColor(protectArr))
                }
            }
        }

        //Thorns Check
        if (getConfig().options?.toggleThorns == true) {
            val thornsColor = getThornColor()
            for (count in 0..9) {
                if (thorns.total == 0 || count * 2 + 1 > thorns.total) break

                val xPos = screenWidth + count * 8
                if (count * 2 + 1 < thorns.total) {
                    drawTexture(matrices, xPos, yPos, 36, 18, thornsColor)
                }
                if (count * 2 + 1 == thorns.total) {
                    drawTexture(matrices, xPos, yPos, 27, 18, thornsColor)
                }
            }
        }

        //Durability Color
        if (getConfig().options?.toggleDurability == true) {
            var lowDur = getLowDurability(player.armorItems)
            val lowDurColor = getLowDurabilityColor()
            val halfArmors = ceil(playerArmor / 2.0).toInt() - 1
            if (playerArmor != 0 && lowDur != 0) {
                for (count in 0..halfArmors) {
                    if (lowDur == 0) break

                    val xPos = screenWidth + (halfArmors - count) * 8
                    if (playerArmor == (halfArmors - count) * 2 + 1) {
                        if (count == 0) {
                            drawTexture(matrices, xPos, yPos, 27, 0, lowDurColor)
                            lowDur--
                        }
                    } else {
                        if (lowDur == 1) {
                            drawTexture(matrices, xPos, yPos, 18, 0, lowDurColor)
                            lowDur = 0
                        } else {
                            drawTexture(matrices, xPos, yPos, 9, 0, lowDurColor)
                            lowDur -= 2
                        }
                    }
                }
            }

        }

        //Mending Color
        if (getConfig().options?.toggleMending == true) {
            val mendingTime = DetailArmorBar.getTicks() - LAST_MENDING
            val mendingSpeed = 3
            for (count in 0..9) {
                if (playerArmor == 0 || mendingTime >= (mendingSpeed * 4)) break

                if (mendingTime % (mendingSpeed * 2) < mendingSpeed) {
                    val xPos = screenWidth + count * 8
                    drawTexture(matrices, xPos, yPos, 9, 0)
                }
            }
        }


        RenderSystem.disableBlend()
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        RenderSystem.setShaderTexture(0, DrawableHelper.GUI_ICONS_TEXTURE)
    }

    private fun drawEnchantTexture(matrices: MatrixStack, x: Int, y: Int, color: Color, half: Int = 0) {
        var u = 0
        var v = 0
        val t = (hud.ticks/3) % 36

        if (getConfig().options?.effectType == ProtectionEffectType.AURA) {
            if (t < 12) {
                u = (t % 12) * 9
                v = 27 + (half * 9)
            }
        } else if (getConfig().options?.effectType == ProtectionEffectType.OUTLINE) {
            u = 9 + (half * 9)
        } else return

        drawTexture(matrices, x, y, u, v, color)
    }

    private fun drawTexture(matrices: MatrixStack, x: Int, y: Int, u: Int, v: Int, color: Color) {
        RenderSystem.setShaderColor(color.red/255f, color.green/255f, color.blue/255f, color.alpha/100f)
        drawTexture(matrices, x, y, u.toFloat(), v.toFloat(), 9, 9, 128, 128, false)
    }

    private fun drawTexture(matrices: MatrixStack, x: Int, y: Int, u: Int, v: Int, mirror: Boolean = false) {
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