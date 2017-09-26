package com.mcmoddev.lib.item;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mcmoddev.basemetals.items.MMDToolEffects;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.registry.CrusherRecipeRegistry;
import com.mcmoddev.lib.registry.recipe.ICrusherRecipe;
import com.mcmoddev.lib.util.Oredicts;

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
import net.minecraftforge.oredict.OreDictionary;

/**
 * Crack Hammers
 */
@SuppressWarnings("deprecation")
public class ItemMMDCrackHammer extends net.minecraft.item.ItemTool implements IMMDObject {
	private static final float ATTACK_SPEED = -3.0F;

	private final MMDMaterial material;
	private final Set<String> toolTypes;
	private final String repairOreDictName;
	private static final long REGEN_INTERVAL = 200;

	/**
	 *
	 * @param material
	 *            The material to make the crackhammer from
	 */
	public ItemMMDCrackHammer(MMDMaterial material) {
		super(1 + Materials.getToolMaterialFor(material).getAttackDamage(), ATTACK_SPEED, Materials.getToolMaterialFor(material), new HashSet<Block>());
		this.material = material;
		this.attackDamage = 5F + 2F * this.material.getBaseAttackDamage();
		attackSpeed = -3.5F;
		setMaxDamage((int) (0.75 * this.material.getToolDurability()));
		this.efficiency = this.material.getToolEfficiency();
		toolTypes = new HashSet<>();
		toolTypes.add("crackhammer");
		toolTypes.add("pickaxe");
		repairOreDictName = Oredicts.INGOT + this.material.getCapitalizedName();
	}

	@Override
	public float getDestroySpeed(final ItemStack tool, final IBlockState target) {
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
					int num = output.getCount();
					output.setCount(1);
					for (int i = 0; i < num; i++) {
						world.spawnEntity(new EntityItem(world, coord.getX() + 0.5, coord.getY() + 0.5, coord.getZ() + 0.5, output.copy()));
					}
				}
			}
		}
		return super.onBlockDestroyed(tool, world, target, coord, player);
	}

	@Override
	public EnumActionResult onItemUse(final EntityPlayer player, final World w, final BlockPos coord, EnumHand hand, final EnumFacing facing, final float partialX, final float partialY, final float partialZ) {
		if (facing != EnumFacing.UP) {
			return EnumActionResult.PASS;
		}
		List<EntityItem> entities = w.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(coord.getX(), coord.getY() + 1, coord.getZ(), coord.getX() + 1, coord.getY() + 2, coord.getZ() + 1));
		boolean success = false;
		for (EntityItem target : entities) {
			ItemStack targetItem = target.getItem();
			if (targetItem != null) {
				ICrusherRecipe recipe = CrusherRecipeRegistry.getRecipeForInputItem(targetItem);
				if (recipe != null) {
					// hardness check
					if ((Options.enforceHardness()) && (targetItem.getItem() instanceof ItemBlock)) {
						Block b = ((ItemBlock) targetItem.getItem()).getBlock();
						if (!this.canHarvestBlock(b.getStateFromMeta(targetItem.getMetadata()))) {
							// cannot harvest the block, no crush for you!
							return EnumActionResult.PASS;
						}
					}
					// crush the item (server side only)
					if (!w.isRemote) {
						ItemStack output = recipe.getOutput().copy();
						int count = output.getCount();
						int toolDamage;
						if (Options.crackHammerFullStack()) {
							output.setCount(targetItem.getCount());
							if (player.getHeldItemMainhand().isItemDamaged() && (player.getHeldItemMainhand().getItemDamage() < output.getCount())) {
									output.setCount(player.getHeldItemMainhand().getItemDamage());
							}
							toolDamage = output.getCount();
						} else {
							output.setCount(1);
							toolDamage = 1;
						}
						double x = target.posX;
						double y = target.posY;
						double z = target.posZ;

						if (Options.crackHammerFullStack()) {
							targetItem.setCount(0);
						} else {
							targetItem.shrink(1);
						}
						if (targetItem.getCount() <= 0) {
							w.removeEntity(target);
						}
						for (int i = 0; i < count; i++) {
							w.spawnEntity(new EntityItem(w, x, y, z, output.copy()));
						}
						player.getHeldItemMainhand().damageItem(toolDamage, player);
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
		return CrusherRecipeRegistry.getRecipeForInputItem(new ItemStack(block.getBlock(), 1, block.getBlock().getMetaFromState(block)));
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
	public int getHarvestLevel(ItemStack stack, String typeRequested, EntityPlayer player, IBlockState blockState) {
		if (typeRequested != null && toolTypes.contains(typeRequested)) {
			if (Options.strongHammers()) {
				return material.getToolHarvestLevel();
			} else {
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
		MMDToolEffects.extraEffectsOnAttack(material, item, target, attacker);
		return true;
	}

	@Override
	public void onCreated(final ItemStack item, final World world, final EntityPlayer crafter) {
		super.onCreated(item, world, crafter);
		MMDToolEffects.extraEffectsOnCrafting(material, item, world, crafter);
	}

	@Override
	public void onUpdate(final ItemStack item, final World world, final Entity player, final int inventoryIndex, final boolean isHeld) {
		if (world.isRemote)
			return;

		if (this.material.regenerates() && isHeld && item.getItemDamage() > 0 && world.getTotalWorldTime() % REGEN_INTERVAL == 0) {
			item.setItemDamage(item.getItemDamage() - 1);
		}
	}

	@Override
	public boolean canHarvestBlock(final IBlockState targetBS) {
		Block target = targetBS.getBlock();
		EntityPlayer player = null;
		// go to net.minecraftforge.common.ForgeHooks.initTools(); to see all
		// tool type strings
		String toolType = target.getHarvestTool(target.getDefaultState());
		if (toolTypes.contains(toolType) || target.getMaterial(targetBS) == Material.ROCK) {
			// can mine like a Pickaxe
			return this.getHarvestLevel(null, "pickaxe", player, targetBS) >= target.getHarvestLevel(target.getDefaultState());
		} else if ("shovel".equals(toolType) && target.getHarvestLevel(target.getDefaultState()) <= 0) {
			// can be dug with wooden shovel
			return true;
		}
		// return true if block doesn't need tools
		return target.getHarvestLevel(target.getDefaultState()) == -1;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		MMDToolEffects.addToolSpecialPropertiesToolTip(this.material, tooltip);
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
