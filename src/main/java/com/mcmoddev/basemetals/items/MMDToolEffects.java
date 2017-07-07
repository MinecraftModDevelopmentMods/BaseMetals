package com.mcmoddev.basemetals.items;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.mcmoddev.basemetals.data.AchievementNames;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Achievements;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.PotionKeys;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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

	private static final String TOOLTIP = "tooltip.";
	private static final String ARMOR = ".armor";
	private static final String TOOL = ".tool";

	private static final ResourceLocation speedPotionKey = PotionKeys.SPEED;
	private static final ResourceLocation jumpPotionKey = PotionKeys.JUMP_BOOST;
	private static final ResourceLocation slowPotionKey = PotionKeys.SLOWNESS;
	private static final ResourceLocation protectionPotionKey = PotionKeys.RESISTANCE;
	private static final ResourceLocation waterBreathingPotionKey = PotionKeys.WATER_BREATHING;
	private static final ResourceLocation waterBuffPotionKey = PotionKeys.RESISTANCE;
	private static final ResourceLocation fatiguePotionKey = PotionKeys.MINING_FATIGUE;
	private static final ResourceLocation fireproofPotionKey = PotionKeys.FIRE_RESISTANCE;
	private static final ResourceLocation witherKey = PotionKeys.WITHER;
	private static final ResourceLocation blindKey = PotionKeys.BLINDNESS;

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
		if (material.getName().equals(MaterialNames.COLDIRON)) {
			if (target.isImmuneToFire()) {
				final DamageSource extraDamage = DamageSource.GENERIC;
				target.attackEntityFrom(extraDamage, 3f);
			}
		} else if (material.getName().equals(MaterialNames.ADAMANTINE)) {
			if (target.getMaxHealth() > 20f) {
				final DamageSource extraDamage = DamageSource.GENERIC;
				target.attackEntityFrom(extraDamage, 4f);
			}
		} else if (material.getName().equals(MaterialNames.MITHRIL)) {
			if (target.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
				final PotionEffect wither = new PotionEffect(Potion.REGISTRY.getObject(witherKey), 60, 3);
				final PotionEffect blind = new PotionEffect(Potion.REGISTRY.getObject(blindKey), 60, 1);
				target.addPotionEffect(wither);
				target.addPotionEffect(blind);
			}
		} else if ((material.getName().equals(MaterialNames.AQUARIUM)) && (target.canBreatheUnderwater())) {
			final DamageSource extraDamage = DamageSource.GENERIC;
			target.attackEntityFrom(extraDamage, 4f);
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
		// TODO: do nothing for now. This would be where achievements or automatic
		// enchantments could appear
	}

	public static void extraEffectsOnArmorUpdate(final World w, final EntityPlayer player, final MMDMaterial material, final ItemStack armor, int i) {
		// some sanity checks
		if (armor == ItemStack.EMPTY)
			return;
		if (armor.getItem() == null)
			return;
		if (player == null)
			return;
		if (i % 2 == 0) {
			// count armor pieces
			if (material.getName().equals(MaterialNames.STARSTEEL)) {
				starsteel: {
					// used to count up the starsteel armor items
					countArmorPieces(starsteelUpdateCache, player);
					// Achievement
					if ((armor.getItem() == Materials.getMaterialByName(MaterialNames.STARSTEEL).getItem(Names.BOOTS)) && (Options.enableAchievements())) {
						player.addStat(Achievements.getAchievementByName(AchievementNames.MOON_BOOTS), 1);
					}
					break starsteel;
				}
			}
			if (material.getName().equals(MaterialNames.LEAD)) {
				lead: {
					// used to count up the Lead armor items
					countArmorPieces(leadUpdateCache, player);
					break lead;
				}
			}
			if (material.getName().equals(MaterialNames.ADAMANTINE)) {
				adamantine: {
					// used to count up the Adamantine armor items
					countArmorPieces(adamantineUpdateCache, player);
					break adamantine;
				}
			}
		} else {
			// apply potion effects. Note that "Level I" is actually effect level 0 in the
			// effect constructor
			if (material.getName().equals(MaterialNames.STARSTEEL)) {
				starsteel: {
					if (!starsteelUpdateCache.containsKey(player))
						break starsteel;
					int num = starsteelUpdateCache.get(player).getAndSet(0);
					if (num == 0)
						break starsteel;
					final PotionEffect jumpBoost = new PotionEffect(Potion.REGISTRY.getObject(jumpPotionKey), EFFECT_DURATION, num - 1, false, false);
					player.addPotionEffect(jumpBoost);
					if (num > 1) {
						final PotionEffect speedBoost = new PotionEffect(Potion.REGISTRY.getObject(speedPotionKey), EFFECT_DURATION, num - 2, false, false);
						player.addPotionEffect(speedBoost);
					}
					break starsteel;
				}
			}
			if (material.getName().equals(MaterialNames.LEAD)) {
				lead: {
					if (!(leadUpdateCache.containsKey(player)))
						break lead;
					int level = leadUpdateCache.get(player).getAndSet(0) / 2;
					if (level == 0)
						break lead;
					if (level > 0) {
						final PotionEffect speedLoss = new PotionEffect(Potion.REGISTRY.getObject(slowPotionKey), EFFECT_DURATION, level - 1, false, false);
						player.addPotionEffect(speedLoss);
					}
					break lead;
				}
			}
			if (material.getName().equals(MaterialNames.ADAMANTINE)) {
				adamantine: {
					if (!(adamantineUpdateCache.containsKey(player)))
						break adamantine;
					int num = adamantineUpdateCache.get(player).getAndSet(0);
					int level = num / 2;
					if (level == 0)
						break adamantine;
					if (level > 0) {
						final PotionEffect protection = new PotionEffect(Potion.REGISTRY.getObject(protectionPotionKey), EFFECT_DURATION, level - 1, false, false);
						player.addPotionEffect(protection);
					}
					// Achievement
					if ((Options.enableAchievements()) && (hasFullSuit(player, MaterialNames.ADAMANTINE))) {
						player.addStat(Achievements.getAchievementByName(AchievementNames.JUGGERNAUT), 1);
					}
					break adamantine;
				}
			}
			// full suit of cold-iron makes you fire-proof
			if ((material.getName().equals(MaterialNames.COLDIRON)) && (hasFullSuit(player, MaterialNames.COLDIRON))) {
				final PotionEffect fireProtection = new PotionEffect(Potion.REGISTRY.getObject(fireproofPotionKey), EFFECT_DURATION, 0, false, false);
				player.addPotionEffect(fireProtection);
				// Achievement
				if ((player.getHeldItemMainhand() != ItemStack.EMPTY && player.getHeldItemMainhand().getItem() == Materials.getMaterialByName(MaterialNames.COLDIRON).getItem(Names.SWORD)) && (Options.enableAchievements())) {
					player.addStat(Achievements.getAchievementByName(AchievementNames.DEMON_SLAYER), 1);
				}
			}
			// full suit of Mithril protects you from withering, poison, nausea,
			// and hunger effects
			if ((material.getName().equals(MaterialNames.MITHRIL)) && (hasFullSuit(player, MaterialNames.MITHRIL))) {
				final List<Potion> removeList = new LinkedList<>(); // needed to avoid concurrent modification error
				Iterator<PotionEffect> effectIterator = player.getActivePotionEffects().iterator();
				while (effectIterator.hasNext()) {
					PotionEffect pe = effectIterator.next();
					Potion p = pe.getPotion();
					if (p.isBadEffect()) {
						removeList.add(p);
					}
				}
				for (Potion p : removeList) {
					player.removePotionEffect(p);
				}
				// Achievement
				if ((player.getHeldItemMainhand() != ItemStack.EMPTY && player.getHeldItemMainhand().getItem() == Materials.getMaterialByName(MaterialNames.MITHRIL).getItem(Names.SWORD)) && (Options.enableAchievements())) {
					player.addStat(Achievements.getAchievementByName(AchievementNames.ANGEL_OF_DEATH), 1);
				}
			}
			// full suit of Aquarium makes you breathe and heal under water
			if ((material.getName().equals(MaterialNames.AQUARIUM)) && ((hasFullSuit(player, MaterialNames.AQUARIUM)) && (player.posY > 0) && (player.posY < 255))) {
				Block b1 = w.getBlockState(new BlockPos(player.posX, player.posY, player.posZ)).getBlock();
				Block b2 = w.getBlockState(new BlockPos(player.posX, player.posY + 1, player.posZ)).getBlock();
				if (b1 == Blocks.WATER && b2 == Blocks.WATER) {
					final PotionEffect waterBreathing = new PotionEffect(Potion.REGISTRY.getObject(waterBreathingPotionKey), EFFECT_DURATION, 0, false, false);
					player.addPotionEffect(waterBreathing);
					final PotionEffect protection = new PotionEffect(Potion.REGISTRY.getObject(waterBuffPotionKey), EFFECT_DURATION, 0, false, false);
					player.addPotionEffect(protection);
					player.removePotionEffect(Potion.REGISTRY.getObject(fatiguePotionKey));
					// Achievement
					if (Options.enableAchievements()) {
						player.addStat(Achievements.getAchievementByName(AchievementNames.SCUBA_DIVER), 1);
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
		if (material.getName().equals(MaterialNames.ADAMANTINE)) {
			tooltipList.add(I18n.format(TOOLTIP + MaterialNames.ADAMANTINE + TOOL, 4));
		} else if (material.getName().equals(MaterialNames.AQUARIUM)) {
			tooltipList.add(I18n.format(TOOLTIP + MaterialNames.AQUARIUM + TOOL, 4));
		} else if (material.getName().equals(MaterialNames.COLDIRON)) {
			tooltipList.add(I18n.format(TOOLTIP + MaterialNames.COLDIRON + TOOL, 3));
		} else if (material.getName().equals(MaterialNames.MITHRIL)) {
			tooltipList.add(I18n.format(TOOLTIP + MaterialNames.MITHRIL + TOOL));
		} else if (material.getName().equals(MaterialNames.STARSTEEL)) {
			tooltipList.add(I18n.format(TOOLTIP + MaterialNames.STARSTEEL + TOOL, 10));
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
		if (material.getName().equals(MaterialNames.ADAMANTINE)) {
			tooltipList.add(I18n.format(TOOLTIP + MaterialNames.ADAMANTINE + ARMOR, 4));
		} else if (material.getName().equals(MaterialNames.AQUARIUM)) {
			tooltipList.add(I18n.format(TOOLTIP + MaterialNames.AQUARIUM + ARMOR, 4));
		} else if (material.getName().equals(MaterialNames.COLDIRON)) {
			tooltipList.add(I18n.format(TOOLTIP + MaterialNames.COLDIRON + ARMOR, 3));
		} else if (material.getName().equals(MaterialNames.MITHRIL)) {
			tooltipList.add(I18n.format(TOOLTIP + MaterialNames.MITHRIL + ARMOR));
		} else if (material.getName().equals(MaterialNames.STARSTEEL)) {
			tooltipList.add(I18n.format(TOOLTIP + MaterialNames.STARSTEEL + ARMOR, 10));
		}
	}

	private static void countArmorPieces(Map<EntityPlayer, AtomicInteger> updateCache, EntityPlayer player) {
		if (!(updateCache.containsKey(player))) {
			updateCache.put(player, new AtomicInteger(0));
		}

		updateCache.get(player).incrementAndGet();
	}

	private static boolean hasFullSuit(EntityPlayer player, String materialName) {
		MMDMaterial material = Materials.getMaterialByName(materialName);
		if (player.inventory.armorInventory.get(3) != ItemStack.EMPTY && player.inventory.armorInventory.get(2).getItem() == material.getItem(Names.HELMET)
				&& player.inventory.armorInventory.get(2) != ItemStack.EMPTY && player.inventory.armorInventory.get(1).getItem() == material.getItem(Names.CHESTPLATE)
				&& player.inventory.armorInventory.get(1) != ItemStack.EMPTY && player.inventory.armorInventory.get(1).getItem() == material.getItem(Names.LEGGINGS)
				&& player.inventory.armorInventory.get(0) != ItemStack.EMPTY&& player.inventory.armorInventory.get(0).getItem() == material.getItem(Names.BOOTS)) {
				return true;
			}
			return false;
	}
}
