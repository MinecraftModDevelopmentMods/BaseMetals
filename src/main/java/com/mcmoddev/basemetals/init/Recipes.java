package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.registry.CrusherRecipeRegistry;
import com.mcmoddev.lib.util.ConfigBase.Options;
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
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
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
		String oreDictName;

		if (!Options.disableAllHammerRecipes()) {
			if (Options.isMaterialEnabled(MaterialNames.WOOD)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Materials.getMaterialByName(MaterialNames.WOOD).getItem(Names.CRACKHAMMER)), "x", "/", "/", 'x', Oredicts.LOG_WOOD, '/', Oredicts.STICK_WOOD));
			}
			if (Options.isMaterialEnabled(MaterialNames.STONE)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Materials.getMaterialByName(MaterialNames.STONE).getItem(Names.CRACKHAMMER)), "x", "/", "/", 'x', net.minecraft.init.Blocks.STONEBRICK, '/', Oredicts.STICK_WOOD));
			}
		}

		// Iron items
		if (Materials.hasMaterial(MaterialNames.IRON)) {
			// this should all be handled elsewhere in 1.11
			// MMDMaterial iron = Materials.getMaterialByName(MaterialNames.IRON);
			// oreDictName = material.getCapitalizedName();

			// Not needed for 1.11.1+
			/*
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(iron.getItem(Names.NUGGET), 9), Oredicts.INGOT + oreDictName));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(iron.getItem(Names.INGOT)), "xxx", "xxx", "xxx", 'x', Oredicts.NUGGET + oreDictName));
			*/
		}

		if (Materials.hasMaterial(MaterialNames.CHARCOAL)) {
			MMDMaterial charcoal = Materials.getMaterialByName(MaterialNames.CHARCOAL);
			oreDictName = charcoal.getCapitalizedName();

			if (charcoal.hasItem(Names.POWDER)) {
				if (charcoal.hasBlock(Names.BLOCK)) { // Note: Minecraft does not provide a block of charcoal
					CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.BLOCK + oreDictName, new ItemStack(charcoal.getItem(Names.POWDER), 9));
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(charcoal.getBlock(Names.BLOCK)), "xxx", "xxx", "xxx", 'x', charcoal.getItem(Names.POWDER)));
				}
				if (charcoal.hasItem(Names.INGOT)) {
					CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(charcoal.getItem(Names.INGOT), 1, 1), new ItemStack(charcoal.getItem(Names.POWDER), 1));
				}
				if (charcoal.hasItem(Names.SMALLPOWDER)) {
					GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(charcoal.getItem(Names.SMALLPOWDER), 9), new ItemStack(charcoal.getItem(Names.POWDER))));
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(charcoal.getItem(Names.POWDER)), "xxx", "xxx", "xxx", 'x', new ItemStack(charcoal.getItem(Names.SMALLPOWDER))));
					if (charcoal.hasItem(Names.NUGGET)) {
						CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.NUGGET + oreDictName, new ItemStack(charcoal.getItem(Names.SMALLPOWDER), 1));
					}
				}
			}
		}

		if (Materials.hasMaterial(MaterialNames.COAL)) {
			MMDMaterial coal = Materials.getMaterialByName(MaterialNames.COAL);
			oreDictName = coal.getCapitalizedName();

			if (coal.hasItem(Names.POWDER)) {
				if (coal.hasItem(Names.ORE)) {
					CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.ORE + oreDictName, new ItemStack(coal.getItem(Names.POWDER), 2));
				}
				if (coal.hasBlock(Names.BLOCK)) {
					CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.BLOCK + oreDictName, new ItemStack(coal.getItem(Names.POWDER), 9));
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(coal.getBlock(Names.BLOCK)), "xxx", "xxx", "xxx", 'x', coal.getItem(Names.POWDER)));
				}
				if (coal.hasItem(Names.INGOT)) {
					CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(coal.getItem(Names.INGOT), 1, 0), new ItemStack(coal.getItem(Names.POWDER), 1));
				}
				if (coal.hasItem(Names.SMALLPOWDER)) {
					GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(coal.getItem(Names.SMALLPOWDER), 9), new ItemStack(coal.getItem(Names.POWDER))));
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(coal.getItem(Names.POWDER)), "xxx", "xxx", "xxx", 'x', new ItemStack(coal.getItem(Names.SMALLPOWDER))));
					if (coal.hasItem(Names.NUGGET)) {
						CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.NUGGET + oreDictName, new ItemStack(coal.getItem(Names.SMALLPOWDER), 1));
					}
				}
			}
		}
	}

	private static void initModSpecificRecipes() {
		if (Materials.hasMaterial(MaterialNames.ADAMANTINE)) {
			final MMDMaterial adamantine = Materials.getMaterialByName(MaterialNames.ADAMANTINE);

			// Alt oreDict Adamantite
			addAdditionalOredicts(adamantine, "Adamantite");

			// Alt oreDict Adamantium
			addAdditionalOredicts(adamantine, "Adamantium");

			// Alt oreDict Adamant
			addAdditionalOredicts(adamantine, "Adamant");
		}

		if (Materials.hasMaterial(MaterialNames.AQUARIUM)) {
			addAlloyRecipe(Materials.getMaterialByName(MaterialNames.AQUARIUM), 3, MaterialNames.COPPER, MaterialNames.COPPER, MaterialNames.ZINC, MaterialNames.PRISMARINE, MaterialNames.PRISMARINE, MaterialNames.PRISMARINE);
		}

		if (Materials.hasMaterial(MaterialNames.BRASS)) {
			addAlloyRecipe(Materials.getMaterialByName(MaterialNames.BRASS), 3, MaterialNames.COPPER, MaterialNames.COPPER, "Zinc");
		}

		if (Materials.hasMaterial(MaterialNames.BRONZE)) {
			addAlloyRecipe(Materials.getMaterialByName(MaterialNames.BRONZE), 4, MaterialNames.COPPER, MaterialNames.COPPER, MaterialNames.COPPER, MaterialNames.TIN);
		}

		if (Materials.hasMaterial(MaterialNames.CUPRONICKEL)) {
			addAlloyRecipe(Materials.getMaterialByName(MaterialNames.CUPRONICKEL), 4, MaterialNames.COPPER, MaterialNames.COPPER, MaterialNames.COPPER, MaterialNames.NICKEL);
		}

		if (Materials.hasMaterial(MaterialNames.ELECTRUM)) {
			addSimpleAlloyRecipe(Materials.getMaterialByName(MaterialNames.ELECTRUM), 2, MaterialNames.GOLD, MaterialNames.SILVER);
		}

		if (Materials.hasMaterial(MaterialNames.INVAR)) {
			addAlloyRecipe(Materials.getMaterialByName(MaterialNames.INVAR), 3, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.NICKEL);
		}

		if (Materials.hasMaterial(MaterialNames.MERCURY)) {
			final MMDMaterial mercury = Materials.getMaterialByName(MaterialNames.MERCURY);

			addAdditionalOredicts(mercury, "Quicksilver");

			if (FluidRegistry.isUniversalBucketEnabled()) {
				final ItemStack bucketMercury = UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, mercury.getFluid());
				GameRegistry.addRecipe(new ShapelessOreRecipe(bucketMercury, net.minecraft.init.Items.BUCKET, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY));
			}
		}

		if (Materials.hasMaterial(MaterialNames.MITHRIL)) {
			addAlloyRecipe(Materials.getMaterialByName(MaterialNames.MITHRIL), 3, MaterialNames.SILVER, MaterialNames.SILVER, MaterialNames.COLDIRON, MaterialNames.MERCURY);
		}

		if (Materials.hasMaterial(MaterialNames.STEEL)) {
			final MMDMaterial steel = Materials.getMaterialByName(MaterialNames.STEEL);
			if (steel.hasItem(Names.GEAR)) {
				OreDictionary.registerOre(Oredicts.SPROCKET, steel.getItem(Names.GEAR));
			}

			// addAlloyRecipe(steel, 8, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, "Carbon");

			addAlloyRecipe(steel, 8, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.CHARCOAL);

			addAlloyRecipe(steel, 8, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.COAL);

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
