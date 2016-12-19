package cyano.basemetals.proxy;

import cyano.basemetals.BaseMetals;
import cyano.basemetals.client.renderer.RenderCustomArrow;
import cyano.basemetals.entity.EntityCustomArrow;
import cyano.basemetals.init.Blocks;
import cyano.basemetals.init.Fluids;
import cyano.basemetals.init.Items;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		for (final String name : Fluids.getFluidBlockRegistry().keySet()) {
			final Block block = Fluids.getFluidBlockByName(name);
			final Item item = Item.getItemFromBlock(block);
//			FMLLog.severe("FLUID RESOURCE DOMAIN IS " + item.getRegistryName().getResourceDomain());
//			FMLLog.severe("FLUID RESOURCE PATH IS " + item.getRegistryName().getResourcePath());
			if (!item.getRegistryName().getResourceDomain().equals(BaseMetals.MODID))
				continue;
//			FMLLog.severe("Registering");
			final ModelResourceLocation fluidModelLocation = new ModelResourceLocation(item.getRegistryName().getResourceDomain() + ":" + name, "fluid");
			ModelBakery.registerItemVariants(item);
			ModelLoader.setCustomMeshDefinition(item, stack -> fluidModelLocation);
			ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
				@Override
				protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
					return fluidModelLocation;
				}
			});
//			FMLLog.severe("Registered");
		}

		RenderingRegistry.registerEntityRenderingHandler(EntityCustomArrow.class, RenderCustomArrow::new);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		final ItemModelMesher itemModelMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

		for (final String name : Items.getItemRegistry().keySet()) {
			final Item item = Items.getItemByName(name);
//			FMLLog.severe("ITEM RESOURCE DOMAIN IS " + item.getRegistryName().getResourceDomain());
			if (!item.getRegistryName().getResourceDomain().equals(BaseMetals.MODID))
				continue;
//			FMLLog.severe("Registering");
			itemModelMesher.register(item, 0, new ModelResourceLocation(new ResourceLocation(item.getRegistryName().getResourceDomain(), name), "inventory"));
//			FMLLog.severe("Registered");
		}

		for (final String name : Blocks.getBlockRegistry().keySet()) {
			final Block block = Blocks.getBlockByName(name);
			if ((block instanceof BlockDoor) || (block instanceof BlockSlab))
				continue; // do not add door blocks
			final Item item = Item.getItemFromBlock(block);
//			FMLLog.severe("BLOCK RESOURCE DOMAIN IS " + block.getRegistryName().getResourceDomain());
//			FMLLog.severe("BLOCK RESOURCE PATH IS " + block.getRegistryName().getResourcePath());
//			FMLLog.severe("ITEM RESOURCE PATH IS " + item.getRegistryName().getResourcePath());
			if (!item.getRegistryName().getResourceDomain().equals(BaseMetals.MODID))
				continue;
//			FMLLog.severe("Registering");
			itemModelMesher.register(item, 0, new ModelResourceLocation(new ResourceLocation(item.getRegistryName().getResourceDomain(), name), "inventory"));
//			FMLLog.severe("Registered");
		}
	}

	/**
	 *
	 */
	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}
}
