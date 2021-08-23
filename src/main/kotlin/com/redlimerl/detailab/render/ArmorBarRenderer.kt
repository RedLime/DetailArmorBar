@file:Suppress("SameParameterValue")

package com.redlimerl.detailab.render

import com.mojang.blaze3d.systems.RenderSystem
import com.redlimerl.detailab.AnimationType
import com.redlimerl.detailab.DetailArmorBar
import com.redlimerl.detailab.DetailArmorBar.GUI_ARMOR_BAR
import com.redlimerl.detailab.DetailArmorBar.getConfig
import com.redlimerl.detailab.EffectSpeedType
import com.redlimerl.detailab.ProtectionEffectType
import com.redlimerl.detailab.item.CustomArmorBar
import com.redlimerl.detailab.item.CustomArmors
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.hud.InGameHud
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.ProtectionEnchantment
import net.minecraft.enchantment.ThornsEnchantment
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterials
import net.minecraft.item.ItemStack
import net.minecraft.util.math.MathHelper
import net.minecraft.util.registry.Registry
import org.apache.commons.lang3.mutable.MutableInt
import java.awt.Color
import java.lang.Integer.min
import kotlin.math.ceil
import kotlin.math.roundToInt

@Environment(EnvType.CLIENT)
class ArmorBarRenderer {

    data class LevelData(var total: Int, var count: Int)

    companion object {
        val INSTANCE = ArmorBarRenderer()
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
                p > 0 -> Color(112, 51, 173, alpha)
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

        private fun getLowDurabilityItem(equipment: Iterable<ItemStack>): Int {
            var count = 0
            for (itemStack in equipment) {
                if (!itemStack.isEmpty) {
                    if (itemStack.maxDamage != 0 && (itemStack.damage.toFloat() / itemStack.maxDamage * 100f) > 92f) {
                        count += (itemStack.item as? ArmorItem)?.protection ?: 2
                    }
                }
            }
            return count
        }

        private fun getArmorPoints(player: PlayerEntity): Map<Int, Pair<ItemStack, CustomArmorBar>> {
            var armorPoints = 0
            val armorItem = hashMapOf<Int, Pair<ItemStack, CustomArmorBar>>()
            val equipment = player.armorItems.reversed()

            repeat(player.attributes.getBaseValue(EntityAttributes.GENERIC_ARMOR).toInt()) {
                armorItem[armorPoints++] = Pair(ItemStack.EMPTY, CustomArmorBar.DEFAULT)
            }
            for (itemStack in equipment.filter { !it.isEmpty && it.item is ArmorItem }) {
                val armor = itemStack.item as ArmorItem
                val attributes = itemStack.getAttributeModifiers(armor.slotType)
                val barData = if (getConfig().options?.toggleArmorTypes == true) {
                    CustomArmors.armorList.getOrDefault(itemStack.item, CustomArmorBar.DEFAULT)
                } else {
                    if (getConfig().options?.toggleNetherites == true && armor.material == ArmorMaterials.NETHERITE) {
                        CustomArmors.armorList.getOrDefault(itemStack.item, CustomArmorBar.DEFAULT)
                    } else {
                        CustomArmorBar.DEFAULT
                    }
                }
                if (attributes.containsKey(EntityAttributes.GENERIC_ARMOR)) {
                    val b = attributes.get(EntityAttributes.GENERIC_ARMOR).map { it.value }.sum().toInt()
                    repeat(b) {
                        armorItem[armorPoints++] = Pair(itemStack, barData)
                    }
                } else {
                    repeat(armor.protection) {
                        armorItem[armorPoints++] = Pair(itemStack, barData)
                    }
                }
            }
            if (getConfig().options?.toggleItemBar == true) {
                for (itemStack in equipment.filter { !it.isEmpty && it.item !is ArmorItem }) {
                    if (!itemStack.isEmpty) {
                        if (CustomArmors.itemList.containsKey(itemStack.item)) {
                            if (armorPoints % 2 == 1)
                                armorItem[armorPoints++] = Pair(ItemStack.EMPTY, CustomArmorBar.EMPTY)

                            val barData = CustomArmors.itemList[itemStack.item] ?: continue
                            armorItem[armorPoints++] = Pair(itemStack, barData)
                            armorItem[armorPoints++] = Pair(itemStack, barData)
                        }
                    }
                }
            }
            return armorItem
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
        val armorPoints = getArmorPoints(player)
        val thorns = getThorns(player.armorItems)

        val playerHealth = MathHelper.ceil(player.health)
        val maxArmorPoints = armorPoints.size
        val minArmorPoints = min(maxArmorPoints, 20)
        val totalEnchants = protectArr.sum()
        val maxHealth = player.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH).toFloat().coerceAtLeast(playerHealth.toFloat())
        val absorptionHealth = MathHelper.ceil(player.absorptionAmount)
        val resultHealth = MathHelper.ceil((maxHealth + absorptionHealth.toFloat()) / 20.0f)
        val screenWidth = client.window.scaledWidth / 2 - 91
        val screenHeight = client.window.scaledHeight - 39
        val yPos = screenHeight - (resultHealth - 1) * (10 - (resultHealth - 2)).coerceAtLeast(3) - 10


        RenderSystem.enableBlend()
        RenderSystem.setShader(GameRenderer::getPositionTexShader)

        //Default
        if (maxArmorPoints > 0) {
            val stackCount = (maxArmorPoints - 1) / 20
            val stackRow = stackCount * 20
            for (count in 0..9) {
                val xPos = screenWidth + count * 8

                if (count * 2 + 1 + stackRow < maxArmorPoints) {
                    val am1 = armorPoints.getOrDefault(count * 2 + stackRow, Pair(ItemStack.EMPTY, CustomArmorBar.DEFAULT))
                    val am2 = armorPoints.getOrDefault(count * 2 + 1 + stackRow, Pair(ItemStack.EMPTY, CustomArmorBar.DEFAULT))
                    if (am1 == am2 || (am1.first.item is ArmorItem && (am1.first.item as? ArmorItem)?.material == (am2.first.item as? ArmorItem)?.material)) {
                        am1.second.draw(am1.first, matrices, xPos, yPos, false, isMirror = false)
                    } else {
                        am2.second.draw(am2.first, matrices, xPos, yPos, true, isMirror = true)
                        am1.second.draw(am1.first, matrices, xPos, yPos, true, isMirror = false)
                    }
                }
                if (count * 2 + 1 + stackRow == maxArmorPoints) {
                    CustomArmorBar.EMPTY.draw(ItemStack.EMPTY, matrices, xPos, yPos, false, isMirror = false)
                    val am = armorPoints.getOrDefault(count * 2 + stackRow, Pair(ItemStack.EMPTY, CustomArmorBar.DEFAULT))
                    am.second.draw(am.first, matrices, xPos, yPos, true, isMirror = false)
                }
                if (count * 2 + 1 + stackRow > maxArmorPoints) {
                    CustomArmorBar.EMPTY.draw(ItemStack.EMPTY, matrices, xPos, yPos, false, isMirror = false)
                }
            }

            if (armorPoints.size > 20) {
                repeat(stackCount) {
                    CustomArmorBar.DEFAULT.draw(ItemStack.EMPTY, matrices, screenWidth - 7 - ((stackCount-it)*3), yPos, false, isMirror = false)
                }
            }
        }

        //Durability Color
        if (getConfig().options?.toggleDurability == true) {
            var lowDur = getLowDurabilityItem(player.armorItems)
            val lowDurColor = getLowDurabilityColor()
            val halfArmors = ceil(minArmorPoints / 2.0).toInt() - 1
            if (minArmorPoints != 0 && lowDur != 0) {
                for (count in 0..halfArmors) {
                    if (lowDur == 0) break

                    val xPos = screenWidth + (halfArmors - count) * 8
                    val am = armorPoints.getOrDefault((halfArmors - count) * 2, Pair(ItemStack.EMPTY, CustomArmorBar.DEFAULT))
                    if (minArmorPoints == (halfArmors - count) * 2 + 1) {
                        if (count == 0) {
                            am.second.drawOutLine(am.first, matrices, xPos, yPos, true, isMirror = false, lowDurColor)
                            lowDur--
                        }
                    } else {
                        if (lowDur == 1) {
                            am.second.drawOutLine(am.first, matrices, xPos, yPos, true, isMirror = true, lowDurColor)
                            lowDur = 0
                        } else {
                            am.second.drawOutLine(am.first, matrices, xPos, yPos, false, isMirror = false, lowDurColor)
                            lowDur -= 2
                        }
                    }
                }
            }
        }

        //Mending Color
        if (getConfig().options?.toggleMending == true && minArmorPoints != 0 && maxArmorPoints > 0) {
            val mendingTime = DetailArmorBar.getTicks() - LAST_MENDING
            val mendingSpeed = 3
            if (mendingTime < (mendingSpeed * 4)) {
                for (count in 0..9) {
                    if (minArmorPoints == 0 || mendingTime >= (mendingSpeed * 4)) break

                    if (mendingTime % (mendingSpeed * 2) < mendingSpeed) {
                        val xPos = screenWidth + count * 8

                        val am = armorPoints.getOrDefault(count * 2, Pair(ItemStack.EMPTY, CustomArmorBar.EMPTY))
                        am.second.drawOutLine(am.first, matrices, xPos, yPos, false, isMirror = false, Color.WHITE)
                    }
                }
            }
        }

        RenderSystem.setShaderTexture(0, GUI_ARMOR_BAR)

        //Armor Enchantments
        if (getConfig().options?.toggleEnchants == true && totalEnchants != 0 && maxArmorPoints > 0) {
            for (count in 0..9) {
                if (count * 2 + 1 > totalEnchants) break

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
        if (getConfig().options?.toggleThorns == true && thorns.total != 0 && maxArmorPoints > 0) {
            val thornsColor = getThornColor()
            for (count in 0..9) {
                if (count * 2 + 1 > thorns.total) break

                val xPos = screenWidth + count * 8
                if (count * 2 + 1 < thorns.total) {
                    InGameDrawer.drawTexture(matrices, xPos, yPos, 36, 18, thornsColor)
                }
                if (count * 2 + 1 == thorns.total) {
                    InGameDrawer.drawTexture(matrices, xPos, yPos, 27, 18, thornsColor)
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

        InGameDrawer.drawTexture(matrices, x, y, u, v, color)
    }
}