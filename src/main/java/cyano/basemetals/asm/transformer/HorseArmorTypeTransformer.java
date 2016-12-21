package cyano.basemetals.asm.transformer;

import astro.lib.api.integration.ITransformer;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

public class HorseArmorTypeTransformer implements ITransformer {

	@Override
	@Nonnull
	public String getTarget() {
		return "net.minecraft.entity.passive.HorseArmorType";
	}

	@Override
	public void transform(ClassNode node, boolean dev) {
		node.methods.stream().filter(methodNode -> methodNode.name.equals(dev ? "getByItem" : "func_188576_a")).forEachOrdered(methodNode -> {
			final InsnList inject = new InsnList();
			inject.add(new VarInsnNode(ALOAD, 0));
			inject.add(new TypeInsnNode(INSTANCEOF, "astro/lib/api/item/IHorseArmor"));
			final LabelNode label1 = new LabelNode();
			inject.add(new JumpInsnNode(IFEQ, label1));
			inject.add(new VarInsnNode(ALOAD, 0));
			inject.add(new TypeInsnNode(CHECKCAST, "astro/lib/api/item/IHorseArmor"));
			inject.add(new MethodInsnNode(INVOKEINTERFACE, "astro/lib/api/item/IHorseArmor", "getArmorType",
					"()Lnet/minecraft/entity/passive/HorseArmorType;", true));
			inject.add(new InsnNode(ARETURN));
			inject.add(label1);
			methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), inject);
		});
	}
}