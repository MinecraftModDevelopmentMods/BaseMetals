package com.mcmoddev.basemetals.content;

import java.util.Locale;

/** Form families used by the content policy. Registry identities are never removed. */
public enum MaterialForm {
	ARROW,
	AXE,
	BLEND,
	BOOTS,
	BOLT,
	BOW,
	CHESTPLATE,
	CRACKHAMMER,
	CROSSBOW,
	FISHING_ROD,
	GEAR,
	HELMET,
	HOE,
	HORSE_ARMOR,
	INGOT,
	LEGGINGS,
	NUGGET,
	PICKAXE,
	POWDER,
	ROD,
	SCYTHE,
	SHEARS,
	SHIELD,
	SHOVEL,
	SMALLBLEND,
	SMALLPOWDER,
	SWORD,
	CASING,
	CLUMP,
	CRUSHED,
	CRUSHED_PURIFIED,
	CRYSTAL,
	DENSE_PLATE,
	DIRTY_POWDER,
	SHARD,
	ANVIL,
	BARS,
	BLOCK,
	BOOKSHELF,
	BUTTON,
	DOOR,
	DOUBLE_SLAB,
	FENCE,
	FENCE_GATE,
	FLOWER_POT,
	LADDER,
	LEVER,
	PLATE,
	PRESSURE_PLATE,
	SLAB,
	STAIRS,
	TRAPDOOR,
	TRIPWIRE_HOOK,
	WALL,
	ORE,
	FLUID,
	OTHER;

	/** Resolves the conventional suffix used by Base Metals and MMDLib registry IDs. */
	public static MaterialForm fromRegistryPath(final String registryPath) {
		if (registryPath == null) {
			return OTHER;
		}
		final String path = registryPath.toLowerCase(Locale.ROOT);
		MaterialForm match = OTHER;
		int longestMatch = -1;
		for (final MaterialForm form : values()) {
			if (form == OTHER || form == FLUID) {
				continue;
			}
			final String suffix = registrySuffix(form);
			if (path.endsWith("_" + suffix) && suffix.length() > longestMatch) {
				match = form;
				longestMatch = suffix.length();
			}
		}
		return match;
	}

	private static String registrySuffix(final MaterialForm form) {
		if (form == DIRTY_POWDER) {
			return "powder_dirty";
		}
		return form.name().toLowerCase(Locale.ROOT);
	}
}
