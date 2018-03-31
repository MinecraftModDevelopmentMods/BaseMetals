package com.mcmoddev.lib.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Nonnull;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.items.MMDToolEffects;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Armor.
 *
 * @author DrCyano
 *
 */
public class ItemMMDArmor extends net.minecraft.item.ItemArmor implements IMMDObject {

	private final String customTexture;
	private final MMDMaterial mmdMaterial;

	private static final int UPDATE_INTERVAL = 11;
	private static final Map<EntityPlayer, AtomicLong> playerUpdateTimestampMap = new HashMap<>();
	private static final Map<EntityPlayer, AtomicInteger> playerUpdateCountMap = new HashMap<>();

	protected ItemMMDArmor(@Nonnull final MMDMaterial material,
			@Nonnull final ArmorMaterial armorMat, final int renderIndex,
			final EntityEquipmentSlot slot) {
		super(armorMat, renderIndex, slot);
		this.mmdMaterial = material;
		this.customTexture = Loader.instance().activeModContainer().getModId()
				+ ":textures/models/armor/" + material.getName() + "_layer_"
				+ (slot == EntityEquipmentSlot.LEGS ? 2 : 1) + ".png";
	}

	@Override
	public void onArmorTick(final World w, final EntityPlayer player, final ItemStack armor) {
		if (!(playerUpdateTimestampMap.containsKey(player))) {
			playerUpdateTimestampMap.put(player, new AtomicLong(0));
			playerUpdateCountMap.put(player, new AtomicInteger(0));
			return;
		}
		if (!w.isRemote && (w.getTotalWorldTime() > playerUpdateTimestampMap.get(player).get())) {
			playerUpdateTimestampMap.get(player).set(w.getTotalWorldTime() + UPDATE_INTERVAL);
			final int updateCount = playerUpdateCountMap.get(player).getAndIncrement();
			for (int i = 0; i < 4; i++) {
				final ItemStack armorItemStack = player.inventory.armorInventory.get(i);
				if ((!armorItemStack.isEmpty())
						&& (armorItemStack.getItem() instanceof ItemMMDArmor)) {
					MMDToolEffects.extraEffectsOnArmorUpdate(w, player, this.mmdMaterial,
							armorItemStack, updateCount);
				}
			}
		}
	}

	public static ItemMMDArmor createArmor(@Nonnull final MMDMaterial material, final Names name) {
		EntityEquipmentSlot slot = null;
		switch (name) {
			case HELMET:
				slot = EntityEquipmentSlot.HEAD;
				break;
			case CHESTPLATE:
				slot = EntityEquipmentSlot.CHEST;
				break;
			case LEGGINGS:
				slot = EntityEquipmentSlot.LEGS;
				break;
			case BOOTS:
				slot = EntityEquipmentSlot.FEET;
				break;
			default:
		}

		return createArmorBase(material, slot);
	}

	/**
	 *
	 * @param material
	 *            The material to make the armor from
	 * @param slot
	 *            Type of armor
	 * @return The armor
	 */
	protected static ItemMMDArmor createArmorBase(@Nonnull final MMDMaterial material,
			final EntityEquipmentSlot slot) {
		final ArmorMaterial amaterial = Materials.getArmorMaterialFor(material);
		if (amaterial == null) {
			// uh-oh
			BaseMetals.logger.error("Failed to load armor material enum for " + material);
			return null;
		}
		return new ItemMMDArmor(material, amaterial, amaterial.ordinal(), slot);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public String getArmorTexture(final ItemStack stack, final Entity e,
			final EntityEquipmentSlot slot, final String layer) {
		return this.customTexture;
	}

	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer player,
			final List<String> tooltip, final boolean b) {
		super.addInformation(stack, player, tooltip, b);
		MMDToolEffects.addArmorSpecialPropertiesToolTip(this.mmdMaterial.getName(), tooltip);
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
	public void extraEffectsOnCrafting(final ItemStack item, final World world,
			final EntityPlayer crafter) {
		//
	}

	/**
	 * @return whether this item is repairable in an anvil.
	 */
	@Override
	public boolean getIsRepairable(final ItemStack srcItemStack, final ItemStack repairMaterial) {
		// repair with string or wool
		return MMDItemHelper.isToolRepairable(repairMaterial,
				this.mmdMaterial.getCapitalizedName());
	}

	@Override
	public void onCreated(final ItemStack item, final World world, final EntityPlayer crafter) {
		super.onCreated(item, world, crafter);
		this.extraEffectsOnCrafting(item, world, crafter);
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.mmdMaterial;
	}
}
