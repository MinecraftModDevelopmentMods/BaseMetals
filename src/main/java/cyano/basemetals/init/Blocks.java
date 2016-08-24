package cyano.basemetals.init;

import cyano.basemetals.BaseMetals;
import cyano.basemetals.blocks.*;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.IOreDictionaryEntry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockOre;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Map;

/**
 * This class initializes all blocks in Base Metals and provides some utility 
 * methods for looking up blocks. 
 * @author DrCyano
 *
 */
public abstract class Blocks {

	public static Block adamantine_bars;
	public static Block adamantine_block;
	public static Block adamantine_plate;
	public static BlockDoor adamantine_door;
	public static Block adamantine_ore;
	public static Block adamantine_trapdoor;

	public static Block aquarium_bars;
	public static Block aquarium_block;
	public static Block aquarium_plate;
	public static BlockDoor aquarium_door;
	public static Block aquarium_trapdoor;

	public static Block brass_bars;
	public static Block brass_block;
	public static Block brass_plate;
	public static BlockDoor brass_door;
	public static Block brass_trapdoor;

	public static Block bronze_bars;
	public static Block bronze_block;
	public static Block bronze_plate;
	public static BlockDoor bronze_door;
	public static Block bronze_trapdoor;

	public static Block coldiron_bars;
	public static Block coldiron_block;
	public static Block coldiron_plate;
	public static BlockDoor coldiron_door;
	public static Block coldiron_ore;
	public static Block coldiron_trapdoor;

	public static Block copper_bars;
	public static Block copper_block;
	public static Block copper_plate;
	public static BlockDoor copper_door;
	public static Block copper_ore;
	public static Block copper_trapdoor;

	public static Block cupronickel_bars;
	public static Block cupronickel_block;
	public static Block cupronickel_plate;
	public static BlockDoor cupronickel_door;
	public static Block cupronickel_trapdoor;

	public static Block electrum_bars;
	public static Block electrum_block;
	public static Block electrum_plate;
	public static BlockDoor electrum_door;
	public static Block electrum_trapdoor;

	public static Block invar_bars;
	public static Block invar_block;
	public static Block invar_plate;
	public static BlockDoor invar_door;
	public static Block invar_trapdoor;

	public static Block lead_bars;
	public static Block lead_block;
	public static Block lead_plate;
	public static BlockDoor lead_door;
	public static Block lead_ore;
	public static Block lead_trapdoor;

	public static Block mercury_ore;

	public static Block mithril_bars;
	public static Block mithril_block;
	public static Block mithril_plate;
	public static BlockDoor mithril_door;
	public static Block mithril_trapdoor;

	public static Block nickel_bars;
	public static Block nickel_block;
	public static Block nickel_plate;
	public static BlockDoor nickel_door;
	public static Block nickel_ore;
	public static Block nickel_trapdoor;

	public static Block platinum_bars;
	public static Block platinum_block;
	public static Block platinum_plate;
	public static BlockDoor platinum_door;
	public static Block platinum_ore;
	public static Block platinum_trapdoor;

	public static Block silver_bars;
	public static Block silver_block;
	public static Block silver_plate;
	public static BlockDoor silver_door;
	public static Block silver_ore;
	public static Block silver_trapdoor;

	public static Block starsteel_bars;
	public static Block starsteel_block;
	public static Block starsteel_plate;
	public static BlockDoor starsteel_door;
	public static Block starsteel_ore;
	public static Block starsteel_trapdoor;

	public static Block steel_bars;
	public static Block steel_block;
	public static Block steel_plate;
	public static BlockDoor steel_door;
	public static Block steel_trapdoor;

	public static Block tin_bars;
	public static Block tin_block;
	public static Block tin_plate;
	public static BlockDoor tin_door;
	public static Block tin_ore;
	public static Block tin_trapdoor;

	public static Block zinc_bars;
	public static Block zinc_block;
	public static Block zinc_plate;
	public static BlockDoor zinc_door;
	public static Block zinc_ore;
	public static Block zinc_trapdoor;

	public static Block iron_plate;

	public static Block gold_bars;
	public static Block gold_plate;
	public static BlockDoor gold_door;
	public static Block gold_trapdoor;

	public static Block human_detector;

	private static boolean initDone = false;

	private static final Map<String, Block> allBlocks = new HashMap<>();

	/**
	 * Gets an block by its name. The name is the name as it is registered in 
	 * the GameRegistry, not its unlocalized name (the unlocalized name is the 
	 * registered name plus the prefix "basemetals.")
	 * @param name The name of the block in question
	 * @return The block matching that name, or null if there isn't one
	 */
	public static Block getBlockByName(String name) {
		return allBlocks.get(name);
	}

	public static void init() {
		if(initDone)
			return;

		cyano.basemetals.init.Materials.init();
		cyano.basemetals.init.ItemGroups.init();

		adamantine_block = createBlock(Materials.adamantine);
		adamantine_plate = createPlate(Materials.adamantine);
		adamantine_ore = createOre(Materials.adamantine);
		adamantine_bars = createBars(Materials.adamantine);
		adamantine_door = createDoor(Materials.adamantine);
		adamantine_trapdoor = createTrapDoor(Materials.adamantine);

		aquarium_block = createBlock(Materials.aquarium);
		aquarium_plate = createPlate(Materials.aquarium);
		aquarium_bars = createBars(Materials.aquarium);
		aquarium_door = createDoor(Materials.aquarium);
		aquarium_trapdoor = createTrapDoor(Materials.aquarium);

		brass_block = createBlock(Materials.brass);
		brass_plate = createPlate(Materials.brass);
		brass_bars = createBars(Materials.brass);
		brass_door = createDoor(Materials.brass);
		brass_trapdoor = createTrapDoor(Materials.brass);

		bronze_block = createBlock(Materials.bronze);
		bronze_plate = createPlate(Materials.bronze);
		bronze_bars = createBars(Materials.bronze);
		bronze_door = createDoor(Materials.bronze);
		bronze_trapdoor = createTrapDoor(Materials.bronze);

		coldiron_block = createBlock(Materials.coldiron);
		coldiron_plate = createPlate(Materials.coldiron);
		coldiron_ore = createOre(Materials.coldiron);
		coldiron_bars = createBars(Materials.coldiron);
		coldiron_door = createDoor(Materials.coldiron);
		coldiron_trapdoor = createTrapDoor(Materials.coldiron);

		copper_block = createBlock(Materials.copper);
		copper_plate = createPlate(Materials.copper);
		copper_ore = createOre(Materials.copper);
		copper_bars = createBars(Materials.copper);
		copper_door = createDoor(Materials.copper);
		copper_trapdoor = createTrapDoor(Materials.copper);

		cupronickel_block = createBlock(Materials.cupronickel);
		cupronickel_plate = createPlate(Materials.cupronickel);
		cupronickel_bars = createBars(Materials.cupronickel);
		cupronickel_door = createDoor(Materials.cupronickel);
		cupronickel_trapdoor = createTrapDoor(Materials.cupronickel);

		electrum_block = createBlock(Materials.electrum);
		electrum_plate = createPlate(Materials.electrum);
		electrum_bars = createBars(Materials.electrum);
		electrum_door = createDoor(Materials.electrum);
		electrum_trapdoor = createTrapDoor(Materials.electrum);

		invar_block = createBlock(Materials.invar);
		invar_plate = createPlate(Materials.invar);
		invar_bars = createBars(Materials.invar);
		invar_door = createDoor(Materials.invar);
		invar_trapdoor = createTrapDoor(Materials.invar);

		lead_block = createBlock(Materials.lead);
		lead_plate = createPlate(Materials.lead);
		lead_ore = createOre(Materials.lead);
		lead_bars = createBars(Materials.lead);
		lead_door = createDoor(Materials.lead);
		lead_trapdoor = createTrapDoor(Materials.lead);

		mercury_ore = new BlockOre().setHardness(3.0f).setResistance(5.0f).setRegistryName(BaseMetals.MODID, "mercury_ore").setUnlocalizedName(BaseMetals.MODID+".mercury_ore");
		GameRegistry.register(mercury_ore);
		ItemBlock mercury_ore_item = new ItemBlock(mercury_ore);
		mercury_ore_item.setRegistryName(BaseMetals.MODID, "mercury_ore");
		GameRegistry.register(mercury_ore_item);
		allBlocks.put("mercury_ore", mercury_ore);
		OreDictionary.registerOre("oreMercury", mercury_ore);

		mithril_block = createBlock(Materials.mithril);
		mithril_plate = createPlate(Materials.mithril);
		mithril_bars = createBars(Materials.mithril);
		mithril_door = createDoor(Materials.mithril);
		mithril_trapdoor = createTrapDoor(Materials.mithril);

		nickel_block = createBlock(Materials.nickel);
		nickel_plate = createPlate(Materials.nickel);
		nickel_ore = createOre(Materials.nickel);
		nickel_bars = createBars(Materials.nickel);
		nickel_door = createDoor(Materials.nickel);
		nickel_trapdoor = createTrapDoor(Materials.nickel);

		platinum_block = createBlock(Materials.platinum);
		platinum_plate = createPlate(Materials.platinum);
		platinum_ore = createOre(Materials.platinum);
		platinum_bars = createBars(Materials.platinum);
		platinum_door = createDoor(Materials.platinum);
		platinum_trapdoor = createTrapDoor(Materials.platinum);

		silver_block = createBlock(Materials.silver);
		silver_plate = createPlate(Materials.silver);
		silver_ore = createOre(Materials.silver);
		silver_bars = createBars(Materials.silver);
		silver_door = createDoor(Materials.silver);
		silver_trapdoor = createTrapDoor(Materials.silver);

		starsteel_block = createBlock(Materials.starsteel);
		starsteel_plate = createPlate(Materials.starsteel);
		starsteel_ore = createOre(Materials.starsteel);
		starsteel_bars = createBars(Materials.starsteel);
		starsteel_door = createDoor(Materials.starsteel);
		starsteel_trapdoor = createTrapDoor(Materials.starsteel);
		starsteel_block.setLightLevel(0.5f);
		starsteel_plate.setLightLevel(0.5f);
		starsteel_ore.setLightLevel(0.5f);
		starsteel_bars.setLightLevel(0.5f);
		starsteel_door.setLightLevel(0.5f);
		starsteel_trapdoor.setLightLevel(0.5f);

		steel_block = createBlock(Materials.steel);
		steel_plate = createPlate(Materials.steel);
		steel_bars = createBars(Materials.steel);
		steel_door = createDoor(Materials.steel);
		steel_trapdoor = createTrapDoor(Materials.steel);

		tin_block = createBlock(Materials.tin);
		tin_plate = createPlate(Materials.tin);
		tin_ore = createOre(Materials.tin);
		tin_bars = createBars(Materials.tin);
		tin_door = createDoor(Materials.tin);
		tin_trapdoor = createTrapDoor(Materials.tin);

		zinc_block = createBlock(Materials.zinc);
		zinc_plate = createPlate(Materials.zinc);
		zinc_ore = createOre(Materials.zinc);

		iron_plate = createPlate(Materials.vanilla_iron);
		gold_plate = createPlate(Materials.vanilla_gold);

		human_detector = addBlock(new BlockHumanDetector(), "human_detector");

		// TODO: Make this support multiple oredicts
		// final block settings
		for(Block b : allBlocks.values()) {
			if(b instanceof IOreDictionaryEntry)
				OreDictionary.registerOre(((IOreDictionaryEntry)b).getOreDictionaryName(), b);
			if(!(b instanceof BlockMetalDoor))
				b.setCreativeTab(ItemGroups.tab);
		}

		initDone = true;
	}

	private static Block addBlock(Block block, String name) {
		ResourceLocation location = new ResourceLocation(BaseMetals.MODID, name);
		block.setRegistryName(location);
		block.setUnlocalizedName(location.toString());
		GameRegistry.register(block);

		if (!(block instanceof BlockMetalDoor)) {
			ItemBlock itemBlock = new ItemBlock(block);
			itemBlock.setRegistryName(location);
			itemBlock.setUnlocalizedName(location.toString());
			GameRegistry.register(itemBlock);
		}

		allBlocks.put(name, block);
		return block;
	}

	private static Block createPlate(MetalMaterial metal) {
		return addBlock(new BlockMetalPlate(metal), metal.getName() + "_plate");
	}

	private static Block createBars(MetalMaterial metal) {
		return addBlock(new BlockMetalBars(metal), metal.getName() + "_bars");
	}

	private static Block createBlock(MetalMaterial metal) {
		return createBlock(metal, false);
	}

	private static Block createBlock(MetalMaterial metal, boolean glow) {
		return addBlock(new BlockMetalBlock(metal, glow), metal.getName() + "_block");
	}

	private static Block createOre(MetalMaterial metal) {
		return addBlock(new BlockMetalOre(metal), metal.getName() + "_ore");
	}

	private static BlockDoor createDoor(MetalMaterial metal) {
		return (BlockDoor)addBlock(new BlockMetalDoor(metal), metal.getName() + "_door");
	}

	private static Block createTrapDoor(MetalMaterial metal) {
		return addBlock(new BlockMetalTrapDoor(metal), metal.getName() + "_trapdoor");
	}

	/**
	 * 
	 * @param event
	 */
	@SideOnly(Side.CLIENT)
	public static void registerItemRenders(FMLInitializationEvent event) {
		for(String name : allBlocks.keySet()) {
			if(allBlocks.get(name) instanceof BlockDoor)
				continue; // do not add door blocks
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
			.register(net.minecraft.item.Item.getItemFromBlock(allBlocks.get(name)), 0,
				new ModelResourceLocation(new ResourceLocation(BaseMetals.MODID, name), "inventory"));
		}
	}
}
