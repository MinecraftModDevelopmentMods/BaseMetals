package zone.moddev.mc.basemetals.data;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import javax.imageio.ImageIO;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

/** Keeps raw metals separate from the powders used to blend alloys. */
class RawMaterialResourceTest {
    private static final Path GENERATED = Path.of("src/generated/resources");
    private static final Path MAIN = Path.of("src/main/resources");
    private static final List<String> METALS = List.of("adamantine", "antimony", "bismuth", "coldiron",
            "lead", "mercury", "nickel", "platinum", "silver", "starsteel", "tin", "zinc");

    @Test
    void allRawMetalsHaveOneToOneProcessingAndDistinctTags() throws Exception {
        for (String metal : METALS) {
            JsonObject crushing = read("data/basemetals/recipes/" + metal + "_raw_crushing.json");
            assertEquals("basemetals:" + metal + "_powder", crushing.getAsJsonObject("result").get("item").getAsString());
            assertEquals(1, crushing.getAsJsonObject("result").get("count").getAsInt());

            for (String cooking : List.of("smelting", "blasting")) {
                JsonObject recipe = read("data/basemetals/recipes/" + metal + "_raw_" + cooking + ".json");
                assertEquals("basemetals:" + metal + "_ingot", recipe.get("result").getAsString());
            }

            String rawTag = Files.readString(GENERATED.resolve("data/forge/tags/items/raw_materials/" + metal + ".json"));
            assertTrue(rawTag.contains("basemetals:" + metal + "_raw"));
            assertFalse(Files.readString(GENERATED.resolve("data/forge/tags/items/dusts/" + metal + ".json")).contains("_raw"));
        }

        try (Stream<Path> recipes = Files.list(GENERATED.resolve("data/basemetals/recipes"))) {
            for (Path recipe : recipes.filter(file -> file.getFileName().toString().contains("blend")).toList()) {
                assertFalse(Files.readString(recipe).contains("raw_materials"), recipe.toString());
                assertFalse(Files.readString(recipe).contains("_raw"), recipe.toString());
            }
        }
    }

    @Test
    void allRawSpritesAreSmallTransparentItems() throws Exception {
        for (String metal : METALS) {
            BufferedImage image = ImageIO.read(MAIN.resolve("assets/basemetals/textures/item/" + metal + "_raw.png").toFile());
            assertEquals(16, image.getWidth(), metal);
            assertEquals(16, image.getHeight(), metal);
            assertTrue(image.getColorModel().hasAlpha(), metal);
            int visible = 0;
            int transparent = 0;
            for (int y = 0; y < 16; y++) {
                for (int x = 0; x < 16; x++) {
                    if ((image.getRGB(x, y) >>> 24) == 0) transparent++;
                    else visible++;
                }
            }
            assertTrue(visible > 30 && transparent > 30, metal);
        }
    }

    @Test
    void everyLocaleNamesAllTwelveRawItems() throws Exception {
        try (Stream<Path> paths = Files.list(GENERATED.resolve("assets/basemetals/lang"))) {
            List<Path> languages = paths.filter(file -> file.toString().endsWith(".json")).toList();
            assertEquals(18, languages.size());
            for (Path language : languages) {
                JsonObject text = new JsonParser().parse(Files.readString(language)).getAsJsonObject();
                for (String metal : METALS) {
                    assertTrue(text.has("item.basemetals." + metal + "_raw"), language + " " + metal);
                    assertFalse(text.get("item.basemetals." + metal + "_raw").getAsString().isBlank());
                }
            }
        }
    }

    private static JsonObject read(String relative) throws Exception {
        return new JsonParser().parse(Files.readString(GENERATED.resolve(relative))).getAsJsonObject();
    }
}
