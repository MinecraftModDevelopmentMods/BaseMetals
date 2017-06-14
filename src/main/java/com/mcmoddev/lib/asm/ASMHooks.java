package com.mcmoddev.lib.asm;

import java.io.IOException;

import com.google.common.base.Optional;
import com.mcmoddev.lib.common.item.IHorseArmor;

import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ASMHooks {

	private static final DataSerializer<Optional<ItemStack>> OPTIONAL_ITEM_STACK = new DataSerializer<Optional<ItemStack>>() {
		@Override
        public void write(PacketBuffer buf, Optional<ItemStack> value)
        {
			buf.writeItemStack((ItemStack)value.orNull());
        }
		@Override
        public Optional<ItemStack> read(PacketBuffer buf) throws IOException
        {
            return Optional.<ItemStack>fromNullable(buf.readItemStack());
        }
		@Override
        public DataParameter<Optional<ItemStack>> createKey(int id)
        {
            return new DataParameter<Optional<ItemStack>>(id, this);
        }
    };
    
	public static final DataParameter<Optional<ItemStack>> ARMOR_STACK = EntityDataManager.createKey(EntityHorse.class, OPTIONAL_ITEM_STACK);

	private ASMHooks() {
		throw new IllegalAccessError("Not a instantiable class");
	}

	public static void onInitHorse(EntityHorse entity) {
		entity.getDataManager().register(ASMHooks.ARMOR_STACK, Optional.absent());
	}

	public static void setHorseArmorStack(EntityHorse entity, ItemStack stack) {
		entity.getDataManager().set(ASMHooks.ARMOR_STACK, Optional.fromNullable(stack));
	}

	@SideOnly(Side.CLIENT)
	public static String getTextureName(HorseArmorType type, EntityHorse entity) {
		final ItemStack stack = entity.getDataManager().get(ASMHooks.ARMOR_STACK).orNull();
		if (stack != null && stack.getItem() instanceof IHorseArmor)
			return ((IHorseArmor) stack.getItem()).getArmorTexture(entity, stack);
		return type.getTextureName();
	}
}