package com.mcmoddev.lib.util;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.io.IOCase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BMeIocTest {

	static BMeIoC IoC;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		IoC = BMeIoC.getInstance();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testIoCIsInstansiated() {
		assertNotNull(IoC);
	}

}
