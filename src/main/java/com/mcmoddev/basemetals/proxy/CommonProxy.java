package com.mcmoddev.basemetals.proxy;

import javax.annotation.Nullable;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.LegacyMappings;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.init.ItemGroups;
import com.mcmoddev.basemetals.loot.ContentModeLootCondition;
import com.mcmoddev.basemetals.network.ContentModeNetwork;
import com.mcmoddev.basemetals.util.EventHandler;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/** Common setup shared by the client and dedicated server. */
public class CommonProxy {

	/** Registers networking, loot conditions, and common event handlers. */
	public void preInit(final FMLPreInitializationEvent event) {
		ContentModeNetwork.init();
		ContentModeLootCondition.register();
		LegacyMappings.registerDataFixer();
		MinecraftForge.EVENT_BUS.register(com.mcmoddev.basemetals.BaseMetals.class);
	}

	/** Maps the old liquid-mercury block ID to the current fluid block. */
	public void onRemapBlock(final RegistryEvent.MissingMappings<Block> event) {
		for (final RegistryEvent.MissingMappings.Mapping<Block> mapping : event.getAllMappings()) {
			final String name = LegacyMappings.blockTarget(mapping.key.toString());
			if (name != null) {
				final ResourceLocation target = new ResourceLocation(name);
				if (ForgeRegistries.BLOCKS.containsKey(target)) {
					mapping.remap(ForgeRegistries.BLOCKS.getValue(target));
				} else {
					BaseMetals.logger.warn("Cannot recover %s: replacement block %s is not registered",
							mapping.key, target);
				}
			}
		}
	}

	/** Recovers renamed powders, nuggets, fluid items and metal door items. */
	public void onRemapItem(final RegistryEvent.MissingMappings<Item> event) {
		for (final RegistryEvent.MissingMappings.Mapping<Item> mapping : event.getAllMappings()) {
			final String name = LegacyMappings.itemTarget(mapping.key.toString());
			if (name != null) {
				final ResourceLocation target = new ResourceLocation(name);
				if (ForgeRegistries.ITEMS.containsKey(target)) {
					mapping.remap(ForgeRegistries.ITEMS.getValue(target));
				} else {
					BaseMetals.logger.warn("Cannot recover %s: replacement item %s is not registered",
							mapping.key, target);
				}
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
