package cyano.basemetals.init;

import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import cyano.basemetals.BaseMetals;
import cyano.basemetals.blocks.BlockMoltenFluid;
import cyano.basemetals.blocks.InteractiveFluidBlock;
import cyano.basemetals.fluids.CustomFluid;
import cyano.basemetals.material.MetalMaterial;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * This class initializes all fluids in Base Metals and provides some utility
 * methods for looking up fluids.
 *
 * @author DrCyano
 *
 */
public abstract class Fluids {

	static {
		FluidRegistry.enableUniversalBucket();
	}

	public static Fluid fluidAntimony = null;
	public static BlockFluidBase fluidBlockAntimony = null;

	public static Fluid fluidAdamantine = null;
	public static BlockFluidBase fluidBlockAdamantine = null;

	public static Fluid fluidAquarium = null;
	public static BlockFluidBase fluidBlockAquarium = null;

	public static Fluid fluidBismuth = null;
	public static BlockFluidBase fluidBlockBismuth = null;

	public static Fluid fluidBrass = null;
	public static BlockFluidBase fluidBlockBrass = null;

	public static Fluid fluidBronze = null;
	public static BlockFluidBase fluidBlockBronze = null;

	public static Fluid fluidColdIron = null;
	public static BlockFluidBase fluidBlockColdIron = null;

	public static Fluid fluidCopper = null;
	public static BlockFluidBase fluidBlockCopper = null;

	public static Fluid fluidCupronickel = null;
	public static BlockFluidBase fluidBlockCupronickel = null;

	public static Fluid fluidElectrum = null;
	public static BlockFluidBase fluidBlockElectrum = null;

	public static Fluid fluidInvar = null;
	public static BlockFluidBase fluidBlockInvar = null;

	public static Fluid fluidLead = null;
	public static BlockFluidBase fluidBlockLead = null;

	public static Fluid fluidMithril = null;
	public static BlockFluidBase fluidBlockMithril = null;

	public static Fluid fluidMercury = null;
	public static BlockFluidBase fluidBlockMercury = null;

	public static Fluid fluidNickel = null;
	public static BlockFluidBase fluidBlockNickel = null;

	public static Fluid fluidPewter = null;
	public static BlockFluidBase fluidBlockPewter = null;

	public static Fluid fluidPlatinum = null;
	public static BlockFluidBase fluidBlockPlatinum = null;

	public static Fluid fluidSilver = null;
	public static BlockFluidBase fluidBlockSilver = null;

	public static Fluid fluidStarSteel = null;
	public static BlockFluidBase fluidBlockStarSteel = null;

	public static Fluid fluidSteel = null;
	public static BlockFluidBase fluidBlockSteel = null;

	public static Fluid fluidTin = null;
	public static BlockFluidBase fluidBlockTin = null;

	public static Fluid fluidZinc = null;
	public static BlockFluidBase fluidBlockZinc = null;

	private static BiMap<String, BlockFluidBase> fluidBlockRegistry = HashBiMap.create();

	private static final ResourceLocation dizzyPotionKey = new ResourceLocation("nausea");

	private static boolean initDone = false;

	/**
	 *
	 */
	public static void init() {
		if (initDone)
			return;

		// fluids
		fluidAdamantine = addFluid("adamantine", 2000, 10000, 330, 10);
		fluidAntimony = addFluid("antimony", 2000, 10000, 330, 10);
		fluidAquarium = addFluid("aquarium", 2000, 10000, 330, 10);
		fluidBismuth = addFluid("bismuth", 2000, 10000, 330, 10);
		fluidBrass = addFluid("brass", 2000, 10000, 330, 10);
		fluidBronze = addFluid("bronze", 2000, 10000, 330, 10);
		fluidColdIron = addFluid("coldiron", 2000, 10000, 330, 10);
		fluidCopper = addFluid("copper", 2000, 10000, 330, 10);
		fluidCupronickel = addFluid("cupronickel", 2000, 10000, 330, 10);
		fluidElectrum = addFluid("electrum", 2000, 10000, 330, 10);
		fluidInvar = addFluid("invar", 2000, 10000, 330, 10);
		fluidLead = addFluid("lead", 2000, 10000, 330, 10);
		fluidMercury = addFluid("mercury", 13594, 2000, 300, 0);
		fluidMithril = addFluid("mithril", 2000, 10000, 330, 10);
		fluidNickel = addFluid("nickel", 2000, 10000, 330, 10);
		fluidPewter = addFluid("pewter", 2000, 10000, 330, 10);
		fluidPlatinum = addFluid("platinum", 2000, 10000, 330, 10);
		fluidSilver = addFluid("silver", 2000, 10000, 330, 10);
		fluidStarSteel = addFluid("starsteel", 2000, 10000, 330, 10);
		fluidSteel = addFluid("steel", 2000, 10000, 330, 10);
		fluidTin = addFluid("tin", 2000, 10000, 330, 10);
		fluidZinc = addFluid("zinc", 2000, 10000, 330, 10);

		// fluid blocks
		fluidBlockAdamantine = addFluidBlock(fluidAdamantine, new BlockMoltenFluid(fluidAdamantine), "adamantine");
		fluidBlockAntimony = addFluidBlock(fluidAntimony, new BlockMoltenFluid(fluidAntimony), "antimony");
		fluidBlockAquarium = addFluidBlock(fluidAquarium, new BlockMoltenFluid(fluidAquarium), "aquarium");
		fluidBlockBismuth = addFluidBlock(fluidBismuth, new BlockMoltenFluid(fluidBismuth), "bismuth");
		fluidBlockBrass = addFluidBlock(fluidBrass, new BlockMoltenFluid(fluidBrass), "brass");
		fluidBlockBronze = addFluidBlock(fluidBronze, new BlockMoltenFluid(fluidBronze), "bronze");
		fluidBlockColdIron = addFluidBlock(fluidColdIron, new BlockMoltenFluid(fluidColdIron), "coldiron");
		fluidBlockCopper = addFluidBlock(fluidCopper, new BlockMoltenFluid(fluidCopper), "copper");
		fluidBlockCupronickel = addFluidBlock(fluidCupronickel, new BlockMoltenFluid(fluidCupronickel), "cupronickel");
		fluidBlockElectrum = addFluidBlock(fluidElectrum, new BlockMoltenFluid(fluidElectrum), "electrum");
		fluidBlockInvar = addFluidBlock(fluidInvar, new BlockMoltenFluid(fluidInvar), "invar");
		fluidBlockLead = addFluidBlock(fluidLead, new BlockMoltenFluid(fluidLead), "lead");

		fluidBlockMercury = addFluidBlock(fluidMercury,
				new InteractiveFluidBlock(fluidMercury, false, (World w, EntityLivingBase e) -> {
					if (w.rand.nextInt(32) == 0)
						e.addPotionEffect(new PotionEffect(Potion.REGISTRY.getObject(dizzyPotionKey), 30 * 20, 2));
				}), "liquid_mercury");

		fluidBlockMithril = addFluidBlock(fluidMithril, new BlockMoltenFluid(fluidMithril), "mithril");
		fluidBlockNickel = addFluidBlock(fluidNickel, new BlockMoltenFluid(fluidNickel), "nickel");
		fluidBlockPewter = addFluidBlock(fluidPewter, new BlockMoltenFluid(fluidPewter), "pewter");
		fluidBlockPlatinum = addFluidBlock(fluidPlatinum, new BlockMoltenFluid(fluidPlatinum), "platinum");
		fluidBlockSilver = addFluidBlock(fluidSilver, new BlockMoltenFluid(fluidSilver), "silver");
		fluidBlockStarSteel = addFluidBlock(fluidStarSteel, new BlockMoltenFluid(fluidStarSteel), "starsteel");
		fluidBlockSteel = addFluidBlock(fluidSteel, new BlockMoltenFluid(fluidSteel), "steel");
		fluidBlockTin = addFluidBlock(fluidTin, new BlockMoltenFluid(fluidTin), "tin");
		fluidBlockZinc = addFluidBlock(fluidZinc, new BlockMoltenFluid(fluidZinc), "zinc");

		initDone = true;
	}

	private static Fluid addFluid(String name, int density, int viscosity, int temperature, int luminosity) {
		MetalMaterial metal = Materials.getMetalByName(name);
		int tintColor = 0xFF000000;
		if (metal != null) { 
			tintColor = metal.getTintColor();
		} else {
			tintColor = 0xFFD8D8D8; // Hack for Mercury as it doesn't have a metalMaterial
		}

		final Fluid fluid = new CustomFluid(name, new ResourceLocation(BaseMetals.MODID + ":blocks/molten_metal_still"), new ResourceLocation(BaseMetals.MODID + ":blocks/molten_metal_flow"), tintColor);
		fluid.setDensity(density);
		fluid.setViscosity(viscosity);
		fluid.setTemperature(temperature);
		fluid.setLuminosity(luminosity);
		fluid.setUnlocalizedName(BaseMetals.MODID + "." + name);
		FluidRegistry.registerFluid(fluid);
		FluidRegistry.addBucketForFluid(fluid);
		return fluid;
	}

	private static BlockFluidBase addFluidBlock(Fluid fluid, BlockFluidBase block, String name) {
		final ResourceLocation location = new ResourceLocation(BaseMetals.MODID, name);
		block.setRegistryName(location);
		block.setUnlocalizedName(location.toString());
		GameRegistry.register(block);
		block.setCreativeTab(CreativeTabs.MISC);

		final ItemBlock itemBlock = new ItemBlock(block);
		itemBlock.setRegistryName(location);
		itemBlock.setUnlocalizedName(location.toString());
		GameRegistry.register(itemBlock);

		fluidBlockRegistry.put(name, block);
		return block;
	}

	/**
	 * Gets a fluid block by its name. The name is the name as it is registered in
	 * the GameRegistry, not its unlocalized name (the unlocalized name is the
	 * registered name plus the prefix "basemetals.")
	 *
	 * @param name The name of the fluid block in question
	 * @return The fluid block matching that name, or null if there isn't one
	 */
	public static BlockFluidBase getFluidBlockByName(String name) {
		return fluidBlockRegistry.get(name);
	}

	/**
	 * This is the reverse of the getFluidBlockByName(...) method, returning the
	 * registered name of an fluid block instance (Base Metals fluid blocks only).
	 *
	 * @param b The item in question
	 * @return The name of the item, or null if the item is not a Base Metals
	 * fluid block.
	 */
	public static String getNameOfFluidBlock(BlockFluidBase b) {
		return fluidBlockRegistry.inverse().get(b);
	}

	public static Map<String, BlockFluidBase> getFluidBlockRegistry() {
		return fluidBlockRegistry;
	}
}
