package com.mcmoddev.lib.integration.plugins.tinkers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

import com.mcmoddev.lib.data.MaterialStats;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.item.Item;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import slimeknights.tconstruct.library.materials.ArrowShaftMaterialStats;
import slimeknights.tconstruct.library.materials.BowMaterialStats;
import slimeknights.tconstruct.library.materials.BowStringMaterialStats;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.FletchingMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.traits.ITrait;
import slimeknights.tconstruct.shared.TinkerFluids;

public class TCMaterial {
	// internal vars - names should mostly be self-explanatory
	
	// book-keeping and stuff the user should only have to worry about once
	private String name;
	private int tintColor;
	private MMDMaterial material;
	private Material tinkersMaterial;
	private boolean configured = false; // blargh
	
	// book keeping stuffs - trait handling bits
	private Map<String, List<String>> traits;
	private Map<String, List<ITrait>> resolvedTraits;
	// extra melting stuffs
	private Map<String, Integer> extraMelting;
	
	// broken-out stats
	private int headDurability;
	private float miningSpeed;
	private int miningLevel;
	private float headAttackDamage;
	private int bodyDurability;
	private float bodyModifier;
	private int extraDurability;
	private float bowDrawingSpeed;
	private float bowDamage;
	private float bowRange;
	private float bowstringModifier;
	private float shaftModifier;
	private float fletchingAccuracy;
	private float fletchingModifier;
	private int shaftBonusAmmo;
	private boolean castable = true;
	private boolean craftable = false;
	private int ingotAmount;
	private boolean toolForge = false;
	
	// for representative item stuffs
	private Item repItem;
	private String repName;

	// actual stats in the form Tinkers' needs
	private HeadMaterialStats headStats;
	private HandleMaterialStats handleStats;
	private ExtraMaterialStats extraStats;
	private BowMaterialStats bowStats;
	private BowStringMaterialStats bowStringStats;
	private ArrowShaftMaterialStats arrowShaftStats;
    private FletchingMaterialStats fletchingStats;

	// All constructors are private - use the 'get' (or whatever they wind up being named) static's
	private TCMaterial() {
		this.traits = new HashMap<>();
		this.resolvedTraits = new HashMap<>();
		this.extraMelting = new HashMap<>();
	}

	private TCMaterial(String name) {
		this();
		this.name = name;
	}

	private TCMaterial(String name, int color) {
		this(name);
		this.tintColor = color;
	}

	public TCMaterial(MMDMaterial material) {
		this();
		this.material = material;
		this.genStatsInternal();
	}

	public TCMaterial(MMDMaterial material, String name) {
		this(material);
		this.name = name;
	}

	public TCMaterial(MMDMaterial material, String name, int color) {
		this(material, name);
		this.tintColor = color;
	}

	
	// Use these functions to get the object
	public static TCMaterial get() {
		return new TCMaterial();
	}
	
	public static TCMaterial get(@Nonnull final String name) {
		return new TCMaterial(name);
	}
	
	public static TCMaterial get(@Nonnull final String name, @Nonnull final int color) {
		return new TCMaterial(name, color);
	}
	
	public static TCMaterial get(@Nonnull final MMDMaterial material) {
		return new TCMaterial(material);
	}
	
	public static TCMaterial get(@Nonnull final MMDMaterial material, @Nonnull final String name) {
		return new TCMaterial(material, name);
	}
	
	public static TCMaterial get(@Nonnull final MMDMaterial material, @Nonnull final String name, @Nonnull final int color) {
		return new TCMaterial(material, name, color);
	}

	// public utility funcs
	public void genStatsFromSource() {
		if( this.material != null ) {
			this.genStatsInternal();
			configured = true;			
		}
	}
	
	// trait allocation
	public TCMaterial addTrait(@Nonnull final String location, @Nonnull final String name) {
		List<String> traitsAtLoc = this.traits.getOrDefault(location, new ArrayList<>());
		if( traitsAtLoc.contains(name))
			return this;
		
		traitsAtLoc.add(name);
		this.traits.put(location, traitsAtLoc);
		return this;
	}
	
	
	// Setters & Getters
	public TCMaterial setSourceMaterial(@Nonnull final MMDMaterial material) {
		this.material = material;
		if( this.name == null ) {
			this.name = material.getName();
		}
		if( this.tintColor == 0) {
			this.tintColor = material.getTintColor();
		}
		return this;
	}
	
	public TCMaterial setCastable() {
		this.castable = true;
		this.craftable = !this.castable;
		configured = true;
		return this;
	}
	
	public TCMaterial setCraftable() {
		this.craftable = true;
		this.castable = !this.craftable;		
		configured = true;
		return this;
	}
	
	public TCMaterial setHeadDurability(@Nonnull final int durability) {
		this.headDurability = durability;
		configured = true;		
		return this;
	}
	
	public TCMaterial setBodyDurability(@Nonnull final int durability) {
		this.bodyDurability = durability;
		configured = true;		
		return this;
	}
	
	public TCMaterial setExtraDurability(@Nonnull final int durability) {
		this.extraDurability = durability;
		configured = true;		
		return this;
	}
	
	public TCMaterial setMiningSpeed(@Nonnull final float speed) {
		this.miningSpeed = speed;
		configured = true;
		return this;
	}
	
	public TCMaterial setminingLevel(@Nonnull final int level) {
		this.miningLevel = level;
		configured = true;		
		return this;
	}
	
	public TCMaterial setBodyModifier(@Nonnull final float modifier) {
		this.bodyModifier = modifier;
		configured = true;
		return this;
	}
	
	public TCMaterial setBowDrawingSpeed(@Nonnull final float drawingSpeed) {
		this.bowDrawingSpeed = drawingSpeed;
		configured = true;
		return this;
	}
	
	public TCMaterial setBowDamage(@Nonnull final float damage) {
		this.bowDamage = damage;
		configured = true;
		return this;
	}
	
	public TCMaterial setBowRange(@Nonnull final float range) {
		this.bowRange = range;
		configured = true;
		return this;
	}
	
	public TCMaterial setBowstringModifier(@Nonnull final float modifier) {
		this.bowstringModifier = modifier;
		configured = true;
		return this;
	}
	
	public TCMaterial setShaftModifier(@Nonnull final float modifier) {
		this.shaftModifier = modifier;
		configured = true;
		return this;
	}
	
	public TCMaterial setFletchingAccuracy(@Nonnull final float accuracy) {
		this.fletchingAccuracy = accuracy;
		configured = true;
		return this;
	}
	
	public TCMaterial setFletchingModifier(@Nonnull final float modifier) {
		this.fletchingModifier = modifier;
		configured = true;
		return this;
	}

	public String getName() {
		return this.name;
	}

	public int getTintColor() {
		return this.tintColor;
	}
	
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
	
	public int getHeadDurability() {
		return this.headDurability;
	}
	
	public float getMiningSpeed() {
		return this.miningSpeed;
	}
	
	public int getMiningLevel() {
		return this.miningLevel;
	}
	
	public float getHeadAttackDamage() {
		return this.headAttackDamage;
	}
	
	public int getBodyDurability() {
		return this.bodyDurability;
	}
	
	public float getBodyModifier() {
		return this.bodyModifier;
	}
	
	public int getExtraDurability() {
		return this.extraDurability;
	}
	
	public float getBowDrawingSpeed() {
		return this.bowDrawingSpeed;
	}
	
	public float getBowDamage() {
		return this.bowDamage;
	}
	
	public float getBowRange() {
		return this.bowRange;
	}
	
	public float getBowstringModifier() {
		return this.bowstringModifier;
	}
	
	public float getShaftModifier() {
		return this.shaftModifier;
	}
	
	public float getFletchingAccuracy() {
		return this.fletchingAccuracy;
	}
	
	public float getFletchingModifier() {
		return this.fletchingModifier;
	}
	
	public int getShaftBonusAmmo() {
		return this.shaftBonusAmmo;
	}
	
	public boolean getCastable() {
		return this.castable;
	}
	
	public boolean getCraftable() {
		return this.craftable;
	}

	// material stats stuff!
	public HeadMaterialStats getHeadStats() {
		if( this.headStats == null )
			this.headStats = new HeadMaterialStats(this.headDurability, this.miningSpeed, this.headAttackDamage, this.miningLevel);
		
		return this.headStats;
	}

	public HandleMaterialStats getHandleStats() {
		if( this.handleStats == null )
			this.handleStats =  new HandleMaterialStats(this.bodyModifier, this.bodyDurability);
		
		return this.handleStats;
	}
	
	public ExtraMaterialStats getExtraStats() {
		if( this.extraStats == null )
			this.extraStats =  new ExtraMaterialStats(this.extraDurability);

		return this.extraStats;
	}
	
	public BowMaterialStats getBowStats() {
		if( this.bowStats == null )
			this.bowStats =  new BowMaterialStats(this.bowDrawingSpeed, this.bowRange, this.bowDamage);

		return this.bowStats;
	}
	
	public BowStringMaterialStats getBowStringStats() {
		if( this.bowStringStats == null )
			this.bowStringStats =  new BowStringMaterialStats(this.bowstringModifier);

		return this.bowStringStats;
	}
	
	public ArrowShaftMaterialStats getArrowStats() {
		if( this.arrowShaftStats == null )
			this.arrowShaftStats =  new ArrowShaftMaterialStats(this.shaftModifier, this.shaftBonusAmmo);

		return this.arrowShaftStats;
	}
	
	public FletchingMaterialStats getFletchingStats() {
		if( this.fletchingStats == null )
			this.fletchingStats =  new FletchingMaterialStats(this.fletchingAccuracy, this.fletchingModifier);

		return this.fletchingStats;
	}

	public Material getMaterial() {
		if( this.tinkersMaterial == null ) {
			String posName = this.material!=null?this.material.getName():"i_is_b0rken";
			String workingName = this.name!=null?this.name:posName;
			int posCol = this.material!=null?this.material.getTintColor():0xFFFFFFFF;
			int color = this.tintColor!=0?this.tintColor:posCol;
			this.tinkersMaterial = new Material(workingName, color, false);
			if( this.name == null )
				this.name = workingName;
		}
		
		return this.tinkersMaterial;
	}

	public TCMaterial setHeadStats(HeadMaterialStats headStats) {
		this.headStats = headStats;
		this.headDurability = headStats.durability;
		this.headAttackDamage = headStats.attack;
		this.miningSpeed = headStats.miningspeed;
		this.miningLevel = headStats.harvestLevel;
		this.configured = true;
		return this;
	}

    public TCMaterial setHandleStats(HandleMaterialStats handleStats) {
		this.handleStats = handleStats;
		this.bodyDurability = handleStats.durability;
		this.bodyModifier = handleStats.modifier;
		this.configured = true;
		return this;
	}

    public TCMaterial setExtraStats(ExtraMaterialStats extraStats) {
		this.extraStats = extraStats;
		this.extraDurability = extraStats.extraDurability;
		this.configured = true;
		return this;
	}

    public TCMaterial setBowStats(BowMaterialStats bowStats) {
		this.bowStats = bowStats;
		this.bowDamage = bowStats.bonusDamage;
		this.bowDrawingSpeed = bowStats.drawspeed;
		this.bowRange = bowStats.range;
		this.configured = true;
		return this;
	}

	public TCMaterial setArrowShaftStats(ArrowShaftMaterialStats arrowShaftStats) {
		this.arrowShaftStats = arrowShaftStats;
		this.shaftBonusAmmo = arrowShaftStats.bonusAmmo;
		this.shaftModifier = arrowShaftStats.modifier;
		this.configured = true;
		return this;
	}

	public TCMaterial setBowStringStats(BowStringMaterialStats bowStringStats) {
		this.bowStringStats = bowStringStats;
		this.bowstringModifier = bowStringStats.modifier;
		this.configured = true;
		return this;
	}

	public TCMaterial setFletchingStats(FletchingMaterialStats fletchingStats) {
		this.fletchingStats = fletchingStats;
		this.fletchingAccuracy = fletchingStats.accuracy;
		this.fletchingModifier = fletchingStats.modifier;
		this.configured = true;
		return this;
	}
	
	private Fluid getActualFluid(@Nonnull final MMDMaterial mat) {
		Fluid outFluid;

		switch(mat.getName()) {
		case "iron":
			outFluid = TinkerFluids.iron;
			break;
		case "gold":
			outFluid = TinkerFluids.gold;
			break;
		case "emerald":
			outFluid = TinkerFluids.emerald;
			break;
		case "aluminumbrass":
			outFluid = TinkerFluids.alubrass;
			break;
		case "aluminum":
			outFluid = TinkerFluids.aluminum;
			break;
		case "copper":
			outFluid = TinkerFluids.copper;
			break;
		case "brass":
			outFluid = TinkerFluids.brass;
			break;
		case "tin":
			outFluid = TinkerFluids.tin;
			break;
		case "bronze":
			outFluid = TinkerFluids.bronze;
			break;
		case "zinc":
			outFluid = TinkerFluids.zinc;
			break;
		case "lead":
			outFluid = TinkerFluids.lead;
			break;
		case "nickel":
			outFluid = TinkerFluids.nickel;
			break;
		case "silver":
			outFluid = TinkerFluids.silver;
			break;
		case "electrum":
			outFluid = TinkerFluids.electrum;
			break;
		case "steel":
			outFluid = TinkerFluids.steel;
			break;
		default:
			outFluid = mat.getFluid();
		}

		if( outFluid == null )
			outFluid = FluidRegistry.getFluid(mat.getName());
		
		return outFluid;
	}

	public Fluid getFluid() {
		return getActualFluid(this.material);
	}
	
	public TCMaterial setFluid(@Nonnull final Fluid fluid) {
		Fluid f = getFluid();
		if( f.equals(fluid) ) {
			this.tinkersMaterial.setFluid(fluid);
		} else {
			this.tinkersMaterial.setFluid(f);
		}
		return this;
	}
	
	public TCMaterial setIngotAmount(@Nonnull final int amount) {
		this.ingotAmount = amount;
		return this;
	}
	
	public int getIngotAmount() {
		return this.ingotAmount;
	}
	
	public TCMaterial setRepresentativeItem(@Nullable String itemName) {
		this.repName = itemName;
		if( this.material != null ) {
			if( this.material.hasItem(itemName) ) {
				this.repItem = this.material.getItem(itemName);
			} else if( this.material.hasItem(Names.INGOT) ) {
				this.repItem = this.material.getItem(Names.INGOT);
			} else {
				this.repItem = null;
			}
		}
		return this;
	}

	public Item getRepresentativeItem() {
		// if it hasn't been done yet, just return null - or the item doesn't exist - just return null
		// which is what we do here :)
		this.setRepresentativeItem(Names.INGOT.toString());
		return this.repItem;
	}
	
	public String getRepresentativeItemName() {
		if(this.repName == null) {
			return Oredicts.INGOT+this.material.getName();
		} else {
			return this.repName;
		}
	}
	
	public TCMaterial setToolForge() {
		this.toolForge = true;
		return this;
	}
	
	public boolean toolForge() {
		return this.toolForge;
	}
	
	public TCMaterial addExtraMelting(@Nonnull String itemName, @Nonnull int fluidValue) {
		if( !this.extraMelting.containsKey(itemName) )
			this.extraMelting.put(itemName, fluidValue);

		return this;
	}
	
	public Map<String, Integer> getExtraMelting() {
		if( this.extraMelting.isEmpty() )
			return Collections.emptyMap();
		
		return Collections.unmodifiableMap(this.extraMelting);
	}
	
	// resolve any un-set values
	// we only do this if someone hasn't actually configured the material
	// otherwise we'd be overwriting their stuff
	public void settle() {
		if( this.tinkersMaterial == null ) {
			this.tinkersMaterial = this.getMaterial();
			this.configured = false;
		}
		
		this.tinkersMaterial.setFluid(this.getFluid());
		this.tinkersMaterial.setVisible();
		
		if( !this.configured ) {
			this.genStatsInternal();
			this.setHeadStats(this.getHeadStats());
			this.setBowStats(this.getBowStats());
			this.setBowStringStats(this.getBowStringStats());
			this.setExtraStats(this.getExtraStats());
			this.setHandleStats(this.getHandleStats());
			this.setArrowShaftStats(this.getArrowStats());
			this.setFletchingStats(this.getFletchingStats());
			if( this.ingotAmount == 0 ) {
				// doing it this way to keep from an if-else mess if/when more values get added
				switch( this.material.getType()) {
				case GEM:
					this.ingotAmount = 666;
					break;
				case ROCK:
				case WOOD:
				case CRYSTAL:
				case MINERAL:
				case METAL:
				default:
					this.ingotAmount = 144;
					break;
				}
			}
		}
	}

	public void resolveTraits() {
		for( Entry<String, List<String>> ent : this.traits.entrySet() ) {
			String loc = ent.getKey();
			List<ITrait> resTraits = this.resolvedTraits.getOrDefault(loc, new ArrayList<ITrait>());
			for( String traitName : ent.getValue() ) {
				ITrait temp = TraitRegistry.get(traitName);
				if( temp != null ) {
					resTraits.add(temp);
				}
			}
			this.resolvedTraits.put(loc, resTraits);
		}
	}
	
	public Map<String,List<ITrait>> getTraits() {
		if( this.resolvedTraits.isEmpty() ) {
			return Collections.emptyMap();
		}
		
		return Collections.unmodifiableMap(this.resolvedTraits);
	}
	
	// internal helpers
	private void genStatsInternal() {
        this.headDurability = this.material.getToolDurability();
        this.miningSpeed = this.material.getStat(MaterialStats.HARDNESS) * 0.85f;
        this.miningLevel = this.material.getToolHarvestLevel();
        this.headAttackDamage = this.material.getBaseAttackDamage() * 2;
        this.bodyDurability = this.material.getToolDurability() / 7;
        this.bodyModifier = (this.material.getStat(MaterialStats.HARDNESS) + this.material.getStat(MaterialStats.MAGICAFFINITY) * 2) / 9;
        this.extraDurability = this.material.getToolDurability() / 10;
        this.bowDrawingSpeed = calcDrawSpeed(this.material.getToolDurability());
        this.bowDamage = this.material.getBaseAttackDamage() + 3;
        this.bowRange = 15.0f;
        this.bowstringModifier = 1.0f;
        this.shaftModifier = 1.0f;
        this.fletchingAccuracy = 1.0f;
        this.fletchingModifier = 1.0f;
        this.shaftBonusAmmo = 1;
	}

	private float calcDrawSpeed(int durability) {
        float val;
        if (durability < 204) {
            val = 1.0f;
        } else {
            val = ((durability - 200) + 1) / 10.0f;
            val -= Math.floor(val);
        }

        return val;
	}
	
}
