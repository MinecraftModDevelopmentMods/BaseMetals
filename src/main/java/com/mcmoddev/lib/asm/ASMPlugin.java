package com.mcmoddev.lib.asm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.mcmoddev.lib.util.Platform;

import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.SortingIndex;

@Name("BaseMetals")
@SortingIndex(1001)
public class ASMPlugin implements IFMLLoadingPlugin {

	static List<ITransformer> transformerList = new ArrayList<>();

	// HorseArmor patches present in 1.12.2-14.23.1.2592+
	private static final boolean NEEDS_HORSE_ARMOR_PATCH = (ForgeVersion.getMajorVersion() < 14)
			|| (ForgeVersion.getMinorVersion() < 23) || (ForgeVersion.getRevisionVersion() < 1)
			|| (ForgeVersion.getBuildVersion() < 2592);

	/**
	 *
	 */
	public ASMPlugin() {
		if (NEEDS_HORSE_ARMOR_PATCH) {
			transformerList.add(new EntityHorseTransformer());
			transformerList.add(new HorseArmorTypeTransformer());
		}
		// if you add more here, you must remove the check below in getASMTransformerClass()!
	}

	@Override
	public String[] getASMTransformerClass() {
		if (!NEEDS_HORSE_ARMOR_PATCH) {
			return null;
		}
		return new String[] { ASMTransformer.class.getName() };
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Nullable
	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(final Map<String, Object> data) {
		Platform.setDev((Boolean) data.get("runtimeDeobfuscationEnabled"));
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
