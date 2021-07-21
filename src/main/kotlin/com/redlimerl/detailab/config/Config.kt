package com.redlimerl.detailab.config

import com.google.gson.GsonBuilder
import com.redlimerl.detailab.DetailArmorBars
import com.redlimerl.detailab.ProtectionEffectType
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException


class Config(private val file: File) {
    var options: Options? = null
        private set

    fun load() {
        if (file.exists()) {
            try {
                FileReader(file).use { reader ->
                    options = GSON.fromJson(reader, Options::class.java)
                }
            } catch (e: IOException) {
                DetailArmorBars.LOGGER.error("Error loading config", e)
            }
            if (options != null) {
                if (options!!.replaceInvalidOptions(Options.DEFAULT)) {
                    save()
                }
            }
        }
        if (options == null) {
            options = Options()
            save()
        }
    }

    fun save() {
        try {
            FileWriter(file).use { writer -> writer.write(GSON.toJson(options)) }
        } catch (e: IOException) {
            DetailArmorBars.LOGGER.error("Error saving config", e)
        }
    }

    class Options {
        var effectType: ProtectionEffectType? = ProtectionEffectType.AURA
        fun replaceInvalidOptions(options: Options): Boolean {
            var invalid = false
            if (effectType == null) {
                effectType = options.effectType
                invalid = true
            }
            return invalid
        }

        companion object {
            val DEFAULT = Options()
        }
    }

    companion object {
        private val GSON = GsonBuilder()
            .setPrettyPrinting()
            .create()
    }

}