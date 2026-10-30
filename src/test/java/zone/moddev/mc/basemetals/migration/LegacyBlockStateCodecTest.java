package zone.moddev.mc.basemetals.migration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.mojang.serialization.Dynamic;
import net.minecraft.SharedConstants;
import net.minecraft.core.Direction;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.Bootstrap;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class LegacyBlockStateCodecTest {
    @BeforeAll
    static void bootstrap() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
    }

    @Test
    void intermediatePalettesUseHistoricalFieldNames() {
        var state = Blocks.OAK_DOOR.defaultBlockState()
                .setValue(DoorBlock.FACING, Direction.WEST)
                .setValue(DoorBlock.OPEN, true);
        var tag = LegacyBlockStateCodec.writeFlattenedState(state);

        assertEquals("minecraft:oak_door", tag.getStringOr("Name", ""));
        assertEquals("west", tag.getCompoundOrEmpty("Properties").getStringOr("facing", ""));
        assertEquals("true", tag.getCompoundOrEmpty("Properties").getStringOr("open", ""));
        assertFalse(tag.contains("id"));
        assertFalse(tag.contains("properties"));
    }

    @Test
    void vanillaCanUpgradeTheIntermediateStateWithoutLosingItsProperties() {
        BlockState state = Blocks.OAK_DOOR.defaultBlockState()
                .setValue(DoorBlock.FACING, Direction.WEST)
                .setValue(DoorBlock.OPEN, true);
        var source = new Dynamic<>(NbtOps.INSTANCE, LegacyBlockStateCodec.writeFlattenedState(state));
        var updated = DataFixers.getDataFixer().update(References.BLOCK_STATE, source,
                1631, SharedConstants.getCurrentVersion().dataVersion().version());
        var restored = BlockState.CODEC.parse(NbtOps.INSTANCE, updated.getValue()).getOrThrow();

        assertEquals(state, restored);
    }
}
