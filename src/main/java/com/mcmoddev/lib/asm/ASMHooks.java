package com.mcmoddev.lib.asm;

import com.google.common.base.Optional;
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

	public static final DataParameter<Optional<ItemStack>> ARMOR_STACK = EntityDataManager
			.createKey(EntityHorse.class, DataSerializers.OPTIONAL_ITEM_STACK);

	private ASMHooks() {
		throw new IllegalAccessError("Not a instantiable class");
	}

	public static void onInitHorse(final EntityHorse entity) {
		entity.getDataManager().register(ASMHooks.ARMOR_STACK, Optional.absent());
	}

	public static void setHorseArmorStack(final EntityHorse entity, final ItemStack stack) {
		entity.getDataManager().set(ASMHooks.ARMOR_STACK, Optional.fromNullable(stack));
	}

	@SideOnly(Side.CLIENT)
	public static String getTextureName(final HorseArmorType type, final EntityHorse entity) {
		final ItemStack stack = entity.getDataManager().get(ASMHooks.ARMOR_STACK).orNull();
		if ((stack != null) && (stack.getItem() instanceof IHorseArmor)) {
			return ((IHorseArmor) stack.getItem()).getHorseArmorTexture(entity, stack);
		}
		return type.getTextureName();
	}
}
