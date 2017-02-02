package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.registry.CrusherRecipeRegistry;
import com.mcmoddev.basemetals.util.Config;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.util.Oredicts;

import cyano.basemetals.init.Materials;
import net.minecraft.block.Block;
import net.minecraft.item.*;
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

//	private static final Item PRISMARINE_CRYSTALS = net.minecraft.init.Items.PRISMARINE_CRYSTALS;

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

		initPureVanillaOredicts();
		initPureVanillaCrusherRecipes();
		initVanillaRecipes();
		initGeneralRecipes();
		initModSpecificRecipes();

		initDone = true;
	}

	// TODO
/*
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
*/

	protected static void initVanillaRecipes() {
		// TODO
/*
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
*/
		MetalMaterial material;
		String oreDictName;
		Item ingot;
		Item nugget;
		Item powder;
		Item smallpowder;
//		Item rod;
//		Item gear;
		Block block;
//		Block bars;
//		Block plate;
		Block ore;

		if (!Options.DISABLE_ALL_HAMMERS) {
			if (Options.ENABLE_WOOD) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Materials.vanilla_wood.crackhammer), "x", "/", "/", 'x', Oredicts.LOGWOOD, '/', Oredicts.STICKWOOD));
			}
			if (Options.ENABLE_STONE) {
				// TODO
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Materials.vanilla_stone.crackhammer), "x", "/", "/", 'x', net.minecraft.init.Blocks.STONEBRICK, '/', Oredicts.STICKWOOD));
			}
			/*
			if (Options.ENABLE_IRON) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Materials.vanilla_iron.crackhammer), "x", "/", "/", 'x', Oredicts.BLOCKIRON, '/', Oredicts.STICKWOOD));
			}
			*/
			/*
			if (Options.ENABLE_GOLD) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Materials.vanilla_gold.crackhammer), "x", "/", "/", 'x', Oredicts.BLOCKGOLD, '/', Oredicts.STICKWOOD));
			}
			*/
			if (Options.ENABLE_DIAMOND) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Materials.vanilla_diamond.crackhammer), "x", "/", "/", 'x', Oredicts.BLOCKDIAMOND, '/', Oredicts.STICKWOOD));
			}
		}

		// Iron items
		if (Options.ENABLE_IRON) {
			material = Materials.vanilla_iron;
			oreDictName = material.getCapitalizedName();
			// TODO
//			ingot = net.minecraft.init.Items.IRON_INGOT;
//			nugget = material.nugget; // Minecraft does not provide an Iron Nugget till 1.11.1
//			powder = material.powder;
//			smallpowder = material.smallpowder;
//			rod = material.rod;
//			gear = material.gear;
			// TODO
//			bars = net.minecraft.init.Blocks.IRON_BARS;
//			block = net.minecraft.init.Blocks.IRON_BLOCK;
//			plate = material.plate;

			if (!Options.DISABLE_ALL_HAMMERS) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.crackhammer), "x", "/", "/", 'x', Oredicts.BLOCKIRON, '/', Oredicts.STICKWOOD));
			}

			CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.ORE + oreDictName, new ItemStack(material.powder, 2));
			CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.BLOCK + oreDictName, new ItemStack(material.powder, 9));
			CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.INGOT + oreDictName, new ItemStack(material.powder, 1));

			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material.nugget, 9), new ItemStack(material.ingot))); // Not needed for 1.11.1+
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.ingot), "xxx", "xxx", "xxx", 'x', material.nugget)); // Not needed for 1.11.1+
			GameRegistry.addSmelting(material.powder, new ItemStack(material.ingot), 0f);

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
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.gear, Config.Options.GEAR_QTY), " x ", "x/x", " x ", 'x', Oredicts.INGOT + oreDictName, '/', Oredicts.ROD + oreDictName));
				}
				OreDictionary.registerOre(Oredicts.GEAR + oreDictName, material.gear);
				OreDictionary.registerOre(Oredicts.GEAR, material.gear);
			}
			if (material.plate != null) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.plate, Config.Options.PLATE_QTY), "xxx", 'x', Oredicts.INGOT + oreDictName));
			}
		}

		// Gold items
		if (Options.ENABLE_GOLD) {
			material = Materials.vanilla_gold;
			oreDictName = material.getCapitalizedName();
			// TODO
//			ingot = net.minecraft.init.Items.GOLD_INGOT;
//			nugget = net.minecraft.init.Items.GOLD_NUGGET;
//			powder = material.powder;
//			smallpowder = material.smallpowder;
//			rod = material.rod;
//			gear = material.gear;
//			bars = material.bars; // Minecraft does not provide Gold Bars
//			block = net.minecraft.init.Blocks.GOLD_BLOCK;
//			plate = material.plate;

			if (!Options.DISABLE_ALL_HAMMERS) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.crackhammer), "x", "/", "/", 'x', Oredicts.BLOCKGOLD, '/', Oredicts.STICKWOOD));
			}

			CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.ORE + oreDictName, new ItemStack(material.powder, 2));
			CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.BLOCK + oreDictName, new ItemStack(material.powder, 9));
			CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.INGOT + oreDictName, new ItemStack(material.powder, 1));
			GameRegistry.addSmelting(material.powder, new ItemStack(material.ingot), 0f);

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
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.gear, Config.Options.GEAR_QTY), " x ", "x/x", " x ", 'x', Oredicts.INGOT + oreDictName, '/', Oredicts.ROD + oreDictName));
				}
				OreDictionary.registerOre(Oredicts.GEAR + oreDictName, material.gear);
				OreDictionary.registerOre(Oredicts.GEAR, material.gear);
			}
			if (material.plate != null) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.plate, 3), "xxx", 'x', Oredicts.INGOT + oreDictName));
			}
		}

		if (Options.ENABLE_CHARCOAL) {
			material = null; // Materials.charcoal;
//			oreDictName = material.getCapitalizedName();
			oreDictName = Oredicts.CHARCOAL;
			// TODO
			ingot = net.minecraft.init.Items.COAL;
			nugget = null; // Items.charcoal_nugget; // material.nugget;
			powder = Items.charcoal_powder; // material.powder;
			smallpowder = Items.charcoal_smallpowder; // material.smallpowder;
			block = net.minecraft.init.Blocks.COAL_BLOCK; //material.block; // Minecraft does not provide a block of charcoal

			if (powder != null) {
				//if (material.block != null) {
					//CrusherRecipeRegistry.addNewCrusherRecipe(BLOCK + oreDictName, new ItemStack(material.powder, 9)); // Minecraft does not provide a block of charcoal
					//GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material.block), "xxx", "xxx", "xxx", 'x', material.powder)); // Minecraft does not provide a block of charcoal
				//}
				if (ingot != null) {
					CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(ingot, 1, 1), new ItemStack(powder, 1));
				}
				if (smallpowder != null) {
					GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(smallpowder, 9), new ItemStack(powder)));
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(powder), "xxx", "xxx", "xxx", 'x', new ItemStack(smallpowder)));
					if (nugget != null) {
						CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.NUGGET + oreDictName, new ItemStack(smallpowder, 1));
					}
				}
			}
		}

		if (Options.ENABLE_COAL) {
			material = null; //Materials.coal;
//			oreDictName = material.getCapitalizedName();
			oreDictName = Oredicts.COAL;
			// TODO
			ingot = net.minecraft.init.Items.COAL;
			nugget = null; //Items.coal_nugget; // material.nugget;
			powder = Items.coal_powder; // material.powder;
			smallpowder = Items.coal_smallpowder; // material.smallpowder;
			block = net.minecraft.init.Blocks.COAL_BLOCK;
			ore = net.minecraft.init.Blocks.COAL_ORE;

			if (powder != null) {
				if (ore != null) {
					CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.ORE + oreDictName, new ItemStack(powder, 2));
				}
				if (block != null) {
					CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.BLOCK + oreDictName, new ItemStack(powder, 9));
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(block), "xxx", "xxx", "xxx", 'x', powder));
				}
				if (ingot != null) {
					CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(ingot, 1, 0), new ItemStack(powder, 1));
				}
				if (smallpowder != null) {
					GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(smallpowder, 9), new ItemStack(powder)));
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(powder), "xxx", "xxx", "xxx", 'x', new ItemStack(smallpowder)));
					if (nugget != null) {
						CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.NUGGET + oreDictName, new ItemStack(smallpowder, 1));
					}
				}
			}
		}
	}

	private static void initModSpecificRecipes() {
		// alloy blends
		// TODO: Fix this

		if (Options.ENABLE_BRASS) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.brass.blend, 3), Oredicts.DUSTCOPPER, Oredicts.DUSTCOPPER, Oredicts.DUSTZINC));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.brass.smallblend, 3), Oredicts.DUSTTINYCOPPER, Oredicts.DUSTTINYCOPPER, Oredicts.DUSTTINYZINC));
		}
		if (Options.ENABLE_BRONZE) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.bronze.blend, 4), Oredicts.DUSTCOPPER, Oredicts.DUSTCOPPER, Oredicts.DUSTCOPPER, Oredicts.DUSTTIN));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.bronze.smallblend, 4), Oredicts.DUSTTINYCOPPER, Oredicts.DUSTTINYCOPPER, Oredicts.DUSTTINYCOPPER, Oredicts.DUSTTINYTIN));
		}
		if (Options.ENABLE_STEEL) {
			OreDictionary.registerOre(Oredicts.SPROCKET, Materials.steel.gear);

//			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.steel.blend, 8), Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTCARBON));
//			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.steel.smallblend, 8), Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYCARBON));

			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.steel.blend, 8), Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTCHARCOAL));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.steel.smallblend, 8), Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYCHARCOAL));

			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.steel.blend, 8), Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTCOAL));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.steel.smallblend, 8), Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYCOAL));
		}
		if (Options.ENABLE_INVAR) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.invar.blend, 3), Oredicts.DUSTIRON, Oredicts.DUSTIRON, Oredicts.DUSTNICKEL));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.invar.smallblend, 3), Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYIRON, Oredicts.DUSTTINYNICKEL));
		}
		if (Options.ENABLE_CUPRONICKEL) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.cupronickel.blend, 4), Oredicts.DUSTCOPPER, Oredicts.DUSTCOPPER, Oredicts.DUSTCOPPER, Oredicts.DUSTNICKEL));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.cupronickel.smallblend, 4), Oredicts.DUSTTINYCOPPER, Oredicts.DUSTTINYCOPPER, Oredicts.DUSTTINYCOPPER, Oredicts.DUSTTINYNICKEL));
		}
		if (Options.ENABLE_ELECTRUM) {
			addSimpleAlloyRecipe(Materials.electrum, 2, "Gold", "Silver");
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.electrum.blend, 2), Oredicts.DUSTSILVER, Oredicts.DUSTGOLD));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.electrum.smallblend, 2), Oredicts.DUSTTINYSILVER, Oredicts.DUSTTINYGOLD));
		}
		if (Options.ENABLE_MITHRIL) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.mithril.blend, 3), Oredicts.DUSTSILVER, Oredicts.DUSTSILVER, Oredicts.DUSTCOLDIRON, Oredicts.INGOTMERCURY));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.mithril.smallblend, 3), Oredicts.DUSTTINYSILVER, Oredicts.DUSTTINYSILVER, Oredicts.DUSTTINYCOLDIRON, Oredicts.NUGGETMERCURY));
		}
		if (Options.ENABLE_AQUARIUM) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.aquarium.blend, 3), Oredicts.DUSTCOPPER, Oredicts.DUSTCOPPER, Oredicts.DUSTZINC, Oredicts.DUSTPRISMARINE, Oredicts.DUSTPRISMARINE, Oredicts.DUSTPRISMARINE));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.aquarium.smallblend, 3), Oredicts.DUSTTINYCOPPER, Oredicts.DUSTTINYCOPPER, Oredicts.DUSTTINYZINC, Oredicts.DUSTPRISMARINE));
		}

		// Alt oreDict Adamantite
		if (Options.ENABLE_ADAMANTINE) {
			//String oreDictName = "Adamantite";
			//MetalMaterial material = Materials.adamantine;
			addAdditionalOredicts(Materials.adamantine, "Adamantite");
			//OreDictionary.registerOre(Oredicts.ORE + oreDictName, material.ore);
			//OreDictionary.registerOre(Oredicts.BLOCK + oreDictName, material.block);
			//OreDictionary.registerOre(Oredicts.PLATE + oreDictName, material.plate);
			//OreDictionary.registerOre(Oredicts.BARS + oreDictName, material.bars);
//			//OreDictionary.registerOre(Oredicts.DOOR + oreDictName, material.doorBlock);
			//OreDictionary.registerOre(Oredicts.TRAPDOOR + oreDictName, material.trapdoor);
			//OreDictionary.registerOre(Oredicts.INGOT + oreDictName, material.ingot);
			//OreDictionary.registerOre(Oredicts.NUGGET + oreDictName, material.nugget);
			//OreDictionary.registerOre(Oredicts.DUST + oreDictName, material.powder);
			//OreDictionary.registerOre(Oredicts.DUSTTINY + oreDictName, material.smallpowder);
			//OreDictionary.registerOre(Oredicts.DUSTSMALL + oreDictName, material.smallpowder);
			//OreDictionary.registerOre(Oredicts.GEAR + oreDictName, material.gear);
			//OreDictionary.registerOre(Oredicts.ROD + oreDictName, material.rod);
		}

		// Alt oreDict Adamantite
		if (Options.ENABLE_ADAMANTINE) {
			//String oreDictName = "Adamantium";
			//MetalMaterial material = Materials.adamantine;
			addAdditionalOredicts(Materials.adamantine, "Adamantium");
			//OreDictionary.registerOre(Oredicts.ORE + oreDictName, material.ore);
			//OreDictionary.registerOre(Oredicts.BLOCK + oreDictName, material.block);
			//OreDictionary.registerOre(Oredicts.PLATE + oreDictName, material.plate);
			//OreDictionary.registerOre(Oredicts.BARS + oreDictName, material.bars);
//			//OreDictionary.registerOre(Oredicts.DOOR + oreDictName, material.doorBlock);
			//OreDictionary.registerOre(Oredicts.TRAPDOOR + oreDictName, material.trapdoor);
			//OreDictionary.registerOre(Oredicts.INGOT + oreDictName, material.ingot);
			//OreDictionary.registerOre(Oredicts.NUGGET + oreDictName, material.nugget);
			//OreDictionary.registerOre(Oredicts.DUST + oreDictName, material.powder);
			//OreDictionary.registerOre(Oredicts.DUSTTINY + oreDictName, material.smallpowder);
			//OreDictionary.registerOre(Oredicts.DUSTSMALL + oreDictName, material.smallpowder);
			//OreDictionary.registerOre(Oredicts.GEAR + oreDictName, material.gear);
			//OreDictionary.registerOre(Oredicts.ROD + oreDictName, material.rod);
		}

		// Alt oreDict Adamantite
		if (Options.ENABLE_ADAMANTINE) {
			//String oreDictName = "Adamant";
			//MetalMaterial material = Materials.adamantine;
			addAdditionalOredicts(Materials.adamantine, "Adamant");
			//OreDictionary.registerOre(Oredicts.ORE + oreDictName, material.ore);
			//OreDictionary.registerOre(Oredicts.BLOCK + oreDictName, material.block);
			//OreDictionary.registerOre(Oredicts.PLATE + oreDictName, material.plate);
			//OreDictionary.registerOre(Oredicts.BARS + oreDictName, material.bars);
//			//OreDictionary.registerOre(Oredicts.DOOR + oreDictName, material.doorBlock);
			//OreDictionary.registerOre(Oredicts.TRAPDOOR + oreDictName, material.trapdoor);
			//OreDictionary.registerOre(Oredicts.INGOT + oreDictName, material.ingot);
			//OreDictionary.registerOre(Oredicts.NUGGET + oreDictName, material.nugget);
			//OreDictionary.registerOre(Oredicts.DUST + oreDictName, material.powder);
			//OreDictionary.registerOre(Oredicts.DUSTTINY + oreDictName, material.smallpowder);
			//OreDictionary.registerOre(Oredicts.DUSTSMALL + oreDictName, material.smallpowder);
			//OreDictionary.registerOre(Oredicts.GEAR + oreDictName, material.gear);
			//OreDictionary.registerOre(Oredicts.ROD + oreDictName, material.rod);
		}

		if (Options.ENABLE_MERCURY) {
			if (FluidRegistry.isUniversalBucketEnabled()) {
				final UniversalBucket universal_bucket = ForgeModContainer.getInstance().universalBucket;
				final ItemStack bucketMercury = new ItemStack(universal_bucket, 1, 0);
				universal_bucket.fill(bucketMercury, new FluidStack(Fluids.fluidMercury, universal_bucket.getCapacity()), true);
				GameRegistry.addRecipe(new ShapelessOreRecipe(bucketMercury, net.minecraft.init.Items.BUCKET, Oredicts.INGOTMERCURY, Oredicts.INGOTMERCURY, Oredicts.INGOTMERCURY, Oredicts.INGOTMERCURY, Oredicts.INGOTMERCURY, Oredicts.INGOTMERCURY, Oredicts.INGOTMERCURY, Oredicts.INGOTMERCURY));
			}
		}

		// potions
		// TODO: potion recipes

		// misc recipes
		if (Options.ENABLE_STEEL) {
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

		if (Options.ENABLE_MERCURY) {
			CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.OREMERCURY, new ItemStack(Items.mercury_powder, 2));
			GameRegistry.addSmelting(Items.mercury_powder, new ItemStack(Items.mercury_ingot, 1), 0);
			GameRegistry.addSmelting(Blocks.mercury_ore, new ItemStack(Items.mercury_ingot, 1), 1);
		}

		// new recipes using rods and gears
		// TODO
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(net.minecraft.init.Blocks.LEVER, 1), "x", "y", 'x', Oredicts.ROD, 'y', Oredicts.COBBLESTONE));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.human_detector, 1), "xx", "yy", 'x', Oredicts.INGOTBRICK, 'y', Oredicts.GEAR));
	}
}
