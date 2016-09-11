package cyano.basemetals.jei;

import java.util.Collections;
import java.util.List;

import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;

/**
 * 
 * @author Jasmine Iwanek
 *
 */
public class CrusherRecipeJEI  extends BlankRecipeWrapper {

	private final List<List<ItemStack>> input;
	private final List<ItemStack> outputs;
	
	/**
	 * 
	 * @param input
	 * @param output
	 */
	public CrusherRecipeJEI(List<ItemStack> input, ItemStack output) {
		this.input = Collections.singletonList(input);
		this.outputs = Collections.singletonList(output);
	}

	@Override
	public List<List<ItemStack>> getInputs() {
		return input;
	}

	@Override
	public List<ItemStack> getOutputs() {
		return outputs;
	}
}
