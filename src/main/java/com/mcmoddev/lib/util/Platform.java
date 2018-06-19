package com.mcmoddev.lib.util;

import java.util.Map;

import javax.annotation.Nonnull;

import com.mcmoddev.lib.data.SharedStrings;

import net.minecraft.block.properties.IProperty;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class Platform {

	private static boolean devEnv = false;

	private Platform() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/**
	 * Check if the code is running on the server instance.
	 *
	 * @return True if running on the server
	 */
	public static boolean isServer() {
		return FMLCommonHandler.instance().getEffectiveSide().isServer();
	}

	/**
	 * Check if the code is running on the client instance.
	 *
	 * @return True if running on the client
	 */
	public static boolean isClient() {
		return FMLCommonHandler.instance().getEffectiveSide().isClient();
	}

	/**
	 * Compare two ItemStacks to see if they are the same.
	 *
	 * @param itemStack1
	 *            ItemStack 1 to compare
	 * @param itemStack2
	 *            ItemStack 2 to compare
	 *
	 * @return True is they are the same
	 */
	public static boolean isSameItem(@Nonnull final ItemStack itemStack1,
			@Nonnull final ItemStack itemStack2) {
		return ((!itemStack1.isEmpty()) && (!itemStack2.isEmpty())
				&& (itemStack1.isItemEqual(itemStack2)));
	}

	/**
	 * Rotate EnumFacing around clockwise.
	 *
	 * @param forward
	 *            EnumFacing to rotate
	 *
	 * @return rotated EnumFacing
	 */
	public static EnumFacing rotateAround(final EnumFacing forward) {
		switch (forward) {
			case NORTH:
				return EnumFacing.EAST;
			case EAST:
				return EnumFacing.SOUTH;
			case SOUTH:
				return EnumFacing.WEST;
			case WEST:
				return EnumFacing.NORTH;
			default:
				return forward;
		}
	}

	/**
	 * Rotate EnumFacing around anti-clockwise.
	 *
	 * @param forward
	 *            EnumFacing to rotate
	 *
	 * @return rotated EnumFacing
	 */
	public static EnumFacing invertedRotateAround(final EnumFacing forward) {
		switch (forward) {
			case NORTH:
				return EnumFacing.WEST;
			case EAST:
				return EnumFacing.NORTH;
			case SOUTH:
				return EnumFacing.EAST;
			case WEST:
				return EnumFacing.SOUTH;
			default:
				return forward;
		}
	}

	/**
	 * Get PropertyString from BlockState properties.
	 *
	 * @param values
	 *            Values
	 * @param extrasArgs
	 *            Extra Args
	 *
	 * @return PropertyString
	 */
	public static String getPropertyString(final Map<IProperty<?>, Comparable<?>> values,
			final String... extrasArgs) {
		final StringBuilder stringbuilder = new StringBuilder();
		for (final Map.Entry<IProperty<?>, Comparable<?>> entry : values.entrySet()) {
			if (stringbuilder.length() != 0) {
				stringbuilder.append(",");
			}
			final IProperty<?> iproperty = entry.getKey();
			stringbuilder.append(iproperty.getName());
			stringbuilder.append("=");
			stringbuilder.append(getPropertyName(iproperty, (Comparable<?>) entry.getValue()));
		}
		if (stringbuilder.length() == 0) {
			stringbuilder.append("inventory");
		}
		for (final String args : extrasArgs) {
			if (stringbuilder.length() != 0) {
				stringbuilder.append(",");
			}
			stringbuilder.append(args);
		}
		return stringbuilder.toString();
	}

	@SuppressWarnings("unchecked")
	private static <T extends Comparable<T>> String getPropertyName(final IProperty<T> property,
			final Comparable<?> comparable) {
		return property.getName((T) comparable);
	}

	/**
	 * Check if the code is running on a dev-environment instance.
	 *
	 * @return True if running in a dev-environment
	 */
	public static boolean isDevEnv() {
		return devEnv;
	}

	/**
	 *
	 * @param dev
	 */
	public static void setDev(final boolean dev) {
		if (!(devEnv)) {
			devEnv = dev;
		}
	}
}
