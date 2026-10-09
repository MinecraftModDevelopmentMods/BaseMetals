package zone.moddev.mc.basemetals.content;

import zone.moddev.mc.basemetals.material.MaterialDefinition;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;

public final class MaterialTier implements Tier {
    private final MaterialDefinition material;

    public MaterialTier(MaterialDefinition material) {
        this.material = material;
    }

    public MaterialDefinition material() { return material; }
    @Override public int getUses() { return material.toolDurability(); }
    @Override public float getSpeed() { return material.toolEfficiency(); }
    @Override public float getAttackDamageBonus() { return material.baseAttackDamage(); }
    @Override public int getLevel() { return material.toolLevel(); }
    @Override public int getEnchantmentValue() { return material.enchantability(); }
    @Override public Ingredient getRepairIngredient() {
        return Ingredient.of(ItemTags.getAllTags().getTagOrEmpty(
                new ResourceLocation(material.repairIngredientTag())));
    }
}
