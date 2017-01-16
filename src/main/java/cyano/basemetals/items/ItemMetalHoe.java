package cyano.basemetals.items;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cyano.basemetals.init.Materials;
import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Hoes
 * 
 * @author DrCyano
 *
 */
public class ItemMetalHoe extends ItemHoe implements IMetalObject {

	protected final MetalMaterial material;
	protected final Set<String> toolTypes;
	protected final String repairOreDictName;
	protected final boolean regenerates;
	protected final long regenInterval = 200;

	/**
	 *
	 * @param material The material to make the hoe from
	 */
	public ItemMetalHoe(MetalMaterial material) {
		super(Materials.getToolMaterialFor(material));
		this.material = material;
		this.setMaxDamage(this.material.getToolDurability());
		this.toolTypes = new HashSet<>();
		this.toolTypes.add("hoe");
		this.repairOreDictName = "ingot" + this.material.getCapitalizedName();
		this.regenerates = this.material.regenerates;
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
	@Deprecated
	public int getHarvestLevel(final ItemStack item, final String typeRequested) {
		if ((typeRequested != null) && this.toolTypes.contains(typeRequested))
			return this.material.getToolHarvestLevel();
		return -1;
	}

	@Override
	public Set<String> getToolClasses(final ItemStack item) {
		return this.toolTypes;
	}

	@Override
	public float getStrVsBlock(final ItemStack tool, final IBlockState target) {
		if (this.canHarvestBlock(target, tool))
			return Math.max(1.0f, this.material.getToolEfficiency());
		else
			return 1.0f;
	}

	@Override
	public boolean hitEntity(final ItemStack item, final EntityLivingBase target, final EntityLivingBase attacker) {
		super.hitEntity(item, target, attacker);
		MetalToolEffects.extraEffectsOnAttack(this.material, item, target, attacker);
		return true;
	}

	@Override
	public boolean canHarvestBlock(final IBlockState target) {
		if (this.toolTypes.contains(target.getBlock().getHarvestTool(target)))
			return this.material.getToolHarvestLevel() >= target.getBlock().getHarvestLevel(target);
		return false;
	}

	@Override
	public void onCreated(final ItemStack item, final World world, final EntityPlayer crafter) {
		super.onCreated(item, world, crafter);
		MetalToolEffects.extraEffectsOnCrafting(this.material, item, world, crafter);
	}

	@Override
	public void onUpdate(final ItemStack item, final World world, final Entity player, final int inventoryIndex, final boolean isHeld) {
		if (this.regenerates && !world.isRemote && isHeld && (item.getItemDamage() > 0) && ((world.getTotalWorldTime() % this.regenInterval) == 0))
			item.setItemDamage(item.getItemDamage() - 1);
	}

	@Override
	public MetalMaterial getMaterial() {
		return this.material;
	}

	@Override
	@Deprecated
	public MetalMaterial getMetalMaterial() {
		return this.material;
	}

	@Override
	public String getMaterialName() {
		return this.material.getName();
	}
}
