package cyano.basemetals.asm;

import cyano.basemetals.asm.transformer.ITransformer;
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
		for (ITransformer transformer : BaseMetalPlugin.transformerList) {
			if (transformedName.equals(transformer.getTarget())) {
				ClassReader classReader = new ClassReader(bytes);
				ClassNode classNode = new ClassNode();
				classReader.accept(classNode, 0);
				transformer.transform(classNode, BaseMetalPlugin.inDevelopment);
				ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
				classNode.accept(classWriter);
				this.saveBytecode(transformedName, classWriter);
				bytes = classWriter.toByteArray();
			}
		}
		return bytes;
	}

	private void saveBytecode(String name, ClassWriter cw) {
		try {
			File debugDir = new File("debug/");
			if (debugDir.exists()) {
				debugDir.delete();
			}
			debugDir.mkdirs();
			File output = new File(debugDir, name + ".class");
			FileOutputStream out = new FileOutputStream(output);
			out.write(cw.toByteArray());
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
