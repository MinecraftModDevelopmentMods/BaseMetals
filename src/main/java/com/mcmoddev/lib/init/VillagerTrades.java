package com.mcmoddev.lib.init;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nonnull;

import com.mcmoddev.basemetals.util.VillagerTradeHelper;
import com.mcmoddev.lib.data.MaterialStats;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager.ITradeList;
import net.minecraft.entity.passive.EntityVillager.ListEnchantedItemForEmeralds;
import net.minecraft.entity.passive.EntityVillager.PriceInfo;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
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
	protected static final ResourceLocation SMITH_RL = new ResourceLocation("minecraft:smith");
	protected static final int ARMOR_SMITH_ID = 1;
	protected static final int WEAPON_SMITH_ID = 2;
	protected static final int TOOL_SMITH_ID = 3;

	protected static final int TRADES_PER_LEVEL = 4;

	protected VillagerTrades() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/**
	 *
	 */
	public static void init() {
		registerCommonTrades();
	}

	protected static void registerCommonTrades() {
		final String modid = Loader.instance().activeModContainer().getModId();
		// integer is used as byte data: (unused) (profession) (career) (level)
		final Map<Integer, List<ITradeList>> tradesTable = new HashMap<>();

		// Minecraft stores trades in a 4D array:
		// [Profession ID][Sub-profession ID][villager level - 1][trades]

		for (final MMDMaterial material : Materials.getMaterialsByMod(modid)) {

			if (material == null) {
				return;
			}

			final float value = material.getStat(MaterialStats.HARDNESS) + material.getStat(MaterialStats.STRENGTH) + material.getStat(MaterialStats.MAGICAFFINITY) + material.getToolHarvestLevel();
			if (material.isRare()) {
				continue;
			}

			// For reference, Iron has a value of 21.5, Gold would be 14, Copper
			// is 14, and Diamond is 30
			final int emeraldPurch = emeraldPurchaseValue(value);
			final int emeraldPurchEnchantedMin = emeraldPurch + 7;
			final int emeraldPurchEnchantedMax = emeraldPurch + 12;
			final int emeraldSale = emeraldSaleValue(value);
			final int tradeLevel = tradeLevel(value);

			if ((emeraldPurch > 64) || (emeraldSale > 64)) {
				continue; // Too expensive
			}

			for (Names name : Arrays.asList(Names.HELMET, Names.CHESTPLATE, Names.LEGGINGS, Names.BOOTS)) {
				if (material.hasItem(name)) {
					final ItemStack itemStack = material.getItemStack(name);
					final ITradeList[] armorTrades = makeTradePalette(makePurchasePalette(emeraldPurch + (int) (material.getStat(MaterialStats.HARDNESS) / 2), itemStack));
					VillagerTradeHelper.insertTrades(SMITH_RL, ARMOR_SMITH_ID, tradeLevel, armorTrades);
					if (material.getStat(MaterialStats.MAGICAFFINITY) > 5) {
						tradesTable.computeIfAbsent(ARMOR_SMITH | (tradeLevel + 1), (Integer key) -> new ArrayList<>()).addAll(Collections.singletonList(new ListEnchantedItemForEmeralds(itemStack.getItem(), new PriceInfo(emeraldPurchEnchantedMin + (int) (material.getStat(MaterialStats.HARDNESS) / 2), emeraldPurchEnchantedMax + (int) (material.getStat(MaterialStats.HARDNESS) / 2)))));
//						VillagerTradeHelper.insertTrades(SMITH_RL, ARMOR_SMITH_ID, tradeLevel, enchantedArmorTrades);
					}
				}
			}
			if (material.hasItem(Names.CRACKHAMMER)) {
				final ItemStack itemStack = material.getItemStack(Names.CRACKHAMMER);
				final ITradeList[] hammerTrades = makeTradePalette(makePurchasePalette(emeraldPurch, itemStack));
				VillagerTradeHelper.insertTrades(SMITH_RL, TOOL_SMITH_ID, tradeLevel, hammerTrades);
				if (material.getStat(MaterialStats.MAGICAFFINITY) > 5) {
					tradesTable.computeIfAbsent(TOOL_SMITH | (tradeLevel + 2), (Integer key) -> new ArrayList<>()).addAll(Collections.singletonList(new ListEnchantedItemForEmeralds(itemStack.getItem(), new PriceInfo(emeraldPurchEnchantedMin, emeraldPurchEnchantedMax))));
//					VillagerTradeHelper.insertTrades(SMITH_RL, TOOL_SMITH_ID, tradeLevel, enchantedHammerTrades);
				}
			}
			if (material.hasItem(Names.SWORD)) {
				final ItemStack itemStack = material.getItemStack(Names.SWORD);
				final ITradeList[] swordTrades = makeTradePalette(makePurchasePalette((emeraldPurch + (int) (material.getBaseAttackDamage() / 2)) - 1, itemStack));
				VillagerTradeHelper.insertTrades(SMITH_RL, WEAPON_SMITH_ID, tradeLevel, swordTrades);
				if (material.getStat(MaterialStats.MAGICAFFINITY) > 5) {
					tradesTable.computeIfAbsent(WEAPON_SMITH | (tradeLevel + 1), (Integer key) -> new ArrayList<>()).addAll(Collections.singletonList(new ListEnchantedItemForEmeralds(itemStack.getItem(), new PriceInfo(emeraldPurchEnchantedMin + (int) (material.getBaseAttackDamage() / 2) - 1, emeraldPurchEnchantedMax + (int) (material.getBaseAttackDamage() / 2) - 1))));
//					VillagerTradeHelper.insertTrades(SMITH_RL, ARMOR_SMITH_ID, tradeLevel, enchantedSwordTrades);
				}
			}
			for (Names name : Arrays.asList(Names.AXE, Names.HOE, Names.SHOVEL)) {
				if (material.hasItem(name)) {
					final ItemStack itemStack = material.getItemStack(name);
					final ITradeList[] toolTrades = makeTradePalette(makePurchasePalette(emeraldPurch, itemStack));
					VillagerTradeHelper.insertTrades(SMITH_RL, TOOL_SMITH_ID, tradeLevel, toolTrades);
				}
			}
			if (material.hasItem(Names.PICKAXE)) {
				final ItemStack itemStack = material.getItemStack(Names.PICKAXE);
				final ITradeList[] pickaxeTrades = makeTradePalette(makePurchasePalette(emeraldPurch, itemStack));
				VillagerTradeHelper.insertTrades(SMITH_RL, TOOL_SMITH_ID, tradeLevel, pickaxeTrades);
				if (material.getStat(MaterialStats.MAGICAFFINITY) > 5) {
					tradesTable.computeIfAbsent(TOOL_SMITH | (tradeLevel + 1), (Integer key) -> new ArrayList<>()).addAll(Collections.singletonList(new ListEnchantedItemForEmeralds(itemStack.getItem(), new PriceInfo(emeraldPurchEnchantedMin, emeraldPurchEnchantedMax))));
//					VillagerTradeHelper.insertTrades(SMITH_RL, TOOL_SMITH_ID, tradeLevel, enchantedPickaxeTrades);
				}
			}
			if (material.hasItem(Names.INGOT)) {
				final ItemStack itemStack = material.getItemStack(Names.INGOT, 12);
				if ((!itemStack.getItem().equals(net.minecraft.init.Items.EMERALD)) && (!itemStack.getItem().equals(net.minecraft.init.Items.DIAMOND))) {
					final ITradeList[] ingotTrades = makeTradePalette(makePurchasePalette(emeraldPurch, itemStack), makeSalePalette(emeraldSale, itemStack));
					VillagerTradeHelper.insertTrades(SMITH_RL, ARMOR_SMITH_ID, tradeLevel, ingotTrades);
					VillagerTradeHelper.insertTrades(SMITH_RL, WEAPON_SMITH_ID, tradeLevel, ingotTrades);
					VillagerTradeHelper.insertTrades(SMITH_RL, TOOL_SMITH_ID, tradeLevel, ingotTrades);
				}
			}
		}

		commitTrades(tradesTable);
	}

	protected static void registerModSpecificTrades() {
	}

	/**
	 * Commits a Trade Table
	 *
	 * @param tradesTable Trade Table to commit
	 */
	protected static void commitTrades(@Nonnull final Map<Integer, List<ITradeList>> tradesTable) {

		for (final Integer k : tradesTable.keySet()) {
			final List<ITradeList> trades = tradesTable.get(k);
			final int profession = (k >> 16) & 0xFF;
			final int career = (k >> 8) & 0xFF;
			final int level = k & 0xFF;

			VillagerTradeHelper.insertTrades(profession, career, level, new MultiTradeGenerator(TRADES_PER_LEVEL, trades));
		}
	}

	/**
	 *
	 * @param value Purchase value
	 * @return int
	 */
	protected static int emeraldPurchaseValue(@Nonnull final float value) {
		return Math.max(1, (int) (value * 0.2F));
	}

	/**
	 *
	 * @param value Sale value
	 * @return int
	 */
	protected static int emeraldSaleValue(@Nonnull final float value) {
		return Math.max(1, emeraldPurchaseValue(value) / 3);
	}

	/**
	 *
	 * @param value Trade Level
	 * @return int
	 */
	protected static int tradeLevel(@Nonnull final float value) {
		return Math.max(1, Math.min(4, (int) (value * 0.1F)));
	}

	/**
	 *
	 * @param baseValue Base Value
	 * @return int
	 */
	protected static int fluctuation(@Nonnull final int baseValue) {
		if (baseValue <= 1) {
			return 0;
		}
		return Math.max(2, baseValue / 4);
	}

	/**
	 * Creates a trade list for a single item (Buy Only)
	 * @param emeraldPrice Price
	 * @param stackSize Size of Stack
	 * @param items The Items
	 * @return ITradeList
	 */
	protected static ITradeList[] makePurchasePalette(@Nonnull final int emeraldPrice, @Nonnull final int stackSize, @Nonnull final Item... items) {
		final ItemStack[] itemStacks = new ItemStack[items.length];
		for (int i = 0; i < items.length; i++) {
			itemStacks[i] = new ItemStack(items[i], stackSize);
		}
		return makePurchasePalette(emeraldPrice, itemStacks);
	}

	/**
	 * Creates a trade list for a single item (Buy Only)
	 * @param emeraldPrice Purchase Value
	 * @param itemStacks The ItemStacks
	 * @return ITradeList
	 */
	protected static ITradeList[] makePurchasePalette(@Nonnull final int emeraldPrice, @Nonnull final ItemStack... itemStacks) {
		final ITradeList[] trades = new ITradeList[itemStacks.length];
		for (int i = 0; i < itemStacks.length; i++) {
			trades[i] = new SimpleTrade(
					new ItemStack(net.minecraft.init.Items.EMERALD, emeraldPrice, 0), fluctuation(emeraldPrice),
					itemStacks[i], 0);
		}
		return trades;
	}

	/**
	 * Creates a trade list for a single item (Sell Only)
	 *
	 * @param emeraldValue Sale Value
	 * @param stackSize Size of the Stack
	 * @param items The Items
	 * @return ITradeList
	 */
	protected static ITradeList[] makeSalePalette(@Nonnull final int emeraldValue, @Nonnull final int stackSize, @Nonnull final Item... items) {
		final ItemStack[] itemStacks = new ItemStack[items.length];
		for (int i = 0; i < items.length; i++) {
			itemStacks[i] = new ItemStack(items[i], stackSize);
		}
		return makeSalePalette(emeraldValue, itemStacks);
	}

	/**
	 * Creates a trade list for a single item (Sell Only)
	 *
	 * @param emeraldValue Sale Value
	 * @param itemStacks The ItemStacks
	 * @return ITradeList
	 */
	protected static ITradeList[] makeSalePalette(@Nonnull final int emeraldValue, @Nonnull final ItemStack... itemStacks) {
		final ITradeList[] trades = new ITradeList[itemStacks.length];
		for (int i = 0; i < itemStacks.length; i++) {
			trades[i] = new SimpleTrade(
					itemStacks[i], fluctuation(itemStacks[i].getCount()),
					new ItemStack(net.minecraft.init.Items.EMERALD, emeraldValue, 0), 0);
		}
		return trades;
	}

	/**
	 * Creates a trade list (Buy and Sell)
	 *
	 * @param list The Trade Lists
	 * @return ITradeList
	 */
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
			this(in1, v1, ItemStack.EMPTY, 0, out, vout, -1, -1);
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
			this(in1, 0, ItemStack.EMPTY, 0, out, 0, -1, -1);
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
			ItemStack in2 = ItemStack.EMPTY;
			if (input2 != ItemStack.EMPTY && input2.getItem() != null) {
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
