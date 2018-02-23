package com.mcmoddev.lib.block;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

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
	public BlockMMDBlock(final MMDMaterial material) {
		this(material, false);
	}

	public BlockMMDBlock(final MMDMaterial material, final boolean glows) {
		this(material, glows, false);
	}

	public BlockMMDBlock(final MMDMaterial material, final boolean glows, final boolean isBeacon) {
		super(material.getVanillaMaterial());
		this.material = material;

		this.setSoundType(this.material.getSoundType());
		this.fullBlock = true;
		this.lightOpacity = 255;
		this.translucent = false;
		this.blockHardness = this.material.getBlockHardness();
		this.blockResistance = this.material.getBlastResistance();
		this.setHarvestLevel(this.material.getHarvestTool(), this.material.getRequiredHarvestLevel());
		this.beaconBase = isBeacon;
		// Additional
		this.fullBlock = true;

		if (glows)
			this.setLightLevel(0.5f);
	}

	@Override
	public boolean isBeaconBase(final IBlockAccess worldObj, final BlockPos pos, final BlockPos beacon) {
		return beaconBase;
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
