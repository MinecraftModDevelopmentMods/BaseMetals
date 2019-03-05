package com.mcmoddev.basemetals.properties;

import java.util.List;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BMEPropertiesHelper {
	private static final String TOOLTIP = "tooltip.";
	private static final String ARMOR = ".armor";
	private static final String TOOL = ".tool";


	/**
	 *
	 * @param material
	 *            The material
	 * @param itemStack
	 *            The item
	 * @param target
	 *            The target
	 * @param attacker
	 *            The attacker
	 */
	public static void extraEffectsOnAttack(final MMDMaterial material, final ItemStack itemStack,
			final EntityLivingBase target, final EntityLivingBase attacker) {
		final String materialName = material.getName();
		if (materialName.equals(MaterialNames.COLDIRON)) {
			if (target.isImmuneToFire()) {
				final DamageSource extraDamage = DamageSource.GENERIC;
				target.attackEntityFrom(extraDamage, 3f);
			}
		} else if (materialName.equals(MaterialNames.ADAMANTINE)) {
			if (target.getMaxHealth() > 20f) {
				final DamageSource extraDamage = DamageSource.GENERIC;
				target.attackEntityFrom(extraDamage, 4f);
			}
		} else if (materialName.equals(MaterialNames.MITHRIL)) {
			if (target.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
				final PotionEffect wither = new PotionEffect(MobEffects.WITHER, 60, 3);
				final PotionEffect blind = new PotionEffect(MobEffects.BLINDNESS, 60, 1);
				target.addPotionEffect(wither);
				target.addPotionEffect(blind);
			}
		} else if ((materialName.equals(MaterialNames.AQUARIUM))
				&& (target.canBreatheUnderwater())) {
			final DamageSource extraDamage = DamageSource.GENERIC;
			target.attackEntityFrom(extraDamage, 4f);
		}
	}

	/**
	 *
	 * @param materialName
	 *            The materialName
	 * @param tooltipList
	 *            The tooltip list
	 */
	@SideOnly(Side.CLIENT)
	public static void addToolSpecialPropertiesToolTip(final String materialName,
			final List<String> tooltipList) {
		switch (materialName) {
			case MaterialNames.ADAMANTINE:
				tooltipList.add(I18n.format(TOOLTIP + MaterialNames.ADAMANTINE + TOOL, 4));
				break;
			case MaterialNames.AQUARIUM:
				tooltipList.add(I18n.format(TOOLTIP + MaterialNames.AQUARIUM + TOOL, 4));
				break;
			case MaterialNames.COLDIRON:
				tooltipList.add(I18n.format(TOOLTIP + MaterialNames.COLDIRON + TOOL, 3));
				break;
			case MaterialNames.MITHRIL:
				tooltipList.add(I18n.format(TOOLTIP + MaterialNames.MITHRIL + TOOL));
				break;
			case MaterialNames.STARSTEEL:
				tooltipList.add(I18n.format(TOOLTIP + MaterialNames.STARSTEEL + TOOL, 10));
				break;
			default:
		}
	}

	/**
	 *
	 * @param materialName
	 *            The materialName
	 * @param tooltipList
	 *            The tooltip list
	 */
	@SideOnly(Side.CLIENT)
	public static void addArmorSpecialPropertiesToolTip(final String materialName,
			final List<String> tooltipList) {
		switch (materialName) {
			case MaterialNames.ADAMANTINE:
				tooltipList.add(I18n.format(TOOLTIP + MaterialNames.ADAMANTINE + ARMOR, 4));
				break;
			case MaterialNames.AQUARIUM:
				tooltipList.add(I18n.format(TOOLTIP + MaterialNames.AQUARIUM + ARMOR, 4));
				break;
			case MaterialNames.COLDIRON:
				tooltipList.add(I18n.format(TOOLTIP + MaterialNames.COLDIRON + ARMOR, 3));
				break;
			case MaterialNames.MITHRIL:
				tooltipList.add(I18n.format(TOOLTIP + MaterialNames.MITHRIL + ARMOR));
				break;
			case MaterialNames.STARSTEEL:
				tooltipList.add(I18n.format(TOOLTIP + MaterialNames.STARSTEEL + ARMOR, 10));
				break;
			default:
		}
	}	
}
