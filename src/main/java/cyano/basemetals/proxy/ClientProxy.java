package cyano.basemetals.proxy;

import cyano.basemetals.BaseMetals;
import cyano.basemetals.client.renderer.RenderCustomArrow;
import cyano.basemetals.entity.EntityCustomArrow;
import cyano.basemetals.init.Blocks;
import cyano.basemetals.init.Fluids;
import cyano.basemetals.init.Items;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class ClientProxy extends CommonProxy {

	@Override
	public void preInit() {
		for (final String fluidBlockName : Fluids.getFluidBlockRegistry().keySet()) {
			final BlockFluidBase block = Fluids.getFluidBlockByName(fluidBlockName);
			final Item item = Item.getItemFromBlock(block);
			final ModelResourceLocation fluidModelLocation = new ModelResourceLocation(BaseMetals.MODID + ":" + fluidBlockName, "fluid");
			ModelBakery.registerItemVariants(item);
			ModelLoader.setCustomMeshDefinition(item, stack -> fluidModelLocation);
			ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
				@Override
				protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
					return fluidModelLocation;
				}
			});
		}

		RenderingRegistry.registerEntityRenderingHandler(EntityCustomArrow.class, RenderCustomArrow::new);
	}

	@Override
	public void init() {
		final ItemModelMesher itemModelMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

		for (final String itemName : Items.getItemRegistry().keySet()) {
			final Item item = Items.getItemByName(itemName);
			itemModelMesher.register(item, 0, new ModelResourceLocation(new ResourceLocation(BaseMetals.MODID, itemName), "inventory"));
		}

		for (final String blockName : Blocks.getBlockRegistry().keySet()) {
			final Block block = Blocks.getBlockByName(blockName);
			if (block instanceof BlockDoor)
				continue; // do not add door blocks
			itemModelMesher.register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(new ResourceLocation(BaseMetals.MODID, blockName), "inventory"));
		}
	}
}
