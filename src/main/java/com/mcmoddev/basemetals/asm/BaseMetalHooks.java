package com.mcmoddev.basemetals.asm;

import astro.lib.api.item.IHorseArmor;
import com.google.common.base.Optional;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BaseMetalHooks {
	public static final DataParameter<Optional<ItemStack>> ARMOR_STACK = EntityDataManager.createKey(EntityHorse.class, DataSerializers.OPTIONAL_ITEM_STACK);

	public static void onInitHorse(EntityHorse entity) {
		entity.getDataManager().register(BaseMetalHooks.ARMOR_STACK, Optional.absent());
	}

	public static void setHorseArmorStack(EntityHorse entity, ItemStack stack) {
		entity.getDataManager().set(BaseMetalHooks.ARMOR_STACK, Optional.fromNullable(stack));
	}

	@SideOnly(Side.CLIENT)
	public static String getTextureName(HorseArmorType type, EntityHorse entity) {
		final ItemStack stack = entity.getDataManager().get(BaseMetalHooks.ARMOR_STACK).orNull();
		if ((stack != null) && (stack.getItem() instanceof IHorseArmor))
			return ((IHorseArmor) stack.getItem()).getArmorTexture(entity, stack);
		return type.getTextureName();
	}
}