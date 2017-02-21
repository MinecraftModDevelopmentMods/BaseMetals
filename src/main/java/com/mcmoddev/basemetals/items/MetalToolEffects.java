package com.mcmoddev.basemetals.items;

import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.material.MetalMaterial;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.client.resources.I18n;

/**
 * Tool Effects
 */
public abstract class MetalToolEffects {

	private MetalToolEffects() {
		throw new IllegalAccessError("Not a instantiable class");
	}

	/**
	 *
	 * @param material
	 *            The material
	 * @param item
	 *            The item
	 * @param target
	 *            The target
	 * @param attacker
	 *            The attacker
	 */
	public static void extraEffectsOnAttack(final MetalMaterial material, final ItemStack item, final EntityLivingBase target, final EntityLivingBase attacker) {
		if ((Options.enableColdIron) && (material.equals(Materials.coldiron))) {
			if (target.isImmuneToFire()) {
				final DamageSource extraDamage = DamageSource.generic;
				target.attackEntityFrom(extraDamage, 3f);
			}
		} else if ((Options.enableAdamantine) && (material.equals(Materials.adamantine))) {
			if (target.getMaxHealth() > 20f) {
				final DamageSource extraDamage = DamageSource.generic;
				target.attackEntityFrom(extraDamage, 4f);
			}
		} else if ((Options.enableMithril) && (material.equals(Materials.mithril))) {
			if (target.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
				final ResourceLocation witherKey = new ResourceLocation("wither");
				final ResourceLocation blindKey = new ResourceLocation("wither");
				final PotionEffect wither = new PotionEffect(Potion.REGISTRY.getObject(witherKey), 60, 3);
				final PotionEffect blind = new PotionEffect(Potion.REGISTRY.getObject(blindKey), 60, 1);
				target.addPotionEffect(wither);
				target.addPotionEffect(blind);
			}
		} else if ((Options.enableAquarium) && (material.equals(Materials.aquarium))) {
			if (target.canBreatheUnderwater()) {
				final DamageSource extraDamage = DamageSource.generic;
				target.attackEntityFrom(extraDamage, 4f);
			}
		}
	}

	/**
	 *
	 * @param material
	 *            The material
	 * @param item
	 *            The item
	 * @param world
	 *            The world
	 * @param crafter
	 *            The crafter
	 */
	public static void extraEffectsOnCrafting(final MetalMaterial material, final ItemStack item, final World world, final EntityPlayer crafter) {
		// TODO: do nothing for now. This would be where achievements or automatic enchantments could appear
	}

	/**
	 *
	 * @param material
	 *            The material
	 * @param tooltipList
	 *            The tooltip list
	 */
	public static void addToolSpecialPropertiesToolTip(MetalMaterial material, java.util.List<String> tooltipList) {
		if ((Options.enableAdamantine) && (material.equals(Materials.adamantine))) {
			tooltipList.add(I18n.format("tooltip.adamantine.tool", 4));
		}
		if ((Options.enableAquarium) && (material.equals(Materials.aquarium))) {
			tooltipList.add(I18n.format("tooltip.aquarium.tool", 4));
		}
		if ((Options.enableColdIron) && (material.equals(Materials.coldiron))) {
			tooltipList.add(I18n.format("tooltip.coldiron.tool", 3));
		}
		if ((Options.enableMithril) && (material.equals(Materials.mithril))) {
			tooltipList.add(I18n.format("tooltip.mithril.tool"));
		}
		if ((Options.enableStarSteel) && (material.equals(Materials.starsteel))) {
			tooltipList.add(I18n.format("tooltip.starsteel.tool", 10));
		}
	}

	/**
	 *
	 * @param material
	 *            The material
	 * @param tooltipList
	 *            The tooltip list
	 */
	public static void addArmorSpecialPropertiesToolTip(MetalMaterial material, java.util.List<String> tooltipList) {
		if ((Options.enableAdamantine) && (material.equals(Materials.adamantine))) {
			tooltipList.add(I18n.format("tooltip.adamantine.armor", 4));
		}
		if ((Options.enableAquarium) && (material.equals(Materials.aquarium))) {
			tooltipList.add(I18n.format("tooltip.aquarium.armor", 4));
		}
		if ((Options.enableColdIron) && (material.equals(Materials.coldiron))) {
			tooltipList.add(I18n.format("tooltip.coldiron.armor", 3));
		}
		if ((Options.enableMithril) && (material.equals(Materials.mithril))) {
			tooltipList.add(I18n.format("tooltip.mithril.armor"));
		}
		if ((Options.enableStarSteel) && (material.equals(Materials.starsteel))) {
			tooltipList.add(I18n.format("tooltip.starsteel.armor", 10));
		}
	}
}
