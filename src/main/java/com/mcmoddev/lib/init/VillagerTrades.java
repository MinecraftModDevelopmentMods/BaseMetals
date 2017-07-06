package com.mcmoddev.lib.init;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nonnull;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.util.VillagerTradeHelper;
import com.mcmoddev.lib.data.MaterialStats;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.item.ItemMMDCrackHammer;
import com.mcmoddev.lib.item.ItemMMDIngot;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager.ITradeList;
import net.minecraft.entity.passive.EntityVillager.ListEnchantedItemForEmeralds;
import net.minecraft.entity.passive.EntityVillager.PriceInfo;
import net.minecraft.item.*;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.common.Loader;

/**
 *
 * @author DrCyano
 *
 */
public abstract class VillagerTrades {

	protected static final int ARMOR_SMITH = (3 << 16) | (1 << 8);
	protected static final int WEAPON_SMITH = (3 << 16) | (2 << 8);
	protected static final int TOOL_SMITH = (3 << 16) | (3 << 8);

	private static boolean initDone = false;

	protected static final int TRADES_PER_LEVEL = 4;

	protected VillagerTrades() {
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
		String modid = Loader.instance().activeModContainer().getModId();
		// integer is used as byte data: (unused) (profession) (career) (level)
		final Map<Integer, List<ITradeList>> tradesTable = new HashMap<>();

		// Minecraft stores trades in a 4D array:
		// [Profession ID][Sub-profession ID][villager level - 1][trades]

		final int size = Materials.getMaterialsByMod(modid).size();
		final Map<MMDMaterial, List<Item>> allArmors = new HashMap<>(size);
		final Map<MMDMaterial, Item> allHammers = new HashMap<>(size);
		final Map<MMDMaterial, Item> allSwords = new HashMap<>(size);
		final Map<MMDMaterial, Item> allHoes = new HashMap<>(size);
		final Map<MMDMaterial, Item> allAxes = new HashMap<>(size);
		final Map<MMDMaterial, Item> allPickAxes = new HashMap<>(size);
		final Map<MMDMaterial, Item> allShovels = new HashMap<>(size);
		final Map<MMDMaterial, Item> allIngots = new HashMap<>(size);

		// @SuppressWarnings("unused")
		// final Map<Item, Integer> tradeLevelMap = new HashMap<>();

		for (final MMDMaterial material : Materials.getMaterialsByMod(modid)) {
			if (material == null) {
				return;
			}

			if (material.hasItem(Names.HELMET)) {
				final Item item = material.getItem(Names.HELMET);
				if (item instanceof ItemArmor) {
					allArmors.computeIfAbsent(material, (MMDMaterial g) -> new ArrayList<>()).add(item);
				}
			}
			if (material.hasItem(Names.CHESTPLATE)) {
				final Item item = material.getItem(Names.CHESTPLATE);
				if (item instanceof ItemArmor) {
					allArmors.computeIfAbsent(material, (MMDMaterial g) -> new ArrayList<>()).add(item);
				}
			}
			if (material.hasItem(Names.LEGGINGS)) {
				final Item item = material.getItem(Names.LEGGINGS);
				if (item instanceof ItemArmor) {
					allArmors.computeIfAbsent(material, (MMDMaterial g) -> new ArrayList<>()).add(item);
				}
			}
			if (material.hasItem(Names.BOOTS)) {
				final Item item = material.getItem(Names.BOOTS);
				if (item instanceof ItemArmor) {
					allArmors.computeIfAbsent(material, (MMDMaterial g) -> new ArrayList<>()).add(item);
				}
			}
			if (material.hasItem(Names.CRACKHAMMER)) {
				final Item item = material.getItem(Names.CRACKHAMMER); 
				if (item instanceof ItemMMDCrackHammer) {
					allHammers.put(material, item);
				}
			}
			if (material.hasItem(Names.SWORD)) {
				final Item item = material.getItem(Names.SWORD); 
				if (item instanceof ItemSword) {
					allSwords.put(material, item);
				}
			}
			if (material.hasItem(Names.HOE)) {
				final Item item = material.getItem(Names.HOE);
				if (item instanceof ItemHoe) {
					allHoes.put(material, item);
				}
			}
			if (material.hasItem(Names.AXE)) {
				final Item item = material.getItem(Names.AXE);
				if (item instanceof ItemAxe) {
					allAxes.put(material, item);
				}
			}
			if (material.hasItem(Names.PICKAXE)) {
				final Item item = material.getItem(Names.PICKAXE); 
				if (item instanceof ItemPickaxe) {
					allPickAxes.put(material, item);
				}
			}
			if (material.hasItem(Names.SHOVEL)) {
				final Item item = material.getItem(Names.SHOVEL);
				if (item instanceof ItemSpade) {
					allShovels.put(material, item);
				}
			}
			if (material.hasItem(Names.INGOT)) {
				final Item item = material.getItem(Names.INGOT); 
				if (item instanceof ItemMMDIngot) {
					allIngots.put(material, item);
				}
			}
		}

		for (final MMDMaterial material : Materials.getMaterialsByMod(modid)) {
			final float value = material.getStat(MaterialStats.HARDNESS) + material.getStat(MaterialStats.STRENGTH) + material.getStat(MaterialStats.MAGICAFFINITY) + material.getToolHarvestLevel();
			if (material.isRare()) {
				continue;
			}

			// For reference, Iron has a value of 21.5, Gold would be 14, Copper
			// is 14, and Diamond is 30
			final int emeraldPurch = emeraldPurchaseValue(value);
			final int emeraldSale = emeraldSaleValue(value);
			final int tradeLevel = tradeLevel(value);

			if ((emeraldPurch > 64) || (emeraldSale > 64)) {
				continue; // Too expensive
			}

			if (allIngots.containsKey(material)) {
				final ITradeList[] ingotTrades = makeTradePalette(makePurchasePalette(emeraldPurch, 12, allIngots.get(material)), makeSalePalette(emeraldSale, 12, allIngots.get(material)));
				tradesTable.computeIfAbsent(ARMOR_SMITH | (tradeLevel), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(ingotTrades));
				tradesTable.computeIfAbsent(WEAPON_SMITH | (tradeLevel), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(ingotTrades));
				tradesTable.computeIfAbsent(TOOL_SMITH | (tradeLevel), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(ingotTrades));
			}

			if (allHammers.containsKey(material) && allPickAxes.containsKey(material) && allAxes.containsKey(material) && allShovels.containsKey(material) && allHoes.containsKey(material)) {
				tradesTable.computeIfAbsent(TOOL_SMITH | (tradeLevel), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makeTradePalette(makePurchasePalette(emeraldPurch, 1, allPickAxes.get(material), allAxes.get(material), allShovels.get(material), allHoes.get(material)))));
				tradesTable.computeIfAbsent(TOOL_SMITH | (tradeLevel + 1), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makeTradePalette(makePurchasePalette(emeraldPurch, 1, allHammers.get(material)))));
			}

			if (allSwords.containsKey(material)) {
				tradesTable.computeIfAbsent(WEAPON_SMITH | (tradeLevel), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makeTradePalette(makePurchasePalette((emeraldPurch + (int) (material.getBaseAttackDamage() / 2)) - 1, 1, allSwords.get(material)))));
			}

			if (allArmors.containsKey(material)) {
				tradesTable.computeIfAbsent(ARMOR_SMITH | (tradeLevel), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makeTradePalette(makePurchasePalette(emeraldPurch + (int) (material.getStat(MaterialStats.HARDNESS) / 2), 1, allArmors.get(material).toArray(new Item[0])))));
			}

			if (material.getStat(MaterialStats.MAGICAFFINITY) > 5) {
				if (allHammers.containsKey(material)) {
					tradesTable.computeIfAbsent(TOOL_SMITH | (tradeLevel + 2), (Integer key) -> new ArrayList<>()).addAll(Collections.singletonList(new ListEnchantedItemForEmeralds(allHammers.get(material), new PriceInfo(emeraldPurch + 7, emeraldPurch + 12))));
				}

				if (allPickAxes.containsKey(material)) {
					tradesTable.computeIfAbsent(TOOL_SMITH | (tradeLevel + 1), (Integer key) -> new ArrayList<>()).addAll(Collections.singletonList(new ListEnchantedItemForEmeralds(allPickAxes.get(material), new PriceInfo(emeraldPurch + 7, emeraldPurch + 12))));
				}

				if (allArmors.containsKey(material)) {
					for (int i = 0; i < allArmors.get(material).size(); i++) {
						tradesTable.computeIfAbsent(ARMOR_SMITH | (tradeLevel + 1), (Integer key) -> new ArrayList<>()).addAll(Collections.singletonList(new ListEnchantedItemForEmeralds(allArmors.get(material).get(i), new PriceInfo(emeraldPurch + 7 + (int) (material.getStat(MaterialStats.HARDNESS) / 2), emeraldPurch + 12 + (int) (material.getStat(MaterialStats.HARDNESS) / 2)))));
					}
				}

				if (allSwords.containsKey(material)) {
					tradesTable.computeIfAbsent(WEAPON_SMITH | (tradeLevel + 1), (Integer key) -> new ArrayList<>()).addAll(Collections.singletonList(new ListEnchantedItemForEmeralds(allSwords.get(material), new PriceInfo(emeraldPurch + 7 + (int) (material.getBaseAttackDamage() / 2) - 1, emeraldPurch + 12 + (int) (material.getBaseAttackDamage() / 2) - 1))));
				}
			}
		}

		commitTrades(tradesTable);
	}

	protected static void registerModSpecificTrades() {
	}

	protected static void commitTrades(@Nonnull final Map<Integer, List<ITradeList>> tradesTable) {

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

	protected static int emeraldPurchaseValue(@Nonnull final float value) {
		return Math.max(1, (int) (value * 0.2F));
	}

	protected static int emeraldSaleValue(@Nonnull final float value) {
		return Math.max(1, emeraldPurchaseValue(value) / 3);
	}

	protected static int tradeLevel(@Nonnull final float value) {
		return Math.max(1, Math.min(4, (int) (value * 0.1F)));
	}

	protected static int fluctuation(@Nonnull final int baseValue) {
		if (baseValue <= 1) {
			return 0;
		}
		return Math.max(2, baseValue / 4);
	}

	protected static ITradeList[] makePurchasePalette(@Nonnull final int emeraldPrice, @Nonnull final int stackSize, @Nonnull final Item... items) {
		final ITradeList[] trades = new ITradeList[items.length];
		for (int i = 0; i < items.length; i++) {
			final Item item = items[i];
			trades[i] = new SimpleTrade(new ItemStack(net.minecraft.init.Items.EMERALD, emeraldPrice, 0),
					fluctuation(emeraldPrice), null, 0, new ItemStack(item, stackSize, 0), 0);
		}
		return trades;
	}

	protected static ITradeList[] makeSalePalette(@Nonnull final int emeraldValue, @Nonnull final int stackSize, @Nonnull final Item... items) {
		final ITradeList[] trades = new ITradeList[items.length];
		for (int i = 0; i < items.length; i++) {
			final Item item = items[i];
			trades[i] = new SimpleTrade(new ItemStack(item, stackSize, 0), fluctuation(stackSize), null, 0,
					new ItemStack(net.minecraft.init.Items.EMERALD, emeraldValue, 0), 0);
		}
		return trades;
	}

	protected static ITradeList[] makeTradePalette(@Nonnull final ITradeList[]... list) {
		if (list.length == 1) {
			return list[0];
		}
		int totalsize = 0;
		for (final ITradeList[] e : list) {
			totalsize += e.length;
		}
		final ITradeList[] concat = new ITradeList[totalsize];
		int index = 0;
		int element = 0;
		while (index < totalsize) {
			System.arraycopy(list[element], 0, concat, index, list[element].length);
			index += list[element].length;
			element++;
		}
		return concat;
	}

	/**
	 * This ITradeList object holds a list of ITradeLists and picks a few at
	 * random to place in a merchant's trade menu.
	 */
	public static class MultiTradeGenerator implements ITradeList {
		private final int numberOfTrades;
		private final ITradeList[] trades;

		/**
		 * Creates an ITradeList instance that randomly adds multiple trades at
		 * a time
		 * 
		 * @param tradeCount
		 *            Number of trades to add to the merchant's trade menu
		 * @param tradePalette
		 *            The trades to randomly choose from
		 */
		public MultiTradeGenerator(@Nonnull final int tradeCount, @Nonnull final List<ITradeList> tradePalette) {
			numberOfTrades = Math.min(tradeCount, tradePalette.size());
			trades = tradePalette.toArray(new ITradeList[tradePalette.size()]);
		}

		@Override
		public void addMerchantRecipe(@Nonnull final IMerchant merchant, @Nonnull final MerchantRecipeList recipeList, @Nonnull final Random random) {
			for (int n = 0; n < numberOfTrades; n++) {
				trades[random.nextInt(trades.length)].addMerchantRecipe(merchant, recipeList, random);
			}
		}
		
		/**
		 * For debugging purposes only
		 * 
		 * @return String representation
		 */
		@Override
		public String toString() {
			return MultiTradeGenerator.class.getSimpleName() + ": " + numberOfTrades + " trades chosen from "
					+ Arrays.toString(trades);
		}
	}

	/**
	 * A simple, easy to use ITradeList class that holds a single trade recipe
	 */
	public static class SimpleTrade implements ITradeList {

		private final ItemStack input1;
		private final int maxInputMarkup1;
		private final ItemStack input2;
		private final int maxInputMarkup2;
		private final ItemStack output;
		private final int maxOutputMarkup;
		private final int maxTrades;
		private final int maxTradeVariation;

		/**
		 * Full constructor for making a trade recipe
		 * 
		 * @param in1
		 *            Item for the left purchase price trade slot
		 * @param variation1
		 *            range of variation in quantity of <code>in1</code>
		 * @param in2
		 *            Item for the right purchase price trade slot. Can be
		 *            <code>null</code> (and usually is)
		 * @param variation2
		 *            range of variation in quantity of <code>in2</code>
		 * @param out
		 *            The item to be purchased (trade recipe output slot)
		 * @param variationOut
		 *            range of variation in quantity of <code>out</code>
		 * @param numberTrades
		 *            Max number of trades before this recipe is invalidated (-1
		 *            for infinite trading)
		 * @param tradeNumberVariation
		 *            range of variation in value of <code>numberTrades</code>
		 *            (-1 to disable)
		 */
		public SimpleTrade(@Nonnull final ItemStack in1, @Nonnull final int variation1, final ItemStack in2, @Nonnull final int variation2, @Nonnull final ItemStack out,
				@Nonnull final int variationOut, @Nonnull final int numberTrades, @Nonnull final int tradeNumberVariation) {
			input1 = in1;
			maxInputMarkup1 = variation1;
			input2 = in2;
			maxInputMarkup2 = variation2;
			output = out;
			maxOutputMarkup = variationOut;
			maxTrades = numberTrades;
			maxTradeVariation = tradeNumberVariation;
		}

		/**
		 * Constructor for making a simple two-for-one trade recipe with price
		 * variation
		 * 
		 * @param in1
		 *            Item for the left purchase price trade slot
		 * @param v1
		 *            range of variation in quantity of <code>in1</code>
		 * @param in2
		 *            Item for the right purchase price trade slot. Can be
		 *            <code>null</code> (and usually is)
		 * @param v2
		 *            range of variation in quantity of <code>in2</code>
		 * @param out
		 *            The item to be purchased (trade recipe output slot)
		 * @param vout
		 *            range of variation in quantity of <code>out</code>
		 */
		public SimpleTrade(@Nonnull final ItemStack in1, @Nonnull final int v1, final ItemStack in2, @Nonnull final int v2, @Nonnull final ItemStack out, @Nonnull final int vout) {
			this(in1, v1, in2, v2, out, vout, -1, -1);
		}

		/**
		 * Constructor for making a simple one-for-one trade with price
		 * variation
		 * 
		 * @param in1
		 *            Item for the left purchase price trade slot
		 * @param v1
		 *            range of variation in quantity of <code>in1</code>
		 * @param out
		 *            The item to be purchased (trade recipe output slot)
		 * @param vout
		 *            range of variation in quantity of <code>out</code>
		 */
		public SimpleTrade(@Nonnull final ItemStack in1, @Nonnull final int v1, @Nonnull final ItemStack out, @Nonnull final int vout) {
			this(in1, v1, null, 0, out, vout, -1, -1);
		}

		/**
		 * Constructor for making a simple one-for-one trade
		 * 
		 * @param in1
		 *            Item for the left purchase price trade slot
		 * @param out
		 *            The item to be purchased (trade recipe output slot)
		 */
		public SimpleTrade(@Nonnull final ItemStack in1, @Nonnull final ItemStack out) {
			this(in1, 0, null, 0, out, 0, -1, -1);
		}

		@Override
		public String toString() {
			return input1 + " + " + input2 + " => " + output;
		}
		
		/**
		 * Invoked when the merchant generates its trade menu
		 * 
		 * @param recipeList
		 *            existing trade menu
		 * @param random
		 *            a psuedorandom number generator instance
		 */
		@Override
		public void addMerchantRecipe(@Nonnull final IMerchant merchant, @Nonnull final MerchantRecipeList recipeList, @Nonnull final Random random) {
			int numTrades = -1;
			if (maxTrades > 0) {
				if (maxTradeVariation > 0) {
					numTrades = Math.max(1, maxTrades + random.nextInt(maxTradeVariation) - maxTradeVariation / 2);
				} else {
					numTrades = maxTrades;
				}
			}
			ItemStack in1 = input1.copy();
			if (maxInputMarkup1 > 0) {
				in1.setCount(in1.getCount() + random.nextInt(maxInputMarkup1));
			}
			ItemStack in2 = null;
			if (input2 != null && input2.getItem() != null) {
				in2 = input2.copy();
				if (maxInputMarkup2 > 0) {
					in2.setCount(in2.getCount() + random.nextInt(maxInputMarkup2));
				}
			}
			ItemStack out = output.copy();
			if (maxOutputMarkup > 0) {
				out.setCount(out.getCount() + random.nextInt(maxOutputMarkup));
			}

			if (numTrades > 0) {
				recipeList.add(new MerchantRecipe(in1, in2, out, 0, numTrades));
			} else {
				recipeList.add(new MerchantRecipe(in1, in2, out));
			}
		}
	}
}
