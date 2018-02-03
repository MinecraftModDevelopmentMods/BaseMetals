package com.mcmoddev.lib.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import com.mcmoddev.lib.util.Platform;

import net.minecraft.launchwrapper.IClassTransformer;

public class ASMTransformer implements IClassTransformer {

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytesIn) {
		ClassNode classNode = null;
		for (final ITransformer transformer : ASMPlugin.transformerList) {
			if (transformedName.equals(transformer.getTarget())) {
				if (classNode == null) {
					final ClassReader classReader = new ClassReader(bytesIn);
					classNode = new ClassNode();
					classReader.accept(classNode, 0);
				}
				classNode = transformer.transform(classNode, Platform.isDevEnv());
			}
		}
		if (classNode != null) {
			final ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			classNode.accept(classWriter);
			return classWriter.toByteArray();
		}
		return bytesIn;
	}
}
