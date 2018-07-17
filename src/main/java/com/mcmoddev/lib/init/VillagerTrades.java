package com.mcmoddev.lib.init;

import java.util.Arrays;
import java.util.Collections;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

	protected static final ResourceLocation SMITH_RL = new ResourceLocation("minecraft:smith");
	protected static final int ARMOR_SMITH_ID = 1;
	protected static final int WEAPON_SMITH_ID = 2;
	protected static final int TOOL_SMITH_ID = 3;
	protected static final int TRADES_PER_LEVEL = 4;

	protected static final int ENCHANTED_MIN = 7;
	protected static final int ENCHANTED_MAX = 12;

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

		for (final MMDMaterial material : Materials.getMaterialsByMod(modid)) {
			if (material.isEmpty()) {
				return;
			}

			final float value = material.getStat(MaterialStats.HARDNESS)
					+ material.getStat(MaterialStats.STRENGTH)
					+ material.getStat(MaterialStats.MAGICAFFINITY)
					+ material.getToolHarvestLevel();
			if (material.isRare()) {
				continue;
			}

			// For reference, Iron has a value of 21.5, Gold would be 14, Copper
			// is 14, and Diamond is 30
			final int emeraldPurch = emeraldPurchaseValue(value);
			final int emeraldSale = emeraldSaleValue(value);
			final int tradeLevel = tradeLevel(value);
			final float magicAffinity = material.getStat(MaterialStats.MAGICAFFINITY);

			if ((emeraldPurch > 64) || (emeraldSale > 64)) {
				continue; // Too expensive
			}

			registerArmorTrades(material, tradeLevel, emeraldPurch, magicAffinity);
			registerWeaponTrades(material, tradeLevel, emeraldPurch, magicAffinity);
			registerToolTrades(material, tradeLevel, emeraldPurch, magicAffinity);
			registerIngotTrades(material, tradeLevel, emeraldPurch, emeraldSale);
		}
	}

	protected static void registerArmorTrades(@Nonnull final MMDMaterial material, final int tradeLevel, final int emeraldPurch, final float magicAffinity) {
		for (final Names name : Arrays.asList(Names.HELMET, Names.CHESTPLATE, Names.LEGGINGS, Names.BOOTS)) {
			if (material.hasItem(name)) {
				final ItemStack itemStack = material.getItemStack(name);
				registerTrade(ARMOR_SMITH_ID, itemStack, emeraldPurch + (int) (material.getStat(MaterialStats.HARDNESS) / 2), tradeLevel);
				if (material.getStat(MaterialStats.MAGICAFFINITY) > 5) {
					PriceInfo priceRange = new PriceInfo(
							((emeraldPurch + ENCHANTED_MIN)
									+ (int) (material.getBaseAttackDamage() / 2))
									- 1,
							((emeraldPurch + ENCHANTED_MAX)
									+ (int) (material.getBaseAttackDamage() / 2))
									- 1);
					registerEnchantedTrade(ARMOR_SMITH_ID, itemStack, priceRange, (tradeLevel + 1), magicAffinity);
				}
			}
		}
	}

	protected static void registerToolTrades(@Nonnull final MMDMaterial material, final int tradeLevel, final int emeraldPurch, final float magicAffinity) {
		for (final Names name : Arrays.asList(Names.AXE, Names.PICKAXE, Names.HOE, Names.SHOVEL, Names.CRACKHAMMER, Names.SCYTHE)) {
			if (material.hasItem(name)) {
				final ItemStack itemStack = material.getItemStack(name);
				registerTrade(TOOL_SMITH_ID, itemStack, emeraldPurch, tradeLevel);
				final PriceInfo priceRange = new PriceInfo(emeraldPurch + ENCHANTED_MIN, emeraldPurch + ENCHANTED_MAX);
				if (!name.equals(Names.CRACKHAMMER)) {
					registerEnchantedTrade(TOOL_SMITH_ID, itemStack, priceRange, (tradeLevel + 1), magicAffinity);
				} else {
					registerEnchantedTrade(TOOL_SMITH_ID, itemStack, priceRange, (tradeLevel + 2), magicAffinity);
				}
			}
		}
	}

	protected static void registerWeaponTrades(@Nonnull final MMDMaterial material, final int tradeLevel, final int emeraldPurch, final float magicAffinity) {
		for (final Names name : Arrays.asList(Names.SWORD, Names.CROSSBOW, Names.BOW)) {
			if (material.hasItem(name)) {
				final ItemStack itemStack = material.getItemStack(Names.SWORD);
				registerTrade(WEAPON_SMITH_ID, itemStack, (emeraldPurch + (int) (material.getBaseAttackDamage() / 2)) - 1, tradeLevel);
				final PriceInfo priceRange = new PriceInfo(
						((emeraldPurch + ENCHANTED_MIN)
								+ (int) (material.getBaseAttackDamage() / 2))
								- 1,
						((emeraldPurch + ENCHANTED_MAX)
								+ (int) (material.getBaseAttackDamage() / 2))
								- 1);
				registerEnchantedTrade(WEAPON_SMITH_ID, itemStack, priceRange, (tradeLevel + 1), magicAffinity);
			}
		}
	}

	protected static void registerIngotTrades(@Nonnull final MMDMaterial material, final int tradeLevel, final int emeraldPurch, final int emeraldSale) {
			if (material.hasItem(Names.INGOT)) {
				final ItemStack itemStack = material.getItemStack(Names.INGOT, 12);
				if ((!itemStack.getItem().equals(net.minecraft.init.Items.EMERALD))
						&& (!itemStack.getItem().equals(net.minecraft.init.Items.DIAMOND))) {
					final ITradeList[] ingotTrades = makeTradePalette(
							makePurchasePalette(emeraldPurch, itemStack),
							makeSalePalette(emeraldSale, itemStack));
					VillagerTradeHelper.insertTrades(SMITH_RL, ARMOR_SMITH_ID, tradeLevel,
							ingotTrades);
					VillagerTradeHelper.insertTrades(SMITH_RL, WEAPON_SMITH_ID, tradeLevel,
							ingotTrades);
					VillagerTradeHelper.insertTrades(SMITH_RL, TOOL_SMITH_ID, tradeLevel,
							ingotTrades);
				}
			}
	}

	protected static void registerTrade(final int id, @Nonnull final ItemStack itemStack, @Nonnull final int emeraldPurch, final int tradeLevel) {
		final ITradeList[] trades = makeTradePalette(
				makePurchasePalette(emeraldPurch, itemStack));
		VillagerTradeHelper.insertTrades(SMITH_RL, id, tradeLevel, trades);
	}

	protected static void registerEnchantedTrade(final int id, @Nonnull final ItemStack itemStack, @Nonnull final PriceInfo priceRange, final int tradeLevel, final float magicAffinity) {
		if (magicAffinity > 5) {
			final ListEnchantedItemForEmeralds trade = new ListEnchantedItemForEmeralds(
					itemStack.getItem(), priceRange);

			final MultiTradeGenerator multiTrade = new MultiTradeGenerator(TRADES_PER_LEVEL, Collections.singletonList(trade));
			VillagerTradeHelper.insertTrades(SMITH_RL, id, tradeLevel, multiTrade);
		}
	}

	protected static void registerModSpecificTrades() {
	}

	protected static void registerBurnableTrades(@Nonnull final String materialName) {
		if (Materials.hasMaterial(materialName)) {
			final MMDMaterial material = Materials.getMaterialByName(materialName);
			if (material.hasItem(Names.POWDER)) {
				final Item powder = material.getItem(Names.POWDER);
				final ITradeList[] trades = makePurchasePalette(1, 10, powder);

				VillagerTradeHelper.insertTrades(SMITH_RL, ARMOR_SMITH_ID, 1, trades);
				VillagerTradeHelper.insertTrades(SMITH_RL, WEAPON_SMITH_ID, 1, trades);
				VillagerTradeHelper.insertTrades(SMITH_RL, TOOL_SMITH_ID, 1, trades);
			}
		}
	}

	/**
	 * Commits a Trade Table.
	 *
	 * @param tradesTable
	 *            Trade Table to commit
	 * @deprecated
	 */
	@Deprecated
	protected static void commitTrades(@Nonnull final Map<Integer, List<ITradeList>> tradesTable) {
	}

	/**
	 *
	 * @param value
	 *            Purchase value
	 * @return int
	 */
	protected static int emeraldPurchaseValue(@Nonnull final float value) {
		return Math.max(1, (int) (value * 0.2F));
	}

	/**
	 *
	 * @param value
	 *            Sale value
	 * @return int
	 */
	protected static int emeraldSaleValue(@Nonnull final float value) {
		return Math.max(1, emeraldPurchaseValue(value) / 3);
	}

	/**
	 *
	 * @param value
	 *            Trade Level
	 * @return int
	 */
	protected static int tradeLevel(@Nonnull final float value) {
		return Math.max(1, Math.min(4, (int) (value * 0.1F)));
	}

	/**
	 *
	 * @param baseValue
	 *            Base Value
	 * @return int
	 */
	protected static int fluctuation(@Nonnull final int baseValue) {
		if (baseValue <= 1) {
			return 0;
		}
		return Math.max(2, baseValue / 4);
	}

	/**
	 * Creates a trade list for a single item (Buy Only).
	 *
	 * @param emeraldPrice
	 *            Price
	 * @param stackSize
	 *            Size of Stack
	 * @param items
	 *            The Items
	 * @return ITradeList
	 */
	protected static ITradeList[] makePurchasePalette(@Nonnull final int emeraldPrice,
			@Nonnull final int stackSize, @Nonnull final Item... items) {
		final ItemStack[] itemStacks = new ItemStack[items.length];
		for (int i = 0; i < items.length; i++) {
			itemStacks[i] = new ItemStack(items[i], stackSize);
		}
		return makePurchasePalette(emeraldPrice, itemStacks);
	}

	/**
	 * Creates a trade list for a single item (Buy Only).
	 *
	 * @param emeraldPrice
	 *            Purchase Value
	 * @param itemStacks
	 *            The ItemStacks
	 * @return ITradeList
	 */
	protected static ITradeList[] makePurchasePalette(@Nonnull final int emeraldPrice,
			@Nonnull final ItemStack... itemStacks) {
		final ITradeList[] trades = new ITradeList[itemStacks.length];
		for (int i = 0; i < itemStacks.length; i++) {
			trades[i] = new SimpleTrade(
					new ItemStack(net.minecraft.init.Items.EMERALD, emeraldPrice, 0),
					fluctuation(emeraldPrice), itemStacks[i], 0);
		}
		return trades;
	}

	/**
	 * Creates a trade list for a single item (Sell Only).
	 *
	 * @param emeraldValue
	 *            Sale Value
	 * @param stackSize
	 *            Size of the Stack
	 * @param items
	 *            The Items
	 * @return ITradeList
	 */
	protected static ITradeList[] makeSalePalette(@Nonnull final int emeraldValue,
			@Nonnull final int stackSize, @Nonnull final Item... items) {
		final ItemStack[] itemStacks = new ItemStack[items.length];
		for (int i = 0; i < items.length; i++) {
			itemStacks[i] = new ItemStack(items[i], stackSize);
		}
		return makeSalePalette(emeraldValue, itemStacks);
	}

	/**
	 * Creates a trade list for a single item (Sell Only).
	 *
	 * @param emeraldValue
	 *            Sale Value
	 * @param itemStacks
	 *            The ItemStacks
	 * @return ITradeList
	 */
	protected static ITradeList[] makeSalePalette(@Nonnull final int emeraldValue,
			@Nonnull final ItemStack... itemStacks) {
		final ITradeList[] trades = new ITradeList[itemStacks.length];
		for (int i = 0; i < itemStacks.length; i++) {
			trades[i] = new SimpleTrade(itemStacks[i], fluctuation(itemStacks[i].getCount()),
					new ItemStack(net.minecraft.init.Items.EMERALD, emeraldValue, 0), 0);
		}
		return trades;
	}

	/**
	 * Creates a trade list (Buy and Sell).
	 *
	 * @param list
	 *            The Trade Lists
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
	 * This ITradeList object holds a list of ITradeLists and picks a few at random to place in a
	 * merchant's trade menu.
	 */
	public static class MultiTradeGenerator implements ITradeList {

		private final int numberOfTrades;
		private final ITradeList[] trades;

		/**
		 * Creates an ITradeList instance that randomly adds multiple trades at a time.
		 *
		 * @param tradeCount
		 *            Number of trades to add to the merchant's trade menu
		 * @param tradePalette
		 *            The trades to randomly choose from
		 */
		public MultiTradeGenerator(@Nonnull final int tradeCount,
				@Nonnull final List<ITradeList> tradePalette) {
			this.numberOfTrades = Math.min(tradeCount, tradePalette.size());
			this.trades = tradePalette.toArray(new ITradeList[tradePalette.size()]);
		}

		@Override
		public void addMerchantRecipe(@Nonnull final IMerchant merchant,
				@Nonnull final MerchantRecipeList recipeList, @Nonnull final Random random) {
			for (int n = 0; n < this.numberOfTrades; n++) {
				this.trades[random.nextInt(this.trades.length)].addMerchantRecipe(merchant,
						recipeList, random);
			}
		}

		/**
		 * For debugging purposes only.
		 *
		 * @return String representation
		 */
		@Override
		public String toString() {
			return MultiTradeGenerator.class.getSimpleName() + ": " + this.numberOfTrades
					+ " trades chosen from " + Arrays.toString(this.trades);
		}
	}

	/**
	 * A simple, easy to use ITradeList class that holds a single trade recipe.
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
		 * Full constructor for making a trade recipe.
		 *
		 * @param in1
		 *            Item for the left purchase price trade slot
		 * @param variation1
		 *            range of variation in quantity of <code>in1</code>
		 * @param in2
		 *            Item for the right purchase price trade slot. Can be <code>null</code> (and
		 *            usually is)
		 * @param variation2
		 *            range of variation in quantity of <code>in2</code>
		 * @param out
		 *            The item to be purchased (trade recipe output slot)
		 * @param variationOut
		 *            range of variation in quantity of <code>out</code>
		 * @param numberTrades
		 *            Max number of trades before this recipe is invalidated (-1 for infinite
		 *            trading)
		 * @param tradeNumberVariation
		 *            range of variation in value of <code>numberTrades</code> (-1 to disable)
		 */
		public SimpleTrade(@Nonnull final ItemStack in1, @Nonnull final int variation1,
				final ItemStack in2, @Nonnull final int variation2, @Nonnull final ItemStack out,
				@Nonnull final int variationOut, @Nonnull final int numberTrades,
				@Nonnull final int tradeNumberVariation) {
			this.input1 = in1;
			this.maxInputMarkup1 = variation1;
			this.input2 = in2;
			this.maxInputMarkup2 = variation2;
			this.output = out;
			this.maxOutputMarkup = variationOut;
			this.maxTrades = numberTrades;
			this.maxTradeVariation = tradeNumberVariation;
		}

		/**
		 * Constructor for making a simple two-for-one trade recipe with price variation.
		 *
		 * @param in1
		 *            Item for the left purchase price trade slot
		 * @param v1
		 *            range of variation in quantity of <code>in1</code>
		 * @param in2
		 *            Item for the right purchase price trade slot. Can be <code>null</code> (and
		 *            usually is)
		 * @param v2
		 *            range of variation in quantity of <code>in2</code>
		 * @param out
		 *            The item to be purchased (trade recipe output slot)
		 * @param vout
		 *            range of variation in quantity of <code>out</code>
		 */
		public SimpleTrade(@Nonnull final ItemStack in1, @Nonnull final int v1, final ItemStack in2,
				@Nonnull final int v2, @Nonnull final ItemStack out, @Nonnull final int vout) {
			this(in1, v1, in2, v2, out, vout, -1, -1);
		}

		/**
		 * Constructor for making a simple one-for-one trade with price variation.
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
		public SimpleTrade(@Nonnull final ItemStack in1, @Nonnull final int v1,
				@Nonnull final ItemStack out, @Nonnull final int vout) {
			this(in1, v1, ItemStack.EMPTY, 0, out, vout, -1, -1);
		}

		/**
		 * Constructor for making a simple one-for-one trade.
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
			return this.input1 + " + " + this.input2 + " => " + this.output;
		}

		/**
		 * Invoked when the merchant generates its trade menu.
		 *
		 * @param recipeList
		 *            existing trade menu
		 * @param random
		 *            a pseudorandom number generator instance
		 */
		@Override
		public void addMerchantRecipe(@Nonnull final IMerchant merchant,
				@Nonnull final MerchantRecipeList recipeList, @Nonnull final Random random) {
			int numTrades = -1;
			if (this.maxTrades > 0) {
				if (this.maxTradeVariation > 0) {
					numTrades = Math.max(1,
							(this.maxTrades + random.nextInt(this.maxTradeVariation))
									- (this.maxTradeVariation / 2));
				} else {
					numTrades = this.maxTrades;
				}
			}
			final ItemStack in1 = this.input1.copy();
			if (this.maxInputMarkup1 > 0) {
				in1.setCount(in1.getCount() + random.nextInt(this.maxInputMarkup1));
			}
			ItemStack in2 = ItemStack.EMPTY;
			if (!this.input2.isEmpty()) {
				in2 = this.input2.copy();
				if (this.maxInputMarkup2 > 0) {
					in2.setCount(in2.getCount() + random.nextInt(this.maxInputMarkup2));
				}
			}
			final ItemStack out = this.output.copy();
			if (this.maxOutputMarkup > 0) {
				out.setCount(out.getCount() + random.nextInt(this.maxOutputMarkup));
			}

			if (numTrades > 0) {
				recipeList.add(new MerchantRecipe(in1, in2, out, 0, numTrades));
			} else {
				recipeList.add(new MerchantRecipe(in1, in2, out));
			}
		}
	}
}
