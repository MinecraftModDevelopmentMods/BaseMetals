package com.mcmoddev.lib.block;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Metal Block
 */
public class BlockMMDBlock extends net.minecraft.block.Block implements IMMDObject {

	private final MMDMaterial material;
	private final boolean beaconBase;

	/**
	 *
	 * @param material
	 *            The material the block is made from
	 */
	public BlockMMDBlock(MMDMaterial material) {
		this(material, false);
	}

	public BlockMMDBlock(MMDMaterial material, boolean glows) {
		this(material, glows, false);
	}

	public BlockMMDBlock(MMDMaterial material, boolean glows, boolean isBeacon) {
		super(material.getVanillaMaterial());
		this.material = material;

		this.setSoundType(this.material.getSoundType());
		this.fullBlock = true;
		this.lightOpacity = 255;
		this.translucent = false;
		this.blockHardness = this.material.getBlockHardness();
		this.blockResistance = this.material.getBlastResistance();
		this.setHarvestLevel("pickaxe", this.material.getRequiredHarvestLevel());
		this.beaconBase = isBeacon;

		if (glows)
			this.setLightLevel(0.5f);
	}

	@Override
	public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) {
		return beaconBase;
	}

	///// OVERRIDE OF ALL METHODS THAT DEPEND ON BLOCK MATERIAL: /////
	/**
	 * @deprecated
	 */
	@Deprecated
	@Override
	public MapColor getMapColor(IBlockState state, IBlockAccess blockAccess, BlockPos blockPos) {
		return MapColor.IRON;
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public boolean isFullBlock(IBlockState bs) {
		return true;
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public boolean isNormalCube(IBlockState bs) {
		return true;
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public boolean isFullCube(IBlockState bs) {
		return true;
	}

	@Override
	public boolean isPassable(final IBlockAccess worldIn, final BlockPos pos) {
		return false;
	}

	@Override
	public boolean isReplaceable(final IBlockAccess w, final BlockPos p) {
		return false;
	}

	@Override
	public boolean isNormalCube(final IBlockState bs, final IBlockAccess w, final BlockPos coord) {
		return true;
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
