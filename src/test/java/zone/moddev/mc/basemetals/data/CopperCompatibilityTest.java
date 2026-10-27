package zone.moddev.mc.basemetals.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import zone.moddev.mc.basemetals.compat.VanillaCopper;

class CopperCompatibilityTest {
    private static final Path DATA = Path.of("src/generated/resources/data/basemetals");

    @Test
    void overlappingCraftingRecipesProduceVanillaItems() throws Exception {
        for (var entry : VanillaCopper.equivalents().entrySet()) {
            String id = entry.getKey().substring("basemetals:".length());
            String recipeId = switch (id) {
                case "copper_ingot" -> "copper_ingot_from_nuggets";
                case "copper_nugget" -> "copper_nuggets";
                case "copper_horse_armor" -> "copper_horsearmor";
                default -> id;
            };

            assertEquals(entry.getValue(), recipe(recipeId).getAsJsonObject("result")
                    .get("id").getAsString(), recipeId);
        }
    }

    @Test
    void oldCopperItemsAndBlocksRemainRegistered() throws Exception {
        var manifest = read(DATA.resolve("registry_manifest.json"));

        for (String item : VanillaCopper.equivalents().keySet()) {
            assertTrue(manifest.getAsJsonArray("items").toString().contains('"' + item + '"'), item);

            if (java.util.List.of("block", "door", "trapdoor", "slab", "stairs", "bars")
                    .contains(item.substring("basemetals:copper_".length()))) {
                assertTrue(manifest.getAsJsonArray("blocks").toString().contains('"' + item + '"'), item);
            }
        }
    }

    @Test
    void downstreamIngredientsAcceptOldAndVanillaCopper() throws Exception {
        for (var entry : VanillaCopper.equivalents().entrySet()) {
            String tag = VanillaCopper.ingredientTag(entry.getKey()).substring("forge:".length());
            var values = read(Path.of("src/generated/resources/data/forge/tags/item/" + tag + ".json"))
                    .getAsJsonArray("values").toString();

            assertTrue(values.contains('"' + entry.getKey() + '"'), entry.getKey());
            assertTrue(values.contains('"' + entry.getValue() + '"'), entry.getValue());
        }

        assertEquals("forge:ingots/copper", recipe("copper_nuggets").getAsJsonArray("ingredients")
                .get(0).getAsJsonObject().get("tag").getAsString());
    }

    @Test
    void nativeTrapdoorsCannotBeRecycledAtTheOldHigherPrice() throws Exception {
        var craft = recipe("copper_trapdoor");
        var nativeRecycle = recipe("vanilla_copper_trapdoor_recycling");
        var legacyRecycle = recipe("copper_trapdoor_recycling");
        int inputs = 0;

        for (var row : craft.getAsJsonArray("pattern")) {
            inputs += row.getAsString().replace(" ", "").length();
        }

        assertEquals("minecraft:copper_trapdoor", nativeRecycle.getAsJsonObject("ingredient")
                .get("item").getAsString());
        assertEquals(3, nativeRecycle.getAsJsonObject("result").get("count").getAsInt());
        assertEquals("basemetals:copper_trapdoor", legacyRecycle.getAsJsonObject("ingredient")
                .get("item").getAsString());
        assertEquals(4, legacyRecycle.getAsJsonObject("result").get("count").getAsInt());
        assertTrue(nativeRecycle.getAsJsonObject("result").get("count").getAsInt()
                * craft.getAsJsonObject("result").get("count").getAsInt() <= inputs);
    }

    private static JsonObject recipe(String id) throws Exception {
        JsonObject root = read(DATA.resolve("recipe/" + id + ".json"));
        return root.has("recipe") ? root.getAsJsonObject("recipe") : root;
    }

    private static JsonObject read(Path path) throws Exception {
        return RecipeDataSemantics.normalize(JsonParser.parseString(Files.readString(path))).getAsJsonObject();
    }
}
