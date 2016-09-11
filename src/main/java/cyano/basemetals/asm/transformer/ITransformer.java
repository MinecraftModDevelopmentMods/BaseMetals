package cyano.basemetals.asm.transformer;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public interface ITransformer extends Opcodes {

	/**
	 * 
	 * @return
	 */
	String getTarget();

	/**
	 *
	 * @param node
	 * @param dev
	 */
	void transform(ClassNode node, boolean dev);
}
