package zone.moddev.mc.basemetals.migration;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import zone.moddev.mc.basemetals.BaseMetals;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

/** Direct coverage of the pre-flattening overlay around Mojang's data fixer. */
@GameTestHolder(BaseMetals.MOD_ID)
@PrefixGameTestTemplate(false)
public final class MigrationGameTests {
    private MigrationGameTests() {}

    @GameTest(template = "empty")
    public static void floorAndCeilingLeversKeepTheirLegacyOrientation(GameTestHelper helper) {
        var lever = net.minecraft.world.level.block.Blocks.LEVER;
        for (int metadata : new int[] {0, 5, 6, 7, 8, 13, 14, 15}) {
            var expected = LegacyWorldDataHook.legacyState(lever, "", metadata);
            var facing = (metadata & 7) == 0 || (metadata & 7) == 6
                    ? net.minecraft.core.Direction.EAST : net.minecraft.core.Direction.NORTH;
            require(helper, expected.getValue(net.minecraft.world.level.block.LeverBlock.FACING) == facing,
                    "Legacy lever facing changed for metadata " + metadata);
            require(helper, Legacy112WorldMigrator.matchesLegacyMetadata(expected, metadata),
                    "The pre-flattening converter disagrees with the legacy hook for metadata " + metadata);
        }
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void vanillaFixesAndMmdlibStatesSurviveTheSameChunkConversion(GameTestHelper helper) {
        try {
            CompoundTag converted = Legacy112WorldMigrator.convertChunkForTest(legacyChunk(1_343), legacyIds());
            require(helper, converted.getInt("DataVersion") == 1_456,
                    "Migration did not stop at the safe post-flattening data version");

            CompoundTag vanillaLower = Legacy112WorldMigrator.stateAtForTest(converted, 5, index(0, 1, 0));
            CompoundTag vanillaUpper = Legacy112WorldMigrator.stateAtForTest(converted, 5, index(0, 2, 0));
            require(helper, vanillaLower.getString("Name").equals("minecraft:oak_door"),
                    "Mojang's vanilla wooden-door flattening did not run");
            require(helper, hasDoorProperties(vanillaLower) && hasDoorProperties(vanillaUpper),
                    "Mojang's context-sensitive vanilla door fix did not merge both halves");

            CompoundTag aliasLower = Legacy112WorldMigrator.stateAtForTest(converted, 5, index(2, 1, 0));
            CompoundTag aliasUpper = Legacy112WorldMigrator.stateAtForTest(converted, 5, index(2, 2, 0));
            require(helper, aliasLower.getString("Name").equals("basemetals:quartz_door")
                    && aliasUpper.getString("Name").equals("basemetals:quartz_door"),
                    "mmdlib block aliases were not restored under Base Metals");
            require(helper, hasDoorProperties(aliasLower) && hasDoorProperties(aliasUpper),
                    "Base Metals door halves did not receive a complete modern state");
            require(helper, aliasLower.getCompound("Properties").getString("open").equals("true")
                    && aliasUpper.getCompound("Properties").getString("hinge").equals("right")
                    && aliasUpper.getCompound("Properties").getString("powered").equals("true"),
                    "Base Metals door metadata was not merged across its two halves");

            CompoundTag plate = Legacy112WorldMigrator.stateAtForTest(converted, 5, index(4, 1, 0));
            require(helper, plate.getString("Name").equals("basemetals:copper_plate")
                    && plate.getCompound("Properties").getString("facing").equals("east"),
                    "mmdlib metadata was lost while forwarding a stateful block");

            CompoundTag cyanoExternal = Legacy112WorldMigrator.stateAtForTest(converted, 5, index(6, 1, 0));
            require(helper, cyanoExternal.getString("Name").equals("mca:rosegoldore"),
                    "Minecraft 1.10 mixed-case registry paths were not normalized");

            CompoundTag chest = converted.getCompound("Level").getList("TileEntities", Tag.TAG_COMPOUND)
                    .getCompound(0);
            CompoundTag bucket = chest.getList("Items", Tag.TAG_COMPOUND).getCompound(0);
            require(helper, bucket.getString("id").equals("basemetals:mercury_bucket"),
                    "Legacy Forge universal bucket did not become its dedicated Base Metals bucket");
            require(helper, !bucket.contains("tag", Tag.TAG_COMPOUND)
                    || !bucket.getCompound("tag").contains("FluidName", Tag.TAG_STRING),
                    "Obsolete universal-bucket fluid metadata survived conversion");
            CompoundTag damagedTool = chest.getList("Items", Tag.TAG_COMPOUND).getCompound(1);
            require(helper, damagedTool.getCompound("tag").getInt("Damage") == 12,
                    "Legacy mod-item damage was consumed as flattening metadata");
            require(helper, chest.getList("Items", Tag.TAG_COMPOUND).getCompound(2)
                    .getString("id").equals("basemetals:coal_powder"),
                    "Cyano carbon-powder item alias was not forwarded");
            require(helper, chest.getList("Items", Tag.TAG_COMPOUND).getCompound(3)
                    .getString("id").equals("minecraft:iron_nugget"),
                    "Legacy MMDLib iron nugget was not forwarded to vanilla");
            require(helper, chest.getList("Items", Tag.TAG_COMPOUND).getCompound(4)
                    .getString("id").equals("basemetals:quartz_door"),
                    "Legacy MMDLib door-item alias was not forwarded");
            CompoundTag cabinet = findBlockEntity(converted, "cfmCabinet");
            require(helper, cabinet != null,
                    "Third-party block entity at a restored mod block was discarded");
            require(helper, cabinet.getList("cabinetItems", Tag.TAG_COMPOUND).getCompound(0)
                    .getString("id").equals("basemetals:coal_powder"),
                    "Items inside a restored third-party block entity were not migrated");
        } catch (IOException | RuntimeException exception) {
            helper.fail("Legacy chunk conversion failed: " + exception);
            return;
        }
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void mmdForge110ChunksUseTheCompleteFlatteningPath(GameTestHelper helper) {
        try {
            CompoundTag converted = Legacy112WorldMigrator.convertChunkForTest(legacyChunk(512), legacyIds());
            require(helper, converted.getInt("DataVersion") == 1_456,
                    "Forge 1.10 chunk did not reach the safe post-flattening data version");
            require(helper,
                    Legacy112WorldMigrator.stateAtForTest(converted, 5, index(2, 1, 0))
                            .getString("Name").equals("basemetals:quartz_door"),
                    "Forge 1.10 mmdlib block alias was not restored under Base Metals");
            require(helper,
                    Legacy112WorldMigrator.stateAtForTest(converted, 5, index(4, 1, 0))
                            .getString("Name").equals("basemetals:copper_plate"),
                    "Forge 1.10 Base Metals stateful block was not restored");
            CompoundTag chest = converted.getCompound("Level").getList("TileEntities", Tag.TAG_COMPOUND)
                    .getCompound(0);
            require(helper, chest.getList("Items", Tag.TAG_COMPOUND).getCompound(0)
                    .getString("id").equals("basemetals:mercury_bucket"),
                    "Forge 1.10 universal bucket did not become its dedicated Base Metals bucket");
            require(helper, chest.getList("Items", Tag.TAG_COMPOUND).getCompound(1)
                    .getCompound("tag").getInt("Damage") == 12,
                    "Forge 1.10 mod-item durability was consumed as flattening metadata");
            require(helper, chest.getList("Items", Tag.TAG_COMPOUND).getCompound(2)
                    .getString("id").equals("basemetals:coal_powder"),
                    "Forge 1.10 Cyano item alias was not forwarded");
        } catch (IOException | RuntimeException exception) {
            helper.fail("Forge 1.10 MMD chunk conversion failed: " + exception);
            return;
        }
        helper.succeed();
    }

    private static CompoundTag legacyChunk(int dataVersion) {
        byte[] blocks = new byte[4_096];
        byte[] metadata = new byte[2_048];
        byte[] add = new byte[2_048];
        set(blocks, metadata, add, index(0, 1, 0), 64, 4);
        set(blocks, metadata, add, index(0, 2, 0), 64, 11);
        set(blocks, metadata, add, index(2, 1, 0), 300, 4);
        set(blocks, metadata, add, index(2, 2, 0), 300, 11);
        set(blocks, metadata, add, index(4, 1, 0), 301, 5);
        set(blocks, metadata, add, index(6, 1, 0), 302, 0);
        set(blocks, metadata, add, index(10, 0, 0), 303, 0);

        CompoundTag section = new CompoundTag();
        section.putByte("Y", (byte) 5);
        section.put("Blocks", new ByteArrayTag(blocks));
        section.put("Data", new ByteArrayTag(metadata));
        section.put("Add", new ByteArrayTag(add));
        section.put("BlockLight", new ByteArrayTag(new byte[2_048]));
        section.put("SkyLight", new ByteArrayTag(new byte[2_048]));
        ListTag sections = new ListTag();
        sections.add(section);

        CompoundTag level = new CompoundTag();
        level.putInt("xPos", 0);
        level.putInt("zPos", 0);
        level.putLong("LastUpdate", 0L);
        level.putLong("InhabitedTime", 0L);
        level.putBoolean("TerrainPopulated", true);
        level.putBoolean("LightPopulated", true);
        level.put("HeightMap", new IntArrayTag(new int[256]));
        level.put("Biomes", new ByteArrayTag(new byte[256]));
        level.put("Sections", sections);
        level.put("Entities", new ListTag());
        CompoundTag bucketTag = new CompoundTag();
        bucketTag.putString("FluidName", "liquid_mercury");
        bucketTag.putInt("Amount", 1_000);
        CompoundTag bucket = new CompoundTag();
        bucket.putString("id", "forge:bucketFilled");
        bucket.putByte("Count", (byte) 1);
        bucket.putShort("Damage", (short) 0);
        bucket.putByte("Slot", (byte) 0);
        bucket.put("tag", bucketTag);
        ListTag items = new ListTag();
        items.add(bucket);
        CompoundTag damagedTool = new CompoundTag();
        damagedTool.putString("id", "basemetals:copper_pickaxe");
        damagedTool.putByte("Count", (byte) 1);
        damagedTool.putShort("Damage", (short) 12);
        damagedTool.putByte("Slot", (byte) 1);
        items.add(damagedTool);
        items.add(legacyStack("basemetals:carbon_powder", 2));
        items.add(legacyStack("mmdlib:iron_nugget", 3));
        items.add(legacyStack("mmdlib:quartz_door_item", 4));
        CompoundTag chest = new CompoundTag();
        chest.putString("id", "minecraft:chest");
        chest.putInt("x", 8);
        chest.putInt("y", 80);
        chest.putInt("z", 8);
        chest.put("Items", items);
        ListTag tileEntities = new ListTag();
        tileEntities.add(chest);
        CompoundTag cabinet = new CompoundTag();
        cabinet.putString("id", "cfmCabinet");
        cabinet.putInt("x", 10);
        cabinet.putInt("y", 80);
        cabinet.putInt("z", 0);
        ListTag cabinetItems = new ListTag();
        cabinetItems.add(legacyStack("basemetals:carbon_powder", 0));
        cabinet.put("cabinetItems", cabinetItems);
        tileEntities.add(cabinet);
        level.put("TileEntities", tileEntities);
        level.put("TileTicks", new ListTag());

        CompoundTag chunk = new CompoundTag();
        chunk.putInt("DataVersion", dataVersion);
        chunk.put("Level", level);
        return chunk;
    }

    private static CompoundTag legacyStack(String id, int slot) {
        CompoundTag stack = new CompoundTag();
        stack.putString("id", id);
        stack.putByte("Count", (byte) 1);
        stack.putShort("Damage", (short) 0);
        stack.putByte("Slot", (byte) slot);
        return stack;
    }

    private static Map<Integer, String> legacyIds() {
        Map<Integer, String> ids = new LinkedHashMap<>();
        ids.put(0, "minecraft:air");
        ids.put(64, "minecraft:wooden_door");
        ids.put(300, "mmdlib:quartz_door");
        ids.put(301, "mmdlib:copper_plate");
        ids.put(302, "mca:RoseGoldOre");
        ids.put(303, "cfm:cabinet");
        return ids;
    }

    private static CompoundTag findBlockEntity(CompoundTag chunk, String id) {
        for (Tag value : chunk.getCompound("Level").getList("TileEntities", Tag.TAG_COMPOUND)) {
            CompoundTag blockEntity = (CompoundTag) value;
            if (blockEntity.getString("id").equals(id)) return blockEntity;
        }
        return null;
    }

    private static int index(int x, int y, int z) {
        return y << 8 | z << 4 | x;
    }

    private static void set(byte[] blocks, byte[] metadata, byte[] add, int index, int id, int meta) {
        blocks[index] = (byte) id;
        setNibble(add, index, id >>> 8);
        setNibble(metadata, index, meta);
    }

    private static void setNibble(byte[] values, int index, int value) {
        int slot = index >>> 1;
        int shift = (index & 1) * 4;
        values[slot] = (byte) (values[slot] & ~(15 << shift) | (value & 15) << shift);
    }

    private static boolean hasDoorProperties(CompoundTag state) {
        if (!state.contains("Properties", Tag.TAG_COMPOUND)) return false;
        CompoundTag properties = state.getCompound("Properties");
        return properties.contains("half", Tag.TAG_STRING)
                && properties.contains("facing", Tag.TAG_STRING)
                && properties.contains("open", Tag.TAG_STRING)
                && properties.contains("hinge", Tag.TAG_STRING)
                && properties.contains("powered", Tag.TAG_STRING);
    }

    private static void require(GameTestHelper helper, boolean condition, String message) {
        if (!condition) helper.fail(message);
    }
}
