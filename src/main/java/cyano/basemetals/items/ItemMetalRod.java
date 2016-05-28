package cyano.basemetals.items;

import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.IOreDictionaryEntry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemMetalRod extends Item  implements IOreDictionaryEntry, IMetalObject {

	private final MetalMaterial metal;
	private final String oreDict;
	public ItemMetalRod(MetalMaterial m){
		this.metal = m;
		this.setCreativeTab(CreativeTabs.tabMaterials);
		this.oreDict = "dust"+metal.getCapitalizedName();
	}

	public String getOreDictionaryName(){
		return oreDict;
	}

	@Override public MetalMaterial getMetalMaterial(){
		return metal;
	}
}
