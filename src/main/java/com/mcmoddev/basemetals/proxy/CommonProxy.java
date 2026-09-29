package com.mcmoddev.basemetals.proxy;


import javax.annotation.Nullable;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.init.ItemGroups;
import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.basemetals.loot.ContentModeLootCondition;
import com.mcmoddev.basemetals.network.ContentModeNetwork;
import com.mcmoddev.basemetals.util.EventHandler;
import com.mcmoddev.lib.data.Names;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
/** Common setup shared by the client and dedicated server. */
public class CommonProxy {

	/** Registers networking, loot conditions, and common event handlers. */
	public void preInit(final FMLPreInitializationEvent event) {
		ContentModeNetwork.init();
		ContentModeLootCondition.register();
		MinecraftForge.EVENT_BUS.register(com.mcmoddev.basemetals.BaseMetals.class);
	}

	/** Maps the old liquid-mercury block ID to the current fluid block. */
	public void onRemapBlock(final RegistryEvent.MissingMappings<Block> event) {
		for (final RegistryEvent.MissingMappings.Mapping<Block> mapping : event.getAllMappings()) {
			if (mapping.key.getNamespace().equals(BaseMetals.MODID)
					&& (Materials.hasMaterial(MaterialNames.MERCURY))
					&& ("liquid_mercury".equals(mapping.key.getPath()))) {
				mapping.remap(com.mcmoddev.lib.init.Materials
						.getMaterialByName(MaterialNames.MERCURY).getFluidBlock());
			}
		}
	}

	/** Maps the old carbon-powder item ID to coal powder. */
	public void onRemapItem(final RegistryEvent.MissingMappings<Item> event) {
		for (final RegistryEvent.MissingMappings.Mapping<Item> mapping : event.getAllMappings()) {
			if (mapping.key.getNamespace().equals(BaseMetals.MODID)
					&& (Materials.hasMaterial(MaterialNames.COAL))
					&& ("carbon_powder".equals(mapping.key.getPath()))) {
				mapping.remap(com.mcmoddev.lib.init.Materials.getMaterialByName(MaterialNames.COAL)
						.getItem(Names.POWDER));
			}
		}
	}

	/** Registers gameplay handlers and chooses the creative-tab icons. */
	public void init(final FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new EventHandler());

		ItemGroups.setupIcons(MaterialNames.STARSTEEL);
	}
	public void postInit(final FMLPostInitializationEvent event) {
	}
	
	public World getWorld(@Nullable final int dimension) {
		return null;
	}
}
