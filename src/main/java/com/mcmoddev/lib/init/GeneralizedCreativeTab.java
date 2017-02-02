package com.mcmoddev.lib.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Comparator;
import java.util.List;

import com.mcmoddev.basemetals.init.Items;
import com.mcmoddev.lib.material.MetalMaterial;

public class GeneralizedCreativeTab extends CreativeTabs {

	private MetalMaterial material;
	private final boolean searchable;
	private List<ItemStack> cache;
	private Comparator<ItemStack> comparator;

	public GeneralizedCreativeTab(String unlocalizedName, boolean searchable, MetalMaterial material) {
		super(unlocalizedName);
//		this.itemSupplier = itemSupplier;
		this.material = material;
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
		if (this.material.crackhammer != null) {
			return this.material.crackhammer;
		} else {
			return net.minecraft.init.Items.IRON_PICKAXE;
		}
	}

	public void setSortingAlgorithm(Comparator<ItemStack> comparator) {
		this.comparator = comparator;

		if (this.cache != null)
			cache.sort(comparator);
	}

	private static final Comparator<ItemStack> DEFAULT = new Comparator<ItemStack>() {
		@Override
		public int compare(ItemStack first, ItemStack second) {
			final int delta = Items.getSortingValue(first) - Items.getSortingValue(second);
			return (delta == 0) ? first.getUnlocalizedName().compareToIgnoreCase(second.getUnlocalizedName()) : delta;
		}
	};
}
