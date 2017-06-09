package com.mcmoddev.basemetals.items;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Achievements;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Tool Effects
 */
public abstract class MMDToolEffects {

	private static final ResourceLocation speedPotionKey = new ResourceLocation("speed");
	private static final ResourceLocation jumpPotionKey = new ResourceLocation("jump_boost");
	private static final ResourceLocation slowPotionKey = new ResourceLocation("slowness");
	private static final ResourceLocation protectionPotionKey = new ResourceLocation("resistance");
	private static final ResourceLocation waterBreathingPotionKey = new ResourceLocation("water_breathing");
	private static final ResourceLocation waterBuffPotionKey = new ResourceLocation("resistance");
	private static final ResourceLocation fatiguePotionKey = new ResourceLocation("mining_fatigue");
	private static final ResourceLocation fireproofPotionKey = new ResourceLocation("fire_resistance");

	private static final int EFFECT_DURATION = 45;

	private static final Map<EntityPlayer, AtomicInteger> starsteelUpdateCache = new HashMap<>();
	private static final Map<EntityPlayer, AtomicInteger> adamantineUpdateCache = new HashMap<>();
	private static final Map<EntityPlayer, AtomicInteger> leadUpdateCache = new HashMap<>();

	private MMDToolEffects() {
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
	public static void extraEffectsOnAttack(final MMDMaterial material, final ItemStack item, final EntityLivingBase target, final EntityLivingBase attacker) {
		if ((Options.materialEnabled(MaterialNames.COLDIRON)) && (material.equals(Materials.getMaterialByName(MaterialNames.COLDIRON)))) {
			if (target.isImmuneToFire()) {
				final DamageSource extraDamage = DamageSource.GENERIC;
				target.attackEntityFrom(extraDamage, 3f);
			}
		} else if ((Options.materialEnabled(MaterialNames.ADAMANTINE)) && (material.equals(Materials.getMaterialByName(MaterialNames.ADAMANTINE)))) {
			if (target.getMaxHealth() > 20f) {
				final DamageSource extraDamage = DamageSource.GENERIC;
				target.attackEntityFrom(extraDamage, 4f);
			}
		} else if ((Options.materialEnabled(MaterialNames.MITHRIL)) && (material.equals(Materials.getMaterialByName(MaterialNames.MITHRIL)))) {
			if (target.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
				final ResourceLocation witherKey = new ResourceLocation("wither");
				final ResourceLocation blindKey = new ResourceLocation("wither");
				final PotionEffect wither = new PotionEffect(Potion.REGISTRY.getObject(witherKey), 60, 3);
				final PotionEffect blind = new PotionEffect(Potion.REGISTRY.getObject(blindKey), 60, 1);
				target.addPotionEffect(wither);
				target.addPotionEffect(blind);
			}
		} else if ((Options.materialEnabled(MaterialNames.AQUARIUM)) && (material.equals(Materials.getMaterialByName(MaterialNames.AQUARIUM)))) {
			if (target.canBreatheUnderwater()) {
				final DamageSource extraDamage = DamageSource.GENERIC;
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
	public static void extraEffectsOnCrafting(final MMDMaterial material, final ItemStack item, final World world, final EntityPlayer crafter) {
		// TODO: do nothing for now. This would be where achievements or automatic enchantments could appear
	}

	public static void extraEffectsOnArmorUpdate(final World w, final EntityPlayer player, final MMDMaterial material, final ItemStack armor, int i) {
		// some sanity checks
		if (armor == null)
			return;
		if (armor.getItem() == null)
			return;
		if (player == null)
			return;
		Item armorItem = armor.getItem();
		if (i % 2 == 0) {
			// count armor pieces
			if ((Options.materialEnabled(MaterialNames.STARSTEEL)) && (MaterialNames.STARSTEEL.equals(material.getName()))) {
				starsteel: {
				// used to count up the starsteel armor items
				if (!(starsteelUpdateCache.containsKey(player))) {
					starsteelUpdateCache.put(player, new AtomicInteger(0));
				}
				starsteelUpdateCache.get(player).incrementAndGet();
				// Achievement
				if (Options.enableAchievements()) {
					if (armorItem == Materials.getMaterialByName(MaterialNames.STARSTEEL).getItem(Names.BOOTS)) {
						player.addStat(Achievements.getAchievementByName("moon_boots"), 1);
					}
					break starsteel;
				}
			}
			}
			if ((Options.materialEnabled(MaterialNames.LEAD)) && (MaterialNames.LEAD.equals(material.getName()))) {
				lead: {
				// used to count up the Star-Steel armor items
				if (!(leadUpdateCache.containsKey(player))) {
					leadUpdateCache.put(player, new AtomicInteger(0));
				}
				leadUpdateCache.get(player).incrementAndGet();
				break lead;
			}
			}
			if ((Options.materialEnabled(MaterialNames.ADAMANTINE)) && (MaterialNames.ADAMANTINE.equals(material.getName()))) {
				adamantine: {
				// used to count up the Adamantine armor items
				if (!(adamantineUpdateCache.containsKey(player))) {
					adamantineUpdateCache.put(player, new AtomicInteger(0));
				}
				adamantineUpdateCache.get(player).incrementAndGet();
				break adamantine;
			}
			}
			} else {
				// apply potion effects. Note that "Level I" is actually effect level 0 in the effect constructor 
				if (Options.materialEnabled(MaterialNames.STARSTEEL)) {
					starsteel: {
					if (!starsteelUpdateCache.containsKey(player))
						break starsteel;
					int num = starsteelUpdateCache.get(player).getAndSet(0);
					if (num == 0)
						break starsteel;
					final PotionEffect jumpBoost = new PotionEffect(Potion.REGISTRY.getObject(jumpPotionKey), EFFECT_DURATION, num - 1, false, false);
					player.addPotionEffect(jumpBoost);
					if(num > 1) {
						final PotionEffect speedBoost = new PotionEffect(Potion.REGISTRY.getObject(speedPotionKey), EFFECT_DURATION, num - 2, false, false);
						player.addPotionEffect(speedBoost);
					}
					break starsteel;
				}
				}
				if (Options.materialEnabled(MaterialNames.LEAD)) {
					lead: {
					if (!(leadUpdateCache.containsKey(player)))
						break lead;
					int level = leadUpdateCache.get(player).getAndSet(0) / 2;
					if (level == 0)
						break lead;
					if(level > 0) {
						final PotionEffect speedLoss = new PotionEffect(Potion.REGISTRY.getObject(slowPotionKey), EFFECT_DURATION, level - 1, false, false);
						player.addPotionEffect(speedLoss);
					}
					break lead;
				}
				}
				if (Options.materialEnabled(MaterialNames.ADAMANTINE)) {
					adamantine: {
					if (!(adamantineUpdateCache.containsKey(player)))
						break adamantine;
					int num = adamantineUpdateCache.get(player).getAndSet(0);
					int level = num / 2;
					if (level == 0)
						break adamantine;
					if(level > 0) {
						final PotionEffect protection = new PotionEffect(Potion.REGISTRY.getObject(protectionPotionKey), EFFECT_DURATION, level - 1, false, false);
						player.addPotionEffect(protection);
					}
					// Achievement
					if ((Options.enableAchievements()) && (num == 4)) {
						player.addStat(Achievements.getAchievementByName("juggernaut"), 1);
					}
					break adamantine;
				}
				}
				// full suit of cold-iron makes you fire-proof
				if (Options.materialEnabled(MaterialNames.COLDIRON)) {
					if(armorItem == Materials.getMaterialByName(MaterialNames.COLDIRON).getItem(Names.HELMET)) {
						if(player.inventory.armorInventory[2] != null && player.inventory.armorInventory[2].getItem() == Materials.getMaterialByName(MaterialNames.COLDIRON).getItem(Names.CHESTPLATE)
								&& player.inventory.armorInventory[1] != null && player.inventory.armorInventory[1].getItem() == Materials.getMaterialByName(MaterialNames.COLDIRON).getItem(Names.LEGGINGS)
								&& player.inventory.armorInventory[0] != null && player.inventory.armorInventory[0].getItem() == Materials.getMaterialByName(MaterialNames.COLDIRON).getItem(Names.BOOTS)) {
							final PotionEffect fireProtection = new PotionEffect(Potion.REGISTRY.getObject(fireproofPotionKey), EFFECT_DURATION, 0, false, false);
							player.addPotionEffect(fireProtection);
							// Achievement
							if (Options.enableAchievements()) {
								if(player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == Materials.getMaterialByName(MaterialNames.COLDIRON).getItem(Names.SWORD)) {
									player.addStat(Achievements.getAchievementByName("demon_slayer"), 1);
								}
							}
						}
					}
				}
				// full suit of Mithril protects you from withering, poison, nausea,
				// and hunger effects
				if (Options.materialEnabled(MaterialNames.MITHRIL)) {
					if(armorItem == Materials.getMaterialByName(MaterialNames.MITHRIL).getItem(Names.HELMET)) {
						if(player.inventory.armorInventory[2] != null && player.inventory.armorInventory[2].getItem() == Materials.getMaterialByName(MaterialNames.MITHRIL).getItem(Names.CHESTPLATE)
								&& player.inventory.armorInventory[1] != null && player.inventory.armorInventory[1].getItem() == Materials.getMaterialByName(MaterialNames.MITHRIL).getItem(Names.LEGGINGS)
								&& player.inventory.armorInventory[0] != null && player.inventory.armorInventory[0].getItem() == Materials.getMaterialByName(MaterialNames.MITHRIL).getItem(Names.BOOTS)) {
							final List<Potion> removeList = new LinkedList<>(); // needed to avoid concurrent modification error
							Iterator<PotionEffect> effectIterator = player.getActivePotionEffects().iterator();
							while(effectIterator.hasNext()) {
								PotionEffect pe = effectIterator.next();
								Potion p = pe.getPotion();
								if(p.isBadEffect()) {
									removeList.add(p);
								}
							}
							for(Potion p : removeList) {
								player.removePotionEffect(p);
							}
							// Achievement
							if (Options.enableAchievements()) {
								if(player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == Materials.getMaterialByName(MaterialNames.MITHRIL).getItem(Names.SWORD)) {
									player.addStat(Achievements.getAchievementByName("angel_of_death"), 1);
								}
							}
						}
					}
				}
				// full suit of Aquarium makes you breathe and heal under water
				if (Options.materialEnabled(MaterialNames.AQUARIUM)) {
					if(armorItem == Materials.getMaterialByName(MaterialNames.AQUARIUM).getItem(Names.HELMET) && player.posY > 0 && player.posY < 255) {
						if(player.inventory.armorInventory[2] != null && player.inventory.armorInventory[2].getItem() == Materials.getMaterialByName(MaterialNames.AQUARIUM).getItem(Names.CHESTPLATE)
								&& player.inventory.armorInventory[1] != null && player.inventory.armorInventory[1].getItem() == Materials.getMaterialByName(MaterialNames.AQUARIUM).getItem(Names.LEGGINGS)
								&& player.inventory.armorInventory[0] != null && player.inventory.armorInventory[0].getItem() == Materials.getMaterialByName(MaterialNames.AQUARIUM).getItem(Names.BOOTS)) {
							Block b1 = w.getBlockState(new BlockPos(player.posX,player.posY, player.posZ)).getBlock();
							Block b2 = w.getBlockState(new BlockPos(player.posX,player.posY + 1, player.posZ)).getBlock();
							if(b1 == Blocks.WATER && b2 == Blocks.WATER) {
								final PotionEffect waterBreathing = new PotionEffect(Potion.REGISTRY.getObject(waterBreathingPotionKey), EFFECT_DURATION, 0, false, false);
								player.addPotionEffect(waterBreathing);
								final PotionEffect protection = new PotionEffect(Potion.REGISTRY.getObject(waterBuffPotionKey), EFFECT_DURATION, 0, false, false);
								player.addPotionEffect(protection);
								player.removePotionEffect(Potion.REGISTRY.getObject(fatiguePotionKey));
								// Achievement
								if (Options.enableAchievements()) {
									player.addStat(Achievements.getAchievementByName("scuba_diver"), 1);
								}
							}
						}
					}
				}
			}
	}
	
	/**
	 *
	 * @param material
	 *            The material
	 * @param tooltipList
	 *            The tooltip list
	 */
	@SideOnly(Side.CLIENT)
	public static void addToolSpecialPropertiesToolTip(MMDMaterial material, java.util.List<String> tooltipList) {
		if ((Options.materialEnabled(MaterialNames.ADAMANTINE)) && (material.equals(Materials.getMaterialByName(MaterialNames.ADAMANTINE)))) {
			tooltipList.add(I18n.format("tooltip.adamantine.tool", 4));
		}
		if ((Options.materialEnabled(MaterialNames.AQUARIUM)) && (material.equals(Materials.getMaterialByName(MaterialNames.AQUARIUM)))) {
			tooltipList.add(I18n.format("tooltip.aquarium.tool", 4));
		}
		if ((Options.materialEnabled(MaterialNames.COLDIRON)) && (material.equals(Materials.getMaterialByName(MaterialNames.COLDIRON)))) {
			tooltipList.add(I18n.format("tooltip.coldiron.tool", 3));
		}
		if ((Options.materialEnabled(MaterialNames.MITHRIL)) && (material.equals(Materials.getMaterialByName(MaterialNames.MITHRIL)))) {
			tooltipList.add(I18n.format("tooltip.mithril.tool"));
		}
		if ((Options.materialEnabled(MaterialNames.STARSTEEL)) && (material.equals(Materials.getMaterialByName(MaterialNames.STARSTEEL)))) {
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
	@SideOnly(Side.CLIENT)
	public static void addArmorSpecialPropertiesToolTip(MMDMaterial material, java.util.List<String> tooltipList) {
		if ((Options.materialEnabled(MaterialNames.ADAMANTINE)) && (material.equals(Materials.getMaterialByName(MaterialNames.ADAMANTINE)))) {
			tooltipList.add(I18n.format("tooltip.adamantine.armor", 4));
		}
		if ((Options.materialEnabled(MaterialNames.AQUARIUM)) && (material.equals(Materials.getMaterialByName(MaterialNames.AQUARIUM)))) {
			tooltipList.add(I18n.format("tooltip.aquarium.armor", 4));
		}
		if ((Options.materialEnabled(MaterialNames.COLDIRON)) && (material.equals(Materials.getMaterialByName(MaterialNames.COLDIRON)))) {
			tooltipList.add(I18n.format("tooltip.coldiron.armor", 3));
		}
		if ((Options.materialEnabled(MaterialNames.MITHRIL)) && (material.equals(Materials.getMaterialByName(MaterialNames.MITHRIL)))) {
			tooltipList.add(I18n.format("tooltip.mithril.armor"));
		}
		if ((Options.materialEnabled(MaterialNames.STARSTEEL)) && (material.equals(Materials.getMaterialByName(MaterialNames.STARSTEEL)))) {
			tooltipList.add(I18n.format("tooltip.starsteel.armor", 10));
		}
	}
}
