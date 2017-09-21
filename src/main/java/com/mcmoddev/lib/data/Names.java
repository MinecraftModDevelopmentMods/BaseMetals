package com.mcmoddev.lib.data;

import java.util.Locale;
import java.util.TreeMap;

import javax.annotation.Nonnull;

import com.google.common.collect.Maps;

import net.minecraft.util.IStringSerializable;

public enum Names implements IStringSerializable {
	// Items
	ARROW, AXE, BLEND, BOOTS, BOLT, BOW, CHESTPLATE, CRACKHAMMER, CROSSBOW, FISHING_ROD, GEAR, HELMET, HOE, HORSE_ARMOR, INGOT, LEGGINGS, NUGGET, PICKAXE, POWDER, ROD, SHEARS, SHIELD, SHOVEL, SMALLBLEND, SMALLPOWDER, SWORD, GEM,

	// Blocks
	BARS, BLOCK, BOOKSHELF, BUTTON, DOUBLE_SLAB, FLOWER_POT, LADDER, LEVER, PLATE, PRESSURE_PLATE, STAIRS, TRAPDOOR, TRIPWIRE_HOOK, WALL, FENCE, FENCE_GATE,

	// ItemBlocks
	ANVIL, DOOR, SLAB,

	// Mekanism
	CRYSTAL, SHARD, CLUMP, POWDER_DIRTY,

	// IC2
	CASING, DENSE_PLATE, CRUSHED, CRUSHED_PURIFIED,

	// Ores
	ORE, ENDORE, NETHERORE, MEK_CRYSTAL;

	protected static final TreeMap<Integer, Names> MAP = Maps.newTreeMap();

	static {
		for (Names name : values()) {
			MAP.put(name.ordinal(), name);
		}
	}

	@Override
	public String getName() {
		return this.name();
	}

	public static Names getType(@Nonnull int ordinal) {
		if ((ordinal > values().length) || (ordinal < 0)) {
			ordinal = 0;
		}
		return values()[ordinal];
	}

	@Override
	public String toString() {
		return this.getName().toLowerCase(Locale.ROOT);
	}
}
