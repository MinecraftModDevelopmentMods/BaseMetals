package com.mcmoddev.basemetals;

import java.util.Arrays;
import java.util.List;

import com.mcmoddev.lib.MMDLib;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.MaterialNames;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.registry.CrusherRecipeRegistry;
import com.mcmoddev.lib.registry.recipe.ICrusherRecipe;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Mod.EventBusSubscriber//(modid=BaseMetals.MODID)
public class ModelDataFix {
	private static List<String> vanilla_names = Arrays.asList(MaterialNames.CHARCOAL, MaterialNames.COAL,
			MaterialNames.DIAMOND, MaterialNames.EMERALD, MaterialNames.ENDER, 
			MaterialNames.GOLD, MaterialNames.IRON, MaterialNames.LAPIS,
			MaterialNames.OBSIDIAN, MaterialNames.PRISMARINE, MaterialNames.QUARTZ,
			MaterialNames.WOOD, MaterialNames.REDSTONE, MaterialNames.STONE, 
			MaterialNames.REDSTONE);
	
	@SubscribeEvent
	public static void blockRegistryFix(RegistryEvent.MissingMappings<Block> ev) {
		ev.getAllMappings().stream()
		.filter(mapping -> mapping.key.getNamespace().equalsIgnoreCase(MMDLib.MODID))
		.filter(mapping -> {
			String[] pathBits = mapping.key.getPath().split("_");
			String matName = "double".equalsIgnoreCase(pathBits[0])?pathBits[1]:pathBits[0];
			return vanilla_names.contains(mapping.key.getPath().indexOf("_")!=-1?matName:mapping.key.getPath()) || "human_detector".equalsIgnoreCase(mapping.key.getPath());
		})
		.forEach(mapping -> {
			BaseMetals.logger.fatal("mapping: {}", mapping.key);
			if(mapping.key.getPath().indexOf("_") != -1) {
				String p = mapping.key.getPath();
				int idx = mapping.key.getPath().indexOf("_");
				String materialName = p.substring(0, idx);
				String blockName = p.substring(idx+1);
				if("double".equals(materialName)) {
					materialName = blockName.split("_")[0];
					blockName = Names.DOUBLE_SLAB.toString();
				}
				mapping.remap(Materials.getMaterialByName(materialName).getBlock(blockName));
			} else {
				mapping.remap(Materials.getMaterialByName(mapping.key.getPath()).getBlock("fluid"));
			}
		});
	}
	
	@SubscribeEvent
	public static void itemRegistryFix(RegistryEvent.MissingMappings<Item> ev) {
		ev.getAllMappings().stream()
		.filter(mapping -> mapping.key.getNamespace().equalsIgnoreCase(MMDLib.MODID))
		.filter(mapping -> {
			String[] pathBits = mapping.key.getPath().split("_");
			return vanilla_names.contains(mapping.key.getPath().indexOf("_")!=-1?pathBits[0]:mapping.key.getPath());
		})
		.forEach(mapping ->  {
			ResourceLocation remap = new ResourceLocation(BaseMetals.MODID, mapping.key.getPath());
			mapping.remap(ForgeRegistries.ITEMS.getValue(remap));
		});
	}
	
	@SubscribeEvent
	public static void materialRegistryFix(RegistryEvent.MissingMappings<MMDMaterial> ev) {
		ev.getAllMappings().stream()
		.filter(mapping -> mapping.key.getNamespace().equalsIgnoreCase(MMDLib.MODID))
		.filter(mapping -> vanilla_names.contains(mapping.key.getPath()) || 
				"default".equals(mapping.key.getPath()) || "empty".equals(mapping.key.getPath()))
		.forEach(mapping -> mapping.remap(Materials.getMaterialByName(mapping.key.getPath())));
	}

	@SubscribeEvent
	public static void crusherRecipeRegistryFix(RegistryEvent.MissingMappings<ICrusherRecipe> ev) {
		ev.getAllMappings().stream()
		.filter(mapping -> mapping.key.getNamespace().equalsIgnoreCase(MMDLib.MODID))
		.forEach(mapping -> {
			ResourceLocation remap = new ResourceLocation(BaseMetals.MODID, mapping.key.getPath());
			ICrusherRecipe recipeRemap = CrusherRecipeRegistry.getInstance().getRegistry().getValue(remap);
			if(recipeRemap != null) {
				mapping.remap(recipeRemap);
			}
		});
	}

}
