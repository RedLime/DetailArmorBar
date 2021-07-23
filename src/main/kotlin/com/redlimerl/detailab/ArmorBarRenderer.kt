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
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterials
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import net.minecraft.util.registry.Registry
import org.apache.commons.lang3.mutable.MutableInt
import java.awt.Color
import kotlin.math.ceil
import kotlin.math.roundToInt

@Environment(EnvType.CLIENT)
class ArmorBarRenderer {

    data class LevelData(val total: Int, val count: Int)

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

        private fun getNetherites(equipment: Iterable<ItemStack>): LevelData {
            val mutableInt = MutableInt()
            var count = 1
            for (itemStack in equipment) {
                if (!itemStack.isEmpty) {
                    if (itemStack.item is ArmorItem) {
                        val armor = itemStack.item as ArmorItem
                        if (armor.material == ArmorMaterials.NETHERITE) {
                            mutableInt.add(armor.protection)
                            count++
                        }
                    }
                }
            }
            return LevelData(mutableInt.toInt(), count)
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
    }


    private val client: MinecraftClient = MinecraftClient.getInstance()
    private val hud: InGameHud = client.inGameHud


    fun render(matrices: MatrixStack, player: PlayerEntity) {
        client.profiler.swap("armor")

        //println(LAST_MENDING)

        val generic = getProtectLevel(player.armorItems, ProtectionEnchantment.Type.ALL)
        val projectile = getProtectLevel(player.armorItems, ProtectionEnchantment.Type.PROJECTILE)
        val explosive = getProtectLevel(player.armorItems, ProtectionEnchantment.Type.EXPLOSION)
        val fire = getProtectLevel(player.armorItems, ProtectionEnchantment.Type.FIRE)
        //val protectAll = generic.count
        val protectArr = intArrayOf(generic.total + generic.count, projectile.total, explosive.total, fire.total, 0)
        val netherites = getNetherites(player.armorItems)
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


        client.textureManager.bindTexture(GUI_ARMOR_BAR)
        RenderSystem.enableBlend()
        RenderSystem.setShader(GameRenderer::getPositionTexShader)
        RenderSystem.setShaderTexture(0, GUI_ARMOR_BAR)
        //hud.drawTexture(matrices, 0, 0, 0, 0, 256, 256)


        //Netherites Check
        if (getConfig().options?.toggleNetherites == true) {
            for (count in 0..9) {
                if (netherites.total == 0 || count * 2 + 1 > netherites.total) break

                val xPos = screenWidth + count * 8
                if (count * 2 + 1 < netherites.total) {
                    drawTexture(matrices, xPos, yPos, 9, 9)
                }
                if (count * 2 + 1 == netherites.total) {
                    drawTexture(matrices, xPos, yPos, 0, 9)
                }
                if (count * 2 + 1 > netherites.total) break
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
                        drawEnchantTexture(matrices, xPos, yPos, getProtectColor(protectArr), 1)
                        protectArr[min] = 0
                        drawEnchantTexture(matrices, xPos, yPos, getProtectColor(protectArr), 2)
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
                    when {
                        count * 2 + 1 == netherites.total -> drawTexture(matrices, xPos, yPos, 18, 18, thornsColor)
                        count * 2 + 1 < netherites.total -> drawTexture(matrices, xPos, yPos, 36, 18, thornsColor)
                        else -> drawTexture(matrices, xPos, yPos, 9, 18, thornsColor)
                    }
                }
                if (count * 2 + 1 == thorns.total) {
                    if (count * 2 + 1 <= netherites.total)
                        drawTexture(matrices, xPos, yPos, 27, 18, thornsColor)
                    else
                        drawTexture(matrices, xPos, yPos, 0, 18, thornsColor)
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
                            drawTexture(matrices, xPos, yPos, 18, 0, lowDurColor)
                            lowDur--
                        }
                    } else {
                        if (lowDur == 1) {
                            drawTexture(matrices, xPos, yPos, 27, 0, lowDurColor)
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
        client.textureManager.bindTexture(DrawableHelper.GUI_ICONS_TEXTURE)
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
        InGameHud.drawTexture(matrices, x, y, u.toFloat(), v.toFloat(), 9, 9, 128, 128)
    }

    private fun drawTexture(matrices: MatrixStack, x: Int, y: Int, u: Int, v: Int) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        InGameHud.drawTexture(matrices, x, y, u.toFloat(), v.toFloat(), 9, 9, 128, 128)
    }
}