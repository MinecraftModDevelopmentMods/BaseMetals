package com.mcmoddev.lib.items;

import com.mcmoddev.lib.material.IMetalObject;
import com.mcmoddev.lib.material.MetalMaterial;

import net.minecraft.item.Item;

/**
 * version of Item that stores a metal material
 * 
 * @author DrCyano
 *
 */
public class GenericMetalItem extends Item implements IMetalObject {

	private final MetalMaterial material;

	/**
	 *
	 * @param material The material to make the item from
	 */
	public GenericMetalItem(MetalMaterial material) {
		this.material = material;
	}

	@Override
	public MetalMaterial getMaterial() {
		return this.material;
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public MetalMaterial getMetalMaterial() {
		return this.material;
	}
}
