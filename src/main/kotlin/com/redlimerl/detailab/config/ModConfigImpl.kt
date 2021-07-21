package com.redlimerl.detailab.config

import com.redlimerl.detailab.DetailArmorBars
import com.redlimerl.detailab.ProtectionEffectType
import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.TranslatableText
import java.util.*

@Suppress("unused")
class ModConfigImpl : ModMenuApi {

    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory { parent: Screen ->
            val config = DetailArmorBars.getConfig()

            val builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(TranslatableText("options.title"))
                .setSavingRunnable {
                    config.save()
                }

            val general = builder.getOrCreateCategory(TranslatableText("stat.generalButton"))

            val entryBuilder = builder.entryBuilder()
            general.addEntry(entryBuilder.startEnumSelector(TranslatableText("option.detailarmorbar.effect_type"), ProtectionEffectType::class.java, config.options?.effectType)
                .setSaveConsumer {
                    config.options?.effectType = it
                }
                .setDefaultValue(Config.Options.DEFAULT.effectType)
                .setEnumNameProvider {
                    TranslatableText("option.detailarmorbar.effect_type." + it.name.lowercase(Locale.ROOT))
                }
                .build())

            return@ConfigScreenFactory builder.build()
        }
    }
}