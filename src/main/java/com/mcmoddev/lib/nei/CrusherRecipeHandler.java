package com.mcmoddev.lib.nei;

import java.awt.Rectangle;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.opengl.GL11;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.registry.CrusherRecipeRegistry;
import com.mcmoddev.lib.registry.recipe.ICrusherRecipe;

import codechicken.nei.api.stack.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class CrusherRecipeHandler extends TemplateRecipeHandler {

	private static final String CRUSHER = "crusher";

	@Override
	public String getOverlayIdentifier() {
		return CRUSHER;
	}

	@Override
	public String getGuiTexture() {
		return BaseMetals.MODID + ":textures/gui/nei/nei_crusher.png";
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		final ICrusherRecipe recipe = CrusherRecipeRegistry.getInstance().getRecipeForInputItem(ingredient);
		if (recipe == null) {
			return;
		}
		this.arecipes.add(new CrusherPair(ingredient.copy(), recipe.getOutput()));
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		final List<ICrusherRecipe> recipes = CrusherRecipeRegistry.getInstance().getRecipesForOutputItem(result);
		if (recipes == null) {
			return; // no crusher recipes
		}
		for (final ICrusherRecipe r : recipes) {
			this.arecipes.addAll(r.getValidInputs().stream().map(input -> new CrusherPair(input.copy(), r.getOutput().copy())).collect(Collectors.toList()));
		}
	}

	@Override
	public void loadUsageRecipes(String inputId, Object... ingredients) {
		if (inputId.equals(CRUSHER) && (this.getClass() == CrusherRecipeHandler.class)) {
			this.loadCraftingRecipes("smelting");
		} else {
			super.loadUsageRecipes(inputId, ingredients);
		}
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId.equals(CRUSHER) && (this.getClass() == CrusherRecipeHandler.class)) { // we don't want overstep subclasses
			// add all crusher recipes
			final Collection<ICrusherRecipe> recipes = CrusherRecipeRegistry.getInstance().getAllRecipes();
			if (recipes == null) {
				return;
			}
			for (final ICrusherRecipe r : recipes) {
				if (r == null) {
					continue;
				}
				this.arecipes.addAll(r.getValidInputs().stream().map(input -> new CrusherPair(input.copy(), r.getOutput().copy())).collect(Collectors.toList()));
			}
		} else
			super.loadCraftingRecipes(outputId, results);
	}

	@Override
	public TemplateRecipeHandler newInstance() {
		return super.newInstance();
	}

	@Override
	public String getRecipeName() {
		final String key = "nei." + BaseMetals.MODID + ".recipehandler.crusher.name";
		if (I18n.canTranslate(key)) {
			return net.minecraft.client.resources.I18n.format(key);
		} else {
			return "Crusher";
		}
	}

	@Override
	public void loadTransferRects() {
		this.transferRects.add(new RecipeTransferRect(new Rectangle(95, 23, 18, 18), CRUSHER));
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return GuiCrusher.class;
	}

	/**
	 *
	 * @author Jasmine Iwanek
	 *
	 */
	public static class GuiCrusher extends GuiContainer {

		ResourceLocation guiDisplayImage = new ResourceLocation(BaseMetals.MODID + ":textures/gui/nei/nei_crusher.png");

		/**
		 *
		 * @param container
		 *            Container
		 */
		public GuiCrusher(Container container) {
			super(container);
		}

		@Override
		protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			final int x = (this.width - this.xSize) / 2;
			final int y = (this.height - this.ySize) / 2;
			this.mc.renderEngine.bindTexture(this.guiDisplayImage);
			this.drawTexturedModalRect(x, y, 0, 0, 176, 76); // x, y, textureOffsetX, textureOffsetY, width, height)
		}
	}

	/**
	 *
	 * @author Jasmine Iwanek
	 *
	 */
	public static class ContainerCrusher extends Container {

		@Override
		public boolean canInteractWith(EntityPlayer playerIn) {
			return true;
		}
	}

	/**
	 *
	 * @author Jasmine Iwanek
	 *
	 */
	public class CrusherPair extends CachedRecipe {

		PositionedStack ingred;
		PositionedStack result;

		/**
		 *
		 * @param ingred
		 *            ItemStack
		 * @param result
		 *            ItemStack
		 */
		public CrusherPair(ItemStack ingred, ItemStack result) {
			if ((ingred == null) || (result == null)) {
				BaseMetals.logger.warn("Added null item to NEI GUI: " + ingred + " -> " + result);
				if (ingred == null) {
					ingred = new ItemStack(Blocks.AIR);
				}
				if (result == null) {
					result = new ItemStack(Blocks.AIR);
				}
			}
			ingred.stackSize = 1;
			this.ingred = new PositionedStack(ingred, 65, 23);
			this.result = new PositionedStack(result, 123, 23);
		}

		@Override
		public List<PositionedStack> getIngredients() {
			return this.getCycledIngredients(CrusherRecipeHandler.this.cycleticks / 48,
					Collections.singletonList(this.ingred));
		}

		@Override
		public PositionedStack getResult() {
			return this.result;
		}
	}
}
