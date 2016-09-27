package cyano.basemetals.items;

import java.util.List;

import cyano.basemetals.init.Materials;
import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.IOreDictionaryEntry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

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

	/**
	 * Return whether this item is repairable in an anvil.
	 */
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		List<ItemStack> acceptableItems = OreDictionary.getOres(repairOreDictName);
        for(ItemStack i : acceptableItems) {
            if(ItemStack.areItemsEqual(i, repair))
                return true;
        }
        return false;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public String getItemStackDisplayName(ItemStack stack)
    {
        return I18n.translateToLocal(this.getUnlocalizedName() + ".name");
    }
	
	
}
