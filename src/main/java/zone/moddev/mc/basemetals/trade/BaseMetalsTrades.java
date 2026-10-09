package zone.moddev.mc.basemetals.trade;

import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.config.BaseMetalsConfig;
import zone.moddev.mc.basemetals.config.ContentPolicy;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.material.MaterialCatalogue;
import zone.moddev.mc.basemetals.material.MaterialDefinition;

/** Adds the old smith offers to the three village smith professions. */
@Mod.EventBusSubscriber(modid = BaseMetals.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class BaseMetalsTrades {
    private BaseMetalsTrades() {}

    @SubscribeEvent
    public static void register(VillagerTradesEvent event) {
        VillagerProfession profession = event.getType();
        if (profession != VillagerProfession.ARMORER && profession != VillagerProfession.WEAPONSMITH
                && profession != VillagerProfession.TOOLSMITH) return;

        addSale(event, 1, ModContent.item("coal_powder").get(), 10, 1);
        addSale(event, 1, ModContent.item("charcoal_powder").get(), 10, 1);

        for (MaterialDefinition material : MaterialCatalogue.ALL) {
            if (material.kind() == MaterialDefinition.Kind.RARE_ORE
                    || material.kind() == MaterialDefinition.Kind.RARE_ALLOY) continue;

            int value = (int) (material.hardness() + material.strength() + material.magic() + material.toolLevel());
            int cost = Math.max(1, (int) (0.2F * value));
            int level = Math.max(1, Math.min(4, (int) (0.1F * value)));
            addSale(event, level, ModContent.item(material.name() + "_ingot").get(), 12, cost);
            if (!material.hasEquipment()) continue;

            if (profession == VillagerProfession.ARMORER) {
                addEquipment(event, level, cost + (int) (material.hardness() / 2.0D), material,
                        "helmet", "chestplate", "leggings", "boots");
            } else if (profession == VillagerProfession.TOOLSMITH) {
                addEquipment(event, level, cost, material, "pickaxe", "axe", "shovel", "hoe", "crackhammer");
            } else {
                addEquipment(event, level, cost, material, "sword", "bow", "crossbow");
            }
        }
    }

    private static void addEquipment(VillagerTradesEvent event, int level, int cost,
            MaterialDefinition material, String... forms) {
        for (String form : forms) {
            addSale(event, level, ModContent.item(material.name() + "_" + form).get(), 1, cost);
        }
    }

    private static void addSale(VillagerTradesEvent event, int level, Item item, int count, int emeralds) {
        event.getTrades().get(level).add(selling(item, count, emeralds, level));
    }

    public static VillagerTrades.ItemListing selling(Item item, int count, int emeralds, int level) {
        return (merchant, random) -> {
            if (!BaseMetalsConfig.VILLAGER_TRADES.get()
                    || !ContentPolicy.active().allows(item.getRegistryName().toString())) return null;

            int experience = level == 1 ? 1 : level == 2 ? 5 : level == 3 ? 10 : 15;
            return new MerchantOffer(new ItemStack(Items.EMERALD, Math.max(1, emeralds)),
                    new ItemStack(item, count), 12, experience, 0.05F);
        };
    }
}
