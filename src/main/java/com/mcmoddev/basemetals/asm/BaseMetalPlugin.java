package com.mcmoddev.basemetals.asm;

import astro.lib.api.integration.ITransformer;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.SortingIndex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mcmoddev.basemetals.asm.transformer.EntityHorseTransformer;
import com.mcmoddev.basemetals.asm.transformer.HorseArmorTypeTransformer;

@Name("BaseMetals")
@MCVersion("1.10.2")
@SortingIndex(1001)
public class BaseMetalPlugin implements IFMLLoadingPlugin {

	static List<ITransformer> transformerList = new ArrayList<>();
	static boolean inDevelopment;

	public BaseMetalPlugin() {
		transformerList.add(new EntityHorseTransformer());
		transformerList.add(new HorseArmorTypeTransformer());
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{BaseMetalTransformer.class.getName()};
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		inDevelopment = !(Boolean) data.get("runtimeDeobfuscationEnabled");
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}