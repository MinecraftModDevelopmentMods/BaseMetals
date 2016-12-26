package cyano.basemetals.items;

import java.util.List;

import cyano.basemetals.init.Materials;
import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.IOreDictionaryEntry;
import cyano.basemetals.util.Config;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
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
	 * @param metal The material to make the shield from
	 */
	public ItemMetalShield(MetalMaterial metal) {
		this.metal = metal;
		this.setMaxDamage((int) (metal.strength * 168));
		this.setCreativeTab(CreativeTabs.TOOLS);
		this.repairOreDictName = "ingot" + metal.getCapitalizedName();
		if (Config.Options.ENABLE_STARSTEEL) {
			this.regenerates = metal.equals(Materials.getMaterialByName("starsteel"));
		} else {
			this.regenerates = false;
		}
	}

	@Override
	public MetalMaterial getMaterial() {
		return this.metal;
	}

	@Override
	@Deprecated
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
		final List<ItemStack> acceptableItems = OreDictionary.getOres(this.repairOreDictName);
		for (final ItemStack i : acceptableItems)
			if (ItemStack.areItemsEqual(i, repair))
				return true;
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return I18n.translateToLocal(this.getUnlocalizedName() + ".name");
	}

}
