package com.mcmoddev.lib.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.integration.IIntegration;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class DenseOres implements IIntegration {

	public static final String PLUGIN_MODID = "denseores";

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.enableDenseOres) {
			return;
		}

		initDone = true;
	}

	/**
	 * Register a single ore
	 * 
	 * @param name Oredictionary name of this ore in its source mod
	 * @param modID ID of mod that is the source of this ore
	 * @param underlying One of "stone", "netherrack", "end_stone" or "obsidian" - this determines the background of the dense ore texture 
	 * @param meta The metadata value for this ore in the source block
	 * @author Daniel Hazelton &lt;dshadowwolf@gmail.com&gt;
	 */
	public static void registerOre(String name, String modID, String underlying, int meta) {
		BaseMetals.logger.error("Registering %s, baseBlock %s:%s with meta %d and baseBlockTexture blocks/%s", name, modID, name, meta, underlying);
		NBTTagCompound mess = new NBTTagCompound();
		mess.setString("baseBlock", String.format("%s:%s", modID, name));
		mess.setInteger("baseBlockMeta", meta);
		mess.setString("underlyingBlockTexture", String.format( "blocks/%s", underlying ));
		// no idea what this does, but it is looked for
		mess.setInteger("renderType", 0);
		// this, however, is apparently a name, so...
		mess.setString("config_entry", String.format("%s %s ore", modID, name));
		
		BaseMetals.logger.error("Sending Message for material %s with baseBlock set to %s:%s and meta of %d", name, modID, name, meta);
		FMLInterModComms.sendMessage("denseores",  "addDenseOre", mess);
	}
}
