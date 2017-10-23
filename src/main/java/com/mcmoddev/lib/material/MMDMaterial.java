package com.mcmoddev.lib.material;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableList;
import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.data.MaterialStats;
import com.mcmoddev.lib.data.Names;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class MMDMaterial extends IForgeRegistryEntry.Impl<MMDMaterial> {

	/*
	 * data storage - flexible, somewhat important stuff in this part
	 */
	/**
	 * Storage for all "Item" type forms for this material
	 */
	private Map<String, Item> items = new ConcurrentHashMap<>();

	/**
	 * Storage for all "Block" type forms for this material
	 */
	private Map<String, Block> blocks = new ConcurrentHashMap<>();

	/**
	 * If this material has a fluid, it is stored here
	 */

	private Fluid fluid;
	private BlockFluidClassic fluidBlock;

	/*
	 * Material Statistics - see com.mcmoddev.lib.data.MaterialStats for current
	 * known and used ones
	 */
	private Map<MaterialStats, Float> stats = new TreeMap<>();

	/*
	 * Various material flags
	 */
	/**
	 * Whether things made from this material can regenerate
	 */
	private boolean regenerates = false;

	/**
	 * Rare metals, like platinum, are never found in villager trades and
	 * unusually uncommon in world generation
	 */
	private final boolean isRare;

	/**
	 * Whether or not this material has a blend that can be smelted to produce
	 * it
	 */
	private final boolean hasBlend;

	/**
	 * Is this block meant to have an Ore ?
	 */
	private final boolean hasOre;

	/*
	 * Miscelaneous Material Information
	 */
	/**
	 * Color Info for metal
	 */
	private final int tintColor;

	/**
	 * String used to identify items and blocks using this material
	 */
	private final String identifier;

	private final String titleName;

	private final String enumName;

	private int[] cache = null;

	/**
	 * ENUM of all the types of Materials
	 * 
	 * @author Jasmine Iwanek
	 *
	 */
	public enum MaterialType {
		WOOD, ROCK, METAL, MINERAL, GEM, CRYSTAL
	}

	/**
	 * The type of material this is
	 */
	private final MaterialType materialType;

	private int spawnSize;

	private int defaultDimension;

	/**
	 * @param name
	 *            String used to identify items and blocks using this material
	 * @param type
	 *            Base type of the material - is it a Metal, a Mineral, a Gem ?
	 * @param hardness
	 *            hardness on a scale from 0 to 10 (or more), where 0 is
	 *            non-solid and Diamond is 10. For reference, Wood is 3, Stone
	 *            is 5, Iron is 8, Diamond is 10. Used for damage, armor
	 *            protection, and tool effectiveness calculations
	 * @param strength
	 *            durability on a scale from 0 to 10 (or more). For reference,
	 *            Leather is 2.5, Gold is 3, Wood is 2, Stone is 4, Iron is 8,
	 *            Diamond is 10. Used for item durability calculations and blast
	 *            resistance
	 * @param magic
	 *            Scale from 0 to 10 (or more) on how magical the material is.
	 *            For reference, Stone is 2, Iron is 4.5, Diamond is 4, Wood is
	 *            6, Gold is 10. Used to calculate enchantability
	 * @param tintColor
	 *            Color Info for the metal
	 * @param isRare
	 *            If true, this metal is designated as an extremely rare metal
	 * @param hasOre
	 *            If true this material has an ore
	 * @param hasBlend
	 *            If true this material has a blend
	 */
	public MMDMaterial(String name, MaterialType type, float hardness, float strength, float magic, int tintColor, boolean isRare, boolean hasOre, boolean hasBlend) {
		// material stats
		this.stats.put(MaterialStats.HARDNESS, hardness);
		this.stats.put(MaterialStats.STRENGTH, strength);
		this.stats.put(MaterialStats.MAGICAFFINITY, magic);
		this.stats.put(MaterialStats.BLASTRESISTANCE, 2.5f * strength);
		this.stats.put(MaterialStats.BASEDAMAGE, this.round(0.25f * hardness, 1));

		this.tintColor = tintColor;
		this.identifier = name;
		this.titleName = StringUtils.capitalize(name);
		this.enumName = (Loader.instance().activeModContainer().getModId() + "_" + name).toUpperCase(Locale.ENGLISH);
		this.isRare = isRare;
		this.materialType = type;
		this.hasBlend = hasBlend;
		this.hasOre = hasOre;
		this.spawnSize = 8;
		this.defaultDimension = Integer.MIN_VALUE;
	}

	public String getName() {
		return this.identifier;
	}

	public String getCapitalizedName() {
		return this.titleName;
	}

	/**
	 * 
	 * @return MaterialType The type of material this is
	 */
	public MaterialType getType() {
		return this.materialType;
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
		if (o == this) {
			return true;
		} else if (o == null) {
			return false;
		}

		if ((o.hashCode() == this.hashCode()) && (o instanceof MMDMaterial)) {
			final MMDMaterial other = (MMDMaterial) o;
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
		return 0.1f * this.stats.get(MaterialStats.MAGICAFFINITY);
	}

	/**
	 * Gets the tool harvest level
	 * 
	 * @return an integer from -1 (equivalent to no tool) to 3 (diamond tool
	 *         equivalent)
	 */
	public int getToolHarvestLevel() {
		return (int) (this.stats.get(MaterialStats.HARDNESS) / 3f);
	}

	/**
	 * Gets the tool harvest level needed from a tool trying to mine this
	 * metal's ore and other blocks
	 * 
	 * @return an integer from -1 (equivalent to no tool) to 3 (diamond tool
	 *         equivalent)
	 */
	public int getRequiredHarvestLevel() {
		return (int) clamp((0.9f * this.stats.get(MaterialStats.HARDNESS)) / 3f, -1, 3);
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
		return this.stats.get(MaterialStats.BLASTRESISTANCE);
	}

	/**
	 * Gets the number used to determine how quickly a block is mined with a
	 * tool made from this material
	 * 
	 * @return the number used to determine how quickly a block is mined
	 */
	public float getToolEfficiency() {
		return this.stats.get(MaterialStats.HARDNESS);
	}

	/**
	 * Gets the hardness of the ore block for this material
	 * 
	 * @return the hardness of the ore block for this material
	 */
	public float getOreBlockHardness() {
		return 0.5f * this.stats.get(MaterialStats.HARDNESS);
	}

	/**
	 * Gets the hardness for blocks made from this material
	 * 
	 * @return the hardness for blocks made from this material
	 */
	public float getBlockHardness() {
		return 2.0f * this.stats.get(MaterialStats.HARDNESS);
	}

	/**
	 * Gets the number of uses of a tool made from this material
	 * 
	 * @return The number of uses of a tool made from this material
	 */
	public int getToolDurability() {
		return (int) (32 * this.stats.get(MaterialStats.STRENGTH));
	}

	/**
	 * Gets the number used to determine how much damage an armor item can take.
	 * 
	 * @return The number used to determine how much damage an armor item can
	 *         take.
	 */
	public int getArmorMaxDamageFactor() {
		return (int) (2.0f * this.stats.get(MaterialStats.STRENGTH));
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
			final float total = (hardnessFactor * this.stats.get(MaterialStats.HARDNESS)) + minimum;
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
		return this.stats.get(MaterialStats.BASEDAMAGE);
	}

	private float round(float number, int numDecimalPlaces) {
		int x = 1;
		for (int i = 0; i < numDecimalPlaces; i++)
			x *= 10;
		return (float) Math.round(number * x) / (float) x;
	}

	/**
	 * Gets the enchantability score for this material
	 * 
	 * @return the enchantability score for this material
	 */
	public int getEnchantability() {
		return (int) (2.5f * this.stats.get(MaterialStats.MAGICAFFINITY));
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
	public MMDMaterial setBlastResistance(float resistance) {
		this.stats.put(MaterialStats.BLASTRESISTANCE, resistance);
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
	public MMDMaterial setBaseDamage(float damage) {
		this.stats.put(MaterialStats.BASEDAMAGE, damage);
		return this;
	}

	/**
	 * Adds a new item to the list of known items made from this material
	 * 
	 * @param name
	 *            The name of the item. Existing names can be found in
	 *            com.mcmoddev.lib.data.Names
	 * @param item
	 *            The item to add
	 * @return an instance of the material - QOL and call chaining
	 */
	public MMDMaterial addNewItem(Names name, Item item) {
		addNewItem(name.toString(), item);
		return this;
	}

	/**
	 * Adds a new item to the list of known items made from this material
	 * 
	 * @param name
	 *            The name of the item. Existing names can be found in
	 *            com.mcmoddev.lib.data.Names
	 * @param item
	 *            The item to add
	 * @return an instance of the material - QOL and call chaining
	 */
	public MMDMaterial addNewItem(String name, Item item) {
		if (this.items.containsKey(name)) {
			BaseMetals.logger.warn("Tried adding item %s to a material (%s) that already has it, don't do that!", name, this.getCapitalizedName());
			return this;
		}
		this.items.put(name, item);
		return this;
	}

	/**
	 * Adds a new block to the list of known items made from this material
	 * 
	 * @param name
	 *            The name of the block. Existing names can be found in
	 *            com.mcmoddev.lib.data.Names
	 * @param block
	 *            The block to add
	 * @return an instance of the material - QOL and call chaining
	 */
	public MMDMaterial addNewBlock(Names name, Block block) {
		addNewBlock(name.toString(), block);
		return this;
	}

	/**
	 * Adds a new block to the list of known items made from this material
	 * 
	 * @param name
	 *            The name of the block. Existing names can be found in
	 *            com.mcmoddev.lib.data.Names
	 * @param block
	 *            The block to add
	 * @return an instance of the material - QOL and call chaining
	 */
	public MMDMaterial addNewBlock(String name, Block block) {
		if (this.blocks.containsKey(name)) {
			BaseMetals.logger.warn("Tried adding block %s to a material (%s) that already has it, don't do that!", name, this.getCapitalizedName());
			return this;
		}
		this.blocks.put(name, block);
		return this;
	}

	/**
	 * Get the item with name 'name' if it exists, null is returned if the item
	 * does not exist
	 * 
	 * @param name
	 *            Name of the item to retrieve
	 * @return the Item registered with the material, null if one of that name
	 *         was not registered
	 */
	public Item getItem(Names name) {
		return getItem(name.toString());
	}

	/**
	 * Get the item with name 'name' if it exists, null is returned if the item
	 * does not exist
	 * 
	 * @param name
	 *            Name of the item to retrieve
	 * @return the Item registered with the material, null if one of that name
	 *         was not registered
	 */
	public Item getItem(String name) {
		if (this.items.containsKey(name)) {
			return this.items.get(name);
		}
		return null;
	}

	/**
	 * Get the block with name 'name' if it exists, null is returned if the
	 * block does not exist
	 * 
	 * @param name
	 *            Name of the item to retrieve
	 * @return the Block registered with the material, null if one of that name
	 *         was not registered
	 */
	public Block getBlock(Names name) {
		return this.getBlock(name.toString());
	}

	/**
	 * Get the block with name 'name' if it exists, null is returned if the
	 * block does not exist
	 * 
	 * @param name
	 *            Name of the item to retrieve
	 * @return the Block registered with the material, null if one of that name
	 *         was not registered
	 */
	public Block getBlock(String name) {
		if (this.blocks.containsKey(name)) {
			return this.blocks.get(name);
		}
		return null;
	}

	/**
	 * Get all the blocks that are made from this material
	 * @return ImmutableList&lt;Block&gt; - the blocks
	 */
	public ImmutableList<Block> getBlocks() {
		return ImmutableList.copyOf(this.blocks.values());
	}
	
	/**
	 * Get all the items that are made from/with this material
	 * @return ImmutableList&lt;Item&gt; - the items
	 */
	public ImmutableList<Item> getItems() {
		return ImmutableList.copyOf(this.items.values());
	}
	
	public boolean hasOre() {
		return this.hasOre;
	}

	public boolean hasBlend() {
		return this.hasBlend;
	}

	public boolean isRare() {
		return this.isRare;
	}

	public boolean regenerates() {
		return this.regenerates;
	}

	public boolean hasItem(Names name) {
		return this.hasItem(name.toString());
	}

	public boolean hasItem(String name) {
		return this.items.containsKey(name);
	}

	public boolean hasBlock(String name) {
		return this.blocks.containsKey(name);
	}

	public boolean hasBlock(Names name) {
		return this.hasBlock(name.toString());
	}

	public float getStat(MaterialStats name) {
		if (this.stats.containsKey(name)) {
			return this.stats.get(name).floatValue();
		}
		return 0F;
	}

	public void setStat(MaterialStats name, float value) {
		this.stats.put(name, value);
	}

	public Fluid getFluid() {
		return this.fluid;
	}

	public void setFluid(Fluid f) {
		this.fluid = f;
	}

	public BlockFluidClassic getFluidBlock() {
		return this.fluidBlock;
	}

	public void setFluidBlock(BlockFluidClassic b) {
		this.fluidBlock = b;
	}

	public void setRegenerates(boolean regen) {
		this.regenerates = regen;
	}
	
	public final Material getVanillaMaterial() {
		switch (this.getType()) {
			case METAL:
				return Material.IRON;
			case GEM:
			case ROCK:
				return Material.ROCK;
			case MINERAL:
				return Material.GRASS;
			case WOOD:
				return Material.WOOD;
			default:
				return Material.GROUND;
		}
	}

	public final SoundType getSoundType() {
		switch (this.getType()) {
			case METAL:
				return SoundType.METAL;
			case GEM:
				return SoundType.GLASS;
			case ROCK:
				return SoundType.STONE;
			case MINERAL:
				return SoundType.SAND;
			case WOOD:
				return SoundType.WOOD;
			default:
				return SoundType.GROUND;
		}
	}

	public int getSpawnSize() {
		return this.spawnSize;
	}

	public MMDMaterial setSpawnSize(int size) {
		this.spawnSize = size;
		return this;
	}
	
	public int getDefaultDimension() {
		return this.defaultDimension;
	}
	
	public MMDMaterial setDefaultDimension(int dim) {
		this.defaultDimension = dim;
		return this;
	}
}
