package com.mcmoddev.lib.util;

import com.mcmoddev.lib.init.MMDCreativeTab;

public final class TabContainer {

	public final MMDCreativeTab blocksTab;
	public final MMDCreativeTab itemsTab;
	public final MMDCreativeTab toolsTab;

	/**
	 *
	 * @param blocksTab
	 * @param itemsTab
	 * @param toolsTab
	 */
	public TabContainer(final MMDCreativeTab blocksTab, final MMDCreativeTab itemsTab, final MMDCreativeTab toolsTab) {
		this.blocksTab = blocksTab;
		this.itemsTab = itemsTab;
		this.toolsTab = toolsTab;
	}
}
