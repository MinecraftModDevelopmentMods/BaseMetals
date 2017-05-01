package com.mcmoddev.lib.item;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Nonnull;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.items.MMDToolEffects;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Achievements;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Armor
 * 
 * @author DrCyano
 *
 */
public class ItemMMDArmor extends net.minecraft.item.ItemArmor implements IMMDObject {

	private static final ResourceLocation speedPotionKey = new ResourceLocation("speed");
	private static final ResourceLocation jumpPotionKey = new ResourceLocation("jump_boost");
	private static final ResourceLocation slowPotionKey = new ResourceLocation("slowness");
	private static final ResourceLocation protectionPotionKey = new ResourceLocation("resistance");
	private static final ResourceLocation waterBreathingPotionKey = new ResourceLocation("water_breathing");
	private static final ResourceLocation waterBuffPotionKey = new ResourceLocation("resistance");
	private static final ResourceLocation fatiguePotionKey = new ResourceLocation("mining_fatigue");
	private static final ResourceLocation fireproofPotionKey = new ResourceLocation("fire_resistance");

	private final String customTexture;
	private final MMDMaterial material;
	private final String repairOreDictName;

	private static final int UPDATE_INTERVAL = 11;
	private static final int EFFECT_DURATION = 45;
	private static final Map<EntityPlayer, AtomicLong> playerUpdateTimestampMap = new HashMap<>();
	private static final Map<EntityPlayer, AtomicInteger> playerUpdateCountMap = new HashMap<>();

	private static final Map<EntityPlayer, AtomicInteger> starsteelUpdateCache = new HashMap<>();
	private static final Map<EntityPlayer, AtomicInteger> adamantineUpdateCache = new HashMap<>();
	private static final Map<EntityPlayer, AtomicInteger> leadUpdateCache = new HashMap<>();

	protected ItemMMDArmor(@Nonnull MMDMaterial material, @Nonnull ArmorMaterial armorMat, int renderIndex,
							 EntityEquipmentSlot slot) {
		super(armorMat, renderIndex, slot);
		this.material = material;
		this.repairOreDictName = Oredicts.INGOT + material.getCapitalizedName();
		this.customTexture = Loader.instance().activeModContainer().getModId() + ":textures/models/armor/" + material.getName() + "_layer_" + (slot == EntityEquipmentSlot.LEGS ? 2 : 1) + ".png";
	}

	@Override
	public void onArmorTick(World w, EntityPlayer player, ItemStack armor) {
		if (!(playerUpdateTimestampMap.containsKey(player))) {
			playerUpdateTimestampMap.put(player, new AtomicLong(0));
			playerUpdateCountMap.put(player, new AtomicInteger(0));
			return;
		}
		if (!w.isRemote && w.getTotalWorldTime() > playerUpdateTimestampMap.get(player).get()) {
			playerUpdateTimestampMap.get(player).set(w.getTotalWorldTime() + UPDATE_INTERVAL);
			int updateCount = playerUpdateCountMap.get(player).getAndIncrement();
			for(int i = 0; i < 4; i++) {
				if(player.inventory.armorInventory[i] != null && player.inventory.armorInventory[i].getItem() instanceof ItemMMDArmor) {
					doArmorUpdate(w, player, player.inventory.armorInventory[i], updateCount);
				}
			}
		}
	}

	protected void doArmorUpdate(final World w, final EntityPlayer player, final ItemStack armor, int i) {
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
			if ((Options.materialEnabled.get(MaterialNames.STARSTEEL)) && (MaterialNames.STARSTEEL.equals(((ItemMMDArmor) armorItem).material.getName()))) {
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
			if ((Options.materialEnabled.get(MaterialNames.LEAD)) && (MaterialNames.LEAD.equals(((ItemMMDArmor) armorItem).material.getName()))) {
				lead: {
				// used to count up the Star-Steel armor items
				if (!(leadUpdateCache.containsKey(player))) {
					leadUpdateCache.put(player, new AtomicInteger(0));
				}
				leadUpdateCache.get(player).incrementAndGet();
				break lead;
			}
			}
			if ((Options.materialEnabled.get(MaterialNames.ADAMANTINE)) && (MaterialNames.ADAMANTINE.equals(((ItemMMDArmor) armorItem).material.getName()))) {
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
				if (Options.materialEnabled.get(MaterialNames.STARSTEEL)) {
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
				if (Options.materialEnabled.get(MaterialNames.LEAD)) {
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
				if (Options.materialEnabled.get(MaterialNames.ADAMANTINE)) {
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
				if (Options.materialEnabled.get(MaterialNames.COLDIRON)) {
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
				if (Options.materialEnabled.get(MaterialNames.MITHRIL)) {
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
				if (Options.materialEnabled.get(MaterialNames.AQUARIUM)) {
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
	 *            The material to make the armor from
	 * @param slot
	 *            Type of armor
	 * @return The armor
	 */
	protected static ItemMMDArmor createArmorBase(@Nonnull MMDMaterial material, EntityEquipmentSlot slot) {
		ArmorMaterial amaterial = Materials.getArmorMaterialFor(material);
		if (amaterial == null) {
			// uh-oh
			BaseMetals.logger.error("Failed to load armor material enum for " + material);
			return null;
		}
		return new ItemMMDArmor(material, amaterial, amaterial.ordinal(), slot);
	}

	/**
	 *
	 * @param material
	 *            The material to make the helmet from
	 * @return The Helmet
	 */
	public static ItemMMDArmor createHelmet(MMDMaterial material) {
		return createArmorBase(material, EntityEquipmentSlot.HEAD);
	}

	/**
	 * 
	 * @param material
	 *            The material to make the chestplate from
	 * @return The Chestplate
	 */
	public static ItemMMDArmor createChestplate(MMDMaterial material) {
		return createArmorBase(material, EntityEquipmentSlot.CHEST);
	}

	/**
	 * 
	 * @param material
	 *            The material to make the leggings from
	 * @return The Leggings
	 */
	public static ItemMMDArmor createLeggings(MMDMaterial material) {
		return createArmorBase(material, EntityEquipmentSlot.LEGS);
	}

	/**
	 * 
	 * @param material
	 *            The material to make the boots from
	 * @return The Boots
	 */
	public static ItemMMDArmor createBoots(MMDMaterial material) {
		return createArmorBase(material, EntityEquipmentSlot.FEET);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public String getArmorTexture(ItemStack stack, Entity e, EntityEquipmentSlot slot, String layer) {
		return customTexture;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean b) {
		super.addInformation(stack, player, list, b);
		MMDToolEffects.addArmorSpecialPropertiesToolTip(material, list);
	}

	/**
	 * 
	 * @param item
	 *            The item
	 * @param world
	 *            The world
	 * @param crafter
	 *            The crafter
	 */
	public void extraEffectsOnCrafting(final ItemStack item, final World world, final EntityPlayer crafter) {
		//
	}

	/**
	 * @return whether this item is repairable in an anvil.
	 */
	@Override
	public boolean getIsRepairable(ItemStack srcItemStack, ItemStack repairMaterial) {
		// repair with string or wool
		List<ItemStack> acceptableItems = OreDictionary.getOres(repairOreDictName);
		for (ItemStack i : acceptableItems) {
			if (ItemStack.areItemsEqual(i, repairMaterial))
				return true;
		}
		return false;
	}

	@Override
	public void onCreated(final ItemStack item, final World world, final EntityPlayer crafter) {
		super.onCreated(item, world, crafter);
		extraEffectsOnCrafting(item, world, crafter);
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
