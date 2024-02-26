package com.redlimerl.detailab.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.*;

import javax.annotation.Nonnull;
import java.util.*;

import static com.redlimerl.detailab.DetailArmorBar.getConfig;

public class OptionsScreen extends Screen {
    private final Screen parent;
    private final OptionType optionType;

    public enum OptionType {
        FEATURES, ANIMATION, ETC
    }

    public OptionsScreen(Screen screen) {
        this(screen, OptionType.FEATURES);
    }

    public OptionsScreen(Screen parent, OptionType optionType) {
        super(Component.translatable("options.title"));
        this.parent = parent;
        this.optionType = optionType;
    }

    private int buttonCount = 0;

    @Override
    protected void init() {

        if (optionType == OptionType.FEATURES) {

            addRenderableWidget(
                    Button.builder(getToggleName("enchantments", getConfig().getOptions().toggleEnchants),
                    (button) -> {
                        getConfig().getOptions().toggleEnchants = !getConfig().getOptions().toggleEnchants;
                        getConfig().save();
                        button.setMessage(getToggleName("enchantments", getConfig().getOptions().toggleEnchants));
                    }).pos(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2))
                      .width(150).size(150, 20)
                      .tooltip(Tooltip.create(getToggleDescription("enchantments")))
                      .build()
            );
            buttonCount++;

            addRenderableWidget(Button.builder(getToggleName("netherites", getConfig().getOptions().toggleNetherites),
                    (button) -> {
                        getConfig().getOptions().toggleNetherites = !getConfig().getOptions().toggleNetherites;
                        getConfig().save();
                        button.setMessage(getToggleName("netherites", getConfig().getOptions().toggleNetherites));
                    }).pos(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2))
                      .width(150).size(150, 20)
                      .tooltip(Tooltip.create(getToggleDescription("netherites")))
                      .build()
            );
            buttonCount++;

            addRenderableWidget(Button.builder(getToggleName("thorns", getConfig().getOptions().toggleThorns),
                    (button) -> {
                        getConfig().getOptions().toggleThorns = !getConfig().getOptions().toggleThorns;
                        getConfig().save();
                        button.setMessage(getToggleName("thorns", getConfig().getOptions().toggleThorns));
                    }).pos(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2))
                      .width(150).size(150, 20)
                      .tooltip(Tooltip.create(getToggleDescription("thorns")))
                      .build()
            );
            buttonCount++;

            addRenderableWidget(Button.builder(getToggleName("durability", getConfig().getOptions().toggleDurability),
                    (button) -> {
                        getConfig().getOptions().toggleDurability = !getConfig().getOptions().toggleDurability;
                        getConfig().save();
                        button.setMessage(getToggleName("durability", getConfig().getOptions().toggleDurability));
                    }).pos(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2))
                      .width(150).size(150, 20)
                      .tooltip(Tooltip.create(getToggleDescription("durability")))
                      .build()
            );
            buttonCount++;

            addRenderableWidget(Button.builder(getToggleName("mending", getConfig().getOptions().toggleMending),
                    (button) -> {
                        getConfig().getOptions().toggleMending = !getConfig().getOptions().toggleMending;
                        getConfig().save();
                        button.setMessage(getToggleName("mending", getConfig().getOptions().toggleMending));
                    }).pos(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2))
                      .width(150).size(150, 20)
                      .tooltip(Tooltip.create(getToggleDescription("mending")))
                      .build()
            );
            buttonCount++;

            addRenderableWidget(Button.builder(getToggleName("armor_types", getConfig().getOptions().toggleArmorTypes),
                    (button) -> {
                        getConfig().getOptions().toggleArmorTypes = !getConfig().getOptions().toggleArmorTypes;
                        getConfig().save();
                        button.setMessage(getToggleName("armor_types", getConfig().getOptions().toggleArmorTypes));
                    }).pos(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2))
                      .width(150).size(150, 20)
                      .tooltip(Tooltip.create(getToggleDescription("armor_types")))
                      .build()
            );
            buttonCount++;

            addRenderableWidget(Button.builder(getToggleName("item_types", getConfig().getOptions().toggleItemBar),
                    (button) -> {
                        getConfig().getOptions().toggleItemBar = !getConfig().getOptions().toggleItemBar;
                        getConfig().save();
                        button.setMessage(getToggleName("item_types", getConfig().getOptions().toggleItemBar));
                    }).pos(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2))
                      .width(150).size(150, 20)
                      .tooltip(Tooltip.create(getToggleDescription("item_types")))
                      .build()
            );
            buttonCount++;

            addRenderableWidget(Button.builder(getToggleName("empty_bar", getConfig().getOptions().toggleEmptyBar),
                    (button) -> {
                        getConfig().getOptions().toggleEmptyBar = !getConfig().getOptions().toggleEmptyBar;
                        getConfig().save();
                        button.setMessage(getToggleName("empty_bar", getConfig().getOptions().toggleEmptyBar));
                    }).pos(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2))
                      .width(150).size(150, 20)
                      .tooltip(Tooltip.create(getToggleDescription("empty_bar")))
                      .build()
            );
            buttonCount++;

        }

        if (optionType == OptionType.ANIMATION) {
            addRenderableWidget(Button.builder(getEnumName("effect_type", getConfig().getOptions().effectType),
                    (button) -> {
                        getConfig().getOptions().effectType = getEnumNext(getConfig().getOptions().effectType);
                        getConfig().save();
                        button.setMessage(getEnumName("effect_type", getConfig().getOptions().effectType));
                    }).pos(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2))
                      .width(150).size(150, 20)
                      .tooltip(Tooltip.create(getEnumDescription("effect_type", getConfig().getOptions().effectType)))
                      .build()
            );
            buttonCount++;

            addRenderableWidget(Button.builder(getEnumName("effect_speed", getConfig().getOptions().effectSpeed),
                    (button) -> {
                        getConfig().getOptions().effectSpeed = getEnumNext(getConfig().getOptions().effectSpeed);
                        getConfig().save();
                        button.setMessage(getEnumName("effect_speed", getConfig().getOptions().effectSpeed));
                    }).pos(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2))
                      .width(150).size(150, 20)
                      .tooltip(Tooltip.create(getEnumDescription("effect_speed", getConfig().getOptions().effectSpeed)))
                      .build()
            );
            buttonCount++;

            addRenderableWidget(Button.builder(getEnumName("thorn", getConfig().getOptions().effectThorn),
                    (button) -> {
                        getConfig().getOptions().effectThorn = getEnumNext(getConfig().getOptions().effectThorn);
                        getConfig().save();
                        button.setMessage(getEnumName("thorn", getConfig().getOptions().effectThorn));
                    }).pos(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2))
                      .width(150).size(150, 20)
                      .tooltip(Tooltip.create(getEnumDescription("thorn", getConfig().getOptions().effectThorn)))
                      .build()
            );
            buttonCount++;
        }

        if (optionType == OptionType.ETC) {
            addRenderableWidget(Button.builder(getToggleName("vanilla_texture", getConfig().getOptions().toggleVanillaTexture),
                    (button) -> {
                        getConfig().getOptions().toggleVanillaTexture = !getConfig().getOptions().toggleVanillaTexture;
                        getConfig().save();
                        button.setMessage(getToggleName("vanilla_texture", getConfig().getOptions().toggleVanillaTexture));
                    }).pos(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2))
                      .width(150).size(150, 20)
                      .tooltip(Tooltip.create(getToggleDescription("vanilla_texture")))
                      .build()
            );
            buttonCount++;

            addRenderableWidget(Button.builder(getToggleName("compatible_heart_mod", getConfig().getOptions().toggleCompatibleHeartMod),
                    (button) -> {
                        getConfig().getOptions().toggleCompatibleHeartMod = !getConfig().getOptions().toggleCompatibleHeartMod;
                        getConfig().save();
                        button.setMessage(getToggleName("compatible_heart_mod", getConfig().getOptions().toggleCompatibleHeartMod));
                    }).pos(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2))
                      .width(150).size(150, 20)
                      .tooltip(Tooltip.create(getToggleDescription("compatible_heart_mod")))
                      .build()
            );
            buttonCount++;

            addRenderableWidget(Button.builder(getToggleName("inverse_slot", getConfig().getOptions().toggleInverseSlot),
                    (button) -> {
                        getConfig().getOptions().toggleInverseSlot = !getConfig().getOptions().toggleInverseSlot;
                        getConfig().save();
                        button.setMessage(getToggleName("inverse_slot", getConfig().getOptions().toggleInverseSlot));
                    }).pos(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2))
                      .width(150).size(150, 20)
                      .tooltip(Tooltip.create(getToggleDescription("inverse_slot")))
                      .build()
            );
            buttonCount++;

            addRenderableWidget(Button.builder(getToggleName("sort_special_item", getConfig().getOptions().toggleSortSpecialItem),
                    (button) -> {
                        getConfig().getOptions().toggleSortSpecialItem = !getConfig().getOptions().toggleSortSpecialItem;
                        getConfig().save();
                        button.setMessage(getToggleName("sort_special_item", getConfig().getOptions().toggleSortSpecialItem));
                    }).pos(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2))
                      .width(150).size(150, 20)
                      .tooltip(Tooltip.create(getToggleDescription("sort_special_item")))
                      .build()
            );
            buttonCount++;
        }

        Button features = addRenderableWidget(Button.builder(Component.translatable("option.detailarmorbar.title.features"),
                            (button) -> {
                                if (this.minecraft != null) {
                                    this.minecraft.setScreen(new OptionsScreen(parent, OptionType.FEATURES));
                                }
                            })
                            .pos(width / 2 - 92, height / 6 + 140)
                            .size(60, 20)
                            .build()
        );
        features.active = optionType != OptionType.FEATURES;

;
        Button animation = addRenderableWidget(Button.builder(Component.translatable("option.detailarmorbar.title.animation"),
                            (button) -> {
                                if (this.minecraft != null) {
                                    this.minecraft.setScreen(new OptionsScreen(parent, OptionType.ANIMATION));
                                }
                            })
                            .pos(width / 2 - 30, height / 6 + 140)
                            .size(60, 20)
                            .build()
        );
        animation.active = optionType != OptionType.ANIMATION;

        Button etc = addRenderableWidget(Button.builder(Component.translatable("option.detailarmorbar.title.etc"),
                            (button) -> {
                                if (this.minecraft != null) {
                                    this.minecraft.setScreen(new OptionsScreen(parent, OptionType.ETC));
                                }
                            })
                            .pos(width / 2 + 32, height / 6 + 140)
                            .size(60, 20)
                            .build()
        );
        etc.active = optionType != OptionType.ETC;

        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> {
            if (this.minecraft != null) {
                this.minecraft.setScreen(parent);
            }
        }).pos(width / 2 - 100, height / 6 + 168).size(200, 20).build());
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        pGuiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 15, 16777215);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(parent);
        }
    }

    private <T extends Enum<T>> T getEnumNext(T target) {
        List<T> v = EnumSet.allOf(target.getDeclaringClass()).stream().toList();
        return v.get((v.indexOf(target) + 1) % v.size());
    }

    private <T extends Enum<T>> MutableComponent getEnumName(String type, T target) {
        return Component.translatable("option.detailarmorbar.effects."+type)
                .append(": ")
                .append(Component.translatable("option.detailarmorbar.effects."+type+"."+target.name().toLowerCase(Locale.ROOT)));
    }

    private <T extends Enum<T>> Component getEnumDescription(String type, T target) {
        MutableComponent comp = Component.translatable("context.detailarmorbar.effects." + type).withStyle(ChatFormatting.YELLOW);
        comp.append("\n");
        List<T> v = EnumSet.allOf(target.getDeclaringClass()).stream().toList();
        for (T t : v) {
            comp.append("\n");
            comp.append(Component.empty()
                    .append(Component.translatable("option.detailarmorbar.effects."+type+"."+t.name().toLowerCase(Locale.ROOT)).withStyle(ChatFormatting.ITALIC))
                    .append(" - ")
                    .append(Component.translatable("context.detailarmorbar.effects."+type+"."+t.name().toLowerCase(Locale.ROOT))));
        }
        return comp;
    }

    private MutableComponent getToggleName(String type, boolean target) {
        return Component.translatable("option.detailarmorbar.toggle."+type)
                .append(": ")
                .append(target ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF);
    }

    private Component getToggleDescription(String type) {
        return Component.translatable("context.detailarmorbar.toggle." + type);
    }
}
