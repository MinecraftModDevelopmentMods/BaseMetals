package com.mcmoddev.lib.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

class EntityHorseTransformer implements ITransformer {

	private static final String ASMHOOKS_INTERFACE = "com/mcmoddev/lib/asm/ASMHooks";
	private static final String[] ENTITY_INIT = { "func_70088_a", "entityInit" };
	private static final String[] SET_HORSEARMOR_STACK = { "func_146086_d", "setHorseArmorStack" };
	private static final String[] SET_HORSE_TEXTURE_PATHS = { "func_110247_cG", "setHorseTexturePaths" };
	private static final String[] GET_TEXTURE_NAME = { "func_188574_d", "getTextureName" };

	@Override
	public String getTarget() {
		return "net.minecraft.entity.passive.EntityHorse";
	}

	@Override
	public ClassNode transform(final ClassNode node, final boolean dev) {
		final Visitor vis = new Visitor();
		node.accept(vis);
		return vis;
	}

	private static class Visitor extends ClassNodeBase {

		@Override
		public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
			final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
			if (ITransformer.oneEquals(ENTITY_INIT, name)) {
				return new MethodVisitor(Opcodes.ASM5, mv) {

					@Override
					public void visitInsn(final int opcode) {
						if (opcode == Opcodes.RETURN) {
							mv.visitVarInsn(ALOAD, 0);
							mv.visitMethodInsn(INVOKESTATIC, ASMHOOKS_INTERFACE, "onInitHorse",
									"(Lnet/minecraft/entity/passive/EntityHorse;)V", false);
						}
						mv.visitInsn(opcode);
					}
				};
			} else if (ITransformer.oneEquals(SET_HORSEARMOR_STACK, name)) {
				return new MethodVisitor(Opcodes.ASM5, mv) {

					@Override
					public void visitCode() {
						super.visitCode();
						mv.visitVarInsn(ALOAD, 0);
						mv.visitVarInsn(ALOAD, 1);
						mv.visitMethodInsn(INVOKESTATIC, ASMHOOKS_INTERFACE, "setHorseArmorStack",
								"(Lnet/minecraft/entity/passive/EntityHorse;Lnet/minecraft/item/ItemStack;)V", false);
					}
				};
			} else if (ITransformer.oneEquals(SET_HORSE_TEXTURE_PATHS, name)) {
				return new MethodVisitor(Opcodes.ASM5, mv) {

					@Override
					public void visitMethodInsn(final int opcode, final String owner, final String methodCallName, final String desc,
							final boolean itf) {
						if (ITransformer.oneEquals(GET_TEXTURE_NAME, methodCallName)) {
							mv.visitVarInsn(ALOAD, 0);
							mv.visitMethodInsn(INVOKESTATIC, ASMHOOKS_INTERFACE, "getTextureName",
									"(Lnet/minecraft/entity/passive/HorseArmorType;Lnet/minecraft/entity/passive/EntityHorse;)Ljava/lang/String;",
									false);
							return; // replace original call
						}
						super.visitMethodInsn(opcode, owner, methodCallName, desc, itf);
					}
				};
			}
			return mv;
		}
	}
}
