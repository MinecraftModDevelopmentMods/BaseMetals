package zone.moddev.mc.basemetals.content;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.material.MaterialDefinition;

/** Registered armour materials shared by worn armour and horse armour. */
public final class MaterialArmor {
    private static final DeferredRegister<ArmorMaterial> MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, BaseMetals.MOD_ID);
    private static final Map<String, RegistryObject<ArmorMaterial>> BY_NAME = new LinkedHashMap<>();

    private MaterialArmor() {}

    public static void register(MaterialDefinition material) {
        BY_NAME.computeIfAbsent(material.name(), name -> MATERIALS.register(name, () -> create(material)));
    }

    public static void register(IEventBus bus) {
        MATERIALS.register(bus);
    }

    public static Holder<ArmorMaterial> get(MaterialDefinition material) {
        return BY_NAME.get(material.name()).getHolder().orElseThrow();
    }

    private static ArmorMaterial create(MaterialDefinition material) {
        Map<ArmorItem.Type, Integer> defense = new EnumMap<>(ArmorItem.Type.class);
        for (ArmorItem.Type type : ArmorItem.Type.values()) {
            defense.put(type, type == ArmorItem.Type.BODY
                    ? material.horseArmorProtection() : material.armorProtection(type.getSlot()));
        }

        return new ArmorMaterial(defense, material.enchantability(), SoundEvents.ARMOR_EQUIP_IRON,
                () -> Ingredient.of(ItemTags.create(ResourceLocation.parse(material.repairIngredientTag()))),
                List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(BaseMetals.MOD_ID, material.name()))),
                material.armorToughness(), 0.0F);
    }
}
