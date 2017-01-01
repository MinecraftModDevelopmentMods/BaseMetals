package cyano.basemetals.init;

import java.util.Map;

import com.google.common.collect.*;

import cyano.basemetals.BaseMetals;
import cyano.basemetals.blocks.*;
import cyano.basemetals.fluids.CustomFluid;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.util.Config.Options;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.potion.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.Loader;
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

	private static boolean initDone = false;

	private static final BiMap<String, Fluid> fluidRegistry = HashBiMap.create();
	private static final BiMap<String, BlockFluidBase> fluidBlockRegistry = HashBiMap.create();

	private static final ResourceLocation dizzyPotionKey = new ResourceLocation("nausea");

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		String materialName;

		// fluids and fluid blocks
		if (Options.ENABLE_ADAMANTINE) {
			materialName = "adamantine";

			fluidAdamantine = addFluid(materialName, 2000, 10000, 769, 10);
			fluidBlockAdamantine = addFluidBlock(materialName);
		}
		if (Options.ENABLE_ANTIMONY) {
			materialName = "antimony";

			fluidAntimony = addFluid(materialName, 2000, 10000, 769, 10);
			fluidBlockAntimony = addFluidBlock(materialName);
		}
		if (Options.ENABLE_AQUARIUM) {
			materialName = "aquarium";

			fluidAquarium = addFluid(materialName, 2000, 10000, 769, 10);
			fluidBlockAquarium = addFluidBlock(materialName);
		}
		if (Options.ENABLE_BISMUTH) {
			materialName = "bismuth";

			fluidBismuth = addFluid(materialName, 2000, 10000, 769, 10);
			fluidBlockBismuth = addFluidBlock(materialName);
		}
		if (Options.ENABLE_BRASS) {
			materialName = "brass";

			fluidBrass = addFluid(materialName, 2000, 10000, 769, 10);
			fluidBlockBrass = addFluidBlock(materialName);
		}
		if (Options.ENABLE_BRONZE) {
			materialName = "bronze";

			fluidBronze = addFluid(materialName, 2000, 10000, 769, 10);
			fluidBlockBronze = addFluidBlock(materialName);
		}
		if (Options.ENABLE_COLDIRON) {
			materialName = "coldiron";

			fluidColdIron = addFluid(materialName, 2000, 10000, 769, 10);
			fluidBlockColdIron = addFluidBlock(materialName);
		}
		if (Options.ENABLE_COPPER) {
			materialName = "copper";

			fluidCopper = addFluid(materialName, 2000, 10000, 769, 10);
			fluidBlockCopper = addFluidBlock(materialName);
		}
		if (Options.ENABLE_CUPRONICKEL) {
			materialName = "cupronickel";

			fluidCupronickel = addFluid(materialName, 2000, 10000, 769, 10);
			fluidBlockCupronickel = addFluidBlock(materialName);
		}
		if (Options.ENABLE_ELECTRUM) {
			materialName = "electrum";

			fluidElectrum = addFluid(materialName, 2000, 10000, 769, 10);
			fluidBlockElectrum = addFluidBlock(materialName);
		}
		if (Options.ENABLE_INVAR) {
			materialName = "invar";

			fluidInvar = addFluid(materialName, 2000, 10000, 769, 10);
			fluidBlockInvar = addFluidBlock(materialName);
		}
		if (Options.ENABLE_LEAD) {
			materialName = "lead";

			fluidLead = addFluid(materialName, 2000, 10000, 769, 10);
			fluidBlockLead = addFluidBlock(materialName);
		}
		if (Options.ENABLE_MERCURY) {
			materialName = "mercury";

			fluidMercury = addFluid(materialName, 13594, 2000, 769, 0);
			fluidBlockMercury = addFluidBlock(materialName);
		}
		if (Options.ENABLE_MITHRIL) {
			materialName = "mithril";

			fluidMithril = addFluid(materialName, 2000, 10000, 769, 10);
			fluidBlockMithril = addFluidBlock(materialName);
		}
		if (Options.ENABLE_NICKEL) {
			materialName = "nickel";

			fluidNickel = addFluid(materialName, 2000, 10000, 769, 10);
			fluidBlockNickel = addFluidBlock(materialName);
		}
		if (Options.ENABLE_PEWTER) {
			materialName = "pewter";

			fluidPewter = addFluid(materialName, 2000, 10000, 769, 10);
			fluidBlockPewter = addFluidBlock(materialName);
		}
		if (Options.ENABLE_PLATINUM) {
			materialName = "platinum";

			fluidPlatinum = addFluid(materialName, 2000, 10000, 769, 10);
			fluidBlockPlatinum = addFluidBlock(materialName);
		}
		if (Options.ENABLE_SILVER) {
			materialName = "silver";

			fluidSilver = addFluid(materialName, 2000, 10000, 769, 10);
			fluidBlockSilver = addFluidBlock(materialName);
		}
		if (Options.ENABLE_STARSTEEL) {
			materialName = "starsteel";

			fluidStarSteel = addFluid(materialName, 2000, 10000, 769, 10);
			fluidBlockStarSteel = addFluidBlock(materialName);
		}
		if (Options.ENABLE_STEEL) {
			materialName = "steel";

			fluidSteel = addFluid(materialName, 2000, 10000, 769, 10);
			fluidBlockSteel = addFluidBlock(materialName);
		}
		if (Options.ENABLE_TIN) {
			materialName = "tin";

			fluidTin = addFluid(materialName, 2000, 10000, 769, 10);
			fluidBlockTin = addFluidBlock(materialName);
		}
		if (Options.ENABLE_ZINC) {
			materialName = "zinc";

			fluidZinc = addFluid(materialName, 2000, 10000, 769, 10);
			fluidBlockZinc = addFluidBlock(materialName);
		}

		initDone = true;
	}

	protected static Fluid addFluid(String name, int density, int viscosity, int temperature, int luminosity) {
		MetalMaterial metal = Materials.getMaterialByName(name);
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
		fluid.setUnlocalizedName(Loader.instance().activeModContainer().getModId() + "." + name);
		FluidRegistry.registerFluid(fluid);
		FluidRegistry.addBucketForFluid(fluid);

		if (material != null) {
			material.fluid = fluid;
		}
		fluidRegistry.put(name, fluid);
		return fluid;
	}

	protected static BlockFluidBase addFluidBlock(String name) {

		final ResourceLocation location = new ResourceLocation(Loader.instance().activeModContainer().getModId(), name);
		Fluid fluid = getFluidByName(name);
		BlockFluidBase block;

		if (name != "mercury") {
			block = new BlockMoltenFluid(fluid);
		} else {
			block = new InteractiveFluidBlock(fluid, false, (World w, EntityLivingBase e) -> {
				if (w.rand.nextInt(32) == 0) {
					e.addPotionEffect(new PotionEffect(Potion.REGISTRY.getObject(dizzyPotionKey), 30 * 20, 2));
				}
			});
		}

		block.setRegistryName(location);
		block.setUnlocalizedName(location.toString());
		GameRegistry.register(block);
		block.setCreativeTab(CreativeTabs.MISC);

		final ItemBlock itemBlock = new ItemBlock(block);
		itemBlock.setRegistryName(location);
		itemBlock.setUnlocalizedName(location.toString());
		GameRegistry.register(itemBlock);
		if (material != null) {
			material.fluidBlock = block;
		}
		fluidBlockRegistry.put(name, block);
		return block;
	}

	/**
	 * Gets a fluid by its name. The name is the name as it is registered in
	 * the GameRegistry, not its unlocalized name (the unlocalized name is the
	 * registered name plus the prefix "basemetals.")
	 *
	 * @param name The name of the fluid in question
	 * @return The fluid matching that name, or null if there isn't one
	 */
	public static Fluid getFluidByName(String name) {
		return fluidRegistry.get(name);
	}

	/**
	 * This is the reverse of the getFluidByName(...) method, returning the
	 * registered name of an fluid instance (Base Metals fluids only).
	 *
	 * @param b The item in question
	 * @return The name of the item, or null if the item is not a Base Metals
	 * fluid block.
	 */
	public static String getNameOfFluid(Fluid b) {
		return fluidRegistry.inverse().get(b);
	}

	public static Map<String, Fluid> getFluidRegistry() {
		return fluidRegistry;
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
