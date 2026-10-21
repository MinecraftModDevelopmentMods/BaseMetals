package zone.moddev.mc.basemetals.compat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.commons.RemappingClassAdapter;

/** Relinks existing MMD add-ons to the same classes and registries as Base Metals. */
public final class LegacyNamespaceTransformer implements IClassTransformer {

	private static final String OLD = "com/mcmoddev/basemetals/";
	private static final String CURRENT = "zone/moddev/mc/basemetals/";
	private static final Set<String> EXPORTS = loadExports();

	@Override
	public byte[] transform(final String name, final String transformedName, final byte[] bytes) {
		if (bytes == null || name == null || name.startsWith("zone.moddev.mc.basemetals.")
				|| !(contains(bytes, OLD) || contains(bytes, OLD.replace('/', '.')))) {
			return bytes;
		}

		final ClassWriter output = new ClassWriter(0);
		final Remapper names = new Remapper() {
			@Override
			public String map(final String internalName) {
				return currentName(internalName);
			}

			@Override
			public Object mapValue(final Object value) {
				if (value instanceof String) {
					final String text = (String) value;
					final String internal = text.replace('.', '/');
					final String mapped = currentName(internal);
					if (mapped.equals(internal)) return value;
					return text.indexOf('/') < 0 ? mapped.replace('/', '.') : mapped;
				}
				return super.mapValue(value);
			}
		};

		new ClassReader(bytes).accept(new RemappingClassAdapter(output, names), 0);
		return output.toByteArray();
	}

	private static String currentName(final String name) {
		if (!name.startsWith(OLD)) return name;

		final String suffix = name.substring(OLD.length());
		final int nested = suffix.indexOf('$');
		final String owner = nested < 0 ? suffix : suffix.substring(0, nested);
		return EXPORTS.contains(owner) ? CURRENT + suffix : name;
	}

	private static Set<String> loadExports() {
		final Set<String> result = new HashSet<>();
		final InputStream stream = LegacyNamespaceTransformer.class.getClassLoader()
				.getResourceAsStream("META-INF/basemetals-legacy-api.list");
		if (stream == null) throw new IllegalStateException("Missing Base Metals API name list");

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (!line.isEmpty() && !line.startsWith("#")) result.add(line);
			}
		} catch (final IOException failure) {
			throw new IllegalStateException("Cannot read Base Metals API names", failure);
		}
		return Collections.unmodifiableSet(result);
	}

	private static boolean contains(final byte[] bytes, final String text) {
		for (int offset = 0; offset <= bytes.length - text.length(); offset++) {
			int index = 0;
			while (index < text.length() && bytes[offset + index] == text.charAt(index)) index++;
			if (index == text.length()) return true;
		}
		return false;
	}
}
