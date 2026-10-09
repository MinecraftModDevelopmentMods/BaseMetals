package zone.moddev.mc.basemetals.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import zone.moddev.mc.basemetals.material.MaterialCatalogue;
import zone.moddev.mc.basemetals.material.MaterialDefinition;

class OreOverlayModelTest {
    @Test
    void allThirteenNativeModelsKeepKirisTwoLayerGeometry() throws Exception {
        int ores = 0;
        for (MaterialDefinition material : MaterialCatalogue.ALL) {
            if (!material.hasOre()) continue;
            ores++;

            String name = material.name() + "_ore";
            String host = "minecraft:block/stone";
            if ("adamantine".equals(material.name()) || "coldiron".equals(material.name())) {
                host = "minecraft:block/netherrack";
            } else if ("starsteel".equals(material.name())) {
                host = "minecraft:block/end_stone";
            }

            Path modelPath = Paths.get("src/generated/resources/assets/basemetals/models/block/" + name + ".json");
            JsonObject model = new JsonParser().parse(Files.readString(modelPath)).getAsJsonObject();
            assertEquals("minecraft:block/block", model.get("parent").getAsString());
            JsonObject textures = model.getAsJsonObject("textures");
            assertEquals(host, textures.get("base").getAsString());
            assertEquals(host, textures.get("particle").getAsString());
            assertEquals("basemetals:block/ore_overlays/" + name, textures.get("overlay").getAsString());
            assertEquals(2, model.getAsJsonArray("elements").size());
            assertElement(model.getAsJsonArray("elements").get(0).getAsJsonObject(), 0, 16, "#base");
            assertElement(model.getAsJsonArray("elements").get(1).getAsJsonObject(), -0.05, 16.05, "#overlay");

            assertTrue(Files.isRegularFile(Paths.get("src/main/resources/assets/basemetals/textures/block/ore_overlays/" + name + ".png")));
            assertFalse(Files.exists(Paths.get("src/main/resources/assets/basemetals/models/block/" + name + ".json")),
                    "Ore models must have one authoritative location");
        }
        assertEquals(13, ores);
    }

    @Test
    void providerUsesForgeAndRemainsOutsideTheProductionSourceSet() throws Exception {
        String provider = Files.readString(Paths.get("src/dataGenerator/java/zone/moddev/mc/basemetals/data/BMBlockStateProvider.java"));
        assertTrue(provider.contains("extends BlockStateProvider"));
        assertTrue(provider.contains(".allFaces((direction, face) -> face.texture(\"#overlay\").cullface(direction))"));
        assertTrue(provider.contains("@author KiriCattus"));
        assertFalse(Files.isRegularFile(Paths.get("src/modelGenerator/java/zone/moddev/mc/basemetals/data/BMBlockStateProvider.java")));
        assertFalse(Files.exists(Paths.get("src/main/java/zone/moddev/mc/basemetals/data/BMBlockStateProvider.java")));
    }

    private static void assertElement(JsonObject element, double from, double to, String texture) {
        for (int axis = 0; axis < 3; axis++) {
            assertEquals(from, element.getAsJsonArray("from").get(axis).getAsDouble(), 0.000001);
            assertEquals(to, element.getAsJsonArray("to").get(axis).getAsDouble(), 0.000001);
        }
        JsonObject faces = element.getAsJsonObject("faces");
        assertEquals(6, faces.entrySet().size());
        for (Map.Entry<String, JsonElement> face : faces.entrySet()) {
            assertEquals(texture, face.getValue().getAsJsonObject().get("texture").getAsString());
            assertEquals(face.getKey(), face.getValue().getAsJsonObject().get("cullface").getAsString());
        }
    }
}
