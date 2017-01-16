package cyano.basemetals.items;

import java.util.List;

import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.IOreDictionaryEntry;
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

	final MetalMaterial material;
	private final String oreDict;
	protected final String repairOreDictName;
	protected final boolean regenerates;
	protected final long regenInterval = 200;

	/**
	 *
	 * @param material The material to make the shield from
	 */
	public ItemMetalShield(MetalMaterial material) {
		this.material = material;
		this.setMaxDamage((int) (this.material.strength * 168));
		this.setCreativeTab(CreativeTabs.TOOLS);
		this.oreDict = "shield" + this.material.getCapitalizedName();
		this.repairOreDictName = "ingot" + this.material.getCapitalizedName();
		this.regenerates = this.material.regenerates;
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

	@Override
	public String getOreDictionaryName() {
		return this.oreDict;
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
