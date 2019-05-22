package com.mcmoddev.basemetals.vanillabits;

import com.mcmoddev.lib.data.MaterialNames;
import com.mcmoddev.lib.events.MMDLibRegisterBlocks;
import com.mcmoddev.lib.init.Blocks;
import com.mcmoddev.lib.init.Materials;

import java.util.Arrays;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Config.Options;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid=BaseMetals.MODID)
public class VanillaBlocks extends Blocks {

	private VanillaBlocks() {
		throw new IllegalAccessError("Class cannot be instantiated!");
	}

	@SubscribeEvent
	public static void registerVanilla(MMDLibRegisterBlocks ev) {
		// Vanilla Materials get their Ore and Block always
		final MMDMaterial charcoal = Materials.getMaterialByName(MaterialNames.CHARCOAL);
		final MMDMaterial gold = Materials.getMaterialByName(MaterialNames.GOLD);
		final MMDMaterial iron = Materials.getMaterialByName(MaterialNames.IRON);
		final MMDMaterial quartz = Materials.getMaterialByName(MaterialNames.QUARTZ);

		if (Materials.hasMaterial(MaterialNames.CHARCOAL)) {
			create(Names.BLOCK, charcoal);
		}

		Arrays.stream(new String[] {MaterialNames.DIAMOND, MaterialNames.EMERALD, MaterialNames.GOLD,
				MaterialNames.OBSIDIAN })
		.filter(n -> Options.isMaterialEnabled(n.toString()))
		.filter(Materials::hasMaterial)
		.map(Materials::getMaterialByName)
		.forEach(mat -> {
			Arrays.stream(new Names[] { Names.BARS, Names.DOOR, Names.TRAPDOOR, Names.BUTTON, Names.SLAB, Names.DOUBLE_SLAB,
					Names.LEVER, Names.PRESSURE_PLATE, Names.STAIRS, Names.WALL }).forEach(n -> create(n, mat));
		});

		if (Materials.hasMaterial(MaterialNames.GOLD) && Options.isMaterialEnabled(MaterialNames.GOLD)) {
			create(Names.PLATE, gold);
		}
		
 		if (Materials.hasMaterial(MaterialNames.IRON) && Options.isMaterialEnabled(MaterialNames.IRON)) {
			create(Names.PLATE, iron);

			create(Names.BUTTON, iron);
			create(Names.SLAB, iron);
			create(Names.DOUBLE_SLAB, iron);
			create(Names.LEVER, iron);
			create(Names.PRESSURE_PLATE, iron);
			create(Names.STAIRS, iron);
			create(Names.WALL, iron);
		}

		if (Materials.hasMaterial(MaterialNames.QUARTZ) && Options.isMaterialEnabled(MaterialNames.QUARTZ)) {
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
