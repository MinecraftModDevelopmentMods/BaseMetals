package com.mcmoddev.lib.item;

import java.util.Locale;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.common.item.IHorseArmor;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class ItemMMDHorseArmor extends GenericMMDItem implements IHorseArmor {

	private final HorseArmorType myArmorType;

	/**
	 *
	 * @param material
	 */
	public ItemMMDHorseArmor(final MMDMaterial material) {
		super(material);
		this.setMaxStackSize(1);
		this.myArmorType = addArmorType(material.getName(), material.getHorseArmorProtection());
	}

	@Override
	public HorseArmorType getHorseArmorType(final ItemStack stack) {
		if ((stack != null) && (stack.getItem() != this)) { // NB stack CAN be null, when our ASM
															// does it
			return HorseArmorType.NONE;
		}
		return this.myArmorType;
	}

	@Override
	public String getHorseArmorTexture(final EntityLiving entity, final ItemStack stack) {
		return stack.getItem() == this ? this.getArmorTexture() : "";
	}

	private String getArmorTexture() {
		return this.getRegistryName().getResourceDomain()
				+ ":textures/entity/horse/armor/horse_armor_" + this.getMMDMaterial().getName()
				+ ".png";
	}

	private static HorseArmorType addArmorType(final String materialName,
			final int protectionLevel) {
		return EnumHelper.addEnum(HorseArmorType.class,
				"BASEMETALS_" + materialName.toUpperCase(Locale.ROOT),
				new Class[] { int.class, String.class, String.class }, protectionLevel,
				materialName, BaseMetals.MODID + "_" + materialName);
	}
}
