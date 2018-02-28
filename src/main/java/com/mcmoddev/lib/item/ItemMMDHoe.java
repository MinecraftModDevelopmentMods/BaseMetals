package com.mcmoddev.lib.item;

import java.util.HashSet;
import java.util.Set;

import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.basemetals.items.MMDToolEffects;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Hoes.
 *
 * @author DrCyano
 *
 */
public class ItemMMDHoe extends net.minecraft.item.ItemHoe implements IMMDObject {

	private final MMDMaterial material;
	private final Set<String> toolTypes;

	/**
	 *
	 * @param material
	 *            The material to make the hoe from
	 */
	public ItemMMDHoe(final MMDMaterial material) {
		super(Materials.getToolMaterialFor(material));
		this.material = material;
		this.setMaxDamage(this.material.getToolDurability());
		this.toolTypes = new HashSet<>();
		this.toolTypes.add(Names.HOE.toString());
	}

	@Override
	public boolean getIsRepairable(final ItemStack intputItem, final ItemStack repairMaterial) {
		return MMDItemHelper.isToolRepairable(repairMaterial, this.material.getCapitalizedName());
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public int getHarvestLevel(final ItemStack stack, final String typeRequested, final EntityPlayer player, final IBlockState blockState) {
		if ((typeRequested != null) && this.toolTypes.contains(typeRequested)) {
			return this.material.getToolHarvestLevel();
		}
		return -1;
	}

	@Override
	public Set<String> getToolClasses(final ItemStack item) {
		return this.toolTypes;
	}

	@Override
	public float getDestroySpeed(final ItemStack tool, final IBlockState target) {
		if (this.canHarvestBlock(target, tool)) {
			return Math.max(1.0f, this.material.getToolEfficiency());
		} else {
			return 1.0f;
		}
	}

	@Override
	public boolean hitEntity(final ItemStack item, final EntityLivingBase target, final EntityLivingBase attacker) {
		super.hitEntity(item, target, attacker);
		MMDToolEffects.extraEffectsOnAttack(this.material, item, target, attacker);
		return true;
	}

	@Override
	public boolean canHarvestBlock(final IBlockState target) {
		if (this.toolTypes.contains(target.getBlock().getHarvestTool(target))) {
			return this.material.getToolHarvestLevel() >= target.getBlock().getHarvestLevel(target);
		}
		return false;
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
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}

	@Override
	public String getMaterialName() {
		return this.material.getName();
	}
}
