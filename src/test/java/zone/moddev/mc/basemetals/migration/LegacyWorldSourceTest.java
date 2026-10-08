package zone.moddev.mc.basemetals.migration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;

class LegacyWorldSourceTest {
    @TempDir
    Path temporaryWorld;

    @Test
    void removesOnlyTheRetiredVanillaProfessionIndex() {
        CompoundNBT root = professionSnapshot("minecraft:smith");
        CompoundNBT entity = new CompoundNBT();
        entity.putInt("Profession", 3);
        root.put("VillagerProof", entity);

        assertTrue(LegacyWorldDataHook.removeRetiredVanillaProfessionRegistry(root));
        assertTrue(!root.getCompound("fml").getCompound("Registries")
                .contains("minecraft:villagerprofessions"));
        assertEquals(3, root.getCompound("VillagerProof").getInt("Profession"));
        assertTrue(!LegacyWorldDataHook.removeRetiredVanillaProfessionRegistry(root));

        CompoundNBT custom = professionSnapshot("anothermod:smith");
        assertTrue(!LegacyWorldDataHook.removeRetiredVanillaProfessionRegistry(custom));
        assertTrue(custom.getCompound("fml").getCompound("Registries")
                .contains("minecraft:villagerprofessions"));

        CompoundNBT blocked = professionSnapshot("minecraft:smith");
        blocked.getCompound("fml").getCompound("Registries")
                .getCompound("minecraft:villagerprofessions").putIntArray("blocked", new int[] {7});
        assertTrue(!LegacyWorldDataHook.removeRetiredVanillaProfessionRegistry(blocked));

        CompoundNBT aliased = professionSnapshot("minecraft:smith");
        ListNBT aliases = new ListNBT();
        aliases.add(StringNBT.valueOf("anothermod:smith"));
        aliased.getCompound("fml").getCompound("Registries")
                .getCompound("minecraft:villagerprofessions").put("aliases", aliases);
        assertTrue(!LegacyWorldDataHook.removeRetiredVanillaProfessionRegistry(aliased));

        CompoundNBT malformed = professionSnapshot("minecraft:smith");
        malformed.getCompound("fml").getCompound("Registries")
                .getCompound("minecraft:villagerprofessions").put("ids", aliases);
        assertTrue(!LegacyWorldDataHook.removeRetiredVanillaProfessionRegistry(malformed));
    }

    private static CompoundNBT professionSnapshot(String name) {
        CompoundNBT entry = new CompoundNBT();
        entry.putString("K", name);
        entry.putInt("V", 3);
        ListNBT ids = new ListNBT();
        ids.add(entry);
        CompoundNBT snapshot = new CompoundNBT();
        snapshot.put("ids", ids);
        CompoundNBT registries = new CompoundNBT();
        registries.put("minecraft:villagerprofessions", snapshot);
        CompoundNBT fml = new CompoundNBT();
        fml.put("Registries", registries);
        CompoundNBT root = new CompoundNBT();
        root.put("fml", fml);
        return root;
    }

    @Test
    void usesTheUntouchedLegacyRegistryAfterForgeRewritesLevelDat() throws IOException {
        writeLevel("level.dat", 2_975, 1, "minecraft:stone");
        writeLevel("level.dat_old", 512, 1_976, "basemetals:copper_ore");

        Map<Integer, String> ids = LegacyWorldDataHook.legacyBlockIdsForTest(temporaryWorld);

        assertEquals("basemetals:copper_ore", ids.get(1_976));
    }

    @Test
    void normalizesRegistryPathsThatWereLegalInMinecraft110() {
        assertEquals("mca:rosegoldore",
                LegacyWorldDataHook.normalizeLegacyRegistryName("mca:RoseGoldOre"));
        assertEquals("basemetals:copper_ore",
                LegacyWorldDataHook.normalizeLegacyRegistryName("basemetals:copper_ore"));
    }

    @Test
    void targetsNative116ChunkLoadingWithoutTheOldLeavesPatch() throws IOException {
        String coremod = new String(Files.readAllBytes(Paths.get(
                "src/main/resources/coremods/basemetals_116_compatibility.js")), "UTF-8");
        assertTrue(coremod.contains("net.minecraft.world.chunk.storage.ChunkLoader"));
        assertTrue(coremod.contains("Ljava/util/function/Supplier;"));
        assertTrue(coremod.contains("Lcom/mojang/serialization/DynamicOps;"));
        assertTrue(coremod.contains("Lnet/minecraft/util/RegistryKey;"));
        assertTrue(coremod.contains("STATE + 'II)Z'"));
        assertTrue(!coremod.contains("leaves_fixer"));
        assertTrue(!coremod.contains("fluid_renderer"));
    }

    @Test
    void convertsOnlyOldBaseMetalsWallConnections() {
        CompoundNBT state = new CompoundNBT();
        state.putString("Name", "basemetals:steel_wall");
        CompoundNBT properties = new CompoundNBT();
        properties.putString("north", "true");
        properties.putString("east", "false");
        properties.putString("south", "tall");
        properties.putString("west", "low");
        properties.putString("up", "true");
        state.put("Properties", properties);

        assertEquals(2, LegacyWorldDataHook.migrateWallStates(state));
        assertEquals("low", properties.getString("north"));
        assertEquals("none", properties.getString("east"));
        assertEquals("tall", properties.getString("south"));
        assertEquals("low", properties.getString("west"));
        assertEquals("true", properties.getString("up"));
        assertEquals(0, LegacyWorldDataHook.migrateWallStates(state));

        state.putString("Name", "anothermod:steel_wall");
        properties.putString("north", "true");
        assertEquals(0, LegacyWorldDataHook.migrateWallStates(state));
        assertEquals("true", properties.getString("north"));
    }

    @Test
    void preservesAChunkWhoseOnlyLegacyBaseMetalsContentIsInAContainer() throws Exception {
        Field active = LegacyWorldDataHook.class.getDeclaredField("legacyWorldActive");
        active.setAccessible(true);
        boolean previous = active.getBoolean(null);
        try {
            active.setBoolean(null, true);
            CompoundNBT stack = new CompoundNBT();
            stack.putString("id", "basemetals:carbon_powder");
            stack.putByte("Count", (byte) 1);
            ListNBT items = new ListNBT();
            items.add(stack);
            CompoundNBT chest = new CompoundNBT();
            chest.putString("id", "Chest");
            chest.put("Items", items);
            ListNBT tileEntities = new ListNBT();
            tileEntities.add(chest);
            CompoundNBT level = new CompoundNBT();
            level.putInt("xPos", 12);
            level.putInt("zPos", -7);
            level.put("TileEntities", tileEntities);
            CompoundNBT root = new CompoundNBT();
            root.put("Level", level);

            LegacyWorldDataHook.prepareLegacyChunk(root);
            CompoundNBT finalized = LegacyWorldDataHook.finalizeLegacyChunk(root);

            assertEquals("full", finalized.getCompound("Level").getString("Status"));
            CompoundNBT migratedChest = finalized.getCompound("Level")
                    .getList("TileEntities", 10).getCompound(0);
            assertEquals("minecraft:chest", migratedChest.getString("id"));
            assertEquals("basemetals:coal_powder",
                    migratedChest.getList("Items", 10).getCompound(0).getString("id"));
        } finally {
            active.setBoolean(null, previous);
        }
    }

    private void writeLevel(String name, int dataVersion, int blockId, String blockName) throws IOException {
        CompoundNBT entry = new CompoundNBT();
        entry.putInt("V", blockId);
        entry.putString("K", blockName);
        ListNBT ids = new ListNBT();
        ids.add(entry);

        CompoundNBT blocks = new CompoundNBT();
        blocks.put("ids", ids);
        CompoundNBT registries = new CompoundNBT();
        registries.put("minecraft:blocks", blocks);
        CompoundNBT fml = new CompoundNBT();
        fml.put("Registries", registries);
        CompoundNBT data = new CompoundNBT();
        data.putInt("DataVersion", dataVersion);
        CompoundNBT level = new CompoundNBT();
        level.put("FML", fml);
        level.put("Data", data);

        try (FileOutputStream output = new FileOutputStream(temporaryWorld.resolve(name).toFile())) {
            CompressedStreamTools.writeCompressed(level, output);
        }
    }
}
