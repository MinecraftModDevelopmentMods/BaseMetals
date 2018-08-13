package com.mcmoddev.lib.block;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Block.
 */
public class BlockMMDBlock extends net.minecraft.block.Block implements IMMDObject {

	private final MMDMaterial mmdMaterial;

	/**
	 *
	 * @param material
	 *            The material the block is made from
	 */
	public BlockMMDBlock(final MMDMaterial material) {
		super(material.getVanillaMaterial());
		this.mmdMaterial = material;

		this.setSoundType(this.mmdMaterial.getSoundType());
		this.fullBlock = true;
		this.setLightOpacity(255);
		this.translucent = false;
		this.blockHardness = this.mmdMaterial.getBlockHardness();
		this.blockResistance = this.mmdMaterial.getBlastResistance();
		this.setHarvestLevel(this.mmdMaterial.getHarvestTool(),
				this.mmdMaterial.getRequiredHarvestLevel());
		// Additional
		this.fullBlock = true;
	}

	@Override
	public boolean isBeaconBase(final IBlockAccess worldObj, final BlockPos pos,
			final BlockPos beacon) {
		return this.mmdMaterial.isBeaconBase();
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.mmdMaterial;
	}
}
