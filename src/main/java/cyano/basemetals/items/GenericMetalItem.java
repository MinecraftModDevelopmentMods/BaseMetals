package cyano.basemetals.items;

import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
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

	@Override
	@Deprecated
	public MetalMaterial getMetalMaterial() {
		return this.material;
	}
}
