package zone.moddev.mc.basemetals.migration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;

class LegacyWorldSourceTest {
    @TempDir
    Path temporaryWorld;

    @Test
    void usesTheUntouchedLegacyRegistryAfterForgeRewritesLevelDat() throws IOException {
        writeLevel("level.dat", 2_975, 1, "minecraft:stone");
        writeLevel("level.dat_old", 512, 1_976, "natura:nether_glowshroom");

        Map<Integer, String> ids = Legacy112WorldMigrator.legacyBlockIdsForTest(temporaryWorld);

        assertEquals("natura:nether_glowshroom", ids.get(1_976));
    }

    @Test
    void prefersThePreservedRegistryEvenAfterAFlattenedWorldHasBeenUpgradedAgain() throws IOException {
        writeLevel("level.dat", 2_975, 1, "minecraft:stone");
        writeLevel("level.dat_old", 512, 1_976, "minecraft:stone");
        writeLevel("basemetals-legacy-registry.dat", 2_730, 1_976, "natura:nether_glowshroom");

        Map<Integer, String> ids = Legacy112WorldMigrator.legacyBlockIdsForTest(temporaryWorld);

        assertEquals("natura:nether_glowshroom", ids.get(1_976));
    }

    @Test
    void normalizesRegistryPathsThatWereLegalInMinecraft110() {
        assertEquals("mca:rosegoldore",
                Legacy112WorldMigrator.normalizeLegacyRegistryName("mca:RoseGoldOre"));
        assertEquals("basemetals:copper_ore",
                Legacy112WorldMigrator.normalizeLegacyRegistryName("basemetals:copper_ore"));
    }

    private void writeLevel(String name, int dataVersion, int blockId, String blockName) throws IOException {
        CompoundTag entry = new CompoundTag();
        entry.putInt("V", blockId);
        entry.putString("K", blockName);
        ListTag ids = new ListTag();
        ids.add(entry);

        CompoundTag blocks = new CompoundTag();
        blocks.put("ids", ids);
        CompoundTag registries = new CompoundTag();
        registries.put("minecraft:blocks", blocks);
        CompoundTag fml = new CompoundTag();
        fml.put("Registries", registries);
        CompoundTag data = new CompoundTag();
        data.putInt("DataVersion", dataVersion);
        CompoundTag level = new CompoundTag();
        level.put("FML", fml);
        level.put("Data", data);

        NbtIo.writeCompressed(level, temporaryWorld.resolve(name).toFile());
    }
}
