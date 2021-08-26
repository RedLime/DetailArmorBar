package com.redlimerl.detailab.config;

import net.minecraftforge.common.ForgeConfigSpec;

import static com.redlimerl.detailab.ConfigEnumType.*;

public class DABForgeConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<ProtectionEffect> effectType;
    public static final ForgeConfigSpec.ConfigValue<EffectSpeed> effectSpeed;
    public static final ForgeConfigSpec.ConfigValue<Animation> effectThorn;

    public static final ForgeConfigSpec.ConfigValue<Boolean> toggleEnchants;
    public static final ForgeConfigSpec.ConfigValue<Boolean> toggleNetherites;
    public static final ForgeConfigSpec.ConfigValue<Boolean> toggleArmorTypes;
    public static final ForgeConfigSpec.ConfigValue<Boolean> toggleThorns;
    public static final ForgeConfigSpec.ConfigValue<Boolean> toggleDurability;
    public static final ForgeConfigSpec.ConfigValue<Boolean> toggleMending;
    public static final ForgeConfigSpec.ConfigValue<Boolean> toggleEmptyBar;
    public static final ForgeConfigSpec.ConfigValue<Boolean> toggleItemBar;

    static {
        BUILDER.push("Detail Armor Bar Settings");

        effectType = BUILDER.defineEnum("Protection Effect Type", ProtectionEffect.AURA);
        effectSpeed = BUILDER.defineEnum("Animation Speed", EffectSpeed.NORMAL);
        effectThorn = BUILDER.defineEnum("Thorn Animation Type", Animation.ANIMATION);

        toggleEnchants = BUILDER.define("Protection Effect", true);
        toggleNetherites = BUILDER.define("Netherite Armor Bar", true);
        toggleArmorTypes = BUILDER.define("Armor Type Bar", true);
        toggleThorns = BUILDER.define("Thorn Overlay", true);
        toggleDurability = BUILDER.define("Durability Warn Effect", true);
        toggleMending = BUILDER.define("Mending Effect", true);
        toggleEmptyBar = BUILDER.define("Show Empty Armor Bar", true);
        toggleItemBar = BUILDER.define("Show Special Item Bar (like Elytra)", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
