package cyano.basemetals.asm.transformer;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class EntityHorseTransformer implements ITransformer {

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
						inject.add(new MethodInsnNode(INVOKESTATIC, "cyano.basemetals/asm/BaseMetalHooks",
								"onInitHorse", "(Lnet/minecraft/entity/passive/EntityHorse;)V", false));
						methodNode.instructions.insertBefore(insnNode, inject);
					}
			} else if (methodNode.name.equals(dev ? "setHorseArmorStack" : "func_146086_d")) {
				final InsnList inject = new InsnList();
				inject.add(new VarInsnNode(ALOAD, 0));
				inject.add(new VarInsnNode(ALOAD, 1));
				inject.add(new MethodInsnNode(INVOKESTATIC, "cyano.basemetals/asm/BaseMetalHooks", "setHorseArmorStack",
						"(Lnet/minecraft/entity/passive/EntityHorse;Lnet/minecraft/item/ItemStack;)V", false));
				methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), inject);
			} else if (methodNode.name.equals(dev ? "setHorseTexturePaths" : "func_110247_cG"))
				for (final AbstractInsnNode insnNode : methodNode.instructions.toArray())
					if ((insnNode instanceof MethodInsnNode)
							&& ((MethodInsnNode) insnNode).name.equals(dev ? "getTextureName" : "func_188574_d")) {
						final InsnList inject = new InsnList();
						inject.add(new VarInsnNode(ALOAD, 0));
						inject.add(new MethodInsnNode(INVOKESTATIC, "cyano.basemetals/asm/BaseMetalHooks",
								"getTextureName",
								"(Lnet/minecraft/entity/passive/HorseType;Lnet/minecraft/entity/passive/EntityHorse;)Ljava/lang/String;",
								false));
						methodNode.instructions.insertBefore(insnNode, inject);
						methodNode.instructions.remove(insnNode);
					}
	}
}
