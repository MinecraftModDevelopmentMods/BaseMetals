package cyano.basemetals.util;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;

import cyano.basemetals.BaseMetals;
import cyano.basemetals.data.DataConstants;
import cyano.basemetals.registry.CrusherRecipeRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.*;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.versioning.*;
import net.minecraftforge.oredict.OreDictionary;

/**
 * @author p455w0rd
 *
 */
public class Config {

	public static Configuration CONFIG;
	private static final String CONFIG_FILE = "config/BaseMetals.cfg";
	private static final String GENERAL_CAT = "General";
	private static final String INTEGRATION_CAT = "Mod Integration";
	private static final String METALS_CAT = "Metals";
	private static final String VANILLA_CAT = "Vanilla";
	private static final String HAMMER_RECIPES_CAT = "Crack Hammer Recipies";
	private static final String ALT_CFG_PATH = "config/additionalloottables";
	private static final String ORESPAWN_CFG_PATH = "config/orespawn";
	protected static List<String> USER_CRUSHER_RECIPES = new ArrayList<String>();

	@SubscribeEvent
	public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent e) {
		if (e.getModID().equals(BaseMetals.MODID)) {
			init();
		}
	}

	public static void init() {
		if (CONFIG == null) {
			CONFIG = new Configuration(new File(CONFIG_FILE));
			MinecraftForge.EVENT_BUS.register(new Config());
		}

		//GENERAL
		// enablePotionRecipes = config.getBoolean("enable_potions", "options",
		// enablePotionRecipes,
		// "If true, then some metals can be used to brew potions.");

		//GENERAL
		Options.DISABLE_ALL_HAMMERS = CONFIG.getBoolean("disable_crack_hammer", GENERAL_CAT, false, "If true, then the crack hammer cannot be crafted.");
		Options.ENFORCE_HARDNESS = CONFIG.getBoolean("enforce_hardness", GENERAL_CAT, true, "If true, then the crack hammer cannot crush ingots into powders if that \n" + "crackhammer is not hard enough to crush the ingot's ore.");
		Options.STRONG_HAMMERS = CONFIG.getBoolean("strong_hammers", GENERAL_CAT, true, "If true, then the crack hammer can crush ingots/ores that a pickaxe of the same \n" + "material can harvest. If false, then your crack hammer must be made of a harder \n" + "material than the ore you are crushing.");
		Options.AUTODETECT_RECIPES = CONFIG.getBoolean("automatic_recipes", GENERAL_CAT, true, "If true, then Base Metals will scan the Ore Dictionary to automatically add a \n" + "Crack Hammer recipe for every material that has an ore, dust, and ingot.");
		Options.REQUIRE_ORESPAWN = CONFIG.getBoolean("using_orespawn", GENERAL_CAT, true, "If false, then Base Metals will not require DrCyano's Ore Spawn mod. \n" + "Set to false if using another mod to manually handle ore generation.");

		// INTEGRATION
		Options.ENABLE_ENDER_IO = CONFIG.getBoolean("ender_io_integration", INTEGRATION_CAT, true, "If false, then Base Metals will not try and integrate with Ender IO");
		Options.ENABLE_IC2 = CONFIG.getBoolean("ic2_integration", INTEGRATION_CAT, true, "If false, then Base Metals will not try and integrate with IC2");
		Options.ENABLE_MEKANISM = CONFIG.getBoolean("mekanism_integration", INTEGRATION_CAT, true, "If false, then Base Metals will not try and integrate with Mekanism");
		Options.ENABLE_THAUMCRAFT = CONFIG.getBoolean("thaumcraft_integration", INTEGRATION_CAT, true, "If false, then Base Metals will not try and integrate with Thaumcraft");
		Options.ENABLE_TINKERS_CONSTRUCT = CONFIG.getBoolean("tinkers_construct_integration", INTEGRATION_CAT, true, "If false, then Base Metals will not try and integrate with Tinkers Construct");
		Options.ENABLE_VEINMINER = CONFIG.getBoolean("veinminer_integration", INTEGRATION_CAT, true, "If false, then Base Metals will not try and integrate with VeinMiner");

		// METALS
		Options.ENABLE_ADAMANTINE = CONFIG.getBoolean("EnableAdamantine", METALS_CAT, true, "Enable Adamantine Items and Materials");
		Options.ENABLE_ANTIMONY = CONFIG.getBoolean("EnableAntimony", METALS_CAT, true, "Enable Antimony Items and Materials");
		Options.ENABLE_AQUARIUM = CONFIG.getBoolean("EnableAquarium", METALS_CAT, true, "Enable Aquarium Items and Materials");
		Options.ENABLE_BISMUTH = CONFIG.getBoolean("EnableBismuth", METALS_CAT, true, "Enable Bismuth Items and Materials");
		Options.ENABLE_BRASS = CONFIG.getBoolean("EnableBrass", METALS_CAT, true, "Enable Brass Items and Materials");
		Options.ENABLE_BRONZE = CONFIG.getBoolean("EnableBronze", METALS_CAT, true, "Enable Bronze Items and Materials");
		Options.ENABLE_CARBON = CONFIG.getBoolean("EnableCarbon", METALS_CAT, true, "Enable Carbon Items and Materials");
		Options.ENABLE_COLDIRON = CONFIG.getBoolean("EnableColdIron", METALS_CAT, true, "Enable ColdIron Items and Materials");
		Options.ENABLE_COPPER = CONFIG.getBoolean("EnableCopper", METALS_CAT, true, "Enable Copper Items and Materials");
		Options.ENABLE_CUPRONICKEL = CONFIG.getBoolean("EnableCupronickel", METALS_CAT, true, "Enable Cupronickal Items and Materials");
		Options.ENABLE_ELECTRUM = CONFIG.getBoolean("EnableElectrum", METALS_CAT, true, "Enable Electrum Items and Materials");
		Options.ENABLE_INVAR = CONFIG.getBoolean("EnableInvar", METALS_CAT, true, "Enable Invar Items and Materials");
		Options.ENABLE_LEAD = CONFIG.getBoolean("EnableLead", METALS_CAT, true, "Enable Lead Items and Materials");
		Options.ENABLE_MERCURY = CONFIG.getBoolean("EnableMercury", METALS_CAT, true, "Enable Mercury Items and Materials");
		Options.ENABLE_MITHRIL = CONFIG.getBoolean("EnableMitheril", METALS_CAT, true, "Enable Mithril Items and Materials");
		Options.ENABLE_NICKEL = CONFIG.getBoolean("EnableNickerl", METALS_CAT, true, "Enable Nickel Items and Materials");
		Options.ENABLE_PEWTER = CONFIG.getBoolean("EnablePewter", METALS_CAT, true, "Enable Pewter Items and Materials");
		Options.ENABLE_PLATINUM = CONFIG.getBoolean("EnablePlatinum", METALS_CAT, true, "Enable Platinum Items and Materials");
		Options.ENABLE_SILVER = CONFIG.getBoolean("EnableSilver", METALS_CAT, true, "Enable Silver Items and Materials");
		Options.ENABLE_STARSTEEL = CONFIG.getBoolean("EnableStarSteel", METALS_CAT, true, "Enable StarSteel Items and Materials");
		Options.ENABLE_STEEL = CONFIG.getBoolean("EnableSteel", METALS_CAT, true, "Enable Steel Items and Materials");
		Options.ENABLE_TIN = CONFIG.getBoolean("EnableTin", METALS_CAT, true, "Enable Tin Items and Materials");
		Options.ENABLE_ZINC = CONFIG.getBoolean("EnableZinc", METALS_CAT, true, "Enable Zinc Items and Materials");

		//VANILLA
		Options.ENABLE_DIAMOND = CONFIG.getBoolean("EnableDiamond", VANILLA_CAT, true, "Enable Diamond Items and Materials");
		Options.ENABLE_GOLD = CONFIG.getBoolean("EnableGold", VANILLA_CAT, true, "Enable Gold Items and Materials");
		Options.ENABLE_IRON = CONFIG.getBoolean("EnableIron", VANILLA_CAT, true, "Enable Iron Items and Materials");
		Options.ENABLE_STONE = CONFIG.getBoolean("EnableStone", VANILLA_CAT, true, "Enable Stone Items and Materials");
		Options.ENABLE_WOOD = CONFIG.getBoolean("EnableAWood", VANILLA_CAT, true, "Enable Wood Items and Materials");

		//CRACK HAMMER RECIPES
		final ConfigCategory userRecipeCat = CONFIG.getCategory(HAMMER_RECIPES_CAT);
		userRecipeCat.setComment("This section allows you to add your own recipes for the Crack Hammer (and other rock \n" + "crushers). Recipes are specified in semicolon (;) delimited lists of formulas in the \n" + "format modid:name#y->x*modid:name#y, where x is the number of items in a stack and y \n" + "is the metadata value. Note that both x and y are optional, so you can use the \n" + "formula modid:name->modid:name for most items/blocks. \n\n" + "All properties in this section will be parsed for formulas, regardless their name. \n" + "This lets you organize your recipe lists for easier reading.");
		if (userRecipeCat.keySet().size() == 0) {
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

		if (CONFIG.hasChanged()) {
			CONFIG.save();
		}

		if (Options.REQUIRE_ORESPAWN) {
			if (!Loader.isModLoaded("orespawn")) {
				final HashSet<ArtifactVersion> orespawnMod = new HashSet<>();
				orespawnMod.add(new DefaultArtifactVersion("1.0.0"));
				throw new MissingModsException(orespawnMod, "orespawn", "MMD Ore Spawn Mod");
			}
			Path oreSpawnFolder = Paths.get(ORESPAWN_CFG_PATH);
			final Path oreSpawnFile = Paths.get(oreSpawnFolder.toString(), BaseMetals.MODID + ".json");
			if (!(Files.exists(oreSpawnFile))) {
				try {
					Files.createDirectories(oreSpawnFile.getParent());
					Files.write(oreSpawnFile, Arrays.asList(DataConstants.DEFAULT_ORESPAWN_JSON.split("\n")), Charset.forName("UTF-8"));
				}
				catch (final IOException e) {
					FMLLog.severe(BaseMetals.MODID + ": Error: Failed to write file " + oreSpawnFile);
				}
			}
		}
	}

	public static class Options {

		//GENERAL
		public static boolean DISABLE_ALL_HAMMERS = false;
		public static boolean ENFORCE_HARDNESS = true;
		public static boolean STRONG_HAMMERS = true;
		public static boolean AUTODETECT_RECIPES = true;
		public static boolean REQUIRE_ORESPAWN = true;

		// INTEGRATION
		public static boolean ENABLE_ENDER_IO = true;
		public static boolean ENABLE_IC2 = true;
		public static boolean ENABLE_MEKANISM = true;
		public static boolean ENABLE_THAUMCRAFT = true;
		public static boolean ENABLE_TINKERS_CONSTRUCT = true;
		public static boolean ENABLE_VEINMINER = true;

		// METALS
		public static boolean ENABLE_ADAMANTINE = true;
		public static boolean ENABLE_ANTIMONY = true;
		public static boolean ENABLE_AQUARIUM = true;
		public static boolean ENABLE_BISMUTH = true;
		public static boolean ENABLE_BRASS = true;
		public static boolean ENABLE_BRONZE = true;
		public static boolean ENABLE_CARBON = true;
		public static boolean ENABLE_COLDIRON = true;
		public static boolean ENABLE_COPPER = true;
		public static boolean ENABLE_CUPRONICKEL = true;
		public static boolean ENABLE_ELECTRUM = true;
		public static boolean ENABLE_INVAR = true;
		public static boolean ENABLE_LEAD = true;
		public static boolean ENABLE_MERCURY = true;
		public static boolean ENABLE_MITHRIL = true;
		public static boolean ENABLE_NICKEL = true;
		public static boolean ENABLE_PEWTER = true;
		public static boolean ENABLE_PLATINUM = true;
		public static boolean ENABLE_SILVER = true;
		public static boolean ENABLE_STARSTEEL = true;
		public static boolean ENABLE_STEEL = true;
		public static boolean ENABLE_TIN = true;
		public static boolean ENABLE_ZINC = true;

		//VANILLA
		public static boolean ENABLE_DIAMOND = true;
		public static boolean ENABLE_GOLD = true;
		public static boolean ENABLE_IRON = true;
		public static boolean ENABLE_STONE = true;
		public static boolean ENABLE_WOOD = true;

	}

	public static void postInit() {
		for (final String recipe : USER_CRUSHER_RECIPES) {
			FMLLog.info(BaseMetals.MODID + ": adding custom crusher recipe '" + recipe + "'");
			final int i = recipe.indexOf("->");
			final String inputStr = recipe.substring(0, i);
			final String outputStr = recipe.substring(i + 2, recipe.length());
			final ItemStack input = parseStringAsItemStack(inputStr, true);
			final ItemStack output = parseStringAsItemStack(outputStr, false);
			if ((input == null) || (output == null)) {
				FMLLog.severe("Failed to add recipe formula '" + recipe + "' because the blocks/items could not be found");
			}
			else {
				CrusherRecipeRegistry.addNewCrusherRecipe(input, output);
			}
		}

		if (Options.AUTODETECT_RECIPES) {
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
							FMLLog.info(BaseMetals.MODID + ": automatically adding custom crusher recipe \"%s\" -> %s", oreX, dustX2);
							CrusherRecipeRegistry.addNewCrusherRecipe(oreX, dustX2);
						}
						if (!alreadyHasIngotRecipe) {
							FMLLog.info(BaseMetals.MODID + ": automatically adding custom crusher recipe \"%s\" -> %s", ingotX, dustX1);
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
			FMLLog.severe("Failed to find item or block for ID '" + id + "'");
			return null;
		}
	}
}
