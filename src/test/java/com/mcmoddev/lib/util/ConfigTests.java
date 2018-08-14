package com.mcmoddev.lib.util;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mcmoddev.lib.util.Config.Options;

class ConfigTests {

	private static final String name = "TestMod";

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		// MODID = BaseMetals.MODID
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
		Options.clearOptions();
	}

	@Test
	void testIsModEnabledDetectsModIsNotPresent() {
		Assertions.assertEquals(false, Options.isModEnabled(name));
	}

	@Test
	void testIsModEnabledDetectsModIsEnabled() {
		Options.modEnabled(name, true);

		Assertions.assertEquals(true, Options.isModEnabled(name));
	}

	@Test
	void testIsModEnabledDetectsModIsDisabled() {
		Options.modEnabled(name, false);

		Assertions.assertEquals(false, Options.isModEnabled(name));
	}
}
