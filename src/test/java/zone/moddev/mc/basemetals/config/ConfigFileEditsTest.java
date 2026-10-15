package zone.moddev.mc.basemetals.config;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;

class ConfigFileEditsTest {
    @TempDir Path directory;

    @Test
    void savesAllChangesTogetherWithoutMutatingTheLoadedConfig() throws Exception {
        CommentedConfig loaded = CommentedConfig.inMemory();
        loaded.set("contentMode", "high_fantasy");
        loaded.setComment("contentMode", "Restart Minecraft to change modes.");
        loaded.set("specialEffects", true);
        loaded.set("unknownSetting", "keep this");
        Path file = directory.resolve("basemetals-common.toml");

        ConfigFileEdits.save(loaded, file, Map.of("contentMode", "low_fantasy", "specialEffects", false));

        try (CommentedFileConfig saved = CommentedFileConfig.builder(file).sync().build()) {
            saved.load();
            assertEquals("low_fantasy", saved.get("contentMode"));
            assertEquals(Boolean.FALSE, saved.get("specialEffects"));
            assertEquals("keep this", saved.get("unknownSetting"));
            assertEquals(loaded.getComment("contentMode"), saved.getComment("contentMode"));
        }
        assertEquals("high_fantasy", loaded.get("contentMode"));
        assertEquals(Boolean.TRUE, loaded.get("specialEffects"));
        try (var files = Files.list(directory)) {
            assertEquals(1, files.count(), "Temporary config files were left behind");
        }
    }

    @Test
    void consecutiveEditsKeepTheLatestModeAndGameplaySwitches() throws Exception {
        Path file = directory.resolve("basemetals-common.toml");
        try (CommentedFileConfig loaded = CommentedFileConfig.builder(file).sync().autosave().build()) {
            loaded.set("contentMode", "high_fantasy");
            for (int index = 0; index < 20; index++) {
                String mode = index % 2 == 0 ? "low_fantasy" : "high_fantasy";
                ConfigFileEdits.save(loaded, file, Map.of("contentMode", mode, "specialEffects", index % 2 == 0));
                loaded.load();
                assertEquals(mode, loaded.get("contentMode"));
                assertEquals(Boolean.valueOf(index % 2 == 0), loaded.get("specialEffects"));
            }
        }
    }
}
