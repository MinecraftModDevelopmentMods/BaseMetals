package com.mcmoddev.basemetals.util;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.data.ConfigKeys;
import com.mcmoddev.lib.integration.plugins.*;
import com.mcmoddev.lib.util.Config;
import com.mcmoddev.lib.util.IntegrationConfigOptions;
import com.mcmoddev.lib.util.MaterialConfigOptions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

/**
 * @author Jasmine Iwanek
 *
 */
public final class BMeConfig extends Config {

	private static Configuration configuration;
	private static final String CONFIG_FILE = "config/BaseMetals.cfg";
	private static final String GENERAL_CAT = "General";
	private static final String HAMMER_RECIPES_CAT = "Crack Hammer Recipes";
	private static final String TOOLS_CAT = "Tools and Items";

	private static final MaterialConfigOptions[] MATERIAL_CONFIG_OPTIONS = new MaterialConfigOptions[]{
		// Base Metals
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
	};

	private static final IntegrationConfigOptions[] INTEGRATION_CONFIG_OPTIONS = new IntegrationConfigOptions[]{
		new IntegrationConfigOptions("Ender IO", EnderIO.PLUGIN_MODID, true),
		new IntegrationConfigOptions("IC2", IC2.PLUGIN_MODID, true),
		new IntegrationConfigOptions("Mekanism", Mekanism.PLUGIN_MODID, true),
		new IntegrationConfigOptions("Thaumcraft", Thaumcraft.PLUGIN_MODID, true),
		new IntegrationConfigOptions("Tinkers Construct", TinkersConstruct.PLUGIN_MODID, true),
		new IntegrationConfigOptions("Constructs Armory", ConstructsArmory.PLUGIN_MODID, true),
		new IntegrationConfigOptions("VeinMiner", VeinMiner.PLUGIN_MODID, true),
		new IntegrationConfigOptions("TAIGA", "taiga", true),
		new IntegrationConfigOptions("Dense Ores", DenseOres.PLUGIN_MODID, true),
		new IntegrationConfigOptions("Thermal Expansion", ThermalExpansion.PLUGIN_MODID, true)
	};

	/**
	 * Fired when the configuration changes.
	 *
	 * @param event The Event.
	 */
	@SubscribeEvent
	public void onConfigChange(final ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(BaseMetals.MODID)) {
			init();
		}
	}

	/**
	 *
	 */
	public static void init() {
		if (configuration == null) {
			configuration = new Configuration(new File(CONFIG_FILE));
			MinecraftForge.EVENT_BUS.register(new BMeConfig());
		}

		// GENERAL
		Options.setRequireMMDOreSpawn(configuration.getBoolean("using_orespawn", GENERAL_CAT, true,
				"If false, then Base Metals will not require MMD Ore Spawn mod. \n"
						+ "Set to false if using another mod to manually handle ore generation."));
		Options.setEnableAchievements(configuration.getBoolean("achievements", GENERAL_CAT, true,
				"If false, then Base Metals Achievements will be disabled (This is currently required if you disable any metals"));
		Options.thingEnabled(ConfigKeys.VILLAGER_TRADES, configuration.getBoolean("Enable Villager Trades", 
				GENERAL_CAT, true, "No, not the Village People, trades with Villagers.\n" + 
						"Basically... if you don't want BaseMetals (or other *Metals items and blocks) registered as trades with various villagers, set this to false"));
		// fallback orespawn can live as a 'thingEnabled' for a bit...
		Options.setFallbackOreSpawn(configuration.getBoolean("fallback_orespawn", GENERAL_CAT, true,
				"disable this and using_orespawn to make MMDLib based mods not spawn any ores"));
		Options.thingEnabled(ConfigKeys.IC2ITEMS_WITHOUT_PLUGIN, 
				configuration.getBoolean("Enable IC2 Items", GENERAL_CAT, false, 
						"Enable the items for IC2 support even if the IC2 plugin is disabled"));
		Options.thingEnabled(ConfigKeys.MEKITEMS_WITHOUT_PLUGIN, 
				configuration.getBoolean("Enable Mekanism Items", GENERAL_CAT, false, 
						"Enable the items for Mekanism support even if the Mekanism plugin is disabled"));

		configIntegrationOptions(INTEGRATION_CONFIG_OPTIONS, configuration);
		configMaterialOptions(MATERIAL_CONFIG_OPTIONS, configuration);
		if (configuration.hasChanged()) {
			configuration.save();
		}
	}
}
