package com.mcmoddev.lib.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class EntityCustomArrow extends EntityTippedArrow {

	private ItemStack itemStack;

	/**
	 *
	 * @param worldIn
	 *            The World
	 */
	public EntityCustomArrow(final World worldIn) {
		super(worldIn);
	}

	/**
	 *
	 * @param worldIn
	 *            The World
	 * @param x
	 *            X
	 * @param y
	 *            Y
	 * @param z
	 *            Z
	 */
	public EntityCustomArrow(final World worldIn, final double x, final double y, final double z) {
		super(worldIn, x, y, z);
	}

	/**
	 *
	 * @param worldIn
	 *            The World
	 * @param stack
	 *            The itemstack
	 * @param shooter
	 *            The Shooter
	 */
	public EntityCustomArrow(final World worldIn, final ItemStack stack, final EntityPlayer shooter) {
		super(worldIn, shooter);
		this.itemStack = stack;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ this.getArrowStack().hashCode();
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof EntityCustomArrow)) {
			return false;
		}
		return super.equals(other);
	}

	@Override
	protected ItemStack getArrowStack() {
		if (this.itemStack.isEmpty()) {
			this.itemStack = new ItemStack(Items.ARROW);
		}

		return new ItemStack(this.itemStack.getItem(), 1, this.itemStack.getItemDamage());
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
		super.writeToNBT(compound);
		return EntityHelpers.writeToNBTItemStack(compound, this.itemStack);
	}

	@Override
	public void readFromNBT(final NBTTagCompound compound) {
		super.readFromNBT(compound);

		this.itemStack = EntityHelpers.readFromNBTItemStack(compound);
	}
}
