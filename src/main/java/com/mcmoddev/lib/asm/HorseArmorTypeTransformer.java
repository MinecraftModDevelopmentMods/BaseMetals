package com.mcmoddev.lib.asm;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

class HorseArmorTypeTransformer implements ITransformer {

	private static final String HORSE_INTERFACE = "com/mcmoddev/lib/common/item/IHorseArmor";
	private static final String[] GET_BY_ITEM = { "func_188576_a", "getByItem" };

	@Override
	public String getTarget() {
		return "net.minecraft.entity.passive.HorseArmorType";
	}

	@Override
	public ClassNode transform(final ClassNode node, final boolean dev) {
		final Visitor vis = new Visitor();
		node.accept(vis);
		return vis;
	}

	private static class Visitor extends ClassNodeBase {

		@Override
		public MethodVisitor visitMethod(final int access, final String name, final String desc,
				final String signature, final String[] exceptions) {
			final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
			if (ITransformer.oneEquals(GET_BY_ITEM, name)) {
				return new MethodVisitor(Opcodes.ASM5, mv) {

					@Override
					public void visitCode() {
						super.visitCode();
						this.mv.visitVarInsn(ALOAD, 0);
						this.mv.visitTypeInsn(INSTANCEOF, HORSE_INTERFACE);
						final Label label1 = new Label();
						this.mv.visitJumpInsn(IFEQ, label1);
						this.mv.visitVarInsn(ALOAD, 0);
						this.mv.visitTypeInsn(CHECKCAST, HORSE_INTERFACE);
						this.mv.visitInsn(Opcodes.ACONST_NULL);
						this.mv.visitMethodInsn(INVOKEINTERFACE, HORSE_INTERFACE,
								"getHorseArmorType",
								"(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/passive/HorseArmorType;",
								true);
						this.mv.visitInsn(ARETURN);
						this.mv.visitLabel(label1);
						this.mv.visitFrame(F_SAME, 0, null, 0, null);
					}
				};
			}
			return mv;
		}
	}
}
