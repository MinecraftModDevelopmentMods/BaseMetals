package zone.moddev.mc.basemetals.compat;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

class LegacyNamespaceTransformerTests {

	@Test
	void linksOldTypesFieldsInheritanceAndReflectionToCurrentClasses() {
		final byte[] output = new LegacyNamespaceTransformer().transform("example.Consumer", null, fixture());
		final ClassNode type = new ClassNode();
		new ClassReader(output).accept(type, 0);
		assertEquals("example/Consumer", type.name);
		assertEquals("zone/moddev/mc/basemetals/proxy/CommonProxy", type.superName);
		assertEquals("Lzone/moddev/mc/basemetals/BaseMetals;", ((FieldNode) type.fields.get(0)).desc);

		final MethodNode method = (MethodNode) type.methods.get(0);
		assertEquals("zone/moddev/mc/basemetals/BaseMetals", ((FieldInsnNode) method.instructions.get(0)).owner);
		assertEquals("zone.moddev.mc.basemetals.init.Materials", ((LdcInsnNode) method.instructions.get(2)).cst);
		assertEquals(Type.getObjectType("zone/moddev/mc/basemetals/util/Config$Options"),
				((LdcInsnNode) method.instructions.get(4)).cst);
		assertEquals("other.mod/textures/ore.png", ((LdcInsnNode) method.instructions.get(6)).cst);
	}

	@Test
	void doesNotRelocateAddonOwnedClassesInsideTheHistoricalPackage() {
		final ClassWriter source = new ClassWriter(0);
		source.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, "com/mcmoddev/basemetals/addon/Extra", null,
				"com/mcmoddev/basemetals/proxy/CommonProxy", null);
		source.visitEnd();

		final ClassNode type = new ClassNode();
		new ClassReader(new LegacyNamespaceTransformer().transform("com.mcmoddev.basemetals.addon.Extra", null,
				source.toByteArray())).accept(type, 0);
		assertEquals("com/mcmoddev/basemetals/addon/Extra", type.name);
		assertEquals("zone/moddev/mc/basemetals/proxy/CommonProxy", type.superName);
	}

	@Test
	void leavesCanonicalClassesAndTheCyanoBridgeAlone() {
		final LegacyNamespaceTransformer transformer = new LegacyNamespaceTransformer();
		final byte[] bytes = fixture();
		assertSame(bytes, transformer.transform("zone.moddev.mc.basemetals.BaseMetals", null, bytes));
		assertNull(transformer.transform("missing", "missing", null));

		final ClassWriter source = new ClassWriter(0);
		source.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, "example/Golem", null, "java/lang/Object", null);
		source.visitField(Opcodes.ACC_PUBLIC, "old", "Lcyano/basemetals/init/Blocks;", null, null).visitEnd();
		source.visitEnd();
		final byte[] golem = source.toByteArray();
		assertSame(golem, transformer.transform("example.Golem", null, golem));
	}

	private static byte[] fixture() {
		final ClassWriter type = new ClassWriter(0);
		type.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, "example/Consumer", null,
				"com/mcmoddev/basemetals/proxy/CommonProxy", null);
		type.visitField(Opcodes.ACC_PUBLIC, "mod", "Lcom/mcmoddev/basemetals/BaseMetals;", null, null).visitEnd();

		final MethodVisitor method = type.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "link", "()V", null, null);
		method.visitCode();
		method.visitFieldInsn(Opcodes.GETSTATIC, "com/mcmoddev/basemetals/BaseMetals", "instance", "Lcom/mcmoddev/basemetals/BaseMetals;");
		method.visitInsn(Opcodes.POP);
		method.visitLdcInsn("com.mcmoddev.basemetals.init.Materials");
		method.visitInsn(Opcodes.POP);
		method.visitLdcInsn(Type.getObjectType("com/mcmoddev/basemetals/util/Config$Options"));
		method.visitInsn(Opcodes.POP);
		method.visitLdcInsn("other.mod/textures/ore.png");
		method.visitInsn(Opcodes.POP);
		method.visitInsn(Opcodes.RETURN);
		method.visitMaxs(1, 0);
		method.visitEnd();
		type.visitEnd();
		return type.toByteArray();
	}
}
