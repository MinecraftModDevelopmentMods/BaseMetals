package com.mcmoddev.lib.item;

import com.mcmoddev.lib.material.IMetalObject;
import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.registry.IOreDictionaryEntry;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.creativetab.CreativeTabs;

/**
 * Gears
 * 
 * @author Jasmine Iwanek
 *
 */
public class ItemMetalGear extends net.minecraft.item.Item implements IOreDictionaryEntry, IMetalObject {

	private final MetalMaterial material;
	private final String oreDict;

	/**
	 *
	 * @param material The material to make the gear from
	 */
	public ItemMetalGear(MetalMaterial material) {
		this.material = material;
		this.setCreativeTab(CreativeTabs.MATERIALS);
		this.oreDict = Oredicts.GEAR + this.material.getCapitalizedName();
	}

	@Override
	public String getOreDictionaryName() {
		return this.oreDict;
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
