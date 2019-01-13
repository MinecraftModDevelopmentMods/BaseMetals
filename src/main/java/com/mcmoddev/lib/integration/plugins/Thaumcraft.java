package com.mcmoddev.lib.integration.plugins;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.IntegrationPreInitEvent;
import com.mcmoddev.lib.integration.plugins.thaumcraft.BaseAspectGetter;
import com.mcmoddev.lib.integration.plugins.thaumcraft.IAspectGetter;
import com.mcmoddev.lib.integration.plugins.thaumcraft.TCMaterial;
import com.mcmoddev.lib.integration.plugins.thaumcraft.TCSyncEvent;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Config;
import com.mcmoddev.lib.data.NameToken;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import thaumcraft.api.aspects.AspectRegistryEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class 	Thaumcraft implements IIntegration {

	public static final String PLUGIN_MODID = "thaumcraft";

	public static final Thaumcraft INSTANCE = new Thaumcraft();

	private static final IForgeRegistry<TCMaterial> registry = new RegistryBuilder<TCMaterial>()
			.disableSaving().setMaxID(65535)
			.setName(new ResourceLocation("mmdlib", "thaumcraft_registry"))
			.setType(TCMaterial.class).create();
	private static final Map<String, ResourceLocation> nameToResource = new TreeMap<>();
	private static final Map<NameToken, Float> partMultiplierMap = new HashMap<>();

	private static boolean initDone = false;
	
	public static void putPartMultiplier(Names name, Float val){
		putPartMultiplier(name.toString(), val);
	}

	public static void putPartMultiplier(String name, Float val){
		putPartMultiplier(new NameToken(name), val);
	}
	
	public static void putPartMultiplier(NameToken name, Float val) {
		partMultiplierMap.putIfAbsent(name, val);
	}

	public static float getPartMultiplier(Names name){
		return getPartMultiplier(name.toString());
	}

	public static float getPartMultiplier(String name){
		return getPartMultiplier(new NameToken(name));
	}
	
	public static float getPartMultiplier(NameToken name){
		if(partMultiplierMap.containsKey(name)){
			return partMultiplierMap.get(name);
		}
		else{
			return 0f;
		}
	}
	
	@Override
	public void init() {
		if (!Config.Options.isModEnabled(PLUGIN_MODID) || initDone) {
			return;
		}
		initDone = true;
		
		
		MinecraftForge.EVENT_BUS.register(this);

		putPartMultiplier(Names.NUGGET, 1f);
		putPartMultiplier(Names.INGOT, 9f);
		putPartMultiplier(Names.BLOCK, getPartMultiplier(Names.INGOT) * 9);
		putPartMultiplier(Names.ORE, getPartMultiplier(Names.INGOT));

		putPartMultiplier(Names.GEM, getPartMultiplier(Names.INGOT));

		putPartMultiplier(Names.BLEND, getPartMultiplier(Names.INGOT) * 0.8f);
		putPartMultiplier(Names.SMALLBLEND, getPartMultiplier(Names.BLEND) / 9);

		putPartMultiplier(Names.POWDER, getPartMultiplier(Names.BLEND));
		putPartMultiplier(Names.SMALLPOWDER, getPartMultiplier(Names.SMALLBLEND));
	}
	
	@SubscribeEvent
	public void preInitEvents(IntegrationPreInitEvent ev) {
		MinecraftForge.EVENT_BUS.post(new TCSyncEvent(registry, nameToResource));
	}

	@SubscribeEvent
	public void registerAspects(final AspectRegistryEvent ev) {
		// TODO Rewrite this piece of crap with efficiency in mind
		registry.getValuesCollection().stream()
				.forEach( tcMaterial -> tcMaterial.getAspectMapKeys()
						.forEach( key -> {
							ev.register.registerComplexObjectTag(tcMaterial.getItemStack(key), tcMaterial.getAspectFor(key));
							ev.register.registerComplexObjectTag(tcMaterial.getBlockItemStack(key),tcMaterial.getAspectFor(key));
						}));
	}

	public static TCMaterial createVanillaIngotWithAspects(MMDMaterial material){
		return Thaumcraft.createPartsAspects(material, new BaseAspectGetter(material), Names.NUGGET, Names.BLEND, Names.SMALLBLEND, Names.POWDER, Names.SMALLPOWDER, Names.INGOT);
	}

	public static TCMaterial createVanillaWithAspects(MMDMaterial material){
		return Thaumcraft.createPartsAspects(material, new BaseAspectGetter(material), Names.NUGGET, Names.BLEND, Names.SMALLBLEND, Names.POWDER, Names.SMALLPOWDER);
	}

	public static TCMaterial createWithAspects(MMDMaterial material){
		return createPartsAspects(material, new BaseAspectGetter(material), Names.values());
	}

	public static TCMaterial createPartsAspects(MMDMaterial material, IAspectGetter aspectGetter, Names... names){
		TCMaterial tcMaterial = new TCMaterial(material, aspectGetter);
		for (Names name:names) {
			tcMaterial.getAspectsFor(name);
			
		}
		return tcMaterial;
	}

	public static TCMaterial createPartsAspects(MMDMaterial material, IAspectGetter aspectGetter, String... names){
		TCMaterial tcMaterial = new TCMaterial(material, aspectGetter);
		for (String name:names) {
			tcMaterial.getAspectsFor(name);
		}
		
		return tcMaterial;
	}
}
