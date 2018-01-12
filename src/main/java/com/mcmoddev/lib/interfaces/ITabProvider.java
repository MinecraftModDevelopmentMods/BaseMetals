package com.mcmoddev.lib.interfaces;

import com.mcmoddev.lib.init.MMDCreativeTab;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public interface ITabProvider {

	MMDCreativeTab getTabByName(String tabName);

	void insertTab(MMDCreativeTab newTab);

	void addBlockToTab(Block block, String tabName) throws Exception;

	void addItemToTab(Item item, String tabName) throws Exception;

	void setIcon(String tabName, String materialName) throws Exception;
}
