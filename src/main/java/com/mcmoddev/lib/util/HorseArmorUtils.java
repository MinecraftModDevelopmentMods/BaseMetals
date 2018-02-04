package com.mcmoddev.lib.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mcmoddev.lib.data.SharedStrings;

import net.minecraft.entity.passive.HorseArmorType;
import net.minecraftforge.common.util.EnumHelper;

@Nullable
public class HorseArmorUtils {

	private static final Map<String, HorseArmorType> TYPES = new HashMap<>();

	private HorseArmorUtils() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	public static HorseArmorType getArmorType(int protection, @Nonnull String lowerName, @Nonnull String hash) {
		final String name = lowerName.toUpperCase(Locale.ENGLISH);

		if (HorseArmorUtils.TYPES.containsKey(name))
			return HorseArmorUtils.TYPES.get(name);

		final HorseArmorType type = EnumHelper.addEnum(HorseArmorType.class, name.replace(" ", "_"), new Class[] { int.class, String.class, String.class }, protection, name.toLowerCase(), hash);
		HorseArmorUtils.TYPES.put(name, type);

		return type;
	}
}
