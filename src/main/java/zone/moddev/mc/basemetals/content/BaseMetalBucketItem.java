package zone.moddev.mc.basemetals.content;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.BucketItem;

/** A dedicated filled bucket for each molten fluid. */
public final class BaseMetalBucketItem extends BucketItem {
    public BaseMetalBucketItem(Fluid fluid, Item.Properties properties) {
        super(fluid, properties);
    }
}
