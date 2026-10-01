package com.mcmoddev.basemetals.test;

import com.mojang.authlib.GameProfile;
import com.mcmoddev.lib.util.Config.Options;
import net.minecraft.advancements.Advancement;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemSmeltedEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/** Checks achievement awards and saved progress in a running Forge server. */
@Mod(
		modid = "basemetals_advancement_probe",
		version = "1",
		dependencies = "required-after:basemetals",
		acceptedMinecraftVersions = "[1.12.2]")
public final class BaseMetalsAdvancementRuntimeProbe {
	private static final List<String> ALLOYS = Arrays.asList("aquarium", "brass", "bronze",
			"cupronickel", "electrum", "invar", "mithril", "pewter", "steel");
	private static final List<String> GENERAL = Arrays.asList("this_is_new", "blocktastic",
			"geologist", "metallurgy", "angel_of_death", "scuba_diver", "demon_slayer",
			"juggernaut", "moon_boots");
	private MinecraftServer server;
	private String profile;
	private boolean reload;
	private int checks;

	@EventHandler
	public void serverStarted(final FMLServerStartedEvent event) throws Exception {
		server = FMLCommonHandler.instance().getMinecraftServerInstance();
		profile = System.getProperty("basemetals.advancementProbe.profile");
		reload = "Reload".equals(System.getProperty("basemetals.advancementProbe.phase"));

		for (final String id : GENERAL) {
			checkDefinition(id);
		}

		for (final String alloy : ALLOYS) {
			checkDefinition(alloy + "_maker");
		}

		if (!"Disabled".equals(profile)) {
			verifyCraftingAndSmelting();
			verifyEquipment();
			verifyBlockPlacement();

			final boolean enabled = Options.enableAchievements();

			try {
				Options.setEnableAchievements(false);

				final EntityPlayerMP player = player("disabled-events");

				equip(player, "adamantine", 4);
				tick(player);
				require(!done(player, "juggernaut"), "Disabled achievements were awarded");
			} finally {
				Options.setEnableAchievements(enabled);
			}
		}

		final Properties result = new Properties();

		result.setProperty("profile", profile);
		result.setProperty("phase", System.getProperty("basemetals.advancementProbe.phase"));
		result.setProperty("checks", Integer.toString(checks));
		result.setProperty("passed", "true");

		try (FileOutputStream output = new FileOutputStream(
				new File(System.getProperty("basemetals.advancementProbe.marker")))) {
			result.store(output, "Base Metals gameplay advancement checks");
		}

		server.initiateShutdown();
	}

	private void checkDefinition(final String id) {
		final Advancement advancement = advancement(id);
		final boolean missing = "Disabled".equals(profile)
				|| ("MaterialOff".equals(profile)
						&& ("mithril_maker".equals(id) || "angel_of_death".equals(id)));

		require((advancement == null) == missing, "Unexpected advancement availability: " + id);

		if (!missing) {
			require(advancement.getDisplay() != null, id + " has no achievement display");
		}
	}

	private void verifyCraftingAndSmelting() {
		for (final String alloy : ALLOYS) {
			if ("MaterialOff".equals(profile) && "mithril".equals(alloy)) {
				continue;
			}

			final EntityPlayerMP player = player(alloy + "_maker");

			prepare(player, alloy + "_maker");
			MinecraftForge.EVENT_BUS.post(new ItemSmeltedEvent(player, stack(alloy + "_ingot")));
			require(done(player, alloy + "_maker"), alloy + " smelting did not award its advancement");
			require(done(player, "this_is_new"), "Base Metals ingot did not award This is new");
			player.getAdvancements().save();
		}

		for (final String form : Arrays.asList("bronze_blend", "bronze_smallblend", "stone_crackhammer")) {
			final String id = form.endsWith("crackhammer") ? "geologist" : "metallurgy";
			final EntityPlayerMP player = player(form);

			prepare(player, id);

			final InventoryCrafting grid = new InventoryCrafting(new Container() {
				@Override
				public boolean canInteractWith(final net.minecraft.entity.player.EntityPlayer p) {
					return true;
				}
			}, 3, 3);

			MinecraftForge.EVENT_BUS.post(new ItemCraftedEvent(player, stack(form), grid));
			require(done(player, id), form + " crafting did not award " + id);
			player.getAdvancements().save();
		}

		final EntityPlayerMP vanilla = player("vanilla");

		MinecraftForge.EVENT_BUS.post(new ItemSmeltedEvent(vanilla, new ItemStack(Items.IRON_INGOT)));
		require(!done(vanilla, "this_is_new"), "Vanilla ingot awarded Base Metals advancement");
	}

	private void verifyEquipment() {
		for (final String material : Arrays.asList("adamantine", "coldiron", "mithril")) {
			if ("MaterialOff".equals(profile) && "mithril".equals(material)) {
				continue;
			}

			final String id = "adamantine".equals(material)
					? "juggernaut"
					: "coldiron".equals(material) ? "demon_slayer" : "angel_of_death";
			final EntityPlayerMP player = player(id);

			prepare(player, id);
			equip(player, material, 3);
			player.setHeldItem(EnumHand.MAIN_HAND, stack(material + "_sword"));
			tick(player);
			require(!done(player, id), "Three armour pieces awarded " + id);
			equip(player, material, 4);
			player.setItemStackToSlot(EntityEquipmentSlot.HEAD, stack("copper_helmet"));
			tick(player);
			require(!done(player, id), "Mixed armour awarded " + id);
			equip(player, material, 4);

			if (!"adamantine".equals(material)) {
				player.setHeldItem(EnumHand.MAIN_HAND, stack("copper_sword"));
				tick(player);
				require(!done(player, id), "Wrong sword awarded " + id);
				player.setHeldItem(EnumHand.MAIN_HAND, stack(material + "_sword"));
			} else {
				player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
			}

			tick(player);
			require(done(player, id), "Full equipment did not award " + id);
			tick(player);
			require(done(player, id), "Repeating the tick lost " + id);
			player.getAdvancements().save();
		}

		final EntityPlayerMP boots = player("moon_boots");

		prepare(boots, "moon_boots");
		boots.setItemStackToSlot(EntityEquipmentSlot.FEET, stack("copper_boots"));
		tick(boots);
		require(!done(boots, "moon_boots"), "Ordinary boots awarded Moon Walker");
		boots.setItemStackToSlot(EntityEquipmentSlot.FEET, stack("starsteel_boots"));
		tick(boots);
		require(done(boots, "moon_boots"), "Starsteel boots did not award Moon Walker");
		boots.getAdvancements().save();

		final EntityPlayerMP scuba = player("scuba_diver");

		prepare(scuba, "scuba_diver");
		equip(scuba, "aquarium", 4);

		final WorldServer world = server.getWorld(0);
		final BlockPos feet = new BlockPos(8, 200, 8);
		final IBlockState oldFeet = world.getBlockState(feet);
		final IBlockState oldHead = world.getBlockState(feet.up());

		try {
			world.setBlockState(feet, Blocks.AIR.getDefaultState(), 2);
			world.setBlockState(feet.up(), Blocks.AIR.getDefaultState(), 2);
			scuba.setPosition(8.5D, 200.0D, 8.5D);
			tick(scuba);
			require(!done(scuba, "scuba_diver"), "Dry aquarium armour awarded SCUBA Diver");
			world.setBlockState(feet, Blocks.WATER.getDefaultState(), 2);
			world.setBlockState(feet.up(), Blocks.WATER.getDefaultState(), 2);
			scuba.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.EMPTY);
			tick(scuba);
			require(!done(scuba, "scuba_diver"), "Incomplete submerged suit awarded SCUBA Diver");
			equip(scuba, "aquarium", 4);
			tick(scuba);
			require(done(scuba, "scuba_diver"), "Submerged aquarium suit did not award SCUBA Diver");
			scuba.getAdvancements().save();
		} finally {
			world.setBlockState(feet, oldFeet, 2);
			world.setBlockState(feet.up(), oldHead, 2);
		}
	}

	private void verifyBlockPlacement() {
		final EntityPlayerMP player = player("blocktastic");

		prepare(player, "blocktastic");

		final WorldServer world = server.getWorld(0);
		final BlockPos pos = new BlockPos(9, 200, 9);
		final IBlockState original = world.getBlockState(pos);

		try {
			world.setBlockState(pos, Blocks.IRON_BLOCK.getDefaultState(), 2);
			place(player, pos);
			require(!done(player, "blocktastic"), "Vanilla iron block awarded Blocktastic");
			world.setBlockState(pos, Block.REGISTRY.getObject(
					new ResourceLocation("basemetals:copper_block")).getDefaultState(), 2);

			final BlockEvent.EntityPlaceEvent cancelled = placeEvent(player, pos);

			cancelled.setCanceled(true);
			MinecraftForge.EVENT_BUS.post(cancelled);
			require(!done(player, "blocktastic"), "Cancelled placement awarded Blocktastic");
			place(player, pos);
			require(done(player, "blocktastic"), "Copper storage block did not award Blocktastic");
			player.getAdvancements().save();
		} finally {
			world.setBlockState(pos, original, 2);
		}
	}

	private void place(final EntityPlayerMP player, final BlockPos pos) {
		MinecraftForge.EVENT_BUS.post(placeEvent(player, pos));
	}

	private BlockEvent.EntityPlaceEvent placeEvent(final EntityPlayerMP player, final BlockPos pos) {
		return new BlockEvent.EntityPlaceEvent(BlockSnapshot.getBlockSnapshot(server.getWorld(0), pos),
				Blocks.AIR.getDefaultState(), player);
	}

	private void equip(final EntityPlayerMP player, final String material, final int count) {
		final EntityEquipmentSlot[] slots = {EntityEquipmentSlot.FEET, EntityEquipmentSlot.LEGS,
				EntityEquipmentSlot.CHEST, EntityEquipmentSlot.HEAD};
		final String[] forms = {"boots", "leggings", "chestplate", "helmet"};

		for (int index = 0; index < slots.length; index++) {
			player.setItemStackToSlot(slots[index], index < count
					? stack(material + "_" + forms[index])
					: ItemStack.EMPTY);
		}
	}

	private void tick(final EntityPlayerMP player) {
		player.ticksExisted = 20;
		MinecraftForge.EVENT_BUS.post(new TickEvent.PlayerTickEvent(TickEvent.Phase.END, player));
	}

	private EntityPlayerMP player(final String name) {
		final UUID id = UUID.nameUUIDFromBytes((profile + name).getBytes(StandardCharsets.UTF_8));
		final EntityPlayerMP player = new EntityPlayerMP(server, server.getWorld(0),
				new GameProfile(id, name), new PlayerInteractionManager(server.getWorld(0)));

		player.connection = new NetHandlerPlayServer(server,
				new NetworkManager(EnumPacketDirection.SERVERBOUND), player);

		return player;
	}

	private void prepare(final EntityPlayerMP player, final String id) {
		if (reload) {
			require(done(player, id), "Reload lost saved advancement " + id);
		}

		player.getAdvancements().revokeCriterion(advancement(id), "event");
	}

	private Advancement advancement(final String id) {
		return server.getAdvancementManager().getAdvancement(new ResourceLocation("basemetals", id));
	}

	private boolean done(final EntityPlayerMP player, final String id) {
		return player.getAdvancements().getProgress(advancement(id)).isDone();
	}

	private ItemStack stack(final String name) {
		final ResourceLocation id = new ResourceLocation("basemetals", name);

		require(Item.REGISTRY.containsKey(id), "Missing fixture item " + id);

		return new ItemStack(Item.REGISTRY.getObject(id));
	}

	private void require(final boolean condition, final String message) {
		checks++;

		if (!condition) {
			throw new IllegalStateException(message);
		}
	}
}
