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
	public ItemMetalDoor(BlockDoor block,MetalMaterial metal) {
		super(block);
		this.metal = metal;
	}

	@Override
	public String getOreDictionaryName() {
		return "door"+metal.getCapitalizedName();
	}
	@Override
	public MetalMaterial getMetalMaterial() {
		return metal;
	}
}
