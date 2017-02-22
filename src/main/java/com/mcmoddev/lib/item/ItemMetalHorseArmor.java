package com.mcmoddev.lib.item;

import com.mcmoddev.lib.common.item.IHorseArmor;
import com.mcmoddev.lib.material.IMetalObject;
import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.util.HorseArmorUtils;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class ItemMetalHorseArmor extends Item implements IMetalObject, IHorseArmor {

	private final MetalMaterial material;

	public ItemMetalHorseArmor(MetalMaterial material) {
		this.material = material;
		this.setCreativeTab(CreativeTabs.COMBAT);
		this.setMaxStackSize(1);
	}

	@Override
	public HorseArmorType getArmorType() {
//		return HorseArmorType.DIAMOND;
		return HorseArmorUtils.getArmorType( 1024, "test","tes");
	}

	@Override
	public String getArmorTexture(EntityHorse entity, ItemStack stack) {
		return stack.getItem().getRegistryName().getResourceDomain() + ":textures/entity/horse/armor/horse_armor_" + this.material.getName() + ".png";
	}

	@Override
	public MetalMaterial getMaterial() {
		return this.material;
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public MetalMaterial getMetalMaterial() {
		return this.material;
	}
}