package cyano.basemetals.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.minecraft.entity.passive.HorseType;
import net.minecraftforge.common.util.EnumHelper;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class HorseUtilities {

	private static Map<String, HorseType> TYPES = new HashMap<>();

	/**
	 *
	 * @param name
	 * @param protection
	 * @return
	 */
	public static HorseType getType(String name, int protection) {
		name = name.toUpperCase(Locale.ENGLISH);

		if (HorseUtilities.TYPES.containsKey(name))
			return HorseUtilities.TYPES.get(name);

		final HorseType type = null; // EnumHelper.addEnum(new Class[][]{{HorseType.class, int.class}}, HorseType.class, name, protection);
		HorseUtilities.TYPES.put(name, type);

		return type;
	}
}
