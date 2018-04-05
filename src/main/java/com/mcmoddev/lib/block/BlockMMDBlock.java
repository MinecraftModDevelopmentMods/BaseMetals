package com.mcmoddev.lib.block;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Block.
 */
public class BlockMMDBlock extends net.minecraft.block.Block implements IMMDObject {

	private final MMDMaterial material;

	/**
	 *
	 * @param material
	 *            The material the block is made from
	 */
	public BlockMMDBlock(final MMDMaterial material) {
		super(material.getVanillaMaterial());
		this.material = material;

		this.setSoundType(this.material.getSoundType());
		this.fullBlock = true;
		this.setLightOpacity(255);
		this.translucent = false;
		this.blockHardness = this.material.getBlockHardness();
		this.blockResistance = this.material.getBlastResistance();
		this.setHarvestLevel(this.material.getHarvestTool(),
				this.material.getRequiredHarvestLevel());
		// Additional
		this.fullBlock = true;
	}

	@Override
	public boolean isBeaconBase(final IBlockAccess worldObj, final BlockPos pos,
			final BlockPos beacon) {
		return this.material.isBeaconBase();
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
