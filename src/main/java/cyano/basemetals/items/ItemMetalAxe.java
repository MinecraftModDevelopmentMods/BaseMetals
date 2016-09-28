package cyano.basemetals.items;

import java.util.List;

import cyano.basemetals.init.Materials;
import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Axes
 * 
 * @author DrCyano
 *
 */
public class ItemMetalAxe extends ItemAxe implements IMetalObject {

	protected final MetalMaterial metal;
	protected final String repairOreDictName;
	protected final boolean regenerates;
	protected final long regenInterval = 200;

	/**
	 *
	 * @param metal
	 */
	public ItemMetalAxe(MetalMaterial metal) {
		super(Materials.getToolMaterialFor(metal));
		this.metal = metal;
		this.setMaxDamage(metal.getToolDurability());
		this.damageVsEntity = 4F + (2F * metal.getBaseAttackDamage());
		this.attackSpeed = -3.5F + Math.min(0.5F, 0.05F * metal.strength);
		this.efficiencyOnProperMaterial = metal.getToolEfficiency();
		this.repairOreDictName = "ingot" + metal.getCapitalizedName();
		this.regenerates = metal.equals(Materials.starsteel);
	}

	@Override
	public ToolMaterial getToolMaterial() {
		return this.toolMaterial;
	}

	@Override
	public int getItemEnchantability() {
		return this.toolMaterial.getEnchantability();
	}

	@Override
	public String getToolMaterialName() {
		return this.toolMaterial.toString();
	}

	@Override
	public boolean getIsRepairable(final ItemStack intputItem, final ItemStack repairMaterial) {
		final List<ItemStack> acceptableItems = OreDictionary.getOres(this.repairOreDictName);
		for (final ItemStack i : acceptableItems)
			if (ItemStack.areItemsEqual(i, repairMaterial))
				return true;
		return false;
	}

	@Override
	public boolean hitEntity(final ItemStack item, final EntityLivingBase target, final EntityLivingBase attacker) {
		super.hitEntity(item, target, attacker);
		MetalToolEffects.extraEffectsOnAttack(this.metal, item, target, attacker);
		return true;
	}

	@Override
	public void onCreated(final ItemStack item, final World world, final EntityPlayer crafter) {
		super.onCreated(item, world, crafter);
		MetalToolEffects.extraEffectsOnCrafting(this.metal, item, world, crafter);
	}

	@Override
	public void onUpdate(final ItemStack item, final World world, final Entity player, final int inventoryIndex, final boolean isHeld) {
		if (this.regenerates && !world.isRemote && isHeld && (item.getItemDamage() > 0) && ((world.getTotalWorldTime() % this.regenInterval) == 0))
			item.setItemDamage(item.getItemDamage() - 1);
	}

	public String getMaterialName() {
		return this.metal.getName();
	}

	@Override
	public MetalMaterial getMetalMaterial() {
		return this.metal;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean b) {
		super.addInformation(stack, player, list, b);
		MetalToolEffects.addToolSpecialPropertiesToolTip(this.metal, list);
	}
}
