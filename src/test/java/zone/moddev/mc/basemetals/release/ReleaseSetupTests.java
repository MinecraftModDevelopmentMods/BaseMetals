package zone.moddev.mc.basemetals.release;

import com.google.gson.JsonParser;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReleaseSetupTests {
    private static String text(final String name) throws Exception {
        return new String(Files.readAllBytes(Paths.get(name)), StandardCharsets.UTF_8);
    }

    @Test
    void releaseMetadataMatchesTheSharedDispatcher() throws Exception {
        final Properties values = new Properties();
        try (Reader reader = Files.newBufferedReader(Paths.get("gradle.properties"), StandardCharsets.UTF_8)) {
            values.load(reader);
        }

        assertEquals("1.10.2", values.getProperty("minecraft_version"));
        assertTrue(values.getProperty("mod_version").matches("\\d+\\.\\d+\\.\\d+\\.110021"));
        assertEquals("forge", values.getProperty("loader_name"));
        assertEquals("1", values.getProperty("loader_code"));
        assertEquals("240967", values.getProperty("curseforge_project_id"));
        assertEquals("mmd-orespawn", values.getProperty("cf_requirements"));
        assertEquals("245586", values.getProperty("orespawn_curse_project_id"));
        assertEquals("8.0.502+7", values.getProperty("java_setup_version"));
        assertEquals("17.0.1+12", values.getProperty("gradle_java_setup_version"));
        assertTrue(text("build.gradle").contains("legacyDependencyVerificationRepository"));
        assertTrue(Files.isRegularFile(Paths.get("gradle/stage-legacy-dependencies.sh")));
        assertTrue(text("gradle/release/artifacts.gradle").contains("release/SHA256SUMS"));
        assertTrue(text("gradle/release/publishing.gradle").contains("name = 'release'"));
        assertTrue(text("gradle/release/publishing.gradle").contains("verifyPreparedReleaseArtifacts"));
    }

    @Test
    void workflowsUseCanonicalBranchesAndPinnedActions() throws Exception {
        try (Stream<Path> files = Files.list(Paths.get(".github/workflows"))) {
            for (Path path : (Iterable<Path>) files::iterator) {
                final String workflow = text(path.toString());
                assertFalse(workflow.contains("secrets: inherit"), path.toString());
                assertFalse(workflow.contains("pull_request_target"), path.toString());
                for (String line : workflow.split("\n")) {
                    if (line.trim().startsWith("uses:")) {
                        assertTrue(line.trim().matches("uses: [\\w./-]+@[a-f0-9]{40}( #.*)?"), line);
                    }
                }
                if (!path.endsWith("release-on-tag.yml")) {
                    assertTrue(workflow.contains("master-1.10.2"), path.toString());
                    assertFalse(workflow.contains("master-1.12"), path.toString());
                }
            }
        }
        final String ci = text(".github/workflows/ci.yml");
        assertTrue(ci.contains("name: Build, test, and audit"));
        assertTrue(ci.contains("packagedForgeRuntimeTest"));
        assertTrue(ci.contains("cmp \"$RUNNER_TEMP/first-SHA256SUMS\" build/release/SHA256SUMS"));
        assertTrue(text(".github/workflows/release-on-tag.yml").contains("Nothing has been published"));
    }

    @Test
    void everyResourceAndFixtureIsValidJson() throws Exception {
        for (String root : new String[]{"src/main/resources", "tools/validation/legacy110/fixtures"}) {
            try (Stream<Path> files = Files.walk(Paths.get(root))) {
                for (Path path : (Iterable<Path>) files.filter(p -> p.toString().endsWith(".json"))::iterator) {
                    try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                        assertNotNull(new JsonParser().parse(reader), path.toString());
                    }
                }
            }
        }
    }
}
