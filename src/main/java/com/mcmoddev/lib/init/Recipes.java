package com.mcmoddev.lib.init;

import org.apache.commons.lang3.StringUtils;

import com.mcmoddev.basemetals.util.Config;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.material.MMDMaterial.MaterialType;
import com.mcmoddev.lib.recipe.*;
import com.mcmoddev.lib.registry.CrusherRecipeRegistry;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public abstract class Recipes {

	private static boolean initDone = false;
	private static final String SORTLOC = "after:minecraft:shapeless";

	protected Recipes() {
		throw new IllegalAccessError("Not a instantiable class");
	}

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

		initPureVanillaOredicts();
		initPureVanillaCrusherRecipes();
		initVanillaRecipes();
		initGeneralRecipes();
		initModSpecificRecipes();

		initDone = true;
	}

	protected static void initPureVanillaOredicts() {
		OreDictionary.registerOre(Oredicts.BARS_IRON, net.minecraft.init.Blocks.IRON_BARS);
		OreDictionary.registerOre(Oredicts.BARS, net.minecraft.init.Blocks.IRON_BARS);
		OreDictionary.registerOre(Oredicts.DOOR_IRON, net.minecraft.init.Items.IRON_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR, net.minecraft.init.Items.IRON_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR_WOOD, net.minecraft.init.Items.OAK_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR_WOOD, net.minecraft.init.Items.JUNGLE_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR_WOOD, net.minecraft.init.Items.SPRUCE_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR_WOOD, net.minecraft.init.Items.DARK_OAK_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR_WOOD, net.minecraft.init.Items.BIRCH_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR_WOOD, net.minecraft.init.Items.ACACIA_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR, net.minecraft.init.Items.OAK_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR, net.minecraft.init.Items.JUNGLE_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR, net.minecraft.init.Items.SPRUCE_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR, net.minecraft.init.Items.DARK_OAK_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR, net.minecraft.init.Items.BIRCH_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR, net.minecraft.init.Items.ACACIA_DOOR);
	}

	protected static void initPureVanillaCrusherRecipes() {
		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.STONE, new ItemStack(net.minecraft.init.Blocks.COBBLESTONE, 1)); // Stone to Cobblestone
		// CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.STONE, new ItemStack(net.minecraft.init.Blocks.COBBLESTONE, 1)); // Stone to Cobblestone
		// CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(net.minecraft.init.Blocks.STONEBRICK, 1), new ItemStack(net.minecraft.init.Blocks.COBBLESTONE, 1)); // Stone Bricks to Cobblestone
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.STONEBRICK, new ItemStack(net.minecraft.init.Blocks.COBBLESTONE, 1)); // Stone Bricks to Cobblestone
		CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(net.minecraft.init.Blocks.STONE_SLAB, 1, 0), new ItemStack(net.minecraft.init.Blocks.STONE_SLAB, 1, 3)); // Stone Slab to Cobblestone Slab
		CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(net.minecraft.init.Blocks.STONE_SLAB, 1, 5), new ItemStack(net.minecraft.init.Blocks.STONE_SLAB, 1, 3)); // Stone Bricks Slab to Cobblestone Slab

		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.COBBLESTONE, new ItemStack(net.minecraft.init.Blocks.GRAVEL, 1)); // Cobblestone to Gravel
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.MOSSY_COBBLESTONE, new ItemStack(net.minecraft.init.Blocks.GRAVEL, 1)); // Mossy Cobblestone to Gravel
		CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(net.minecraft.init.Blocks.COBBLESTONE_WALL, 1, 0), new ItemStack(net.minecraft.init.Blocks.GRAVEL, 1)); // Cobblestone Wall to Gravel
		CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(net.minecraft.init.Blocks.COBBLESTONE_WALL, 1, 1), new ItemStack(net.minecraft.init.Blocks.GRAVEL, 1)); // Mossy Cobblestone Wall to Gravel

		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.GRAVEL, new ItemStack(net.minecraft.init.Blocks.SAND, 1)); // Gravel to Sand
		// CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(net.minecraft.init.Blocks.SANDSTONE, 1), new ItemStack(net.minecraft.init.Blocks.SAND, 4)); // Sandstone to Sand
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.SANDSTONE, new ItemStack(net.minecraft.init.Blocks.SAND, 4)); // Sandstone to Sand
		CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(net.minecraft.init.Blocks.STONE_SLAB, 1, 1), new ItemStack(net.minecraft.init.Blocks.SAND, 2)); // Sandstone Slab to 2 Sand
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.GLASS, new ItemStack(net.minecraft.init.Blocks.SAND, 1)); // Glass to Sand

		// CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(net.minecraft.init.Blocks.RED_SANDSTONE, 1), new ItemStack(net.minecraft.init.Blocks.SAND, 4, 1)); // Red Sandstone to Red Sand
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.RED_SANDSTONE, new ItemStack(net.minecraft.init.Blocks.SAND, 4, 1)); // Red Sandstone to Red Sand
		CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(net.minecraft.init.Blocks.STONE_SLAB2, 1, 0), new ItemStack(net.minecraft.init.Blocks.SAND, 2, 1)); // Red Sandstone Slab to 2 Red Sand
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.STAINED_GLASS, new ItemStack(net.minecraft.init.Blocks.SAND, 4, 1)); // Red Stained Glass to Red Sand

		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.GLOWSTONE, new ItemStack(net.minecraft.init.Items.GLOWSTONE_DUST, 4)); // Glowstone to 4 Glowstone Dust

		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.ORE_LAPIS, new ItemStack(net.minecraft.init.Items.DYE, 8, 4)); // Lapis Ore to 8 Lapis
		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.BLOCK_LAPIS, new ItemStack(net.minecraft.init.Items.DYE, 9, 4)); // Lapis Block to 9 Lapis

		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.ORE_REDSTONE, new ItemStack(net.minecraft.init.Items.REDSTONE, 8)); // Redstone Ore to 8 Redstone
		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.BLOCK_REDSTONE, new ItemStack(net.minecraft.init.Items.REDSTONE, 9)); // Redstone Block to 9 Redstone

		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Items.REEDS, new ItemStack(net.minecraft.init.Items.SUGAR, 2)); // Sugar Cane to 2 Sugar

		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.BONE_BLOCK, new ItemStack(net.minecraft.init.Items.DYE, 9, 15)); // Bone Block to 9 Bonemeal
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Items.BONE, new ItemStack(net.minecraft.init.Items.DYE, 3, 15)); // Bone to 3 Bonemeal

		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Items.BLAZE_ROD, new ItemStack(net.minecraft.init.Items.BLAZE_POWDER, 2)); // Blaze Rod to 2 Blaze Powder

		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.ORE_QUARTZ, new ItemStack(net.minecraft.init.Items.QUARTZ, 2)); // Nether Quartz Ore to 2 Quartz
		// CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.BLOCKQUARTZ, new ItemStack(net.minecraft.init.Items.QUARTZ, 4)); // Quartz Block to 4 Quartz
		// CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(net.minecraft.init.Blocks.QUARTZ_BLOCK, 1), new ItemStack(net.minecraft.init.Items.QUARTZ, 4)); // Quartz Block to 4 Quartz
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.QUARTZ_BLOCK, new ItemStack(net.minecraft.init.Items.QUARTZ, 4)); // Quartz Block to 4 Quartz
		CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(net.minecraft.init.Blocks.STONE_SLAB, 1, 7), new ItemStack(net.minecraft.init.Items.QUARTZ, 2)); // Quartz Slab to 2 Quartz

		CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(net.minecraft.init.Blocks.PRISMARINE, 1, 0), new ItemStack(net.minecraft.init.Items.PRISMARINE_SHARD, 4)); // Prismarine to Prismarine Shard
		CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(net.minecraft.init.Blocks.PRISMARINE, 1, 1), new ItemStack(net.minecraft.init.Items.PRISMARINE_SHARD, 9)); // Brismarine Bricks to Prismarine Shard
		CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(net.minecraft.init.Blocks.PRISMARINE, 1, 2), new ItemStack(net.minecraft.init.Items.PRISMARINE_SHARD, 8)); // Dark Prismarine to Prismarine Shard

		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.SEA_LANTERN, new ItemStack(net.minecraft.init.Items.PRISMARINE_CRYSTALS, 5)); // Sea Lantern to 5 Prismarine Crystals to Prismarine Shard

		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.SLIME_BLOCK, new ItemStack(net.minecraft.init.Items.SLIME_BALL, 9)); // Slime Block to 9 Slime Balls

	}

	protected static void initVanillaRecipes() {
		// stub
	}

	protected static void initGeneralRecipes() {

		for (final MMDMaterial material : Materials.getAllMaterials()) {
			// first we handle any recipes possibly involving transformations of an ore-block
			makePowderRecipes(material); // this also handles ingot->powder, block->powder, etc..
			makeIngotRecipes(material); // nugget->ingot, ore->ingot, powder->ingot

			// next we hand recipes involving blocks
			makeBlockRecipes(material); // handles any recipe which outputs a block

			// nugget based recipes
			makeNuggetRecipes(material);

			// then the rest of the basic stuff
			makeSimpleRecipes(material); // slab, wall, small-powder, rod, lever, crackhammer, trapdoor, etc...
			makeModRecipes(material); // rod, gear, etc...

			// ranged weapons all use the 'ROD' of a material
			if (material.getItem(Names.ROD) != null) {
				makeCrossbowRecipes(material);
				makeBowRecipes(material);
			}

			// shields are a necessary thing
			makeShieldRecipes(material);

			// for now this can live here
			if ((material.getItem(Names.BLEND) != null) && (material.getItem(Names.SMALLBLEND) != null)) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material.getItem(Names.SMALLBLEND), 9), new ItemStack(material.getItem(Names.BLEND))));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getItem(Names.BLEND)), "xxx", "xxx", "xxx", 'x', new ItemStack(material.getItem(Names.SMALLBLEND))));
			}

			// as can this
			if (material.getItem(Names.INGOT) != null) {
				// Vanilla has all the base tools & armors where they currently matter.
				generateBaseTools(material);
			}
		}
	}

	private static void makeShieldRecipes(MMDMaterial material) {
		final String oreDictName = material.getCapitalizedName();
		final String repairSort = "mmd:shieldrepair";
		final String upgradeSort = "mmd:shieldupgrade";

		if (material.getItem(Names.SHIELD) != null) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getItem(Names.SHIELD)), "xyx", "xxx", " x ", 'y', Oredicts.PLANK_WOOD, 'x', Oredicts.INGOT + oreDictName));
			GameRegistry.addSmelting(new ItemStack(material.getItem(Names.SHIELD)), new ItemStack(material.getItem(Names.INGOT), 6), 0); // 1 wood loss
			if (material.getItem(Names.PLATE) != null) {
				if (Options.enablePlateRepairs()) {
					GameRegistry.addRecipe(new ShieldRepairRecipe(material));
					RecipeSorter.register(repairSort, ShieldRepairRecipe.class, Category.SHAPELESS, SORTLOC);
				}

				if (Options.enableShieldUpgrades()) {
					GameRegistry.addRecipe(new ShieldUpgradeRecipe(material));
					RecipeSorter.register(upgradeSort, ShieldUpgradeRecipe.class, Category.UNKNOWN, SORTLOC);
				}
			}
		}
	}

	private static void makeBowRecipes(MMDMaterial material) {
		final String oreDictName = material.getCapitalizedName();

		if (material.getItem(Names.ARROW) != null) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getItem(Names.ARROW), 4), "x", "y", "z", 'x', Oredicts.NUGGET + oreDictName, 'y', Oredicts.ROD + oreDictName, 'z', Oredicts.FEATHER));
			GameRegistry.addSmelting(new ItemStack(material.getItem(Names.ARROW)), new ItemStack(material.getItem(Names.NUGGET), 1), 0); // 0.25 nugget loss
		}

		if (material.getItem(Names.BOW) != null) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getItem(Names.BOW)), " xy", "x y", " xy", 'x', Oredicts.ROD + oreDictName, 'y', Oredicts.STRING));
			GameRegistry.addSmelting(new ItemStack(material.getItem(Names.BOW)), new ItemStack(material.getItem(Names.INGOT), 1), 0); // 4.5 nugget loss
		}

	}

	private static void makeCrossbowRecipes(MMDMaterial material) {
		final String oreDictName = material.getCapitalizedName();

		if ((material.getItem(Names.GEAR) != null) && (material.getItem(Names.CROSSBOW) != null)) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getItem(Names.CROSSBOW)), "zxx", " yx", "x z", 'x', Oredicts.ROD + oreDictName, 'y', Oredicts.GEAR + oreDictName, 'z', Oredicts.STRING));
			GameRegistry.addSmelting(new ItemStack(material.getItem(Names.CROSSBOW)), new ItemStack(material.getItem(Names.INGOT), 2 + Config.Options.gearQuantity()), 0);
		}

		if (material.getItem(Names.BOLT) != null) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getItem(Names.BOLT), 4), "x", "x", "y", 'x', Oredicts.ROD + oreDictName, 'y', Oredicts.FEATHER));
			GameRegistry.addSmelting(new ItemStack(material.getItem(Names.BOLT)), new ItemStack(material.getItem(Names.NUGGET), 2), 0); // 0.25 nugget loss
		}
	}

	private static void makeNuggetRecipes(MMDMaterial material) {
		final String oreDictName = material.getCapitalizedName();

		if (material.getItem(Names.NUGGET) != null) {
			if (material.getItem(Names.BUTTON) != null) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getItem(Names.BUTTON)), "x", "x", 'x', Oredicts.NUGGET + oreDictName));
				GameRegistry.addSmelting(new ItemStack(material.getItem(Names.BUTTON)), new ItemStack(material.getItem(Names.NUGGET), 2), 0);
			}
			if (material.getItem(Names.ROD) != null) {
				GameRegistry.addSmelting(new ItemStack(material.getItem(Names.ROD)), new ItemStack(material.getItem(Names.NUGGET), 4), 0); // Roughly half a nugget loss
			}

			if ((material.getItem(Names.POWDER) != null) && (material.getItem(Names.SMALLPOWDER) != null)) {
				GameRegistry.addSmelting(new ItemStack(material.getItem(Names.SMALLPOWDER)), new ItemStack(material.getItem(Names.NUGGET), 1), 0);
				CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.NUGGET + oreDictName, new ItemStack(material.getItem(Names.SMALLPOWDER), 1));
			}
		}
		if (material.getItem(Names.SMALLBLEND) != null) {
			GameRegistry.addSmelting(new ItemStack(material.getItem(Names.SMALLBLEND)), new ItemStack(material.getItem(Names.NUGGET), 1), 0);
		}
	}

	private static void makeModRecipes(MMDMaterial material) {
		final String oreDictName = material.getCapitalizedName();

		if (material.getItem(Names.ROD) != null) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getItem(Names.ROD), 4), "x", "x", 'x', Oredicts.INGOT + oreDictName));

			if (material.getItem(Names.GEAR) != null) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getItem(Names.GEAR), Config.Options.gearQuantity()), " x ", "x/x", " x ", 'x', Oredicts.INGOT + oreDictName, '/', Oredicts.ROD + oreDictName));
				if (material.hasItem(Names.INGOT)) {
					// if there is no ingot, no cheese
					GameRegistry.addSmelting(new ItemStack(material.getItem(Names.GEAR)), new ItemStack(material.getItem(Names.INGOT), Config.Options.gearQuantity()), 0); // you lose the rod
				}
			}
		}
	}

	private static void makeSimpleRecipes(MMDMaterial material) {
		final String oreDictName = material.getCapitalizedName();

		if (material.getBlock(Names.BLOCK) != null) {
			if (material.getItem(Names.SLAB) != null) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getItem(Names.SLAB), 6), "xxx", 'x', Oredicts.BLOCK + oreDictName));
				GameRegistry.addSmelting(new ItemStack(material.getItem(Names.SLAB)), new ItemStack(material.getItem(Names.NUGGET), 4), 0); // you lose roughly half a nugget
			}

			if ((material.getItem(Names.ROD) != null) && (material.getItem(Names.LEVER) != null)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getItem(Names.LEVER)), "x", "y", 'x', Oredicts.ROD + oreDictName, 'y', Oredicts.INGOT + oreDictName));
				GameRegistry.addSmelting(new ItemStack(material.getItem(Names.LEVER)), new ItemStack(material.getItem(Names.INGOT), 1), 0); // you lose the rod
			}

			if (material.getBlock(Names.STAIRS) != null) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getBlock(Names.STAIRS), 4), "x  ", "xx ", "xxx", 'x', Oredicts.BLOCK + oreDictName));
			}

			if (material.getBlock(Names.WALL) != null) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getBlock(Names.WALL), 6), "xxx", "xxx", 'x', Oredicts.BLOCK + oreDictName));
				GameRegistry.addSmelting(new ItemStack(material.getBlock(Names.WALL)), new ItemStack(material.getBlock(Names.BLOCK), 1), 0);
			}

			if ((material.getItem(Names.CRACKHAMMER) != null) && (!Options.disableAllHammerRecipes())) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getItem(Names.CRACKHAMMER)), "x", "/", "/", 'x', Oredicts.BLOCK + oreDictName, '/', Oredicts.STICK_WOOD));
				if (Config.Options.furnaceCheese()) {
					GameRegistry.addSmelting(new ItemStack(material.getItem(Names.CRACKHAMMER)), new ItemStack(material.getBlock(Names.BLOCK), 1), 0);
				} else if (Config.Options.furnace1112()) {
					GameRegistry.addSmelting(new ItemStack(material.getItem(Names.CRACKHAMMER)), new ItemStack(material.getItem(Names.NUGGET), 1), 0);
				}
			}

			if (material.getBlock(Names.BARS) != null && material.getItem(Names.ROD) != null) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getBlock(Names.BARS), 4), "xxx", 'x', Oredicts.ROD + oreDictName));
			}

			if (material.getBlock(Names.BLOCK) instanceof IMMDObject) {
				if (material.getBlock(Names.PLATE) != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getBlock(Names.PLATE), Config.Options.plateQuantity()), "xxx", 'x', Oredicts.INGOT + oreDictName));
					GameRegistry.addSmelting(new ItemStack(material.getBlock(Names.PLATE)), new ItemStack(material.getItem(Names.INGOT), 1), 0);
				}

				if (material.getBlock(Names.PRESSUREPLATE) != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getBlock(Names.PRESSUREPLATE)), "xx", 'x', Oredicts.INGOT + oreDictName));
					GameRegistry.addSmelting(new ItemStack(material.getBlock(Names.PRESSUREPLATE)), new ItemStack(material.getItem(Names.INGOT), 2), 0);
				}

				if (material.getBlock(Names.BARS) != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getBlock(Names.BARS), 16), "xxx", "xxx", 'x', Oredicts.INGOT + oreDictName));
					GameRegistry.addSmelting(new ItemStack(material.getBlock(Names.BARS)), new ItemStack(material.getItem(Names.NUGGET), 3), 0); // roughly half a nugget loss
				}

				if (material.getItem(Names.DOOR) != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getItem(Names.DOOR), 3), "xx", "xx", "xx", 'x', Oredicts.INGOT + oreDictName));
					GameRegistry.addSmelting(new ItemStack(material.getItem(Names.DOOR)), new ItemStack(material.getItem(Names.INGOT), 2), 0);
				}

				if (material.getBlock(Names.TRAPDOOR) != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getBlock(Names.TRAPDOOR)), "xx", "xx", 'x', Oredicts.INGOT + oreDictName));
					GameRegistry.addSmelting(new ItemStack(material.getBlock(Names.TRAPDOOR)), new ItemStack(material.getItem(Names.INGOT), 4), 0);
				}

				// Diamond, Gold & Iron Horse armor are in vanilla so dont do them for vanilla mats
				if ((material.getItem(Names.HORSEARMOR) instanceof IMMDObject) && (material.getItem(Names.HORSEARMOR) != null)) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getItem(Names.HORSEARMOR)), "  x", "xyx", "xxx", 'x', Oredicts.INGOT + oreDictName, 'y', net.minecraft.init.Blocks.WOOL));
					GameRegistry.addSmelting(new ItemStack(material.getItem(Names.HORSEARMOR)), new ItemStack(material.getItem(Names.INGOT), 6), 0); // 1 wool loss
				}

				if (material.getItem(Names.FISHINGROD) != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getItem(Names.FISHINGROD)), "  x", " xy", "x y", 'x', Oredicts.ROD + oreDictName, 'y', Oredicts.STRING));
					GameRegistry.addSmelting(new ItemStack(material.getItem(Names.FISHINGROD)), new ItemStack(material.getItem(Names.INGOT), 1), 0); // 4.5 nugget loss
				}

			}
		}
	}

	private static void makeBlockRecipes(MMDMaterial material) {
		final String oreDictName = material.getCapitalizedName();

		if (material.hasBlock(Names.BLOCK) && (material.getBlock(Names.BLOCK) instanceof IMMDObject) && (material.hasItem(Names.INGOT))) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material.getItem(Names.INGOT), 9), Oredicts.BLOCK + oreDictName));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getItem(Names.BLOCK)), "xxx", "xxx", "xxx", 'x', Oredicts.INGOT + oreDictName));
		}
	}

	private static void makeIngotRecipes(MMDMaterial material) {
		final String oreDictName = material.getCapitalizedName();
		// NOTE: smelting XP is based on output item, not input item
		final float oreSmeltXP = material.getOreSmeltXP();

		if (material.hasItem(Names.INGOT)) {
			if (material.hasOre()) {
				GameRegistry.addSmelting(new ItemStack(material.getBlock(Names.ORE)), new ItemStack(material.getItem(Names.INGOT), 1), oreSmeltXP);
			} else if (material.hasBlend()) {
				GameRegistry.addSmelting(new ItemStack(material.getItem(Names.BLEND)), new ItemStack(material.getItem(Names.INGOT)), oreSmeltXP);
			}

			if (material.hasItem(Names.NUGGET)) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material.getItem(Names.NUGGET), 9), Oredicts.INGOT + oreDictName));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getItem(Names.INGOT)), "xxx", "xxx", "xxx", 'x', Oredicts.NUGGET + oreDictName));
			}

			if (material.hasItem(Names.POWDER)) {
				GameRegistry.addSmelting(new ItemStack(material.getItem(Names.POWDER)), new ItemStack(material.getItem(Names.INGOT), 1), oreSmeltXP);
			}
		}
	}

	private static void makePowderRecipes(MMDMaterial material) {
		final String oreDictName = material.getCapitalizedName();

		if (material.hasItem(Names.POWDER)) {
			if (material.hasOre()) {
				CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.ORE + oreDictName, new ItemStack(material.getItem(Names.POWDER), 2));
			}
			if (material.getItem(Names.INGOT) != null) {
				CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.INGOT + oreDictName, new ItemStack(material.getItem(Names.POWDER), 1));
			}
			if (material.getItem(Names.BLOCK) != null) {
				CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.BLOCK + oreDictName, new ItemStack(material.getItem(Names.POWDER), 9));
			}
			if (material.getItem(Names.SMALLPOWDER) != null) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material.getItem(Names.SMALLPOWDER), 9), new ItemStack(material.getItem(Names.POWDER))));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getItem(Names.POWDER)), "xxx", "xxx", "xxx", 'x', new ItemStack(material.getItem(Names.SMALLPOWDER))));
				if (material.getItem(Names.NUGGET) != null) {
					CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.NUGGET + oreDictName, new ItemStack(material.getItem(Names.SMALLPOWDER), 1));
				}
			}
		}
	}

	private static void generateBaseTools(final MMDMaterial material) {
		final String oreDictName = material.getCapitalizedName();
		Item boots = material.getItem(Names.BOOTS);
		Item plate = material.getItem(Names.PLATE);
		Item helmet = material.getItem(Names.HELMET);
		Item chestplate = material.getItem(Names.CHESTPLATE);
		Item leggings = material.getItem(Names.LEGGINGS);
		Item ingot = material.getItem(Names.INGOT);
		Item nugget = material.getItem(Names.NUGGET);
		Item axe = material.getItem(Names.AXE);
		Item shovel = material.getItem(Names.SHOVEL);
		Item hoe = material.getItem(Names.HOE);
		Item pickaxe = material.getItem(Names.PICKAXE);
		Item sword = material.getItem(Names.SWORD);
		Item shears = material.getItem(Names.SHEARS);

		if ((boots != null) && (boots instanceof IMMDObject)) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(boots), "x x", "x x", 'x', Oredicts.INGOT + oreDictName));
			if ((plate != null) && (Options.enablePlateRepairs())) {
				GameRegistry.addRecipe(new BootsRepairRecipe(material));
				RecipeSorter.register("mmd:bootsrepair", BootsRepairRecipe.class, Category.SHAPELESS, SORTLOC);
			}
			if (Config.Options.furnaceCheese()) {
				GameRegistry.addSmelting(new ItemStack(boots), new ItemStack(ingot, 4), 0);
			} else if (Config.Options.furnace1112()) {
				GameRegistry.addSmelting(new ItemStack(boots), new ItemStack(nugget, 1), 0);
			}
		}

		if ((helmet != null) && (helmet instanceof IMMDObject)) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(helmet), "xxx", "x x", 'x', Oredicts.INGOT + oreDictName));
			if ((plate != null) && (Options.enablePlateRepairs())) {
				GameRegistry.addRecipe(new HelmetRepairRecipe(material));
				RecipeSorter.register("mmd:helmetrepair", HelmetRepairRecipe.class, Category.SHAPELESS, SORTLOC);
			}
			if (Config.Options.furnaceCheese()) {
				GameRegistry.addSmelting(new ItemStack(helmet), new ItemStack(ingot, 5), 0);
			} else if (Config.Options.furnace1112()) {
				GameRegistry.addSmelting(new ItemStack(helmet), new ItemStack(nugget, 1), 0);
			}
		}

		if ((chestplate != null) && (chestplate instanceof IMMDObject)) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(chestplate), "x x", "xxx", "xxx", 'x', Oredicts.INGOT + oreDictName));
			if ((plate != null) && (Options.enablePlateRepairs())) {
				GameRegistry.addRecipe(new ChestplateRepairRecipe(material));
				RecipeSorter.register("mmd:chestplaterepair", ChestplateRepairRecipe.class, Category.SHAPELESS, SORTLOC);
			}
			if (Config.Options.furnaceCheese()) {
				GameRegistry.addSmelting(new ItemStack(chestplate), new ItemStack(ingot, 8), 0);
			} else if (Config.Options.furnace1112()) {
				GameRegistry.addSmelting(new ItemStack(chestplate), new ItemStack(nugget, 1), 0);
			}
		}

		if ((leggings != null) && (leggings instanceof IMMDObject)) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(leggings), "xxx", "x x", "x x", 'x', Oredicts.INGOT + oreDictName));
			if ((plate != null) && (Options.enablePlateRepairs())) {
				GameRegistry.addRecipe(new LeggingsRepairRecipe(material));
				RecipeSorter.register("mmd:leggingsrepair", LeggingsRepairRecipe.class, Category.SHAPELESS, SORTLOC);
			}
			if (Config.Options.furnaceCheese()) {
				GameRegistry.addSmelting(new ItemStack(leggings), new ItemStack(ingot, 7), 0);
			} else if (Config.Options.furnace1112()) {
				GameRegistry.addSmelting(new ItemStack(leggings), new ItemStack(nugget, 1), 0);
			}
		}

		if ((axe != null) && (axe instanceof IMMDObject)) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(axe), "xx", "x/", " /", 'x', Oredicts.INGOT + oreDictName, '/', Oredicts.STICK_WOOD));
			if (Config.Options.furnaceCheese()) {
				GameRegistry.addSmelting(new ItemStack(axe), new ItemStack(ingot, 3), 0);
			} else if (Config.Options.furnace1112()) {
				GameRegistry.addSmelting(new ItemStack(axe), new ItemStack(nugget, 1), 0);
			}
		}

		if ((hoe != null) && (hoe instanceof IMMDObject)) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(hoe), "xx", " /", " /", 'x', Oredicts.INGOT + oreDictName, '/', Oredicts.STICK_WOOD));
			if (Config.Options.furnaceCheese()) {
				GameRegistry.addSmelting(new ItemStack(hoe), new ItemStack(ingot, 2), 0);
			} else if (Config.Options.furnace1112()) {
				GameRegistry.addSmelting(new ItemStack(hoe), new ItemStack(nugget, 1), 0);
			}
		}

		if ((pickaxe != null) && (pickaxe instanceof IMMDObject)) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(pickaxe), "xxx", " / ", " / ", 'x', Oredicts.INGOT + oreDictName, '/', Oredicts.STICK_WOOD));
			if (Config.Options.furnaceCheese()) {
				GameRegistry.addSmelting(new ItemStack(pickaxe), new ItemStack(ingot, 3), 0);
			} else if (Config.Options.furnace1112()) {
				GameRegistry.addSmelting(new ItemStack(pickaxe), new ItemStack(nugget, 1), 0);
			}
		}

		if ((shovel != null) && (shovel instanceof IMMDObject)) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(shovel), "x", "/", "/", 'x', Oredicts.INGOT + oreDictName, '/', Oredicts.STICK_WOOD));
			if (Config.Options.furnaceCheese()) {
				GameRegistry.addSmelting(new ItemStack(shovel), new ItemStack(ingot, 1), 0);
			} else if (Config.Options.furnace1112()) {
				GameRegistry.addSmelting(new ItemStack(shovel), new ItemStack(nugget, 1), 0);
			}
		}

		if ((sword != null) && (sword instanceof IMMDObject)) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(sword), "x", "x", "/", 'x', Oredicts.INGOT + oreDictName, '/', Oredicts.STICK_WOOD));
			if (Config.Options.furnaceCheese()) {
				GameRegistry.addSmelting(new ItemStack(sword), new ItemStack(ingot, 2), 0);
			} else if (Config.Options.furnace1112()) {
				GameRegistry.addSmelting(new ItemStack(sword), new ItemStack(nugget, 1), 0);
			}
		}

		if ((shears != null) && (shears instanceof IMMDObject)) {
			String oredict = Oredicts.INGOT;
			if (material.getType() == MMDMaterial.MaterialType.GEM) {
				oredict = Oredicts.GEM;
			}
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(shears), " x", "x ", 'x', oredict + oreDictName));
			GameRegistry.addSmelting(new ItemStack(shears), new ItemStack(ingot, 2), 0);
		}
	}

	private static void initModSpecificRecipes() {

	}

	protected static void addAdditionalOredicts(MMDMaterial material, String oreDictName) {
		if (material == null) {
			return;
		}

		oreDictName = StringUtils.capitalize(oreDictName);

		if ((material.hasOre()) && (material.getBlock(Names.ORE) != null)) {
			OreDictionary.registerOre(Oredicts.ORE + oreDictName, material.getBlock(Names.ORE));
		}
		if (material.getBlock(Names.BLOCK) != null) {
			OreDictionary.registerOre(Oredicts.BLOCK + oreDictName, material.getBlock(Names.BLOCK));
		}
		if (material.getItem(Names.PLATE) != null) {
			OreDictionary.registerOre(Oredicts.PLATE + oreDictName, material.getItem(Names.PLATE));
		}
		if (material.getBlock(Names.BARS) != null) {
			OreDictionary.registerOre(Oredicts.BARS + oreDictName, material.getBlock(Names.BARS));
		}
		if (material.getItem(Names.DOOR) != null) {
			OreDictionary.registerOre(Oredicts.DOOR + oreDictName, material.getItem(Names.DOOR));
		}
		if (material.getItem(Names.TRAPDOOR) != null) {
			OreDictionary.registerOre(Oredicts.TRAPDOOR + oreDictName, material.getItem(Names.TRAPDOOR));
		}
		if (material.hasBlend()) {
			if (material.getItem(Names.BLEND) != null) {
				OreDictionary.registerOre(Oredicts.DUST + oreDictName, material.getItem(Names.BLEND));
			}
			if (material.getItem(Names.SMALLBLEND) != null) {
				OreDictionary.registerOre(Oredicts.DUST_TINY + oreDictName, material.getItem(Names.SMALLBLEND));
				OreDictionary.registerOre(Oredicts.DUST_SMALL + oreDictName, material.getItem(Names.SMALLBLEND));
			}
		}
		if (material.getItem(Names.INGOT) != null) {
			if (material.getType() != MaterialType.GEM) {
				OreDictionary.registerOre(Oredicts.INGOT + oreDictName, material.getItem(Names.INGOT)); // For non-gem based
			} else if (material.getType() == MaterialType.GEM) {
				OreDictionary.registerOre(Oredicts.GEM + oreDictName, material.getItem(Names.INGOT)); // For Gem based
			}
		}
		if (material.getItem(Names.NUGGET) != null) {
			OreDictionary.registerOre(Oredicts.NUGGET + oreDictName, material.getItem(Names.NUGGET));
		}
		if (material.getItem(Names.POWDER) != null) {
			OreDictionary.registerOre(Oredicts.DUST + oreDictName, material.getItem(Names.POWDER));
		}
		if (material.getItem(Names.SMALLPOWDER) != null) {
			OreDictionary.registerOre(Oredicts.DUST_TINY + oreDictName, material.getItem(Names.SMALLPOWDER));
			OreDictionary.registerOre(Oredicts.DUST_SMALL + oreDictName, material.getItem(Names.SMALLPOWDER));
		}
		if (material.getItem(Names.GEAR) != null) {
			OreDictionary.registerOre(Oredicts.GEAR + oreDictName, material.getItem(Names.GEAR));
		}
		if (material.getItem(Names.ROD) != null) {
			OreDictionary.registerOre(Oredicts.ROD + oreDictName, material.getItem(Names.ROD));
			OreDictionary.registerOre(Oredicts.STICK + oreDictName, material.getItem(Names.ROD));
		}
	}

	protected static void addAlloyRecipe(MMDMaterial material, int outputQty, Object... ingredients) {
		if (material == null) {
			return;
		}

		if (ingredients.length == 2 && ingredients[0] instanceof String && ingredients[1] instanceof String) {
			addSimpleAlloyRecipe(material, outputQty, (String) ingredients[0], (String) ingredients[1]);
		}

		Object[] dustIngredients = (new String[ingredients.length]);
		Object[] tinyDustIngredients = new String[ingredients.length];

		for (int i = 0; i < ingredients.length; i++) {
			dustIngredients[i] = String.format("%s%s", Oredicts.DUST, StringUtils.capitalize((String) ingredients[i]));
			tinyDustIngredients[i] = String.format("%s%s", Oredicts.DUST_TINY, StringUtils.capitalize((String) ingredients[i]));
		}

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material.getItem(Names.BLEND), outputQty), dustIngredients));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material.getItem(Names.SMALLBLEND), outputQty), tinyDustIngredients));
	}

	protected static void addSimpleAlloyRecipe(MMDMaterial material, int outputQty, String oredict1, String oredict2) {
		if (material == null) {
			return;
		}

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material.getItem(Names.BLEND), outputQty), Oredicts.DUST + StringUtils.capitalize(oredict1), Oredicts.DUST + StringUtils.capitalize(oredict2)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material.getItem(Names.SMALLBLEND), outputQty), Oredicts.DUST_TINY + StringUtils.capitalize(oredict1), Oredicts.DUST_TINY + StringUtils.capitalize(oredict2)));
	}
}
