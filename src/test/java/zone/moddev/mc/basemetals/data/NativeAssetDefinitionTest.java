package zone.moddev.mc.basemetals.data;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

class NativeAssetDefinitionTest {
    private static final Path ASSETS = Path.of("src/generated/resources/assets/basemetals");

    @Test
    void everySavedItemHasANativeModelDefinition() throws Exception {
        var manifest = read(Path.of("src/generated/resources/data/basemetals/registry_manifest.json"));
        int count = 0;
        for (var item : manifest.getAsJsonArray("items")) {
            String id = item.getAsString().substring("basemetals:".length());
            var definition = read(ASSETS.resolve("items/" + id + ".json"));
            assertTrue(definition.has("model"), id);
            checkModels(definition.get("model"));
            count++;
        }
        assertEquals(1127, count);
    }

    @Test
    void armourAssetsHaveBothPlayerLayersAndTheHorseLayer() throws Exception {
        try (var files = Files.list(ASSETS.resolve("equipment"))) {
            for (Path file : files.toList()) {
                var layers = read(file).getAsJsonObject("layers");
                for (String layer : new String[] {"humanoid", "humanoid_leggings", "horse_body"}) {
                    String texture = layers.getAsJsonArray(layer).get(0).getAsJsonObject()
                            .get("texture").getAsString().substring("basemetals:".length());
                    assertTrue(Files.isRegularFile(ASSETS.resolve(
                            "textures/entity/equipment/" + layer + "/" + texture + ".png")), file.toString());
                }
            }
        }
    }

    @Test
    void filledBucketsKeepTheirSpritesOnTheItemAtlas() throws Exception {
        var atlas = read(Path.of("src/main/resources/assets/minecraft/atlases/items.json"));
        assertEquals(5, atlas.getAsJsonArray("sources").size());
        assertFalse(Files.exists(Path.of("src/main/resources/assets/minecraft/atlases/blocks.json")));

        for (var source : atlas.getAsJsonArray("sources")) {
            var alias = source.getAsJsonObject();
            assertTrue(alias.get("sprite").getAsString().startsWith("basemetals:item/"));
            assertEquals("minecraft:single", alias.get("type").getAsString());
        }

        try (var files = Files.list(ASSETS.resolve("models/item"))) {
            var buckets = files.filter(file -> file.getFileName().toString().endsWith("_bucket.json")).toList();
            assertEquals(36, buckets.size());

            for (Path file : buckets) {
                var textures = read(file).getAsJsonObject("textures");
                assertEquals("basemetals:item/bucket_base", textures.get("base").getAsString());
                assertEquals("basemetals:item/bucket_fluid", textures.get("fluid").getAsString());
                assertEquals("basemetals:item/bucket_cover", textures.get("cover").getAsString());
                assertEquals("#base", textures.get("particle").getAsString());
            }
        }
    }

    @Test
    void copperAgeAddsNoNewSaveIdentities() {
        assertEquals(18, zone.moddev.mc.basemetals.compat.VanillaCopper.equivalents().size());
        assertEquals("minecraft:copper_nugget", zone.moddev.mc.basemetals.compat.VanillaCopper.output("basemetals:copper_nugget"));
        assertEquals("minecraft:copper_horse_armor", zone.moddev.mc.basemetals.compat.VanillaCopper.output("basemetals:copper_horse_armor"));
        assertEquals("basemetals:copper_bow", zone.moddev.mc.basemetals.compat.VanillaCopper.output("basemetals:copper_bow"));
    }

    private static void checkModels(JsonElement element) {
        if (element.isJsonArray()) {
            element.getAsJsonArray().forEach(NativeAssetDefinitionTest::checkModels);
        } else if (element.isJsonObject()) {
            var object = element.getAsJsonObject();
            if (object.has("model") && object.get("model").isJsonPrimitive()) {
                String model = object.get("model").getAsString();
                if (model.startsWith("basemetals:")) {
                    String path = "models/" + model.substring("basemetals:".length()) + ".json";
                    assertTrue(Files.isRegularFile(ASSETS.resolve(path)) || Files.isRegularFile(
                            Path.of("src/main/resources/assets/basemetals").resolve(path)), model);
                }
            }
            object.entrySet().forEach(entry -> checkModels(entry.getValue()));
        }
    }

    private static JsonObject read(Path file) throws Exception {
        return JsonParser.parseString(Files.readString(file)).getAsJsonObject();
    }
}
