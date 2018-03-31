package com.mcmoddev.lib.item;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

/**
 * version of Item that stores a material.
 *
 * @author DrCyano
 *
 */
public class GenericMMDItem extends net.minecraft.item.Item implements IMMDObject {

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
}
