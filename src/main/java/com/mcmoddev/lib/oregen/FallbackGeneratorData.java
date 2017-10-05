package com.mcmoddev.lib.oregen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class FallbackGeneratorData {
	private final static Map<Integer, Map<MMDMaterial, List<String>>> materials = new TreeMap<>();
	private final static FallbackGeneratorData INSTANCE = new FallbackGeneratorData();
	private final static Map<Integer, List<WorldGenMinable>> spawns = new HashMap<>();
	
	private FallbackGeneratorData() {
		// blank
	}
	
	public void addMaterial(@Nonnull final String materialName, @Nonnull final String blockName, @Nullable final Integer dimension) {
		MMDMaterial mat = Materials.getMaterialByName(materialName);
		int targetDim = dimension!=null?dimension:Integer.MIN_VALUE;
		Map<MMDMaterial, List<String>> blockMap = materials.getOrDefault(targetDim, new HashMap<MMDMaterial,List<String>>());
		List<String> blocks = blockMap.getOrDefault(mat, new ArrayList<>());
		
		if( !blocks.contains(blockName) ) {
			blocks.add(blockName);
		}
		
		blockMap.put(mat, blocks);
		materials.put(targetDim, blockMap);
	}
	
	static class StonePredicate implements Predicate<IBlockState> {
		private StonePredicate() {
			
		}
		public boolean apply(IBlockState comp) {
			if( comp != null && comp.getBlock() == Blocks.STONE) {
				BlockStone.EnumType block = comp.getValue(BlockStone.VARIANT);
				return block.isNatural();
			}
			return false;
		}
	}
	
	static class NetherPredicate implements Predicate<IBlockState> {
		private NetherPredicate() {
			
		}
		public boolean apply(IBlockState comp) {
			if( comp != null ) {
				Block b = comp.getBlock();
				return b.equals(Blocks.NETHERRACK);
			}
			return false;
		}
	}
	
	static class EndPredicate implements Predicate<IBlockState> {
		private EndPredicate() {
			
		}
		public boolean apply(IBlockState comp) {
			if( comp != null ) {
				Block b = comp.getBlock();
				return b.equals(Blocks.END_STONE);
			}
			return false;
		}
	}

	public void setup() {
		for( Entry<Integer, Map<MMDMaterial, List<String>>> matMap : materials.entrySet() ) {			
			Map<MMDMaterial, List<String>> mats = matMap.getValue();
			List<WorldGenMinable> spawnList = spawns.getOrDefault(matMap.getKey(), new ArrayList<>());
			final Predicate<IBlockState> pred = getPredicateForDimension(matMap.getKey());
			
			for( Entry<MMDMaterial, List<String>> matList : mats.entrySet() ) {
				MMDMaterial mat = matList.getKey();
				matList.getValue().stream().forEach(blockName -> spawnList.add(new WorldGenMinable(mat.getBlock(blockName).getDefaultState(),mat.getSpawnSize()>0?mat.getSpawnSize():8,pred)) );
			}
			spawns.put(matMap.getKey(), spawnList);
		}
	}
	
	private Predicate<IBlockState> getPredicateForDimension(Integer key) {
		switch(key) {
		case -1:
			return new NetherPredicate();
		case 1:
			return new EndPredicate();
		case 0:
		case Integer.MIN_VALUE:
		default:
			return new StonePredicate();
		}
	}

	public List<WorldGenMinable> getSpawnsForDimension(int dimension) {
		return ImmutableList.copyOf(spawns.getOrDefault(dimension, Collections.emptyList()));
	}
	
	public static FallbackGeneratorData getInstance() {
		return INSTANCE;
	}
}
