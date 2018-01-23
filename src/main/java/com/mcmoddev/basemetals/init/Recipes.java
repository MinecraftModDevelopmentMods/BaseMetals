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

	private Recipes() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/**
	 *
	 */
	public static void init() {
		initVanillaRecipes();
		initModSpecificRecipes();
	}

	protected static void initVanillaRecipes() {
		String oreDictName;

		if (!Options.disableAllHammerRecipes()) {
			if (Options.isMaterialEnabled(MaterialNames.WOOD)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(Materials.getMaterialByName(MaterialNames.WOOD).getItemStack(Names.CRACKHAMMER), "x", "/", "/", 'x', Oredicts.LOG_WOOD, '/', Oredicts.STICK_WOOD));
			}
			if (Options.isMaterialEnabled(MaterialNames.STONE)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(Materials.getMaterialByName(MaterialNames.STONE).getItemStack(Names.CRACKHAMMER), "x", "/", "/", 'x', net.minecraft.init.Blocks.STONEBRICK, '/', Oredicts.STICK_WOOD));
			}
		}

		if (Materials.hasMaterial(MaterialNames.CHARCOAL)) {
			MMDMaterial charcoal = Materials.getMaterialByName(MaterialNames.CHARCOAL);
			oreDictName = charcoal.getCapitalizedName();

			if (charcoal.hasItem(Names.POWDER)) {
				if (charcoal.hasBlock(Names.BLOCK)) { // Note: Minecraft does not provide a block of charcoal
					CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.BLOCK + oreDictName, charcoal.getItemStack(Names.POWDER, 9));
					GameRegistry.addRecipe(new ShapedOreRecipe(charcoal.getBlockItemStack(Names.BLOCK), "xxx", "xxx", "xxx", 'x', charcoal.getItemStack(Names.POWDER)));
				}
				if (charcoal.hasItem(Names.INGOT)) {
					CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(charcoal.getItem(Names.INGOT), 1, 1), charcoal.getItemStack(Names.POWDER, 1));
				}
				if (charcoal.hasItem(Names.SMALLPOWDER)) {
					GameRegistry.addRecipe(new ShapelessOreRecipe(charcoal.getItemStack(Names.SMALLPOWDER, 9), charcoal.getItemStack(Names.POWDER)));
					GameRegistry.addRecipe(new ShapedOreRecipe(charcoal.getItemStack(Names.POWDER), "xxx", "xxx", "xxx", 'x', charcoal.getItemStack(Names.SMALLPOWDER)));
					if (charcoal.hasItem(Names.NUGGET)) {
						CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.NUGGET + oreDictName, charcoal.getItemStack(Names.SMALLPOWDER, 1));
					}
				}
			}
		}

		if (Materials.hasMaterial(MaterialNames.COAL)) {
			MMDMaterial coal = Materials.getMaterialByName(MaterialNames.COAL);
			oreDictName = coal.getCapitalizedName();

			if (coal.hasItem(Names.POWDER)) {
				if (coal.hasItem(Names.ORE)) {
					CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.ORE + oreDictName, coal.getItemStack(Names.POWDER, 2));
				}
				if (coal.hasBlock(Names.BLOCK)) {
					CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.BLOCK + oreDictName, coal.getItemStack(Names.POWDER, 9));
					GameRegistry.addRecipe(new ShapedOreRecipe(coal.getBlockItemStack(Names.BLOCK), "xxx", "xxx", "xxx", 'x', coal.getItemStack(Names.POWDER)));
				}
				if (coal.hasItem(Names.INGOT)) {
					CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(coal.getItem(Names.INGOT), 1, 0), coal.getItemStack(Names.POWDER, 1));
				}
				if (coal.hasItem(Names.SMALLPOWDER)) {
					GameRegistry.addRecipe(new ShapelessOreRecipe(coal.getItemStack(Names.SMALLPOWDER, 9), coal.getItemStack(Names.POWDER)));
					GameRegistry.addRecipe(new ShapedOreRecipe(coal.getItemStack(Names.POWDER), "xxx", "xxx", "xxx", 'x', coal.getItemStack(Names.SMALLPOWDER)));
					if (coal.hasItem(Names.NUGGET)) {
						CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.NUGGET + oreDictName, coal.getItemStack(Names.SMALLPOWDER, 1));
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
				OreDictionary.registerOre(Oredicts.SPROCKET, steel.getItemStack(Names.GEAR));
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
