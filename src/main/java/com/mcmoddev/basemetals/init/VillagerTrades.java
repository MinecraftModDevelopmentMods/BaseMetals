package com.mcmoddev.basemetals.init;

import java.util.*;

import com.mcmoddev.basemetals.util.VillagerTradeHelper;
import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.items.ItemMetalCrackHammer;
import com.mcmoddev.lib.items.ItemMetalIngot;
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
		final Map<Integer, List<ITradeList>> tradesTable = new HashMap<>(); // integer is used as byte data: (unused) (profession) (career) (level)

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
		Items.getItemsByMaterial().entrySet().stream().forEach((Map.Entry<MetalMaterial, List<Item>> e) -> {
			final MetalMaterial m = e.getKey();
			if (m == null) {
				return;
			}

			for (final Item i : e.getValue()) {
				if (i.getRegistryName().getResourceDomain().equals(modid)) {
					if (i instanceof ItemArmor) {
						allArmors.computeIfAbsent(m, (MetalMaterial g) -> new ArrayList<>()).add(i);
					} else if (i instanceof ItemMetalCrackHammer) {
						allHammers.put(m, i);
					} else if (i instanceof ItemSword) {
						allSwords.put(m, i);
					} else if (i instanceof ItemHoe) {
						allHoes.put(m, i);
					} else if (i instanceof ItemAxe) {
						allAxes.put(m, i);
					} else if (i instanceof ItemPickaxe) {
						allPickAxes.put(m, i);
					} else if (i instanceof ItemSpade) {
						allShovels.put(m, i);
					} else if (i instanceof ItemMetalIngot) {
						allIngots.put(m, i);
					}
				}
			}
		});

		for (final MetalMaterial m : Materials.getAllMaterials()) {
			final float value = m.hardness + m.strength + m.magicAffinity + m.getToolHarvestLevel();
			if (m.isRare) {
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

			if (allIngots.containsKey(m)) {
				final ITradeList[] ingotTrades = makeTradePalette(makePurchasePalette(emeraldPurch, 12, allIngots.get(m)), makeSalePalette(emeraldSale, 12, allIngots.get(m)));
				tradesTable.computeIfAbsent(armorsmith, (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(ingotTrades));
				tradesTable.computeIfAbsent(weaponsmith, (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(ingotTrades));
				tradesTable.computeIfAbsent(toolsmith, (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(ingotTrades));
			}
			if (allHammers.containsKey(m) && allPickAxes.containsKey(m) && allAxes.containsKey(m) && allShovels.containsKey(m) && allHoes.containsKey(m)) {
				tradesTable.computeIfAbsent(toolsmith, (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makeTradePalette(makePurchasePalette(emeraldPurch, 1, allPickAxes.get(m), allAxes.get(m), allShovels.get(m), allHoes.get(m)))));
				tradesTable.computeIfAbsent((3 << 16) | (3 << 8) | (tradeLevel + 1), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makeTradePalette(makePurchasePalette(emeraldPurch, 1, allHammers.get(m)))));
			}
			if (allSwords.containsKey(m)) {
				tradesTable.computeIfAbsent(weaponsmith, (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makeTradePalette(makePurchasePalette((emeraldPurch + (int) (m.getBaseAttackDamage() / 2)) - 1, 1, allSwords.get(m)))));
			}
			if (allArmors.containsKey(m)) {
				tradesTable.computeIfAbsent(armorsmith, (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makeTradePalette(makePurchasePalette(emeraldPurch + (int) (m.hardness / 2), 1, allArmors.get(m).toArray(new Item[0])))));
			}

			if (m.magicAffinity > 5) {
				if (allHammers.containsKey(m)) {
					tradesTable.computeIfAbsent((3 << 16) | (3 << 8) | (tradeLevel + 2), (Integer key) -> new ArrayList<>()).addAll(Collections.singletonList(new ListEnchantedItemForEmeralds(allHammers.get(m), new PriceInfo(emeraldPurch + 7, emeraldPurch + 12))));
				}
				if (allPickAxes.containsKey(m)) {
					tradesTable.computeIfAbsent((3 << 16) | (3 << 8) | (tradeLevel + 1), (Integer key) -> new ArrayList<>()).addAll(Collections.singletonList(new ListEnchantedItemForEmeralds(allPickAxes.get(m), new PriceInfo(emeraldPurch + 7, emeraldPurch + 12))));
				}
				if (allArmors.containsKey(m)) {
					for (int i = 0; i < allArmors.get(m).size(); i++) {
						tradesTable.computeIfAbsent((3 << 16) | (1 << 8) | (tradeLevel + 1), (Integer key) -> new ArrayList<>()).addAll(Collections.singletonList(new ListEnchantedItemForEmeralds(allArmors.get(m).get(i), new PriceInfo(emeraldPurch + 7 + (int) (m.hardness / 2), emeraldPurch + 12 + (int) (m.hardness / 2)))));
					}
				}
				if (allSwords.containsKey(m)) {
					tradesTable.computeIfAbsent((3 << 16) | (2 << 8) | (tradeLevel + 1), (Integer key) -> new ArrayList<>()).addAll(Collections.singletonList(new ListEnchantedItemForEmeralds(allSwords.get(m), new PriceInfo(emeraldPurch + 7 + (int) (m.getBaseAttackDamage() / 2) - 1, emeraldPurch + 12 + (int) (m.getBaseAttackDamage() / 2) - 1))));
				}
			}
		}

		if (Loader.instance().activeModContainer().getModId().equals("basemetals")) {
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
//				FMLLog.log(Level.ERROR, ex, "Java Reflection Exception");
			}
		}
	}
}