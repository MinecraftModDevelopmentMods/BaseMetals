package zone.moddev.mc.basemetals.content;

import java.util.function.Supplier;

import zone.moddev.mc.basemetals.material.MaterialDefinition;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public final class MaterialArmor implements ArmorMaterial {
    private static final int[] DURABILITY = {13, 15, 16, 11};
    private final MaterialDefinition material;
    private final Supplier<Ingredient> repair;

    public MaterialArmor(MaterialDefinition material) {
        this.material = material;
        this.repair = () -> Ingredient.of(ItemTags.create(
                new ResourceLocation(material.repairIngredientTag())));
    }

    public MaterialDefinition material() { return material; }
    @Override public int getDurabilityForType(net.minecraft.world.item.ArmorItem.Type type) {
        return DURABILITY[type.getSlot().getIndex()] * material.armorDurabilityFactor();
    }
    @Override public int getDefenseForType(net.minecraft.world.item.ArmorItem.Type type) { return material.armorProtection(type.getSlot()); }
    @Override public int getEnchantmentValue() { return material.enchantability(); }
    @Override public SoundEvent getEquipSound() { return SoundEvents.ARMOR_EQUIP_IRON; }
    @Override public Ingredient getRepairIngredient() { return repair.get(); }
    @Override public String getName() { return "basemetals:" + material.name(); }
    @Override public float getToughness() { return material.armorToughness(); }
    @Override public float getKnockbackResistance() { return 0.0F; }
}
