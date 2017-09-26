package com.mcmoddev.lib.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Nonnull;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.items.MMDToolEffects;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
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


	private final String customTexture;
	private final MMDMaterial mmd_material;
	private final String repairOreDictName;

	private static final int UPDATE_INTERVAL = 11;
	private static final Map<EntityPlayer, AtomicLong> playerUpdateTimestampMap = new HashMap<>();
	private static final Map<EntityPlayer, AtomicInteger> playerUpdateCountMap = new HashMap<>();

	protected ItemMMDArmor(@Nonnull MMDMaterial material, @Nonnull ArmorMaterial armorMat, int renderIndex,
							 EntityEquipmentSlot slot) {
		super(armorMat, renderIndex, slot);
		this.mmd_material = material;
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
				if(player.inventory.armorInventory.get(i) != null && player.inventory.armorInventory.get(i).getItem() instanceof ItemMMDArmor) {
					MMDToolEffects.extraEffectsOnArmorUpdate(w, player, this.mmd_material, player.inventory.armorInventory.get(i), updateCount);
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
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		MMDToolEffects.addArmorSpecialPropertiesToolTip(this.mmd_material, tooltip);
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
		return this.mmd_material;
	}
}
