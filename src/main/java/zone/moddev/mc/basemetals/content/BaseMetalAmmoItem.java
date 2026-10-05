package zone.moddev.mc.basemetals.content;

import java.util.function.Supplier;

import zone.moddev.mc.basemetals.entity.MaterialProjectile;
import zone.moddev.mc.basemetals.material.MaterialDefinition;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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
    public AbstractArrowEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        MaterialProjectile projectile = new MaterialProjectile(entityType.get(), world, shooter, stack);

        // A vanilla bow adds one damage. Material bows replace that with their own damage.
        projectile.setDamage(1.0D + material.baseAttackDamage());

        return projectile;
    }
}
