package com.mcmoddev.lib.item;

import com.mcmoddev.lib.block.BlockMMDAnvil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MMDContainerRepair extends ContainerRepair {

	private final BlockPos myPos;
	private final World myWorld;

	/**
	 *
	 * @param playerInventory
	 * @param worldIn
	 * @param player
	 */
	public MMDContainerRepair(final InventoryPlayer playerInventory, final World worldIn,
			final EntityPlayer player) {
		super(playerInventory, worldIn, player);
		this.myPos = BlockPos.ORIGIN;
		this.myWorld = worldIn;
	}

	/**
	 *
	 * @param playerInventory
	 * @param worldIn
	 * @param blockPosIn
	 * @param player
	 */
	public MMDContainerRepair(final InventoryPlayer playerInventory, final World worldIn,
			final BlockPos blockPosIn, final EntityPlayer player) {
		super(playerInventory, worldIn, blockPosIn, player);
		this.myPos = blockPosIn;
		this.myWorld = worldIn;
	}

	@Override
	public boolean canInteractWith(final EntityPlayer playerIn) {
		if (!(this.myWorld.getBlockState(this.myPos).getBlock() instanceof BlockMMDAnvil)) {
			return false;
		} else {
			return playerIn.getDistanceSq(this.myPos.getX() + 0.5D, this.myPos.getY() + 0.5D,
					this.myPos.getZ() + 0.5D) <= 64.0D;
		}
	}
}
