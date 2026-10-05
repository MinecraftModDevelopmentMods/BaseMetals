package zone.moddev.mc.basemetals.material;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import net.minecraft.inventory.EquipmentSlotType;

class MaterialCatalogueTest {
    @Test
    void containsTheHistoricalTwentyTwoMaterials() {
        assertEquals(22, MaterialCatalogue.ALL.size());
        assertEquals(13, MaterialCatalogue.ALL.stream().filter(MaterialDefinition::hasOre).count());
        assertTrue(MaterialCatalogue.BY_NAME.containsKey("mercury"));
        assertFalse(MaterialCatalogue.get("mercury").hasEquipment());
    }

    @Test
    void derivesHistoricalAdamantineStatisticsWithinModernBounds() {
        MaterialDefinition adamantine = MaterialCatalogue.get("adamantine");
        assertEquals(24.0F, adamantine.blockHardness());
        assertEquals(6.0F, adamantine.oreHardness());
        assertEquals(2000.0F, adamantine.blastResistance());
        assertEquals(4, adamantine.toolLevel());
        assertEquals(3200, adamantine.toolDurability());
        assertEquals(200, adamantine.armorDurabilityFactor());
        assertEquals(0, adamantine.enchantability());
        assertEquals(2, adamantine.armorProtection(EquipmentSlotType.HEAD));
        assertEquals(8, adamantine.armorProtection(EquipmentSlotType.CHEST));
        assertEquals(7, adamantine.armorProtection(EquipmentSlotType.LEGS));
        assertEquals(3, adamantine.armorProtection(EquipmentSlotType.FEET));
    }

    @Test
    void processingFormsRemainLimitedToTheEightLegacyFamilies() {
        assertEquals(8, MaterialCatalogue.ALL.stream().filter(MaterialDefinition::processingForms).count());
        assertTrue(MaterialCatalogue.get("zinc").processingForms());
        assertFalse(MaterialCatalogue.get("copper").processingForms());
    }

    @Test
    void everyDerivedValueUsesTheHistoricalFormulaAndModernBounds() {
        for (MaterialDefinition material : MaterialCatalogue.ALL) {
            assertEquals((float) (2.0D * material.hardness()), material.blockHardness(), material.name());
            assertEquals((float) (0.5D * material.hardness()), material.oreHardness(), material.name());
            float resistance = material.name().equals("adamantine") || material.name().equals("starsteel")
                    ? 2000.0F : (float) (2.5D * material.strength());
            assertEquals(resistance, material.blastResistance(), material.name());
            assertEquals(Math.max(0, Math.min(4, (int) (material.hardness() / 3.0D))),
                    material.toolLevel(), material.name());
            assertEquals(Math.max(1, (int) (32.0D * material.strength())),
                    material.toolDurability(), material.name());
            assertEquals((float) material.hardness(), material.toolEfficiency(), material.name());
            assertEquals(Math.max(0, (int) (2.5D * material.magic())),
                    material.enchantability(), material.name());
            assertEquals(Math.max(1, (int) (2.0D * material.strength())),
                    material.armorDurabilityFactor(), material.name());
            assertEquals(4.0F + (2.0F * material.baseAttackDamage()),
                    material.axeAttackDamage(), material.name());
            assertEquals(-3.5F + Math.min(0.5F, 0.05F * (float) material.strength()),
                    material.axeAttackSpeed(), material.name());
            assertEquals(5.0F + (2.0F * material.baseAttackDamage()),
                    material.crackhammerAttackDamage(), material.name());
            assertEquals(Math.max(1.0F, 0.5F * material.toolEfficiency()),
                    material.crackhammerDestroySpeed(), material.name());
            assertEquals(Math.max(1, (int) (0.75D * material.toolDurability())),
                    material.crackhammerDurability(), material.name());
            assertEquals(Math.max(1, (int) (168.0D * material.strength())),
                    material.shieldDurability(), material.name());
            assertEquals("forge:ingots/" + material.name(), material.repairIngredientTag(),
                    material.name());
            assertEquals(material.hardness() > 10.0D ? (float) (int) (material.hardness() / 5.0D) : 0.0F,
                    material.armorToughness(), material.name());
            for (EquipmentSlotType slot : Arrays.asList(EquipmentSlotType.HEAD, EquipmentSlotType.CHEST,
                    EquipmentSlotType.LEGS, EquipmentSlotType.FEET)) {
                assertTrue(material.armorProtection(slot) >= 0 && material.armorProtection(slot) <= 30,
                        material.name() + " " + slot);
            }
        }
        assertEquals(0.9F, MaterialCatalogue.get("brass").baseAttackDamage());
        assertEquals(2.0F, MaterialCatalogue.get("bronze").baseAttackDamage());
        assertEquals(0.0F, MaterialCatalogue.get("starsteel").armorToughness());
        assertEquals(2.0F, MaterialCatalogue.get("adamantine").armorToughness());
    }
}
