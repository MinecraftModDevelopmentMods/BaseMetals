package cyano.basemetals.init;

import cyano.basemetals.material.AdamantineMaterial;
import cyano.basemetals.material.LeadMaterial;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.material.StarSteelMaterial;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.FMLLog;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This class initializes all of the metal materials in Base Metals. It also 
 * contains utility methods for looking up materials by name and finding the 
 * tool and armor material equivalents for a given metal.  
 * @author DrCyano
 *
 */
public abstract class Materials {

	private static Map<String,MetalMaterial> allMaterials = new HashMap<>();
	private static Map<MetalMaterial,ArmorMaterial> armorMaterialMap= new HashMap<>();
	private static Map<MetalMaterial,ToolMaterial> toolMaterialMap= new HashMap<>();
	
	// vanilla imports
	public static MetalMaterial vanilla_wood;
	public static MetalMaterial vanilla_stone;
	public static MetalMaterial vanilla_iron;
	public static MetalMaterial vanilla_gold;
	public static MetalMaterial vanilla_diamond;

	public static MetalMaterial adamantine;
	public static MetalMaterial aquarium;
	public static MetalMaterial brass;
	public static MetalMaterial bronze;
	public static MetalMaterial coldiron;
	public static MetalMaterial copper;
	public static MetalMaterial cupronickel;
	public static MetalMaterial electrum;
	public static MetalMaterial invar;
	public static MetalMaterial lead;
	public static MetalMaterial mithril;
	public static MetalMaterial nickel;
	public static MetalMaterial platinum;
	public static MetalMaterial silver;
	public static MetalMaterial starsteel;
	public static MetalMaterial steel;
	public static MetalMaterial tin;
	public static MetalMaterial zinc;

	private static boolean initDone = false;
	public static void init() {
		if(initDone) return;

		// Vanilla Materials
		vanilla_wood = addMaterial("wood", 2, 2, 6, 0);
		vanilla_stone = addMaterial("stone", 5, 4, 2, 0);
		vanilla_iron = addMaterial("iron", 8, 8, 4.5, 0.3);
		vanilla_gold = addMaterial("gold", 1, 1, 10, 0.1);
		vanilla_diamond = addMaterial("diamond", 10, 15, 4, 0);

		// Mod Metals
		adamantine = new AdamantineMaterial("adamantine", 12, 100, 0, 0.01f);
		registerMaterial(adamantine.getName(), adamantine);
		aquarium = addMaterial("aquarium", 4, 10, 15, 0.05);
		brass = addMaterial("brass", 3.5, 3, 9, 0.3);
		bronze = addMaterial("bronze", 8, 4, 4.5, 0.3);
		coldiron = addMaterial("coldiron", 7, 7, 7, 0.05);
		copper = addMaterial("copper", 4, 4, 5, 0.5);
		cupronickel = addMaterial("cupronickel", 6, 6, 6, 0.5);
		electrum = addMaterial("electrum", 5, 4, 10, 0.1);
		invar = addMaterial("invar", 9, 10, 3, 0.1);
		lead = new LeadMaterial("lead", 1, 1, 1, 0.5f);
		registerMaterial(lead.getName(), lead);
		mithril = addMaterial("mithril", 9, 9, 9, 0.05);
		nickel = addMaterial("nickel", 4, 4, 7, 0.1);
		platinum = addMaterial("platinum", 3, 5, 12, 0.5);
		silver = addMaterial("silver", 5, 4, 6, 0.1);
		starsteel = new StarSteelMaterial("starsteel", 10, 25, 12, 0.01f);
		registerMaterial(starsteel.getName(), starsteel);
		steel = addMaterial("steel", 8, 15, 2, 0.3);
		tin = addMaterial("tin", 3, 1, 2, 0.5);
		zinc = addMaterial("zinc", 1, 1, 1, 0.3);

		initDone = true;
	}

	private static MetalMaterial addMaterial(String name, double hardness, double strength, double magic, double rarity) {
		MetalMaterial m = new MetalMaterial(name, (float)hardness, (float)strength, (float)magic, (int)(Math.max(50*rarity,1)));
		registerMaterial(name, m);
		return m;
	}

	protected static void registerMaterial(String name, MetalMaterial m){
		allMaterials.put(name, m);
		
		String enumName = m.getEnumName();
		String texName = m.getName();
		int[] protection = m.getDamageReductionArray();
		int durability = m.getArmorMaxDamageFactor();
		ArmorMaterial am = EnumHelper.addArmorMaterial(enumName, texName, durability, protection, m.getEnchantability());
		if(am == null) {
			// uh-oh
			FMLLog.severe("Failed to create armor material enum for "+m);
		}
		armorMaterialMap.put(m, am);
		FMLLog.info("Created armor material enum "+am);
		
		ToolMaterial tm = EnumHelper.addToolMaterial(enumName, m.getToolHarvestLevel(), m.getToolDurability(), m.getToolEfficiency(), m.getBaseAttackDamage(), m.getEnchantability());
		if(tm == null) {
			// uh-oh
			FMLLog.severe("Failed to create tool material enum for "+m);
		}
		toolMaterialMap.put(m, tm);
		FMLLog.info("Created tool material enum "+tm);
	}

	/**
	 * Gets the armor material for a given metal 
	 * @param m The metal of interest
	 * @return The armor material for this metal, or null if there isn't one
	 */
	public static ArmorMaterial getArmorMaterialFor(MetalMaterial m) {
		return armorMaterialMap.get(m);
	}

	/**
	 * Gets the tool material for a given metal 
	 * @param m The metal of interest
	 * @return The tool material for this metal, or null if there isn't one
	 */
	public static ToolMaterial getToolMaterialFor(MetalMaterial m) {
		return toolMaterialMap.get(m);
	}

	/**
	 * Returns a list of all metal materials in Base Metals. All of the metals 
	 * in this list are also available as static public members of this class. 
	 * @return A Collection of MetalMaterial instances.
	 */
	public static Collection<MetalMaterial> getAllMetals() {
		return allMaterials.values();
	}

	/**
	 * Gets a metal material by its name (e.g. "copper").
	 * @param metalName The name of a metal
	 * @return The material representing the named metal, or null if no metals 
	 * have been registered under that name.
	 */
	public static MetalMaterial getMetalByName(String metalName) {
		return allMaterials.get(metalName);
	}
}
