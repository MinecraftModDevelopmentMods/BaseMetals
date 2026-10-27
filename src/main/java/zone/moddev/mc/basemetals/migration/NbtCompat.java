package zone.moddev.mc.basemetals.migration;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;

/** Type checks for saved data written before NBT's optional getters were introduced. */
public final class NbtCompat {
    private NbtCompat() {}

    public static boolean containsTag(CompoundTag compound, String key, int type) {
        Tag value = compound.get(key);
        return value != null && (type == 99 ? value instanceof NumericTag : value.getId() == type);
    }
}
