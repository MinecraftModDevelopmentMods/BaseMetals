package zone.moddev.mc.basemetals.entity;

import zone.moddev.mc.basemetals.content.ModContent;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.common.ForgeHooks;

public final class MaterialProjectile extends AbstractArrow implements IEntityAdditionalSpawnData, ItemSupplier {
    private ItemStack ammunition = ItemStack.EMPTY;

    public MaterialProjectile(EntityType<? extends MaterialProjectile> type, Level level) {
        super(type, level);
    }

    public MaterialProjectile(EntityType<? extends MaterialProjectile> type, Level level,
            LivingEntity shooter, ItemStack ammunition) {
        this(type, level, shooter, ammunition, null);
    }

    public MaterialProjectile(EntityType<? extends MaterialProjectile> type, Level level,
            LivingEntity shooter, ItemStack ammunition, ItemStack weapon) {
        super(type, shooter, level, ammunition.copyWithCount(1), weapon);

        this.ammunition = ammunition.copy();
        this.ammunition.setCount(1);
        setPickupItemStack(this.ammunition);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        String form = getType() == zone.moddev.mc.basemetals.entity.ModEntities.CUSTOM_BOLT.get() ? "bolt" : "arrow";
        return ModContent.item("copper_" + form).get().getDefaultInstance();
    }

    @Override
    protected ItemStack getPickupItem() {
        return ammunition.isEmpty() ? getDefaultPickupItem() : ammunition.copy();
    }

    /** Used by the renderer so arrows and bolts retain their actual ammunition appearance. */
    @Override
    public ItemStack getItem() {
        return getPickupItem();
    }

    @Override
    public void addAdditionalSaveData(net.minecraft.world.level.storage.ValueOutput tag) {
        super.addAdditionalSaveData(tag);
        if (!ammunition.isEmpty()) tag.store("Ammunition", ItemStack.CODEC, ammunition);
    }

    @Override
    public void readAdditionalSaveData(net.minecraft.world.level.storage.ValueInput tag) {
        super.readAdditionalSaveData(tag);
        CompoundTag saved = tag.read("Ammunition", CompoundTag.CODEC).orElseGet(CompoundTag::new);
        if (saved.contains("Count")) {
            saved = (CompoundTag) net.minecraft.util.datafix.DataFixers.getDataFixer().update(
                    net.minecraft.util.datafix.fixes.References.ITEM_STACK,
                    new com.mojang.serialization.Dynamic<>(net.minecraft.nbt.NbtOps.INSTANCE, saved),
                    3465, net.minecraft.SharedConstants.getCurrentVersion().dataVersion().version()).getValue();
        }
        ammunition = decodeAmmunition(saved);
        if (!ammunition.isEmpty()) setPickupItemStack(ammunition);
    }

    private ItemStack decodeAmmunition(CompoundTag saved) {
        if (saved == null || saved.isEmpty()) return ItemStack.EMPTY;

        return ItemStack.CODEC.parse(registryAccess().createSerializationContext(
                net.minecraft.nbt.NbtOps.INSTANCE), saved).getOrThrow();
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeNbt(ammunition.isEmpty() ? new CompoundTag()
                : (CompoundTag) ItemStack.CODEC.encodeStart(registryAccess().createSerializationContext(
                        net.minecraft.nbt.NbtOps.INSTANCE), ammunition).getOrThrow());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buffer) {
        ammunition = decodeAmmunition(buffer.readNbt());
        if (!ammunition.isEmpty()) setPickupItemStack(ammunition);
    }

    /**
     * AbstractArrow's vanilla packet omits Forge additional spawn data. The
     * Forge packet carries the retained ammunition to the client before the
     * projectile's first render.
     */
    @Override
    public Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getAddEntityPacket(
            net.minecraft.server.level.ServerEntity tracking) {
        return ForgeHooks.getEntitySpawnPacket(this);
    }
}
