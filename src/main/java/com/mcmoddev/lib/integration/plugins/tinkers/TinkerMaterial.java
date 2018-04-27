package com.mcmoddev.lib.integration.plugins.tinkers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.data.MaterialStats;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistryEntry;
import slimeknights.tconstruct.library.materials.ArrowShaftMaterialStats;
import slimeknights.tconstruct.library.materials.BowMaterialStats;
import slimeknights.tconstruct.library.materials.BowStringMaterialStats;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.FletchingMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.IMaterialStats;
import slimeknights.tconstruct.library.materials.Material;

public class TinkerMaterial  extends IForgeRegistryEntry.Impl<TinkerMaterial> implements IMMDObject {
	@SuppressWarnings("rawtypes")
	private final Map<TinkersStat, IStat> stats = Maps.<TinkersStat, IStat>newConcurrentMap();
	private String name;
	private int tintColor;
	private final MMDMaterial material;
	private Material tinkersMaterial;
	private boolean toolForge = false;
	private boolean craftable = false;
	private boolean castable = true;
	
	// book keeping stuffs - trait handling bits
	private final Map<TinkersTraitLocation, List<String>> traits = Maps.newConcurrentMap();
	private final Map<String, Integer> extraMelting = Maps.newConcurrentMap();
	private final Map<TinkersStatTypes, IMaterialStats> tinkersStats = Maps.newConcurrentMap();
	
	public TinkerMaterial(MMDMaterial basis) {
		this.material = basis;
		this.name = basis.getCapitalizedName();
		this.tintColor = basis.getTintColor();
		
		for( TinkersStat k : TinkersStat.values()) {
			switch(k) {
			case HEAD_DURABILITY:
				this.stats.put(k, () -> new IntStat(basis.getToolDurability()));
				break;
			case MINING_SPEED:
				this.stats.put(k, () -> new FloatStat(basis.getStat(MaterialStats.HARDNESS) * 0.85f));
				break;
			case MINING_LEVEL:
				this.stats.put(k, () -> new IntStat(basis.getToolHarvestLevel()));
			case HEAD_ATTACK_DAMAGE:
				this.stats.put(k, () -> new FloatStat(basis.getBaseAttackDamage() * 2));
				break;
			case BODY_DURABILITY:
				this.stats.put(k, () -> new IntStat(basis.getToolDurability() / 7));
				break;
			case BODY_MODIFIER:
				this.stats.put(k, () -> new FloatStat(basis.getStat(MaterialStats.HARDNESS) 
						+ (basis.getStat(MaterialStats.MAGICAFFINITY) * 2) / 9));
				break;
			case EXTRA_DURABILTIY:			
				this.stats.put(k, () -> new IntStat(basis.getToolDurability() / 10));
				break;
			case BOW_DRAW_SPEED:
				this.stats.put(k, () -> new FloatStat(this.calcDrawSpeed(basis.getToolDurability())));
				break;
			case BOW_DAMAGE:
				this.stats.put(k, () -> new FloatStat(basis.getBaseAttackDamage() + 3));
				break;
			case BOW_RANGE:
				this.stats.put(k, () -> new FloatStat(15.0f));
				break;
			case BOWSTRING_MODIFIER:
			case ARROWSHAFT_MODIFIER:
			case FLETCHING_ACCURACY:
			case FLETCHING_MODIFIER:
				this.stats.put(k, () -> new FloatStat(1.0f));
				break;
			case ARROWSHAFT_BONUS_AMMO:
				this.stats.put(k, () -> new IntStat(1));
				break;
			default:
				BaseMetals.logger.error("Unknown Tinkers' Construct Stat %s, ignoring", k);	
			}
		}
	}

	public TinkerMaterial toggleToolForge() {
		this.toolForge = !this.toolForge;
		return this;
	}
	
	public TinkerMaterial setToolForge(boolean val) {
		this.toolForge = val;
		return this;
	}
	
	public boolean getToolForge() {
		return this.toolForge;
	}

	public TinkerMaterial toggleCastable() {
		this.castable = !this.castable;
		if (this.castable && this.craftable) this.craftable = false;
		return this;
	}

	public TinkerMaterial toggleCraftable() {
		this.craftable = !this.craftable;
		if (this.craftable && this.castable) this.castable = false;
		return this;
	}

	public TinkerMaterial setCastable(boolean val) {
		this.castable = val;
		if (this.castable && this.craftable) this.craftable = false;
		return this;
	}
	
	public TinkerMaterial setCraftable(boolean val) {
		this.craftable = val;
		if (this.craftable && this.castable) this.castable = false;
		return this;
	}

	public boolean getCastable() {
		return this.castable;
	}
	
	public boolean getCraftable() {
		return this.craftable;
	}
	
	public TinkerMaterial setStat(TinkersStat stat, IntStat value) {
		this.stats.put(stat, value);
		return this;
	}

	public TinkerMaterial setStat(TinkersStat stat, FloatStat value) {
		this.stats.put(stat, value);
		return this;
	}

	public TinkerMaterial setStat(TinkersStat stat, BoolStat value) {
		this.stats.put(stat, value);
		return this;
	}

	public TinkerMaterial setTintColor(int color) {
		this.tintColor = color;
		return this;
	}
	
	public int getTintColor() {
		return this.tintColor;
	}
	
	@SuppressWarnings("rawtypes")
	public Float getFloatStat(TinkersStat stat) {
		IStat r = this.stats.get(stat);
		if (r.value() instanceof Float) {
			return (Float)r.value();
		} else if (r.value() instanceof Integer) {
			return Float.valueOf((float)((Integer)r.value()).intValue());
		} else {
			return Float.valueOf(0f);
		}
	}

	@SuppressWarnings("rawtypes")
	public Integer getIntStat(TinkersStat stat) {
		IStat r = this.stats.get(stat);
		if (r.value() instanceof Float) {
			return (Integer)r.value();
		} else if (r.value() instanceof Integer) {
			return Integer.valueOf((int)((Float)r.value()).floatValue());
		} else {
			return Integer.valueOf(0);
		}
	}

	@SuppressWarnings("rawtypes")
	public Boolean getBoolStat(TinkersStat stat) {
		IStat r = this.stats.get(stat);
		if (r.value() instanceof Boolean) {
			return (Boolean)r.value();
		} else {
			return Boolean.valueOf((Float)r.value()!=0f);
		}
	}

	public TinkerMaterial setName(String newName) {
		this.name = newName;
		return this;
	}
	
	public String getName() {
		return this.name;
	}

	public TinkerMaterial addTrait(String name, TinkersTraitLocation location) {
		List<String> traitsForLoc = this.traits.getOrDefault(location, new ArrayList<>());
		traitsForLoc.add(name);
		this.traits.put(location, traitsForLoc);
		
		return this;
	}
	
	private float calcDrawSpeed(final int durability) {
		float val;
		if (durability < 204) {
			val = 1.0f;
		} else {
			val = ((durability - 200) + 1) / 10.0f;
			val -= Math.floor(val);
		}

		return val;
	}

	public void settle() {
		if (this.tinkersMaterial != null) return;
		this.tinkersMaterial = new Material(this.name, this.tintColor);
		this.tinkersMaterial.addCommonItems(this.material.getCapitalizedName());
/*		if (this.traits.size() > 0) {
			this.traits.entrySet().stream()
			.filter(ent -> ent.getKey() == TinkersTraitLocation.GENERAL)
			.forEach(ent -> ent.getValue().forEach( trait -> this.tinkersMaterial.addTrait(trait)));
		} */

		this.tinkersMaterial.setCastable(this.castable);
		this.tinkersMaterial.setCraftable(this.craftable);
		this.tinkersMaterial.setFluid(this.material.getFluid());
		
		ItemStack represents = null;
		switch(this.material.getType()) {
		case GEM:
			represents = this.material.getItemStack(Names.GEM);
			break;
		case CRYSTAL:
		case MINERAL:
		case METAL:
			represents = this.material.getItemStack(Names.INGOT);
			break;
		case ROCK:
		case WOOD:
			represents = this.material.getBlockItemStack(Names.BLOCK);
			break;
		}
		
		if(represents != null) this.tinkersMaterial.setRepresentativeItem(represents);
	
		Arrays.asList(TinkersStatTypes.values()).stream()
		.forEach( stat -> this.tinkersStats.computeIfAbsent(stat, k -> this.getDefaultStat(k)) );

	}

	public ImmutableMap<TinkersTraitLocation, List<String>> getTraits() {
		Map<TinkersTraitLocation, List<String>> rv = Maps.newConcurrentMap();
		
		this.traits.entrySet().stream()
		.filter(ent -> ent.getKey() != TinkersTraitLocation.GENERAL)
		.forEach(ent -> {
			List<String> traits = rv.getOrDefault(ent.getKey(), Lists.newArrayList());
			traits.addAll(ent.getValue());
			rv.put(ent.getKey(), traits);
		});
		return ImmutableMap.copyOf(rv);
	}
	
	public ImmutableList<String> getTraits(TinkersTraitLocation location) {
		return ImmutableList.copyOf(this.traits.getOrDefault(location, new ArrayList<>()));
	}
	
	public List<IMaterialStats> getStats() {
		List<IMaterialStats> rv = new ArrayList<>();
		for(TinkersStatTypes k : TinkersStatTypes.values()) {
			rv.add(this.tinkersStats.getOrDefault(k, this.getDefaultStat(k)));
		}
		// generate stats, add them to rv
		return rv;
	}
	
	private IMaterialStats getDefaultStat(TinkersStatTypes k) {
		switch(k) {
		case ARROWSHAFT:
			return new ArrowShaftMaterialStats(this.getFloatStat(TinkersStat.ARROWSHAFT_MODIFIER),
					this.getIntStat(TinkersStat.ARROWSHAFT_BONUS_AMMO));
		case BOW:
			return new BowMaterialStats(this.getFloatStat(TinkersStat.BOW_DRAW_SPEED),
					this.getFloatStat(TinkersStat.BOW_RANGE), this.getFloatStat(TinkersStat.BOW_DAMAGE));
		case BOWSTRING:
			return new BowStringMaterialStats(this.getFloatStat(TinkersStat.BOWSTRING_MODIFIER));
		case EXTRA:
			return new ExtraMaterialStats(this.getIntStat(TinkersStat.EXTRA_DURABILTIY));
		case FLETCHING:
			return new FletchingMaterialStats(this.getFloatStat(TinkersStat.FLETCHING_ACCURACY),
					this.getFloatStat(TinkersStat.FLETCHING_MODIFIER));
		case HANDLE:
			return new HandleMaterialStats(this.getFloatStat(TinkersStat.BODY_MODIFIER),
					this.getIntStat(TinkersStat.BODY_DURABILITY));
		case HEAD:
			return new HeadMaterialStats(this.getIntStat(TinkersStat.HEAD_DURABILITY),
					this.getFloatStat(TinkersStat.MINING_SPEED), 
					this.getFloatStat(TinkersStat.HEAD_ATTACK_DAMAGE),
					this.getIntStat(TinkersStat.MINING_LEVEL));
		default:
			BaseMetals.logger.error("Unknown Tinkers Stat Type %s, returning null (cross your fingers, this will probably cause a crash)", k);
			return null;
		}
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}

	public TinkerMaterial addExtraMelting(String itemName, int amount) {
		this.extraMelting.put(itemName, Integer.valueOf(amount));
		return this;
	}
	
	public ImmutableMap<String,Integer> getExtraMeltings() {
		return ImmutableMap.copyOf(this.extraMelting);
	}
	
	public enum TinkersStat {
		HEAD_DURABILITY, MINING_SPEED, MINING_LEVEL, HEAD_ATTACK_DAMAGE,
		BODY_DURABILITY, BODY_MODIFIER, EXTRA_DURABILTIY, BOW_DRAW_SPEED,
		BOW_RANGE, BOWSTRING_MODIFIER, ARROWSHAFT_MODIFIER, FLETCHING_ACCURACY,
		BOW_DAMAGE, FLETCHING_MODIFIER, ARROWSHAFT_BONUS_AMMO
	}

	public enum TinkersStatTypes {
		HEAD, HANDLE, EXTRA, BOW, BOWSTRING, ARROWSHAFT, FLETCHING
	}
	
	public enum TinkersTraitLocation {
		HEAD, HANDLE, EXTRA, BOW, BOWSTRING, PROJECTILE, SHAFT, FLETCHING, GENERAL;
		
		@Override
		public String toString() {
			return this.name().toLowerCase();
		}
	}
	
	public interface IStat<T> {
		T value();
	}
	
	public class IntStat implements IStat<Integer> {
		private final Integer val;

		public IntStat(Integer v) {
			this.val  = v;
		}
		
		public Integer value() {
			return this.val;
		}
	}
	
	public class FloatStat implements IStat<Float> {
		private final Float val;
		
		public FloatStat(Float v) {
			this.val = v;
		}
		
		public Float value() {
			return this.val;
		}
	}
	
	public class BoolStat implements IStat<Boolean> {
		private final Boolean val;
		
		public BoolStat(Boolean v) {
			this.val = v;
		}
		
		public Boolean value() {
			return this.val;
		}
	}
	
	public Material getTinkerMaterial() {
		return this.tinkersMaterial;
	}
	
	public IMaterialStats getStat(TinkersStatTypes type) {
		return tinkersStats.get(type);
	}
}
