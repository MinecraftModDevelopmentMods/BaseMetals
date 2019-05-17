package com.mcmoddev.basemetals.vanillabits;

import java.util.Arrays;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.MMDLib;
import com.mcmoddev.lib.client.renderer.FluidStateMapper;
import com.mcmoddev.lib.data.VanillaMaterialNames;
import com.mcmoddev.lib.init.Fluids;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid=BaseMetals.MODID)
public class VanillaBitsRegistryEvents {

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> ev) {
		Materials.getAllMaterials().stream()
		.filter(MMDMaterial::isVanilla)
		.forEach(mat -> mat.getBlocks().stream()
				.filter(bl -> BaseMetals.MODID.equals(bl.getRegistryName().getNamespace()))
				.forEach(bl -> ev.getRegistry().register(bl)));
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public static void registerItems(RegistryEvent.Register<Item> ev) {
		Materials.getAllMaterials().stream()
		.filter(mat -> mat.isVanilla())
		.forEach(mat -> mat.getItems().stream()
				.map(it -> it.getItem())
				.filter(it -> BaseMetals.MODID.equals(it.getRegistryName().getNamespace()))
				.forEach(it -> ev.getRegistry().register(it)));
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void modelRegistryBits(ModelRegistryEvent ev) {
		Arrays.asList(VanillaMaterialNames.CHARCOAL, VanillaMaterialNames.COAL,
				VanillaMaterialNames.DIAMOND, VanillaMaterialNames.EMERALD, VanillaMaterialNames.ENDER, 
				VanillaMaterialNames.GOLD, VanillaMaterialNames.IRON, VanillaMaterialNames.LAPIS,
				VanillaMaterialNames.OBSIDIAN, VanillaMaterialNames.PRISMARINE, VanillaMaterialNames.QUARTZ,
				VanillaMaterialNames.WOOD, VanillaMaterialNames.REDSTONE, VanillaMaterialNames.STONE, 
				VanillaMaterialNames.REDSTONE).stream()
		.forEach(name -> {
			if(!VanillaMaterialNames.PRISMARINE.equalsIgnoreCase(name)) {
				Materials.getMaterialByName(name).getItems().stream()
				.filter(item -> MMDLib.MODID.equalsIgnoreCase(item.getItem().getRegistryName().getNamespace()))
				.filter(item -> !item.getItem().getRegistryName().getPath().equalsIgnoreCase(name))
				.forEach(item -> {
					ModelLoader.setCustomModelResourceLocation(item.getItem(), 0,
						new ModelResourceLocation(item.getItem().getRegistryName(), "inventory"));
				});
			}

			final Block block = Fluids.getFluidBlockByName(name);
			if(block != null) {
				final Item item = Item.getItemFromBlock(block);
				final ResourceLocation resourceLocation = block.getRegistryName();
				final FluidStateMapper mapper = new FluidStateMapper(
						resourceLocation.getNamespace() + ":" + name);

				if(BaseMetals.MODID.equalsIgnoreCase(item.getRegistryName().getNamespace())) {
					if (item != null) {
						ModelBakery.registerItemVariants(item);
						ModelLoader.setCustomMeshDefinition(item, mapper);
					}
					ModelLoader.setCustomStateMapper(block, mapper);
				}
			}
		});
	}
    
	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> ev) {
		// do nothing, as we have nothing to do
	}
}
