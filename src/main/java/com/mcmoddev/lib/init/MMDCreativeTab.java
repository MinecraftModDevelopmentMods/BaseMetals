package com.mcmoddev.lib.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Comparator;
import java.util.List;

import com.mcmoddev.basemetals.init.Items;

/**
 * This class is a MetalMaterial based Wrapper for making a CreativeTab.
 *
 * @author Jasmine Iwanek
 *
 */
public class MMDCreativeTab extends CreativeTabs {

	private Item iconItem;
	
	private final boolean searchable;
	private List<ItemStack> cache;
	private Comparator<ItemStack> comparator;

	private static final Comparator<ItemStack> DEFAULT = new Comparator<ItemStack>() {
		@Override
		public int compare(ItemStack first, ItemStack second) {
			final int delta = Items.getSortingValue(first) - Items.getSortingValue(second);
			return (delta == 0) ? first.getUnlocalizedName().compareToIgnoreCase(second.getUnlocalizedName()) : delta;
		}
	};

	public MMDCreativeTab(String unlocalizedName, boolean searchable, Item iconItem ) {
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
	public void displayAllRelevantItems(List<ItemStack> itemList) {
		if (cache == null) {

			super.displayAllRelevantItems(itemList);
			cache = itemList;

			if (comparator != null)
				cache.sort(comparator);
		}

		itemList.addAll(cache);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		if( this.iconItem == null ) {
				return net.minecraft.init.Items.IRON_PICKAXE;
		}		
		return this.iconItem;
	}

	public void setSortingAlgorithm(Comparator<ItemStack> comparator) {
		this.comparator = comparator;

		if (this.cache != null)
			cache.sort(comparator);
	}
	
	public void setTabIconItem( Item iconItem ) {
		this.iconItem = iconItem;
	}
	
}
