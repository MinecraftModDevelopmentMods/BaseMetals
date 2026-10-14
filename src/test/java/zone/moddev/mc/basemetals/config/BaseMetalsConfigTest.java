package zone.moddev.mc.basemetals.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class BaseMetalsConfigTest {
    @Test
    void oldConfigsDefaultToHighFantasyAndKeepTheFourGameplaySwitches() {
        assertEquals(5, BaseMetalsConfig.SPEC.getValues().size());
        assertEquals(ContentMode.HIGH_FANTASY, ContentMode.parse(BaseMetalsConfig.CONTENT_MODE.get()));
        assertTrue(BaseMetalsConfig.SPECIAL_EFFECTS.get());
        assertTrue(BaseMetalsConfig.STARSTEEL_REGENERATION.get());
        assertTrue(BaseMetalsConfig.MERCURY_EFFECTS.get());
        assertTrue(BaseMetalsConfig.VILLAGER_TRADES.get());
    }
}
