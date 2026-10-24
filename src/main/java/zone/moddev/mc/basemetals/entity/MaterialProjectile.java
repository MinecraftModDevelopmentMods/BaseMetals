package zone.moddev.mc.basemetals.entity;

import javax.annotation.Nullable;

import com.mojang.serialization.Dynamic;

import zone.moddev.mc.basemetals.content.ModContent;

import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;

public final class MaterialProjectile extends AbstractArrow implements IEntityWithComplexSpawn, ItemSupplier {
    public MaterialProjectile(EntityType<? extends MaterialProjectile> type, Level level) {
        super(type, level);
    }

    public MaterialProjectile(EntityType<? extends MaterialProjectile> type, Level level,
            LivingEntity shooter, ItemStack ammunition) {
        this(type, level, shooter, ammunition, null);
    }

    public MaterialProjectile(EntityType<? extends MaterialProjectile> type, Level level,
            LivingEntity shooter, ItemStack ammunition, @Nullable ItemStack weapon) {
        super(type, shooter, level, ammunition.copyWithCount(1), weapon);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        String form = getType() == ModEntities.CUSTOM_BOLT.get() ? "bolt" : "arrow";

        return ModContent.item("copper_" + form).get().getDefaultInstance();
    }

    /** Used by the renderer so arrows and bolts retain their actual ammunition appearance. */
    @Override
    public ItemStack getItem() {
        return getPickupItem();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        if (tag.contains("item", Tag.TAG_COMPOUND) || !tag.contains("Ammunition", Tag.TAG_COMPOUND)) {
            return;
        }

        CompoundTag ammunition = tag.getCompound("Ammunition").copy();

        // Older projectiles stored their stack outside vanilla's item conversion.
        // Convert it here so names, enchantments and custom data survive the upgrade.
        if (ammunition.contains("Count")) {
            ammunition = (CompoundTag) DataFixers.getDataFixer().update(References.ITEM_STACK,
                    new Dynamic<>(NbtOps.INSTANCE, ammunition), 3465,
                    SharedConstants.getCurrentVersion().getDataVersion().getVersion()).getValue();
        }

        ItemStack.parse(registryAccess(), ammunition).ifPresent(this::setPickupItemStack);
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        ItemStack.STREAM_CODEC.encode(buffer, getPickupItem());
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf buffer) {
        setPickupItemStack(ItemStack.STREAM_CODEC.decode(buffer));
    }
}
