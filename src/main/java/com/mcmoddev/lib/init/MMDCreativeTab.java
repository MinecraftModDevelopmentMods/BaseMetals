package com.mcmoddev.lib.init;

import java.util.Comparator;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mcmoddev.basemetals.init.Items;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This class is a MMDMaterial based Wrapper for making a CreativeTab.
 *
 * @author Jasmine Iwanek
 *
 */
public class MMDCreativeTab extends CreativeTabs {

	private ItemStack iconItem;
	
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

	public MMDCreativeTab(@Nonnull final String unlocalizedName, @Nonnull final boolean searchable) {
		this(unlocalizedName, searchable, (ItemStack) null);
	}

	public MMDCreativeTab(@Nonnull final String unlocalizedName, @Nonnull final boolean searchable, @Nullable final Block iconBlock) {
		this(unlocalizedName, searchable, new ItemStack(Item.getItemFromBlock(iconBlock)));
	}

	public MMDCreativeTab(@Nonnull final String unlocalizedName, @Nonnull final boolean searchable, @Nullable final Item iconItem) {
		this(unlocalizedName, searchable, new ItemStack(iconItem));
	}

	public MMDCreativeTab(@Nonnull final String unlocalizedName, @Nonnull final boolean searchable, @Nullable final ItemStack iconItem) {
		super(unlocalizedName);
		if (iconItem == null) {
			this.iconItem =  new ItemStack(net.minecraft.init.Items.IRON_PICKAXE);
		} else {
			this.iconItem = iconItem;
		}
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
		return this.iconItem.getItem();
	}

	public void setSortingAlgorithm(@Nonnull final Comparator<ItemStack> comparator) {
		this.comparator = comparator;

		if (this.cache != null)
			cache.sort(comparator);
	}

	public void setTabIconItem(@Nonnull final Block iconBlock) {
		this.iconItem = new ItemStack(Item.getItemFromBlock(iconBlock));
	}

	public void setTabIconItem(@Nonnull final Item iconItem) {
		this.iconItem = new ItemStack(iconItem);
	}

	public void setTabIconItem(@Nonnull final ItemStack iconItem) {
		this.iconItem = iconItem;
	}
}