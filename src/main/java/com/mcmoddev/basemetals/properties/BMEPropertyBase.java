package com.mcmoddev.basemetals.properties;

import java.util.Arrays;

import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.properties.MMDMaterialPropertyBase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public abstract class BMEPropertyBase extends MMDMaterialPropertyBase {
	protected static final int EFFECT_DURATION = 45;

	protected static int countArmorPieces(final MMDMaterial mat, final EntityPlayer player) {
		return (int) Arrays.stream(new EntityEquipmentSlot[]{
				EntityEquipmentSlot.HEAD,
				EntityEquipmentSlot.CHEST,
				EntityEquipmentSlot.LEGS,
				EntityEquipmentSlot.FEET
		})
				.map(player::getItemStackFromSlot)
				.filter(stack -> stackIsArmorMaterial(stack, mat))
				.count();
	}

	protected static boolean hasFullSuit(final EntityPlayer player, final String materialName) {
		final MMDMaterial material = Materials.getMaterialByName(materialName);

		return ((player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == material
				.getItem(Names.HELMET))
				&& (player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == material
						.getItem(Names.CHESTPLATE))
				&& (player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() == material
						.getItem(Names.LEGGINGS))
				&& (player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == material
						.getItem(Names.BOOTS)));
	}

	protected static boolean stackIsArmorMaterial(ItemStack stack, MMDMaterial material) {
		return Arrays.stream(new Names[]{Names.HELMET, Names.CHESTPLATE, Names.LEGGINGS, Names.BOOTS})
				.anyMatch(n -> stack.getItem() == material.getItem(n));
	}
}
