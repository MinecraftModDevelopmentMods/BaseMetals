package com.mcmoddev.basemetals.properties;

import java.util.ArrayList;
import java.util.List;

import com.mcmoddev.basemetals.data.MaterialNames;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Builds localized tooltips for material-specific armor and tool effects. */
public final class BMEPropertiesHelper {
	private static final String TOOLTIP = "tooltip.";
	private static final String ARMOR = ".armor";
	private static final String TOOL = ".tool";


	/** Returns the special tool-effect tooltip for a material, when it has one. */
	@SideOnly(Side.CLIENT)
	public static List<String> addToolSpecialPropertiesToolTip(final String materialName) {
		List<String> rv = new ArrayList<>();
		switch (materialName) {
			case MaterialNames.ADAMANTINE:
				rv.add(I18n.format(TOOLTIP + MaterialNames.ADAMANTINE + TOOL, 4));
				break;
			case MaterialNames.AQUARIUM:
				rv.add(I18n.format(TOOLTIP + MaterialNames.AQUARIUM + TOOL, 4));
				break;
			case MaterialNames.COLDIRON:
				rv.add(I18n.format(TOOLTIP + MaterialNames.COLDIRON + TOOL, 3));
				break;
			case MaterialNames.MITHRIL:
				rv.add(I18n.format(TOOLTIP + MaterialNames.MITHRIL + TOOL));
				break;
			case MaterialNames.STARSTEEL:
				rv.add(I18n.format(TOOLTIP + MaterialNames.STARSTEEL + TOOL, 10));
				break;
			default:
		}
		return rv;
	}

	/** Returns the special armor-effect tooltip for a material, when it has one. */
	@SideOnly(Side.CLIENT)
	public static List<String> addArmorSpecialPropertiesToolTip(final String materialName) {
		List<String> rv = new ArrayList<>();
		switch (materialName) {
			case MaterialNames.ADAMANTINE:
				rv.add(I18n.format(TOOLTIP + MaterialNames.ADAMANTINE + ARMOR, 4));
				break;
			case MaterialNames.AQUARIUM:
				rv.add(I18n.format(TOOLTIP + MaterialNames.AQUARIUM + ARMOR, 4));
				break;
			case MaterialNames.COLDIRON:
				rv.add(I18n.format(TOOLTIP + MaterialNames.COLDIRON + ARMOR, 3));
				break;
			case MaterialNames.MITHRIL:
				rv.add(I18n.format(TOOLTIP + MaterialNames.MITHRIL + ARMOR));
				break;
			case MaterialNames.STARSTEEL:
				rv.add(I18n.format(TOOLTIP + MaterialNames.STARSTEEL + ARMOR, 10));
				break;
			default:
		}
		return rv;
	}
}
