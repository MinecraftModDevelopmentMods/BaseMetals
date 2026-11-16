package zone.moddev.mc.basemetals.data;

import static org.junit.jupiter.api.Assertions.*;
import com.google.gson.*;
import java.nio.file.*;
import org.junit.jupiter.api.Test;

class GeologyDefaultsTest {
    private static final Path ROOT = NeoForgeResourcesTest.projectRoot();
    @Test void overworldTuningDoesNotReplaceFallbacksOrAddCopper() throws Exception {
        JsonObject p=JsonParser.parseString(Files.readString(ROOT.resolve("src/main/resources/data/basemetals/orespawn/provider.json"))).getAsJsonObject();
        JsonObject ores=p.getAsJsonObject("ores");
        assertEquals(10,ores.size());assertFalse(ores.has("basemetals:ore/copper"));
        for(String name:new String[]{"tin","lead","zinc","silver","mercury","nickel","platinum"}) {
            JsonObject ore=ores.getAsJsonObject("basemetals:ore/"+name);
            assertFalse(ore.get("retrogen").getAsBoolean());
            assertEquals(8,ore.getAsJsonObject("dimensions").getAsJsonObject("minecraft:overworld").getAsJsonObject("geomes").size());
            assertEquals(4,ore.getAsJsonObject("dimension_selectors").getAsJsonObject("orespawn:all_except_nether_end").getAsJsonArray("host_families").size());
        }
        assertFalse(ores.getAsJsonObject("basemetals:ore/coldiron").getAsJsonObject("dimensions").has("minecraft:overworld"));
    }
    @Test void mineralogyIsOptionalAndOnlyRawRocksJoinTheMaficTag() throws Exception {
        JsonObject tag=JsonParser.parseString(Files.readString(ROOT.resolve("src/main/resources/data/basemetals/tags/block/ore_hosts/mafic.json"))).getAsJsonObject();
        assertEquals(2,tag.getAsJsonArray("values").size());
        for(var value:tag.getAsJsonArray("values")) {
            assertFalse(value.getAsJsonObject().get("required").getAsBoolean());
            assertTrue(value.getAsJsonObject().get("id").getAsString().matches("mineralogy:(gabbro|peridotite)"));
        }
    }
}
