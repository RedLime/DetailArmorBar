package com.redlimerl.detailab.render;

import com.google.common.collect.Multimap;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.redlimerl.detailab.DetailArmorBar;
import com.redlimerl.detailab.api.DetailArmorBarAPI;
import com.redlimerl.detailab.api.render.CustomArmorBar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import static com.redlimerl.detailab.ConfigEnumType.Animation;
import static com.redlimerl.detailab.ConfigEnumType.ProtectionEffect;
import static com.redlimerl.detailab.DetailArmorBar.GUI_ARMOR_BAR;
import static com.redlimerl.detailab.DetailArmorBar.getConfig;

public class ArmorBarRenderer {
    static class LevelData {
        int level;
        int count;
        LevelData(int level, int count) {
            this.level = level;
            this.count = count;
        }
    }

    public static final ArmorBarRenderer INSTANCE = new ArmorBarRenderer();
    public static long LAST_THORNS = 0L;
    public static long LAST_MENDING = 0L;

    private static int getAnimationSpeed() {
        return switch (getConfig().getOptions().effectSpeed) {
            case VERY_SLOW -> 45;
            case SLOW -> 37;
            case FAST -> 23;
            case VERY_FAST -> 15;
            default -> 30;
        };
    }

    private static float lerp(float delta, float start, float end) {
        return start + delta * (end - start);
    }

    private static Color getProtectColor(int g, int p, int e, int f, int a) {
        int speed = getAnimationSpeed();
        int alpha;
        if (getConfig().getOptions().effectType == ProtectionEffect.AURA) alpha = 80;
        else if (getConfig().getOptions().effectType == ProtectionEffect.OUTLINE) {
            long time = DetailArmorBar.getTicks();
            if (time % (speed*4L) < (speed*2L)) alpha = 0;
            else if (time % (speed*2L) < speed)
                alpha = Math.round(lerp((time % speed) / (speed - 1f), 0f, 0.65f) * 255);
            else alpha = Math.round(lerp((time % speed) / (speed - 1f), 0.65f, 0f) * 255);
        } else alpha = 0;

        if (g > 0) return new Color(153, 255, 255, alpha);
        if (p > 0) return new Color(112, 51, 173, alpha);
        if (e > 0) return new Color(255, 255, 0, alpha);
        if (f > 0) return new Color(210, 56, 0, alpha);
        if (a > 0) return new Color(255, 255, 255, alpha);
        return Color.WHITE;
    }

    private static Color getProtectColor(int[] s) {
        return getProtectColor(s[0], s[1], s[2], s[3], s[4]);
    }

    private static Color getLowDurabilityColor() {
        int speed = getAnimationSpeed();
        long time = DetailArmorBar.getTicks();
        int alpha;
        if (time % (speed*4L) >= (speed*2L)) alpha = 0;
        else if (time % (speed*2L) < speed)
            alpha = Math.round(lerp((time % speed) / (speed - 1f), 0f, 0.65f) * 255);
        else alpha = Math.round(lerp((time % speed) / (speed - 1f), 0.65f, 0f) * 255);

        return new Color(255, 25, 25, alpha);
    }

    private static Color getThornColor() {
        long time = DetailArmorBar.getTicks() - LAST_THORNS;
        if (getConfig().getOptions().effectThorn == Animation.STATIC) return Color.WHITE;
        if (time > 19) return Color.WHITE;

        int cc = Math.round(lerp((time % 20) / 19f, 0f, 1f)*255);
        return new Color(255, cc, cc);
    }

    private static Map<Enchantment, LevelData> getEnchantments(Iterable<ItemStack> equipment) {
        HashMap<Enchantment, LevelData> result = new HashMap<>();

        for (ItemStack itemStack : equipment) {
            if (!itemStack.isEmpty()) {
                EnchantmentHelper.getEnchantments(itemStack).forEach((enchantment, integer) -> {
                    LevelData enchantData = result.getOrDefault(enchantment, new LevelData(0, 0));
                    enchantData.count++;
                    enchantData.level += integer;
                    if (enchantment == Enchantments.THORNS) enchantData.level += integer - 1;
                    result.put(enchantment, enchantData);
                });
            }
        }

        return result;
    }

    private static LevelData getEnchantLevel(Iterable<ItemStack> equipment, Enchantment type) {
        return getEnchantments(equipment).getOrDefault(type, new LevelData(0, 0));
    }

    private int getLowDurabilityItem(Iterable<ItemStack> equipment) {
        var count = 0;
        for (ItemStack itemStack : equipment) {
            if (!itemStack.isEmpty()) {
                if (itemStack.getMaxDamage() != 0 && ((itemStack.getDamageValue() * 100f) / (itemStack.getMaxDamage() * 100f)) >= 0.92f) {
                    count += itemStack.getItem() instanceof ArmorItem ? getDefense(itemStack) : 2;
                }
            }
        }

        return count;
    }

    private static Map<Integer, Pair<ItemStack, CustomArmorBar>> getArmorPoints(Player player) {
        int armorPoints = 0;
        HashMap<Integer, Pair<ItemStack, CustomArmorBar>> armorItem = new HashMap<>();
        Stack<ItemStack> equipment = new Stack<>();
        for (ItemStack item : player.getArmorSlots()) {
            equipment.add(0, item);
        }

        for (int i = 0; i < player.getAttributes().getBaseValue(Attributes.ARMOR); i++)
            armorItem.put(armorPoints++, new Pair<>(ItemStack.EMPTY, CustomArmorBar.DEFAULT));

        for (ItemStack itemStack : equipment) {
            if (!itemStack.isEmpty() && itemStack.getItem() instanceof ArmorItem armor) {
                Multimap<Attribute, AttributeModifier> attributes = itemStack.getAttributeModifiers(armor.getSlot());

                CustomArmorBar barData;
                if (getConfig().getOptions().toggleArmorTypes) {
                    barData = DetailArmorBarAPI.getArmorBarList().getOrDefault(armor, CustomArmorBar.DEFAULT);
                } else if (getConfig().getOptions().toggleNetherites && armor.getMaterial() == ArmorMaterials.NETHERITE) {
                    barData = DetailArmorBarAPI.getArmorBarList().getOrDefault(armor, CustomArmorBar.DEFAULT);
                } else {
                    barData = CustomArmorBar.DEFAULT;
                }

                for (int i = 0; i < getDefense(itemStack); i++) {
                    armorItem.put(armorPoints++, new Pair<>(itemStack, barData));
                }
            }
        }

        if (getConfig().getOptions().toggleItemBar) {
            for (ItemStack itemStack : equipment) {
                if (!itemStack.isEmpty() && !(itemStack.getItem() instanceof ArmorItem) && DetailArmorBarAPI.getItemBarList().containsKey(itemStack.getItem())) {
                    if (armorPoints % 2 == 1)
                        armorItem.put(armorPoints++, new Pair<>(ItemStack.EMPTY, CustomArmorBar.EMPTY));

                    var barData = DetailArmorBarAPI.getItemBarList().get(itemStack.getItem());
                    armorItem.put(armorPoints++, new Pair<>(itemStack, barData));
                    armorItem.put(armorPoints++, new Pair<>(itemStack, barData));
                }
            }
        }
        return armorItem;
    }

    private static int getDefense(ItemStack itemStack) {
        ArmorItem armorItem = (ArmorItem) itemStack.getItem();
        Multimap<Attribute, AttributeModifier> attributes = itemStack.getAttributeModifiers(armorItem.getSlot());
        if (attributes.containsKey(Attributes.ARMOR)) {
            return attributes.get(Attributes.ARMOR).stream().mapToInt(att -> (int) att.getAmount()).sum();
        } else {
            return armorItem.getDefense();
        }
    }



    private final Minecraft client = Minecraft.getInstance();
    private final Gui hud = client.gui;

    public void render(PoseStack matrices, Player player) {
        client.getProfiler().popPush("armor");

        var generic = getEnchantLevel(player.getArmorSlots(), Enchantments.ALL_DAMAGE_PROTECTION);
        var projectile = getEnchantLevel(player.getArmorSlots(), Enchantments.PROJECTILE_PROTECTION);
        var explosive = getEnchantLevel(player.getArmorSlots(), Enchantments.BLAST_PROTECTION);
        var fire = getEnchantLevel(player.getArmorSlots(), Enchantments.FIRE_PROTECTION);
        var protectArr = new int[] { generic.level + generic.count, projectile.level, explosive.level, fire.level, 0 };
        var armorPoints = getArmorPoints(player);
        var thorns = getEnchantLevel(player.getArmorSlots(), Enchantments.THORNS);

        var playerHealth = Math.ceil(player.getHealth());
        var totalArmorPoint = armorPoints.size();
        var totalEnchants = Arrays.stream(protectArr).sum();
        var maxHealth = Math.max(player.getAttributeValue(Attributes.MAX_HEALTH), playerHealth);
        var absorptionHealth = Math.ceil(player.getAbsorptionAmount());
        var healthRow = (int) Math.ceil((maxHealth + absorptionHealth) / 20.0f);
        var screenWidth = client.getWindow().getGuiScaledWidth() / 2 - 91;
        var screenHeight = client.getWindow().getGuiScaledHeight() - 39;
        var yPos = screenHeight - (healthRow - 1) * Math.max(10 - (healthRow - 2), 3) - 10;

        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        //Default
        if (totalArmorPoint > 0) {
            int stackCount = (totalArmorPoint - 1) / 20;
            int stackRow = stackCount * 20;

            for (int count = 0; count < 10; count++) {
                int xPos = screenWidth + count * 8;

                if (count * 2 + 1 + stackRow < totalArmorPoint) {
                    Pair<ItemStack, CustomArmorBar> am1 = armorPoints.getOrDefault(count * 2 + stackRow, new Pair<>(ItemStack.EMPTY, CustomArmorBar.DEFAULT));
                    Pair<ItemStack, CustomArmorBar> am2 = armorPoints.getOrDefault(count * 2 + 1 + stackRow, new Pair<>(ItemStack.EMPTY, CustomArmorBar.DEFAULT));
                    if (am1 == am2 || (am1.getSecond() == am2.getSecond())) {
                        am1.getSecond().draw(am1.getFirst(), matrices, xPos, yPos, false, false);
                    } else {
                        am2.getSecond().draw(am2.getFirst(), matrices, xPos, yPos, true, true);
                        am1.getSecond().draw(am1.getFirst(), matrices, xPos, yPos, true, false);
                    }
                }
                if (count * 2 + 1 + stackRow == totalArmorPoint) {
                    CustomArmorBar.EMPTY.draw(ItemStack.EMPTY, matrices, xPos, yPos, false, false);
                    Pair<ItemStack, CustomArmorBar> am = armorPoints.getOrDefault(count * 2 + stackRow, new Pair<>(ItemStack.EMPTY, CustomArmorBar.DEFAULT));
                    am.getSecond().draw(am.getFirst(), matrices, xPos, yPos, true, false);
                }
                if (count * 2 + 1 + stackRow > totalArmorPoint) {
                    CustomArmorBar.EMPTY.draw(ItemStack.EMPTY, matrices, xPos, yPos, false, false);
                }
            }

            if (armorPoints.size() > 20) {
                for (int i = 0; i < stackCount; i++) {
                    CustomArmorBar.DEFAULT.draw(ItemStack.EMPTY, matrices, screenWidth - 7 - ((stackCount - i)*3), yPos, false, false);
                }
            }
        }

        //Durability Color
        if (getConfig().getOptions().toggleDurability) {
            int lowDur = getLowDurabilityItem(player.getArmorSlots());

            if (totalArmorPoint != 0 && lowDur != 0) {
                Color lowDurColor = getLowDurabilityColor();
                if (lowDurColor.getAlpha() != 0) {
                    int armorPreset = ((totalArmorPoint - 1) % 20) + 1;
                    int halfArmors = (int) Math.ceil(armorPreset / 2.0) - 1;
                    for (int count = 0; count < halfArmors + 1; count++) {
                        if (lowDur == 0) break;

                        int xPos = screenWidth + (halfArmors - count) * 8;
                        Pair<ItemStack, CustomArmorBar> am = armorPoints.getOrDefault((halfArmors - count) * 2, new Pair<>(ItemStack.EMPTY, CustomArmorBar.DEFAULT));

                        if (armorPreset == (halfArmors - count) * 2 + 1) {
                            if (count == 0) {
                                am.getSecond().drawOutLine(am.getFirst(), matrices, xPos, yPos, true, false, lowDurColor);
                                lowDur--;
                            }
                        } else {
                            if (lowDur == 1) {
                                am.getSecond().drawOutLine(am.getFirst(), matrices, xPos, yPos, true, true, lowDurColor);
                                lowDur = 0;
                            } else {
                                am.getSecond().drawOutLine(am.getFirst(), matrices, xPos, yPos, false, false, lowDurColor);
                                lowDur -= 2;
                            }
                        }
                    }
                }
            }
        }

        //Mending Color
        if (getConfig().getOptions().toggleMending && totalArmorPoint != 0) {
            var mendingTime = DetailArmorBar.getTicks() - LAST_MENDING;
            var mendingSpeed = 3;

            if (mendingTime < (mendingSpeed * 4)) {
                for (int count = 0; count < 10; count++) {
                    if (mendingTime % (mendingSpeed * 2) < mendingSpeed) {
                        int xPos = screenWidth + count * 8;

                        Pair<ItemStack, CustomArmorBar> am = armorPoints.getOrDefault(count * 2, new Pair<>(ItemStack.EMPTY, CustomArmorBar.EMPTY));
                        am.getSecond().drawOutLine(am.getFirst(), matrices, xPos, yPos, false, false, Color.WHITE);
                    }
                }
            }
        }

        RenderSystem.setShaderTexture(0, GUI_ARMOR_BAR);

        //Armor Enchantments
        if (getConfig().getOptions().toggleEnchants && totalEnchants > 0 && totalArmorPoint > 0) {
            for (int count = 0; count * 2 + 1 <= totalEnchants; count++) {
                if (count > 9) break;

                var xPos = screenWidth + count * 8;
                if (count * 2 + 1 < totalEnchants) {
                    var min = -1;
                    var max = -1;
                    for (int pw = 0; pw < 5; pw++) {
                        if (min == -1 && protectArr[pw] > 1) {
                            min = pw;
                            break;
                        } else if (min == -1 && protectArr[pw] == 1) {
                            min = pw;
                        } else if (min != -1 && max == -1 && protectArr[pw] >= 1) max = pw;
                    }
                    if (min != -1 && max != -1) {
                        drawEnchantTexture(matrices, xPos, yPos, getProtectColor(protectArr), 2);
                        protectArr[min] = 0;
                        drawEnchantTexture(matrices, xPos, yPos, getProtectColor(protectArr), 1);
                        protectArr[max] -= 1;
                    } else {
                        drawEnchantTexture(matrices, xPos, yPos, getProtectColor(protectArr), 0);
                        protectArr[min] -= 2;
                    }
                }
                if (count * 2 + 1 == totalEnchants) {
                    drawEnchantTexture(matrices, xPos, yPos, getProtectColor(protectArr), 2);
                }
            }
        }

        //Thorns Check
        if (getConfig().getOptions().toggleThorns && thorns.level > 0 && totalArmorPoint > 0) {
            Color thornsColor = getThornColor();
            for (int count = 0; count < 10; count++) {
                if (count * 2 + 1 > thorns.level) break;

                int xPos = screenWidth + count * 8;
                if (count * 2 + 1 < thorns.level) {
                    InGameDrawer.drawTexture(matrices, xPos, yPos, 36, 18, thornsColor, false);
                }
                if (count * 2 + 1 == thorns.level) {
                    InGameDrawer.drawTexture(matrices, xPos, yPos, 27, 18, thornsColor, false);
                }
            }
        }


        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, GuiComponent.GUI_ICONS_LOCATION);
    }

    private void drawEnchantTexture(PoseStack matrices, int x, int y, Color color, int half) {
        int u = 0;
        int v = 0;
        var t = (hud.getGuiTicks()/3) % 36;

        if (getConfig().getOptions().effectType == ProtectionEffect.AURA) {
            if (t < 12) {
                u = (t % 12) * 9;
                v = 27 + (half * 9);
            }
        } else if (getConfig().getOptions().effectType == ProtectionEffect.OUTLINE) {
            u = 9 + (half * 9);
        } else return;

        InGameDrawer.drawTexture(matrices, x, y, u, v, color, false);
    }
}
