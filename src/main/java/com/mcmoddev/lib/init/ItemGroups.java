package com.mcmoddev.lib.init;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mcmoddev.lib.data.SharedStrings;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

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

	private static final Map<String, List<MMDCreativeTab>> itemGroupsByModID = new HashMap<>();

	protected ItemGroups() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/**
	 *
	 */
	public static void init() {
	}

	protected static int addTab(@Nonnull final String name, @Nonnull final boolean searchable) {
		final String modName = Loader.instance().activeModContainer().getModId();
		final String internalTabName = String.format("%s.%s", modName, name);
		final MMDCreativeTab tab = new MMDCreativeTab(internalTabName, searchable);
		if (itemGroupsByModID.containsKey(modName)) {
			itemGroupsByModID.get(modName).add(tab);
		} else {
			final List<MMDCreativeTab> nl = new ArrayList<>();
			nl.add(tab);
			itemGroupsByModID.put(modName, nl);
		}

		return itemGroupsByModID.get(modName).size() - 1;
	}

	@Nullable
	protected static MMDCreativeTab getTab(@Nonnull final int id) {
		return getTab(Loader.instance().activeModContainer().getModId(), id);
	}

	@Nullable
	protected static MMDCreativeTab getTab(@Nonnull final String modName, @Nonnull final int id) {
		if ((itemGroupsByModID.containsKey(modName)) && (itemGroupsByModID.get(modName).size() > id)) {
			return itemGroupsByModID.get(modName).get(id);
		}
		return null;
	}

	/**
	 * Gets a map of all tabs added, sorted by modID
	 *
	 * @return An unmodifiable map of added tabs categorized by modID
	 */
	public static Map<String, List<MMDCreativeTab>> getItemsGroupsByModID() {
		return Collections.unmodifiableMap(itemGroupsByModID);
	}
}
