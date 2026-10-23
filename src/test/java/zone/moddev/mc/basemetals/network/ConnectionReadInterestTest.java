package zone.moddev.mc.basemetals.network;

import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Forge 50 owns protocol transitions; Base Metals only checks its own content mode. */
class ConnectionReadInterestTest {
    @Test
    void obsoleteConnectionPatchIsNotInstalled() throws Exception {
        assertFalse(Files.exists(Path.of("src/main/resources/coremods/connection_read_interest.js")));
        String coremods = Files.readString(Path.of("src/main/resources/META-INF/coremods.json"));
        assertFalse(coremods.contains("connection_read_interest"));
    }

    @Test
    void bothDirectionsUseTheConfigurationPhase() throws Exception {
        String source = Files.readString(Path.of(
                "src/main/java/zone/moddev/mc/basemetals/network/ContentModeNetwork.java"));
        assertTrue(source.contains("NetworkDirection.CONFIGURATION_TO_CLIENT"));
        assertTrue(source.contains("NetworkDirection.CONFIGURATION_TO_SERVER"));
        assertTrue(source.contains("listener.finishCurrentTask(TASK)"));
        assertFalse(source.contains("HandshakeHandler"));
        assertFalse(source.contains("SimpleConfigurationTask"));
    }

    @Test
    void legacyDefaultsAndExplicitHighFantasyAreEquivalent() {
        assertTrue(ContentModeNetwork.modesMatch(null, "high_fantasy"));
        assertTrue(ContentModeNetwork.modesMatch("low_fantasy", "low_fantasy"));
        assertFalse(ContentModeNetwork.modesMatch("low_fantasy", "high_fantasy"));
    }
}
