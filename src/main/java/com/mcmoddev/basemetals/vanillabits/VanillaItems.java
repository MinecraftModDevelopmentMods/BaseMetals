package com.mcmoddev.basemetals.vanillabits;

import java.util.Arrays;

import javax.annotation.Nonnull;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.MaterialNames;
import com.mcmoddev.lib.events.MMDLibRegisterItems;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.IMMDBurnableObject;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Config.Options;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid=BaseMetals.MODID)
public class VanillaItems extends com.mcmoddev.lib.init.Items {

	private VanillaItems() {
		// TODO Auto-generated constructor stub
	}
	
	@SubscribeEvent
	public static void registerItemsEvent(MMDLibRegisterItems ev) {
		addDiamondBits();
		addGoldBits();
		addIronBits();
		addStoneBits();
		addWoodBits();
		doSpecialMats();
		
		// these three are the only ones that need tools and armor
		Arrays.asList(MaterialNames.EMERALD, MaterialNames.OBSIDIAN, MaterialNames.QUARTZ)
		.stream().map(Materials::getMaterialByName)
		.filter(mat -> Options.isMaterialEnabled(mat.toString()))
		.forEach(material -> Arrays.asList(Names.AXE, Names.BOOTS, 
				Names.CHESTPLATE, Names.HELMET, Names.HOE, Names.LEGGINGS, Names.PICKAXE, Names.SHOVEL, Names.HORSE_ARMOR)
				.stream()
				.filter(n -> !material.hasItem(n)).forEach(n -> create(n, material)));
		
		// shields
		Arrays.asList(MaterialNames.DIAMOND, MaterialNames.EMERALD, MaterialNames.GOLD, 
				MaterialNames.IRON, MaterialNames.OBSIDIAN, MaterialNames.QUARTZ)
		.stream().map(Materials::getMaterialByName)
		.filter(n -> Options.isMaterialEnabled(n.toString()))
		.forEach(material -> Arrays.asList(Names.BOLT, Names.ARROW, Names.BOW, 
				Names.CROSSBOW, Names.FISHING_ROD, Names.ROD, Names.BARS, Names.SHEARS, Names.BUTTON, Names.SWORD,
				Names.SHIELD, Names.DOOR, Names.SLAB)
				.stream()
				.filter(n -> !material.hasItem(n)).forEach(n -> create(n, material)));
		
		// bits that everything should have
		Arrays.asList(MaterialNames.DIAMOND, MaterialNames.EMERALD, MaterialNames.GOLD, 
				MaterialNames.IRON, MaterialNames.OBSIDIAN, MaterialNames.QUARTZ, 
				MaterialNames.STONE, MaterialNames.WOOD)
		.stream().map(Materials::getMaterialByName)
		.filter(n -> Options.isMaterialEnabled(n.toString()))
		.forEach(material -> Arrays.asList(Names.CRACKHAMMER, Names.GEAR, 
				Names.SCYTHE).stream()
				.filter(n -> !material.hasItem(n)).forEach(n -> create(n, material)));
		
		// last few bits
		Arrays.asList(MaterialNames.COAL, MaterialNames.CHARCOAL, MaterialNames.DIAMOND,
				MaterialNames.EMERALD, MaterialNames.GOLD, MaterialNames.IRON, 
				MaterialNames.OBSIDIAN, MaterialNames.REDSTONE, 
				MaterialNames.QUARTZ).stream()
		.map(Materials::getMaterialByName)
		.filter(n -> Options.isMaterialEnabled(n.toString()))
		.forEach(material -> 
		Arrays.asList(Names.SMALLPOWDER, Names.POWDER).stream()
		.filter(n -> !material.hasItem(n))
		.forEach(n -> create(n, material)));
		Arrays.asList(MaterialNames.COAL, MaterialNames.CHARCOAL, MaterialNames.DIAMOND,
				MaterialNames.EMERALD, MaterialNames.GOLD, MaterialNames.IRON, 
				MaterialNames.OBSIDIAN, MaterialNames.QUARTZ).stream()
		.filter(Options::isMaterialEnabled)
		.map(Materials::getMaterialByName)
		.filter(m -> !m.hasItem(Names.NUGGET)).forEach(material -> create(Names.NUGGET, material));
		
		//these bits just are too... specialized to fit the iteration above
		if(Options.isMaterialEnabled(MaterialNames.STONE)) {
			create(Names.ROD, Materials.getMaterialByName(MaterialNames.STONE));
		}
		if(Options.isMaterialEnabled(MaterialNames.LAPIS)) {
			create(Names.SMALLPOWDER, Materials.getMaterialByName(MaterialNames.LAPIS));
		}
		if(Options.isMaterialEnabled(MaterialNames.OBSIDIAN)) {
			create(Names.INGOT, Materials.getMaterialByName(MaterialNames.OBSIDIAN));
		}
		if(Options.isMaterialEnabled(MaterialNames.REDSTONE)) {
			create(Names.INGOT, Materials.getMaterialByName(MaterialNames.REDSTONE));
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
		if (material.hasBlock(Names.BLOCK) && material.getName().equals(MaterialNames.CHARCOAL)) {
			((IMMDBurnableObject) material.getItem("ItemBlock_charcoal_block")).setBurnTime(BLOCK_BURN_TIME);
		}
	}
	
	private static void doSpecialMats() {
		if (Materials.hasMaterial(MaterialNames.CHARCOAL) && 
				Options.isMaterialEnabled(MaterialNames.CHARCOAL)) {
			final MMDMaterial charcoal = Materials.getMaterialByName(MaterialNames.CHARCOAL);

			create(Names.NUGGET, charcoal);
			create(Names.POWDER, charcoal);
			create(Names.SMALLPOWDER, charcoal);

			setBurnTimes(charcoal);
		}

		if (Materials.hasMaterial(MaterialNames.COAL) && 
				Options.isMaterialEnabled(MaterialNames.COAL)) {
			final MMDMaterial coal = Materials.getMaterialByName(MaterialNames.COAL);

			create(Names.NUGGET, coal);
			create(Names.POWDER, coal);
			create(Names.SMALLPOWDER, coal);

			setBurnTimes(coal);
		}

		if (Materials.hasMaterial(MaterialNames.REDSTONE) && 
				Options.isMaterialEnabled(MaterialNames.REDSTONE)) {
			final MMDMaterial redstone = Materials.getMaterialByName(MaterialNames.REDSTONE);

			create(Names.INGOT, redstone);
			create(Names.SMALLPOWDER, redstone);
		}

		if (Materials.hasMaterial(MaterialNames.LAPIS) && 
				Options.isMaterialEnabled(MaterialNames.LAPIS)) {
			create(Names.SMALLPOWDER, Materials.getMaterialByName(MaterialNames.LAPIS));
		}
	}


	private static void addDiamondBits() {
		final MMDMaterial diamond = Materials.getMaterialByName(MaterialNames.DIAMOND);

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

		if (Materials.hasMaterial(MaterialNames.STONE)) {
			create(Names.CRACKHAMMER, stone);
			create(Names.ROD, stone);
			create(Names.GEAR, stone);
			create(Names.SCYTHE, stone);
		}
	}

	private static void addWoodBits() {
		final MMDMaterial wood = Materials.getMaterialByName(MaterialNames.WOOD);

		if (Materials.hasMaterial(MaterialNames.WOOD)) {
			create(Names.CRACKHAMMER, wood);
			create(Names.GEAR, wood);
			create(Names.SCYTHE, wood);
		}
	}
}
