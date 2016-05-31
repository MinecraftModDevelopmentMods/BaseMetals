package cyano.basemetals.init;

import cyano.basemetals.material.AdamantineMaterial;
import cyano.basemetals.material.LeadMaterial;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.material.StarSteelMaterial;
import net.minecraft.init.SoundEvents;
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
	
	public static MetalMaterial copper;
	public static MetalMaterial silver;
	public static MetalMaterial tin;
	public static MetalMaterial lead;
	public static MetalMaterial nickel;
	public static MetalMaterial bronze;
	public static MetalMaterial brass;
	public static MetalMaterial steel;
	public static MetalMaterial invar;
	public static MetalMaterial electrum;
	public static MetalMaterial coldiron;
	public static MetalMaterial mithril;
	public static MetalMaterial adamantine;
	public static MetalMaterial starsteel;
	public static MetalMaterial zinc;
	public static MetalMaterial aquarium;
	public static MetalMaterial cupronickel;
	public static MetalMaterial platinum;

	// vanilla imports
	public static MetalMaterial vanilla_wood;
	public static MetalMaterial vanilla_stone;
	public static MetalMaterial vanilla_iron;
	public static MetalMaterial vanilla_gold;
	public static MetalMaterial vanilla_diamond;
	
	private static boolean initDone = false;
	public static void init(){
		if(initDone)return;
		
		// vanilla metals
		vanilla_wood = addMaterial("wood",2,2,6);
		vanilla_stone = addMaterial("stone",5,4,2);
		vanilla_iron = addMaterial("iron",8,8,4.5);
		vanilla_gold = addMaterial("gold",1,1,10);
		vanilla_diamond = addMaterial("diamond",10,15,4);

		// mod metals
		copper = addMaterial("copper",4,4,5);
		silver = addMaterial("silver", 5, 4, 6);
		tin = addMaterial("tin", 3, 1, 2);
		lead = new LeadMaterial("lead", 1, 1, 1);
		registerMaterial(lead.getName(), lead);
		nickel = addMaterial("nickel", 4, 4, 7);
		zinc = addMaterial("zinc", 1, 1, 1);
		bronze = addMaterial("bronze", 8, 4, 4.5);
		brass = addMaterial("brass", 3.5, 3, 9);
		steel = addMaterial("steel", 8, 15, 2);
		invar = addMaterial("invar", 9, 10, 3);
		electrum = addMaterial("electrum", 5, 4, 10);
		coldiron = addMaterial("coldiron", 7, 7, 7);
		mithril = addMaterial("mithril", 9, 9, 9);
		adamantine = new AdamantineMaterial("adamantine", 12, 100, 0);
		registerMaterial(adamantine.getName(), adamantine);
		starsteel = new StarSteelMaterial("starsteel", 10, 25, 12);
		registerMaterial(starsteel.getName(), starsteel);
		aquarium = addMaterial("aquarium", 4, 10, 15);
		
		cupronickel = addMaterial("cupronickel", 6, 6, 6);
		platinum = addRareMaterial("platinum", 3, 5, 15);
		
		initDone = true;
	}

	private static MetalMaterial addMaterial(String name, double hardness, double strength, double magic){
		MetalMaterial m = new MetalMaterial(name,(float)hardness,(float)strength,(float)magic);
		registerMaterial(name, m);
		return m;
	}

	private static MetalMaterial addRareMaterial(String name, double hardness, double strength, double magic){
		MetalMaterial m = new MetalMaterial(name,(float)hardness,(float)strength,(float)magic,true);
		registerMaterial(name, m);
		return m;
	}
	
	protected static void registerMaterial(String name, MetalMaterial m){

		allMaterials.put(name, m);
		
		String enumName = m.getEnumName();
		String texName = m.getName();
		int[] protection = m.getDamageReductionArray();
		int durability = m.getArmorMaxDamageFactor();
		ArmorMaterial am = EnumHelper.addArmorMaterial(enumName, texName, durability, protection, m.getEnchantability(), SoundEvents.ITEM_ARMOR_EQUIP_IRON, (m.hardness > 10 ? (int)(m.hardness / 5) : 0));
		if(am == null){
			// uh-oh
			FMLLog.severe("Failed to create armor material enum for "+m);
		}
		armorMaterialMap.put(m, am);
		FMLLog.info("Created armor material enum "+am);
		
		ToolMaterial tm = EnumHelper.addToolMaterial(enumName, m.getToolHarvestLevel(), m.getToolDurability(), m.getToolEfficiency(), m.getBaseAttackDamage(), m.getEnchantability());
		if(tm == null){
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
	public static ArmorMaterial getArmorMaterialFor(MetalMaterial m){
		return armorMaterialMap.get(m);
	}


	/**
	 * Gets the tool material for a given metal 
	 * @param m The metal of interest
	 * @return The tool material for this metal, or null if there isn't one
	 */
	public static ToolMaterial getToolMaterialFor(MetalMaterial m){
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
	public static MetalMaterial getMetalByName(String metalName){
		return allMaterials.get(metalName);
	}
}
