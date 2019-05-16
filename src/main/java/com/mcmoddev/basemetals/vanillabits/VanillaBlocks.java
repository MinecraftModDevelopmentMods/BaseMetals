package com.mcmoddev.basemetals.vanillabits;

import com.mcmoddev.lib.data.VanillaMaterialNames;
import com.mcmoddev.lib.events.MMDLibRegisterBlocks;
import com.mcmoddev.lib.init.Blocks;
import com.mcmoddev.lib.init.Materials;

import java.util.Arrays;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Config.Options;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid=BaseMetals.MODID)
public class VanillaBlocks extends Blocks {

	private VanillaBlocks() {
		throw new IllegalAccessError("Class cannot be instantiated!");
	}

	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public static void registerVanilla(MMDLibRegisterBlocks ev) {
		// Vanilla Materials get their Ore and Block always
		final MMDMaterial charcoal = Materials.getMaterialByName(VanillaMaterialNames.CHARCOAL);
		final MMDMaterial coal = Materials.getMaterialByName(VanillaMaterialNames.COAL);
		final MMDMaterial diamond = Materials.getMaterialByName(VanillaMaterialNames.DIAMOND);
		final MMDMaterial emerald = Materials.getMaterialByName(VanillaMaterialNames.EMERALD);
		final MMDMaterial gold = Materials.getMaterialByName(VanillaMaterialNames.GOLD);
		final MMDMaterial iron = Materials.getMaterialByName(VanillaMaterialNames.IRON);
		final MMDMaterial lapis = Materials.getMaterialByName(VanillaMaterialNames.LAPIS);
		final MMDMaterial obsidian = Materials.getMaterialByName(VanillaMaterialNames.OBSIDIAN);
		final MMDMaterial quartz = Materials.getMaterialByName(VanillaMaterialNames.QUARTZ);
		final MMDMaterial redstone = Materials.getMaterialByName(VanillaMaterialNames.REDSTONE);

		coal.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.COAL_BLOCK);
		coal.addNewBlock(Names.ORE, net.minecraft.init.Blocks.COAL_ORE);

		diamond.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.DIAMOND_BLOCK);
		diamond.addNewBlock(Names.ORE, net.minecraft.init.Blocks.DIAMOND_ORE);

		emerald.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.EMERALD_BLOCK);
		emerald.addNewBlock(Names.ORE, net.minecraft.init.Blocks.EMERALD_ORE);

		gold.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.GOLD_BLOCK);
		gold.addNewBlock(Names.ORE, net.minecraft.init.Blocks.GOLD_ORE);
		gold.addNewBlock(Names.PRESSURE_PLATE,
				net.minecraft.init.Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE);

		iron.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.IRON_BLOCK);
		iron.addNewBlock(Names.ORE, net.minecraft.init.Blocks.IRON_ORE);
		iron.addNewBlock(Names.BARS, net.minecraft.init.Blocks.IRON_BARS);
		iron.addNewBlock(Names.DOOR, net.minecraft.init.Blocks.IRON_DOOR);
		iron.addNewBlock(Names.TRAPDOOR, net.minecraft.init.Blocks.IRON_TRAPDOOR);
		iron.addNewBlock(Names.PRESSURE_PLATE,
				net.minecraft.init.Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);

		lapis.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.LAPIS_BLOCK);
		lapis.addNewBlock(Names.ORE, net.minecraft.init.Blocks.LAPIS_ORE);

		obsidian.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.OBSIDIAN);

		quartz.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.QUARTZ_BLOCK);
		quartz.addNewBlock(Names.ORE, net.minecraft.init.Blocks.QUARTZ_ORE);
		quartz.addNewBlock(Names.STAIRS, net.minecraft.init.Blocks.QUARTZ_STAIRS);

		redstone.addNewBlock(Names.BLOCK, net.minecraft.init.Blocks.REDSTONE_BLOCK);
		redstone.addNewBlock(Names.ORE, net.minecraft.init.Blocks.REDSTONE_ORE);

		if (Materials.hasMaterial(VanillaMaterialNames.CHARCOAL)) {
			create(Names.BLOCK, charcoal);
		}

		Arrays.stream(new String[] {VanillaMaterialNames.DIAMOND, VanillaMaterialNames.EMERALD, VanillaMaterialNames.GOLD,
				VanillaMaterialNames.OBSIDIAN })
		.filter(n -> Options.isMaterialEnabled(n.toString()))
		.filter(Materials::hasMaterial)
		.map(Materials::getMaterialByName)
		.forEach(mat -> {
			Arrays.stream(new Names[] { Names.BARS, Names.DOOR, Names.TRAPDOOR, Names.BUTTON, Names.SLAB, Names.DOUBLE_SLAB,
					Names.LEVER, Names.PRESSURE_PLATE, Names.STAIRS, Names.WALL }).forEach(n -> create(n, mat));
		});

		if (Materials.hasMaterial(VanillaMaterialNames.GOLD) && Options.isMaterialEnabled(VanillaMaterialNames.GOLD)) {
			create(Names.PLATE, gold);
		}
		
 		if (Materials.hasMaterial(VanillaMaterialNames.IRON) && Options.isMaterialEnabled(VanillaMaterialNames.IRON)) {
			create(Names.PLATE, iron);

			create(Names.BUTTON, iron);
			create(Names.SLAB, iron);
			create(Names.DOUBLE_SLAB, iron);
			create(Names.LEVER, iron);
			create(Names.PRESSURE_PLATE, iron);
			create(Names.STAIRS, iron);
			create(Names.WALL, iron);
		}

		if (Materials.hasMaterial(VanillaMaterialNames.QUARTZ) && Options.isMaterialEnabled(VanillaMaterialNames.QUARTZ)) {
			create(Names.BARS, quartz);
			create(Names.DOOR, quartz);
			create(Names.TRAPDOOR, quartz);

			create(Names.BUTTON, quartz);
			create(Names.LEVER, quartz);
			create(Names.PRESSURE_PLATE, quartz);
			create(Names.WALL, quartz);
		}
	}
}
