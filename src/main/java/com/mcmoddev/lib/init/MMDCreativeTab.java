package com.mcmoddev.lib.init;

import java.util.Comparator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
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
	private Comparator<ItemStack> comparator;

	public MMDCreativeTab(@Nonnull final String unlocalizedName,
			@Nonnull final boolean searchable) {
		this(unlocalizedName, searchable, ItemStack.EMPTY);
	}

	/**
	 *
	 * @param unlocalizedName
	 * @param searchable
	 * @param iconBlock
	 */
	public MMDCreativeTab(@Nonnull final String unlocalizedName, @Nonnull final boolean searchable,
			@Nullable final Block iconBlock) {
		this(unlocalizedName, searchable,
				(iconBlock != null) ? new ItemStack(Item.getItemFromBlock(iconBlock))
						: ItemStack.EMPTY);
	}

	public MMDCreativeTab(@Nonnull final String unlocalizedName, @Nonnull final boolean searchable,
			@Nullable final Item iconItem) {
		this(unlocalizedName, searchable,
				(iconItem != null) ? new ItemStack(iconItem) : ItemStack.EMPTY);
	}

	/**
	 *
	 * @param unlocalizedName
	 * @param searchable
	 * @param iconItem
	 */
	public MMDCreativeTab(@Nonnull final String unlocalizedName, @Nonnull final boolean searchable,
			@Nonnull final ItemStack iconItem) {
		super(unlocalizedName);
		if (iconItem.isEmpty()) {
			this.iconItem = new ItemStack(net.minecraft.init.Items.IRON_PICKAXE);
		} else {
			this.iconItem = iconItem;
		}
		this.searchable = searchable;
		this.setSortingAlgorithm((final ItemStack first, final ItemStack second) -> {
			final int delta = Items.getSortingValue(first) - Items.getSortingValue(second);
			return (delta == 0)
					? first.getTranslationKey().compareToIgnoreCase(second.getTranslationKey())
					: delta;
		});
		if (searchable) {
			this.setBackgroundImageName("item_search.png");
		}
	}

	@Override
	public boolean hasSearchBar() {
		return this.searchable;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void displayAllRelevantItems(@Nonnull final NonNullList<ItemStack> itemList) {
		super.displayAllRelevantItems(itemList);
		if (this.comparator != null) {
			itemList.sort(this.comparator);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack createIcon() {
		return this.iconItem;
	}

	public void setSortingAlgorithm(@Nonnull final Comparator<ItemStack> comparator) {
		this.comparator = comparator;
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
