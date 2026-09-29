package com.mcmoddev.basemetals.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LootPolicyContractTest {
	@Test
	void everyBaseMetalsAuxiliaryLootEntryUsesTheContentPolicyCondition() throws Exception {
		final Path root = Paths.get("src", "main", "resources", "assets", "basemetals", "alt", "chests");
		final AtomicInteger checked = new AtomicInteger();
		try (java.util.stream.Stream<Path> files = Files.list(root)) {
			for (final Path file : (Iterable<Path>) files.filter(path -> path.toString().endsWith(".json"))::iterator) {
				try (Reader reader = Files.newBufferedReader(file)) {
					visit(new JsonParser().parse(reader), file, checked);
				}
			}
		}
		assertTrue(checked.get() > 500, "Expected the complete legacy auxiliary-loot inventory");
	}

	private static void visit(final JsonElement element, final Path file,
			final AtomicInteger checked) {
		if (element.isJsonArray()) {
			for (final JsonElement child : element.getAsJsonArray()) {
				visit(child, file, checked);
			}
			return;
		}
		if (!element.isJsonObject()) {
			return;
		}
		final JsonObject object = element.getAsJsonObject();
		if (object.has("type") && "item".equals(object.get("type").getAsString())
				&& object.has("name")
				&& object.get("name").getAsString().startsWith("basemetals:")) {
			final JsonArray conditions = object.getAsJsonArray("conditions");
			assertTrue(conditions != null, file + " " + object.get("name"));
			int matches = 0;
			for (final JsonElement condition : conditions) {
				final JsonObject value = condition.getAsJsonObject();
				if (value.has("condition")
						&& "basemetals:content_policy".equals(value.get("condition").getAsString())) {
					matches++;
					assertEquals(object.get("name").getAsString(), value.get("item").getAsString());
				}
			}
			assertEquals(1, matches, file + " " + object.get("name"));
			checked.incrementAndGet();
		}
		for (final java.util.Map.Entry<String, JsonElement> child : object.entrySet()) {
			visit(child.getValue(), file, checked);
		}
	}
}
