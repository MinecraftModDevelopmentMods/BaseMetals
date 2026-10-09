package zone.moddev.mc.basemetals.content;

import java.util.function.Supplier;

import zone.moddev.mc.basemetals.entity.MaterialProjectile;
import zone.moddev.mc.basemetals.material.MaterialDefinition;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class BaseMetalAmmoItem extends ArrowItem implements MaterialBacked {
    public enum Kind { ARROW, BOLT }
    private final MaterialDefinition material;
    private final Kind kind;
    private final Supplier<EntityType<MaterialProjectile>> entityType;

    public BaseMetalAmmoItem(MaterialDefinition material, Kind kind,
            Supplier<EntityType<MaterialProjectile>> entityType, Item.Properties properties) {
        super(properties);
        this.material = material;
        this.kind = kind;
        this.entityType = entityType;
    }

    public Kind kind() { return kind; }
    @Override public MaterialDefinition baseMetalsMaterial() { return material; }

    @Override
    public AbstractArrow createArrow(Level world, ItemStack stack, LivingEntity shooter) {
        MaterialProjectile projectile = new MaterialProjectile(entityType.get(), world, shooter, stack);

        // A vanilla bow adds one damage. Material bows replace that with their own damage.
        projectile.setBaseDamage(1.0D + material.baseAttackDamage());

        return projectile;
    }
}
