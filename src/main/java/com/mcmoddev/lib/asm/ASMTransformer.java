package com.mcmoddev.lib.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import com.mcmoddev.lib.util.Platform;

import net.minecraft.launchwrapper.IClassTransformer;

public class ASMTransformer implements IClassTransformer {

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		byte[] rv = bytes;
		for (final ITransformer transformer : ASMPlugin.transformerList) {
			if (transformedName.equals(transformer.getTarget())) {
				final ClassReader classReader = new ClassReader(rv);
				final ClassNode classNode = new ClassNode();
				classReader.accept(classNode, 0);
				transformer.transform(classNode, Platform.isDevEnv());
				final ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
				classNode.accept(classWriter);
				rv = classWriter.toByteArray();
			}
		}
		return rv;
	}
}
