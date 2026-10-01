package com.mcmoddev.basemetals.util;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.FMLInjectionData;

import java.io.File;
import java.lang.reflect.Field;

/** Test-only factory for a disposable Base Metals configuration. */
public final class TestConfigFactory {
	private TestConfigFactory() {
	}

	public static Configuration create(final File file) {
		prepareForgeConfigurationHome(file.getParentFile());

		final Configuration configuration = new Configuration(file);

		BMeConfig.populateConfiguration(configuration);

		return configuration;
	}

	private static void prepareForgeConfigurationHome(final File home) {
		try {
			final Field minecraftHome = FMLInjectionData.class.getDeclaredField("minecraftHome");

			minecraftHome.setAccessible(true);
			minecraftHome.set(null, home);
		} catch (ReflectiveOperationException failure) {
			throw new IllegalStateException("Could not initialize disposable Forge config home", failure);
		}
	}
}
