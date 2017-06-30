package com.mcmoddev.lib.asm;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

class HorseArmorTypeTransformer implements ITransformer {

	private static final String HORSE_INTERFACE = "com/mcmoddev/lib/common/item/IHorseArmor";
	private static final String GET_BY_ITEM = "func_188576_a";
	
	@Override
	public String getTarget() {
		return "net.minecraft.entity.passive.HorseArmorType";
	}

	@Override
	public void transform(ClassNode node, boolean dev) {
		node.methods.stream().filter(methodNode -> GET_BY_ITEM.equals(methodNode.name)).forEachOrdered(methodNode -> {
			final InsnList inject = new InsnList();
			inject.add(new VarInsnNode(ALOAD, 0));
			inject.add(new TypeInsnNode(INSTANCEOF, HORSE_INTERFACE));
			final LabelNode label1 = new LabelNode();
			inject.add(new JumpInsnNode(IFEQ, label1));
			inject.add(new VarInsnNode(ALOAD, 0));
			inject.add(new TypeInsnNode(CHECKCAST, HORSE_INTERFACE));
			inject.add(new MethodInsnNode(INVOKEINTERFACE, HORSE_INTERFACE, "getArmorType", "()Lnet/minecraft/entity/passive/HorseArmorType;", true));
			inject.add(new InsnNode(ARETURN));
			inject.add(label1);
			methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), inject);
		});
	}
}