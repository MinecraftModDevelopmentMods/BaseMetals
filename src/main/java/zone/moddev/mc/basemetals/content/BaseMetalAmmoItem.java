package zone.moddev.mc.basemetals.content;

import java.util.function.Supplier;
import javax.annotation.Nullable;

import zone.moddev.mc.basemetals.entity.MaterialProjectile;
import zone.moddev.mc.basemetals.material.MaterialDefinition;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class BaseMetalAmmoItem extends ArrowItem implements MaterialBacked {
    public enum Kind { ARROW, BOLT }
    private final MaterialDefinition material;
    private final Kind kind;
    private final Supplier<EntityType<MaterialProjectile>> entityType;

    public BaseMetalAmmoItem(MaterialDefinition material, Kind kind,
            Supplier<EntityType<MaterialProjectile>> entityType, Properties properties) {
        super(properties);
        this.material = material;
        this.kind = kind;
        this.entityType = entityType;
    }

    public Kind kind() { return kind; }
    @Override public MaterialDefinition baseMetalsMaterial() { return material; }

    /*
    
    public AbstractArrow createArrow(Level p_40513_, ItemStack p_40514_, LivingEntity p_40515_, @Nullable ItemStack p_344832_) {
        return new Arrow(p_40513_, p_40515_, p_40514_.copyWithCount(1), p_344832_);
    }
    
    */
    @Override
    public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity shooter, @Nullable ItemStack firedFrom) {
        MaterialProjectile projectile = new MaterialProjectile(entityType.get(), level, shooter, stack, firedFrom);
        projectile.setBaseDamage(1.0D + material.baseAttackDamage());
        return projectile;
    }
}
