package zone.moddev.mc.basemetals.content;

import java.util.function.Supplier;

import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.material.MaterialDefinition;

import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.core.Registry;

public final class MaterialTier  {
    private final MaterialDefinition material;
    private final Supplier<Ingredient> repair;

    public MaterialTier(MaterialDefinition material) {
        this.material = material;
        TagKey<Item> tag = ItemTags.create(Identifier.parse(material.repairIngredientTag()));
        this.repair = () -> Ingredient.of(net.minecraft.core.registries.BuiltInRegistries.ITEM
                .getOrThrow(tag));
    }

    public ToolMaterial toolMaterial() {
        return new ToolMaterial(getIncorrectBlocksForDrops(), getUses(), getSpeed(),
                getAttackDamageBonus(), getEnchantmentValue(),
                ItemTags.create(Identifier.parse(material.repairIngredientTag())));
    }

    public MaterialDefinition material() {
        return material;
    }

    public int getUses() { return material.toolDurability(); }
    public float getSpeed() { return material.toolEfficiency(); }
    public float getAttackDamageBonus() { return material.baseAttackDamage(); }
    public net.minecraft.tags.TagKey<net.minecraft.world.level.block.Block> getIncorrectBlocksForDrops() {
        return switch (material.toolLevel()) {
            case 0 -> net.minecraft.tags.BlockTags.INCORRECT_FOR_WOODEN_TOOL;
            case 1 -> net.minecraft.tags.BlockTags.INCORRECT_FOR_STONE_TOOL;
            case 2 -> net.minecraft.tags.BlockTags.INCORRECT_FOR_IRON_TOOL;
            default -> net.minecraft.tags.BlockTags.INCORRECT_FOR_DIAMOND_TOOL;
        };
    }
    public int getEnchantmentValue() { return material.enchantability(); }
    public Ingredient getRepairIngredient() { return repair.get(); }
}
