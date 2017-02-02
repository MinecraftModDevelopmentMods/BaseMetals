package com.mcmoddev.lib.items;

import java.util.List;

import com.mcmoddev.basemetals.items.MetalToolEffects;
import com.mcmoddev.lib.material.IMetalObject;
import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.util.Oredicts;

import cyano.basemetals.init.Materials;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Shovels
 * 
 * @author DrCyano
 *
 */
public class ItemMetalShovel extends ItemSpade implements IMetalObject {

	private final MetalMaterial material;
	private final String repairOreDictName;
	private final boolean regenerates;
	private final long regenInterval = 200;

	/**
	 *
	 * @param material The material to make the shovel from
	 */
	public ItemMetalShovel(MetalMaterial material) {
		super(Materials.getToolMaterialFor(material));
		this.material = material;
		this.setMaxDamage(this.material.getToolDurability());
		this.efficiencyOnProperMaterial = this.material.getToolEfficiency();
		this.repairOreDictName = Oredicts.INGOT + this.material.getCapitalizedName();
		this.regenerates = this.material.regenerates;
	}

	@Override
	public ToolMaterial getToolMaterial() {
		return this.toolMaterial;
	}

	@Override
	public int getItemEnchantability() {
		return this.toolMaterial.getEnchantability();
	}

	@Override
	public String getToolMaterialName() {
		return this.toolMaterial.toString();
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
	public boolean hitEntity(final ItemStack item, final EntityLivingBase target, final EntityLivingBase attacker) {
		super.hitEntity(item, target, attacker);
		MetalToolEffects.extraEffectsOnAttack(this.material, item, target, attacker);
		return true;
	}

	@Override
	public void onCreated(final ItemStack item, final World world, final EntityPlayer crafter) {
		super.onCreated(item, world, crafter);
		MetalToolEffects.extraEffectsOnCrafting(this.material, item, world, crafter);
	}

	@Override
	public void onUpdate(final ItemStack item, final World world, final Entity player, final int inventoryIndex, final boolean isHeld) {
		if (this.regenerates && !world.isRemote && isHeld && (item.getItemDamage() > 0)
				&& ((world.getTotalWorldTime() % this.regenInterval) == 0))
			item.setItemDamage(item.getItemDamage() - 1);
	}

	public String getMaterialName() {
		return this.material.getName();
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean b) {
		super.addInformation(stack, player, list, b);
		MetalToolEffects.addToolSpecialPropertiesToolTip(this.material, list);
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
}
