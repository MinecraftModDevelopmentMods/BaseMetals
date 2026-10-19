package com.mcmoddev.basemetals;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.util.datafix.FixTypes;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.fml.common.FMLCommonHandler;

/** Historical registry names whose equivalent content is still available. */
public final class LegacyMappings {

	private static final Map<String, String> ITEMS = itemAliases();

	private LegacyMappings() {
	}

	/** Registers raw-stack recovery before chunks, players and entities are deserialized. */
	public static void registerDataFixer() {
		ModFixs fixes = FMLCommonHandler.instance().getDataFixer().init(BaseMetals.MODID, 1);
		LegacyStackFix fix = new LegacyStackFix();
		for (FixTypes type : new FixTypes[] { FixTypes.CHUNK, FixTypes.PLAYER, FixTypes.ENTITY,
				FixTypes.BLOCK_ENTITY, FixTypes.ITEM_INSTANCE }) fixes.registerFix(type, fix);
	}

	/** Returns the current item name, or null for an unrelated registry entry. */
	public static String itemTarget(final String name) {
		return ITEMS.get(name);
	}

	/** Returns the current fluid-block name, or null for an unrelated entry. */
	public static String blockTarget(final String name) {
		return "basemetals:liquid_mercury".equals(name) ? "basemetals:mercury" : null;
	}

	private static Map<String, String> itemAliases() {
		final Map<String, String> aliases = new LinkedHashMap<>();
		aliases.put("basemetals:liquid_mercury", "basemetals:mercury");
		aliases.put("basemetals:carbon_powder", "basemetals:coal_powder");
		aliases.put("basemetals:iron_nugget", "minecraft:iron_nugget");
		for (String material : new String[] { "adamantine", "aquarium", "brass", "bronze",
				"coldiron", "copper", "cupronickel", "electrum", "invar", "lead", "mithril",
				"nickel", "platinum", "silver", "starsteel", "steel", "tin" }) {
			aliases.put("basemetals:" + material + "_door_item", "basemetals:" + material + "_door");
		}
		return Collections.unmodifiableMap(aliases);
	}
}
