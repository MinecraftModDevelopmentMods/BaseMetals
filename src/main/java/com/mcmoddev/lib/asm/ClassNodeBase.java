package com.mcmoddev.lib.asm;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

public abstract class ClassNodeBase extends ClassNode {

	public ClassNodeBase() {
		super(Opcodes.ASM5);
	}
}
