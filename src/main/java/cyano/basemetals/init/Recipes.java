package cyano.basemetals.init;

import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.CrusherRecipeRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.Arrays;
import java.util.List;

public abstract class Recipes {
	
	private static boolean initDone = false;
	public static void init(){
		if(initDone)return;

		cyano.basemetals.init.Materials.init();
		cyano.basemetals.init.Blocks.init();
		cyano.basemetals.init.Items.init();
		
		initPureVanillaCrusherRecipes();
		initVanillaRecipes();
		initMetalRecipes();
		
		initDone = true;
	}
	
	
	private static void initPureVanillaCrusherRecipes(){
		// see net.minecraftforge.oredict.OreDictionary.initVanillaEntries() for vanilla oreDict names
		CrusherRecipeRegistry.addNewCrusherRecipe("stone", new ItemStack(Blocks.COBBLESTONE,1));
		CrusherRecipeRegistry.addNewCrusherRecipe("cobblestone", new ItemStack(Blocks.GRAVEL,1));
		CrusherRecipeRegistry.addNewCrusherRecipe(Blocks.GRAVEL, new ItemStack(Blocks.SAND,1));
		CrusherRecipeRegistry.addNewCrusherRecipe("sandstone", new ItemStack(Blocks.SAND,4));
		CrusherRecipeRegistry.addNewCrusherRecipe(Blocks.RED_SANDSTONE, new ItemStack(Blocks.SAND,4,1));
		CrusherRecipeRegistry.addNewCrusherRecipe(Blocks.GLOWSTONE, new ItemStack(Items.GLOWSTONE_DUST,4));
		CrusherRecipeRegistry.addNewCrusherRecipe("oreLapis", new ItemStack(Items.DYE,8,4));
		CrusherRecipeRegistry.addNewCrusherRecipe(Blocks.REDSTONE_ORE, new ItemStack(Items.REDSTONE,4));
		CrusherRecipeRegistry.addNewCrusherRecipe(Blocks.REDSTONE_BLOCK, new ItemStack(Items.REDSTONE,9));
		CrusherRecipeRegistry.addNewCrusherRecipe(Items.REEDS, new ItemStack(Items.SUGAR,2));
		CrusherRecipeRegistry.addNewCrusherRecipe(Items.BONE, new ItemStack(Items.DYE,3,15));
		CrusherRecipeRegistry.addNewCrusherRecipe(Items.BLAZE_ROD, new ItemStack(Items.BLAZE_POWDER,2));
		CrusherRecipeRegistry.addNewCrusherRecipe("oreQuartz", new ItemStack(Items.QUARTZ,2));
		CrusherRecipeRegistry.addNewCrusherRecipe("blockQuartz", new ItemStack(Items.QUARTZ,4));
		CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(Blocks.PRISMARINE,1,0), new ItemStack(Items.PRISMARINE_SHARD,4));
		CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(Blocks.PRISMARINE,1,1), new ItemStack(Items.PRISMARINE_SHARD,9));
		CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(Blocks.PRISMARINE,1,2), new ItemStack(Items.PRISMARINE_SHARD,8));
		CrusherRecipeRegistry.addNewCrusherRecipe(Blocks.SEA_LANTERN, new ItemStack(Items.PRISMARINE_CRYSTALS,3));
		CrusherRecipeRegistry.addNewCrusherRecipe(Blocks.SLIME_BLOCK, new ItemStack(Items.SLIME_BALL,9));
		
	}
	
	private static void initVanillaRecipes(){
		OreDictionary.registerOre("barsIron", Blocks.IRON_BARS);
		OreDictionary.registerOre("bars", Blocks.IRON_BARS);
		OreDictionary.registerOre("doorIron", Items.IRON_DOOR);
		OreDictionary.registerOre("door", Items.IRON_DOOR);
		OreDictionary.registerOre("doorWood", Items.OAK_DOOR);
		OreDictionary.registerOre("doorWood", Items.JUNGLE_DOOR);
		OreDictionary.registerOre("doorWood", Items.SPRUCE_DOOR);
		OreDictionary.registerOre("doorWood", Items.DARK_OAK_DOOR);
		OreDictionary.registerOre("doorWood", Items.BIRCH_DOOR);
		OreDictionary.registerOre("doorWood", Items.ACACIA_DOOR);
		OreDictionary.registerOre("door", Items.OAK_DOOR);
		OreDictionary.registerOre("door", Items.JUNGLE_DOOR);
		OreDictionary.registerOre("door", Items.SPRUCE_DOOR);
		OreDictionary.registerOre("door", Items.DARK_OAK_DOOR);
		OreDictionary.registerOre("door", Items.BIRCH_DOOR);
		OreDictionary.registerOre("door", Items.ACACIA_DOOR);
		
		CrusherRecipeRegistry.addNewCrusherRecipe("oreIron", new ItemStack(cyano.basemetals.init.Items.iron_powder,2));
		CrusherRecipeRegistry.addNewCrusherRecipe("blockIron", new ItemStack(cyano.basemetals.init.Items.iron_powder,9));
		CrusherRecipeRegistry.addNewCrusherRecipe("ingotIron", new ItemStack(cyano.basemetals.init.Items.iron_powder,1));
		CrusherRecipeRegistry.addNewCrusherRecipe("oreGold", new ItemStack(cyano.basemetals.init.Items.gold_powder,2));
		CrusherRecipeRegistry.addNewCrusherRecipe("blockGold", new ItemStack(cyano.basemetals.init.Items.gold_powder,9));
		CrusherRecipeRegistry.addNewCrusherRecipe("ingotGold", new ItemStack(cyano.basemetals.init.Items.gold_powder,1));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(cyano.basemetals.init.Items.iron_nugget,9), new ItemStack(Items.IRON_INGOT)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.IRON_INGOT), "xxx","xxx","xxx",'x',cyano.basemetals.init.Items.iron_nugget));
		GameRegistry.addSmelting(cyano.basemetals.init.Items.iron_powder, new ItemStack(Items.IRON_INGOT), 0f);
		GameRegistry.addSmelting(cyano.basemetals.init.Items.gold_powder, new ItemStack(Items.GOLD_INGOT), 0f);
		CrusherRecipeRegistry.addNewCrusherRecipe("oreCoal", new ItemStack(cyano.basemetals.init.Items.carbon_powder,2));
		CrusherRecipeRegistry.addNewCrusherRecipe("blockCoal", new ItemStack(cyano.basemetals.init.Items.carbon_powder,9));
		CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(Items.COAL,1,0), new ItemStack(cyano.basemetals.init.Items.carbon_powder,1));
		CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(Items.COAL,1,1), new ItemStack(cyano.basemetals.init.Items.carbon_powder,1));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.COAL_BLOCK), "xxx","xxx","xxx",'x',cyano.basemetals.init.Items.carbon_powder));
	}
	
	private static void initMetalRecipes(){
		List<MetalMaterial> exceptions = Arrays.asList(Materials.vanilla_iron,Materials.vanilla_gold,Materials.vanilla_diamond,Materials.vanilla_stone,Materials.vanilla_wood); 

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(cyano.basemetals.init.Items.wood_crackhammer), "x","/","/",'x',"logWood",'/',"stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(cyano.basemetals.init.Items.stone_crackhammer), "x","/","/",'x',net.minecraft.init.Blocks.STONEBRICK,'/',"stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(cyano.basemetals.init.Items.iron_crackhammer), "x","/","/",'x',"blockIron",'/',"stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(cyano.basemetals.init.Items.diamond_crackhammer), "x","/","/",'x',"blockDiamond",'/',"stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(cyano.basemetals.init.Blocks.iron_plate,3), "xxx",'x',"ingotIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(cyano.basemetals.init.Blocks.gold_plate,3), "xxx",'x',"ingotGold"));
		
		for(MetalMaterial metal : Materials.getAllMetals()){
			if(exceptions.contains(metal))continue;
			String baseName = metal.getName()+"_";
			String oreDictName = metal.getCapitalizedName();
			Item axe = cyano.basemetals.init.Items.getItemByName(baseName+"axe");
			Item blend = cyano.basemetals.init.Items.getItemByName(baseName+"blend");
			Item boots = cyano.basemetals.init.Items.getItemByName(baseName+"boots");
			Item chestplate = cyano.basemetals.init.Items.getItemByName(baseName+"chestplate");
			Item crackhammer = cyano.basemetals.init.Items.getItemByName(baseName+"crackhammer");
			Item door = cyano.basemetals.init.Items.getItemByName(baseName+"door_item");
			Item helmet = cyano.basemetals.init.Items.getItemByName(baseName+"helmet");
			Item hoe = cyano.basemetals.init.Items.getItemByName(baseName+"hoe");
			Item ingot = cyano.basemetals.init.Items.getItemByName(baseName+"ingot");
			Item leggings = cyano.basemetals.init.Items.getItemByName(baseName+"leggings");
			Item nugget = cyano.basemetals.init.Items.getItemByName(baseName+"nugget");
			Item pickaxe = cyano.basemetals.init.Items.getItemByName(baseName+"pickaxe");
			Item powder = cyano.basemetals.init.Items.getItemByName(baseName+"powder");
			Item shovel = cyano.basemetals.init.Items.getItemByName(baseName+"shovel");
			Item sword = cyano.basemetals.init.Items.getItemByName(baseName+"sword");
			Item rod = cyano.basemetals.init.Items.getItemByName(baseName+"rod");
			Item gear = cyano.basemetals.init.Items.getItemByName(baseName+"gear");
			Block bars = cyano.basemetals.init.Blocks.getBlockByName(baseName+"bars");
			Block block = cyano.basemetals.init.Blocks.getBlockByName(baseName+"block");
			Block plate = cyano.basemetals.init.Blocks.getBlockByName(baseName+"plate");
			Block ore = cyano.basemetals.init.Blocks.getBlockByName(baseName+"ore");
			Block trapdoor = cyano.basemetals.init.Blocks.getBlockByName(baseName+"trapdoor");
			
			
			// NOTE: smelting XP is based on output item, not input item
			// ingot-related recipes 
			if(ore != null && powder != null){
				CrusherRecipeRegistry.addNewCrusherRecipe("ore"+oreDictName,new ItemStack(powder,2));
			}
			if(ore != null && ingot != null){
				GameRegistry.addSmelting(ore, new ItemStack(ingot,1), metal.getOreSmeltXP());
			}
			if(ingot != null && powder != null){
				CrusherRecipeRegistry.addNewCrusherRecipe("ingot"+oreDictName,new ItemStack(powder,1));
				GameRegistry.addSmelting(powder, new ItemStack(ingot,1), metal.getOreSmeltXP());
			}
			if(ingot != null && blend != null){
				GameRegistry.addSmelting(blend, new ItemStack(ingot,1), metal.getOreSmeltXP());
			}
			if(ingot != null && nugget != null){
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(nugget,9), new ItemStack(ingot)));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ingot), "xxx","xxx","xxx",'x',"nugget"+oreDictName));
			}
			if(ingot != null && block != null){
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ingot,9), new ItemStack(block)));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(block), "xxx","xxx","xxx",'x',"ingot"+oreDictName));
			}
			if(ingot != null && plate != null){
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(plate,3), "xxx",'x',"ingot"+oreDictName));
				GameRegistry.addSmelting(plate, new ItemStack(ingot,1), metal.getOreSmeltXP());
			}
			if(block != null && powder != null){
				CrusherRecipeRegistry.addNewCrusherRecipe("block"+oreDictName, new ItemStack(powder,9));
			}
			if(ingot != null && bars != null){
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(bars,16), "xxx","xxx",'x',"ingot"+oreDictName));
				OreDictionary.registerOre("bars", bars);
			}
			if(ingot != null && rod != null){
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(rod,4), "x","x",'x',"ingot"+oreDictName));
				OreDictionary.registerOre("stick"+oreDictName,rod);
				OreDictionary.registerOre("rod"+oreDictName,rod);
				OreDictionary.registerOre("rod",rod);
			}
			if(nugget != null && rod != null){
				GameRegistry.addSmelting(rod, new ItemStack(nugget,4), 0);
			}
			if(rod != null && bars != null){
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(bars,4), "xxx",'x',"rod"+oreDictName));
			}
			if(rod != null && ingot != null && gear != null){
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(gear,4), " x ","x/x"," x ",'x',"ingot"+oreDictName,'/',"rod"+oreDictName));
				OreDictionary.registerOre("gear"+oreDictName,gear);
				OreDictionary.registerOre("gear",gear);
				if(metal == Materials.steel)OreDictionary.registerOre("sprocket",gear);
			}
			if(ingot != null && door != null){
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(door,3), "xx","xx","xx",'x',"ingot"+oreDictName));
				OreDictionary.registerOre("door", door);
			}
			if(ingot != null && trapdoor != null){
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(trapdoor), "xx","xx",'x',"ingot"+oreDictName));
				OreDictionary.registerOre("trapdoor", trapdoor);
			}
			
			// armor and tools
			if(ingot != null && boots != null) GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(boots), "x x","x x",'x',"ingot"+oreDictName));
			if(ingot != null && helmet != null) GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(helmet), "xxx","x x",'x',"ingot"+oreDictName));
			if(ingot != null && chestplate != null) GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(chestplate), "x x","xxx","xxx",'x',"ingot"+oreDictName));
			if(ingot != null && leggings != null) GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(leggings), "xxx","x x","x x",'x',"ingot"+oreDictName));
			if(ingot != null && axe != null) GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(axe), "xx","x/"," /",'x',"ingot"+oreDictName,'/',"stickWood"));
			if(ingot != null && axe != null) GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(axe), "xx","/x","/ ",'x',"ingot"+oreDictName,'/',"stickWood"));
			if(block != null && crackhammer != null) GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(crackhammer), "x","/","/",'x',"block"+oreDictName,'/',"stickWood"));
			if(ingot != null && hoe != null) GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(hoe), "xx"," /"," /",'x',"ingot"+oreDictName,'/',"stickWood"));
			if(ingot != null && hoe != null) GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(hoe), "xx","/ ","/ ",'x',"ingot"+oreDictName,'/',"stickWood"));
			if(ingot != null && pickaxe != null) GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(pickaxe), "xxx"," / "," / ",'x',"ingot"+oreDictName,'/',"stickWood"));
			if(ingot != null && shovel != null) GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(shovel), "x","/","/",'x',"ingot"+oreDictName,'/',"stickWood"));
			if(ingot != null && sword != null) GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(sword),   "x","x","/",'x',"ingot"+oreDictName,'/',"stickWood"));
			
			// misc recipes
			if(ingot != null && pickaxe != null) GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.BUCKET), "x x"," x ",'x',"ingot"+oreDictName));
		}
		
		// alloy blends
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(cyano.basemetals.init.Items.brass_blend,3), "dustCopper","dustCopper","dustZinc"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(cyano.basemetals.init.Items.bronze_blend,4), "dustCopper","dustCopper","dustCopper","dustTin"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(cyano.basemetals.init.Items.steel_blend,8), "dustIron", "dustIron", "dustIron", "dustIron", "dustIron", "dustIron", "dustIron", "dustIron", "dustCarbon"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(cyano.basemetals.init.Items.steel_blend,8), "dustIron", "dustIron", "dustIron", "dustIron", "dustIron", "dustIron", "dustIron", "dustIron", "dustCoal"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(cyano.basemetals.init.Items.invar_blend,3), "dustIron","dustIron","dustNickel"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(cyano.basemetals.init.Items.cupronickel_blend,3), "dustCopper","dustCopper","dustNickel"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(cyano.basemetals.init.Items.electrum_blend,2), "dustSilver","dustGold"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(cyano.basemetals.init.Items.mithril_blend,3), "dustSilver","dustSilver","dustColdiron","ingotMercury"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(cyano.basemetals.init.Items.aquarium_blend,3), "dustCopper","dustCopper","dustZinc", Items.PRISMARINE_CRYSTALS, Items.PRISMARINE_CRYSTALS, Items.PRISMARINE_CRYSTALS));

		if(FluidRegistry.isUniversalBucketEnabled()) {
			UniversalBucket universal_bucket = ForgeModContainer.getInstance().universalBucket;
			ItemStack bucketMercury = new ItemStack(universal_bucket, 1, 0);
			universal_bucket.fill(bucketMercury, new FluidStack(Fluids.fluidMercury, universal_bucket.getCapacity()), true);
			GameRegistry.addRecipe(new ShapelessOreRecipe(bucketMercury, Items.BUCKET, "ingotMercury", "ingotMercury", "ingotMercury", "ingotMercury", "ingotMercury", "ingotMercury", "ingotMercury", "ingotMercury"));
		}
		// potions
		// TODO: potion recipes
		
		// misc recipes
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.ACTIVATOR_RAIL,6), "x/x","x*x","x/x",'x',"ingotSteel",'/',"stickWood",'*',Blocks.REDSTONE_TORCH));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.DETECTOR_RAIL,6), "x x","x-x","x*x",'x',"ingotSteel",'/',"stickWood",'-',Blocks.STONE_PRESSURE_PLATE, '*',"dustRedstone"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.RAIL,16), "x x","x/x","x x",'x',"ingotSteel",'/',"stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE), "xx",'x',"ingotSteel"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.TRIPWIRE_HOOK), "x  ","/  ","w  ",'x',"ingotSteel",'/',"stickWood",'w',"plankWood"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.FLINT_AND_STEEL), "ingotSteel",Items.FLINT));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.HOPPER), "x x","x/x"," x ",'x',"ingotSteel",'/',"chestWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.MINECART), "x x","xxx",'x',"ingotSteel"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.PISTON), "www","sxs","s*s",'x',"ingotSteel",'w',"plankWood",'s',"cobblestone",'*',"dustRedstone"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.SHEARS), "x "," x",'x',"ingotSteel"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.SHEARS), " x","x ",'x',"ingotSteel"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.SHIELD), "wxw","www", " w ",'w',"plankWood",'x',"ingotSteel"));
		
		CrusherRecipeRegistry.addNewCrusherRecipe("oreMercury",new ItemStack(cyano.basemetals.init.Items.mercury_powder,2));
		GameRegistry.addSmelting(cyano.basemetals.init.Items.mercury_powder, new ItemStack(cyano.basemetals.init.Items.mercury_ingot,1), 0);
		GameRegistry.addSmelting(cyano.basemetals.init.Blocks.mercury_ore, new ItemStack(cyano.basemetals.init.Items.mercury_ingot,1), 1);

		// new recipes using rods and gears
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.LEVER,1), "x","y",'x',"rod",'y', "cobblestone"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(cyano.basemetals.init.Blocks.human_detector,1), "xx","yy",'x',"ingotBrick",'y', "gear"));
		
	}

}
