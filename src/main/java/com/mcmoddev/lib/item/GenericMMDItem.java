package com.mcmoddev.lib.item;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.item.Item;

/**
 * version of Item that stores a metal material
 * 
 * @author DrCyano
 *
 */
public class GenericMMDItem extends Item implements IMMDObject {

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
}
