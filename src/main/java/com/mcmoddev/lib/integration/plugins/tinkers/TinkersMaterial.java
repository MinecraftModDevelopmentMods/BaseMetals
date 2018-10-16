package com.mcmoddev.lib.integration.plugins.tinkers;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import c4.conarm.lib.materials.CoreMaterialStats;
import c4.conarm.lib.materials.PlatesMaterialStats;
import c4.conarm.lib.materials.TrimMaterialStats;
import net.minecraft.inventory.EntityEquipmentSlot;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mcmoddev.lib.data.MaterialStats;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.IForgeRegistryEntry;
import slimeknights.tconstruct.library.materials.ArrowShaftMaterialStats;
import slimeknights.tconstruct.library.materials.BowMaterialStats;
import slimeknights.tconstruct.library.materials.BowStringMaterialStats;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.FletchingMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;

public class TinkersMaterial extends IForgeRegistryEntry.Impl<TinkersMaterial> implements IMMDObject {
	private Map<String, Number> rawStats;
	private List<Pair<String, TinkerTraitLocation>> traits;
	private List<ItemStack> items;
	private List<FluidStack> recipe = Lists.newCopyOnWriteArrayList();
	private Set<Pair<ItemStack,Integer>> extraMeltings;
	private ItemStack representativeItem;
	private Material ticonMat = Material.UNKNOWN;
	private MMDMaterial baseMaterial;
	private FluidStack recipeOutput;
	private boolean toolForge;
	private boolean castable = true;
	private boolean craftable = false;

	public TinkersMaterial(MMDMaterial base) {
		this.baseMaterial = base;
		this.rawStats = Maps.newConcurrentMap();
		this.traits = Lists.newCopyOnWriteArrayList();
		this.items = Lists.newCopyOnWriteArrayList();
		this.extraMeltings = Sets.newLinkedHashSet();
		this.representativeItem = base.getItemStack(Names.INGOT);
		this.setRegistryName(this.baseMaterial.getRegistryName());
	}
	
	public TinkersMaterial addStat(String statName, Number statValue) {
		this.rawStats.put(statName, statValue);
		return this;
	}

	public TinkersMaterial addTrait(String trait) {
		this.traits.add(Pair.of(trait, TinkerTraitLocation.GENERAL));
		return this;
	}
	
	public TinkersMaterial addTrait(String trait, TinkerTraitLocation location) {
		this.traits.add(Pair.of(trait, location));
		return this;
	}
	
	public TinkersMaterial setRepresentativeItem(Item item) {
		return this.setRepresentativeItem(new ItemStack(item));
	}
	
	public TinkersMaterial setRepresentativeItem(Block block) {
		return this.setRepresentativeItem(Item.getItemFromBlock(block));
	}
	
	public TinkersMaterial setRepresentativeItem(ItemStack itemStack) {
		this.representativeItem = itemStack.copy();
		return this;
	}
	
	public TinkersMaterial addItem(Item item) {
		return this.addItem(new ItemStack(item));
	}
	
	public TinkersMaterial addItem(Block block) {
		return this.addItem(Item.getItemFromBlock(block));
	}
	
	public TinkersMaterial addItem(ItemStack itemStack) {
		this.items.add(itemStack.copy());
		return this;
	}

	public TinkersMaterial addAlloyRecipe(int outputAmount, FluidStack...recipe) {
		this.recipeOutput = FluidRegistry.getFluidStack(this.getMMDMaterial().getName(), outputAmount);
		this.recipe.addAll((List<FluidStack>)Arrays.asList(recipe).stream().map(obj -> (FluidStack)obj).collect(Collectors.toList()));
		return this;
	}
	
	public TinkersMaterial addAlloyRecipe(int outputAmount, Object...recipe) {
		this.recipeOutput = FluidRegistry.getFluidStack(this.getMMDMaterial().getName(), outputAmount);
		for(int i = 0; i < recipe.length; i += 2) {
			this.recipe.add(FluidRegistry.getFluidStack((String)recipe[i], (Integer)recipe[i+1]));
		}
		return this;
	}

	public ArrowShaftMaterialStats getArrowShaftStats() {
		/*
		 * TODO: figure out a nice setup for "modifier" and "bonus" that is based on existing mod stats so
		 * the crappy defaults don't get used
		 */
		float modifier = this.rawStats.getOrDefault(TinkerConstants.TinkerStatNames.ARROWSHAFT_MODIFIER,
				Float.valueOf(1.0f)).floatValue();
		int bonus = this.rawStats.getOrDefault(TinkerConstants.TinkerStatNames.ARROWSHAFT_BONUS_AMMO,
				Integer.valueOf(1)).intValue();
		return new ArrowShaftMaterialStats(modifier, bonus);
	}

	private static Float calcDrawSpeed(final int durability) {
		float val;
		if (durability < 204) {
			val = 1.0f;
		} else {
			val = ((durability - 200) + 1) / 10.0f;
			val -= Math.floor(val);
		}

		return val;
	}
	
	public BowMaterialStats getBowStats() {
		/*
		 * TODO: figure out something nice for BOW_RANGE so we don't always have this fallback to a pretty terrible default
		 */
		float speed = this.rawStats.getOrDefault(TinkerConstants.TinkerStatNames.BOW_DRAW_SPEED,
				calcDrawSpeed(this.baseMaterial.getToolDurability())).floatValue();
		float damage = this.rawStats.getOrDefault(TinkerConstants.TinkerStatNames.BOW_DAMAGE,
				Float.valueOf(this.baseMaterial.getBaseAttackDamage() + 3)).floatValue();
		float range = this.rawStats.getOrDefault(TinkerConstants.TinkerStatNames.BOW_RANGE,
				Float.valueOf(15)).floatValue();

		return new BowMaterialStats(speed, range, damage);
	}
	
	public BowStringMaterialStats getBowStringStats() {
		// TODO: this needs to have something besides the default, like others
		float modifier = this.rawStats.getOrDefault(TinkerConstants.TinkerStatNames.BOWSTRING_MODIFIER,
				1.0f).floatValue();

		return new BowStringMaterialStats(modifier);
	}
	
	public ExtraMaterialStats getExtraStats() {
		int durability = this.rawStats.getOrDefault(TinkerConstants.TinkerStatNames.EXTRA_DURABILTIY,
				this.baseMaterial.getToolDurability() / 10).intValue();

		return new ExtraMaterialStats(durability);
	}

	public FletchingMaterialStats getFletchingStats() {
		// TODO: this needs to have something besides the default, like others
		float accuracy = this.rawStats.getOrDefault(TinkerConstants.TinkerStatNames.FLETCHING_ACCURACY,
				Float.valueOf(1.0f)).floatValue();
		float modifier = this.rawStats.getOrDefault(TinkerConstants.TinkerStatNames.FLETCHING_MODIFIER,
				Float.valueOf(1.0f)).floatValue();

		return new FletchingMaterialStats(accuracy, modifier);
	}
	
	public HandleMaterialStats getHandleStats() {
		int durability = this.rawStats.getOrDefault(TinkerConstants.TinkerStatNames.BODY_DURABILITY,
				this.baseMaterial.getToolDurability() / 7).intValue();
		float modifier = this.rawStats.getOrDefault(TinkerConstants.TinkerStatNames.BODY_MODIFIER,
				Float.valueOf((this.baseMaterial.getStat(MaterialStats.MAGICAFFINITY) * 2) / 9)).floatValue();
		
		return new HandleMaterialStats(modifier, durability);
	}
	
	public HeadMaterialStats getHeadStats() {
		int durability = this.rawStats.getOrDefault(TinkerConstants.TinkerStatNames.HEAD_DURABILITY,
				this.baseMaterial.getToolDurability()).intValue();
		float speed = this.rawStats.getOrDefault(TinkerConstants.TinkerStatNames.MINING_SPEED,
				Float.valueOf(this.baseMaterial.getStat(MaterialStats.HARDNESS) * 0.85f)).floatValue();
		float damage = this.rawStats.getOrDefault(TinkerConstants.TinkerStatNames.HEAD_ATTACK_DAMAGE,
				Float.valueOf(this.baseMaterial.getBaseAttackDamage() * 2)).floatValue();
		int miningLevel = this.rawStats.getOrDefault(TinkerConstants.TinkerStatNames.MINING_LEVEL,
				this.baseMaterial.getToolHarvestLevel()).intValue();
		
		return new HeadMaterialStats(durability, speed, damage, miningLevel);
	}

	public CoreMaterialStats getCoreStats()
	{
		// TODO: needs proper stat calculation
		final float minimum = 5f;

		final float hardnessFactor = 1.25f;

		int durability = (int) (2.0f * this.baseMaterial.getArmorMaxDamageFactor());

		float defense = hardnessFactor * (float) this.baseMaterial.getDamageReductionArray()[EntityEquipmentSlot.CHEST.getIndex()] + minimum;

		return new CoreMaterialStats(durability, defense);
	}

	public PlatesMaterialStats getPlatesStats()
	{
		// TODO: needs proper stat calculation
		float modifier = (this.baseMaterial.getStat(MaterialStats.MAGICAFFINITY) * 2) / 9;

		int durability = this.baseMaterial.getToolDurability() / 7;

		float toughness = this.baseMaterial.getStat(MaterialStats.HARDNESS) > 10
				? (int) (this.baseMaterial.getStat(MaterialStats.HARDNESS) / 5) : 0;

		return new PlatesMaterialStats(modifier, durability, toughness);
	}

	public TrimMaterialStats getTrimStats()
	{
		// TODO: needs proper stat calculation
		int durability = this.baseMaterial.getToolDurability() / 10;

		return new TrimMaterialStats(durability);
	}

	public TinkersMaterial setToolForge(boolean toolForge) {
		this.toolForge = toolForge;
		return this;
	}
	
	public boolean getToolForge() {
		return this.toolForge;
	}

	/*
	 * classically we've had "castable" and "craftable" as incompatible...
	 * turns out that TiCon itself doesn't view them as incompatible and Obsidian has both set.
	 */
	public TinkersMaterial setCastable(boolean castable) {
		this.castable = castable;
		return this;
	}

	public TinkersMaterial setCraftable(boolean craftable) {
		this.craftable = craftable;
		return this;
	}
	
	public boolean getCastable() {
		return this.castable;
	}
	
	public boolean getCraftable() {
		return this.craftable;
	}
	
	public ItemStack getRepresentativeItem() {
		return this.representativeItem.copy();
	}
	
	public TinkersMaterial create() {
		if(this.ticonMat != Material.UNKNOWN) return this;
		
		this.ticonMat = new Material(this.baseMaterial.getName(), this.baseMaterial.getTintColor());
		
		return this;
	}

	public TinkersMaterial addExtraMelting(Item item, int amount) {
		return this.addExtraMelting(new ItemStack(item), amount);
	}
	
	public TinkersMaterial addExtraMelting(Block block, int amount) {
		return this.addExtraMelting(Item.getItemFromBlock(block), amount);
	}
	
	public TinkersMaterial addExtraMelting(ItemStack itemStack, int amount) {
		this.extraMeltings.add(Pair.of(itemStack.copy(), amount));
		return this;
	}
	
	public TinkersMaterial addExtraMelting(String name, int amount) {
		return this.addExtraMelting(this.baseMaterial.getItemStack(name), amount);
	}
	
	public List<FluidStack> getAlloyRecipe() {
		List<FluidStack> rv = new LinkedList<>();
		rv.add(this.recipeOutput);
		rv.addAll(this.recipe);

		return rv;
	}
	
	public String getName() {
		return this.baseMaterial.getName();
	}
	
	public Material getTinkerMaterial() {
		if(this.ticonMat == Material.UNKNOWN) {
			return this.create().getTinkerMaterial();
		}
		
		return this.ticonMat;
	}
	
	public boolean hasTraits() {
		return !this.traits.isEmpty();
	}
	
	public boolean hasAlloyRecipe() {
		return !this.recipe.isEmpty();
	}
	
	public List<Pair<String, TinkerTraitLocation>> getTraits() {
		return Collections.unmodifiableList(this.traits);
	}
	
	@Override
	public MMDMaterial getMMDMaterial() {
		return this.baseMaterial;
	}
}
