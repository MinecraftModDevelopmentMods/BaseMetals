package com.mcmoddev.lib.item;

import java.util.List;

import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.basemetals.items.MMDToolEffects;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Shovels
 * 
 * @author DrCyano
 *
 */
public class ItemMMDShovel extends net.minecraft.item.ItemSpade implements IMMDObject {

	private final MMDMaterial material;

	/**
	 *
	 * @param material
	 *            The material to make the shovel from
	 */
	public ItemMMDShovel(final MMDMaterial material) {
		super(Materials.getToolMaterialFor(material));
		this.material = material;
		this.setMaxDamage(this.material.getToolDurability());
		this.efficiencyOnProperMaterial = this.material.getToolEfficiency();
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
		return MMDItemHelper.isToolRepairable(repairMaterial, this.material.getCapitalizedName());
	}

	@Override
	public boolean hitEntity(final ItemStack item, final EntityLivingBase target, final EntityLivingBase attacker) {
		super.hitEntity(item, target, attacker);
		MMDToolEffects.extraEffectsOnAttack(this.material, item, target, attacker);
		return true;
	}

	@Override
	public void onCreated(final ItemStack item, final World world, final EntityPlayer crafter) {
		super.onCreated(item, world, crafter);
		MMDToolEffects.extraEffectsOnCrafting(this.material, item, world, crafter);
	}

	@Override
	public void onUpdate(final ItemStack item, final World world, final Entity player, final int inventoryIndex, final boolean isHeld) {
		MMDItemHelper.doRegeneration(item, world, isHeld, this.material.regenerates());
	}

	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer player, final List<String> tooltip, final boolean b) {
		super.addInformation(stack, player, tooltip, b);
		MMDToolEffects.addToolSpecialPropertiesToolTip(this.material.getName(), tooltip);
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
