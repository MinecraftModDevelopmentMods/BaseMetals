package cyano.basemetals;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Level;

import cyano.basemetals.data.AdditionalLootTables;
import cyano.basemetals.data.DataConstants;
import cyano.basemetals.proxy.CommonProxy;
import cyano.basemetals.registry.CrusherRecipeRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.MissingModsException;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;
import net.minecraftforge.oredict.OreDictionary;

/**
 * This is the entry point for this mod. If you are writing your own mod that
 * uses this mod, the classes of interest to you are the init classes (classes
 * in package cyano.basemetals.init) and the CrusherRecipeRegistry class (in
 * package cyano.basemetals.registry). Note that you should add 'dependencies =
 * "required-after:basemetals"' to your &#64;Mod annotation (e.g. <br>
 * &#64;Mod(modid = "moremetals", name="More Metals!", version = "1.2.3",
 * dependencies = "required-after:basemetals") <br>
 * )
 * 
 * @author DrCyano
 *
 */
@Mod(modid = BaseMetals.MODID, name = BaseMetals.NAME, version = BaseMetals.VERSION,
		dependencies = "required-after:Forge",
		acceptedMinecraftVersions = "[1.10.2,)",
		updateJSON = "https://raw.githubusercontent.com/MinecraftModDevelopment/BaseMetals/master/update.json")
public class BaseMetals {
	// TODO: use metal plates to modify or repair shields

	@Instance
	public static BaseMetals INSTANCE = null;

	/** ID of this mod */
	public static final String MODID = "basemetals";

	/** display name of this mod */
	public static final String NAME = "Base Metals";

	/**
	 * Version number, in Major.Minor.Build format. The minor number is
	 * increased whenever a change is made that has the potential to break
	 * compatibility with other mods that depend on this one.
	 */
	public static final String VERSION = "2.5.0";

	@SidedProxy(clientSide = "cyano.basemetals.proxy.ClientProxy", serverSide = "cyano.basemetals.proxy.ServerProxy")
	public static CommonProxy PROXY = null;

	static {
		// Forge says this needs to be statically initialized here.
		FluidRegistry.enableUniversalBucket();
	}

	// /** If true, some metals can be used to brew potions */
	// public static boolean enablePotionRecipes = true;

	/** If true, hammers cannot crush ores that they cannot mine */
	public static boolean enforceHardness = true;

	/**
	 * If true, then crack hammers can mine on all the same blocks that their
	 * pick-axe equivalent can mine. If false, then the hammer is 1 step weaker
	 * than the pick-axe
	 */
	public static boolean strongHammers = true;

	/** If true, hammers cannot be crafted */
	public static boolean disableAllHammers = false;

	/** For when the user adds specific recipies via the config file */
	protected static List<String> userCrusherRecipes = new ArrayList<>();

	/** location of ore-spawn files */
	public static Path oreSpawnFolder = null;

	/**
	 * if true, then this mod will scan the Ore Dictionary for obvious hammer
	 * recipes from other mods
	 */
	public static boolean autoDetectRecipes = true;

	/** if true, then this mod will require the orespawn mod */
	public static boolean requireOreSpawn = true;

	/**
	 *
	 * @param event
	 */
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		INSTANCE = this;

		// load config
		final Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();

		// enablePotionRecipes = config.getBoolean("enable_potions", "options",
		// enablePotionRecipes,
		// "If true, then some metals can be used to brew potions.");

		disableAllHammers = config.getBoolean("disable_crack_hammer", "options", disableAllHammers,
				"If true, then the crack hammer cannot be crafted.");

		enforceHardness = config.getBoolean("enforce_hardness", "options", enforceHardness,
				"If true, then the crack hammer cannot crush ingots into powders if that \n"
						+ "crackhammer is not hard enough to crush the ingot's ore.");

		strongHammers = config.getBoolean("strong_hammers", "options", strongHammers,
				"If true, then the crack hammer can crush ingots/ores that a pickaxe of the same \n"
						+ "material can harvest. If false, then your crack hammer must be made of a harder \n"
						+ "material than the ore you are crushing.");

		autoDetectRecipes = config.getBoolean("automatic_recipes", "options", autoDetectRecipes,
				"If true, then Base Metals will scan the Ore Dictionary to automatically add a \n"
						+ "Crack Hammer recipe for every material that has an ore, dust, and ingot.");

		requireOreSpawn = config.getBoolean("using_orespawn", "options", requireOreSpawn,
				"If false, then Base Metals will not require DrCyano's Ore Spawn mod. \n"
						+ "Set to false if using another mod to manually handle ore generation.");

		final ConfigCategory userRecipeCat = config.getCategory("hammer recipes");
		userRecipeCat
				.setComment("This section allows you to add your own recipes for the Crack Hammer (and other rock \n"
						+ "crushers). Recipes are specified in semicolon (;) delimited lists of formulas in the \n"
						+ "format modid:name#y->x*modid:name#y, where x is the number of items in a stack and y \n"
						+ "is the metadata value. Note that both x and y are optional, so you can use the \n"
						+ "formula modid:name->modid:name for most items/blocks. \n\n"
						+ "All properties in this section will be parsed for formulas, regardless their name. \n"
						+ "This lets you organize your recipe lists for easier reading.");
		if (userRecipeCat.keySet().size() == 0) {
			final Property prop = new Property("custom", "", Property.Type.STRING);
			prop.setComment("Example: minecraft:stained_glass#11->minecraft:dye#4; minecraft:wool->4*minecraft:string");
			userRecipeCat.put("custom", prop);
		}
		for (final Property p : userRecipeCat.values()) {
			final String[] recipes = p.getString().split(";");
			for (final String r : recipes) {
				final String recipe = r.trim();
				if (recipe.isEmpty())
					continue;
				if (!(recipe.contains("->")))
					throw new IllegalArgumentException("Malformed hammer recipe expression '" + recipe + "'. Should be in format 'modid:itemname->modid:itemname'");
				userCrusherRecipes.add(recipe);
			}
		}

		config.save();

		if (requireOreSpawn) {
			if (!net.minecraftforge.fml.common.Loader.isModLoaded("orespawn")) {
				final HashSet<ArtifactVersion> orespawnMod = new HashSet<>();
				orespawnMod.add(new DefaultArtifactVersion("1.0.0"));
				throw new MissingModsException(orespawnMod, "orespawn", "DrCyano's Ore Spawn Mod");
			}
			oreSpawnFolder = Paths.get(event.getSuggestedConfigurationFile().toPath().getParent().toString(), "orespawn");
			final Path oreSpawnFile = Paths.get(oreSpawnFolder.toString(), MODID + ".json");
			if (!(Files.exists(oreSpawnFile)))
				try {
					Files.createDirectories(oreSpawnFile.getParent());
					Files.write(oreSpawnFile, Arrays.asList(DataConstants.DEFAULT_ORESPAWN_JSON.split("\n")), Charset.forName("UTF-8"));
				} catch (final IOException e) {
					FMLLog.severe(MODID + ": Error: Failed to write file " + oreSpawnFile);
				}
		}

		cyano.basemetals.init.Fluids.init();
		cyano.basemetals.init.Materials.init();
		cyano.basemetals.init.ItemGroups.init();
		cyano.basemetals.init.Blocks.init();
		cyano.basemetals.init.Items.init();
		cyano.basemetals.init.VillagerTrades.init();
		cyano.basemetals.init.EnderIOPlugin.init();
		if (Loader.isModLoaded("tconstruct"))
			cyano.basemetals.init.TinkersConstructPlugin.init();
		if (Loader.isModLoaded("Mekanism"))
			cyano.basemetals.init.MekanismPlugin.init();
		cyano.basemetals.init.VeinMinerPlugin.init();

		final Path ALTPath = Paths.get(event.getSuggestedConfigurationFile().getParent(), "additional-loot-tables");
		final Path myLootFolder = ALTPath.resolve(MODID);
		if (Files.notExists(myLootFolder))
			try {
				Files.createDirectories(myLootFolder.resolve("chests"));
				Files.write(myLootFolder.resolve("chests").resolve("abandoned_mineshaft.json"),
						Collections.singletonList(AdditionalLootTables.abandoned_mineshaft));
				Files.write(myLootFolder.resolve("chests").resolve("desert_pyramid.json"),
						Collections.singletonList(AdditionalLootTables.desert_pyramid));
				Files.write(myLootFolder.resolve("chests").resolve("end_city_treasure.json"),
						Collections.singletonList(AdditionalLootTables.end_city_treasure));
				Files.write(myLootFolder.resolve("chests").resolve("jungle_temple.json"),
						Collections.singletonList(AdditionalLootTables.jungle_temple));
				Files.write(myLootFolder.resolve("chests").resolve("nether_bridge.json"),
						Collections.singletonList(AdditionalLootTables.nether_bridge));
				Files.write(myLootFolder.resolve("chests").resolve("simple_dungeon.json"),
						Collections.singletonList(AdditionalLootTables.simple_dungeon));
				Files.write(myLootFolder.resolve("chests").resolve("spawn_bonus_chest.json"),
						Collections.singletonList(AdditionalLootTables.spawn_bonus_chest));
				Files.write(myLootFolder.resolve("chests").resolve("stronghold_corridor.json"),
						Collections.singletonList(AdditionalLootTables.stronghold_corridor));
				Files.write(myLootFolder.resolve("chests").resolve("stronghold_crossing.json"),
						Collections.singletonList(AdditionalLootTables.stronghold_crossing));
				Files.write(myLootFolder.resolve("chests").resolve("village_blacksmith.json"),
						Collections.singletonList(AdditionalLootTables.village_blacksmith));
			} catch (final IOException ex) {
				FMLLog.log(Level.ERROR, ex, "%s: Failed to extract additional loot tables", MODID);
			}

		PROXY.preInit();
	}

	/**
	 *
	 * @param event
	 */
	@EventHandler
	public void init(FMLInitializationEvent event) {

		cyano.basemetals.init.Recipes.init();
		cyano.basemetals.init.DungeonLoot.init();
		cyano.basemetals.init.Entities.init();

		cyano.basemetals.init.Achievements.init();

		PROXY.init();

		MinecraftForge.EVENT_BUS.register(new cyano.basemetals.util.EventHandler());
	}

	/**
	 *
	 * @param event
	 */
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		cyano.basemetals.init.WorldGen.init();

		// parse user crusher recipes
		for (final String recipe : userCrusherRecipes) {
			FMLLog.info(MODID + ": adding custom crusher recipe '" + recipe + "'");
			final int i = recipe.indexOf("->");
			final String inputStr = recipe.substring(0, i);
			final String outputStr = recipe.substring(i + 2, recipe.length());
			final ItemStack input = parseStringAsItemStack(inputStr, true);
			final ItemStack output = parseStringAsItemStack(outputStr, false);
			if ((input == null) || (output == null))
				FMLLog.severe("Failed to add recipe formula '" + recipe + "' because the blocks/items could not be found");
			else
				CrusherRecipeRegistry.addNewCrusherRecipe(input, output);
		}

		if (autoDetectRecipes) {
			// add recipe for every X where the Ore Dictionary has dustX, oreX,
			// and ingotX
			final Set<String> dictionary = new HashSet<>();
			dictionary.addAll(Arrays.asList(OreDictionary.getOreNames()));
			for (final String entry : dictionary) {
				if (entry.contains("Mercury"))
					continue;
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
						for (final ItemStack i : oreBlocks)
							alreadyHasOreRecipe = alreadyHasOreRecipe
									&& (CrusherRecipeRegistry.getInstance().getRecipeForInputItem(i) != null);
						final List<ItemStack> ingotStacks = OreDictionary.getOres(ingotX);
						boolean alreadyHasIngotRecipe = true;
						for (final ItemStack i : ingotStacks)
							alreadyHasIngotRecipe = alreadyHasIngotRecipe
									&& (CrusherRecipeRegistry.getInstance().getRecipeForInputItem(i) != null);
						if (!alreadyHasOreRecipe) {
							FMLLog.info(MODID + ": automatically adding custom crusher recipe \"%s\" -> %s", oreX,
									dustX2);
							CrusherRecipeRegistry.addNewCrusherRecipe(oreX, dustX2);
						}
						if (!alreadyHasIngotRecipe) {
							FMLLog.info(MODID + ": automatically adding custom crusher recipe \"%s\" -> %s", ingotX,
									dustX1);
							CrusherRecipeRegistry.addNewCrusherRecipe(ingotX, dustX1);
						}
					}
				}
			}
		}

		CrusherRecipeRegistry.getInstance().clearCache();

		PROXY.postInit();
	}

	/**
	 * Parses a String in the format (stack-size)*(modid):(item/block
	 * name)#(metadata value). The stacksize and metadata value parameters are
	 * optional.
	 * 
	 * @param str A String describing an itemstack (e.g. "4*minecraft:dye#15" or "minecraft:bow")
	 * @param allowWildcard If true, then item strings that do not specify a metadata
	 *            value will use the OreDictionary wildcard value. If false,
	 *            then the default meta value is 0 instead.
	 * @return An ItemStack representing the item, or null if the item is not
	 *         found
	 */
	public static ItemStack parseStringAsItemStack(String str, boolean allowWildcard) {
		str = str.trim();
		int count = 1;
		int meta;
		if (allowWildcard)
			meta = OreDictionary.WILDCARD_VALUE;
		else
			meta = 0;
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
		if (Block.getBlockFromName(id) != null)
			// is a block
			return new ItemStack(Block.getBlockFromName(id), count, meta);
		else if (Item.getByNameOrId(id) != null)
			// is an item
			return new ItemStack(Item.getByNameOrId(id), count, meta);
		else {
			// item not found
			FMLLog.severe("Failed to find item or block for ID '" + id + "'");
			return null;
		}
	}


}
