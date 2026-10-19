package com.mcmoddev.basemetals.test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mcmoddev.basemetals.LegacyMappings;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.ThreadedFileIOBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/** Loads the indexed content in a disposable legacy save and checks item recovery. */
@Mod(modid = "basemetals_legacy_probe", name = "Base Metals Legacy Runtime Probe", version = "1",
		dependencies = "required-after:basemetals", acceptedMinecraftVersions = "[1.12.2]")
public final class BaseMetalsLegacyRuntimeProbe {
	private MinecraftServer server;
	private final List<int[]> chunks = new ArrayList<>();
	private int next;
	private boolean ciFixture;

	@Mod.EventHandler
	public void started(final FMLServerStartedEvent event) throws Exception {
		server = FMLCommonHandler.instance().getMinecraftServerInstance();
		ciFixture = Boolean.getBoolean("basemetals.legacyProbe.ciFixture");
		if (ciFixture) {
			MinecraftForge.EVENT_BUS.register(this);
			return;
		}

		for (String line : Files.readAllLines(Paths.get(required("basemetals.legacyProbe.index")),
				StandardCharsets.UTF_8)) {
			String[] values = line.split(",");
			chunks.add(new int[] { Integer.parseInt(values[0]), Integer.parseInt(values[1]),
					Integer.parseInt(values[2]) });
		}
		if (chunks.isEmpty()) throw new AssertionError("No source-content chunks were indexed");
		verifyMappingsAndStacks();
		for (WorldServer world : server.worlds) {
			world.getGameRules().setOrCreateGameRule("randomTickSpeed", "0");
			world.getGameRules().setOrCreateGameRule("doFireTick", "false");
		}
		MinecraftForge.EVENT_BUS.register(this);
		System.out.println("BASE_METALS_LEGACY_INDEX chunks=" + chunks.size());
	}

	private void verifyMappingsAndStacks() {
		RegistryEvent.MissingMappings.Mapping<Block> fluid = new RegistryEvent.MissingMappings.Mapping<>(
				ForgeRegistries.BLOCKS, ForgeRegistries.BLOCKS,
				new ResourceLocation("basemetals:liquid_mercury"), 218);
		MinecraftForge.EVENT_BUS.post(new RegistryEvent.MissingMappings<>(
				new ResourceLocation("minecraft:blocks"), ForgeRegistries.BLOCKS,
				Collections.singletonList(fluid)));
		require(fluid.getAction() == RegistryEvent.MissingMappings.Action.REMAP, "Mercury handler was not called");
		require(fluid.getTarget() == ForgeRegistries.BLOCKS.getValue(new ResourceLocation("basemetals:mercury")),
				"Wrong mercury replacement");
		List<String> names = new ArrayList<>();
		Collections.addAll(names, "basemetals:liquid_mercury", "basemetals:carbon_powder", "basemetals:iron_nugget");
		for (String material : new String[] { "adamantine", "aquarium", "brass", "bronze", "coldiron",
				"copper", "cupronickel", "electrum", "invar", "lead", "mithril", "nickel", "platinum",
				"silver", "starsteel", "steel", "tin" }) names.add("basemetals:" + material + "_door_item");
		int checked = 0;
		for (String name : names) {
			ResourceLocation target = new ResourceLocation(LegacyMappings.itemTarget(name));
			RegistryEvent.MissingMappings.Mapping<Item> mapping = new RegistryEvent.MissingMappings.Mapping<>(
					ForgeRegistries.ITEMS, ForgeRegistries.ITEMS, new ResourceLocation(name), 4203 + checked);
			MinecraftForge.EVENT_BUS.post(new RegistryEvent.MissingMappings<>(new ResourceLocation("minecraft:items"),
					ForgeRegistries.ITEMS, Collections.singletonList(mapping)));
			require(mapping.getAction() == RegistryEvent.MissingMappings.Action.REMAP, "Item handler missed " + name);
			require(mapping.getTarget() == ForgeRegistries.ITEMS.getValue(target), "Wrong target for " + name);
			NBTTagCompound saved = new NBTTagCompound();
			saved.setString("id", name);
			saved.setByte("Count", (byte) 49);
			saved.setShort("Damage", (short) 3);
			NBTTagCompound custom = new NBTTagCompound();
			custom.setString("legacy_note", "preserve this item data");
			custom.setLong("serial", 1234567890123L);
			NBTTagCompound display = new NBTTagCompound();
			display.setString("Name", "Legacy material");
			custom.setTag("display", display);
			NBTTagCompound enchantment = new NBTTagCompound();
			enchantment.setShort("id", (short) 34);
			enchantment.setShort("lvl", (short) 3);
			NBTTagList enchantments = new NBTTagList();
			enchantments.appendTag(enchantment);
			custom.setTag("ench", enchantments);
			saved.setTag("tag", custom);
			NBTTagCompound fixed = FMLCommonHandler.instance().getDataFixer().process(FixTypes.ITEM_INSTANCE, saved);
			require(target.toString().equals(fixed.getString("id")), "Raw stack fixer missed " + name);
			ItemStack recovered = new ItemStack(fixed);
			require(!recovered.isEmpty() && recovered.getItem() == mapping.getTarget(), "Saved alias failed: " + name);
			require(recovered.getCount() == 49 && recovered.getItemDamage() == 3, "Count or damage changed: " + name);
			require(custom.equals(recovered.getTagCompound()), "Custom NBT changed: " + name);
			ItemStack roundTrip = new ItemStack(recovered.writeToNBT(new NBTTagCompound()));
			require(ItemStack.areItemStacksEqual(recovered, roundTrip), "Reload changed " + name);
			checked++;
		}
		System.out.println("BASE_METALS_LEGACY_MAPPING_PASS block=1 items=" + checked + " nbt_roundtrips=" + checked);
	}

	@SubscribeEvent
	public void tick(final TickEvent.ServerTickEvent event) {
		if (event.phase != TickEvent.Phase.END) return;
		try {
			if (ciFixture) {
				verifyCiFixture();
				MinecraftForge.EVENT_BUS.unregister(this);
				server.initiateShutdown();
				return;
			}

			for (int i = 0; i < 4 && next < chunks.size(); i++) {
				int[] point = chunks.get(next++);
				WorldServer world = server.getWorld(point[0]);
				Chunk chunk = world.getChunk(point[1], point[2]);
				world.getChunkProvider().queueUnload(chunk);
			}
			if (next == chunks.size()) {
				ThreadedFileIOBase.getThreadedIOInstance().waitForFinish();
				String phase = required("basemetals.legacyProbe.phase");
				Files.write(new File(required("basemetals.legacyProbe.marker")).toPath(),
						Collections.singletonList("phase=" + phase + " chunks=" + next), StandardCharsets.UTF_8);
				System.out.println("BASE_METALS_LEGACY_WORLD_PASS phase=" + phase + " chunks=" + next);
				MinecraftForge.EVENT_BUS.unregister(this);
				server.initiateShutdown();
			}
		} catch (Throwable failure) {
			failure.printStackTrace();
			FMLCommonHandler.instance().exitJava(2, true);
		}
	}

	private void verifyCiFixture() throws Exception {
		WorldServer world = server.getWorld(0);
		String phase = required("basemetals.legacyProbe.phase");
		File marker = new File(required("basemetals.legacyProbe.marker"));
		world.getGameRules().setOrCreateGameRule("randomTickSpeed", "0");
		world.getGameRules().setOrCreateGameRule("doFireTick", "false");
		world.getGameRules().setOrCreateGameRule("doMobSpawning", "false");

		if ("seed".equals(phase)) {
			LegacySaveFixture.seed(world, marker.getParentFile());
		} else {
			require(world.getChunkProvider().getLoadedChunk(LegacySaveFixture.LATER_CHUNK,
					LegacySaveFixture.LATER_CHUNK) == null, "Distant chunk loaded before the test visited it");
			LegacySaveFixture.verifyLoaded(world, 0);

			if ("delayed".equals(phase) || "stable".equals(phase)) {
				LegacySaveFixture.verifyLoaded(world, LegacySaveFixture.LATER_CHUNK);
			}
		}

		server.saveAllWorlds(false);
		ThreadedFileIOBase.getThreadedIOInstance().waitForFinish();
		Files.write(marker.toPath(), ("phase=" + phase + "\npassed=true\n").getBytes(StandardCharsets.UTF_8));
		System.out.println("BASE_METALS_LEGACY_RUNTIME_PASS phase=" + phase);
	}

	private static String required(final String key) {
		String value = System.getProperty(key);
		if (value == null) throw new AssertionError("Missing property " + key);
		return value;
	}

	private static void require(final boolean passed, final String message) {
		if (!passed) throw new AssertionError(message);
	}
}
