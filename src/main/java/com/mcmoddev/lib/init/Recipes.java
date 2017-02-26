package com.mcmoddev.lib.init;

import com.mcmoddev.basemetals.util.Config;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.recipe.*;
import com.mcmoddev.lib.registry.CrusherRecipeRegistry;
import com.mcmoddev.lib.material.IMetalObject;
import com.mcmoddev.lib.util.Oredicts;

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
		OreDictionary.registerOre(Oredicts.BARSIRON, net.minecraft.init.Blocks.IRON_BARS);
		OreDictionary.registerOre(Oredicts.BARS, net.minecraft.init.Blocks.IRON_BARS);
		OreDictionary.registerOre(Oredicts.DOORIRON, net.minecraft.init.Items.IRON_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR, net.minecraft.init.Items.IRON_DOOR);
		OreDictionary.registerOre(Oredicts.DOORWOOD, net.minecraft.init.Items.OAK_DOOR);
		OreDictionary.registerOre(Oredicts.DOORWOOD, net.minecraft.init.Items.JUNGLE_DOOR);
		OreDictionary.registerOre(Oredicts.DOORWOOD, net.minecraft.init.Items.SPRUCE_DOOR);
		OreDictionary.registerOre(Oredicts.DOORWOOD, net.minecraft.init.Items.DARK_OAK_DOOR);
		OreDictionary.registerOre(Oredicts.DOORWOOD, net.minecraft.init.Items.BIRCH_DOOR);
		OreDictionary.registerOre(Oredicts.DOORWOOD, net.minecraft.init.Items.ACACIA_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR, net.minecraft.init.Items.OAK_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR, net.minecraft.init.Items.JUNGLE_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR, net.minecraft.init.Items.SPRUCE_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR, net.minecraft.init.Items.DARK_OAK_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR, net.minecraft.init.Items.BIRCH_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR, net.minecraft.init.Items.ACACIA_DOOR);
	}

	// TODO
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

		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.ORELAPIS, new ItemStack(net.minecraft.init.Items.DYE, 8, 4)); // Lapis Ore to 8 Lapis
		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.BLOCKLAPIS, new ItemStack(net.minecraft.init.Items.DYE, 9, 4)); // Lapis Block to 9 Lapis

		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.OREREDSTONE, new ItemStack(net.minecraft.init.Items.REDSTONE, 8)); // Redstone Ore to 8 Redstone
		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.BLOCKREDSTONE, new ItemStack(net.minecraft.init.Items.REDSTONE, 9)); // Redstone Block to 9 Redstone

		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Items.REEDS, new ItemStack(net.minecraft.init.Items.SUGAR, 2)); // Sugar Cane to 2 Sugar

		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.BONE_BLOCK, new ItemStack(net.minecraft.init.Items.DYE, 9, 15)); // Bone Block to 9 Bonemeal
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Items.BONE, new ItemStack(net.minecraft.init.Items.DYE, 3, 15)); // Bone to 3 Bonemeal

		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Items.BLAZE_ROD, new ItemStack(net.minecraft.init.Items.BLAZE_POWDER, 2)); // Blaze Rod to 2 Blaze Powder

		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.OREQUARTZ, new ItemStack(net.minecraft.init.Items.QUARTZ, 2)); // Nether Quartz Ore to 2 Quartz
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

		for (final MetalMaterial material : Materials.getAllMaterials()) {
			final String oreDictName = material.getCapitalizedName();
			// NOTE: smelting XP is based on output item, not input item
			final float oreSmeltXP = material.getOreSmeltXP();

			if (material.ore != null) {
				if (material.powder != null) {
					CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.ORE + oreDictName, new ItemStack(material.powder, 2));
				}

				if ((material.ingot != null) && (material.ingot instanceof IMetalObject)) {
					GameRegistry.addSmelting(material.ore, new ItemStack(material.ingot, 1), oreSmeltXP);
				}
			}

			if (material.block != null) {
				if (material.powder != null) {
					CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.BLOCK + oreDictName, new ItemStack(material.powder, 9));
				}

				if (material.slab != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.slab, 6), "xxx", 'x', Oredicts.BLOCK + oreDictName));
					GameRegistry.addSmelting(material.slab, new ItemStack(material.nugget, 4), 0); // you lose roughly half a nugget
				}

				if ((material.rod != null) && (material.lever != null)) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.lever), "x", "y", 'x', Oredicts.ROD + oreDictName, 'y', Oredicts.INGOT + oreDictName));
					GameRegistry.addSmelting(material.lever, new ItemStack(material.ingot, 1), 0); // you lose the rod
				}
				
				/*
				 * TODO: Figure out why this crashes
				if (material.stairs != null) { // Crashes
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.stairs, 4), "x", "xx", "xxx", 'x', Oredicts.BLOCK + oreDictName));
				}
				*/
				 
				if (material.wall != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.wall, 6), "xxx", "xxx", 'x', Oredicts.BLOCK + oreDictName));
					GameRegistry.addSmelting(material.wall, new ItemStack(material.block, 1), 0);
				}

				if ((material.crackhammer != null) && (!Options.disableAllHammerRecipes)) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.crackhammer), "x", "/", "/", 'x', Oredicts.BLOCK + oreDictName, '/', Oredicts.STICKWOOD));
					if (Config.Options.furnaceCheese) {
						GameRegistry.addSmelting(material.crackhammer, new ItemStack(material.block, 1), 0);
					} else if (Config.Options.furnace1112) {
						GameRegistry.addSmelting(material.crackhammer, new ItemStack(material.nugget, 1), 0);
					}
				}
			}

			if (material.getType() == MetalMaterial.MaterialType.MINERAL && material.powder != null) {
				if (material.block != null && material.block instanceof IMetalObject) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.block), "xxx", "xxx", "xxx", 'x', Oredicts.DUST + oreDictName));
					GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material.powder, 9), Oredicts.BLOCK + oreDictName));
				}
			} else if (material.ingot != null) {
				// Vanilla has all the base tools & armors where they currently matter.
				generateBaseTools(material);

				if (material.powder != null) {
					CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.INGOT + oreDictName, new ItemStack(material.powder, 1));
					GameRegistry.addSmelting(material.powder, new ItemStack(material.ingot, 1), oreSmeltXP);
				}

				if (material.blend != null) {
					GameRegistry.addSmelting(material.blend, new ItemStack(material.ingot, 1), oreSmeltXP);
				}

				if ((material.nugget != null) && (material.nugget instanceof IMetalObject)) {
					GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material.nugget, 9), Oredicts.INGOT + oreDictName));
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.ingot), "xxx", "xxx", "xxx", 'x', Oredicts.NUGGET + oreDictName));
				}

				if ((material.block) != null && (material.block instanceof IMetalObject)) {
					GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material.ingot, 9), Oredicts.BLOCK + oreDictName));
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.block), "xxx", "xxx", "xxx", 'x', (material.getType() == MetalMaterial.MaterialType.GEM ? Oredicts.GEM : Oredicts.INGOT) + oreDictName));
				}

				if (material.plate != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.plate, Config.Options.plateQuantity), "xxx", 'x', Oredicts.INGOT + oreDictName));
					GameRegistry.addSmelting(material.plate, new ItemStack(material.ingot, 1), 0);
				}

				if ((material.pressurePlate != null) && (material.pressurePlate instanceof IMetalObject)) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.pressurePlate), "xx", 'x', Oredicts.INGOT + oreDictName));
					GameRegistry.addSmelting(material.pressurePlate, new ItemStack(material.ingot, 2), 0);
				}

				if ((material.bars != null) && (material.bars instanceof IMetalObject)) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.bars, 16), "xxx", "xxx", 'x', Oredicts.INGOT + oreDictName));
					GameRegistry.addSmelting(material.bars, new ItemStack(material.nugget, 3), 0); // roughly half a nugget loss
				}

				if (material.rod != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.rod, 4), "x", "x", 'x', Oredicts.INGOT + oreDictName));
				}

				if ((material.door != null) && (material.door instanceof IMetalObject)) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.door, 3), "xx", "xx", "xx", 'x', Oredicts.INGOT + oreDictName));
					GameRegistry.addSmelting(material.door, new ItemStack(material.ingot, 2), 0);
				}

				if (material.shield != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.shield), "xyx", "xxx", " x ", 'y', Oredicts.PLANKWOOD, 'x', Oredicts.INGOT + oreDictName));
					GameRegistry.addSmelting(material.shield, new ItemStack(material.ingot, 6), 0); // 1 wood loss
					if (material.plate != null) {
						if (Options.enablePlateRepairs) {
							GameRegistry.addRecipe(new ShieldRepairRecipe(material));
							RecipeSorter.register("mmd:shieldrepair", ShieldRepairRecipe.class, Category.SHAPELESS, SORTLOC);
						}

						if (Options.enableShieldUpgrades) {
							GameRegistry.addRecipe(new ShieldUpgradeRecipe(material));
							RecipeSorter.register("mmd:shieldupgrade", ShieldUpgradeRecipe.class, Category.UNKNOWN, SORTLOC);
						}
					}
				}

				if ((material.trapdoor != null) && (material.trapdoor instanceof IMetalObject)) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.trapdoor), "xx", "xx", 'x', Oredicts.INGOT + oreDictName));
					GameRegistry.addSmelting(material.trapdoor, new ItemStack(material.ingot, 4), 0);
				}

				// Diamond, Gold & Iron Horse armor are in vanilla so dont do them for vanilla mats
				if ((material.horseArmor instanceof IMetalObject) && (material.horseArmor != null)) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.horseArmor), "  x", "xyx", "xxx", 'x', Oredicts.INGOT + oreDictName, 'y', net.minecraft.init.Blocks.WOOL));
					GameRegistry.addSmelting(material.horseArmor, new ItemStack(material.ingot, 6), 0); // 1 wool loss
				}

				if ((material.rod != null) && (material.gear != null)) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.gear, Config.Options.gearQuantity), " x ", "x/x", " x ", 'x', Oredicts.INGOT + oreDictName, '/', Oredicts.ROD + oreDictName));
					GameRegistry.addSmelting(material.gear, new ItemStack(material.ingot, Config.Options.gearQuantity), 0); // you lose the rod
				}

				// TODO: (Possibly) Buckets
				/*
				if ((material.ingot != null) && material.fluid != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Items.BUCKET), "x x", " x ", 'x', Oredicts.INGOT + oreDictName));
				}
				*/
			}

			if ((material.nugget != null) && (material.button != null)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.button), "x", "x", 'x', Oredicts.NUGGET + oreDictName));
				GameRegistry.addSmelting(material.button, new ItemStack(material.nugget, 2), 0);
			}

			if (material.rod != null) {
				if (material.nugget != null) {
					GameRegistry.addSmelting(material.rod, new ItemStack(material.nugget, 4), 0); // Roughly half a nugget loss
				}

				if (material.bars != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.bars, 4), "xxx", 'x', Oredicts.ROD + oreDictName));
				}

				if (material.arrow != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.arrow, 4), "x", "y", "z", 'x', Oredicts.NUGGET + oreDictName, 'y', Oredicts.ROD + oreDictName, 'z', Oredicts.FEATHER));
					GameRegistry.addSmelting(material.arrow, new ItemStack(material.nugget, 1), 0); // 0.25 nugget loss
				}

				if (material.bow != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.bow), " xy", "x y", " xy", 'x', Oredicts.ROD + oreDictName, 'y', Oredicts.STRING));
					GameRegistry.addSmelting(material.bow, new ItemStack(material.ingot, 1), 0); // 4.5 nugget loss
				}

				if ((material.gear != null) && (material.crossbow != null)) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.crossbow), "zxx", " yx", "x z", 'x', Oredicts.ROD + oreDictName, 'y', Oredicts.GEAR + oreDictName, 'z', Oredicts.STRING));
					GameRegistry.addSmelting(material.crossbow, new ItemStack(material.ingot, 2 + Config.Options.gearQuantity), 0);
				}

				if (material.bolt != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.bolt, 4), "x", "x", "y", 'x', Oredicts.ROD + oreDictName, 'y', Oredicts.FEATHER));
					GameRegistry.addSmelting(material.bolt, new ItemStack(material.nugget, 2), 0); // 0.25 nugget loss
				}

				if (material.fishingRod != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.fishingRod), "  x", " xy", "x y", 'x', Oredicts.ROD + oreDictName, 'y', Oredicts.STRING));
					GameRegistry.addSmelting(material.fishingRod, new ItemStack(material.ingot, 1), 0); // 4.5 nugget loss
				}
			}

			if ((material.blend != null) && (material.smallblend != null)) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material.smallblend, 9), new ItemStack(material.blend)));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.blend), "xxx", "xxx", "xxx", 'x', new ItemStack(material.smallblend)));
				if (material.nugget != null) {
					GameRegistry.addSmelting(material.smallblend, new ItemStack(material.nugget, 1), 0);
				}
			}

			if ((material.powder != null) && (material.smallpowder != null)) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material.smallpowder, 9), new ItemStack(material.powder)));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.powder), "xxx", "xxx", "xxx", 'x', new ItemStack(material.smallpowder)));
				if (material.nugget != null) {
					GameRegistry.addSmelting(material.smallpowder, new ItemStack(material.nugget, 1), 0);
					CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.NUGGET + oreDictName, new ItemStack(material.smallpowder, 1));
				}
			}
		}
	}

	private static void generateBaseTools(final MetalMaterial material) {
		final String oreDictName = material.getCapitalizedName();
		if ((material.boots != null) && (material.boots instanceof IMetalObject)) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.boots), "x x", "x x", 'x', Oredicts.INGOT + oreDictName));
			if ((material.plate != null) && (Options.enablePlateRepairs)) {
				GameRegistry.addRecipe(new BootsRepairRecipe(material));
				RecipeSorter.register("mmd:bootsrepair", BootsRepairRecipe.class, Category.SHAPELESS, SORTLOC);
			}
			if (Config.Options.furnaceCheese) {
				GameRegistry.addSmelting(material.boots, new ItemStack(material.ingot, 4), 0);
			} else if (Config.Options.furnace1112) {
				GameRegistry.addSmelting(material.boots, new ItemStack(material.nugget, 1), 0);
			}
		}

		if ((material.helmet != null) && (material.helmet instanceof IMetalObject)) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.helmet), "xxx", "x x", 'x', Oredicts.INGOT + oreDictName));
			if ((material.plate != null) && (Options.enablePlateRepairs)) {
				GameRegistry.addRecipe(new HelmetRepairRecipe(material));
				RecipeSorter.register("mmd:helmetrepair", HelmetRepairRecipe.class, Category.SHAPELESS, SORTLOC);
			}
			if (Config.Options.furnaceCheese) {
				GameRegistry.addSmelting(material.helmet, new ItemStack(material.ingot, 5), 0);
			} else if (Config.Options.furnace1112) {
				GameRegistry.addSmelting(material.helmet, new ItemStack(material.nugget, 1), 0);
			}
		}

		if ((material.chestplate != null) && (material.chestplate instanceof IMetalObject)) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.chestplate), "x x", "xxx", "xxx", 'x', Oredicts.INGOT + oreDictName));
			if ((material.plate != null) && (Options.enablePlateRepairs)) {
				GameRegistry.addRecipe(new ChestplateRepairRecipe(material));
				RecipeSorter.register("mmd:chestplaterepair", ChestplateRepairRecipe.class, Category.SHAPELESS, SORTLOC);
			}
			if (Config.Options.furnaceCheese) {
				GameRegistry.addSmelting(material.chestplate, new ItemStack(material.ingot, 8), 0);
			} else if (Config.Options.furnace1112) {
				GameRegistry.addSmelting(material.chestplate, new ItemStack(material.nugget, 1), 0);
			}
		}

		if ((material.leggings != null) && (material.leggings instanceof IMetalObject)) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.leggings), "xxx", "x x", "x x", 'x', Oredicts.INGOT + oreDictName));
			if ((material.plate != null) && (Options.enablePlateRepairs)) {
				GameRegistry.addRecipe(new LeggingsRepairRecipe(material));
				RecipeSorter.register("mmd:leggingsrepair", LeggingsRepairRecipe.class, Category.SHAPELESS, SORTLOC);
			}
			if (Config.Options.furnaceCheese) {
				GameRegistry.addSmelting(material.leggings, new ItemStack(material.ingot, 7), 0);
			} else if (Config.Options.furnace1112) {
				GameRegistry.addSmelting(material.leggings, new ItemStack(material.nugget, 1), 0);
			}
		}

		if ((material.axe != null) && (material.axe instanceof IMetalObject)) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.axe), "xx", "x/", " /", 'x', Oredicts.INGOT + oreDictName, '/', Oredicts.STICKWOOD));
			if (Config.Options.furnaceCheese) {
				GameRegistry.addSmelting(material.axe, new ItemStack(material.ingot, 3), 0);
			} else if (Config.Options.furnace1112) {
				GameRegistry.addSmelting(material.axe, new ItemStack(material.nugget, 1), 0);
			}
		}

		if ((material.hoe != null) && (material.hoe instanceof IMetalObject)) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.hoe), "xx", " /", " /", 'x', Oredicts.INGOT + oreDictName, '/', Oredicts.STICKWOOD));
			if (Config.Options.furnaceCheese) {
				GameRegistry.addSmelting(material.hoe, new ItemStack(material.ingot, 2), 0);
			} else if (Config.Options.furnace1112) {
				GameRegistry.addSmelting(material.hoe, new ItemStack(material.nugget, 1), 0);
			}
		}

		if ((material.pickaxe != null) && (material.pickaxe instanceof IMetalObject)) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.pickaxe), "xxx", " / ", " / ", 'x', Oredicts.INGOT + oreDictName, '/', Oredicts.STICKWOOD));
			if (Config.Options.furnaceCheese) {
				GameRegistry.addSmelting(material.pickaxe, new ItemStack(material.ingot, 3), 0);
			} else if (Config.Options.furnace1112) {
				GameRegistry.addSmelting(material.pickaxe, new ItemStack(material.nugget, 1), 0);
			}
		}

		if ((material.shovel != null) && (material.shovel instanceof IMetalObject)) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.shovel), "x", "/", "/", 'x', Oredicts.INGOT + oreDictName, '/', Oredicts.STICKWOOD));
			if (Config.Options.furnaceCheese) {
				GameRegistry.addSmelting(material.shovel, new ItemStack(material.ingot, 1), 0);
			} else if (Config.Options.furnace1112) {
				GameRegistry.addSmelting(material.shovel, new ItemStack(material.nugget, 1), 0);
			}
		}

		if ((material.sword != null) && (material.sword instanceof IMetalObject)) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.sword), "x", "x", "/", 'x', Oredicts.INGOT + oreDictName, '/', Oredicts.STICKWOOD));
			if (Config.Options.furnaceCheese) {
				GameRegistry.addSmelting(material.sword, new ItemStack(material.ingot, 2), 0);
			} else if (Config.Options.furnace1112) {
				GameRegistry.addSmelting(material.sword, new ItemStack(material.nugget, 1), 0);
			}
		}

		if ((material.shears != null) && (material.shears instanceof IMetalObject)) {
			String oredict = Oredicts.INGOT;
			if (material.getType() == MetalMaterial.MaterialType.GEM) {
				oredict = Oredicts.GEM;
			}
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.shears), " x", "x ", 'x', oredict + oreDictName));
			GameRegistry.addSmelting(material.shears, new ItemStack(material.ingot, 2), 0);
		}
	}

	private static void initModSpecificRecipes() {

	}

	protected static void addAdditionalOredicts(MetalMaterial material, String oreDictName) {
		if (material == null) {
			return;
		}

		if ((material.hasOre) && (material.ore != null)) {
			OreDictionary.registerOre(Oredicts.ORE + oreDictName, material.ore);
		}
		if (material.block != null) {
			OreDictionary.registerOre(Oredicts.BLOCK + oreDictName, material.block);
		}
		if (material.plate != null) {
			OreDictionary.registerOre(Oredicts.PLATE + oreDictName, material.plate);
		}
		if (material.bars != null) {
			OreDictionary.registerOre(Oredicts.BARS + oreDictName, material.bars);
		}
		if (material.door != null) {
			OreDictionary.registerOre(Oredicts.DOOR + oreDictName, material.door);
		}
		if (material.trapdoor != null) {
			OreDictionary.registerOre(Oredicts.TRAPDOOR + oreDictName, material.trapdoor);
		}
		if (material.hasBlend) {
			if (material.blend != null) {
				OreDictionary.registerOre(Oredicts.DUST + oreDictName, material.blend);
			}
			if (material.smallblend != null) {
				OreDictionary.registerOre(Oredicts.DUSTTINY + oreDictName, material.smallblend);
				OreDictionary.registerOre(Oredicts.DUSTSMALL + oreDictName, material.smallblend);
			}
		}
		if (material.ingot != null) {
			if (material.getType() != MetalMaterial.MaterialType.GEM) {
				OreDictionary.registerOre(Oredicts.INGOT + oreDictName, material.ingot); // For non-gem based
			} else if (material.getType() == MetalMaterial.MaterialType.GEM) {
				OreDictionary.registerOre(Oredicts.GEM + oreDictName, material.ingot); // For Gem based
			}
		}
		if (material.nugget != null) {
			OreDictionary.registerOre(Oredicts.NUGGET + oreDictName, material.nugget);
		}
		if (material.powder != null) {
			OreDictionary.registerOre(Oredicts.DUST + oreDictName, material.powder);
		}
		if (material.smallpowder != null) {
			OreDictionary.registerOre(Oredicts.DUSTTINY + oreDictName, material.smallpowder);
			OreDictionary.registerOre(Oredicts.DUSTSMALL + oreDictName, material.smallpowder);
		}
		if (material.gear != null) {
			OreDictionary.registerOre(Oredicts.GEAR + oreDictName, material.gear);
		}
		if (material.rod != null) {
			OreDictionary.registerOre(Oredicts.ROD + oreDictName, material.rod);
			OreDictionary.registerOre(Oredicts.STICK + oreDictName, material.rod);
		}
	}

	protected static void addSimpleAlloyRecipe(MetalMaterial material, int outputQty, String oredict1, String oredict2) {
		if (material == null) {
			return;
		}

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material.blend, outputQty), Oredicts.DUST + oredict1, Oredicts.DUST + oredict2));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material.smallblend, outputQty), Oredicts.DUSTTINY + oredict1, Oredicts.DUSTTINY + oredict2));
	}
}
