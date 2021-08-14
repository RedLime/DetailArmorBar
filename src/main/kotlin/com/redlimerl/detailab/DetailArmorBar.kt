package com.redlimerl.detailab

import com.redlimerl.detailab.api.ArmorBarRenderManager
import com.redlimerl.detailab.api.DetailArmorBarAPI
import com.redlimerl.detailab.api.ItemBarRenderManager
import com.redlimerl.detailab.api.TextureOffset
import com.redlimerl.detailab.config.Config
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.item.Items
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File
import java.nio.file.Path


@Suppress("UNUSED")
object DetailArmorBar: ClientModInitializer {

    val LOGGER: Logger = LogManager.getLogger("DetailArmorBar")
    const val MOD_ID = "detailab"
    val GUI_ARMOR_BAR = Identifier(MOD_ID, "textures/armor_bar.png")

    private var config: Config? = null

    fun getConfig(): Config {
        if (config == null) loadConfig()
        return config!!
    }

    fun getTicks(): Long {
        return System.currentTimeMillis()/50
    }

    private fun loadConfig() {
        val configPath: Path = FabricLoader.getInstance().configDir
        val configFile = File(configPath.toFile(), "detailarmorbar.json")
        config = Config(configFile)
        config!!.load()
    }

    override fun onInitializeClient() {
        val outline = TextureOffset(9, 0)
        val outlineHalf = TextureOffset(27, 0)

        DetailArmorBarAPI.customArmorBarBuilder().armor(Items.NETHERITE_CHESTPLATE, Items.NETHERITE_HELMET, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS).render {
            ArmorBarRenderManager(GUI_ARMOR_BAR, 128, 128, TextureOffset(9, 9), TextureOffset(0, 9), outline, outlineHalf)
        }.register()
        DetailArmorBarAPI.customArmorBarBuilder().armor(Items.DIAMOND_HELMET, Items.DIAMOND_LEGGINGS, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_BOOTS).render {
            ArmorBarRenderManager(GUI_ARMOR_BAR, 128, 128, TextureOffset(27, 9), TextureOffset(18, 9), outline, outlineHalf)
        }.register()
        DetailArmorBarAPI.customArmorBarBuilder().armor(Items.TURTLE_HELMET).render {
            ArmorBarRenderManager(GUI_ARMOR_BAR, 128, 128, TextureOffset(45, 9), TextureOffset(36, 9), outline, outlineHalf)
        }.register()
        DetailArmorBarAPI.customArmorBarBuilder().armor(Items.IRON_HELMET, Items.IRON_LEGGINGS, Items.IRON_CHESTPLATE, Items.IRON_BOOTS).render {
            ArmorBarRenderManager(GUI_ARMOR_BAR, 128, 128, TextureOffset(63, 9), TextureOffset(54, 9), outline, outlineHalf)
        }.register()
        DetailArmorBarAPI.customArmorBarBuilder().armor(Items.CHAINMAIL_HELMET, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_BOOTS).render {
            ArmorBarRenderManager(GUI_ARMOR_BAR, 128, 128, TextureOffset(81, 9), TextureOffset(72, 9), outline, outlineHalf)
        }.register()
        DetailArmorBarAPI.customArmorBarBuilder().armor(Items.GOLDEN_HELMET, Items.GOLDEN_LEGGINGS, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_BOOTS).render {
            ArmorBarRenderManager(GUI_ARMOR_BAR, 128, 128, TextureOffset(99, 9), TextureOffset(90, 9), outline, outlineHalf)
        }.register()
        DetailArmorBarAPI.customArmorBarBuilder().armor(Items.LEATHER_HELMET, Items.LEATHER_LEGGINGS, Items.LEATHER_CHESTPLATE, Items.LEATHER_BOOTS).render {
            ArmorBarRenderManager(GUI_ARMOR_BAR, 128, 128, TextureOffset(117, 9), TextureOffset(108, 9), outline, outlineHalf)
        }.register()
        DetailArmorBarAPI.customItemBarBuilder().item(Items.ELYTRA).render {
            ItemBarRenderManager(GUI_ARMOR_BAR, 128, 128, TextureOffset(36, 0), TextureOffset(54, 0), true)
        }.register()
    }
}