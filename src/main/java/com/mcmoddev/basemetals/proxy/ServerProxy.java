package com.mcmoddev.basemetals.proxy;

import javax.annotation.Nullable;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

/** Provides dedicated-server access to dimension worlds. */
public final class ServerProxy extends CommonProxy {
	@Override
	public World getWorld(@Nullable final int dimension) {
		return FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dimension);
	}
}
