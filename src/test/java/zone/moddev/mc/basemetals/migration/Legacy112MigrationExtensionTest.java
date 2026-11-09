package zone.moddev.mc.basemetals.migration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import zone.moddev.mc.basemetals.api.migration.Legacy112MigrationExtension;

class Legacy112MigrationExtensionTest {
    @BeforeAll
    static void gameVersion() { SharedConstants.tryDetectVersion(); }

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
                CompoundTag itemData = new CompoundTag();
                itemData.putInt("Damage", stack.getShort("Damage"));
                stack.put("tag", itemData);
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
    }

    @Test
    void unclaimedThirdPartyBlocksStillKeepTheirNameAndTheDecoderReceivesRawMetadata() throws Exception {
        List<Integer> metadata = new ArrayList<>();
        Legacy112MigrationExtension extension = new Legacy112MigrationExtension() {
            @Override public BlockState decodeBlockState(ResourceLocation id, int data) {
                assertEquals(new ResourceLocation("example", "legacy_block"), id);
                metadata.add(data);
                return null;
            }
        };
        CompoundTag fixed = Legacy112WorldMigrator.convertChunkForTest(chunk(200, 13),
                Map.of(0, "minecraft:air", 200, "example:legacy_block"), List.of(extension));
        assertEquals(List.of(13), metadata);
        assertEquals("example:legacy_block", Legacy112WorldMigrator.stateAtForTest(fixed, 0, 0).getString("Name"));
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
            CompoundTag stack = new CompoundTag();
            stack.putString("id", "example:wrench");
            stack.putByte("Count", (byte) 1);
            stack.putByte("Slot", (byte) 4);
            stack.putShort("Damage", (short) 3);
            ListTag items = new ListTag();
            items.add(stack);
            chest.put("Items", items);
            blockEntities.add(chest);
        }
        level.put("TileEntities", blockEntities);
        level.put("Entities", new ListTag());
        root.put("Level", level);
        return root;
    }
}
