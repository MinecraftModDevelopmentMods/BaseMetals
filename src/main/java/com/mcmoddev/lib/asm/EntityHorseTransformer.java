package com.mcmoddev.lib.asm;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

class EntityHorseTransformer implements ITransformer {

	private static final String ASMHOOKS_INTERFACE = "com/mcmoddev/lib/asm/ASMHooks";
	private static final String entityInit = "func_70088_a";
	private static final String setHorseArmorStack = "func_146086_d";
	private static final String setHorseTexturePaths = "func_110247_cG";
	private static final String getTextureName = "func_188574_d";
	
	@Override
	public String getTarget() {
		return "net.minecraft.entity.passive.EntityHorse";
	}

	@Override
	public void transform(ClassNode node, boolean dev) {
		for (final MethodNode methodNode : node.methods) {
			if (entityInit.equals(methodNode.name)) {
				for (final AbstractInsnNode insnNode : methodNode.instructions.toArray()) {
					if (insnNode.getOpcode() == RETURN) {
						final InsnList inject = new InsnList();
						inject.add(new VarInsnNode(ALOAD, 0));
						inject.add(new MethodInsnNode(INVOKESTATIC, ASMHOOKS_INTERFACE, "onInitHorse", "(Lnet/minecraft/entity/passive/EntityHorse;)V", false));
						methodNode.instructions.insertBefore(insnNode, inject);
					}
				}
			} else if (setHorseArmorStack.equals(methodNode.name)) {
				final InsnList inject = new InsnList();
				inject.add(new VarInsnNode(ALOAD, 0));
				inject.add(new VarInsnNode(ALOAD, 1));
				inject.add(new MethodInsnNode(INVOKESTATIC, ASMHOOKS_INTERFACE, "setHorseArmorStack", "(Lnet/minecraft/entity/passive/EntityHorse;Lnet/minecraft/item/ItemStack;)V", false));
				methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), inject);
			} else if (setHorseTexturePaths.equals(methodNode.name)) {
				for (final AbstractInsnNode insnNode : methodNode.instructions.toArray()) {
					if (insnNode instanceof MethodInsnNode && getTextureName.equals(((MethodInsnNode) insnNode).name)) {
						final InsnList inject = new InsnList();
						inject.add(new VarInsnNode(ALOAD, 0));
						inject.add(new MethodInsnNode(INVOKESTATIC, ASMHOOKS_INTERFACE, "getTextureName", "(Lnet/minecraft/entity/passive/HorseArmorType;Lnet/minecraft/entity/passive/EntityHorse;)Ljava/lang/String;", false));
						methodNode.instructions.insertBefore(insnNode, inject);
						methodNode.instructions.remove(insnNode);
					}
				}
			}
		}
	}
}
