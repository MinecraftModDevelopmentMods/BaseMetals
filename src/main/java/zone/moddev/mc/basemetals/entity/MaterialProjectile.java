package zone.moddev.mc.basemetals.entity;

import javax.annotation.Nullable;

import zone.moddev.mc.basemetals.content.ModContent;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
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
        return ModContent.item("copper_arrow").get().getDefaultInstance();
    }

    /** Used by the renderer so arrows and bolts retain their actual ammunition appearance. */
    @Override
    public ItemStack getItem() {
        return getPickupItem();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        // New saves use AbstractArrow's "item" field. Accept the former custom
        // field when it contains a stack in the current component format.
        if (!tag.contains("item", net.minecraft.nbt.Tag.TAG_COMPOUND)
                && tag.contains("Ammunition", net.minecraft.nbt.Tag.TAG_COMPOUND)) {
            ItemStack.parse(registryAccess(), tag.getCompound("Ammunition"))
                    .ifPresent(this::setPickupItemStack);
        }
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
