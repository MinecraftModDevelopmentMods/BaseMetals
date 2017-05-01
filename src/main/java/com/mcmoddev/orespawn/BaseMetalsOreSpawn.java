package com.mcmoddev.orespawn;

import com.google.common.base.Function;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.data.Names;

import mmd.orespawn.api.OreSpawnAPI;
import mmd.orespawn.api.SpawnLogic;

public class BaseMetalsOreSpawn implements Function<OreSpawnAPI, SpawnLogic> {

	@Override
	public SpawnLogic apply(OreSpawnAPI api) {
		SpawnLogic logic = api.createSpawnLogic();

		if(Options.materialEnabled.get(MaterialNames.COLDIRON)) {
			logic.getDimension(-1).addOre(Materials.getMaterialByName(MaterialNames.COLDIRON).getBlock(Names.ORE).getDefaultState(), 8, 4, 5, 0, 128);
		}

		if(Options.materialEnabled.get(MaterialNames.ADAMANTINE)) {
			logic.getDimension(-1).addOre(Materials.getMaterialByName(MaterialNames.ADAMANTINE).getBlock(Names.ORE).getDefaultState(), 8, 4, 2, 0, 128);
		}

		if(Options.materialEnabled.get(MaterialNames.COPPER)) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.getMaterialByName(MaterialNames.COPPER).getBlock(Names.ORE).getDefaultState(), 8, 4, 10, 0, 96);
		}

		if(Options.materialEnabled.get(MaterialNames.SILVER)) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.getMaterialByName(MaterialNames.SILVER).getBlock(Names.ORE).getDefaultState(), 8, 4, 4, 0, 32);
		}

		if(Options.materialEnabled.get(MaterialNames.TIN)) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.getMaterialByName(MaterialNames.TIN).getBlock(Names.ORE).getDefaultState(), 8, 4, 10, 0, 128);
		}

		if(Options.materialEnabled.get(MaterialNames.LEAD)) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.getMaterialByName(MaterialNames.LEAD).getBlock(Names.ORE).getDefaultState(), 8, 4, 5, 0, 64);
		}
	
		if(Options.materialEnabled.get(MaterialNames.ZINC)) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.getMaterialByName(MaterialNames.ZINC).getBlock(Names.ORE).getDefaultState(), 8, 4, 5, 0, 96);
		}
		
		if(Options.materialEnabled.get(MaterialNames.MERCURY)) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.getMaterialByName(MaterialNames.MERCURY).getBlock(Names.ORE).getDefaultState(), 8, 4, 3, 0, 32);
		}
		
		if(Options.materialEnabled.get(MaterialNames.NICKEL)) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.getMaterialByName(MaterialNames.NICKEL).getBlock(Names.ORE).getDefaultState(), 8, 4, 1, 32, 96);
		}
		
/*	Currently broken
		if( Options.materials.get(MaterialNames.PLATINUM)) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.getMaterialByName(MaterialNames.PLATINUM).getBlock(Names.ORE).getDefaultState(), 8, 4, 0.125f, 1, 32);
		}
*/
		
		if(Options.materialEnabled.get(MaterialNames.STARSTEEL)) {
			logic.getDimension(1).addOre(Materials.getMaterialByName(MaterialNames.STARSTEEL).getBlock(Names.ORE).getDefaultState(), 8, 4, 5, 0, 255);
		}

		return logic;
	}
}
