package com.mcmoddev.lib.util;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.registry.CrusherRecipeRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.text.WordUtils;

import java.util.*;
import java.util.stream.Collectors;

public class Config {

	private static final String FLUIDS_CAT = "Fluids";
	private static final String MATERIALS_CAT = "Metals";
	private static final String VANILLA_CAT = "Vanilla";
	private static final String INTEGRATION_CAT = "Mod Integration";
	private static final String FORCED_TRAIT_CAT = "Forced Traits";

	private static final List<String> UserCrusherRecipes = new ArrayList<>();

	// shut up SonarLint/SonarQube
	protected Config() {

	}

	private static void integrationEnabled(String identifier, String modid, Boolean defaultValue, Configuration configuration){
		String newIdentifier = WordUtils.capitalizeFully(identifier);
		Options.modEnabled(modid,
				configuration.getBoolean(modid + "_integration", INTEGRATION_CAT, defaultValue, "If false, then Base Metals will not try and integrate with " + newIdentifier));
	}

	protected static void configIntegrationOptions(IntegrationConfigOptions[] integrationConfigOptions, Configuration configuration){
		for (IntegrationConfigOptions integration:integrationConfigOptions) {
			integrationEnabled(integration.getIdentifier(), integration.getModid(), integration.getEnabled(), configuration);
		}
	}

	private static void forceTraitRegistration(String identifier, Boolean hasTraits, Boolean defaultValue, Configuration configuration){
		if(hasTraits){
			Options.forcedTrait(identifier, configuration.getBoolean("Force " + identifier + " trait registration", FORCED_TRAIT_CAT, defaultValue, "Enables the forced registration of traits for " + identifier));
		}
	}

	private static void fluidEnabled(String identifier, Boolean defaultValue, Configuration configuration){
		String newIdentifier = WordUtils.capitalizeFully(identifier);
		Options.fluidEnabled(newIdentifier, configuration.getBoolean("Enables " + identifier, FLUIDS_CAT, defaultValue, "Enables the molten fluid of " + identifier));
	}

	private static void materialEnabled(String identifier, Boolean defaultValue, Boolean isVanilla, Configuration configuration){
		String newIdentifier = WordUtils.capitalizeFully(identifier);
		String cat = isVanilla ? VANILLA_CAT : MATERIALS_CAT;
		Options.materialEnabled(newIdentifier, configuration.getBoolean("Enables " + identifier, cat, defaultValue, "Enables " + newIdentifier + " Items and Materials"));
	}

	protected static void configMaterialOptions(MaterialConfigOptions[] materials, Configuration configuration){
		for (MaterialConfigOptions materialConfigOptions:materials) {
			materialEnabled(materialConfigOptions.getIdentifier(), materialConfigOptions.getMaterialEnabled(), materialConfigOptions.getVanilla(), configuration);
			if(materialConfigOptions.getHasFluid()){
				fluidEnabled(materialConfigOptions.getIdentifier(), materialConfigOptions.getFluidEnabled(), configuration);
			}
			forceTraitRegistration(materialConfigOptions.getIdentifier(), materialConfigOptions.getHasTraits(), materialConfigOptions.getForceTraits(), configuration);
		}
	}

	protected static void manageUserHammerRecipes(final Collection<Property> values) {
		for (final Property p : values) {
			final String[] recipes = p.getString().split(";");
			for (final String r : recipes) {
				final String recipe = r.trim();
				if (recipe.isEmpty()) {
					continue;
				}
				if (!(recipe.contains("->"))) {
					throw new IllegalArgumentException("Malformed hammer recipe expression '"
							+ recipe + "'. Should be in format 'modid:itemname->modid:itemname'");
				}
				UserCrusherRecipes.add(recipe);
			}
		}
	}

	protected static String[] parseDisabledRecipes(final String rawDisabledRecipes) {
		if (!rawDisabledRecipes.isEmpty() && rawDisabledRecipes.contains(";")) {
			return rawDisabledRecipes.split(";");
		} else {
			return new String[] { rawDisabledRecipes };
		}
	}

	/**
	 *
	 */
	public static void postInit() {
		addUserRecipes();

		if (Options.autoDetectRecipes()) {
			// add recipe for every X where the Ore Dictionary has dustX, oreX, and ingotX
			final Set<String> dictionary = new HashSet<>();
			dictionary.addAll(Arrays.asList(OreDictionary.getOreNames()).stream()
					.filter(item -> !item.contains("Mercury"))
					.filter(item -> !item.contains("Redstone"))
					.filter(item -> item.startsWith("dust")).filter(item -> {
						final String ingotX = Oredicts.INGOT.concat(item.substring(4));
						final String oreX = Oredicts.ORE.concat(item.substring(4));
						return (dictionary.contains(oreX) && dictionary.contains(ingotX)
								&& !OreDictionary.getOres(item).isEmpty());
					}).collect(Collectors.<String>toSet()));

			addOreRecipes(dictionary);
			addIngotRecipes(dictionary);
		}
	}

	private static void addIngotRecipes(final Set<String> dictionary) {
		dictionary.stream().filter(entry -> {
			final List<ItemStack> itemstacks = OreDictionary
					.getOres(Oredicts.INGOT.concat(entry.substring(4)));
			for (final ItemStack i : itemstacks) {
				if ((CrusherRecipeRegistry.getRecipeForInputItem(i) != null)) {
					return true;
				}
			}
			return false;
		}).forEach(entry -> {
			final String ingotX = Oredicts.INGOT.concat(entry.substring(4));
			final ItemStack dustX = OreDictionary.getOres(entry).get(0).copy();
			dustX.setCount(2);
			BaseMetals.logger.info("Automatically adding custom crusher recipe '{}' -> {}", ingotX,
					dustX);
			CrusherRecipeRegistry.addNewCrusherRecipe(ingotX, dustX);
		});
	}

	private static void addOreRecipes(final Set<String> dictionary) {
		dictionary.stream().filter(entry -> {
			final List<ItemStack> itemstacks = OreDictionary
					.getOres(Oredicts.ORE.concat(entry.substring(4)));
			for (final ItemStack i : itemstacks) {
				if ((CrusherRecipeRegistry.getRecipeForInputItem(i) != null)) {
					return true;
				}
			}
			return false;
		}).forEach(entry -> {
			final String oreX = Oredicts.ORE.concat(entry.substring(4));
			final ItemStack dustX = OreDictionary.getOres(entry).get(0).copy();
			dustX.setCount(2);
			BaseMetals.logger.info("Automatically adding custom crusher recipe '{}' -> {}", oreX,
					dustX);
			CrusherRecipeRegistry.addNewCrusherRecipe(oreX, dustX);
		});
	}

	private static void addUserRecipes() {
		for (final String recipe : UserCrusherRecipes) {
			BaseMetals.logger.info("Adding custom crusher recipe '{}'", recipe);
			final int i = recipe.indexOf("->");
			final String inputStr = recipe.substring(0, i);
			final String outputStr = recipe.substring(i + 2, recipe.length());
			final ItemStack input = parseStringAsItemStack(inputStr, true);
			final ItemStack output = parseStringAsItemStack(outputStr, false);
			if ((input.isEmpty()) || (output.isEmpty())) {
				BaseMetals.logger.error(
						"Failed to add recipe formula '{}' because the blocks/items could not be found",
						recipe);
			} else {
				CrusherRecipeRegistry.addNewCrusherRecipe(input, output);
			}
		}
	}

	/**
	 * Parses a String in the format (stack-size)*(modid):(item/block name)#(metadata value). The
	 * stacksize and metadata value parameters are optional.
	 *
	 * @param str
	 *            A String describing an ItemStack (e.g. "4*minecraft:dye#15" or "minecraft:bow")
	 * @param allowWildcard
	 *            If true, then item strings that do not specify a metadata value will use the
	 *            OreDictionary wildcard value. If false, then the default meta value is 0 instead.
	 * @return An ItemStack representing the item, or null if the item is not found
	 */
	public static ItemStack parseStringAsItemStack(final String str, final boolean allowWildcard) {
		final String work = str.trim();
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
			BaseMetals.logger.info("Failed to find item or block for ID '{}'", id);
			return ItemStack.EMPTY;
		}

	}

	public static class Options {
		// GENERAL

		private static boolean disableAllHammerRecipes = false;

		public static void setDisableAllHammerRecipes(final boolean bool) {
			disableAllHammerRecipes = bool;
		}

		private static boolean enforceHardness = true;

		public static void setEnforceHardness(final boolean bool) {
			enforceHardness = bool;
		}

		private static boolean strongHammers = true;

		public static void setStrongHammers(final boolean bool) {
			strongHammers = bool;
		}

		private static boolean twoDustDrop = true;

		public static void setTwoDustDrop(final boolean bool) {
			twoDustDrop = bool;
		}

		private static boolean autoDetectRecipes = true;

		public static void setAutoDetectRecipes(final boolean bool) {
			autoDetectRecipes = bool;
		}

		private static boolean requireMMDOreSpawn = true;

		public static void setRequireMMDOreSpawn(final boolean bool) {
			requireMMDOreSpawn = bool;
		}

		private static boolean enableAchievements = true;

		public static void setEnableAchievements(final boolean bool) {
			enableAchievements = bool;
		}

		private static boolean crackHammerFullStack = false;

		public static void setCrackHammerFullStack(final boolean bool) {
			crackHammerFullStack = bool;
		}

		private static boolean enableShieldUpgrades = true;

		public static void setEnableShieldUpgrades(final boolean bool) {
			enableShieldUpgrades = bool;
		}

		private static boolean enablePlateRepairs = true;

		public static void setEnablePlateRepairs(final boolean bool) {
			enablePlateRepairs = bool;
		}

		private static boolean enableModderSupportThings = true;

		public static void setEnableModderSupportThings(final boolean bool) {
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

		public static boolean twoDustDrop() {
			return twoDustDrop;
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

		/**
		 *
		 * @param string
		 */
		public static void setDisabledRecipes(final String[] string) {
			disabledRecipes = string;
			Arrays.asList(disabledRecipes).stream()
					.forEach(com.mcmoddev.lib.registry.CrusherRecipeRegistry::disableRecipe);
		}

		public static String[] disabledRecipes() {
			return disabledRecipes;
		}

		// RECIPE AMOUNTS
		private static int gearQuantity = 4;

		public static void setGearQuantity(final int qty) {
			gearQuantity = qty;
		}

		public static int gearQuantity() {
			return gearQuantity;
		}

		private static int plateQuantity = 3;

		public static void setPlateQuantity(final int qty) {
			plateQuantity = qty;
		}

		public static int plateQuantity() {
			return plateQuantity;
		}

		private static boolean furnaceCheese = true;

		public static void setFurnaceCheese(final boolean bool) {
			furnaceCheese = bool;
		}

		public static boolean furnaceCheese() {
			return furnaceCheese;
		}

		private static boolean furnace1112 = true; // Overridden by FURNACE_CHEESE

		public static void setFurnace1112(final boolean bool) {
			furnace1112 = bool;
		}

		public static boolean furnace1112() {
			return furnace1112;
		}

		protected static void clearOptions() { // to support testability
			modEnabled.clear();
			materialEnabled.clear();
			thingEnabled.clear();
			fluidEnabled.clear();
		}

		// INTEGRATION
		private static final Map<String, Boolean> modEnabled = new HashMap<>();

		/**
		 *
		 * @param modName
		 * @return
		 */
		public static boolean isModEnabled(final String modName) {
			final String testName = modName.toLowerCase(Locale.ROOT);
			if (modEnabled.containsKey(testName)) {
				return modEnabled.get(testName);
			}
			return false;
		}

		/**
		 *
		 * @param modName
		 * @param bool
		 */
		public static void modEnabled(final String modName, final Boolean bool) {
			if (!modEnabled.containsKey(modName)) {
				modEnabled.put(modName.toLowerCase(Locale.ROOT), bool);
			}
		}

		// MATERIALS
		private static final Map<String, Boolean> materialEnabled = new HashMap<>();

		/**
		 *
		 * @param name
		 * @return
		 */
		public static boolean isMaterialEnabled(final String name) {
			final String testName = name.toLowerCase(Locale.ROOT);
			if (materialEnabled.containsKey(testName)) {
				return materialEnabled.get(testName);
			}
			return false;
		}

		/**
		 *
		 * @param materialName
		 * @param bool
		 */
		public static void materialEnabled(final String materialName, final Boolean bool) {
			if (!materialEnabled.containsKey(materialName)) {
				materialEnabled.put(materialName.toLowerCase(Locale.ROOT), bool);
			}
		}

		// THINGS
		private static final Map<String, Boolean> thingEnabled = new HashMap<>();

		/**
		 *
		 * @param name
		 * @return
		 */
		public static boolean isThingEnabled(final String name) {
			final String testName = name.toLowerCase(Locale.ROOT);
			if (thingEnabled.containsKey(testName)) {
				return thingEnabled.get(testName);
			}
			return false;
		}

		/**
		 *
		 * @param name
		 * @param bool
		 */
		public static void thingEnabled(final String name, final Boolean bool) {
			if (!thingEnabled.containsKey(name)) {
				thingEnabled.put(name.toLowerCase(Locale.ROOT), bool);
			}
		}

		// FLUIDS
		private static final Map<String, Boolean> fluidEnabled = new HashMap<>();

		/**
		 *
		 * @param name
		 * @return
		 */
		public static boolean isFluidEnabled(final String name) {
			final String testName = name.toLowerCase(Locale.ROOT);
			if (fluidEnabled.containsKey(testName)) {
				return fluidEnabled.get(testName);
			}
			return false;
		}

		// FORCED TRAIT REGISTRATIONS
		private static final Map<String, Boolean> forcedTrait = new HashMap<>();

		/**
		 *
		 * @param name
		 * @return
		 */
		public static boolean isForcedTrait(final String name){
			final String testName = name.toLowerCase(Locale.ROOT);
			if (forcedTrait.containsKey(testName)) {
				return forcedTrait.get(testName);
			}
			return false;
		}

		/**
		 *
		 * @param name
		 * @param bool
		 */
		public static void fluidEnabled(final String name, final Boolean bool) {
			if (!fluidEnabled.containsKey(name)) {
				fluidEnabled.put(name.toLowerCase(Locale.ROOT), bool);
			}
		}

		/**
		 *
		 * @param name
		 * @param bool
		 */
		public static void forcedTrait(final String name, final Boolean bool) {
			if (!forcedTrait.containsKey(name)) {
				forcedTrait.put(name.toLowerCase(Locale.ROOT), bool);
			}
		}

		private static int explosionChance = 0;

		public static int explosionChance() {
			return explosionChance;
		}

		public static void explosionChance(final int chance) {
			explosionChance = chance;
		}

		private static int angerPigmenRange = 0;

		public static int angerPigmenRange() {
			return angerPigmenRange;
		}

		public static void angerPigmenRange(final int range) {
			angerPigmenRange = range;
		}

		protected Options() {
			throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
		}

		private static boolean fallbackOrespawn;

		public static void setFallbackOreSpawn(final boolean boolean1) {
			fallbackOrespawn = boolean1;
		}

		public static boolean fallbackOrespawn() {
			return fallbackOrespawn;
		}

		private static int rodQuantity = 4;

		public static void setRodQuantity(final int quantity) {
			rodQuantity = quantity;
		}

		public static int rodQuantity() {
			return rodQuantity;
		}
	}
}
