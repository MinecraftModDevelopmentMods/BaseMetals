package cyano.basemetals.items;

import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.IOreDictionaryEntry;
import net.minecraft.creativetab.CreativeTabs;

/**
 * Powders
 */
public class ItemMetalPowder extends net.minecraft.item.Item implements IOreDictionaryEntry, IMetalObject {

	protected final MetalMaterial metal;
	private final String oreDict;

	public ItemMetalPowder(MetalMaterial metal) {
		this.metal = metal;
		this.setCreativeTab(CreativeTabs.MATERIALS);
		this.oreDict = "dust"+metal.getCapitalizedName();
	}

	public String getOreDictionaryName() {
		return oreDict;
	}

	@Override
	public MetalMaterial getMetalMaterial() {
		return metal;
	}
}
