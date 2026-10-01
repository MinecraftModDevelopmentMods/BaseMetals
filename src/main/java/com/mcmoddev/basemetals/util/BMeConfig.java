package com.mcmoddev.basemetals.util;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.content.ContentMode;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.util.Config;
import com.mcmoddev.lib.util.MaterialConfigOptions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Loads BaseMetals.cfg and keeps the content mode fixed until restart. */
public final class BMeConfig extends Config {

	private static final Logger LOGGER = LogManager.getFormatterLogger("basemetals");
	private static Configuration configuration;
	private static volatile ContentMode activeContentMode = ContentMode.HIGH_FANTASY;
	private static boolean contentModeLatched;
	private static final String CONFIG_FILE = "config/BaseMetals.cfg";
	public static final String CONTENT_MODE_PROPERTY = "contentMode";
	public static final String GENERAL_CAT = "General";
	public static final String MATERIALS_CAT = "Metals";
	public static final String VANILLA_CAT = "Vanilla";
	public static final String FLUIDS_CAT = "Fluids";
	public static final String FORCED_TRAITS_CAT = "Forced Traits";
	public static final List<String> GUI_CATEGORIES = Collections.unmodifiableList(Arrays.asList(
			GENERAL_CAT, MATERIALS_CAT, VANILLA_CAT, FLUIDS_CAT, FORCED_TRAITS_CAT));

	private static final Map<String, String> GENERATED_ORE_RULES;
	static {
		final Map<String, String> rules = new LinkedHashMap<>();

		rules.put(MaterialNames.COLDIRON, "basemetals:legacy/coldiron_ore");
		rules.put(MaterialNames.ADAMANTINE, "basemetals:legacy/adamantine_ore");
		rules.put(MaterialNames.STARSTEEL, "basemetals:legacy/starsteel_ore");
		rules.put(MaterialNames.COPPER, "basemetals:legacy/copper_ore");
		rules.put(MaterialNames.SILVER, "basemetals:legacy/silver_ore");
		rules.put(MaterialNames.TIN, "basemetals:legacy/tin_ore");
		rules.put(MaterialNames.LEAD, "basemetals:legacy/lead_ore");
		rules.put(MaterialNames.ZINC, "basemetals:legacy/zinc_ore");
		rules.put(MaterialNames.MERCURY, "basemetals:legacy/mercury_ore");
		rules.put(MaterialNames.NICKEL, "basemetals:legacy/nickel_ore");
		rules.put(MaterialNames.PLATINUM, "basemetals:legacy/platinum_ore");
		GENERATED_ORE_RULES = Collections.unmodifiableMap(rules);
	}

	private static final MaterialConfigOptions[] MATERIAL_CONFIG_OPTIONS = new MaterialConfigOptions[]{
			// Base Metals material switches.
			new MaterialConfigOptions(MaterialNames.ADAMANTINE, false, true, true, true, true),
			new MaterialConfigOptions(MaterialNames.ANTIMONY, false, true, true, true, true),
			new MaterialConfigOptions(MaterialNames.AQUARIUM, false, true, true, true, true),
			new MaterialConfigOptions(MaterialNames.BISMUTH, false, true, true, true, true),
			new MaterialConfigOptions(MaterialNames.BRASS, false, true, true, true, true),
			new MaterialConfigOptions(MaterialNames.BRONZE, false, true, true, true, true),
			new MaterialConfigOptions(MaterialNames.COLDIRON, false, true, true, true, true),
			new MaterialConfigOptions(MaterialNames.COPPER, false, true, true, true, true),
			new MaterialConfigOptions(MaterialNames.CUPRONICKEL, false, true, true, true, true),
			new MaterialConfigOptions(MaterialNames.ELECTRUM, false, true, true, true, true),
			new MaterialConfigOptions(MaterialNames.INVAR, false, true, true, true, true),
			new MaterialConfigOptions(MaterialNames.LEAD, false, true, true, true, true),
			new MaterialConfigOptions(MaterialNames.MERCURY, false, true, true, true, true),
			new MaterialConfigOptions(MaterialNames.MITHRIL, false, true, true, true, true),
			new MaterialConfigOptions(MaterialNames.NICKEL, false, true, true, true, true),
			new MaterialConfigOptions(MaterialNames.PEWTER, false, true, true, true, true),
			new MaterialConfigOptions(MaterialNames.PLATINUM, false, true, true, true, true),
			new MaterialConfigOptions(MaterialNames.SILVER, false, true, true, true, true),
			new MaterialConfigOptions(MaterialNames.STARSTEEL, false, true, true, true, true),
			new MaterialConfigOptions(MaterialNames.STEEL, false, true, true, true, true),
			new MaterialConfigOptions(MaterialNames.TIN, false, true, true, true, true),
			new MaterialConfigOptions(MaterialNames.ZINC, false, true, true, true, true),

			// Load Vanilla Bits settings before MMDLib registers their items and blocks.
			new MaterialConfigOptions(MaterialNames.CHARCOAL, true, true, true, true),
			new MaterialConfigOptions(MaterialNames.COAL, true, true, true, true),
			new MaterialConfigOptions(MaterialNames.DIAMOND, true, true, true, true),
			new MaterialConfigOptions(MaterialNames.EMERALD, true, true, true, true),
			new MaterialConfigOptions(MaterialNames.GOLD, true, true, true, true),
			new MaterialConfigOptions(MaterialNames.IRON, true, true, true, true),
			new MaterialConfigOptions(MaterialNames.STONE, true, true, false, false),
			new MaterialConfigOptions(MaterialNames.WOOD, true, true, false, false),
			new MaterialConfigOptions(MaterialNames.ENDER, true, true, true, true),
			new MaterialConfigOptions(MaterialNames.QUARTZ, true, true, true, true),
			new MaterialConfigOptions(MaterialNames.OBSIDIAN, true, true, true, true),
			new MaterialConfigOptions(MaterialNames.LAPIS, true, true, false, false),
			new MaterialConfigOptions(MaterialNames.PRISMARINE, true, true, true, true),
			new MaterialConfigOptions(MaterialNames.REDSTONE, true, true, true, true),
	};

	/** Reloads values saved through Forge's configuration screen. */
	@SubscribeEvent
	public void onConfigChange(final ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(BaseMetals.MODID)) {
			init();
		}
	}

	/** Loads settings without changing the content mode of a running game. */
	public static void init() {
		if (configuration == null) {
			configuration = new Configuration(new File(CONFIG_FILE));
			MinecraftForge.EVENT_BUS.register(new BMeConfig());
		}

		populateConfiguration(configuration);

		if (!contentModeLatched) {
			activeContentMode = configuredContentMode(configuration);
			contentModeLatched = true;
		}

		if (configuration.hasChanged()) {
			configuration.save();
		}
	}

	static void populateConfiguration(final Configuration target) {
		Options.setEnableAchievements(target.getBoolean("achievements", GENERAL_CAT, true,
				"If false, Base Metals gameplay achievements will be disabled. Recipe-book unlocks are unaffected."));

		final Property achievements = target.getCategory(GENERAL_CAT).get("achievements");
		final Property contentMode = target.get(GENERAL_CAT, CONTENT_MODE_PROPERTY,
				ContentMode.HIGH_FANTASY.serializedName(),
				"Controls Base Metals acquisition: high_fantasy keeps every historical recipe; "
						+ "low_fantasy restricts implausible forms.",
				ContentMode.serializedNames());
		final String configuredValue = contentMode.getString();

		if (!ContentMode.isValidSerializedName(configuredValue)) {
			LOGGER.warn("Invalid Base Metals content mode '{}'; using '{}'.",
					configuredValue, ContentMode.HIGH_FANTASY.serializedName());
			contentMode.set(ContentMode.HIGH_FANTASY.serializedName());
		}

		configMaterialOptions(MATERIAL_CONFIG_OPTIONS, target);
		configureGuiMetadata(target, achievements, contentMode);
	}

	private static void configureGuiMetadata(final Configuration target,
			final Property achievements, final Property contentMode) {
		achievements.setLanguageKey("config.basemetals.option.achievements")
				.setRequiresMcRestart(true);
		contentMode.setLanguageKey("config.basemetals.option.content_mode")
				.setValidValues(ContentMode.serializedNames())
				.setValidValuesDisplay(ContentMode.translationKeys())
				.setRequiresMcRestart(true);

		for (final String categoryName : GUI_CATEGORIES) {
			final String languageSuffix = categoryName.toLowerCase(java.util.Locale.ROOT)
					.replace(' ', '_');

			target.getCategory(categoryName)
					.setLanguageKey("config.basemetals.category." + languageSuffix)
					.setRequiresMcRestart(true);
		}

		for (final MaterialConfigOptions options : MATERIAL_CONFIG_OPTIONS) {
			final String identifier = options.getIdentifier();
			final String propertySuffix = WordUtils.capitalizeFully(identifier);
			final String materialLanguageKey = "config.basemetals.material." + identifier;
			final String category = options.getVanilla() ? VANILLA_CAT : MATERIALS_CAT;

			decorate(target, category, "Enable" + propertySuffix, materialLanguageKey);

			if (options.getHasFluid()) {
				decorate(target, FLUIDS_CAT, "Enabled " + propertySuffix, materialLanguageKey);
			}

			if (options.getHasTraits()) {
				decorate(target, FORCED_TRAITS_CAT,
						"Force" + propertySuffix + "TraitRegistration", materialLanguageKey);
			}
		}
	}

	private static void decorate(final Configuration target, final String category,
			final String propertyName, final String languageKey) {
		final Property property = target.getCategory(category).get(propertyName);

		if (property == null) {
			throw new IllegalStateException("Missing Base Metals configuration property "
					+ category + "/" + propertyName);
		}

		property.setLanguageKey(languageKey).setRequiresMcRestart(true);
	}

	public static Configuration getConfiguration() {
		if (configuration == null) {
			throw new IllegalStateException("Base Metals configuration has not been initialized");
		}

		return configuration;
	}

	/** The mode chosen at startup, not a pending change in the config screen. */
	public static ContentMode getActiveContentMode() {
		return activeContentMode;
	}

	/** Old config files without a mode keep the High Fantasy default. */
	public static ContentMode configuredContentMode(final Configuration target) {
		if (target == null || !target.hasCategory(GENERAL_CAT)) {
			return ContentMode.HIGH_FANTASY;
		}

		final Property property = target.getCategory(GENERAL_CAT).get(CONTENT_MODE_PROPERTY);

		return property == null
				? ContentMode.HIGH_FANTASY
				: ContentMode.fromSerializedName(property.getString());
	}

	public static Map<String, String> getGeneratedOreRules() {
		return GENERATED_ORE_RULES;
	}

	public static String getMaterialPropertyName(final String identifier) {
		return "Enable" + WordUtils.capitalizeFully(identifier);
	}
}
