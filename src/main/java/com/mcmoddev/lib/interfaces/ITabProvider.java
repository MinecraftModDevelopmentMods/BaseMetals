package com.mcmoddev.lib.interfaces;

import com.mcmoddev.lib.init.MMDCreativeTab;

import net.minecraft.block.Block;

public interface ITabProvider {
	MMDCreativeTab getTabByName(String tabName);
	void insertTab(MMDCreativeTab newTab);
	void addBlockToTab(Block block, String tabName) throws Exception;
}
