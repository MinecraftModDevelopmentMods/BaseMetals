package zone.moddev.mc.basemetals.data;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

class CommonTagCompatibilityTest {
    @Test
    void oldForgeNamesLeadToValidCommonTagsWithoutCycles() throws Exception {
        Map<String, List<String>> tags = new LinkedHashMap<>();
        for (String source : List.of("src/main/resources/data", "src/generated/resources/data")) {
            Path root = Path.of(source);
            try (var files = Files.walk(root)) {
                for (Path file : files.filter(path -> path.toString().endsWith(".json")).toList()) {
                    String relative = root.relativize(file).toString().replace('\\', '/');
                    String[] parts = relative.split("/", 4);
                    if (parts.length != 4 || !parts[1].equals("tags")) continue;
                    var values = JsonParser.parseString(Files.readString(file)).getAsJsonObject().getAsJsonArray("values");
                    if (values == null) continue;
                    String id = parts[2] + "/" + parts[0] + ":" + parts[3].replaceFirst("\\.json$", "");
                    tags.put(id, values.asList().stream().filter(value -> value.isJsonPrimitive())
                            .map(value -> value.getAsString()).toList());
                }
            }
        }

        for (String legacy : Files.readAllLines(Path.of("src/test/resources/legacy_forge_tag_paths.txt"))) {
            if (legacy.isBlank() || legacy.startsWith("#")) continue;
            int slash = legacy.indexOf('/');
            String id = legacy.substring(0, slash) + "/forge:" + legacy.substring(slash + 1);
            assertTrue(tags.containsKey(id), "Lost the released tag " + id);
        }

        int aliases = 0;
        for (var entry : tags.entrySet()) {
            if (!entry.getKey().contains("/forge:")) continue;
            assertEquals(1, entry.getValue().size(), entry.getKey());
            assertTrue(entry.getValue().getFirst().startsWith("#c:"), entry.getKey());
            visit(entry.getKey(), tags, new HashSet<>());
            aliases++;
        }
        assertTrue(aliases >= 458, "The legacy material tags were not all retained");
    }

    private static void visit(String id, Map<String, List<String>> tags, Set<String> path) throws Exception {
        assertTrue(path.add(id), "Tag cycle: " + path + " -> " + id);
        var values = tags.get(id);
        if (values == null) {
            String kind = id.substring(0, id.indexOf('/'));
            String[] name = id.substring(id.indexOf('/') + 1).split(":", 2);
            String resource = "/data/" + name[0] + "/tags/" + kind + "/" + name[1] + ".json";
            try (var input = CommonTagCompatibilityTest.class.getResourceAsStream(resource)) {
                assertNotNull(input, "Missing native common tag " + id);
            }
        } else {
            String kind = id.substring(0, id.indexOf('/') + 1);
            for (String value : values) {
                if (value.startsWith("#")) visit(kind + value.substring(1), tags, path);
            }
        }
        path.remove(id);
    }
}
