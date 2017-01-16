package cyano.basemetals.items;

import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.IOreDictionaryEntry;
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
		this.oreDict = "door" + this.material.getCapitalizedName();
	}

	/**
	 *
	 * @param block The block to use to make the door
	 * @param material The material to make the door from
	 */
	public ItemMetalDoor(BlockDoor block, MetalMaterial material) {
		super(block);
		this.material = material;
		this.oreDict = "door" + this.material.getCapitalizedName();
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
