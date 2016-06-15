package cyano.basemetals;

import cyano.basemetals.data.AdditionalLootTables;
import cyano.basemetals.data.DataConstants;
import cyano.basemetals.registry.CrusherRecipeRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.MissingModsException;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


/**
 * This is the entry point for this mod. If you are writing your own mod that 
 * uses this mod, the classes of interest to you are the init classes (classes 
 * in package cyano.basemetals.init) and the CrusherRecipeRegistry class (in 
 * package cyano.basemetals.registry). Note that you should add 
 * 'dependencies = "required-after:basemetals"' to your &#64;Mod annotation 
 * (e.g. <br> 
 * &#64;Mod(modid = "moremetals", name="More Metals!", version = "1.2.3", dependencies = "required-after:basemetals")
 * <br>)
 * @author DrCyano
 *
 */
@Mod(
		modid = BaseMetals.MODID,
		name=BaseMetals.NAME,
		version = BaseMetals.VERSION,
		acceptedMinecraftVersions = "[1.9.4,)") // see VersionRange.createFromVersionSpec(String) for explanation of this convoluted feature
//		updateJSON = "https://raw.githubusercontent.com/cyanobacterium/BaseMetals/master/update.json")

public class BaseMetals
{
	//TODO: use metal plates to modify or repair shields

	public static BaseMetals INSTANCE = null;
	/** ID of this mod */
	public static final String MODID = "basemetals";
	/** display name of this mod */
	public static final String NAME ="Base Metals";
	/** Version number, in Major.Minor.Build format. The minor number is increased whenever a change 
	 * is made that has the potential to break compatibility with other mods that depend on this one. */
	public static final String VERSION = "2.3.3";


	static {
		// Forge says this needs to be statically initialized here.
		FluidRegistry.enableUniversalBucket();
	}
	
//	/** If true, some metals can be used to brew potions */
//	public static boolean enablePotionRecipes = true;
	
	/** If true, hammers cannot crush ores that they cannot mine */
	public static boolean enforceHardness = true;
	/** If true, then crack hammers can mine on all the same blocks that their pick-axe equivalent 
	 * can mine. If false, then the hammer is 1 step weaker than the pick-axe */
	public static boolean strongHammers = true;
	/** If true, hammers cannot be crafted */
	public static boolean disableAllHammers = false;
	/** For when the user adds specific recipies via the config file */
	public static List<String> userCrusherRecipes = new ArrayList<>();
	/** location of ore-spawn files */
	public static Path oreSpawnFolder = null;
	/** if true, then this mod will scan the Ore Dictionary for obvious hammer recipes from other mods */
	public static boolean autoDetectRecipes = true;
	/** if true, then this mod will require the orespawn mod */
	public static boolean requireOreSpawn = true;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		INSTANCE = this;
		// load config
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		
//		enablePotionRecipes = config.getBoolean("enable_potions", "options", enablePotionRecipes, 
//				"If true, then some metals can be used to brew potions.");


		disableAllHammers = config.getBoolean("disable_crack_hammer", "options", disableAllHammers,
				"If true, then the crack hammer cannot be crafted.");

		enforceHardness = config.getBoolean("enforce_hardness", "options", enforceHardness,
				"If true, then the crack hammer cannot crush ingots into powders if that \n"
						+	"crackhammer is not hard enough to crush the ingot's ore.");
		
		strongHammers = config.getBoolean("strong_hammers", "options", strongHammers, 
				"If true, then the crack hammer can crush ingots/ores that a pickaxe of the same \n"
			+	"material can harvest. If false, then your crack hammer must be made of a harder \n"
			+	"material than the ore you are crushing.");


		autoDetectRecipes = config.getBoolean("automatic_recipes", "options", autoDetectRecipes,
				"If true, then Base Metals will scan the Ore Dictionary to automatically add a \n"
						+	"Crack Hammer recipe for every material that has an ore, dust, and ingot.");

		requireOreSpawn = config.getBoolean("using_orespawn", "options", requireOreSpawn,
				"If false, then Base Metals will not require DrCyano's Ore Spawn mod. \n" +
						"Set to false if using another mod to manually handle ore generation.");

		
		ConfigCategory userRecipeCat = config.getCategory("hammer recipes");
		userRecipeCat.setComment(
			  "This section allows you to add your own recipes for the Crack Hammer (and other rock \n"
			+ "crushers). Recipes are specified in semicolon (;) delimited lists of formulas in the \n"
			+ "format modid:name#y->x*modid:name#y, where x is the number of items in a stack and y \n"
			+ "is the metadata value. Note that both x and y are optional, so you can use the \n"
			+ "formula modid:name->modid:name for most items/blocks. \n\n"
			+ "All properties in this section will be parsed for formulas, regardless their name. \n"
			+ "This lets you organize your recipe lists for easier reading.");
		if(userRecipeCat.keySet().size()==0){
			Property prop = new Property("custom","",Property.Type.STRING);
			prop.setComment("Example: minecraft:stained_glass#11->minecraft:dye#4; minecraft:wool->4*minecraft:string");
			userRecipeCat.put("custom", prop);
		}
		for(Property p : userRecipeCat.values()){
			String[] recipes = p.getString().split(";");
			for(String r : recipes){
				String recipe = r.trim();
				if(recipe.isEmpty()) continue;
				if(recipe.contains("->") == false){
					throw new IllegalArgumentException ("Malformed hammer recipe expression '"+recipe+"'. Should be in format 'modid:itemname->modid:itemname'");
				}
				userCrusherRecipes.add(recipe);
			}
		}

		if(requireOreSpawn) {
			if(!net.minecraftforge.fml.common.Loader.isModLoaded("orespawn")){
				HashSet<ArtifactVersion> orespawnMod = new HashSet<>();
				orespawnMod.add(new DefaultArtifactVersion("1.0.0"));
				throw new MissingModsException(orespawnMod, "orespawn", "DrCyano's Ore Spawn Mod");
			}
			oreSpawnFolder = Paths.get(event.getSuggestedConfigurationFile().toPath().getParent().toString(), "orespawn");
			Path oreSpawnFile = Paths.get(oreSpawnFolder.toString(), MODID + ".json");
			if (Files.exists(oreSpawnFile) == false) {
				try {
					Files.createDirectories(oreSpawnFile.getParent());
					Files.write(oreSpawnFile, Arrays.asList(DataConstants.defaultOreSpawnJSON.split("\n")), Charset.forName("UTF-8"));
				} catch (IOException e) {
					FMLLog.severe(MODID + ": Error: Failed to write file " + oreSpawnFile);
				}
			}
		}

		config.save();

		cyano.basemetals.init.Fluids.init();
		cyano.basemetals.init.Materials.init();
		cyano.basemetals.init.ItemGroups.init();
		cyano.basemetals.init.Blocks.init();
		cyano.basemetals.init.Items.init();
		cyano.basemetals.init.VillagerTrades.init();
		
		


		Path ALTPath = Paths.get(event.getSuggestedConfigurationFile().getParent(),"additional-loot-tables");
		Path myLootFolder = ALTPath.resolve(MODID);
		if(Files.notExists(myLootFolder)){
			try{
				Files.createDirectories(myLootFolder.resolve("chests"));
				Files.write(myLootFolder.resolve("chests").resolve("abandoned_mineshaft.json"),
						Arrays.asList(         AdditionalLootTables.abandoned_mineshaft));
				Files.write(myLootFolder.resolve("chests").resolve("desert_pyramid.json"),
						Arrays.asList(         AdditionalLootTables.desert_pyramid));
				Files.write(myLootFolder.resolve("chests").resolve("end_city_treasure.json"),
						Arrays.asList(         AdditionalLootTables.end_city_treasure));
				Files.write(myLootFolder.resolve("chests").resolve("jungle_temple.json"),
						Arrays.asList(         AdditionalLootTables.jungle_temple));
				Files.write(myLootFolder.resolve("chests").resolve("nether_bridge.json"),
						Arrays.asList(         AdditionalLootTables.nether_bridge));
				Files.write(myLootFolder.resolve("chests").resolve("simple_dungeon.json"),
						Arrays.asList(         AdditionalLootTables.simple_dungeon));
				Files.write(myLootFolder.resolve("chests").resolve("spawn_bonus_chest.json"),
						Arrays.asList(         AdditionalLootTables.spawn_bonus_chest));
				Files.write(myLootFolder.resolve("chests").resolve("stronghold_corridor.json"),
						Arrays.asList(         AdditionalLootTables.stronghold_corridor));
				Files.write(myLootFolder.resolve("chests").resolve("stronghold_crossing.json"),
						Arrays.asList(         AdditionalLootTables.stronghold_crossing));
				Files.write(myLootFolder.resolve("chests").resolve("village_blacksmith.json"),
						Arrays.asList(         AdditionalLootTables.village_blacksmith));
			} catch(IOException ex){
				FMLLog.log(Level.ERROR,ex,"%s: Failed to extract additional loot tables",MODID);
			}
		}

		if(event.getSide() == Side.CLIENT){
			clientPreInit(event);
		}
		if(event.getSide() == Side.SERVER){
			serverPreInit(event);
		}
	}
	
	@SideOnly(Side.CLIENT)
	private void clientPreInit(FMLPreInitializationEvent event){
		// client-only code
		cyano.basemetals.init.Fluids.bakeModels(MODID);

	}
	@SideOnly(Side.SERVER)
	private void serverPreInit(FMLPreInitializationEvent event){
		// server-only code
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{

		cyano.basemetals.init.Recipes.init();
		cyano.basemetals.init.DungeonLoot.init();
		cyano.basemetals.init.Entities.init();
		
		cyano.basemetals.init.Achievements.init();


		if(event.getSide() == Side.CLIENT){
			clientInit(event);
		}
		if(event.getSide() == Side.SERVER){
			serverInit(event);
		}
	}
	

	@SideOnly(Side.CLIENT)
	private void clientInit(FMLInitializationEvent event){
		// client-only code
		cyano.basemetals.init.Items.registerItemRenders(event);
		cyano.basemetals.init.Blocks.registerItemRenders(event);
	}
	@SideOnly(Side.SERVER)
	private void serverInit(FMLInitializationEvent event){
		// server-only code
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		cyano.basemetals.init.WorldGen.init();

		
		// parse user crusher recipes
		for(String recipe : userCrusherRecipes){
			FMLLog.info(MODID+": adding custom crusher recipe '"+recipe+"'");
			int i = recipe.indexOf("->");
			String inputStr = recipe.substring(0,i);
			String outputStr = recipe.substring(i+2,recipe.length());
			ItemStack input = parseStringAsItemStack(inputStr,true);
			ItemStack output = parseStringAsItemStack(outputStr,false);
			if(input == null || output == null){
				FMLLog.severe("Failed to add recipe formula '"+recipe+"' because the blocks/items could not be found");
			} else {
				CrusherRecipeRegistry.addNewCrusherRecipe(input, output);
			}
		}
		

		if(autoDetectRecipes){
			// add recipe for every X where the Ore Dictionary has dustX, oreX, and ingotX
			Set<String> dictionary = new HashSet<>();
			dictionary.addAll(Arrays.asList(OreDictionary.getOreNames()));
			for(String entry : dictionary){
				if(entry.contains("Mercury")) continue;
				if(entry.startsWith("dust")){
					String X = entry.substring("dust".length());
					String dustX = entry;
					String ingotX = "ingot".concat(X);
					String oreX = "ore".concat(X);
					if(dictionary.contains(oreX) && dictionary.contains(ingotX) && !OreDictionary.getOres(dustX).isEmpty()){
						ItemStack dustX1 = OreDictionary.getOres(dustX).get(0).copy();
						dustX1.stackSize = 1; 
						ItemStack dustX2 = dustX1.copy();
						dustX2.stackSize = 2; 
						// recipe found
						// but is it already registered
						List<ItemStack> oreBlocks = OreDictionary.getOres(oreX);
						boolean alreadyHasOreRecipe = true;
						for(ItemStack i : oreBlocks){
							alreadyHasOreRecipe = alreadyHasOreRecipe 
									&& (CrusherRecipeRegistry.getInstance().getRecipeForInputItem(i) != null);
						}
						List<ItemStack> ingotStacks = OreDictionary.getOres(ingotX);
						boolean alreadyHasIngotRecipe = true;
						for(ItemStack i : ingotStacks){
							alreadyHasIngotRecipe = alreadyHasIngotRecipe 
									&& (CrusherRecipeRegistry.getInstance().getRecipeForInputItem(i) != null);
						}
						if(!alreadyHasOreRecipe){
							FMLLog.info(MODID+": automatically adding custom crusher recipe \"%s\" -> %s",oreX,dustX2);
							CrusherRecipeRegistry.addNewCrusherRecipe(oreX, dustX2);
						}
						if(!alreadyHasIngotRecipe){
							FMLLog.info(MODID+": automatically adding custom crusher recipe \"%s\" -> %s",ingotX,dustX1);
							CrusherRecipeRegistry.addNewCrusherRecipe(ingotX, dustX1);
						}
					}
				}
			}
		}
		
		if(event.getSide() == Side.CLIENT){
			clientPostInit(event);
		}
		if(event.getSide() == Side.SERVER){
			serverPostInit(event);
		}
		
		CrusherRecipeRegistry.getInstance().clearCache();
	}
	

	@SideOnly(Side.CLIENT)
	private void clientPostInit(FMLPostInitializationEvent event){
		// client-only code
	}
	@SideOnly(Side.SERVER)
	private void serverPostInit(FMLPostInitializationEvent event){
		// server-only code
	}
	

	/**
	 * Parses a String in the format (stack-size)*(modid):(item/block name)#(metadata value). The 
	 * stacksize and metadata value parameters are optional.
	 * @param str A String describing an itemstack (e.g. "4*minecraft:dye#15" or "minecraft:bow")
	 * @param allowWildcard If true, then item strings that do not specify a metadata value will use 
	 * the OreDictionary wildcard value. If false, then the default meta value is 0 instead.
	 * @return An ItemStack representing the item, or null if the item is not found
	 */
	public static ItemStack parseStringAsItemStack(String str, boolean allowWildcard){
		str = str.trim();
		int count = 1;
		int meta;
		if(allowWildcard){
			meta = OreDictionary.WILDCARD_VALUE;
		} else {
			meta = 0;
		}
		int nameStart = 0;
		int nameEnd = str.length();
		if(str.contains("*")){
			count = Integer.parseInt(str.substring(0,str.indexOf("*")).trim());
			nameStart = str.indexOf("*")+1;
		}
		if(str.contains("#")){
			meta = Integer.parseInt(str.substring(str.indexOf("#")+1,str.length()).trim());
			nameEnd = str.indexOf("#");
		}
		String id = str.substring(nameStart,nameEnd).trim();
		if(Block.getBlockFromName(id) != null){
			// is a block
			return new ItemStack(Block.getBlockFromName(id),count,meta);
		} else if(Item.getByNameOrId(id) != null){
			// is an item
			return new ItemStack(Item.getByNameOrId(id),count,meta);
		} else {
			// item not found
			FMLLog.severe("Failed to find item or block for ID '"+id+"'");
			return null;
		}
	}




}
