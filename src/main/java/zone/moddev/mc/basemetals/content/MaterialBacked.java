package zone.moddev.mc.basemetals.content;

import zone.moddev.mc.basemetals.material.MaterialDefinition;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;

public interface MaterialBacked {
    MaterialDefinition baseMetalsMaterial();

    default boolean isMaterialRepairIngredient(ItemStack repair) {
        return repair.is(ItemTags.create(ResourceLocation.parse(
                baseMetalsMaterial().repairIngredientTag())));
    }
}
