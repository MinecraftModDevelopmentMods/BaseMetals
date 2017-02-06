package com.mcmoddev.basemetals.util;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.AdditionalLootTables;
import com.mcmoddev.basemetals.data.DataConstants;
import com.mcmoddev.basemetals.registry.CrusherRecipeRegistry;

import net.minecraft.block.Block;
import net.minecraft.item.*;
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
import net.minecraftforge.oredict.OreDictionary;

/**
 * @author p455w0rd
 *
 */
public class Config {

	private static Configuration configuration;
	private static final String CONFIG_FILE = "config/BaseMetals.cfg";
	private static final String GENERAL_CAT = "General";
	private static final String INTEGRATION_CAT = "Mod Integration";
	private static final String MATERIALS_CAT = "Metals";
	private static final String VANILLA_CAT = "Vanilla";
	private static final String HAMMER_RECIPES_CAT = "Crack Hammer Recipies";
	private static final String ALT_CFG_PATH = "config/additional-loot-tables"; // + BaseMetals.MODID;
	private static final String ORESPAWN_CFG_PATH = "config/orespawn";
	private static List<String> USER_CRUSHER_RECIPES = new ArrayList<String>();

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

		//GENERAL
		// enablePotionRecipes = config.getBoolean("enable_potions", "options",
		// enablePotionRecipes,
		// "If true, then some metals can be used to brew potions.");

		//GENERAL
		Options.disableAllHammerRecipes = configuration.getBoolean("disable_crack_hammer", GENERAL_CAT, false, "If true, then the crack hammer cannot be crafted.");
		Options.enforceHardness = configuration.getBoolean("enforce_hardness", GENERAL_CAT, true, "If true, then the crack hammer cannot crush ingots into powders if that \n" + "crackhammer is not hard enough to crush the ingot's ore.");
		Options.strongHammers = configuration.getBoolean("strong_hammers", GENERAL_CAT, true, "If true, then the crack hammer can crush ingots/ores that a pickaxe of the same \n" + "material can harvest. If false, then your crack hammer must be made of a harder \n" + "material than the ore you are crushing.");
		Options.autodetectRecipes = configuration.getBoolean("automatic_recipes", GENERAL_CAT, true, "If true, then Base Metals will scan the Ore Dictionary to automatically add a \n" + "Crack Hammer recipe for every material that has an ore, dust, and ingot.");
		Options.requireMMDOreSpawn = configuration.getBoolean("using_orespawn", GENERAL_CAT, true, "If false, then Base Metals will not require DrCyano's Ore Spawn mod. \n" + "Set to false if using another mod to manually handle ore generation.");
		Options.enableAchievements = configuration.getBoolean("achievements", GENERAL_CAT, true, "If false, then Base Metals Achievements will be disabled (This is currently required if you disable any metals");

		// INTEGRATION
		Options.enableEnderIO = configuration.getBoolean("ender_io_integration", INTEGRATION_CAT, true, "If false, then Base Metals will not try and integrate with Ender IO");
		Options.enableIC2 = configuration.getBoolean("ic2_integration", INTEGRATION_CAT, true, "If false, then Base Metals will not try and integrate with IC2");
		Options.enableMekanism = configuration.getBoolean("mekanism_integration", INTEGRATION_CAT, true, "If false, then Base Metals will not try and integrate with Mekanism");
		Options.enableThaumcraft = configuration.getBoolean("thaumcraft_integration", INTEGRATION_CAT, true, "If false, then Base Metals will not try and integrate with Thaumcraft");
		Options.enableTinkersConstruct = configuration.getBoolean("tinkers_construct_integration", INTEGRATION_CAT, true, "If false, then Base Metals will not try and integrate with Tinkers Construct");
		Options.enableVeinminer = configuration.getBoolean("veinminer_integration", INTEGRATION_CAT, true, "If false, then Base Metals will not try and integrate with VeinMiner");

		// METALS
		Options.enableAdamantine = configuration.getBoolean("EnableAdamantine", MATERIALS_CAT, true, "Enable Adamantine Items and Materials");
		Options.enableAntimony = configuration.getBoolean("EnableAntimony", MATERIALS_CAT, true, "Enable Antimony Items and Materials");
		Options.enableAquarium = configuration.getBoolean("EnableAquarium", MATERIALS_CAT, true, "Enable Aquarium Items and Materials");
		Options.enableBismuth = configuration.getBoolean("EnableBismuth", MATERIALS_CAT, true, "Enable Bismuth Items and Materials");
		Options.enableBrass = configuration.getBoolean("EnableBrass", MATERIALS_CAT, true, "Enable Brass Items and Materials");
		Options.enableBronze = configuration.getBoolean("EnableBronze", MATERIALS_CAT, true, "Enable Bronze Items and Materials");
		Options.enableCharcoal = configuration.getBoolean("EnableCharcoal", MATERIALS_CAT, true, "Enable Charcoal Items and Materials");
		Options.enableCoal = configuration.getBoolean("EnableCoal", MATERIALS_CAT, true, "Enable Coal Items and Materials");
		Options.enableColdIron = configuration.getBoolean("EnableColdIron", MATERIALS_CAT, true, "Enable ColdIron Items and Materials");
		Options.enableCopper = configuration.getBoolean("EnableCopper", MATERIALS_CAT, true, "Enable Copper Items and Materials");
		Options.enableCupronickel = configuration.getBoolean("EnableCupronickel", MATERIALS_CAT, true, "Enable Cupronickel Items and Materials");
		Options.enableElectrum = configuration.getBoolean("EnableElectrum", MATERIALS_CAT, true, "Enable Electrum Items and Materials");
		Options.enableInvar = configuration.getBoolean("EnableInvar", MATERIALS_CAT, true, "Enable Invar Items and Materials");
		Options.enableLead = configuration.getBoolean("EnableLead", MATERIALS_CAT, true, "Enable Lead Items and Materials");
		Options.enableMercury = configuration.getBoolean("EnableMercury", MATERIALS_CAT, true, "Enable Mercury Items and Materials");
		Options.enableMithril = configuration.getBoolean("EnableMitheril", MATERIALS_CAT, true, "Enable Mithril Items and Materials");
		Options.enableNickel = configuration.getBoolean("EnableNickel", MATERIALS_CAT, true, "Enable Nickel Items and Materials");
		Options.enablePewter = configuration.getBoolean("EnablePewter", MATERIALS_CAT, true, "Enable Pewter Items and Materials");
		Options.enablePlatinum = configuration.getBoolean("EnablePlatinum", MATERIALS_CAT, true, "Enable Platinum Items and Materials");
		Options.enableSilver = configuration.getBoolean("EnableSilver", MATERIALS_CAT, true, "Enable Silver Items and Materials");
		Options.enableStarSteel = configuration.getBoolean("EnableStarSteel", MATERIALS_CAT, true, "Enable StarSteel Items and Materials");
		Options.enableSteel = configuration.getBoolean("EnableSteel", MATERIALS_CAT, true, "Enable Steel Items and Materials");
		Options.enableTin = configuration.getBoolean("EnableTin", MATERIALS_CAT, true, "Enable Tin Items and Materials");
		Options.enableZinc = configuration.getBoolean("EnableZinc", MATERIALS_CAT, true, "Enable Zinc Items and Materials");

		//VANILLA
		Options.enableDiamond = configuration.getBoolean("EnableDiamond", VANILLA_CAT, true, "Enable Diamond Items and Materials");
		Options.enableGold = configuration.getBoolean("EnableGold", VANILLA_CAT, true, "Enable Gold Items and Materials");
		Options.enableIron = configuration.getBoolean("EnableIron", VANILLA_CAT, true, "Enable Iron Items and Materials");
		Options.enableStone = configuration.getBoolean("EnableStone", VANILLA_CAT, true, "Enable Stone Items and Materials");
		Options.enableWood = configuration.getBoolean("EnableWood", VANILLA_CAT, true, "Enable Wood Items and Materials");

		//CRACK HAMMER RECIPES
		final ConfigCategory userRecipeCat = configuration.getCategory(HAMMER_RECIPES_CAT);
		userRecipeCat.setComment("This section allows you to add your own recipes for the Crack Hammer (and other rock \n" + "crushers). Recipes are specified in semicolon (;) delimited lists of formulas in the \n" + "format modid:name#y->x*modid:name#y, where x is the number of items in a stack and y \n" + "is the metadata value. Note that both x and y are optional, so you can use the \n" + "formula modid:name->modid:name for most items/blocks. \n\n" + "All properties in this section will be parsed for formulas, regardless their name. \n" + "This lets you organize your recipe lists for easier reading.");
		// TODO: verify
//		if (userRecipeCat.keySet().size() == 0) {
		if (userRecipeCat.keySet().isEmpty()) {
			final Property prop = new Property("custom", "", Property.Type.STRING);
			prop.setComment("Example: minecraft:stained_glass#11->minecraft:dye#4; minecraft:wool->4*minecraft:string");
			userRecipeCat.put("custom", prop);
		}
		for (final Property p : userRecipeCat.values()) {
			final String[] recipes = p.getString().split(";");
			for (final String r : recipes) {
				final String recipe = r.trim();
				if (recipe.isEmpty()) {
					continue;
				}
				if (!(recipe.contains("->"))) {
					throw new IllegalArgumentException("Malformed hammer recipe expression '" + recipe + "'. Should be in format 'modid:itemname->modid:itemname'");
				}
				USER_CRUSHER_RECIPES.add(recipe);
			}
		}

		if (configuration.hasChanged()) {
			configuration.save();
		}

		if (Options.requireMMDOreSpawn) {
			if (!Loader.isModLoaded("orespawn")) {
				final HashSet<ArtifactVersion> orespawnMod = new HashSet<>();
				orespawnMod.add(new DefaultArtifactVersion("1.1.0"));
				throw new MissingModsException(orespawnMod, "orespawn", "MMD Ore Spawn Mod");
			}
			final Path oreSpawnFile = Paths.get(ORESPAWN_CFG_PATH, BaseMetals.MODID + ".json");
			if (!(oreSpawnFile.toFile().exists())) {
				try {
					Files.createDirectories(oreSpawnFile.getParent());
					Files.write(oreSpawnFile, Arrays.asList(DataConstants.DEFAULT_ORESPAWN_JSON.split("\n")), Charset.forName("UTF-8"));
				}
				catch (final IOException ex) {
					BaseMetals.logger.error("Failed to write file " + oreSpawnFile, ex);
//					FMLLog.log(Level.ERROR, ex, "%s: Failed to write file " + oreSpawnFile, Loader.instance().activeModContainer().getModId());
				}
			}
		}

		final Path myLootFolder = Paths.get(ALT_CFG_PATH, BaseMetals.MODID);
		if (!(myLootFolder.toFile().exists())) {
			try {
				final String chests = "chests";
				Files.createDirectories(myLootFolder.resolve(chests));
				Files.write(myLootFolder.resolve(chests).resolve("abandoned_mineshaft.json"), Collections.singletonList(AdditionalLootTables.ABANDONED_MINESHAFT));
				Files.write(myLootFolder.resolve(chests).resolve("desert_pyramid.json"), Collections.singletonList(AdditionalLootTables.DESERT_PYRAMID));
				Files.write(myLootFolder.resolve(chests).resolve("end_city_treasure.json"), Collections.singletonList(AdditionalLootTables.END_CITY_TREASURE));
				Files.write(myLootFolder.resolve(chests).resolve("jungle_temple.json"), Collections.singletonList(AdditionalLootTables.JUNGLE_TEMPLE));
				Files.write(myLootFolder.resolve(chests).resolve("nether_bridge.json"), Collections.singletonList(AdditionalLootTables.NETHER_BRIDGE));
				Files.write(myLootFolder.resolve(chests).resolve("simple_dungeon.json"), Collections.singletonList(AdditionalLootTables.SIMPLE_DUNGEON));
				Files.write(myLootFolder.resolve(chests).resolve("spawn_bonus_chest.json"), Collections.singletonList(AdditionalLootTables.SPAWN_BONUS_CHEST));
				Files.write(myLootFolder.resolve(chests).resolve("stronghold_corridor.json"), Collections.singletonList(AdditionalLootTables.STRONGHOLD_CORRIDOR));
				Files.write(myLootFolder.resolve(chests).resolve("stronghold_crossing.json"), Collections.singletonList(AdditionalLootTables.STRONGHOLD_CROSSING));
				Files.write(myLootFolder.resolve(chests).resolve("village_blacksmith.json"), Collections.singletonList(AdditionalLootTables.VILLAGE_BLACKSMITH));
			} catch (final IOException ex) {
				BaseMetals.logger.error("Failed to extract additional loot tables", ex);
//				FMLLog.log(Level.ERROR, ex, "%s: Failed to extract additional loot tables", Loader.instance().activeModContainer().getModId());
			}
		}
	}

	public static class Options {

		//GENERAL
		public static boolean disableAllHammerRecipes = false;
		public static boolean enforceHardness = true;
		public static boolean strongHammers = true;
		public static boolean autodetectRecipes = true;
		public static boolean requireMMDOreSpawn = true;
		public static boolean enableAchievements = true;
		// TODO: Add these to config file
		public static int gearQuantity = 4;
		public static int plateQuantity = 3;
		public static boolean furnaceCheese = true;
		public static boolean furnace1112 = true; // Overridden by FURNACE_CHEESE

		// INTEGRATION
		public static boolean enableEnderIO = true;
		public static boolean enableIC2 = true;
		public static boolean enableMekanism = true;
		public static boolean enableThaumcraft = true;
		public static boolean enableTinkersConstruct = true;
		public static boolean enableVeinminer = true;

		// MATERIALS
		public static boolean enableAdamantine = true;
		public static boolean enableAntimony = true;
		public static boolean enableAquarium = true;
		public static boolean enableBismuth = true;
		public static boolean enableBrass = true;
		public static boolean enableBronze = true;
		public static boolean enableCharcoal = true;
		public static boolean enableCoal = true;
		public static boolean enableColdIron = true;
		public static boolean enableCopper = true;
		public static boolean enableCupronickel = true;
		public static boolean enableElectrum = true;
		public static boolean enableInvar = true;
		public static boolean enableLead = true;
		public static boolean enableMercury = true;
		public static boolean enableMithril = true;
		public static boolean enableNickel = true;
		public static boolean enablePewter = true;
		public static boolean enablePlatinum = true;
		public static boolean enableSilver = true;
		public static boolean enableStarSteel = true;
		public static boolean enableSteel = true;
		public static boolean enableTin = true;
		public static boolean enableZinc = true;

		//VANILLA
		public static boolean enableDiamond = true;
		public static boolean enableGold = true;
		public static boolean enableIron = true;
		public static boolean enableStone = true;
		public static boolean enableWood = true;

		// THINGS
		public static boolean enableBasics = true; // Nugget, ingot, powder, blend, block, ore
		public static boolean enableBasicTools = true; // Axe, Hoe, Pickaxe, Shovel, Sword
		public static boolean enableCrossbowAndBolt = true; // crossbows, bolts
		public static boolean enableBowAndArrow = true; // Bows, Arrows
		public static boolean enableArmor = true; // Helmet, Chestplate, Leggings, Boots
		public static boolean enableCrackHammer = true;
		public static boolean enableFishingRod = true;
		public static boolean enableHorseArmor = true;
		public static boolean enableShears = true;
		public static boolean enableSmallDust = true; // Small powder, small blend
		public static boolean enableRod = true;
		public static boolean enableGear = true;
		public static boolean enableShield = true;

		public static boolean enableBars = true;
		public static boolean enablePlate = true;
		public static boolean enableDoor = true; // Also item
		public static boolean enableTrapdoor = true;
		public static boolean enableButton = true;
		public static boolean enableSlab = true; // Also item, double slab
		public static boolean enableLever = true;
		public static boolean enablePressurePlate = true;
		public static boolean enableStairs = true;
		public static boolean enableWall = true;

		private Options() {
			throw new IllegalAccessError("Not a instantiable class");
		}
	}

	public static void postInit() {
		for (final String recipe : USER_CRUSHER_RECIPES) {
			BaseMetals.logger.info("Adding custom crusher recipe '%s'", recipe);
//			FMLLog.info(BaseMetals.MODID + ": adding custom crusher recipe '" + recipe + "'");
			final int i = recipe.indexOf("->");
			final String inputStr = recipe.substring(0, i);
			final String outputStr = recipe.substring(i + 2, recipe.length());
			final ItemStack input = parseStringAsItemStack(inputStr, true);
			final ItemStack output = parseStringAsItemStack(outputStr, false);
			if ((input == null) || (output == null)) {
				BaseMetals.logger.error("Failed to add recipe formula '%s' because the blocks/items could not be found", recipe);
//				FMLLog.severe("Failed to add recipe formula '" + recipe + "' because the blocks/items could not be found");
			}
			else {
				CrusherRecipeRegistry.addNewCrusherRecipe(input, output);
			}
		}

		if (Options.autodetectRecipes) {
			// add recipe for every X where the Ore Dictionary has dustX, oreX,
			// and ingotX
			final Set<String> dictionary = new HashSet<>();
			dictionary.addAll(Arrays.asList(OreDictionary.getOreNames()));
			for (final String entry : dictionary) {
				if (entry.contains("Mercury")) {
					continue;
				}
				if (entry.startsWith("dust")) {
					final String X = entry.substring("dust".length());
					final String dustX = entry;
					final String ingotX = "ingot".concat(X);
					final String oreX = "ore".concat(X);
					if (dictionary.contains(oreX) && dictionary.contains(ingotX) && !OreDictionary.getOres(dustX).isEmpty()) {
						final ItemStack dustX1 = OreDictionary.getOres(dustX).get(0).copy();
						dustX1.stackSize = 1;
						final ItemStack dustX2 = dustX1.copy();
						dustX2.stackSize = 2;
						// recipe found
						// but is it already registered
						final List<ItemStack> oreBlocks = OreDictionary.getOres(oreX);
						boolean alreadyHasOreRecipe = true;
						for (final ItemStack i : oreBlocks) {
							alreadyHasOreRecipe = alreadyHasOreRecipe && (CrusherRecipeRegistry.getInstance().getRecipeForInputItem(i) != null);
						}
						final List<ItemStack> ingotStacks = OreDictionary.getOres(ingotX);
						boolean alreadyHasIngotRecipe = true;
						for (final ItemStack i : ingotStacks) {
							alreadyHasIngotRecipe = alreadyHasIngotRecipe && (CrusherRecipeRegistry.getInstance().getRecipeForInputItem(i) != null);
						}
						if (!alreadyHasOreRecipe) {
							BaseMetals.logger.info("Automatically adding custom crusher recipe '%s' -> %s", oreX, dustX2);
//							FMLLog.info(BaseMetals.MODID + ": automatically adding custom crusher recipe \"%s\" -> %s", oreX, dustX2);
							CrusherRecipeRegistry.addNewCrusherRecipe(oreX, dustX2);
						}
						if (!alreadyHasIngotRecipe) {
							BaseMetals.logger.info("Automatically adding custom crusher recipe '%s' -> %s", ingotX, dustX1);
//							FMLLog.info(BaseMetals.MODID + ": automatically adding custom crusher recipe \"%s\" -> %s", ingotX, dustX1);
							CrusherRecipeRegistry.addNewCrusherRecipe(ingotX, dustX1);
						}
					}
				}
			}
		}

		CrusherRecipeRegistry.getInstance().clearCache();
	}

	/**
	 * Parses a String in the format (stack-size)*(modid):(item/block
	 * name)#(metadata value). The stacksize and metadata value parameters are
	 * optional.
	 *
	 * @param str
	 *            A String describing an itemstack (e.g. "4*minecraft:dye#15" or
	 *            "minecraft:bow")
	 * @param allowWildcard
	 *            If true, then item strings that do not specify a metadata
	 *            value will use the OreDictionary wildcard value. If false,
	 *            then the default meta value is 0 instead.
	 * @return An ItemStack representing the item, or null if the item is not
	 *         found
	 */
	public static ItemStack parseStringAsItemStack(String str, boolean allowWildcard) {
		str = str.trim();
		int count = 1;
		int meta;
		if (allowWildcard) {
			meta = OreDictionary.WILDCARD_VALUE;
		}
		else {
			meta = 0;
		}
		int nameStart = 0;
		int nameEnd = str.length();
		if (str.contains("*")) {
			count = Integer.parseInt(str.substring(0, str.indexOf('*')).trim());
			nameStart = str.indexOf('*') + 1;
		}
		if (str.contains("#")) {
			meta = Integer.parseInt(str.substring(str.indexOf('#') + 1, str.length()).trim());
			nameEnd = str.indexOf('#');
		}
		final String id = str.substring(nameStart, nameEnd).trim();
		if (Block.getBlockFromName(id) != null) {
			// is a block
			return new ItemStack(Block.getBlockFromName(id), count, meta);
		}
		else if (Item.getByNameOrId(id) != null) {
			// is an item
			return new ItemStack(Item.getByNameOrId(id), count, meta);
		}
		else {
			// item not found
			BaseMetals.logger.info("Failed to find item or block for ID '%s'", id);
//			FMLLog.severe("Failed to find item or block for ID '" + id + "'");
			return null;
		}
	}
}
