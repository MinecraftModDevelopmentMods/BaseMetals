package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.MMDCreativeTab;
import com.mcmoddev.lib.util.TabContainer;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * This class initializes all item groups in Base Metals.
 *
 * @author Jasmine Iwanek
 *
 */
public class ItemGroups extends com.mcmoddev.lib.init.ItemGroups {

	private static boolean initDone = false;

	private static final int BLOCKS_TAB_ID = addTab("blocks", true);
	private static final int ITEMS_TAB_ID = addTab("items", true);
	private static final int TOOLS_TAB_ID = addTab("tools", true);
	private static final MMDCreativeTab blocksTab = getTab(BLOCKS_TAB_ID);
	private static final MMDCreativeTab itemsTab = getTab(ITEMS_TAB_ID);
	private static final MMDCreativeTab toolsTab = getTab(TOOLS_TAB_ID);
	public static final TabContainer myTabs = new TabContainer(blocksTab, itemsTab, toolsTab);

	private ItemGroups() {
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

	public static void setupIcons() {
		Block temp;
		ItemStack blocksTabIconItem;
		ItemStack itemsTabIconItem;
		ItemStack toolsTabIconItem;
		if (Options.isMaterialEnabled(MaterialNames.STARSTEEL)) {
			temp = Materials.getMaterialByName(MaterialNames.STARSTEEL).getBlock(Names.BLOCK);
		} else {
			temp = net.minecraft.init.Blocks.IRON_BLOCK;
		}
		blocksTabIconItem = new ItemStack(Item.getItemFromBlock(temp));

		if (Options.isThingEnabled("Gear")) {
			itemsTabIconItem = new ItemStack(Materials.getMaterialByName(MaterialNames.IRON).getItem(Names.GEAR));
		} else {
			itemsTabIconItem = new ItemStack(net.minecraft.init.Items.STICK);
		}

		if (Options.isThingEnabled("BasicTools")) {
			toolsTabIconItem = new ItemStack(Materials.getMaterialByName(MaterialNames.DIAMOND).getItem(Names.SWORD));
		} else {
			toolsTabIconItem = new ItemStack(net.minecraft.init.Items.DIAMOND_SWORD);
		}

		blocksTab.setTabIconItem(blocksTabIconItem);
		itemsTab.setTabIconItem(itemsTabIconItem);
		toolsTab.setTabIconItem(toolsTabIconItem);
	}
}
