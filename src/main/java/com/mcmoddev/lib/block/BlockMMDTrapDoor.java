package com.mcmoddev.lib.block;

import javax.annotation.Nullable;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.material.MMDMaterialType.MaterialType;

import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Trap Door.
 */
public class BlockMMDTrapDoor extends net.minecraft.block.BlockTrapDoor implements IMMDObject {

	private final MMDMaterial mmdMaterial;

	/**
	 *
	 * @param material
	 *            The material the trapdoor is made from
	 */
	public BlockMMDTrapDoor(final MMDMaterial material) {
		super(material.getVanillaMaterial());
		this.mmdMaterial = material;
		this.blockHardness = this.mmdMaterial.getBlockHardness();
		this.blockResistance = this.mmdMaterial.getBlastResistance();
		this.blockSoundType = this.mmdMaterial.getSoundType();
		this.setHarvestLevel(this.mmdMaterial.getHarvestTool(),
				this.mmdMaterial.getRequiredHarvestLevel());
		this.disableStats();
	}

	@Override
	public boolean onBlockActivated(final World worldIn, final BlockPos pos,
			final IBlockState state, final EntityPlayer playerIn, final EnumHand hand,
			final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
		if ((this.mmdMaterial.getToolHarvestLevel() > 1)
				|| (this.mmdMaterial.getType().equals(MaterialType.METAL))) {
			return false;
		} else {
			final IBlockState newState = state.cycleProperty(BlockTrapDoor.OPEN);
			worldIn.setBlockState(pos, newState, 2);
			this.playSound(playerIn, worldIn, pos, newState.getValue(OPEN).booleanValue());
			return true;
		}
	}

	// Magic Number mappings from net.minecraft.client.renderer.RenderGlobal#playEvent
	// SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN = 1037
	// SoundEvents.BLOCK_WOODEN_TRAPDOOR_OPEN = 1007
	// SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE = 1036
	// SoundEvents.BLOCK_WOODEN_TRAPDOOR_CLOSE = 1013
	@Override
	protected void playSound(@Nullable final EntityPlayer player, final World worldIn,
			final BlockPos pos, final boolean open) {
		int retVal;
		final boolean isMetal = this.getMMDMaterial().getType().equals(MaterialType.METAL);
		if (open) {
			retVal = isMetal ? 1037 : 1007;
		} else {
			retVal = isMetal ? 1036 : 1013;
		}
		worldIn.playEvent(player, retVal, pos, 0);
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.mmdMaterial;
	}
}
