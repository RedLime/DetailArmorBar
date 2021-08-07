package com.redlimerl.detailab.config

import com.redlimerl.detailab.AnimationType
import com.redlimerl.detailab.DetailArmorBar
import com.redlimerl.detailab.EffectSpeedType
import com.redlimerl.detailab.ProtectionEffectType
import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import me.shedaniel.clothconfig2.api.ConfigBuilder
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.TranslatableText
import java.util.*

@Suppress("unused")
class ModConfigImpl : ModMenuApi {

    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory { parent: Screen ->
            val config = DetailArmorBar.getConfig()
            val default = Config.Options.DEFAULT

            val builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(TranslatableText("options.title"))
                .setSavingRunnable {
                    config.save()
                }

            val toggles = builder.getOrCreateCategory(TranslatableText("option.title.detailarmorbar.toggles"))
            val effects = builder.getOrCreateCategory(TranslatableText("option.title.detailarmorbar.effects"))

            val entryBuilder = builder.entryBuilder()


            //Effect Type
            effects.addEntry(entryBuilder.startEnumSelector(TranslatableText("option.detailarmorbar.effects.effect_type"), ProtectionEffectType::class.java, config.options?.effectType)
                .setSaveConsumer {
                    config.options?.effectType = it
                }
                .setDefaultValue(default.effectType)
                .setEnumNameProvider {
                    TranslatableText("option.detailarmorbar.effects.effect_type." + it.name.lowercase(Locale.ROOT))
                }
                .build())

            //Effect Speed
            effects.addEntry(entryBuilder.startEnumSelector(TranslatableText("option.detailarmorbar.effects.effect_speed"), EffectSpeedType::class.java, config.options?.effectSpeed)
                .setSaveConsumer {
                    config.options?.effectSpeed = it
                }
                .setDefaultValue(default.effectSpeed)
                .setEnumNameProvider {
                    TranslatableText("option.detailarmorbar.effects.effect_speed." + it.name.lowercase(Locale.ROOT))
                }
                .build())

            //Thorn Animation Type
            effects.addEntry(entryBuilder.startEnumSelector(TranslatableText("option.detailarmorbar.effects.thorn"), AnimationType::class.java, config.options?.effectThorn)
                .setSaveConsumer {
                    config.options?.effectThorn = it
                }
                .setDefaultValue(default.effectThorn)
                .setEnumNameProvider {
                    TranslatableText("option.detailarmorbar.effects.thorn." + it.name.lowercase(Locale.ROOT))
                }
                .build())

            //Enchantments
            toggles.addEntry(entryBuilder.startBooleanToggle(TranslatableText("option.detailarmorbar.toggle.enchantments"), config.options?.toggleEnchants ?: true)
                .setDefaultValue(default.toggleEnchants)
                .setYesNoTextSupplier {
                    TranslatableText("option.detailarmorbar.${if (it) "enable" else "disable"}")
                }
                .setSaveConsumer {
                    config.options?.toggleEnchants = it
                }
                .build())

            //Armor Types
            toggles.addEntry(entryBuilder.startBooleanToggle(TranslatableText("option.detailarmorbar.toggle.armor_types"), config.options?.toggleArmorTypes ?: true)
                .setDefaultValue(default.toggleArmorTypes)
                .setYesNoTextSupplier {
                    TranslatableText("option.detailarmorbar.${if (it) "enable" else "disable"}")
                }
                .setSaveConsumer {
                    config.options?.toggleArmorTypes = it
                }
                .build())

            //Netherites
            toggles.addEntry(entryBuilder.startBooleanToggle(TranslatableText("option.detailarmorbar.toggle.netherites"), config.options?.toggleNetherites ?: true)
                .setDefaultValue(default.toggleNetherites)
                .setYesNoTextSupplier {
                    TranslatableText("option.detailarmorbar.${if (it) "enable" else "disable"}")
                }
                .setSaveConsumer {
                    config.options?.toggleNetherites = it
                }
                .build())

            //Thorns
            toggles.addEntry(entryBuilder.startBooleanToggle(TranslatableText("option.detailarmorbar.toggle.thorns"), config.options?.toggleThorns ?: true)
                .setDefaultValue(default.toggleThorns)
                .setYesNoTextSupplier {
                    TranslatableText("option.detailarmorbar.${if (it) "enable" else "disable"}")
                }
                .setSaveConsumer {
                    config.options?.toggleThorns = it
                }
                .build())

            //Durability
            toggles.addEntry(entryBuilder.startBooleanToggle(TranslatableText("option.detailarmorbar.toggle.durability"), config.options?.toggleDurability ?: true)
                .setDefaultValue(default.toggleDurability)
                .setYesNoTextSupplier {
                    TranslatableText("option.detailarmorbar.${if (it) "enable" else "disable"}")
                }
                .setSaveConsumer {
                    config.options?.toggleDurability = it
                }
                .build())

            //Mending
            toggles.addEntry(entryBuilder.startBooleanToggle(TranslatableText("option.detailarmorbar.toggle.mending"), config.options?.toggleMending ?: true)
                .setDefaultValue(default.toggleMending)
                .setYesNoTextSupplier {
                    TranslatableText("option.detailarmorbar.${if (it) "enable" else "disable"}")
                }
                .setSaveConsumer {
                    config.options?.toggleMending = it
                }
                .build())

            return@ConfigScreenFactory builder.build()
        }
    }
}