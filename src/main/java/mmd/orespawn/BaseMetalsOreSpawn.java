package mmd.orespawn;

import com.google.common.base.Function;

import cyano.basemetals.init.Blocks;
import mmd.orespawn.api.OreSpawnAPI;
import mmd.orespawn.api.SpawnLogic;

public class BaseMetalsOreSpawn implements Function<OreSpawnAPI, SpawnLogic> {

	@Override
	public SpawnLogic apply(OreSpawnAPI api) {
		SpawnLogic logic = api.createSpawnLogic();


		logic.getDimension(-1)
				.addOre(Blocks.coldiron_ore.getDefaultState(), 8, 4, 5, 0, 128)
				.addOre(Blocks.adamantine_ore.getDefaultState(), 8, 4, 2, 0, 128);

		logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD)
				.addOre(Blocks.copper_ore.getDefaultState(), 8, 4, 10, 0,  96)
				.addOre(Blocks.silver_ore.getDefaultState(), 8, 4,  4, 0,  32)
				.addOre(Blocks.tin_ore.getDefaultState(), 8, 4, 10, 0, 128)
				.addOre(Blocks.lead_ore.getDefaultState(), 8, 4, 5, 0, 64)
				.addOre(Blocks.zinc_ore.getDefaultState(), 8, 4, 5, 0, 96)
				.addOre(Blocks.mercury_ore.getDefaultState(), 8, 4, 3, 0, 32)
				.addOre(Blocks.nickel_ore.getDefaultState(), 8, 4, 1, 32, 96);
//				.addOre(Blocks.platinum_ore.getDefaultState(), 8, 4, 0.125f, 1, 32); // Currently broken

		logic.getDimension(1)
				.addOre(Blocks.starsteel_ore.getDefaultState(), 8, 4, 5, 0, 255);

		return logic;
	}
}
