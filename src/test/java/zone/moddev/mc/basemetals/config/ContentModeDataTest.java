package zone.moddev.mc.basemetals.config;

import static org.junit.jupiter.api.Assertions.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import zone.moddev.mc.basemetals.network.ContentModeNetwork;

class ContentModeDataTest {
    private static final Path DATA = Paths.get("src/generated/resources/data/basemetals");

    @Test
    void highFantasyRetainsThePreviousAcquisitionCatalogue() throws Exception {
        JsonObject baseline = read(Paths.get("src/test/resources/content/high_fantasy_acquisition.json"));
        assertDigest(DATA.resolve("recipes"), baseline.getAsJsonObject("recipes"));
        assertDigest(DATA.resolve("loot_tables/chests/inject"), baseline.getAsJsonObject("chests"));
    }

    private static void assertDigest(Path directory, JsonObject expected) throws Exception {
        List<Path> files;
        try (Stream<Path> paths = Files.walk(directory)) {
            files = paths.filter(p -> p.toString().endsWith(".json"))
                    .sorted(java.util.Comparator.comparing(p -> directory.relativize(p).toString().replace('\\', '/')))
                    .collect(Collectors.toList());
        }
        assertEquals(expected.get("count").getAsInt(), files.size());
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        for (Path file : files) {
            JsonObject value = read(file);
            if (value.has("recipe")) value = value.getAsJsonObject("recipe");
            if (file.getFileName().toString().equals("mercury_smallpowder_smelting.json")) {
                value.addProperty("type", "minecraft:smelting");
                if (value.get("result").isJsonObject()) {
                    value.add("result", value.getAsJsonObject("result").get("item"));
                }
            }
            stripPolicyConditions(value);
            String relative = directory.relativize(file).toString().replace('\\', '/');
            digest.update((relative + "\n" + canonical(value) + "\n").getBytes(StandardCharsets.UTF_8));
        }
        StringBuilder hash = new StringBuilder();
        for (byte value : digest.digest()) hash.append(String.format("%02x", value & 255));
        assertEquals(expected.get("sha256").getAsString(), hash.toString(), directory.toString());
    }

    private static void stripPolicyConditions(JsonElement value) {
        if (value.isJsonObject()) {
            JsonObject object = value.getAsJsonObject();
            if (object.has("conditions") && object.get("conditions").isJsonArray()) {
                JsonArray retained = new JsonArray();
                for (JsonElement condition : object.getAsJsonArray("conditions")) {
                    if (!condition.isJsonObject() || !condition.getAsJsonObject().has("condition")
                            || !"basemetals:content_mode".equals(condition.getAsJsonObject().get("condition").getAsString())) {
                        retained.add(condition);
                    }
                }
                if (retained.size() == 0) object.remove("conditions");
                else object.add("conditions", retained);
            }
            for (Map.Entry<String, JsonElement> entry : object.entrySet()) stripPolicyConditions(entry.getValue());
        } else if (value.isJsonArray()) {
            for (JsonElement child : value.getAsJsonArray()) stripPolicyConditions(child);
        }
    }

    private static String canonical(JsonElement value) {
        if (value.isJsonObject()) {
            List<String> keys = new ArrayList<>();
            for (Map.Entry<String, JsonElement> entry : value.getAsJsonObject().entrySet()) keys.add(entry.getKey());
            Collections.sort(keys);
            return "{" + keys.stream().map(key -> new com.google.gson.JsonPrimitive(key).toString() + ":"
                    + canonical(value.getAsJsonObject().get(key))).collect(Collectors.joining(",")) + "}";
        }
        if (value.isJsonArray()) {
            List<String> values = new ArrayList<>();
            for (JsonElement child : value.getAsJsonArray()) values.add(canonical(child));
            return "[" + String.join(",", values) + "]";
        }
        if (value.isJsonPrimitive() && value.getAsJsonPrimitive().isNumber()) {
            return value.getAsBigDecimal().stripTrailingZeros().toPlainString();
        }
        return value.toString();
    }

    @Test
    void modeHandshakeUsesParsedValuesRatherThanThePresenceOfAConfigKey() {
        assertTrue(ContentModeNetwork.modesMatch(null, "high_fantasy"));
        assertTrue(ContentModeNetwork.modesMatch(null, null));
        assertTrue(ContentModeNetwork.modesMatch("high_fantasy", " HIGH_FANTASY "));
        assertTrue(ContentModeNetwork.modesMatch("low_fantasy", "low_fantasy"));
        assertFalse(ContentModeNetwork.modesMatch("low_fantasy", null));
        assertFalse(ContentModeNetwork.modesMatch("high_fantasy", "low_fantasy"));
    }

    @Test
    void everyLocaleContainsAllGuiKeysAndMatchingFormatArguments() throws Exception {
        Path directory = Paths.get("src/generated/resources/assets/basemetals/lang");
        JsonObject english = read(directory.resolve("en_us.json"));
        List<String> keys = english.entrySet().stream().map(Map.Entry::getKey)
                .filter(k -> k.startsWith("config.basemetals.")).collect(Collectors.toList());
        assertEquals(21, keys.size());
        try (Stream<Path> files = Files.list(directory)) {
            List<Path> languages = files.filter(p -> p.toString().endsWith(".json")).collect(Collectors.toList());
            assertEquals(18, languages.size());
            for (Path file : languages) {
                JsonObject language = read(file);
                for (String key : keys) {
                    assertTrue(language.has(key), file + " lacks " + key);
                    assertFalse(language.get(key).getAsString().trim().isEmpty(), file + " " + key);
                }
                String message = language.get("config.basemetals.connection.mismatch").getAsString();
                assertEquals(2, message.split("%s", -1).length - 1, file.toString());
            }
        }
    }

    private static JsonObject read(Path file) throws Exception {
        return new JsonParser().parse(new String(Files.readAllBytes(file), StandardCharsets.UTF_8)).getAsJsonObject();
    }
}
