package zone.moddev.mc.basemetals.entity;

import zone.moddev.mc.basemetals.content.ModContent;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

public final class MaterialProjectile extends AbstractArrow implements IEntityAdditionalSpawnData, ItemSupplier {
    private ItemStack ammunition = ItemStack.EMPTY;

    public MaterialProjectile(EntityType<? extends MaterialProjectile> type, Level level) {
        super(type, level);
    }

    public MaterialProjectile(EntityType<? extends MaterialProjectile> type, Level level,
            LivingEntity shooter, ItemStack ammunition) {
        this(type, level);
        setOwner(shooter);
        setPos(shooter.getX(), shooter.getEyeY() - 0.1D, shooter.getZ());
        this.ammunition = ammunition.copy();
        this.ammunition.setCount(1);
    }

    @Override
    protected ItemStack getPickupItem() {
        return ammunition.isEmpty() ? ModContent.item("copper_arrow").get().getDefaultInstance() : ammunition.copy();
    }

    /** Used by the renderer so arrows and bolts retain their actual ammunition appearance. */
    @Override
    public ItemStack getItem() {
        return getPickupItem();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (!ammunition.isEmpty()) tag.put("Ammunition", ammunition.save(new CompoundTag()));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        ammunition = tag.contains("Ammunition") ? ItemStack.of(tag.getCompound("Ammunition")) : ItemStack.EMPTY;
    }

    @Override public void writeSpawnData(FriendlyByteBuf buffer) { buffer.writeItem(ammunition); }
    @Override public void readSpawnData(FriendlyByteBuf additionalData) { ammunition = additionalData.readItem(); }

    /**
     * AbstractArrow's vanilla packet omits Forge additional spawn data. The
     * Forge packet carries the retained ammunition to the client before the
     * projectile's first render.
     */
    @Override
    public Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
