package cyano.basemetals.blocks;

import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.IOreDictionaryEntry;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Metal Trap Door
 */
public class BlockMetalTrapDoor extends net.minecraft.block.BlockTrapDoor implements IOreDictionaryEntry, IMetalObject {

	final MetalMaterial metal;
	public BlockMetalTrapDoor(MetalMaterial metal) {
		super(Material.IRON);
		this.metal = metal;
		this.blockHardness = metal.getMetalBlockHardness();
		this.blockResistance = metal.getBlastResistance();
		this.blockSoundType = SoundType.METAL;
		this.setHarvestLevel("pickaxe", metal.getRequiredHarvestLevel());
		this.disableStats();
	}

	@Override
	public boolean onBlockActivated(final World world, final BlockPos coord, IBlockState state,
									final EntityPlayer player, EnumHand hand, ItemStack heldItem, final EnumFacing facing,
									final float partialX, final float partialY, final float partialZ) {
		if (this.metal.getToolHarvestLevel() > 1) {
			return true;
		}
		state = state.cycleProperty(BlockTrapDoor.OPEN);
		world.setBlockState(coord, state, 2);
		world.playEvent(player, ((Boolean)state.getValue(BlockTrapDoor.OPEN)) ? 1003 : 1006, coord, 0);
		return true;
	}

	@Override
	public String getOreDictionaryName() {
		return "trapdoor"+metal.getCapitalizedName();
	}

	@Override
	public MetalMaterial getMetalMaterial() {
		return metal;
	}
}
