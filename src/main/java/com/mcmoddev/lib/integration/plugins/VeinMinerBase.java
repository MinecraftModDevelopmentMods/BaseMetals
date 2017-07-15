package com.mcmoddev.lib.integration.plugins;

import javax.annotation.Nonnull;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.ConfigBase.Options;

import net.minecraftforge.fml.common.Loader;
import portablejim.veinminer.api.IMCMessage;

/**
 * VeinMiner Integration Plugin
 *
 * @author Jasmine Iwanek
 *
 */
public class VeinMinerBase implements IIntegration {

	public static final String PLUGIN_MODID = "veinminer";

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}

		initDone = true;
	}

	/**
	 * Add tools to the VeinMiner list from a Material
	 *
	 * @param material
	 *            Material to add
	 */
	protected static void addToolsForMaterial(@Nonnull final MMDMaterial material) {
		addToolsForMaterial(material.getName());
	}

	/**
	 * Add tools to the VeinMiner list from a Material
	 *
	 * @param materialName
	 *            Name of Material to add
	 */
	protected static void addToolsForMaterial(@Nonnull final String materialName) {
		final String ownerModID = Loader.instance().activeModContainer().getModId();
		IMCMessage.addTool(Names.AXE.toString(), ownerModID + ":" + materialName + "_" + Names.AXE.toString());
		IMCMessage.addTool(Names.HOE.toString(), ownerModID + ":" + materialName + "_" + Names.HOE.toString());
		IMCMessage.addTool(Names.PICKAXE.toString(), ownerModID + ":" + materialName + "_" + Names.PICKAXE.toString());
		IMCMessage.addTool(Names.SHEARS.toString(), ownerModID + ":" + materialName + "_" + Names.SHEARS.toString());
		IMCMessage.addTool(Names.SHOVEL.toString(), ownerModID + ":" + materialName + "_" + Names.SHOVEL.toString());
		// IMCMessage.addTool("crook", ownerModID + ":" + materialName + "_crook");
		IMCMessage.addTool("hammer", ownerModID + ":" + materialName + "_hammer");
	}
}
