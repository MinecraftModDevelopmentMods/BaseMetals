package com.mcmoddev.lib.integration.plugins;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.plugins.thaumcraft.AspectsMath;
import com.mcmoddev.lib.integration.plugins.thaumcraft.TCMaterial;
import com.mcmoddev.lib.integration.plugins.thaumcraft.TCSyncEvent;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Config;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import thaumcraft.api.aspects.AspectRegistryEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Thaumcraft implements IIntegration {

	public static final String PLUGIN_MODID = "thaumcraft";

	private static boolean initDone = false;

	public static final Thaumcraft INSTANCE = new Thaumcraft();

	private static final IForgeRegistry<TCMaterial> registry = new RegistryBuilder<TCMaterial>()
			.disableSaving().setMaxID(65535)
			.setName(new ResourceLocation("mmdlib", "thaumcraft_registry"))
			.setType(TCMaterial.class).create();
	private static final Map<String, ResourceLocation> nameToResource = new TreeMap<>();
	private static final Map<String, Float> partMultiplierMap = new HashMap<>();

	public static void putPartMultiplier(Names name, Float val){
		putPartMultiplier(name.toString(), val);
	}

	public static void putPartMultiplier(String name, Float val){
		partMultiplierMap.putIfAbsent(name, val);
	}

	public static float getPartMultiplier(String name){
		return partMultiplierMap.get(name);
	}
	
	@Override
	public void init() {
		if (!Config.Options.isModEnabled(PLUGIN_MODID) || initDone) {
			return;
		}
		initDone = true;

		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.post(new TCSyncEvent(registry, nameToResource));

		putPartMultiplier(Names.NUGGET, 1f);
		putPartMultiplier(Names.INGOT, 9f);
		putPartMultiplier(Names.GEM, partMultiplierMap.get(Names.INGOT));
		putPartMultiplier(Names.BLEND, partMultiplierMap.get(Names.INGOT) * 0.8f);
		putPartMultiplier(Names.ORE, partMultiplierMap.get(Names.INGOT));
	}

	@SubscribeEvent
	public void registerAspects(final AspectRegistryEvent ev) {
		registry.getValuesCollection().stream()
				.forEach( tcMaterial -> tcMaterial.getAspectMapKeys()
						.forEach( key -> ev.register.registerComplexObjectTag(new ItemStack(tcMaterial.getItem(key)),tcMaterial.getAspectFor(key))));
	}

	public static TCMaterial createPartsAspects(MMDMaterial material, String... names){
		TCMaterial tcMaterial = new TCMaterial(material);
		for (String name:names) {
			tcMaterial.addAspect(name, AspectsMath.addAspects(material, name));
		}
		return tcMaterial;
	}

	public static TCMaterial createWithAspects(MMDMaterial material){
		return createPartsAspects(material, Names.NUGGET.toString(), Names.INGOT.toString(), Names.ORE.toString(), Names.GEM.toString(), Names.BLEND.toString());
	}
}
