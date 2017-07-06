package com.mcmoddev.lib.data;

import java.util.TreeMap;

import javax.annotation.Nonnull;

import com.google.common.collect.Maps;

import net.minecraft.util.IStringSerializable;

public enum MaterialStats implements IStringSerializable {
	/**
	 * hardness on a scale from 0 to 10 (or more), where 0 is non-solid and
	 * diamond is 10. For reference, wood is 3, stone is 5, iron is 8, diamond
	 * is 10.
	 */
	HARDNESS,
	/**
	 * durability on a scale from 0 to 10 (or more). For reference, leather is
	 * 2.5, gold is 3, wood is 2, stone is 4, iron is 8, minecraft diamond is
	 * 10.
	 */
	STRENGTH,
	/**
	 * Scale from 0 to 10 (or more) on how magical the material is. For
	 * reference, stone is 2, iron is 4.5, diamond is 4, wood is 6, gold is 10.
	 */
	MAGICAFFINITY,
	/**
	 * The resistance the material has against explosions.
	 */
	BLASTRESISTANCE,
	/**
	 * The base attack damage for the material.
	 */
	BASEDAMAGE;

	protected static final TreeMap<Integer, MaterialStats> MAP = Maps.newTreeMap();

	static {
		for (MaterialStats stat : values()) {
			MAP.put(stat.ordinal(), stat);
		}
	}

	@Override
	public String getName() {
		return name().toLowerCase();
	}

	public static MaterialStats getType(@Nonnull int ordinal) {
		if ((ordinal > values().length) || (ordinal < 0)) {
			ordinal = 0;
		}
		return values()[ordinal];
	}

	@Override
	public String toString() {
		return getName();
	}
}
