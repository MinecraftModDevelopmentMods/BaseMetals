package com.mcmoddev.lib.util;

import java.util.Collection;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.exceptions.MaterialNotFoundException;
import com.mcmoddev.lib.exceptions.TabNotFoundException;
import com.mcmoddev.lib.init.MMDCreativeTab;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.interfaces.ITabProvider;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * @deprecated this is the old implementation of tabcontainer, wrapped in the tabprovider interface
 *
 */
@Deprecated
public final class TabContainer implements ITabProvider {
	// @SkyBlade: temporary code until the tabcontainer is replaced
	
	private static final String TAB_NOT_FOUND = "Tab not found: ";
	public final MMDCreativeTab blocksTab;
	public final MMDCreativeTab itemsTab;
	public final MMDCreativeTab toolsTab;

	private Multimap<String, String> tabItemMapping;
	
	public TabContainer(MMDCreativeTab blocksTab, MMDCreativeTab itemsTab, MMDCreativeTab toolsTab) {
		this.blocksTab = blocksTab;
		this.itemsTab = itemsTab;
		this.toolsTab = toolsTab;
		
		tabItemMapping = ArrayListMultimap.create();
	}

	public TabContainer(MMDCreativeTab blocksTab, MMDCreativeTab itemsTab, MMDCreativeTab toolsTab, Multimap<String, String> tabItemMapping) {
		this.blocksTab = blocksTab;
		this.itemsTab = itemsTab;
		this.toolsTab = toolsTab;
		this.tabItemMapping = tabItemMapping;
	}
	
	@Override
	public MMDCreativeTab getTabByName(String tabName) throws TabNotFoundException {
		switch (tabName) {
		case "blocksTab":
			return this.blocksTab;			
		case "itemsTab":
			return this.itemsTab;
		case "toolsTab":
			return this.toolsTab;
		default:
			throw new TabNotFoundException(tabName);
		}
	}

	@Override
	public void addBlockToTab(String tabName, Block block) throws TabNotFoundException {
		MMDCreativeTab tab = this.getTabByName(tabName);
		
		if (tab == null) 
			throw new TabNotFoundException(TAB_NOT_FOUND + tabName);
		
		block.setCreativeTab(tab);
	}
	
	@Override
	public void addItemToTab(String tabName, Item item) throws TabNotFoundException {
		MMDCreativeTab tab = this.getTabByName(tabName);

		if (tab == null)
			throw new TabNotFoundException(TAB_NOT_FOUND + tabName);
		
		item.setCreativeTab(tab);
	}

	@Override
	public void setIcon(String tabName, String materialName) throws TabNotFoundException, MaterialNotFoundException {
		MMDCreativeTab tab = this.getTabByName(tabName);
		
		if (tab == null) 
			throw new TabNotFoundException(TAB_NOT_FOUND + tabName);
		
		Block temp;
		ItemStack blocksTabIconItem;

		MMDMaterial material = Materials.getMaterialByName(materialName);
		
		if (material.getName().equals(materialName) && (material.hasBlock(Names.BLOCK)))
			temp = material.getBlock(Names.BLOCK);
		else
			temp = net.minecraft.init.Blocks.IRON_BLOCK;
		
		blocksTabIconItem = new ItemStack(Item.getItemFromBlock(temp));
		blocksTab.setTabIconItem(blocksTabIconItem);
	}

	@Override
	public String getTab(String itemName, String modID) {
		return getTab(itemName);
	}

	@Override
	public void setTabItemMapping(String tabName, String itemName) {
		tabItemMapping.put(itemName, tabName);
	}

	@Override
	public String getTab(String itemName) {
		Collection<String> tabs = tabItemMapping.get(itemName);

		return tabs.isEmpty() ? "blocksTab" : tabs.iterator().next();
	}
}
