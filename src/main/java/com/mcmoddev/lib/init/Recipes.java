package com.mcmoddev.lib.init;

import com.mcmoddev.basemetals.material.MetalMaterial;
import com.mcmoddev.basemetals.registry.CrusherRecipeRegistry;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.*;

/**
 *
 * @author DrCyano
 *
 */
public abstract class Recipes {

	static final Item PRISMARINE_CRYSTALS = net.minecraft.init.Items.PRISMARINE_CRYSTALS;
	static final Item PRISMARINE_SHARD = net.minecraft.init.Items.PRISMARINE_SHARD;
	static final Block PRISMARINE = net.minecraft.init.Blocks.PRISMARINE;

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

	// TODO
	private static void initPureVanillaCrusherRecipes() {
		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.STONE, new ItemStack(net.minecraft.init.Blocks.COBBLESTONE, 1));
		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.COBBLESTONE, new ItemStack(net.minecraft.init.Blocks.GRAVEL, 1));
		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.GRAVEL, new ItemStack(net.minecraft.init.Blocks.SAND, 1));
		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.SANDSTONE, new ItemStack(net.minecraft.init.Blocks.SAND, 4));
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.RED_SANDSTONE, new ItemStack(net.minecraft.init.Blocks.SAND, 4, 1));
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.GLOWSTONE, new ItemStack(net.minecraft.init.Items.GLOWSTONE_DUST, 4));
		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.ORELAPIS, new ItemStack(net.minecraft.init.Items.DYE, 8, 4));
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.REDSTONE_ORE, new ItemStack(net.minecraft.init.Items.REDSTONE, 4));
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.REDSTONE_BLOCK, new ItemStack(net.minecraft.init.Items.REDSTONE, 9));
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Items.REEDS, new ItemStack(net.minecraft.init.Items.SUGAR, 2));
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Items.BONE, new ItemStack(net.minecraft.init.Items.DYE, 3, 15));
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Items.BLAZE_ROD, new ItemStack(net.minecraft.init.Items.BLAZE_POWDER, 2));
		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.OREQUARTZ, new ItemStack(net.minecraft.init.Items.QUARTZ, 2));
		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.BLOCKQUARTZ, new ItemStack(net.minecraft.init.Items.QUARTZ, 4));
		CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(PRISMARINE, 1, 0), new ItemStack(PRISMARINE_SHARD, 4));
		CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(PRISMARINE, 1, 1), new ItemStack(PRISMARINE_SHARD, 9));
		CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(PRISMARINE, 1, 2), new ItemStack(PRISMARINE_SHARD, 8));
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.SEA_LANTERN, new ItemStack(PRISMARINE_CRYSTALS, 3));
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.SLIME_BLOCK, new ItemStack(net.minecraft.init.Items.SLIME_BALL, 9));
	}

	private static void initVanillaRecipes() {
	}

	private static void initGeneralRecipes() {

		for (final MetalMaterial material : Materials.getAllMaterials()) {
			if (material.isVanilla) {
				continue;
			}

			final String oreDictName = material.getCapitalizedName();
			// NOTE: smelting XP is based on output item, not input item
			final float oreSmeltXP = material.getOreSmeltXP();

			if (material.ore != null) {
				if (material.powder != null) {
					CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.ORE + oreDictName, new ItemStack(material.powder, 2));
				}

				if (material.ingot != null) {
					GameRegistry.addSmelting(material.ore, new ItemStack(material.ingot, 1), oreSmeltXP);
				}
			}

			if (material.block != null) {
				if (material.powder != null) {
					CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.BLOCK + oreDictName, new ItemStack(material.powder, 9));
				}

				if (material.slab != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.slab, 6), "xxx", 'x', Oredicts.BLOCK + oreDictName));
				}

				if ((material.rod != null) && (material.lever != null)) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.lever), "x", "y", 'x', Oredicts.ROD + oreDictName, 'y', Oredicts.BLOCK + oreDictName));
				}

				if (material.stairs != null) { // Crashes
					//GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.stairs, 4), "x", "xx", "xxx", 'x', BLOCK + oreDictName));
				}

				if (material.wall != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.wall, 6), "xxx", "xxx", 'x', Oredicts.BLOCK + oreDictName));
				}

				if ((material.crackhammer != null) && (!Options.DISABLE_ALL_HAMMERS)) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.crackhammer), "x", "/", "/", 'x', Oredicts.BLOCK + oreDictName, '/', Oredicts.STICKWOOD));
				}
			}

			if (material.ingot != null) {
				// armor and tools
				if (material.boots != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.boots), "x x", "x x", 'x', Oredicts.INGOT + oreDictName));
				}

				if (material.helmet != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.helmet), "xxx", "x x", 'x', Oredicts.INGOT + oreDictName));
				}

				if (material.chestplate != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.chestplate), "x x", "xxx", "xxx", 'x', Oredicts.INGOT + oreDictName));
				}

				if (material.leggings != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.leggings), "xxx", "x x", "x x", 'x', Oredicts.INGOT + oreDictName));
				}

				if (material.axe != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.axe), "xx", "x/", " /", 'x', Oredicts.INGOT + oreDictName, '/', Oredicts.STICKWOOD));
				}

				// if (material.axe != null) {
				// 	GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.axe), "xx", "/x", "/ ", 'x', Oredicts.INGOT + oreDictName, '/', Oredicts.STICKWOOD));
				// }

				if (material.hoe != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.hoe), "xx", " /", " /", 'x', Oredicts.INGOT + oreDictName, '/', Oredicts.STICKWOOD));
				}

				// if (material.hoe != null) {
				// 	GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.hoe), "xx", "/ ", "/ ", 'x', INGOT + oreDictName, '/', STICKWOOD));
				// }

				if (material.pickaxe != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.pickaxe), "xxx", " / ", " / ", 'x', Oredicts.INGOT + oreDictName, '/', Oredicts.STICKWOOD));
				}

				if (material.shovel != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.shovel), "x", "/", "/", 'x', Oredicts.INGOT + oreDictName, '/', Oredicts.STICKWOOD));
				}

				if (material.sword != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.sword), "x", "x", "/", 'x', Oredicts.INGOT + oreDictName, '/', Oredicts.STICKWOOD));
				}

				if (material.shears != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.shears), " x", "x ", 'x', Oredicts.INGOT + oreDictName));
				}

				if (material.powder != null) {
					CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.INGOT + oreDictName, new ItemStack(material.powder, 1));
					GameRegistry.addSmelting(material.powder, new ItemStack(material.ingot, 1), oreSmeltXP);
				}

				if (material.blend != null) {
					GameRegistry.addSmelting(material.blend, new ItemStack(material.ingot, 1), oreSmeltXP);
				}

				if (material.nugget != null) {
					GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material.nugget, 9), new ItemStack(material.ingot)));
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.ingot), "xxx", "xxx", "xxx", 'x', Oredicts.NUGGET + oreDictName));
				}

				if (material.block != null) {
					GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material.ingot, 9), new ItemStack(material.block)));
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.block), "xxx", "xxx", "xxx", 'x', Oredicts.INGOT + oreDictName));
				}

				if (material.plate != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.plate, 3), "xxx", 'x', Oredicts.INGOT + oreDictName));
					GameRegistry.addSmelting(material.plate, new ItemStack(material.ingot, 1), oreSmeltXP);
				}

				if (material.pressure_plate != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.pressure_plate), "xx", 'x', Oredicts.INGOT + oreDictName));
				}

				if (material.bars != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.bars, 16), "xxx", "xxx", 'x', Oredicts.INGOT + oreDictName));
					OreDictionary.registerOre(Oredicts.BARS, material.bars);
				}

				if (material.rod != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.rod, 4), "x", "x", 'x', Oredicts.INGOT + oreDictName));
					OreDictionary.registerOre(Oredicts.STICK + oreDictName, material.rod);
					OreDictionary.registerOre(Oredicts.ROD, material.rod);
				}

				if (material.door != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.door, 3), "xx", "xx", "xx", 'x', Oredicts.INGOT + oreDictName));
					OreDictionary.registerOre(Oredicts.DOOR, material.door);
				}

				if (material.shield != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.shield), "xyx", "xxx", " x ", 'y', Oredicts.PLANKWOOD, 'x', Oredicts.INGOT + oreDictName));
				}

				if (material.trapdoor != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.trapdoor), "xx", "xx", 'x', Oredicts.INGOT + oreDictName));
					OreDictionary.registerOre(Oredicts.TRAPDOOR, material.trapdoor);
				}

				if (material.horse_armor != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.horse_armor), "  x", "xyx", "xxx", 'x', Oredicts.INGOT + oreDictName, 'y', net.minecraft.init.Blocks.WOOL));
				}

				if ((material.rod != null) && (material.gear != null)) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.gear, 4), " x ", "x/x", " x ", 'x', Oredicts.INGOT + oreDictName, '/', Oredicts.ROD + oreDictName));
					OreDictionary.registerOre(Oredicts.GEAR, material.gear);

//					if (Options.ENABLE_STEEL) {
//						if (material == Materials.steel) {
//							OreDictionary.registerOre(Oredicts.SPROCKET, material.gear);
//						}
//					}
				}

				// misc recipes
				if (material.pickaxe != null) {
					// TODO
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Items.BUCKET), "x x", " x ", 'x', Oredicts.INGOT + oreDictName));
				}
			}

			if ((material.nugget != null) && (material.button != null)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.button), "x", "x", 'x', Oredicts.NUGGET + oreDictName));
			}

			if (material.rod != null) {
				if (material.nugget != null) {
					GameRegistry.addSmelting(material.rod, new ItemStack(material.nugget, 4), 0);
				}

				if (material.bars != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.bars, 4), "xxx", 'x', Oredicts.ROD + oreDictName));
				}

				if (material.arrow != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.arrow, 4), "x", "y", "z", 'x', Oredicts.NUGGET + oreDictName, 'y', Oredicts.ROD + oreDictName, 'z', net.minecraft.init.Items.FEATHER));
				}

				if (material.bow != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.bow), " xy", "x y", " xy", 'x', Oredicts.ROD + oreDictName, 'y', net.minecraft.init.Items.STRING));
				}

				if ((material.gear != null) && (material.crossbow != null)) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.crossbow), "zxx", " yx", "x z", 'x', Oredicts.ROD + oreDictName, 'y', Oredicts.GEAR + oreDictName, 'z', net.minecraft.init.Items.STRING));
				}

				if (material.bolt != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.bolt, 4), "x", "x", "y", 'x', Oredicts.ROD + oreDictName, 'y', net.minecraft.init.Items.FEATHER));
				}

				if (material.fishing_rod != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.fishing_rod), "  x", " xy", "x y", 'x', Oredicts.ROD + oreDictName, 'y', net.minecraft.init.Items.STRING));
				}
			}

			if ((material.blend != null) && (material.smallblend != null)) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material.smallblend, 9), new ItemStack(material.blend)));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.blend), "xxx", "xxx", "xxx", 'x', new ItemStack(material.smallblend)));
				if (material.nugget != null) {
					GameRegistry.addSmelting(material.smallblend, new ItemStack(material.nugget, 1), oreSmeltXP);
				}
			}

			if ((material.powder != null) && (material.smallpowder != null)) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material.smallpowder, 9), new ItemStack(material.powder)));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.powder), "xxx", "xxx", "xxx", 'x', new ItemStack(material.smallpowder)));
				if (material.nugget != null) {
					GameRegistry.addSmelting(material.smallpowder, new ItemStack(material.nugget, 1), oreSmeltXP);
					CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.NUGGET + oreDictName, new ItemStack(material.smallpowder, 1));
				}
			}
		}
	}

	private static void initModSpecificRecipes() {

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
