package com.mcmoddev.lib.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

/**
 * Created by Thiakil on 11/01/2018.
 */
public abstract class ClassNodeBase extends ClassNode {
	public ClassNodeBase(){
		super(Opcodes.ASM5);
	}
}
