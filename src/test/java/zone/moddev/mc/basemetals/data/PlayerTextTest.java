package zone.moddev.mc.basemetals.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

class PlayerTextTest {
    private static final Path LANGUAGES = Paths.get("src/generated/resources/assets/basemetals/lang");
    private static final Pattern ARGUMENT = Pattern.compile("%(?:[0-9]+\\$)?[sd]");

    @Test
    void allLanguagesKeepTheReviewedTextAndItsFormatArguments() throws Exception {
        JsonObject config = read(Paths.get("tools/config_translations.json"));
        JsonObject players = read(Paths.get("tools/player_translations.json")).getAsJsonObject("locales");
        JsonObject english = players.getAsJsonObject("en_us");

        try (Stream<Path> files = Files.list(LANGUAGES)) {
            List<Path> languages = files.filter(path -> path.toString().endsWith(".json"))
                    .collect(Collectors.toList());
            assertEquals(18, languages.size());

            for (Path file : languages) {
                String locale = file.getFileName().toString().replace(".json", "");
                if (config.getAsJsonObject("aliases").has(locale)) {
                    locale = config.getAsJsonObject("aliases").get(locale).getAsString();
                }

                JsonObject expected = players.getAsJsonObject(locale);
                JsonObject actual = read(file);
                assertEquals(english.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toSet()),
                        expected.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toSet()), locale);

                for (Map.Entry<String, JsonElement> entry : expected.entrySet()) {
                    String key = entry.getKey();
                    String text = actual.get(key).getAsString();
                    assertEquals(entry.getValue().getAsString(), text, file + " " + key);
                    assertFalse(text.trim().isEmpty(), file + " " + key);
                    assertFalse(text.contains("???"), file + " " + key);
                    assertEquals(arguments(english.get(key).getAsString()), arguments(text), file + " " + key);
                }
            }
        }
    }

    @Test
    void configHelpDescribesGameplayRatherThanRegistrationDetails() throws Exception {
        JsonObject english = read(LANGUAGES.resolve("en_us.json"));
        for (Map.Entry<String, JsonElement> entry : english.entrySet()) {
            if (!entry.getKey().startsWith("config.basemetals.")) continue;
            String text = entry.getValue().getAsString();
            for (String jargon : new String[] { "registered items", "material forms", "acquisition", "server ticks" }) {
                assertFalse(text.contains(jargon), entry.getKey() + " " + jargon);
            }
        }

        assertTrue(english.get("config.basemetals.starsteelRegeneration.tooltip").getAsString().contains("10 seconds"));
        assertTrue(english.get("config.basemetals.connection.mismatch").getAsString().contains("Mods → Base Metals → Config"));
    }

    @Test
    void advancementHintsNameTheActualBlendIngredients() throws Exception {
        JsonObject english = read(LANGUAGES.resolve("en_us.json"));
        assertTrue(english.get("advancements.basemetals.pewter_maker.description").getAsString()
                .contains("tin, copper and lead"));
        assertTrue(english.get("advancements.basemetals.aquarium_maker.description").getAsString()
                .contains("prismarine powders"));
        assertTrue(english.get("advancements.basemetals.steel_maker.description").getAsString()
                .contains("coal powders"));
    }

    private static List<String> arguments(String text) {
        List<String> result = new ArrayList<>();
        Matcher matcher = ARGUMENT.matcher(text);
        while (matcher.find()) result.add(matcher.group());
        return result;
    }

    private static JsonObject read(Path file) throws Exception {
        return new JsonParser().parse(new String(Files.readAllBytes(file), StandardCharsets.UTF_8)).getAsJsonObject();
    }
}
