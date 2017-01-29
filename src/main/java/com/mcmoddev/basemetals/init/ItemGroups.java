package com.mcmoddev.basemetals.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mcmoddev.basemetals.init.GeneralizedCreativeTab;
import com.mcmoddev.basemetals.init.Items;
import com.mcmoddev.basemetals.material.MetalMaterial;

import cyano.basemetals.init.Materials;

/**
 * This class initializes all item groups in Base Metals.
 *
 * @author DrCyano
 *
 */
public class ItemGroups {

	public static final java.util.function.BiFunction<ItemStack, ItemStack, Integer> sortingAlgorithm = (ItemStack a, ItemStack b) -> {
		final int delta = Items.getSortingValue(a) - Items.getSortingValue(b);
		if (delta == 0) {
			return a.getItem().getUnlocalizedName().compareToIgnoreCase(b.getItem().getUnlocalizedName());
		}
		return delta;
	};

	public static CreativeTabs tab_blocks;
	public static CreativeTabs tab_items;
	public static CreativeTabs tab_tools;

	private static Map<String, List<GeneralizedCreativeTab>> itemGroupsByModID = new HashMap<>();

	private static boolean initDone = false;

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		// placeholders
		// Item blockItem = Items.copper_crackhammer;
		// Item itemItem = Items.copper_crackhammer;
		// Item toolItem = Items.copper_crackhammer;

		tab_blocks = addTab("blocks", true, Materials.vanilla_iron);
		tab_items = addTab("items", true, Materials.vanilla_iron);
		tab_tools = addTab("tools", true, Materials.vanilla_iron);

//		tab_items = tab_blocks;
//		tab_tools = tab_items;

		initDone = true;
	}

	protected static GeneralizedCreativeTab addTab(String name, boolean searchable, MetalMaterial material) {
		String MODID = Loader.instance().activeModContainer().getModId();
		String TEMP = MODID.concat( "." + name);
		FMLLog.warning("Making GeneralizedCreativeTab for " + MODID + "named:" + name);
		FMLLog.warning("TEMP Was: " + TEMP);
		GeneralizedCreativeTab tab = new GeneralizedCreativeTab(name, searchable, material);
//		itemGroupsByModID.get(name).add(tab);
		return tab;
	}

	/**
	 * Gets a map of all items added, sorted by material
	 *
	 * @return An unmodifiable map of added items categorized by material
	 */
	public static Map<String, List<GeneralizedCreativeTab>> getItemsGroupsByModID() {
		return Collections.unmodifiableMap(itemGroupsByModID);
	}
}
