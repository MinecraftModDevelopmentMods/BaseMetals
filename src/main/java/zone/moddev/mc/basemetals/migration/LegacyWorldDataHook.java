package zone.moddev.mc.basemetals.migration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.Dynamic;

import cpw.mods.modlauncher.api.INameMappingService;
import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.MissingMappings;
import zone.moddev.mc.basemetals.content.BaseMetalAnvilBlock;
import zone.moddev.mc.basemetals.content.CompatibilityDoubleSlabBlock;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.content.PlateBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.fixes.BlockStateData;
import net.minecraft.world.level.storage.LevelStorageSource.LevelStorageAccess;
import net.minecraft.world.level.storage.WorldData;
import net.minecraftforge.fmllegacy.WorldPersistenceHooks;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Restores old modded block IDs before Minecraft's 1.13 world conversion can
 * replace them with air. Also converts old item names, universal buckets and
 * durability data before Minecraft reads the items.
 */
public final class LegacyWorldDataHook implements WorldPersistenceHooks.WorldPersistenceHook {
    private static final LegacyWorldDataHook INSTANCE = new LegacyWorldDataHook();
    private static final ResourceLocation BLOCK_REGISTRY = new ResourceLocation("minecraft", "blocks");
    private static final Map<String, CompoundTag> LEGACY_WORLD_DATA = new ConcurrentHashMap<String, CompoundTag>();
    private static final BitSet LEGACY_BASE_METALS_BLOCK_IDS = new BitSet();
    private static volatile Map<ResourceLocation, LongSet> legacyTerrainChunks = Collections.emptyMap();
    private static final Map<Long, Integer> LEGACY_BASE_METALS_BLOCK_COUNTS =
            new ConcurrentHashMap<Long, Integer>();
    private static final Map<String, String> VANILLA_BLOCK_ENTITY_IDS = vanillaBlockEntityIds();
    private static final String PRESERVE_CHUNK_MARKER = "BaseMetalsLegacyPreserveChunk";
    private static volatile boolean legacyWorldActive;
    private static boolean registered;

    private LegacyWorldDataHook() {}

    public static synchronized void register() {
        if (!registered) {
            WorldPersistenceHooks.addHook(INSTANCE);
            registered = true;
        }
    }

    /** Called by the coremod before Forge reads level.dat. */
    public static synchronized void prepareLegacyWorld(File levelDat) {
        legacyWorldActive = false;
        legacyTerrainChunks = Collections.emptyMap();
        LEGACY_BASE_METALS_BLOCK_COUNTS.clear();
        removeRetiredProfessionSnapshot(levelDat);
        File source = legacyRegistrySource(levelDat);
        if (source == null) return;
        try (FileInputStream input = new FileInputStream(source)) {
            CompoundTag root = NbtIo.readCompressed(input);
            if (!root.contains("FML", 10)) return;
            prepareLegacyData(levelDat.getParentFile(), root.getCompound("FML"));
            migrateLoosePlayerData(levelDat, root);
        } catch (IOException exception) {
            BaseMetals.LOGGER.warn("Could not inspect '{}' for legacy Base Metals registry data", source, exception);
        }
    }

    private static void removeRetiredProfessionSnapshot(File levelDat) {
        if (!levelDat.isFile()) return;
        try {
            CompoundTag root;
            try (FileInputStream input = new FileInputStream(levelDat)) {
                root = NbtIo.readCompressed(input);
            }
            if (removeRetiredVanillaProfessionRegistry(root)) writeWithBackup(levelDat, root);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not prepare the old villager registry in " + levelDat, exception);
        }
    }

    static boolean removeRetiredVanillaProfessionRegistry(CompoundTag root) {
        boolean changed = false;
        for (String namespace : new String[] {"fml", "FML"}) {
            CompoundTag registries = root.getCompound(namespace).getCompound("Registries");
            String key = "minecraft:villagerprofessions";
            if (!registries.contains(key, 10)) continue;
            CompoundTag snapshot = registries.getCompound(key);
            if (!snapshot.contains("ids", 9)) continue;
            boolean vanillaOnly = true;
            for (Tag entry : (ListTag) snapshot.get("ids")) {
                if (!(entry instanceof CompoundTag)) {
                    vanillaOnly = false;
                    continue;
                }
                String name = ((CompoundTag) entry).getString("K");
                if (!Arrays.asList("minecraft:farmer", "minecraft:librarian", "minecraft:priest",
                        "minecraft:smith", "minecraft:butcher", "minecraft:nitwit").contains(name)) {
                    vanillaOnly = false;
                }
            }
            for (String list : new String[] {"aliases", "overrides", "dummied"}) {
                if (snapshot.contains(list, 9) && !((ListTag) snapshot.get(list)).isEmpty()) vanillaOnly = false;
            }
            if (snapshot.getIntArray("blocked").length > 0) vanillaOnly = false;
            if (!vanillaOnly) continue;

            // Vanilla converts the villagers themselves. This retired Forge registry
            // is only a saved ID index, not the villagers or their trade offers.
            registries.remove(key);
            changed = true;
        }
        return changed;
    }

    /** Called by the coremod immediately after a legacy chunk NBT is read. */
    public static synchronized void prepareLegacyChunk(CompoundTag root) {
        if (root == null) return;
        migrateLegacyItems(root);
        migrateWallStates(root);
        if (!legacyWorldActive || !root.contains("Level", 10)) return;
        CompoundTag level = root.getCompound("Level");
        int legacyBlocks = countLegacyBaseMetalsBlocks(level);
        if (legacyBlocks > 0) {
            LEGACY_BASE_METALS_BLOCK_COUNTS.put(Long.valueOf(chunkKey(level.getInt("xPos"), level.getInt("zPos"))),
                    Integer.valueOf(legacyBlocks));
        }
        // Chests or entities may contain the only Base Metals items in this chunk.
        // Mark it as finished terrain too, so conversion cannot regenerate it and lose those items.
        level.putBoolean("TerrainPopulated", true);
        level.putBoolean("LightPopulated", true);
        level.putBoolean(PRESERVE_CHUNK_MARKER, true);
    }

    /** Old wall connections were booleans; 1.16 expects none, low or tall. */
    static int migrateWallStates(Tag value) {
        int changed = 0;
        if (value instanceof CompoundTag) {
            CompoundTag compound = (CompoundTag) value;
            String name = compound.getString("Name");
            if (name.startsWith("basemetals:") && name.endsWith("_wall")) {
                CompoundTag properties = compound.getCompound("Properties");
                for (String direction : new String[] {"north", "east", "south", "west"}) {
                    String connection = properties.getString(direction);
                    if ("true".equals(connection) || "false".equals(connection)) {
                        properties.putString(direction, "true".equals(connection) ? "low" : "none");
                        changed++;
                    }
                }
            }
            for (String key : compound.getAllKeys()) changed += migrateWallStates(compound.get(key));
        } else if (value instanceof ListTag) {
            for (Tag entry : (ListTag) value) changed += migrateWallStates(entry);
        }
        return changed;
    }

    /** Called after vanilla datafixing, before a legacy chunk is returned. */
    public static CompoundTag finalizeLegacyChunk(CompoundTag root) {
        if (root != null && root.contains("Level", 10)) {
            CompoundTag level = root.getCompound("Level");
            if (level.getBoolean(PRESERVE_CHUNK_MARKER)) {
                removePhantomBedEntities(level);
                level.putString("Status", "full");
                level.remove(PRESERVE_CHUNK_MARKER);
            }
        }
        return root;
    }

    static int removePhantomBedEntities(CompoundTag level) {
        ListTag entities = level.getList("TileEntities", 10);
        int removed = 0;

        for (int index = entities.size() - 1; index >= 0; index--) {
            CompoundTag entity = entities.getCompound(index);
            if (!"minecraft:bed".equals(entity.getString("id"))) continue;

            // Vanilla's pre-flattening bed fixer ignores the high bits of modded block IDs.
            // A bed entity cannot belong to a Base Metals block; real beds are left alone.
            String block = paletteBlockAt(level, entity.getInt("x"), entity.getInt("y"), entity.getInt("z"));
            if (block.startsWith("basemetals:")) {
                entities.remove(index);
                removed++;
            }
        }

        return removed;
    }

    private static String paletteBlockAt(CompoundTag level, int x, int y, int z) {
        for (Tag entry : level.getList("Sections", 10)) {
            CompoundTag section = (CompoundTag) entry;
            if (section.getByte("Y") != (y >> 4)) continue;

            ListTag palette = section.getList("Palette", 10);
            if (palette.isEmpty()) return "";
            int state = 0;
            if (palette.size() > 1) {
                int bits = Math.max(4, 32 - Integer.numberOfLeadingZeros(palette.size() - 1));
                int perWord = 64 / bits;
                int position = ((y & 15) << 8) | ((z & 15) << 4) | (x & 15);
                long[] states = section.getLongArray("BlockStates");
                int word = position / perWord;
                if (word >= states.length) return "";
                state = (int) ((states[word] >>> ((position % perWord) * bits)) & ((1L << bits) - 1));
            }
            return state < palette.size() ? palette.getCompound(state).getString("Name") : "";
        }
        return "";
    }

    public static boolean shouldBlockWorldgenWrite(net.minecraft.server.level.WorldGenRegion region,
            net.minecraft.core.BlockPos position) {
        return legacyWorldActive && position != null
                && isProtectedChunk(region.getLevel().dimension().location(), position);
    }

    static boolean isProtectedChunk(ResourceLocation dimension, net.minecraft.core.BlockPos position) {
        LongSet chunks = legacyTerrainChunks.get(dimension);
        return chunks != null && chunks.contains(chunkKey(position.getX() >> 4, position.getZ() >> 4));
    }

    @Override public String getModId() { return "FML"; }

    @Override
    public CompoundTag getDataForWriting(LevelStorageAccess handler, WorldData info) {
        CompoundTag legacy = LEGACY_WORLD_DATA.get(worldKey(handler.getWorldDir().toFile()));
        return legacy == null ? new CompoundTag() : legacy.copy();
    }

    @Override
    public void readData(LevelStorageAccess handler, WorldData info, CompoundTag tag) {
        prepareLegacyData(handler.getWorldDir().toFile(), tag);
    }

    private static synchronized void prepareLegacyData(File worldDirectory, CompoundTag tag) {
        if (!tag.contains("Registries", 10)) return;
        CompoundTag registries = tag.getCompound("Registries");
        if (!registries.contains(BLOCK_REGISTRY.toString(), 10)) return;
        String key = worldKey(worldDirectory);
        boolean first = !LEGACY_WORLD_DATA.containsKey(key);
        int states = installLegacyBlockStates(registries.getCompound(BLOCK_REGISTRY.toString()));
        if (first) LEGACY_WORLD_DATA.put(key, tag.copy());
        legacyWorldActive = states > 0;
        int chunks = indexLegacyChunks(worldDirectory);
        if (first && states > 0) {
            BaseMetals.LOGGER.info("Prepared {} legacy Base Metals states and protected {} existing chunks in '{}'",
                    Integer.valueOf(states), Integer.valueOf(chunks), worldDirectory);
        }
    }

    private static int installLegacyBlockStates(CompoundTag snapshot) {
        LEGACY_BASE_METALS_BLOCK_IDS.clear();
        Map<ResourceLocation, Integer> ids = new LinkedHashMap<ResourceLocation, Integer>();
        ListTag savedIds = snapshot.getList("ids", 10);
        int highestState = 0;
        for (int index = 0; index < savedIds.size(); index++) {
            CompoundTag entry = savedIds.getCompound(index);
            String normalized = normalizeLegacyRegistryName(entry.getString("K"));
            int separator = normalized.indexOf(':');
            if (separator < 0) continue;
            String namespace = normalized.substring(0, separator);
            if (!BaseMetals.MOD_ID.equals(namespace) && !"mmdlib".equals(namespace)) continue;
            ResourceLocation id = new ResourceLocation(normalized);
            int numeric = entry.getInt("V");
            ids.put(id, Integer.valueOf(numeric));
            LEGACY_BASE_METALS_BLOCK_IDS.set(numeric);
            highestState = Math.max(highestState, (numeric << 4) | 15);
        }
        if (ids.isEmpty()) return 0;
        Dynamic<?>[] table = expandFlatteningTable(highestState + 1);
        int mapped = 0;
        for (Map.Entry<ResourceLocation, Integer> entry : ids.entrySet()) {
            Block block = resolveCurrentBlock(entry.getKey());
            for (int meta = 0; meta < 16; meta++) {
                BlockState state = legacyState(block, entry.getKey().getPath(), meta);
                table[(entry.getValue().intValue() << 4) | meta] =
                        BlockStateData.parse(NbtUtils.writeBlockState(state).toString());
                mapped++;
            }
        }
        return mapped;
    }

    private static Block resolveCurrentBlock(ResourceLocation oldId) {
        String path = MissingMappings.blockTargetPath(oldId.getPath());
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(BaseMetals.MOD_ID, path));
        if (block == null) throw new IllegalStateException("No Base Metals replacement for legacy block " + oldId);
        return block;
    }

    static BlockState legacyState(Block block, String path, int meta) {
        BlockState state = block.defaultBlockState();
        if (block instanceof PlateBlock) return state.setValue(PlateBlock.FACING, Direction.from3DDataValue(meta));
        if (block instanceof CompatibilityDoubleSlabBlock) return state.setValue(SlabBlock.TYPE, SlabType.DOUBLE);
        if (block instanceof SlabBlock) return state.setValue(SlabBlock.TYPE,
                (meta & 8) == 0 ? SlabType.BOTTOM : SlabType.TOP);
        if (block instanceof StairBlock) {
            Direction facing = Direction.from3DDataValue(5 - (meta & 3));
            return state.setValue(StairBlock.FACING, facing)
                    .setValue(StairBlock.HALF, (meta & 4) == 0 ? Half.BOTTOM : Half.TOP)
                    .setValue(StairBlock.SHAPE, StairsShape.STRAIGHT)
                    .setValue(StairBlock.WATERLOGGED, Boolean.FALSE);
        }
        if (block instanceof DoorBlock) {
            if ((meta & 8) != 0) {
                return state.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER)
                        .setValue(DoorBlock.HINGE, (meta & 1) != 0 ? DoorHingeSide.RIGHT : DoorHingeSide.LEFT)
                        .setValue(DoorBlock.POWERED, Boolean.valueOf((meta & 2) != 0));
            }
            return state.setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER)
                    .setValue(DoorBlock.FACING, Direction.from2DDataValue(meta & 3).getCounterClockWise())
                    .setValue(DoorBlock.OPEN, Boolean.valueOf((meta & 4) != 0));
        }
        if (block instanceof TrapDoorBlock) {
            Direction[] facing = { Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST };
            return state.setValue(HorizontalDirectionalBlock.FACING, facing[meta & 3])
                    .setValue(TrapDoorBlock.OPEN, Boolean.valueOf((meta & 4) != 0))
                    .setValue(TrapDoorBlock.HALF, (meta & 8) == 0 ? Half.BOTTOM : Half.TOP)
                    .setValue(TrapDoorBlock.POWERED, Boolean.FALSE)
                    .setValue(TrapDoorBlock.WATERLOGGED, Boolean.FALSE);
        }
        if (block instanceof BaseMetalAnvilBlock) {
            return state.setValue(AnvilBlock.FACING, Direction.from2DDataValue(meta & 3))
                    .setValue(BaseMetalAnvilBlock.DAMAGE, Integer.valueOf(Math.min(2, (meta & 15) >> 2)));
        }
        if (block instanceof ButtonBlock) return attachedState(state, meta, ButtonBlock.POWERED);
        if (block instanceof LeverBlock) return legacyLeverState(state, meta);
        if (block instanceof PressurePlateBlock) return state.setValue(PressurePlateBlock.POWERED,
                Boolean.valueOf(meta > 0));
        if (block instanceof LiquidBlock) return state.setValue(LiquidBlock.LEVEL,
                Integer.valueOf(Math.min(15, meta)));
        return state;
    }

    private static BlockState attachedState(BlockState state, int meta,
            net.minecraft.world.level.block.state.properties.BooleanProperty powered) {
        Direction oldFacing;
        switch (meta & 7) {
            case 0: oldFacing = Direction.DOWN; break;
            case 1: oldFacing = Direction.EAST; break;
            case 2: oldFacing = Direction.WEST; break;
            case 3: oldFacing = Direction.SOUTH; break;
            case 4: oldFacing = Direction.NORTH; break;
            default: oldFacing = Direction.UP;
        }
        AttachFace face = oldFacing == Direction.DOWN ? AttachFace.CEILING
                : oldFacing == Direction.UP ? AttachFace.FLOOR : AttachFace.WALL;
        Direction horizontal = oldFacing.getAxis().isHorizontal() ? oldFacing : Direction.NORTH;
        return state.setValue(FaceAttachedHorizontalDirectionalBlock.FACE, face)
                .setValue(HorizontalDirectionalBlock.FACING, horizontal)
                .setValue(powered, Boolean.valueOf((meta & 8) != 0));
    }

    private static BlockState legacyLeverState(BlockState state, int meta) {
        int orientation = meta & 7;
        AttachFace face;
        Direction horizontal;
        if (orientation == 0 || orientation == 7) {
            face = AttachFace.CEILING;
            horizontal = orientation == 0 ? Direction.EAST : Direction.NORTH;
        } else if (orientation == 5 || orientation == 6) {
            face = AttachFace.FLOOR;
            horizontal = orientation == 6 ? Direction.EAST : Direction.NORTH;
        } else {
            face = AttachFace.WALL;
            horizontal = new Direction[] { Direction.NORTH, Direction.EAST, Direction.WEST,
                    Direction.SOUTH, Direction.NORTH }[orientation];
        }
        return state.setValue(FaceAttachedHorizontalDirectionalBlock.FACE, face)
                .setValue(HorizontalDirectionalBlock.FACING, horizontal)
                .setValue(LeverBlock.POWERED, Boolean.valueOf((meta & 8) != 0));
    }

    static Map<Integer, String> legacyBlockIdsForTest(java.nio.file.Path root) throws IOException {
        File source = legacyRegistrySource(root.resolve("level.dat").toFile());
        if (source != null) {
            try (FileInputStream input = new FileInputStream(source)) {
                Map<Integer, String> result = idsFromRoot(NbtIo.readCompressed(input));
                if (!result.isEmpty()) return result;
            }
        }
        return embeddedBlockIds();
    }

    private static Map<Integer, String> idsFromRoot(CompoundTag root) {
        Map<Integer, String> result = new LinkedHashMap<Integer, String>();
        ListTag ids = root.getCompound("FML").getCompound("Registries")
                .getCompound(BLOCK_REGISTRY.toString()).getList("ids", 10);
        for (int index = 0; index < ids.size(); index++) {
            CompoundTag entry = ids.getCompound(index);
            result.put(Integer.valueOf(entry.getInt("V")), normalizeLegacyRegistryName(entry.getString("K")));
        }
        return result;
    }

    private static Map<Integer, String> embeddedBlockIds() throws IOException {
        InputStream input = LegacyWorldDataHook.class.getResourceAsStream(
                "/data/basemetals/migration/legacy_block_ids_1_12.json");
        if (input == null) throw new IOException("Missing packaged legacy registry map");
        try (java.io.Reader reader = new java.io.InputStreamReader(input, StandardCharsets.UTF_8)) {
            JsonObject ids = new JsonParser().parse(reader).getAsJsonObject().getAsJsonObject("ids");
            List<Integer> keys = new ArrayList<Integer>();
            for (Map.Entry<String, JsonElement> entry : ids.entrySet()) keys.add(Integer.valueOf(entry.getKey()));
            Collections.sort(keys);
            Map<Integer, String> result = new LinkedHashMap<Integer, String>();
            for (Integer key : keys) result.put(key, normalizeLegacyRegistryName(ids.get(key.toString()).getAsString()));
            return result;
        }
    }

    public static String normalizeLegacyRegistryName(String name) {
        if (name == null) return "";
        String value = name.trim().toLowerCase(Locale.ROOT);
        return value.indexOf(':') < 0 ? BaseMetals.MOD_ID + ":" + value : value;
    }

    private static void migrateLoosePlayerData(File levelDat, CompoundTag root) throws IOException {
        boolean changed = migrateLegacyItems(root) > 0;
        if (changed && levelDat.isFile()) writeWithBackup(levelDat, root);
        File playerData = new File(levelDat.getParentFile(), "playerdata");
        File[] files = playerData.listFiles((directory, name) -> name.endsWith(".dat"));
        if (files == null) return;
        Arrays.sort(files);
        for (File file : files) {
            try (FileInputStream input = new FileInputStream(file)) {
                CompoundTag player = NbtIo.readCompressed(input);
                if (migrateLegacyItems(player) > 0) writeWithBackup(file, player);
            }
        }
    }

    private static int migrateLegacyItems(Tag value) {
        int changed = 0;
        if (value instanceof CompoundTag) {
            CompoundTag compound = (CompoundTag) value;
            String id = compound.getString("id");
            String vanillaBlockEntity = VANILLA_BLOCK_ENTITY_IDS.get(id);
            if (vanillaBlockEntity != null) {
                compound.putString("id", vanillaBlockEntity);
                id = vanillaBlockEntity;
                changed++;
            }
            if ("forge:bucketfilled".equalsIgnoreCase(id) && compound.contains("tag", 10)) {
                CompoundTag tag = compound.getCompound("tag");
                String target = MissingMappings.fluidTargetPath(tag.getString("FluidName"));
                if (ModContent.fluids().containsKey(target)) {
                    compound.putString("id", BaseMetals.MOD_ID + ":" + target + "_bucket");
                    tag.remove("FluidName");
                    tag.remove("Amount");
                    if (tag.isEmpty()) compound.remove("tag");
                    changed++;
                }
            } else if (compound.contains("Count", 99)) {
                ResourceLocation oldId = safeId(id);
                if (oldId != null && (BaseMetals.MOD_ID.equals(oldId.getNamespace())
                        || "mmdlib".equals(oldId.getNamespace()))) {
                    ResourceLocation target = MissingMappings.itemTargetId(oldId.getPath());
                    if (!target.equals(oldId)) {
                        compound.putString("id", target.toString());
                        changed++;
                    }
                    if (compound.contains("Damage", 99) && compound.getInt("Damage") > 0) {
                        CompoundTag tag = compound.contains("tag", 10)
                                ? compound.getCompound("tag") : new CompoundTag();
                        if (!tag.contains("Damage", 99)) {
                            tag.putInt("Damage", compound.getInt("Damage"));
                            compound.put("tag", tag);
                            changed++;
                        }
                    }
                }
            }
            for (String key : new ArrayList<String>(compound.getAllKeys())) {
                Tag child = compound.get(key);
                if (child != null) changed += migrateLegacyItems(child);
            }
        } else if (value instanceof ListTag) {
            ListTag list = (ListTag) value;
            for (int index = 0; index < list.size(); index++) changed += migrateLegacyItems(list.get(index));
        }
        return changed;
    }

    private static Map<String, String> vanillaBlockEntityIds() {
        Map<String, String> ids = new LinkedHashMap<String, String>();
        ids.put("Airportal", "minecraft:end_portal");
        ids.put("Banner", "minecraft:banner");
        ids.put("Beacon", "minecraft:beacon");
        ids.put("Cauldron", "minecraft:brewing_stand");
        ids.put("Chest", "minecraft:chest");
        ids.put("Comparator", "minecraft:comparator");
        ids.put("Control", "minecraft:command_block");
        ids.put("DLDetector", "minecraft:daylight_detector");
        ids.put("Dropper", "minecraft:dropper");
        ids.put("EnchantTable", "minecraft:enchanting_table");
        ids.put("EndGatewayPlacementDecorator", "minecraft:end_gateway");
        ids.put("EnderChest", "minecraft:ender_chest");
        ids.put("FlowerPot", "minecraft:flower_pot");
        ids.put("Furnace", "minecraft:furnace");
        ids.put("Hopper", "minecraft:hopper");
        ids.put("MobSpawner", "minecraft:mob_spawner");
        ids.put("Music", "minecraft:noteblock");
        ids.put("Piston", "minecraft:piston");
        ids.put("RecordPlayer", "minecraft:jukebox");
        ids.put("Sign", "minecraft:sign");
        ids.put("Skull", "minecraft:skull");
        ids.put("StructureFeature", "minecraft:structure_block");
        ids.put("Trap", "minecraft:dispenser");
        return Collections.unmodifiableMap(ids);
    }

    private static ResourceLocation safeId(String value) {
        try { return value == null || value.isEmpty() ? null : new ResourceLocation(value.toLowerCase(Locale.ROOT)); }
        catch (RuntimeException ignored) { return null; }
    }

    private static void writeWithBackup(File file, CompoundTag data) throws IOException {
        File backup = new File(file.getParentFile(), file.getName() + ".basemetals-legacy-backup");
        if (!backup.exists()) Files.copy(file.toPath(), backup.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
        File temporary = new File(file.getParentFile(), file.getName() + ".basemetals.tmp");
        try (FileOutputStream output = new FileOutputStream(temporary)) {
            NbtIo.writeCompressed(data, output);
        }
        try {
            Files.move(temporary.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);
        } catch (java.nio.file.AtomicMoveNotSupportedException ignored) {
            Files.move(temporary.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static File legacyRegistrySource(File levelDat) {
        for (File candidate : new File[] { levelDat, new File(levelDat.getParentFile(), "level.dat_old") }) {
            if (!candidate.isFile()) continue;
            try (FileInputStream input = new FileInputStream(candidate)) {
                CompoundTag root = NbtIo.readCompressed(input);
                if (root.contains("FML", 10) && containsLegacyBaseMetalsRegistryEntry(root)) return candidate;
            } catch (IOException ignored) {}
        }
        return null;
    }

    private static boolean containsLegacyBaseMetalsRegistryEntry(CompoundTag root) {
        for (String id : idsFromRoot(root).values()) {
            int separator = id.indexOf(':');
            String namespace = separator < 0 ? BaseMetals.MOD_ID : id.substring(0, separator);
            if (BaseMetals.MOD_ID.equals(namespace) || "mmdlib".equals(namespace)) return true;
        }
        return false;
    }

    private static int countLegacyBaseMetalsBlocks(CompoundTag level) {
        int found = 0;
        ListTag sections = level.getList("Sections", 10);
        for (int sectionIndex = 0; sectionIndex < sections.size(); sectionIndex++) {
            CompoundTag section = sections.getCompound(sectionIndex);
            byte[] blocks = section.getByteArray("Blocks");
            if (blocks.length != 4096) continue;
            byte[] add = section.getByteArray("Add");
            for (int index = 0; index < blocks.length; index++) {
                int high = add.length == 2048 ? (add[index >> 1] >> ((index & 1) * 4)) & 15 : 0;
                if (LEGACY_BASE_METALS_BLOCK_IDS.get((blocks[index] & 255) | (high << 8))) found++;
            }
        }
        return found;
    }

    /** Snapshot used only by the isolated upgrade probe after spawn chunks have loaded. */
    public static Map<Long, Integer> legacyPreparedBlockCounts() {
        return new LinkedHashMap<Long, Integer>(LEGACY_BASE_METALS_BLOCK_COUNTS);
    }

    static int indexLegacyChunks(File worldDirectory) {
        Map<ResourceLocation, LongSet> indexed = new LinkedHashMap<>();
        indexRegionDirectory(indexed, new ResourceLocation("minecraft:overworld"), new File(worldDirectory, "region"));
        indexRegionDirectory(indexed, new ResourceLocation("minecraft:the_nether"), new File(worldDirectory, "DIM-1/region"));
        indexRegionDirectory(indexed, new ResourceLocation("minecraft:the_end"), new File(worldDirectory, "DIM1/region"));

        Path dimensions = worldDirectory.toPath().resolve("dimensions");
        if (Files.isDirectory(dimensions)) {
            try (Stream<Path> directories = Files.walk(dimensions)) {
                directories.filter(path -> Files.isDirectory(path) && path.getFileName().toString().equals("region"))
                        .forEach(path -> {
                            Path relative = dimensions.relativize(path.getParent());
                            if (relative.getNameCount() < 2) return;
                            String namespace = relative.getName(0).toString();
                            String name = relative.subpath(1, relative.getNameCount()).toString().replace(File.separatorChar, '/');
                            indexRegionDirectory(indexed, new ResourceLocation(namespace, name), path.toFile());
                        });
            } catch (IOException exception) {
                throw new IllegalStateException("Could not inspect saved dimensions in " + dimensions, exception);
            }
        }

        legacyTerrainChunks = Collections.unmodifiableMap(indexed);
        return indexed.values().stream().mapToInt(LongSet::size).sum();
    }

    private static void indexRegionDirectory(Map<ResourceLocation, LongSet> indexed,
            ResourceLocation dimension, File regionDirectory) {
        File[] regions = regionDirectory.listFiles((directory, name) -> name.matches("r\\.-?\\d+\\.-?\\d+\\.mc[ar]"));
        if (regions == null) return;
        LongSet chunks = indexed.computeIfAbsent(dimension, key -> new LongOpenHashSet());
        byte[] locations = new byte[4096];
        for (File region : regions) {
            String[] parts = region.getName().split("\\.");
            if (parts.length != 4) continue;
            try (InputStream input = Files.newInputStream(region.toPath())) {
                int read = 0;
                while (read < locations.length) {
                    int count = input.read(locations, read, locations.length - read);
                    if (count < 0) break;
                    read += count;
                }
                int regionX = Integer.parseInt(parts[1]);
                int regionZ = Integer.parseInt(parts[2]);
                for (int index = 0; index < read / 4; index++) {
                    int offset = index * 4;
                    if ((locations[offset] | locations[offset + 1] | locations[offset + 2] | locations[offset + 3]) != 0) {
                        chunks.add(chunkKey(regionX * 32 + (index & 31), regionZ * 32 + (index >> 5)));
                    }
                }
            } catch (IOException | NumberFormatException exception) {
                BaseMetals.LOGGER.warn("Could not inspect legacy chunk locations in '{}'", region, exception);
            }
        }
    }

    private static long chunkKey(int x, int z) {
        return ((long) x & 0xffffffffL) << 32 | ((long) z & 0xffffffffL);
    }

    @SuppressWarnings("unchecked")
    private static Dynamic<?>[] expandFlatteningTable(int requiredLength) {
        try {
            String fieldName = ObfuscationReflectionHelper.remapName(INameMappingService.Domain.FIELD, "f_14934_");
            Field valuesField = BlockStateData.class.getDeclaredField(fieldName);
            valuesField.setAccessible(true);
            if (Modifier.isFinal(valuesField.getModifiers())) {
                throw new IllegalStateException("The Base Metals flattening-table hook did not run");
            }
            Dynamic<?>[] current = (Dynamic<?>[]) valuesField.get(null);
            if (current.length >= requiredLength) return current;
            Dynamic<?>[] expanded = Arrays.copyOf(current, requiredLength);
            valuesField.set(null, expanded);
            return expanded;
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Could not expand Minecraft's legacy block-state flattening table", exception);
        }
    }

    private static String worldKey(File directory) {
        try { return directory.getCanonicalPath(); }
        catch (IOException exception) { return directory.getAbsolutePath(); }
    }
}
