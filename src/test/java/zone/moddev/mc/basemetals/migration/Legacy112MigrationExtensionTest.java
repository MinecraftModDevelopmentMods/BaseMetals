package zone.moddev.mc.basemetals.migration;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.Bootstrap;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.CleanupMode;
import org.junit.jupiter.api.io.TempDir;
import zone.moddev.mc.basemetals.api.migration.Legacy112MigrationExtension;

class Legacy112MigrationExtensionTest {
    private static final Map<Integer, String> COMPANION_BLOCKS = Map.of(
            0, "minecraft:air", 200, "example:legacy_block");
    private static final String MARKER = "BASEMETALS_1_12_BLOCK_MIGRATION_COMPLETE.txt";

    @BeforeAll
    static void gameVersion() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
    }

    @Test
    void migratesAThirdPartyItemInAVanillaOnlyChunkBeforeTheDataFixer() throws Exception {
        CompoundTag chunk = chunk(54, 2);
        CompoundTag chest = chunk.getCompound("Level").getList("TileEntities", Tag.TAG_COMPOUND).getCompound(0);
        List<String> calls = new ArrayList<>();
        Legacy112MigrationExtension extension = new Legacy112MigrationExtension() {
            @Override public boolean prepareData(CompoundTag tag, int version, Payload payload) {
                assertEquals(1343, version);
                assertEquals(Payload.CHUNK, payload);
                CompoundTag stack = chest.getList("Items", Tag.TAG_COMPOUND).getCompound(0);
                prepareStack(stack);
                calls.add("prepare");
                return true;
            }
            @Override public void beforeChunkFlattening(CompoundTag source) {
                assertSame(chunk, source);
                assertEquals(3, chest.getList("Items", Tag.TAG_COMPOUND).getCompound(0).getCompound("tag").getInt("Damage"));
                calls.add("flatten");
            }
        };
        CompoundTag fixed = Legacy112WorldMigrator.convertChunkForTest(chunk,
                Map.of(0, "minecraft:air", 54, "minecraft:chest"), List.of(extension));
        CompoundTag stack = fixed.getCompound("Level").getList("TileEntities", Tag.TAG_COMPOUND)
                .getCompound(0).getList("Items", Tag.TAG_COMPOUND).getCompound(0);
        assertEquals(List.of("prepare", "flatten"), calls);
        assertEquals("example:wrench", stack.getString("id"));
        assertEquals(1, stack.getByte("Count"));
        assertEquals(4, stack.getByte("Slot"));
        assertEquals(3, stack.getCompound("tag").getInt("Damage"));

        CompoundTag current = DataFixTypes.CHUNK.updateToCurrentVersion(
                DataFixers.getDataFixer(), fixed, fixed.getInt("DataVersion"));
        assertComponentStack(current.getList("block_entities", Tag.TAG_COMPOUND)
                .getCompound(0).getList("Items", Tag.TAG_COMPOUND).getCompound(0));
    }

    @Test
    void unclaimedThirdPartyBlocksStillKeepTheirNameAndTheDecoderReceivesRawMetadata() throws Exception {
        List<Integer> metadata = new ArrayList<>();
        Legacy112MigrationExtension extension = new Legacy112MigrationExtension() {
            @Override public BlockState decodeBlockState(ResourceLocation id, int data) {
                assertEquals(ResourceLocation.fromNamespaceAndPath("example", "legacy_block"), id);
                metadata.add(data);
                return null;
            }
        };
        CompoundTag fixed = Legacy112WorldMigrator.convertChunkForTest(chunk(200, 13),
                COMPANION_BLOCKS, List.of(extension));
        assertEquals(List.of(13), metadata);
        assertEquals("example:legacy_block", Legacy112WorldMigrator.stateAtForTest(fixed, 0, 0).getString("Name"));
    }

    @Test
    void savesTheDecodedBlockPropertiesAndThePreparedOpaqueBlockEntity() throws Exception {
        CompoundTag chunk = chunk(200, 13);
        CompoundTag entity = new CompoundTag();
        entity.putString("id", "example:machine");
        entity.putInt("x", 0);
        entity.putInt("y", 0);
        entity.putInt("z", 0);
        chunk.getCompound("Level").getList("TileEntities", Tag.TAG_COMPOUND).add(entity);
        Legacy112MigrationExtension extension = new Legacy112MigrationExtension() {
            @Override public void beforeChunkFlattening(CompoundTag source) {
                source.getCompound("Level").getList("TileEntities", Tag.TAG_COMPOUND)
                        .getCompound(0).putInt("energy", 125);
            }

            @Override public BlockState decodeBlockState(ResourceLocation id, int data) {
                assertEquals(13, data);
                return Blocks.BARREL.defaultBlockState().setValue(BarrelBlock.FACING, Direction.EAST);
            }
        };
        CompoundTag fixed = Legacy112WorldMigrator.convertChunkForTest(chunk, COMPANION_BLOCKS, List.of(extension));
        CompoundTag state = Legacy112WorldMigrator.stateAtForTest(fixed, 0, 0);
        assertEquals("minecraft:barrel", state.getString("Name"));
        assertEquals("east", state.getCompound("Properties").getString("facing"));
        CompoundTag saved = fixed.getCompound("Level").getList("TileEntities", Tag.TAG_COMPOUND).getCompound(0);
        assertEquals("example:machine", saved.getString("id"));
        assertEquals(125, saved.getInt("energy"));
    }

    @Test
    void rejectsAirAndAmbiguousDecoders() {
        Legacy112MigrationExtension air = new Legacy112MigrationExtension() {
            @Override public BlockState decodeBlockState(ResourceLocation id, int data) {
                return Blocks.AIR.defaultBlockState();
            }
        };
        Legacy112MigrationExtension stone = new Legacy112MigrationExtension() {
            @Override public BlockState decodeBlockState(ResourceLocation id, int data) {
                return Blocks.STONE.defaultBlockState();
            }
        };
        assertThrows(IOException.class, () -> Legacy112WorldMigrator.convertChunkForTest(
                chunk(200, 13), COMPANION_BLOCKS, List.of(air)));
        assertThrows(IOException.class, () -> Legacy112WorldMigrator.convertChunkForTest(
                chunk(200, 13), COMPANION_BLOCKS, List.of(stone, stone)));
    }

    @Test
    void refreshesTheIntegratedPlayerWithoutWritingComponentsIntoALegacyLevel(
            @TempDir(cleanup = CleanupMode.ON_SUCCESS) Path world) throws Exception {
        verifyEmbeddedPlayer(world, 1343, false);
    }

    @Test
    void restoresTheCapturedPlayerAfterTheDedicatedServerRewritesALegacyLevel(
            @TempDir(cleanup = CleanupMode.ON_SUCCESS) Path world) throws Exception {
        verifyEmbeddedPlayer(world, 512, true);
    }

    @Test
    void restoresTheCapturedPlayerWithTheUnsplitNativeFixerWhenNoExtensionIsRegistered(
            @TempDir(cleanup = CleanupMode.ON_SUCCESS) Path world) throws Exception {
        Path levelDat = world.resolve("level.dat");
        CompoundTag original = player();
        addPlayerWitnesses(original);
        CompoundTag legacy = level(512, original);
        Legacy112WorldMigrator.beginWorldLoad();
        try {
            Legacy112WorldMigrator.prepareWorld(levelDat, legacy);
            LegacyNbt.writeCompressed(level(currentVersion(), new CompoundTag()), levelDat.toFile());
            CompoundTag expected = DataFixTypes.PLAYER.updateToCurrentVersion(
                    DataFixers.getDataFixer(), original.copy(), 512);

            Legacy112WorldMigrator.migrateIfNeeded(world, List.of());

            CompoundTag restored = LegacyNbt.readCompressed(levelDat.toFile())
                    .getCompound("Data").getCompound("Player");
            assertEquals(expected, restored);
            assertEquals(7, stack(restored, "Inventory", 2).getInt("count"));
            assertEquals("minecraft:red_wool", stack(restored, "Inventory", 2).getString("id"));
        } finally {
            Legacy112WorldMigrator.beginWorldLoad();
        }
    }

    private static void verifyEmbeddedPlayer(Path world, int version, boolean rewritten) throws Exception {
        Path levelDat = world.resolve("level.dat");
        CompoundTag original = player();
        addPlayerWitnesses(original);
        CompoundTag legacy = level(version, original);
        LegacyNbt.writeCompressed(legacy, levelDat.toFile());
        Legacy112WorldMigrator.beginWorldLoad();
        try {
            Legacy112WorldMigrator.prepareWorld(levelDat, legacy);
            CompoundTag onDisk = rewritten ? level(currentVersion(), new CompoundTag()) : legacy;
            if (rewritten) LegacyNbt.writeCompressed(onDisk, levelDat.toFile());
            byte[] originalLevel = Files.readAllBytes(levelDat);
            List<String> calls = new ArrayList<>();
            Legacy112MigrationExtension extension = new Legacy112MigrationExtension() {
                @Override public boolean prepareData(CompoundTag tag, int dataVersion, Payload payload) {
                    assertEquals(version, dataVersion);
                    assertEquals(Payload.PLAYER, payload);
                    prepareStack(inventoryStack(tag));
                    calls.add("prepare");
                    return true;
                }

                @Override public void afterPlayerFlattening(CompoundTag source, CompoundTag result) {
                    assertEquals(3, inventoryStack(source).getCompound("tag").getInt("Damage"));
                    assertLegacyWitnesses(result);
                    assertEquals(3, inventoryStack(result).getCompound("tag").getInt("Damage"));
                    assertEquals("minecraft:wool", stack(source, "Inventory", 2).getString("id"));
                    restoreBookName(source, result, "Inventory", 1, "example:precision", 3);
                    restoreBookName(source, result, "EnderItems", 0, "example:ward", 2);
                    result.putInt("companionEnergy", 125);
                    calls.add("after");
                }
            };
            Legacy112MigrationExtension observer = new Legacy112MigrationExtension() {
                @Override public void afterPlayerFlattening(CompoundTag source, CompoundTag result) {
                    assertEquals(List.of("prepare", "after"), calls);
                    assertLegacyWitnesses(result);
                    assertEquals(125, result.getInt("companionEnergy"));
                    assertEquals("example:precision", storedEnchantment(stack(result, "Inventory", 1)).getString("id"));
                    assertEquals("example:ward", storedEnchantment(stack(result, "EnderItems", 0)).getString("id"));
                    calls.add("observe");
                }
            };
            List<Legacy112MigrationExtension> extensions = List.of(extension, observer);
            Legacy112WorldMigrator.migrateIfNeeded(world, extensions);
            CompoundTag loaded = new CompoundTag();
            loaded.putBoolean("stale", true);
            Legacy112WorldMigrator.refreshLoadedSingleplayerPlayer(loaded, world);
            CompoundTag diskData = LegacyNbt.readCompressed(levelDat.toFile()).getCompound("Data");
            assertEquals(List.of("prepare", "after", "observe"), calls);
            assertFalse(loaded.contains("stale"));
            assertComponentStack(inventoryStack(loaded));
            assertComponentBook(stack(loaded, "Inventory", 1), 9, "example:precision", 3);
            assertComponentBook(stack(loaded, "EnderItems", 0), 5, "example:ward", 2);
            CompoundTag wool = stack(loaded, "Inventory", 2);
            assertEquals("minecraft:red_wool", wool.getString("id"));
            assertEquals(7, wool.getInt("count"));
            assertEquals(12, wool.getByte("Slot"));
            assertFalse(wool.contains("Count"));
            assertEquals(125, loaded.getInt("companionEnergy"));
            assertEquals(rewritten ? currentVersion() : version, diskData.getInt("DataVersion"));
            if (rewritten) {
                assertEquals(loaded, diskData.getCompound("Player"));
            } else {
                CompoundTag stack = inventoryStack(diskData.getCompound("Player"));
                assertEquals(3, stack.getCompound("tag").getInt("Damage"));
                assertFalse(stack.contains("components"));
                assertFalse(diskData.getCompound("Player").contains("companionEnergy"));
            }
            assertArrayEquals(originalLevel, Files.readAllBytes(world.resolve("basemetals-1.12-region-backup/level.dat")));
            assertTrue(Files.readString(world.resolve(MARKER)).contains("Companion player data migrated."));
            Legacy112WorldMigrator.migrateIfNeeded(world, extensions);
            assertEquals(List.of("prepare", "after", "observe"), calls);
        } finally {
            Legacy112WorldMigrator.beginWorldLoad();
        }
    }

    @Test
    void onlyLegacyLoosePlayerDataReceivesThePrepareHook(
            @TempDir(cleanup = CleanupMode.ON_SUCCESS) Path world) throws Exception {
        LegacyNbt.writeCompressed(level(1343, null), world.resolve("level.dat").toFile());
        Path playerData = Files.createDirectory(world.resolve("playerdata"));
        Path legacyFile = playerData.resolve("legacy.dat");
        Path modernFile = playerData.resolve("modern.dat");
        CompoundTag legacy = player();
        legacy.putInt("DataVersion", 1343);
        LegacyNbt.writeCompressed(legacy, legacyFile.toFile());
        CompoundTag modern = new CompoundTag();
        modern.putInt("DataVersion", currentVersion());
        LegacyNbt.writeCompressed(modern, modernFile.toFile());
        byte[] originalLegacy = Files.readAllBytes(legacyFile);
        byte[] originalModern = Files.readAllBytes(modernFile);
        List<Integer> versions = new ArrayList<>();
        Legacy112MigrationExtension extension = new Legacy112MigrationExtension() {
            @Override public boolean prepareData(CompoundTag tag, int version, Payload payload) {
                assertEquals(Payload.PLAYER, payload);
                versions.add(version);
                prepareStack(inventoryStack(tag));
                return true;
            }

            @Override public void afterPlayerFlattening(CompoundTag source, CompoundTag result) {
                fail("The embedded player hook must not run for detached playerdata");
            }
        };
        Legacy112WorldMigrator.migrateIfNeeded(world, List.of(extension));
        assertEquals(List.of(1343), versions);
        assertArrayEquals(originalModern, Files.readAllBytes(modernFile));
        assertArrayEquals(originalLegacy,
                Files.readAllBytes(world.resolve("basemetals-1.12-region-backup/playerdata/legacy.dat")));
        CompoundTag prepared = LegacyNbt.readCompressed(legacyFile.toFile());
        assertEquals(1343, prepared.getInt("DataVersion"));
        assertComponentStack(inventoryStack(DataFixTypes.PLAYER.updateToCurrentVersion(
                DataFixers.getDataFixer(), prepared, 1343)));
    }

    private static int currentVersion() {
        return SharedConstants.getCurrentVersion().getDataVersion().getVersion();
    }

    private static CompoundTag level(int version, CompoundTag player) {
        CompoundTag root = new CompoundTag();
        CompoundTag data = new CompoundTag();
        data.putInt("DataVersion", version);
        if (player != null) data.put("Player", player);
        root.put("Data", data);
        return root;
    }

    private static CompoundTag player() {
        CompoundTag player = new CompoundTag();
        ListTag items = new ListTag();
        items.add(legacyStack());
        player.put("Inventory", items);
        return player;
    }

    private static CompoundTag inventoryStack(CompoundTag player) {
        return stack(player, "Inventory", 0);
    }

    private static CompoundTag stack(CompoundTag player, String inventory, int index) {
        return player.getList(inventory, Tag.TAG_COMPOUND).getCompound(index);
    }

    private static void addPlayerWitnesses(CompoundTag player) {
        ListTag inventory = player.getList("Inventory", Tag.TAG_COMPOUND);
        inventory.add(legacyBook(9, "example:precision", 3));
        CompoundTag wool = new CompoundTag();
        wool.putString("id", "minecraft:wool");
        wool.putByte("Count", (byte) 7);
        wool.putByte("Slot", (byte) 12);
        wool.putShort("Damage", (short) 14);
        inventory.add(wool);
        ListTag enderItems = new ListTag();
        enderItems.add(legacyBook(5, "example:ward", 2));
        player.put("EnderItems", enderItems);
    }

    private static CompoundTag legacyBook(int slot, String name, int level) {
        CompoundTag book = new CompoundTag();
        book.putString("id", "minecraft:enchanted_book");
        book.putByte("Count", (byte) 1);
        book.putByte("Slot", (byte) slot);
        CompoundTag enchantment = new CompoundTag();
        enchantment.putString("id", name);
        enchantment.putShort("lvl", (short) level);
        ListTag enchantments = new ListTag();
        enchantments.add(enchantment);
        CompoundTag tag = new CompoundTag();
        tag.put("StoredEnchantments", enchantments);
        book.put("tag", tag);
        return book;
    }

    private static CompoundTag storedEnchantment(CompoundTag stack) {
        ListTag enchantments = stack.getCompound("tag").getList("StoredEnchantments", Tag.TAG_COMPOUND);
        assertEquals(1, enchantments.size());
        return enchantments.getCompound(0);
    }

    private static void assertLegacyWitnesses(CompoundTag result) {
        assertEquals(3, result.getList("Inventory", Tag.TAG_COMPOUND).size());
        assertEquals(1, result.getList("EnderItems", Tag.TAG_COMPOUND).size());
        CompoundTag wool = stack(result, "Inventory", 2);
        assertEquals("minecraft:red_wool", wool.getString("id"));
        assertEquals(7, wool.getByte("Count"));
        assertEquals(12, wool.getByte("Slot"));
        assertFalse(wool.contains("count"));
        assertFalse(wool.contains("components"));
    }

    private static void restoreBookName(CompoundTag source, CompoundTag result, String inventory, int index,
            String name, int level) {
        CompoundTag before = stack(source, inventory, index);
        CompoundTag after = stack(result, inventory, index);
        assertEquals("minecraft:enchanted_book", after.getString("id"));
        assertEquals(before.get("Count"), after.get("Count"));
        assertEquals(before.get("Slot"), after.get("Slot"));
        assertFalse(after.contains("count"));
        assertFalse(after.contains("components"));
        assertEquals(name, storedEnchantment(before).getString("id"));
        assertEquals(level, storedEnchantment(after).getInt("lvl"));
        storedEnchantment(after).putString("id", name);
    }

    private static void assertComponentBook(CompoundTag stack, int slot, String name, int level) {
        assertEquals("minecraft:enchanted_book", stack.getString("id"));
        assertEquals(1, stack.getInt("count"));
        assertEquals(slot, stack.getByte("Slot"));
        assertFalse(stack.contains("Count"));
        assertFalse(stack.contains("tag"));
        CompoundTag levels = stack.getCompound("components").getCompound("minecraft:stored_enchantments")
                .getCompound("levels");
        assertEquals(1, levels.size());
        assertEquals(level, levels.getInt(name));
    }

    private static CompoundTag legacyStack() {
        CompoundTag stack = new CompoundTag();
        stack.putString("id", "example:wrench");
        stack.putByte("Count", (byte) 1);
        stack.putByte("Slot", (byte) 4);
        stack.putShort("Damage", (short) 3);
        return stack;
    }

    private static void prepareStack(CompoundTag stack) {
        CompoundTag itemData = new CompoundTag();
        itemData.putInt("Damage", stack.getShort("Damage"));
        itemData.putInt("companionCharge", 42);
        stack.put("tag", itemData);
    }

    private static void assertComponentStack(CompoundTag stack) {
        assertEquals("example:wrench", stack.getString("id"));
        assertEquals(1, stack.getInt("count"));
        assertEquals(4, stack.getByte("Slot"));
        assertFalse(stack.contains("Count"));
        assertFalse(stack.contains("tag"));
        CompoundTag components = stack.getCompound("components");
        assertEquals(3, components.getInt("minecraft:damage"));
        assertEquals(42, components.getCompound("minecraft:custom_data").getInt("companionCharge"));
    }

    private static CompoundTag chunk(int block, int metadata) {
        CompoundTag root = new CompoundTag();
        root.putInt("DataVersion", 1343);
        CompoundTag level = new CompoundTag();
        level.putBoolean("TerrainPopulated", true);
        level.putBoolean("LightPopulated", true);
        level.putByteArray("Biomes", new byte[256]);
        level.putIntArray("HeightMap", new int[256]);
        CompoundTag section = new CompoundTag();
        byte[] blocks = new byte[4096];
        blocks[0] = (byte) block;
        section.putByteArray("Blocks", blocks);
        byte[] data = new byte[2048];
        data[0] = (byte) metadata;
        section.putByteArray("Data", data);
        section.putByte("Y", (byte) 0);
        ListTag sections = new ListTag();
        sections.add(section);
        level.put("Sections", sections);
        ListTag blockEntities = new ListTag();
        if (block == 54) {
            CompoundTag chest = new CompoundTag();
            chest.putString("id", "minecraft:chest");
            ListTag items = new ListTag();
            items.add(legacyStack());
            chest.put("Items", items);
            blockEntities.add(chest);
        }
        level.put("TileEntities", blockEntities);
        level.put("Entities", new ListTag());
        root.put("Level", level);
        return root;
    }
}
