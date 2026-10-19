package com.mcmoddev.basemetals;

import java.util.function.Predicate;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.IFixableData;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/** Recovers named item stacks even when their chunk was not opened on the first upgrade. */
final class LegacyStackFix implements IFixableData {

	private final Predicate<String> available;

	LegacyStackFix() {
		this(name -> ForgeRegistries.ITEMS.containsKey(new ResourceLocation(name)));
	}

	LegacyStackFix(final Predicate<String> available) {
		this.available = available;
	}

	@Override
	public int getFixVersion() {
		return 1;
	}

	@Override
	public NBTTagCompound fixTagCompound(final NBTTagCompound compound) {
		rewrite(compound);
		return compound;
	}

	private void rewrite(final NBTBase value) {
		if (value instanceof NBTTagCompound) {
			NBTTagCompound compound = (NBTTagCompound) value;
			if (compound.hasKey("id", 8) && compound.hasKey("Count", 99)) {
				String target = LegacyMappings.itemTarget(compound.getString("id"));
				if (target != null && available.test(target)) compound.setString("id", target);
			}
			for (String key : compound.getKeySet()) rewrite(compound.getTag(key));
		} else if (value instanceof NBTTagList) {
			for (NBTBase child : (NBTTagList) value) rewrite(child);
		}
	}
}
