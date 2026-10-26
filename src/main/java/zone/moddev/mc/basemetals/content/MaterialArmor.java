package zone.moddev.mc.basemetals.content;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;
import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.material.MaterialDefinition;

/** Shared equipment properties for each material. */
public final class MaterialArmor {
    private static final Map<String, ArmorMaterial> BY_NAME = new LinkedHashMap<>();

    private MaterialArmor() {}

    public static void register(MaterialDefinition material) {
        BY_NAME.computeIfAbsent(material.name(), name -> create(material));
    }

    public static ArmorMaterial get(MaterialDefinition material) {
        return BY_NAME.get(material.name());
    }

    private static ArmorMaterial create(MaterialDefinition material) {
        Map<ArmorType, Integer> defense = new EnumMap<>(ArmorType.class);

        for (ArmorType type : ArmorType.values()) {
            defense.put(type, type == ArmorType.BODY
                    ? material.horseArmorProtection() : material.armorProtection(type.getSlot()));
        }

        return new ArmorMaterial(material.armorDurabilityFactor(), defense,
                material.enchantability(), SoundEvents.ARMOR_EQUIP_IRON,
                material.armorToughness(), 0.0F,
                ItemTags.create(Identifier.parse(material.repairIngredientTag())),
                ResourceKey.create(EquipmentAssets.ROOT_ID,
                        Identifier.fromNamespaceAndPath(BaseMetals.MOD_ID, material.name())));
    }
}
