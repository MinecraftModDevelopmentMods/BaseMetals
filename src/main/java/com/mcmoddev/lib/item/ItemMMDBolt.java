package com.mcmoddev.lib.item;

import com.mcmoddev.lib.entity.EntityCustomBolt;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class ItemMMDBolt extends ItemBolt implements IMMDObject {

	private final MMDMaterial material;

	/**
	 *
	 * @param material
	 *            The material to make the bolt from
	 */
	public ItemMMDBolt(final MMDMaterial material) {
		this.material = material;
	}

	/**
	 *
	 * @param worldIn
	 *            The world
	 * @param stack
	 *            The itemstack
	 * @param shooter
	 *            The shooter
	 * @return The Custom Bolt
	 */
	@Override
	public EntityCustomBolt createBolt(final World worldIn, final ItemStack stack,
			final EntityPlayer shooter) {
		return new EntityCustomBolt(worldIn, stack, shooter);
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
