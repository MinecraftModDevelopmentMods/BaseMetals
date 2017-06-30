package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.registry.CrusherRecipeRegistry;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class Recipes extends com.mcmoddev.lib.init.Recipes {

	private static boolean initDone = false;

	private Recipes() {
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

	protected static void initVanillaRecipes() {
		MMDMaterial material;
		String oreDictName;

		if (!Options.disableAllHammerRecipes()) {
			if (Options.materialEnabled(MaterialNames.WOOD)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Materials.getMaterialByName(MaterialNames.WOOD).getItem(Names.CRACKHAMMER)), "x", "/", "/", 'x', Oredicts.LOG_WOOD, '/', Oredicts.STICK_WOOD));
			}
			if (Options.materialEnabled(MaterialNames.STONE)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Materials.getMaterialByName(MaterialNames.STONE).getItem(Names.CRACKHAMMER)), "x", "/", "/", 'x', net.minecraft.init.Blocks.STONEBRICK, '/', Oredicts.STICK_WOOD));
			}
		}

		// Iron items
		if (Options.materialEnabled(MaterialNames.IRON)) {
			// this should all be handled elsewhere in 1.11
			//material = Materials.getMaterialByName(MaterialNames.IRON);
			//oreDictName = material.getCapitalizedName();

		// Not needed for 1.11.1+
		/*
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material.getItem(Names.NUGGET), 9), Oredicts.INGOT + oreDictName));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getItem(Names.INGOT)), "xxx", "xxx", "xxx", 'x', Oredicts.NUGGET + oreDictName));
		*/		}

		if (Options.materialEnabled(MaterialNames.CHARCOAL)) {
			material = Materials.getMaterialByName(MaterialNames.CHARCOAL);
			oreDictName = material.getCapitalizedName();

			if (material.hasItem(Names.POWDER)) {
				if (material.hasBlock(Names.BLOCK)) { // Note: Minecraft does not provide a block of charcoal
					CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.BLOCK + oreDictName, new ItemStack(material.getItem(Names.POWDER), 9));
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getBlock(Names.BLOCK)), "xxx", "xxx", "xxx", 'x', material.getItem(Names.POWDER)));
				}
				if (material.hasItem(Names.INGOT)) {
					CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(material.getItem(Names.INGOT), 1, 1), new ItemStack(material.getItem(Names.POWDER), 1));
				}
				if (material.hasItem(Names.SMALLPOWDER)) {
					GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material.getItem(Names.SMALLPOWDER), 9), new ItemStack(material.getItem(Names.POWDER))));
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getItem(Names.POWDER)), "xxx", "xxx", "xxx", 'x', new ItemStack(material.getItem(Names.SMALLPOWDER))));
					if (material.hasItem(Names.NUGGET)) {
						CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.NUGGET + oreDictName, new ItemStack(material.getItem(Names.SMALLPOWDER), 1));
					}
				}
			}
		}

		if (Options.materialEnabled(MaterialNames.COAL)) {
			material = Materials.getMaterialByName(MaterialNames.COAL);
			oreDictName = material.getCapitalizedName();

			if (material.hasItem(Names.POWDER)) {
				if (material.hasItem(Names.ORE)) {
					CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.ORE + oreDictName, new ItemStack(material.getItem(Names.POWDER), 2));
				}
				if (material.hasBlock(Names.BLOCK)) {
					CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.BLOCK + oreDictName, new ItemStack(material.getItem(Names.POWDER), 9));
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getBlock(Names.BLOCK)), "xxx", "xxx", "xxx", 'x', material.getItem(Names.POWDER)));
				}
				if (material.hasItem(Names.INGOT)) {
					CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(material.getItem(Names.INGOT), 1, 0), new ItemStack(material.getItem(Names.POWDER), 1));
				}
				if (material.hasItem(Names.SMALLPOWDER)) {
					GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material.getItem(Names.SMALLPOWDER), 9), new ItemStack(material.getItem(Names.POWDER))));
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getItem(Names.POWDER)), "xxx", "xxx", "xxx", 'x', new ItemStack(material.getItem(Names.SMALLPOWDER))));
					if (material.hasItem(Names.NUGGET)) {
						CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.NUGGET + oreDictName, new ItemStack(material.getItem(Names.SMALLPOWDER), 1));
					}
				}
			}
		}
	}

	private static void initModSpecificRecipes() {
		if (Options.materialEnabled(MaterialNames.ADAMANTINE)) {
			// Alt oreDict Adamantite
			addAdditionalOredicts(Materials.getMaterialByName(MaterialNames.ADAMANTINE), "Adamantite");

			// Alt oreDict Adamantium
			addAdditionalOredicts(Materials.getMaterialByName(MaterialNames.ADAMANTINE), "Adamantium");

			// Alt oreDict Adamant
			addAdditionalOredicts(Materials.getMaterialByName(MaterialNames.ADAMANTINE), "Adamant");
		}

		if (Options.materialEnabled(MaterialNames.AQUARIUM)) {
			addAlloyRecipe(Materials.getMaterialByName(MaterialNames.AQUARIUM), 3, MaterialNames.COPPER, MaterialNames.COPPER, MaterialNames.ZINC, MaterialNames.PRISMARINE, MaterialNames.PRISMARINE, MaterialNames.PRISMARINE);
		}

		if (Options.materialEnabled(MaterialNames.BRASS)) {
			addAlloyRecipe(Materials.getMaterialByName(MaterialNames.BRASS), 3, MaterialNames.COPPER, MaterialNames.COPPER, "Zinc");
		}

		if (Options.materialEnabled(MaterialNames.BRONZE)) {
			addAlloyRecipe(Materials.getMaterialByName(MaterialNames.BRONZE), 4, MaterialNames.COPPER, MaterialNames.COPPER, MaterialNames.COPPER, MaterialNames.TIN);
		}

		if (Options.materialEnabled(MaterialNames.CUPRONICKEL)) {
			addAlloyRecipe(Materials.getMaterialByName(MaterialNames.CUPRONICKEL), 4, MaterialNames.COPPER, MaterialNames.COPPER, MaterialNames.COPPER, MaterialNames.NICKEL);
		}

		if (Options.materialEnabled(MaterialNames.ELECTRUM)) {
			addSimpleAlloyRecipe(Materials.getMaterialByName(MaterialNames.ELECTRUM), 2, MaterialNames.GOLD, MaterialNames.SILVER);
		}

		if (Options.materialEnabled(MaterialNames.INVAR)) {
			addAlloyRecipe(Materials.getMaterialByName(MaterialNames.INVAR), 3, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.NICKEL);
		}

		if (Options.materialEnabled(MaterialNames.MERCURY)) {
			addAdditionalOredicts(Materials.getMaterialByName(MaterialNames.MERCURY), "Quicksilver");

			if (FluidRegistry.isUniversalBucketEnabled()) {
				final ItemStack bucketMercury = UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, Materials.getMaterialByName(MaterialNames.MERCURY).getFluid());
				GameRegistry.addRecipe(new ShapelessOreRecipe(bucketMercury, net.minecraft.init.Items.BUCKET, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY));
			}
		}

		if (Options.materialEnabled(MaterialNames.MITHRIL)) {
			addAlloyRecipe(Materials.getMaterialByName(MaterialNames.MITHRIL), 3, MaterialNames.SILVER, MaterialNames.SILVER, MaterialNames.COLDIRON, MaterialNames.MERCURY);
		}

		if (Options.materialEnabled(MaterialNames.STEEL)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.STEEL);
			OreDictionary.registerOre(Oredicts.SPROCKET, material.getItem(Names.GEAR));

			// addAlloyRecipe(material, 8, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, "Carbon");

			addAlloyRecipe(material, 8, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.CHARCOAL);

			addAlloyRecipe(material, 8, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.COAL);

			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Blocks.ACTIVATOR_RAIL, 6), "x/x", "x*x", "x/x", 'x', Oredicts.INGOT_STEEL, '/', Oredicts.STICK_WOOD, '*', net.minecraft.init.Blocks.REDSTONE_TORCH));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Blocks.DETECTOR_RAIL, 6), "x x", "x-x", "x*x", 'x', Oredicts.INGOT_STEEL, '/', Oredicts.STICK_WOOD, '-', net.minecraft.init.Blocks.STONE_PRESSURE_PLATE, '*', Oredicts.DUST_REDSTONE));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Blocks.RAIL, 16), "x x", "x/x", "x x", 'x', Oredicts.INGOT_STEEL, '/', Oredicts.STICK_WOOD));
			// GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE), "xx", 'x', Oredicts.INGOTSTEEL));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Blocks.TRIPWIRE_HOOK), "x  ", "/  ", "w  ", 'x', Oredicts.INGOT_STEEL, '/', Oredicts.STICK_WOOD, 'w', Oredicts.PLANK_WOOD));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(net.minecraft.init.Items.FLINT_AND_STEEL), Oredicts.INGOT_STEEL, net.minecraft.init.Items.FLINT));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Blocks.HOPPER), "x x", "x/x", " x ", 'x', Oredicts.INGOT_STEEL, '/', Oredicts.CHEST_WOOD));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Items.MINECART), "x x", "xxx", 'x', Oredicts.INGOT_STEEL));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Blocks.PISTON), "www", "sxs", "s*s", 'x', Oredicts.INGOT_STEEL, 'w', Oredicts.PLANK_WOOD, 's', Oredicts.COBBLESTONE, '*', Oredicts.DUST_REDSTONE));
			// GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Items.SHEARS), "x ", " x", 'x', Oredicts.INGOT_STEEL));
			// GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Items.SHEARS), " x", "x ", 'x', Oredicts.INGOT_STEEL));
			// GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Items.SHIELD), "wxw", "www", " w ", 'w', Oredicts.PLANK_WOOD, 'x', Oredicts.INGOT_STEEL));
		}

		// new recipes using rods and gears
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Blocks.LEVER, 1), "x", "y", 'x', Oredicts.ROD, 'y', Oredicts.COBBLESTONE));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.humanDetector, 1), "xx", "yy", 'x', Oredicts.INGOT_BRICK, 'y', Oredicts.GEAR));
	}
}
