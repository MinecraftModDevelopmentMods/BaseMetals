package zone.moddev.mc.basemetals.content;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.BucketItem;

/** A dedicated filled bucket; Forge 28 has no universal bucket replacement. */
public final class BaseMetalBucketItem extends BucketItem {
    public BaseMetalBucketItem(Fluid fluid, Item.Properties properties) {
        super(fluid, properties);
    }
}
