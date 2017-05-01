package com.mcmoddev.lib.item;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.creativetab.CreativeTabs;

/**
 * Gears
 * 
 * @author Jasmine Iwanek
 *
 */
public class ItemMMDGear extends net.minecraft.item.Item implements IMMDObject {

	private final MMDMaterial material;

	/**
	 *
	 * @param material
	 *            The material to make the gear from
	 */
	public ItemMMDGear(MMDMaterial material) {
		this.material = material;
		this.setCreativeTab(CreativeTabs.MATERIALS);
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
