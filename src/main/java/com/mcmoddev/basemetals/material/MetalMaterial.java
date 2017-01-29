package com.mcmoddev.basemetals.material;

import java.util.Locale;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockSlab;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Loader;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class MetalMaterial {

	/**
	 * hardness on a scale from 0 to 10 (or more), where 0 is non-solid and
	 * diamond is 10. For reference, wood is 3, stone is 5, iron is 8, diamond
	 * is 10.
	 */
	public final float hardness;

	/**
	 * durability on a scale from 0 to 10 (or more). For reference, leather is
	 * 2.5, gold is 3, wood is 2, stone is 4, iron is 8, minecraft diamond is
	 * 10.
	 */
	public final float strength;

	/**
	 * Scale from 0 to 10 (or more) on how magical the material is. For
	 * reference, stone is 2, iron is 4.5, diamond is 4, wood is 6, gold is 10.
	 */
	public final float magicAffinity;

	/**
	 * Whether things made from this material can regenerate
	 */
	public boolean regenerates = false;

	/**
	 * Color Info for metal
	 */
	public final int tintColor;
	
	/**
	 * Rare metals, like platinum, are never found in villager trades and
	 * unusually uncommon in world generation
	 */
	public final boolean isRare;

	/**
	 * Does this Material have an Ore?
	 * May go away
	 */
	public final boolean hasOre;
	/**
	 * Does this Material have a blend?
	 * May go away
	 */
	public final boolean hasBlend;

	/**
	 * Set for Vanilla Materials
	 * May go away
	 */
	public boolean isVanilla = false;

	/**
	 * Set for Materials which only have the basic things.
	 * May go away
	 */
	public boolean isBasic = false;

	/**
	 * ENUM of all the types of Materials
	 * 
	 * @author Jasmine Iwanek
	 *
	 */
	public enum MaterialType {
	    WOOD, ROCK, METAL, MINERAL, GEM
	}
	
	/**
	 * The type of material this is
	 */
	public MaterialType materialType;

	/**
	 * String used to identify items and blocks using this material
	 */
	final String identifier;

	final String titleName;

	private final String enumName;

	private int[] cache = null;

	/**
	 * The resistance the material has against explosions.
	 */
	private float blastResistance;

	/**
	 * The base attack damage for the material.
	 */
	private float baseDamage;

	@Nullable
	public Item arrow;
	@Nullable
	public Item axe;
	@Nullable
	public Item blend;
	@Nullable
	public Item boots;
	@Nullable
	public Item bolt;
	@Nullable
	public Item bow;
	@Nullable
	public Item chestplate;
	@Nullable
	public Item crackhammer;
	@Nullable
	public Item crossbow;
	@Nullable
	public Item door;
	@Nullable
	public Item fishing_rod;
	@Nullable
	public Item gear;
	@Nullable
	public Item helmet;
	@Nullable
	public Item hoe;
	@Nullable
	public Item horse_armor;
	@Nullable
	public Item ingot;
	@Nullable
	public Item leggings;
	@Nullable
	public Item nugget;
	@Nullable
	public Item pickaxe;
	@Nullable
	public Item powder;
	@Nullable
	public Item rod;
	@Nullable
	public Item shears;
	@Nullable
	public Item shield;
	@Nullable
	public Item shovel;
	@Nullable
	public Item slab;
	@Nullable
	public Item smallblend;
	@Nullable
	public Item smallpowder;
	@Nullable
	public Item sword;

	@Nullable
	public Block bars;
	@Nullable
	public Block block;
	@Nullable
	public Block button;
	@Nullable
	public BlockDoor doorBlock;
	@Nullable
	public BlockSlab double_slab;
	@Nullable
	public BlockSlab half_slab;
	@Nullable
	public Block lever;
	@Nullable
	public Block ore;
	@Nullable
	public Block plate;
	@Nullable
	public Block pressure_plate;
	@Nullable
	public Block stairs;
	@Nullable
	public Block trapdoor;
	@Nullable
	public Block wall;

	@Nullable
	public Fluid fluid;
	@Nullable
	public BlockFluidBase fluidBlock;
	
	@Nullable
	public Item crystal;
	@Nullable
	public Item shard;
	@Nullable
	public Item clump;
	@Nullable
	public Item powder_dirty;

	@Nullable
	public Item dense_plate;
	@Nullable
	public Item crushed;
	@Nullable
	public Item crushed_purified;

	/**
	 * @deprecated
	 * @param name
	 *            String used to identify items and blocks using this material
	 * @param hardness
	 *            hardness on a scale from 0 to 10 (or more), where 0 is
	 *            non-solid and diamond is 10. For reference, wood is 3, stone
	 *            is 5, iron is 8, diamond is 10. Used for damage, armor
	 *            protection, and tool effectiveness calculations
	 * @param strength
	 *            durability on a scale from 0 to 10 (or more). For reference,
	 *            leather is 2.5, gold is 3, wood is 2, stone is 4, iron is 8,
	 *            minecraft diamond is 10. Used for item durability calculations
	 *            and blast resistance
	 * @param magic
	 *            Scale from 0 to 10 (or more) on how magical the material is.
	 *            For reference, stone is 2, iron is 4.5, diamond is 4, wood is
	 *            6, gold is 10. Used to calculate enchantibility
	 */
	@Deprecated
	public MetalMaterial(String name, float hardness, float strength, float magic) {
		this(name, hardness, strength, magic, 0x00000000);
	}

	/**
	 * @param name
	 *            String used to identify items and blocks using this material
	 * @param hardness
	 *            hardness on a scale from 0 to 10 (or more), where 0 is
	 *            non-solid and diamond is 10. For reference, wood is 3, stone
	 *            is 5, iron is 8, diamond is 10. Used for damage, armor
	 *            protection, and tool effectiveness calculations
	 * @param strength
	 *            durability on a scale from 0 to 10 (or more). For reference,
	 *            leather is 2.5, gold is 3, wood is 2, stone is 4, iron is 8,
	 *            minecraft diamond is 10. Used for item durability calculations
	 *            and blast resistance
	 * @param magic
	 *            Scale from 0 to 10 (or more) on how magical the material is.
	 *            For reference, stone is 2, iron is 4.5, diamond is 4, wood is
	 *            6, gold is 10. Used to calculate enchantibility
	 * @param tintColor
	 *            Color Info for the metal
	 * 
	 * @deprecated
	 */
	@Deprecated
	public MetalMaterial(String name, float hardness, float strength, float magic, int tintColor) {
		this(name, hardness, strength, magic, tintColor, false, true, true);
	}

	public MetalMaterial(String name, float hardness, float strength, float magic, int tintColor, boolean hasOre, boolean hasBlend) {
		this(name, hardness, strength, magic, tintColor, false, hasOre, hasBlend);
	}
	/**
	 * @deprecated
	 * @param name
	 *            String used to identify items and blocks using this material
	 * @param hardness
	 *            hardness on a scale from 0 to 10 (or more), where 0 is
	 *            non-solid and diamond is 10. For reference, wood is 3, stone
	 *            is 5, iron is 8, diamond is 10. Used for damage, armor
	 *            protection, and tool effectiveness calculations
	 * @param strength
	 *            durability on a scale from 0 to 10 (or more). For reference,
	 *            leather is 2.5, gold is 3, wood is 2, stone is 4, iron is 8,
	 *            minecraft diamond is 10. Used for item durability calculations
	 *            and blast resistance
	 * @param magic
	 *            Scale from 0 to 10 (or more) on how magical the material is.
	 *            For reference, stone is 2, iron is 4.5, diamond is 4, wood is
	 *            6, gold is 10. Used to calculate enchantibility
	 * @param isRare
	 *            If true, this metal is designated as an extremely rare metal
	 */
	@Deprecated
	public MetalMaterial(String name, float hardness, float strength, float magic, boolean isRare) {
		this(name, hardness, strength, magic, 0x00000000, false, true, true);
	}

	@Deprecated
	public MetalMaterial(String name, float hardness, float strength, float magic, int tintColor, boolean isRare) {
		this(name, hardness, strength, magic, tintColor, false, true, true);
	}

	/**
	 * @param name
	 *            String used to identify items and blocks using this material
	 * @param hardness
	 *            hardness on a scale from 0 to 10 (or more), where 0 is
	 *            non-solid and diamond is 10. For reference, wood is 3, stone
	 *            is 5, iron is 8, diamond is 10. Used for damage, armor
	 *            protection, and tool effectiveness calculations
	 * @param strength
	 *            durability on a scale from 0 to 10 (or more). For reference,
	 *            leather is 2.5, gold is 3, wood is 2, stone is 4, iron is 8,
	 *            minecraft diamond is 10. Used for item durability calculations
	 *            and blast resistance
	 * @param magic
	 *            Scale from 0 to 10 (or more) on how magical the material is.
	 *            For reference, stone is 2, iron is 4.5, diamond is 4, wood is
	 *            6, gold is 10. Used to calculate enchantibility
	 * @param tintColor
	 *            Color Info for the metal
	 * @param isRare
	 *            If true, this metal is designated as an extremely rare metal
	 * @param hasOre
	 *            If true this material has an ore
	 * @param hasBlend
	 *            If true this material has a blend
	 */
	public MetalMaterial(String name, float hardness, float strength, float magic, int tintColor, boolean isRare, boolean hasOre, boolean hasBlend) {
		this.hardness = hardness;
		this.strength = strength;
		this.magicAffinity = magic;
		this.tintColor = tintColor;
		this.identifier = name;
		this.titleName = StringUtils.capitalize(name);
		this.enumName = (Loader.instance().activeModContainer().getModId() + "_" + name).toUpperCase(Locale.ENGLISH);
		this.hasBlend = hasBlend;
		this.hasOre = hasOre;
		this.isRare = isRare;
		this.blastResistance = 2.5f * this.strength;
		this.baseDamage = this.round(0.25f * this.hardness, 1);
	}

	public String getName() {
		return this.identifier;
	}

	public String getCapitalizedName() {
		return this.titleName;
	}

	@Override
	public String toString() {
		return this.getName();
	}

	@Override
	public int hashCode() {
		return this.identifier.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if ((o.hashCode() == this.hashCode()) && (o instanceof MetalMaterial)) {
			final MetalMaterial other = (MetalMaterial) o;
			return this.identifier.equals(other.identifier);
		}
		return false;
	}

	/**
	 * Gets the amount of XP per ore block that is smelted
	 * 
	 * @return XP value per ore block
	 */
	public float getOreSmeltXP() {
		return 0.1f * this.magicAffinity;
	}

	/**
	 * Gets the tool harvest level
	 * 
	 * @return an integer from -1 (equivalent to no tool) to 3 (diamond tool
	 *         equivalent)
	 */
	public int getToolHarvestLevel() {
		return (int) (this.hardness / 3f);
	}

	/**
	 * Gets the tool harvest level needed from a tool trying to mine this
	 * metal's ore and other blocks
	 * 
	 * @return an integer from -1 (equivalent to no tool) to 3 (diamond tool
	 *         equivalent)
	 */
	public int getRequiredHarvestLevel() {
		return (int) clamp((0.9f * this.hardness) / 3f, -1, 3);
	}

	static int clamp(int x, int min, int max) {
		if (x < min)
			return min;
		if (x > max)
			return max;
		return x;
	}

	static float clamp(float x, float min, float max) {
		if (x < min)
			return min;
		if (x > max)
			return max;
		return x;
	}

	static double clamp(double x, double min, double max) {
		if (x < min)
			return min;
		if (x > max)
			return max;
		return x;
	}

	/**
	 * Gets the resistance of blocks made from this metal to explosions
	 * 
	 * @return the blast resistance score
	 */
	public float getBlastResistance() {
		return this.blastResistance;
	}

	/**
	 * Gets the number used to determine how quickly a block is mined with a
	 * tool made from this material
	 * 
	 * @return the number used to determine how quickly a block is mined
	 */
	public float getToolEfficiency() {
		return this.hardness;
	}

	/**
	 * Gets the hardness of the ore block for this material
	 * 
	 * @return the hardness of the ore block for this material
	 */
	public float getOreBlockHardness() {
		return 0.5f * this.hardness;
	}

	/**
	 * Gets the hardness for blocks made from this material
	 * 
	 * @return the hardness for blocks made from this material
	 */
	public float getMetalBlockHardness() {
		return 2.0f * this.hardness;
	}

	/**
	 * Gets the number of uses of a tool made from this material
	 * 
	 * @return The number of uses of a tool made from this material
	 */
	public int getToolDurability() {
		return (int) (32 * this.strength);
	}

	/**
	 * Gets the number used to determine how much damage an armor item can take.
	 * 
	 * @return The number used to determine how much damage an armor item can
	 *         take.
	 */
	public int getArmorMaxDamageFactor() {
		return (int) (2.0f * this.strength);
	}

	/**
	 * Gets the protection value for helmets, chestplates, leg armor, and boots
	 * made from this material
	 * 
	 * @return the protection value for helmets, chestplates, leg armor, and
	 *         boots made from this material
	 */
	public int[] getDamageReductionArray() {
		if (this.cache == null) {
			final float minimum = 5f; // most metals should be better than
										// leather armor
			final float hardnessFactor = 1.25f;
			final float total = (hardnessFactor * this.hardness) + minimum;
			this.cache = new int[4];
			final int feetIndex = EntityEquipmentSlot.FEET.getIndex();
			final int legsIndex = EntityEquipmentSlot.LEGS.getIndex();
			final int chestIndex = EntityEquipmentSlot.CHEST.getIndex();
			final int headIndex = EntityEquipmentSlot.HEAD.getIndex();
			this.cache[headIndex] = Math.round(0.1f * total);// head
			this.cache[chestIndex] = Math.round(0.4f * total);// torso
			this.cache[legsIndex] = Math.round(0.35f * total);// legs
			this.cache[feetIndex] = Math.round(0.15f * total);// feet
		}
		return this.cache;
	}

	/**
	 * Gets the base damage from attacks with tools made from this material
	 * 
	 * @return the base damage from attacks with tools made from this material
	 */
	public float getBaseAttackDamage() {
		return this.baseDamage;
	}

	private float round(float number, int numDecimalPlaces) {
		int x = 1;
		for (int i = 0; i < numDecimalPlaces; i++)
			x *= 10;
		return (float) Math.round(number * x) / (float) x;
	}

	/**
	 * Gets the enchantibility score for this material
	 * 
	 * @return the enchantibility score for this material
	 */
	public int getEnchantability() {
		return (int) (2.5f * this.magicAffinity);
	}

	public String getEnumName() {
		return this.enumName;
	}
	
	/**
	 * Gets the tint color for this material
	 * 
	 * @return the tint color for this material
	 */
	public int getTintColor() {
		return this.tintColor;
	}

	/**
	 * Sets the blast resistance of the material. Should only be used as a
	 * builder method.
	 * 
	 * @param resistance
	 *            The resistance for the material.
	 * @return An instance of the material, for quality of life.
	 */
	public MetalMaterial setBlastResistance(float resistance) {
		this.blastResistance = resistance;
		return this;
	}

	/**
	 * Sets the base weapon damage for the material. Should only be used as a
	 * builder method.
	 * 
	 * @param damage
	 *            The base damage of the material.
	 * @return An instance of the material, for quality of life.
	 */
	public MetalMaterial setBaseDamage(float damage) {
		this.baseDamage = damage;
		return this;
	}
}
