package com.mcmoddev.lib.entity;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.lib.data.Names;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class EntityCustomBolt extends EntityTippedArrow {

	private ItemStack itemStack;

	/**
	 *
	 * @param worldIn
	 *            The World
	 */
	public EntityCustomBolt(World worldIn) {
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
	public EntityCustomBolt(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	/**
	 *
	 * @param worldIn
	 *            The World
	 * @param stack
	 *            The ItemStack
	 * @param shooter
	 *            The Shooter
	 */
	public EntityCustomBolt(World worldIn, ItemStack stack, EntityPlayer shooter) {
		super(worldIn, shooter);
		this.itemStack = stack;
	}

	@Override
	protected ItemStack getArrowStack() {
		return this.getBoltStack();
	}

	protected ItemStack getBoltStack() {
		if (this.itemStack == null) {
			// FIXME - this is potentially unreliable
			this.itemStack = new ItemStack(Materials.getMaterialByName(MaterialNames.WOOD).getItem(Names.BOLT));
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
