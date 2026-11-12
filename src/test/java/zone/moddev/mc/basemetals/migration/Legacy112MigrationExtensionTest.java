package zone.moddev.mc.basemetals.migration;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.SharedConstants;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.Identifier;
import net.minecraft.server.Bootstrap;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import zone.moddev.mc.basemetals.api.migration.Legacy112MigrationExtension;

class Legacy112MigrationExtensionTest {
    private static final Identifier EXTENSION = Identifier.parse("example:host_player_test");
    private static final UUID PLAYER = UUID.fromString("22fd001d-a32d-4c0d-b921-7f606aa48e8e");
    private static final Map<Integer, String> BLOCKS = Map.of(0, "minecraft:air", 200, "example:machine");

    @BeforeAll
    static void gameVersion() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        assertEquals(4903, SharedConstants.getCurrentVersion().dataVersion().version());
    }

    @Test
    void vanillaChestReceivesCompanionInventoryBeforeFlattening() throws Exception {
        CompoundTag chunk = chunk(54, 2);
        CompoundTag chest = chunk.getCompoundOrEmpty("Level").getListOrEmpty("TileEntities").getCompoundOrEmpty(0);
        List<String> calls = new ArrayList<>();
        var extension = new Legacy112MigrationExtension() {
            @Override public boolean prepareData(CompoundTag tag, int version, Payload payload) {
                assertEquals(1343, version);
                assertEquals(Payload.CHUNK, payload);
                prepareStack(chest.getListOrEmpty("Items").getCompoundOrEmpty(0));
                calls.add("prepare");
                return true;
            }
            @Override public void beforeChunkFlattening(CompoundTag tag) {
                assertSame(chunk, tag);
                calls.add("flatten");
            }
        };
        CompoundTag fixed = Legacy112WorldMigrator.convertChunkForTest(chunk,
                Map.of(0, "minecraft:air", 54, "minecraft:chest"), List.of(extension));
        assertEquals(List.of("prepare", "flatten"), calls);
        CompoundTag current = DataFixTypes.CHUNK.updateToCurrentVersion(DataFixers.getDataFixer(), fixed, 1456);
        assertStack(current.getListOrEmpty("block_entities").getCompoundOrEmpty(0)
                .getListOrEmpty("Items").getCompoundOrEmpty(0));
    }

    @Test
    void directionsAndOpaqueStagedEntitiesSurviveTheSnapshot() throws Exception {
        CompoundTag chunk = chunk(200, 13);
        CompoundTag machine = new CompoundTag();
        machine.putString("id", "example:machine");
        machine.putInt("x", 0);
        machine.putInt("y", 0);
        machine.putInt("z", 0);
        chunk.getCompoundOrEmpty("Level").getListOrEmpty("TileEntities").add(machine);
        var extension = new Legacy112MigrationExtension() {
            @Override public void beforeChunkFlattening(CompoundTag source) {
                machine.putInt("energy", 125);
                CompoundTag staged = new CompoundTag();
                staged.putString("id", "example:cart");
                staged.putLong("UUIDMost", PLAYER.getMostSignificantBits());
                staged.putLong("UUIDLeast", PLAYER.getLeastSignificantBits());
                ListTag inventory = new ListTag();
                inventory.add(legacyStack());
                staged.put("Items", inventory);
                machine.put("stagedEntity", staged);
            }
            @Override public BlockState decodeBlockState(Identifier id, int metadata) {
                assertEquals("example:machine", id.toString());
                assertEquals(13, metadata);
                return Blocks.BARREL.defaultBlockState().setValue(BarrelBlock.FACING, Direction.EAST);
            }
        };
        CompoundTag fixed = Legacy112WorldMigrator.convertChunkForTest(chunk, BLOCKS, List.of(extension));
        CompoundTag state = Legacy112WorldMigrator.stateAtForTest(fixed, 0, 0);
        assertEquals("east", state.getCompoundOrEmpty("Properties").getStringOr("facing", ""));
        CompoundTag saved = fixed.getCompoundOrEmpty("Level").getListOrEmpty("TileEntities").getCompoundOrEmpty(0);
        assertEquals(125, saved.getIntOr("energy", 0));
        assertEquals("example:cart", saved.getCompoundOrEmpty("stagedEntity").getStringOr("id", ""));
        assertEquals(PLAYER.getMostSignificantBits(), saved.getCompoundOrEmpty("stagedEntity").getLongOr("UUIDMost", 0));
        assertEquals(machine.get("stagedEntity"), saved.get("stagedEntity"));
    }

    @Test
    void unclaimedStatesKeepTheirIdentityAndAirOrAmbiguousClaimsFail() throws Exception {
        CompoundTag fixed = Legacy112WorldMigrator.convertChunkForTest(chunk(200, 13), BLOCKS, List.of());
        assertEquals("example:machine", Legacy112WorldMigrator.stateAtForTest(fixed, 0, 0).getStringOr("Name", ""));
        var air = decoder(Blocks.AIR.defaultBlockState());
        var stone = decoder(Blocks.STONE.defaultBlockState());
        assertThrows(IOException.class, () -> Legacy112WorldMigrator.convertChunkForTest(chunk(200, 13), BLOCKS, List.of(air)));
        assertThrows(IOException.class, () -> Legacy112WorldMigrator.convertChunkForTest(chunk(200, 13), BLOCKS, List.of(stone, stone)));
    }

    @Test
    void modernChunksNeverReachCompanionCallbacks() {
        CompoundTag chunk = chunk(200, 13);
        chunk.putInt("DataVersion", 4903);
        CompoundTag before = chunk.copy();
        var extension = new Legacy112MigrationExtension() {
            @Override public boolean prepareData(CompoundTag tag, int version, Payload payload) {
                fail("Modern chunk reached legacy preparation");
                return false;
            }
        };
        assertThrows(IOException.class, () -> Legacy112WorldMigrator.convertChunkForTest(chunk, BLOCKS, List.of(extension)));
        assertEquals(before, chunk);
    }

    @Test
    void detachedPlayersInBothLayoutsKeepTheirVersionAndModernFilesStayExact(@TempDir Path world) throws Exception {
        LegacyNbt.writeCompressed(level(1343, null), world.resolve("level.dat").toFile());
        List<Integer> versions = new ArrayList<>();
        var extension = new Legacy112MigrationExtension() {
            @Override public boolean prepareData(CompoundTag tag, int version, Payload payload) {
                assertEquals(Payload.PLAYER, payload);
                versions.add(version);
                prepareStack(stack(tag));
                return true;
            }
            @Override public void afterPlayerFlattening(CompoundTag source, CompoundTag result) {
                fail("Detached player must be loaded through native PlayerDataStorage");
            }
        };
        for (String layout : List.of("playerdata", "players/data")) {
            Path directory = Files.createDirectories(world.resolve(layout));
            CompoundTag legacy = player();
            legacy.putInt("DataVersion", 1343);
            LegacyNbt.writeCompressed(legacy, directory.resolve("legacy.dat").toFile());
            CompoundTag modern = player();
            modern.putInt("DataVersion", 4903);
            LegacyNbt.writeCompressed(modern, directory.resolve("modern.dat").toFile());
        }
        byte[] modern = Files.readAllBytes(world.resolve("players/data/modern.dat"));
        byte[] old = Files.readAllBytes(world.resolve("playerdata/legacy.dat"));
        Legacy112WorldMigrator.migrateIfNeeded(world, List.of(extension));
        assertEquals(List.of(1343, 1343), versions);
        assertArrayEquals(modern, Files.readAllBytes(world.resolve("players/data/modern.dat")));
        assertArrayEquals(old, Files.readAllBytes(world.resolve("basemetals-1.12-region-backup/playerdata/legacy.dat")));
        assertEquals(1343, LegacyNbt.readCompressed(world.resolve("players/data/legacy.dat").toFile()).getIntOr("DataVersion", 0));
        Legacy112WorldMigrator.migrateIfNeeded(world, List.of(extension));
        assertEquals(2, versions.size());
    }

    @Test
    void hostSeedSplitsThePlayerFixerWithoutComponentizingLegacyLevel(@TempDir Path world) throws Exception {
        CompoundTag player = player();
        CompoundTag legacy = level(512, player);
        Path levelDat = world.resolve("level.dat");
        LegacyNbt.writeCompressed(legacy, levelDat.toFile());
        byte[] original = Files.readAllBytes(levelDat);
        List<String> calls = new ArrayList<>();
        var extension = new Legacy112MigrationExtension() {
            @Override public boolean prepareData(CompoundTag tag, int version, Payload payload) {
                assertEquals(512, version);
                assertEquals(Payload.PLAYER, payload);
                prepareStack(stack(tag));
                calls.add("prepare");
                return true;
            }
            @Override public void afterPlayerFlattening(CompoundTag source, CompoundTag result) {
                assertEquals("example:musket", stack(source).getStringOr("id", ""));
                assertEquals(1, stack(result).getByteOr("Count", (byte) 0));
                assertFalse(stack(result).contains("components"));
                result.putInt("companionEnergy", 125);
                calls.add("after");
            }
        };
        withExtension(extension, () -> LegacyWorldDataHook.prepareSelectedWorld(levelDat));
        assertEquals(List.of("prepare", "after"), calls);
        CompoundTag seeded = LegacyNbt.readCompressed(world.resolve("playerdata/" + PLAYER + ".dat").toFile());
        assertEquals(4903, seeded.getIntOr("DataVersion", 0));
        assertStack(stack(seeded));
        assertEquals(125, seeded.getIntOr("companionEnergy", 0));
        CompoundTag disk = LegacyNbt.readCompressed(levelDat.toFile()).getCompoundOrEmpty("Data");
        assertEquals(512, disk.getIntOr("DataVersion", 0));
        assertFalse(stack(disk.getCompoundOrEmpty("Player")).contains("components"));
        assertArrayEquals(original, Files.readAllBytes(world.resolve("basemetals-1.12-region-backup/level.dat")));
    }

    @Test
    void currentLooseHostAuthorityWinsInEitherLayout(@TempDir Path root) throws Exception {
        for (String layout : List.of("playerdata", "players/data")) {
            Path world = Files.createDirectories(root.resolve(layout.replace('/', '-')));
            Path levelDat = world.resolve("level.dat");
            LegacyNbt.writeCompressed(level(512, player()), levelDat.toFile());
            Path existing = Files.createDirectories(world.resolve(layout)).resolve(PLAYER + ".dat");
            CompoundTag current = new CompoundTag();
            current.putInt("DataVersion", 4903);
            current.putInt("currentAuthority", 91);
            LegacyNbt.writeCompressed(current, existing.toFile());
            byte[] bytes = Files.readAllBytes(existing);
            withExtension(new Legacy112MigrationExtension() {
                @Override public void afterPlayerFlattening(CompoundTag source, CompoundTag result) {
                    fail("Existing authoritative host file must not be replaced");
                }
            }, () -> LegacyWorldDataHook.prepareSelectedWorld(levelDat));
            assertArrayEquals(bytes, Files.readAllBytes(existing));
            if (layout.equals("players/data")) assertFalse(Files.exists(world.resolve("playerdata/" + PLAYER + ".dat")));
        }
    }

    @Test
    void postFlattenCallbacksCooperateInBothOrdersAndPreserveTheirSource() {
        for (boolean reverse : new boolean[] {false, true}) {
            CompoundTag player = player();
            prepareStack(stack(player));
            CompoundTag before = player.copy();
            var first = new Legacy112MigrationExtension() {
                @Override public void afterPlayerFlattening(CompoundTag source, CompoundTag result) {
                    assertEquals(before, source);
                    result.putInt("firstCompanion", 11);
                }
            };
            var second = new Legacy112MigrationExtension() {
                @Override public void afterPlayerFlattening(CompoundTag source, CompoundTag result) {
                    assertEquals(before, source);
                    result.putInt("secondCompanion", 22);
                }
            };
            CompoundTag fixed = Legacy112WorldMigrator.flattenEmbeddedPlayer(player.copy(), 512,
                    reverse ? List.of(second, first) : List.of(first, second));
            assertEquals(before, player);
            assertEquals(11, fixed.getIntOr("firstCompanion", 0));
            assertEquals(22, fixed.getIntOr("secondCompanion", 0));
            assertStack(stack(fixed));
        }
    }

    @Test
    void duplicateAndNullRegistrationsFailClosed() throws Exception {
        withExtension(new Legacy112MigrationExtension() {}, () -> {
            assertThrows(IllegalArgumentException.class, () -> Legacy112MigrationExtension.register(EXTENSION, new Legacy112MigrationExtension() {}));
            assertThrows(NullPointerException.class, () -> Legacy112MigrationExtension.register(null, new Legacy112MigrationExtension() {}));
            assertThrows(NullPointerException.class, () -> Legacy112MigrationExtension.register(Identifier.parse("example:null"), null));
        });
    }

    @Test
    void namedBookEnchantmentsAreRestoredBeforeComponentsWithoutChangingSlotsOrCounts() {
        for (int version : new int[] {512, 1343}) {
            CompoundTag player = player();
            ListTag enderItems = new ListTag();
            CompoundTag book = new CompoundTag();
            book.putString("id", "minecraft:enchanted_book");
            book.putByte("Count", (byte) 1);
            book.putByte("Slot", (byte) 9);
            CompoundTag enchantment = new CompoundTag();
            enchantment.putString("id", "example:precision");
            enchantment.putShort("lvl", (short) 3);
            ListTag enchantments = new ListTag();
            enchantments.add(enchantment);
            CompoundTag tag = new CompoundTag();
            tag.put("StoredEnchantments", enchantments);
            book.put("tag", tag);
            enderItems.add(book);
            player.put("EnderItems", enderItems);
            CompoundTag original = player.copy();
            var extension = new Legacy112MigrationExtension() {
                @Override public void afterPlayerFlattening(CompoundTag source, CompoundTag result) {
                    assertEquals(original, source);
                    CompoundTag fixedBook = result.getListOrEmpty("EnderItems").getCompoundOrEmpty(0);
                    assertEquals(book.get("Count"), fixedBook.get("Count"));
                    assertEquals(book.get("Slot"), fixedBook.get("Slot"));
                    assertFalse(fixedBook.contains("components"));
                    fixedBook.getCompoundOrEmpty("tag").getListOrEmpty("StoredEnchantments")
                            .getCompoundOrEmpty(0).putString("id", "example:precision");
                }
            };
            CompoundTag fixed = Legacy112WorldMigrator.flattenEmbeddedPlayer(player, version, List.of(extension));
            CompoundTag fixedBook = fixed.getListOrEmpty("EnderItems").getCompoundOrEmpty(0);
            assertEquals(1, fixedBook.getIntOr("count", 0));
            assertEquals(9, fixedBook.getByteOr("Slot", (byte) 0));
            assertEquals(3, fixedBook.getCompoundOrEmpty("components")
                    .getCompoundOrEmpty("minecraft:stored_enchantments").getIntOr("example:precision", 0));
        }
    }

    private static void withExtension(Legacy112MigrationExtension extension, Runnable action) throws Exception {
        Legacy112MigrationExtension.register(EXTENSION, extension);
        try {
            action.run();
        } finally {
            var field = Legacy112WorldMigrator.class.getDeclaredField("EXTENSIONS");
            field.setAccessible(true);
            ((Map<?, ?>) field.get(null)).remove(EXTENSION);
            Legacy112WorldMigrator.beginWorldLoad();
        }
    }

    private static Legacy112MigrationExtension decoder(BlockState state) {
        return new Legacy112MigrationExtension() {
            @Override public BlockState decodeBlockState(Identifier id, int metadata) { return state; }
        };
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
        player.putLong("UUIDMost", PLAYER.getMostSignificantBits());
        player.putLong("UUIDLeast", PLAYER.getLeastSignificantBits());
        ListTag inventory = new ListTag();
        inventory.add(legacyStack());
        player.put("Inventory", inventory);
        return player;
    }

    private static CompoundTag stack(CompoundTag player) {
        return player.getListOrEmpty("Inventory").getCompoundOrEmpty(0);
    }

    private static CompoundTag legacyStack() {
        CompoundTag stack = new CompoundTag();
        stack.putString("id", "example:musket");
        stack.putByte("Count", (byte) 1);
        stack.putByte("Slot", (byte) 4);
        stack.putShort("Damage", (short) 3);
        return stack;
    }

    private static void prepareStack(CompoundTag stack) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Damage", stack.getShortOr("Damage", (short) 0));
        tag.putInt("companionCharge", 42);
        stack.put("tag", tag);
    }

    private static void assertStack(CompoundTag stack) {
        assertEquals("example:musket", stack.getStringOr("id", ""));
        assertEquals(1, stack.getIntOr("count", 0));
        assertEquals(4, stack.getByteOr("Slot", (byte) 0));
        assertFalse(stack.contains("Count"));
        assertFalse(stack.contains("tag"));
        assertEquals(3, stack.getCompoundOrEmpty("components").getIntOr("minecraft:damage", 0));
        assertEquals(42, stack.getCompoundOrEmpty("components").getCompoundOrEmpty("minecraft:custom_data").getIntOr("companionCharge", 0));
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
        ListTag tiles = new ListTag();
        if (block == 54) {
            CompoundTag chest = new CompoundTag();
            chest.putString("id", "minecraft:chest");
            ListTag items = new ListTag();
            items.add(legacyStack());
            chest.put("Items", items);
            tiles.add(chest);
        }
        level.put("TileEntities", tiles);
        level.put("Entities", new ListTag());
        root.put("Level", level);
        return root;
    }
}
