package com.mcmoddev.lib.block;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.BlockTrapDoor;
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
public class BlockMMDTrapDoor extends net.minecraft.block.BlockTrapDoor implements IMMDObject {

	private final MMDMaterial material;

	/**
	 *
	 * @param material
	 *            The material the trapdoor is made from
	 */
	public BlockMMDTrapDoor(MMDMaterial material) {
		super(material.getVanillaMaterial());
		this.material = material;
		this.blockHardness = this.material.getBlockHardness();
		this.blockResistance = this.material.getBlastResistance();
		this.blockSoundType = this.material.getSoundType();
		this.setHarvestLevel("pickaxe", this.material.getRequiredHarvestLevel());
		this.disableStats();
	}

	@Override
	public boolean onBlockActivated(final World world, final BlockPos coord, IBlockState state,
									final EntityPlayer player, EnumHand hand, ItemStack heldItem, final EnumFacing facing,
									final float partialX, final float partialY, final float partialZ) {
		if (this.material.getToolHarvestLevel() > 1)
			return true;
		state = state.cycleProperty(BlockTrapDoor.OPEN);
		world.setBlockState(coord, state, 2);
		world.playEvent(player, ((Boolean) state.getValue(BlockTrapDoor.OPEN)) ? 1003 : 1006, coord, 0);
		return true;
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
