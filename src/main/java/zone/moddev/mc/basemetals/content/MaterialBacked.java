package zone.moddev.mc.basemetals.content;

import zone.moddev.mc.basemetals.material.MaterialDefinition;

import net.minecraft.util.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.item.ItemStack;

public interface MaterialBacked {
    MaterialDefinition baseMetalsMaterial();

    default boolean isMaterialRepairIngredient(ItemStack repair) {
        return ItemTags.getAllTags().getTagOrEmpty(new ResourceLocation(
                baseMetalsMaterial().repairIngredientTag())).contains(repair.getItem());
    }
}
