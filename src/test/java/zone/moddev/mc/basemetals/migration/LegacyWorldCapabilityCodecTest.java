package zone.moddev.mc.basemetals.migration;

import static org.junit.jupiter.api.Assertions.*;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import org.junit.jupiter.api.Test;

class LegacyWorldCapabilityCodecTest {
    private static final Codec<CompoundTag> CODEC = LegacyWorldCapabilityCodec.acceptLegacyPayload(
            CompoundTag.CODEC.fieldOf("data").codec());

    @Test
    void readsBothFormatsAndWritesOnlyTheCurrentFormat() {
        CompoundTag contents = new CompoundTag();
        contents.putLongArray("custom_array", new long[] {11, 22, 33});
        contents.putString("owner", "Existing world");
        CompoundTag legacy = new CompoundTag();
        legacy.put("another_mod:world_data", contents);
        CompoundTag original = legacy.copy();

        CompoundTag loaded = CODEC.parse(NbtOps.INSTANCE, legacy).getOrThrow();
        assertEquals(original, loaded);
        assertEquals(original, legacy, "Reading must not modify the original tag");

        var encoded = CODEC.encodeStart(NbtOps.INSTANCE, loaded).getOrThrow();
        assertEquals(original, ((CompoundTag) encoded).getCompoundOrEmpty("data"));
        assertEquals(original, CODEC.parse(NbtOps.INSTANCE, encoded).getOrThrow());
    }

    @Test
    void acceptsEmptyOldDataButRejectsBrokenCurrentData() {
        assertTrue(CODEC.parse(NbtOps.INSTANCE, new CompoundTag()).isSuccess());
        CompoundTag malformed = new CompoundTag();
        malformed.putInt("data", 12);
        assertTrue(CODEC.parse(NbtOps.INSTANCE, malformed).isError());

        CompoundTag unrelated = new CompoundTag();
        unrelated.putString("unexpected_key", "not a capability ID");
        assertTrue(CODEC.parse(NbtOps.INSTANCE, unrelated).isError());
    }
}
