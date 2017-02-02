package com.mcmoddev.lib.items;

import com.mcmoddev.basemetals.registry.IOreDictionaryEntry;
import com.mcmoddev.lib.material.IMetalObject;
import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.block.BlockDoor;

/**
 * Doors
 */
public class ItemMetalDoor extends net.minecraft.item.ItemDoor implements IOreDictionaryEntry, IMetalObject {

	private final MetalMaterial material;
	private final String oreDict;

	/**
	 *
	 * @param material The material to make the door from
	 */
	public ItemMetalDoor(MetalMaterial material) {
		super(material.doorBlock);
		this.material = material;
		this.oreDict = Oredicts.DOOR + this.material.getCapitalizedName();
	}

	/**
	 *
	 * @param block The block to use to make the door
	 * @param material The material to make the door from
	 */
	public ItemMetalDoor(BlockDoor block, MetalMaterial material) {
		super(block);
		this.material = material;
		this.oreDict = Oredicts.DOOR + this.material.getCapitalizedName();
	}

	@Override
	public String getOreDictionaryName() {
		return this.oreDict;
	}

	@Override
	public MetalMaterial getMaterial() {
		return this.material;
	}

	@Override
	@Deprecated
	public MetalMaterial getMetalMaterial() {
		return this.material;
	}
}
