package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.data.Names;
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
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Materials.vanillaWood.getItem( Names.CRACKHAMMER )), "x", "/", "/", 'x', Oredicts.LOG_WOOD, '/', Oredicts.STICK_WOOD));
			}
			if (Options.enableStone) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Materials.vanillaStone.getItem( Names.CRACKHAMMER )), "x", "/", "/", 'x', net.minecraft.init.Blocks.STONEBRICK, '/', Oredicts.STICK_WOOD));
			}
		}

		// Iron items
		if (Options.enableIron) {
			material = Materials.vanillaIron;
			oreDictName = material.getCapitalizedName();

			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material.getItem(Names.NUGGET), 9), Oredicts.INGOT + oreDictName)); // Not needed for 1.11.1+
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getItem(Names.INGOT)), "xxx", "xxx", "xxx", 'x', Oredicts.NUGGET + oreDictName)); // Not needed for 1.11.1+
		}

		if (Options.enableCharcoal) {
			material = Materials.vanillaCharcoal;
			oreDictName = material.getCapitalizedName();

			if (material.getItem(Names.POWDER) != null) {
				if (material.getBlock(Names.BLOCK) != null) { // Note: Minecraft does not provide a block of charcoal
					CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.BLOCK + oreDictName, new ItemStack(material.getItem(Names.POWDER), 9));
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getBlock(Names.BLOCK)), "xxx", "xxx", "xxx", 'x', material.getItem(Names.POWDER)));
				}
				if (material.getItem(Names.INGOT) != null) {
					CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(material.getItem(Names.INGOT), 1, 1), new ItemStack(material.getItem(Names.POWDER), 1));
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

		if (Options.enableCoal) {
			material = Materials.vanillaCoal;
			oreDictName = material.getCapitalizedName();

			if (material.getItem(Names.POWDER) != null) {
				if (material.getItem( Names.ORE) != null) {
					CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.ORE + oreDictName, new ItemStack(material.getItem(Names.POWDER), 2));
				}
				if (material.getBlock(Names.BLOCK) != null) {
					CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.BLOCK + oreDictName, new ItemStack(material.getItem(Names.POWDER), 9));
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.getBlock(Names.BLOCK)), "xxx", "xxx", "xxx", 'x', material.getItem(Names.POWDER)));
				}
				if (material.getItem(Names.INGOT) != null) {
					CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(material.getItem(Names.INGOT), 1, 0), new ItemStack(material.getItem(Names.POWDER), 1));
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
		// furnace cheese all the things!
		for (MMDMaterial mat : Materials.getAllMaterials()) {
			if ((mat.getItem(Names.INGOT)) != null && !(mat.getItem(Names.INGOT) instanceof com.mcmoddev.lib.material.IMMDObject) && mat.hasOre() ) {
				if (Options.furnaceCheese) {
					if (mat.getItem(Names.BOOTS) != null)
						GameRegistry.addSmelting(mat.getItem(Names.BOOTS), new ItemStack(mat.getItem(Names.INGOT), 4), 0);

					if (mat.getItem(Names.HELMET) != null)
						GameRegistry.addSmelting(mat.getItem(Names.HELMET), new ItemStack(mat.getItem(Names.INGOT), 5), 0);

					if (mat.getItem(Names.SWORD) != null)
						GameRegistry.addSmelting(mat.getItem(Names.SWORD), new ItemStack(mat.getItem(Names.INGOT), 2), 0);

					if (mat.getItem( Names.SHOVEL) != null)
						GameRegistry.addSmelting(mat.getItem(Names.SHOVEL), new ItemStack(mat.getItem(Names.INGOT), 1), 0);

					if (mat.getItem( Names.PICKAXE) != null)
						GameRegistry.addSmelting(mat.getItem(Names.PICKAXE), new ItemStack(mat.getItem(Names.INGOT), 3), 0);

					if (mat.getItem( Names.HOE) != null)
						GameRegistry.addSmelting(mat.getItem(Names.HOE), new ItemStack(mat.getItem(Names.INGOT), 2), 0);

					if (mat.getItem( Names.AXE) != null)
						GameRegistry.addSmelting(mat.getItem( Names.AXE), new ItemStack(mat.getItem(Names.INGOT), 3), 0);

					if (mat.getItem( Names.LEGGINGS) != null)
						GameRegistry.addSmelting(mat.getItem( Names.LEGGINGS), new ItemStack(mat.getItem(Names.INGOT), 7), 0);

					if (mat.getItem( Names.CHESTPLATE) != null)
						GameRegistry.addSmelting(mat.getItem( Names.CHESTPLATE), new ItemStack(mat.getItem(Names.INGOT), 8), 0);

				} else if (Options.furnace1112) {
					if (mat.getItem(Names.BOOTS) != null)
						GameRegistry.addSmelting(mat.getItem(Names.BOOTS), new ItemStack(mat.getItem(Names.NUGGET), 1), 0);

					if (mat.getItem(Names.HELMET) != null)
						GameRegistry.addSmelting(mat.getItem(Names.HELMET), new ItemStack(mat.getItem(Names.NUGGET), 1), 0);

					if (mat.getItem(Names.SWORD) != null)
						GameRegistry.addSmelting(mat.getItem(Names.SWORD), new ItemStack(mat.getItem(Names.NUGGET), 1), 0);

					if (mat.getItem(Names.SHOVEL) != null)
						GameRegistry.addSmelting(mat.getItem(Names.SHOVEL), new ItemStack(mat.getItem(Names.NUGGET), 1), 0);

					if (mat.getItem(Names.PICKAXE) != null)
						GameRegistry.addSmelting(mat.getItem(Names.PICKAXE), new ItemStack(mat.getItem(Names.NUGGET), 1), 0);

					if (mat.getItem(Names.HOE) != null)
						GameRegistry.addSmelting(mat.getItem(Names.HOE), new ItemStack(mat.getItem(Names.NUGGET), 1), 0);

					if (mat.getItem(Names.AXE) != null)
						GameRegistry.addSmelting(mat.getItem(Names.AXE), new ItemStack(mat.getItem(Names.NUGGET), 1), 0);

					if (mat.getItem(Names.LEGGINGS) != null)
						GameRegistry.addSmelting(mat.getItem(Names.LEGGINGS), new ItemStack(mat.getItem(Names.NUGGET), 1), 0);

					if (mat.getItem(Names.CHESTPLATE) != null)
						GameRegistry.addSmelting(mat.getItem(Names.CHESTPLATE), new ItemStack(mat.getItem(Names.NUGGET), 1), 0);
				}
			}
		}
	}

	private static void initModSpecificRecipes() {
		if (Options.enableBrass) {
			addAlloyRecipe(Materials.brass, 3, "Copper", "Copper", "Zinc");
			// GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.brass.getItem(Names.BLEND), 3), Oredicts.DUST_COPPER, Oredicts.DUST_COPPER, Oredicts.DUST_ZINC));
			// GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.brass.getItem(Names.SMALLBLEND), 3), Oredicts.DUST_TINY_COPPER, Oredicts.DUST_TINY_COPPER, Oredicts.DUST_TINY_ZINC));
		}
		if (Options.enableBronze) {
			addAlloyRecipe(Materials.bronze, 4, "Copper", "Copper", "Copper", "Tin");
			// GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.bronze.getItem(Names.BLEND), 4), Oredicts.DUST_COPPER, Oredicts.DUST_COPPER, Oredicts.DUST_COPPER, Oredicts.DUST_TIN));
			// GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.bronze.getItem(Names.SMALLBLEND), 4), Oredicts.DUST_TINY_COPPER, Oredicts.DUST_TINY_COPPER, Oredicts.DUST_TINY_COPPER, Oredicts.DUST_TINY_TIN));
		}
		if (Options.enableSteel) {
			OreDictionary.registerOre(Oredicts.SPROCKET, Materials.steel.getItem( Names.GEAR ));

			// addAlloyRecipe(Materials.steel, 8, "Iron", "Iron", "Iron", "Iron", "Iron", "Iron", "Iron", "Iron", "Carbon");
			// GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.steel.getItem(Names.BLEND), 8), Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_CARBON));
			// GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.steel.getItem(Names.SMALLBLEND), 8), Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_CARBON));

			addAlloyRecipe(Materials.steel, 8, "Iron", "Iron", "Iron", "Iron", "Iron", "Iron", "Iron", "Iron", "Charcoal");
			// GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.steel.getItem( Names.BLEND ), 8), Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_CHARCOAL));
			// GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.steel.getItem( Names.SMALLBLEND ), 8), Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_CHARCOAL));

			addAlloyRecipe(Materials.steel, 8, "Iron", "Iron", "Iron", "Iron", "Iron", "Iron", "Iron", "Iron", "Coal");
			// GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.steel.getItem( Names.BLEND ), 8), Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_COAL));
			// GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.steel.getItem( Names.SMALLBLEND ), 8), Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_COAL));
		}
		if (Options.enableInvar) {
			addAlloyRecipe(Materials.invar, 3, "Iron", "Iron", "Nickel");
			// GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.invar.getItem( Names.BLEND ), 3), Oredicts.DUST_IRON, Oredicts.DUST_IRON, Oredicts.DUST_NICKEL));
			// GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.invar.getItem( Names.SMALLBLEND ), 3), Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_IRON, Oredicts.DUST_TINY_NICKEL));
		}
		if (Options.enableCupronickel) {
			addAlloyRecipe(Materials.cupronickel, 4, "Copper", "Copper", "Copper", "Nickel");
			// GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.cupronickel.getItem( Names.BLEND ), 4), Oredicts.DUST_COPPER, Oredicts.DUST_COPPER, Oredicts.DUST_COPPER, Oredicts.DUST_NICKEL));
			// GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.cupronickel.getItem( Names.SMALLBLEND ), 4), Oredicts.DUST_TINY_COPPER, Oredicts.DUST_TINY_COPPER, Oredicts.DUST_TINY_COPPER, Oredicts.DUST_TINY_NICKEL));
		}
		if (Options.enableElectrum) {
			addSimpleAlloyRecipe(Materials.electrum, 2, "Gold", "Silver");
			//GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.electrum.getItem( Names.BLEND ), 2), Oredicts.DUST_SILVER, Oredicts.DUST_GOLD));
			//GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.electrum.getItem( Names.SMALLBLEND ), 2), Oredicts.DUST_TINY_SILVER, Oredicts.DUST_TINY_GOLD));
		}
		if (Options.enableMithril) {
			addAlloyRecipe(Materials.mithril, 3, "Silver", "Silver", "Coldiron", "Mercury");
			// GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.mithril.getItem( Names.BLEND ), 3), Oredicts.DUST_SILVER, Oredicts.DUST_SILVER, Oredicts.DUST_COLDIRON, Oredicts.INGOT_MERCURY));
			// GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.mithril.getItem( Names.SMALLBLEND ), 3), Oredicts.DUST_TINY_SILVER, Oredicts.DUST_TINY_SILVER, Oredicts.DUST_TINY_COLDIRON, Oredicts.NUGGET_MERCURY));
		}
		if (Options.enableAquarium) {
			addAlloyRecipe(Materials.brass, 3, "Copper", "Copper", "Zinc", "Prismarine", "Prismarine", "Prismarine");
			// GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.aquarium.getItem( Names.BLEND ), 3), Oredicts.DUST_COPPER, Oredicts.DUST_COPPER, Oredicts.DUST_ZINC, Oredicts.DUST_PRISMARINE, Oredicts.DUST_PRISMARINE, Oredicts.DUST_PRISMARINE));
			// GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.aquarium.getItem( Names.SMALLBLEND ), 3), Oredicts.DUST_TINY_COPPER, Oredicts.DUST_TINY_COPPER, Oredicts.DUST_TINY_ZINC, Oredicts.DUST_PRISMARINE));
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
				universalBucket.fill(bucketMercury, new FluidStack(Materials.mercury.getFluid(), universalBucket.getCapacity()), true);
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
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.humanDetector, 1), "xx", "yy", 'x', Oredicts.INGOT_BRICK, 'y', Oredicts.GEAR));
	}
}
