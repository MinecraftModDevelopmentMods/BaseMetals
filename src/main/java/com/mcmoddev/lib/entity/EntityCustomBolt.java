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
	public EntityCustomBolt(final World worldIn) {
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
	public EntityCustomBolt(final World worldIn, final double x, final double y, final double z) {
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
	public EntityCustomBolt(final World worldIn, final ItemStack stack, final EntityPlayer shooter) {
		super(worldIn, shooter);
		this.itemStack = stack;
	}

	@Override
	protected ItemStack getArrowStack() {
		return this.getBoltStack();
	}

	protected ItemStack getBoltStack() {
		if (this.itemStack.isEmpty()) {
			// FIXME - this is potentially unreliable
			this.itemStack = new ItemStack(Materials.getMaterialByName(MaterialNames.WOOD).getItem(Names.BOLT));
		}

		return new ItemStack(this.itemStack.getItem(), 1, this.itemStack.getItemDamage());
	}


	@Override
	public int hashCode() {
		return super.hashCode() ^ this.getArrowStack().hashCode();
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof EntityCustomBolt)) {
			return false;
		}
		return super.equals(other);
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
