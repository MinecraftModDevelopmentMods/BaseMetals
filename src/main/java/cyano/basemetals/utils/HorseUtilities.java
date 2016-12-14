package cyano.basemetals.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.entity.passive.HorseType;
import net.minecraftforge.common.util.EnumHelper;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class HorseUtilities {

	private static Map<String, HorseArmorType> TYPES = new HashMap<>();

	public static HorseArmorType getType(String name, int protection, String hash) {
		name = name.toUpperCase(Locale.ENGLISH);

		if (HorseUtilities.TYPES.containsKey(name))
			return HorseUtilities.TYPES.get(name);

		final HorseArmorType type = EnumHelper.addEnum(HorseArmorType.class, name, new Class[]{int.class, String.class, String.class}, protection, name.toLowerCase(), hash);
		HorseUtilities.TYPES.put(name, type);

		return type;
	}
}