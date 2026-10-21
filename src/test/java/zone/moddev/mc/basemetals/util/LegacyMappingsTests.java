package zone.moddev.mc.basemetals.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class LegacyMappingsTests {

	@Test
	void originalDoorItemsKeepTheirMaterial() {
		for (final String material : new String[] { "adamantine", "aquarium", "brass", "bronze",
				"coldiron", "copper", "cupronickel", "electrum", "invar", "lead", "mithril",
				"nickel", "platinum", "silver", "starsteel", "steel", "tin" }) {
			assertEquals(material + "_door", LegacyMappings.itemReplacement(material + "_door_item"));
		}
	}

	@Test
	void originalProcessingItemsUseTheirCurrentNames() {
		assertEquals("coal_powder", LegacyMappings.itemReplacement("carbon_powder"));
		assertEquals("mercury", LegacyMappings.itemReplacement("liquid_mercury"));
	}

	@Test
	void unchangedAndUnknownItemsAreNotRemapped() {
		assertNull(LegacyMappings.itemReplacement("iron_nugget"));
		assertNull(LegacyMappings.itemReplacement("human_detector"));
		assertNull(LegacyMappings.itemReplacement("copper_door"));
		assertNull(LegacyMappings.itemReplacement("unknown_door_item"));
	}

	@Test
	void historicalNamesCannotBeChangedAtRuntime() {
		assertEquals(19, LegacyMappings.itemReplacements().size());
		assertThrows(UnsupportedOperationException.class,
				() -> LegacyMappings.itemReplacements().put("iron_nugget", "wrong_item"));
	}
}
