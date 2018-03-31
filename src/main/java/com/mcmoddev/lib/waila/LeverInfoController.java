package com.mcmoddev.lib.waila;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class LeverInfoController implements IWailaDataProvider {

	@Override
	public ItemStack getWailaStack(final IWailaDataAccessor accessor,
			final IWailaConfigHandler config) {
		return ItemStack.EMPTY;
	}

	@Override
	public List<String> getWailaHead(final ItemStack itemStack, final List<String> currentTip,
			final IWailaDataAccessor accessor, final IWailaConfigHandler config) {
		return currentTip;
	}

	@Override
	public List<String> getWailaBody(final ItemStack itemStack, final List<String> currentTip,
			final IWailaDataAccessor accessor, final IWailaConfigHandler config) {
		final String redstoneOn = (accessor.getMetadata() & 8) == 0 ? I18n.format("hud.msg.off")
				: I18n.format("hud.msg.on");
		currentTip.add(I18n.format("%s : %s", I18n.format("hud.msg.state"), redstoneOn));
		return currentTip;
	}

	@Override
	public List<String> getWailaTail(final ItemStack itemStack, final List<String> currentTip,
			final IWailaDataAccessor accessor, final IWailaConfigHandler config) {
		return currentTip;
	}

	@Override
	public NBTTagCompound getNBTData(final EntityPlayerMP player, final TileEntity te,
			final NBTTagCompound tag, final World world, final BlockPos pos) {
		return tag;
	}
}
