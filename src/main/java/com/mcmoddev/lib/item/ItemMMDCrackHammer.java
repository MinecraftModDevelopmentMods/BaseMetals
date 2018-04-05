package com.mcmoddev.lib.item;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.mcmoddev.basemetals.items.MMDToolEffects;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.registry.CrusherRecipeRegistry;
import com.mcmoddev.lib.registry.recipe.ICrusherRecipe;
import com.mcmoddev.lib.util.ConfigBase.Options;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Crack Hammers.
 */
@SuppressWarnings("deprecation")
public class ItemMMDCrackHammer extends net.minecraft.item.ItemTool implements IMMDObject {

	private static final float ATTACK_SPEED = -3.0F;

	private final MMDMaterial material;
	private final Set<String> toolTypes;

	/**
	 *
	 * @param material
	 *            The material to make the crackhammer from
	 */
	public ItemMMDCrackHammer(final MMDMaterial material) {
		super(1 + Materials.getToolMaterialFor(material).getAttackDamage(), ATTACK_SPEED,
				Materials.getToolMaterialFor(material), new HashSet<Block>());
		this.material = material;
		this.attackDamage = 5F + (2F * this.material.getBaseAttackDamage());
		this.attackSpeed = -3.5F;
		this.setMaxDamage((int) (0.75 * this.material.getToolDurability()));
		this.efficiency = this.material.getToolEfficiency();
		this.toolTypes = new HashSet<>();
		this.toolTypes.add(Names.CRACKHAMMER.toString());
		this.toolTypes.add(Names.PICKAXE.toString());
	}

	@Override
	public float getDestroySpeed(final ItemStack tool, final IBlockState target) {
		if (this.isCrushableBlock(target) && this.canHarvestBlock(target)) {
			return Math.max(1.0f, 0.5f * this.material.getToolEfficiency());
		}
		return 1.0f;
	}

	@Override
	public boolean onBlockDestroyed(final ItemStack tool, final World world,
			final IBlockState target, final BlockPos coord, final EntityLivingBase player) {
		if (!world.isRemote && this.canHarvestBlock(target)) {
			final IBlockState bs = world.getBlockState(coord);
			final ICrusherRecipe recipe = this.getCrusherRecipe(bs);
			if (recipe != null) {
				final ItemStack output = recipe.getOutput().copy();
				world.setBlockToAir(coord);
				if (output != null) {
					final int num = output.getCount();
					output.setCount(1);
					for (int i = 0; i < num; i++) {
						world.spawnEntity(new EntityItem(world, coord.getX() + 0.5,
								coord.getY() + 0.5, coord.getZ() + 0.5, output.copy()));
					}
				}
			}
		}
		return super.onBlockDestroyed(tool, world, target, coord, player);
	}

	@Override
	public EnumActionResult onItemUse(final EntityPlayer player, final World w,
			final BlockPos coord, final EnumHand hand, final EnumFacing facing,
			final float partialX, final float partialY, final float partialZ) {
		final ItemStack item = player.getHeldItemMainhand();
		if (facing != EnumFacing.UP) {
			return EnumActionResult.PASS;
		}
		/* List<EntityItem> entities = */
		final AxisAlignedBB boundingBox = new AxisAlignedBB(coord.getX(), coord.getY() + 1,
				coord.getZ(), coord.getX() + 1, coord.getY() + 2, coord.getZ() + 1);
		final List<EntityItem> entities = w.getEntitiesWithinAABB(EntityItem.class, boundingBox)
				.stream().filter(elem -> (elem.getItem() != null))
				.filter(elem -> (CrusherRecipeRegistry
						.getRecipeForInputItem(elem.getItem()) != null))
				.collect(Collectors.toList());

		if (!entities.isEmpty()) {
			final ItemStack targetItem = entities.get(0).getItem();
			final ICrusherRecipe recipe = CrusherRecipeRegistry.getRecipeForInputItem(targetItem);
			if (this.hardnessCheck(targetItem)) {
				return EnumActionResult.PASS;
			}

			this.maybeDoCrack(recipe, targetItem, item, entities.get(0), player, w);
			w.playSound(player, coord, SoundEvents.BLOCK_GRAVEL_BREAK, SoundCategory.BLOCKS, 0.5F,
					0.5F + (itemRand.nextFloat() * 0.3F));
			return EnumActionResult.SUCCESS;
		}

		return EnumActionResult.PASS;
	}

	private void maybeDoCrack(final ICrusherRecipe recipe, final ItemStack targetItem,
			final ItemStack item, final EntityItem target, final EntityPlayer player,
			final World w) {
		if (!w.isRemote) {
			final ItemStack output = recipe.getOutput().copy();
			final int crackedCount = this.doDamageCheck(targetItem, item);

			final double x = target.posX;
			final double y = target.posY;
			final double z = target.posZ;

			this.doCrack(targetItem, crackedCount);
			this.maybeRemoveEntity(targetItem, target, w);
			this.spawnResults(output, x, y, z, crackedCount, w);

			item.damageItem(crackedCount, player);
		}
	}

	private boolean hardnessCheck(final ItemStack targetItem) {
		if ((Options.enforceHardness()) && (targetItem.getItem() instanceof ItemBlock)) {
			final Block b = ((ItemBlock) targetItem.getItem()).getBlock();
			if (!this.canHarvestBlock(b.getStateFromMeta(targetItem.getMetadata()))) {
				return true;
			}
		}
		return false;
	}

	private void spawnResults(final ItemStack output, final double x, final double y,
			final double z, final int crackedCount, final World w) {
		for (int i = 0; i < crackedCount; i++) {
			w.spawnEntity(new EntityItem(w, x, y, z, output.copy()));
		}
	}

	private void maybeRemoveEntity(final ItemStack targetItem, final EntityItem target,
			final World w) {
		if (targetItem.getCount() <= 0) {
			w.removeEntity(target);
		}
	}

	private void doCrack(final ItemStack targetItem, final int crackedCount) {
		if (Options.crackHammerFullStack()) {
			targetItem.shrink(crackedCount);
		} else {
			targetItem.shrink(1);
		}
	}

	private int doDamageCheck(final ItemStack targetItem, final ItemStack item) {
		if (Options.crackHammerFullStack()) {
			final int remainingDamage = item.getMaxDamage() - item.getItemDamage();
			return Math.min(targetItem.getCount(),
					Math.min(targetItem.getMaxStackSize(), remainingDamage));
		}

		return 1;
	}

	protected boolean isCrushableBlock(final IBlockState block) {
		return this.getCrusherRecipe(block) != null;
	}

	protected boolean isCrushableBlock(final Block block) {
		return this.getCrusherRecipe(block) != null;
	}

	protected ICrusherRecipe getCrusherRecipe(final Block block) {
		return this.getCrusherRecipe(block.getDefaultState());
	}

	protected ICrusherRecipe getCrusherRecipe(final IBlockState blockState) {
		if (blockState == null) {
			return null;
		}

		final Block block = blockState.getBlock();
		if (Item.getItemFromBlock(block) == null) {
			return null;
		}
		return CrusherRecipeRegistry
				.getRecipeForInputItem(new ItemStack(block, 1, block.getMetaFromState(blockState)));
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
		return MMDItemHelper.isToolRepairable(repairMaterial, this.material.getCapitalizedName());
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public int getHarvestLevel(final ItemStack stack, final String typeRequested,
			final EntityPlayer player, final IBlockState blockState) {
		if ((typeRequested != null) && this.toolTypes.contains(typeRequested)) {
			if (Options.strongHammers()) {
				return this.material.getToolHarvestLevel();
			} else {
				return this.material.getToolHarvestLevel() - 1;
			}
		}
		return -1;
	}

	@Override
	public Set<String> getToolClasses(final ItemStack item) {
		return this.toolTypes;
	}

	@Override
	public boolean hitEntity(final ItemStack item, final EntityLivingBase target,
			final EntityLivingBase attacker) {
		super.hitEntity(item, target, attacker);
		MMDToolEffects.extraEffectsOnAttack(this.material, item, target, attacker);
		return true;
	}

	@Override
	public void onCreated(final ItemStack item, final World world, final EntityPlayer crafter) {
		super.onCreated(item, world, crafter);
		MMDToolEffects.extraEffectsOnCrafting(this.material, item, world, crafter);
	}

	@Override
	public void onUpdate(final ItemStack item, final World world, final Entity player,
			final int inventoryIndex, final boolean isHeld) {
		MMDItemHelper.doRegeneration(item, world, isHeld, this.material.regenerates());
	}

	@Override
	public boolean canHarvestBlock(final IBlockState targetBS) {
		final Block target = targetBS.getBlock();
		final EntityPlayer player = null;
		// go to net.minecraftforge.common.ForgeHooks.initTools(); to see all
		// tool type strings
		final String toolType = target.getHarvestTool(target.getDefaultState());
		if (this.toolTypes.contains(toolType) || (target.getMaterial(targetBS) == Material.ROCK)) {
			// can mine like a Pickaxe
			return this.getHarvestLevel(ItemStack.EMPTY, Names.PICKAXE.toString(), player,
					targetBS) >= target.getHarvestLevel(target.getDefaultState());
		} else if (Names.SHOVEL.toString().equals(toolType)
				&& (target.getHarvestLevel(target.getDefaultState()) <= 0)) {
			// can be dug with wooden shovel
			return true;
		}
		// return true if block doesn't need tools
		return target.getHarvestLevel(target.getDefaultState()) == -1;
	}

	@Override
	public void addInformation(final ItemStack stack, final World worldIn,
			final List<String> tooltip, final ITooltipFlag flagIn) {
		MMDToolEffects.addToolSpecialPropertiesToolTip(this.material.getName(), tooltip);
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
