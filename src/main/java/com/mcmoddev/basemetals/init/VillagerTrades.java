package com.mcmoddev.basemetals.init;

import java.util.*;

import com.mcmoddev.basemetals.util.VillagerTradeHelper;
import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.item.ItemMetalCrackHammer;
import com.mcmoddev.lib.item.ItemMetalIngot;
import com.mcmoddev.lib.material.MetalMaterial;

import net.minecraft.entity.passive.EntityVillager.*;
import net.minecraft.item.*;
import net.minecraftforge.fml.common.Loader;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class VillagerTrades extends com.mcmoddev.lib.init.VillagerTrades {

	private static boolean initDone = false;

	private VillagerTrades() {
		throw new IllegalAccessError("Not a instantiable class");
	}

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		Materials.init();
		Items.init();

		registerCommonTrades();

		initDone = true;
	}

	protected static void registerCommonTrades() {
		// integer is used as byte data: (unused) (profession) (career) (level)
		final Map<Integer, List<ITradeList>> tradesTable = new HashMap<>();

		// Minecraft stores trades in a 4D array:
		// [Profession ID][Sub-profession ID][villager level - 1][trades]

		final int size = Materials.getAllMaterials().size();
		final Map<MetalMaterial, List<Item>> allArmors = new HashMap<>(size);
		final Map<MetalMaterial, Item> allHammers = new HashMap<>(size);
		final Map<MetalMaterial, Item> allSwords = new HashMap<>(size);
		final Map<MetalMaterial, Item> allHoes = new HashMap<>(size);
		final Map<MetalMaterial, Item> allAxes = new HashMap<>(size);
		final Map<MetalMaterial, Item> allPickAxes = new HashMap<>(size);
		final Map<MetalMaterial, Item> allShovels = new HashMap<>(size);
		final Map<MetalMaterial, Item> allIngots = new HashMap<>(size);

		String modid = Loader.instance().activeModContainer().getModId();
		Items.getItemsByMaterial().entrySet().stream().forEach((Map.Entry<MetalMaterial, List<Item>> entry) -> {
			final MetalMaterial material = entry.getKey();
			if (material == null) {
				return;
			}

			for (final Item item : entry.getValue()) {
				if (item.getRegistryName().getResourceDomain().equals(modid)) {
					if (item instanceof ItemArmor) {
						allArmors.computeIfAbsent(material, (MetalMaterial g) -> new ArrayList<>()).add(item);
					} else if (item instanceof ItemMetalCrackHammer) {
						allHammers.put(material, item);
					} else if (item instanceof ItemSword) {
						allSwords.put(material, item);
					} else if (item instanceof ItemHoe) {
						allHoes.put(material, item);
					} else if (item instanceof ItemAxe) {
						allAxes.put(material, item);
					} else if (item instanceof ItemPickaxe) {
						allPickAxes.put(material, item);
					} else if (item instanceof ItemSpade) {
						allShovels.put(material, item);
					} else if (item instanceof ItemMetalIngot) {
						allIngots.put(material, item);
					}
				}
			}
		});

		for (final MetalMaterial material : Materials.getAllMaterials()) {
			final float value = material.hardness + material.strength + material.magicAffinity + material.getToolHarvestLevel();
			if (material.isRare) {
				continue;
			}

			// for reference, iron has a value of 21.5, gold would be 14, copper
			// is 14, and diamond is 30
			final int emeraldPurch = emeraldPurchaseValue(value);
			final int emeraldSale = emeraldSaleValue(value);
			final int tradeLevel = tradeLevel(value);

			if ((emeraldPurch > 64) || (emeraldSale > 64)) {
				continue; // too expensive
			}

			final int armorsmith = (3 << 16) | (1 << 8) | (tradeLevel);
			final int weaponsmith = (3 << 16) | (2 << 8) | (tradeLevel);
			final int toolsmith = (3 << 16) | (3 << 8) | (tradeLevel);

			if (allIngots.containsKey(material)) {
				final ITradeList[] ingotTrades = makeTradePalette(makePurchasePalette(emeraldPurch, 12, allIngots.get(material)), makeSalePalette(emeraldSale, 12, allIngots.get(material)));
				tradesTable.computeIfAbsent(armorsmith, (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(ingotTrades));
				tradesTable.computeIfAbsent(weaponsmith, (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(ingotTrades));
				tradesTable.computeIfAbsent(toolsmith, (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(ingotTrades));
			}
			if (allHammers.containsKey(material) && allPickAxes.containsKey(material) && allAxes.containsKey(material) && allShovels.containsKey(material) && allHoes.containsKey(material)) {
				tradesTable.computeIfAbsent(toolsmith, (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makeTradePalette(makePurchasePalette(emeraldPurch, 1, allPickAxes.get(material), allAxes.get(material), allShovels.get(material), allHoes.get(material)))));
				tradesTable.computeIfAbsent((3 << 16) | (3 << 8) | (tradeLevel + 1), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makeTradePalette(makePurchasePalette(emeraldPurch, 1, allHammers.get(material)))));
			}
			if (allSwords.containsKey(material)) {
				tradesTable.computeIfAbsent(weaponsmith, (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makeTradePalette(makePurchasePalette((emeraldPurch + (int) (material.getBaseAttackDamage() / 2)) - 1, 1, allSwords.get(material)))));
			}
			if (allArmors.containsKey(material)) {
				tradesTable.computeIfAbsent(armorsmith, (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makeTradePalette(makePurchasePalette(emeraldPurch + (int) (material.hardness / 2), 1, allArmors.get(material).toArray(new Item[0])))));
			}

			if (material.magicAffinity > 5) {
				if (allHammers.containsKey(material)) {
					tradesTable.computeIfAbsent((3 << 16) | (3 << 8) | (tradeLevel + 2), (Integer key) -> new ArrayList<>()).addAll(Collections.singletonList(new ListEnchantedItemForEmeralds(allHammers.get(material), new PriceInfo(emeraldPurch + 7, emeraldPurch + 12))));
				}
				if (allPickAxes.containsKey(material)) {
					tradesTable.computeIfAbsent((3 << 16) | (3 << 8) | (tradeLevel + 1), (Integer key) -> new ArrayList<>()).addAll(Collections.singletonList(new ListEnchantedItemForEmeralds(allPickAxes.get(material), new PriceInfo(emeraldPurch + 7, emeraldPurch + 12))));
				}
				if (allArmors.containsKey(material)) {
					for (int i = 0; i < allArmors.get(material).size(); i++) {
						tradesTable.computeIfAbsent((3 << 16) | (1 << 8) | (tradeLevel + 1), (Integer key) -> new ArrayList<>()).addAll(Collections.singletonList(new ListEnchantedItemForEmeralds(allArmors.get(material).get(i), new PriceInfo(emeraldPurch + 7 + (int) (material.hardness / 2), emeraldPurch + 12 + (int) (material.hardness / 2)))));
					}
				}
				if (allSwords.containsKey(material)) {
					tradesTable.computeIfAbsent((3 << 16) | (2 << 8) | (tradeLevel + 1), (Integer key) -> new ArrayList<>()).addAll(Collections.singletonList(new ListEnchantedItemForEmeralds(allSwords.get(material), new PriceInfo(emeraldPurch + 7 + (int) (material.getBaseAttackDamage() / 2) - 1, emeraldPurch + 12 + (int) (material.getBaseAttackDamage() / 2) - 1))));
				}
			}
		}

		if ("basemetals".equals(Loader.instance().activeModContainer().getModId())) {
			if (Options.enableCharcoal) {
				tradesTable.computeIfAbsent((3 << 16) | (1 << 8) | (1), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makePurchasePalette(1, 10, Materials.vanilla_charcoal.powder)));
				tradesTable.computeIfAbsent((3 << 16) | (2 << 8) | (1), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makePurchasePalette(1, 10, Materials.vanilla_charcoal.powder)));
				tradesTable.computeIfAbsent((3 << 16) | (3 << 8) | (1), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makePurchasePalette(1, 10, Materials.vanilla_charcoal.powder)));
			}

			if (Options.enableCoal) {
				tradesTable.computeIfAbsent((3 << 16) | (1 << 8) | (1), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makePurchasePalette(1, 10, Materials.vanilla_coal.powder)));
				tradesTable.computeIfAbsent((3 << 16) | (2 << 8) | (1), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makePurchasePalette(1, 10, Materials.vanilla_coal.powder)));
				tradesTable.computeIfAbsent((3 << 16) | (3 << 8) | (1), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makePurchasePalette(1, 10, Materials.vanilla_coal.powder)));
			}
		}

		for (final Integer k : tradesTable.keySet()) {
			final List<ITradeList> trades = tradesTable.get(k);
			final int profession = (k >> 16) & 0xFF;
			final int career = (k >> 8) & 0xFF;
			final int level = k & 0xFF;

			try {
				VillagerTradeHelper.insertTrades(profession, career, level, new MultiTradeGenerator(TRADES_PER_LEVEL, trades));
			} catch (NoSuchFieldException | IllegalAccessException ex) {
				BaseMetals.logger.error("Java Reflection Exception", ex);
			}
		}
	}
}
