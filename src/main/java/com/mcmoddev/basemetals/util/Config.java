package com.mcmoddev.basemetals.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.AdditionalLootTables;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.util.ConfigBase;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.MissingModsException;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;

/**
 * @author Jasmine Iwanek
 *
 */
public class Config extends ConfigBase {

	private static Configuration configuration;
	private static final String CONFIG_FILE = "config/BaseMetals.cfg";
	private static final String GENERAL_CAT = "General";
	private static final String INTEGRATION_CAT = "Mod Integration";
	private static final String MATERIALS_CAT = "Metals";
	private static final String VANILLA_CAT = "Vanilla";
	private static final String HAMMER_RECIPES_CAT = "Crack Hammer Recipies";
	private static final String TOOLS_CAT = "Tools and Items";
	private static final String ALT_CFG_PATH = "config/additional-loot-tables";

	@SubscribeEvent
	public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent e) {
		if (e.getModID().equals(BaseMetals.MODID)) {
			init();
		}
	}

	public static void init() {
		if (configuration == null) {
			configuration = new Configuration(new File(CONFIG_FILE));
			MinecraftForge.EVENT_BUS.register(new Config());
		}

		// GENERAL
		Options.disableAllHammerRecipes = configuration.getBoolean("disable_crack_hammer", GENERAL_CAT, false,
				"If true, then the crack hammer cannot be crafted.");
		Options.enforceHardness = configuration.getBoolean("enforce_hardness", GENERAL_CAT, true,
				"If true, then the crack hammer cannot crush ingots into powders if that \n"
						+ "crackhammer is not hard enough to crush the ingot's ore.");
		Options.strongHammers = configuration.getBoolean("strong_hammers", GENERAL_CAT, true,
				"If true, then the crack hammer can crush ingots/ores that a pickaxe of the same \n"
						+ "material can harvest. If false, then your crack hammer must be made of a harder \n"
						+ "material than the ore you are crushing.");
		Options.autoDetectRecipes = configuration.getBoolean("automatic_recipes", GENERAL_CAT, true,
				"If true, then Base Metals will scan the Ore Dictionary to automatically add a \n"
						+ "Crack Hammer recipe for every material that has an ore, dust, and ingot.");
		Options.requireMMDOreSpawn = configuration.getBoolean("using_orespawn", GENERAL_CAT, true,
				"If false, then Base Metals will not require MMD Ore Spawn mod. \n"
						+ "Set to false if using another mod to manually handle ore generation.");
		Options.enableAchievements = configuration.getBoolean("achievements", GENERAL_CAT, true,
				"If false, then Base Metals Achievements will be disabled (This is currently required if you disable any metals");
		Options.crackHammerFullStack = configuration.getBoolean("crackhammer_full_stacks", GENERAL_CAT, false,
				"If true then you can crackhammer full stacks of dropped items.");
		Options.enablePlateRepairs = configuration.getBoolean("repair_using_plates", GENERAL_CAT, true,
				"Repair shields and armor with metal plates of the same type");
		Options.enableShieldUpgrades = configuration.getBoolean("upgrade_shields", GENERAL_CAT, true,
				"Upgrade a fully repaired shield to a material at least as hard as the shields current one using a plate of that material in the Anvil");

		// INTEGRATION
		Options.modEnabled("enderio", configuration.getBoolean("ender_io_integration", INTEGRATION_CAT, true,
				"If false, then Base Metals will not try and integrate with Ender IO"));
		Options.modEnabled("ic2", configuration.getBoolean("ic2_integration", INTEGRATION_CAT, true,
				"If false, then Base Metals will not try and integrate with IC2"));
		Options.modEnabled("mekanism", configuration.getBoolean("mekanism_integration", INTEGRATION_CAT, true,
				"If false, then Base Metals will not try and integrate with Mekanism"));
		Options.modEnabled("thaumcraft", configuration.getBoolean("thaumcraft_integration", INTEGRATION_CAT, true,
				"If false, then Base Metals will not try and integrate with Thaumcraft"));
		Options.modEnabled("tinkersconstruct", configuration.getBoolean("tinkers_construct_integration", INTEGRATION_CAT, true,
				"If false, then Base Metals will not try and integrate with Tinkers Construct"));
		Options.modEnabled("veinminer", configuration.getBoolean("veinminer_integration", INTEGRATION_CAT, true,
				"If false, then Base Metals will not try and integrate with VeinMiner"));
		Options.modEnabled("taiga", configuration.getBoolean("taiga_integration", INTEGRATION_CAT, true,
				"Requires that Tinkers' Construct integration also be on. If false, TAIGA provided materials and traits will not be available in Base Metals"));
		Options.modEnabled("denseores", configuration.getBoolean("denseores", INTEGRATION_CAT, true,
				"If DenseOres is available, this will allow automatic integration"));
		Options.modEnabled("thermalexpansion", configuration.getBoolean("thermal_expansion", INTEGRATION_CAT, true, 
				"If Thermal Expansion is available, this wil automatically integrate materials with the various machines"));

		// METALS
		Options.materialEnabled(MaterialNames.ADAMANTINE, configuration.getBoolean("EnableAdamantine", MATERIALS_CAT, true,
				"Enable Adamantine Items and Materials"));
		Options.materialEnabled(MaterialNames.ANTIMONY, configuration.getBoolean("EnableAntimony", MATERIALS_CAT, true,
				"Enable Antimony Items and Materials"));
		Options.materialEnabled(MaterialNames.AQUARIUM, configuration.getBoolean("EnableAquarium", MATERIALS_CAT, true,
				"Enable Aquarium Items and Materials"));
		Options.materialEnabled(MaterialNames.BISMUTH, configuration.getBoolean("EnableBismuth", MATERIALS_CAT, true,
				"Enable Bismuth Items and Materials"));
		Options.materialEnabled(MaterialNames.BRASS, configuration.getBoolean("EnableBrass", MATERIALS_CAT, true,
				"Enable Brass Items and Materials"));
		Options.materialEnabled(MaterialNames.BRONZE, configuration.getBoolean("EnableBronze", MATERIALS_CAT, true,
				"Enable Bronze Items and Materials"));
		Options.materialEnabled(MaterialNames.CHARCOAL, configuration.getBoolean("EnableCharcoal", MATERIALS_CAT, true,
				"Enable Charcoal Items and Materials"));
		Options.materialEnabled(MaterialNames.COAL, configuration.getBoolean("EnableCoal", MATERIALS_CAT, true,
				"Enable Coal Items and Materials"));
		Options.materialEnabled(MaterialNames.COLDIRON, configuration.getBoolean("EnableColdIron", MATERIALS_CAT, true,
				"Enable ColdIron Items and Materials"));
		Options.materialEnabled(MaterialNames.COPPER, configuration.getBoolean("EnableCopper", MATERIALS_CAT, true,
				"Enable Copper Items and Materials"));
		Options.materialEnabled(MaterialNames.CUPRONICKEL, configuration.getBoolean("EnableCupronickel", MATERIALS_CAT, true,
				"Enable Cupronickel Items and Materials"));
		Options.materialEnabled(MaterialNames.ELECTRUM, configuration.getBoolean("EnableElectrum", MATERIALS_CAT, true,
				"Enable Electrum Items and Materials"));
		Options.materialEnabled(MaterialNames.INVAR, configuration.getBoolean("EnableInvar", MATERIALS_CAT, true,
				"Enable Invar Items and Materials"));
		Options.materialEnabled(MaterialNames.LEAD, configuration.getBoolean("EnableLead", MATERIALS_CAT, true,
				"Enable Lead Items and Materials"));
		Options.materialEnabled(MaterialNames.MERCURY, configuration.getBoolean("EnableMercury", MATERIALS_CAT, true,
				"Enable Mercury Items and Materials"));
		Options.materialEnabled(MaterialNames.MITHRIL, configuration.getBoolean("EnableMithril", MATERIALS_CAT, true,
				"Enable Mithril Items and Materials"));
		Options.materialEnabled(MaterialNames.NICKEL, configuration.getBoolean("EnableNickel", MATERIALS_CAT, true,
				"Enable Nickel Items and Materials"));
		Options.materialEnabled(MaterialNames.PEWTER, configuration.getBoolean("EnablePewter", MATERIALS_CAT, true,
				"Enable Pewter Items and Materials"));
		Options.materialEnabled(MaterialNames.PLATINUM, configuration.getBoolean("EnablePlatinum", MATERIALS_CAT, true,
				"Enable Platinum Items and Materials"));
		Options.materialEnabled(MaterialNames.SILVER, configuration.getBoolean("EnableSilver", MATERIALS_CAT, true,
				"Enable Silver Items and Materials"));
		Options.materialEnabled(MaterialNames.STARSTEEL, configuration.getBoolean("EnableStarSteel", MATERIALS_CAT, true,
				"Enable StarSteel Items and Materials"));
		Options.materialEnabled(MaterialNames.STEEL, configuration.getBoolean("EnableSteel", MATERIALS_CAT, true,
				"Enable Steel Items and Materials"));
		Options.materialEnabled(MaterialNames.TIN, configuration.getBoolean("EnableTin", MATERIALS_CAT, true,
				"Enable Tin Items and Materials"));
		Options.materialEnabled(MaterialNames.ZINC, configuration.getBoolean("EnableZinc", MATERIALS_CAT, true,
				"Enable Zinc Items and Materials"));

		// VANILLA
		Options.materialEnabled(MaterialNames.DIAMOND, configuration.getBoolean("EnableDiamond", VANILLA_CAT, true,
				"Enable Diamond Items and Materials"));
		Options.materialEnabled(MaterialNames.EMERALD, configuration.getBoolean("EnableEmerald", VANILLA_CAT, true,
				"Enable Emerald Items and Materials"));
		Options.materialEnabled(MaterialNames.GOLD, configuration.getBoolean("EnableGold", VANILLA_CAT, true,
				"Enable Gold Items and Materials"));
		Options.materialEnabled(MaterialNames.IRON, configuration.getBoolean("EnableIron", VANILLA_CAT, true,
				"Enable Iron Items and Materials"));
		Options.materialEnabled(MaterialNames.STONE, configuration.getBoolean("EnableStone", VANILLA_CAT, true,
				"Enable Stone Items and Materials"));
		Options.materialEnabled(MaterialNames.WOOD, configuration.getBoolean("EnableWood", VANILLA_CAT, true,
				"Enable Wood Items and Materials"));
		Options.materialEnabled(MaterialNames.ENDER, configuration.getBoolean("EnableEnder", VANILLA_CAT, true,
				"Enable Ender Items and Materials (not currently in use)"));
		Options.materialEnabled(MaterialNames.QUARTZ, configuration.getBoolean("EnableQuartz", VANILLA_CAT, true,
				"Enable Nether Quartz Items and Materials (not currently in use)"));
		Options.materialEnabled(MaterialNames.OBSIDIAN, configuration.getBoolean("EnableObsidian", VANILLA_CAT, true,
				"Enable Obsidian Items and Materials (not currently in use)"));
		Options.materialEnabled(MaterialNames.LAPIS, configuration.getBoolean("EnableLapis", VANILLA_CAT, true,
				"Enable Lapis Lazuli Items and Materials (not currently in use)"));
		Options.materialEnabled(MaterialNames.PRISMARINE, configuration.getBoolean("EnablePrismarine", VANILLA_CAT, true,
				"Enable Prismarine Items and Materials (not currently in use)"));
		Options.materialEnabled(MaterialNames.REDSTONE, configuration.getBoolean("EnableRedstone", VANILLA_CAT, true,
				"Enable Redstone Items and Materials (not currently in use)"));

		// RECIPE AMOUNTS/TOOL&ITEM DISABLING
		Options.gearQuantity = configuration.getInt("Gear Quantity", TOOLS_CAT, 4, 1, 64, "Number of Gears per recipe");
		Options.plateQuantity = configuration.getInt("Plate Quantity", TOOLS_CAT, 3, 1, 64,
				"Number of Plates per recipe");
		Options.furnaceCheese = configuration.getBoolean("Furnace Cheese", TOOLS_CAT, true,
				"Melt down armor and tools for full value");
		Options.furnace1112 = configuration.getBoolean("Furnace1112", TOOLS_CAT, true,
				"Mimic 1.11.2 armor and tool melting.\n Overridden by Furnace Cheese");
		
		Options.thingEnabled("Basics", configuration.getBoolean("Enable Basics", TOOLS_CAT, true,
				"Set to false to disable: Nuggets, Ingots, Powders, Blends, Blocks and Ores"));
		Options.thingEnabled("BasicTools", configuration.getBoolean("Enable Basic Tools", TOOLS_CAT, true,
				"Set to false to disable: Axe, Hoe, Pickaxe, Shovel and Sword"));
		Options.thingEnabled("BowAndArrow", configuration.getBoolean("Enable Bow and Arrow", TOOLS_CAT, true,
				"Set to false to turn off custom bows and arrows"));
		Options.thingEnabled("CrossbowAndBolt", configuration.getBoolean("Enable Crossbow and Bolts", TOOLS_CAT, true,
				"Set to false to disable custom crossbows and bolts"));
		Options.thingEnabled("Armor", configuration.getBoolean("Enable Armor", TOOLS_CAT, true,
				"Set to false to disable: Helmet, Chestplate, Leggings and Boots"));
		Options.thingEnabled("CrackHammer", configuration.getBoolean("Enable Crackhammer", TOOLS_CAT, true,
				"Why would you want to disable a valuable early-game tool ?\nThe question we're all asking though is will it blend?"));
		Options.thingEnabled("FishingRod", configuration.getBoolean("Enable Fishing Rod", TOOLS_CAT, true,
				"Set to false to turn off added fishing rods"));
		Options.thingEnabled("HorseArmor", configuration.getBoolean("Enable Horse Armor", TOOLS_CAT, true,
				"Set to false to disable extra Horse Armor"));
		Options.thingEnabled("Shears", configuration.getBoolean("Enable Shears", TOOLS_CAT, true,
				"I love Shears, do you? If you're a Shears hater, set this to false"));
		Options.thingEnabled("SmallDust", configuration.getBoolean("Enable Small Dust", TOOLS_CAT, true,
				"Turn this off to disable nugget-sized piles of dust.\n(Dust is a drug and drugs are bad. Don't do drugs, mmm-kay ?"));
		Options.thingEnabled("Rod", configuration.getBoolean("Enable Rod", TOOLS_CAT, true,
				"Spare the Rod... Wait, no, that's Biblical...\nThis controls whether or not rods of various materials (similar to Tinkers' Construct Tool Rod) sare available"));
		Options.thingEnabled("Gear", configuration.getBoolean("Enable Gear", TOOLS_CAT, true,
				"A lot of mods have Gears, we can provide them. Turn this off if you think you don't need them."));
		Options.thingEnabled("Shield", configuration.getBoolean("Enable Shield", TOOLS_CAT, true,
				"The Combat Update brought Shields to Vanilla Minecraft. Turn this off if you don't want them to multiply"));
		Options.thingEnabled("Bars", configuration.getBoolean("Enable Bars", TOOLS_CAT, true,
				"No, not the kind you drink at. The kind you find on jail-cells."));
		Options.thingEnabled("Plate", configuration.getBoolean("Enable Plates", TOOLS_CAT, true,
				"IC2, Tech Reborn and a number of other mods require plates of material. This provides recipes for them."));
		Options.thingEnabled("Door", configuration.getBoolean("Enable Door", TOOLS_CAT, true,
				"Doors of many wondrous materials"));
		Options.thingEnabled("Trapdoor", configuration.getBoolean("Enable Trapdoors", TOOLS_CAT, true,
				"Do you want trapdoors? Because that's how you get trapdoors!"));
		Options.thingEnabled("Button", configuration.getBoolean("Enable Buttons", TOOLS_CAT, true,
				"Ooooh, what does this button do?"));
		Options.thingEnabled("Slab", configuration.getBoolean("Enable Slab", TOOLS_CAT, true,
				"Slabs of all the materials that get added"));
		Options.thingEnabled("Lever", configuration.getBoolean("Enable Lever", TOOLS_CAT, true,
				"Levers of all different materials - they even have different hardness's"));
		Options.thingEnabled("PressurePlate", configuration.getBoolean("Enable Pressure-plates", TOOLS_CAT, true,
				"Now your traps can be hidden in even more places!"));
		Options.thingEnabled("Stairs", configuration.getBoolean("Enable Stairs", TOOLS_CAT, true,
				"Stairs of our wonderful metals! Come and get your own - or don't. It's up to you."));
		Options.thingEnabled("Wall", configuration.getBoolean("Enable Wall", TOOLS_CAT, true,
				"Hey, Teachers! Leave those kids alone!"));
		final String experimental = "experimental";
		Options.thingEnabled(experimental, configuration.getBoolean("Enable Experimental", TOOLS_CAT, false,
				"Enable experimental features (Don't blame us if they burn down your cat or kill your house)"));
		// Add some utility bits that are referenced
		Options.thingEnabled("anvil", Options.isThingEnabled(experimental));
		Options.thingEnabled("bookshelf", Options.isThingEnabled(experimental));
		Options.thingEnabled("flowerpot", Options.isThingEnabled(experimental));
		Options.thingEnabled("ladder", Options.isThingEnabled(experimental));
		Options.thingEnabled("tripwire", Options.isThingEnabled(experimental));
		
		// DISABLE CRACK HAMMER RECIPES
		Options.disabledRecipes = parseDisabledRecipes(configuration.getString("DisabledCrackhammerRecipes", GENERAL_CAT, "",
				"Disable the recipes by putting the input materials ore dictionary name ore registry name in this key.\nThe format is a semicolon (;) separate list of ore dictionary names (ie:  oreGold;oreIron;oreCopper - this would blacklist Gold, Iron and Copper ores from working"));

		// CRACK HAMMER RECIPES
		final ConfigCategory userRecipeCat = configuration.getCategory(HAMMER_RECIPES_CAT);

		userRecipeCat
				.setComment("This section allows you to add your own recipes for the Crack Hammer (and other rock \n"
						+ "crushers). Recipes are specified in semicolon (;) delimited lists of formulas in the \n"
						+ "format modid:name#y->x*modid:name#y, where x is the number of items in a stack and y \n"
						+ "is the metadata value. Note that both x and y are optional, so you can use the \n"
						+ "formula modid:name->modid:name for most items/blocks. \n\n"
						+ "All properties in this section will be parsed for formulas, regardless their name. \n"
						+ "This lets you organize your recipe lists for easier reading.");
		if (userRecipeCat.keySet().isEmpty()) {
			final Property prop = new Property("custom", "", Property.Type.STRING);
			prop.setComment("Example: minecraft:stained_glass#11->minecraft:dye#4; minecraft:wool->4*minecraft:string");
			userRecipeCat.put("custom", prop);
		}

		manageUserHammerRecipes(userRecipeCat.values());
		
		if (configuration.hasChanged()) {
			configuration.save();
		}

		String orespawn = "orespawn";
		if ((Options.requireMMDOreSpawn()) && (!Loader.isModLoaded(orespawn))) {
			final HashSet<ArtifactVersion> orespawnMod = new HashSet<>();
			orespawnMod.add(new DefaultArtifactVersion("3.1.0"));
			throw new MissingModsException(orespawnMod, orespawn, "MMD Ore Spawn Mod");
		}

		final Path myLootFolder = Paths.get(ALT_CFG_PATH, BaseMetals.MODID);
		if (!(myLootFolder.toFile().exists())) {
			try {
				final String chests = "chests";
				Files.createDirectories(myLootFolder.resolve(chests));
				Files.write(myLootFolder.resolve(chests).resolve("abandoned_mineshaft.json"),
						Collections.singletonList(AdditionalLootTables.ABANDONED_MINESHAFT));
				Files.write(myLootFolder.resolve(chests).resolve("desert_pyramid.json"),
						Collections.singletonList(AdditionalLootTables.DESERT_PYRAMID));
				Files.write(myLootFolder.resolve(chests).resolve("end_city_treasure.json"),
						Collections.singletonList(AdditionalLootTables.END_CITY_TREASURE));
				Files.write(myLootFolder.resolve(chests).resolve("jungle_temple.json"),
						Collections.singletonList(AdditionalLootTables.JUNGLE_TEMPLE));
				Files.write(myLootFolder.resolve(chests).resolve("nether_bridge.json"),
						Collections.singletonList(AdditionalLootTables.NETHER_BRIDGE));
				Files.write(myLootFolder.resolve(chests).resolve("simple_dungeon.json"),
						Collections.singletonList(AdditionalLootTables.SIMPLE_DUNGEON));
				Files.write(myLootFolder.resolve(chests).resolve("spawn_bonus_chest.json"),
						Collections.singletonList(AdditionalLootTables.SPAWN_BONUS_CHEST));
				Files.write(myLootFolder.resolve(chests).resolve("stronghold_corridor.json"),
						Collections.singletonList(AdditionalLootTables.STRONGHOLD_CORRIDOR));
				Files.write(myLootFolder.resolve(chests).resolve("stronghold_crossing.json"),
						Collections.singletonList(AdditionalLootTables.STRONGHOLD_CROSSING));
				Files.write(myLootFolder.resolve(chests).resolve("village_blacksmith.json"),
						Collections.singletonList(AdditionalLootTables.VILLAGE_BLACKSMITH));
			} catch (final IOException ex) {
				BaseMetals.logger.error("Failed to extract additional loot tables", ex);
			}
		}
	}

	public static class Options {

		// GENERAL
		protected static boolean disableAllHammerRecipes = false;
		protected static boolean enforceHardness = true;
		protected static boolean strongHammers = true;
		protected static boolean autoDetectRecipes = true;
		protected static boolean requireMMDOreSpawn = true;
		protected static boolean enableAchievements = true;
		protected static boolean crackHammerFullStack = false;
		protected static boolean enableShieldUpgrades = true;
		protected static boolean enablePlateRepairs = true;
		protected static boolean enableModderSupportThings = true;

		public static boolean disableAllHammerRecipes() {
			return disableAllHammerRecipes;
		}

		public static boolean enforceHardness() {
			return enforceHardness;
		}

		public static boolean strongHammers() {
			return strongHammers;
		}

		public static boolean autoDetectRecipes() {
			return autoDetectRecipes;
		}

		public static boolean requireMMDOreSpawn() {
			return requireMMDOreSpawn;
		}

		public static boolean enableAchievements() {
			return enableAchievements;
		}

		public static boolean crackHammerFullStack() {
			return crackHammerFullStack;
		}

		public static boolean enableShieldUpgrades() {
			return enableShieldUpgrades;
		}

		public static boolean enablePlateRepairs() {
			return enablePlateRepairs;
		}

		public static boolean enableModderSupportThings() {
			return enableModderSupportThings;
		}

		private static String[] disabledRecipes = null;
		public static String[] disabledRecipes() {
			return disabledRecipes;
		}

		// RECIPE AMOUNTS
		protected static int gearQuantity = 4;

		public static int gearQuantity() {
			return gearQuantity;
		}

		protected static int plateQuantity = 3;

		public static int plateQuantity() {
			return plateQuantity;
		}

		protected static boolean furnaceCheese = true;
		public static boolean furnaceCheese() {
			return furnaceCheese;
		}

		protected static boolean furnace1112 = true; // Overridden by FURNACE_CHEESE
		public static boolean furnace1112() {
			return furnace1112;
		}

		// INTEGRATION
		private static final Map<String, Boolean> modEnabled = new HashMap<>();
		public static boolean isModEnabled(String modName) {
			String testName = modName.toLowerCase(Locale.ROOT);
			if( modEnabled.containsKey(testName) ) {
				return modEnabled.get(testName);
			}
			return false;
		}

		public static void modEnabled(String modName, Boolean bool) {
			if (!modEnabled.containsKey(modName)) {
				modEnabled.put(modName.toLowerCase(Locale.ROOT), bool);
			}
		}

		// MATERIALS
		private static final Map<String, Boolean> materialEnabled = new HashMap<>();
		public static boolean isMaterialEnabled(String name) {
			String testName = name.toLowerCase(Locale.ROOT);
			if( materialEnabled.containsKey(testName) ) {
				return materialEnabled.get(testName);
			}
			return false;
		}

		public static void materialEnabled(String materialName, Boolean bool) {
			if (!materialEnabled.containsKey(materialName)) {
				materialEnabled.put(materialName.toLowerCase(Locale.ROOT), bool);
			}
		}

		// THINGS
		private static final Map<String, Boolean> thingEnabled = new HashMap<>();
		public static boolean isThingEnabled(String name) {
			String testName = name.toLowerCase(Locale.ROOT);
			if( thingEnabled.containsKey(testName) ) {
				return thingEnabled.get(testName);
			}
			return false;
		}

		public static void thingEnabled(String name, Boolean bool) {
			if (thingEnabled.containsKey(name)) {
				thingEnabled.put(name.toLowerCase(Locale.ROOT), bool);
			}
		}

		
		private Options() {
			throw new IllegalAccessError("Not a instantiable class");
		}
	}
}
