package com.mcmoddev.basemetals.client.renderer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.util.ResourceLocation;

import org.junit.jupiter.api.Test;

/** Locks the texture used by Base Metals' in-world bolt renderer. */
final class BoltRendererContractTest {

	private static final ResourceLocation VANILLA_PROJECTILE_TEXTURE = new ResourceLocation("minecraft",
			"textures/entity/projectiles/arrow.png");

	@Test
	void boltRendererUsesAnExistingVanillaProjectileTexture() {
		assertEquals(VANILLA_PROJECTILE_TEXTURE,
				new RenderBaseMetalsBolt(null).getEntityTexture(null));
		assertNotNull(RenderBaseMetalsBolt.class.getClassLoader().getResource(
				"assets/minecraft/textures/entity/projectiles/arrow.png"),
				"Selected projectile texture is absent from the Minecraft client");
	}

	@Test
	void everyBoltItemModelRetainsItsMaterialTexture() throws IOException {
		final Path modelDirectory = Paths.get(
				"src/main/resources/assets/basemetals/models/item");
		final Path textureDirectory = Paths.get(
				"src/main/resources/assets/basemetals/textures/items");
		final List<Path> models;

		try (Stream<Path> files = Files.list(modelDirectory)) {
			models = files.filter(path -> path.getFileName().toString().endsWith("_bolt.json"))
					.sorted().collect(Collectors.toList());
		}

		assertEquals(27, models.size(), "Unexpected Base Metals bolt model count");

		for (final Path modelPath : models) {
			final String fileName = modelPath.getFileName().toString();
			final String itemName = fileName.substring(0, fileName.length() - ".json".length());
			final JsonObject model = new JsonParser().parse(new String(
					Files.readAllBytes(modelPath), StandardCharsets.UTF_8)).getAsJsonObject();

			assertEquals("item/generated", model.get("parent").getAsString(), fileName);
			assertEquals("basemetals:items/" + itemName,
					model.getAsJsonObject("textures").get("layer0").getAsString(), fileName);

			final Path texturePath = textureDirectory.resolve(itemName + ".png");

			assertTrue(Files.isRegularFile(texturePath), "Missing texture for " + fileName);

			final BufferedImage texture = javax.imageio.ImageIO.read(texturePath.toFile());

			assertNotNull(texture, "Unreadable texture for " + fileName);
			assertEquals(16, texture.getWidth(), fileName);
			assertEquals(16, texture.getHeight(), fileName);
		}
	}
}
