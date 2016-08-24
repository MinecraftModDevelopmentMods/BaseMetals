package cyano.basemetals.items;

import cyano.basemetals.BaseMetals;
import cyano.basemetals.init.Achievements;
import cyano.basemetals.init.Materials;
import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.CrusherRecipeRegistry;
import cyano.basemetals.registry.recipe.ICrusherRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Crack Hammers
 */
public class ItemMetalCrackHammer extends ItemTool implements IMetalObject {
	private static final float attack_speed = -3.0F;

	private final MetalMaterial metal;
	private final Set<String> toolTypes;
	private final String repairOreDictName;
	private final boolean regenerates;
	private final long regenInterval = 200;

	public ItemMetalCrackHammer(MetalMaterial metal) {
		super(1 + Materials.getToolMaterialFor(metal).getDamageVsEntity(), attack_speed, Materials.getToolMaterialFor(metal), new HashSet<Block>());
		this.metal = metal;
		this.damageVsEntity = 5F + 2F * metal.getBaseAttackDamage();
		this.attackSpeed = -3.5F;
		this.setMaxDamage((int)(0.75 * metal.getToolDurability()));
		this.efficiencyOnProperMaterial = metal.getToolEfficiency();
		this.toolTypes = new HashSet<>();
		toolTypes.add("crackhammer");
		toolTypes.add("pickaxe");
		repairOreDictName = "ingot" + metal.getCapitalizedName();
		regenerates = metal.equals(Materials.starsteel);
	}

	@Override
	public float getStrVsBlock(final ItemStack tool, final IBlockState target) {
		if(isCrushableBlock(target) && canHarvestBlock(target) ) {
			return Math.max(1.0f, 0.5f * this.metal.getToolEfficiency());
		}
		return 1.0f;
	}
	
	@Override
	public boolean onBlockDestroyed(final ItemStack tool, final World world, 
			final IBlockState target, final BlockPos coord, final EntityLivingBase player) {
		if (!world.isRemote && this.canHarvestBlock(target)) {
			IBlockState bs = world.getBlockState(coord);
			ICrusherRecipe recipe = getCrusherRecipe(bs);
			if (recipe != null) {
				if (recipe.getOutput() != null) {
					ItemStack output = recipe.getOutput().copy();
					world.setBlockToAir(coord);
					int num = output.stackSize;
					output.stackSize = 1;
					for (int i = 0; i < num; i++) {
						world.spawnEntityInWorld(new EntityItem(world, coord.getX() + 0.5, coord.getY() + 0.5, coord.getZ() + 0.5, output.copy()));
					}
				}
			}
		}
		return super.onBlockDestroyed(tool, world, target, coord, player);
	}

	@Override
	public EnumActionResult onItemUse(final ItemStack item, final EntityPlayer player, final World w,
									  final BlockPos coord, EnumHand hand, final EnumFacing facing,
									  final float partialX, final float partialY, final float partialZ) {
		if (facing != EnumFacing.UP) return EnumActionResult.PASS;
		List<EntityItem> entities = w.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(
				coord.getX(), coord.getY() + 1, coord.getZ(),
				coord.getX() + 1, coord.getY() + 2, coord.getZ() + 1));
		boolean success = false;
		for (EntityItem target : entities) {
			ItemStack targetItem = target.getEntityItem();
			ICrusherRecipe recipe = CrusherRecipeRegistry.getInstance().getRecipeForInputItem(targetItem);
			if (recipe != null) {
				// hardness check
				if (BaseMetals.enforceHardness) {
					// cannot harvest the block, no crush for you!
					if (targetItem.getItem() instanceof ItemBlock && !this.canHarvestBlock(((ItemBlock) targetItem.getItem()).getBlock().getStateFromMeta(targetItem.getMetadata())))
						return EnumActionResult.PASS;
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
					if (targetItem.stackSize <= 0) w.removeEntity(target);
					for (int i = 0; i < count; i++) w.spawnEntityInWorld(new EntityItem(w, x, y, z, output.copy()));
					item.damageItem(1, player);
				}
				success = true;
				break;
			}
		}
		if (success) w.playSound(player, coord, SoundEvents.BLOCK_GRAVEL_BREAK, SoundCategory.BLOCKS, 0.5F, 0.5F + (itemRand.nextFloat() * 0.3F));
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
		if(block == null || Item.getItemFromBlock(block.getBlock()) == null)return null;
		//return CrusherRecipeRegistry.getInstance().getRecipeForInputItem(block);
		return CrusherRecipeRegistry.getInstance().getRecipeForInputItem(new ItemStack(block.getBlock(), 1, block.getBlock().getMetaFromState(block)));
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
	public int getHarvestLevel(final ItemStack item, final String typeRequested) {
		if (typeRequested != null && toolTypes.contains(typeRequested)) {
			if(BaseMetals.strongHammers) {
				return metal.getToolHarvestLevel();
			}else{
				return metal.getToolHarvestLevel() - 1;
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
		MetalToolEffects.extraEffectsOnAttack(metal, item, target, attacker);
		return true;
	}
	
	@Override
	public void onCreated(final ItemStack item, final World world, final EntityPlayer crafter) {
		super.onCreated(item, world, crafter);
		MetalToolEffects.extraEffectsOnCrafting(metal,item, world, crafter);
		// achievement
		crafter.addStat(Achievements.geologist, 1);
	}

	@Override
	public void onUpdate(final ItemStack item, final World world, final Entity player, final int inventoryIndex, final boolean isHeld) {
		if(regenerates && !world.isRemote && isHeld && item.getItemDamage() > 0 && world.getTotalWorldTime() % regenInterval == 0) {
			item.setItemDamage(item.getItemDamage() - 1);
		}
	}

	@Override
	public boolean canHarvestBlock(final IBlockState targetBS) {
        Block target = targetBS.getBlock();
		// go to net.minecraftforge.common.ForgeHooks.initTools(); to see all tool type strings
		String toolType = target.getHarvestTool(target.getDefaultState());
		if(this.toolTypes.contains(toolType) || target.getMaterial(targetBS) == Material.ROCK) {
			// can mine like a Pickaxe
			return this.getHarvestLevel(null, "pickaxe") >= target.getHarvestLevel(target.getDefaultState());
		} else if("shovel".equals(toolType) && target.getHarvestLevel(target.getDefaultState()) <= 0) {
			// can be dug with wooden shovel
			return true;
		}
		// return true if block doesn't need tools
		return target.getHarvestLevel(target.getDefaultState()) == -1;
	}

	public String getMaterialName() {
		return metal.getName();
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean b) {
		super.addInformation(stack,player,list,b);
		MetalToolEffects.addToolSpecialPropertiesToolTip(metal, list);
	}

	@Override
	public MetalMaterial getMetalMaterial() {
		return metal;
	}
}
