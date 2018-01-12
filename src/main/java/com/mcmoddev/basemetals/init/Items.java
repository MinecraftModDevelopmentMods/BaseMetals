package com.mcmoddev.basemetals.init;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.item.ItemMMDBlock;
import com.mcmoddev.lib.item.ItemMMDNugget;
import com.mcmoddev.lib.item.ItemMMDPowder;
import com.mcmoddev.lib.item.ItemMMDSmallPowder;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * This class initializes all items in Base Metals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Items extends com.mcmoddev.lib.init.Items {

	private static boolean initDone = false;

	protected Items() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		com.mcmoddev.basemetals.util.Config.init();
		Blocks.init();
		com.mcmoddev.lib.init.Items.init();

		Materials.getMaterialByName(MaterialNames.CHARCOAL).addNewItem(Names.INGOT,
				new ItemStack(net.minecraft.init.Items.COAL, 1, 1).getItem());
		Materials.getMaterialByName(MaterialNames.COAL).addNewItem(Names.INGOT,
				new ItemStack(net.minecraft.init.Items.COAL, 1, 0).getItem());

		addDiamondBits();
		addGoldBits();
		addIronBits();
		addStoneBits();
		addWoodBits();

		Materials.getMaterialByName(MaterialNames.EMERALD).addNewItem(Names.INGOT, net.minecraft.init.Items.EMERALD);
		Materials.getMaterialByName(MaterialNames.QUARTZ).addNewItem(Names.INGOT, net.minecraft.init.Items.QUARTZ);
		Materials.getMaterialByName(MaterialNames.REDSTONE).addNewItem(Names.POWDER, net.minecraft.init.Items.REDSTONE);

		doSpecialMats();

		final List<String> materials = Arrays.asList(MaterialNames.ADAMANTINE, MaterialNames.ANTIMONY,
				MaterialNames.AQUARIUM, MaterialNames.BISMUTH, MaterialNames.BRASS, MaterialNames.BRONZE,
				MaterialNames.COLDIRON, MaterialNames.COPPER, MaterialNames.CUPRONICKEL, MaterialNames.EMERALD,
				MaterialNames.ELECTRUM, MaterialNames.INVAR, MaterialNames.LEAD, MaterialNames.OBSIDIAN,
				MaterialNames.MITHRIL, MaterialNames.NICKEL, MaterialNames.PEWTER, MaterialNames.PLATINUM,
				MaterialNames.QUARTZ, MaterialNames.SILVER, MaterialNames.STARSTEEL, MaterialNames.STEEL,
				MaterialNames.TIN, MaterialNames.ZINC);

		// create and register modded stuffs
		final List<String> materialsModSupport = Arrays.asList(MaterialNames.ADAMANTINE, MaterialNames.ANTIMONY,
				MaterialNames.BISMUTH, MaterialNames.COLDIRON, MaterialNames.PLATINUM, MaterialNames.NICKEL,
				MaterialNames.STARSTEEL, MaterialNames.ZINC);

		materials.stream().filter(Materials::hasMaterial)
				.filter(materialName -> !Materials.getMaterialByName(materialName).equals(Materials.emptyMaterial))
				.forEach(materialName -> {
					final MMDMaterial material = Materials.getMaterialByName(materialName);

					create(Names.BLEND, material);
					create(Names.INGOT, material);
					create(Names.NUGGET, material);
					create(Names.POWDER, material);
					create(Names.SMALLBLEND, material);
					create(Names.SMALLPOWDER, material);

					create(Names.ARROW, material);
					create(Names.AXE, material);
					create(Names.BOLT, material);
					create(Names.BOOTS, material);
					create(Names.BOW, material);
					create(Names.CHESTPLATE, material);
					create(Names.CRACKHAMMER, material);
					create(Names.CROSSBOW, material);
					create(Names.DOOR, material);
					create(Names.FISHING_ROD, material);
					create(Names.HELMET, material);
					create(Names.HOE, material);
					create(Names.HORSE_ARMOR, material);
					create(Names.LEGGINGS, material);
					create(Names.PICKAXE, material);
					create(Names.SHEARS, material);
					create(Names.SHIELD, material);
					create(Names.SHOVEL, material);
					create(Names.SCYTHE, material);
					create(Names.SLAB, material);
					create(Names.SWORD, material);
					create(Names.ROD, material);
					create(Names.GEAR, material);
				});

		materialsModSupport.stream().filter(Materials::hasMaterial)
				.filter(materialName -> !Materials.getMaterialByName(materialName).equals(Materials.emptyMaterial))
				.forEach(materialName -> {
					final MMDMaterial material = Materials.getMaterialByName(materialName);

					create(Names.CASING, material);
					create(Names.DENSE_PLATE, material);

					if (material.hasOre()) {
						create(Names.CRUSHED, material);
						create(Names.CRUSHED_PURIFIED, material);

						createMekCrystal(material, ItemGroups.myTabs.itemsTab);
						create(Names.SHARD, material);
						create(Names.CLUMP, material);
						create(Names.POWDER_DIRTY, material);
						create(Names.CRYSTAL, material);
					}
				});

		if (Materials.hasMaterial(MaterialNames.MERCURY)) {
			final MMDMaterial mercury = Materials.getMaterialByName(MaterialNames.MERCURY);

			create(Names.INGOT, mercury);
			create(Names.NUGGET, mercury);
			create(Names.POWDER, mercury);
			create(Names.SMALLPOWDER, mercury);
		}

		Arrays.asList(MaterialNames.STONE, MaterialNames.STEEL, MaterialNames.ADAMANTINE).stream()
				.filter(Materials::hasMaterial)
				.forEach(materialName -> create(Names.ANVIL, Materials.getMaterialByName(materialName)));

		addToMetList();

		initDone = true;
	}

	private static void doSpecialMats() {
		if (Materials.hasMaterial(MaterialNames.CHARCOAL)) {
			final MMDMaterial charcoal = Materials.getMaterialByName(MaterialNames.CHARCOAL);

			create(Names.NUGGET, charcoal);
			create(Names.POWDER, charcoal);
			create(Names.SMALLPOWDER, charcoal);

			if (charcoal.hasItem(Names.NUGGET))
				((ItemMMDNugget) charcoal.getItem(Names.NUGGET)).setBurnTime(200);

			if (charcoal.hasItem(Names.POWDER))
				((ItemMMDPowder) charcoal.getItem(Names.POWDER)).setBurnTime(1600);

			if (charcoal.hasItem(Names.SMALLPOWDER))
				((ItemMMDSmallPowder) charcoal.getItem(Names.SMALLPOWDER)).setBurnTime(200);

			if (charcoal.hasBlock(Names.BLOCK))
				((ItemMMDBlock) charcoal.getItem("ItemBlock_charcoal_block")).setBurnTime(16000);
		}

		if (Materials.hasMaterial(MaterialNames.COAL)) {
			final MMDMaterial coal = Materials.getMaterialByName(MaterialNames.COAL);

			create(Names.NUGGET, coal);
			create(Names.POWDER, coal);
			create(Names.SMALLPOWDER, coal);

			if (coal.hasItem(Names.NUGGET))
				((ItemMMDNugget) coal.getItem(Names.NUGGET)).setBurnTime(200);

			if (coal.hasItem(Names.POWDER))
				((ItemMMDPowder) coal.getItem(Names.POWDER)).setBurnTime(1600);

			if (coal.hasItem(Names.SMALLPOWDER))
				((ItemMMDSmallPowder) coal.getItem(Names.SMALLPOWDER)).setBurnTime(200);
		}

		if (Materials.hasMaterial(MaterialNames.REDSTONE)) {
			final MMDMaterial redstone = Materials.getMaterialByName(MaterialNames.REDSTONE);

			create(Names.INGOT, redstone);
			create(Names.SMALLPOWDER, redstone);
		}

		if (Materials.hasMaterial(MaterialNames.LAPIS)) {
			create(Names.SMALLPOWDER, Materials.getMaterialByName(MaterialNames.LAPIS));
		}
	}

	private static void addDiamondBits() {
		final MMDMaterial diamond = Materials.getMaterialByName(MaterialNames.DIAMOND);

		diamond.addNewItem(Names.AXE, net.minecraft.init.Items.DIAMOND_AXE);
		diamond.addNewItem(Names.HOE, net.minecraft.init.Items.DIAMOND_HOE);
		diamond.addNewItem(Names.HORSE_ARMOR, net.minecraft.init.Items.DIAMOND_HORSE_ARMOR);
		diamond.addNewItem(Names.PICKAXE, net.minecraft.init.Items.DIAMOND_PICKAXE);
		diamond.addNewItem(Names.SHOVEL, net.minecraft.init.Items.DIAMOND_SHOVEL);
		diamond.addNewItem(Names.SWORD, net.minecraft.init.Items.DIAMOND_SWORD);
		diamond.addNewItem(Names.BOOTS, net.minecraft.init.Items.DIAMOND_BOOTS);
		diamond.addNewItem(Names.CHESTPLATE, net.minecraft.init.Items.DIAMOND_CHESTPLATE);
		diamond.addNewItem(Names.HELMET, net.minecraft.init.Items.DIAMOND_HELMET);
		diamond.addNewItem(Names.LEGGINGS, net.minecraft.init.Items.DIAMOND_LEGGINGS);
		diamond.addNewItem(Names.INGOT, net.minecraft.init.Items.DIAMOND);

		if (Materials.hasMaterial(MaterialNames.DIAMOND)) {
			create(Names.BLEND, diamond);
			create(Names.NUGGET, diamond);
			create(Names.POWDER, diamond);
			create(Names.SMALLBLEND, diamond);
			create(Names.SMALLPOWDER, diamond);

			create(Names.ARROW, diamond);
			create(Names.BOLT, diamond);
			create(Names.BOW, diamond);
			create(Names.CRACKHAMMER, diamond);
			create(Names.CROSSBOW, diamond);
			create(Names.DOOR, diamond);
			create(Names.FISHING_ROD, diamond);
			create(Names.SHEARS, diamond);
			create(Names.SHIELD, diamond);
			create(Names.SLAB, diamond);
			create(Names.ROD, diamond);
			create(Names.GEAR, diamond);
			create(Names.SCYTHE, diamond);
		}
	}

	private static void addGoldBits() {
		final MMDMaterial gold = Materials.getMaterialByName(MaterialNames.GOLD);

		gold.addNewItem(Names.AXE, net.minecraft.init.Items.GOLDEN_AXE);
		gold.addNewItem(Names.HOE, net.minecraft.init.Items.GOLDEN_HOE);
		gold.addNewItem(Names.HORSE_ARMOR, net.minecraft.init.Items.GOLDEN_HORSE_ARMOR);
		gold.addNewItem(Names.PICKAXE, net.minecraft.init.Items.GOLDEN_PICKAXE);
		gold.addNewItem(Names.SHOVEL, net.minecraft.init.Items.GOLDEN_SHOVEL);
		gold.addNewItem(Names.SWORD, net.minecraft.init.Items.GOLDEN_SWORD);
		gold.addNewItem(Names.BOOTS, net.minecraft.init.Items.GOLDEN_BOOTS);
		gold.addNewItem(Names.CHESTPLATE, net.minecraft.init.Items.GOLDEN_CHESTPLATE);
		gold.addNewItem(Names.HELMET, net.minecraft.init.Items.GOLDEN_HELMET);
		gold.addNewItem(Names.LEGGINGS, net.minecraft.init.Items.GOLDEN_LEGGINGS);
		gold.addNewItem(Names.INGOT, net.minecraft.init.Items.GOLD_INGOT);
		gold.addNewItem(Names.NUGGET, net.minecraft.init.Items.GOLD_NUGGET);

		if (Materials.hasMaterial(MaterialNames.GOLD)) {
			create(Names.BLEND, gold);
			create(Names.POWDER, gold);
			create(Names.SMALLBLEND, gold);
			create(Names.SMALLPOWDER, gold);

			create(Names.ARROW, gold);
			create(Names.BOLT, gold);
			create(Names.BOW, gold);
			create(Names.CRACKHAMMER, gold);
			create(Names.CROSSBOW, gold);
			create(Names.DOOR, gold);
			create(Names.FISHING_ROD, gold);
			create(Names.SHEARS, gold);
			create(Names.SHIELD, gold);
			create(Names.SLAB, gold);
			create(Names.ROD, gold);
			create(Names.GEAR, gold);
			create(Names.SCYTHE, gold);
		}
	}

	private static void addIronBits() {
		final MMDMaterial iron = Materials.getMaterialByName(MaterialNames.IRON);

		iron.addNewItem(Names.AXE, net.minecraft.init.Items.IRON_AXE);
		iron.addNewItem(Names.DOOR, net.minecraft.init.Items.IRON_DOOR);
		iron.addNewItem(Names.HOE, net.minecraft.init.Items.IRON_HOE);
		iron.addNewItem(Names.HORSE_ARMOR, net.minecraft.init.Items.IRON_HORSE_ARMOR);
		iron.addNewItem(Names.PICKAXE, net.minecraft.init.Items.IRON_PICKAXE);
		iron.addNewItem(Names.SHOVEL, net.minecraft.init.Items.IRON_SHOVEL);
		iron.addNewItem(Names.SWORD, net.minecraft.init.Items.IRON_SWORD);
		iron.addNewItem(Names.BOOTS, net.minecraft.init.Items.IRON_BOOTS);
		iron.addNewItem(Names.CHESTPLATE, net.minecraft.init.Items.IRON_CHESTPLATE);
		iron.addNewItem(Names.HELMET, net.minecraft.init.Items.IRON_HELMET);
		iron.addNewItem(Names.LEGGINGS, net.minecraft.init.Items.IRON_LEGGINGS);
		iron.addNewItem(Names.INGOT, net.minecraft.init.Items.IRON_INGOT);
		iron.addNewItem(Names.NUGGET, net.minecraft.init.Items.IRON_NUGGET);
		iron.addNewItem(Names.SHEARS, net.minecraft.init.Items.SHEARS);

		if (Materials.hasMaterial(MaterialNames.IRON)) {
			create(Names.BLEND, iron);
			create(Names.INGOT, iron);
			create(Names.NUGGET, iron);
			create(Names.POWDER, iron);
			create(Names.SMALLBLEND, iron);
			create(Names.SMALLPOWDER, iron);

			create(Names.ARROW, iron);
			create(Names.AXE, iron);
			create(Names.BOLT, iron);
			create(Names.BOOTS, iron);
			create(Names.BOW, iron);
			create(Names.CHESTPLATE, iron);
			create(Names.CRACKHAMMER, iron);
			create(Names.CROSSBOW, iron);
			create(Names.DOOR, iron);
			create(Names.FISHING_ROD, iron);
			create(Names.HELMET, iron);
			create(Names.HOE, iron);
			create(Names.HORSE_ARMOR, iron);
			create(Names.LEGGINGS, iron);
			create(Names.PICKAXE, iron);
			create(Names.SHEARS, iron);
			create(Names.SHIELD, iron);
			create(Names.SHOVEL, iron);
			create(Names.SLAB, iron);
			create(Names.SWORD, iron);
			create(Names.ROD, iron);
			create(Names.GEAR, iron);
			create(Names.SCYTHE, iron);
		}
	}

	private static void addStoneBits() {
		final MMDMaterial stone = Materials.getMaterialByName(MaterialNames.STONE);

		stone.addNewItem(Names.AXE, net.minecraft.init.Items.STONE_AXE);
		stone.addNewItem(Names.HOE, net.minecraft.init.Items.STONE_HOE);
		stone.addNewItem(Names.PICKAXE, net.minecraft.init.Items.STONE_PICKAXE);
		stone.addNewItem(Names.SHOVEL, net.minecraft.init.Items.STONE_SHOVEL);
		stone.addNewItem(Names.SWORD, net.minecraft.init.Items.STONE_SWORD);
		stone.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.STONE);
		stone.addNewBlock(Names.SLAB, net.minecraft.init.Blocks.STONE_SLAB);
		stone.addNewBlock(Names.DOUBLE_SLAB, net.minecraft.init.Blocks.DOUBLE_STONE_SLAB);
		stone.addNewBlock(Names.STAIRS, net.minecraft.init.Blocks.STONE_STAIRS);

		if (Materials.hasMaterial(MaterialNames.STONE)) {
			create(Names.CRACKHAMMER, stone);
			create(Names.ROD, stone);
			create(Names.GEAR, stone);
			create(Names.SCYTHE, stone);
		}
	}

	private static void addWoodBits() {
		final MMDMaterial wood = Materials.getMaterialByName(MaterialNames.WOOD);

		wood.addNewItem(Names.AXE, net.minecraft.init.Items.WOODEN_AXE);
		wood.addNewItem(Names.DOOR, net.minecraft.init.Items.OAK_DOOR);
		wood.addNewItem(Names.HOE, net.minecraft.init.Items.WOODEN_HOE);
		wood.addNewItem(Names.PICKAXE, net.minecraft.init.Items.WOODEN_PICKAXE);
		wood.addNewItem(Names.SHOVEL, net.minecraft.init.Items.WOODEN_SHOVEL);
		wood.addNewItem(Names.SWORD, net.minecraft.init.Items.WOODEN_SWORD);
		wood.addNewBlock(Names.DOOR, net.minecraft.init.Blocks.OAK_DOOR);
		wood.addNewBlock(Names.ORE, net.minecraft.init.Blocks.LOG);
		wood.addNewBlock(Names.TRAPDOOR, net.minecraft.init.Blocks.TRAPDOOR);
		wood.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.PLANKS);
		wood.addNewBlock(Names.SLAB, net.minecraft.init.Blocks.WOODEN_SLAB);
		wood.addNewBlock(Names.DOUBLE_SLAB, net.minecraft.init.Blocks.DOUBLE_WOODEN_SLAB);
		wood.addNewBlock(Names.STAIRS, net.minecraft.init.Blocks.OAK_STAIRS);
		wood.addNewItem(Names.SHEARS, net.minecraft.init.Items.SHEARS);

		if (Materials.hasMaterial(MaterialNames.WOOD)) {
			create(Names.CRACKHAMMER, wood);
			create(Names.GEAR, wood);
			create(Names.SCYTHE, wood);
		}
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		for (MMDMaterial mat : Materials.getMaterialsByMod(BaseMetals.MODID)) {
			for (Item item : mat.getItems()) {
				if (item.getRegistryName().getResourceDomain().equals(BaseMetals.MODID)) {
					event.getRegistry().register(item);
				}
			}
		}

		if (Blocks.humanDetector != null) {
			final ItemBlock itemBlock = new ItemBlock(Blocks.humanDetector);

			itemBlock.setRegistryName("human_detector");
			itemBlock
					.setUnlocalizedName(Blocks.humanDetector.getRegistryName().getResourceDomain() + ".human_detector");
			event.getRegistry().register(itemBlock);
		}

		Oredicts.registerItemOreDictionaryEntries();
		Oredicts.registerBlockOreDictionaryEntries();
	}

	protected static Item create(@Nonnull final Names name, @Nonnull final MMDMaterial material) {
		CreativeTabs tab;

		if ((name.equals(Names.DOOR)) || (name.equals(Names.SLAB))) {
			tab = ItemGroups.myTabs.blocksTab;
		} else if ((name.equals(Names.BLEND)) || (name.equals(Names.INGOT)) || (name.equals(Names.NUGGET))
				|| (name.equals(Names.POWDER)) || (name.equals(Names.SMALLBLEND)) || (name.equals(Names.SMALLPOWDER))
				|| (name.equals(Names.ROD)) || (name.equals(Names.GEAR))) {
			tab = ItemGroups.myTabs.itemsTab;
		} else {
			tab = ItemGroups.myTabs.toolsTab;
		}

		return create(name, material, tab);
	}
}
