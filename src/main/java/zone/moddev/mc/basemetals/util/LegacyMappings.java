package zone.moddev.mc.basemetals.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** Registry names used by the original 1.10 release. */
public final class LegacyMappings {

	private static final Map<String, String> ITEMS;

	static {
		final Map<String, String> items = new LinkedHashMap<>();
		items.put("carbon_powder", "coal_powder");
		items.put("liquid_mercury", "mercury");

		for (final String material : new String[] { "adamantine", "aquarium", "brass", "bronze",
				"coldiron", "copper", "cupronickel", "electrum", "invar", "lead", "mithril",
				"nickel", "platinum", "silver", "starsteel", "steel", "tin" }) {
			items.put(material + "_door_item", material + "_door");
		}

		ITEMS = Collections.unmodifiableMap(items);
	}

	private LegacyMappings() {
	}

	public static String itemReplacement(final String oldPath) {
		return ITEMS.get(oldPath);
	}

	public static Map<String, String> itemReplacements() {
		return ITEMS;
	}
}
