package com.mcmoddev.orespawn;

import com.google.common.base.Function;
import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.data.Names;

import mmd.orespawn.api.OreSpawnAPI;
import mmd.orespawn.api.SpawnLogic;

public class BaseMetalsOreSpawn implements Function<OreSpawnAPI, SpawnLogic> {

	@Override
	public SpawnLogic apply(OreSpawnAPI api) {
		SpawnLogic logic = api.createSpawnLogic();

		if( Options.enableColdIron ) {
			logic.getDimension(-1).addOre(Materials.coldiron.getBlock(Names.ORE).getDefaultState(), 8, 4, 5, 0, 128);
		}
		
		if( Options.enableAdamantine ) {
			logic.getDimension(-1).addOre(Materials.adamantine.getBlock(Names.ORE).getDefaultState(), 8, 4, 2, 0, 128);
		}
		
		if( Options.enableCopper ) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.copper.getBlock(Names.ORE).getDefaultState(), 8, 4, 10, 0, 96);
		}
		
		if( Options.enableSilver ) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.silver.getBlock(Names.ORE).getDefaultState(), 8, 4, 4, 0, 32);
		}
		
		if( Options.enableTin ) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.tin.getBlock(Names.ORE).getDefaultState(), 8, 4, 10, 0, 128);
		}
		
		if( Options.enableLead ) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.lead.getBlock(Names.ORE).getDefaultState(), 8, 4, 5, 0, 64);
		}
		
		if( Options.enableZinc ) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.zinc.getBlock(Names.ORE).getDefaultState(), 8, 4, 5, 0, 96);
		}
		
		if( Options.enableMercury ) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.mercury.getBlock(Names.ORE).getDefaultState(), 8, 4, 3, 0, 32);
		}
		
		if( Options.enableNickel ) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.nickel.getBlock(Names.ORE).getDefaultState(), 8, 4, 1, 32, 96);
		}
		
/*	Currently broken
		if( Options.enablePlatinum ) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.platinum.getBlock(Names.ORE).getDefaultState(), 8, 4, 0.125f, 1, 32);
		}
*/
		
		if( Options.enableStarSteel ) {
			logic.getDimension(1).addOre(Materials.starsteel.getBlock(Names.ORE).getDefaultState(), 8, 4, 5, 0, 255);
		}

		return logic;
	}
}
