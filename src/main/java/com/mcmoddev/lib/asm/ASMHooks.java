package com.mcmoddev.lib.asm;

import com.mcmoddev.lib.common.item.IHorseArmor;

import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ASMHooks {

	public static final DataParameter<ItemStack> ARMOR_STACK = EntityDataManager.createKey(EntityHorse.class,
			DataSerializers.ITEM_STACK);

	private ASMHooks() {
		throw new IllegalAccessError("Not a instantiable class");
	}

	public static void onInitHorse(EntityHorse entity) {
		entity.getDataManager().register(ASMHooks.ARMOR_STACK, ItemStack.EMPTY);
	}

	public static void setHorseArmorStack(EntityHorse entity, ItemStack stack) {
		entity.getDataManager().set(ASMHooks.ARMOR_STACK, stack);
	}

	@SideOnly(Side.CLIENT)
	public static String getTextureName(HorseArmorType type, EntityHorse entity) {
		final ItemStack stack = entity.getDataManager().get(ASMHooks.ARMOR_STACK);
		if (!stack.isEmpty() && stack.getItem() instanceof IHorseArmor)
			return ((IHorseArmor) stack.getItem()).getArmorTexture(entity, stack);
		return type.getTextureName();
	}
}
