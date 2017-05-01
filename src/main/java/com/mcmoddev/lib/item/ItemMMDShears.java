package com.mcmoddev.lib.item;

import java.util.List;

import com.mcmoddev.basemetals.items.MMDToolEffects;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class ItemMMDShears extends ItemShears implements IMMDObject {

	protected final MMDMaterial material;
	protected final String repairOreDictName;
	protected final boolean regenerates;
	protected static final long REGEN_INTERVAL = 200;

	/**
	 *
	 * @param material
	 *            The material to make the shears from
	 */
	public ItemMMDShears(MMDMaterial material) {
		this.material = material;
		this.setMaxDamage(this.material.getToolDurability());
		this.setCreativeTab(CreativeTabs.TOOLS);
		this.repairOreDictName = Oredicts.INGOT + this.material.getCapitalizedName();
		this.regenerates = this.material.regenerates();
	}

	@Override
	public boolean getIsRepairable(final ItemStack intputItem, final ItemStack repairMaterial) {
		final List<ItemStack> acceptableItems = OreDictionary.getOres(this.repairOreDictName);
		for (final ItemStack i : acceptableItems)
			if (ItemStack.areItemsEqual(i, repairMaterial))
				return true;

		return false;
	}

	@Override
	public void onUpdate(final ItemStack item, final World world, final Entity player, final int inventoryIndex, final boolean isHeld) {
		if (this.regenerates && !world.isRemote && isHeld && (item.getItemDamage() > 0) && ((world.getTotalWorldTime() % REGEN_INTERVAL) == 0))
			item.setItemDamage(item.getItemDamage() - 1);
	}

	public String getMaterialName() {
		return this.material.getName();
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean b) {
		super.addInformation(stack, player, list, b);
		MMDToolEffects.addToolSpecialPropertiesToolTip(this.material, list);
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
