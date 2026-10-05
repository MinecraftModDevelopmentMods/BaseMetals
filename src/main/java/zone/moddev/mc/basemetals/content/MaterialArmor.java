package zone.moddev.mc.basemetals.content;

import java.util.function.Supplier;

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
    private final Supplier<Ingredient> repair;

    public MaterialArmor(MaterialDefinition material) {
        this.material = material;
        final ItemTags.Wrapper tag = new ItemTags.Wrapper(new ResourceLocation(material.repairIngredientTag()));
        this.repair = () -> Ingredient.fromTag(tag);
    }

    public MaterialDefinition material() { return material; }
    @Override public int getDurability(EquipmentSlotType slot) {
        return DURABILITY[slot.getIndex()] * material.armorDurabilityFactor();
    }
    @Override public int getDamageReductionAmount(EquipmentSlotType slot) { return material.armorProtection(slot); }
    @Override public int getEnchantability() { return material.enchantability(); }
    @Override public SoundEvent getSoundEvent() { return SoundEvents.ITEM_ARMOR_EQUIP_IRON; }
    @Override public Ingredient getRepairMaterial() { return repair.get(); }
    @Override public String getName() { return "basemetals:" + material.name(); }
    @Override public float getToughness() { return material.armorToughness(); }
}
