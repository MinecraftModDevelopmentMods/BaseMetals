package cyano.basemetals.blocks;

import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

/**
 * Door Block
 */
public class BlockMetalDoor extends net.minecraft.block.BlockDoor implements IMetalObject {

	final MetalMaterial metal;
	public BlockMetalDoor(MetalMaterial metal) {
		super((metal.getToolHarvestLevel() > 0) ? Material.iron : Material.rock);
		this.stepSound = Block.soundTypeMetal;
		this.metal = metal;
		this.blockHardness = metal.getMetalBlockHardness();
		this.blockResistance = metal.getBlastResistance();
		this.setHarvestLevel("pickaxe", metal.getRequiredHarvestLevel());
		this.disableStats();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Item getItem(final World w, final BlockPos c) {
		return cyano.basemetals.init.Items.getDoorItemForBlock(this);
	}

	@Override
	public Item getItemDropped(final IBlockState bs, final Random prng, final int i) {
		return (bs.getValue(BlockDoor.HALF) == EnumDoorHalf.UPPER) ? null : cyano.basemetals.init.Items.getDoorItemForBlock(this);
	}

	@Override
	public boolean onBlockActivated(final World world, final BlockPos coord, IBlockState blockstate, 
			final EntityPlayer player, final EnumFacing face, 
			final float partialX, final float partialY, final float partialZ) {
		if (this.metal.getToolHarvestLevel() > 1) {
			return false;
		}
		final BlockPos blockpos1 = (blockstate.getValue(BlockDoor.HALF) == EnumDoorHalf.LOWER) ? coord : coord.down();
		final IBlockState iblockstate1 = coord.equals(blockpos1) ? blockstate : world.getBlockState(blockpos1);
		if (iblockstate1.getBlock() != this) {
			return false;
		}
		blockstate = iblockstate1.cycleProperty(BlockDoor.OPEN);
		world.setBlockState(blockpos1, blockstate, 2);
		world.markBlockRangeForRenderUpdate(blockpos1, coord);
		world.playAuxSFXAtEntity(player, ((Boolean)blockstate.getValue(BlockDoor.OPEN)) ? 1003 : 1006, coord, 0);
		return true;
	}

	@Override
	public MetalMaterial getMetalMaterial() {
		return metal;
	}
}
