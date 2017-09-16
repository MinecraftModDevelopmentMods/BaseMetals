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
		if (Platform.isDevEnv()) {
			File output = null; 
			FileOutputStream out = null;
			try {
				final File debugDir = new File("mmd/asm/debug/");
				if (debugDir.exists())
					debugDir.delete();
				debugDir.mkdirs();
				output = new File(debugDir, name + ".class");
				out = new FileOutputStream(output);
				debugDir.mkdirs();
				out.write(cw.toByteArray());
			} catch (final IOException ex) {
				BaseMetals.logger.error(ex);
			} finally {
				if( out != null ) {
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
