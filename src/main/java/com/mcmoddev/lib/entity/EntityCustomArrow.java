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
	public boolean equals(Object other) {
		if (!(other instanceof EntityCustomArrow)) return false;
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
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		return EntityHelpers.writeToNBTItemStack(compound, this.itemStack);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		this.itemStack = EntityHelpers.readFromNBTItemStack(compound);
	}
}
