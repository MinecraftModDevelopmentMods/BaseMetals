package com.mcmoddev.lib.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraftforge.fml.common.Loader;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mcmoddev.lib.init.Items;
import com.mcmoddev.lib.material.MetalMaterial;

/**
 * This class initializes all item groups in Base Metals.
 *
 * @author Jasmine Iwanek
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

	public static CreativeTabs blocksTab;
	public static CreativeTabs itemsTab;
	public static CreativeTabs toolsTab;

	private static Map<String, List<GeneralizedCreativeTab>> itemGroupsByModID = new HashMap<>();

	private static boolean initDone = false;

	protected ItemGroups() {
		throw new IllegalAccessError("Not a instantiable class");
	}

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		initDone = true;
	}

	protected static GeneralizedCreativeTab addTab(String name, boolean searchable, MetalMaterial material) {
		GeneralizedCreativeTab tab = new GeneralizedCreativeTab(Loader.instance().activeModContainer().getModId() + "." + name, searchable, material);
		// itemGroupsByModID.get(name).add(tab);
		return tab;
	}

	/**
	 * Gets a map of all items added, sorted by material
	 *
	 * @return An unmodifiable map of added items catagorized by metal material
	 */
	public static Map<String, List<GeneralizedCreativeTab>> getItemsGroupsByModID() {
		return Collections.unmodifiableMap(itemGroupsByModID);
	}
}
