package com.mcmoddev.lib.item;

import java.util.List;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.registry.IOreDictionaryEntry;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class ItemMMDShield extends ItemShield implements IOreDictionaryEntry, IMMDObject {

	final MMDMaterial material;
	private final String oreDict;
	protected final String repairOreDictName;
	protected final boolean regenerates;
	protected static final long REGEN_INTERVAL = 200;

	/**
	 *
	 * @param material
	 *            The material to make the shield from
	 */
	public ItemMMDShield(MMDMaterial material) {
		this.material = material;
		this.setMaxDamage((int) (this.material.strength * 168));
		this.setCreativeTab(CreativeTabs.TOOLS);
		this.oreDict = Oredicts.SHIELD + this.material.getCapitalizedName();
		this.repairOreDictName = Oredicts.INGOT + this.material.getCapitalizedName();
		this.regenerates = this.material.regenerates;
	}

	@Override
	public MMDMaterial getMaterial() {
		return this.material;
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public MMDMaterial getMetalMaterial() {
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

	@Override
	@SuppressWarnings("deprecation")
	public String getItemStackDisplayName(ItemStack stack) {
		String name = String.format("%s.name", this.getUnlocalizedName());
		if( net.minecraft.util.text.translation.I18n.canTranslate(name) ) {
			if( stack.getSubCompound("BlockEntityTag", false) != null ) {
				String colored_name = String.format("%s.%s.name", this.getUnlocalizedName(), ItemBanner.getBaseColor(stack));
				return net.minecraft.util.text.translation.I18n.translateToLocal(colored_name);
			} else {
				return name;
			}
		}
		return name;
	}

}
