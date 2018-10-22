package com.mcmoddev.lib.block;

import java.util.Random;

import javax.annotation.Nullable;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.material.MMDMaterialType.MaterialType;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Door Block.
 *
 * @author DrCyano
 *
 */
public class BlockMMDDoor extends net.minecraft.block.BlockDoor implements IMMDObject {

	private final MMDMaterial mmdMaterial;

	/**
	 *
	 * @param material
	 *            The material the door is made from
	 */
	public BlockMMDDoor(final MMDMaterial material) {
		super((material.getToolHarvestLevel() > 0) ? material.getVanillaMaterial() : Material.ROCK);
		this.mmdMaterial = material;
		this.setSoundType(this.mmdMaterial.getSoundType());
		this.blockHardness = this.mmdMaterial.getBlockHardness();
		this.blockResistance = this.mmdMaterial.getBlastResistance();
		this.setHarvestLevel(this.mmdMaterial.getHarvestTool(),
				this.mmdMaterial.getRequiredHarvestLevel());
		this.disableStats();
	}

	/**
	 * @deprecated
	 */
	@SideOnly(Side.CLIENT)
	@Override
	@Deprecated
	public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
		return new ItemStack(this.mmdMaterial.getItem(Names.DOOR));
	}

	@Override
	@Nullable
	public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
		return (state.getValue(BlockDoor.HALF) == EnumDoorHalf.UPPER) ? null
				: this.mmdMaterial.getItem(Names.DOOR);
	}

	@Override
	public boolean onBlockActivated(final World worldIn, final BlockPos pos,
			final IBlockState state, final EntityPlayer playerIn, final EnumHand hand,
			final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
		if ((this.mmdMaterial.getToolHarvestLevel() > 1)
				|| (this.mmdMaterial.getType().equals(MaterialType.METAL))) {
			return false;
		} else {
			final BlockPos blockpos = (state.getValue(BlockDoor.HALF) == EnumDoorHalf.LOWER) ? pos
					: pos.down();
			final IBlockState iblockstate = pos.equals(blockpos) ? state
					: worldIn.getBlockState(blockpos);
			if (iblockstate.getBlock() != this) {
				return false;
			} else {
				final IBlockState newState = iblockstate.cycleProperty(BlockDoor.OPEN);
				worldIn.setBlockState(blockpos, newState, 10);
				worldIn.markBlockRangeForRenderUpdate(blockpos, pos);
				this.playSound(playerIn, worldIn, blockpos,
						newState.getValue(BlockDoor.OPEN).booleanValue());
				return true;
			}
		}
	}

	// Magic Number mappings from net.minecraft.client.renderer.RenderGlobal#playEvent
	// SoundEvents.BLOCK_IRON_DOOR_OPEN = 1005
	// SoundEvents.BLOCK_WOODEN_DOOR_OPEN = 1006
	// SoundEvents.BLOCK_IRON_DOOR_CLOSE = 1011
	// SoundEvents.BLOCK_WOODEN_DOOR_CLOSE = 1012
	protected void playSound(@Nullable final EntityPlayer player, final World worldIn,
			final BlockPos pos, final boolean open) {
		int retVal;
		final boolean isMetal = this.getMMDMaterial().getType().equals(MaterialType.METAL);
		if (open) {
			retVal = isMetal ? 1005 : 1006;
		} else {
			retVal = isMetal ? 1011 : 1012;
		}
		worldIn.playEvent(player, retVal, pos, 0);
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.mmdMaterial;
	}
}
