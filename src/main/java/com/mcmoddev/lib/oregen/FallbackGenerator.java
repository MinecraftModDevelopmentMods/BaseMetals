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
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {

		int dimension = world.provider.getDimension();
		List<WorldGenMinable> ores = new ArrayList<>(FallbackGeneratorData.getInstance().getSpawnsForDimension(dimension));
		if( dimension != -1 && dimension != 1 ) ores.addAll(FallbackGeneratorData.getInstance().getSpawnsForDimension(Integer.MIN_VALUE));
		
		int minY = dimension==-1||dimension==1?0:16;
		int maxY = dimension==-1?126:dimension==1?256:80;
		
		for( WorldGenMinable ore : ores ) {
			for( int i = 0; i < 8; i++ ) {
				int pos_x = chunkX * 16 + random.nextInt(16);
				int pos_z = chunkZ * 16 + random.nextInt(16);
				int pos_y = minY + random.nextInt((maxY - minY));
				BlockPos p = new BlockPos(pos_x, pos_y, pos_z);
				ore.generate(world, random, p);
			}
		}
	}

}
