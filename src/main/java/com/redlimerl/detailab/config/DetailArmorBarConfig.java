package com.redlimerl.detailab.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.redlimerl.detailab.DetailArmorBar;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static com.redlimerl.detailab.ConfigEnumType.*;

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
                options = GSON.fromJson(Files.readString(file.toPath(), StandardCharsets.UTF_8), Options.class);
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
            Files.writeString(file.toPath(), GSON.toJson(options), StandardCharsets.UTF_8);
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

        boolean replaceInvalidOptions() {
            var invalid = false;
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
