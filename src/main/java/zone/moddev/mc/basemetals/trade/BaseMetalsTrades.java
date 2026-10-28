package zone.moddev.mc.basemetals.trade;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.material.MaterialCatalogue;
import zone.moddev.mc.basemetals.material.MaterialDefinition;

/** The historical smith offers, shared by data generation and regression tests. */
public final class BaseMetalsTrades {
    private BaseMetalsTrades() {}

    public record Offer(String id, String profession, int level, Identifier item, int count,
            int emeralds, boolean enchanted) {}

    public static List<Offer> offers() {
        List<Offer> offers = new ArrayList<>();

        for (String profession : List.of("armorer", "toolsmith", "weaponsmith")) {
            for (MaterialDefinition material : MaterialCatalogue.ALL) {
                if (material.kind() == MaterialDefinition.Kind.RARE_ORE
                        || material.kind() == MaterialDefinition.Kind.RARE_ALLOY) continue;

                int value = (int) (material.hardness() + material.strength() + material.magic() + material.toolLevel());
                int cost = Math.max(1, (int) (0.2F * value));
                int level = Math.max(1, Math.min(4, (int) (0.1F * value)));
                var ingot = material.name().equals("copper") ? Items.COPPER_INGOT
                        : ModContent.item(material.name() + "_ingot").get();
                offers.add(new Offer(profession + "/" + material.name() + "_ingot", profession, level,
                        ForgeRegistries.ITEMS.getKey(ingot), 12, cost, false));

                if (!material.hasEquipment()) continue;

                switch (profession) {
                    case "armorer" -> add(offers, profession, level, cost + (int) (material.hardness() / 2),
                            material, false, "helmet", "chestplate", "leggings", "boots");
                    case "toolsmith" -> add(offers, profession, level, cost,
                            material, false, "pickaxe", "axe", "shovel", "hoe", "crackhammer");
                    case "weaponsmith" -> {
                        add(offers, profession, level, cost + (int) (material.baseAttackDamage() / 2) - 1,
                                material, false, "sword");
                        add(offers, profession, level, cost, material, false, "bow", "crossbow");
                    }
                }

                if (material.magic() <= 5) continue;

                int advanced = Math.min(5, level + 1);
                switch (profession) {
                    case "armorer" -> add(offers, profession, advanced, cost + 7 + (int) (material.hardness() / 2),
                            material, true, "helmet", "chestplate", "leggings", "boots");
                    case "weaponsmith" -> add(offers, profession, advanced,
                            cost + 6 + Math.max(0, (int) (material.baseAttackDamage() / 2)),
                            material, true, "sword", "crossbow", "bow");
                    case "toolsmith" -> {
                        add(offers, profession, advanced, cost + 7, material, true, "axe", "hoe", "shovel", "pickaxe");
                        add(offers, profession, Math.min(5, level + 2), cost + 7, material, true, "crackhammer");
                    }
                }
            }

            for (String fuel : List.of("coal_powder", "charcoal_powder")) {
                offers.add(new Offer(profession + "/" + fuel, profession, 1,
                        Identifier.fromNamespaceAndPath("basemetals", fuel), 10, 1, false));
            }
        }

        return List.copyOf(offers);
    }

    private static void add(List<Offer> offers, String profession, int level, int cost,
            MaterialDefinition material, boolean enchanted, String... forms) {
        for (String form : forms) {
            String name = material.name() + "_" + form;
            var item = ModContent.item(name).get();
            offers.add(new Offer(profession + "/" + name + (enchanted ? "_enchanted" : ""),
                    profession, level, ForgeRegistries.ITEMS.getKey(item), 1, Math.max(1, cost), enchanted));
        }
    }
}
