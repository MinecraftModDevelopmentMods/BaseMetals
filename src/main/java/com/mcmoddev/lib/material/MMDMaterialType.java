package com.mcmoddev.lib.material;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.text.WordUtils;

import net.minecraft.block.material.Material;

public class MMDMaterialType {
	private final Material vanillaType;
	private final MaterialType MMDLibType;
	private final List<VariantType> variants;
	private final String variantName;
	
	/**
	 * ENUM of all the types of Materials.
	 *
	 * @author Jasmine Iwanek
	 *
	 */
	public enum MaterialType {
		WOOD, ROCK, METAL, MINERAL, GEM, CRYSTAL
	}

	public static enum VariantType {
		// 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x100, 0x200...
		VANILLA(1), ORELESS(2), ALLOY(4), RARE(8), NORMAL(16), SPECIAL(32);
		
		private int type;
		
		private VariantType(int type) {
			this.type = type;
		}
		
		public int value() {
			return this.type;
		}
		
		public String toString() {
			return super.toString().toLowerCase(Locale.getDefault());
		}
		
		public String toCapString() {
			return WordUtils.capitalize(this.toString());
		}
	}
	
	public MMDMaterialType(final String name, final MaterialType mmdType, VariantType...types) {
		this.variantName = name;
		this.MMDLibType = mmdType;
		this.vanillaType = getVanillaTypeInternal();
		this.variants = new LinkedList<>();
		Arrays.asList(types).stream().forEach(this.variants::add);
	}
	
	public MaterialType getMaterialType() {
		return this.MMDLibType;
	}
	
	private Material getVanillaTypeInternal() {
		switch (this.MMDLibType) {
		case METAL:
			return Material.IRON;
		case GEM:
		case ROCK:
			return Material.ROCK;
		case MINERAL:
			return Material.GRASS;
		case WOOD:
			return Material.WOOD;
		default:
			return Material.GROUND;
		}
	}
	
	public Material getVanillaType() {
		return this.vanillaType;
	}
	
	private String buildVariantName() {
		StringBuilder x = new StringBuilder();
		this.variants.stream().forEach(var -> x.append(var.toCapString()));
		
		return x.toString();
	}
	
	private String buildFullMaterialTypeName() {
		StringBuilder x = new StringBuilder();
		x.append(this.buildVariantName());
		x.append(String.format("of {} ({})", this.MMDLibType.toString(), this.vanillaType.toString()));
		
		return WordUtils.capitalize(x.toString());
	}
	
	private String buildFullName() {
		return String.format("{} is {}", this.variantName, this.buildFullMaterialTypeName());
	}
	
	public String getVariantName() {
		return this.buildVariantName();
	}
	
	public String getFullName() {
		return this.buildFullName();
	}
	
	public String getFullVariantName() {
		return this.buildFullMaterialTypeName();
	}
	
	public boolean hasVanilla() {
		return this.variants.contains(VariantType.VANILLA);
	}
	
	public boolean hasRare() {
		return this.variants.contains(VariantType.RARE);
	}
	
	public boolean hasAlloy() {
		return this.variants.contains(VariantType.ALLOY);
	}

	public boolean hasOreless() {
		return this.variants.contains(VariantType.ORELESS);
	}

	public boolean hasSpecial() {
		return this.variants.contains(VariantType.SPECIAL);
	}

}
