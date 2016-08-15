package cyano.basemetals.items;

import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import net.minecraft.item.Item;

/**
 * version of Item that stores a metal material
 * @author DrCyano
 *
 */
public class GenericMetalItem extends Item  implements IMetalObject {

	private final MetalMaterial metal;

	/**
	 * 
	 * @param metal
	 */
	public GenericMetalItem(MetalMaterial metal) {
		this.metal = metal;
	}

	@Override
	public MetalMaterial getMetalMaterial() {
		return metal;
	}
}
