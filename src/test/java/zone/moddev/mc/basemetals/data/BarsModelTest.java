package zone.moddev.mc.basemetals.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

class BarsModelTest {
    private static final Path MAIN = Path.of("src/main/resources/assets/basemetals/models");
    private static final Path GENERATED = Path.of("src/generated/resources");
    private static final List<String> CONNECTIONS = List.of("north", "south", "west", "east");

    @Test
    void everyConnectionPatternHasNoOverlappingFaces() throws Exception {
        JsonArray blocks = read(GENERATED.resolve("data/basemetals/registry_manifest.json"))
                .getAsJsonArray("blocks");
        int materials = 0;

        for (JsonElement block : blocks) {
            String id = block.getAsString().substring("basemetals:".length());
            if (!id.endsWith("_bars")) continue;
            materials++;

            JsonArray parts = read(GENERATED.resolve("assets/basemetals/blockstates/" + id + ".json"))
                    .getAsJsonArray("multipart");

            for (int connections = 0; connections < 16; connections++) {
                List<Face> faces = new ArrayList<>();

                for (JsonElement part : parts) {
                    JsonObject entry = part.getAsJsonObject();
                    if (!matches(entry, connections)) continue;

                    JsonObject apply = entry.getAsJsonObject("apply");
                    String model = apply.get("model").getAsString().substring("basemetals:".length());
                    int turns = apply.has("y") ? apply.get("y").getAsInt() / 90 : 0;
                    Path path = MAIN.resolve(model + ".json");
                    if (!Files.exists(path)) {
                        path = GENERATED.resolve("assets/basemetals/models/" + model + ".json");
                    }
                    addFaces(faces, read(path), turns, model);
                }

                for (int first = 0; first < faces.size(); first++) {
                    for (int second = first + 1; second < faces.size(); second++) {
                        Face a = faces.get(first);
                        Face b = faces.get(second);
                        assertFalse(a.overlaps(b), id + " connections=" + connections
                                + " has overlapping faces: " + a.source() + " and " + b.source());
                    }
                }
            }
        }

        assertEquals(26, materials);
    }

    private static boolean matches(JsonObject part, int connections) {
        if (!part.has("when")) return true;

        for (Map.Entry<String, JsonElement> condition : part.getAsJsonObject("when").entrySet()) {
            boolean connected = (connections & (1 << CONNECTIONS.indexOf(condition.getKey()))) != 0;
            if (connected != condition.getValue().getAsBoolean()) return false;
        }

        return true;
    }

    private static void addFaces(List<Face> faces, JsonObject model, int turns, String source) {
        for (JsonElement entry : model.getAsJsonArray("elements")) {
            JsonObject element = entry.getAsJsonObject();
            double[] from = coordinates(element.getAsJsonArray("from"));
            double[] to = coordinates(element.getAsJsonArray("to"));

            for (String direction : element.getAsJsonObject("faces").keySet()) {
                int axis = switch (direction) {
                    case "west", "east" -> 0;
                    case "down", "up" -> 1;
                    default -> 2;
                };
                int sign = List.of("west", "down", "north").contains(direction) ? -1 : 1;
                double[] low = from.clone();
                double[] high = to.clone();
                low[axis] = high[axis] = sign < 0 ? from[axis] : to[axis];

                for (int turn = 0; turn < turns; turn++) {
                    double lowX = low[0];
                    double highX = high[0];
                    low[0] = 16 - high[2];
                    high[0] = 16 - low[2];
                    low[2] = lowX;
                    high[2] = highX;
                    if (axis == 0) {
                        axis = 2;
                    } else if (axis == 2) {
                        axis = 0;
                        sign = -sign;
                    }
                }

                faces.add(new Face(axis, sign, low, high, source + "/" + direction));
            }
        }
    }

    private static double[] coordinates(JsonArray values) {
        return new double[] {values.get(0).getAsDouble(), values.get(1).getAsDouble(),
                values.get(2).getAsDouble()};
    }

    private static JsonObject read(Path path) throws Exception {
        return JsonParser.parseString(Files.readString(path)).getAsJsonObject();
    }

    private record Face(int axis, int sign, double[] low, double[] high, String source) {
        boolean overlaps(Face other) {
            if (axis != other.axis || sign != other.sign
                    || Math.abs(low[axis] - other.low[axis]) > 0.01) return false;

            for (int dimension = 0; dimension < 3; dimension++) {
                if (dimension == axis) continue;
                if (Math.min(high[dimension], other.high[dimension])
                        - Math.max(low[dimension], other.low[dimension]) < 0.000001) return false;
            }

            return true;
        }
    }
}
