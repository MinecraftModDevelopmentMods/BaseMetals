package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.fuels.FuelRegistry;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;
import com.mcmoddev.lib.util.TabContainer;

import net.minecraft.item.ItemStack;

/**
 * This class initializes all items in Base Metals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Items extends com.mcmoddev.lib.init.Items {

	private static boolean initDone = false;
	private static TabContainer myTabs = new TabContainer(ItemGroups.blocksTab, ItemGroups.itemsTab, ItemGroups.toolsTab);

	protected Items() {
		throw new IllegalAccessError("Not a instantiable class");
	}

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		Blocks.init();
		com.mcmoddev.lib.init.Items.init();

		// Vanilla stuff always gets those bits that exist in vanilla assigned
		Materials.getMaterialByName(MaterialNames.CHARCOAL).addNewItem(Names.INGOT, new ItemStack(net.minecraft.init.Items.COAL, 1, 1).getItem());

		Materials.getMaterialByName(MaterialNames.COAL).addNewItem(Names.INGOT, new ItemStack(net.minecraft.init.Items.COAL, 1, 0).getItem());

		Materials.getMaterialByName(MaterialNames.DIAMOND).addNewItem(Names.AXE, net.minecraft.init.Items.DIAMOND_AXE);
		Materials.getMaterialByName(MaterialNames.DIAMOND).addNewItem(Names.HOE, net.minecraft.init.Items.DIAMOND_HOE);
		Materials.getMaterialByName(MaterialNames.DIAMOND).addNewItem(Names.HORSE_ARMOR, net.minecraft.init.Items.DIAMOND_HORSE_ARMOR);
		Materials.getMaterialByName(MaterialNames.DIAMOND).addNewItem(Names.PICKAXE, net.minecraft.init.Items.DIAMOND_PICKAXE);
		Materials.getMaterialByName(MaterialNames.DIAMOND).addNewItem(Names.SHOVEL, net.minecraft.init.Items.DIAMOND_SHOVEL);
		Materials.getMaterialByName(MaterialNames.DIAMOND).addNewItem(Names.SWORD, net.minecraft.init.Items.DIAMOND_SWORD);
		Materials.getMaterialByName(MaterialNames.DIAMOND).addNewItem(Names.BOOTS, net.minecraft.init.Items.DIAMOND_BOOTS);
		Materials.getMaterialByName(MaterialNames.DIAMOND).addNewItem(Names.CHESTPLATE, net.minecraft.init.Items.DIAMOND_CHESTPLATE);
		Materials.getMaterialByName(MaterialNames.DIAMOND).addNewItem(Names.HELMET, net.minecraft.init.Items.DIAMOND_HELMET);
		Materials.getMaterialByName(MaterialNames.DIAMOND).addNewItem(Names.LEGGINGS, net.minecraft.init.Items.DIAMOND_LEGGINGS);
		Materials.getMaterialByName(MaterialNames.DIAMOND).addNewItem(Names.INGOT, net.minecraft.init.Items.DIAMOND);

		Materials.getMaterialByName(MaterialNames.EMERALD).addNewItem(Names.INGOT, net.minecraft.init.Items.EMERALD);

		Materials.getMaterialByName(MaterialNames.GOLD).addNewItem(Names.AXE, net.minecraft.init.Items.GOLDEN_AXE);
		Materials.getMaterialByName(MaterialNames.GOLD).addNewItem(Names.HOE, net.minecraft.init.Items.GOLDEN_HOE);
		Materials.getMaterialByName(MaterialNames.GOLD).addNewItem(Names.HORSE_ARMOR, net.minecraft.init.Items.GOLDEN_HORSE_ARMOR);
		Materials.getMaterialByName(MaterialNames.GOLD).addNewItem(Names.PICKAXE, net.minecraft.init.Items.GOLDEN_PICKAXE);
		Materials.getMaterialByName(MaterialNames.GOLD).addNewItem(Names.SHOVEL, net.minecraft.init.Items.GOLDEN_SHOVEL);
		Materials.getMaterialByName(MaterialNames.GOLD).addNewItem(Names.SWORD, net.minecraft.init.Items.GOLDEN_SWORD);
		Materials.getMaterialByName(MaterialNames.GOLD).addNewItem(Names.BOOTS, net.minecraft.init.Items.GOLDEN_BOOTS);
		Materials.getMaterialByName(MaterialNames.GOLD).addNewItem(Names.CHESTPLATE, net.minecraft.init.Items.GOLDEN_CHESTPLATE);
		Materials.getMaterialByName(MaterialNames.GOLD).addNewItem(Names.HELMET, net.minecraft.init.Items.GOLDEN_HELMET);
		Materials.getMaterialByName(MaterialNames.GOLD).addNewItem(Names.LEGGINGS, net.minecraft.init.Items.GOLDEN_LEGGINGS);
		Materials.getMaterialByName(MaterialNames.GOLD).addNewItem(Names.INGOT, net.minecraft.init.Items.GOLD_INGOT);
		Materials.getMaterialByName(MaterialNames.GOLD).addNewItem(Names.NUGGET, net.minecraft.init.Items.GOLD_NUGGET);

		Materials.getMaterialByName(MaterialNames.IRON).addNewItem(Names.AXE, net.minecraft.init.Items.IRON_AXE);
		Materials.getMaterialByName(MaterialNames.IRON).addNewItem(Names.DOOR, net.minecraft.init.Items.IRON_DOOR);
		Materials.getMaterialByName(MaterialNames.IRON).addNewItem(Names.HOE, net.minecraft.init.Items.IRON_HOE);
		Materials.getMaterialByName(MaterialNames.IRON).addNewItem(Names.HORSE_ARMOR, net.minecraft.init.Items.IRON_HORSE_ARMOR);
		Materials.getMaterialByName(MaterialNames.IRON).addNewItem(Names.PICKAXE, net.minecraft.init.Items.IRON_PICKAXE);
		Materials.getMaterialByName(MaterialNames.IRON).addNewItem(Names.SHOVEL, net.minecraft.init.Items.IRON_SHOVEL);
		Materials.getMaterialByName(MaterialNames.IRON).addNewItem(Names.SWORD, net.minecraft.init.Items.IRON_SWORD);
		Materials.getMaterialByName(MaterialNames.IRON).addNewItem(Names.BOOTS, net.minecraft.init.Items.IRON_BOOTS);
		Materials.getMaterialByName(MaterialNames.IRON).addNewItem(Names.CHESTPLATE, net.minecraft.init.Items.IRON_CHESTPLATE);
		Materials.getMaterialByName(MaterialNames.IRON).addNewItem(Names.HELMET, net.minecraft.init.Items.IRON_HELMET);
		Materials.getMaterialByName(MaterialNames.IRON).addNewItem(Names.LEGGINGS, net.minecraft.init.Items.IRON_LEGGINGS);
		Materials.getMaterialByName(MaterialNames.IRON).addNewItem(Names.DOOR, net.minecraft.init.Items.IRON_DOOR);
		Materials.getMaterialByName(MaterialNames.IRON).addNewItem(Names.INGOT, net.minecraft.init.Items.IRON_INGOT);
		Materials.getMaterialByName(MaterialNames.IRON).addNewItem(Names.NUGGET, net.minecraft.init.Items.field_191525_da); // Items.IRON_NUGGET
		Materials.getMaterialByName(MaterialNames.IRON).addNewItem(Names.SHEARS, net.minecraft.init.Items.SHEARS);

		Materials.getMaterialByName(MaterialNames.LAPIS).addNewItem(Names.POWDER, new ItemStack(net.minecraft.init.Items.DYE, 1, 4).getItem());

		Materials.getMaterialByName(MaterialNames.QUARTZ).addNewItem(Names.INGOT, net.minecraft.init.Items.QUARTZ);

		Materials.getMaterialByName(MaterialNames.REDSTONE).addNewItem(Names.POWDER, net.minecraft.init.Items.REDSTONE);

		Materials.getMaterialByName(MaterialNames.STONE).addNewItem(Names.AXE, net.minecraft.init.Items.STONE_AXE);
		Materials.getMaterialByName(MaterialNames.STONE).addNewItem(Names.HOE, net.minecraft.init.Items.STONE_HOE);
		Materials.getMaterialByName(MaterialNames.STONE).addNewItem(Names.PICKAXE, net.minecraft.init.Items.STONE_PICKAXE);
		Materials.getMaterialByName(MaterialNames.STONE).addNewItem(Names.SHOVEL, net.minecraft.init.Items.STONE_SHOVEL);
		Materials.getMaterialByName(MaterialNames.STONE).addNewItem(Names.SWORD, net.minecraft.init.Items.STONE_SWORD);
		Materials.getMaterialByName(MaterialNames.STONE).addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.STONE);
		Materials.getMaterialByName(MaterialNames.STONE).addNewBlock(Names.SLAB, net.minecraft.init.Blocks.STONE_SLAB);
		Materials.getMaterialByName(MaterialNames.STONE).addNewBlock(Names.DOUBLE_SLAB, net.minecraft.init.Blocks.DOUBLE_STONE_SLAB);
		Materials.getMaterialByName(MaterialNames.STONE).addNewBlock(Names.STAIRS, net.minecraft.init.Blocks.STONE_STAIRS);

		Materials.getMaterialByName(MaterialNames.WOOD).addNewItem(Names.AXE, net.minecraft.init.Items.WOODEN_AXE);
		Materials.getMaterialByName(MaterialNames.WOOD).addNewItem(Names.DOOR, net.minecraft.init.Items.OAK_DOOR);
		Materials.getMaterialByName(MaterialNames.WOOD).addNewItem(Names.HOE, net.minecraft.init.Items.WOODEN_HOE);
		Materials.getMaterialByName(MaterialNames.WOOD).addNewItem(Names.PICKAXE, net.minecraft.init.Items.WOODEN_PICKAXE);
		Materials.getMaterialByName(MaterialNames.WOOD).addNewItem(Names.SHOVEL, net.minecraft.init.Items.WOODEN_SHOVEL);
		Materials.getMaterialByName(MaterialNames.WOOD).addNewItem(Names.SWORD, net.minecraft.init.Items.WOODEN_SWORD);
		Materials.getMaterialByName(MaterialNames.WOOD).addNewBlock(Names.DOOR, net.minecraft.init.Blocks.OAK_DOOR);
		Materials.getMaterialByName(MaterialNames.WOOD).addNewBlock(Names.ORE, net.minecraft.init.Blocks.LOG);
		Materials.getMaterialByName(MaterialNames.WOOD).addNewBlock(Names.TRAPDOOR, net.minecraft.init.Blocks.TRAPDOOR);
		Materials.getMaterialByName(MaterialNames.WOOD).addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.PLANKS);
		Materials.getMaterialByName(MaterialNames.WOOD).addNewBlock(Names.SLAB, net.minecraft.init.Blocks.WOODEN_SLAB);
		Materials.getMaterialByName(MaterialNames.WOOD).addNewBlock(Names.DOUBLE_SLAB, net.minecraft.init.Blocks.DOUBLE_WOODEN_SLAB);
		Materials.getMaterialByName(MaterialNames.WOOD).addNewBlock(Names.STAIRS, net.minecraft.init.Blocks.OAK_STAIRS);
		Materials.getMaterialByName(MaterialNames.WOOD).addNewItem(Names.SHEARS, net.minecraft.init.Items.SHEARS);
		
		if (Options.materialEnabled(MaterialNames.ADAMANTINE)) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.ADAMANTINE), myTabs);
			createItemsModSupport(Materials.getMaterialByName(MaterialNames.ADAMANTINE), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.ANTIMONY)) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.ANTIMONY), myTabs);
			createItemsModSupport(Materials.getMaterialByName(MaterialNames.ANTIMONY), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.AQUARIUM)) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.AQUARIUM), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.BISMUTH)) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.BISMUTH), myTabs);
			createItemsModSupport(Materials.getMaterialByName(MaterialNames.BISMUTH), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.BRASS)) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.BRASS), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.BRONZE)) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.BRONZE), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.CHARCOAL)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.CHARCOAL);

			createNugget(material, myTabs.itemsTab);
			createPowder(material, myTabs.itemsTab);
			createSmallPowder(material, myTabs.itemsTab);
			FuelRegistry.addFuel(Oredicts.NUGGET_CHARCOAL, 200);
			FuelRegistry.addFuel(Oredicts.DUST_CHARCOAL, 1600);
			FuelRegistry.addFuel(Oredicts.DUST_SMALL_CHARCOAL, 200);
			FuelRegistry.addFuel(Oredicts.DUST_TINY_CHARCOAL, 200);
			FuelRegistry.addFuel(Oredicts.BLOCK_CHARCOAL, 16000);
		}

		if (Options.materialEnabled(MaterialNames.COAL)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.COAL);

			createNugget(material, myTabs.itemsTab);
			createPowder(material, myTabs.itemsTab);
			createSmallPowder(material, myTabs.itemsTab);
			FuelRegistry.addFuel(Oredicts.NUGGET_COAL, 200);
			FuelRegistry.addFuel(Oredicts.DUST_COAL, 1600);
			FuelRegistry.addFuel(Oredicts.DUST_SMALL_COAL, 200);
			FuelRegistry.addFuel(Oredicts.DUST_TINY_COAL, 200);
		}

		if (Options.materialEnabled(MaterialNames.COLDIRON)) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.COLDIRON), myTabs);
			createItemsModSupport(Materials.getMaterialByName(MaterialNames.COLDIRON), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.COPPER)) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.COPPER), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.CUPRONICKEL)) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.CUPRONICKEL), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.DIAMOND)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.DIAMOND);

			createItemsFull(material, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.EMERALD)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.EMERALD);

			createItemsFull(material, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.ELECTRUM)) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.ELECTRUM), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.GOLD)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.GOLD);

			createItemsFull(material, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.INVAR)) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.INVAR), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.IRON)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.IRON);
			// material.addNewItem(Names.NUGGET, net.minecraft.init.Items.IRON_NUGGET); // Not till after 1.11

			createItemsFull(material, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.LAPIS)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.LAPIS);
			createSmallPowder(material, myTabs.itemsTab);

		}

		if (Options.materialEnabled(MaterialNames.LEAD)) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.LEAD), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.OBSIDIAN)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.OBSIDIAN);
			createItemsFull(material, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.PLATINUM)) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.PLATINUM), myTabs);
			createItemsModSupport(Materials.getMaterialByName(MaterialNames.PLATINUM), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.MERCURY)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.MERCURY);

			createIngot(material, myTabs.itemsTab);
			createNugget(material, myTabs.itemsTab);
			createPowder(material, myTabs.itemsTab);
			createSmallPowder(material, myTabs.itemsTab);
		}

		if (Options.materialEnabled(MaterialNames.MITHRIL)) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.MITHRIL), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.NICKEL)) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.NICKEL), myTabs);
			createItemsModSupport(Materials.getMaterialByName(MaterialNames.NICKEL), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.PEWTER)) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.PEWTER), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.REDSTONE)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.REDSTONE);
			createIngot(material, myTabs.itemsTab);
			createSmallPowder(material, myTabs.itemsTab);
		}

		if (Options.materialEnabled(MaterialNames.QUARTZ)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.QUARTZ);
			// material.slab = ;

			createItemsFull(material, myTabs);
		}

		if (Options.materialEnabled(MaterialNames.SILVER)) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.SILVER), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.STARSTEEL)) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.STARSTEEL), myTabs);
			createItemsModSupport(Materials.getMaterialByName(MaterialNames.STARSTEEL), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.STONE)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.STONE);

			createCrackhammer(material, myTabs.toolsTab);
			createRod(material, myTabs.itemsTab);
			createGear(material, myTabs.itemsTab);
		}

		if (Options.materialEnabled(MaterialNames.STEEL)) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.STEEL), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.TIN)) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.TIN), myTabs);
		}

		if (Options.materialEnabled(MaterialNames.WOOD)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.WOOD);

			createCrackhammer(material, myTabs.toolsTab);
			createGear(material, myTabs.itemsTab);
		}

		if (Options.materialEnabled(MaterialNames.ZINC)) {
			createItemsFull(Materials.getMaterialByName(MaterialNames.ZINC), myTabs);
			createItemsModSupport(Materials.getMaterialByName(MaterialNames.ZINC), myTabs);
		}

		addToMetList();

		initDone = true;
	}
}
