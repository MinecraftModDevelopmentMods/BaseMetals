package com.mcmoddev.basemetals;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LegacyMappingsTest {

	@Test
	void classRegisteredHandlersMustBeStatic() throws Exception {
		for (String name : new String[] { "onRemapBlock", "onRemapItem" }) {
			Method method = BaseMetals.class.getMethod(name, RegistryEvent.MissingMappings.class);
			assertTrue(Modifier.isStatic(method.getModifiers()), name + " is registered through a class");
			assertTrue(method.isAnnotationPresent(SubscribeEvent.class));
		}
	}

	@Test
	void recoversTheRenamedMaterialForms() {
		assertEquals("basemetals:mercury", LegacyMappings.blockTarget("basemetals:liquid_mercury"));
		assertEquals("basemetals:mercury", LegacyMappings.itemTarget("basemetals:liquid_mercury"));
		assertEquals("basemetals:coal_powder", LegacyMappings.itemTarget("basemetals:carbon_powder"));
		assertEquals("minecraft:iron_nugget", LegacyMappings.itemTarget("basemetals:iron_nugget"));
	}

	@Test
	void coversEveryHistoricalMetalDoorItem() {
		for (String material : new String[] { "adamantine", "aquarium", "brass", "bronze",
				"coldiron", "copper", "cupronickel", "electrum", "invar", "lead", "mithril",
				"nickel", "platinum", "silver", "starsteel", "steel", "tin" }) {
			assertEquals("basemetals:" + material + "_door",
					LegacyMappings.itemTarget("basemetals:" + material + "_door_item"));
		}
	}

	@Test
	void leavesCurrentAndForeignNamesAlone() {
		for (String name : new String[] { "othermod:carbon_powder", "othermod:steel_door_item",
				"basemetals:steel_door", "basemetals:unknown_door_item", "minecraft:iron_nugget" }) {
			assertNull(LegacyMappings.itemTarget(name));
			assertNull(LegacyMappings.blockTarget(name));
		}
		assertNull(LegacyMappings.blockTarget("basemetals:mercury"));
	}
}
