package com.mcmoddev.lib.block;

import java.util.Random;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Ore Block
 */
public class BlockMMDOre extends net.minecraft.block.BlockOre implements IMMDObject {

	private final MMDMaterial material;

	/**
	 *
	 * @param material
	 *            The material the ore is made from
	 */
	public BlockMMDOre(MMDMaterial material) {
		this(material, false);
	}

	public BlockMMDOre(MMDMaterial material, boolean isSoft) {
		super();
		this.material = material;
		float hardnessMax = 5f;
		float resistMax = 1.5f;
		String tool = "pickaxe";
		
		if (isSoft) {
			this.setSoundType(SoundType.GROUND);
			hardnessMax = 2.5f;
			resistMax = 1.5f;
			tool = "shovel";
		} else {
			this.setSoundType(SoundType.STONE);
		}
		
		this.blockHardness = Math.max(hardnessMax, this.material.getOreBlockHardness());
		this.blockResistance = Math.max(resistMax, this.material.getBlastResistance() * 0.75f);
		this.setHarvestLevel(tool, this.material.getRequiredHarvestLevel());
	}
    
	@Override
	public int getExpDrop(final IBlockState bs, IBlockAccess w, final BlockPos coord, final int i) {
		return 0; // XP comes from smelting
	}

	@Override
	public boolean canEntityDestroy(IBlockState bs, IBlockAccess w, BlockPos coord, Entity entity) {
		if ((this == Materials.getMaterialByName(MaterialNames.STARSTEEL).getBlock(Names.ORE)) && (entity instanceof net.minecraft.entity.boss.EntityDragon))
			return false;
		return super.canEntityDestroy(bs, w, coord, entity);
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random) {
		int most = 1;
		int least = 1;
		int total;
		switch (this.material.getType()) {
			case WOOD:
			case ROCK:
			case METAL:
				// stupid hack - but at least this doesn't get into a race where
				// users have figured out how to subvert putting a "this was placed by the user"
				// chunk of data on the ores situation to get around the fortune drops
				return 1;
			case MINERAL:
				most = 4;
				least = 2;
				break;
			case CRYSTAL:
				most = 4;
				least = 1;
				break;
			case GEM:
				most = 3;
				least = 2;
				break;
		}
		total = ((most - least) + fortune) + 1;
		return least + random.nextInt(total);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random random, int fortune) {
		if (this.material.getType() == MMDMaterial.MaterialType.MINERAL) {
			return this.material.getItem(Names.POWDER);
		} else if (this.material.getType() == MMDMaterial.MaterialType.CRYSTAL) {
			return this.material.getItem(Names.CRYSTAL);
		} else if (this.material.getType() == MMDMaterial.MaterialType.GEM) {
			return this.material.getItem(Names.GEM);
		}
		return Item.getItemFromBlock(this);
	}
}
