package zone.moddev.mc.basemetals.content;

import java.util.function.Supplier;

import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.material.MaterialDefinition;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.core.Registry;

public final class MaterialTier implements Tier {
    private final MaterialDefinition material;
    private final Supplier<Ingredient> repair;

    public MaterialTier(MaterialDefinition material) {
        this.material = material;
        TagKey<Item> tag = ItemTags.create(ResourceLocation.parse(material.repairIngredientTag()));
        this.repair = () -> Ingredient.of(tag);
    }

    public MaterialDefinition material() {
        return material;
    }

    @Override public int getUses() { return material.toolDurability(); }
    @Override public float getSpeed() { return material.toolEfficiency(); }
    @Override public float getAttackDamageBonus() { return material.baseAttackDamage(); }
    @Override
    public net.minecraft.tags.TagKey<net.minecraft.world.level.block.Block> getIncorrectBlocksForDrops() {
        return switch (material.toolLevel()) {
            case 0 -> net.minecraft.tags.BlockTags.INCORRECT_FOR_WOODEN_TOOL;
            case 1 -> net.minecraft.tags.BlockTags.INCORRECT_FOR_STONE_TOOL;
            case 2 -> net.minecraft.tags.BlockTags.INCORRECT_FOR_IRON_TOOL;
            default -> net.minecraft.tags.BlockTags.INCORRECT_FOR_DIAMOND_TOOL;
        };
    }
    @Override public int getEnchantmentValue() { return material.enchantability(); }
    @Override public Ingredient getRepairIngredient() { return repair.get(); }
}
