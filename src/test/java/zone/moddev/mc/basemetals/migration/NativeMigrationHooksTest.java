package zone.moddev.mc.basemetals.migration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Modifier;
import java.nio.file.Path;

import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.datafix.fixes.BlockStateData;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.storage.SimpleRegionStorage;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.level.Level;
import org.junit.jupiter.api.Test;

/** Catches renamed injection targets before a packaged world is opened. */
class NativeMigrationHooksTest {
    @Test
    void levelCaptureStillRunsBeforeVanillaConversion() throws Exception {
        var method = LevelStorageSource.class.getDeclaredMethod(
                "readLevelDataTagFixed", Path.class, DataFixer.class);

        assertTrue(Modifier.isStatic(method.getModifiers()));
        assertEquals(Dynamic.class, method.getReturnType());
    }

    @Test
    void chunkAndTerrainTargetsKeepTheirNativeSignatures() throws Exception {
        var upgrade = SimpleRegionStorage.class.getDeclaredMethod(
                "upgradeChunkTag", CompoundTag.class, int.class, CompoundTag.class);
        var write = WorldGenRegion.class.getDeclaredMethod("ensureCanWrite", BlockPos.class);

        assertEquals(CompoundTag.class, upgrade.getReturnType());
        assertEquals(boolean.class, write.getReturnType());
    }

    @Test
    void anvilDamageAndLegacyTablesRemainAvailable() throws Exception {
        var damage = AnvilBlock.class.getDeclaredMethod("damage", BlockState.class);

        assertTrue(Modifier.isStatic(damage.getModifiers()));
        assertEquals(BlockState.class, damage.getReturnType());

        for (String name : new String[] {"MAP", "BLOCK_DEFAULTS"}) {
            var field = BlockStateData.class.getDeclaredField(name);

            assertTrue(Modifier.isStatic(field.getModifiers()), name);
            assertEquals(Dynamic[].class, field.getType(), name);
        }
    }

    @Test
    void capabilityHookTargetsOnlyForgesWorldDataCodec() throws Exception {
        var factory = net.minecraftforge.common.util.LevelCapabilityData.class.getDeclaredMethod(
                "type", net.minecraft.server.level.ServerLevel.class);

        assertTrue(Modifier.isStatic(factory.getModifiers()));
        assertEquals(net.minecraft.world.level.saveddata.SavedDataType.class, factory.getReturnType());
    }

    @Test
    void anvilWearHookTargetsOnlyTheNativeTakeCallback() throws Exception {
        var callback = AnvilMenu.class.getDeclaredMethod(
                "lambda$onTake$2", Player.class, float.class, Level.class, BlockPos.class);

        assertTrue(Modifier.isStatic(callback.getModifiers()));
        assertEquals(void.class, callback.getReturnType());
    }
}
