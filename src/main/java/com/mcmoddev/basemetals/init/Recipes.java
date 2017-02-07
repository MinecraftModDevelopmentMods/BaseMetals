package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.registry.CrusherRecipeRegistry;
import com.mcmoddev.basemetals.util.Config;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.material.MetalMaterial;
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
		MetalMaterial material;
		String oreDictName;

		if (!Options.disableAllHammerRecipes) {
			if (Options.enableWood) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Materials.vanilla_wood.crackhammer), "x", "/", "/", 'x', Oredicts.LOGWOOD, '/', Oredicts.STICKWOOD));
			}
			if (Options.enableStone) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Materials.vanilla_stone.crackhammer), "x", "/", "/", 'x', net.minecraft.init.Blocks.STONEBRICK, '/', Oredicts.STICKWOOD));
			}
		}

		// Iron items
		if (Options.enableIron) {
			material = Materials.vanilla_iron;
			oreDictName = material.getCapitalizedName();
			runVanillaMaterialRecipe(material);

			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material.nugget, 9), new ItemStack(material.ingot))); // Not needed for 1.11.1+
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.ingot), "xxx", "xxx", "xxx", 'x', material.nugget)); // Not needed for 1.11.1+
		}

		// Diamond items
		if (Options.enableDiamond) {
			material = Materials.vanilla_diamond;
			oreDictName = material.getCapitalizedName();
			runVanillaMaterialRecipe(material);
		}

		// Gold items
		if (Options.enableGold) {
			material = Materials.vanilla_gold;
			oreDictName = material.getCapitalizedName();
			runVanillaMaterialRecipe(material);
		}

		if (Options.enableCharcoal) {
			material = Materials.vanilla_charcoal;
			oreDictName = material.getCapitalizedName();
//			oreDictName = Oredicts.CHARCOAL;
			// TODO
//			ingot = net.minecraft.init.Items.COAL;
//			nugget = .charcoal_nugget; // material.nugget;
//			powder = Items.charcoal_powder; // material.powder;
//			smallpowder = Items.charcoal_smallpowder; // material.smallpowder;
//			block = net.minecraft.init.Blocks.COAL_BLOCK; //material.block; // Minecraft does not provide a block of charcoal

			if (material.powder != null) {
				//if (block != null) {
					//CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.BLOCK + oreDictName, new ItemStack(material.powder, 9)); // Minecraft does not provide a block of charcoal
					//GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.block), "xxx", "xxx", "xxx", 'x', material.powder)); // Minecraft does not provide a block of charcoal
				//}
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
//			oreDictName = Oredicts.COAL;
			// TODO
//			ingot = net.minecraft.init.Items.COAL;
//			nugget = Items.coal_nugget; // material.nugget;
//			powder = Items.coal_powder; // material.powder;
//			smallpowder = Items.coal_smallpowder; // material.smallpowder;
//			block = net.minecraft.init.Blocks.COAL_BLOCK;
//			ore = net.minecraft.init.Blocks.COAL_ORE;

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
	}

	private static void initModSpecificRecipes() {
		if (Options.enableBrass) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.brass.blend, 3), Oredicts.DUSTCOPPER, Oredicts.DUSTCOPPER, Oredicts.DUSTZINC));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.brass.smallblend, 3), Oredicts.DUSTTINYCOPPER, Oredicts.DUSTTINYCOPPER, Oredicts.DUSTTINYZINC));
		}
		if (Options.enableBronze) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.bronze.blend, 4), Oredicts.DUSTCOPPER, Oredicts.DUSTCOPPER, Oredicts.DUSTCOPPER, Oredicts.DUSTTIN));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.bronze.smallblend, 4), Oredicts.DUSTTINYCOPPER, Oredicts.DUSTTINYCOPPER, Oredicts.DUSTTINYCOPPER, Oredicts.DUSTTINYTIN));
		}
		if (Options.enableSteel) {
			OreDictionary.registerOre(Oredicts.SPROCKET, Materials.steel.gear);

//			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.steel.blend, 8), Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTCARBON));
//			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.steel.smallblend, 8), Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYCARBON));

			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.steel.blend, 8), Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTCHARCOAL));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.steel.smallblend, 8), Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYCHARCOAL));

			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.steel.blend, 8), Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTCOAL));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.steel.smallblend, 8), Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYCOAL));
		}
		if (Options.enableInvar) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.invar.blend, 3), Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTNICKEL));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.invar.smallblend, 3), Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYNICKEL));
		}
		if (Options.enableCupronickel) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.cupronickel.blend, 4), Oredicts.DUSTCOPPER, Oredicts.DUSTCOPPER, Oredicts.DUSTCOPPER, Oredicts.DUSTNICKEL));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.cupronickel.smallblend, 4), Oredicts.DUSTTINYCOPPER, Oredicts.DUSTTINYCOPPER, Oredicts.DUSTTINYCOPPER, Oredicts.DUSTTINYNICKEL));
		}
		if (Options.enableElectrum) {
			addSimpleAlloyRecipe(Materials.electrum, 2, "Gold", "Silver");
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.electrum.blend, 2), Oredicts.DUSTSILVER, Oredicts.DUSTGOLD));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.electrum.smallblend, 2), Oredicts.DUSTTINYSILVER, Oredicts.DUSTTINYGOLD));
		}
		if (Options.enableMithril) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.mithril.blend, 3), Oredicts.DUSTSILVER, Oredicts.DUSTSILVER, Oredicts.DUSTCOLDIRON, Oredicts.INGOTMERCURY));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.mithril.smallblend, 3), Oredicts.DUSTTINYSILVER, Oredicts.DUSTTINYSILVER, Oredicts.DUSTTINYCOLDIRON, Oredicts.NUGGETMERCURY));
		}
		if (Options.enableAquarium) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.aquarium.blend, 3), Oredicts.DUSTCOPPER, Oredicts.DUSTCOPPER, Oredicts.DUSTZINC, Oredicts.DUSTPRISMARINE, Oredicts.DUSTPRISMARINE, Oredicts.DUSTPRISMARINE));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.aquarium.smallblend, 3), Oredicts.DUSTTINYCOPPER, Oredicts.DUSTTINYCOPPER, Oredicts.DUSTTINYZINC, Oredicts.DUSTPRISMARINE));
		}

		// Alt oreDict Adamantite
		if (Options.enableAdamantine) {
			addAdditionalOredicts(Materials.adamantine, "Adamantite");
		}

		// Alt oreDict Adamantite
		if (Options.enableAdamantine) {
			addAdditionalOredicts(Materials.adamantine, "Adamantium");
		}

		// Alt oreDict Adamantite
		if (Options.enableAdamantine) {
			addAdditionalOredicts(Materials.adamantine, "Adamant");
		}

		if (Options.enableMercury) {
			if (FluidRegistry.isUniversalBucketEnabled()) {
				final UniversalBucket universalBucket = ForgeModContainer.getInstance().universalBucket;
				final ItemStack bucketMercury = new ItemStack(universalBucket, 1, 0);
				universalBucket.fill(bucketMercury, new FluidStack(Materials.mercury.fluid, universalBucket.getCapacity()), true);
				GameRegistry.addRecipe(new ShapelessOreRecipe(bucketMercury, net.minecraft.init.Items.BUCKET, Oredicts.INGOTMERCURY, Oredicts.INGOTMERCURY, Oredicts.INGOTMERCURY, Oredicts.INGOTMERCURY, Oredicts.INGOTMERCURY, Oredicts.INGOTMERCURY, Oredicts.INGOTMERCURY, Oredicts.INGOTMERCURY));
			}
		}

		// misc recipes
		if (Options.enableSteel) {
			// TODO
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Blocks.ACTIVATOR_RAIL, 6), "x/x", "x*x", "x/x", 'x', Oredicts.INGOTSTEEL, '/', Oredicts.STICKWOOD, '*', net.minecraft.init.Blocks.REDSTONE_TORCH));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Blocks.DETECTOR_RAIL, 6), "x x", "x-x", "x*x", 'x', Oredicts.INGOTSTEEL, '/', Oredicts.STICKWOOD, '-', net.minecraft.init.Blocks.STONE_PRESSURE_PLATE, '*', Oredicts.DUSTREDSTONE));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Blocks.RAIL, 16), "x x", "x/x", "x x", 'x', Oredicts.INGOTSTEEL, '/', Oredicts.STICKWOOD));
//			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE), "xx", 'x', Oredicts.INGOTSTEEL));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Blocks.TRIPWIRE_HOOK), "x  ", "/  ", "w  ", 'x', Oredicts.INGOTSTEEL, '/', Oredicts.STICKWOOD, 'w', Oredicts.PLANKWOOD));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(net.minecraft.init.Items.FLINT_AND_STEEL), Oredicts.INGOTSTEEL, net.minecraft.init.Items.FLINT));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Blocks.HOPPER), "x x", "x/x", " x ", 'x', Oredicts.INGOTSTEEL, '/', Oredicts.CHESTWOOD));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Items.MINECART), "x x", "xxx", 'x', Oredicts.INGOTSTEEL));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Blocks.PISTON), "www", "sxs", "s*s", 'x', Oredicts.INGOTSTEEL, 'w', Oredicts.PLANKWOOD, 's', Oredicts.COBBLESTONE, '*', Oredicts.DUSTREDSTONE));
//			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Items.SHEARS), "x ", " x", 'x', Oredicts.INGOTSTEEL));
//			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Items.SHEARS), " x", "x ", 'x', Oredicts.INGOTSTEEL));
//			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Items.SHIELD), "wxw", "www", " w ", 'w', Oredicts.PLANKWOOD, 'x', Oredicts.INGOTSTEEL));
		}

		// new recipes using rods and gears
		// TODO
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Blocks.LEVER, 1), "x", "y", 'x', Oredicts.ROD, 'y', Oredicts.COBBLESTONE));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.human_detector, 1), "xx", "yy", 'x', Oredicts.INGOTBRICK, 'y', Oredicts.GEAR));
	}
	
	private static void runVanillaMaterialRecipe(MetalMaterial material) {
		final String oreDictName = material.getCapitalizedName();

		if (!Options.disableAllHammerRecipes) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.crackhammer), "x", "/", "/", 'x', Oredicts.BLOCK + oreDictName, '/', Oredicts.STICKWOOD));
		}
		if (material.powder != null) {
			CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.ORE + oreDictName, new ItemStack(material.powder, 2));
			if (material.block != null) {
				CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.BLOCK + oreDictName, new ItemStack(material.powder, 9));
			}
			if (material.ingot != null) {
				CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.INGOT + oreDictName, new ItemStack(material.powder, 1));
				GameRegistry.addSmelting(material.powder, new ItemStack(material.ingot), 0f);
			}
			if (material.smallpowder != null) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material.smallpowder, 9), new ItemStack(material.powder)));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.powder), "xxx", "xxx", "xxx", 'x', new ItemStack(material.smallpowder)));
				if (material.nugget != null) {
					CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.NUGGET + oreDictName, new ItemStack(material.smallpowder, 1));
				}
			}
		}
		if (material.rod != null) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.rod, 4), "x", "x", 'x', Oredicts.INGOT + oreDictName));
			OreDictionary.registerOre(Oredicts.STICK + oreDictName, material.rod);
			OreDictionary.registerOre(Oredicts.ROD + oreDictName, material.rod);
			OreDictionary.registerOre(Oredicts.ROD, material.rod);
			if (material.nugget != null) {
				GameRegistry.addSmelting(material.rod, new ItemStack(material.nugget, 4), 0);
			}
			if (material.bars != null) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.bars, 4), "xxx", 'x', Oredicts.ROD + oreDictName));
			}
		}
		if (material.gear != null) {
			if (material.rod != null) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.gear, Config.Options.gearQuantity), " x ", "x/x", " x ", 'x', Oredicts.INGOT + oreDictName, '/', Oredicts.ROD + oreDictName));
			}
			OreDictionary.registerOre(Oredicts.GEAR + oreDictName, material.gear);
			OreDictionary.registerOre(Oredicts.GEAR, material.gear);
		}
		if (material.plate != null) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.plate, Config.Options.plateQuantity), "xxx", 'x', Oredicts.INGOT + oreDictName));
		}
	}
}
