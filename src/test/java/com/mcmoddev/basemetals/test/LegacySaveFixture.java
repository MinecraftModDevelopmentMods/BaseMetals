package com.mcmoddev.basemetals.test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.storage.RegionFile;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/** A tiny Anvil save with old registry names and a chest outside the spawn area. */
public final class LegacySaveFixture {

	private static final String SENTINEL = "Base Metals disposable legacy-save fixture\n";
	static final int LATER_CHUNK = 64;
	static final Map<String, String> ITEMS = itemNames();

	private LegacySaveFixture() {
	}

	private static Map<String, String> itemNames() {
		Map<String, String> names = new LinkedHashMap<>();
		names.put("basemetals:liquid_mercury", "basemetals:mercury");
		names.put("basemetals:carbon_powder", "basemetals:coal_powder");
		names.put("basemetals:iron_nugget", "minecraft:iron_nugget");

		for (String material : new String[] { "adamantine", "aquarium", "brass", "bronze", "coldiron",
				"copper", "cupronickel", "electrum", "invar", "lead", "mithril", "nickel", "platinum",
				"silver", "starsteel", "steel", "tin" }) {
			names.put("basemetals:" + material + "_door_item", "basemetals:" + material + "_door");
		}

		return names;
	}

	static NBTTagCompound stack(final String id, final int slot) {
		NBTTagCompound item = new NBTTagCompound();
		item.setString("id", id);
		item.setByte("Slot", (byte) slot);
		item.setByte("Count", (byte) 49);
		item.setShort("Damage", (short) 3);

		NBTTagCompound data = new NBTTagCompound();
		data.setLong("serial", 1234567890123L + slot);
		data.setString("legacy_note", "Keep this item data");
		NBTTagCompound display = new NBTTagCompound();
		display.setString("Name", "Legacy material " + slot);
		data.setTag("display", display);
		NBTTagCompound enchantment = new NBTTagCompound();
		enchantment.setShort("id", (short) 34);
		enchantment.setShort("lvl", (short) 3);
		NBTTagList enchantments = new NBTTagList();
		enchantments.appendTag(enchantment);
		data.setTag("ench", enchantments);
		item.setTag("tag", data);

		return item;
	}

	static void seed(final WorldServer world, final File runDirectory) throws Exception {
		world.setSpawnPoint(new BlockPos(0, 81, 0));

		for (int chunk : new int[] { 0, LATER_CHUNK }) {
			BlockPos chestPosition = chestPosition(chunk);
			world.getChunk(chunk, chunk);
			world.setBlockState(chestPosition, Blocks.CHEST.getDefaultState(), 2);
			TileEntityChest chest = (TileEntityChest) world.getTileEntity(chestPosition);
			require(chest != null, "Fixture chest was not created");
			int slot = 0;

			for (String current : ITEMS.values()) {
				Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(current));
				require(item != null, "Fixture item is not registered: " + current);
				chest.setInventorySlotContents(slot, new ItemStack(stack(current, slot)));
				slot++;
			}

			chest.setInventorySlotContents(slot, new ItemStack(stack("minecraft:diamond", slot)));
			chest.markDirty();

			// Enclose the source block so fluid ticks cannot change the fixture.
			BlockPos mercuryPosition = chestPosition.add(3, 0, 3);
			for (EnumFacing side : EnumFacing.values()) {
				world.setBlockState(mercuryPosition.offset(side), Blocks.STONE.getDefaultState(), 2);
			}

			Block mercury = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("basemetals:mercury"));
			require(mercury != null, "Mercury block is not registered");
			world.setBlockState(mercuryPosition, mercury.getDefaultState(), 2);
		}

		Files.write(new File(runDirectory, "legacy-fixture.txt").toPath(), SENTINEL.getBytes(StandardCharsets.UTF_8));
	}

	static void verifyLoaded(final WorldServer world, final int chunk) {
		world.getChunk(chunk, chunk);
		BlockPos position = chestPosition(chunk);
		require(world.getTileEntity(position) instanceof TileEntityChest, "Missing chest in chunk " + chunk);
		TileEntityChest chest = (TileEntityChest) world.getTileEntity(position);
		int slot = 0;

		for (String current : ITEMS.values()) {
			verifyStack(chest.getStackInSlot(slot).writeToNBT(new NBTTagCompound()), current, slot);
			slot++;
		}

		verifyStack(chest.getStackInSlot(slot).writeToNBT(new NBTTagCompound()), "minecraft:diamond", slot);
		require(new ResourceLocation("basemetals:mercury").equals(
				world.getBlockState(position.add(3, 0, 3)).getBlock().getRegistryName()),
				"Placed Mercury changed in chunk " + chunk);

		// Loading alone need not save a chunk. Force a write to test the next restart.
		world.getChunk(chunk, chunk).markDirty();
	}

	private static BlockPos chestPosition(final int chunk) {
		return new BlockPos(chunk * 16, 80, chunk * 16);
	}

	private static void verifyStack(final NBTTagCompound actual, final String id, final int slot) {
		NBTTagCompound expected = stack(id, slot);
		require(id.equals(actual.getString("id")), "Wrong item in slot " + slot + ": " + actual);
		require(actual.getByte("Count") == 49, "Item count changed in slot " + slot);
		require(actual.getShort("Damage") == 3, "Item damage changed in slot " + slot);
		require(expected.getCompoundTag("tag").equals(actual.getCompoundTag("tag")),
				"Custom name, enchantment or other NBT changed in slot " + slot);
	}

	/** Runs between server boots, when no region files are open. */
	public static void main(final String[] arguments) throws Exception {
		require(arguments.length == 2, "Expected an operation and fixture directory");
		File run = new File(arguments[1]).getCanonicalFile();
		File sentinel = new File(run, "legacy-fixture.txt");
		require(sentinel.isFile() && SENTINEL.equals(new String(Files.readAllBytes(sentinel.toPath()),
				StandardCharsets.UTF_8)), "Refusing to edit a save not created by the fixture probe");

		if ("prepare".equals(arguments[0])) {
			prepare(run);
		} else {
			verifySaved(run, arguments[0]);
		}
	}

	private static void prepare(final File run) throws Exception {
		File levelFile = new File(run, "world/level.dat");
		NBTTagCompound level;
		try (FileInputStream input = new FileInputStream(levelFile)) {
			level = CompressedStreamTools.readCompressed(input);
		}

		NBTTagCompound registries = level.getCompoundTag("FML").getCompoundTag("Registries");
		renameRegistry(registries.getCompoundTag("minecraft:items").getTagList("ids", 10), ITEMS);
		Map<String, String> blocks = new LinkedHashMap<>();
		blocks.put("basemetals:liquid_mercury", "basemetals:mercury");
		renameRegistry(registries.getCompoundTag("minecraft:blocks").getTagList("ids", 10), blocks);
		makeLegacy(level.getCompoundTag("Data"));

		try (FileOutputStream output = new FileOutputStream(levelFile)) {
			CompressedStreamTools.writeCompressed(level, output);
		}

		for (int chunk : new int[] { 0, LATER_CHUNK }) {
			NBTTagCompound data = readChunk(run, chunk);
			NBTTagList items = chestItems(data, chunk);
			require(items.tagCount() == 21, "Incomplete seed chest");
			int slot = 0;

			for (Map.Entry<String, String> name : ITEMS.entrySet()) {
				NBTTagCompound item = items.getCompoundTagAt(slot);
				verifyStack(item, name.getValue(), slot);
				item.setString("id", name.getKey());
				slot++;
			}

			makeLegacy(data);
			writeChunk(run, chunk, data);
		}

		Properties baseline = new Properties();
		baseline.setProperty("deferredRegionSha256", hash(regionFile(run, LATER_CHUNK)));
		try (FileOutputStream output = new FileOutputStream(new File(run, "legacy-baseline.properties"))) {
			baseline.store(output, "Unopened legacy chunk");
		}

		System.out.println("BASE_METALS_LEGACY_FIXTURE_READY aliases=20 blocks=1 chunks=2");
	}

	static void renameRegistry(final NBTTagList entries, final Map<String, String> names) {
		for (Map.Entry<String, String> name : names.entrySet()) {
			int matches = 0;
			for (int i = 0; i < entries.tagCount(); i++) {
				NBTTagCompound entry = entries.getCompoundTagAt(i);
				if (name.getValue().equals(entry.getString("K"))) {
					entry.setString("K", name.getKey());
					matches++;
				}
			}

			require(matches == 1, "Expected one registry entry for " + name.getValue() + ", found " + matches);
		}
	}

	private static void makeLegacy(final NBTTagCompound root) {
		root.setInteger("DataVersion", 512);
		root.getCompoundTag("ForgeDataVersion").removeTag("basemetals");
		root.getCompoundTag("ForgeDataVersion").setInteger("minecraft", 512);
	}

	private static void verifySaved(final File run, final String phase) throws Exception {
		require("first".equals(phase) || "reload".equals(phase) || "delayed".equals(phase)
				|| "stable".equals(phase), "Unknown fixture phase: " + phase);
		verifyCanonicalChunk(run, 0);
		boolean deferred = "first".equals(phase) || "reload".equals(phase);

		if (deferred) {
			Properties baseline = new Properties();
			try (FileInputStream input = new FileInputStream(new File(run, "legacy-baseline.properties"))) {
				baseline.load(input);
			}

			require(hash(regionFile(run, LATER_CHUNK)).equals(baseline.getProperty("deferredRegionSha256")),
					"An unopened region was changed during " + phase);
			NBTTagCompound data = readChunk(run, LATER_CHUNK);
			require(!data.getCompoundTag("ForgeDataVersion").hasKey("basemetals"),
					"Unopened chunk was marked migrated");
			NBTTagList items = chestItems(data, LATER_CHUNK);
			int slot = 0;
			for (String legacy : ITEMS.keySet()) {
				verifyStack(items.getCompoundTagAt(slot), legacy, slot++);
			}
		} else {
			verifyCanonicalChunk(run, LATER_CHUNK);
		}

		Files.write(new File(run, phase + "-save.properties").toPath(),
				("phase=" + phase + "\npassed=true\ndeferredUnchanged=" + deferred + "\n")
						.getBytes(StandardCharsets.UTF_8));
		System.out.println("BASE_METALS_LEGACY_SAVE_PASS phase=" + phase + " deferredUnchanged=" + deferred);
	}

	private static void verifyCanonicalChunk(final File run, final int chunk) throws Exception {
		NBTTagCompound data = readChunk(run, chunk);
		require(data.getCompoundTag("ForgeDataVersion").getInteger("basemetals") == 1,
				"Chunk " + chunk + " has no Base Metals migration stamp");
		NBTTagList items = chestItems(data, chunk);
		require(items.tagCount() == 21, "Saved chest lost items in chunk " + chunk);
		int slot = 0;

		for (String current : ITEMS.values()) {
			verifyStack(items.getCompoundTagAt(slot), current, slot++);
		}

		verifyStack(items.getCompoundTagAt(slot), "minecraft:diamond", slot);
	}

	private static NBTTagList chestItems(final NBTTagCompound data, final int chunk) {
		NBTTagList tiles = data.getCompoundTag("Level").getTagList("TileEntities", 10);
		BlockPos position = chestPosition(chunk);
		for (int i = 0; i < tiles.tagCount(); i++) {
			NBTTagCompound tile = tiles.getCompoundTagAt(i);
			if (tile.getInteger("x") == position.getX() && tile.getInteger("y") == position.getY()
					&& tile.getInteger("z") == position.getZ()) {
				return tile.getTagList("Items", 10);
			}
		}

		throw new AssertionError("No saved chest in chunk " + chunk);
	}

	private static File regionFile(final File run, final int chunk) {
		return new File(run, "world/region/r." + (chunk >> 5) + "." + (chunk >> 5) + ".mca");
	}

	private static NBTTagCompound readChunk(final File run, final int chunk) throws Exception {
		File path = regionFile(run, chunk);
		require(path.isFile(), "Missing fixture region: " + path);
		RegionFile region = new RegionFile(path);
		try (DataInputStream input = region.getChunkDataInputStream(chunk & 31, chunk & 31)) {
			require(input != null, "Missing fixture chunk: " + chunk);
			return CompressedStreamTools.read(input);
		} finally {
			region.close();
		}
	}

	private static void writeChunk(final File run, final int chunk, final NBTTagCompound data) throws Exception {
		RegionFile region = new RegionFile(regionFile(run, chunk));
		try (DataOutputStream output = region.getChunkDataOutputStream(chunk & 31, chunk & 31)) {
			CompressedStreamTools.write(data, output);
		} finally {
			region.close();
		}
	}

	private static String hash(final File file) throws Exception {
		byte[] digest = MessageDigest.getInstance("SHA-256").digest(Files.readAllBytes(file.toPath()));
		StringBuilder result = new StringBuilder();
		for (byte value : digest) {
			result.append(String.format("%02x", value & 255));
		}
		return result.toString();
	}

	static void require(final boolean condition, final String message) {
		if (!condition) {
			throw new AssertionError(message);
		}
	}
}
