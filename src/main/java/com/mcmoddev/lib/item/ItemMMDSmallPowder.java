package com.mcmoddev.lib.item;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.item.Item;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class ItemMMDSmallPowder extends Item implements IMMDObject {

	protected final MMDMaterial material;

	/**
	 *
	 * @param material
	 *            The material to make the small powder from
	 */
	public ItemMMDSmallPowder(MMDMaterial material) {
		this.material = material;
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
