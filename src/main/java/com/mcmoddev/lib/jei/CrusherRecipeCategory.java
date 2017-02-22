package com.mcmoddev.lib.jei;

import javax.annotation.Nonnull;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

@SuppressWarnings({ "rawtypes", "deprecation" })
public class CrusherRecipeCategory implements IRecipeCategory {

	@Nonnull public static final String UID = BaseMetalsJEIPlugin.JEIUID + ".crackhammer";

	private final String title;
	private final IDrawable background;
	private final IDrawable icon;
	protected final IDrawableAnimated hammer;

	public CrusherRecipeCategory(IGuiHelper guiHelper) {
		title = I18n.translateToLocalFormatted(UID);
		ResourceLocation resourceLocation = new ResourceLocation("basemetals", "textures/jei/JEIhammeroverlay.png");
		background = guiHelper.createDrawable(resourceLocation, 0, 0, 166, 130);
		icon = guiHelper.createDrawable(resourceLocation, 170, 2, 16, 16);

		IDrawableStatic hammerDrawable = guiHelper.createDrawable(resourceLocation, 169, 17, 32, 32);
		hammer = guiHelper.createAnimatedDrawable(hammerDrawable, 200, IDrawableAnimated.StartDirection.BOTTOM, false);
	}

	@Nonnull
	@Override
	public String getUid() {
		return UID;
	}

	@Nonnull
	@Override
	public String getTitle() {
		return title;
	}

	@Nonnull
	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Nonnull
	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void drawExtras(Minecraft minecraft) {
		// TODO Auto-generated method stub
	}

	@Override
	public void drawAnimations(Minecraft minecraft) {
		hammer.draw(minecraft, 71, 6);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper) {
		// deprecated
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper, IIngredients ingredients) {
		// custom tooltips
		recipeLayout.getItemStacks().addTooltipCallback((CrusherRecipeWrapper) recipeWrapper);

		// input
		recipeLayout.getItemStacks().init(0, true, 40, 14);
		recipeLayout.getItemStacks().set(0, (ItemStack) recipeWrapper.getInputs().get(0));

		// output
		recipeLayout.getItemStacks().init(2, false, 119, 14);
		recipeLayout.getItemStacks().set(2, (ItemStack) recipeWrapper.getOutputs().get(0));
	}
}
