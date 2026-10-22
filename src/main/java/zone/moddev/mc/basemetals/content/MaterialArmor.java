package zone.moddev.mc.basemetals.content;

import java.util.List;
import java.util.Map;

import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.material.MaterialDefinition;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Registered armor statistics shared by humanoid and horse armor items. */
public final class MaterialArmor {
    public static final DeferredRegister<ArmorMaterial> REGISTER =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, BaseMetals.MOD_ID);

    private MaterialArmor() {}

    public static DeferredHolder<ArmorMaterial, ArmorMaterial> register(MaterialDefinition material) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(BaseMetals.MOD_ID, material.name());
        return REGISTER.register(material.name(), () -> new ArmorMaterial(
                Map.of(
                        ArmorItem.Type.HELMET, material.armorProtection(EquipmentSlot.HEAD),
                        ArmorItem.Type.CHESTPLATE, material.armorProtection(EquipmentSlot.CHEST),
                        ArmorItem.Type.LEGGINGS, material.armorProtection(EquipmentSlot.LEGS),
                        ArmorItem.Type.BOOTS, material.armorProtection(EquipmentSlot.FEET),
                        ArmorItem.Type.BODY, material.horseArmorProtection()),
                material.enchantability(),
                SoundEvents.ARMOR_EQUIP_IRON,
                () -> Ingredient.of(ItemTags.create(ResourceLocation.parse(material.repairIngredientTag()))),
                List.of(new ArmorMaterial.Layer(id)),
                material.armorToughness(),
                0.0F));
    }
}
