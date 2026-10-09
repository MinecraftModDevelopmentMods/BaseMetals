package zone.moddev.mc.basemetals.content;

import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BucketItem;

/** A dedicated filled bucket for each molten fluid. */
public final class BaseMetalBucketItem extends BucketItem {
    public BaseMetalBucketItem(Fluid fluid, Item.Properties properties) {
        super(fluid, properties);
    }
}
