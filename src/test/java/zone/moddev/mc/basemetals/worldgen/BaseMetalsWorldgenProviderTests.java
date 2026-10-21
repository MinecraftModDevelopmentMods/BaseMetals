package zone.moddev.mc.basemetals.worldgen;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import org.junit.jupiter.api.Test;

class BaseMetalsWorldgenProviderTests {

	@Test
	void preservesTheElevenHistoricalRules() throws Exception {
		final JsonObject provider = BaseMetalsWorldgenProvider.build(block -> true).get().toJson();
		final JsonObject ores = provider.getAsJsonObject("ores");
		final JsonObject legacy = new JsonParser().parse(new String(Files.readAllBytes(
				Paths.get("tools/validation/legacy110/fixtures/orespawn3-basemetals.json")),
				StandardCharsets.UTF_8)).getAsJsonObject().getAsJsonObject("spawns");

		assertEquals(5, provider.get("schema_version").getAsInt());
		assertEquals(1, provider.get("provider_revision").getAsInt());
		assertEquals(11, ores.entrySet().size());
		for (final Map.Entry<String, JsonElement> entry : legacy.entrySet()) {
			final JsonObject old = entry.getValue().getAsJsonObject();
			final JsonObject ore = ores.getAsJsonObject("basemetals:legacy/" + entry.getKey());
			final JsonObject parameters = old.getAsJsonObject("parameters");
			assertEquals(old.getAsJsonArray("blocks").get(0).getAsJsonObject().get("name"), ore.get("block"));
			assertFalse(ore.get("native_generation").getAsBoolean());
			assertFalse(ore.get("retrogen").getAsBoolean());

			final String dimension = entry.getKey().equals("starsteel_ore") ? "minecraft:the_end" : "minecraft:the_nether";
			final JsonObject placement = old.getAsJsonArray("dimensions").size() == 0
					? ore.getAsJsonObject("dimension_selectors").getAsJsonObject("orespawn:all_except_nether_end")
					: ore.getAsJsonObject("dimensions").getAsJsonObject(dimension);
			assertEquals(parameters.get("minHeight").getAsInt(), placement.get("min_y").getAsInt());
			assertEquals(parameters.get("maxHeight").getAsInt() - 1, placement.get("max_y").getAsInt());
			assertEquals(parameters.get("frequency").getAsDouble(), placement.get("frequency").getAsDouble());
			assertEquals(4, placement.get("min_quantity").getAsInt());
			assertEquals(11, placement.get("max_quantity").getAsInt());
			assertEquals("orespawn:standard", placement.get("placement_channel").getAsString());
		}
		for (final String empty : new String[] { "rocks", "terrain_dimensions", "fluid_deposits", "biome_palettes" }) {
			assertEquals(0, provider.getAsJsonObject(empty).entrySet().size());
		}
	}

	@Test
	void omitsMaterialsThatThePackDisabled() {
		final JsonObject ores = BaseMetalsWorldgenProvider.build(block -> !block.getResourcePath().equals("copper_ore"))
				.get().toJson().getAsJsonObject("ores");
		assertEquals(10, ores.entrySet().size());
		assertFalse(ores.has("basemetals:legacy/copper_ore"));
		assertFalse(BaseMetalsWorldgenProvider.build(block -> false).isPresent());
	}

	@Test
	void doesNotShipTheOldGenerators() {
		for (final String name : new String[] { "com.mcmoddev.orespawn.BaseMetalsOreSpawn",
				"com.mcmoddev.lib.oregen.FallbackGenerator", "com.mcmoddev.lib.oregen.FallbackGeneratorData" }) {
			assertThrows(ClassNotFoundException.class, () -> Class.forName(name, false, getClass().getClassLoader()));
		}
	}
}
