package com.mcmoddev.lib.block;

import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.lib.material.IMetalObject;
import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.registry.IOreDictionaryEntry;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.block.BlockOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.Random;

/**
 * Ore Block
 */
public class BlockMetalOre extends BlockOre implements IOreDictionaryEntry, IMetalObject {

	private final MetalMaterial material;
	private final String oreDict;

	/**
	 *
	 * @param material
	 *            The material the ore is made from
	 */
	public BlockMetalOre(MetalMaterial material) {
		super();
		// this.setSoundType(SoundType.STONE);
		this.material = material;
		this.blockHardness = Math.max(5f, material.getOreBlockHardness());
		this.blockResistance = Math.max(1.5f, material.getBlastResistance() * 0.75f);
		this.setHarvestLevel("pickaxe", material.getRequiredHarvestLevel());
		this.oreDict = Oredicts.ORE + material.getCapitalizedName();
		// BaseMetals.logger.info(material.getName() + " ore harvest level set to " + material.getRequiredHarvestLevel());
	}

	@Override
	public int getExpDrop(final IBlockState bs, IBlockAccess w, final BlockPos coord, final int i) {
		return 0; // XP comes from smelting
	}

	@Override
	public boolean canEntityDestroy(IBlockState bs, IBlockAccess w, BlockPos coord, Entity entity) {
		if ((this == Materials.starsteel.ore) && (entity instanceof net.minecraft.entity.boss.EntityDragon))
			return false;
		return super.canEntityDestroy(bs, w, coord, entity);
	}

	public MetalMaterial getMetal() {
		return this.material;
	}

	@Override
	public String getOreDictionaryName() {
		return this.oreDict;
	}

	@Override
	public MetalMaterial getMaterial() {
		return this.material;
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public MetalMaterial getMetalMaterial() {
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
		if (this.material.getType() == MetalMaterial.MaterialType.MINERAL) {
			return this.material.powder;
		} else if (this.material.getType() == MetalMaterial.MaterialType.GEM) {
			return this.material.ingot;
		}
		return Item.getItemFromBlock(this);
	}
}
