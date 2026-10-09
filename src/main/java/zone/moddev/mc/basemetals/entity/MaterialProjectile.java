package zone.moddev.mc.basemetals.entity;

import zone.moddev.mc.basemetals.content.ModContent;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.common.registry.IEntityAdditionalSpawnData;

public final class MaterialProjectile extends AbstractArrow implements IEntityAdditionalSpawnData {
    private ItemStack ammunition = ItemStack.EMPTY;

    public MaterialProjectile(EntityType<? extends AbstractArrow> type, Level world) {
        super(type, world);
    }

    public MaterialProjectile(EntityType<? extends AbstractArrow> type, Level world, LivingEntity shooter, ItemStack ammunition) {
        super(type, shooter, world);
        this.ammunition = single(ammunition);
    }

    private static ItemStack single(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.setCount(1);
        return copy;
    }

    @Override
    protected ItemStack getPickupItem() {
        return ammunition.isEmpty() ? new ItemStack(ModContent.item("copper_arrow").get()) : ammunition.copy();
    }

    public ItemStack getAmmunition() {
        return getPickupItem();
    }

    @Override
    public net.minecraft.network.protocol.Packet<?> getAddEntityPacket() {
        return net.minecraftforge.fmllegacy.network.NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (!ammunition.isEmpty()) tag.put("Ammunition", ammunition.save(new CompoundTag()));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        ammunition = tag.contains("Ammunition", 10)
                ? ItemStack.of(tag.getCompound("Ammunition")) : ItemStack.EMPTY;
    }

    @Override public void writeSpawnData(FriendlyByteBuf buffer) { buffer.writeItem(ammunition); }
    @Override public void readSpawnData(FriendlyByteBuf buffer) { ammunition = buffer.readItem(); }
}
