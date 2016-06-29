package cyano.basemetals.nei;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.FMLLog;

import org.lwjgl.opengl.GL11;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cyano.basemetals.BaseMetals;
import cyano.basemetals.registry.CrusherRecipeRegistry;
import cyano.basemetals.registry.recipe.ICrusherRecipe;

public class CrusherRecipeHandler extends TemplateRecipeHandler {

	@Override
	public String getOverlayIdentifier() {
		return "crusher";
	}

	@Override
	public String getGuiTexture() {
		return BaseMetals.MODID+":textures/gui/nei/nei_crusher.png";
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		ICrusherRecipe recipe = CrusherRecipeRegistry.getInstance().getRecipeForInputItem(ingredient);
		if(recipe == null) return;
		arecipes.add(new CrusherPair(ingredient.copy(),recipe.getOutput()));
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		List<ICrusherRecipe> recipes = CrusherRecipeRegistry.getInstance().getRecipesForOutputItem(result);
		if(recipes == null) return; // no crusher recipes
		for(ICrusherRecipe r : recipes){
			for(ItemStack input : r.getValidInputs()){
				arecipes.add(new CrusherPair(input.copy(),r.getOutput().copy()));
			}
		}
	}

	@Override
	public void loadUsageRecipes(String inputId, Object... ingredients) {
		if (inputId.equals("crusher") && getClass() == CrusherRecipeHandler.class) {// we don't want overstep subclasses
			loadCraftingRecipes("smelting");
		} else {
			super.loadUsageRecipes(inputId, ingredients);
		}
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId.equals("crusher") && getClass() == CrusherRecipeHandler.class) {// we don't want overstep subclasses
			// add all crusher recipes
			Collection<ICrusherRecipe> recipes = CrusherRecipeRegistry.getInstance().getAllRecipes();
			if(recipes == null) return;
			for(ICrusherRecipe r : recipes) {
				if(r == null) continue;
				for(ItemStack input : r.getValidInputs()){
					arecipes.add(new CrusherPair(input.copy(),r.getOutput().copy()));
				}
			}
		} else {
			super.loadCraftingRecipes(outputId, results);
		}
	}

	@Override
	public TemplateRecipeHandler newInstance() {
		return super.newInstance();
	}

	@Override
	public String getRecipeName() {
		String key = "nei."+BaseMetals.MODID+".recipehandler.crusher.name";
		if(I18n.canTranslate(key)) {
			return I18n.translateToLocal(key);
		} else {
			return "Crusher";
		}
	}

	@Override
	public void loadTransferRects() {
		transferRects.add(new RecipeTransferRect(new Rectangle(95, 23, 18, 18), "crusher"));
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return GuiCrusher.class;
	}

	public static class GuiCrusher extends GuiContainer {

		ResourceLocation guiDisplayImage = new ResourceLocation(BaseMetals.MODID+":textures/gui/nei/nei_crusher.png");
		public GuiCrusher(Container container) {
			super(container);
		}

		@Override
		protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			int x = (width - xSize) / 2;
			int y = (height - ySize) / 2;
			this.mc.renderEngine.bindTexture(guiDisplayImage);
			this.drawTexturedModalRect(x, y, 0, 0, 176, 76); // x, y, textureOffsetX, textureOffsetY, width, height)
		}
	}
	
	public static class ContainerCrusher extends Container {

		@Override
		public boolean canInteractWith(EntityPlayer playerIn) {
			return true;
		}
	}
	
	public class CrusherPair extends CachedRecipe {

		public CrusherPair(ItemStack ingred, ItemStack result) {
			if(ingred == null || result == null){
				FMLLog.warning("Added null item to NEI GUI: "+ingred+" -> "+result);
				if(ingred == null) {
					ingred = new ItemStack(Blocks.AIR);
				}
				if(result == null) {
					result = new ItemStack(Blocks.AIR);
				}
			}
			ingred.stackSize = 1;
			this.ingred = new PositionedStack(ingred, 65, 23);
			this.result = new PositionedStack(result, 123, 23);
		}

		public List<PositionedStack> getIngredients() {
			return getCycledIngredients(cycleticks / 48, Arrays.asList(ingred));
		}

		public PositionedStack getResult() {
			return result;
		}

		PositionedStack ingred;
		PositionedStack result;
	}
}
