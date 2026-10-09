package zone.moddev.mc.basemetals.config;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ContentPolicyTest {
    private static final String[] MATERIALS = ("adamantine antimony aquarium bismuth brass bronze charcoal coal "
            + "coldiron copper cupronickel diamond electrum emerald ender gold invar iron lapis lead mercury "
            + "mithril nickel obsidian pewter platinum prismarine quartz redstone silver starsteel steel stone tin wood zinc").split(" ");
    private static final Set<String> FULL = names("adamantine aquarium bronze coldiron copper cupronickel invar mithril nickel starsteel steel iron");
    private static final Set<String> ARMOR = names("adamantine aquarium brass bronze coldiron copper cupronickel electrum invar mithril nickel platinum silver starsteel steel iron gold diamond");
    private static final Set<String> GEARS = names("adamantine aquarium brass bronze coldiron copper cupronickel invar mithril nickel starsteel steel iron gold wood");

    @Test
    void everyMaterialAndFormMatchesTheReviewed112Matrix() {
        for (String material : MATERIALS) {
            for (MaterialForm form : MaterialForm.values()) {
                assertTrue(ContentPolicy.forMode(ContentMode.HIGH_FANTASY).allows(material, form));
                assertEquals(expectedLow(material, form),
                        ContentPolicy.forMode(ContentMode.LOW_FANTASY).allows(material, form), material + " " + form);
            }
        }
    }

    private static boolean expectedLow(String material, MaterialForm form) {
        switch (form) {
            case BOW: case CROSSBOW: case FISHING_ROD: return false;
            case HELMET: case CHESTPLATE: case LEGGINGS: case BOOTS: case SHIELD: case HORSE_ARMOR:
                return ARMOR.contains(material);
            case ARROW: case BOLT:
                return FULL.contains(material) || names("silver obsidian diamond").contains(material);
            case GEAR: return GEARS.contains(material);
            case ANVIL: return names("stone steel adamantine").contains(material);
            case PICKAXE: case SHOVEL: case HOE: return FULL.contains(material);
            case AXE: return FULL.contains(material) || material.equals("obsidian");
            case SWORD: return FULL.contains(material) || names("silver obsidian").contains(material);
            case SHEARS: return FULL.contains(material) || material.equals("diamond");
            case SCYTHE: return FULL.contains(material) || names("obsidian diamond stone").contains(material);
            case CRACKHAMMER: return FULL.contains(material) || names("diamond stone wood").contains(material);
            default:
                if (!material.equals("mercury")) return true;
                return form == MaterialForm.ORE || form == MaterialForm.RAW || form == MaterialForm.POWDER || form == MaterialForm.SMALLPOWDER
                        || form == MaterialForm.FLUID || form == MaterialForm.INGOT;
        }
    }

    @Test
    void actualIdSuffixesAndForeignItemsAreHandledWithoutChangingRegistrations() {
        ContentPolicy low = ContentPolicy.forMode(ContentMode.LOW_FANTASY);
        assertFalse(low.allows("basemetals:adamantine_bow"));
        assertFalse(low.allows("basemetals:gold_fishing_rod"));
        assertFalse(low.allows("basemetals:pewter_horse_armor"));
        assertFalse(low.allows("basemetals:mercury_pressure_plate"));
        assertFalse(low.allows("basemetals:double_mercury_slab"));
        assertTrue(low.allows("basemetals:mercury_bucket"));
        assertTrue(low.allows("basemetals:flowing_mercury"));
        assertTrue(low.allows("basemetals:mercury_ingot"));
        assertTrue(low.allows("basemetals:adamantine_pickaxe"));
        assertTrue(low.allows("basemetals:human_detector"));
        assertTrue(low.allows("minecraft:bow"));
        assertTrue(low.allows("pack:adamantine_bow"));
        assertEquals(MaterialForm.PRESSURE_PLATE, MaterialForm.fromPath("mercury_pressure_plate"));
        assertEquals(MaterialForm.CRUSHED_PURIFIED, MaterialForm.fromPath("nickel_crushed_purified"));
        assertEquals(MaterialForm.DIRTY_POWDER, MaterialForm.fromPath("nickel_powder_dirty"));
    }

    @Test
    void oldOrInvalidConfigValuesDefaultToHighFantasy() {
        assertEquals(ContentMode.HIGH_FANTASY, ContentMode.parse(null));
        assertEquals(ContentMode.HIGH_FANTASY, ContentMode.parse(""));
        assertEquals(ContentMode.HIGH_FANTASY, ContentMode.parse("realism"));
        assertEquals(ContentMode.LOW_FANTASY, ContentMode.parse(" LOW_FANTASY "));
        assertFalse(ContentMode.isValid("realism"));
        assertTrue(ContentMode.isValid("HIGH_FANTASY"));
    }

    @Test
    void undoAndDefaultsRestoreBothStringsAndBooleans() {
        ConfigEdits edits = new ConfigEdits(ContentMode.LOW_FANTASY, true, false, true, false);
        edits.cycleMode();
        edits.toggle(0);
        assertTrue(edits.changed());
        edits.undo();
        assertFalse(edits.changed());
        assertEquals(ContentMode.LOW_FANTASY, edits.mode());
        assertTrue(edits.option(0));
        assertFalse(edits.option(1));
        edits.defaults();
        assertEquals(ContentMode.HIGH_FANTASY, edits.mode());
        for (int i = 0; i < 4; i++) assertTrue(edits.option(i));
        assertTrue(edits.modeChanged());
    }

    private static Set<String> names(String values) {
        return new HashSet<>(Arrays.asList(values.split(" ")));
    }
}
