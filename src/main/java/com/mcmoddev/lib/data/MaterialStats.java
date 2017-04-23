package com.mcmoddev.lib.data;

public final class MaterialStats {
	/**
	 * hardness on a scale from 0 to 10 (or more), where 0 is non-solid and
	 * diamond is 10. For reference, wood is 3, stone is 5, iron is 8, diamond
	 * is 10.
	 */
	public static final String HARDNESS        = "hardness";
	/**
	 * durability on a scale from 0 to 10 (or more). For reference, leather is
	 * 2.5, gold is 3, wood is 2, stone is 4, iron is 8, minecraft diamond is
	 * 10.
	 */
	public static final String STRENGTH        = "strength";
	/**
	 * Scale from 0 to 10 (or more) on how magical the material is. For
	 * reference, stone is 2, iron is 4.5, diamond is 4, wood is 6, gold is 10.
	 */
	public static final String MAGICAFFINITY   = "magicAffinity";
	/**
	 * The resistance the material has against explosions.
	 */
	public static final String BLASTRESISTANCE = "blastResistance";
	/**
	 * The base attack damage for the material.
	 */
	public static final String BASEDAMAGE      = "baseDamage";
}
