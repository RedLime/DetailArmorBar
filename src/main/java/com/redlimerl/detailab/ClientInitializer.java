package com.redlimerl.detailab;

import com.redlimerl.detailab.api.DetailArmorBarAPI;
import com.redlimerl.detailab.api.render.ArmorBarRenderManager;
import com.redlimerl.detailab.api.render.ItemBarRenderManager;
import com.redlimerl.detailab.api.render.TextureOffset;
import com.redlimerl.detailab.config.DABForgeConfig;
import com.redlimerl.detailab.screen.OptionsScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.awt.*;

public class ClientInitializer {
    public final static ResourceLocation GUI_ARMOR_BAR = new ResourceLocation(DetailArmorBar.MOD_ID, "textures/armor_bar.png");
    private final static String[] compatibilityMods = { "healthoverlay" };

    public static void onClientSetup() {
        initializeModFeatures();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, DABForgeConfig.SPEC);
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((mc, screen) -> new OptionsScreen(screen)));
    }

    private static void initializeModFeatures() {
        TextureOffset outline = new TextureOffset(9, 0);
        TextureOffset outlineHalf = new TextureOffset(27, 0);

        DetailArmorBarAPI.customArmorBarBuilder().armor((ArmorItem) Items.NETHERITE_CHESTPLATE, (ArmorItem) Items.NETHERITE_HELMET, (ArmorItem) Items.NETHERITE_LEGGINGS, (ArmorItem) Items.NETHERITE_BOOTS)
                .render((ItemStack itemStack) ->
                        new ArmorBarRenderManager(GUI_ARMOR_BAR, 128, 128,
                                new TextureOffset(9, 9 + DetailArmorBar.isVanillaTexture()), new TextureOffset(0, 9 + DetailArmorBar.isVanillaTexture()), outline, outlineHalf)
                ).register();

        DetailArmorBarAPI.customArmorBarBuilder().armor((ArmorItem) Items.DIAMOND_HELMET, (ArmorItem) Items.DIAMOND_LEGGINGS, (ArmorItem) Items.DIAMOND_CHESTPLATE, (ArmorItem) Items.DIAMOND_BOOTS)
                .render((ItemStack itemStack) ->
                        new ArmorBarRenderManager(GUI_ARMOR_BAR, 128, 128,
                                new TextureOffset(27, 9 + DetailArmorBar.isVanillaTexture()), new TextureOffset(18, 9 + DetailArmorBar.isVanillaTexture()), outline, outlineHalf)
                ).register();

        DetailArmorBarAPI.customArmorBarBuilder().armor((ArmorItem) Items.TURTLE_HELMET)
                .render((ItemStack itemStack) ->
                        new ArmorBarRenderManager(GUI_ARMOR_BAR, 128, 128,
                                new TextureOffset(45, 9 + DetailArmorBar.isVanillaTexture()), new TextureOffset(36, 9 + DetailArmorBar.isVanillaTexture()), outline, outlineHalf)
                ).register();

        DetailArmorBarAPI.customArmorBarBuilder().armor((ArmorItem) Items.IRON_HELMET, (ArmorItem) Items.IRON_LEGGINGS, (ArmorItem) Items.IRON_CHESTPLATE, (ArmorItem) Items.IRON_BOOTS)
                .render((ItemStack itemStack) ->
                        new ArmorBarRenderManager(GUI_ARMOR_BAR, 128, 128,
                                new TextureOffset(63, 9 + DetailArmorBar.isVanillaTexture()), new TextureOffset(54, 9 + DetailArmorBar.isVanillaTexture()), outline, outlineHalf)
                ).register();

        DetailArmorBarAPI.customArmorBarBuilder().armor((ArmorItem) Items.CHAINMAIL_HELMET, (ArmorItem) Items.CHAINMAIL_LEGGINGS, (ArmorItem) Items.CHAINMAIL_CHESTPLATE, (ArmorItem) Items.CHAINMAIL_BOOTS)
                .render((ItemStack itemStack) ->
                        new ArmorBarRenderManager(GUI_ARMOR_BAR, 128, 128,
                                new TextureOffset(81, 9 + DetailArmorBar.isVanillaTexture()), new TextureOffset(72, 9 + DetailArmorBar.isVanillaTexture()), outline, outlineHalf)
                ).register();

        DetailArmorBarAPI.customArmorBarBuilder().armor((ArmorItem) Items.GOLDEN_HELMET, (ArmorItem) Items.GOLDEN_LEGGINGS, (ArmorItem) Items.GOLDEN_CHESTPLATE, (ArmorItem) Items.GOLDEN_BOOTS)
                .render((ItemStack itemStack) ->
                        new ArmorBarRenderManager(GUI_ARMOR_BAR, 128, 128,
                                new TextureOffset(99, 9 + DetailArmorBar.isVanillaTexture()), new TextureOffset(90, 9 + DetailArmorBar.isVanillaTexture()), outline, outlineHalf)
                ).register();

        DetailArmorBarAPI.customArmorBarBuilder().armor((ArmorItem) Items.LEATHER_HELMET, (ArmorItem) Items.LEATHER_LEGGINGS, (ArmorItem) Items.LEATHER_CHESTPLATE, (ArmorItem) Items.LEATHER_BOOTS)
                .render((ItemStack itemStack) -> {
                            var leatherArmor = ((DyeableLeatherItem) itemStack.getItem()).getColor(itemStack);
                            var color = new Color(leatherArmor);
                            return new ArmorBarRenderManager(GUI_ARMOR_BAR, 128, 128,
                                    new TextureOffset(117, 9 + DetailArmorBar.isVanillaTexture()), new TextureOffset(108, 9 + DetailArmorBar.isVanillaTexture()), outline, outlineHalf, color);
                        }
                ).register();

        DetailArmorBarAPI.customItemBarBuilder().item(Items.ELYTRA)
                .render((ItemStack itemStack) ->
                        new ItemBarRenderManager(GUI_ARMOR_BAR, 128, 128,
                                new TextureOffset(36, 0), new TextureOffset(54, 0), true)
                ).register();

        for (String compatibilityMod : compatibilityMods) {
            if (ModList.get().getModObjectById(compatibilityMod).isPresent()) {
                DetailArmorBar.getConfig().getOptions().toggleCompatibleHeartMod = true;
            }
        }
    }
}
