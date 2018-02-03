package com.mcmoddev.lib.asm;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

public interface ITransformer extends Opcodes {

	String getTarget();

	ClassNode transform(ClassNode node, boolean dev);

	public static boolean oneEquals(String[] names, String target) {
		for (String n : names) {
			if (target.equals(n)) {
				return true;
			}
		}
		return false;
	}
}
