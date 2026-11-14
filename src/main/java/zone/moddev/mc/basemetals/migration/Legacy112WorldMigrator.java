package zone.moddev.mc.basemetals.migration;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;

import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.MissingMappings;
import zone.moddev.mc.basemetals.api.migration.Legacy112MigrationExtension;
import zone.moddev.mc.basemetals.api.migration.Legacy112MigrationExtension.Payload;
import zone.moddev.mc.lib.common.runtime.MaterialAnvilBlock;
import zone.moddev.mc.lib.common.runtime.CompatibilityDoubleSlabBlock;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.lib.common.runtime.PlateBlock;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.storage.RegionFile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
import net.minecraft.core.registries.BuiltInRegistries;

/**
 * Runs Mojang's complete legacy-to-flattening chunk data fixes, then restores the
 * third-party block positions whose numeric IDs Mojang cannot know. This keeps
 * all of the context-sensitive vanilla, entity, block-entity, and item fixes
 * while preserving Base Metals and other mod blocks by registry name.
 */
public final class Legacy112WorldMigrator {
    private static final int LAST_SUPPORTED_LEGACY_DATA_VERSION = 1_343;
    private static final int PALETTED_PRE_PROTOCHUNK_DATA_VERSION = 1_456;
    private static final int NAMESPACED_BLOCK_ENTITY_DATA_VERSION = 704;
    private static final int PRE_COMPONENT_PLAYER_DATA_VERSION = 3_465;
    private static final int REGION_HEADER_BYTES = 8_192;
    private static final int SECTOR_BYTES = 4_096;
    private static final String MARKER = "BASEMETALS_1_12_BLOCK_MIGRATION_COMPLETE.txt";
    private static final String BACKUP_DIRECTORY = "basemetals-1.12-region-backup";
    private static final String PLAYER_SOURCE = "player-migration-source.dat";
    private static final Map<ResourceLocation, Legacy112MigrationExtension> EXTENSIONS = new ConcurrentSkipListMap<>();
    private static Path preparedWorld;
    private static CompoundTag preparedPlayer;
    private static int preparedVersion;
    private static CompoundTag preparedIntegratedPlayer;

    private Legacy112WorldMigrator() {}

    static synchronized void beginWorldLoad() {
        preparedWorld = null;
        preparedPlayer = null;
        preparedVersion = 0;
        preparedIntegratedPlayer = null;
    }

    /** Keep the original player before Forge and vanilla read the old save. */
    static synchronized void prepareWorld(Path levelDat, CompoundTag root) throws IOException {
        Path world = levelDat.toAbsolutePath().normalize().getParent();
        if (Files.isRegularFile(world.resolve(MARKER)) && sourceDataVersion(root) >= currentDataVersion()) return;
        Path journal = world.resolve(BACKUP_DIRECTORY).resolve(PLAYER_SOURCE);
        if (Files.exists(journal) && !Files.isRegularFile(journal))
            throw new IOException("Pending legacy player snapshot is not a file: " + journal);
        if (isLegacyLevel(root)) {
            preparedWorld = world;
            preparedVersion = sourceDataVersion(root);
            CompoundTag data = root.getCompound("Data");
            preparedPlayer = data.contains("Player", Tag.TAG_COMPOUND) ? data.getCompound("Player").copy() : null;
            // Persist before the loader can rewrite level.dat. Never replace the
            // first snapshot with an already-fixed player after an interrupted load.
            if (preparedPlayer != null && !Files.exists(journal)) {
                CompoundTag saved = new CompoundTag();
                saved.putInt("DataVersion", preparedVersion);
                saved.put("Player", preparedPlayer.copy());
                Files.createDirectories(journal.getParent());
                writeCompressedAtomically(journal, saved);
            }
        }
        if (Files.isRegularFile(journal)) {
            CompoundTag saved = NbtIo.readCompressed(journal, NbtAccounter.unlimitedHeap());
            if (!saved.contains("Player", Tag.TAG_COMPOUND)
                    || !saved.contains("DataVersion", Tag.TAG_ANY_NUMERIC)
                    || saved.getInt("DataVersion") > LAST_SUPPORTED_LEGACY_DATA_VERSION) {
                throw new IOException("Invalid pending legacy player snapshot: " + journal);
            }
            preparedWorld = world;
            preparedVersion = saved.getInt("DataVersion");
            preparedPlayer = saved.getCompound("Player").copy();
        }
    }

    private static boolean isLegacyLevel(CompoundTag root) {
        return root.contains("Data", Tag.TAG_COMPOUND)
                && sourceDataVersion(root) <= LAST_SUPPORTED_LEGACY_DATA_VERSION;
    }

    static int sourceDataVersion(CompoundTag root) {
        CompoundTag data = root.getCompound("Data");
        if (data.contains("DataVersion", Tag.TAG_ANY_NUMERIC)) return data.getInt("DataVersion");
        if (data.getCompound("Version").contains("Id", Tag.TAG_ANY_NUMERIC))
            return data.getCompound("Version").getInt("Id");
        // Very old saves have no version tag. Known modern level metadata must
        // not opt into the packaged numeric-ID fallback just because a tag is missing.
        return data.contains("WorldGenSettings", Tag.TAG_COMPOUND) ? Integer.MAX_VALUE : 0;
    }

    private static boolean hasLegacySource(Path root) throws IOException {
        if (root.equals(preparedWorld) || isLegacyLevel(NbtIo.readCompressed(root.resolve("level.dat"), NbtAccounter.unlimitedHeap()))) {
            return true;
        }

        // This sidecar survives Forge's first rewrite. An old level.dat_old alone
        // is not evidence that the current world still needs flattening.
        Path sidecar = root.resolve("basemetals-legacy-registry.dat");
        if (!Files.isRegularFile(sidecar)) return false;

        CompoundTag source = NbtIo.readCompressed(sidecar, NbtAccounter.unlimitedHeap());
        return isLegacyLevel(source) || oldBlockIds(source).values().stream()
                .map(Legacy112WorldMigrator::normalizeLegacyRegistryName)
                .anyMatch(id -> id.startsWith(BaseMetals.MOD_ID + ":") || id.startsWith("mmdlib:"));
    }

    public static void registerExtension(ResourceLocation id, Legacy112MigrationExtension extension) {
        if (EXTENSIONS.putIfAbsent(Objects.requireNonNull(id), Objects.requireNonNull(extension)) != null) {
            throw new IllegalArgumentException("Duplicate legacy world migration extension: " + id);
        }
    }

    public static synchronized boolean migrateIfNeeded(Path worldRoot) throws IOException {
        return migrateIfNeeded(worldRoot, List.copyOf(EXTENSIONS.values()));
    }

    static synchronized boolean migrateIfNeeded(Path worldRoot, List<Legacy112MigrationExtension> extensions) throws IOException {
        Path root = worldRoot.toAbsolutePath().normalize();
        if (!Files.isRegularFile(root.resolve("level.dat"))) return false;
        if (Files.isRegularFile(root.resolve(MARKER))) {
            // Region conversion may finish before the first modern level.dat save.
            // A new process must still repair that host, without converting regions again.
            if (root.equals(preparedWorld) && preparedPlayer != null && preparedIntegratedPlayer == null
                    && sourceDataVersion(NbtIo.readCompressed(root.resolve("level.dat"), NbtAccounter.unlimitedHeap()))
                            < currentDataVersion()) {
                return !migrateLooseInventories(root, extensions).isEmpty();
            }
            return false;
        }
        if (!hasLegacySource(root)) return false;

        Map<Integer, String> oldBlockIds = legacyBlockIds(root);
        if (oldBlockIds.values().stream().noneMatch(id -> id.startsWith(BaseMetals.MOD_ID + ":"))) return false;

        List<Path> regions = findRegionFiles(root);

        int convertedRegions = 0;
        int convertedChunks = 0;
        int externalStates = 0;
        int preservedBlockEntities = 0;
        int convertedBuckets = 0;
        int convertedItemAliases = 0;
        int convertedDamageValues = 0;
        for (Path region : regions) {
            RegionConversion result = convertRegion(root, region, oldBlockIds, extensions);
            if (result.convertedChunks() > 0) {
                convertedRegions++;
                convertedChunks += result.convertedChunks();
                externalStates += result.externalStates();
                preservedBlockEntities += result.preservedBlockEntities();
                convertedBuckets += result.convertedBuckets();
                convertedItemAliases += result.convertedItemAliases();
                convertedDamageValues += result.convertedDamageValues();
            }
        }
        ItemMigration looseItems = restoreRewrittenSingleplayerPlayer(root, extensions)
                .plus(migrateLooseInventories(root, extensions));
        convertedBuckets += looseItems.convertedBuckets();
        convertedItemAliases += looseItems.convertedItemAliases();
        convertedDamageValues += looseItems.convertedDamageValues();

        if (convertedChunks == 0 && convertedBuckets == 0
                && convertedItemAliases == 0 && convertedDamageValues == 0 && !looseItems.extensionChanged()) {
            Files.writeString(root.resolve(MARKER),
                    "No legacy Forge numeric chunks or filled buckets required Base Metals migration."
                            + System.lineSeparator(),
                    StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
            return false;
        }

        String result = "Converted " + convertedChunks + " legacy chunks in " + convertedRegions
                + " region files through Mojang's legacy flattening fixes. Backups: " + BACKUP_DIRECTORY
                + ". Third-party states restored: "
                + externalStates + ". Legacy block entities preserved: " + preservedBlockEntities
                + ". Legacy filled buckets converted: " + convertedBuckets
                + ". Legacy item aliases forwarded: " + convertedItemAliases
                + ". Mod-item damage values preserved: " + convertedDamageValues + "."
                + (looseItems.extensionChanged() ? " Companion player data migrated." : "");
        Files.writeString(root.resolve(MARKER), result + System.lineSeparator(), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE_NEW);
        BaseMetals.LOGGER.info(result);
        return true;
    }

    /**
     * The integrated server reads the root Player tag before
     * {@link net.neoforged.neoforge.event.server.ServerAboutToStartEvent}. Reload
     * that one tag from the corrected level.dat so the first upgraded
     * single-player session receives the same item migration as playerdata.
     */
    public static synchronized void refreshLoadedSingleplayerPlayer(MinecraftServer server, Path worldRoot) throws IOException {
        refreshLoadedSingleplayerPlayer(server.getWorldData().getLoadedPlayerTag(), worldRoot);
    }

    static synchronized void refreshLoadedSingleplayerPlayer(CompoundTag loaded, Path worldRoot) throws IOException {
        if (loaded == null) return;
        CompoundTag corrected;
        if (preparedIntegratedPlayer != null && worldRoot.toAbsolutePath().normalize().equals(preparedWorld)) {
            corrected = preparedIntegratedPlayer;
            preparedIntegratedPlayer = null;
        } else {
            CompoundTag level = NbtIo.readCompressed(worldRoot.resolve("level.dat"), NbtAccounter.unlimitedHeap());
            CompoundTag data = level.getCompound("Data");
            if (!data.contains("Player", Tag.TAG_COMPOUND)) return;
            corrected = DataFixTypes.PLAYER.updateToCurrentVersion(DataFixers.getDataFixer(),
                    data.getCompound("Player"), data.getInt("DataVersion"));
        }
        for (String key : List.copyOf(loaded.getAllKeys())) loaded.remove(key);
        loaded.merge(corrected);
        preparedPlayer = null;
    }

    private static Map<Integer, String> oldBlockIds(CompoundTag level) {
        CompoundTag registries = level.getCompound("FML").getCompound("Registries");
        ListTag ids = registries.getCompound("minecraft:blocks").getList("ids", Tag.TAG_COMPOUND);
        Map<Integer, String> result = new LinkedHashMap<>();
        for (Tag value : ids) {
            CompoundTag entry = (CompoundTag) value;
            result.put(entry.getInt("V"), entry.getString("K"));
        }
        return result;
    }

    private static Map<Integer, String> legacyBlockIds(Path root) throws IOException {
        for (String fileName : List.of("level.dat", "basemetals-legacy-registry.dat", "level.dat_old")) {
            Path file = root.resolve(fileName);
            if (!Files.isRegularFile(file)) continue;
            CompoundTag candidate = NbtIo.readCompressed(file, NbtAccounter.unlimitedHeap());
            int dataVersion = sourceDataVersion(candidate);
            if (dataVersion > LAST_SUPPORTED_LEGACY_DATA_VERSION
                    && !fileName.equals("basemetals-legacy-registry.dat")) continue;
            Map<Integer, String> ids = oldBlockIds(candidate);
            if (!ids.isEmpty()) return ids;
        }
        return embeddedBlockIds();
    }

    static synchronized Map<Integer, String> legacyBlockIdsForTest(Path root) throws IOException {
        if (!Files.isRegularFile(root.resolve("level.dat")) || !hasLegacySource(root)) return Map.of();
        return legacyBlockIds(root);
    }

    private static Map<Integer, String> embeddedBlockIds() throws IOException {
        String resource = "/data/basemetals/migration/legacy_block_ids_1_12.json";
        try (InputStream input = Legacy112WorldMigrator.class.getResourceAsStream(resource)) {
            if (input == null) throw new IOException("Missing packaged migration map " + resource);
            JsonObject ids = JsonParser.parseReader(new java.io.InputStreamReader(input, StandardCharsets.UTF_8))
                    .getAsJsonObject().getAsJsonObject("ids");
            Map<Integer, String> result = new LinkedHashMap<>();
            ids.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey(java.util.Comparator.comparingInt(Integer::parseInt)))
                    .forEach(entry -> result.put(Integer.parseInt(entry.getKey()), entry.getValue().getAsString()));
            return result;
        }
    }

    private static List<Path> findRegionFiles(Path root) throws IOException {
        List<Path> result = new ArrayList<>();
        try (var paths = Files.walk(root)) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().matches("r\\.-?\\d+\\.-?\\d+\\.mca"))
                    .filter(path -> path.getParent() != null && path.getParent().getFileName().toString().equals("region"))
                    .filter(path -> !path.startsWith(root.resolve(BACKUP_DIRECTORY)))
                    .sorted()
                    .forEach(result::add);
        }
        return result;
    }

    private static void ensureBackupCapacity(Path root, List<Path> regions) throws IOException {
        long bytesRequired = 0;
        Path backupRoot = root.resolve(BACKUP_DIRECTORY);
        for (Path region : regions) {
            Path backup = backupRoot.resolve(root.relativize(region));
            if (!Files.exists(backup)) bytesRequired += Files.size(region);
        }
        FileStore store = Files.getFileStore(root);
        if (store.getUsableSpace() < bytesRequired + 64L * 1_024L * 1_024L) {
            throw new IOException("Insufficient space for the Base Metals legacy region backup: need "
                    + bytesRequired + " bytes plus a 64 MiB safety margin");
        }
    }

    private static RegionConversion convertRegion(Path root, Path region, Map<Integer, String> ids,
            List<Legacy112MigrationExtension> extensions) throws IOException {
        byte[] source = Files.readAllBytes(region);
        if (source.length == 0) return new RegionConversion(0, 0, 0, 0, 0, 0);
        if (source.length < REGION_HEADER_BYTES) throw new IOException("Invalid region header: " + region);
        ByteBuffer locations = ByteBuffer.wrap(source, 0, SECTOR_BYTES).order(ByteOrder.BIG_ENDIAN);
        Map<ChunkPos, CompoundTag> updates = new LinkedHashMap<>();
        int converted = 0;
        int externalStates = 0;
        int preservedBlockEntities = 0;
        int convertedBuckets = 0;
        int convertedItemAliases = 0;
        int convertedDamageValues = 0;
        Set<Path> externalFiles = new java.util.LinkedHashSet<>();
        String[] coordinates = region.getFileName().toString().split("\\.");
        int regionX = Integer.parseInt(coordinates[1]);
        int regionZ = Integer.parseInt(coordinates[2]);

        // RegionFile opens read-write and pads on close. Inspect a disposable copy
        // so even a no-op scan cannot alter the source or its later backup.
        Path inspection = Files.createTempDirectory("basemetals-region-");
        try {
            Path copy = inspection.resolve(region.getFileName());
            Files.copy(region, copy);
            for (int index = 0; index < 1_024; index++) {
                int location = locations.getInt(index * 4);
                if (location == 0) continue;
                int offset = location >>> 8;
                int position = offset * SECTOR_BYTES;
                if (position < REGION_HEADER_BYTES || position + 5 > source.length) {
                    throw new IOException("Invalid chunk offset in " + region + " entry " + index);
                }
                int length = intAt(source, position);
                if (length < 1 || position + 4 + length > source.length) {
                    throw new IOException("Invalid chunk length in " + region + " entry " + index);
                }
                int compression = source[position + 4] & 0xff;
                ChunkPos chunkPos = new ChunkPos(regionX * 32 + (index & 31), regionZ * 32 + (index >>> 5));
                if ((compression & 128) != 0) {
                    Path external = region.getParent().resolve("c." + chunkPos.x + "." + chunkPos.z + ".mcc");
                    externalFiles.add(external);
                    Files.copy(external, inspection.resolve(external.getFileName()));
                }
            }
            try (RegionFile reader = new RegionFile(new RegionStorageInfo("Base Metals migration", Level.OVERWORLD, "chunk"),
                    copy, inspection, false)) {
                for (int index = 0; index < 1_024; index++) {
                    if (locations.getInt(index * 4) == 0) continue;
                    ChunkPos chunkPos = new ChunkPos(regionX * 32 + (index & 31), regionZ * 32 + (index >>> 5));
                    CompoundTag chunk;
                    try (DataInputStream input = reader.getChunkDataInputStream(chunkPos)) {
                        if (input == null) throw new IOException("Could not read chunk " + chunkPos + " in " + region);
                        chunk = NbtIo.read(input, NbtAccounter.unlimitedHeap());
                    }
                    ChunkConversion conversion = convertChunk(chunk, ids, extensions);
                    if (!conversion.converted()) continue;
                    updates.put(chunkPos, conversion.chunk());
                    converted++;
                    externalStates += conversion.externalStates();
                    preservedBlockEntities += conversion.preservedBlockEntities();
                    convertedBuckets += conversion.convertedBuckets();
                    convertedItemAliases += conversion.convertedItemAliases();
                    convertedDamageValues += conversion.convertedDamageValues();
                }
            }

            if (converted == 0) return new RegionConversion(0, 0, 0, 0, 0, 0);
            List<Path> originals = new ArrayList<>(externalFiles);
            originals.add(region);
            ensureBackupCapacity(root, originals);
            for (Path original : originals) {
                Path backup = root.resolve(BACKUP_DIRECTORY).resolve(root.relativize(original));
                Files.createDirectories(backup.getParent());
                if (!Files.exists(backup)) Files.copy(original, backup, StandardCopyOption.COPY_ATTRIBUTES);
            }

            // Let Minecraft handle compression, oversized .mcc records and their
            // stub/header updates rather than imposing an inline sector limit.
            try (RegionFile writer = new RegionFile(new RegionStorageInfo("Base Metals migration", Level.OVERWORLD, "chunk"),
                    region, region.getParent(), true)) {
                for (var entry : updates.entrySet()) {
                    try (DataOutputStream output = writer.getChunkDataOutputStream(entry.getKey())) {
                        NbtIo.write(entry.getValue(), output);
                    }
                }
            }
        } finally {
            try (var paths = Files.walk(inspection)) {
                for (Path path : paths.sorted(java.util.Comparator.reverseOrder()).toList()) Files.deleteIfExists(path);
            }
        }
        return new RegionConversion(converted, externalStates, preservedBlockEntities, convertedBuckets,
                convertedItemAliases, convertedDamageValues);
    }

    private static ChunkConversion convertChunk(CompoundTag chunk, Map<Integer, String> ids,
            List<Legacy112MigrationExtension> extensions) throws IOException {
        int sourceVersion = chunk.getInt("DataVersion");
        if (sourceVersion > LAST_SUPPORTED_LEGACY_DATA_VERSION) {
            return new ChunkConversion(false, 0, 0, 0, 0, 0, chunk);
        }

        ItemMigration itemMigration = convertLegacyItems(chunk);
        boolean extensionChanged = prepareExtensions(extensions, chunk, sourceVersion, Payload.CHUNK);
        CompoundTag level = chunk.getCompound("Level");
        ListTag sections = level.getList("Sections", Tag.TAG_COMPOUND);
        List<LegacyBlock> savedBlocks = new ArrayList<>();
        int externalStates = 0;
        for (Tag value : sections) {
            CompoundTag section = (CompoundTag) value;
            if (!section.contains("Blocks", Tag.TAG_BYTE_ARRAY)) continue;
            SectionSnapshot snapshot = snapshotSection(section, ids);
            savedBlocks.addAll(snapshot.blocks());
            externalStates += snapshot.externalStates();
        }
        if (savedBlocks.isEmpty() && itemMigration.isEmpty() && !extensionChanged) {
            return new ChunkConversion(false, 0, 0, 0, 0, 0, chunk);
        }

        for (Legacy112MigrationExtension extension : extensions) extension.beforeChunkFlattening(chunk);
        Map<Long, List<CompoundTag>> savedBlockEntities =
                snapshotLegacyBlockEntities(chunk.getCompound("Level"), savedBlocks);
        CompoundTag fixed = DataFixTypes.CHUNK.update(DataFixers.getDataFixer(),
                chunk, sourceVersion, PALETTED_PRE_PROTOCHUNK_DATA_VERSION);
        fixed.putInt("DataVersion", PALETTED_PRE_PROTOCHUNK_DATA_VERSION);
        if (!savedBlocks.isEmpty()) {
            restoreSavedBlocks(fixed, savedBlocks, extensions);
            restoreLegacyBlockEntities(fixed, savedBlocks, savedBlockEntities);
        }
        int preservedBlockEntities = savedBlockEntities.values().stream().mapToInt(List::size).sum();
        return new ChunkConversion(true, externalStates, preservedBlockEntities, itemMigration.convertedBuckets(),
                itemMigration.convertedItemAliases(), itemMigration.convertedDamageValues(), fixed);
    }

    static CompoundTag convertChunkForTest(CompoundTag chunk, Map<Integer, String> ids) throws IOException {
        return convertChunkForTest(chunk, ids, List.of());
    }

    static CompoundTag convertChunkForTest(CompoundTag chunk, Map<Integer, String> ids,
            List<Legacy112MigrationExtension> extensions) throws IOException {
        ChunkConversion conversion = convertChunk(chunk, ids, extensions);
        if (!conversion.converted()) throw new IOException("Validation chunk did not contain a third-party block");
        return conversion.chunk();
    }

    private static boolean prepareExtensions(List<Legacy112MigrationExtension> extensions, CompoundTag tag,
            int dataVersion, Payload payload) {
        if (dataVersion > LAST_SUPPORTED_LEGACY_DATA_VERSION) return false;
        boolean changed = false;
        for (Legacy112MigrationExtension extension : extensions) changed |= extension.prepareData(tag, dataVersion, payload);
        return changed;
    }

    private static ItemMigration migrateLooseInventories(Path root, List<Legacy112MigrationExtension> extensions)
            throws IOException {
        List<Path> files = new ArrayList<>();
        Path levelDat = root.resolve("level.dat");
        files.add(levelDat);
        Path playerData = root.resolve("playerdata");
        if (Files.isDirectory(playerData)) {
            try (var paths = Files.list(playerData)) {
                paths.filter(Files::isRegularFile)
                        .filter(path -> path.getFileName().toString().endsWith(".dat"))
                        .sorted()
                        .forEach(files::add);
            }
        }

        ItemMigration converted = ItemMigration.NONE;
        for (Path file : files) {
            if (!Files.isRegularFile(file)) continue;
            CompoundTag data = NbtIo.readCompressed(file, NbtAccounter.unlimitedHeap());
            ItemMigration changed = convertLegacyItems(data);
            boolean extensionChanged = false;
            if (file.equals(levelDat)) {
                CompoundTag world = data.getCompound("Data");
                if (root.equals(preparedWorld) && preparedPlayer != null && sourceDataVersion(data) < currentDataVersion()) {
                    CompoundTag player = preparedPlayer.copy();
                    ItemMigration originalItems = convertLegacyItems(player);
                    prepareExtensions(extensions, player, preparedVersion, Payload.PLAYER);
                    preparedIntegratedPlayer = flattenPreparedPlayer(player, preparedVersion, extensions).player();
                    converted = converted.plus(originalItems).withExtensionChange(true);
                } else if (!extensions.isEmpty() && isLegacyLevel(data) && world.contains("Player", Tag.TAG_COMPOUND)) {
                    CompoundTag player = world.getCompound("Player");
                    int version = sourceDataVersion(data);
                    extensionChanged = prepareExtensions(extensions, player, version, Payload.PLAYER);
                    if (root.equals(preparedWorld)) {
                        // Integrated startup can still have legacy level.dat on disk. Keep its
                        // version and layout intact; refresh only the already-loaded player.
                        PlayerConversion fixed = flattenPreparedPlayer(player, version, extensions);
                        preparedIntegratedPlayer = fixed.player();
                        converted = converted.withExtensionChange(fixed.extensionChanged());
                    }
                }
            } else {
                int version = data.getInt("DataVersion");
                extensionChanged = prepareExtensions(extensions, data, version, Payload.PLAYER);
                if (version <= LAST_SUPPORTED_LEGACY_DATA_VERSION) {
                    PlayerConversion fixed = flattenPreparedPlayer(data, version, extensions);
                    data = fixed.player();
                    data.putInt("DataVersion", currentDataVersion());
                    extensionChanged = true;
                }
            }
            if (changed.isEmpty() && !extensionChanged) continue;
            Path backup = root.resolve(BACKUP_DIRECTORY).resolve(root.relativize(file));
            Files.createDirectories(backup.getParent());
            if (!Files.exists(backup)) Files.copy(file, backup, StandardCopyOption.COPY_ATTRIBUTES);
            writeCompressedAtomically(file, data);
            converted = converted.plus(changed.withExtensionChange(extensionChanged));
        }
        return converted;
    }

    /** Restore the original player captured before rewriting, including crash recovery. */
    private static ItemMigration restoreRewrittenSingleplayerPlayer(Path root,
            List<Legacy112MigrationExtension> extensions) throws IOException {
        Path currentFile = root.resolve("level.dat");
        if (!root.equals(preparedWorld) || preparedPlayer == null || !Files.isRegularFile(currentFile)) {
            return ItemMigration.NONE;
        }

        CompoundTag current = NbtIo.readCompressed(currentFile, NbtAccounter.unlimitedHeap());
        CompoundTag currentData = current.getCompound("Data");
        if (sourceDataVersion(current) < currentDataVersion()) {
            return ItemMigration.NONE;
        }

        CompoundTag player = preparedPlayer.copy();
        ItemMigration migration = convertLegacyItems(player);
        boolean extensionChanged = prepareExtensions(extensions, player, preparedVersion, Payload.PLAYER);
        PlayerConversion fixed = flattenPreparedPlayer(player, preparedVersion, extensions);
        boolean playerChanged = !currentData.getCompound("Player").equals(fixed.player());
        currentData.put("Player", fixed.player());
        preparedIntegratedPlayer = fixed.player().copy();

        Path backup = root.resolve(BACKUP_DIRECTORY).resolve(root.relativize(currentFile));
        Files.createDirectories(backup.getParent());
        if (!Files.exists(backup)) Files.copy(currentFile, backup, StandardCopyOption.COPY_ATTRIBUTES);
        writeCompressedAtomically(currentFile, current);
        return migration.withExtensionChange(extensionChanged || fixed.extensionChanged() || playerChanged);
    }

    private static PlayerConversion flattenPreparedPlayer(CompoundTag player, int version,
            List<Legacy112MigrationExtension> extensions) {
        var fixer = DataFixers.getDataFixer();
        if (extensions.isEmpty()) {
            return new PlayerConversion(DataFixTypes.PLAYER.updateToCurrentVersion(fixer, player, version), false);
        }

        CompoundTag preparedSource = player.copy();
        if (version < NAMESPACED_BLOCK_ENTITY_DATA_VERSION) {
            player = DataFixTypes.PLAYER.update(fixer, player, version,
                    NAMESPACED_BLOCK_ENTITY_DATA_VERSION);
            version = NAMESPACED_BLOCK_ENTITY_DATA_VERSION;
        }

        // Give extensions the flattened 1.20.1 item shape so they can restore names before componentization.
        CompoundTag flattened = DataFixTypes.PLAYER.update(fixer, player, version, PRE_COMPONENT_PLAYER_DATA_VERSION);
        CompoundTag beforeCallbacks = flattened.copy();
        for (Legacy112MigrationExtension extension : extensions) {
            extension.afterPlayerFlattening(preparedSource, flattened);
        }
        boolean changed = !beforeCallbacks.equals(flattened);
        CompoundTag current = DataFixTypes.PLAYER.updateToCurrentVersion(fixer, flattened, PRE_COMPONENT_PLAYER_DATA_VERSION);
        return new PlayerConversion(current, changed);
    }

    private static int currentDataVersion() {
        return net.minecraft.SharedConstants.getCurrentVersion().getDataVersion().getVersion();
    }

    private static ItemMigration convertLegacyItems(Tag value) {
        int convertedBuckets = 0;
        int convertedItemAliases = 0;
        int convertedDamageValues = 0;
        if (value instanceof CompoundTag compound) {
            if (compound.getString("id").equalsIgnoreCase("forge:bucketfilled")
                    && compound.contains("tag", Tag.TAG_COMPOUND)) {
                CompoundTag itemTag = compound.getCompound("tag");
                String fluid = itemTag.getString("FluidName");
                String target = MissingMappings.fluidTargetPath(fluid);
                if (ModContent.fluids().containsKey(target)) {
                    compound.putString("id", BaseMetals.MOD_ID + ":" + target + "_bucket");
                    itemTag.remove("FluidName");
                    itemTag.remove("Amount");
                    if (itemTag.isEmpty()) compound.remove("tag");
                    convertedBuckets++;
                }
            } else {
                ResourceLocation oldId = ResourceLocation.tryParse(compound.getString("id"));
                if (oldId != null && (oldId.getNamespace().equals(BaseMetals.MOD_ID)
                        || oldId.getNamespace().equals("mmdlib"))
                        && compound.contains("Count", Tag.TAG_ANY_NUMERIC)) {
                    ResourceLocation targetId = MissingMappings.itemTargetId(oldId.getPath());
                    net.minecraft.world.item.Item item = BuiltInRegistries.ITEM.getOptional(targetId).orElse(null);
                    if (item != null && !oldId.equals(targetId)) {
                        compound.putString("id", targetId.toString());
                        convertedItemAliases++;
                    }
                    int damage = compound.getInt("Damage");
                    if (item != null && item.getDefaultInstance().isDamageableItem() && damage > 0
                            && compound.contains("Damage", Tag.TAG_ANY_NUMERIC)) {
                        CompoundTag itemTag = compound.contains("tag", Tag.TAG_COMPOUND)
                                ? compound.getCompound("tag") : new CompoundTag();
                        if (!itemTag.contains("Damage", Tag.TAG_ANY_NUMERIC)) {
                            itemTag.putInt("Damage", damage);
                            compound.put("tag", itemTag);
                            convertedDamageValues++;
                        }
                    }
                }
            }
            for (String key : List.copyOf(compound.getAllKeys())) {
                Tag child = compound.get(key);
                if (child != null) {
                    ItemMigration childMigration = convertLegacyItems(child);
                    convertedBuckets += childMigration.convertedBuckets();
                    convertedItemAliases += childMigration.convertedItemAliases();
                    convertedDamageValues += childMigration.convertedDamageValues();
                }
            }
        } else if (value instanceof ListTag list) {
            for (Tag child : list) {
                ItemMigration childMigration = convertLegacyItems(child);
                convertedBuckets += childMigration.convertedBuckets();
                convertedItemAliases += childMigration.convertedItemAliases();
                convertedDamageValues += childMigration.convertedDamageValues();
            }
        }
        return new ItemMigration(convertedBuckets, convertedItemAliases, convertedDamageValues);
    }

    private static void writeCompressedAtomically(Path file, CompoundTag data) throws IOException {
        Path temporary = file.resolveSibling(file.getFileName() + ".basemetals.tmp");
        NbtIo.writeCompressed(data, temporary);
        try (var channel = java.nio.channels.FileChannel.open(temporary, StandardOpenOption.WRITE)) {
            channel.force(true);
        }
        try {
            Files.move(temporary, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (java.nio.file.AtomicMoveNotSupportedException ignored) {
            Files.move(temporary, file, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    static CompoundTag stateAtForTest(CompoundTag chunk, int sectionY, int index) throws IOException {
        ListTag sections = chunk.getCompound("Level").getList("Sections", Tag.TAG_COMPOUND);
        for (Tag value : sections) {
            CompoundTag section = (CompoundTag) value;
            if (section.getByte("Y") != sectionY) continue;
            ListTag palette = section.getList("Palette", Tag.TAG_COMPOUND);
            int[] values = unpackBlockStates(section, palette.size());
            return ((CompoundTag) palette.get(values[index])).copy();
        }
        throw new IOException("Validation chunk has no section Y=" + sectionY);
    }

    private static SectionSnapshot snapshotSection(CompoundTag section, Map<Integer, String> ids)
            throws IOException {
        byte[] blocks = section.getByteArray("Blocks");
        byte[] metadata = section.getByteArray("Data");
        byte[] add = section.contains("Add", Tag.TAG_BYTE_ARRAY) ? section.getByteArray("Add") : null;
        int sectionY = section.getByte("Y");
        List<LegacyBlock> savedBlocks = new ArrayList<>();
        int externalStates = 0;

        for (int index = 0; index < 4_096; index++) {
            int blockId = Byte.toUnsignedInt(blocks[index]);
            if (add != null) blockId |= nibble(add, index) << 8;
            int meta = nibble(metadata, index);
            String name = ids.get(blockId);
            if (name == null) {
                throw new IOException("Legacy chunk uses numeric block ID " + blockId
                        + " which is absent from the saved legacy registry map and packaged fallback");
            }
            if (name.startsWith("minecraft:")) continue;
            savedBlocks.add(new LegacyBlock(sectionY, index, name, meta));
            if (!name.startsWith(BaseMetals.MOD_ID + ":") && !name.startsWith("mmdlib:")) externalStates++;
        }
        return new SectionSnapshot(savedBlocks, externalStates);
    }

    private static void restoreSavedBlocks(CompoundTag chunk, List<LegacyBlock> savedBlocks,
            List<Legacy112MigrationExtension> extensions) throws IOException {
        Map<Integer, CompoundTag> targetSections = new HashMap<>();
        ListTag sections = chunk.getCompound("Level").getList("Sections", Tag.TAG_COMPOUND);
        for (Tag value : sections) {
            CompoundTag section = (CompoundTag) value;
            targetSections.put((int) section.getByte("Y"), section);
        }

        Map<Long, LegacyBlock> byPosition = new HashMap<>();
        Map<Integer, List<LegacyBlock>> bySection = new LinkedHashMap<>();
        for (LegacyBlock block : savedBlocks) {
            byPosition.put(block.positionKey(), block);
            bySection.computeIfAbsent(block.sectionY(), ignored -> new ArrayList<>()).add(block);
        }

        for (Map.Entry<Integer, List<LegacyBlock>> entry : bySection.entrySet()) {
            CompoundTag section = targetSections.get(entry.getKey());
            if (section == null) {
                throw new IOException("Mojang's flattening data fix removed legacy section Y=" + entry.getKey());
            }
            ListTag palette = section.getList("Palette", Tag.TAG_COMPOUND);
            if (palette.isEmpty()) {
                throw new IOException("Mojang's flattening data fix produced an empty palette at section Y="
                        + entry.getKey());
            }
            int[] values = unpackBlockStates(section, palette.size());
            Map<CompoundTag, Integer> paletteIds = new LinkedHashMap<>();
            for (int index = 0; index < palette.size(); index++) {
                paletteIds.put(((CompoundTag) palette.get(index)).copy(), index);
            }

            for (LegacyBlock legacy : entry.getValue()) {
                CompoundTag state = restoredState(legacy, byPosition, extensions);
                Integer paletteId = paletteIds.get(state);
                if (paletteId == null) {
                    paletteId = palette.size();
                    paletteIds.put(state, paletteId);
                    palette.add(state);
                }
                values[legacy.index()] = paletteId;
            }
            section.put("Palette", palette);
            section.put("BlockStates", new LongArrayTag(packBlockStates(values, palette.size())));
        }
    }

    private static Map<Long, List<CompoundTag>> snapshotLegacyBlockEntities(
            CompoundTag level, List<LegacyBlock> savedBlocks) {
        Set<Long> savedPositions = new HashSet<>();
        for (LegacyBlock block : savedBlocks) savedPositions.add(block.positionKey());

        Map<Long, List<CompoundTag>> result = new LinkedHashMap<>();
        for (Tag value : level.getList("TileEntities", Tag.TAG_COMPOUND)) {
            CompoundTag blockEntity = (CompoundTag) value;
            long position = localPositionKey(blockEntity);
            if (!savedPositions.contains(position)) continue;
            result.computeIfAbsent(position, ignored -> new ArrayList<>()).add(blockEntity.copy());
        }
        return result;
    }

    private static void restoreLegacyBlockEntities(CompoundTag chunk, List<LegacyBlock> savedBlocks,
            Map<Long, List<CompoundTag>> savedBlockEntities) {
        Set<Long> savedPositions = new HashSet<>();
        for (LegacyBlock block : savedBlocks) savedPositions.add(block.positionKey());

        CompoundTag level = chunk.getCompound("Level");
        ListTag restored = new ListTag();
        for (Tag value : level.getList("TileEntities", Tag.TAG_COMPOUND)) {
            CompoundTag blockEntity = (CompoundTag) value;
            if (!savedPositions.contains(localPositionKey(blockEntity))) restored.add(blockEntity.copy());
        }
        for (List<CompoundTag> atPosition : savedBlockEntities.values()) {
            for (CompoundTag blockEntity : atPosition) restored.add(blockEntity.copy());
        }
        level.put("TileEntities", restored);
    }

    private static long localPositionKey(CompoundTag positioned) {
        return BlockPos.asLong(Math.floorMod(positioned.getInt("x"), 16),
                positioned.getInt("y"), Math.floorMod(positioned.getInt("z"), 16));
    }

    private static int[] unpackBlockStates(CompoundTag section, int paletteSize) throws IOException {
        int[] values = new int[4_096];
        long[] packed = section.getLongArray("BlockStates");
        if (packed.length == 0 && paletteSize == 1) return values;
        int bits = bitsForPalette(paletteSize);
        int requiredLongs = (int) Math.ceil(values.length * (double) bits / 64.0D);
        if (packed.length < requiredLongs) {
            throw new IOException("Flattened block-state array is shorter than its palette requires");
        }
        long mask = (1L << bits) - 1L;
        for (int index = 0; index < values.length; index++) {
            int bitIndex = index * bits;
            int longIndex = bitIndex >>> 6;
            int offset = bitIndex & 63;
            long value = packed[longIndex] >>> offset;
            if (offset + bits > 64) value |= packed[longIndex + 1] << (64 - offset);
            values[index] = (int) (value & mask);
            if (values[index] >= paletteSize) {
                throw new IOException("Flattened block-state index is outside its palette");
            }
        }
        return values;
    }

    private static long[] packBlockStates(int[] values, int paletteSize) {
        int bits = bitsForPalette(paletteSize);
        long[] packed = new long[(int) Math.ceil(values.length * (double) bits / 64.0D)];
        long mask = (1L << bits) - 1L;
        for (int index = 0; index < values.length; index++) {
            int bitIndex = index * bits;
            int longIndex = bitIndex >>> 6;
            int offset = bitIndex & 63;
            long value = values[index] & mask;
            packed[longIndex] |= value << offset;
            if (offset + bits > 64) packed[longIndex + 1] |= value >>> (64 - offset);
        }
        return packed;
    }

    private static int bitsForPalette(int paletteSize) {
        return Math.max(4, 32 - Integer.numberOfLeadingZeros(Math.max(1, paletteSize - 1)));
    }

    private static CompoundTag restoredState(LegacyBlock legacy, Map<Long, LegacyBlock> byPosition,
            List<Legacy112MigrationExtension> extensions) throws IOException {
        String normalizedName = normalizeLegacyRegistryName(legacy.name());
        ResourceLocation oldId = ResourceLocation.tryParse(normalizedName);
        if (oldId == null) throw new IOException("Invalid legacy block registry name " + legacy.name());
        if (oldId.getNamespace().equals(BaseMetals.MOD_ID) || oldId.getNamespace().equals("mmdlib")) {
            String path = MissingMappings.blockTargetPath(oldId.getPath());
            ResourceLocation targetId = ResourceLocation.fromNamespaceAndPath(BaseMetals.MOD_ID, path);
            Block block = BuiltInRegistries.BLOCK.getOptional(targetId).orElse(null);
            if (block == null) throw new IOException("No 1.18 block exists for legacy block " + legacy.name());
            BlockState state = stateForLegacyMetadata(block, legacy.metadata());
            if (block instanceof DoorBlock) {
                LegacyBlock lower = (legacy.metadata() & 8) == 0
                        ? legacy : byPosition.get(legacy.offsetPositionKey(-1));
                LegacyBlock upper = (legacy.metadata() & 8) != 0
                        ? legacy : byPosition.get(legacy.offsetPositionKey(1));
                if (sameTargetBlock(lower, path) && sameTargetBlock(upper, path)) {
                    BlockState lowerState = stateForLegacyMetadata(block, lower.metadata());
                    BlockState upperState = stateForLegacyMetadata(block, upper.metadata());
                    state = state.setValue(DoorBlock.FACING, lowerState.getValue(DoorBlock.FACING))
                            .setValue(DoorBlock.OPEN, lowerState.getValue(DoorBlock.OPEN))
                            .setValue(DoorBlock.HINGE, upperState.getValue(DoorBlock.HINGE))
                            .setValue(DoorBlock.POWERED, upperState.getValue(DoorBlock.POWERED));
                }
            }
            return NbtUtils.writeBlockState(state);
        }

        BlockState decoded = null;
        for (Legacy112MigrationExtension extension : extensions) {
            BlockState state = extension.decodeBlockState(oldId, legacy.metadata());
            if (state == null) continue;
            if (decoded != null || state.isAir()) {
                throw new IOException("Unsafe or ambiguous migration state for legacy block " + oldId);
            }
            decoded = state;
        }
        if (decoded != null) return NbtUtils.writeBlockState(decoded);

        CompoundTag state = new CompoundTag();
        state.putString("Name", normalizedName);
        return state;
    }

    private static boolean sameTargetBlock(LegacyBlock block, String targetPath) {
        if (block == null) return false;
        ResourceLocation id = ResourceLocation.tryParse(normalizeLegacyRegistryName(block.name()));
        return id != null
                && (id.getNamespace().equals(BaseMetals.MOD_ID) || id.getNamespace().equals("mmdlib"))
                && MissingMappings.blockTargetPath(id.getPath()).equals(targetPath);
    }

    static String normalizeLegacyRegistryName(String name) {
        return name.indexOf(':') >= 0 ? name.toLowerCase(Locale.ROOT) : name;
    }

    /** Maps Base Metals' persisted 1.12 metadata onto its 1.18 state schema. */
    public static BlockState stateForLegacyMetadata(Block block, int metadata) {
        BlockState state = block.defaultBlockState();
        if (block instanceof LiquidBlock) {
            return state.setValue(LiquidBlock.LEVEL, metadata & 15);
        }
        if (block instanceof PlateBlock) {
            return state.setValue(PlateBlock.FACING, Direction.from3DDataValue(metadata));
        }
        if (block instanceof CompatibilityDoubleSlabBlock) {
            return state.setValue(SlabBlock.TYPE, SlabType.DOUBLE).setValue(SlabBlock.WATERLOGGED, false);
        }
        if (block instanceof MaterialAnvilBlock) {
            return state.setValue(MaterialAnvilBlock.FACING, Direction.from2DDataValue(metadata & 3))
                    .setValue(MaterialAnvilBlock.DAMAGE, Math.min(2, (metadata & 15) >> 2));
        }
        if (block instanceof DoorBlock) {
            if ((metadata & 8) != 0) {
                return state.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER)
                        .setValue(DoorBlock.HINGE, (metadata & 1) != 0 ? DoorHingeSide.RIGHT : DoorHingeSide.LEFT)
                        .setValue(DoorBlock.POWERED, (metadata & 2) != 0);
            }
            return state.setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER)
                    .setValue(DoorBlock.FACING, Direction.from2DDataValue(metadata & 3).getCounterClockWise())
                    .setValue(DoorBlock.OPEN, (metadata & 4) != 0);
        }
        if (block instanceof TrapDoorBlock) {
            Direction facing = switch (metadata & 3) {
                case 0 -> Direction.NORTH;
                case 1 -> Direction.SOUTH;
                case 2 -> Direction.WEST;
                default -> Direction.EAST;
            };
            return state.setValue(TrapDoorBlock.FACING, facing)
                    .setValue(TrapDoorBlock.OPEN, (metadata & 4) != 0)
                    .setValue(TrapDoorBlock.HALF, (metadata & 8) == 0 ? Half.BOTTOM : Half.TOP)
                    .setValue(TrapDoorBlock.WATERLOGGED, false);
        }
        if (block instanceof ButtonBlock) {
            Direction direction = legacyButtonDirection(metadata & 7);
            return state.setValue(ButtonBlock.FACE, attachFace(direction))
                    .setValue(ButtonBlock.FACING, horizontalAttachmentDirection(direction, metadata & 7))
                    .setValue(ButtonBlock.POWERED, (metadata & 8) != 0);
        }
        if (block instanceof LeverBlock) {
            return LegacyWorldDataHook.legacyState(block, "", metadata);
        }
        if (block instanceof PressurePlateBlock) {
            return state.setValue(PressurePlateBlock.POWERED, metadata == 1);
        }
        if (block instanceof SlabBlock) {
            return state.setValue(SlabBlock.TYPE, (metadata & 8) == 0 ? SlabType.BOTTOM : SlabType.TOP)
                    .setValue(SlabBlock.WATERLOGGED, false);
        }
        if (block instanceof StairBlock) {
            return state.setValue(StairBlock.HALF, (metadata & 4) == 0 ? Half.BOTTOM : Half.TOP)
                    .setValue(StairBlock.FACING, Direction.from3DDataValue(5 - (metadata & 3)))
                    .setValue(StairBlock.SHAPE, StairsShape.STRAIGHT)
                    .setValue(StairBlock.WATERLOGGED, false);
        }
        return state;
    }

    /** Runtime fixture comparison for the persisted properties represented by 1.12 metadata. */
    public static boolean matchesLegacyMetadata(BlockState actual, int metadata) {
        Block block = actual.getBlock();
        BlockState expected = stateForLegacyMetadata(block, metadata);
        if (block instanceof DoorBlock) {
            if ((metadata & 8) != 0) {
                return actual.getValue(DoorBlock.HALF) == expected.getValue(DoorBlock.HALF)
                        && actual.getValue(DoorBlock.HINGE) == expected.getValue(DoorBlock.HINGE)
                        && actual.getValue(DoorBlock.POWERED) == expected.getValue(DoorBlock.POWERED);
            }
            return actual.getValue(DoorBlock.HALF) == expected.getValue(DoorBlock.HALF)
                    && actual.getValue(DoorBlock.FACING) == expected.getValue(DoorBlock.FACING)
                    && actual.getValue(DoorBlock.OPEN) == expected.getValue(DoorBlock.OPEN);
        }
        if (block instanceof ButtonBlock || block instanceof PressurePlateBlock || block instanceof LiquidBlock) {
            // These states legitimately change while spawn chunks prepare.
            return true;
        }
        return actual.equals(expected);
    }

    private static Direction legacyButtonDirection(int orientation) {
        return switch (orientation) {
            case 0 -> Direction.DOWN;
            case 1 -> Direction.EAST;
            case 2 -> Direction.WEST;
            case 3 -> Direction.SOUTH;
            case 4 -> Direction.NORTH;
            default -> Direction.UP;
        };
    }

    private static AttachFace attachFace(Direction direction) {
        return direction == Direction.DOWN ? AttachFace.CEILING
                : direction == Direction.UP ? AttachFace.FLOOR : AttachFace.WALL;
    }

    private static Direction horizontalAttachmentDirection(Direction direction, int orientation) {
        if (direction.getAxis().isHorizontal()) return direction;
        return switch (orientation) {
            case 0, 6 -> Direction.EAST;
            default -> Direction.SOUTH;
        };
    }

    private static int nibble(byte[] values, int index) {
        if (values.length == 0) return 0;
        int value = Byte.toUnsignedInt(values[index >>> 1]);
        return value >>> ((index & 1) * 4) & 15;
    }

    private static int intAt(byte[] bytes, int position) {
        return ByteBuffer.wrap(bytes, position, 4).order(ByteOrder.BIG_ENDIAN).getInt();
    }

    private record RegionConversion(int convertedChunks, int externalStates, int preservedBlockEntities,
            int convertedBuckets,
            int convertedItemAliases, int convertedDamageValues) {}
    private record ChunkConversion(boolean converted, int externalStates, int preservedBlockEntities,
            int convertedBuckets,
            int convertedItemAliases, int convertedDamageValues, CompoundTag chunk) {}
    private record PlayerConversion(CompoundTag player, boolean extensionChanged) {}
    private record ItemMigration(int convertedBuckets, int convertedItemAliases, int convertedDamageValues,
            boolean extensionChanged) {
        private static final ItemMigration NONE = new ItemMigration(0, 0, 0);

        private ItemMigration(int convertedBuckets, int convertedItemAliases, int convertedDamageValues) {
            this(convertedBuckets, convertedItemAliases, convertedDamageValues, false);
        }

        private boolean isEmpty() {
            return convertedBuckets == 0 && convertedItemAliases == 0 && convertedDamageValues == 0 && !extensionChanged;
        }

        private ItemMigration withExtensionChange(boolean changed) {
            return changed && !extensionChanged
                    ? new ItemMigration(convertedBuckets, convertedItemAliases, convertedDamageValues, true) : this;
        }

        private ItemMigration plus(ItemMigration other) {
            return new ItemMigration(convertedBuckets + other.convertedBuckets,
                    convertedItemAliases + other.convertedItemAliases,
                    convertedDamageValues + other.convertedDamageValues, extensionChanged || other.extensionChanged);
        }
    }
    private record SectionSnapshot(List<LegacyBlock> blocks, int externalStates) {}
    private record LegacyBlock(int sectionY, int index, String name, int metadata) {
        private int x() {
            return index & 15;
        }

        private int y() {
            return sectionY * 16 + (index >>> 8);
        }

        private int z() {
            return index >>> 4 & 15;
        }

        private long positionKey() {
            return BlockPos.asLong(x(), y(), z());
        }

        private long offsetPositionKey(int yOffset) {
            return BlockPos.asLong(x(), y() + yOffset, z());
        }
    }
}
