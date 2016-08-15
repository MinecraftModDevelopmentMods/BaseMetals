package cyano.basemetals.items;

import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.IOreDictionaryEntry;
import net.minecraft.block.BlockDoor;

/**
 * Doors
 */
public class ItemMetalDoor extends net.minecraft.item.ItemDoor implements IOreDictionaryEntry, IMetalObject {

	private final MetalMaterial metal;
	private final String oreDict;

	/**
	 * 
	 * @param metal
	 */
	public ItemMetalDoor(BlockDoor block, MetalMaterial metal) {
		super(block);
		this.metal = metal;
		this.oreDict = "door" + metal.getCapitalizedName();
	}

	@Override
	public String getOreDictionaryName() {
		return oreDict;
	}

	@Override
	public MetalMaterial getMetalMaterial() {
		return metal;
	}
}
