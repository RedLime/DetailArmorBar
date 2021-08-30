package com.redlimerl.detailab;

import com.redlimerl.detailab.api.DetailArmorBarAPI;
import com.redlimerl.detailab.api.render.ArmorBarRenderManager;
import com.redlimerl.detailab.api.render.ItemBarRenderManager;
import com.redlimerl.detailab.api.render.TextureOffset;
import com.redlimerl.detailab.config.DetailArmorBarConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.File;
import java.nio.file.Path;

public class DetailArmorBar implements ClientModInitializer {

    public static Logger LOGGER = LogManager.getLogger("DetailArmorBar");
    public static String MOD_ID = "detailab";
    public static Identifier GUI_ARMOR_BAR = new Identifier(MOD_ID, "textures/armor_bar.png");

    private static DetailArmorBarConfig config = null;

    public static DetailArmorBarConfig getConfig() {
        if (config == null) loadConfig();
        return config;
    }

    public static long getTicks() {
        return System.currentTimeMillis()/50;
    }

    private static void loadConfig() {
        Path configPath = FabricLoader.getInstance().getConfigDir();
        File configFile = new File(configPath.toFile(), "detailarmorbar.json");
        config = new DetailArmorBarConfig(configFile);
        config.load();
    }

    public void onInitializeClient() {
        TextureOffset outline = new TextureOffset(9, 0);
        TextureOffset outlineHalf = new TextureOffset(27, 0);

        DetailArmorBarAPI.customArmorBarBuilder().armor((ArmorItem) Items.NETHERITE_CHESTPLATE, (ArmorItem) Items.NETHERITE_HELMET, (ArmorItem) Items.NETHERITE_LEGGINGS, (ArmorItem) Items.NETHERITE_BOOTS)
                .render((ItemStack itemStack) ->
            new ArmorBarRenderManager(GUI_ARMOR_BAR, 128, 128,
                    new TextureOffset(9, 9), new TextureOffset(0, 9), outline, outlineHalf)
        ).register();

        DetailArmorBarAPI.customArmorBarBuilder().armor((ArmorItem) Items.DIAMOND_HELMET, (ArmorItem) Items.DIAMOND_LEGGINGS, (ArmorItem) Items.DIAMOND_CHESTPLATE, (ArmorItem) Items.DIAMOND_BOOTS)
                .render((ItemStack itemStack) ->
            new ArmorBarRenderManager(GUI_ARMOR_BAR, 128, 128,
                    new TextureOffset(27, 9), new TextureOffset(18, 9), outline, outlineHalf)
        ).register();

        DetailArmorBarAPI.customArmorBarBuilder().armor((ArmorItem) Items.TURTLE_HELMET)
                .render((ItemStack itemStack) ->
            new ArmorBarRenderManager(GUI_ARMOR_BAR, 128, 128,
                    new TextureOffset(45, 9), new TextureOffset(36, 9), outline, outlineHalf)
        ).register();

        DetailArmorBarAPI.customArmorBarBuilder().armor((ArmorItem) Items.IRON_HELMET, (ArmorItem) Items.IRON_LEGGINGS, (ArmorItem) Items.IRON_CHESTPLATE, (ArmorItem) Items.IRON_BOOTS)
                .render((ItemStack itemStack) ->
            new ArmorBarRenderManager(GUI_ARMOR_BAR, 128, 128,
                    new TextureOffset(63, 9), new TextureOffset(54, 9), outline, outlineHalf)
        ).register();

        DetailArmorBarAPI.customArmorBarBuilder().armor((ArmorItem) Items.CHAINMAIL_HELMET, (ArmorItem) Items.CHAINMAIL_LEGGINGS, (ArmorItem) Items.CHAINMAIL_CHESTPLATE, (ArmorItem) Items.CHAINMAIL_BOOTS)
                .render((ItemStack itemStack) ->
            new ArmorBarRenderManager(GUI_ARMOR_BAR, 128, 128,
                    new TextureOffset(81, 9), new TextureOffset(72, 9), outline, outlineHalf)
        ).register();

        DetailArmorBarAPI.customArmorBarBuilder().armor((ArmorItem) Items.GOLDEN_HELMET, (ArmorItem) Items.GOLDEN_LEGGINGS, (ArmorItem) Items.GOLDEN_CHESTPLATE, (ArmorItem) Items.GOLDEN_BOOTS)
                .render((ItemStack itemStack) ->
            new ArmorBarRenderManager(GUI_ARMOR_BAR, 128, 128,
                    new TextureOffset(99, 9), new TextureOffset(90, 9), outline, outlineHalf)
        ).register();

        DetailArmorBarAPI.customArmorBarBuilder().armor((ArmorItem) Items.LEATHER_HELMET, (ArmorItem) Items.LEATHER_LEGGINGS, (ArmorItem) Items.LEATHER_CHESTPLATE, (ArmorItem) Items.LEATHER_BOOTS)
                .render((ItemStack itemStack) -> {
                    int leatherArmor = ((DyeableArmorItem) itemStack.getItem()).getColor(itemStack);
                    Color color = new Color(leatherArmor);
                    return new ArmorBarRenderManager(GUI_ARMOR_BAR, 128, 128,
                                    new TextureOffset(117, 9), new TextureOffset(108, 9), outline, outlineHalf, color);
                }
        ).register();

        DetailArmorBarAPI.customItemBarBuilder().item(Items.ELYTRA)
                .render((ItemStack itemStack) ->
            new ItemBarRenderManager(GUI_ARMOR_BAR, 128, 128,
                    new TextureOffset(36, 0), new TextureOffset(54, 0), true)
        ).register();
    }
}
