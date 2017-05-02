package com.mcmoddev.lib.item;

import com.mcmoddev.lib.common.item.IHorseArmor;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.HorseArmorUtils;

import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.item.ItemStack;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class ItemMMDHorseArmor extends GenericMMDItem implements IHorseArmor {

	public ItemMMDHorseArmor(MMDMaterial material) {
		super(material);
		this.setMaxStackSize(1);
	}

	@Override
	public HorseArmorType getArmorType() {
		// return HorseArmorType.DIAMOND;
		return HorseArmorUtils.getArmorType(1024, "test", "tes");
	}

	@Override
	public String getArmorTexture(EntityHorse entity, ItemStack stack) {
		return stack.getItem().getRegistryName().getResourceDomain() + ":textures/entity/horse/armor/horse_armor_" + getMMDMaterial().getName() + ".png";
	}
}