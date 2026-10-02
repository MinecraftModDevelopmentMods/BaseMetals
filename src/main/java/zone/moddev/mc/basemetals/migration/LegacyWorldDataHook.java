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
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.datafixers.Dynamic;

import cpw.mods.modlauncher.api.INameMappingService;
import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.MissingMappings;
import zone.moddev.mc.basemetals.content.BaseMetalAnvilBlock;
import zone.moddev.mc.basemetals.content.CompatibilityDoubleSlabBlock;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.content.PlateBlock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockHorizontalFace;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.BlockFlowingFluid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.SlabType;
import net.minecraft.state.properties.StairsShape;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.fixes.BlockStateFlatteningMap;
import net.minecraft.world.storage.SaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.WorldPersistenceHooks;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Installs the registry-backed block states from 1.10/1.12 before Mojang's
 * vanilla-only flattening pass can replace them with air. The hook also updates
 * old universal buckets, item aliases, and pre-flattening durability in the NBT
 * that is about to be decoded.
 */
public final class LegacyWorldDataHook implements WorldPersistenceHooks.WorldPersistenceHook {
    private static final LegacyWorldDataHook INSTANCE = new LegacyWorldDataHook();
    private static final ResourceLocation BLOCK_REGISTRY = new ResourceLocation("minecraft", "blocks");
    private static final Map<String, NBTTagCompound> LEGACY_WORLD_DATA = new ConcurrentHashMap<String, NBTTagCompound>();
    private static final BitSet LEGACY_BASE_METALS_BLOCK_IDS = new BitSet();
    private static final Set<Long> LEGACY_BASE_METALS_CHUNKS = Collections.newSetFromMap(
            new ConcurrentHashMap<Long, Boolean>());
    private static final Map<Long, Integer> LEGACY_BASE_METALS_BLOCK_COUNTS =
            new ConcurrentHashMap<Long, Integer>();
    private static final Map<String, String> VANILLA_BLOCK_ENTITY_IDS = vanillaBlockEntityIds();
    private static final String PRESERVE_CHUNK_MARKER = "BaseMetalsLegacyPreserveChunk";
    private static boolean legacyWorldActive;
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
        LEGACY_BASE_METALS_CHUNKS.clear();
        LEGACY_BASE_METALS_BLOCK_COUNTS.clear();
        File source = legacyRegistrySource(levelDat);
        if (source == null) return;
        try (FileInputStream input = new FileInputStream(source)) {
            NBTTagCompound root = CompressedStreamTools.readCompressed(input);
            if (!root.contains("FML", 10)) return;
            prepareLegacyData(levelDat.getParentFile(), root.getCompound("FML"));
            migrateLoosePlayerData(levelDat, root);
        } catch (IOException exception) {
            BaseMetals.LOGGER.warn("Could not inspect '{}' for legacy Base Metals registry data", source, exception);
        }
    }

    /** Called by the coremod immediately after a legacy chunk NBT is read. */
    public static synchronized void prepareLegacyChunk(NBTTagCompound root) {
        if (root == null) return;
        migrateLegacyItems(root);
        if (!legacyWorldActive || !root.contains("Level", 10)) return;
        NBTTagCompound level = root.getCompound("Level");
        int legacyBlocks = countLegacyBaseMetalsBlocks(level);
        if (legacyBlocks > 0) {
            LEGACY_BASE_METALS_BLOCK_COUNTS.put(Long.valueOf(chunkKey(level.getInt("xPos"), level.getInt("zPos"))),
                    Integer.valueOf(legacyBlocks));
        }
        // A legacy chunk can contain Base Metals only in inventories or entities. It must still be
        // treated as completed terrain or 1.13 regenerates the chunk and discards that content.
        level.setBoolean("TerrainPopulated", true);
        level.setBoolean("LightPopulated", true);
        level.setBoolean(PRESERVE_CHUNK_MARKER, true);
    }

    /** Called after vanilla datafixing, before a legacy chunk is returned. */
    public static NBTTagCompound finalizeLegacyChunk(NBTTagCompound root) {
        if (root != null && root.contains("Level", 10)) {
            NBTTagCompound level = root.getCompound("Level");
            if (level.getBoolean(PRESERVE_CHUNK_MARKER)) {
                level.setString("Status", "postprocessed");
                level.removeTag(PRESERVE_CHUNK_MARKER);
            }
        }
        return root;
    }

    public static boolean shouldBlockWorldgenWrite(net.minecraft.util.math.BlockPos position) {
        return legacyWorldActive && position != null
                && LEGACY_BASE_METALS_CHUNKS.contains(chunkKey(position.getX() >> 4, position.getZ() >> 4));
    }

    @Override public String getModId() { return "FML"; }

    @Override
    public NBTTagCompound getDataForWriting(SaveHandler handler, WorldInfo info) {
        NBTTagCompound legacy = LEGACY_WORLD_DATA.get(worldKey(handler.getWorldDirectory()));
        return legacy == null ? new NBTTagCompound() : legacy.copy();
    }

    @Override
    public void readData(SaveHandler handler, WorldInfo info, NBTTagCompound tag) {
        prepareLegacyData(handler.getWorldDirectory(), tag);
    }

    private static synchronized void prepareLegacyData(File worldDirectory, NBTTagCompound tag) {
        if (!tag.contains("Registries", 10)) return;
        NBTTagCompound registries = tag.getCompound("Registries");
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

    private static int installLegacyBlockStates(NBTTagCompound snapshot) {
        LEGACY_BASE_METALS_BLOCK_IDS.clear();
        Map<ResourceLocation, Integer> ids = new LinkedHashMap<ResourceLocation, Integer>();
        NBTTagList savedIds = snapshot.getList("ids", 10);
        int highestState = 0;
        for (int index = 0; index < savedIds.size(); index++) {
            NBTTagCompound entry = savedIds.getCompound(index);
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
                IBlockState state = legacyState(block, entry.getKey().getPath(), meta);
                table[(entry.getValue().intValue() << 4) | meta] =
                        BlockStateFlatteningMap.makeDynamic(NBTUtil.writeBlockState(state).toString());
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

    static IBlockState legacyState(Block block, String path, int meta) {
        IBlockState state = block.getDefaultState();
        if (block instanceof PlateBlock) return state.with(PlateBlock.FACING, EnumFacing.byIndex(meta));
        if (block instanceof CompatibilityDoubleSlabBlock) return state.with(BlockSlab.TYPE, SlabType.DOUBLE);
        if (block instanceof BlockSlab) return state.with(BlockSlab.TYPE,
                (meta & 8) == 0 ? SlabType.BOTTOM : SlabType.TOP);
        if (block instanceof BlockStairs) {
            EnumFacing facing = EnumFacing.byIndex(5 - (meta & 3));
            return state.with(BlockStairs.FACING, facing)
                    .with(BlockStairs.HALF, (meta & 4) == 0 ? Half.BOTTOM : Half.TOP)
                    .with(BlockStairs.SHAPE, StairsShape.STRAIGHT)
                    .with(BlockStairs.WATERLOGGED, Boolean.FALSE);
        }
        if (block instanceof BlockDoor) {
            if ((meta & 8) != 0) {
                return state.with(BlockDoor.HALF, DoubleBlockHalf.UPPER)
                        .with(BlockDoor.HINGE, (meta & 1) != 0 ? DoorHingeSide.RIGHT : DoorHingeSide.LEFT)
                        .with(BlockDoor.POWERED, Boolean.valueOf((meta & 2) != 0));
            }
            return state.with(BlockDoor.HALF, DoubleBlockHalf.LOWER)
                    .with(BlockDoor.FACING, EnumFacing.byHorizontalIndex(meta & 3).rotateYCCW())
                    .with(BlockDoor.OPEN, Boolean.valueOf((meta & 4) != 0));
        }
        if (block instanceof BlockTrapDoor) {
            EnumFacing[] facing = { EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST };
            return state.with(BlockHorizontal.HORIZONTAL_FACING, facing[meta & 3])
                    .with(BlockTrapDoor.OPEN, Boolean.valueOf((meta & 4) != 0))
                    .with(BlockTrapDoor.HALF, (meta & 8) == 0 ? Half.BOTTOM : Half.TOP)
                    .with(BlockTrapDoor.POWERED, Boolean.FALSE)
                    .with(BlockTrapDoor.WATERLOGGED, Boolean.FALSE);
        }
        if (block instanceof BaseMetalAnvilBlock) {
            return state.with(BlockAnvil.FACING, EnumFacing.byHorizontalIndex(meta & 3))
                    .with(BaseMetalAnvilBlock.DAMAGE, Integer.valueOf(Math.min(2, (meta & 15) >> 2)));
        }
        if (block instanceof BlockButton) return attachedState(state, meta, BlockButton.POWERED);
        if (block instanceof BlockLever) return legacyLeverState(state, meta);
        if (block instanceof BlockPressurePlate) return state.with(BlockPressurePlate.POWERED,
                Boolean.valueOf(meta > 0));
        if (block instanceof BlockFlowingFluid) return state.with(BlockFlowingFluid.LEVEL,
                Integer.valueOf(Math.min(15, meta)));
        return state;
    }

    private static IBlockState attachedState(IBlockState state, int meta,
            net.minecraft.state.BooleanProperty powered) {
        EnumFacing oldFacing;
        switch (meta & 7) {
            case 0: oldFacing = EnumFacing.DOWN; break;
            case 1: oldFacing = EnumFacing.EAST; break;
            case 2: oldFacing = EnumFacing.WEST; break;
            case 3: oldFacing = EnumFacing.SOUTH; break;
            case 4: oldFacing = EnumFacing.NORTH; break;
            default: oldFacing = EnumFacing.UP;
        }
        AttachFace face = oldFacing == EnumFacing.DOWN ? AttachFace.CEILING
                : oldFacing == EnumFacing.UP ? AttachFace.FLOOR : AttachFace.WALL;
        EnumFacing horizontal = oldFacing.getAxis().isHorizontal() ? oldFacing : EnumFacing.NORTH;
        return state.with(BlockHorizontalFace.FACE, face)
                .with(BlockHorizontal.HORIZONTAL_FACING, horizontal)
                .with(powered, Boolean.valueOf((meta & 8) != 0));
    }

    private static IBlockState legacyLeverState(IBlockState state, int meta) {
        int orientation = meta & 7;
        AttachFace face;
        EnumFacing horizontal;
        if (orientation == 0 || orientation == 7) {
            face = AttachFace.CEILING;
            horizontal = orientation == 0 ? EnumFacing.EAST : EnumFacing.NORTH;
        } else if (orientation == 5 || orientation == 6) {
            face = AttachFace.FLOOR;
            horizontal = orientation == 6 ? EnumFacing.EAST : EnumFacing.NORTH;
        } else {
            face = AttachFace.WALL;
            horizontal = new EnumFacing[] { EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST,
                    EnumFacing.SOUTH, EnumFacing.NORTH }[orientation];
        }
        return state.with(BlockHorizontalFace.FACE, face)
                .with(BlockHorizontal.HORIZONTAL_FACING, horizontal)
                .with(BlockLever.POWERED, Boolean.valueOf((meta & 8) != 0));
    }

    static Map<Integer, String> legacyBlockIdsForTest(java.nio.file.Path root) throws IOException {
        File source = legacyRegistrySource(root.resolve("level.dat").toFile());
        if (source != null) {
            try (FileInputStream input = new FileInputStream(source)) {
                Map<Integer, String> result = idsFromRoot(CompressedStreamTools.readCompressed(input));
                if (!result.isEmpty()) return result;
            }
        }
        return embeddedBlockIds();
    }

    private static Map<Integer, String> idsFromRoot(NBTTagCompound root) {
        Map<Integer, String> result = new LinkedHashMap<Integer, String>();
        NBTTagList ids = root.getCompound("FML").getCompound("Registries")
                .getCompound(BLOCK_REGISTRY.toString()).getList("ids", 10);
        for (int index = 0; index < ids.size(); index++) {
            NBTTagCompound entry = ids.getCompound(index);
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

    private static void migrateLoosePlayerData(File levelDat, NBTTagCompound root) throws IOException {
        boolean changed = migrateLegacyItems(root) > 0;
        if (changed && levelDat.isFile()) writeWithBackup(levelDat, root);
        File playerData = new File(levelDat.getParentFile(), "playerdata");
        File[] files = playerData.listFiles((directory, name) -> name.endsWith(".dat"));
        if (files == null) return;
        Arrays.sort(files);
        for (File file : files) {
            try (FileInputStream input = new FileInputStream(file)) {
                NBTTagCompound player = CompressedStreamTools.readCompressed(input);
                if (migrateLegacyItems(player) > 0) writeWithBackup(file, player);
            }
        }
    }

    private static int migrateLegacyItems(INBTBase value) {
        int changed = 0;
        if (value instanceof NBTTagCompound) {
            NBTTagCompound compound = (NBTTagCompound) value;
            String id = compound.getString("id");
            String vanillaBlockEntity = VANILLA_BLOCK_ENTITY_IDS.get(id);
            if (vanillaBlockEntity != null) {
                compound.setString("id", vanillaBlockEntity);
                id = vanillaBlockEntity;
                changed++;
            }
            if ("forge:bucketfilled".equalsIgnoreCase(id) && compound.contains("tag", 10)) {
                NBTTagCompound tag = compound.getCompound("tag");
                String target = MissingMappings.fluidTargetPath(tag.getString("FluidName"));
                if (ModContent.fluids().containsKey(target)) {
                    compound.setString("id", BaseMetals.MOD_ID + ":" + target + "_bucket");
                    tag.removeTag("FluidName");
                    tag.removeTag("Amount");
                    if (tag.isEmpty()) compound.removeTag("tag");
                    changed++;
                }
            } else if (compound.contains("Count", 99)) {
                ResourceLocation oldId = safeId(id);
                if (oldId != null && (BaseMetals.MOD_ID.equals(oldId.getNamespace())
                        || "mmdlib".equals(oldId.getNamespace()))) {
                    ResourceLocation target = MissingMappings.itemTargetId(oldId.getPath());
                    if (!target.equals(oldId)) {
                        compound.setString("id", target.toString());
                        changed++;
                    }
                    if (compound.contains("Damage", 99) && compound.getInt("Damage") > 0) {
                        NBTTagCompound tag = compound.contains("tag", 10)
                                ? compound.getCompound("tag") : new NBTTagCompound();
                        if (!tag.contains("Damage", 99)) {
                            tag.setInt("Damage", compound.getInt("Damage"));
                            compound.setTag("tag", tag);
                            changed++;
                        }
                    }
                }
            }
            for (String key : new ArrayList<String>(compound.keySet())) {
                INBTBase child = compound.getTag(key);
                if (child != null) changed += migrateLegacyItems(child);
            }
        } else if (value instanceof NBTTagList) {
            NBTTagList list = (NBTTagList) value;
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
        ids.put("EndGateway", "minecraft:end_gateway");
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
        ids.put("Structure", "minecraft:structure_block");
        ids.put("Trap", "minecraft:dispenser");
        return Collections.unmodifiableMap(ids);
    }

    private static ResourceLocation safeId(String value) {
        try { return value == null || value.isEmpty() ? null : new ResourceLocation(value.toLowerCase(Locale.ROOT)); }
        catch (RuntimeException ignored) { return null; }
    }

    private static void writeWithBackup(File file, NBTTagCompound data) throws IOException {
        File backup = new File(file.getParentFile(), file.getName() + ".basemetals-legacy-backup");
        if (!backup.exists()) Files.copy(file.toPath(), backup.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
        File temporary = new File(file.getParentFile(), file.getName() + ".basemetals.tmp");
        try (FileOutputStream output = new FileOutputStream(temporary)) {
            CompressedStreamTools.writeCompressed(data, output);
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
                NBTTagCompound root = CompressedStreamTools.readCompressed(input);
                if (root.contains("FML", 10) && containsLegacyBaseMetalsRegistryEntry(root)) return candidate;
            } catch (IOException ignored) {}
        }
        return null;
    }

    private static boolean containsLegacyBaseMetalsRegistryEntry(NBTTagCompound root) {
        for (String id : idsFromRoot(root).values()) {
            int separator = id.indexOf(':');
            String namespace = separator < 0 ? BaseMetals.MOD_ID : id.substring(0, separator);
            if (BaseMetals.MOD_ID.equals(namespace) || "mmdlib".equals(namespace)) return true;
        }
        return false;
    }

    private static int countLegacyBaseMetalsBlocks(NBTTagCompound level) {
        int found = 0;
        NBTTagList sections = level.getList("Sections", 10);
        for (int sectionIndex = 0; sectionIndex < sections.size(); sectionIndex++) {
            NBTTagCompound section = sections.getCompound(sectionIndex);
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

    private static int indexLegacyChunks(File worldDirectory) {
        LEGACY_BASE_METALS_CHUNKS.clear();
        File regionDirectory = new File(worldDirectory, "region");
        File[] regions = regionDirectory.listFiles((directory, name) -> name.matches("r\\.-?\\d+\\.-?\\d+\\.mc[ar]"));
        if (regions == null) return 0;
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
                        LEGACY_BASE_METALS_CHUNKS.add(Long.valueOf(chunkKey(
                                regionX * 32 + (index & 31), regionZ * 32 + (index >> 5))));
                    }
                }
            } catch (IOException | NumberFormatException exception) {
                BaseMetals.LOGGER.warn("Could not inspect legacy chunk locations in '{}'", region, exception);
            }
        }
        return LEGACY_BASE_METALS_CHUNKS.size();
    }

    private static long chunkKey(int x, int z) {
        return ((long) x & 0xffffffffL) << 32 | ((long) z & 0xffffffffL);
    }

    @SuppressWarnings("unchecked")
    private static Dynamic<?>[] expandFlatteningTable(int requiredLength) {
        try {
            String fieldName = ObfuscationReflectionHelper.remapName(INameMappingService.Domain.FIELD, "field_199200_b");
            Field valuesField = BlockStateFlatteningMap.class.getDeclaredField(fieldName);
            valuesField.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(valuesField, valuesField.getModifiers() & ~Modifier.FINAL);
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
