package com.mcmoddev.lib.init;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.init.Items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This class is a MetalMaterial based Wrapper for making a CreativeTab.
 *
 * @author Jasmine Iwanek
 *
 */
public class MMDCreativeTab extends CreativeTabs {

	private ItemStack iconItem;
	
	private final boolean searchable;
	private Comparator<ItemStack> comparator;

	private static final Comparator<ItemStack> DEFAULT = new Comparator<ItemStack>() {
		@Override
		public int compare(ItemStack first, ItemStack second) {
			final int delta = Items.getSortingValue(first) - Items.getSortingValue(second);
			return (delta == 0) ? first.getUnlocalizedName().compareToIgnoreCase(second.getUnlocalizedName()) : delta;
		}
	};

	public MMDCreativeTab(String unlocalizedName, boolean searchable, ItemStack iconItem) {
		super(unlocalizedName);
		// this.itemSupplier = itemSupplier;
		this.iconItem = iconItem;
		this.searchable = searchable;
		this.setSortingAlgorithm(DEFAULT);
		if (searchable)
			setBackgroundImageName("item_search.png");
	}

	@Override
	public boolean hasSearchBar() {
		return searchable;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void displayAllRelevantItems(NonNullList<ItemStack> itemList) {
		super.displayAllRelevantItems(itemList);
		if (comparator != null) {
			itemList.sort(comparator);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getTabIconItem() {
		if (this.iconItem == null) {
			return new ItemStack(net.minecraft.init.Items.IRON_PICKAXE);
		}
		return this.iconItem;
	}

	public void setSortingAlgorithm(Comparator<ItemStack> comparator) {
		this.comparator = comparator;
	}

	public void setTabIconItem(ItemStack iconItem) {
		this.iconItem = iconItem;
	}
}
