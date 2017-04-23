package com.mcmoddev.lib.item;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.registry.IOreDictionaryEntry;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.block.BlockDoor;

/**
 * Doors
 */
public class ItemMMDDoor extends net.minecraft.item.ItemDoor implements IOreDictionaryEntry, IMMDObject {

	private final MMDMaterial material;
	private final String oreDict;

	/**
	 *
	 * @param material
	 *            The material to make the door from
	 */
	public ItemMMDDoor(MMDMaterial material) {
		super(material.getBlock(Names.DOORBLOCK));
		this.material = material;
		this.oreDict = Oredicts.DOOR + this.material.getCapitalizedName();
	}

	/**
	 *
	 * @param block
	 *            The block to use to make the door
	 * @param material
	 *            The material to make the door from
	 */
	public ItemMMDDoor(BlockDoor block, MMDMaterial material) {
		super(block);
		this.material = material;
		this.oreDict = Oredicts.DOOR + this.material.getCapitalizedName();
	}

	@Override
	public String getOreDictionaryName() {
		return this.oreDict;
	}

	@Override
	public MMDMaterial getMaterial() {
		return this.material;
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public MMDMaterial getMetalMaterial() {
		return this.material;
	}
}
