package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.registry.CrusherRecipeRegistry;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.*;

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

		if (!Options.disableAllHammerRecipes) {
			if (Options.enableWood) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Materials.vanilla_wood.crackhammer), "x", "/", "/", 'x', Oredicts.LOG_WOOD, '/', Oredicts.STICK_WOOD));
			}
			if (Options.enableStone) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Materials.vanilla_stone.crackhammer), "x", "/", "/", 'x', net.minecraft.init.Blocks.STONEBRICK, '/', Oredicts.STICK_WOOD));
			}
		}

		// Iron items
		if (Options.enableIron) {
			material = Materials.vanilla_iron;
			oreDictName = material.getCapitalizedName();

			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material.nugget, 9), Oredicts.INGOT + oreDictName)); // Not needed for 1.11.1+
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.ingot), "xxx", "xxx", "xxx", 'x', Oredicts.NUGGET + oreDictName)); // Not needed for 1.11.1+
		}

		if (Options.enableCharcoal) {
			material = Materials.vanilla_charcoal;
			oreDictName = material.getCapitalizedName();

			if (material.powder != null) {
				if (material.block != null) { // Note: Minecraft does not provide a block of charcoal
					CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.BLOCK + oreDictName, new ItemStack(material.powder, 9));
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.block), "xxx", "xxx", "xxx", 'x', material.powder));
				}
				if (material.ingot != null) {
					CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(material.ingot, 1, 1), new ItemStack(material.powder, 1));
				}
				if (material.smallpowder != null) {
					GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material.smallpowder, 9), new ItemStack(material.powder)));
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.powder), "xxx", "xxx", "xxx", 'x', new ItemStack(material.smallpowder)));
					if (material.nugget != null) {
						CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.NUGGET + oreDictName, new ItemStack(material.smallpowder, 1));
					}
				}
			}
		}

		if (Options.enableCoal) {
			material = Materials.vanilla_coal;
			oreDictName = material.getCapitalizedName();

			if (material.powder != null) {
				if (material.ore != null) {
					CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.ORE + oreDictName, new ItemStack(material.powder, 2));
				}
				if (material.block != null) {
					CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.BLOCK + oreDictName, new ItemStack(material.powder, 9));
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.block), "xxx", "xxx", "xxx", 'x', material.powder));
				}
				if (material.ingot != null) {
					CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(material.ingot, 1, 0), new ItemStack(material.powder, 1));
				}
				if (material.smallpowder != null) {
					GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material.smallpowder, 9), new ItemStack(material.powder)));
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.powder), "xxx", "xxx", "xxx", 'x', new ItemStack(material.smallpowder)));
					if (material.nugget != null) {
						CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.NUGGET + oreDictName, new ItemStack(material.smallpowder, 1));
					}
				}
			}
		}
		// furnace cheese all the things!
		for( MMDMaterial mat : Materials.getAllMaterials() ) {
			if( (mat.ingot) != null && !(mat.ingot instanceof com.mcmoddev.lib.material.IMMDObject) && mat.hasOre ) {
				if (Options.furnaceCheese) {
					if( mat.boots != null )
						GameRegistry.addSmelting(mat.boots, new ItemStack(mat.ingot, 4), 0);
					
					if( mat.helmet != null )
						GameRegistry.addSmelting(mat.helmet, new ItemStack(mat.ingot, 5), 0);
					
					if( mat.sword != null )
						GameRegistry.addSmelting(mat.sword, new ItemStack(mat.ingot, 2), 0);

					if( mat.shovel != null )
						GameRegistry.addSmelting(mat.shovel, new ItemStack(mat.ingot, 1), 0);
					
					if( mat.pickaxe != null )
						GameRegistry.addSmelting(mat.pickaxe, new ItemStack(mat.ingot, 3), 0);

					if( mat.hoe != null )
						GameRegistry.addSmelting(mat.hoe, new ItemStack(mat.ingot, 2), 0);
					
					if( mat.axe != null )
						GameRegistry.addSmelting(mat.axe, new ItemStack(mat.ingot, 3), 0);
					
					if( mat.leggings != null )
						GameRegistry.addSmelting(mat.leggings, new ItemStack(mat.ingot, 7), 0);
					
					if( mat.chestplate != null )
						GameRegistry.addSmelting(mat.chestplate, new ItemStack(mat.ingot, 8), 0);
					
				} else if (Options.furnace1112) {
					if( mat.boots != null )
						GameRegistry.addSmelting(mat.boots, new ItemStack(mat.nugget, 1), 0);
					
					if( mat.helmet != null )
						GameRegistry.addSmelting(mat.helmet, new ItemStack(mat.nugget, 1), 0);
					
					if( mat.sword != null )
						GameRegistry.addSmelting(mat.sword, new ItemStack(mat.nugget, 1), 0);

					if( mat.shovel != null )
						GameRegistry.addSmelting(mat.shovel, new ItemStack(mat.nugget, 1), 0);
					
					if( mat.pickaxe != null )
						GameRegistry.addSmelting(mat.pickaxe, new ItemStack(mat.nugget, 1), 0);

					if( mat.hoe != null )
						GameRegistry.addSmelting(mat.hoe, new ItemStack(mat.nugget, 1), 0);
					
					if( mat.axe != null )
						GameRegistry.addSmelting(mat.axe, new ItemStack(mat.nugget, 1), 0);
					
					if( mat.leggings != null )
						GameRegistry.addSmelting(mat.leggings, new ItemStack(mat.nugget, 1), 0);
					
					if( mat.chestplate != null )
						GameRegistry.addSmelting(mat.chestplate, new ItemStack(mat.nugget, 1), 0);
				}
			}
		}		

	}

	private static void initModSpecificRecipes() {
		if (Options.enableBrass) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.brass.blend, 3), Oredicts.DUST_COPPER, Oredicts.DUST_COPPER, Oredicts.DUST_ZINC));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.brass.smallblend, 3), Oredicts.DUST_TINY_COPPER, Oredicts.DUST_TINY_COPPER, Oredicts.DUST_TINY_ZINC));
		}
		if (Options.enableBronze) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.bronze.blend, 4), Oredicts.DUST_COPPER, Oredicts.DUST_COPPER, Oredicts.DUST_COPPER, Oredicts.DUST_TIN));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.bronze.smallblend, 4), Oredicts.DUST_TINY_COPPER, Oredicts.DUST_TINY_COPPER, Oredicts.DUST_TINY_COPPER, Oredicts.DUST_TINY_TIN));
		}
		if (Options.enableSteel) {
			OreDictionary.registerOre(Oredicts.SPROCKET, Materials.steel.gear);

			// GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.steel.blend, 8), Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_CARBON));
			// GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.steel.smallblend, 8), Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_CARBON));

			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.steel.blend, 8), Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_CHARCOAL));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.steel.smallblend, 8), Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_CHARCOAL));

			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.steel.blend, 8), Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_COAL));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.steel.smallblend, 8), Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_COAL));
		}
		if (Options.enableInvar) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.invar.blend, 3), Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_NICKEL));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.invar.smallblend, 3), Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_NICKEL));
		}
		if (Options.enableCupronickel) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.cupronickel.blend, 4), Oredicts.DUST_COPPER, Oredicts.DUST_COPPER, Oredicts.DUST_COPPER, Oredicts.DUST_NICKEL));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.cupronickel.smallblend, 4), Oredicts.DUST_TINY_COPPER, Oredicts.DUST_TINY_COPPER, Oredicts.DUST_TINY_COPPER, Oredicts.DUST_TINY_NICKEL));
		}
		if (Options.enableElectrum) {
			addSimpleAlloyRecipe(Materials.electrum, 2, "Gold", "Silver");
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.electrum.blend, 2), Oredicts.DUST_SILVER, Oredicts.DUST_GOLD));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.electrum.smallblend, 2), Oredicts.DUST_TINY_SILVER, Oredicts.DUST_TINY_GOLD));
		}
		if (Options.enableMithril) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.mithril.blend, 3), Oredicts.DUST_SILVER, Oredicts.DUST_SILVER, Oredicts.DUST_COLDIRON, Oredicts.INGOT_MERCURY));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.mithril.smallblend, 3), Oredicts.DUST_TINY_SILVER, Oredicts.DUST_TINY_SILVER, Oredicts.DUST_TINY_COLDIRON, Oredicts.NUGGET_MERCURY));
		}
		if (Options.enableAquarium) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.aquarium.blend, 3), Oredicts.DUST_COPPER, Oredicts.DUST_COPPER, Oredicts.DUST_ZINC, Oredicts.DUST_PRISMARINE, Oredicts.DUST_PRISMARINE, Oredicts.DUST_PRISMARINE));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.aquarium.smallblend, 3), Oredicts.DUST_TINY_COPPER, Oredicts.DUST_TINY_COPPER, Oredicts.DUST_TINY_ZINC, Oredicts.DUST_PRISMARINE));
		}

		// Alt oreDict Adamantite
		if (Options.enableAdamantine) {
			addAdditionalOredicts(Materials.adamantine, "Adamantite");
		}

		// Alt oreDict Adamantium
		if (Options.enableAdamantine) {
			addAdditionalOredicts(Materials.adamantine, "Adamantium");
		}

		// Alt oreDict Adamant
		if (Options.enableAdamantine) {
			addAdditionalOredicts(Materials.adamantine, "Adamant");
		}

		if (Options.enableMercury) {
			addAdditionalOredicts(Materials.mercury, "Quicksilver");

			if (FluidRegistry.isUniversalBucketEnabled()) {
				final UniversalBucket universalBucket = ForgeModContainer.getInstance().universalBucket;
				final ItemStack bucketMercury = new ItemStack(universalBucket, 1, 0);
				universalBucket.fill(bucketMercury, new FluidStack(Materials.mercury.fluid, universalBucket.getCapacity()), true);
				GameRegistry.addRecipe(new ShapelessOreRecipe(bucketMercury, net.minecraft.init.Items.BUCKET, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY));
			}
		}

		// misc recipes
		if (Options.enableSteel) {
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
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.human_detector, 1), "xx", "yy", 'x', Oredicts.INGOT_BRICK, 'y', Oredicts.GEAR));
	}
}
