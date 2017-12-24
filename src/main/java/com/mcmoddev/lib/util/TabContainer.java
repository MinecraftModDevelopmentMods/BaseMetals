package com.mcmoddev.lib.util;

import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.MMDCreativeTab;
import com.mcmoddev.lib.interfaces.ITabProvider;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class TabContainer implements ITabProvider {
	// @SkyBlade: temporary code until the tabcontainer is replaced
	
	public final MMDCreativeTab blocksTab;
	public final MMDCreativeTab itemsTab;
	public final MMDCreativeTab toolsTab;

	public TabContainer(MMDCreativeTab blocksTab, MMDCreativeTab itemsTab, MMDCreativeTab toolsTab) {
		this.blocksTab = blocksTab;
		this.itemsTab = itemsTab;
		this.toolsTab = toolsTab;
	}

	@Override
	public MMDCreativeTab getTabByName(String tabName) {
		// rubbish temporary code
		switch (tabName) {
		case "blocksTab":
			return this.blocksTab;			
		case "itemsTab":
			return this.itemsTab;
		case "toolsTab":
			return this.toolsTab;
		default:
			return null;
		}
	}

	@Override
	public void insertTab(MMDCreativeTab newTab) {
		throw new UnsupportedOperationException(); // old tab provider doesn't add new tabs
	}

	@Override
	public void addBlockToTab(Block block, String tabName) throws Exception {
		MMDCreativeTab tab = this.getTabByName(tabName);
		
		if (tab == null) 
			throw new Exception("Tab not found: " + tabName);
		
		block.setCreativeTab(tab);
	}
	
	@Override
	public void addItemToTab(Item item, String tabName) throws Exception {
		MMDCreativeTab tab = this.getTabByName(tabName);
		
		if (tab == null)
			throw new Exception("Tab not found: " + tabName);
		
		item.setCreativeTab(tab);
	}

	@Override
	public void setIcon(String tabName, String materialName) throws Exception {
		MMDCreativeTab tab = this.getTabByName(tabName);
		
		if (tab == null) 
			throw new Exception("Tab not found: " + tabName);
		
		Block temp;
		ItemStack blocksTabIconItem;

		MMDMaterial material = Materials.getMaterialByName(materialName);
		
		if ((Materials.hasMaterial(materialName)) && (material.hasBlock(Names.BLOCK)))
			temp = material.getBlock(Names.BLOCK);
		else
			temp = net.minecraft.init.Blocks.IRON_BLOCK;
		
		blocksTabIconItem = new ItemStack(Item.getItemFromBlock(temp));
		blocksTab.setTabIconItem(blocksTabIconItem);
	}
}
