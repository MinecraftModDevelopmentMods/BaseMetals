package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.MMDCreativeTab;
import com.mcmoddev.lib.util.TabContainer;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

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
		Block blocksTabIconItem;
		Item itemsTabIconItem;
		Item toolsTabIconItem;
		if (Options.isMaterialEnabled(MaterialNames.STARSTEEL)) {
			blocksTabIconItem = Materials.getMaterialByName(MaterialNames.STARSTEEL).getBlock(Names.BLOCK);
		} else {
			blocksTabIconItem = net.minecraft.init.Blocks.IRON_BLOCK;
		}

		if (Options.isThingEnabled("Gear")) {
			itemsTabIconItem = Materials.getMaterialByName(MaterialNames.IRON).getItem(Names.GEAR);
		} else {
			itemsTabIconItem = net.minecraft.init.Items.STICK;
		}

		if (Options.isThingEnabled("BasicTools")) {
			toolsTabIconItem = Materials.getMaterialByName(MaterialNames.DIAMOND).getItem(Names.SWORD);
		} else {
			toolsTabIconItem = net.minecraft.init.Items.DIAMOND_SWORD;
		}

		blocksTab.setTabIconItem(blocksTabIconItem);
		itemsTab.setTabIconItem(itemsTabIconItem);
		toolsTab.setTabIconItem(toolsTabIconItem);
	}
}
