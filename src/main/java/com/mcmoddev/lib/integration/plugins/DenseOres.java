package com.mcmoddev.lib.integration.plugins;

import javax.annotation.Nonnull;

import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.util.Config.Options;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class DenseOres implements IIntegration {

	public static final String PLUGIN_MODID = "denseores";

	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}
	}

	/**
	 * Register a single ore.
	 *
	 * @param name
	 *            Oredictionary name of this ore in its source mod
	 * @param underlying
	 *            One of "stone", "netherrack", "end_stone" or "obsidian" - this determines the
	 *            background of the dense ore texture
	 * @param meta
	 *            The metadata value for this ore in the source block
	 * @author Daniel Hazelton &lt;dshadowwolf@gmail.com&gt;
	 */
	protected static void registerOre(@Nonnull final String name, @Nonnull final String underlying,
			@Nonnull final int meta) {
		final String modID = Loader.instance().activeModContainer().getModId();
		registerOre(name, modID, underlying, meta);
	}

	/**
	 * Register a single ore.
	 *
	 * @param name
	 *            Oredictionary name of this ore in its source mod
	 * @param modID
	 *            ID of mod that is the source of this ore
	 * @param underlying
	 *            One of "stone", "netherrack", "end_stone" or "obsidian" - this determines the
	 *            background of the dense ore texture
	 * @param meta
	 *            The metadata value for this ore in the source block
	 * @author Daniel Hazelton &lt;dshadowwolf@gmail.com&gt;
	 */
	protected static void registerOre(@Nonnull final String name, @Nonnull final String modID,
			@Nonnull final String underlying, @Nonnull final int meta) {
		final NBTTagCompound mess = new NBTTagCompound();

		mess.setString("baseBlock", String.format("%s:%s", modID, name));
		mess.setInteger("baseBlockMeta", meta);
		mess.setString("underlyingBlockTexture", String.format("blocks/%s", underlying));
		// no idea what this does, but it is looked for
		mess.setInteger("renderType", 0);
		// this, however, is apparently a name, so...
		mess.setString("config_entry", String.format("%s %s ore", modID, name));

		FMLInterModComms.sendMessage(PLUGIN_MODID, "addDenseOre", mess);
	}

	/**
	 * Register a single ore that spawns in the nether and has an oredict of
	 * 'nether_&lt;name&gt;_ore'.
	 *
	 * @param name
	 *            Oredictionary name of this ore in its source mod
	 * @param meta
	 *            The metadata value for this ore in the source block
	 * @author Daniel Hazelton &lt;dshadowwolf@gmail.com&gt;
	 */
	protected static void registerNetherOre(@Nonnull final String name, @Nonnull final int meta) {
		final String modID = Loader.instance().activeModContainer().getModId();
		registerOre(String.format("nether_%s", name), modID, "netherrack", meta);
	}

	/**
	 * Register a single ore that spawns in the nether and has an oredict of
	 * 'nether_&lt;name&gt;_ore'.
	 *
	 * @param name
	 *            Oredictionary name of this ore in its source mod
	 * @param modID
	 *            ID of mod that is the source of this ore
	 * @param meta
	 *            The metadata value for this ore in the source block
	 * @author Daniel Hazelton &lt;dshadowwolf@gmail.com&gt;
	 */
	protected static void registerNetherOre(@Nonnull final String name, @Nonnull final String modID,
			@Nonnull final int meta) {
		registerOre(String.format("nether_%s", name), modID, "netherrack", meta);
	}

	/**
	 * Register a single ore that spawns in the end and has an oredict of 'end_&lt;name&gt;_ore'.
	 *
	 * @param name
	 *            Oredictionary name of this ore in its source mod
	 * @param meta
	 *            The metadata value for this ore in the source block
	 * @author Daniel Hazelton &lt;dshadowwolf@gmail.com&gt;
	 */
	protected static void registerEndOre(@Nonnull final String name, @Nonnull final int meta) {
		final String modID = Loader.instance().activeModContainer().getModId();
		registerOre(String.format("end_%s", name), modID, "end_stone", meta);
	}

	/**
	 * Register a single ore that spawns in the end and has an oredict of 'end_&lt;name&gt;_ore'.
	 *
	 * @param name
	 *            Oredictionary name of this ore in its source mod
	 * @param modID
	 *            ID of mod that is the source of this ore
	 * @param meta
	 *            The metadata value for this ore in the source block
	 * @author Daniel Hazelton &lt;dshadowwolf@gmail.com&gt;
	 */
	protected static void registerEndOre(@Nonnull final String name, @Nonnull final String modID,
			@Nonnull final int meta) {
		registerOre(String.format("end_%s", name), modID, "end_stone", meta);
	}
}
