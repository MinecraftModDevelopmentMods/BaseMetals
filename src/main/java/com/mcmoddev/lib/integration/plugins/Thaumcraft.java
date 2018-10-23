package com.mcmoddev.lib.integration.plugins;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.plugins.thaumcraft.TCMaterial;
import com.mcmoddev.lib.integration.plugins.thaumcraft.TCSyncEvent;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Config;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.AspectRegistryEvent;

import java.util.EnumMap;
import java.util.Map;
import java.util.TreeMap;

import static com.mcmoddev.lib.integration.plugins.thaumcraft.AspectsMath.addAspects;

public class Thaumcraft implements IIntegration {

	public static final String PLUGIN_MODID = "thaumcraft";

	private static boolean initDone = false;

	public static final Thaumcraft INSTANCE = new Thaumcraft();

	private static final IForgeRegistry<TCMaterial> registry = new RegistryBuilder<TCMaterial>()
			.disableSaving().setMaxID(65535)
			.setName(new ResourceLocation("mmdlib", "thaumcraft_registry"))
			.setType(TCMaterial.class).create();
	private static final Map<String, ResourceLocation> nameToResource = new TreeMap<>();
	private static final Map<Names, Float> nameToMultiplier = new EnumMap<>(Names.class);

	public static void putInNameToMultiplier(Names name, Float val){
		nameToMultiplier.putIfAbsent(name, val);
	}

	public static float getFromNameToMultiplier(Names name){
		return nameToMultiplier.get(name);
	}
	
	@Override
	public void init() {
		if (!Config.Options.isModEnabled(PLUGIN_MODID) || initDone) {
			return;
		}
		initDone = true;

		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.post(new TCSyncEvent(registry,nameToResource));

		nameToMultiplier.putIfAbsent(Names.NUGGET, 1f);
		nameToMultiplier.putIfAbsent(Names.INGOT, 9f);
		nameToMultiplier.putIfAbsent(Names.GEM, nameToMultiplier.get(Names.INGOT));
		nameToMultiplier.putIfAbsent(Names.BLEND, nameToMultiplier.get(Names.INGOT) * 0.8f);
		nameToMultiplier.putIfAbsent(Names.ORE, nameToMultiplier.get(Names.INGOT));
		nameToMultiplier.putIfAbsent(Names.SHARD, nameToMultiplier.get(Names.NUGGET));
	}

	@SubscribeEvent
	public void registerAspects(final AspectRegistryEvent ev) {
//		registry.getValuesCollection().stream()
//				.forEach( tcMaterial -> ev.register.registerComplexObjectTag(new ItemStack(tcMaterial.getMMDMaterial().getItem(Names.INGOT)), tcMaterial.getAspectList()));
//	}
		Materials.getAllMaterials().stream()
				.filter( mat -> !mat.isVanilla())
				.forEach( mat -> registerAspects(ev, mat));

		Materials.getAllMaterials().stream()
				.filter( mat -> mat.isVanilla())
				.forEach( mat -> registerAspects(ev, mat, Names.NUGGET));
	}

	protected void registerAspects(final AspectRegistryEvent ev, MMDMaterial mat){
		registerAspects(ev, mat, Names.NUGGET);
		registerAspects(ev, mat, Names.INGOT);
		registerAspects(ev, mat, Names.ORE);
	}

	protected void registerAspects(final AspectRegistryEvent ev, MMDMaterial mat, Names name){
		ev.register.registerComplexObjectTag(mat.getItemStack(name), addAspects(new AspectList(), mat, name));
	}
}
