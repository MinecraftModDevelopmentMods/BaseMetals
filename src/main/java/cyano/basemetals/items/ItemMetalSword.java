package cyano.basemetals.items;

import java.util.List;

import cyano.basemetals.init.Materials;
import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.util.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Swords
 * 
 * @author DrCyano
 *
 */
public class ItemMetalSword extends ItemSword implements IMetalObject {
	protected final MetalMaterial metal;
	protected final String repairOreDictName;
	protected final boolean regenerates;
	protected final long regenInterval = 200;
	protected final float attackDamage;

	/**
	 *
	 * @param metal
	 */
	public ItemMetalSword(MetalMaterial metal) {
		super(Materials.getToolMaterialFor(metal));
		this.metal = metal;
		this.setMaxDamage(metal.getToolDurability());
		// this.damageVsEntity = attackDamage + metal.getBaseAttackDamage(); // damageVsEntity is private, sadly
		this.attackDamage = 3F + metal.getBaseAttackDamage();
		this.repairOreDictName = "ingot" + metal.getCapitalizedName();
		if (Config.Options.ENABLE_STARSTEEL) {
			this.regenerates = metal.equals(Materials.getMaterialByName("starsteel"));
		} else {
			this.regenerates = false;
		}
		this.setCreativeTab(CreativeTabs.COMBAT);
	}

	@Override
	public boolean hitEntity(final ItemStack item, final EntityLivingBase target, final EntityLivingBase attacker) {
		item.damageItem(1, attacker);
		MetalToolEffects.extraEffectsOnAttack(this.metal, item, target, attacker);
		return true;
	}

	@Override
	public boolean onBlockDestroyed(final ItemStack item, final World world, final IBlockState block, final BlockPos coord,
									final EntityLivingBase entity) {
		if (block.getBlockHardness(world, coord) != 0.0)
			item.damageItem(2, entity);
		return true;
	}

	@Override
	public int getItemEnchantability() {
		return this.metal.getEnchantability();
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
	public void onCreated(final ItemStack item, final World world, final EntityPlayer crafter) {
		super.onCreated(item, world, crafter);
		MetalToolEffects.extraEffectsOnCrafting(this.metal, item, world, crafter);
	}

	@Override
	public void onUpdate(final ItemStack item, final World world, final Entity player, final int inventoryIndex, final boolean isHeld) {
		if (this.regenerates && !world.isRemote && isHeld && (item.getItemDamage() > 0) && ((world.getTotalWorldTime() % this.regenInterval) == 0))
			item.setItemDamage(item.getItemDamage() - 1);
	}

	/**
	 *
	 * @return The amount of damage dealt to an entity when attacked by this
	 * item
	 */
	public float getAttackDamage() {
		return this.attackDamage;
	}

	public String getMaterialName() {
		return this.metal.getName();
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean b) {
		super.addInformation(stack, player, list, b);
		MetalToolEffects.addToolSpecialPropertiesToolTip(this.metal, list);
	}

	@Override
	public MetalMaterial getMaterial() {
		return this.metal;
	}

	@Override
	@Deprecated
	public MetalMaterial getMetalMaterial() {
		return this.metal;
	}
}
