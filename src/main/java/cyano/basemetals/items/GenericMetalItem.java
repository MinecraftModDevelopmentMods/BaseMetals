package cyano.basemetals.items;

import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import net.minecraft.item.Item;

/**
 * version of Item that stores a metal material
 */
public class GenericMetalItem extends Item  implements IMetalObject {

	private final MetalMaterial metal;

	public GenericMetalItem(MetalMaterial m){
		this.metal = m;
	}

	@Override public MetalMaterial getMetalMaterial(){
		return metal;
	}
}
