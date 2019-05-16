package com.mcmoddev.basemetals.util;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.data.VanillaMaterialNames;
import com.mcmoddev.lib.util.Config;
import com.mcmoddev.lib.util.MaterialConfigOptions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
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
		new MaterialConfigOptions(VanillaMaterialNames.CHARCOAL, true, true, true, true),
		new MaterialConfigOptions(VanillaMaterialNames.COAL, true, true, true,true),
		new MaterialConfigOptions(VanillaMaterialNames.DIAMOND,true, true, true, true),
		new MaterialConfigOptions(VanillaMaterialNames.EMERALD,true, true, true, true),
		new MaterialConfigOptions(VanillaMaterialNames.GOLD, true,true, true, true),
		new MaterialConfigOptions(VanillaMaterialNames.IRON, true,true, true, true),
		new MaterialConfigOptions(VanillaMaterialNames.STONE, true,true, false, false),
		new MaterialConfigOptions(VanillaMaterialNames.WOOD, true,true, false, false),
		new MaterialConfigOptions(VanillaMaterialNames.ENDER, true,true, true, true),
		new MaterialConfigOptions(VanillaMaterialNames.QUARTZ, true,true, true, true),
		new MaterialConfigOptions(VanillaMaterialNames.OBSIDIAN,true, true, true, true),
		new MaterialConfigOptions(VanillaMaterialNames.LAPIS, true,true, false, false),
		new MaterialConfigOptions(VanillaMaterialNames.PRISMARINE,true, true, true, true),
		new MaterialConfigOptions(VanillaMaterialNames.REDSTONE,true, true, true, true)
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
		Options.setEnableAchievements(configuration.getBoolean("achievements", GENERAL_CAT, true,
				"If false, then Base Metals Achievements will be disabled (This is currently required if you disable any metals"));
		
		configMaterialOptions(MATERIAL_CONFIG_OPTIONS, configuration);
		if (configuration.hasChanged()) {
			configuration.save();
		}
	}
}
