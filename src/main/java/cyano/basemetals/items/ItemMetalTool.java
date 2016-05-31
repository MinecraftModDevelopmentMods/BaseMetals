package cyano.basemetals.items;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import cyano.basemetals.init.Materials;
import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;

/**
 * Tools
 */
public abstract class ItemMetalTool extends net.minecraft.item.ItemTool implements IMetalObject {

	protected final MetalMaterial metal;
	protected final Set<String> toolTypes;
	protected final String repairOreDictName;
	protected final boolean regenerates;
	protected final long regenInterval = 200; 
	protected final float attackDamage;

	/**
	 * 
	 * @param toolType The string descriptor for this type of tool ("pickaxe", 
	 * "axe", "shovel", "hoe", or "sword")
	 * @param metal The material that this tool is made from
	 * @param attackDamageBonus The amount of damage dealt by this tool beyond 
	 * the base for the material (4 for swords, 3 for axes, 2 for picks, 1 for shovels, and 0 for hoes)
	 */
	protected ItemMetalTool(String toolType, MetalMaterial metal, float attackDamageBonus) {
		super(attackDamageBonus + metal.getBaseAttackDamage() - ToolMaterial.IRON.getDamageVsEntity(), ToolMaterial.IRON, new HashSet<Block>());
		this.metal = metal;
		this.setMaxDamage(metal.getToolDurability());
		this.efficiencyOnProperMaterial = metal.getToolEfficiency();
		// this.damageVsEntity = attackDamage + metal.getBaseAttackDamage(); // damageVsEntity  is private, sadly
		this.attackDamage = attackDamageBonus + metal.getBaseAttackDamage();
		// this.toolClass = toolType; toolClass is private, sadly
		this.toolTypes = new HashSet<>();
		toolTypes.add(toolType);
		repairOreDictName = "ingot"+metal.getCapitalizedName();
		if(metal.equals(Materials.starsteel)) {
			regenerates = true;
		} else {
			regenerates = false;
		}
		this.setCreativeTab(CreativeTabs.tabTools);
	}

	public ToolMaterial getToolMaterial() {
		return this.toolMaterial;
	}

	@Override
	public int getItemEnchantability() {
		return this.toolMaterial.getEnchantability();
	}

	public String getToolMaterialName() {
		return this.toolMaterial.toString();
	}

	@Override
	public boolean getIsRepairable(final ItemStack intputItem, final ItemStack repairMaterial) {
		List<ItemStack> acceptableItems = OreDictionary.getOres(repairOreDictName);
		for(ItemStack i : acceptableItems ){
			if(ItemStack.areItemsEqual(i, repairMaterial)) return true;
		}
		return false;
	}

	@Override
	public int getHarvestLevel(final ItemStack item, final String typeRequested) {
		if (typeRequested != null && toolTypes.contains(typeRequested)) {
			return metal.getToolHarvestLevel();
		}
		return -1;
	}

	@Override
	public Set<String> getToolClasses(final ItemStack item) {
		return toolTypes;
	}

	@Override
	public float getStrVsBlock(final ItemStack tool, final Block target) {
		if(this.canHarvestBlock(target,tool)) {
			return Math.max(1.0f,metal.getToolEfficiency());
		} else {
			return 1.0f;
		}
	}

	@Override
	public boolean hitEntity(final ItemStack item, final EntityLivingBase target, final EntityLivingBase attacker) {
		super.hitEntity(item, target, attacker);
		extraEffectsOnAttack(item, target, attacker);
		return true;
	}

	public void extraEffectsOnAttack(final ItemStack item, final EntityLivingBase target, final EntityLivingBase attacker){
		if(metal.equals(Materials.coldiron)) {
			if(target.isImmuneToFire()) {
				DamageSource extraDamage = DamageSource.generic; 
				target.attackEntityFrom(extraDamage, 3f);
			}
		} else if(metal.equals(Materials.adamantine)) {
    		if(target.getMaxHealth() > 20f) {
    			DamageSource extraDamage = DamageSource.generic; 
    			target.attackEntityFrom(extraDamage, 2f);
    		}
		} else if(metal.equals(Materials.mithril)) {
			if(target.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD){
				final PotionEffect poison = new PotionEffect(20,60,3);
				final PotionEffect blind = new PotionEffect(15,60,1);
				target.addPotionEffect(poison);
				target.addPotionEffect(blind);
			}
		}
	}

	@Override
	public void onCreated(final ItemStack item, final World world, final EntityPlayer crafter) {
		super.onCreated(item, world, crafter);
		extraEffectsOnCrafting(item, world, crafter);
	}

	public void extraEffectsOnCrafting(final ItemStack item, final World world, final EntityPlayer crafter) {
		if(metal.equals(Materials.mithril)) {
			item.addEnchantment(Enchantment.smite, 2);
		}
	}
	
	@Override
	public void onUpdate(final ItemStack item, final World world, final Entity player, final int inventoryIndex, final boolean isHeld) {
		if(regenerates && !world.isRemote && isHeld && item.getItemDamage() > 0 && world.getTotalWorldTime() % regenInterval == 0){
			item.setItemDamage(item.getItemDamage() - 1);
		}
	}

	@Override
	public boolean canHarvestBlock(final Block target) {
		if(this.toolTypes.contains(target.getHarvestTool(target.getDefaultState()))){
			return metal.getToolHarvestLevel() >= target.getHarvestLevel(target.getDefaultState());
		}
		return false;
	}

	/**
	 * 
	 * @return The amount of damage dealt to an entity when attacked by this 
	 * item
	 */
	public float getAttackDamage() {
		return attackDamage;
	}

	public String getMaterialName() {
		return metal.getName();
	}

	@Override
	public MetalMaterial getMetalMaterial(){
		return metal;
	}
}
