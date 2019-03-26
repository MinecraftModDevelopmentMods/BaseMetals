package com.mcmoddev.basemetals.init;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.block.BlockHumanDetector;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.events.MMDLibRegisterBlocks;
import com.mcmoddev.lib.init.ItemGroups;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * This class initializes all blocks in Base Metals.
 *
 * @author Jasmine Iwanek
 *
 */
@Mod.EventBusSubscriber(modid=BaseMetals.MODID)
public final class Blocks extends com.mcmoddev.lib.init.Blocks {

	private Blocks() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/**
	 *
	 */
	public static void init(final MMDLibRegisterBlocks event) {
		final List<String> materials = Arrays.asList(MaterialNames.ADAMANTINE,
				MaterialNames.ANTIMONY, MaterialNames.AQUARIUM, MaterialNames.BISMUTH,
				MaterialNames.BRASS, MaterialNames.BRONZE, MaterialNames.COLDIRON,
				MaterialNames.COPPER, MaterialNames.CUPRONICKEL, MaterialNames.ELECTRUM,
				MaterialNames.INVAR, MaterialNames.LEAD, MaterialNames.MITHRIL,
				MaterialNames.NICKEL, MaterialNames.PEWTER, MaterialNames.PLATINUM,
				MaterialNames.SILVER, MaterialNames.STARSTEEL, MaterialNames.STEEL,
				MaterialNames.TIN, MaterialNames.ZINC);

		materials.stream().filter(Materials::hasMaterial).forEach(materialName -> {
			final MMDMaterial material = Materials.getMaterialByName(materialName);

			create(Names.BLOCK, material);
			create(Names.PLATE, material);
			create(Names.ORE, material);
			create(Names.BARS, material);
			create(Names.DOOR, material);
			create(Names.TRAPDOOR, material);

			create(Names.BUTTON, material);
			create(Names.SLAB, material);
			create(Names.DOUBLE_SLAB, material);
			create(Names.LEVER, material);
			create(Names.PRESSURE_PLATE, material);
			create(Names.STAIRS, material);
			create(Names.WALL, material);
		});

		createMercury();
		createAnvils();

		addBlock(new BlockHumanDetector(), "human_detector",
				ItemGroups.getTab(SharedStrings.TAB_BLOCKS));

		MinecraftForge.EVENT_BUS.register(Blocks.class);
	}

	private static void createAnvils() {
		Arrays.asList(MaterialNames.STONE, MaterialNames.STEEL, MaterialNames.ADAMANTINE).stream()
				.filter(Materials::hasMaterial)
				.forEach(name -> create(Names.ANVIL, Materials.getMaterialByName(name)));
	}

	private static void createMercury() {
		if (Materials.hasMaterial(MaterialNames.MERCURY)) {
			final MMDMaterial mercury = Materials.getMaterialByName(MaterialNames.MERCURY);
			create(Names.ORE, mercury);
			if (mercury.hasBlock(Names.ORE)) {
				mercury.getBlock(Names.ORE).setHardness(3.0f).setResistance(5.0f);
			}
		}
	}

	/**
	 * Registers Blocks for this mod.
	 *
	 * @param event The Event.
	 */
	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		Materials.getMaterialsByMod(BaseMetals.MODID).stream()
				.forEach(mat -> regBlocks(event.getRegistry(), mat.getBlocks()));
	}

	private static void regBlocks(final IForgeRegistry<Block> registry,
			final ImmutableList<Block> blocks) {
		blocks.stream().filter(
				block -> block.getRegistryName().getNamespace().equals(BaseMetals.MODID))
				.forEach(registry::register);
	}
}
