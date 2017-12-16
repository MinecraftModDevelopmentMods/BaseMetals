package com.mcmoddev.lib.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mcmoddev.lib.interfaces.ITabProvider;

class BMeIocTest {

	static BMeIoC IoC;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		IoC = BMeIoC.getInstance(false); // cannot yet test autowirup
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

	@Test
	void testIoCCanRegisterAndResolve() {
		ITabProvider tabProvider = mock(ITabProvider.class);
		
		when(tabProvider.getTab("Axes", "mmdlib")).thenReturn("axesTab");
		
		IoC.register(ITabProvider.class, tabProvider);
		
		ITabProvider tabProviderResolved = IoC.resolve(ITabProvider.class);
		String tabResolved = tabProviderResolved.getTab("Axes", "mmdlib");
		
		assertNotNull(tabResolved);
		assertEquals(tabResolved, "axesTab");
	}
	
	@Test
	void testIoCCanRegisterAndNotResolve() {
		ITabProvider tabProvider = mock(ITabProvider.class);
		
		when(tabProvider.getTab("Axes", "mmdlib")).thenReturn("axesTab");
		
		IoC.register(ITabProvider.class, tabProvider);
		
		ITabProvider tabProviderResolved = IoC.resolve(ITabProvider.class);
		String tabResolved = tabProviderResolved.getTab("Blocks", "mmdlib");
		
		assertNull(tabResolved);
	}
}
