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
import slimeknights.tconstruct.tools.TinkerTraits;

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
		// Vanilla
		new MaterialConfigOptions(MaterialNames.CHARCOAL, false, true, true, true),
		new MaterialConfigOptions(MaterialNames.COAL,false, true, true,true),
		new MaterialConfigOptions(MaterialNames.DIAMOND,true, false, true, false),
		new MaterialConfigOptions(MaterialNames.EMERALD,true, false, true, false),
		new MaterialConfigOptions(MaterialNames.GOLD, true,false, true, false),
		new MaterialConfigOptions(MaterialNames.IRON, true,false, true, false),
		new MaterialConfigOptions(MaterialNames.STONE, true,true, false, false),
		new MaterialConfigOptions(MaterialNames.WOOD, true,true, false, false),
		new MaterialConfigOptions(MaterialNames.ENDER, true,true, false, false),
		new MaterialConfigOptions(MaterialNames.QUARTZ, true,true, false, false),
		new MaterialConfigOptions(MaterialNames.OBSIDIAN,true, true, true, false),
		new MaterialConfigOptions(MaterialNames.LAPIS, true,true, false, false),
		new MaterialConfigOptions(MaterialNames.PRISMARINE,true, true, true, true),
		new MaterialConfigOptions(MaterialNames.REDSTONE,true, true, true, false),
		// Base Metals
		new MaterialConfigOptions(MaterialNames.ADAMANTINE, false, true, true, true),
		new MaterialConfigOptions(MaterialNames.ANTIMONY, false, true, true, true),
		new MaterialConfigOptions(MaterialNames.AQUARIUM, false, true, true, true),
		new MaterialConfigOptions(MaterialNames.BISMUTH, false, true, true, true),
		new MaterialConfigOptions(MaterialNames.BRASS, false, true, true, true),
		new MaterialConfigOptions(MaterialNames.BRONZE, false, true, true, true),
		new MaterialConfigOptions(MaterialNames.COLDIRON, false, true, true, true),
		new MaterialConfigOptions(MaterialNames.COPPER, false, true, true, true),
		new MaterialConfigOptions(MaterialNames.CUPRONICKEL, false, true, true, true),
		new MaterialConfigOptions(MaterialNames.ELECTRUM, false, true, true, true),
		new MaterialConfigOptions(MaterialNames.INVAR, false, true, true, true),
		new MaterialConfigOptions(MaterialNames.LEAD, false, true, true, true),
		new MaterialConfigOptions(MaterialNames.MERCURY, false, true, true, true),
		new MaterialConfigOptions(MaterialNames.MITHRIL, false, true, true, true),
		new MaterialConfigOptions(MaterialNames.NICKEL, false, true, true, true),
		new MaterialConfigOptions(MaterialNames.PEWTER, false, true, true, true),
		new MaterialConfigOptions(MaterialNames.PLATINUM, false, true, true, true),
		new MaterialConfigOptions(MaterialNames.SILVER, false, true, true, true),
		new MaterialConfigOptions(MaterialNames.STARSTEEL, false, true, true, true),
		new MaterialConfigOptions(MaterialNames.STEEL, false, true, true, true),
		new MaterialConfigOptions(MaterialNames.TIN, false, true, true, true),
		new MaterialConfigOptions(MaterialNames.ZINC, false, true, true, true),
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
		Options.setDisableAllHammerRecipes(configuration.getBoolean("disable_crack_hammer",
				GENERAL_CAT, false, "If true, then the crack hammer cannot be crafted."));
		Options.setEnforceHardness(configuration.getBoolean("enforce_hardness", GENERAL_CAT, true,
				"If true, then the crack hammer cannot crush ingots into powders if that \n"
						+ "crackhammer is not hard enough to crush the ingot's ore."));
		Options.setStrongHammers(configuration.getBoolean("strong_hammers", GENERAL_CAT, true,
				"If true, then the crack hammer can crush ingots/ores that a pickaxe of the same \n"
						+ "material can harvest. If false, then your crack hammer must be made of a harder \n"
						+ "material than the ore you are crushing."));
		Options.setAutoDetectRecipes(configuration.getBoolean("automatic_recipes", GENERAL_CAT,
				true,
				"If true, then Base Metals will scan the Ore Dictionary to automatically add a \n"
						+ "Crack Hammer recipe for every material that has an ore, dust, and ingot."));
		Options.setRequireMMDOreSpawn(configuration.getBoolean("using_orespawn", GENERAL_CAT, true,
				"If false, then Base Metals will not require MMD Ore Spawn mod. \n"
						+ "Set to false if using another mod to manually handle ore generation."));
		Options.setEnableAchievements(configuration.getBoolean("achievements", GENERAL_CAT, true,
				"If false, then Base Metals Achievements will be disabled (This is currently required if you disable any metals"));
		Options.setCrackHammerFullStack(
				configuration.getBoolean("crackhammer_full_stacks", GENERAL_CAT, false,
						"If true then you can crackhammer full stacks of dropped items."));
		Options.setEnablePlateRepairs(configuration.getBoolean("repair_using_plates", GENERAL_CAT,
				true, "Repair shields and armor with metal plates of the same type"));
		Options.setEnableShieldUpgrades(configuration.getBoolean("upgrade_shields", GENERAL_CAT,
				true,
				"Upgrade a fully repaired shield to a material at least as hard as the shields current one using a plate of that material in the Anvil"));
		Options.setTwoDustDrop(configuration.getBoolean("hammer_produces_two_dust", GENERAL_CAT,
				true,
				"If true, then the crack hammer when crushing ingots/ores two dust will drop"));
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

		Config.configIntegrationOptions(INTEGRATION_CONFIG_OPTIONS, configuration);

		configMaterialOptions(MATERIAL_CONFIG_OPTIONS, configuration);

		// RECIPE AMOUNTS/TOOL&ITEM DISABLING
		Options.setGearQuantity(configuration.getInt("Gear Quantity", TOOLS_CAT, 4, 1, 64,
				"Number of Gears per recipe"));
		Options.setPlateQuantity(configuration.getInt("Plate Quantity", TOOLS_CAT, 3, 1, 64,
				"Number of Plates per recipe"));
		Options.setRodQuantity(configuration.getInt("Rod Quantity", TOOLS_CAT, 4, 1, 64,
				"Number of Rods per recipe"));
		Options.setFurnaceCheese(configuration.getBoolean("Furnace Cheese", TOOLS_CAT, true,
				"Melt down armor and tools for full value"));
		Options.setFurnace1112(configuration.getBoolean("Furnace1112", TOOLS_CAT, true,
				"Mimic 1.11.2 armor and tool melting.\n Overridden by Furnace Cheese"));

		Options.thingEnabled(ConfigKeys.BASICS, configuration.getBoolean("Enable Basics", TOOLS_CAT,
				true,
				"Set to false to disable: Nuggets, Ingots, Powders, Blends, Blocks and Ores"));
		Options.thingEnabled(ConfigKeys.BASIC_TOOLS, configuration.getBoolean("Enable Basic Tools",
				TOOLS_CAT, true, "Set to false to disable: Axe, Hoe, Pickaxe, Shovel and Sword"));
		Options.thingEnabled(ConfigKeys.BOW_AND_ARROW,
				configuration.getBoolean("Enable Bow and Arrow", TOOLS_CAT, true,
						"Set to false to turn off custom bows and arrows"));
		Options.thingEnabled(ConfigKeys.CROSSBOW_AND_BOLT,
				configuration.getBoolean("Enable Crossbow and Bolts", TOOLS_CAT, true,
						"Set to false to disable custom crossbows and bolts"));
		Options.thingEnabled(ConfigKeys.ARMOR, configuration.getBoolean("Enable Armor", TOOLS_CAT,
				true, "Set to false to disable: Helmet, Chestplate, Leggings and Boots"));
		Options.thingEnabled(ConfigKeys.CRACKHAMMER, configuration.getBoolean("Enable Crackhammer",
				TOOLS_CAT, true,
				"Why would you want to disable a valuable early-game tool ?\nThe question we're all asking though is will it blend?"));
		Options.thingEnabled(ConfigKeys.FISHING_ROD, configuration.getBoolean("Enable Fishing Rod",
				TOOLS_CAT, true, "Set to false to turn off added fishing rods"));
		Options.thingEnabled(ConfigKeys.HORSE_ARMOR, configuration.getBoolean("Enable Horse Armor",
				TOOLS_CAT, true, "Set to false to disable extra Horse Armor"));
		Options.thingEnabled(ConfigKeys.SHEARS, configuration.getBoolean("Enable Shears", TOOLS_CAT,
				true, "I love Shears, do you? If you're a Shears hater, set this to false"));
		Options.thingEnabled(ConfigKeys.SMALL_DUST, configuration.getBoolean("Enable Small Dust",
				TOOLS_CAT, true,
				"Turn this off to disable nugget-sized piles of dust.\n(Dust is a drug and drugs are bad. Don't do drugs, mmm-kay ?"));
		Options.thingEnabled(ConfigKeys.ROD, configuration.getBoolean("Enable Rod", TOOLS_CAT, true,
				"Spare the Rod... Wait, no, that's Biblical...\nThis controls whether or not rods of various materials (similar to Tinkers' Construct Tool Rod) sare available"));
		Options.thingEnabled(ConfigKeys.GEAR, configuration.getBoolean("Enable Gear", TOOLS_CAT,
				true,
				"A lot of mods have Gears, we can provide them. Turn this off if you think you don't need them."));
		Options.thingEnabled(ConfigKeys.SHIELD, configuration.getBoolean("Enable Shield", TOOLS_CAT,
				true,
				"The Combat Update brought Shields to Vanilla Minecraft. Turn this off if you don't want them to multiply"));
		Options.thingEnabled(ConfigKeys.BARS, configuration.getBoolean("Enable Bars", TOOLS_CAT,
				true, "No, not the kind you drink at. The kind you find on jail-cells."));
		Options.thingEnabled(ConfigKeys.PLATE, configuration.getBoolean("Enable Plates", TOOLS_CAT,
				true,
				"IC2, Tech Reborn and a number of other mods require plates of material. This provides recipes for them."));
		Options.thingEnabled(ConfigKeys.DOOR, configuration.getBoolean("Enable Door", TOOLS_CAT,
				true, "Doors of many wondrous materials"));
		Options.thingEnabled(ConfigKeys.TRAPDOOR, configuration.getBoolean("Enable Trapdoors",
				TOOLS_CAT, true, "Do you want trapdoors? Because that's how you get trapdoors!"));
		Options.thingEnabled(ConfigKeys.BUTTON, configuration.getBoolean("Enable Buttons",
				TOOLS_CAT, true, "Ooooh, what does this button do?"));
		Options.thingEnabled(ConfigKeys.SLAB, configuration.getBoolean("Enable Slab", TOOLS_CAT,
				true, "Slabs of all the materials that get added"));
		Options.thingEnabled(ConfigKeys.LEVER, configuration.getBoolean("Enable Lever", TOOLS_CAT,
				true, "Levers of all different materials - they even have different hardness's"));
		Options.thingEnabled(ConfigKeys.PRESSURE_PLATE,
				configuration.getBoolean("Enable Pressure-plates", TOOLS_CAT, true,
						"Now your traps can be hidden in even more places!"));
		Options.thingEnabled(ConfigKeys.STAIRS, configuration.getBoolean("Enable Stairs", TOOLS_CAT,
				true,
				"Stairs of our wonderful metals! Come and get your own - or don't. It's up to you."));
		Options.thingEnabled(ConfigKeys.WALL, configuration.getBoolean("Enable Wall", TOOLS_CAT,
				true, "Hey, Teachers! Leave those kids alone!"));
		Options.thingEnabled(ConfigKeys.EXPERIMENTAL, configuration.getBoolean(
				"Enable Experimental", TOOLS_CAT, false,
				"Enable experimental features (Don't blame us if they burn down your cat or kill your house)"));
		Options.setEnableModderSupportThings(configuration.getBoolean("Enable Mod Support", TOOLS_CAT, true, 
				"Enable some pieces to support other mods and integrating with them (currently just 'casings' and 'dense plates')"));
		// Add some utility bits that are referenced
		Options.thingEnabled(ConfigKeys.ANVIL, Options.isThingEnabled(ConfigKeys.EXPERIMENTAL));
		Options.thingEnabled(ConfigKeys.BOOKSHELF, Options.isThingEnabled(ConfigKeys.EXPERIMENTAL));
		Options.thingEnabled(ConfigKeys.FLOWERPOT, Options.isThingEnabled(ConfigKeys.EXPERIMENTAL));
		Options.thingEnabled(ConfigKeys.LADDER, Options.isThingEnabled(ConfigKeys.EXPERIMENTAL));
		Options.thingEnabled(ConfigKeys.TRIPWIRE_HOOK,
				Options.isThingEnabled(ConfigKeys.EXPERIMENTAL));
		Options.thingEnabled(ConfigKeys.SCYTHE,
				configuration.getBoolean("Enable Scythe", TOOLS_CAT, false, "Enable Scythe"));

		// DISABLE CRACK HAMMER RECIPES
		Options.setDisabledRecipes(parseDisabledRecipes(configuration.getString(
				"DisabledCrackhammerRecipes", GENERAL_CAT, "",
				"Disable the recipes by putting the input materials ore dictionary name ore registry name in this key.\nThe format is a semicolon (;) separate list of ore dictionary names (ie:  oreGold;oreIron;oreCopper - this would blacklist Gold, Iron and Copper ores from working")));

		// CRACK HAMMER RECIPES
		final ConfigCategory userRecipeCat = configuration.getCategory(HAMMER_RECIPES_CAT);

		userRecipeCat.setComment(
				"This section allows you to add your own recipes for the Crack Hammer (and other rock \n"
						+ "crushers). Recipes are specified in semicolon (;) delimited lists of formulas in the \n"
						+ "format modid:name#y->x*modid:name#y, where x is the number of items in a stack and y \n"
						+ "is the metadata value. Note that both x and y are optional, so you can use the \n"
						+ "formula modid:name->modid:name for most items/blocks. \n\n"
						+ "All properties in this section will be parsed for formulas, regardless their name. \n"
						+ "This lets you organize your recipe lists for easier reading.");
		if (userRecipeCat.keySet().isEmpty()) {
			final Property prop = new Property("custom", "", Property.Type.STRING);
			prop.setComment(
					"Example: minecraft:stained_glass#11->minecraft:dye#4; minecraft:wool->4*minecraft:string");
			userRecipeCat.put("custom", prop);
		}

		manageUserHammerRecipes(userRecipeCat.values());

		if (configuration.hasChanged()) {
			configuration.save();
		}
	}
}
