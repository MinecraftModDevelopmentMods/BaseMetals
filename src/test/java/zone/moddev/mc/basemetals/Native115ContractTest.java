package zone.moddev.mc.basemetals;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import org.junit.jupiter.api.Test;

class Native115ContractTest {
    @Test
    void registersBothProjectileTypesAndNativeRenderLayers() throws Exception {
        String setup = source("src/main/java/zone/moddev/mc/basemetals/client/ClientSetup.java");
        assertTrue(setup.contains("ModEntities.CUSTOM_ARROW.get()"));
        assertTrue(setup.contains("ModEntities.CUSTOM_BOLT.get()"));
        assertTrue(setup.contains("RenderTypeLookup.setRenderLayer"));
        assertTrue(setup.contains("RenderType.getCutoutMipped()"));
        assertTrue(setup.contains("RenderType.getTranslucent()"));
        assertFalse(setup.contains("registerEntityRenderingHandler(MaterialProjectile.class"));
        assertFalse(source("src/main/java/zone/moddev/mc/basemetals/content/MoltenFluid.java")
                .contains("BlockRenderLayer"));
        assertFalse(source("src/main/java/zone/moddev/mc/basemetals/content/ModContent.java")
                .contains("BlockRenderLayer"));
    }

    @Test
    void pinsThePublishedOreSpawnRelease() throws Exception {
        Properties properties = new Properties();

        try (InputStream input = Files.newInputStream(Paths.get("gradle.properties"))) {
            properties.load(input);
        }

        assertEquals("release", properties.getProperty("orespawn_dependency_mode"));
        assertEquals("4.1.0.115021", properties.getProperty("orespawn_version"));
        assertEquals("245586", properties.getProperty("orespawn_curse_project_id"));
        assertEquals("9073591", properties.getProperty("orespawn_curse_file_id"));
        assertEquals("F7C9A110E834EC3F73D55C20E8F52EEA1CF48C4E3ACCFBE8FCEFB60E78ABE387",
                properties.getProperty("orespawn_sha256"));
        assertTrue(source("build.gradle").contains("curse.maven:${orespawnModule}:${orespawn_curse_file_id}"));
    }

    @Test
    void keepsTheLocalCandidatePublicationGuard() throws Exception {
        assertTrue(source("gradle/stage-orespawn-release.sh")
                .contains("Publication is blocked"));
        String artifacts = source("gradle/release/artifacts.gradle");
        assertTrue(artifacts.contains("PublishToMavenLocal"));
        assertTrue(artifacts.contains("Publication is blocked until the public OreSpawn release is pinned"));
    }

    private static String source(String path) throws Exception {
        return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
    }
}
