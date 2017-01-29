package com.mcmoddev.basemetals.asm;

import astro.lib.api.integration.ITransformer;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BaseMetalTransformer implements IClassTransformer {

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		for (final ITransformer transformer : BaseMetalPlugin.transformerList) {
			if (transformedName.equals(transformer.getTarget())) {
				final ClassReader classReader = new ClassReader(bytes);
				final ClassNode classNode = new ClassNode();
				classReader.accept(classNode, 0);
				transformer.transform(classNode, BaseMetalPlugin.inDevelopment);
				final ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
				classNode.accept(classWriter);
				this.saveBytecode(transformedName, classWriter);
				bytes = classWriter.toByteArray();
			}
		}
		return bytes;
	}

	private void saveBytecode(String name, ClassWriter cw) {
		if (BaseMetalPlugin.inDevelopment)
			try {
				final File debugDir = new File("astro/asm/debug/");
				if (debugDir.exists())
					debugDir.delete();
				debugDir.mkdirs();
				final File output = new File(debugDir, name + ".class");
				final FileOutputStream out = new FileOutputStream(output);
				out.write(cw.toByteArray());
				out.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
	}
}
