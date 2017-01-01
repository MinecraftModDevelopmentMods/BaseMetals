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
			CrusherRecipeRegistry.addNewCrusherRecipe("oreIron", new ItemStack(Materials.vanilla_iron.powder, 2));
			CrusherRecipeRegistry.addNewCrusherRecipe("blockIron", new ItemStack(Materials.vanilla_iron.powder, 9));
			CrusherRecipeRegistry.addNewCrusherRecipe("ingotIron", new ItemStack(Materials.vanilla_iron.powder, 1));

			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.vanilla_iron.nugget, 9), new ItemStack(net.minecraft.init.Items.IRON_INGOT)));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Items.IRON_INGOT), "xxx", "xxx", "xxx", 'x', Materials.vanilla_iron.nugget));
			GameRegistry.addSmelting(Materials.vanilla_iron.powder, new ItemStack(net.minecraft.init.Items.IRON_INGOT), 0f);
		}

		if (Options.ENABLE_GOLD) {		
			CrusherRecipeRegistry.addNewCrusherRecipe("oreGold", new ItemStack(Materials.vanilla_gold.powder, 2));
			CrusherRecipeRegistry.addNewCrusherRecipe("blockGold", new ItemStack(Materials.vanilla_gold.powder, 9));
			CrusherRecipeRegistry.addNewCrusherRecipe("ingotGold", new ItemStack(Materials.vanilla_gold.powder, 1));
			GameRegistry.addSmelting(Materials.vanilla_gold.powder, new ItemStack(net.minecraft.init.Items.GOLD_INGOT), 0f);
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
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Materials.vanilla_wood.crackhammer), "x", "/", "/", 'x', "logWood", '/', "stickWood"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Materials.vanilla_stone.crackhammer), "x", "/", "/", 'x', net.minecraft.init.Blocks.STONEBRICK, '/', "stickWood"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Materials.vanilla_iron.crackhammer), "x", "/", "/", 'x', "blockIron", '/', "stickWood"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Materials.vanilla_gold.crackhammer), "x", "/", "/", 'x', "blockGold", '/', "stickWood"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Materials.vanilla_diamond.crackhammer), "x", "/", "/", 'x', "blockDiamond", '/', "stickWood"));
		}
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Materials.vanilla_iron.plate, 3), "xxx", 'x', "ingotIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Materials.vanilla_gold.plate, 3), "xxx", 'x', "ingotGold"));

		Item v_rod = null;
		Item v_nugget = null;
		Item v_gear = null;
		Block v_bars = null;
		String v_oreDictName = "";

		// Iron items
		if (Options.ENABLE_IRON) {
			v_rod = Materials.vanilla_iron.rod;
			v_nugget = Materials.vanilla_iron.nugget;
			v_gear = Materials.vanilla_iron.gear;
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
			v_rod = Materials.vanilla_gold.rod;
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
			final Item arrow = metal.arrow;
			final Item axe = metal.axe;
			final Item blend = metal.blend;
			final Item boots = metal.boots;
			final Item bolt = metal.bolt;
			final Item bow = metal.bow;
			final Item chestplate = metal.chestplate;
			final Item crackhammer = metal.crackhammer;
			final Item crossbow = metal.crossbow;
			final Item door = metal.door;
			final Item fishingrod = metal.fishing_rod;
			final Item gear = metal.gear;
			final Item helmet = metal.helmet;
			final Item hoe = metal.hoe;
			final Item horse_armor = metal.horse_armor;
			final Item ingot = metal.ingot;
			final Item leggings = metal.leggings;
			final Item nugget = metal.nugget;
			final Item pickaxe = metal.pickaxe;
			final Item powder = metal.powder;
			final Item rod = metal.rod;
			final Item shears = metal.shears;
			final Item shield = metal.shield;
			final Item shovel = metal.shovel;
			final Item slab = metal.slab;
			final Item smallblend = metal.smallblend;
			final Item smallpowder = metal.smallpowder;
			final Item sword = metal.sword;
			final Block bars = metal.bars;
			final Block block = metal.block;
			final Block button = metal.button;
			final Block lever = metal.lever;
			final Block ore = metal.ore;
			final Block plate = metal.plate;
			final Block pressure_plate = metal.pressure_plate;
			final Block stairs = metal.stairs;
			final Block trapdoor = metal.trapdoor;
			final Block wall = metal.wall;

			// NOTE: smelting XP is based on output item, not input item
			if (ore != null) {
				if (powder != null) {
					CrusherRecipeRegistry.addNewCrusherRecipe("ore" + oreDictName, new ItemStack(powder, 2));
				}

				if (ingot != null) {
					GameRegistry.addSmelting(ore, new ItemStack(ingot, 1), metal.getOreSmeltXP());
				}
			}

			if (block != null) {
				if (powder != null) {
					CrusherRecipeRegistry.addNewCrusherRecipe("block" + oreDictName, new ItemStack(powder, 9));
				}

				if (slab != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(slab, 6), "xxx", 'x', "block" + oreDictName));
				}

				if ((rod != null) && (lever != null)) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(lever), "x", "y", 'x', "rod" + oreDictName, 'y', "block" + oreDictName));
				}

				if (stairs != null) { // Crashes
					//GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(stairs, 4), "x", "xx", "xxx", 'x', "block" + oreDictName));
				}

				if (wall != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(wall, 6), "xxx", "xxx", 'x', "block" + oreDictName));
				}

				if ((crackhammer != null) && (!Options.DISABLE_ALL_HAMMERS)) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(crackhammer), "x", "/", "/", 'x', "block" + oreDictName, '/', "stickWood"));
				}
			}

			if (ingot != null) {
				// armor and tools
				if (boots != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(boots), "x x", "x x", 'x', "ingot" + oreDictName));
				}

				if (helmet != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(helmet), "xxx", "x x", 'x', "ingot" + oreDictName));
				}

				if (chestplate != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(chestplate), "x x", "xxx", "xxx", 'x', "ingot" + oreDictName));
				}

				if (leggings != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(leggings), "xxx", "x x", "x x", 'x', "ingot" + oreDictName));
				}

				if (axe != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(axe), "xx", "x/", " /", 'x', "ingot" + oreDictName, '/', "stickWood"));
				}

				// if (axe != null) {
				// 	GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(axe), "xx", "/x", "/ ", 'x', "ingot" + oreDictName, '/', "stickWood"));
				// }

				if (hoe != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(hoe), "xx", " /", " /", 'x', "ingot" + oreDictName, '/', "stickWood"));
				}

				// if (hoe != null) {
				// 	GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(hoe), "xx", "/ ", "/ ", 'x', "ingot" + oreDictName, '/', "stickWood"));
				// }

				if (pickaxe != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(pickaxe), "xxx", " / ", " / ", 'x', "ingot" + oreDictName, '/', "stickWood"));
				}

				if (shovel != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(shovel), "x", "/", "/", 'x', "ingot" + oreDictName, '/', "stickWood"));
				}

				if (sword != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(sword), "x", "x", "/", 'x', "ingot" + oreDictName, '/', "stickWood"));
				}

				if (shears != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(shears), " x", "x ", 'x', "ingot" + oreDictName));
				}

				if (powder != null) {
					CrusherRecipeRegistry.addNewCrusherRecipe("ingot" + oreDictName, new ItemStack(powder, 1));
					GameRegistry.addSmelting(powder, new ItemStack(ingot, 1), metal.getOreSmeltXP());
				}

				if (blend != null) {
					GameRegistry.addSmelting(blend, new ItemStack(ingot, 1), metal.getOreSmeltXP());
				}

				if (nugget != null) {
					GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(nugget, 9), new ItemStack(ingot)));
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ingot), "xxx", "xxx", "xxx", 'x', "nugget" + oreDictName));
				}

				if (block != null) {
					GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ingot, 9), new ItemStack(block)));
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(block), "xxx", "xxx", "xxx", 'x', "ingot" + oreDictName));
				}

				if (plate != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(plate, 3), "xxx", 'x', "ingot" + oreDictName));
					GameRegistry.addSmelting(plate, new ItemStack(ingot, 1), metal.getOreSmeltXP());
				}

				if (pressure_plate != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(pressure_plate), "xx", 'x', "ingot" + oreDictName));
				}

				if (bars != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(bars, 16), "xxx", "xxx", 'x', "ingot" + oreDictName));
					OreDictionary.registerOre("bars", bars);
				}

				if (rod != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(rod, 4), "x", "x", 'x', "ingot" + oreDictName));
					OreDictionary.registerOre("stick" + oreDictName, rod);
					OreDictionary.registerOre("rod", rod);
				}

				if (door != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(door, 3), "xx", "xx", "xx", 'x', "ingot" + oreDictName));
					OreDictionary.registerOre("door", door);
				}

				if (shield != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(shield), "xyx", "xxx", " x ", 'y', "plankWood", 'x', "ingot" + oreDictName));
				}

				if (trapdoor != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(trapdoor), "xx", "xx", 'x', "ingot" + oreDictName));
					OreDictionary.registerOre("trapdoor", trapdoor);
				}

				if (horse_armor != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(horse_armor), "  x", "xyx", "xxx", 'x', "ingot" + oreDictName, 'y', net.minecraft.init.Blocks.WOOL));
				}

				if ((rod != null) && (gear != null)) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(gear, 4), " x ", "x/x", " x ", 'x', "ingot" + oreDictName, '/', "rod" + oreDictName));
					OreDictionary.registerOre("gear", gear);

					if (Options.ENABLE_STEEL) {
						if (metal == Materials.getMaterialByName("steel")) {
							OreDictionary.registerOre("sprocket", gear);
						}
					}
				}

				// misc recipes
				if (pickaxe != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Items.BUCKET), "x x", " x ", 'x', "ingot" + oreDictName));
				}
			}

			if ((nugget != null) && (button != null)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(button), "x", "x", 'x', "nugget" + oreDictName));
			}

			if (rod != null) {
				if (nugget != null) {
					GameRegistry.addSmelting(rod, new ItemStack(nugget, 4), 0);
				}

				if (bars != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(bars, 4), "xxx", 'x', "rod" + oreDictName));
				}

				if (arrow != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(arrow, 4), "x", "y", "z", 'x', "nugget" + oreDictName, 'y', "rod" + oreDictName, 'z', net.minecraft.init.Items.FEATHER));
				}

				if (bow != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(bow), " xy", "x y", " xy", 'x', "rod" + oreDictName, 'y', net.minecraft.init.Items.STRING));
				}

				if ((gear != null) && (crossbow != null)) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(crossbow), "zxx", " yx", "x z", 'x', "rod" + oreDictName, 'y', "gear" + oreDictName, 'z', net.minecraft.init.Items.STRING));
				}

				if (bolt != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(bolt, 4), "x", "x", "y", 'x', "rod" + oreDictName, 'y', net.minecraft.init.Items.FEATHER));
				}

				if (fishingrod != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(fishingrod), "  x", " xy", "x y", 'x', "rod" + oreDictName, 'y', net.minecraft.init.Items.STRING));
				}
			}

			if ((blend != null) && (smallblend != null)) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(smallblend, 9), new ItemStack(blend)));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blend), "xxx", "xxx", "xxx", 'x', new ItemStack(smallblend)));
				if (nugget != null) {
					GameRegistry.addSmelting(smallblend, new ItemStack(nugget, 1), metal.getOreSmeltXP());
				}
			}

			if ((powder != null) && (smallpowder != null)) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(smallpowder, 9), new ItemStack(powder)));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(powder), "xxx", "xxx", "xxx", 'x', new ItemStack(smallpowder)));
				if (nugget != null) {
					GameRegistry.addSmelting(smallpowder, new ItemStack(nugget, 1), metal.getOreSmeltXP());
					CrusherRecipeRegistry.addNewCrusherRecipe("nugget" + oreDictName, new ItemStack(smallpowder, 1));
				}
			}
		}
	}

	private static void initModSpecificRecipes() {
		// alloy blends
		// TODO: Fix this
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.brass.blend, 3), "dustCopper", "dustCopper", "dustZinc"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.bronze.blend, 4), "dustCopper", "dustCopper", "dustCopper", "dustTin"));
//		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.steel.blend, 8), "dustIron", "dustIron", "dustIron", "dustIron", "dustIron", "dustIron", "dustIron", "dustIron", "dustCarbon"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.steel.blend, 8), "dustIron", "dustIron", "dustIron", "dustIron", "dustIron", "dustIron", "dustIron", "dustIron", "dustCoal"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.invar.blend, 3), "dustIron", "dustIron", "dustNickel"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.cupronickel.blend, 4), "dustCopper", "dustCopper", "dustCopper", "dustNickel"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.electrum.blend, 2), "dustSilver", "dustGold"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.mithril.blend, 3), "dustSilver", "dustSilver", "dustColdiron", "ingotMercury"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.aquarium.blend, 3), "dustCopper", "dustCopper", "dustZinc", net.minecraft.init.Items.PRISMARINE_CRYSTALS, net.minecraft.init.Items.PRISMARINE_CRYSTALS, net.minecraft.init.Items.PRISMARINE_CRYSTALS));

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
	 * @param oreDictEntries An array of oredict entries
	 * @param name The block they are for
	 */
	public static void addOredicts(String[] oreDictEntries, Block name) {
		addOredicts(oreDictEntries, new ItemStack(name));
		// for (int i = 0; i < oreDictEntries.length; i++)
		//	OreDictionary.registerOre(oreDictEntries[i], name);
	}

	/**
	 *
	 * @param oreDictEntries An array of oredict entries
	 * @param name The item they are for
	 */
	public static void addOredicts(String[] oreDictEntries, Item name) {
		addOredicts(oreDictEntries, new ItemStack(name));
		// for (int i = 0; i < oreDictEntries.length; i++)
		//	OreDictionary.registerOre(oreDictEntries[i], name);
	}

	/**
	 *
	 * @param oreDictEntries An array of oredict entries
	 * @param itemStackName Itemstacy they are for
	 */
	public static void addOredicts(String[] oreDictEntries, ItemStack itemStackName) {
		// for (int i = 0; i < oreDictEntries.length; i++)
		//	OreDictionary.registerOre(oreDictEntries[i], itemStackName);
		for (final String oreDictEntry : oreDictEntries) {
			OreDictionary.registerOre(oreDictEntry, itemStackName);
		}
	}
}
