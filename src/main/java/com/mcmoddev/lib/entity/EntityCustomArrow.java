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
	public EntityCustomArrow(World worldIn) {
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
	public EntityCustomArrow(World worldIn, double x, double y, double z) {
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
	public EntityCustomArrow(World worldIn, ItemStack stack, EntityPlayer shooter) {
		super(worldIn, shooter);
		this.itemStack = stack;
	}

	@Override
	protected ItemStack getArrowStack() {
		if (this.itemStack == null) {
			this.itemStack = new ItemStack(Items.ARROW);
		}

		return new ItemStack(this.itemStack.getItem(), 1, this.itemStack.getItemDamage());
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		final NBTTagCompound itemStackCompound = new NBTTagCompound();
		this.itemStack.writeToNBT(itemStackCompound);

		compound.setTag("itemstack", itemStackCompound);

		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		this.itemStack = new ItemStack(compound.getCompoundTag("itemstack"));
	}
}
