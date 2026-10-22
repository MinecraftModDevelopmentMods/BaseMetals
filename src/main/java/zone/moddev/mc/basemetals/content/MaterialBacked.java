package zone.moddev.mc.basemetals.content;

import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.material.MaterialDefinition;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;

public interface MaterialBacked {
    MaterialDefinition baseMetalsMaterial();

    default boolean isMaterialRepairIngredient(ItemStack repair) {
        ResourceLocation ml = ResourceLocation.fromNamespaceAndPath(BaseMetals.MOD_ID, baseMetalsMaterial().repairIngredientTag());
        return repair.is(ItemTags.create(ml));
    }
}
