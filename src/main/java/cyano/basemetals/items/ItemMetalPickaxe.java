package cyano.basemetals.items;

import cyano.basemetals.init.Materials;
import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Pickaxes
 */
public class ItemMetalPickaxe extends ItemPickaxe  implements IMetalObject {

	protected final MetalMaterial metal;
	protected final Set<String> toolTypes;
	protected final String repairOreDictName;
	protected final boolean regenerates;
	protected final long regenInterval = 200; 

	public ItemMetalPickaxe(MetalMaterial metal) {
		super(Materials.getToolMaterialFor(metal));
		this.metal = metal;
		this.setMaxDamage(metal.getToolDurability());
		this.efficiencyOnProperMaterial = metal.getToolEfficiency();
		this.toolTypes = new HashSet<>();
		toolTypes.add("pickaxe");
		repairOreDictName = "ingot"+metal.getCapitalizedName();
		if(metal.equals(Materials.starsteel)) {
			regenerates = true;
		} else {
			regenerates = false;
		}
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
		for(ItemStack i : acceptableItems ) {
			if(ItemStack.areItemsEqual(i, repairMaterial)) return true;
		}
		return false;
	}

	@Override
	public boolean hitEntity(final ItemStack item, final EntityLivingBase target, final EntityLivingBase attacker) {
		super.hitEntity(item, target, attacker);
		MetalToolEffects.extraEffectsOnAttack(metal,item, target, attacker);
		return true;
	}

	@Override
	public void onCreated(final ItemStack item, final World world, final EntityPlayer crafter) {
		super.onCreated(item, world, crafter);
		MetalToolEffects.extraEffectsOnCrafting(metal,item, world, crafter);
	}

	@Override
	public void onUpdate(final ItemStack item, final World world, final Entity player, final int inventoryIndex, final boolean isHeld) {
		if(regenerates && !world.isRemote && isHeld && item.getItemDamage() > 0 && world.getTotalWorldTime() % regenInterval == 0) {
			item.setItemDamage(item.getItemDamage() - 1);
		}
	}

	@Override
	public boolean canHarvestBlock(final Block target) {
		if(this.toolTypes.contains(target.getHarvestTool(target.getDefaultState()))) {
			return metal.getToolHarvestLevel() >= target.getHarvestLevel(target.getDefaultState());
		}
		return super.canHarvestBlock(target);
	}

	public String getMaterialName() {
		return metal.getName();
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
		super.addInformation(stack,player,list,b);
		MetalToolEffects.addToolSpecialPropertiesToolTip(metal,list);
	}

	@Override
	public MetalMaterial getMetalMaterial() {
		return metal;
	}
}
