package com.mcmoddev.lib.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.registry.CrusherRecipeRegistry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.oredict.OreDictionary;

public class ConfigBase {

	private static final List<String> UserCrusherRecipes = new ArrayList<>();

	// shut up SonarLint/SonarQube
	protected ConfigBase() {
		
	}
	
	protected static void manageUserHammerRecipes(Collection<Property> values) {
		for (final Property p : values) {
			final String[] recipes = p.getString().split(";");
			for (final String r : recipes) {
				final String recipe = r.trim();
				if (recipe.isEmpty()) {
					continue;
				}
				if (!(recipe.contains("->"))) {
					throw new IllegalArgumentException("Malformed hammer recipe expression '" + recipe + "'. Should be in format 'modid:itemname->modid:itemname'");
				}
				UserCrusherRecipes.add(recipe);
			}
		}
	}

	protected static String[] parseDisabledRecipes(String rawDisabledRecipes) {
		if (!rawDisabledRecipes.isEmpty() && rawDisabledRecipes.contains(";")) {
			return rawDisabledRecipes.split(";");
		} else {
			return new String[] { rawDisabledRecipes };
		}
	}

	public static void postInit() {
		addUserRecipes();

		if (Options.autoDetectRecipes()) {
			// add recipe for every X where the Ore Dictionary has dustX, oreX, and ingotX
			final Set<String> dictionary = new HashSet<>();
			dictionary.addAll(Arrays.asList(OreDictionary.getOreNames()).stream().filter( item -> !item.contains("Mercury") )
			.filter( item -> !item.contains("Redstone") ).filter( item -> item.startsWith("dust") )
			.filter( item -> {
				String ingotX = Oredicts.INGOT.concat(item.substring(4));
				String oreX = Oredicts.ORE.concat(item.substring(4));
				return (dictionary.contains(oreX) && dictionary.contains(ingotX) && !OreDictionary.getOres(item).isEmpty());
			}).collect(Collectors.<String>toSet()));

			addOreRecipes( dictionary );
			addIngotRecipes( dictionary );
		}
	}

	private static void addIngotRecipes(Set<String> dictionary) {
		dictionary.stream()
		.filter( entry -> {
			List<ItemStack> iS = OreDictionary.getOres(Oredicts.INGOT.concat(entry.substring(4)));
			for( ItemStack i : iS ) {
				if((CrusherRecipeRegistry.getRecipeForInputItem(i) != null )) {
					return true;
				}
			}
			return false;
		})
		.forEach( entry -> {
			String ingotX = Oredicts.INGOT.concat(entry.substring(4));
			ItemStack dustX = OreDictionary.getOres(entry).get(0).copy();
			dustX.setCount(2);
			BaseMetals.logger.info("Automatically adding custom crusher recipe '{}' -> {}", ingotX, dustX);
			CrusherRecipeRegistry.addNewCrusherRecipe(ingotX, dustX);				
		});			
	}

	private static void addOreRecipes(Set<String> dictionary) {
		dictionary.stream()
		.filter( entry -> {
			List<ItemStack> iS = OreDictionary.getOres(Oredicts.ORE.concat(entry.substring(4)));
			for( ItemStack i : iS ) {
				if((CrusherRecipeRegistry.getRecipeForInputItem(i) != null )) {
					return true;
				}
			}
			return false;
		})
		.forEach( entry -> {
			String oreX = Oredicts.ORE.concat(entry.substring(4));
			ItemStack dustX = OreDictionary.getOres(entry).get(0).copy();
			dustX.setCount(2);
			BaseMetals.logger.info("Automatically adding custom crusher recipe '{}' -> {}", oreX, dustX);
			CrusherRecipeRegistry.addNewCrusherRecipe(oreX, dustX);				
		});
	}

	private static void addUserRecipes() {
		for (final String recipe : UserCrusherRecipes) {
			BaseMetals.logger.info("Adding custom crusher recipe '%s'", recipe);
			final int i = recipe.indexOf("->");
			final String inputStr = recipe.substring(0, i);
			final String outputStr = recipe.substring(i + 2, recipe.length());
			final ItemStack input = parseStringAsItemStack(inputStr, true);
			final ItemStack output = parseStringAsItemStack(outputStr, false);
			if ((input == null) || (output == null)) {
				BaseMetals.logger.error("Failed to add recipe formula '%s' because the blocks/items could not be found", recipe);
			} else {
				CrusherRecipeRegistry.addNewCrusherRecipe(input, output);
			}
		}
	}

	/**
	 * Parses a String in the format (stack-size)*(modid):(item/block
	 * name)#(metadata value). The stacksize and metadata value parameters are
	 * optional.
	 *
	 * @param str
	 *            A String describing an ItemStack (e.g. "4*minecraft:dye#15" or
	 *            "minecraft:bow")
	 * @param allowWildcard
	 *            If true, then item strings that do not specify a metadata
	 *            value will use the OreDictionary wildcard value. If false,
	 *            then the default meta value is 0 instead.
	 * @return An ItemStack representing the item, or null if the item is not
	 *         found
	 */
	public static ItemStack parseStringAsItemStack(String str, final boolean allowWildcard) {
		String work = str.trim();
		int count = 1;
		int meta;
		if (allowWildcard) {
			meta = OreDictionary.WILDCARD_VALUE;
		} else {
			meta = 0;
		}
		int nameStart = 0;
		int nameEnd = work.length();
		if (work.contains("*")) {
			count = Integer.parseInt(work.substring(0, work.indexOf('*')).trim());
			nameStart = work.indexOf('*') + 1;
		}
		if (work.contains("#")) {
			meta = Integer.parseInt(work.substring(work.indexOf('#') + 1, str.length()).trim());
			nameEnd = work.indexOf('#');
		}
		final String id = work.substring(nameStart, nameEnd).trim();
		if (Block.getBlockFromName(id) != null) {
			// is a block
			return new ItemStack(Block.getBlockFromName(id), count, meta);
		} else if (Item.getByNameOrId(id) != null) {
			// is an item
			return new ItemStack(Item.getByNameOrId(id), count, meta);
		} else {
			// item not found
			BaseMetals.logger.info("Failed to find item or block for ID '%s'", id);
			return null;
		}

	}

	public static class Options {
		// GENERAL

		private static boolean disableAllHammerRecipes = false;
		public static void setDisableAllHammerRecipes(boolean bool) {
			disableAllHammerRecipes = bool;
		}

		private static boolean enforceHardness = true;
		public static void setEnforceHardness(boolean bool) {
			enforceHardness = bool;
		}

		private static boolean strongHammers = true;
		public static void setStrongHammers(boolean bool) {
			strongHammers = bool;
		}

		private static boolean autoDetectRecipes = true;
		public static void setAutoDetectRecipes(boolean bool) {
			autoDetectRecipes = bool;
		}

		private static boolean requireMMDOreSpawn = true;
		public static void setRequireMMDOreSpawn(boolean bool) {
			requireMMDOreSpawn = bool;
		}

		private static boolean enableAchievements = true;
		public static void setEnableAchievements(boolean bool) {
			enableAchievements = bool;
		}

		private static boolean crackHammerFullStack = false;
		public static void setCrackHammerFullStack(boolean bool) {
			crackHammerFullStack = bool;
		}

		private static boolean enableShieldUpgrades = true;
		public static void setEnableShieldUpgrades(boolean bool) {
			enableShieldUpgrades = bool;
		}

		private static boolean enablePlateRepairs = true;
		public static void setEnablePlateRepairs(boolean bool) {
			enablePlateRepairs = bool;
		}

		private static boolean enableModderSupportThings = true;
		public static void setEnableModderSupportThings(boolean bool) {
			enableModderSupportThings = bool;
		}

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
		public static void setDisabledRecipes(String[] string) {
			disabledRecipes = string;
		}
		public static String[] disabledRecipes() {
			return disabledRecipes;
		}

		// RECIPE AMOUNTS
		protected static int gearQuantity = 4;
		public static void setGearQuantity(int qty) {
			gearQuantity = qty;
		}

		public static int gearQuantity() {
			return gearQuantity;
		}

		private static int plateQuantity = 3;
		public static void setPlateQuantity(int qty) {
			plateQuantity = qty;
		}

		public static int plateQuantity() {
			return plateQuantity;
		}

		private static boolean furnaceCheese = true;
		public static void setFurnaceCheese(boolean bool) {
			furnaceCheese = bool;
		}
		public static boolean furnaceCheese() {
			return furnaceCheese;
		}

		private static boolean furnace1112 = true; // Overridden by FURNACE_CHEESE
		public static void setFurnace1112(boolean bool) {
			furnace1112 = bool;
		}
		public static boolean furnace1112() {
			return furnace1112;
		}
		
		// INTEGRATION
		private static final Map<String, Boolean> modEnabled = new HashMap<>();
		public static boolean isModEnabled(String modName) {
			String testName = modName.toLowerCase(Locale.ROOT);
			if (modEnabled.containsKey(testName)) {
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
			if (materialEnabled.containsKey(testName)) {
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
			if (thingEnabled.containsKey(testName)) {
				return thingEnabled.get(testName);
			}
			return false;
		}

		public static void thingEnabled(String name, Boolean bool) {
			if (!thingEnabled.containsKey(name)) {
				thingEnabled.put(name.toLowerCase(Locale.ROOT), bool);
			}
		}

		private static int explosionChance = 0;
		public static int explosionChance() {
			return explosionChance;
		}
		
		public static void explosionChance(int chance) {
			explosionChance = chance;
		}
		
		private static int angerPigmenRange = 0;
		public static int angerPigmenRange() {
			return angerPigmenRange;
		}
		
		public static void angerPigmenRange(int range) {
			angerPigmenRange = range;
		}
		
		private Options() {
			throw new IllegalAccessError("Not a instantiable class");
		}

		private static boolean fallbackOrespawn;
		public static void setFallbackOreSpawn(boolean boolean1) {
			fallbackOrespawn = boolean1;
		}
		
		public static boolean fallbackOrespawn() {
			return fallbackOrespawn;
		}
	}
}
