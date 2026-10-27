package zone.moddev.mc.basemetals.content;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;

/** Reads the same saved fields that the game uses, rather than private entity fields. */
public final class RuntimeTestData {
    private RuntimeTestData() {}

    public static CompoundTag save(Entity entity) {
        TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, entity.registryAccess());
        entity.saveWithoutId(output);
        return output.buildResult();
    }

    public static void load(Entity entity, CompoundTag saved) {
        entity.load(TagValueInput.create(ProblemReporter.DISCARDING, entity.registryAccess(), saved));
    }

    public static double damage(AbstractArrow arrow) {
        return save(arrow).getDoubleOr("damage", 2.0D);
    }
}
