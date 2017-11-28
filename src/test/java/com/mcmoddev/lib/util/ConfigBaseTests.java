package com.mcmoddev.lib.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.util.ConfigBase.Options;

class ConfigBaseTests {

	private static String MODID;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		MODID = BaseMetals.MODID;
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
		String name = "TestMod";
		
		assertEquals(false, Options.isModEnabled(name));
	}
	
	@Test
	void testIsModEnabledDetectsModIsEnabled() {
		String name = "TestMod";
		
		Options.modEnabled(name, true);
		
		
		assertEquals(true, Options.isModEnabled(name));
	}
	
	@Test
	void testIsModEnabledDetectsModIsDisabled() {
		String name = "TestMod";
		
		Options.modEnabled(name, false);
		
		
		assertEquals(false, Options.isModEnabled(name));
	}
}
