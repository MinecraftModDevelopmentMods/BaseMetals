package zone.moddev.mc.basemetals.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class NeoForgeResourcesTest {
    private static final Path ROOT = projectRoot();
    private static final Path DATA = ROOT.resolve("src/generated/resources/data");

    static Path projectRoot() {
        String configured = System.getProperty("basemetals.test.projectDir");
        if (configured != null && !configured.isBlank()) return Path.of(configured);
        Path working = Path.of(".").toAbsolutePath().normalize();
        for (Path candidate = working; candidate != null; candidate = candidate.getParent()) {
            if (Files.isRegularFile(candidate.resolve("build.gradle"))
                    && Files.isDirectory(candidate.resolve("src/generated/resources/data/basemetals"))) return candidate;
        }
        return working;
    }

    @Test
    void projectRootFallsBackWhenTheIdeDoesNotSetAGradleProperty() {
        String configured = System.getProperty("basemetals.test.projectDir");
        try {
            System.clearProperty("basemetals.test.projectDir");
            assertEquals(ROOT.toAbsolutePath().normalize(), projectRoot());
            System.setProperty("basemetals.test.projectDir", "");
            assertEquals(ROOT.toAbsolutePath().normalize(), projectRoot());
            System.setProperty("basemetals.test.projectDir", "example-checkout");
            assertEquals(Path.of("example-checkout"), projectRoot());
        } finally {
            if (configured == null) System.clearProperty("basemetals.test.projectDir");
            else System.setProperty("basemetals.test.projectDir", configured);
        }
    }

    @Test
    void recipeDiscoveryUsesNativeAdvancementFilesAndStableIds() throws Exception {
        Path file = DATA.resolve("basemetals/advancement/recipes/adamantine_bow.json");

        assertTrue(Files.isRegularFile(file), "The Adamantine bow needs its recipe-book unlock");
        assertFalse(Files.isDirectory(DATA.resolve("basemetals/advancements")));

        JsonObject unlock = read(file);

        assertEquals("minecraft:recipes/root", unlock.get("parent").getAsString());
        assertEquals("basemetals:adamantine_bow", unlock.getAsJsonObject("rewards")
                .getAsJsonArray("recipes").get(0).getAsString());
        assertEquals("#c:ingots/adamantine", unlock.getAsJsonObject("criteria")
                .getAsJsonObject("has_material").getAsJsonObject("conditions")
                .getAsJsonArray("items").get(0).getAsJsonObject().get("items").getAsString());

        try (var files = Files.walk(DATA.resolve("basemetals/advancement"))) {
            assertTrue(files.filter(path -> path.toString().endsWith(".json")).count() > 1_000);
        }
    }

    @Test
    void chestAdditionsUseNeoForgeRegistrationAndConditions() throws Exception {
        Path registration = DATA.resolve("neoforge/loot_modifiers/global_loot_modifiers.json");

        assertTrue(Files.isRegularFile(registration), "NeoForge must be able to find the loot additions");
        assertFalse(Files.exists(DATA.resolve("forge/loot_modifiers/global_loot_modifiers.json")));

        var entries = read(registration).getAsJsonArray("entries");

        assertEquals(12, entries.size());

        for (var entry : entries) {
            String id = entry.getAsString().substring("basemetals:".length());
            JsonObject modifier = read(DATA.resolve("basemetals/loot_modifiers/" + id + ".json"));

            assertEquals("neoforge:loot_table_id", modifier.getAsJsonArray("conditions")
                    .get(0).getAsJsonObject().get("condition").getAsString());
        }
    }

    @Test
    void everyLocaleIncludesTheCurrentPlayerText() throws Exception {
        Path languages = ROOT.resolve("src/generated/resources/assets/basemetals/lang");
        var expected = read(languages.resolve("en_us.json")).keySet().stream()
                .filter(key -> key.startsWith("config.basemetals.")
                        || key.startsWith("message.basemetals.")
                        || key.startsWith("advancements.basemetals.")
                        || key.endsWith("_raw") || key.endsWith("_bucket"))
                .collect(java.util.stream.Collectors.toSet());

        try (var files = Files.list(languages)) {
            var locales = files.filter(path -> path.toString().endsWith(".json")).toList();

            assertEquals(18, locales.size());

            for (Path locale : locales) {
                assertTrue(read(locale).keySet().containsAll(expected), locale.toString());
            }
        }
    }

    private static JsonObject read(Path file) throws Exception {
        return JsonParser.parseString(Files.readString(file)).getAsJsonObject();
    }
}
