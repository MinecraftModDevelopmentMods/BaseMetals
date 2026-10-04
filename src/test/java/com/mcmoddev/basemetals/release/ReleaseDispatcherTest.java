package com.mcmoddev.basemetals.release;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Timeout(120)
class ReleaseDispatcherTest {

	@TempDir
	Path directory;

	@Test
	void releasesJava8PortsWithoutAddingMmdlibToTheirDependencies() throws Exception {
		final Map<String, String> properties = metadata("1.13.2", "forge", "8.0.502+7");
		final Result result = preflight("master-1.13.2", properties);

		assertEquals(0, result.exitCode, result.log);
		assertTrue(result.outputs.contains("gradle_java_setup_version=17.0.1+12"));
		assertTrue(result.outputs.contains("install_gradle_java=true"));
		assertTrue(result.outputs.contains("245586(required)"));
		assertFalse(result.outputs.contains("261744(required)"));
	}

	@Test
	void keepsBothRequiredDependenciesForTheLegacyRelease() throws Exception {
		final Map<String, String> properties = metadata("1.12.2", "forge", "8.0.502+7");
		properties.remove("orespawn_curse_project_id");
		properties.put("orespawn4_curse_project_id", "245586");
		properties.put("cf_requirements", "mmd-orespawn,mmdlib");

		final Result result = preflight("master-1.12", properties);

		assertEquals(0, result.exitCode, result.log);
		assertTrue(result.outputs.contains("245586(required)\n261744(required)"));
	}

	@Test
	void acceptsOreSpawnsForgeAndNeoForgeBranchNames() throws Exception {
		final String[][] targets = {
				{"master-1.8.9", "1.8.9", "forge", "8.0.502+7"},
				{"master-1.9", "1.9", "forge", "8.0.502+7"},
				{"master-1.9.4", "1.9.4", "forge", "8.0.502+7"},
				{"master-1.10", "1.10.2", "forge", "8.0.502+7"},
				{"master-1.11", "1.11", "forge", "8.0.502+7"},
				{"master-1.11.2", "1.11.2", "forge", "8.0.502+7"},
				{"master-1.12.2", "1.12.2", "forge", "8.0.502+7"},
				{"master-1.14.4", "1.14.4", "forge", "8.0.502+7"},
				{"master-1.15.2", "1.15.2", "forge", "8.0.502+7"},
				{"master-1.16.5", "1.16.5", "forge", "8.0.502+7"},
				{"master-1.17.1", "1.17.1", "forge", "16.0.2+7"},
				{"master-1.18", "1.18.2", "forge", "17.0.1+12"},
				{"master-1.19", "1.19.4", "forge", "17.0.1+12"},
				{"master-1.20.1", "1.20.1", "forge", "17.0.1+12"},
				{"master-1.20-neo", "1.20.1", "neoforge", "17.0.1+12"},
				{"master-1.20.6", "1.20.6", "forge", "21.0.8+9"},
				{"master-1.20.6-neo", "1.20.6", "neoforge", "21.0.8+9"},
				{"master-1.21.1", "1.21.1", "forge", "21.0.8+9"},
				{"master-1.21.1-neo", "1.21.1", "neoforge", "21.0.8+9"},
				{"master-1.21.11", "1.21.11", "forge", "21.0.8+9"},
				{"master-1.21.11-neo", "1.21.11", "neoforge", "21.0.8+9"},
				{"master-26.1.2", "26.1.2", "forge", "25.0.3+9"},
				{"master-26.1.2-neo", "26.1.2", "neoforge", "25.0.3+9"},
				{"master-26.2", "26.2", "forge", "25.0.3+9"},
				{"master-26.2-neo", "26.2", "neoforge", "25.0.3+9"},
				{"master-26.3", "26.3", "forge", "25.0.3+9"},
				{"master-26.3-neo", "26.3", "neoforge", "25.0.3+9"}
		};

		for (final String[] target : targets) {
			final Result result = preflight(target[0], metadata(target[1], target[2], target[3]));

			assertEquals(0, result.exitCode, target[0] + "\n" + result.log);
			assertTrue(result.outputs.contains("target_branch=" + target[0]));
			assertTrue(result.outputs.contains("loader_name=" + target[2]));
		}
	}

	@Test
	void rejectsWrongVersionsProjectsLoadersAndJavaSelectors() throws Exception {
		final String[][] invalid = {
				{"mod_version", "4.1.0.113021"},
				{"curseforge_project_id", "245586"},
				{"loader_code", "2"},
				{"java_setup_version", "17.0.1+12"},
				{"gradle_java_setup_version", "8.0.502+7"},
				{"orespawn_curse_project_id", "1"},
				{"cf_requirements", "mmdlib"},
				{"cf_requirements", "unknown-mod"}
		};

		for (final String[] change : invalid) {
			final Map<String, String> properties = metadata("1.13.2", "forge", "8.0.502+7");
			properties.put(change[0], change[1]);

			final Result result = preflight("master-1.13.2", properties);

			assertTrue(result.exitCode != 0, change[0] + " was accepted\n" + result.log);
		}
	}

	@Test
	void requiresASuccessfulPriorBuild() throws Exception {
		final Map<String, String> environment = new LinkedHashMap<>();
		environment.put("TEST_CI_COUNT", "0");

		final Result result = preflight("master-1.13.2",
				metadata("1.13.2", "forge", "8.0.502+7"), environment);

		assertTrue(result.exitCode != 0, result.log);
		assertTrue(result.log.contains("no successful Build, test, and audit check"), result.log);
	}

	@Test
	void refusesToReleaseFromAFork() throws Exception {
		final Map<String, String> environment = new LinkedHashMap<>();
		environment.put("GITHUB_REPOSITORY", "SkyBlade1978/BaseMetals");

		final Result result = preflight("master-1.13.2",
				metadata("1.13.2", "forge", "8.0.502+7"), environment);

		assertTrue(result.exitCode != 0, result.log);
		assertTrue(result.log.contains("Releases can only run in"), result.log);
	}

	@Test
	void cleansNeoForgeSeparatelyAndKeepsTheForgeBuildInOneInvocation() throws Exception {
		final String gradleStub = "#!/usr/bin/env bash\nprintf '%s\\n' \"$*\" >> invocations.txt\n";
		Files.write(directory.resolve("gradlew"), gradleStub.getBytes(StandardCharsets.UTF_8));
		final String script = runBlock("Build, test, and audit once")
				.replace("${{ steps.java-paths.outputs.paths }}", "/jdk")
				.replace("${{ steps.dependencies.outputs.property }}", "orespawnVerificationRepository")
				.replace("${{ steps.dependencies.outputs.repository }}", "/mirror");

		for (final String loader : new String[] {"forge", "neoforge"}) {
			Files.deleteIfExists(directory.resolve("invocations.txt"));
			final Map<String, String> environment = new LinkedHashMap<>();
			environment.put("LOADER_NAME", loader);

			final Result result = execute(script, environment);
			final String[] invocations = read(directory.resolve("invocations.txt")).trim().split("\n");

			assertEquals(0, result.exitCode, result.log);
			assertEquals("neoforge".equals(loader) ? 2 : 1, invocations.length);
			assertTrue(invocations[0].startsWith("clean "));
			assertTrue(invocations[invocations.length - 1].contains("check build javadoc verifyReleaseArtifacts"));
			for (final String invocation : invocations) {
				assertTrue(invocation.contains("-PorespawnVerificationRepository=/mirror"));
			}
		}
	}

	private Result preflight(final String branch, final Map<String, String> properties) throws Exception {
		return preflight(branch, properties, new LinkedHashMap<>());
	}

	private Result preflight(final String branch, final Map<String, String> properties,
			final Map<String, String> overrides) throws Exception {
		final StringBuilder metadata = new StringBuilder();
		properties.forEach((key, value) -> metadata.append(key).append('=').append(value).append('\n'));
		Files.write(directory.resolve("gradle.properties"), metadata.toString().getBytes(StandardCharsets.UTF_8));

		// Run the workflow's Bash, but never contact GitHub or create a real tag.
		final String stubs = "gh() {\n"
				+ " case \"$1 $2\" in\n"
				+ "  \"api repos/$GITHUB_REPOSITORY/branches/$TEST_BRANCH\") return 0 ;;\n"
				+ "  \"api repos/$GITHUB_REPOSITORY/commits/\"*\"/check-runs?per_page=100\") echo \"$TEST_CI_COUNT\" ;;\n"
				+ "  \"api repos/$GITHUB_REPOSITORY/branches/\"*|\"api repos/$GITHUB_REPOSITORY/git/ref/tags/\"*) return 1 ;;\n"
				+ "  *) echo \"Unexpected GitHub call: $*\" >&2; return 99 ;;\n"
				+ " esac\n}\n"
				+ "git() { [[ \"$*\" == 'rev-parse HEAD' ]] || return 99; echo 0123456789012345678901234567890123456789; }\n";
		final String script = stubs + runBlock("Resolve target branch from version") + "\n"
				+ runBlock("Validate version, target, tag, and prior CI")
						.replace("${{ steps.route.outputs.target_branch }}", branch);
		final Map<String, String> environment = new LinkedHashMap<>();
		environment.put("GITHUB_REPOSITORY", "MinecraftModDevelopmentMods/BaseMetals");
		environment.put("RELEASE_VERSION", version(properties.get("minecraft_version"),
				properties.get("loader_name")));
		environment.put("REQUESTED_VERSION", environment.get("RELEASE_VERSION"));
		environment.put("TEST_BRANCH", branch);
		environment.put("TEST_CI_COUNT", "1");
		environment.putAll(overrides);

		return execute(script, environment);
	}

	private Result execute(final String script, final Map<String, String> environment) throws Exception {
		final Path scriptFile = directory.resolve("preflight.sh");
		final Path outputFile = directory.resolve("outputs.txt");
		final Path logFile = directory.resolve("log.txt");
		Files.write(scriptFile, script.getBytes(StandardCharsets.UTF_8));
		Files.deleteIfExists(outputFile);

		final ProcessBuilder builder = new ProcessBuilder(bash(), scriptFile.toString().replace('\\', '/'));
		builder.directory(directory.toFile());
		builder.redirectErrorStream(true);
		builder.redirectOutput(logFile.toFile());
		builder.environment().put("GITHUB_OUTPUT", outputFile.toString().replace('\\', '/'));
		builder.environment().putAll(environment);

		final Process process = builder.start();
		if (!process.waitFor(15, TimeUnit.SECONDS)) {
			process.destroyForcibly();
			throw new AssertionError("Release preflight timed out");
		}

		return new Result(process.exitValue(), read(logFile), Files.exists(outputFile) ? read(outputFile) : "");
	}

	private static Map<String, String> metadata(final String minecraft, final String loader, final String java) {
		final Map<String, String> properties = new LinkedHashMap<>();
		final String javaMajor = java.split("\\.")[0];
		properties.put("mod_version", version(minecraft, loader));
		properties.put("minecraft_version", minecraft);
		properties.put("loader_name", loader);
		properties.put("loader_code", "neoforge".equals(loader) ? "2" : "1");
		properties.put("java_version", javaMajor);
		properties.put("java_toolchain_version", java);
		properties.put("gradle_java_version", Integer.parseInt(javaMajor) < 17 ? "17" : javaMajor);
		properties.put("curseforge_project_id", "240967");
		properties.put("orespawn_curse_project_id", "245586");
		return properties;
	}

	private static String version(final String minecraft, final String loader) {
		final String[] parts = minecraft.split("\\.");
		return String.format("3.0.1.%s%02d%02d%s", parts[0], Integer.parseInt(parts[1]),
				parts.length > 2 ? Integer.parseInt(parts[2]) : 0, "neoforge".equals(loader) ? "2" : "1");
	}

	private static String runBlock(final String step) throws IOException {
		final String workflow = read(Paths.get(".github/workflows/deploy-release.yml"));
		final int start = workflow.indexOf("      - name: " + step + "\n");
		assertTrue(start >= 0, "Missing workflow step " + step);
		final int body = workflow.indexOf("        run: |\n", start) + "        run: |\n".length();
		final StringBuilder script = new StringBuilder();

		for (final String line : workflow.substring(body).split("\n", -1)) {
			if (!line.isEmpty() && !line.startsWith("          ")) {
				break;
			}
			script.append(line.isEmpty() ? "" : line.substring(10)).append('\n');
		}

		assertFalse(script.toString().trim().isEmpty(), "Empty workflow step " + step);
		return script.toString();
	}

	private static String bash() {
		if (System.getProperty("os.name").startsWith("Windows")) {
			final Path gitBash = Paths.get(System.getenv("ProgramFiles"), "Git", "bin", "bash.exe");
			if (Files.isRegularFile(gitBash)) {
				return gitBash.toString();
			}
		}
		return "bash";
	}

	private static String read(final Path path) throws IOException {
		return new String(Files.readAllBytes(path), StandardCharsets.UTF_8).replace("\r\n", "\n");
	}

	private static final class Result {
		private final int exitCode;
		private final String log;
		private final String outputs;

		private Result(final int exitCode, final String log, final String outputs) {
			this.exitCode = exitCode;
			this.log = log;
			this.outputs = outputs;
		}
	}
}
