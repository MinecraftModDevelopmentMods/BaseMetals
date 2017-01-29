package com.mcmoddev.basemetals.items;

import com.mcmoddev.basemetals.material.MetalMaterial;
import com.mcmoddev.basemetals.util.Config.Options;

import cyano.basemetals.init.Materials;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Tool Effects
 */
public abstract class MetalToolEffects {

	/**
	 *
	 * @param material The material
	 * @param item The item
	 * @param target The target
	 * @param attacker The attacker
	 */
	public static void extraEffectsOnAttack(final MetalMaterial material, final ItemStack item, final EntityLivingBase target, final EntityLivingBase attacker) {
		if (Options.ENABLE_COLDIRON) {
			if (material.equals(Materials.coldiron)) {
				if (target.isImmuneToFire()) {
					final DamageSource extraDamage = DamageSource.generic;
					target.attackEntityFrom(extraDamage, 3f);
				}
			}
		} else if (Options.ENABLE_ADAMANTINE) {
			if (material.equals(Materials.adamantine)) {
				if (target.getMaxHealth() > 20f) {
					final DamageSource extraDamage = DamageSource.generic;
					target.attackEntityFrom(extraDamage, 4f);
				}
			}
		} else if (Options.ENABLE_MITHRIL) {
			if (material.equals(Materials.mithril)) {
				if (target.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
					final ResourceLocation witherKey = new ResourceLocation("wither");
					final ResourceLocation blindKey = new ResourceLocation("wither");
					final PotionEffect wither = new PotionEffect(Potion.REGISTRY.getObject(witherKey), 60, 3);
					final PotionEffect blind = new PotionEffect(Potion.REGISTRY.getObject(blindKey), 60, 1);
					target.addPotionEffect(wither);
					target.addPotionEffect(blind);
				}
			}
		} else if (Options.ENABLE_AQUARIUM) {
			if (material.equals(Materials.aquarium)) {
				if (target.canBreatheUnderwater()) {
					final DamageSource extraDamage = DamageSource.generic;
					target.attackEntityFrom(extraDamage, 4f);
				}
			}
		}
	}

	/**
	 *
	 * @param material The material
	 * @param item The item
	 * @param world The world
	 * @param crafter The crafter
	 */
	public static void extraEffectsOnCrafting(final MetalMaterial material, final ItemStack item, final World world, final EntityPlayer crafter) {
		// do nothing for now. This would be where achievements or automatic enchantments could appear
	}

	/**
	 *
	 * @param material The material
	 * @param tooltipList The tooltip list
	 */
	public static void addToolSpecialPropertiesToolTip(MetalMaterial material, java.util.List<String> tooltipList) {
		if (Options.ENABLE_ADAMANTINE) {
			if (material.equals(Materials.adamantine)) {
				tooltipList.add(net.minecraft.client.resources.I18n.format("tooltip.adamantine.tool").replace("%x", String.valueOf(4)));
			}
		}
		if (Options.ENABLE_AQUARIUM) {
			if (material.equals(Materials.aquarium)) {
				tooltipList.add(net.minecraft.client.resources.I18n.format("tooltip.aquarium.tool").replace("%x", String.valueOf(4)));
			}
		}
		if (Options.ENABLE_COLDIRON) {
			if (material.equals(Materials.coldiron)) {
				tooltipList.add(net.minecraft.client.resources.I18n.format("tooltip.coldiron.tool").replace("%x", String.valueOf(3)));
			}
		}
		if (Options.ENABLE_MITHRIL) {
			if (material.equals(Materials.mithril)) {
				tooltipList.add(net.minecraft.client.resources.I18n.format("tooltip.mithril.tool"));
			}
		}
		if (Options.ENABLE_STARSTEEL) {
			if (material.equals(Materials.starsteel)) {
				tooltipList.add(net.minecraft.client.resources.I18n.format("tooltip.starsteel.tool").replace("%x", String.valueOf(10)));
			}
		}
	}

	/**
	 *
	 * @param material The material
	 * @param tooltipList The tooltip list
	 */
	public static void addArmorSpecialPropertiesToolTip(MetalMaterial material, java.util.List<String> tooltipList) {
		if (Options.ENABLE_ADAMANTINE) {
			if (material.equals(Materials.adamantine)) {
				tooltipList.add(net.minecraft.client.resources.I18n.format("tooltip.adamantine.armor").replace("%x", String.valueOf(4)));
			}
		}
		if (Options.ENABLE_AQUARIUM) {
			if (material.equals(Materials.aquarium)) {
				tooltipList.add(net.minecraft.client.resources.I18n.format("tooltip.aquarium.armor").replace("%x", String.valueOf(4)));
			}
		}
		if (Options.ENABLE_COLDIRON) {
			if (material.equals(Materials.coldiron)) {
				tooltipList.add(net.minecraft.client.resources.I18n.format("tooltip.coldiron.armor").replace("%x", String.valueOf(3)));
			}
		}
		if (Options.ENABLE_MITHRIL) {
			if (material.equals(Materials.mithril)) {
				tooltipList.add(net.minecraft.client.resources.I18n.format("tooltip.mithril.armor"));
			}
		}
		if (Options.ENABLE_STARSTEEL) {
			if (material.equals(Materials.starsteel)) {
				tooltipList.add(net.minecraft.client.resources.I18n.format("tooltip.starsteel.armor").replace("%x", String.valueOf(10)));
			}
		}
	}
}
