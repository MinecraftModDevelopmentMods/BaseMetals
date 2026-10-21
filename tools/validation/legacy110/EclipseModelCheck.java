package review.ide;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.gradle.tooling.model.build.BuildEnvironment;
import org.gradle.tooling.model.eclipse.EclipseProject;
import org.gradle.tooling.model.eclipse.EclipseSourceDirectory;
import org.gradle.tooling.model.gradle.GradleBuild;

/** Requests the same project models used by Eclipse Buildship's import wizard. */
public final class EclipseModelCheck {

    public static void main(final String[] args) throws Exception {
        final File project = new File(args[0]);
        final File cache = new File(args[1]);
        final File javaHome = new File(args[2]);

        try (ProjectConnection connection = GradleConnector.newConnector()
                .forProjectDirectory(project).useGradleUserHomeDir(cache)
                .useBuildDistribution().connect()) {
            final BuildEnvironment environment = connection.model(BuildEnvironment.class)
                    .setJavaHome(javaHome).withArguments("--offline").get();
            final GradleBuild build = connection.model(GradleBuild.class)
                    .setJavaHome(javaHome).withArguments("--offline").get();
            final EclipseProject eclipse = connection.model(EclipseProject.class)
                    .setJavaHome(javaHome).withArguments("--offline").get();

            if (!"9.6.1".equals(environment.getGradle().getGradleVersion())
                    || !"BaseMetals".equals(build.getRootProject().getName())
                    || !"BaseMetals".equals(eclipse.getName())
                    || eclipse.getSourceDirectories().stream().noneMatch(source -> "build/resources/main".equals(source.getPath()))
                    || !"1.8".equals(eclipse.getJavaSourceSettings().getSourceLanguageLevel().toString())) {
                throw new AssertionError("Wrong wrapper, project or Java source level in the Eclipse model");
            }
            eclipse.getClasspath().forEach(dependency -> {
                if (!dependency.getFile().isFile()) {
                    throw new AssertionError("Missing Eclipse library: " + dependency.getFile());
                }
            });
            if (eclipse.getSourceDirectories().stream().map(source -> source.getPath()).distinct().count()
                    != eclipse.getSourceDirectories().size()) {
                throw new AssertionError("Duplicate Eclipse source directories");
            }

            for (final EclipseSourceDirectory source : eclipse.getSourceDirectories()) {
                verifySourcePackages(new File(project, source.getPath()).toPath());
            }

            if (args.length > 3) {
                final List<String> libraries = new ArrayList<>();
                eclipse.getClasspath().forEach(dependency -> libraries.add(dependency.getFile().getAbsolutePath()));
                Files.write(new File(args[3]).toPath(), libraries, StandardCharsets.UTF_8);
            }

            System.out.println("BASE_METALS_ECLIPSE_MODEL_PASS gradle="
                    + environment.getGradle().getGradleVersion() + " source=1.8 libraries="
                    + eclipse.getClasspath().size() + " gradleJava="
                    + environment.getJava().getJavaHome());
        }
    }

    private static void verifySourcePackages(final Path root) throws IOException {
        if (!Files.isDirectory(root)) return;

        final Pattern packageDeclaration = Pattern.compile("(?m)^\\s*package\\s+([\\w.]+)\\s*;");
        final List<Path> sources;
        try (Stream<Path> files = Files.walk(root)) {
            sources = files.filter(path -> path.toString().endsWith(".java"))
                    .collect(Collectors.toList());
        }

        for (final Path file : sources) {
            final Path parent = root.relativize(file).getParent();
            final String expected = parent == null ? "" : parent.toString().replace(File.separatorChar, '.');
            final String text = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
            final Matcher declaration = packageDeclaration.matcher(text);
            final String declared = declaration.find() ? declaration.group(1) : "";

            if (!expected.equals(declared)) {
                throw new AssertionError("Package does not match the Eclipse source folder: " + file);
            }
        }
    }
}
