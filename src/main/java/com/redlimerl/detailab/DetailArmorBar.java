package com.redlimerl.detailab;

import com.redlimerl.detailab.config.DetailArmorBarConfig;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;


@Mod(DetailArmorBar.MOD_ID)
public class DetailArmorBar {

    public final static Logger LOGGER = LogManager.getLogger("DetailArmorBar");
    public final static String MOD_ID = "detailab";

    private static DetailArmorBarConfig config = null;

    public DetailArmorBar() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::clientSetup);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ClientInitializer.onClientSetup();
    }

    public static DetailArmorBarConfig getConfig() {
        if (config == null) loadConfig();
        return config;
    }

    public static long getTicks() {
        return System.currentTimeMillis()/50;
    }

    private static void loadConfig() {
        File configFile = new File("config/", "detailarmorbar.json");
        config = new DetailArmorBarConfig(configFile);
        config.load();
    }

    public static int isVanillaTexture() {
        return getConfig().getOptions().toggleVanillaTexture ? 45 : 0;
    }
}
