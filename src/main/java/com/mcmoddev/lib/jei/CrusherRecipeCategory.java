package com.mcmoddev.lib.jei;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.util.Translator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CrusherRecipeCategory extends BlankRecipeCategory<IRecipeWrapper> {

	private final ResourceLocation resourceLocation = new ResourceLocation("basemetals", "textures/jei/JEIhammeroverlay.png");
	private final IDrawable background;
	private final IDrawable icon;
	protected final IDrawableAnimated hammer;
	private final ICraftingGridHelper craftingGridHelper;
	
	public CrusherRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(resourceLocation, 0, 0, 166, 130);
		icon = guiHelper.createDrawable(resourceLocation, 170, 2, 16, 16);

		IDrawableStatic hammerDrawable = guiHelper.createDrawable(resourceLocation, 169, 17, 32, 32);
		hammer = guiHelper.createAnimatedDrawable(hammerDrawable, 200, IDrawableAnimated.StartDirection.BOTTOM, false);
		craftingGridHelper = guiHelper.createCraftingGridHelper(0, 1);
	}

	@Override
	public String getUid() {
		return BaseMetalsJEIPlugin.JEIUID + ".crackhammer";
	}

	@Override
	public String getTitle() {
		return Translator.translateToLocal(getUid());
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Nullable
	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		
		// init the input slot
		guiItemStacks.init(0, true, 40, 14);
		
		// init the output slot
		guiItemStacks.init(1, false, 119, 14);

		// load the output and input bits
		List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
		List<List<ItemStack>> outputs = ingredients.getOutputs(ItemStack.class);
		
		// setup the data
		craftingGridHelper.setInputs(guiItemStacks, inputs);
		guiItemStacks.set(1, outputs.get(0));
	}
}
