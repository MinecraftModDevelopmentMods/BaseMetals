package zone.moddev.mc.basemetals.worldgen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

class OreSpawnProviderTest {
    private static final Path PROVIDER = Paths.get("src", "main", "resources", "data",
            "basemetals", "orespawn", "provider.json");

    @Test
    void providerIsOreOnlyAndRetainsTheHistorical113Defaults() throws IOException {
        JsonObject root = readProvider();
        assertEquals(3, root.get("schema_version").getAsInt());
        assertEquals(1, root.get("provider_revision").getAsInt());
        assertEquals(11, root.getAsJsonObject("ores").size());
        assertTrue(root.getAsJsonObject("rocks").entrySet().isEmpty());
        assertTrue(root.getAsJsonObject("geomes").entrySet().isEmpty());
        assertTrue(root.getAsJsonObject("biome_rules").entrySet().isEmpty());
        assertTrue(root.getAsJsonObject("terrain_dimensions").entrySet().isEmpty());
        assertTrue(root.getAsJsonObject("fluid_deposits").entrySet().isEmpty());
        assertFalse(root.getAsJsonObject("ores").has("basemetals:ore/antimony"));
        assertFalse(root.getAsJsonObject("ores").has("basemetals:ore/bismuth"));

        JsonObject copper = selectorRule(root, "copper");
        assertTrue(copper.get("enabled").getAsBoolean());
        assertEquals(0, copper.get("min_y").getAsInt());
        assertEquals(95, copper.get("max_y").getAsInt());
        assertEquals("uniform", copper.get("height_distribution").getAsString());
    }

    @Test
    void everyRuleUsesTheSharedVeinContract() throws IOException {
        for (Map.Entry<String, JsonElement> entry : readProvider().getAsJsonObject("ores").entrySet()) {
            JsonObject ore = entry.getValue().getAsJsonObject();
            JsonObject rule = firstRule(ore);
            assertEquals("vein", rule.get("pattern").getAsString(), entry.getKey());
            assertEquals(4, rule.get("min_quantity").getAsInt(), entry.getKey());
            assertEquals(11, rule.get("max_quantity").getAsInt(), entry.getKey());
            assertEquals(8, rule.get("spread").getAsInt(), entry.getKey());
            assertEquals(4, rule.get("vertical_spread").getAsInt(), entry.getKey());
            assertEquals(4, rule.get("node_size").getAsInt(), entry.getKey());
            assertEquals(16, rule.get("length").getAsInt(), entry.getKey());
            assertEquals(0.0D, rule.get("discard_chance_on_air_exposure").getAsDouble(), 0.0D,
                    entry.getKey());
            assertEquals(!entry.getKey().equals("basemetals:ore/copper"),
                    ore.get("enabled").getAsBoolean(), entry.getKey());
            assertFalse(ore.get("retrogen").getAsBoolean(), entry.getKey());
        }
    }

    @Test
    void everyRuleMatchesThePublished113Distribution() throws IOException {
        Map<String, ExpectedRule> expected = new LinkedHashMap<String, ExpectedRule>();
        expected.put("coldiron", explicit("minecraft:the_nether", 0, 127, 5.0D,
                "minecraft:netherrack", "forge:netherrack"));
        expected.put("adamantine", explicit("minecraft:the_nether", 0, 127, 2.0D,
                "minecraft:netherrack", "forge:netherrack"));
        expected.put("starsteel", explicit("minecraft:the_end", 0, 254, 5.0D,
                "minecraft:end_stone", "forge:end_stones"));
        expected.put("copper", ordinary(0, 95, 10.0D));
        expected.put("silver", ordinary(0, 31, 4.0D));
        expected.put("tin", ordinary(0, 127, 10.0D));
        expected.put("lead", ordinary(0, 63, 5.0D));
        expected.put("zinc", ordinary(0, 95, 5.0D));
        expected.put("mercury", ordinary(0, 31, 3.0D));
        expected.put("nickel", ordinary(32, 95, 1.0D));
        expected.put("platinum", ordinary(1, 31, 0.125D));

        JsonObject ores = readProvider().getAsJsonObject("ores");
        assertEquals(expected.size(), ores.size());
        for (Map.Entry<String, ExpectedRule> entry : expected.entrySet()) {
            String name = entry.getKey();
            ExpectedRule contract = entry.getValue();
            JsonObject ore = ores.getAsJsonObject("basemetals:ore/" + name);
            assertEquals("basemetals:" + name + "_ore", ore.get("block").getAsString());
            JsonObject rules = ore.getAsJsonObject(contract.explicitDimension ? "dimensions" : "dimension_selectors");
            assertEquals(1, rules.size());
            JsonObject rule = rules.getAsJsonObject(contract.selector);
            assertEquals(contract.minY, rule.get("min_y").getAsInt());
            assertEquals(contract.maxY, rule.get("max_y").getAsInt());
            assertEquals(contract.frequency, rule.get("frequency").getAsDouble(), 0.000001D);
            assertEquals("uniform", rule.get("height_distribution").getAsString());
            assertEquals(contract.hostBlocks, strings(rule.getAsJsonArray("host_blocks")));
            assertEquals(contract.hostTags, strings(rule.getAsJsonArray("host_tags")));
            assertEquals(contract.hostFamilies, strings(rule.getAsJsonArray("host_families")));
            assertTrue(rule.get("enabled").getAsBoolean());
        }
    }

    private static JsonObject readProvider() throws IOException {
        try (Reader reader = Files.newBufferedReader(PROVIDER, StandardCharsets.UTF_8)) {
            return new JsonParser().parse(reader).getAsJsonObject();
        }
    }

    private static JsonObject selectorRule(JsonObject root, String name) {
        return root.getAsJsonObject("ores").getAsJsonObject("basemetals:ore/" + name)
                .getAsJsonObject("dimension_selectors").getAsJsonObject("orespawn:all_except_nether_end");
    }

    private static JsonObject firstRule(JsonObject ore) {
        JsonObject rules = ore.has("dimensions")
                ? ore.getAsJsonObject("dimensions") : ore.getAsJsonObject("dimension_selectors");
        return rules.entrySet().iterator().next().getValue().getAsJsonObject();
    }

    private static List<String> strings(JsonArray array) {
        List<String> result = new ArrayList<String>();
        for (JsonElement value : array) result.add(value.getAsString());
        return result;
    }

    private static ExpectedRule explicit(String selector, int minY, int maxY, double frequency,
            String hostBlock, String hostTag) {
        return new ExpectedRule(selector, minY, maxY, frequency, true,
                Arrays.asList(hostBlock), Arrays.asList(hostTag), new ArrayList<String>());
    }

    private static ExpectedRule ordinary(int minY, int maxY, double frequency) {
        return new ExpectedRule("orespawn:all_except_nether_end", minY, maxY, frequency, false,
                Arrays.asList("minecraft:stone"), Arrays.asList("forge:stone"), Arrays.asList(
                        "sedimentary", "metamorphic", "igneous_intrusive", "igneous_volcanic"));
    }

    private static final class ExpectedRule {
        final String selector;
        final int minY;
        final int maxY;
        final double frequency;
        final boolean explicitDimension;
        final List<String> hostBlocks;
        final List<String> hostTags;
        final List<String> hostFamilies;

        ExpectedRule(String selector, int minY, int maxY, double frequency, boolean explicitDimension,
                List<String> hostBlocks, List<String> hostTags, List<String> hostFamilies) {
            this.selector = selector;
            this.minY = minY;
            this.maxY = maxY;
            this.frequency = frequency;
            this.explicitDimension = explicitDimension;
            this.hostBlocks = hostBlocks;
            this.hostTags = hostTags;
            this.hostFamilies = hostFamilies;
        }
    }
}
