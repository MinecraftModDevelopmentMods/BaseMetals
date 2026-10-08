package zone.moddev.mc.basemetals.entity;

import zone.moddev.mc.basemetals.content.ModContent;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public final class MaterialProjectile extends AbstractArrowEntity implements IEntityAdditionalSpawnData {
    private ItemStack ammunition = ItemStack.EMPTY;

    public MaterialProjectile(EntityType<? extends AbstractArrowEntity> type, World world) {
        super(type, world);
    }

    public MaterialProjectile(EntityType<? extends AbstractArrowEntity> type, World world, LivingEntity shooter, ItemStack ammunition) {
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
    public net.minecraft.network.IPacket<?> getAddEntityPacket() {
        return net.minecraftforge.fml.network.NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT tag) {
        super.addAdditionalSaveData(tag);
        if (!ammunition.isEmpty()) tag.put("Ammunition", ammunition.save(new CompoundNBT()));
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT tag) {
        super.readAdditionalSaveData(tag);
        ammunition = tag.contains("Ammunition", 10)
                ? ItemStack.of(tag.getCompound("Ammunition")) : ItemStack.EMPTY;
    }

    @Override public void writeSpawnData(PacketBuffer buffer) { buffer.writeItem(ammunition); }
    @Override public void readSpawnData(PacketBuffer buffer) { ammunition = buffer.readItem(); }
}
