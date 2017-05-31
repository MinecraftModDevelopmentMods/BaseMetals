package com.mcmoddev.lib.asm;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

public interface ITransformer extends Opcodes {

	String getTarget();

	void transform(ClassNode node, boolean dev);
}
