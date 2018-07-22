package com.mcmoddev.lib.item;

import javax.annotation.Nonnull;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.IMMDBurnableObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;

/**
 * version of Item that stores a material.
 *
 * @author DrCyano
 *
 */
public class GenericMMDItem extends Item implements IMMDObject, IMMDBurnableObject {

	private int burnTime = 0;
	private final MMDMaterial material;

	/**
	 *
	 * @param material
	 *            The material to make the item from
	 */
	public GenericMMDItem(final MMDMaterial material) {
		this.material = material;
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}

	@Override
	public void setBurnTime(final int timeInTicks) {
		this.burnTime = timeInTicks;
	}

	@Override
	public int getItemBurnTime(@Nonnull final ItemStack itemStack) {
		if (itemStack.getItem().equals(this)) {
			return this.burnTime;
		} else {
			return itemStack.getItem().getItemBurnTime(itemStack);
		}
	}

/*	@Override
	public float getSmeltingExperience(ItemStack itemStack) {
		// clamp to the valid range
		return Math.min(1.0f, Math.max(material.getOreSmeltXP(), 0.0f));
	}*/
}
