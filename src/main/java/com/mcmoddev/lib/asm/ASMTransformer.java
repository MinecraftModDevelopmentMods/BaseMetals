package com.mcmoddev.lib.asm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.util.Platform;

import net.minecraft.launchwrapper.IClassTransformer;

public class ASMTransformer implements IClassTransformer {

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		for (final ITransformer transformer : ASMPlugin.transformerList) {
			if (transformedName.equals(transformer.getTarget())) {
				final ClassReader classReader = new ClassReader(bytes);
				final ClassNode classNode = new ClassNode();
				classReader.accept(classNode, 0);
				transformer.transform(classNode, Platform.isDevEnv());
				final ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
				classNode.accept(classWriter);
				this.saveBytecode(transformedName, classWriter);
				bytes = classWriter.toByteArray();
			}
		}
		return bytes;
	}

	private void saveBytecode(String name, ClassWriter cw) {
		// this checks whether we are in a de-obfuscated environment
		// but we are calling it in an ASM transformer running
		// @SortingIndex(1001) so it will always be de-obfuscated
		if (Platform.isDevEnv()) {
			File output = null;
			FileOutputStream out = null;
			try {
				final File debugDir = new File("mmd/asm/debug/");
				if (debugDir.exists())
					debugDir.delete();
				debugDir.mkdirs(); // moved this as next line was throwing an exception on first execution
				output = new File(debugDir, name + ".class");
				out = new FileOutputStream(output);
				out.write(cw.toByteArray());
			} catch (final IOException ex) {
				BaseMetals.logger.error(ex);
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (IOException ex) {
						BaseMetals.logger.error(ex);
					}
				}
			}
		}
	}
}
