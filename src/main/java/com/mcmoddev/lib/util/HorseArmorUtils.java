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
public final class HorseArmorUtils {

	private static final Map<String, HorseArmorType> TYPES = new HashMap<>();

	private HorseArmorUtils() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/**
	 *
	 * @param protection
	 * @param lowerName
	 * @param hash
	 * @return
	 */
	public static HorseArmorType getArmorType(final int protection, @Nonnull final String lowerName,
			@Nonnull final String hash) {
		final String name = lowerName.toUpperCase(Locale.ROOT);

		if (HorseArmorUtils.TYPES.containsKey(name)) {
			return HorseArmorUtils.TYPES.get(name);
		}

		final HorseArmorType type = EnumHelper.addEnum(HorseArmorType.class, name.replace(" ", "_"),
				new Class[] { int.class, String.class, String.class }, protection,
				name.toLowerCase(Locale.ROOT), hash);
		HorseArmorUtils.TYPES.put(name, type);

		return type;
	}
}
