package zone.moddev.mc.basemetals.migration;

import java.io.DataInput;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;

/** Disk-only NBT access for the legacy migration and its backed-up source files. */
public final class LegacyNbt {
    private LegacyNbt() {}

    public static CompoundTag readCompressed(File file) throws IOException {
        return NbtIo.readCompressed(file.toPath(), NbtAccounter.unlimitedHeap());
    }

    public static CompoundTag readCompressed(InputStream input) throws IOException {
        return NbtIo.readCompressed(input, NbtAccounter.unlimitedHeap());
    }

    public static CompoundTag read(DataInput input) throws IOException {
        return NbtIo.read(input, NbtAccounter.unlimitedHeap());
    }

    public static void writeCompressed(CompoundTag tag, File file) throws IOException {
        NbtIo.writeCompressed(tag, file.toPath());
    }

    public static void writeCompressed(CompoundTag tag, OutputStream output) throws IOException {
        NbtIo.writeCompressed(tag, output);
    }
}
