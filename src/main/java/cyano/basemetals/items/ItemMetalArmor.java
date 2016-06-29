package cyano.basemetals.items;

import cyano.basemetals.BaseMetals;
import cyano.basemetals.init.Achievements;
import cyano.basemetals.init.Materials;
import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
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
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Armor
 */
public class ItemMetalArmor extends net.minecraft.item.ItemArmor implements IMetalObject {

    private static final ResourceLocation speedPotionKey = new ResourceLocation("speed");
    private static final ResourceLocation jumpPotionKey = new ResourceLocation("jump_boost");
    private static final ResourceLocation slowPotionKey = new ResourceLocation("slowness");
    private static final ResourceLocation protectionPotionKey = new ResourceLocation("resistance");
    private static final ResourceLocation waterBreathingPotionKey = new ResourceLocation("water_breathing");
    private static final ResourceLocation waterBuffPotionKey = new ResourceLocation("resistance");
    private static final ResourceLocation fatiguePotionKey = new ResourceLocation("mining_fatigue");
    private static final ResourceLocation fireproofPotionKey = new ResourceLocation("fire_resistance");


    protected final String customTexture;
	protected final MetalMaterial metal;
	protected final String repairOreDictName;

	protected ItemMetalArmor(MetalMaterial metal, ArmorMaterial armorMat, int renderIndex,
                             EntityEquipmentSlot slot) {
		super(armorMat, renderIndex, slot);
		this.metal = metal;
		this.repairOreDictName = "ingot"+metal.getCapitalizedName();
		this.customTexture = BaseMetals.MODID+":textures/models/armor/"+metal.getName()+"_layer_"+(slot == EntityEquipmentSlot.LEGS ? 2 : 1)+".png";
	}

	private static final int UPDATE_INTERVAL = 11;
	private static final int EFFECT_DURATION = 45;
	private static final Map<EntityPlayer,AtomicLong> playerUpdateTimestampMap = new HashMap<>();
	private static final Map<EntityPlayer,AtomicInteger> playerUpdateCountMap = new HashMap<>();

	@Override
	public void onArmorTick(World w, EntityPlayer player, ItemStack armor) {
		if(playerUpdateTimestampMap.containsKey(player) == false) {
			playerUpdateTimestampMap.put(player, new AtomicLong(0));
			playerUpdateCountMap.put(player, new AtomicInteger(0));
			return;
		}
		if(!w.isRemote && w.getTotalWorldTime() > playerUpdateTimestampMap.get(player).get()) {
	//		FMLLog.info("onArmorTick update triggered at time="+w.getTotalWorldTime()+" (update target was "+playerUpdateTimestampMap.get(player).get()+") for player "+player.getName()+" by item "+armor); // debug code
			playerUpdateTimestampMap.get(player).set(w.getTotalWorldTime() + UPDATE_INTERVAL);
			int updateCount = playerUpdateCountMap.get(player).getAndIncrement();
			for(int i = 0; i < 4; i++){
				if(player.inventory.armorInventory[i] != null && player.inventory.armorInventory[i].getItem() instanceof ItemMetalArmor) {
					doArmorUpdate(w,player,player.inventory.armorInventory[i],updateCount);
				}
			}
		}
	}

	private static final Map<EntityPlayer,AtomicInteger> starsteelUpdateCache = new HashMap<>();
	private static final Map<EntityPlayer,AtomicInteger> adamantineUpdateCache = new HashMap<>();
	private static final Map<EntityPlayer,AtomicInteger> leadUpdateCache = new HashMap<>();

	protected void doArmorUpdate(final World w, final EntityPlayer player, final ItemStack armor,
			int i) {
		// some sanity checks
		if(armor == null) return;
		if(armor.getItem() == null) return;
		if(player == null) return;
	//	FMLLog.info("doArmorUpdate "+i+" for "+player.getName()+" on item "+armor); // debug code
		Item armorItem = armor.getItem();
		if(i % 2 == 0){
			// count armor pieces
			if(((ItemMetalArmor)armorItem).metal.equals(Materials.starsteel)) {
				starsteel: {
					// used to count up the starsteel armor items
					if(starsteelUpdateCache.containsKey(player) == false){
						starsteelUpdateCache.put(player, new AtomicInteger(0));
					}
					starsteelUpdateCache.get(player).incrementAndGet();
					if(armorItem == cyano.basemetals.init.Items.starsteel_boots) player.addStat(Achievements.moon_boots, 1);
					break starsteel;
				}
			}
			if(((ItemMetalArmor)armorItem).metal.equals(Materials.lead)) {
				lead: {
					// used to count up the starsteel armor items
					if(leadUpdateCache.containsKey(player) == false){
						leadUpdateCache.put(player, new AtomicInteger(0));
					}
					leadUpdateCache.get(player).incrementAndGet();
					break lead;
				}
			}
			if(((ItemMetalArmor)armorItem).metal.equals(Materials.adamantine)) {
				adamantine: {
					// used to count up the adamantine armor items
					if(adamantineUpdateCache.containsKey(player) == false){
						adamantineUpdateCache.put(player, new AtomicInteger(0));
					}
					adamantineUpdateCache.get(player).incrementAndGet();
					break adamantine;
				}
			}
		} else {
			// apply potion effects. Note that "Level I" is actually effect level 0 in the effect constructor 
			starsteel: {
				if(starsteelUpdateCache.containsKey(player) == false) break starsteel;
				int num = starsteelUpdateCache.get(player).getAndSet(0);
				if(num == 0) break starsteel;
				final PotionEffect jumpBoost = new PotionEffect(Potion.REGISTRY.getObject(jumpPotionKey), EFFECT_DURATION, num-1, false, false);
				player.addPotionEffect(jumpBoost);
				if(num > 1) {
					final PotionEffect speedBoost = new PotionEffect(Potion.REGISTRY.getObject(speedPotionKey), EFFECT_DURATION, num-2, false, false);
					player.addPotionEffect(speedBoost);
				}
				break starsteel;
			}
			lead: {
				if(leadUpdateCache.containsKey(player) == false) break lead;
				int level = leadUpdateCache.get(player).getAndSet(0) / 2;
				if(level == 0) break lead;
				if(level > 0) {
					final PotionEffect speedLoss = new PotionEffect(Potion.REGISTRY.getObject(slowPotionKey), EFFECT_DURATION, level-1, false, false);
					player.addPotionEffect(speedLoss);
				}
				break lead;
			}
			adamantine: {
				if(adamantineUpdateCache.containsKey(player) == false) break adamantine;
				int num = adamantineUpdateCache.get(player).getAndSet(0);
				int level = num / 2;
				if(level == 0) break adamantine;
				if(level > 0) {
					final PotionEffect protection = new PotionEffect(Potion.REGISTRY.getObject(protectionPotionKey), EFFECT_DURATION, level-1, false, false);
					player.addPotionEffect(protection);
				}
				if(num == 4) {
					player.addStat(Achievements.juggernaut, 1);
				}
				break adamantine;
			}
			// full suit of cold-iron makes you fire-proof
			if(armorItem == cyano.basemetals.init.Items.coldiron_helmet) {
				if(player.inventory.armorInventory[2] != null && player.inventory.armorInventory[2].getItem() == cyano.basemetals.init.Items.coldiron_chestplate
						&& player.inventory.armorInventory[1] != null && player.inventory.armorInventory[1].getItem() == cyano.basemetals.init.Items.coldiron_leggings
						&& player.inventory.armorInventory[0] != null && player.inventory.armorInventory[0].getItem() == cyano.basemetals.init.Items.coldiron_boots) {
					final PotionEffect fireProtection = new PotionEffect(Potion.REGISTRY.getObject(fireproofPotionKey), EFFECT_DURATION, 0, false, false);
					player.addPotionEffect(fireProtection);
					if(player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == cyano.basemetals.init.Items.coldiron_sword) {
						player.addStat(Achievements.demon_slayer, 1);
					}
				}
			}
			// full suit of mithril protects you from withering, poison, nausea, and hunger effects
			if(armorItem == cyano.basemetals.init.Items.mithril_helmet) {
				if(player.inventory.armorInventory[2] != null && player.inventory.armorInventory[2].getItem() == cyano.basemetals.init.Items.mithril_chestplate
						&& player.inventory.armorInventory[1] != null && player.inventory.armorInventory[1].getItem() == cyano.basemetals.init.Items.mithril_leggings
						&& player.inventory.armorInventory[0] != null && player.inventory.armorInventory[0].getItem() == cyano.basemetals.init.Items.mithril_boots) {
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
					if(player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == cyano.basemetals.init.Items.mithril_sword) {
						player.addStat(Achievements.angel_of_death, 1);
					}
				}
			}
			// full suit of aquarium makes you breathe and heal underwater
			if(armorItem == cyano.basemetals.init.Items.aquarium_helmet && player.posY > 0 && player.posY < 255) {
				if(player.inventory.armorInventory[2] != null && player.inventory.armorInventory[2].getItem() == cyano.basemetals.init.Items.aquarium_chestplate
						&& player.inventory.armorInventory[1] != null && player.inventory.armorInventory[1].getItem() == cyano.basemetals.init.Items.aquarium_leggings
						&& player.inventory.armorInventory[0] != null && player.inventory.armorInventory[0].getItem() == cyano.basemetals.init.Items.aquarium_boots) {
					Block b1 = w.getBlockState(new BlockPos(player.posX,player.posY, player.posZ)).getBlock();
					Block b2 = w.getBlockState(new BlockPos(player.posX,player.posY+1, player.posZ)).getBlock();
					if(b1 == Blocks.WATER && b2 == Blocks.WATER) {
						final PotionEffect waterBreathing = new PotionEffect(Potion.REGISTRY.getObject(waterBreathingPotionKey), EFFECT_DURATION, 0, false, false);
						player.addPotionEffect(waterBreathing);
						final PotionEffect protection = new PotionEffect(Potion.REGISTRY.getObject(waterBuffPotionKey), EFFECT_DURATION, 0, false, false);
						player.addPotionEffect(protection);
                        player.removePotionEffect(Potion.REGISTRY.getObject(fatiguePotionKey));
						player.addStat(Achievements.scuba_diver, 1);
					}
				}
			}
		}
	}

	public static ItemMetalArmor createHelmet(MetalMaterial metal) {
		ArmorMaterial material = cyano.basemetals.init.Materials.getArmorMaterialFor(metal);
		if(material == null) {
			// uh-oh
			FMLLog.severe("Failed to load armor material enum for "+metal);
		}
		return new ItemMetalArmor(metal, material, material.ordinal(), EntityEquipmentSlot.HEAD);
	}
	
	public static ItemMetalArmor createChestplate(MetalMaterial metal) {
		ArmorMaterial material = cyano.basemetals.init.Materials.getArmorMaterialFor(metal);
		if(material == null) {
			// uh-oh
			FMLLog.severe("Failed to load armor material enum for "+metal);
		}
		return new ItemMetalArmor(metal, material, material.ordinal(), EntityEquipmentSlot.CHEST);
	}
	
	public static ItemMetalArmor createLeggings(MetalMaterial metal) {
		ArmorMaterial material = cyano.basemetals.init.Materials.getArmorMaterialFor(metal);
		if(material == null) {
			// uh-oh
			FMLLog.severe("Failed to load armor material enum for "+metal);
		}
		return new ItemMetalArmor(metal, material, material.ordinal(), EntityEquipmentSlot.LEGS);
	}

	
	public static ItemMetalArmor createBoots(MetalMaterial metal) {
		ArmorMaterial material = cyano.basemetals.init.Materials.getArmorMaterialFor(metal);
		if(material == null) {
			// uh-oh
			FMLLog.severe("Failed to load armor material enum for "+metal);
		}
		return new ItemMetalArmor(metal, material, material.ordinal(), EntityEquipmentSlot.FEET);
	}

	@Override
	public void onCreated(final ItemStack item, final World world, final EntityPlayer crafter) {
		super.onCreated(item, world, crafter);
		extraEffectsOnCrafting(item, world, crafter);
	}

	public void extraEffectsOnCrafting(final ItemStack item, final World world, final EntityPlayer crafter) {
		//
	}

	/**
     * Return whether this item is repairable in an anvil.
     */
	@Override public boolean getIsRepairable(ItemStack srcItemStack, ItemStack repairMaterial) {
		// repair with string or wool
		List<ItemStack> acceptableItems = OreDictionary.getOres(repairOreDictName);
		for(ItemStack i : acceptableItems ){
			if(ItemStack.areItemsEqual(i, repairMaterial)) return true;
		}
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override public String getArmorTexture(ItemStack stack, Entity e, EntityEquipmentSlot slot, String layer){
		return customTexture;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b){
		super.addInformation(stack, player, list, b);
		MetalToolEffects.addArmorSpecialPropertiesToolTip(metal, list);
	}

	@Override
	public MetalMaterial getMetalMaterial() {
		return metal;
	}
}
