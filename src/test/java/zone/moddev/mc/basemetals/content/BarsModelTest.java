package zone.moddev.mc.basemetals.content;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

class BarsModelTest {
	private static final Path ASSETS = Paths.get("src/main/resources/assets/basemetals");
	private static final String[] DIRECTIONS = { "east", "north", "south", "west" };

	@Test
	void everyBarsConnectionPatternUsesANonOverlappingModel() throws Exception {
		int models = 0;
		try (Stream<Path> files = Files.list(ASSETS.resolve("blockstates"))) {
			for (final Path state : (Iterable<Path>) files.sorted()::iterator) {
				if (!state.getFileName().toString().endsWith("_bars.json")) {
					continue;
				}

				final JsonObject variants = read(state).getAsJsonObject("variants");
				assertEquals(16, variants.entrySet().size(), state.toString());
				for (int pattern = 0; pattern < 16; pattern++) {
					final List<String> bits = new ArrayList<>();
					for (int bit = 0; bit < DIRECTIONS.length; bit++) {
						bits.add(DIRECTIONS[bit] + "=" + ((pattern & (1 << bit)) != 0));
					}
					final String key = String.join(",", bits);
					assertTrue(variants.has(key), state + " lacks " + key);
					final String model = variants.getAsJsonObject(key).get("model").getAsString();
					checkFaces(resolveModel(model, new HashSet<>()), model);
				}
				models++;
			}
		}

		assertEquals(27, models);
	}

	private static JsonArray resolveModel(final String id, final Set<String> parents) throws Exception {
		assertTrue(parents.add(id), "Model parent cycle: " + id);
		assertTrue(id.startsWith("basemetals:"), "Unexpected bars model " + id);
		String name = id.substring("basemetals:".length());
		if (!name.startsWith("block/")) {
			name = "block/" + name;
		}
		final JsonObject model = read(ASSETS.resolve("models/" + name + ".json"));
		return model.has("elements") ? model.getAsJsonArray("elements")
				: resolveModel(model.get("parent").getAsString(), parents);
	}

	private static void checkFaces(final JsonArray elements, final String model) {
		final List<Face> faces = new ArrayList<>();
		for (final JsonElement value : elements) {
			final JsonObject element = value.getAsJsonObject();
			for (final Map.Entry<String, JsonElement> side : element.getAsJsonObject("faces").entrySet()) {
				faces.add(new Face(side.getKey(), coordinates(element, "from"), coordinates(element, "to")));
			}
		}

		for (int first = 0; first < faces.size(); first++) {
			for (int second = first + 1; second < faces.size(); second++) {
				assertFalse(faces.get(first).overlaps(faces.get(second)), model + " has overlapping faces");
			}
		}
	}

	private static double[] coordinates(final JsonObject element, final String name) {
		final JsonArray values = element.getAsJsonArray(name);
		return new double[] { values.get(0).getAsDouble(), values.get(1).getAsDouble(), values.get(2).getAsDouble() };
	}

	private static JsonObject read(final Path path) throws Exception {
		try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			return new JsonParser().parse(reader).getAsJsonObject();
		}
	}

	private static final class Face {
		private final String side;
		private final int axis;
		private final double[] from;
		private final double[] to;
		private final double plane;

		private Face(final String side, final double[] from, final double[] to) {
			this.side = side;
			this.axis = "west".equals(side) || "east".equals(side) ? 0
					: "down".equals(side) || "up".equals(side) ? 1 : 2;
			this.from = from;
			this.to = to;
			this.plane = "west".equals(side) || "down".equals(side) || "north".equals(side)
					? from[axis] : to[axis];
		}

		private boolean overlaps(final Face other) {
			if (!side.equals(other.side) || Math.abs(plane - other.plane) > 0.01) {
				return false;
			}
			for (int dimension = 0; dimension < 3; dimension++) {
				if (dimension != axis && Math.min(to[dimension], other.to[dimension])
						- Math.max(from[dimension], other.from[dimension]) < 0.000001) {
					return false;
				}
			}
			return true;
		}
	}
}
