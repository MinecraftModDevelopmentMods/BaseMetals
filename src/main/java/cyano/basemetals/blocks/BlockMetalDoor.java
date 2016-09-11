package cyano.basemetals.blocks;

import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.SoundType;
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

import java.util.Random;

/**
 * Door Block
 *
 * @author DrCyano
 *
 */
public class BlockMetalDoor extends net.minecraft.block.BlockDoor implements IMetalObject {

	private final MetalMaterial metal;

	/**
	 *
	 * @param metal
	 */
	public BlockMetalDoor(MetalMaterial metal) {
		super((metal.getToolHarvestLevel() > 0) ? Material.IRON : Material.ROCK);
		this.setSoundType(SoundType.METAL);
		this.metal = metal;
		this.blockHardness = metal.getMetalBlockHardness();
		this.blockResistance = metal.getBlastResistance();
		this.setHarvestLevel("pickaxe", metal.getRequiredHarvestLevel());
		this.disableStats();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ItemStack getItem(final World w, final BlockPos c, final IBlockState bs) {
		return new ItemStack(cyano.basemetals.init.Items.getDoorItemForBlock(this));
	}

	@Override
	public Item getItemDropped(final IBlockState bs, final Random prng, final int i) {
		return (bs.getValue(BlockDoor.HALF) == EnumDoorHalf.UPPER) ? null : cyano.basemetals.init.Items.getDoorItemForBlock(this);
	}

	@Override
	public boolean onBlockActivated(final World world, final BlockPos coord, IBlockState blockstate, 
									final EntityPlayer player,
									final EnumHand hand, ItemStack heldItem,
                                    final EnumFacing face,
    		                        final float partialX, final float partialY, final float partialZ) {
		if (this.metal.getToolHarvestLevel() > 1) {
			return false;
		}
		final BlockPos pos = (blockstate.getValue(BlockDoor.HALF) == EnumDoorHalf.LOWER) ? coord : coord.down();
		final IBlockState bs = coord.equals(pos) ? blockstate : world.getBlockState(pos);
		if (bs.getBlock() != this) {
			return false;
		}
		blockstate = bs.cycleProperty(BlockDoor.OPEN);
		world.setBlockState(pos, blockstate, 2);
		world.markBlockRangeForRenderUpdate(pos, coord);
		world.playEvent(player, ((Boolean)blockstate.getValue(BlockDoor.OPEN)) ? 1003 : 1006, coord, 0);
		return true;
	}

	@Override
	public MetalMaterial getMetalMaterial() {
		return metal;
	}
}
