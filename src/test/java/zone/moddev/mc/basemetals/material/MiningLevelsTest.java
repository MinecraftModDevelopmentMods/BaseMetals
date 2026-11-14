package zone.moddev.mc.basemetals.material;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class MiningLevelsTest {
    @Test
    void explicitMiningLevelsDoNotRewritePhysicalStatistics() {
        MaterialDefinition material = new MaterialDefinition("reviewed", MaterialDefinition.Kind.ORE,
                9.5D, 8.0D, 1.0D, 0xffffff, false, 1, 2, 1);
        assertEquals(1, material.toolLevel());
        assertEquals(2, material.requiredHarvestLevel());
        assertEquals(1, material.requiredOreHarvestLevel());
        assertEquals(9.5D, material.hardness());
        assertEquals(9.5F, material.toolEfficiency());
        assertEquals(256, material.toolDurability());
    }

    @Test
    void legacyConstructorRetainsItsAbiAndDefaultBehaviourForCustomMaterials() {
        MaterialDefinition custom = new MaterialDefinition("custom", MaterialDefinition.Kind.ORE,
                9.5D, 8.0D, 1.0D, 0xffffff, false);
        assertEquals(3, custom.toolLevel());
        assertEquals(2, custom.requiredHarvestLevel());
        assertEquals(2, custom.requiredOreHarvestLevel());
    }

    @Test
    void invalidMiningProfilesAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> profile(5, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> profile(-1, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> profile(1, -2, 1));
        assertThrows(IllegalArgumentException.class, () -> profile(1, 4, 1));
        assertThrows(IllegalArgumentException.class, () -> profile(1, 1, -2));
        assertThrows(IllegalArgumentException.class, () -> profile(1, 1, 4));
    }

    private static MaterialDefinition profile(int tool, int block, int ore) {
        return new MaterialDefinition("custom", MaterialDefinition.Kind.ORE,
                4.0D, 4.0D, 1.0D, 0xffffff, false, tool, block, ore);
    }

    @Test
    void everyMaterialHasItsReviewedToolSolidAndOreRequirements() {
        String[] rows = {
            "adamantine:4:3:3", "antimony:0:0:1", "aquarium:1:1:1", "bismuth:0:0:1",
            "brass:1:1:1", "bronze:2:2:1", "coldiron:2:2:2", "copper:1:1:1",
            "cupronickel:1:1:1", "electrum:0:0:1", "invar:2:2:1", "lead:0:0:1",
            "mercury:0:0:1", "mithril:3:2:1", "nickel:1:1:1", "pewter:0:0:1",
            "platinum:0:0:1", "silver:0:0:1", "starsteel:3:3:3", "steel:2:2:1",
            "tin:0:0:1", "zinc:0:0:1"
        };
        assertEquals(rows.length, MaterialCatalogue.ALL.size());
        List<Executable> checks = new ArrayList<>();
        for (String row : rows) {
            String[] value = row.split(":");
            MaterialDefinition material = MaterialCatalogue.get(value[0]);
            checks.add(() -> assertEquals(Integer.parseInt(value[1]), material.toolLevel(), value[0] + " tool"));
            checks.add(() -> assertEquals(Integer.parseInt(value[2]), material.requiredHarvestLevel(), value[0] + " solid"));
            checks.add(() -> assertEquals(Integer.parseInt(value[3]), oreLevel(material), value[0] + " ore"));
        }
        assertAll(checks);
    }

    private static int oreLevel(MaterialDefinition material) throws Exception {
        try {
            return (Integer) material.getClass().getMethod("requiredOreHarvestLevel").invoke(material);
        } catch (NoSuchMethodException legacy) {
            return material.requiredHarvestLevel();
        }
    }
}
