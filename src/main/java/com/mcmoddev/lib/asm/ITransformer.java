package com.mcmoddev.lib.asm;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

public interface ITransformer extends Opcodes {

	String getTarget();

	ClassNode transform(ClassNode node, boolean dev);

	/**
	 *
	 * @param names
	 * @param target
	 * @return
	 */
	public static boolean oneEquals(final String[] names, final String target) {
		for (final String name : names) {
			if (target.equals(name)) {
				return true;
			}
		}
		return false;
	}
}
