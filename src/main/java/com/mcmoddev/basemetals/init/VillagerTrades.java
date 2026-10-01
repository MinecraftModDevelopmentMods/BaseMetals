package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.content.ContentMode;
import com.mcmoddev.basemetals.content.ContentPolicy;
import com.mcmoddev.basemetals.content.MaterialForm;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.util.VillagerTradeHelper;
import com.mcmoddev.lib.data.ConfigKeys;
import com.mcmoddev.lib.data.MaterialStats;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.entity.passive.EntityVillager.ITradeList;
import net.minecraft.entity.passive.EntityVillager.ListEnchantedItemForEmeralds;
import net.minecraft.entity.passive.EntityVillager.PriceInfo;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Adds Base Metals offers to the vanilla smith careers. */
public final class VillagerTrades extends com.mcmoddev.lib.init.VillagerTrades {

	private VillagerTrades() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/** Registers trades that are permitted by the startup content policy. */
	public static void init() {
		if (com.mcmoddev.lib.util.Config.Options.isThingEnabled(ConfigKeys.VILLAGER_TRADES)) {
			if (ContentPolicy.active().mode() == ContentMode.HIGH_FANTASY) {
				registerCommonTrades();
			} else {
				registerRestrictedTrades(ContentPolicy.active());
			}

			registerModSpecificTrades();
		}
	}

	private static void registerRestrictedTrades(final ContentPolicy policy) {
		final Map<Integer, List<ITradeList>> enchanted = new HashMap<>();

		Materials.getMaterialsByMod(BaseMetals.MODID).stream()
				.filter(material -> !material.isEmpty() && !material.isRare())
				.filter(VillagerTrades::hasAffordableTradeValue)
				.forEach(material -> {
					registerIngotTrades(material, policy);

					if (material.getStat(MaterialStats.MAGICAFFINITY) > 5.0F) {
						registerEnchantedTrades(material, policy, enchanted);
					}
				});
		commitTrades(enchanted);
	}

	private static boolean hasAffordableTradeValue(final MMDMaterial material) {
		final float value = tradeValue(material);

		return emeraldPurchaseValue(value) < 65 && emeraldSaleValue(value) < 65;
	}

	private static float tradeValue(final MMDMaterial material) {
		return material.getStat(MaterialStats.HARDNESS)
				+ material.getStat(MaterialStats.STRENGTH)
				+ material.getStat(MaterialStats.MAGICAFFINITY)
				+ material.getToolHarvestLevel();
	}

	private static void registerIngotTrades(final MMDMaterial material,
			final ContentPolicy policy) {
		if (!material.hasItem(Names.INGOT)
				|| !policy.allows(material.getName(), MaterialForm.INGOT)) {
			return;
		}

		final net.minecraft.item.ItemStack stack = material.getItemStack(Names.INGOT, 12);

		if (stack.getItem() == net.minecraft.init.Items.EMERALD
				|| stack.getItem() == net.minecraft.init.Items.DIAMOND) {
			return;
		}

		final int price = emeraldPurchaseValue(tradeValue(material));
		final int level = tradeLevel(tradeValue(material));
		final ITradeList[] trades = makePurchasePalette(price, stack);

		VillagerTradeHelper.insertTrades(SMITH_RL, ARMOR_SMITH_ID, level, trades);
		VillagerTradeHelper.insertTrades(SMITH_RL, WEAPON_SMITH_ID, level, trades);
		VillagerTradeHelper.insertTrades(SMITH_RL, TOOL_SMITH_ID, level, trades);
	}

	private static void registerEnchantedTrades(final MMDMaterial material,
			final ContentPolicy policy, final Map<Integer, List<ITradeList>> table) {
		final int level = tradeLevel(tradeValue(material));
		final int basePrice = emeraldPurchaseValue(tradeValue(material));

		addEnchanted(table, WEAPON_SMITH | (level + 1), material, policy,
				basePrice, (int) (material.getBaseAttackDamage() / 2) - 1,
				Names.SWORD, Names.CROSSBOW, Names.BOW);
		addEnchanted(table, ARMOR_SMITH | (level + 1), material, policy,
				basePrice, (int) (material.getStat(MaterialStats.HARDNESS) / 2),
				Names.HELMET, Names.CHESTPLATE, Names.LEGGINGS, Names.BOOTS);
		addEnchanted(table, TOOL_SMITH | (level + 1), material, policy,
				basePrice, 0, Names.AXE, Names.HOE, Names.SHOVEL, Names.PICKAXE);
		addEnchanted(table, TOOL_SMITH | (level + 2), material, policy,
				basePrice, 0, Names.CRACKHAMMER);
	}

	private static void addEnchanted(final Map<Integer, List<ITradeList>> table,
			final int key, final MMDMaterial material, final ContentPolicy policy,
			final int basePrice, final int priceModifier, final Names... forms) {
		for (final Names name : Arrays.asList(forms)) {
			if (!material.hasItem(name)
					|| !policy.allows(material.getName(), toForm(name))) {
				continue;
			}

			table.computeIfAbsent(key, ignored -> new ArrayList<>())
					.addAll(Collections.singletonList(new ListEnchantedItemForEmeralds(
							material.getItem(name), new PriceInfo(basePrice + 7 + priceModifier,
									basePrice + 12 + priceModifier))));
		}
	}

	private static MaterialForm toForm(final Names name) {
		try {
			return MaterialForm.valueOf(name.name());
		} catch (final IllegalArgumentException ignored) {
			return MaterialForm.OTHER;
		}
	}

	protected static void registerModSpecificTrades() {

		if (Materials.hasMaterial(MaterialNames.CHARCOAL)) {
			final MMDMaterial charcoal = Materials.getMaterialByName(MaterialNames.CHARCOAL);

			if (charcoal.hasItem(Names.POWDER)) {
				final Item charcoalPowder = charcoal.getItem(Names.POWDER);
				final ITradeList[] charcoalTrades = makePurchasePalette(1, 10, charcoalPowder);

				VillagerTradeHelper.insertTrades(SMITH_RL, ARMOR_SMITH_ID, 1, charcoalTrades);
				VillagerTradeHelper.insertTrades(SMITH_RL, WEAPON_SMITH_ID, 1, charcoalTrades);
				VillagerTradeHelper.insertTrades(SMITH_RL, TOOL_SMITH_ID, 1, charcoalTrades);
			}
		}

		if (Materials.hasMaterial(MaterialNames.COAL)) {
			final MMDMaterial coal = Materials.getMaterialByName(MaterialNames.COAL);

			if (coal.hasItem(Names.POWDER)) {
				final Item coalPowder = coal.getItem(Names.POWDER);
				final ITradeList[] coalTrades = makePurchasePalette(1, 10, coalPowder);

				VillagerTradeHelper.insertTrades(SMITH_RL, ARMOR_SMITH_ID, 1, coalTrades);
				VillagerTradeHelper.insertTrades(SMITH_RL, WEAPON_SMITH_ID, 1, coalTrades);
				VillagerTradeHelper.insertTrades(SMITH_RL, TOOL_SMITH_ID, 1, coalTrades);
			}
		}
	}
}
