package zone.moddev.mc.basemetals.smoke;

import java.util.IdentityHashMap;
import java.util.Map;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import zone.moddev.mc.basemetals.BaseMetals;

/** Checks an actual older capabilities.dat file through Forge's normal load and save path. */
final class SavedWorldCapabilitySmoke {
    private static final Map<Level, ProbeData> LEVELS = new IdentityHashMap<>();

    private SavedWorldCapabilitySmoke() {}

    static void register() {
        AttachCapabilitiesEvent.Levels.BUS.addListener(event -> {
            var data = new ProbeData();
            LEVELS.put(event.getObject(), data);
            event.addCapability(Identifier.parse("basemetals_server_smoke:legacy_data"), data);
        });
    }

    static void verify(MinecraftServer server) {
        var data = LEVELS.get(server.overworld());
        var expected = new CompoundTag();
        expected.putInt("counter", 37);
        expected.putString("note", "Saved before Forge 61");
        var nested = new CompoundTag();
        nested.putIntArray("custom_array", new int[] {4, 8, 15, 16, 23, 42});
        nested.putString("owner", "Existing world");
        expected.put("nested", nested);

        if (data == null || !expected.equals(data.value)) {
            throw new IllegalStateException("Saved world-capability data was lost or changed: "
                    + (data == null ? "no provider" : data.value));
        }

        BaseMetals.LOGGER.info("BASEMETALS_SAVED_CAPABILITY PASS nonempty_disk_payload=unchanged");
    }

    private static final class ProbeData implements ICapabilitySerializable<CompoundTag> {
        private CompoundTag value = new CompoundTag();

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
            return LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT(HolderLookup.Provider registries) {
            return value.copy();
        }

        @Override
        public void deserializeNBT(HolderLookup.Provider registries, CompoundTag saved) {
            value = saved.copy();
        }
    }
}
