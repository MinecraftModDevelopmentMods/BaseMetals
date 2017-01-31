package com.mcmoddev.lib.asm;

import org.objectweb.asm.tree.*;

class EntityHorseTransformer implements ITransformer {

	@Override
	public String getTarget() {
		return "net.minecraft.entity.passive.EntityHorse";
	}

	@Override
	public void transform(ClassNode node, boolean dev) {
		for (final MethodNode methodNode : node.methods)
			if (methodNode.name.equals(dev ? "entityInit" : "func_70088_a")) {
				for (final AbstractInsnNode insnNode : methodNode.instructions.toArray())
					if (insnNode.getOpcode() == RETURN) {
						final InsnList inject = new InsnList();
						inject.add(new VarInsnNode(ALOAD, 0));
						inject.add(new MethodInsnNode(INVOKESTATIC, "com/mcmoddev/lib/asm/ASMHooks", "onInitHorse", "(Lnet/minecraft/entity/passive/EntityHorse;)V", false));
						methodNode.instructions.insertBefore(insnNode, inject);
					}
			} else if (methodNode.name.equals(dev ? "setHorseArmorStack" : "func_146086_d")) {
				final InsnList inject = new InsnList();
				inject.add(new VarInsnNode(ALOAD, 0));
				inject.add(new VarInsnNode(ALOAD, 1));
				inject.add(new MethodInsnNode(INVOKESTATIC, "com/mcmoddev/lib/asm/ASMHooks", "setHorseArmorStack", "(Lnet/minecraft/entity/passive/EntityHorse;Lnet/minecraft/item/ItemStack;)V", false));
				methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), inject);
			} else if (methodNode.name.equals(dev ? "setHorseTexturePaths" : "func_110247_cG"))
				for (final AbstractInsnNode insnNode : methodNode.instructions.toArray())
					if (insnNode instanceof MethodInsnNode && ((MethodInsnNode) insnNode).name.equals(dev ? "getTextureName" : "func_188574_d")) {
						final InsnList inject = new InsnList();
						inject.add(new VarInsnNode(ALOAD, 0));
						inject.add(new MethodInsnNode(INVOKESTATIC, "com/mcmoddev/lib/asm/ASMHooks", "getTextureName", "(Lnet/minecraft/entity/passive/HorseArmorType;Lnet/minecraft/entity/passive/EntityHorse;)Ljava/lang/String;", false));
						methodNode.instructions.insertBefore(insnNode, inject);
						methodNode.instructions.remove(insnNode);
					}
	}
}
