package com.mcmoddev.basemetals.init;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.items.ItemBaseMetalsArrow;
import com.mcmoddev.basemetals.items.ItemBaseMetalsBolt;
import com.mcmoddev.basemetals.items.ItemBaseMetalsBow;
import com.mcmoddev.basemetals.items.ItemBaseMetalsCrossbow;
import com.mcmoddev.basemetals.items.ItemBaseMetalsHoe;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.events.MMDLibRegisterItems;
import com.mcmoddev.lib.init.ItemGroups;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

/** Creates and registers Base Metals items. */
@Mod.EventBusSubscriber(modid = BaseMetals.MODID)
public final class Items extends com.mcmoddev.lib.init.Items {

	private Items() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/** Creates material forms before Forge opens the item registry. */
	@SubscribeEvent
	public static void registerItems(MMDLibRegisterItems ev) {
		final List<String> materials = Arrays.asList(MaterialNames.ADAMANTINE,
				MaterialNames.ANTIMONY, MaterialNames.AQUARIUM, MaterialNames.BISMUTH,
				MaterialNames.BRASS, MaterialNames.BRONZE, MaterialNames.COLDIRON,
				MaterialNames.COPPER, MaterialNames.CUPRONICKEL,
				MaterialNames.ELECTRUM, MaterialNames.INVAR, MaterialNames.LEAD,
				MaterialNames.MITHRIL, MaterialNames.NICKEL,
				MaterialNames.PEWTER, MaterialNames.PLATINUM,
				MaterialNames.SILVER, MaterialNames.STARSTEEL, MaterialNames.STEEL,
				MaterialNames.TIN, MaterialNames.ZINC);

		// These materials need extra forms for other mods' ore-processing chains.
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

					createArrow(material);
					create(Names.AXE, material);
					createBolt(material);
					create(Names.BOOTS, material);
					createBow(material);
					create(Names.CHESTPLATE, material);
					create(Names.CRACKHAMMER, material);
					createCrossbow(material);
					create(Names.DOOR, material);
					create(Names.FISHING_ROD, material);
					create(Names.HELMET, material);
					createHoe(material);
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

	/** Adds the previously created items to Forge's item registry. */
	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		applyFishingRodDurability();

		Materials.getAllMaterials()
				.stream()
				.filter(mat -> !mat.isVanilla())
				.forEach(mat -> regItems(event.getRegistry(), mat.getItems()));

		Oredicts.registerItemOreDictionaryEntries();
		Oredicts.registerBlockOreDictionaryEntries();
	}

	private static void applyFishingRodDurability() {
		Materials.getAllMaterials().stream()
				.filter(material -> material.hasItem(Names.FISHING_ROD))
				.forEach(material -> {
					final Item fishingRod = material.getItem(Names.FISHING_ROD);

					if (fishingRod.getRegistryName() != null
							&& BaseMetals.MODID.equals(fishingRod.getRegistryName().getNamespace())) {
						fishingRod.setMaxDamage(material.getToolDurability());
					}
				});
	}

	/**
	 * Gets or creates a material arrow.
	 *
	 * @return the existing or newly created arrow, or {@code null} when disabled
	 */
	public static Item createArrow(final MMDMaterial material) {
		if (material.hasItem(Names.ARROW)) {
			return material.getItem(Names.ARROW);
		}

		if (!isNameEnabled(Names.ARROW)) {
			return null;
		}

		return registerRangedItem(Names.ARROW, material, new ItemBaseMetalsArrow(material));
	}

	/**
	 * Gets or creates a material bolt.
	 *
	 * @return the existing or newly created bolt, or {@code null} when disabled
	 */
	public static Item createBolt(final MMDMaterial material) {
		if (material.hasItem(Names.BOLT)) {
			return material.getItem(Names.BOLT);
		}

		if (!isNameEnabled(Names.BOLT)) {
			return null;
		}

		return registerRangedItem(Names.BOLT, material, new ItemBaseMetalsBolt(material));
	}

	/**
	 * Gets or creates a material bow.
	 *
	 * @return the existing or newly created bow, or {@code null} when disabled
	 */
	public static Item createBow(final MMDMaterial material) {
		if (material.hasItem(Names.BOW)) {
			return material.getItem(Names.BOW);
		}

		if (!isNameEnabled(Names.BOW)) {
			return null;
		}

		return registerRangedItem(Names.BOW, material, new ItemBaseMetalsBow(material));
	}

	/**
	 * Gets or creates a crossbow that fires bolts on the server.
	 *
	 * @return the existing or newly created crossbow, or {@code null} when disabled
	 */
	public static Item createCrossbow(final MMDMaterial material) {
		if (material.hasItem(Names.CROSSBOW)) {
			return material.getItem(Names.CROSSBOW);
		}

		if (!isNameEnabled(Names.CROSSBOW)) {
			return null;
		}

		return registerRangedItem(Names.CROSSBOW, material,
				new ItemBaseMetalsCrossbow(material));
	}

	/**
	 * Gets or creates a hoe with a vanilla-compatible tool-material name.
	 *
	 * @return the existing or newly created hoe, or {@code null} when disabled
	 */
	public static Item createHoe(final MMDMaterial material) {
		if (material.hasItem(Names.HOE)) {
			return material.getItem(Names.HOE);
		}

		if (!isNameEnabled(Names.HOE)) {
			return null;
		}

		final Item registered = addItem(new ItemBaseMetalsHoe(material),
				Names.HOE.toString(), material, ItemGroups.getTab(SharedStrings.TAB_TOOLS));

		material.addNewItem(Names.HOE, registered);

		return registered;
	}

	private static Item registerRangedItem(final Names name, final MMDMaterial material,
			final Item item) {
		final Item registered = addItem(item, name.toString(), material,
				ItemGroups.getTab(SharedStrings.TAB_COMBAT));

		material.addNewItem(name, registered);

		return registered;
	}

	private static void regItems(final IForgeRegistry<Item> registry,
			final ImmutableList<ItemStack> items) {
		items.stream().filter(Items::isThisMod).map(Items::getItem)
				.forEach(registry::register);
	}

	private static Item getItem(final ItemStack it) {
		return it.getItem();
	}

	private static boolean isThisMod(final ItemStack it) {
		return it.getItem().getRegistryName().getNamespace()
				.equalsIgnoreCase(BaseMetals.MODID);
	}
}
