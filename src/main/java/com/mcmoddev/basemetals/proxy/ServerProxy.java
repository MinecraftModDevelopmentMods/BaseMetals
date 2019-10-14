package com.mcmoddev.basemetals.proxy;

import javax.annotation.Nullable;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * Base Metals Server Proxy.
 *
 * @author Jasmine Iwanek
 *
 */
public final class ServerProxy extends CommonProxy {
	// Nothing to see here people
	@Override
	public World getWorld(@Nullable final int dimension) {
		return FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dimension);
	}
}
