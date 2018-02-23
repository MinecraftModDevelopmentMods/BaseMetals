package com.mcmoddev.lib.item;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class ItemMMDSlab extends net.minecraft.item.ItemSlab implements IMMDObject {

	final MMDMaterial material;

	private final BlockSlab singleSlab;
	private final BlockSlab doubleSlab;

	/**
	 *
	 * @param material
	 *            The material to make the slab from
	 */
	public ItemMMDSlab(final MMDMaterial material) {
		super((BlockSlab) material.getBlock(Names.SLAB), (BlockSlab) material.getBlock(Names.SLAB), (BlockSlab) material.getBlock(Names.DOUBLE_SLAB));
		this.material = material;
		this.singleSlab = (BlockSlab) material.getBlock(Names.SLAB);
		this.doubleSlab = (BlockSlab) material.getBlock(Names.DOUBLE_SLAB);
		this.setMaxDamage(0);
		this.setHasSubtypes(false);
	}

	/**
	 *
	 * @param material
	 *            The material to make the slab from
	 * @param block
	 *            The block to use to make the slab
	 * @param slab
	 *            The half slab block to use to make the slab
	 * @param doubleslab
	 *            The double slab block to use to make the slab
	 */
	public ItemMMDSlab(final MMDMaterial material, final Block block, final BlockSlab slab, final BlockSlab doubleslab) {
		super(block, slab, doubleslab);
		this.material = material;
		this.singleSlab = (BlockSlab) material.getBlock(Names.SLAB);
		this.doubleSlab = (BlockSlab) material.getBlock(Names.DOUBLE_SLAB);
		this.setMaxDamage(0);
		this.setHasSubtypes(false);
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}

	/**
	 * Called when a Block is right-clicked with this Item
	 */
	@Override
	public EnumActionResult onItemUse(final EntityPlayer player, final World worldIn, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
		final ItemStack itemstack = player.getHeldItem(hand);

		if (!itemstack.isEmpty() && player.canPlayerEdit(pos.offset(facing), facing, itemstack)) {
			final IBlockState iblockstate = worldIn.getBlockState(pos);

			if (iblockstate.getBlock() == this.singleSlab) {
				final BlockSlab.EnumBlockHalf half = iblockstate.getValue(BlockSlab.HALF);

				if ((facing == EnumFacing.UP && half == BlockSlab.EnumBlockHalf.BOTTOM || facing == EnumFacing.DOWN && half == BlockSlab.EnumBlockHalf.TOP)) {
					final IBlockState iblockstate1 = this.doubleSlab.getDefaultState();
					final AxisAlignedBB axisalignedbb = iblockstate1.getCollisionBoundingBox(worldIn, pos);

					if (axisalignedbb != Block.NULL_AABB && worldIn.checkNoEntityCollision(axisalignedbb.offset(pos)) && worldIn.setBlockState(pos, iblockstate1, 11)) {
						final SoundType soundtype = this.doubleSlab.getSoundType(iblockstate1, worldIn, pos, player);
						worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
						itemstack.shrink(1);
					}

					return EnumActionResult.SUCCESS;
				}
			}

			return this.tryPlace(player, itemstack, worldIn, pos.offset(facing)) ? EnumActionResult.SUCCESS : super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		} else {
			return EnumActionResult.FAIL;
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean canPlaceBlockOnSide(final World worldIn, BlockPos pos, final EnumFacing side, final EntityPlayer player, final ItemStack stack) {
		BlockPos blockpos = pos;
		final IBlockState iblockstate = worldIn.getBlockState(pos);

		if (iblockstate.getBlock() == this.singleSlab) {
			final boolean flag = iblockstate.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP;

			if ((side == EnumFacing.UP && !flag || side == EnumFacing.DOWN && flag)) {
				return true;
			}
		}

		pos = pos.offset(side);
		final IBlockState iblockstate1 = worldIn.getBlockState(pos);
		return iblockstate1.getBlock() == this.singleSlab ? true : super.canPlaceBlockOnSide(worldIn, blockpos, side, player, stack);
	}

	private boolean tryPlace(final EntityPlayer player, final ItemStack stack, final World worldIn, final BlockPos pos) {
		final IBlockState iblockstate = worldIn.getBlockState(pos);

		if (iblockstate.getBlock() == this.singleSlab) {
				final IBlockState iblockstate1 = this.doubleSlab.getDefaultState();
				final AxisAlignedBB axisalignedbb = iblockstate1.getCollisionBoundingBox(worldIn, pos);

				if (axisalignedbb != Block.NULL_AABB && worldIn.checkNoEntityCollision(axisalignedbb.offset(pos)) && worldIn.setBlockState(pos, iblockstate1, 11)) {
					final SoundType soundtype = this.doubleSlab.getSoundType(iblockstate1, worldIn, pos, player);
					worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
					stack.shrink(1);
				}

				return true;
		}

		return false;
	}
}
