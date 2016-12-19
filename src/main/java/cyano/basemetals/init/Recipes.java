package cyano.basemetals.init;

import java.util.*;

import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.CrusherRecipeRegistry;
import cyano.basemetals.util.Config.Options;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.*;

/**
 *
 * @author DrCyano
 *
 */
public abstract class Recipes {

	private static boolean initDone = false;

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		Materials.init();
		Blocks.init();
		Items.init();

		initPureVanillaCrusherRecipes();
		initVanillaRecipes();
		initGeneralRecipes();
		initModSpecificRecipes();

		initDone = true;
	}

	private static void initPureVanillaCrusherRecipes() {
		// see net.minecraftforge.oredict.OreDictionary.initVanillaEntries() for vanilla oreDict names
		CrusherRecipeRegistry.addNewCrusherRecipe("stone", new ItemStack(net.minecraft.init.Blocks.COBBLESTONE, 1));
		CrusherRecipeRegistry.addNewCrusherRecipe("cobblestone", new ItemStack(net.minecraft.init.Blocks.GRAVEL, 1));
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.GRAVEL, new ItemStack(net.minecraft.init.Blocks.SAND, 1));
		CrusherRecipeRegistry.addNewCrusherRecipe("sandstone", new ItemStack(net.minecraft.init.Blocks.SAND, 4));
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.RED_SANDSTONE, new ItemStack(net.minecraft.init.Blocks.SAND, 4, 1));
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.GLOWSTONE, new ItemStack(net.minecraft.init.Items.GLOWSTONE_DUST, 4));
		CrusherRecipeRegistry.addNewCrusherRecipe("oreLapis", new ItemStack(net.minecraft.init.Items.DYE, 8, 4));
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.REDSTONE_ORE, new ItemStack(net.minecraft.init.Items.REDSTONE, 4));
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.REDSTONE_BLOCK, new ItemStack(net.minecraft.init.Items.REDSTONE, 9));
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Items.REEDS, new ItemStack(net.minecraft.init.Items.SUGAR, 2));
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Items.BONE, new ItemStack(net.minecraft.init.Items.DYE, 3, 15));
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Items.BLAZE_ROD, new ItemStack(net.minecraft.init.Items.BLAZE_POWDER, 2));
		CrusherRecipeRegistry.addNewCrusherRecipe("oreQuartz", new ItemStack(net.minecraft.init.Items.QUARTZ, 2));
		CrusherRecipeRegistry.addNewCrusherRecipe("blockQuartz", new ItemStack(net.minecraft.init.Items.QUARTZ, 4));
		CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(net.minecraft.init.Blocks.PRISMARINE, 1, 0), new ItemStack(net.minecraft.init.Items.PRISMARINE_SHARD, 4));
		CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(net.minecraft.init.Blocks.PRISMARINE, 1, 1), new ItemStack(net.minecraft.init.Items.PRISMARINE_SHARD, 9));
		CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(net.minecraft.init.Blocks.PRISMARINE, 1, 2), new ItemStack(net.minecraft.init.Items.PRISMARINE_SHARD, 8));
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.SEA_LANTERN, new ItemStack(net.minecraft.init.Items.PRISMARINE_CRYSTALS, 3));
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.SLIME_BLOCK, new ItemStack(net.minecraft.init.Items.SLIME_BALL, 9));
	}

	private static void initVanillaRecipes() {
		OreDictionary.registerOre("barsIron", net.minecraft.init.Blocks.IRON_BARS);
		OreDictionary.registerOre("bars", net.minecraft.init.Blocks.IRON_BARS);
		OreDictionary.registerOre("doorIron", net.minecraft.init.Items.IRON_DOOR);
		OreDictionary.registerOre("door", net.minecraft.init.Items.IRON_DOOR);
		OreDictionary.registerOre("doorWood", net.minecraft.init.Items.OAK_DOOR);
		OreDictionary.registerOre("doorWood", net.minecraft.init.Items.JUNGLE_DOOR);
		OreDictionary.registerOre("doorWood", net.minecraft.init.Items.SPRUCE_DOOR);
		OreDictionary.registerOre("doorWood", net.minecraft.init.Items.DARK_OAK_DOOR);
		OreDictionary.registerOre("doorWood", net.minecraft.init.Items.BIRCH_DOOR);
		OreDictionary.registerOre("doorWood", net.minecraft.init.Items.ACACIA_DOOR);
		OreDictionary.registerOre("door", net.minecraft.init.Items.OAK_DOOR);
		OreDictionary.registerOre("door", net.minecraft.init.Items.JUNGLE_DOOR);
		OreDictionary.registerOre("door", net.minecraft.init.Items.SPRUCE_DOOR);
		OreDictionary.registerOre("door", net.minecraft.init.Items.DARK_OAK_DOOR);
		OreDictionary.registerOre("door", net.minecraft.init.Items.BIRCH_DOOR);
		OreDictionary.registerOre("door", net.minecraft.init.Items.ACACIA_DOOR);

		if (Options.ENABLE_IRON) {
			CrusherRecipeRegistry.addNewCrusherRecipe("oreIron", new ItemStack(Items.iron_powder, 2));
			CrusherRecipeRegistry.addNewCrusherRecipe("blockIron", new ItemStack(Items.iron_powder, 9));
			CrusherRecipeRegistry.addNewCrusherRecipe("ingotIron", new ItemStack(Items.iron_powder, 1));

			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.iron_nugget, 9), new ItemStack(net.minecraft.init.Items.IRON_INGOT)));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Items.IRON_INGOT), "xxx", "xxx", "xxx", 'x', Items.iron_nugget));
			GameRegistry.addSmelting(Items.iron_powder, new ItemStack(net.minecraft.init.Items.IRON_INGOT), 0f);
		}

		if (Options.ENABLE_GOLD) {		
			CrusherRecipeRegistry.addNewCrusherRecipe("oreGold", new ItemStack(Items.gold_powder, 2));
			CrusherRecipeRegistry.addNewCrusherRecipe("blockGold", new ItemStack(Items.gold_powder, 9));
			CrusherRecipeRegistry.addNewCrusherRecipe("ingotGold", new ItemStack(Items.gold_powder, 1));
			GameRegistry.addSmelting(Items.gold_powder, new ItemStack(net.minecraft.init.Items.GOLD_INGOT), 0f);
		}

		if (Options.ENABLE_CHARCOAL) {
			// CrusherRecipeRegistry.addNewCrusherRecipe("oreCharcoal", new ItemStack(Items.charcoal_powder, 2));
//			CrusherRecipeRegistry.addNewCrusherRecipe("blockCharcoal", new ItemStack(Items.charcoal_powder, 9));
			//CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(net.minecraft.init.Items.COAL, 1, 0), new ItemStack(Items.charcoal_powder, 1));
			CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(net.minecraft.init.Items.COAL, 1, 1), new ItemStack(Items.charcoal_powder, 1));
			//GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Blocks.COAL_BLOCK), "xxx", "xxx", "xxx", 'x', Items.charcoal_powder));
		}

		if (Options.ENABLE_COAL) {
			CrusherRecipeRegistry.addNewCrusherRecipe("oreCoal", new ItemStack(Items.coal_powder, 2));
			CrusherRecipeRegistry.addNewCrusherRecipe("blockCoal", new ItemStack(Items.coal_powder, 9));
			CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(net.minecraft.init.Items.COAL, 1, 0), new ItemStack(Items.coal_powder, 1));
			//CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(net.minecraft.init.Items.COAL, 1, 1), new ItemStack(Items.coal_powder, 1));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Blocks.COAL_BLOCK), "xxx", "xxx", "xxx", 'x', Items.coal_powder));
		}
	}

	private static void initGeneralRecipes() {
		final List<MetalMaterial> exceptions = Arrays.asList(Materials.vanilla_iron, Materials.vanilla_gold, Materials.vanilla_diamond, Materials.vanilla_stone, Materials.vanilla_wood);

		if (!Options.DISABLE_ALL_HAMMERS) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.wood_crackhammer), "x", "/", "/", 'x', "logWood", '/', "stickWood"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.stone_crackhammer), "x", "/", "/", 'x', net.minecraft.init.Blocks.STONEBRICK, '/', "stickWood"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.iron_crackhammer), "x", "/", "/", 'x', "blockIron", '/', "stickWood"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.gold_crackhammer), "x", "/", "/", 'x', "blockGold", '/', "stickWood"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.diamond_crackhammer), "x", "/", "/", 'x', "blockDiamond", '/', "stickWood"));
		}
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.iron_plate, 3), "xxx", 'x', "ingotIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.gold_plate, 3), "xxx", 'x', "ingotGold"));

		Item v_rod = null;
		Item v_nugget = null;
		Item v_gear = null;
		Block v_bars = null;
		String v_oreDictName = "";

		// Iron items
		if (Options.ENABLE_IRON) {
			v_rod = Items.iron_rod;
			v_nugget = Items.iron_nugget;
			v_gear = Items.iron_gear;
			v_bars = net.minecraft.init.Blocks.IRON_BARS;
			v_oreDictName = "Iron";
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(v_rod, 4), "x", "x", 'x', "ingot" + v_oreDictName));
			OreDictionary.registerOre("stick" + v_oreDictName, v_rod);
			OreDictionary.registerOre("rod" + v_oreDictName, v_rod);
			OreDictionary.registerOre("rod", v_rod);
			GameRegistry.addSmelting(v_rod, new ItemStack(v_nugget, 4), 0);
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(v_bars, 4), "xxx", 'x', "rod" + v_oreDictName));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(v_gear, 4), " x ", "x/x", " x ", 'x', "ingot" + v_oreDictName, '/', "rod" + v_oreDictName));
			OreDictionary.registerOre("gear" + v_oreDictName, v_gear);
			OreDictionary.registerOre("gear", v_gear);
		}

		// Gold items
		if (Options.ENABLE_GOLD) {
			v_rod = Items.gold_rod;
			v_nugget = net.minecraft.init.Items.GOLD_NUGGET;
			v_oreDictName = "Gold";
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(v_rod, 4), "x", "x", 'x', "ingot" + v_oreDictName));
			OreDictionary.registerOre("stick" + v_oreDictName, v_rod);
			OreDictionary.registerOre("rod" + v_oreDictName, v_rod);
			OreDictionary.registerOre("rod", v_rod);
			GameRegistry.addSmelting(v_rod, new ItemStack(v_nugget, 4), 0);
		}

		for (final MetalMaterial metal : Materials.getAllMaterials()) {
			if (exceptions.contains(metal)) {
				continue;
			}

			final String oreDictName = metal.getCapitalizedName();
			final Item axe = metal.axe;
			final Item blend = metal.blend;
			final Item boots = metal.boots;
			final Item chestplate = metal.chestplate;
			final Item crackhammer = metal.crackhammer;
			final Item door = metal.door;
			final Item helmet = metal.helmet;
			final Item hoe = metal.hoe;
			final Item ingot = metal.ingot;
			final Item leggings = metal.leggings;
			final Item nugget = metal.nugget;
			final Item pickaxe = metal.pickaxe;
			final Item powder = metal.powder;
			final Item shovel = metal.shovel;
			final Item sword = metal.sword;
			final Item rod = metal.rod;
			final Item gear = metal.gear;
			final Block bars = metal.bars;
			final Block block = metal.block;
			final Block plate = metal.plate;
			final Block ore = metal.ore;
			final Block trapdoor = metal.trapdoor;

			final Item arrow = metal.arrow;
			final Item bow = metal.bow;
			final Item bolt = metal.bolt;
			final Item crossbow = metal.crossbow;
			final Item shears = metal.shears;
			final Item smallblend = metal.smallblend;
			final Item smallpowder = metal.smallpowder;
			final Item fishingrod = metal.fishing_rod;
			final Item horsearmor = metal.horsearmor;

			final Block button = metal.button;
			final Item slab = metal.slab;
			final Block lever = metal.lever;
			final Block pressure_plate = metal.pressure_plate;
			final Block stairs = metal.stairs;
			final Block wall = metal.wall;

			// NOTE: smelting XP is based on output item, not input item
			// ingot-related recipes
			if ((ore != null) && (powder != null)) {
				CrusherRecipeRegistry.addNewCrusherRecipe("ore" + oreDictName, new ItemStack(powder, 2));
			}

			if ((ore != null) && (ingot != null)) {
				GameRegistry.addSmelting(ore, new ItemStack(ingot, 1), metal.getOreSmeltXP());
			}

			if ((ingot != null) && (powder != null)) {
				CrusherRecipeRegistry.addNewCrusherRecipe("ingot" + oreDictName, new ItemStack(powder, 1));
				GameRegistry.addSmelting(powder, new ItemStack(ingot, 1), metal.getOreSmeltXP());
			}

			if ((ingot != null) && (blend != null)) {
				GameRegistry.addSmelting(blend, new ItemStack(ingot, 1), metal.getOreSmeltXP());
			}

			if ((ingot != null) && (nugget != null)) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(nugget, 9), new ItemStack(ingot)));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ingot), "xxx", "xxx", "xxx", 'x', "nugget" + oreDictName));
			}

			if ((ingot != null) && (block != null)) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ingot, 9), new ItemStack(block)));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(block), "xxx", "xxx", "xxx", 'x', "ingot" + oreDictName));
			}

			if ((ingot != null) && (plate != null)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(plate, 3), "xxx", 'x', "ingot" + oreDictName));
				GameRegistry.addSmelting(plate, new ItemStack(ingot, 1), metal.getOreSmeltXP());
			}

			if ((block != null) && (powder != null)) {
				CrusherRecipeRegistry.addNewCrusherRecipe("block" + oreDictName, new ItemStack(powder, 9));
			}

			if ((ingot != null) && (bars != null)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(bars, 16), "xxx", "xxx", 'x', "ingot" + oreDictName));
				OreDictionary.registerOre("bars", bars);
			}

			if ((ingot != null) && (rod != null)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(rod, 4), "x", "x", 'x', "ingot" + oreDictName));
				OreDictionary.registerOre("stick" + oreDictName, rod);
				OreDictionary.registerOre("rod", rod);
			}

			if ((nugget != null) && (rod != null)) {
				GameRegistry.addSmelting(rod, new ItemStack(nugget, 4), 0);
			}

			if ((rod != null) && (bars != null)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(bars, 4), "xxx", 'x', "rod" + oreDictName));
			}

			if ((rod != null) && (ingot != null) && (gear != null)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(gear, 4), " x ", "x/x", " x ", 'x', "ingot" + oreDictName, '/', "rod" + oreDictName));
				OreDictionary.registerOre("gear", gear);

				if (metal == Materials.steel) {
					OreDictionary.registerOre("sprocket", gear);
				}
			}

			if ((ingot != null) && (door != null)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(door, 3), "xx", "xx", "xx", 'x', "ingot" + oreDictName));
				OreDictionary.registerOre("door", door);
			}

			if ((ingot != null) && (trapdoor != null)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(trapdoor), "xx", "xx", 'x', "ingot" + oreDictName));
				OreDictionary.registerOre("trapdoor", trapdoor);
			}

			if ((blend != null) && (smallblend != null)) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(smallblend, 9), new ItemStack(blend)));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blend), "xxx", "xxx", "xxx", 'x', new ItemStack(smallblend)));
				GameRegistry.addSmelting(smallblend, new ItemStack(nugget, 1), metal.getOreSmeltXP());
			}

			if ((powder != null) && (smallpowder != null)) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(smallpowder, 9), new ItemStack(powder)));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(powder), "xxx", "xxx", "xxx", 'x', new ItemStack(smallpowder)));
				if (nugget != null) {
					GameRegistry.addSmelting(smallpowder, new ItemStack(nugget, 1), metal.getOreSmeltXP());
					CrusherRecipeRegistry.addNewCrusherRecipe("nugget" + oreDictName, new ItemStack(smallpowder, 1));
				}
			}

			// armor and tools
			if ((ingot != null) && (boots != null)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(boots), "x x", "x x", 'x', "ingot" + oreDictName));
			}

			if ((ingot != null) && (helmet != null)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(helmet), "xxx", "x x", 'x', "ingot" + oreDictName));
			}

			if ((ingot != null) && (chestplate != null)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(chestplate), "x x", "xxx", "xxx", 'x', "ingot" + oreDictName));
			}

			if ((ingot != null) && (leggings != null)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(leggings), "xxx", "x x", "x x", 'x', "ingot" + oreDictName));
			}

			if ((ingot != null) && (axe != null)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(axe), "xx", "x/", " /", 'x', "ingot" + oreDictName, '/', "stickWood"));
			}

			// if ((ingot != null) && (axe != null)) {
			// 	GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(axe), "xx", "/x", "/ ", 'x', "ingot" + oreDictName, '/', "stickWood"));
			// }

			if ((block != null) && (crackhammer != null) && (!Options.DISABLE_ALL_HAMMERS)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(crackhammer), "x", "/", "/", 'x', "block" + oreDictName, '/', "stickWood"));
			}

			if ((ingot != null) && (hoe != null)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(hoe), "xx", " /", " /", 'x', "ingot" + oreDictName, '/', "stickWood"));
			}

			// if ((ingot != null) && (hoe != null)) {
			// 	GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(hoe), "xx", "/ ", "/ ", 'x', "ingot" + oreDictName, '/', "stickWood"));
			// }

			if ((ingot != null) && (pickaxe != null)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(pickaxe), "xxx", " / ", " / ", 'x', "ingot" + oreDictName, '/', "stickWood"));
			}

			if ((ingot != null) && (shovel != null)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(shovel), "x", "/", "/", 'x', "ingot" + oreDictName, '/', "stickWood"));
			}

			if ((ingot != null) && (sword != null)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(sword), "x", "x", "/", 'x', "ingot" + oreDictName, '/', "stickWood"));
			}

			if ((ingot != null) && (shears != null)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(shears), " x", "x ", 'x', "ingot" + oreDictName));
			}

			if ((rod != null) && (fishingrod != null)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(fishingrod), "  x", " xy", "x y", 'x', "rod" + oreDictName, 'y', net.minecraft.init.Items.STRING));
			}

			if ((ingot != null) && (horsearmor != null)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(horsearmor), "  x", "xyx", "xxx", 'x', "ingot" + oreDictName, 'y', net.minecraft.init.Blocks.WOOL));
			}

			// Bows and Crossbows
			if ((rod != null) && (arrow != null)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(arrow, 4), "x", "y", "z", 'x', "nugget" + oreDictName, 'y', "rod" + oreDictName, 'z', net.minecraft.init.Items.FEATHER));
			}

			if ((rod != null) && (bow != null)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(bow), " xy", "x y", " xy", 'x', "rod" + oreDictName, 'y', net.minecraft.init.Items.STRING));
			}

			if ((rod != null) && (gear != null) && (crossbow != null)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(crossbow), "zxx", " yx", "x z", 'x', "rod" + oreDictName, 'y', "gear" + oreDictName, 'z', net.minecraft.init.Items.STRING));
			}

			if ((rod != null) && (bolt != null)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(bolt, 4), "x", "x", "y", 'x', "rod" + oreDictName, 'y', net.minecraft.init.Items.FEATHER));
			}

			if (nugget != null) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(button), "x", "x", 'x', "nugget" + oreDictName));
			}

			if (block != null) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(slab, 6), "xxx", 'x', "block" + oreDictName));
			}

			if ((block != null) && (rod != null)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(lever), "x", "y", 'x', "rod" + oreDictName, 'y', "block" + oreDictName));
			}

			if (ingot != null) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(pressure_plate), "xx", 'x', "ingot" + oreDictName));
			}

			// if (block != null) { // Crashes
			// 	GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(stairs, 4), "x", "xx", "xxx", 'x', "block" + oreDictName));
			// }

			if (block != null) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(wall, 6), "xxx", "xxx", 'x', "block" + oreDictName));
			}

			// misc recipes
			if ((ingot != null) && (pickaxe != null)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Items.BUCKET), "x x", " x ", 'x', "ingot" + oreDictName));
			}
		}
	}

	private static void initModSpecificRecipes() {
		// alloy blends
		// TODO: Fix this
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.brass_blend, 3), "dustCopper", "dustCopper", "dustZinc"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.bronze_blend, 4), "dustCopper", "dustCopper", "dustCopper", "dustTin"));
//		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.steel_blend, 8), "dustIron", "dustIron", "dustIron", "dustIron", "dustIron", "dustIron", "dustIron", "dustIron", "dustCarbon"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.steel_blend, 8), "dustIron", "dustIron", "dustIron", "dustIron", "dustIron", "dustIron", "dustIron", "dustIron", "dustCoal"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.invar_blend, 3), "dustIron", "dustIron", "dustNickel"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.cupronickel_blend, 4), "dustCopper", "dustCopper", "dustCopper", "dustNickel"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.electrum_blend, 2), "dustSilver", "dustGold"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.mithril_blend, 3), "dustSilver", "dustSilver", "dustColdiron", "ingotMercury"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.aquarium_blend, 3), "dustCopper", "dustCopper", "dustZinc", net.minecraft.init.Items.PRISMARINE_CRYSTALS, net.minecraft.init.Items.PRISMARINE_CRYSTALS, net.minecraft.init.Items.PRISMARINE_CRYSTALS));

		if (Options.ENABLE_MERCURY) {
			if (FluidRegistry.isUniversalBucketEnabled()) {
				final UniversalBucket universal_bucket = ForgeModContainer.getInstance().universalBucket;
				final ItemStack bucketMercury = new ItemStack(universal_bucket, 1, 0);
				universal_bucket.fill(bucketMercury, new FluidStack(Fluids.fluidMercury, universal_bucket.getCapacity()), true);
				GameRegistry.addRecipe(new ShapelessOreRecipe(bucketMercury, net.minecraft.init.Items.BUCKET, "ingotMercury", "ingotMercury", "ingotMercury", "ingotMercury", "ingotMercury", "ingotMercury", "ingotMercury", "ingotMercury"));
			}
		}

		// potions
		// TODO: potion recipes

		// misc recipes
		if (Options.ENABLE_STEEL) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Blocks.ACTIVATOR_RAIL, 6), "x/x", "x*x", "x/x", 'x', "ingotSteel", '/', "stickWood", '*', net.minecraft.init.Blocks.REDSTONE_TORCH));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Blocks.DETECTOR_RAIL, 6), "x x", "x-x", "x*x", 'x', "ingotSteel", '/', "stickWood", '-', net.minecraft.init.Blocks.STONE_PRESSURE_PLATE, '*', "dustRedstone"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Blocks.RAIL, 16), "x x", "x/x", "x x", 'x', "ingotSteel", '/', "stickWood"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE), "xx", 'x', "ingotSteel"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Blocks.TRIPWIRE_HOOK), "x  ", "/  ", "w  ", 'x', "ingotSteel", '/', "stickWood", 'w', "plankWood"));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(net.minecraft.init.Items.FLINT_AND_STEEL), "ingotSteel", net.minecraft.init.Items.FLINT));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Blocks.HOPPER), "x x", "x/x", " x ", 'x', "ingotSteel", '/', "chestWood"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Items.MINECART), "x x", "xxx", 'x', "ingotSteel"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Blocks.PISTON), "www", "sxs", "s*s", 'x', "ingotSteel", 'w', "plankWood", 's', "cobblestone", '*', "dustRedstone"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Items.SHEARS), "x ", " x", 'x', "ingotSteel"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Items.SHEARS), " x", "x ", 'x', "ingotSteel"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Items.SHIELD), "wxw", "www", " w ", 'w', "plankWood", 'x', "ingotSteel"));
		}

		if (Options.ENABLE_MERCURY) {
			CrusherRecipeRegistry.addNewCrusherRecipe("oreMercury", new ItemStack(Items.mercury_powder, 2));
			GameRegistry.addSmelting(Items.mercury_powder, new ItemStack(Items.mercury_ingot, 1), 0);
			GameRegistry.addSmelting(Blocks.mercury_ore, new ItemStack(Items.mercury_ingot, 1), 1);
		}

		// new recipes using rods and gears
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Blocks.LEVER, 1), "x", "y", 'x', "rod", 'y', "cobblestone"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.human_detector, 1), "xx", "yy", 'x', "ingotBrick", 'y', "gear"));
	}

	/**
	 *
	 * @param oreDictEntries
	 * @param name
	 */
	public static void addOredicts(String[] oreDictEntries, Block name) {
		addOredicts(oreDictEntries, new ItemStack(name));
		// for (int i = 0; i < oreDictEntries.length; i++)
		//	OreDictionary.registerOre(oreDictEntries[i], name);
	}

	/**
	 *
	 * @param oreDictEntries
	 * @param name
	 */
	public static void addOredicts(String[] oreDictEntries, Item name) {
		addOredicts(oreDictEntries, new ItemStack(name));
		// for (int i = 0; i < oreDictEntries.length; i++)
		//	OreDictionary.registerOre(oreDictEntries[i], name);
	}

	/**
	 *
	 * @param oreDictEntries
	 * @param itemStackName
	 */
	public static void addOredicts(String[] oreDictEntries, ItemStack itemStackName) {
		// for (int i = 0; i < oreDictEntries.length; i++)
		//	OreDictionary.registerOre(oreDictEntries[i], itemStackName);
		for (final String oreDictEntry : oreDictEntries) {
			OreDictionary.registerOre(oreDictEntry, itemStackName);
		}
	}
}
