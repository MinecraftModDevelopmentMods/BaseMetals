package zone.moddev.mc.basemetals.migration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;

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
    void chunkFinalizationCannotCancelCompanionReturnHooks() throws Exception {
        var node = new org.objectweb.asm.tree.ClassNode();
        try (var input = getClass().getResourceAsStream(
                "/zone/moddev/mc/basemetals/mixin/SimpleRegionStorageMixin.class")) {
            new org.objectweb.asm.ClassReader(input).accept(node, 0);
        }
        var finish = node.methods.stream().filter(method -> method.name.equals("basemetals$finishChunk"))
                .findFirst().orElseThrow();
        var annotations = new java.util.ArrayList<org.objectweb.asm.tree.AnnotationNode>();
        if (finish.visibleAnnotations != null) annotations.addAll(finish.visibleAnnotations);
        if (finish.invisibleAnnotations != null) annotations.addAll(finish.invisibleAnnotations);
        var inject = annotations.stream().filter(annotation -> annotation.desc.endsWith("/Inject;"))
                .findFirst().orElseThrow();
        int cancellation = inject.values.indexOf("cancellable");
        assertTrue(cancellation < 0 || !Boolean.TRUE.equals(inject.values.get(cancellation + 1)));
        for (var instruction : finish.instructions.toArray()) {
            if (instruction instanceof org.objectweb.asm.tree.MethodInsnNode call) {
                assertFalse(call.name.equals("setReturnValue") || call.name.equals("cancel"));
            }
        }
    }

    @Test
    void finalizationEditsTheSameCompoundAndRetainsUnownedData() throws Exception {
        var field = LegacyWorldDataHook.class.getDeclaredField("PRESERVE_CHUNK_MARKER");
        field.setAccessible(true);
        String marker = (String) field.get(null);
        CompoundTag root = new CompoundTag();
        root.putBoolean(marker, true);
        root.putString("UnownedWitness", "retain exactly");
        assertSame(root, LegacyWorldDataHook.finalizeLegacyChunk(root));
        assertFalse(root.contains(marker));
        assertEquals("full", root.getString("Status").orElseThrow());
        assertEquals("retain exactly", root.getString("UnownedWitness").orElseThrow());
    }

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
