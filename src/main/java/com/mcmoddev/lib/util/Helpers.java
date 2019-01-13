package com.mcmoddev.lib.util;

import java.util.Map;

import com.mcmoddev.lib.data.SharedStrings;

import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;

public class Helpers {

	public Helpers() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
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
        return forward.rotateY();
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
		return forward.rotateYCCW();
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
}
