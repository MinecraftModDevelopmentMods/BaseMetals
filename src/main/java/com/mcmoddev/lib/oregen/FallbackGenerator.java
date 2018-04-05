package com.mcmoddev.lib.oregen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

public class FallbackGenerator implements IWorldGenerator {

	@Override
	public void generate(final Random random, final int chunkX, final int chunkZ, final World world,
			final IChunkGenerator chunkGenerator, final IChunkProvider chunkProvider) {

		final int dimension = world.provider.getDimension();
		final List<WorldGenMinable> ores = new ArrayList<>(
				FallbackGeneratorData.getInstance().getSpawnsForDimension(dimension));
		if ((dimension != -1) && (dimension != 1)) {
			ores.addAll(
					FallbackGeneratorData.getInstance().getSpawnsForDimension(Integer.MIN_VALUE));
		}

		final int minY = this.getMinYForDimension(dimension);
		final int maxY = this.getMaxYForDimension(dimension);

		for (final WorldGenMinable ore : ores) {
			for (int i = 0; i < 8; i++) {
				final int pos_x = (chunkX * 16) + random.nextInt(16);
				final int pos_z = (chunkZ * 16) + random.nextInt(16);
				final int pos_y = minY + random.nextInt((maxY - minY));
				final BlockPos p = new BlockPos(pos_x, pos_y, pos_z);
				ore.generate(world, random, p);
			}
		}
	}

	private int getMaxYForDimension(final int dimension) {
		return dimension == -1 ? 126 : dimension == 1 ? 256 : 80;
	}

	private int getMinYForDimension(final int dimension) {
		return (dimension == -1) || (dimension == 1) ? 0 : 16;
	}
}
