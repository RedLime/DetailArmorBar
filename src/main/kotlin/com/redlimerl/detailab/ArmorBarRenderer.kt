@file:Suppress("SameParameterValue")

package com.redlimerl.detailab

import com.mojang.blaze3d.systems.RenderSystem
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.hud.InGameHud
import net.minecraft.client.render.*
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.ProtectionEnchantment
import net.minecraft.enchantment.ThornsEnchantment
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterials
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import net.minecraft.util.registry.Registry
import org.apache.commons.lang3.mutable.MutableInt
import java.awt.Color
import kotlin.math.roundToInt

@Environment(EnvType.CLIENT)
class ArmorBarRenderer {

    data class LevelData(val total: Int, val count: Int)

    companion object {
        val INSTANCE = ArmorBarRenderer()
        val GUI_ARMOR_BAR = Identifier(DetailArmorBars.MOD_ID, "textures/armor_bar.png")

        private fun getProtectColor(g: Int, p: Int, e: Int, f: Int, a: Int): Color {
            val alpha = when (DetailArmorBars.getConfig().options?.effectType) {
                ProtectionEffectType.AURA -> {
                    80
                }
                ProtectionEffectType.OUTLINE -> {
                    val time = System.currentTimeMillis()/50
                    when {
                        time % 120 < 60 -> 0
                        time % 60 < 30 -> (MathHelper.lerp((time % 30) / 29f, 0f, 0.65f)*255).roundToInt()
                        else -> (MathHelper.lerp((time % 30) / 29f, 0.65f, 0f)*255).roundToInt()
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
    }


    private val client: MinecraftClient = MinecraftClient.getInstance()
    private val hud: InGameHud = client.inGameHud


    fun render(matrices: MatrixStack, player: PlayerEntity) {
        val generic = getProtectLevel(player.armorItems, ProtectionEnchantment.Type.ALL)
        val projectile = getProtectLevel(player.armorItems, ProtectionEnchantment.Type.PROJECTILE)
        val explosive = getProtectLevel(player.armorItems, ProtectionEnchantment.Type.EXPLOSION)
        val fire = getProtectLevel(player.armorItems, ProtectionEnchantment.Type.FIRE)
        //val protectAll = generic.count
        val protectArr = intArrayOf(generic.total + generic.count, projectile.total, explosive.total, fire.total, 0)
        val netherites = getNetherites(player.armorItems)
        val thorns = getThorns(player.armorItems)

        val playerHealth = MathHelper.ceil(player.health)
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
        for (count in 0..9) {
            if (netherites.total > 0) {
                val xPos = screenWidth + count * 8
                if (count * 2 + 1 < netherites.total) {
                    ArmorBarRenderer().drawTexture(matrices, xPos, yPos, 9, 9)
                }
                if (count * 2 + 1 == netherites.total) {
                    ArmorBarRenderer().drawTexture(matrices, xPos, yPos, 0, 9)
                }
                if (count * 2 + 1 > netherites.total) break
            }
        }

        //Armor Enchantments
        for (count in 0..9) {
            if (totalEnchants > 0) {
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
                        ArmorBarRenderer().drawEnchantTexture(matrices, xPos, yPos, getProtectColor(protectArr), 1)
                        protectArr[min] = 0
                        ArmorBarRenderer().drawEnchantTexture(matrices, xPos, yPos, getProtectColor(protectArr), 2)
                        protectArr[max] -= 1
                    } else {
                        ArmorBarRenderer().drawEnchantTexture(matrices, xPos, yPos, getProtectColor(protectArr))
                        protectArr[min] -= 2
                    }
                }
                if (count * 2 + 1 == totalEnchants) {
                    ArmorBarRenderer().drawEnchantTexture(matrices, xPos, yPos, getProtectColor(protectArr))
                }
                if (count * 2 + 1 > totalEnchants) break

            }
        }

        //Thorns Check
        val thornsColor = if (player.recentDamageSource?.attacker == null) Color.WHITE else Color.RED
        for (count in 0..9) {
            if (thorns.total > 0) {
                val xPos = screenWidth + count * 8
                if (count * 2 + 1 < thorns.total) {
                    when {
                        count * 2 + 1 == netherites.total -> ArmorBarRenderer().drawTexture(matrices, xPos, yPos, 18, 18, thornsColor)
                        count * 2 + 1 < netherites.total -> ArmorBarRenderer().drawTexture(matrices, xPos, yPos, 36, 18, thornsColor)
                        else -> ArmorBarRenderer().drawTexture(matrices, xPos, yPos, 9, 18, thornsColor)
                    }
                }
                if (count * 2 + 1 == thorns.total) {
                    if (count * 2 + 1 <= netherites.total)
                        ArmorBarRenderer().drawTexture(matrices, xPos, yPos, 27, 18, thornsColor)
                    else
                        ArmorBarRenderer().drawTexture(matrices, xPos, yPos, 0, 18, thornsColor)
                }
                if (count * 2 + 1 > thorns.total) break
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

        if (DetailArmorBars.getConfig().options?.effectType == ProtectionEffectType.AURA) {
            if (t < 12) {
                u = (t % 12) * 9
                v = 27 + (half * 9)
            }
        } else if (DetailArmorBars.getConfig().options?.effectType == ProtectionEffectType.OUTLINE) {
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