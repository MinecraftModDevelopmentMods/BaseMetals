package zone.moddev.mc.basemetals.content;

import zone.moddev.mc.basemetals.material.MaterialDefinition;

import net.minecraft.util.SoundEvents;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public final class MaterialArmor implements IArmorMaterial {
    private static final int[] DURABILITY = {13, 15, 16, 11};
    private final MaterialDefinition material;

    public MaterialArmor(MaterialDefinition material) {
        this.material = material;
    }

    public MaterialDefinition material() { return material; }
    @Override public int getDurabilityForSlot(EquipmentSlotType slot) {
        return DURABILITY[slot.getIndex()] * material.armorDurabilityFactor();
    }
    @Override public int getDefenseForSlot(EquipmentSlotType slot) { return material.armorProtection(slot); }
    @Override public int getEnchantmentValue() { return material.enchantability(); }
    @Override public SoundEvent getEquipSound() { return SoundEvents.ARMOR_EQUIP_IRON; }
    @Override public Ingredient getRepairIngredient() {
        return Ingredient.of(ItemTags.getAllTags().getTagOrEmpty(
                new ResourceLocation(material.repairIngredientTag())));
    }
    @Override public String getName() { return "basemetals:" + material.name(); }
    @Override public float getToughness() { return material.armorToughness(); }
    @Override public float getKnockbackResistance() { return 0.0F; }
}
