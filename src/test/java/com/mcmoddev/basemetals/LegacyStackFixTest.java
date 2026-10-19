package com.mcmoddev.basemetals;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LegacyStackFixTest {
	@Test
	void changesOnlyTheIdentityAndIsIdempotent() {
		NBTTagCompound item = stack("basemetals:carbon_powder", 64);
		NBTTagCompound tag = new NBTTagCompound();
		tag.setLong("serial", 9876543210L);
		tag.setString("custom_name", "Keep this data");
		item.setTag("tag", tag);
		item.setByte("Slot", (byte) 9);
		NBTTagCompound expected = item.copy();
		expected.setString("id", "basemetals:coal_powder");
		LegacyStackFix fix = new LegacyStackFix(name -> true);
		assertSame(item, fix.fixTagCompound(item));
		assertEquals(expected, item);
		fix.fixTagCompound(item);
		assertEquals(expected, item);
	}

	@Test
	void visitsNestedInventoriesWithoutChangingTheirContainerFormat() {
		NBTTagCompound chunk = new NBTTagCompound();
		NBTTagCompound machine = new NBTTagCompound();
		machine.setString("id", "example:machine");
		NBTTagList inventory = new NBTTagList();
		inventory.appendTag(stack("basemetals:bronze_door_item", 5));
		machine.setTag("customInventory", inventory);
		NBTTagList tiles = new NBTTagList();
		tiles.appendTag(machine);
		chunk.setTag("TileEntities", tiles);
		new LegacyStackFix(name -> true).fixTagCompound(chunk);
		assertEquals("example:machine", machine.getString("id"));
		assertEquals("basemetals:bronze_door", inventory.getCompoundTagAt(0).getString("id"));
		assertEquals(5, inventory.getCompoundTagAt(0).getByte("Count"));
		assertTrue(machine.hasKey("customInventory"));
	}

	@Test
	void leavesUnrelatedDataAndUnavailableTargetsAlone() {
		NBTTagCompound unavailable = stack("basemetals:carbon_powder", 12);
		NBTTagCompound expected = unavailable.copy();
		new LegacyStackFix(name -> false).fixTagCompound(unavailable);
		assertEquals(expected, unavailable);
		NBTTagCompound label = new NBTTagCompound();
		label.setString("id", "basemetals:carbon_powder");
		new LegacyStackFix(name -> true).fixTagCompound(label);
		assertEquals("basemetals:carbon_powder", label.getString("id"));
		NBTTagCompound foreign = stack("othermod:carbon_powder", 12);
		new LegacyStackFix(name -> true).fixTagCompound(foreign);
		assertEquals("othermod:carbon_powder", foreign.getString("id"));
	}

	private static NBTTagCompound stack(final String id, final int count) {
		NBTTagCompound stack = new NBTTagCompound();
		stack.setString("id", id);
		stack.setByte("Count", (byte) count);
		stack.setShort("Damage", (short) 3);
		return stack;
	}
}
