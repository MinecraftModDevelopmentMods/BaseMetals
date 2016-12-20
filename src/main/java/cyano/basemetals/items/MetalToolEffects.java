package cyano.basemetals.items;

import cyano.basemetals.init.Materials;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.util.Config.Options;
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
	 * @param metal
	 * @param item
	 * @param target
	 * @param attacker
	 */
	public static void extraEffectsOnAttack(final MetalMaterial metal, final ItemStack item, final EntityLivingBase target, final EntityLivingBase attacker) {
		if (Options.ENABLE_COLDIRON) {
			if (metal.equals(Materials.getMaterialByName("coldiron"))) {
				if (target.isImmuneToFire()) {
					final DamageSource extraDamage = DamageSource.generic;
					target.attackEntityFrom(extraDamage, 3f);
				}
			}
		} else if (Options.ENABLE_ADAMANTINE) {
			if (metal.equals(Materials.getMaterialByName("adamantine"))) {
				if (target.getMaxHealth() > 20f) {
					final DamageSource extraDamage = DamageSource.generic;
					target.attackEntityFrom(extraDamage, 4f);
				}
			}
		} else if (Options.ENABLE_MITHRIL) {
			if (metal.equals(Materials.getMaterialByName("mithril"))) {
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
			if (metal.equals(Materials.getMaterialByName("aquarium"))) {
				if (target.canBreatheUnderwater()) {
					final DamageSource extraDamage = DamageSource.generic;
					target.attackEntityFrom(extraDamage, 4f);
				}
			}
		}
	}

	/**
	 *
	 * @param metal
	 * @param item
	 * @param world
	 * @param crafter
	 */
	public static void extraEffectsOnCrafting(final MetalMaterial metal, final ItemStack item, final World world, final EntityPlayer crafter) {
		// do nothing for now. This would be where achievements or automatic enchantments could appear
	}

	/**
	 *
	 * @param metal
	 * @param tooltipList
	 */
	public static void addToolSpecialPropertiesToolTip(MetalMaterial metal, java.util.List<String> tooltipList) {
		if (Options.ENABLE_ADAMANTINE) {
			if (metal == Materials.getMaterialByName("adamantine")) {
				tooltipList.add(net.minecraft.client.resources.I18n.format("tooltip.adamantine.tool").replace("%x", String.valueOf(4)));
			}
		}
		if (Options.ENABLE_AQUARIUM) {
			if (metal == Materials.getMaterialByName("aquarium")) {
				tooltipList.add(net.minecraft.client.resources.I18n.format("tooltip.aquarium.tool").replace("%x", String.valueOf(4)));
			}
		}
		if (Options.ENABLE_COLDIRON) {
			if (metal == Materials.getMaterialByName("coldiron")) {
				tooltipList.add(net.minecraft.client.resources.I18n.format("tooltip.coldiron.tool").replace("%x", String.valueOf(3)));
			}
		}
		if (Options.ENABLE_MITHRIL) {
			if (metal == Materials.getMaterialByName("mithril")) {
				tooltipList.add(net.minecraft.client.resources.I18n.format("tooltip.mithril.tool"));
			}
		}
		if (Options.ENABLE_STARSTEEL) {
			if (metal == Materials.getMaterialByName("starsteel")) {
				tooltipList.add(net.minecraft.client.resources.I18n.format("tooltip.starsteel.tool").replace("%x", String.valueOf(10)));
			}
		}
	}

	/**
	 *
	 * @param metal
	 * @param tooltipList
	 */
	public static void addArmorSpecialPropertiesToolTip(MetalMaterial metal, java.util.List<String> tooltipList) {
		if (Options.ENABLE_ADAMANTINE) {
			if (metal == Materials.getMaterialByName("adamantine")) {
				tooltipList.add(net.minecraft.client.resources.I18n.format("tooltip.adamantine.armor").replace("%x", String.valueOf(4)));
			}
		}
		if (Options.ENABLE_AQUARIUM) {
			if (metal == Materials.getMaterialByName("aquarium")) {
				tooltipList.add(net.minecraft.client.resources.I18n.format("tooltip.aquarium.armor").replace("%x", String.valueOf(4)));
			}
		}
		if (Options.ENABLE_COLDIRON) {
			if (metal == Materials.getMaterialByName("coldiron")) {
				tooltipList.add(net.minecraft.client.resources.I18n.format("tooltip.coldiron.armor").replace("%x", String.valueOf(3)));
			}
		}
		if (Options.ENABLE_MITHRIL) {
			if (metal == Materials.getMaterialByName("mithril")) {
				tooltipList.add(net.minecraft.client.resources.I18n.format("tooltip.mithril.armor"));
			}
		}
		if (Options.ENABLE_STARSTEEL) {
			if (metal == Materials.getMaterialByName("starsteel")) {
				tooltipList.add(net.minecraft.client.resources.I18n.format("tooltip.starsteel.armor").replace("%x", String.valueOf(10)));
			}
		}
	}
}
