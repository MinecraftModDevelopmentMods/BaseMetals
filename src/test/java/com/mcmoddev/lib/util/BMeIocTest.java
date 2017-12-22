package com.mcmoddev.lib.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mcmoddev.lib.init.MMDCreativeTab;
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
		MMDCreativeTab tab = mock(MMDCreativeTab.class);
		
		when(tab.getTabLabel()).thenReturn("blocks tab");
		when(tabProvider.getTabByName("blocks")).thenReturn(tab);
		
		IoC.register(ITabProvider.class, tabProvider);
		
		ITabProvider tabProviderResolved = IoC.resolve(ITabProvider.class);
		MMDCreativeTab tabResolved = tabProviderResolved.getTabByName("blocks");
		
		assertNotNull(tabResolved);
		assertEquals(tabResolved.getTabLabel(), "blocks tab");
	}
	
	@Test
	void testIoCCanRegisterAndNotResolve() {
		ITabProvider tabProvider = mock(ITabProvider.class);
		MMDCreativeTab tab = mock(MMDCreativeTab.class);
		
		when(tab.getTabLabel()).thenReturn("blocks tab");
		when(tabProvider.getTabByName("blocks")).thenReturn(tab);
		
		IoC.register(ITabProvider.class, tabProvider);
		
		ITabProvider tabProviderResolved = IoC.resolve(ITabProvider.class);
		MMDCreativeTab tabResolved = tabProviderResolved.getTabByName("blocks2");
		
		assertNull(tabResolved);
	}
}
