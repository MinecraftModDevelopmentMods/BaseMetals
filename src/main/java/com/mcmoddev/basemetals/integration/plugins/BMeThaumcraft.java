package com.mcmoddev.basemetals.integration.plugins;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.data.MaterialStats;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.integration.plugins.Thaumcraft;
import com.mcmoddev.lib.integration.plugins.thaumcraft.TCMaterial;
import com.mcmoddev.lib.integration.plugins.thaumcraft.TCSyncEvent;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.material.MMDMaterialType;
import com.mcmoddev.lib.util.Config;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thaumcraft.api.aspects.Aspect;

@MMDPlugin(addonId = BaseMetals.MODID, pluginId = BMeThaumcraft.PLUGIN_MODID, versions = BMeThaumcraft.PLUGIN_MODID
		+ "@[1.12-6.1.BETA,)")
public final class 	BMeThaumcraft implements IIntegration {

	public static final String PLUGIN_MODID = Thaumcraft.PLUGIN_MODID;
	private static final Map<String, TCMaterial> tcMaterials = new HashMap<>();


	public BMeThaumcraft() {
		if (!Config.Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}
		
		MinecraftForge.EVENT_BUS.register(this.getClass());
	}
	
	@Override
	public void init() {
		Thaumcraft.INSTANCE.init();
		if (!Config.Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}
		List<String> materials = Arrays.asList(MaterialNames.COPPER, MaterialNames.SILVER, MaterialNames.DIAMOND, 
				MaterialNames.EMERALD, MaterialNames.STEEL, MaterialNames.BRASS, MaterialNames.BRONZE, MaterialNames.TIN,
				MaterialNames.MITHRIL, MaterialNames.AQUARIUM);
				
		Materials.getAllMaterials().stream()
		.filter(mat -> !mat.isVanilla())
		.filter(mat -> !mat.isEmpty())
		.forEach(mat -> {
			String mn = mat.getName();
			TCMaterial tcm = Thaumcraft.createWithAspects(mat);
			tcMaterials.put(mn, tcm);
		});

		Materials.getAllMaterials().stream()
		.filter( mat -> mat.isVanilla())
		.filter( mat -> !mat.isEmpty())
		.filter( mat -> mat.getType() == MMDMaterialType.MaterialType.MINERAL
		|| mat.getType() == MMDMaterialType.MaterialType.ROCK)
		.forEach( mat ->  {
			String mn = mat.getName();
			TCMaterial tcm = Thaumcraft.createVanillaIngotWithAspects(mat);
			tcMaterials.put(mn, tcm);
		});
		
		materials.stream()
		.filter(Materials::hasMaterial)
		.map(name -> tcMaterials.getOrDefault(name, Thaumcraft.createWithAspects(Materials.getMaterialByName(name))))
		.forEach(tcm -> {
			String myName = tcm.getName();
			switch(myName) {
			case "mithril":
			case "aquarium":
				tcm.addMaterialAspect(Aspect.MAGIC, 5);
				break;
			case "copper":
				tcm.addMaterialAspect(Aspect.EXCHANGE, 5);
				break;
			case "tin":
				tcm.addMaterialAspect(Aspect.CRYSTAL, 5);
				break;
			case "steel":
				tcm.addMaterialAspect(Aspect.ORDER, 5);
				break;
			case "brass":
			case "bronze":
				tcm.addMaterialAspect(Aspect.TOOL, 5);
				break;
			case "silver":
			case "diamond":
			case "emerald":
				MMDMaterial mat = tcm.getMMDMaterial();
				tcm.addMaterialAspect(Aspect.DESIRE, (m) -> mat.isRare()?(int)(mat.getStat(MaterialStats.MAGICAFFINITY) * 2/ 45 * m):5);
				break;
			}
			tcMaterials.put(myName, tcm);
		});
		
	}
	
	@SubscribeEvent
	public static void registerAspects(final TCSyncEvent ev) {
		tcMaterials.values().stream().forEach(ev::register);
	}
}
