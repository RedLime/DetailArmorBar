package com.redlimerl.detailab.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.*;

import javax.annotation.Nonnull;
import java.util.*;

import static com.redlimerl.detailab.DetailArmorBar.getConfig;

public class OptionsScreen extends Screen {
    private final Screen parent;

    public OptionsScreen(Screen parent) {
        super(new TranslationTextComponent("options.title"));
        this.parent = parent;
    }

    private int buttonCount = 0;

    @Override
    protected void init() {
        addButton(new Button(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20,
                getEnumName("effect_type", getConfig().getOptions().effectType), (button) -> {
            getConfig().getOptions().effectType = getEnumNext(getConfig().getOptions().effectType);
            getConfig().save();
            button.setMessage(getEnumName("effect_type", getConfig().getOptions().effectType));
        }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, getEnumDescription("effect_type", getConfig().getOptions().effectType), mouseX, mouseY)));
        buttonCount++;

        addButton(new Button(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20,
                getEnumName("effect_speed", getConfig().getOptions().effectSpeed), (button) -> {
            getConfig().getOptions().effectSpeed = getEnumNext(getConfig().getOptions().effectSpeed);
            getConfig().save();
            button.setMessage(getEnumName("effect_speed", getConfig().getOptions().effectSpeed));
        }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, getEnumDescription("effect_speed", getConfig().getOptions().effectSpeed), mouseX, mouseY)));
        buttonCount++;

        addButton(new Button(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20,
                getEnumName("thorn", getConfig().getOptions().effectThorn), (button) -> {
            getConfig().getOptions().effectThorn = getEnumNext(getConfig().getOptions().effectThorn);
            getConfig().save();
            button.setMessage(getEnumName("thorn", getConfig().getOptions().effectThorn));
        }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, getEnumDescription("thorn", getConfig().getOptions().effectThorn), mouseX, mouseY)));
        buttonCount++;

        addButton(new Button(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20,
                getToggleName("enchantments", getConfig().getOptions().toggleEnchants), (button) -> {
            getConfig().getOptions().toggleEnchants = !getConfig().getOptions().toggleEnchants; getConfig().save();
            button.setMessage(getToggleName("enchantments", getConfig().getOptions().toggleEnchants));
        }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, getToggleDescription("enchantments"), mouseX, mouseY)));
        buttonCount++;

        addButton(new Button(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20,
                getToggleName("netherites", getConfig().getOptions().toggleNetherites), (button) -> {
            getConfig().getOptions().toggleNetherites = !getConfig().getOptions().toggleNetherites; getConfig().save();
            button.setMessage(getToggleName("netherites", getConfig().getOptions().toggleNetherites));
        }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, getToggleDescription("netherites"), mouseX, mouseY)));
        buttonCount++;

        addButton(new Button(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20,
                getToggleName("thorns", getConfig().getOptions().toggleThorns), (button) -> {
            getConfig().getOptions().toggleThorns = !getConfig().getOptions().toggleThorns; getConfig().save();
            button.setMessage(getToggleName("thorns", getConfig().getOptions().toggleThorns));
        }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, getToggleDescription("thorns"), mouseX, mouseY)));
        buttonCount++;

        addButton(new Button(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20,
                getToggleName("durability", getConfig().getOptions().toggleDurability), (button) -> {
            getConfig().getOptions().toggleDurability = !getConfig().getOptions().toggleDurability; getConfig().save();
            button.setMessage(getToggleName("durability", getConfig().getOptions().toggleDurability));
        }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, getToggleDescription("durability"), mouseX, mouseY)));
        buttonCount++;

        addButton(new Button(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20,
                getToggleName("mending", getConfig().getOptions().toggleMending), (button) -> {
            getConfig().getOptions().toggleMending = !getConfig().getOptions().toggleMending; getConfig().save();
            button.setMessage(getToggleName("mending", getConfig().getOptions().toggleMending));
        }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, getToggleDescription("mending"), mouseX, mouseY)));
        buttonCount++;

        addButton(new Button(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20,
                getToggleName("armor_types", getConfig().getOptions().toggleArmorTypes), (button) -> {
            getConfig().getOptions().toggleArmorTypes = !getConfig().getOptions().toggleArmorTypes; getConfig().save();
            button.setMessage(getToggleName("armor_types", getConfig().getOptions().toggleArmorTypes));
        }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, getToggleDescription("armor_types"), mouseX, mouseY)));
        buttonCount++;

        addButton(new Button(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20,
                getToggleName("item_types", getConfig().getOptions().toggleItemBar), (button) -> {
            getConfig().getOptions().toggleItemBar = !getConfig().getOptions().toggleItemBar; getConfig().save();
            button.setMessage(getToggleName("item_types", getConfig().getOptions().toggleItemBar));
        }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, getToggleDescription("item_types"), mouseX, mouseY)));
        buttonCount++;

        addButton(new Button(width / 2 - 155 + buttonCount % 2 * 160, height / 6 - 12 + 24 * (buttonCount / 2), 150, 20,
                getToggleName("empty_bar", getConfig().getOptions().toggleEmptyBar), (button) -> {
            getConfig().getOptions().toggleEmptyBar = !getConfig().getOptions().toggleEmptyBar; getConfig().save();
            button.setMessage(getToggleName("empty_bar", getConfig().getOptions().toggleEmptyBar));
        }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, getToggleDescription("empty_bar"), mouseX, mouseY)));
        buttonCount++;

        addButton(new Button(width / 2 - 100, height / 6 + 168, 200, 20, DialogTexts.GUI_DONE, (matrixStack) -> {
            if (this.minecraft != null) {
                this.minecraft.setScreen(parent);
            }
        }));
    }

    @Override
    public void render(@Nonnull MatrixStack matrices, int mouseX, int mouseY, float delta) {
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
        ArrayList<T> v = new ArrayList<>(EnumSet.allOf(target.getDeclaringClass()));
        return v.get((v.indexOf(target) + 1) % v.size());
    }

    private <T extends Enum<T>> ITextComponent getEnumName(String type, T target) {
        return new TranslationTextComponent("option.detailarmorbar.effects."+type)
                .append(": ")
                .append(new TranslationTextComponent("option.detailarmorbar.effects."+type+"."+target.name().toLowerCase(Locale.ROOT)));
    }

    private <T extends Enum<T>> List<IReorderingProcessor> getEnumDescription(String type, T target) {
        ArrayList<IReorderingProcessor> list = new ArrayList<>();
        for (String s : new TranslationTextComponent("context.detailarmorbar.effects." + type).getString().split("/"))
            list.add(new StringTextComponent(s).withStyle(TextFormatting.YELLOW).getVisualOrderText());

        list.add(new StringTextComponent("").getVisualOrderText());

        ArrayList<T> v = new ArrayList<>(EnumSet.allOf(target.getDeclaringClass()));
        for (T t : v) {
            list.add(new StringTextComponent(" ")
                    .append(new TranslationTextComponent("option.detailarmorbar.effects."+type+"."+t.name().toLowerCase(Locale.ROOT)).withStyle(TextFormatting.ITALIC))
                    .append(" - ")
                    .append(new TranslationTextComponent("context.detailarmorbar.effects."+type+"."+t.name().toLowerCase(Locale.ROOT))).getVisualOrderText());
        }
        return list;
    }

    private IFormattableTextComponent getToggleName(String type, boolean target) {
        return new TranslationTextComponent("option.detailarmorbar.toggle."+type)
                .append(": ")
                .append(target ? DialogTexts.OPTION_ON : DialogTexts.OPTION_OFF);
    }

    private  List<IReorderingProcessor> getToggleDescription(String type) {
        ArrayList<IReorderingProcessor> list = new ArrayList<>();
        for (String s : new TranslationTextComponent("context.detailarmorbar.toggle." + type).getString().split("/"))
            list.add(new StringTextComponent(s).withStyle(TextFormatting.YELLOW).getVisualOrderText());
        return list;
    }
}
