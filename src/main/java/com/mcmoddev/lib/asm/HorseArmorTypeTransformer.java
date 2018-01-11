package com.mcmoddev.lib.asm;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

class HorseArmorTypeTransformer implements ITransformer {

	private static final String HORSE_INTERFACE = "com/mcmoddev/lib/common/item/IHorseArmor";
	private static final String[] GET_BY_ITEM = {"func_188576_a", "getByItem"};
	
	@Override
	public String getTarget() {
		return "net.minecraft.entity.passive.HorseArmorType";
	}

	@Override
	public ClassNode transform(ClassNode node, boolean dev) {
		Visitor vis = new Visitor();
		node.accept(vis);
		return vis;
	}

	private static class Visitor extends ClassNodeBase {
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
			if (ITransformer.oneEquals(GET_BY_ITEM, name)){
				return new MethodVisitor(Opcodes.ASM5, mv) {
					@Override
					public void visitCode() {
						super.visitCode();
						mv.visitVarInsn(ALOAD, 0);
						mv.visitTypeInsn(INSTANCEOF, HORSE_INTERFACE);
						final Label label1 = new Label();
						mv.visitJumpInsn(IFEQ, label1);
						mv.visitVarInsn(ALOAD, 0);
						mv.visitTypeInsn(CHECKCAST, HORSE_INTERFACE);
						mv.visitMethodInsn(INVOKEINTERFACE, HORSE_INTERFACE, "getArmorType", "()Lnet/minecraft/entity/passive/HorseArmorType;", true);
						mv.visitInsn(ARETURN);
						mv.visitLabel(label1);
						mv.visitFrame(F_SAME, 0, null, 0, null);
					}
				};
			}
			return mv;
		}
	}
}
