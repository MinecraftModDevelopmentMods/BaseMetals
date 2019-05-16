package com.mcmoddev.basemetals.vanillabits;

import java.util.Arrays;

import javax.annotation.Nonnull;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.VanillaMaterialNames;
import com.mcmoddev.lib.events.MMDLibRegisterItems;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.IMMDBurnableObject;
import com.mcmoddev.lib.material.MMDMaterial;
//import com.mcmoddev.lib.util.Config;
import com.mcmoddev.lib.util.Config.Options;

import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid=BaseMetals.MODID)
public class VanillaItems extends com.mcmoddev.lib.init.Items {

	private VanillaItems() {
		// TODO Auto-generated constructor stub
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public static void registerItemsEvent(MMDLibRegisterItems ev) {
		Materials.getMaterialByName(VanillaMaterialNames.CHARCOAL).addNewItemFromItemStack(Names.INGOT,
				new ItemStack(net.minecraft.init.Items.COAL, 1, 1));
		Materials.getMaterialByName(VanillaMaterialNames.COAL).addNewItemFromItemStack(Names.INGOT,
				new ItemStack(net.minecraft.init.Items.COAL, 1, 0));

		Materials.getMaterialByName(VanillaMaterialNames.EMERALD).addNewItem(Names.INGOT,
				net.minecraft.init.Items.EMERALD);
		Materials.getMaterialByName(VanillaMaterialNames.LAPIS).addNewItemFromItemStack(Names.INGOT,
				new ItemStack(net.minecraft.init.Items.DYE, 1, 4));
		Materials.getMaterialByName(VanillaMaterialNames.QUARTZ).addNewItem(Names.INGOT,
				net.minecraft.init.Items.QUARTZ);
		Materials.getMaterialByName(VanillaMaterialNames.REDSTONE).addNewItem(Names.POWDER,
				net.minecraft.init.Items.REDSTONE);
		addDiamondBits();
		addGoldBits();
		addIronBits();
		addStoneBits();
		addWoodBits();
		doSpecialMats();
		
		// these three are the only ones that need tools and armor
		Arrays.asList(VanillaMaterialNames.EMERALD, VanillaMaterialNames.OBSIDIAN, VanillaMaterialNames.QUARTZ)
		.stream().map(Materials::getMaterialByName)
		.filter(mat -> Options.isMaterialEnabled(mat.toString()))
		.forEach(material -> Arrays.asList(Names.AXE, Names.BOOTS, 
				Names.CHESTPLATE, Names.HELMET, Names.HOE, Names.LEGGINGS, Names.PICKAXE, Names.SHOVEL, Names.HORSE_ARMOR)
				.stream()
				.filter(n -> !material.hasItem(n)).forEach(n -> create(n, material)));
		
		// shields
		Arrays.asList(VanillaMaterialNames.DIAMOND, VanillaMaterialNames.EMERALD, VanillaMaterialNames.GOLD, 
				VanillaMaterialNames.IRON, VanillaMaterialNames.OBSIDIAN, VanillaMaterialNames.QUARTZ)
		.stream().map(Materials::getMaterialByName)
		.filter(n -> Options.isMaterialEnabled(n.toString()))
		.forEach(material -> Arrays.asList(Names.BOLT, Names.ARROW, Names.BOW, 
				Names.CROSSBOW, Names.FISHING_ROD, Names.ROD, Names.BARS, Names.SHEARS, Names.BUTTON, Names.SWORD,
				Names.SHIELD, Names.DOOR, Names.SLAB)
				.stream()
				.filter(n -> !material.hasItem(n)).forEach(n -> create(n, material)));
		
		// bits that everything should have
		Arrays.asList(VanillaMaterialNames.DIAMOND, VanillaMaterialNames.EMERALD, VanillaMaterialNames.GOLD, 
				VanillaMaterialNames.IRON, VanillaMaterialNames.OBSIDIAN, VanillaMaterialNames.QUARTZ, 
				VanillaMaterialNames.STONE, VanillaMaterialNames.WOOD)
		.stream().map(Materials::getMaterialByName)
		.filter(n -> Options.isMaterialEnabled(n.toString()))
		.forEach(material -> Arrays.asList(Names.CRACKHAMMER, Names.GEAR, 
				Names.SCYTHE).stream()
				.filter(n -> !material.hasItem(n)).forEach(n -> create(n, material)));
		
		// last few bits
		Arrays.asList(VanillaMaterialNames.COAL, VanillaMaterialNames.CHARCOAL, VanillaMaterialNames.DIAMOND,
				VanillaMaterialNames.EMERALD, VanillaMaterialNames.GOLD, VanillaMaterialNames.IRON, 
				VanillaMaterialNames.OBSIDIAN, VanillaMaterialNames.REDSTONE, 
				VanillaMaterialNames.QUARTZ).stream()
		.map(Materials::getMaterialByName)
		.filter(n -> Options.isMaterialEnabled(n.toString()))
		.forEach(material -> 
		Arrays.asList(Names.SMALLPOWDER, Names.POWDER).stream()
		.filter(n -> !material.hasItem(n))
		.forEach(n -> create(n, material)));
		Arrays.asList(VanillaMaterialNames.COAL, VanillaMaterialNames.CHARCOAL, VanillaMaterialNames.DIAMOND,
				VanillaMaterialNames.EMERALD, VanillaMaterialNames.GOLD, VanillaMaterialNames.IRON, 
				VanillaMaterialNames.OBSIDIAN, VanillaMaterialNames.QUARTZ).stream()
		.filter(Options::isMaterialEnabled)
		.map(Materials::getMaterialByName)
		.filter(m -> !m.hasItem(Names.NUGGET)).forEach(material -> create(Names.NUGGET, material));
		
		//these bits just are too... specialized to fit the iteration above
		if(Options.isMaterialEnabled(VanillaMaterialNames.STONE)) {
			create(Names.ROD, Materials.getMaterialByName(VanillaMaterialNames.STONE));
		}
		if(Options.isMaterialEnabled(VanillaMaterialNames.LAPIS)) {
			create(Names.SMALLPOWDER, Materials.getMaterialByName(VanillaMaterialNames.LAPIS));
		}
		if(Options.isMaterialEnabled(VanillaMaterialNames.OBSIDIAN)) {
			create(Names.INGOT, Materials.getMaterialByName(VanillaMaterialNames.OBSIDIAN));
		}
		if(Options.isMaterialEnabled(VanillaMaterialNames.REDSTONE)) {
			create(Names.INGOT, Materials.getMaterialByName(VanillaMaterialNames.REDSTONE));
		}
	}
	
	private static void setBurnTimes(@Nonnull final MMDMaterial material) {
		if (material.hasItem(Names.NUGGET)) {
			((IMMDBurnableObject) material.getItem(Names.NUGGET)).setBurnTime(NUGGET_BURN_TIME);
		}

		if (material.hasItem(Names.POWDER)) {
			((IMMDBurnableObject) material.getItem(Names.POWDER)).setBurnTime(INGOT_BURN_TIME);
		}

		if (material.hasItem(Names.SMALLPOWDER)) {
			((IMMDBurnableObject) material.getItem(Names.SMALLPOWDER)).setBurnTime(NUGGET_BURN_TIME);
		}

		// simple hack to fix this shit - I give up on trying for more
		if (material.hasBlock(Names.BLOCK) && material.getName().equals(VanillaMaterialNames.CHARCOAL)) {
			((IMMDBurnableObject) material.getItem("ItemBlock_charcoal_block")).setBurnTime(BLOCK_BURN_TIME);
		}
	}
	
	private static void doSpecialMats() {
		if (Materials.hasMaterial(VanillaMaterialNames.CHARCOAL) && 
				Options.isMaterialEnabled(VanillaMaterialNames.CHARCOAL)) {
			final MMDMaterial charcoal = Materials.getMaterialByName(VanillaMaterialNames.CHARCOAL);

			create(Names.NUGGET, charcoal);
			create(Names.POWDER, charcoal);
			create(Names.SMALLPOWDER, charcoal);

			setBurnTimes(charcoal);
		}

		if (Materials.hasMaterial(VanillaMaterialNames.COAL) && 
				Options.isMaterialEnabled(VanillaMaterialNames.COAL)) {
			final MMDMaterial coal = Materials.getMaterialByName(VanillaMaterialNames.COAL);

			create(Names.NUGGET, coal);
			create(Names.POWDER, coal);
			create(Names.SMALLPOWDER, coal);

			setBurnTimes(coal);
		}

		if (Materials.hasMaterial(VanillaMaterialNames.REDSTONE) && 
				Options.isMaterialEnabled(VanillaMaterialNames.REDSTONE)) {
			final MMDMaterial redstone = Materials.getMaterialByName(VanillaMaterialNames.REDSTONE);

			create(Names.INGOT, redstone);
			create(Names.SMALLPOWDER, redstone);
		}

		if (Materials.hasMaterial(VanillaMaterialNames.LAPIS) && 
				Options.isMaterialEnabled(VanillaMaterialNames.LAPIS)) {
			create(Names.SMALLPOWDER, Materials.getMaterialByName(VanillaMaterialNames.LAPIS));
		}
	}


	private static void addDiamondBits() {
		final MMDMaterial diamond = Materials.getMaterialByName(VanillaMaterialNames.DIAMOND);

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

		if (Materials.hasMaterial(VanillaMaterialNames.DIAMOND)) {
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
		final MMDMaterial gold = Materials.getMaterialByName(VanillaMaterialNames.GOLD);

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

		if (Materials.hasMaterial(VanillaMaterialNames.GOLD)) {
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
		final MMDMaterial iron = Materials.getMaterialByName(VanillaMaterialNames.IRON);

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

		if (Materials.hasMaterial(VanillaMaterialNames.IRON)) {
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
		final MMDMaterial stone = Materials.getMaterialByName(VanillaMaterialNames.STONE);

		stone.addNewItem(Names.AXE, net.minecraft.init.Items.STONE_AXE);
		stone.addNewItem(Names.HOE, net.minecraft.init.Items.STONE_HOE);
		stone.addNewItem(Names.PICKAXE, net.minecraft.init.Items.STONE_PICKAXE);
		stone.addNewItem(Names.SHOVEL, net.minecraft.init.Items.STONE_SHOVEL);
		stone.addNewItem(Names.SWORD, net.minecraft.init.Items.STONE_SWORD);
		stone.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.STONE);
		stone.addNewBlock(Names.SLAB, net.minecraft.init.Blocks.STONE_SLAB);
		stone.addNewBlock(Names.DOUBLE_SLAB, net.minecraft.init.Blocks.DOUBLE_STONE_SLAB);
		stone.addNewBlock(Names.STAIRS, net.minecraft.init.Blocks.STONE_STAIRS);

		if (Materials.hasMaterial(VanillaMaterialNames.STONE)) {
			create(Names.CRACKHAMMER, stone);
			create(Names.ROD, stone);
			create(Names.GEAR, stone);
			create(Names.SCYTHE, stone);
		}
	}

	private static void addWoodBits() {
		final MMDMaterial wood = Materials.getMaterialByName(VanillaMaterialNames.WOOD);

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
		
		if (Materials.hasMaterial(VanillaMaterialNames.WOOD)) {
			create(Names.CRACKHAMMER, wood);
			create(Names.GEAR, wood);
			create(Names.SCYTHE, wood);
		}
	}
}
