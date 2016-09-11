package cyano.basemetals.items;

import cyano.basemetals.init.Materials;
import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.IOreDictionaryEntry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemShield;

/**
 * 
 * @author Jasmine Iwanek
 *
 */
public class ItemMetalShield extends ItemShield implements IOreDictionaryEntry, IMetalObject {

	final MetalMaterial metal;
	protected final String repairOreDictName;
	protected final boolean regenerates;
	protected final long regenInterval = 200;

	/**
	 *
	 * @param metal
	 */
	public ItemMetalShield(MetalMaterial metal) {
		this.metal = metal;
		this.setMaxDamage(metal.getToolDurability());
		this.setCreativeTab(CreativeTabs.TOOLS);
		this.repairOreDictName = "ingot" + metal.getCapitalizedName();
		this.regenerates = metal.equals(Materials.starsteel);
	}

	@Override
	public MetalMaterial getMetalMaterial() {
		return this.metal;
	}

	@Override
	public String getOreDictionaryName() {
		return "shield" + this.metal.getCapitalizedName();
	}
}
