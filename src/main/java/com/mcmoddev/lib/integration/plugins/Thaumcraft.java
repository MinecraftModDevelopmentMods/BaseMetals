package com.mcmoddev.lib.integration.plugins;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Config;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.AspectRegistryEvent;

public class Thaumcraft implements IIntegration {

	public static final String PLUGIN_MODID = "thaumcraft";

	private static boolean initDone = false;

	public static final Thaumcraft INSTANCE = new Thaumcraft();

	@Override
	public void init() {
		if (!Config.Options.isModEnabled(PLUGIN_MODID) || initDone) {
			return;
		}
		initDone = true;

		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void registerAspects(final AspectRegistryEvent ev) {
		Materials.getAllMaterials().stream()
				.filter(mat -> mat.hasOre()
						|| mat.hasItem(Names.INGOT)
						|| mat.hasItem(Names.CRYSTAL)
						|| mat.hasItem(Names.GEM))
				.forEach( mat -> {
					AspectList aspects = new AspectList();
					addAspects(aspects, mat);
					ev.register.registerComplexObjectTag(mat.getItemStack(Names.INGOT), aspects);
					ev.register.registerComplexObjectTag(mat.getItemStack(Names.CRYSTAL), aspects);
					ev.register.registerComplexObjectTag(mat.getItemStack(Names.GEM), aspects);
					ev.register.registerComplexObjectTag(mat.getBlockItemStack(Names.ORE), aspects);
				});
	}

	private AspectList addAspects(AspectList aspectList, MMDMaterial material){
		addMetalAspect(aspectList, material);
		addCrystalAspect(aspectList, material);
		addMagicAspect(aspectList, material);
		addDesireAspect(aspectList, material);

		return aspectList;
	}

	private AspectList addMetalAspect(AspectList aspectList, MMDMaterial material){
		if(material.getType() == MMDMaterial.MaterialType.METAL){
			aspectList.add(Aspect.METAL, getMetalAspect(material));
		}
		return aspectList;
	}

	private int getMetalAspect(MMDMaterial material){

		float harvestLevel = material.getRequiredHarvestLevel();
		float blockHardness = material.getBlockHardness();

		float value  = harvestLevel * blockHardness  / 2;
		if(value < 6){
			value = 6;
		}

		return (int)value;
	}

	private AspectList addCrystalAspect(AspectList aspectList, MMDMaterial material){
		if(material.getType() == MMDMaterial.MaterialType.CRYSTAL || material.getType() == MMDMaterial.MaterialType.GEM){
			aspectList.add(Aspect.CRYSTAL, getMetalAspect(material));
		}
		return aspectList;
	}

	private AspectList addDesireAspect(AspectList aspectList, MMDMaterial material){
		int value = getDesireAspect(material);
		if(value > 0){
			aspectList.add(Aspect.DESIRE, value);
		}
		return aspectList;
	}

	private int getDesireAspect(MMDMaterial material){
		return getDesireAspect(material.isRare(), material.getEnchantability());
	}

	private int getDesireAspect(boolean isRare, float enchantability){
		if(isRare){
			return (int)((enchantability * 8) / 20);
		}
		return 0;
	}

	private AspectList addMagicAspect(AspectList aspectList, MMDMaterial material){
		int value = getMagicAspect(material);
		if(value > 0){
			aspectList.add(Aspect.MAGIC, value);
		}
		return aspectList;
	}

	private int getMagicAspect(MMDMaterial material){
		return getMagicAspect(material.getEnchantability());
	}

	private int getMagicAspect(float enchantability){
		if(enchantability >= 15){
			return (int)(enchantability * 3);
		}
		return 0;
	}
}
