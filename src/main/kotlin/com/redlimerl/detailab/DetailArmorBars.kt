package com.redlimerl.detailab

import com.redlimerl.detailab.config.Config
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.loader.api.FabricLoader
import org.apache.logging.log4j.LogManager
import java.io.File
import java.nio.file.Path


@Suppress("UNUSED")
object DetailArmorBars: ClientModInitializer {

    val LOGGER = LogManager.getLogger("DetailArmorBar")
    const val MOD_ID = "detailab"

    private var config: Config? = null

    fun getConfig(): Config {
        if (config == null) {
            loadConfig();
        }
        return config!!
    }

    private fun loadConfig() {
        val configPath: Path = FabricLoader.getInstance().configDir
        val configFile = File(configPath.toFile(), "detailarmorbar.json")
        config = Config(configFile)
        config!!.load()
    }

    override fun onInitializeClient() {
    }
}