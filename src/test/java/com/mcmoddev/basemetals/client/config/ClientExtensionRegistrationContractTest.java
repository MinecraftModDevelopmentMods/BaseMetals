package com.mcmoddev.basemetals.client.config;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClientExtensionRegistrationContractTest {
	@Test
	void registersExactlyOneConventionalBaseMetalsExtensionFromTheClientProxy() throws Exception {
		final Path clientProxy = Paths.get("src", "main", "java", "com", "mcmoddev",
				"basemetals", "proxy", "ClientProxy.java");
		final String source = new String(Files.readAllBytes(clientProxy), StandardCharsets.UTF_8);
		assertEquals(1, occurrences(source,
				"WorldSettingsExtensionRegistry.registerConfigScreen"));
		assertTrue(source.contains("BaseMetals.MODID"));
		assertTrue(source.contains("BaseMetalsConfigScreen::new"));

		final Path commonProxy = clientProxy.resolveSibling("CommonProxy.java");
		final String commonSource = new String(Files.readAllBytes(commonProxy), StandardCharsets.UTF_8);
		assertFalse(commonSource.contains("WorldSettingsExtensionRegistry"));
		assertFalse(commonSource.contains("BaseMetalsConfigScreen"));
	}

	private static int occurrences(final String source, final String value) {
		int count = 0;
		for (int index = 0; (index = source.indexOf(value, index)) >= 0;
				index += value.length()) {
			count++;
		}
		return count;
	}
}
