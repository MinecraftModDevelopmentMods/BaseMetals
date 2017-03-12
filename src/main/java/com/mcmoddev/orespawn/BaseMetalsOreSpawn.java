package com.mcmoddev.orespawn;

import com.google.common.base.Function;
import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.basemetals.util.Config.Options;

import mmd.orespawn.api.OreSpawnAPI;
import mmd.orespawn.api.SpawnLogic;

public class BaseMetalsOreSpawn implements Function<OreSpawnAPI, SpawnLogic> {

	@Override
	public SpawnLogic apply(OreSpawnAPI api) {
		SpawnLogic logic = api.createSpawnLogic();

		if( Options.enableColdIron ) {
			logic.getDimension(-1).addOre(Materials.coldiron.ore.getDefaultState(), 8, 4, 5, 0, 128);
		}
		
		if( Options.enableAdamantine ) {
			logic.getDimension(-1).addOre(Materials.adamantine.ore.getDefaultState(), 8, 4, 2, 0, 128);
		}
		
		if( Options.enableCopper ) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.copper.ore.getDefaultState(), 8, 4, 10, 0, 96);
		}
		
		if( Options.enableSilver ) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.silver.ore.getDefaultState(), 8, 4, 4, 0, 32);
		}
		
		if( Options.enableTin ) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.tin.ore.getDefaultState(), 8, 4, 10, 0, 128);
		}
		
		if( Options.enableLead ) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.lead.ore.getDefaultState(), 8, 4, 5, 0, 64);
		}
		
		if( Options.enableZinc ) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.zinc.ore.getDefaultState(), 8, 4, 5, 0, 96);
		}
		
		if( Options.enableMercury ) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.mercury.ore.getDefaultState(), 8, 4, 3, 0, 32);
		}
		
		if( Options.enableNickel ) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.nickel.ore.getDefaultState(), 8, 4, 1, 32, 96);
		}
		
/*	Currently broken
 * 		if( Options.enablePlatinum ) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.platinum.ore.getDefaultState(), 8, 4, 0.125f, 1, 32);
		}
*/
		
		if( Options.enableStarSteel ) {
			logic.getDimension(1).addOre(Materials.starsteel.ore.getDefaultState(), 8, 4, 5, 0, 255);
		}

		return logic;
	}
}
