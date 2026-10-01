package com.mcmoddev.basemetals.content;

import com.mcmoddev.basemetals.data.MaterialNames;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContentPolicyTest {
	private static final Set<String> ALL_MATERIALS = set(
			"adamantine", "antimony", "aquarium", "bismuth", "brass", "bronze",
			"charcoal", "coal", "coldiron", "copper", "cupronickel", "diamond",
			"electrum", "emerald", "ender", "gold", "invar", "iron", "lapis",
			"lead", "mercury", "mithril", "nickel", "obsidian", "pewter",
			"platinum", "prismarine", "quartz", "redstone", "silver",
			"starsteel", "steel", "stone", "tin", "wood", "zinc");
	private static final Set<String> FULL_TOOLS = set("adamantine", "aquarium", "bronze",
			"coldiron", "copper", "cupronickel", "invar", "mithril", "nickel",
			"starsteel", "steel", "iron");
	private static final Set<String> ARMOR = set("adamantine", "aquarium", "brass",
			"bronze", "coldiron", "copper", "cupronickel", "electrum", "invar",
			"mithril", "nickel", "platinum", "silver", "starsteel", "steel",
			"iron", "gold", "diamond");
	private static final Set<String> AMMO = union(FULL_TOOLS,
			set("silver", "obsidian", "diamond"));
	private static final Set<String> GEARS = set("adamantine", "aquarium", "brass",
			"bronze", "coldiron", "copper", "cupronickel", "invar", "mithril",
			"nickel", "starsteel", "steel", "iron", "gold", "wood");
	private static final Set<MaterialForm> TOOL_FORMS = EnumSet.of(MaterialForm.AXE,
			MaterialForm.CRACKHAMMER, MaterialForm.HOE, MaterialForm.PICKAXE,
			MaterialForm.SCYTHE, MaterialForm.SHEARS, MaterialForm.SHOVEL,
			MaterialForm.SWORD);
	private static final Set<MaterialForm> ARMOR_FORMS = EnumSet.of(MaterialForm.BOOTS,
			MaterialForm.CHESTPLATE, MaterialForm.HELMET, MaterialForm.LEGGINGS,
			MaterialForm.HORSE_ARMOR, MaterialForm.SHIELD);

	@Test
	void highFantasyAllowsEveryMaterialAndForm() {
		final ContentPolicy policy = ContentPolicy.forMode(ContentMode.HIGH_FANTASY);

		for (final String material : ALL_MATERIALS) {
			for (final MaterialForm form : MaterialForm.values()) {
				assertTrue(policy.allows(material, form), material + "/" + form);
			}
		}
	}

	@Test
	void lowFantasyMaterialAndFormDecisionsMatchTheReviewedMatrix() {
		final ContentPolicy policy = ContentPolicy.forMode(ContentMode.LOW_FANTASY);

		for (final String material : ALL_MATERIALS) {
			for (final MaterialForm form : MaterialForm.values()) {
				assertTrue(policy.allows(material, form) == expected(material, form),
						material + "/" + form);
			}
		}
	}

	@Test
	void restrictedModesApplyTheReviewedEquipmentMatrix() {
		final ContentPolicy low = ContentPolicy.forMode(ContentMode.LOW_FANTASY);

		assertFalse(low.allows(MaterialNames.ADAMANTINE, MaterialForm.BOW));
		assertTrue(low.allows(MaterialNames.ADAMANTINE, MaterialForm.PICKAXE));
		assertFalse(low.allows(MaterialNames.PEWTER, MaterialForm.CHESTPLATE));
		assertTrue(low.allows(MaterialNames.BRASS, MaterialForm.CHESTPLATE));
		assertTrue(low.allows(MaterialNames.SILVER, MaterialForm.SWORD));
		assertFalse(low.allows(MaterialNames.SILVER, MaterialForm.PICKAXE));
		assertTrue(low.allows(MaterialNames.OBSIDIAN, MaterialForm.SCYTHE));
		assertFalse(low.allows(MaterialNames.OBSIDIAN, MaterialForm.HOE));
		assertTrue(low.allows(MaterialNames.DIAMOND, MaterialForm.CRACKHAMMER));
		assertTrue(low.allows(MaterialNames.IRON, MaterialForm.SHIELD));
		assertFalse(low.allows(MaterialNames.TIN, MaterialForm.GEAR));
	}

	@Test
	void mercuryRetainsOnlyTheExplicitProcessingForms() {
		final ContentPolicy low = ContentPolicy.forMode(ContentMode.LOW_FANTASY);

		assertTrue(low.allows(MaterialNames.MERCURY, MaterialForm.INGOT));
		assertFalse(low.allows(MaterialNames.MERCURY, MaterialForm.BLOCK));
		assertTrue(low.allows(MaterialNames.MERCURY, MaterialForm.ORE));
		assertTrue(low.allows(MaterialNames.MERCURY, MaterialForm.POWDER));
		assertTrue(low.allows(MaterialNames.MERCURY, MaterialForm.FLUID));
		assertFalse(low.allows(MaterialNames.MERCURY, MaterialForm.SWORD));
	}

	@Test
	void compoundRegistrySuffixesResolveToTheMostSpecificForm() {
		assertTrue(MaterialForm.fromRegistryPath("nickel_dense_plate") == MaterialForm.DENSE_PLATE);
		assertTrue(MaterialForm.fromRegistryPath("nickel_powder_dirty") == MaterialForm.DIRTY_POWDER);
		assertTrue(MaterialForm.fromRegistryPath("nickel_crushed_purified") == MaterialForm.CRUSHED_PURIFIED);
		assertTrue(MaterialForm.fromRegistryPath("nickel_pressure_plate") == MaterialForm.PRESSURE_PLATE);
	}

	private static boolean expected(final String material, final MaterialForm form) {
		if (form == MaterialForm.OTHER) {
			return !"mercury".equals(material);
		}

		if (form == MaterialForm.BOW || form == MaterialForm.CROSSBOW
				|| form == MaterialForm.FISHING_ROD) {
			return false;
		}

		if (TOOL_FORMS.contains(form)) {
			if (FULL_TOOLS.contains(material)) {
				return true;
			}

			if ("silver".equals(material)) {
				return form == MaterialForm.SWORD;
			}

			if ("obsidian".equals(material)) {
				return form == MaterialForm.AXE
						|| form == MaterialForm.SWORD || form == MaterialForm.SCYTHE;
			}

			if ("diamond".equals(material)) {
				return form == MaterialForm.CRACKHAMMER
						|| form == MaterialForm.SCYTHE || form == MaterialForm.SHEARS;
			}

			if ("stone".equals(material)) {
				return form == MaterialForm.CRACKHAMMER
						|| form == MaterialForm.SCYTHE;
			}

			return "wood".equals(material) && form == MaterialForm.CRACKHAMMER;
		}

		if (ARMOR_FORMS.contains(form)) {
			return ARMOR.contains(material);
		}

		if (form == MaterialForm.ARROW || form == MaterialForm.BOLT) {
			return AMMO.contains(material);
		}

		if (form == MaterialForm.GEAR) {
			return GEARS.contains(material);
		}

		if (form == MaterialForm.ANVIL) {
			return "stone".equals(material)
					|| "steel".equals(material) || "adamantine".equals(material);
		}

		if ("mercury".equals(material)) {
			return form == MaterialForm.ORE
					|| form == MaterialForm.POWDER || form == MaterialForm.SMALLPOWDER
					|| form == MaterialForm.FLUID || form == MaterialForm.INGOT;
		}

		return true;
	}

	private static Set<String> set(final String... values) {
		return new HashSet<>(Arrays.asList(values));
	}

	private static Set<String> union(final Set<String> first, final Set<String> second) {
		final Set<String> result = new HashSet<>(first);

		result.addAll(second);

		return result;
	}
}
