package zone.moddev.mc.basemetals.migration;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;

class LegacyWorldDataHookTest {
    @TempDir Path world;

    @Test
    void levelHookPassesTheFileBeforeComponentConversion() throws IOException {
        String hook = Files.readString(Path.of("src/main/resources/coremods/basemetals_118_compatibility.js"));
        int start = hook.indexOf("'basemetals_legacy_world_info'");
        int end = hook.indexOf("'basemetals_legacy_chunk_status'");
        String levelHook = hook.substring(start, end);
        assertTrue(levelHook.contains("'readLevelDataTagFixed'"));
        assertTrue(levelHook.contains("'toFile', '()Ljava/io/File;'"));
        assertFalse(levelHook.contains("'getParent'"));
        assertTrue(levelHook.contains("'prepareLegacyWorld'"));
    }

    @Test
    void obsoleteVanillaBiomeSnapshotIsRemovedWithoutChangingSavedBiomesOrOtherRegistries() {
        CompoundTag root = biomeSnapshot("minecraft:plains", "minecraft:mountains");
        CompoundTag data = new CompoundTag();
        data.putString("biome", "minecraft:plains");
        root.put("Data", data);
        CompoundTag blocks = new CompoundTag();
        blocks.putInt("marker", 42);
        root.getCompound("fml").getCompound("Registries").put("minecraft:block", blocks);
        assertTrue(LegacyWorldDataHook.removeObsoleteVanillaBiomeSnapshot(root));
        assertEquals(data, root.getCompound("Data"));
        assertEquals(blocks, root.getCompound("fml").getCompound("Registries").getCompound("minecraft:block"));
        assertFalse(LegacyWorldDataHook.removeObsoleteVanillaBiomeSnapshot(root));
    }

    @Test
    void moddedUnknownAndOverriddenBiomeSnapshotsAreNotRemoved() {
        for (String name : new String[] {"example:forest", "minecraft:custom_forest"}) {
            CompoundTag root = biomeSnapshot("minecraft:plains", name);
            CompoundTag before = root.copy();
            assertFalse(LegacyWorldDataHook.removeObsoleteVanillaBiomeSnapshot(root));
            assertEquals(before, root);
        }
        for (String list : new String[] {"aliases", "overrides", "dummied"}) {
            CompoundTag root = biomeSnapshot("minecraft:plains");
            ListTag entries = new ListTag();
            entries.add(new CompoundTag());
            root.getCompound("fml").getCompound("Registries").getCompound("minecraft:worldgen/biome").put(list, entries);
            CompoundTag before = root.copy();
            assertFalse(LegacyWorldDataHook.removeObsoleteVanillaBiomeSnapshot(root));
            assertEquals(before, root);
        }
    }

    private static CompoundTag biomeSnapshot(String... names) {
        CompoundTag root = new CompoundTag();
        CompoundTag fml = new CompoundTag();
        CompoundTag registries = new CompoundTag();
        CompoundTag snapshot = new CompoundTag();
        ListTag ids = new ListTag();
        for (String name : names) {
            CompoundTag id = new CompoundTag();
            id.putString("K", name);
            id.putInt("V", ids.size());
            ids.add(id);
        }
        snapshot.put("ids", ids);
        registries.put("minecraft:worldgen/biome", snapshot);
        fml.put("Registries", registries);
        root.put("fml", fml);
        return root;
    }

    @Test
    void oldCustomOverworldDimensionsUseTheNativeBiomeSourceReplacement() {
        CompoundTag root = dimensionFixture("minecraft:vanilla_layered", "minecraft:overworld");
        CompoundTag generator = dimensionGenerator(root);
        generator.getCompound("biome_source").putBoolean("large_biomes", true);

        assertTrue(LegacyWorldDataHook.migrateRemovedVanillaBiomeSources(root));
        assertEquals("minecraft:multi_noise", generator.getCompound("biome_source").getString("type"));
        assertEquals("minecraft:overworld", generator.getCompound("biome_source").getString("preset"));
        assertEquals("minecraft:large_biomes", generator.getString("settings"));
        assertEquals(1234L, generator.getLong("seed"));
        assertEquals("example:overworld", root.getCompound("Data").getCompound("WorldGenSettings")
                .getCompound("dimensions").getCompound("example:overworld").getString("type"));
        assertFalse(LegacyWorldDataHook.migrateRemovedVanillaBiomeSources(root));
    }

    @Test
    void biomeSourceMigrationLeavesModdedAndModernWorldSettingsAlone() {
        CompoundTag modded = dimensionFixture("example:custom_biomes", "example:noise_settings");
        CompoundTag copy = modded.copy();
        assertFalse(LegacyWorldDataHook.migrateRemovedVanillaBiomeSources(modded));
        assertEquals(copy, modded);

        CompoundTag modern = dimensionFixture("minecraft:vanilla_layered", "minecraft:overworld");
        modern.getCompound("Data").putInt("DataVersion", 2975);
        assertFalse(LegacyWorldDataHook.migrateRemovedVanillaBiomeSources(modern));
    }

    private static CompoundTag dimensionFixture(String source, String settings) {
        CompoundTag root = new CompoundTag();
        CompoundTag data = new CompoundTag();
        data.putInt("DataVersion", 2730);
        CompoundTag worldgen = new CompoundTag();
        CompoundTag dimensions = new CompoundTag();
        CompoundTag dimension = new CompoundTag();
        dimension.putString("type", "example:overworld");
        CompoundTag generator = new CompoundTag();
        generator.putString("type", "minecraft:noise");
        generator.putString("settings", settings);
        generator.putLong("seed", 1234L);
        CompoundTag biomeSource = new CompoundTag();
        biomeSource.putString("type", source);
        generator.put("biome_source", biomeSource);
        dimension.put("generator", generator);
        dimensions.put("example:overworld", dimension);
        worldgen.put("dimensions", dimensions);
        data.put("WorldGenSettings", worldgen);
        root.put("Data", data);
        return root;
    }

    private static CompoundTag dimensionGenerator(CompoundTag root) {
        return root.getCompound("Data").getCompound("WorldGenSettings").getCompound("dimensions")
                .getCompound("example:overworld").getCompound("generator");
    }

    @Test
    void oldWallConnectionsBecomeNativeHeightsWithoutChangingTallConnections() {
        CompoundTag wall = new CompoundTag();
        wall.putString("Name", "basemetals:steel_wall");
        CompoundTag properties = new CompoundTag();
        properties.putString("north", "true");
        properties.putString("east", "false");
        properties.putString("south", "tall");
        wall.put("Properties", properties);

        assertEquals(2, LegacyWorldDataHook.migrateWallStates(wall));
        assertEquals("low", properties.getString("north"));
        assertEquals("none", properties.getString("east"));
        assertEquals("tall", properties.getString("south"));
        assertEquals(0, LegacyWorldDataHook.migrateWallStates(wall));
    }

    @Test
    void modernChunkCleanupRemovesOnlyBedsAtBaseMetalsBlocks() {
        CompoundTag root = new CompoundTag();
        ListTag sections = new ListTag();
        sections.add(section(0, "basemetals:steel_block"));
        sections.add(section(1, "minecraft:red_bed"));
        root.put("sections", sections);
        ListTag entities = new ListTag();
        entities.add(bed(1));
        entities.add(bed(17));
        root.put("block_entities", entities);

        assertEquals(1, LegacyWorldDataHook.removePhantomBedEntities(root));
        assertEquals(1, entities.size());
        assertEquals(17, entities.getCompound(0).getInt("y"));
    }

    @Test
    void convertedChunksKeepBelowZeroRetrogen() {
        CompoundTag root = new CompoundTag();
        root.putBoolean("BaseMetalsLegacyPreserveChunk", true);
        root.putString("Status", "noise");
        root.put("below_zero_retrogen", new CompoundTag());

        assertSame(root, LegacyWorldDataHook.finalizeLegacyChunk(root));
        assertEquals("noise", root.getString("Status"));
        assertFalse(root.contains("BaseMetalsLegacyPreserveChunk"));
    }

    @Test
    void terrainGuardProtectsSavedChunksButAllowsNewUnderground() throws IOException {
        Path region = Files.createDirectories(world.resolve("region")).resolve("r.0.0.mca");
        byte[] locations = new byte[8192];
        locations[2] = 2;
        locations[3] = 1;
        Files.write(region, locations);
        assertEquals(1, LegacyWorldDataHook.indexLegacyChunks(world.toFile()));
        ResourceLocation overworld = ResourceLocation.parse("minecraft:overworld");

        assertTrue(LegacyWorldDataHook.isProtectedChunk(overworld, new BlockPos(1, 0, 1)));
        assertTrue(LegacyWorldDataHook.isProtectedChunk(overworld, new BlockPos(1, 255, 1)));
        assertFalse(LegacyWorldDataHook.isProtectedChunk(overworld, new BlockPos(1, -1, 1)));
        assertFalse(LegacyWorldDataHook.isProtectedChunk(overworld, new BlockPos(1, 256, 1)));
        assertFalse(LegacyWorldDataHook.isProtectedChunk(overworld, new BlockPos(17, 64, 1)));
        assertFalse(LegacyWorldDataHook.isProtectedChunk(ResourceLocation.parse("minecraft:the_nether"),
                new BlockPos(1, 64, 1)));
    }

    private static CompoundTag section(int y, String block) {
        CompoundTag section = new CompoundTag();
        section.putByte("Y", (byte) y);
        CompoundTag state = new CompoundTag();
        state.putString("Name", block);
        ListTag palette = new ListTag();
        palette.add(state);
        CompoundTag states = new CompoundTag();
        states.put("palette", palette);
        section.put("block_states", states);
        return section;
    }

    private static CompoundTag bed(int y) {
        CompoundTag bed = new CompoundTag();
        bed.putString("id", "minecraft:bed");
        bed.putInt("x", 0);
        bed.putInt("y", y);
        bed.putInt("z", 0);
        return bed;
    }
}
