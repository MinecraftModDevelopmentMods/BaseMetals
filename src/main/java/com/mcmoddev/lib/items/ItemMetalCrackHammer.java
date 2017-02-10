package com.mcmoddev.lib.items;

import java.util.*;

import com.mcmoddev.basemetals.init.Achievements;
import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.basemetals.items.MetalToolEffects;
import com.mcmoddev.basemetals.registry.CrusherRecipeRegistry;
import com.mcmoddev.basemetals.registry.recipe.ICrusherRecipe;
import com.mcmoddev.basemetals.util.Config;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.material.IMetalObject;
import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Crack Hammers
 */
@SuppressWarnings("deprecation")
public class ItemMetalCrackHammer extends ItemTool implements IMetalObject {
	private static final float ATTACK_SPEED = -3.0F;

	private final MetalMaterial material;
	private final Set<String> toolTypes;
	private final String repairOreDictName;
	private final boolean regenerates;
	private static final long REGEN_INTERVAL = 200;

	/**
	 *
	 * @param material The material to make the crackhammer from
	 */
	public ItemMetalCrackHammer(MetalMaterial material) {
		super(1 + Materials.getToolMaterialFor(material).getDamageVsEntity(), ATTACK_SPEED, Materials.getToolMaterialFor(material), new HashSet<Block>());
		this.material = material;
		damageVsEntity = 5F + 2F * this.material.getBaseAttackDamage();
		attackSpeed = -3.5F;
		setMaxDamage((int) (0.75 * this.material.getToolDurability()));
		efficiencyOnProperMaterial = this.material.getToolEfficiency();
		toolTypes = new HashSet<>();
		toolTypes.add("crackhammer");
		toolTypes.add("pickaxe");
		repairOreDictName = Oredicts.INGOT + this.material.getCapitalizedName();
		this.regenerates = this.material.regenerates;
	}

	@Override
	public float getStrVsBlock(final ItemStack tool, final IBlockState target) {
		if (isCrushableBlock(target) && canHarvestBlock(target)) {
			return Math.max(1.0f, 0.5f * this.material.getToolEfficiency());
		}
		return 1.0f;
	}

	@Override
	public boolean onBlockDestroyed(final ItemStack tool, final World world, final IBlockState target, final BlockPos coord, final EntityLivingBase player) {
		if (!world.isRemote && this.canHarvestBlock(target)) {
			IBlockState bs = world.getBlockState(coord);
			ICrusherRecipe recipe = getCrusherRecipe(bs);
			if (recipe != null) {
				ItemStack output = recipe.getOutput().copy();
				world.setBlockToAir(coord);
				if (output != null) {
					int num = output.stackSize;
					output.stackSize = 1;
					for (int i = 0; i < num; i++) {
						world.spawnEntity(new EntityItem(world, coord.getX() + 0.5, coord.getY() + 0.5, coord.getZ() + 0.5, output.copy()));
					}
				}
			}
		}
		return super.onBlockDestroyed(tool, world, target, coord, player);
	}

	@Override
	public EnumActionResult onItemUse(final ItemStack item, final EntityPlayer player, final World w, final BlockPos coord, EnumHand hand, final EnumFacing facing, final float partialX, final float partialY, final float partialZ) {
		if (facing != EnumFacing.UP) {
			return EnumActionResult.PASS;
		}
		List<EntityItem> entities = w.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(coord.getX(), coord.getY() + 1, coord.getZ(), coord.getX() + 1, coord.getY() + 2, coord.getZ() + 1));
		boolean success = false;
		for (EntityItem target : entities) {
			ItemStack targetItem = target.getEntityItem();
			if (targetItem != null) {
				ICrusherRecipe recipe = CrusherRecipeRegistry.getInstance().getRecipeForInputItem(targetItem);
				if (recipe != null) {
					// hardness check
					if ((Config.Options.enforceHardness) && (targetItem.getItem() instanceof ItemBlock)) {
						Block b = ((ItemBlock) targetItem.getItem()).getBlock();
						if (!this.canHarvestBlock(b.getStateFromMeta(targetItem.getMetadata()))) {
							// cannot harvest the block, no crush for you!
							return EnumActionResult.PASS;
						}
					}
					// crush the item (server side only)
					if (!w.isRemote) {
						ItemStack output = recipe.getOutput().copy();
						int count = output.stackSize;
						output.stackSize = 1;
						double x = target.posX;
						double y = target.posY;
						double z = target.posZ;

						targetItem.stackSize--;
						if (targetItem.stackSize <= 0) {
							w.removeEntity(target);
						}
						for (int i = 0; i < count; i++) {
							w.spawnEntity(new EntityItem(w, x, y, z, output.copy()));
						}
						item.damageItem(1, player);
					}
					success = true;
					break;
				}
			}
		}
		if (success) {
			w.playSound(player, coord, SoundEvents.BLOCK_GRAVEL_BREAK, SoundCategory.BLOCKS, 0.5F, 0.5F + (itemRand.nextFloat() * 0.3F));
		}
		return success ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
	}

	protected boolean isCrushableBlock(IBlockState block) {
		return getCrusherRecipe(block) != null;
	}

	protected boolean isCrushableBlock(Block block) {
		return getCrusherRecipe(block) != null;
	}

	protected ICrusherRecipe getCrusherRecipe(Block block) {
		return getCrusherRecipe(block.getDefaultState());
	}

	protected ICrusherRecipe getCrusherRecipe(IBlockState block) {
		if (block == null || Item.getItemFromBlock(block.getBlock()) == null) {
			return null;
		}
		//return CrusherRecipeRegistry.getInstance().getRecipeForInputItem(block);
		return CrusherRecipeRegistry.getInstance().getRecipeForInputItem(new ItemStack(block.getBlock(), 1, block.getBlock().getMetaFromState(block)));
	}

	@Override
	public ToolMaterial getToolMaterial() {
		return toolMaterial;
	}

	@Override
	public int getItemEnchantability() {
		return toolMaterial.getEnchantability();
	}

	@Override
	public String getToolMaterialName() {
		return toolMaterial.toString();
	}

	@Override
	public boolean getIsRepairable(final ItemStack intputItem, final ItemStack repairMaterial) {
		List<ItemStack> acceptableItems = OreDictionary.getOres(repairOreDictName);
		for (ItemStack i : acceptableItems) {
			if (ItemStack.areItemsEqual(i, repairMaterial)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public int getHarvestLevel(final ItemStack item, final String typeRequested) {
		if (typeRequested != null && toolTypes.contains(typeRequested)) {
			if (Config.Options.strongHammers) {
				return material.getToolHarvestLevel();
			}
			else {
				return material.getToolHarvestLevel() - 1;
			}
		}
		return -1;
	}

	@Override
	public Set<String> getToolClasses(final ItemStack item) {
		return toolTypes;
	}

	@Override
	public boolean hitEntity(final ItemStack item, final EntityLivingBase target, final EntityLivingBase attacker) {
		super.hitEntity(item, target, attacker);
		MetalToolEffects.extraEffectsOnAttack(material, item, target, attacker);
		return true;
	}

	@Override
	public void onCreated(final ItemStack item, final World world, final EntityPlayer crafter) {
		super.onCreated(item, world, crafter);
		MetalToolEffects.extraEffectsOnCrafting(material, item, world, crafter);
		// achievement
    	if (Options.enableAchievements) {
    		crafter.addStat(Achievements.geologist, 1);
    	}
	}

	@Override
	public void onUpdate(final ItemStack item, final World world, final Entity player, final int inventoryIndex, final boolean isHeld) {
		if (regenerates && !world.isRemote && isHeld && item.getItemDamage() > 0 && world.getTotalWorldTime() % REGEN_INTERVAL == 0) {
			item.setItemDamage(item.getItemDamage() - 1);
		}
	}

	@Override
	public boolean canHarvestBlock(final IBlockState targetBS) {
		Block target = targetBS.getBlock();
		// go to net.minecraftforge.common.ForgeHooks.initTools(); to see all tool type strings
		String toolType = target.getHarvestTool(target.getDefaultState());
		if (toolTypes.contains(toolType) || target.getMaterial(targetBS) == Material.ROCK) {
			// can mine like a Pickaxe
			return this.getHarvestLevel(null, "pickaxe") >= target.getHarvestLevel(target.getDefaultState());
		}
		else if ("shovel".equals(toolType) && target.getHarvestLevel(target.getDefaultState()) <= 0) {
			// can be dug with wooden shovel
			return true;
		}
		// return true if block doesn't need tools
		return target.getHarvestLevel(target.getDefaultState()) == -1;
	}
/*
	public String getMaterialName() {
		return this.material.getName();
	}
*/
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean b) {
		super.addInformation(stack, player, list, b);
		MetalToolEffects.addToolSpecialPropertiesToolTip(this.material, list);
	}

	@Override
	public MetalMaterial getMaterial() {
		return this.material;
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public MetalMaterial getMetalMaterial() {
		return this.material;
	}
}
