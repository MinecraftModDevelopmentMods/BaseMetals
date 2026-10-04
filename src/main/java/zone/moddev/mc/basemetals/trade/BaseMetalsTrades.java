package zone.moddev.mc.basemetals.trade;

import zone.moddev.mc.basemetals.config.BaseMetalsConfig;
import zone.moddev.mc.basemetals.config.ContentPolicy;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.material.MaterialCatalogue;
import zone.moddev.mc.basemetals.material.MaterialDefinition;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import net.minecraftforge.registries.ForgeRegistries;

/** Adds the historical smith careers' Base Metals offers after item registration. */
public final class BaseMetalsTrades {
    private static boolean registered;
    private BaseMetalsTrades() {}

    public static synchronized void register() {
        if (registered) return;
        VillagerProfession smith = ForgeRegistries.VILLAGER_PROFESSIONS.getValue(
                new ResourceLocation("minecraft", "smith"));
        if (smith == null) return;
        VillagerCareer armor = smith.getCareer(1);
        VillagerCareer weapons = smith.getCareer(2);
        VillagerCareer tools = smith.getCareer(3);

        armor.addTrade(1, selling(ModContent.item("coal_powder").get(), 10, 1),
                selling(ModContent.item("charcoal_powder").get(), 10, 1));
        weapons.addTrade(1, selling(ModContent.item("coal_powder").get(), 10, 1),
                selling(ModContent.item("charcoal_powder").get(), 10, 1));
        tools.addTrade(1, selling(ModContent.item("coal_powder").get(), 10, 1),
                selling(ModContent.item("charcoal_powder").get(), 10, 1));

        for (MaterialDefinition material : MaterialCatalogue.ALL) {
            if (material.kind() == MaterialDefinition.Kind.RARE_ORE
                    || material.kind() == MaterialDefinition.Kind.RARE_ALLOY) continue;
            int value = (int) (material.hardness() + material.strength() + material.magic() + material.toolLevel());
            int cost = Math.max(1, (int) (0.2F * value));
            int level = Math.max(1, Math.min(4, (int) (0.1F * value)));
            EntityVillager.ITradeList ingot = selling(ModContent.item(material.name() + "_ingot").get(), 12, cost);
            armor.addTrade(level, ingot);
            weapons.addTrade(level, ingot);
            tools.addTrade(level, ingot);
            if (!material.hasEquipment()) continue;
            addSales(armor, level, cost + (int) (material.hardness() / 2.0D), material,
                    "helmet", "chestplate", "leggings", "boots");
            addSales(tools, level, cost, material, "pickaxe", "axe", "shovel", "hoe", "crackhammer");
            addSales(weapons, level, cost, material, "sword", "bow", "crossbow");
        }
        registered = true;
    }

    private static void addSales(VillagerCareer career, int level, int cost,
            MaterialDefinition material, String... forms) {
        for (String form : forms) {
            career.addTrade(level, selling(ModContent.item(material.name() + "_" + form).get(), 1, cost));
        }
    }

    private static EntityVillager.ITradeList selling(final Item item, final int count, final int emeralds) {
        return (merchant, recipes, random) -> {
            if (BaseMetalsConfig.VILLAGER_TRADES.get()
                    && ContentPolicy.active().allows(item.getRegistryName().toString())) {
                recipes.add(new MerchantRecipe(
                        new ItemStack(Items.EMERALD, Math.max(1, emeralds)),
                        new ItemStack(item, count)));
            }
        };
    }
}
