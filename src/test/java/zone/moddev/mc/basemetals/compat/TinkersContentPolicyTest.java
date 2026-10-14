package zone.moddev.mc.basemetals.compat;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import zone.moddev.mc.basemetals.config.ContentMode;

class TinkersContentPolicyTest {
    @Test
    void highFantasyKeepsAllStatsWhileLowFantasyPreventsWholeFamilyBypasses() {
        for (String material : new String[] {"adamantine", "iron", "steel", "silver", "pewter", "mercury"}) {
            for (String stat : new String[] {"head", "handle", "extra", "limb", "grip", "plating"}) {
                assertTrue(TinkersContentPolicy.allows(ContentMode.HIGH_FANTASY, material, "tconstruct:" + stat));
            }
            assertFalse(TinkersContentPolicy.allows(ContentMode.LOW_FANTASY, material, "tconstruct:limb"));
            assertFalse(TinkersContentPolicy.allows(ContentMode.LOW_FANTASY, material, "tconstruct:grip"));
        }
        assertTrue(TinkersContentPolicy.allows(ContentMode.LOW_FANTASY, "steel", "tconstruct:head"));
        assertFalse(TinkersContentPolicy.allows(ContentMode.LOW_FANTASY, "silver", "tconstruct:head"));
        assertFalse(TinkersContentPolicy.allows(ContentMode.LOW_FANTASY, "pewter", "tconstruct:plating"));
    }
}
