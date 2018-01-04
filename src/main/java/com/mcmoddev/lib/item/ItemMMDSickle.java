package com.mcmoddev.lib.item;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;

public class ItemMMDSickle extends ItemTool implements IMMDObject {
	private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(net.minecraft.init.Blocks.TALLGRASS,
			net.minecraft.init.Blocks.RED_FLOWER, net.minecraft.init.Blocks.YELLOW_FLOWER,
			net.minecraft.init.Blocks.LEAVES, net.minecraft.init.Blocks.LEAVES2, net.minecraft.init.Blocks.WEB,
			net.minecraft.init.Blocks.WHEAT, net.minecraft.init.Blocks.VINE, net.minecraft.init.Blocks.SAPLING,
			net.minecraft.init.Blocks.BEETROOTS, net.minecraft.init.Blocks.BROWN_MUSHROOM,
			net.minecraft.init.Blocks.CACTUS, net.minecraft.init.Blocks.CARROTS, net.minecraft.init.Blocks.DEADBUSH,
			net.minecraft.init.Blocks.DOUBLE_PLANT, net.minecraft.init.Blocks.HAY_BLOCK,
			net.minecraft.init.Blocks.LIT_PUMPKIN, net.minecraft.init.Blocks.MELON_STEM,
			net.minecraft.init.Blocks.NETHER_WART, net.minecraft.init.Blocks.PUMPKIN_STEM,
			net.minecraft.init.Blocks.RED_MUSHROOM, net.minecraft.init.Blocks.REEDS,
			net.minecraft.init.Blocks.WATERLILY, net.minecraft.init.Blocks.WOOL);

	public static final ImmutableSet<net.minecraft.block.material.Material> vanilla_materials = ImmutableSet.of(
			net.minecraft.block.material.Material.WEB, net.minecraft.block.material.Material.LEAVES,
			net.minecraft.block.material.Material.PLANTS, net.minecraft.block.material.Material.VINE,
			net.minecraft.block.material.Material.GOURD, net.minecraft.block.material.Material.CACTUS);

	private final MMDMaterial material;
	private int actionDiameter;// = 3;
	private int actionHeight;// = 2;

	public ItemMMDSickle(MMDMaterial material) {
		// attack damage (float), attack speed (float), ToolMaterial, effectiveBlocks
		// (Set<Block>)
		super(Materials.getToolMaterialFor(material), EFFECTIVE_ON);
		this.attackDamage = calcAttackDamage(material);
		this.attackSpeed = calcAttackSpeed(material);
		this.efficiency = material.getToolEfficiency();
		this.setMaxDamage(material.getToolDurability());
		this.material = material;
		this.actionDiameter = 3;
		this.actionHeight = 2;
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		{
			for (String type : getToolClasses(stack)) {
				if (state.getBlock().isToolEffective(type, state))
					return efficiency;
			}
			return this.isEffective(state) ? this.efficiency : 1.0F;
		}
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
		this.getEffectedBlocks(pos, player.getEntityWorld(), player, stack, this.actionDiameter, this.actionHeight)
				.stream().filter(entityPos -> this.isEffective(player.getEntityWorld().getBlockState(pos)))
				.forEach(entityPos -> breakBlock(stack, player.getEntityWorld(), player, pos, entityPos));

		return super.onBlockStartBreak(stack, pos, player);
	}

	private static void sendPacket(Entity player, Packet<?> packet) {
		if (player instanceof EntityPlayerMP && ((EntityPlayerMP) player).connection != null) {
			((EntityPlayerMP) player).connection.sendPacket(packet);
		}
	}

	private void breakBlock(ItemStack tool, World world, EntityPlayer player, BlockPos centralPosition,
			BlockPos actualPosition) {
		// ToolHelper.breakExtraBlock(stack, player.getEntityWorld(), player, pos,
		// refPos);
		if (world.isAirBlock(actualPosition)) {
			return;
		}

		IBlockState bsatapos = world.getBlockState(actualPosition);
		Block block = bsatapos.getBlock();
		// fire off this event
		tool.onBlockDestroyed(world, bsatapos, centralPosition, player);

		if (!world.isRemote) {
			// send the blockbreak event
			int xp = ForgeHooks.onBlockBreakEvent(world, ((EntityPlayerMP) player).interactionManager.getGameType(),
					(EntityPlayerMP) player, actualPosition);
			if (xp == -1) {
				return;
			}

			// serverside we reproduce ItemInWorldManager.tryHarvestBlock

			TileEntity tileEntity = world.getTileEntity(actualPosition);

			if (block.removedByPlayer(bsatapos, world, actualPosition, player, true)) // boolean is if block can be
																						// harvested, checked above
			{
				block.onBlockDestroyedByPlayer(world, actualPosition, bsatapos);
				block.harvestBlock(world, player, actualPosition, bsatapos, tileEntity, tool);
				block.dropXpOnBlockBreak(world, actualPosition, xp);
			}

			sendPacket(player, new SPacketBlockChange(world, actualPosition));
		} else {
			world.playBroadcastSound(2001, actualPosition, Block.getStateId(bsatapos));
			if (block.removedByPlayer(bsatapos, world, actualPosition, player, true)) {
				block.onBlockDestroyedByPlayer(world, actualPosition, bsatapos);
			}

			tool.onBlockDestroyed(world, bsatapos, actualPosition, player);

			// tool should be, mostly, equal to what the player is holding... Maybe literal
			// equality is needed, dunno
			if (tool.getCount() == 0 && tool.equals(player.getHeldItemMainhand())) {
				ForgeEventFactory.onPlayerDestroyItem(player, tool, EnumHand.MAIN_HAND);
				player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
			}

			// send an update to the server, so we get an update back
			// if(PHConstruct.extraBlockUpdates)
			NetHandlerPlayClient netHandlerPlayClient = Minecraft.getMinecraft().getConnection();
			assert netHandlerPlayClient != null;
			netHandlerPlayClient.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
					actualPosition, Minecraft.getMinecraft().objectMouseOver.sideHit));
		}
	}

	/**
	 * Determine if a particular tool is effective against a given block/blockstate
	 * 
	 * @param stack
	 *            the tool in question
	 * @param state
	 *            the block/blockstate in question
	 * @return true if the tool is effective, false otherwise
	 */
	private static boolean isToolEffective(ItemStack stack, IBlockState state) {
		// check material
		// map the "Tool Classes" string-list to a list of Booleans where a "true"
		// equates to the "stack" being effective against a particular block-state
		List<Boolean> isEffective = stack.getItem().getToolClasses(stack).stream()
				.map(type -> state.getBlock().isToolEffective(type, state)).collect(Collectors.toList());
		// return true if the list we've generated contains a true
		return isEffective.contains(true);
	}

	private ImmutableList<BlockPos> getEffectedBlocks(BlockPos pos, World world, EntityPlayer player, ItemStack stack,
			int range, int heightDiff) {
		if (stack.isEmpty() || !(stack.getItem() instanceof ItemMMDSickle)) {
			return ImmutableList.of();
		}

		// where is the player, really ?
		IBlockState playerPositionState = world.getBlockState(pos);

		// the below "isEffective" check is also needed, but...
		// only if we get past this point
		if (!isToolEffective(stack, playerPositionState)) {
			return ImmutableList.of();
		}

		int rangeOff = (range - 1) / 2; // range should always be odd
		BlockPos start = pos.add(-rangeOff, -heightDiff, -rangeOff);
		BlockPos finish = start.add(range + 1, heightDiff + 1, range + 1);

		ImmutableList.Builder<BlockPos> builder = ImmutableList.builder();

		int xOff = 1;
		int yOff = 1;
		int zOff = 1;

		if (finish.getX() < start.getX()) {
			xOff = -1;
		}

		if (finish.getZ() < start.getZ()) {
			zOff = -1;
		}

		if (finish.getY() < start.getY()) { // unlikely, but maybe ?
			yOff = -1;
		}

		for (int x = start.getX(); x != finish.getX(); x += xOff) {
			for (int y = start.getY(); y != finish.getY(); y += yOff) {
				for (int z = start.getZ(); z != finish.getZ(); z += zOff) {
					BlockPos potential = new BlockPos(x, y, z);
					if (isEffective(world.getBlockState(potential))) {
						builder.add(potential);
					}
				}
			}
		}
		return builder.build();
	}

	private boolean isEffective(IBlockState state) {
		return vanilla_materials.contains(state.getMaterial());
	}

	private static float calcAttackDamage(MMDMaterial material) {
		return Float.max(material.getToolDurability() * 1.0f,
				(material.getToolEfficiency() * material.getToolHarvestLevel()) / 2.0f);
	}

	private static float calcAttackSpeed(MMDMaterial material) {
		return (Float.max(material.getToolDurability(), material.getToolEfficiency()) * 1.50f);
	}
}
