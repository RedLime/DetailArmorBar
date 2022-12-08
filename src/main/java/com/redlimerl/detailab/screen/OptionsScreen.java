package com.redlimerl.detailab.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

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
        super(Text.translatable("options.title"));
        this.parent = parent;
        this.optionType = optionType;
    }

    private int buttonCount = 0;

    @Override
    protected void init() {
        super.init();

        if (optionType == OptionType.FEATURES) {
            addDrawableChild(
                    ButtonWidget
                        .builder(getToggleName("enchantments", getConfig().getOptions().toggleEnchants), (button) -> {
                            getConfig().getOptions().toggleEnchants = !getConfig().getOptions().toggleEnchants; getConfig().save();
                            button.setMessage(getToggleName("enchantments", getConfig().getOptions().toggleEnchants));
                        })
                        .dimensions(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20)
                        .tooltip(Tooltip.of(Text.translatable("context.detailarmorbar.toggle.enchantments")))
                        .build()
            );
            buttonCount++;

            addDrawableChild(
                    ButtonWidget.builder(getToggleName("netherites", getConfig().getOptions().toggleNetherites), (button) -> {
                        getConfig().getOptions().toggleNetherites = !getConfig().getOptions().toggleNetherites; getConfig().save();
                        button.setMessage(getToggleName("netherites", getConfig().getOptions().toggleNetherites));
                    }).dimensions(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20)
                        .tooltip(Tooltip.of(Text.translatable("context.detailarmorbar.toggle.netherites")))
                        .build()
            );
            buttonCount++;

            addDrawableChild(ButtonWidget.builder(getToggleName("thorns", getConfig().getOptions().toggleThorns), (button) -> {
                        getConfig().getOptions().toggleThorns = !getConfig().getOptions().toggleThorns; getConfig().save();
                        button.setMessage(getToggleName("thorns", getConfig().getOptions().toggleThorns));
                    }).dimensions(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20)
                            .tooltip(Tooltip.of(Text.translatable("context.detailarmorbar.toggle.thorns")))
                    .build()
            );
            buttonCount++;

            addDrawableChild(ButtonWidget.builder(getToggleName("durability", getConfig().getOptions().toggleDurability), (button) -> {
                        getConfig().getOptions().toggleDurability = !getConfig().getOptions().toggleDurability; getConfig().save();
                        button.setMessage(getToggleName("durability", getConfig().getOptions().toggleDurability));
                    }).dimensions(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20)
                            .tooltip(Tooltip.of(Text.translatable("context.detailarmorbar.toggle.durability")))
                            .build()
            );
            buttonCount++;

            addDrawableChild(ButtonWidget.builder(getToggleName("mending", getConfig().getOptions().toggleMending), (button) -> {
                        getConfig().getOptions().toggleMending = !getConfig().getOptions().toggleMending; getConfig().save();
                        button.setMessage(getToggleName("mending", getConfig().getOptions().toggleMending));
                    }).dimensions(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20)
                            .tooltip(Tooltip.of(Text.translatable("context.detailarmorbar.toggle.mending")))
                    .build()
            );
            buttonCount++;

            addDrawableChild(ButtonWidget.builder(getToggleName("armor_types", getConfig().getOptions().toggleArmorTypes), (button) -> {
                        getConfig().getOptions().toggleArmorTypes = !getConfig().getOptions().toggleArmorTypes; getConfig().save();
                        button.setMessage(getToggleName("armor_types", getConfig().getOptions().toggleArmorTypes));
                    }).dimensions(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20)
                            .tooltip(Tooltip.of(Text.translatable("context.detailarmorbar.toggle.armor_types")))
                    .build()
            );
            buttonCount++;

            addDrawableChild(ButtonWidget.builder(getToggleName("item_types", getConfig().getOptions().toggleItemBar), (button) -> {
                        getConfig().getOptions().toggleItemBar = !getConfig().getOptions().toggleItemBar; getConfig().save();
                        button.setMessage(getToggleName("item_types", getConfig().getOptions().toggleItemBar));
                    }).dimensions(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20)
                            .tooltip(Tooltip.of(Text.translatable("context.detailarmorbar.toggle.item_types")))
                    .build()
            );
            buttonCount++;

            addDrawableChild(ButtonWidget.builder(getToggleName("empty_bar", getConfig().getOptions().toggleEmptyBar), (button) -> {
                        getConfig().getOptions().toggleEmptyBar = !getConfig().getOptions().toggleEmptyBar; getConfig().save();
                        button.setMessage(getToggleName("empty_bar", getConfig().getOptions().toggleEmptyBar));
                    }).dimensions(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20)
                            .tooltip(Tooltip.of(Text.translatable("context.detailarmorbar.toggle.empty_bar")))
                    .build()
            );
            buttonCount++;
        }

        if (optionType == OptionType.ANIMATION) {
            addDrawableChild(ButtonWidget.builder(getEnumName("effect_type", getConfig().getOptions().effectType), (button) -> {
                        getConfig().getOptions().effectType = getEnumNext(getConfig().getOptions().effectType);
                        getConfig().save();
                        button.setMessage(getEnumName("effect_type", getConfig().getOptions().effectType));
                    }).dimensions(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20)
                            .tooltip(Tooltip.of(Text.literal(getEnumDescription("effect_type", getConfig().getOptions().effectType))))
                    .build()
            );
            buttonCount++;

            addDrawableChild(ButtonWidget.builder(getEnumName("effect_speed", getConfig().getOptions().effectSpeed), (button) -> {
                        getConfig().getOptions().effectSpeed = getEnumNext(getConfig().getOptions().effectSpeed);
                        getConfig().save();
                        button.setMessage(getEnumName("effect_speed", getConfig().getOptions().effectSpeed));
                    }).dimensions(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20)
                            .tooltip(Tooltip.of(Text.literal(getEnumDescription("effect_speed", getConfig().getOptions().effectSpeed))))
                    .build()
            );
            buttonCount++;

            addDrawableChild(ButtonWidget.builder(getEnumName("thorn", getConfig().getOptions().effectThorn), (button) -> {
                        getConfig().getOptions().effectThorn = getEnumNext(getConfig().getOptions().effectThorn);
                        getConfig().save();
                        button.setMessage(getEnumName("thorn", getConfig().getOptions().effectThorn));
                    }).dimensions(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20)
                            .tooltip(Tooltip.of(Text.literal(getEnumDescription("thorn", getConfig().getOptions().effectThorn))))
                            .build()
            );
            buttonCount++;
        }

        if (optionType == OptionType.ETC) {
            addDrawableChild(ButtonWidget.builder(getToggleName("vanilla_texture", getConfig().getOptions().toggleVanillaTexture), (button) -> {
                        getConfig().getOptions().toggleVanillaTexture = !getConfig().getOptions().toggleVanillaTexture; getConfig().save();
                        button.setMessage(getToggleName("vanilla_texture", getConfig().getOptions().toggleVanillaTexture));
                    }).dimensions(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20)
                            .tooltip(Tooltip.of(Text.translatable("context.detailarmorbar.toggle.vanilla_texture")))
                    .build()
            );
            buttonCount++;

            addDrawableChild(ButtonWidget.builder(getToggleName("compatible_heart_mod", getConfig().getOptions().toggleCompatibleHeartMod), (button) -> {
                        getConfig().getOptions().toggleCompatibleHeartMod = !getConfig().getOptions().toggleCompatibleHeartMod; getConfig().save();
                        button.setMessage(getToggleName("compatible_heart_mod", getConfig().getOptions().toggleCompatibleHeartMod));
                    }).dimensions(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20)
                            .tooltip(Tooltip.of(Text.translatable("context.detailarmorbar.toggle.compatible_heart_mod")))
                            .build()
            );
            buttonCount++;

            addDrawableChild(ButtonWidget.builder(getToggleName("inverse_slot", getConfig().getOptions().toggleInverseSlot), (button) -> {
                        getConfig().getOptions().toggleInverseSlot = !getConfig().getOptions().toggleInverseSlot; getConfig().save();
                        button.setMessage(getToggleName("inverse_slot", getConfig().getOptions().toggleInverseSlot));
                    }).dimensions(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20)
                            .tooltip(Tooltip.of(Text.translatable("context.detailarmorbar.toggle.inverse_slot")))
                            .build()
            );
            buttonCount++;

            addDrawableChild(ButtonWidget.builder(getToggleName("sort_special_item", getConfig().getOptions().toggleSortSpecialItem), (button) -> {
                        getConfig().getOptions().toggleSortSpecialItem = !getConfig().getOptions().toggleSortSpecialItem; getConfig().save();
                        button.setMessage(getToggleName("sort_special_item", getConfig().getOptions().toggleSortSpecialItem));
                    }).dimensions(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20)
                            .tooltip(Tooltip.of(Text.translatable("context.detailarmorbar.toggle.sort_special_item")))
                            .build()
            );
            buttonCount++;
        }

        ButtonWidget features = addDrawableChild(ButtonWidget.builder(
                Text.translatable("option.detailarmorbar.title.features"), (matrixStack) -> {
            if (client != null) {
                client.setScreen(new OptionsScreen(parent, OptionType.FEATURES));
            }
        }).dimensions(width / 2 - 92, height / 6 + 140, 60, 20).build());
        features.active = optionType != OptionType.FEATURES;

        ButtonWidget animation = addDrawableChild(ButtonWidget.builder(
                Text.translatable("option.detailarmorbar.title.animation"), (matrixStack) -> {
            if (client != null) {
                client.setScreen(new OptionsScreen(parent, OptionType.ANIMATION));
            }
        }).dimensions(width / 2 - 30, height / 6 + 140, 60, 20).build());
        animation.active = optionType != OptionType.ANIMATION;

        ButtonWidget etc = addDrawableChild(ButtonWidget.builder(
                Text.translatable("option.detailarmorbar.title.etc"), (matrixStack) -> {
            if (client != null) {
                client.setScreen(new OptionsScreen(parent, OptionType.ETC));
            }
        }).dimensions(width / 2 + 32, height / 6 + 140, 60, 20).build());
        etc.active = optionType != OptionType.ETC;

        addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, (matrixStack) -> {
            if (client != null) {
                client.setScreen(parent);
            }
        }).dimensions(width / 2 - 100, height / 6 + 168, 200, 20).build());
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        buttonCount = 0;
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 15, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        if (client != null) {
            client.setScreen(parent);
        }
    }

    private <T extends Enum<T>> T getEnumNext(T target) {
        List<T> v = EnumSet.allOf(target.getDeclaringClass()).stream().toList();
        return v.get((v.indexOf(target) + 1) % v.size());
    }

    private <T extends Enum<T>> MutableText getEnumName(String type, T target) {
        return Text.translatable("option.detailarmorbar.effects."+type)
                .append(": ")
                .append(Text.translatable("option.detailarmorbar.effects."+type+"."+target.name().toLowerCase(Locale.ROOT)));
    }

    private <T extends Enum<T>> String getEnumDescription(String type, T target) {
        ArrayList<String> list = new ArrayList<>();
        for (String s : Text.translatable("context.detailarmorbar.effects." + type).getString().split("/"))
            list.add(Text.literal("§e" + s).getString());

        list.add("§f");

        List<T> v = EnumSet.allOf(target.getDeclaringClass()).stream().toList();
        for (T t : v) {
            list.add(Text.literal(" ")
                    .append(Text.translatable("option.detailarmorbar.effects."+type+"."+t.name().toLowerCase(Locale.ROOT)).formatted(Formatting.ITALIC))
                    .append(" - ")
                    .append(Text.translatable("context.detailarmorbar.effects."+type+"."+t.name().toLowerCase(Locale.ROOT))).getString());
        }
        return String.join("\n", list);
    }

    private MutableText getToggleName(String type, boolean target) {
        return Text.translatable("option.detailarmorbar.toggle."+type)
                .append(": ")
                .append(target ? ScreenTexts.ON : ScreenTexts.OFF);
    }

}
