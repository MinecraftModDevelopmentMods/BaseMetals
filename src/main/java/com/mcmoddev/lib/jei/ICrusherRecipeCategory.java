package com.mcmoddev.lib.jei;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.mcmoddev.basemetals.BaseMetals;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

public class ICrusherRecipeCategory extends BlankRecipeCategory<ICrusherRecipeWrapper> {

	private final ResourceLocation resourceLocation = new ResourceLocation(BaseMetals.MODID, "textures/jei/JEIhammeroverlay.png");
	private final IDrawable background;
	private final IDrawable icon;
	protected final IDrawableAnimated hammer;

	public ICrusherRecipeCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createDrawable(resourceLocation, 0, 0, 166, 130);
		this.icon = guiHelper.createDrawable(resourceLocation, 170, 2, 16, 16);

		IDrawableStatic hammerDrawable = guiHelper.createDrawable(resourceLocation, 169, 17, 32, 32);
		this.hammer = guiHelper.createAnimatedDrawable(hammerDrawable, 200, IDrawableAnimated.StartDirection.BOTTOM, false);
	}

	@Override
	public String getUid() {
		return BaseMetalsJEIPlugin.RECIPE_UID;
	}

	@Override
	public String getModName() {
		return 	BaseMetalsJEIPlugin.JEI_UID;
	}

	@Override
	public String getTitle() {
		return new TextComponentTranslation(String.format(getUid())).getFormattedText();
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void drawExtras(Minecraft minecraft) {
		this.hammer.draw(minecraft, 71, 6);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, @Nonnull ICrusherRecipeWrapper recipeWrapper, IIngredients ingredients) {
		final IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		// init the input slot
		guiItemStacks.init(0, true, 40, 14);

		// init the output slot
		guiItemStacks.init(1, false, 119, 14);

		// load the output and input bits
		List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
		List<List<ItemStack>> outputs = ingredients.getOutputs(ItemStack.class);

		// setup the data
		guiItemStacks.set(0, inputs.get(0));
		guiItemStacks.set(1, outputs.get(0));
	}

	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		return Collections.<String>emptyList();
	}
}
