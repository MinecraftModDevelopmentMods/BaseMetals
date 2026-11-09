package zone.moddev.mc.basemetals.content;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.util.ResourceLocation;
import org.junit.jupiter.api.Test;

class ContentPolicyTest {
	private final ContentPolicy low = ContentPolicy.forMode(ContentMode.LOW_FANTASY);

	@Test
	void olderConfigsAndInvalidValuesKeepTheFullRecipeSet() {
		assertEquals(ContentMode.HIGH_FANTASY, ContentMode.fromSerializedName(null));
		assertEquals(ContentMode.HIGH_FANTASY, ContentMode.fromSerializedName("invalid"));
		assertEquals(ContentMode.LOW_FANTASY, ContentMode.fromSerializedName(" Low_Fantasy "));

		final ContentPolicy high = ContentPolicy.forMode(null);
		for (final MaterialForm form : MaterialForm.values()) {
			assertTrue(high.allows("adamantine", form));
			assertTrue(high.allows("mercury", form));
		}
	}

	@Test
	void lowFantasyKeepsMaterialsButLimitsTheFormsTheyCanMake() {
		assertTrue(low.allows(id("adamantine_ore")));
		assertTrue(low.allows(id("adamantine_pickaxe")));
		assertTrue(low.allows(id("adamantine_arrow")));
		assertFalse(low.allows(id("adamantine_bow")));
		assertFalse(low.allows(id("steel_crossbow")));
		assertFalse(low.allows(id("steel_fishing_rod")));
		assertFalse(low.allows(id("pewter_chestplate")));
		assertFalse(low.allows(id("pewter_horse_armor")));
		assertTrue(low.allows(id("double_pewter_slab")));
		assertTrue(low.allows(id("silver_sword")));
		assertFalse(low.allows(id("silver_pickaxe")));
		assertTrue(low.allows(id("diamond_crackhammer")));
		assertFalse(low.allows(id("copper_anvil")));
		assertTrue(low.allows(id("steel_anvil")));
		assertTrue(low.allows(id("mercury")));
		assertTrue(low.allows(id("mercury_ingot")));
		assertTrue(low.allows(id("mercury_powder")));
		assertFalse(low.allows(id("mercury_block")));
		assertFalse(low.allows(id("mercury_powder_dirty")));
	}

	@Test
	void vanillaAndAddOnsKeepTheirOwnRules() {
		assertTrue(low.allows(new ResourceLocation("minecraft:bow")));
		assertTrue(low.allows(new ResourceLocation("basegems:diamond_bow")));
		assertTrue(low.allows(new ResourceLocation("modernmetals:steel_crossbow")));
		assertTrue(low.allows(id("human_detector")));
	}

	private static ResourceLocation id(final String name) {
		return new ResourceLocation("basemetals", name);
	}
}
