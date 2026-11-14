package zone.moddev.mc.basemetals.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

class RecipeRegressionTest {
    private static final Path DATA = Paths.get("src", "generated", "resources", "data", "basemetals");

    @Test
    void alloyInputsAcceptMatchingFullBlendsWithoutChangingTheirMaterialCost() throws Exception {
        for (String[] expected : new String[][] {
                {"electrum", "gold", "1", "2"}, {"invar", "iron", "2", "3"}, {"steel", "iron", "8", "8"}}) {
            JsonObject recipe = read(DATA.resolve("recipes/" + expected[0] + "_blend.json"));
            int positions = Integer.parseInt(expected[2]);
            for (int index = 0; index < positions; index++) {
                com.google.gson.JsonArray choices = recipe.getAsJsonArray("ingredients").get(index).getAsJsonArray();
                assertEquals(2, choices.size());
                assertEquals("forge:dusts/" + expected[1], choices.get(0).getAsJsonObject().get("tag").getAsString());
                assertEquals("forge:blends/" + expected[1], choices.get(1).getAsJsonObject().get("tag").getAsString());
            }
            assertEquals(Integer.parseInt(expected[3]), recipe.getAsJsonObject("result").get("count").getAsInt());
            JsonObject criteria = read(DATA.resolve("advancements/recipes/" + expected[0] + "_blend.json"))
                    .getAsJsonObject("criteria");
            assertTrue(criteria.has("has_blend_" + expected[1]));
            assertEquals("forge:dusts/" + expected[1], criteria.getAsJsonObject("has_ingredient_1")
                    .getAsJsonObject("conditions").getAsJsonArray("items").get(0).getAsJsonObject().get("tag").getAsString());
        }
    }

    @Test
    void doorsAndTrapdoorsHaveDifferentPatterns() throws Exception {
        try (Stream<Path> files = Files.list(DATA.resolve("recipes"))) {
            for (Path file : (Iterable<Path>) files.filter(path -> path.toString().endsWith("_door.json"))::iterator) {
                JsonObject door = read(file);
                String trapdoorName = file.getFileName().toString().replace("_door.json", "_trapdoor.json");
                JsonObject trapdoor = read(DATA.resolve("recipes").resolve(trapdoorName));

                assertEquals(3, door.getAsJsonArray("pattern").size(), file.toString());
                assertEquals("quartz_trapdoor.json".equals(trapdoorName) ? 3 : 2,
                        trapdoor.getAsJsonArray("pattern").size(), trapdoorName);
                assertFalse(door.get("pattern").equals(trapdoor.get("pattern")), file.toString());
            }
        }
    }

    @Test
    void quartzTrapdoorKeepsFourQuartzWithoutMatchingVanillasSquare() throws Exception {
        JsonObject recipe = read(DATA.resolve("recipes/quartz_trapdoor.json"));
        assertEquals("[\" X \",\"X X\",\" X \"]", recipe.getAsJsonArray("pattern").toString());
        assertEquals("forge:gems/quartz", recipe.getAsJsonObject("key").getAsJsonObject("X").get("tag").getAsString());
    }

    @Test
    void steelRailsGiveTheVanillaBatchSize() throws Exception {
        JsonObject result = read(DATA.resolve("recipes/rail.json")).getAsJsonObject("result");
        assertEquals(16, result.has("count") ? result.get("count").getAsInt() : 1);
    }

    @Test
    void craftingRecipesHaveIngredientBasedUnlocksButRepairsStayHidden() throws Exception {
        try (Stream<Path> files = Files.list(DATA.resolve("recipes"))) {
            for (Path file : (Iterable<Path>) files::iterator) {
                JsonObject recipe = read(file);
                String type = recipe.get("type").getAsString();
                Path unlock = DATA.resolve("advancements/recipes").resolve(file.getFileName());

                if (type.startsWith("minecraft:crafting_")) {
                    assertTrue(Files.isRegularFile(unlock), "Missing unlock for " + file);
                    JsonObject advancement = read(unlock);
                    assertEquals("minecraft:recipes/root", advancement.get("parent").getAsString());
                    assertTrue(advancement.getAsJsonObject("criteria").size() >= 2, file.toString());
                    assertEquals("basemetals:" + file.getFileName().toString().replace(".json", ""),
                            advancement.getAsJsonObject("rewards").getAsJsonArray("recipes").get(0).getAsString());
                } else if ("basemetals:plate_repair".equals(type)) {
                    assertFalse(Files.exists(unlock), "Repair recipes must not be in the recipe book");
                }
            }
        }
    }

    @Test
    void everyFurnaceRecipeHasAnIngredientUnlock() throws Exception {
        int tested = 0;
        try (Stream<Path> files = Files.list(DATA.resolve("recipes"))) {
            for (Path file : (Iterable<Path>) files::iterator) {
                JsonObject recipe = read(file);
                String type = recipe.get("type").getAsString();
                if (!"minecraft:smelting".equals(type) && !"basemetals:legacy_smelting".equals(type)) continue;
                JsonObject advancement = read(DATA.resolve("advancements/recipes").resolve(file.getFileName()));
                JsonObject criteria = advancement.getAsJsonObject("criteria");
                assertTrue(criteria.has("has_ingredient_1"), file.toString());
                assertFalse(criteria.has("has_material"), file.toString());
                assertEquals(1, advancement.getAsJsonArray("requirements").size(), file.toString());
                assertEquals("basemetals:" + file.getFileName().toString().replace(".json", ""),
                        advancement.getAsJsonObject("rewards").getAsJsonArray("recipes").get(0).getAsString());
                tested++;
            }
        }
        assertEquals(754, tested, "Check the full furnace catalogue");
    }

    @Test
    void everyCraftingRecipeHasThe112MaterialDiscoveryTrigger() throws Exception {
        Map<String, String> discovery = materialDiscovery();
        int tested = 0;

        try (Stream<Path> files = Files.list(DATA.resolve("recipes"))) {
            for (Path file : (Iterable<Path>) files::iterator) {
                JsonObject recipe = read(file);
                if (!recipe.get("type").getAsString().startsWith("minecraft:crafting_")) continue;

                String name = file.getFileName().toString().replace(".json", "");
                String material = name.matches("activator_rail|detector_rail|flint_and_steel|human_detector|minecart|piston|rail|tripwire_hook")
                        ? "steel" : name.substring(0, name.indexOf('_'));
                assertTrue(discovery.containsKey(material), "Missing discovery material for " + name);

                JsonObject advancement = read(DATA.resolve("advancements/recipes").resolve(file.getFileName()));
                JsonObject criteria = advancement.getAsJsonObject("criteria");
                assertTrue(criteria.has("has_material"), "Missing material discovery for " + name);
                JsonObject acquired = criteria.getAsJsonObject("has_material");
                assertEquals("minecraft:inventory_changed", acquired.get("trigger").getAsString(), name);

                String[] expected = discovery.get(material).split("=", 2);
                JsonObject predicate = acquired.getAsJsonObject("conditions").getAsJsonArray("items")
                        .get(0).getAsJsonObject();
                assertEquals(expected[1], predicate.get(expected[0]).getAsString(), name);
                assertEquals(1, advancement.getAsJsonArray("requirements").size(), name);
                assertTrue(advancement.getAsJsonArray("requirements").get(0).getAsJsonArray().toString()
                        .contains("\"has_material\""), "Material discovery is not an alternative for " + name);
                tested++;
            }
        }

        assertEquals(1024, tested, "Check the complete crafting catalogue");
    }

    @Test
    void existing113RecipeAdvancementCriteriaRemainAvailable() throws Exception {
        JsonObject criteria = read(DATA.resolve("advancements/recipes/adamantine_bow.json"))
                .getAsJsonObject("criteria");
        assertEquals("minecraft:recipe_unlocked", criteria.getAsJsonObject("has_recipe").get("trigger").getAsString());
        assertEquals("forge:rods/adamantine", criteria.getAsJsonObject("has_ingredient_1")
                .getAsJsonObject("conditions").getAsJsonArray("items").get(0).getAsJsonObject().get("tag").getAsString());
    }

    @Test
    void obsidianCannotBeMultipliedThroughCrushingAndCompacting() throws Exception {
        JsonObject block = read(DATA.resolve("recipes/obsidian_block.json"));
        int ingots = 0;
        for (com.google.gson.JsonElement row : block.getAsJsonArray("pattern")) {
            ingots += row.getAsString().replace(" ", "").length();
        }
        JsonObject crushing = read(DATA.resolve("recipes/obsidian_crushing.json"));
        assertTrue(ingots >= crushing.getAsJsonObject("result").get("count").getAsInt());
        assertEquals("forge:ingots/obsidian", block.getAsJsonObject("key").getAsJsonObject("X")
                .get("tag").getAsString());
        assertEquals("basemetals:obsidian_ingot", read(DATA.resolve("recipes/obsidian_ingot.json"))
                .getAsJsonObject("result").get("item").getAsString());
        assertFalse(block.get("pattern").equals(read(DATA.resolve("recipes/obsidian_bars.json"))
                .get("pattern")), "Obsidian blocks must not collide with bars");
    }

    @Test
    void craftingAndRecyclingCannotIncreaseTheSameMaterial() throws Exception {
        Map<String, JsonObject> recipes = new LinkedHashMap<String, JsonObject>();
        try (Stream<Path> files = Files.list(DATA.resolve("recipes"))) {
            for (Path file : (Iterable<Path>) files::iterator) {
                recipes.put(file.getFileName().toString(), read(file));
            }
        }

        int tested = 0;
        for (Map.Entry<String, JsonObject> entry : recipes.entrySet()) {
            if (!entry.getKey().endsWith("_recycling.json")) continue;
            JsonObject craft = recipes.get(entry.getKey().replace("_recycling", ""));
            if (craft == null || !craft.has("pattern")) continue;

            JsonObject result = entry.getValue().getAsJsonObject("result");
            String output = result.get("item").getAsString().split(":")[1];
            String material = output.contains("_") ? output.substring(0, output.indexOf('_')) : output;
            double spent = 0;
            for (com.google.gson.JsonElement row : craft.getAsJsonArray("pattern")) {
                for (char symbol : row.getAsString().toCharArray()) {
                    JsonObject ingredient = craft.getAsJsonObject("key").getAsJsonObject(String.valueOf(symbol));
                    if (ingredient != null) spent += units(ingredient, material);
                }
            }
            if (spent == 0) continue;

            double recovered = (output.endsWith("_nugget") ? 1 : 9) * count(result)
                    * count(craft.getAsJsonObject("result"));
            assertTrue(recovered <= spent, entry.getKey() + " consumes " + spent
                    + " nugget units but recovers " + recovered);
            tested++;
        }
        assertTrue(tested > 400, "Expected the full material recycling catalogue");
    }

    private static int count(JsonObject result) {
        return result.has("count") ? result.get("count").getAsInt() : 1;
    }

    private static Map<String, String> materialDiscovery() {
        Map<String, String> result = new LinkedHashMap<>();
        for (String metal : new String[] {"adamantine", "antimony", "aquarium", "bismuth", "brass", "bronze",
                "coldiron", "copper", "cupronickel", "electrum", "gold", "invar", "iron", "lead", "mercury",
                "mithril", "nickel", "obsidian", "pewter", "platinum", "silver", "starsteel", "steel", "tin", "zinc"}) {
            result.put(metal, "tag=forge:ingots/" + metal);
        }
        for (String gem : new String[] {"diamond", "emerald", "quartz"}) {
            result.put(gem, "tag=forge:gems/" + gem);
        }
        result.put("coal", "item=minecraft:coal");
        result.put("charcoal", "item=minecraft:charcoal");
        result.put("redstone", "tag=forge:dusts/redstone");
        result.put("stone", "tag=forge:stone");
        result.put("wood", "tag=minecraft:logs");
        return result;
    }

    private static double units(JsonObject ingredient, String material) {
        String id = ingredient.has("tag") ? ingredient.get("tag").getAsString()
                : ingredient.get("item").getAsString();
        if (!id.endsWith("/" + material) && !id.endsWith(":" + material + "_ingot")
                && !id.endsWith(":" + material + "_nugget")) return 0;
        if (id.contains("nuggets/") || id.endsWith("_nugget")) return 1;
        if (id.contains("rods/")) return 4.5D;
        if (id.contains("storage_blocks/")) return 81;
        return 9;
    }

    private static JsonObject read(Path path) throws Exception {
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            JsonObject json = new JsonParser().parse(reader).getAsJsonObject();
            return json.has("recipe") ? json.getAsJsonObject("recipe") : json;
        }
    }
}
