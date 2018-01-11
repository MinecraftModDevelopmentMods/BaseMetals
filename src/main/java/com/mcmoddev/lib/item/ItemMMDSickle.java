package com.mcmoddev.lib.item;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;

public class ItemMMDSickle extends GenericMMDItem implements IMMDObject {
	public static final ImmutableSet<net.minecraft.block.material.Material> vanilla_materials = ImmutableSet.of(
			net.minecraft.block.material.Material.WEB, net.minecraft.block.material.Material.LEAVES,
			net.minecraft.block.material.Material.PLANTS, net.minecraft.block.material.Material.VINE,
			net.minecraft.block.material.Material.GOURD, net.minecraft.block.material.Material.CACTUS);

	private final MMDMaterial material;
	private int actionDiameter;// = 3;
	private float efficiency;
	private float attackDamage;
	private float attackSpeed;

	public ItemMMDSickle(MMDMaterial material) {
		super(material);
		this.efficiency = material.getToolEfficiency();
		this.setMaxDamage(material.getToolDurability());
		this.material = material;
		this.actionDiameter = 3;
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
		this.getEffectedBlocks(pos, player.getEntityWorld(), player, stack, this.actionDiameter).stream()
				.filter(entityPos -> this.isEffective(player.getEntityWorld().getBlockState(entityPos)))
				.forEach(entityPos -> breakBlock(stack, player.getEntityWorld(), player, pos, entityPos));

		return true;// super.onBlockStartBreak(stack, pos, player);
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
	@SuppressWarnings("unused")
	private static boolean isToolEffective(ItemStack stack, IBlockState state) {
		// check material
		// map the "Tool Classes" string-list to a list of Booleans where a "true"
		// equates to the "stack" being effective against a particular block-state
		List<Boolean> isEffective = stack.getItem().getToolClasses(stack).stream()
				.map(type -> state.getBlock().isToolEffective(type, state)).collect(Collectors.toList());
		// return true if the list we've generated contains a true
		stack.getItem().getToolClasses(stack).forEach(BaseMetals.logger::fatal);
		isEffective.forEach(b -> BaseMetals.logger.debug("%s", b));
		return isEffective.contains(true);
	}

	private ImmutableList<BlockPos> getEffectedBlocks(BlockPos pos, World world, EntityPlayer player, ItemStack stack,
			int range) {

		BaseMetals.logger.debug("Entered getEffectedBlocks");
		if (stack.isEmpty() || !(stack.getItem() instanceof ItemMMDSickle)) {
			BaseMetals.logger.debug("Early out because tool-stack is empty");
			return ImmutableList.of();
		}

		// where is the player, really ?
		// FIXME: ties into later tool-effectiveness optimization
		// IBlockState playerPositionState = world.getBlockState(pos);

		// the below "isEffective" check is also needed, but...
		// only if we get past this point
		// FIXME: This should early-out if the original block clicked isn't harvestable
		// but doesn't work
		/*
		 * if (!isToolEffective(stack, playerPositionState)) { BaseMetals.logger.
		 * debug("Early out because isEffective on players position is false"); return
		 * ImmutableList.of(); }
		 */

		int rangeOff = (range - 1) / 2; // range should always be odd

		ImmutableList.Builder<BlockPos> builder = ImmutableList.builder();
		for (int x = -rangeOff; x < (rangeOff + 1); x++) {
			for (int z = -rangeOff; z < (rangeOff + 1); z++) {
				BlockPos potential = pos.add(x, 0, z);
				if (isEffective(world.getBlockState(potential))) {
					builder.add(potential);
				}
			}
		}

		return builder.build();
	}

	private boolean isEffective(IBlockState state) {
		return vanilla_materials.contains(state.getMaterial());
	}

	/**
	 * Gets a map of item attribute modifiers, used by ItemSword to increase hit
	 * damage.
	 */
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

		if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
					new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", (double) this.attackDamage, 0));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(),
					new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", (double) this.attackSpeed, 0));
		}

		return multimap;
	}

	@Nullable
	private String toolClass;

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass,
			@javax.annotation.Nullable net.minecraft.entity.player.EntityPlayer player,
			@javax.annotation.Nullable IBlockState blockState) {
		int level = super.getHarvestLevel(stack, toolClass, player, blockState);
		if (level == -1 && toolClass.equals(this.toolClass)) {
			return this.material.getToolHarvestLevel();
		} else {
			return level;
		}
	}

	@Override
	public Set<String> getToolClasses(ItemStack stack) {
		return toolClass != null ? com.google.common.collect.ImmutableSet.of(toolClass) : super.getToolClasses(stack);
	}
}