package zone.moddev.mc.basemetals.data;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import zone.moddev.mc.basemetals.trade.BaseMetalsTrades;

import static zone.moddev.mc.basemetals.data.BaseMetalsDataGenerators.object;

/** Appends Base Metals offers to the vanilla smith trade sets. */
final class SmithTradeProvider implements DataProvider {
    private final Path output;

    SmithTradeProvider(PackOutput output) {
        this.output = output.getOutputFolder();
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        var writes = new ArrayList<CompletableFuture<?>>();
        Map<String, JsonArray> tags = new LinkedHashMap<>();

        for (var offer : BaseMetalsTrades.offers()) {
            JsonObject trade = object(
                    "wants", object("id", "minecraft:emerald", "count", offer.emeralds()),
                    "gives", object("id", offer.item(), "count", offer.count()),
                    "max_uses", offer.enchanted() ? 3 : 6,
                    "xp", offer.enchanted() ? 15 : 5,
                    "reputation_discount", 0.2,
                    "merchant_predicate", object("condition", "basemetals:trade_enabled", "item", offer.item()));

            if (offer.enchanted()) {
                JsonArray modifiers = new JsonArray();
                modifiers.add(object("function", "minecraft:enchant_with_levels",
                        "levels", object("type", "minecraft:uniform", "min", 5, "max", 19),
                        "options", "#minecraft:on_random_loot", "include_additional_cost_component", true));
                trade.add("given_item_modifiers", modifiers);
            }

            writes.add(DataProvider.saveStable(cache, trade,
                    output.resolve("data/basemetals/villager_trade/" + offer.id() + ".json")));
            tags.computeIfAbsent(offer.profession() + "/level_" + offer.level(), key -> new JsonArray())
                    .add("basemetals:" + offer.id());
        }

        tags.forEach((name, entries) -> writes.add(DataProvider.saveStable(cache,
                object("replace", false, "values", entries),
                output.resolve("data/minecraft/tags/villager_trade/" + name + ".json"))));

        return CompletableFuture.allOf(writes.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Base Metals smith trades";
    }
}
