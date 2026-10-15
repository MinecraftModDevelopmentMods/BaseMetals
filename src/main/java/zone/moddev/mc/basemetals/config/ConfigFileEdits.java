package zone.moddev.mc.basemetals.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.electronwill.nightconfig.toml.TomlFormat;

/** Writes a complete GUI edit without exposing half-saved settings to Forge's file watcher. */
final class ConfigFileEdits {
    private ConfigFileEdits() {}

    static void save(CommentedConfig current, Path destination, Map<String, Object> edits) throws IOException {
        CommentedConfig snapshot = CommentedConfig.copy(current);
        snapshot.valueMap().putAll(edits);

        Path temporary = Files.createTempFile(destination.getParent(), destination.getFileName().toString(), ".tmp");
        try {
            TomlFormat.instance().createWriter().write(snapshot, temporary, WritingMode.REPLACE, StandardCharsets.UTF_8);
            try {
                Files.move(temporary, destination, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(temporary, destination, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporary);
        }
    }
}
