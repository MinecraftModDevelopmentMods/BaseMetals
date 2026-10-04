package zone.moddev.mc.basemetals.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import zone.moddev.mc.basemetals.material.MaterialCatalogue;
import zone.moddev.mc.basemetals.material.MaterialDefinition;

class OreOverlayModelTest {
    @TempDir Path resources;

    @Test
    void kirisTwoLayerGeometryKeepsTheOffsetAndAllSixCullFaces() {
        JsonObject model = BMBlockStateProvider.createOverlayOre(
                "minecraft:block/stone", "basemetals:block/ore_overlays/tin_ore");

        assertEquals("minecraft:block/block", model.get("parent").getAsString());
        JsonObject textures = model.getAsJsonObject("textures");
        assertEquals("minecraft:block/stone", textures.get("particle").getAsString());
        assertEquals("minecraft:block/stone", textures.get("base").getAsString());
        assertEquals("basemetals:block/ore_overlays/tin_ore", textures.get("overlay").getAsString());

        JsonArray elements = model.getAsJsonArray("elements");
        assertEquals(2, elements.size());
        assertElement(elements.get(0).getAsJsonObject(), 0, 16, "#base");
        assertElement(elements.get(1).getAsJsonObject(), -0.001, 16.001, "#overlay");
    }

    @Test
    void everyOreUsesItsVanillaHostTexture() {
        int ores = 0;
        for (MaterialDefinition material : MaterialCatalogue.ALL) {
            if (!material.hasOre()) continue;
            ores++;

            String expected = "minecraft:block/stone";
            if ("adamantine".equals(material.name()) || "coldiron".equals(material.name())) {
                expected = "minecraft:block/netherrack";
            } else if ("starsteel".equals(material.name())) {
                expected = "minecraft:block/end_stone";
            }

            assertEquals(expected, BMBlockStateProvider.baseTexture(material.name()));
            JsonObject model = BMBlockStateProvider.modelFor(material.name() + "_ore", expected,
                    "basemetals:block/ore_overlays/" + material.name() + "_ore", true);
            assertEquals(expected, model.getAsJsonObject("textures").get("base").getAsString());
        }

        assertEquals(13, ores);
    }

    @Test
    void shippedOreModelsUseKirisOverlays() throws Exception {
        Path mainResources = Paths.get("src/main/resources");
        int ores = 0;

        for (MaterialDefinition material : MaterialCatalogue.ALL) {
            if (!material.hasOre()) continue;
            ores++;

            String name = material.name() + "_ore";
            Path texture = mainResources.resolve("assets/basemetals/textures/block/ore_overlays/" + name + ".png");
            assertTrue(Files.isRegularFile(texture), name + " overlay is in the wrong directory or missing");

            Path modelPath = mainResources.resolve("assets/basemetals/models/block/" + name + ".json");
            JsonObject model = new JsonParser().parse(new String(Files.readAllBytes(modelPath),
                    StandardCharsets.UTF_8)).getAsJsonObject();

            assertEquals(BMBlockStateProvider.baseTexture(material.name()),
                    model.getAsJsonObject("textures").get("base").getAsString(), name);
            assertEquals("basemetals:block/ore_overlays/" + name,
                    model.getAsJsonObject("textures").get("overlay").getAsString(), name);
            assertEquals(2, model.getAsJsonArray("elements").size(), name);
        }

        assertEquals(13, ores);
    }

    @Test
    void missingOverlaysKeepTheCurrentArtworkWithoutMissingTextureReferences() {
        JsonObject model = BMBlockStateProvider.modelFor("tin_ore", "minecraft:block/stone",
                "basemetals:block/ore_overlays/tin_ore", false);

        assertEquals("block/cube_all", model.get("parent").getAsString());
        assertEquals("basemetals:block/tin_ore", model.getAsJsonObject("textures").get("all").getAsString());
        assertFalse(model.has("elements"));
        assertFalse(model.toString().contains("ore_overlays"));
    }

    @Test
    void generationEnablesOnlySuppliedOverlaysAndCanReturnToTheLegacyModel() throws Exception {
        Path overlay = resources.resolve("assets/basemetals/textures/block/ore_overlays/tin_ore.png");
        Files.createDirectories(overlay.getParent());
        Files.copy(Paths.get("src/main/resources/assets/basemetals/textures/block/tin_ore.png"), overlay);

        BMBlockStateProvider.main(new String[] {resources.toString()});
        assertTrue(readModel("tin_ore").has("elements"));
        assertFalse(readModel("copper_ore").has("elements"));
        byte[] first = Files.readAllBytes(modelPath("tin_ore"));
        BMBlockStateProvider.main(new String[] {resources.toString()});
        org.junit.jupiter.api.Assertions.assertArrayEquals(first, Files.readAllBytes(modelPath("tin_ore")));

        Files.delete(overlay);
        BMBlockStateProvider.main(new String[] {resources.toString()});
        assertFalse(readModel("tin_ore").has("elements"));
    }

    @Test
    void generationRepairsInvalidJsonAndKeepsLineEndingsStable() throws Exception {
        BMBlockStateProvider.main(new String[] {resources.toString()});
        byte[] expected = Files.readAllBytes(modelPath("tin_ore"));

        Files.write(modelPath("tin_ore"), "{unfinished model".getBytes(StandardCharsets.UTF_8));
        BMBlockStateProvider.main(new String[] {resources.toString()});
        org.junit.jupiter.api.Assertions.assertArrayEquals(expected, Files.readAllBytes(modelPath("tin_ore")));

        String windowsModel = new String(expected, StandardCharsets.UTF_8).replace("\n", "\r\n");
        Files.write(modelPath("tin_ore"), windowsModel.getBytes(StandardCharsets.UTF_8));
        BMBlockStateProvider.main(new String[] {resources.toString()});
        org.junit.jupiter.api.Assertions.assertArrayEquals(expected, Files.readAllBytes(modelPath("tin_ore")));
    }

    @Test
    void verificationOutputDoesNotRewriteTheSourceModels() throws Exception {
        BMBlockStateProvider.main(new String[] {resources.toString()});
        byte[] original = Files.readAllBytes(modelPath("tin_ore"));

        Path overlay = resources.resolve("assets/basemetals/textures/block/ore_overlays/tin_ore.png");
        Files.createDirectories(overlay.getParent());
        Files.copy(Paths.get("src/main/resources/assets/basemetals/textures/block/ore_overlays/tin_ore.png"), overlay);

        Path output = resources.resolve("verification-output");
        BMBlockStateProvider.main(new String[] {resources.toString(), "--output", output.toString()});

        org.junit.jupiter.api.Assertions.assertArrayEquals(original, Files.readAllBytes(modelPath("tin_ore")));
        JsonObject generated = new JsonParser().parse(new String(Files.readAllBytes(output.resolve(
                "assets/basemetals/models/block/tin_ore.json")), StandardCharsets.UTF_8)).getAsJsonObject();
        assertEquals("basemetals:block/ore_overlays/tin_ore",
                generated.getAsJsonObject("textures").get("overlay").getAsString());
        assertFalse(Files.exists(output.resolve("assets/minecraft")), "Verification must not add client fixtures");
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

    private Path modelPath(String name) {
        return resources.resolve("assets/basemetals/models/block/" + name + ".json");
    }

    private JsonObject readModel(String name) throws Exception {
        return new JsonParser().parse(new String(Files.readAllBytes(modelPath(name)),
                StandardCharsets.UTF_8)).getAsJsonObject();
    }
}
