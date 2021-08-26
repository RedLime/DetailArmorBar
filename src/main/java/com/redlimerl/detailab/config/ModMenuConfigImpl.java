//package com.redlimerl.detailab.config;
//
//import com.redlimerl.detailab.DetailArmorBar;
//import me.shedaniel.clothconfig2.api.ConfigBuilder;
//import me.shedaniel.clothconfig2.api.ConfigCategory;
//import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
//import net.minecraft.network.chat.TranslatableComponent;
//
//import java.util.Locale;
//
//import static com.redlimerl.detailab.ConfigEnumType.*;
//
//public class ModMenuConfigImpl  {
//
//    public static ConfigBuilder getOptionScreen() {
//        DetailArmorBarConfig config = DetailArmorBar.getConfig();
//        DetailArmorBarConfig.Options defaultConf = DetailArmorBarConfig.Options.DEFAULT;
//
//        ConfigBuilder builder = ConfigBuilder.create()
//                .setTitle(new TranslatableComponent("options.title"))
//                .setSavingRunnable(config::save);
//
//        ConfigCategory toggles = builder.getOrCreateCategory(new TranslatableComponent("option.title.detailarmorbar.toggles"));
//        ConfigCategory effects = builder.getOrCreateCategory(new TranslatableComponent("option.title.detailarmorbar.effects"));
//
//        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
//
//
//        //Effect Type
//        effects.addEntry(entryBuilder.startEnumSelector(new TranslatableComponent("option.detailarmorbar.effects.effect_type"), ProtectionEffect.class, config.getOptions().effectType)
//                .setSaveConsumer((type) -> config.getOptions().effectType = type)
//                .setDefaultValue(defaultConf.effectType)
//                .setEnumNameProvider((asEnum) -> new TranslatableComponent("option.detailarmorbar.effects.effect_type." + asEnum.name().toLowerCase(Locale.ROOT)))
//                .build());
//
//        //Effect Speed
//        effects.addEntry(entryBuilder.startEnumSelector(new TranslatableComponent("option.detailarmorbar.effects.effect_speed"), EffectSpeed.class, config.getOptions().effectSpeed)
//                .setSaveConsumer((type) -> config.getOptions().effectSpeed = type)
//                .setDefaultValue(defaultConf.effectSpeed)
//                .setEnumNameProvider((asEnum) -> new TranslatableComponent("option.detailarmorbar.effects.effect_speed." + asEnum.name().toLowerCase(Locale.ROOT)))
//                .build());
//
//        //Thorn Animation Type
//        effects.addEntry(entryBuilder.startEnumSelector(new TranslatableComponent("option.detailarmorbar.effects.thorn"), Animation.class, config.getOptions().effectThorn)
//                .setSaveConsumer((type) -> config.getOptions().effectThorn = type)
//                .setDefaultValue(defaultConf.effectThorn)
//                .setEnumNameProvider((asEnum) -> new TranslatableComponent("option.detailarmorbar.effects.thorn." + asEnum.name().toLowerCase(Locale.ROOT)))
//                .build());
//
//        //Enchantments
//        toggles.addEntry(entryBuilder.startBooleanToggle(new TranslatableComponent("option.detailarmorbar.toggle.enchantments"), config.getOptions().toggleEnchants)
//                .setDefaultValue(defaultConf.toggleEnchants)
//                .setYesNoTextSupplier((bool) -> new TranslatableComponent("option.detailarmorbar."+(bool ? "enable" : "disable")))
//                .setSaveConsumer((bool) -> config.getOptions().toggleEnchants = bool)
//                .build());
//
//        //Armor Types
//        toggles.addEntry(entryBuilder.startBooleanToggle(new TranslatableComponent("option.detailarmorbar.toggle.armor_types"), config.getOptions().toggleArmorTypes)
//                .setDefaultValue(defaultConf.toggleArmorTypes)
//                .setYesNoTextSupplier((bool) -> new TranslatableComponent("option.detailarmorbar."+(bool ? "enable" : "disable")))
//                .setSaveConsumer((bool) -> config.getOptions().toggleArmorTypes = bool)
//                .build());
//
//        //Netherites
//        toggles.addEntry(entryBuilder.startBooleanToggle(new TranslatableComponent("option.detailarmorbar.toggle.netherites"), config.getOptions().toggleNetherites)
//                .setDefaultValue(defaultConf.toggleNetherites)
//                .setYesNoTextSupplier((bool) -> new TranslatableComponent("option.detailarmorbar."+(bool ? "enable" : "disable")))
//                .setSaveConsumer((bool) -> config.getOptions().toggleNetherites = bool)
//                .build());
//
//        //Empty Bar
//        toggles.addEntry(entryBuilder.startBooleanToggle(new TranslatableComponent("option.detailarmorbar.toggle.empty_bar"), config.getOptions().toggleEmptyBar)
//                .setDefaultValue(defaultConf.toggleEmptyBar)
//                .setYesNoTextSupplier((bool) -> new TranslatableComponent("option.detailarmorbar."+(bool ? "enable" : "disable")))
//                .setSaveConsumer((bool) -> config.getOptions().toggleEmptyBar = bool)
//                .build());
//
//        //Item Bar
//        toggles.addEntry(entryBuilder.startBooleanToggle(new TranslatableComponent("option.detailarmorbar.toggle.item_types"), config.getOptions().toggleItemBar)
//                .setDefaultValue(defaultConf.toggleItemBar)
//                .setYesNoTextSupplier((bool) -> new TranslatableComponent("option.detailarmorbar."+(bool ? "enable" : "disable")))
//                .setSaveConsumer((bool) -> config.getOptions().toggleItemBar = bool)
//                .build());
//
//        //Thorns
//        toggles.addEntry(entryBuilder.startBooleanToggle(new TranslatableComponent("option.detailarmorbar.toggle.thorns"), config.getOptions().toggleThorns)
//                .setDefaultValue(defaultConf.toggleThorns)
//                .setYesNoTextSupplier((bool) -> new TranslatableComponent("option.detailarmorbar."+(bool ? "enable" : "disable")))
//                .setSaveConsumer((bool) -> config.getOptions().toggleThorns = bool)
//                .build());
//
//        //Durability
//        toggles.addEntry(entryBuilder.startBooleanToggle(new TranslatableComponent("option.detailarmorbar.toggle.durability"), config.getOptions().toggleDurability)
//                .setDefaultValue(defaultConf.toggleDurability)
//                .setYesNoTextSupplier((bool) -> new TranslatableComponent("option.detailarmorbar."+(bool ? "enable" : "disable")))
//                .setSaveConsumer((bool) -> config.getOptions().toggleDurability = bool)
//                .build());
//
//        //Mending
//        toggles.addEntry(entryBuilder.startBooleanToggle(new TranslatableComponent("option.detailarmorbar.toggle.mending"), config.getOptions().toggleMending)
//                .setDefaultValue(defaultConf.toggleMending)
//                .setYesNoTextSupplier((bool) -> new TranslatableComponent("option.detailarmorbar."+(bool ? "enable" : "disable")))
//                .setSaveConsumer((bool) -> config.getOptions().toggleMending = bool)
//                .build());
//
//        return builder;
//    }
//}
