package zone.moddev.mc.basemetals;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import zone.moddev.mc.basemetals.material.MaterialCatalogue;
import zone.moddev.mc.basemetals.material.MaterialDefinition;
import org.junit.jupiter.api.Test;

class MechanicsContractTest {
    @Test
    void baseMetalsUsesTheMmdDomainNamespace() {
        assertEquals("zone.moddev.mc.basemetals", BaseMetals.class.getPackage().getName());
    }

    @Test
    void historicalAliasesResolveToTheirPreservedIds() {
        assertEquals("mercury", MissingMappings.blockTargetPath("liquid_mercury"));
        assertEquals("mercury", MissingMappings.fluidTargetPath("liquid_mercury"));
        assertEquals("coal_powder", MissingMappings.itemTargetPath("carbon_powder"));
        assertEquals("mercury_bucket", MissingMappings.itemTargetPath("liquid_mercury"));
        assertEquals("adamantine_door", MissingMappings.itemTargetPath("adamantine_door_item"));
        assertEquals("minecraft:iron_nugget", MissingMappings.itemTargetId("iron_nugget").toString());
        assertEquals("mercury", MissingMappings.blockTargetPath(
                new net.minecraft.resources.ResourceLocation("mmdlib", "liquid_mercury").getPath()));
        assertEquals("double_diamond_slab", MissingMappings.blockTargetPath("double_diamond_slab"));
        assertEquals("human_detector", MissingMappings.blockTargetPath("human_detector"));
        assertEquals("emerald_bow", MissingMappings.itemTargetPath("emerald_bow"));
    }

    @Test
    void shieldUpgradeCostUsesHardnessMagicAndEnchantments() {
        MaterialDefinition copper = MaterialCatalogue.get("copper");
        MaterialDefinition silver = MaterialCatalogue.get("silver");
        MaterialDefinition starsteel = MaterialCatalogue.get("starsteel");
        assertEquals(5, BaseMetalsEvents.shieldUpgradeCost(copper, silver, 0));
        assertEquals(35, BaseMetalsEvents.shieldUpgradeCost(copper, silver, 5));
        assertEquals(90, BaseMetalsEvents.shieldUpgradeCost(copper, starsteel, 5));
        assertEquals(7, BaseMetalsEvents.shieldUpgradeCost(
                MaterialCatalogue.get("brass"), copper, 1),
                "The 1.12 recipe truncated fractional level costs");
    }

    @Test
    void mapsOnlyKnownVanillaNamesRetiredIn114() {
        String[][] aliases = {{"sign", "oak_sign"}, {"wall_sign", "oak_wall_sign"},
                {"rose_red", "red_dye"}, {"cactus_green", "green_dye"},
                {"dandelion_yellow", "yellow_dye"}};
        for (String[] alias : aliases) {
            assertEquals("minecraft:" + alias[1], MissingMappings.vanilla114Target(
                    new net.minecraft.resources.ResourceLocation("minecraft", alias[0])).toString());
        }
        assertNull(MissingMappings.vanilla114Target(new net.minecraft.resources.ResourceLocation("anothermod", "sign")));
        assertNull(MissingMappings.vanilla114Target(new net.minecraft.resources.ResourceLocation("minecraft", "unknown")));
    }
}
