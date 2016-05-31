package cyano.basemetals.material;

import cyano.basemetals.BaseMetals;
import net.minecraft.inventory.EntityEquipmentSlot;

import java.util.Locale;

public class MetalMaterial {

	/** hardness on a scale from 0 to 10 (or more), where 0 is non-solid and 
	 * diamond is 10. For reference, wood is 3, stone is 5, iron is 8, diamond is 10.
	 * */
	public final float hardness;
	/** durability on a scale from 0 to 10 (or more).
	 * For reference, leather is 2.5, gold is 3, wood is 2, stone is 4, iron is 8, minecraft diamond is 10.
	 *  */
	public final float strength;
	/**
	 * Scale from 0 to 10 (or more) on how magical the material is.
	 * For reference, stone is 2, iron is 4.5, diamond is 4, wood is 6, gold is 10.
	 */
	public final float magicAffinity;
	/**
	 * Rare metals, like platinum, are never found in villager trades and unusually uncommon
	 * in world generation
	 */
	public final boolean isRare;
	/**
	 * String used to identify items and blocks using this material
	 */
	final String identifier;
	
	final String titleName;
	
	private final String enumName;

	/**
	 * @param name String used to identify items and blocks using this material
	 * @param hardness hardness on a scale from 0 to 10 (or more), where 0 is non-solid and 
	 * diamond is 10. For reference, wood is 3, stone is 5, iron is 8, diamond is 10.
	 * Used for damage, armor protection, and tool effectiveness calculations
	 * @param strength durability on a scale from 0 to 10 (or more).
	 * For reference, leather is 2.5, gold is 3, wood is 2, stone is 4, iron is 8, minecraft diamond is 10.
	 * Used for item durability calculations and blast resistance
	 * @param magic Scale from 0 to 10 (or more) on how magical the material is.
	 * For reference, stone is 2, iron is 4.5, diamond is 4, wood is 6, gold is 10.
	 * Used to calculate enchantibility
	 * @param lootFrequency (unused in MC 1.9)
	 */
	@Deprecated
	public MetalMaterial(String name, float hardness, float strength, float magic, int lootFrequency){
		this(name,hardness,strength,magic);
	}
	/**
	 * @param name String used to identify items and blocks using this material
	 * @param hardness hardness on a scale from 0 to 10 (or more), where 0 is non-solid and
	 * diamond is 10. For reference, wood is 3, stone is 5, iron is 8, diamond is 10.
	 * Used for damage, armor protection, and tool effectiveness calculations
	 * @param strength durability on a scale from 0 to 10 (or more).
	 * For reference, leather is 2.5, gold is 3, wood is 2, stone is 4, iron is 8, minecraft diamond is 10.
	 * Used for item durability calculations and blast resistance
	 * @param magic Scale from 0 to 10 (or more) on how magical the material is.
	 * For reference, stone is 2, iron is 4.5, diamond is 4, wood is 6, gold is 10.
	 * Used to calculate enchantibility
	 */
	public MetalMaterial(String name, float hardness, float strength, float magic){
		this.hardness = hardness;
		this.strength = strength;
		this.magicAffinity = magic;
		this.identifier = name;
		String firstLetter = name.substring(0,1);
		String rest = name.substring(1);
		titleName = firstLetter.toUpperCase(Locale.ENGLISH)+rest;
		enumName = (BaseMetals.MODID+"_"+name).toUpperCase(Locale.ENGLISH);
		isRare = false;
	}
	/**
	 * @param name String used to identify items and blocks using this material
	 * @param hardness hardness on a scale from 0 to 10 (or more), where 0 is non-solid and
	 * diamond is 10. For reference, wood is 3, stone is 5, iron is 8, diamond is 10.
	 * Used for damage, armor protection, and tool effectiveness calculations
	 * @param strength durability on a scale from 0 to 10 (or more).
	 * For reference, leather is 2.5, gold is 3, wood is 2, stone is 4, iron is 8, minecraft diamond is 10.
	 * Used for item durability calculations and blast resistance
	 * @param magic Scale from 0 to 10 (or more) on how magical the material is.
	 * For reference, stone is 2, iron is 4.5, diamond is 4, wood is 6, gold is 10.
	 * Used to calculate enchantibility
	 * @param isRare If true, this metal is designated as an extremely rare metal
	 */
	public MetalMaterial(String name, float hardness, float strength, float magic, boolean isRare){
		this.hardness = hardness;
		this.strength = strength;
		this.magicAffinity = magic;
		this.identifier = name;
		String firstLetter = name.substring(0,1);
		String rest = name.substring(1);
		titleName = firstLetter.toUpperCase(Locale.ENGLISH)+rest;
		enumName = (BaseMetals.MODID+"_"+name).toUpperCase(Locale.ENGLISH);
		this.isRare = isRare;
	}
	
	public String getName(){
		return identifier;
	}
	
	public String getCapitalizedName(){
		return titleName;
	}
	
	@Override public String toString(){
		return getName();
	}
	
	@Override
	public int hashCode(){
		return identifier.hashCode();
	}
	@Override
	public boolean equals(Object o){
		if(o == this) return true;
		if(o.hashCode() == this.hashCode() && o instanceof MetalMaterial){
			MetalMaterial other = (MetalMaterial)o;
			return this.identifier.equals(other.identifier); 
		}
		return false;
	}
	

	/**
	 * Gets the amount of XP per ore block that is smelted
	 * @return XP value per ore block
	 */
	public float getOreSmeltXP(){
		return 0.1f * magicAffinity;
	}
	
	
	/**
	 * Gets the tool harvest level
	 * @return an integer from -1 (equivalent to no tool) to 3 (diamond tool equivalent)
	 */
	public int getToolHarvestLevel(){
		return (int)(hardness / 3f); 
	}
	/**
	 * Gets the tool harvest level needed from a tool trying to mine this 
	 * metal's ore and other blocks
	 * @return an integer from -1 (equivalent to no tool) to 3 (diamond tool equivalent)
	 */
	public int getRequiredHarvestLevel(){
		return (int)clamp((0.9f*hardness / 3f),-1,3); 
	}
	
	static int clamp(int x, int min, int max){
		if(x < min)return min;
		if(x > max) return max;
		return x;
	}
	static float clamp(float x, float min, float max){
		if(x < min)return min;
		if(x > max) return max;
		return x;
	}
	static double clamp(double x, double min, double max){
		if(x < min)return min;
		if(x > max) return max;
		return x;
	}
	/**
	 * Gets the resistance of blocks made from this metal to explosions
	 * @return the blast resistance score
	 */
	public float getBlastResistance(){
		return 2.5f * strength;
	}
	/**
	 * Gets the number used to determine how quickly a block is mined with a 
	 * tool made from this material
	 * @return the number used to determine how quickly a block is mined
	 */
	public float getToolEfficiency(){
		return hardness;
	}
	/**
	 * Gets the hardness of the ore block for this material
	 * @return the hardness of the ore block for this material
	 */
	public float getOreBlockHardness(){
		return 0.5f * hardness;
	}
	/**
	 * Gets the hardness for blocks made from this material
	 * @return the hardness for blocks made from this material
	 */
	public float getMetalBlockHardness(){
		return 2.0f * hardness;
	}
	/**
	 * Gets the number of uses of a tool made from this material 
	 * @return The number of uses of a tool made from this material 
	 */
	public int getToolDurability(){
		return (int)(32 * strength);
	}
	/**
	 * Gets the number used to determine how much damage an armor item can 
	 * take.
	 * @return The number used to determine how much damage an armor item can 
	 * take.
	 */
	public int getArmorMaxDamageFactor(){
		return (int)(2.0f * strength);
	}
	
	private int[] cache =  null;
	/**
	 * Gets the protection value for helmets, chestplates, leg armor, and boots 
	 * made from this material
	 * @return the protection value for helmets, chestplates, leg armor, and boots 
	 * made from this material
	 */
	public int[] getDamageReductionArray(){
		if(cache == null){
			final float minimum = 5f; // most metals should be better than leather armor
			final float hardnessFactor = 1.25f;
			final float total = hardnessFactor * hardness + minimum;
			cache = new int[4];
			final int feetIndex = EntityEquipmentSlot.FEET.getIndex();
			final int legsIndex = EntityEquipmentSlot.LEGS.getIndex();
			final int chestIndex = EntityEquipmentSlot.CHEST.getIndex();
			final int headIndex = EntityEquipmentSlot.HEAD.getIndex();
			cache[headIndex] = Math.round(0.1f * total);// head
			cache[chestIndex] = Math.round(0.4f * total);// torso
			cache[legsIndex] = Math.round(0.35f * total);// legs
			cache[feetIndex] = Math.round(0.15f * total);// feet
		}
		return cache;
	}
	/**
	 * Gets the base damage from attacks with tools made from this material
	 * @return the base damage from attacks with tools made from this material
	 */
	public float getBaseAttackDamage(){
		return round(0.25f * hardness,1);
	}
	
	private float round(float number, int numDecimalPlaces){
		int x = 1;
		for(int i = 0; i < numDecimalPlaces; i++){
			x *= 10;
		}
		return (float)Math.round(number * x) / (float)x;
	}
	/**
	 * Gets the enchantibility score for this material
	 * @return the enchantibility score for this material
	 */
	public int getEnchantability(){
		return (int)(2.5f * magicAffinity);
	}

	public String getEnumName() {
		return enumName;
	}

}
