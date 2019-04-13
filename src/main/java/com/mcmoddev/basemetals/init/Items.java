package com.mcmoddev.basemetals.init;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.events.MMDLibRegisterItems;
import com.mcmoddev.lib.init.ItemGroups;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.IMMDBurnableObject;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * This class initializes all items in Base Metals.
 *
 * @author Jasmine Iwanek
 *
 */
@Mod.EventBusSubscriber(modid=BaseMetals.MODID)
public final class Items extends com.mcmoddev.lib.init.Items {

	private Items() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/**
	 *
	 */
	@SubscribeEvent
	public static void registerItems(MMDLibRegisterItems ev) {
		final List<String> materials = Arrays.asList(MaterialNames.ADAMANTINE,
				MaterialNames.ANTIMONY, MaterialNames.AQUARIUM, MaterialNames.BISMUTH,
				MaterialNames.BRASS, MaterialNames.BRONZE, MaterialNames.COLDIRON,
				MaterialNames.COPPER, MaterialNames.CUPRONICKEL, MaterialNames.EMERALD,
				MaterialNames.ELECTRUM, MaterialNames.INVAR, MaterialNames.LEAD,
				MaterialNames.OBSIDIAN, MaterialNames.MITHRIL, MaterialNames.NICKEL,
				MaterialNames.PEWTER, MaterialNames.PLATINUM, MaterialNames.QUARTZ,
				MaterialNames.SILVER, MaterialNames.STARSTEEL, MaterialNames.STEEL,
				MaterialNames.TIN, MaterialNames.ZINC);

		// create and register modded stuffs
		final List<String> materialsModSupport = Arrays.asList(MaterialNames.ADAMANTINE,
				MaterialNames.ANTIMONY, MaterialNames.BISMUTH, MaterialNames.COLDIRON,
				MaterialNames.PLATINUM, MaterialNames.NICKEL, MaterialNames.STARSTEEL,
				MaterialNames.ZINC);

		materials.stream().filter(Materials::hasMaterial)
				.filter(materialName -> !Materials.getMaterialByName(materialName).isEmpty())
				.forEach(materialName -> {
					final MMDMaterial material = Materials.getMaterialByName(materialName);

					create(Names.BLEND, material);
					create(Names.INGOT, material);
					create(Names.NUGGET, material);
					create(Names.POWDER, material);
					create(Names.SMALLBLEND, material);
					create(Names.SMALLPOWDER, material);

					create(Names.ARROW, material);
					create(Names.AXE, material);
					create(Names.BOLT, material);
					create(Names.BOOTS, material);
					create(Names.BOW, material);
					create(Names.CHESTPLATE, material);
					create(Names.CRACKHAMMER, material);
					create(Names.CROSSBOW, material);
					create(Names.DOOR, material);
					create(Names.FISHING_ROD, material);
					create(Names.HELMET, material);
					create(Names.HOE, material);
					create(Names.HORSE_ARMOR, material);
					create(Names.LEGGINGS, material);
					create(Names.PICKAXE, material);
					create(Names.SHEARS, material);
					create(Names.SHIELD, material);
					create(Names.SHOVEL, material);
					create(Names.SCYTHE, material);
					create(Names.SLAB, material);
					create(Names.SWORD, material);
					create(Names.ROD, material);
					create(Names.GEAR, material);
				});

		materials.stream()
		.filter(Materials::hasMaterial)
		.map(Materials::getMaterialByName)
		.forEach(com.mcmoddev.basemetals.init.Items::setBurnTimes);
		
		materialsModSupport.stream().filter(Materials::hasMaterial)
				.filter(materialName -> !Materials.getMaterialByName(materialName).isEmpty())
				.forEach(materialName -> {
					final MMDMaterial material = Materials.getMaterialByName(materialName);

					create(Names.CASING, material);
					create(Names.DENSE_PLATE, material);

					if (material.hasOre()) {
						create(Names.CRUSHED, material);
						create(Names.CRUSHED_PURIFIED, material);

						createMekCrystal(material, ItemGroups.getTab(SharedStrings.TAB_ITEMS));
						create(Names.SHARD, material);
						create(Names.CLUMP, material);
						create(Names.POWDER_DIRTY, material);
						create(Names.CRYSTAL, material);
					}
				});

		if (Materials.hasMaterial(MaterialNames.MERCURY)) {
			final MMDMaterial mercury = Materials.getMaterialByName(MaterialNames.MERCURY);

			create(Names.INGOT, mercury);
			create(Names.NUGGET, mercury);
			create(Names.POWDER, mercury);
			create(Names.SMALLPOWDER, mercury);
		}

		Arrays.asList(MaterialNames.STONE, MaterialNames.STEEL, MaterialNames.ADAMANTINE).stream()
				.filter(Materials::hasMaterial).forEach(materialName -> create(Names.ANVIL,
						Materials.getMaterialByName(materialName)));

		addToMetList();

		MinecraftForge.EVENT_BUS.register(Items.class);
	}

	private static void setBurnTimes(@Nonnull final MMDMaterial material) {
		if (material.hasItem(Names.NUGGET)) {
			((IMMDBurnableObject) material.getItem(Names.NUGGET)).setBurnTime(NUGGET_BURN_TIME);
		}

		if (material.hasItem(Names.POWDER)) {
			((IMMDBurnableObject) material.getItem(Names.POWDER)).setBurnTime(INGOT_BURN_TIME);
		}

		if (material.hasItem(Names.SMALLPOWDER)) {
			((IMMDBurnableObject) material.getItem(Names.SMALLPOWDER)).setBurnTime(NUGGET_BURN_TIME);
		}
	}

	/**
	 * Registers Items for this mod.
	 *
	 * @param event The Event.
	 */
	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		
		Materials.getMaterialsByMod(BaseMetals.MODID).stream()
				.forEach(mat ->	regItems(event.getRegistry(), mat.getItems()));
		regItems(event.getRegistry(), ImmutableList.copyOf(Materials.DEFAULT.getItems().stream()
				.filter(is -> BaseMetals.MODID.equals(is.getItem().getRegistryName().getNamespace()))
				.collect(Collectors.toList())));
/*		if(Materials.getMaterialByName("default").hasBlock("human_detector")) {
			Block hd = Materials.getMaterialByName("default").getBlock("human_detector");
			final ItemBlock itemBlock = new ItemBlock(hd);

			itemBlock.setRegistryName("human_detector");
			itemBlock.setTranslationKey("tile." + hd.getRegistryName().getNamespace() + ".human_detector");
			event.getRegistry().register(itemBlock);
		}*/
		Oredicts.registerItemOreDictionaryEntries();
		Oredicts.registerBlockOreDictionaryEntries();
	}

	private static void regItems(final IForgeRegistry<Item> registry,
			final ImmutableList<ItemStack> items) {
		items.stream().filter(Items::isThisMod).map(Items::getItem)
				.forEach( registry::register );
	}

	private static Item getItem(final ItemStack it) {
		return it.getItem();
	}

	private static boolean isThisMod(final ItemStack it) {
		return it.getItem().getRegistryName().getNamespace()
				.equalsIgnoreCase(BaseMetals.MODID);
	}
}
