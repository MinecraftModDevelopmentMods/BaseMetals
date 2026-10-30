package zone.moddev.mc.basemetals.migration;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

/** Reads saved palettes and writes the old format needed during flattening. */
final class LegacyBlockStateCodec {
    private LegacyBlockStateCodec() {}

    static CompoundTag writeFlattenedState(BlockState state) {
        CompoundTag tag = new CompoundTag();
        tag.putString("Name", BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString());

        if (!state.getProperties().isEmpty()) {
            CompoundTag properties = new CompoundTag();
            for (Property<?> property : state.getProperties()) {
                writeProperty(properties, state, property);
            }
            tag.put("Properties", properties);
        }

        return tag;
    }

    private static <T extends Comparable<T>> void writeProperty(CompoundTag target,
            BlockState state, Property<T> property) {
        target.putString(property.getName(), property.getName(state.getValue(property)));
    }

    static String blockName(Tag state) {
        if (state instanceof CompoundTag compound) {
            return compound.getStringOr("id", compound.getStringOr("Name", ""));
        }
        if (state instanceof StringTag text) {
            return text.value();
        }

        return "";
    }

    static CompoundTag properties(CompoundTag state) {
        return state.getCompoundOrEmpty(state.contains("properties") ? "properties" : "Properties");
    }
}
