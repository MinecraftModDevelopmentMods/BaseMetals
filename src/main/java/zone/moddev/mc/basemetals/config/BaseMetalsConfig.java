package zone.moddev.mc.basemetals.config;

import java.io.IOException;
import java.util.Map;

import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import zone.moddev.mc.basemetals.BaseMetals;

public final class BaseMetalsConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.BooleanValue SPECIAL_EFFECTS;
    public static final ModConfigSpec.BooleanValue STARSTEEL_REGENERATION;
    public static final ModConfigSpec.BooleanValue MERCURY_EFFECTS;
    public static final ModConfigSpec.BooleanValue VILLAGER_TRADES;
    public static final ModConfigSpec.ConfigValue<String> CONTENT_MODE;
    private static ContentMode activeMode = ContentMode.HIGH_FANTASY;
    private static boolean modeLoaded;
    private static ModConfig loadedConfig;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.comment("Base Metals gameplay settings. Existing items remain usable in either mode. Change ore generation in OreSpawn.");
        CONTENT_MODE = builder.comment("high_fantasy lets you craft freely with every metal.",
                        "low_fantasy limits what you can make from each material, with no Base Metals bows, crossbows or fishing rods.",
                        "Restart Minecraft after changing this setting. To join a server, use the same mode as the server.")
                .worldRestart().define("contentMode", "high_fantasy");
        SPECIAL_EFFECTS = builder.comment("Give certain metals their special armour and melee bonuses.")
                .define("specialEffects", true);
        STARSTEEL_REGENERATION = builder.comment("Starsteel equipment repairs itself while held: 1 durability every 10 seconds. Armour does not self-repair.")
                .define("starsteelRegeneration", true);
        MERCURY_EFFECTS = builder.comment("Touching liquid mercury can make you nauseous.")
                .define("mercuryImmersionEffects", true);
        VILLAGER_TRADES = builder.comment("Let blacksmiths sell Base Metals ingots and equipment.")
                .define("villagerTrades", true);
        SPEC = builder.build();
    }

    private BaseMetalsConfig() {}

    public static void onConfigLoading(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() != SPEC || modeLoaded) return;
        loadedConfig = event.getConfig();
        String value = CONTENT_MODE.get();
        if (!ContentMode.isValid(value)) {
            BaseMetals.LOGGER.warn("Unknown Base Metals content mode '{}'; using high_fantasy.", value);
            set(CONTENT_MODE, "high_fantasy");
            save();
        }

        activeMode = ContentMode.parse(value);
        modeLoaded = true;
    }

    public static ContentMode activeMode() {
        return activeMode;
    }

    public static void save() {
        requireLoadedConfig().save();
    }

    public static void saveEdits(ConfigEdits edits) {
        IConfigSpec.ILoadedConfig config = requireLoadedConfig();
        Map<String, Object> values = Map.of(
                "contentMode", edits.mode().serializedName(),
                "specialEffects", edits.option(0),
                "starsteelRegeneration", edits.option(1),
                "mercuryImmersionEffects", edits.option(2),
                "villagerTrades", edits.option(3));
        try {
            ConfigFileEdits.save(config.config(), loadedConfig.getFullPath(), values);
            // Loaded data is no longer a CommentedFileConfig. Mirror the
            // successfully saved edits in memory without writing the file again.
            values.forEach((key, value) -> config.config().set(key, value));
            SPEC.afterReload();
        } catch (IOException exception) {
            throw new IllegalStateException("Could not save Base Metals settings", exception);
        }
    }

    private static IConfigSpec.ILoadedConfig requireLoadedConfig() {
        if (loadedConfig == null || loadedConfig.getLoadedConfig() == null) {
            throw new IllegalStateException("Base Metals configuration has not loaded");
        }
        return loadedConfig.getLoadedConfig();
    }

    public static <T> void set(ModConfigSpec.ConfigValue<T> property, T value) {
        requireLoadedConfig();
        property.set(value);
    }
}
