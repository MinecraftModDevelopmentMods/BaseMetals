package zone.moddev.mc.basemetals.migration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.Identifier;

/** Reads old Forge world capabilities without changing the format used for new saves. */
public final class LegacyWorldCapabilityCodec {
    private LegacyWorldCapabilityCodec() {}

    public static <T> Codec<T> acceptLegacyPayload(Codec<T> current) {
        Codec<CompoundTag> legacy = CompoundTag.CODEC.validate(payload -> {
            for (String key : payload.keySet()) {
                Identifier id = Identifier.tryParse(key);

                // Capability owners have namespaced IDs. A broken current wrapper is not an old payload.
                if (id == null || !id.toString().equals(key)) {
                    return DataResult.error(() -> "Not a legacy world-capability payload");
                }
            }

            return DataResult.success(payload);
        });

        return Codec.withAlternative(current, legacy, payload -> {
            CompoundTag wrapper = new CompoundTag();
            wrapper.put("data", payload.copy());
            return current.parse(NbtOps.INSTANCE, wrapper).getOrThrow();
        });
    }
}
