package zone.moddev.mc.basemetals.trade;

import java.util.List;

import zone.moddev.mc.basemetals.config.BaseMetalsConfig;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.material.MaterialCatalogue;
import zone.moddev.mc.basemetals.material.MaterialDefinition;

import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.core.registries.BuiltInRegistries;

import net.neoforged.neoforge.event.village.VillagerTradesEvent;

public final class BaseMetalsTrades {
    private BaseMetalsTrades() {}

    public static void add(VillagerTradesEvent event) {
        if (!BaseMetalsConfig.VILLAGER_TRADES.get()) return;
        if (event.getType() != VillagerProfession.ARMORER
                && event.getType() != VillagerProfession.TOOLSMITH
                && event.getType() != VillagerProfession.WEAPONSMITH) return;
        for (MaterialDefinition material : MaterialCatalogue.ALL) {
            if (material.kind() == MaterialDefinition.Kind.RARE_ORE
                    || material.kind() == MaterialDefinition.Kind.RARE_ALLOY) continue;
            int value = (int) (material.hardness() + material.strength() + material.magic() + material.toolLevel());
            int emeraldCost = Math.max(1, (int) (0.2F * value));
            int level = Math.max(1, Math.min(4, (int) (0.1F * value)));
            Item ingot = ModContent.item(material.name() + "_ingot").get();
            event.getTrades().get(level).add(selling(ingot, 12, emeraldCost));
            if (!material.hasEquipment()) continue;
            if (event.getType() == VillagerProfession.ARMORER) {
                addSales(event, level, emeraldCost + (int) (material.hardness() / 2.0D),
                        material, "helmet", "chestplate", "leggings", "boots");
            } else if (event.getType() == VillagerProfession.TOOLSMITH) {
                addSales(event, level, emeraldCost, material, "pickaxe", "axe", "shovel", "hoe", "crackhammer");
            } else if (event.getType() == VillagerProfession.WEAPONSMITH) {
                addSales(event, level,
                        emeraldCost + ((int) (material.baseAttackDamage() / 2.0F)) - 1,
                        material, "sword");
                addSales(event, level, emeraldCost, material, "bow", "crossbow");
            }
            if (material.magic() > 5.0D) {
                addEnchantedSales(event, level, emeraldCost, material);
            }
        }
        if (event.getType() == VillagerProfession.ARMORER || event.getType() == VillagerProfession.TOOLSMITH
                || event.getType() == VillagerProfession.WEAPONSMITH) {
            event.getTrades().get(1).add(selling(ModContent.item("coal_powder").get(), 10, 1));
            event.getTrades().get(1).add(selling(ModContent.item("charcoal_powder").get(), 10, 1));
        }
    }

    private static void addSales(VillagerTradesEvent event, int level, int cost,
            MaterialDefinition material, String... forms) {
        for (String form : forms) {
            Item item = ModContent.item(material.name() + "_" + form).get();
            if (!zone.moddev.mc.basemetals.config.ContentPolicy.active().allows(BuiltInRegistries.ITEM.getKey(item).toString())) continue;
            event.getTrades().get(level).add(selling(item, cost));
        }
    }

    private static void addEnchantedSales(VillagerTradesEvent event, int level, int cost,
            MaterialDefinition material) {
        int advanced = Math.min(5, level + 1);
        if (event.getType() == VillagerProfession.ARMORER) {
            int armorCost = cost + 7 + (int) (material.hardness() / 2.0D);
            for (String form : List.of("helmet", "chestplate", "leggings", "boots")) {
                if (!zone.moddev.mc.basemetals.config.ContentPolicy.active().allows("basemetals:" + material.name() + "_" + form)) continue;
                event.getTrades().get(advanced).add(enchanted(
                        ModContent.item(material.name() + "_" + form).get(), armorCost));
            }
        } else if (event.getType() == VillagerProfession.WEAPONSMITH) {
            int weaponCost = cost + 6 + Math.max(0, (int) (material.baseAttackDamage() / 2.0F));
            for (String form : List.of("sword", "crossbow", "bow")) {
                if (!zone.moddev.mc.basemetals.config.ContentPolicy.active().allows("basemetals:" + material.name() + "_" + form)) continue;
                event.getTrades().get(advanced).add(enchanted(
                        ModContent.item(material.name() + "_" + form).get(), weaponCost));
            }
        } else if (event.getType() == VillagerProfession.TOOLSMITH) {
            for (String form : List.of("axe", "hoe", "shovel", "pickaxe")) {
                if (!zone.moddev.mc.basemetals.config.ContentPolicy.active().allows("basemetals:" + material.name() + "_" + form)) continue;
                event.getTrades().get(advanced).add(enchanted(
                        ModContent.item(material.name() + "_" + form).get(), cost + 7));
            }
            if (zone.moddev.mc.basemetals.config.ContentPolicy.active().allows("basemetals:" + material.name() + "_crackhammer")) {
                event.getTrades().get(Math.min(5, level + 2)).add(enchanted(
                        ModContent.item(material.name() + "_crackhammer").get(), cost + 7));
            }
        }
    }

    private static ItemListing selling(Item item, int emeralds) {
        return selling(item, 1, emeralds);
    }

    private static ItemListing selling(Item item, int count, int emeralds) {
        return (trader, random) -> new MerchantOffer(new ItemCost(Items.EMERALD, Math.max(1, emeralds)),
                new ItemStack(item, count), 6, 5, 0.2F);
    }

    private static ItemListing enchanted(Item item, int baseCost) {
        return (trader, random) -> {
            int enchantmentLevel = 5 + random.nextInt(15);
            // RandomSource, ItemStack, int, Stream
            ItemStack result = EnchantmentHelper.enchantItem(random, new ItemStack(item), enchantmentLevel, null);
            return new MerchantOffer(new ItemCost(Items.EMERALD, Math.min(64, baseCost + enchantmentLevel)),
                    result, 3, 15, 0.2F);
        };
    }
}
