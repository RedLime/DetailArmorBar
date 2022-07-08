package com.redlimerl.detailab.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
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

            addRenderableWidget(new Button(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20,
                    getToggleName("enchantments", getConfig().getOptions().toggleEnchants), (button) -> {
                getConfig().getOptions().toggleEnchants = !getConfig().getOptions().toggleEnchants; getConfig().save();
                button.setMessage(getToggleName("enchantments", getConfig().getOptions().toggleEnchants));
            }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, getToggleDescription("enchantments"), Optional.empty(), mouseX, mouseY)));
            buttonCount++;

            addRenderableWidget(new Button(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20,
                    getToggleName("netherites", getConfig().getOptions().toggleNetherites), (button) -> {
                getConfig().getOptions().toggleNetherites = !getConfig().getOptions().toggleNetherites; getConfig().save();
                button.setMessage(getToggleName("netherites", getConfig().getOptions().toggleNetherites));
            }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, getToggleDescription("netherites"), Optional.empty(), mouseX, mouseY)));
            buttonCount++;

            addRenderableWidget(new Button(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20,
                    getToggleName("thorns", getConfig().getOptions().toggleThorns), (button) -> {
                getConfig().getOptions().toggleThorns = !getConfig().getOptions().toggleThorns; getConfig().save();
                button.setMessage(getToggleName("thorns", getConfig().getOptions().toggleThorns));
            }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, getToggleDescription("thorns"), Optional.empty(), mouseX, mouseY)));
            buttonCount++;

            addRenderableWidget(new Button(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20,
                    getToggleName("durability", getConfig().getOptions().toggleDurability), (button) -> {
                getConfig().getOptions().toggleDurability = !getConfig().getOptions().toggleDurability; getConfig().save();
                button.setMessage(getToggleName("durability", getConfig().getOptions().toggleDurability));
            }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, getToggleDescription("durability"), Optional.empty(), mouseX, mouseY)));
            buttonCount++;

            addRenderableWidget(new Button(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20,
                    getToggleName("mending", getConfig().getOptions().toggleMending), (button) -> {
                getConfig().getOptions().toggleMending = !getConfig().getOptions().toggleMending; getConfig().save();
                button.setMessage(getToggleName("mending", getConfig().getOptions().toggleMending));
            }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, getToggleDescription("mending"), Optional.empty(), mouseX, mouseY)));
            buttonCount++;

            addRenderableWidget(new Button(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20,
                    getToggleName("armor_types", getConfig().getOptions().toggleArmorTypes), (button) -> {
                getConfig().getOptions().toggleArmorTypes = !getConfig().getOptions().toggleArmorTypes; getConfig().save();
                button.setMessage(getToggleName("armor_types", getConfig().getOptions().toggleArmorTypes));
            }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, getToggleDescription("armor_types"), Optional.empty(), mouseX, mouseY)));
            buttonCount++;

            addRenderableWidget(new Button(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20,
                    getToggleName("item_types", getConfig().getOptions().toggleItemBar), (button) -> {
                getConfig().getOptions().toggleItemBar = !getConfig().getOptions().toggleItemBar; getConfig().save();
                button.setMessage(getToggleName("item_types", getConfig().getOptions().toggleItemBar));
            }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, getToggleDescription("item_types"), Optional.empty(), mouseX, mouseY)));
            buttonCount++;

            addRenderableWidget(new Button(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20,
                    getToggleName("empty_bar", getConfig().getOptions().toggleEmptyBar), (button) -> {
                getConfig().getOptions().toggleEmptyBar = !getConfig().getOptions().toggleEmptyBar; getConfig().save();
                button.setMessage(getToggleName("empty_bar", getConfig().getOptions().toggleEmptyBar));
            }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, getToggleDescription("empty_bar"), Optional.empty(), mouseX, mouseY)));
            buttonCount++;
        }

        if (optionType == OptionType.ANIMATION) {
            addRenderableWidget(new Button(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20,
                    getEnumName("effect_type", getConfig().getOptions().effectType), (button) -> {
                getConfig().getOptions().effectType = getEnumNext(getConfig().getOptions().effectType);
                getConfig().save();
                button.setMessage(getEnumName("effect_type", getConfig().getOptions().effectType));
            }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, getEnumDescription("effect_type", getConfig().getOptions().effectType), Optional.empty(), mouseX, mouseY)));
            buttonCount++;

            addRenderableWidget(new Button(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20,
                    getEnumName("effect_speed", getConfig().getOptions().effectSpeed), (button) -> {
                getConfig().getOptions().effectSpeed = getEnumNext(getConfig().getOptions().effectSpeed);
                getConfig().save();
                button.setMessage(getEnumName("effect_speed", getConfig().getOptions().effectSpeed));
            }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, getEnumDescription("effect_speed", getConfig().getOptions().effectSpeed), Optional.empty(), mouseX, mouseY)));
            buttonCount++;

            addRenderableWidget(new Button(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20,
                    getEnumName("thorn", getConfig().getOptions().effectThorn), (button) -> {
                getConfig().getOptions().effectThorn = getEnumNext(getConfig().getOptions().effectThorn);
                getConfig().save();
                button.setMessage(getEnumName("thorn", getConfig().getOptions().effectThorn));
            }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, getEnumDescription("thorn", getConfig().getOptions().effectThorn), Optional.empty(), mouseX, mouseY)));
            buttonCount++;
        }

        if (optionType == OptionType.ETC) {
            addRenderableWidget(new Button(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20,
                    getToggleName("vanilla_texture", getConfig().getOptions().toggleVanillaTexture), (button) -> {
                getConfig().getOptions().toggleVanillaTexture = !getConfig().getOptions().toggleVanillaTexture; getConfig().save();
                button.setMessage(getToggleName("vanilla_texture", getConfig().getOptions().toggleVanillaTexture));
            }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, getToggleDescription("vanilla_texture"), Optional.empty(), mouseX, mouseY)));
            buttonCount++;

            addRenderableWidget(new Button(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20,
                    getToggleName("compatible_heart_mod", getConfig().getOptions().toggleCompatibleHeartMod), (button) -> {
                getConfig().getOptions().toggleCompatibleHeartMod = !getConfig().getOptions().toggleCompatibleHeartMod; getConfig().save();
                button.setMessage(getToggleName("compatible_heart_mod", getConfig().getOptions().toggleCompatibleHeartMod));
            }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, getToggleDescription("compatible_heart_mod"), Optional.empty(), mouseX, mouseY)));
            buttonCount++;

            addRenderableWidget(new Button(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20,
                    getToggleName("inverse_slot", getConfig().getOptions().toggleInverseSlot), (button) -> {
                getConfig().getOptions().toggleInverseSlot = !getConfig().getOptions().toggleInverseSlot; getConfig().save();
                button.setMessage(getToggleName("inverse_slot", getConfig().getOptions().toggleInverseSlot));
            }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, getToggleDescription("inverse_slot"), Optional.empty(), mouseX, mouseY)));
            buttonCount++;

            addRenderableWidget(new Button(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20,
                    getToggleName("sort_special_item", getConfig().getOptions().toggleSortSpecialItem), (button) -> {
                getConfig().getOptions().toggleSortSpecialItem = !getConfig().getOptions().toggleSortSpecialItem; getConfig().save();
                button.setMessage(getToggleName("sort_special_item", getConfig().getOptions().toggleSortSpecialItem));
            }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, getToggleDescription("sort_special_item"), Optional.empty(), mouseX, mouseY)));
            buttonCount++;
        }

        Button features = addRenderableWidget(new Button(width / 2 - 92, height / 6 + 140, 60, 20,
                Component.translatable("option.detailarmorbar.title.features"), (matrixStack) -> {
            if (this.minecraft != null) {
                this.minecraft.setScreen(new OptionsScreen(parent, OptionType.FEATURES));
            }
        }));
        features.active = optionType != OptionType.FEATURES;

        Button animation = addRenderableWidget(new Button(width / 2 - 30, height / 6 + 140, 60, 20,
                Component.translatable("option.detailarmorbar.title.animation"), (matrixStack) -> {
            if (this.minecraft != null) {
                this.minecraft.setScreen(new OptionsScreen(parent, OptionType.ANIMATION));
            }
        }));
        animation.active = optionType != OptionType.ANIMATION;

        Button etc = addRenderableWidget(new Button(width / 2 + 32, height / 6 + 140, 60, 20,
                Component.translatable("option.detailarmorbar.title.etc"), (matrixStack) -> {
            if (this.minecraft != null) {
                this.minecraft.setScreen(new OptionsScreen(parent, OptionType.ETC));
            }
        }));
        etc.active = optionType != OptionType.ETC;

        addRenderableWidget(new Button(width / 2 - 100, height / 6 + 168, 200, 20, CommonComponents.GUI_DONE, (matrixStack) -> {
            if (this.minecraft != null) {
                this.minecraft.setScreen(parent);
            }
        }));
    }

    @Override
    public void render(@Nonnull PoseStack matrices, int mouseX, int mouseY, float delta) {
        //buttonCount = 0;
        this.renderBackground(matrices);
        drawCenteredString(matrices, this.font, this.title, this.width / 2, 15, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
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

    private <T extends Enum<T>> List<Component> getEnumDescription(String type, T target) {
        ArrayList<Component> list = new ArrayList<>();
        for (String s : Component.translatable("context.detailarmorbar.effects." + type).getString().split("/"))
            list.add(Component.translatable(s).withStyle(ChatFormatting.YELLOW));

        list.add(Component.empty());

        List<T> v = EnumSet.allOf(target.getDeclaringClass()).stream().toList();
        for (T t : v) {
            list.add(Component.literal(" ")
                    .append(Component.translatable("option.detailarmorbar.effects."+type+"."+t.name().toLowerCase(Locale.ROOT)).withStyle(ChatFormatting.ITALIC))
                    .append(" - ")
                    .append(Component.translatable("context.detailarmorbar.effects."+type+"."+t.name().toLowerCase(Locale.ROOT))));
        }
        return list;
    }

    private MutableComponent getToggleName(String type, boolean target) {
        return Component.translatable("option.detailarmorbar.toggle."+type)
                .append(": ")
                .append(target ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF);
    }

    private  List<Component> getToggleDescription(String type) {
        ArrayList<Component> list = new ArrayList<>();
        for (String s : Component.translatable("context.detailarmorbar.toggle." + type).getString().split("/"))
            list.add(Component.translatable(s).withStyle(ChatFormatting.YELLOW));
        return list;
    }
}
