package com.mcmoddev.lib.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mcmoddev.lib.interfaces.ITabProvider;

import net.minecraft.util.ResourceLocation;

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
	
	@Test
	void testIoCCanRegisterAndResolveResourceLocation() {
		ITabProvider tabProvider = mock(ITabProvider.class);
		
		when(tabProvider.getTab("Axes", "mmdlib")).thenReturn("axesTab");
		
		ResourceLocation resourceLocation = new ResourceLocation("MyDomain", "MyPath");
		
		IoC.register(ITabProvider.class, tabProvider, resourceLocation);
		
		ITabProvider tabProviderResolved = IoC.resolve(ITabProvider.class, resourceLocation);
		String tabResolved = tabProviderResolved.getTab("Axes", "mmdlib");
		
		assertNotNull(tabResolved);
		assertEquals(tabResolved, "axesTab");
	}
	
	@Test
	void testIoCCanRegisterAndNotResolveResourceLocation() {
		ITabProvider tabProvider = mock(ITabProvider.class);
		
		when(tabProvider.getTab("Axes", "mmdlib")).thenReturn("axesTab");
		
		ResourceLocation resourceLocation = new ResourceLocation("MyDomain", "MyPath");
		
		IoC.register(ITabProvider.class, tabProvider, resourceLocation);
		
		ResourceLocation resourceLocationDifferent = new ResourceLocation("MyDomain", "MyPathIsDifferent");
		
		ITabProvider tabProviderResolved = IoC.resolve(ITabProvider.class, resourceLocationDifferent);
		
		assertNull(tabProviderResolved);
	}
	
	@Test
	void testIoCCanRegisterAndResolveMMDResourceLocation() {
		ITabProvider tabProvider = mock(ITabProvider.class);
		
		when(tabProvider.getTab("Axes", "mmdlib")).thenReturn("axesTab");
		
		ResourceLocation resourceLocation = new MMDResourceLocation("MyNamespace", "MyEntryType", "MyEntryName");
		
		IoC.register(ITabProvider.class, tabProvider, resourceLocation);
		
		ITabProvider tabProviderResolved = IoC.resolve(ITabProvider.class, resourceLocation);
		String tabResolved = tabProviderResolved.getTab("Axes", "mmdlib");
		
		assertNotNull(tabResolved);
		assertEquals(tabResolved, "axesTab");
	}
	
	@Test
	void testIoCCanRegisterAndNotResolveMMDResourceLocation() {
		ITabProvider tabProvider = mock(ITabProvider.class);
		
		when(tabProvider.getTab("Axes", "mmdlib")).thenReturn("axesTab");
		
		ResourceLocation resourceLocation = new MMDResourceLocation("MyNamespace", "MyEntryType", "MyEntryName");
		
		IoC.register(ITabProvider.class, tabProvider, resourceLocation);
		
		ResourceLocation resourceLocationDifferent = new MMDResourceLocation("MyNamespace", "MyEntryTypeIsDifferent", "MyEntryName");
		
		ITabProvider tabProviderResolved = IoC.resolve(ITabProvider.class, resourceLocationDifferent);
		
		assertNull(tabProviderResolved);
	}
	
	@Test
	void testIoCCanRegisterAndResolveString() {
		ITabProvider tabProvider = mock(ITabProvider.class);
		
		when(tabProvider.getTab("Axes", "mmdlib")).thenReturn("axesTab");
				
		IoC.register(ITabProvider.class, tabProvider, "myConcrete", "myMod");
		
		ITabProvider tabProviderResolved = IoC.resolve(ITabProvider.class, "myConcrete", "myMod");
		String tabResolved = tabProviderResolved.getTab("Axes", "mmdlib");
		
		assertNotNull(tabResolved);
		assertEquals(tabResolved, "axesTab");
	}
	
	@Test
	void testIoCCanRegisterAndNotResolveString() {
		ITabProvider tabProvider = mock(ITabProvider.class);
		
		when(tabProvider.getTab("Axes", "mmdlib")).thenReturn("axesTab");
		
		IoC.register(ITabProvider.class, tabProvider, "myConcrete", "myMod");
		
		ITabProvider tabProviderResolved = IoC.resolve(ITabProvider.class, "myConcreteIsDifferent", "myMod");
		
		assertNull(tabProviderResolved);
	}
	
	@Test
	void testIoCCanRegisterAndNotResolveWrongModString() {
		ITabProvider tabProvider = mock(ITabProvider.class);
		
		when(tabProvider.getTab("Axes", "mmdlib")).thenReturn("axesTab");
		
		IoC.register(ITabProvider.class, tabProvider, "myConcrete", "myMod");
		
		ITabProvider tabProviderResolved = IoC.resolve(ITabProvider.class, "myConcrete", "myModIsDifferent");
		
		assertNull(tabProviderResolved);
	}
}
