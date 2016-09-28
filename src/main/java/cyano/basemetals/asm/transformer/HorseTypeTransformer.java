package cyano.basemetals.asm.transformer;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class HorseTypeTransformer implements ITransformer {

	@Override
	public String getTarget() {
		return "net.minecraft.entity.passive.HorseType";
	}

	@Override
	public void transform(ClassNode node, boolean dev) {
		for (final MethodNode methodNode : node.methods)
			if (methodNode.name.equals(dev ? "getByItem" : "func_188576_a")) {
				final InsnList inject = new InsnList();
				inject.add(new VarInsnNode(ALOAD, 0));
				inject.add(new TypeInsnNode(INSTANCEOF, "cyano.basemetals/items/IHorseArmor"));
				final LabelNode label1 = new LabelNode();
				inject.add(new JumpInsnNode(IFEQ, label1));
				inject.add(new VarInsnNode(ALOAD, 0));
				inject.add(new TypeInsnNode(CHECKCAST, "cyano.basemetals/items/IHorseArmor"));
				inject.add(new MethodInsnNode(INVOKEINTERFACE, "cyano.basenmetals/items/IHorseArmor", "getArmorType",
						"()Lnet/minecraft/entity/passive/HorseType;", true));
				inject.add(new InsnNode(ARETURN));
				inject.add(label1);
				methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), inject);
			}
	}
}
