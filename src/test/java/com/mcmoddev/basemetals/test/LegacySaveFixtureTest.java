package com.mcmoddev.basemetals.test;

import java.nio.file.Path;
import java.util.Collections;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LegacySaveFixtureTest {

	@TempDir
	Path directory;

	@Test
	void refusesDirectoriesNotCreatedByTheProbe() {
		assertThrows(AssertionError.class,
				() -> LegacySaveFixture.main(new String[] { "prepare", directory.toString() }));
		assertEquals(0, directory.toFile().list().length);
	}

	@Test
	void renamesRegistryKeysWithoutChangingNumericIdsOrOtherEntries() {
		NBTTagList entries = new NBTTagList();
		NBTTagCompound mercury = entry("basemetals:mercury", 218);
		NBTTagCompound foreign = entry("othermod:mercury", 500);
		entries.appendTag(mercury);
		entries.appendTag(foreign);

		LegacySaveFixture.renameRegistry(entries,
				Collections.singletonMap("basemetals:liquid_mercury", "basemetals:mercury"));

		assertEquals("basemetals:liquid_mercury", mercury.getString("K"));
		assertEquals(218, mercury.getInteger("V"));
		assertEquals("othermod:mercury", foreign.getString("K"));
		assertEquals(500, foreign.getInteger("V"));
	}

	@Test
	void rejectsMissingOrAmbiguousRegistryEntries() {
		NBTTagList entries = new NBTTagList();
		assertThrows(AssertionError.class, () -> LegacySaveFixture.renameRegistry(entries,
				Collections.singletonMap("old:name", "new:name")));

		entries.appendTag(entry("new:name", 1));
		entries.appendTag(entry("new:name", 2));
		assertThrows(AssertionError.class, () -> LegacySaveFixture.renameRegistry(entries,
				Collections.singletonMap("old:name", "new:name")));
	}

	private static NBTTagCompound entry(final String key, final int value) {
		NBTTagCompound entry = new NBTTagCompound();
		entry.setString("K", key);
		entry.setInteger("V", value);
		return entry;
	}
}
