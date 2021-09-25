package com.redlimerl.detailab.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.redlimerl.detailab.DetailArmorBar;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.redlimerl.detailab.config.ConfigEnumType.*;

public class DetailArmorBarConfig {

    private final File file;
    private Options options = null;
    private static final Gson GSON = new GsonBuilder()
                .setPrettyPrinting()
                .create();

    public DetailArmorBarConfig(File file) {
        this.file = file;
    }

    public Options getOptions() {
        return options;
    }

    public void load() {
        if (file.exists()) {
            try {
                options = GSON.fromJson(FileUtils.readFileToString(file, StandardCharsets.UTF_8), Options.class);
            } catch (IOException e) {
                DetailArmorBar.LOGGER.error("Error loading config", e);
            }

            if (options != null && options.replaceInvalidOptions()) {
                save();
            }
        }
        if (options == null) {
            options = new Options();
            save();
        }
    }

    public void save() {
        try {
            FileUtils.writeStringToFile(file, GSON.toJson(options), StandardCharsets.UTF_8);
        } catch (IOException e) {
            DetailArmorBar.LOGGER.error("Error saving config", e);
        }
    }

    public static class Options {
        public static final Options DEFAULT = new Options();
        public ProtectionEffect effectType = ProtectionEffect.AURA;
        public EffectSpeed effectSpeed = EffectSpeed.NORMAL;
        public Animation effectThorn = Animation.ANIMATION;

        public boolean toggleEnchants = true;
        public boolean toggleNetherites = true;
        public boolean toggleArmorTypes = true;
        public boolean toggleThorns = true;
        public boolean toggleDurability = true;
        public boolean toggleMending = true;
        public boolean toggleEmptyBar = true;
        public boolean toggleItemBar = true;
        public boolean toggleVanillaTexture = true;
        public boolean toggleCompatibleHeartMod = false;
        public boolean toggleInverseSlot = false;
        public boolean toggleSortSpecialItem = true;

        boolean replaceInvalidOptions() {
            boolean invalid = false;
            if (effectType == null) {
                effectType = Options.DEFAULT.effectType;
                invalid = true;
            }
            if (effectSpeed == null) {
                effectSpeed = Options.DEFAULT.effectSpeed;
                invalid = true;
            }
            if (effectThorn == null) {
                effectThorn = Options.DEFAULT.effectThorn;
                invalid = true;
            }
            return invalid;
        }
    }
}
