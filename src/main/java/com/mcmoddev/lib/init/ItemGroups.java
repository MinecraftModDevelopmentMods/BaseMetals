package com.mcmoddev.lib.init;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

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

	public static final BiFunction<ItemStack, ItemStack, Integer> sortingAlgorithm = (
			final ItemStack a, final ItemStack b) -> {
		final int delta = Items.getSortingValue(a) - Items.getSortingValue(b);
		if (delta == 0) {
			return a.getItem().getUnlocalizedName()
					.compareToIgnoreCase(b.getItem().getUnlocalizedName());
		}
		return delta;
	};

	private static final Map<String, MMDCreativeTab> itemGroupsByFullTabName = new HashMap<>();
	private static final Map<String, List<MMDCreativeTab>> itemGroupsByModID = new HashMap<>();

	protected ItemGroups() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/**
	 * Initializer.
	 */
	public static void init() {
		// Blank initializer
	}

	/**
	 * Adds a non Searchable CreativeTab.
	 *
	 * @param name
	 *            The Name of the CreativeTab
	 * @return The CreativeTab
	 */
	protected static MMDCreativeTab addTab(@Nonnull final String name) {
		return addTab(name, false);
	}

	/**
	 * Adds a CreativeTab.
	 *
	 * @param name
	 *            The Name of the CreativeTab
	 * @param searchable
	 *            Is is searchable?
	 * @return The CreativeTab
	 */
	protected static MMDCreativeTab addTab(@Nonnull final String name,
			@Nonnull final boolean searchable) {
		final String modName = Loader.instance().activeModContainer().getModId();
		final String internalTabName = String.format("%s.%s", modName, name);
		final MMDCreativeTab tab = new MMDCreativeTab(internalTabName, searchable);

		if (!itemGroupsByFullTabName.containsKey(modName)) {
			itemGroupsByFullTabName.put(internalTabName, tab);
		}

		if (itemGroupsByModID.containsKey(modName)) {
			itemGroupsByModID.get(modName).add(tab);
		} else {
			final List<MMDCreativeTab> nl = new ArrayList<>();
			nl.add(tab);
			itemGroupsByModID.put(modName, nl);
		}

		return itemGroupsByFullTabName.get(internalTabName);
	}

	/**
	 *
	 * @param name
	 *            Name of the tab to get
	 * @return The Tab
	 */
	@Nullable
	public static MMDCreativeTab getTab(@Nonnull final String name) {
		return getTab(Loader.instance().activeModContainer().getModId(), name);
	}

	/**
	 *
	 * @param modName
	 *            the ModID
	 * @param name
	 *            Name of the tab to get
	 * @return The Tab
	 */
	@Nullable
	public static MMDCreativeTab getTab(@Nonnull final String modName, @Nonnull final String name) {
		final String finalName = String.format("%s.%s", modName, name);
		if (itemGroupsByFullTabName.containsKey(finalName)) {
			return itemGroupsByFullTabName.get(finalName);
		}
		return null;
	}

	/**
	 * Gets a map of all tabs added, sorted by modID.
	 *
	 * @return An unmodifiable map of added tabs categorized by modID
	 */
	public static Map<String, List<MMDCreativeTab>> getItemsGroupsByModID() {
		return Collections.unmodifiableMap(itemGroupsByModID);
	}
}
