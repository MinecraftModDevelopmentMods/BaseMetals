package com.mcmoddev.lib.item;

import javax.annotation.Nonnull;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.item.ItemStack;

/**
 * version of Item that stores a material
 * 
 * @author DrCyano
 *
 */
public class GenericMMDItem extends net.minecraft.item.Item implements IMMDObject {
	private int burnTime = 0;

	private final MMDMaterial material;

	/**
	 *
	 * @param material
	 *            The material to make the item from
	 */
	public GenericMMDItem(MMDMaterial material) {
		this.material = material;
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
	
	
	public void setBurnTime(int timeInTicks) {
		this.burnTime = timeInTicks;
	}
	
	@Override
	public int getItemBurnTime(@Nonnull ItemStack itemStack) {
		if( itemStack.getItem().equals(this) ) {
			return this.burnTime;
		} else {
			return itemStack.getItem().getItemBurnTime(itemStack);
		}
	}

}
