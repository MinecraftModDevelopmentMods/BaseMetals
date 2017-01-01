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














	public static Fluid fluidMercury = null;
	public static BlockFluidBase fluidBlockMercury = null;









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

		String material;

		// fluids and fluid blocks
		if (Options.ENABLE_ADAMANTINE) {
			material = "adamantine";

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_ANTIMONY) {
			material = "antimony";

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_AQUARIUM) {
			material = "aquarium";

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_BISMUTH) {
			material = "bismuth";

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_BRASS) {
			material = "brass";

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_BRONZE) {
			material = "bronze";

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_COLDIRON) {
			material = "coldiron";

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_COPPER) {
			material = "copper";

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_CUPRONICKEL) {
			material = "cupronickel";

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_ELECTRUM) {
			material = "electrum";

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_INVAR) {
			material = "invar";

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_LEAD) {
			material = "lead";

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_MERCURY) {
			material = "mercury";

			fluidMercury = addFluid(material, 13594, 2000, 769, 0);
			fluidBlockMercury = addFluidBlock(material);
		}
		if (Options.ENABLE_MITHRIL) {
			material = "mithril";

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_NICKEL) {
			material = "nickel";

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_PEWTER) {
			material = "pewter";

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_PLATINUM) {
			material = "platinum";

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_SILVER) {
			material = "silver";

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_STARSTEEL) {
			material = "starsteel";

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_STEEL) {
			material = "steel";

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_TIN) {
			material = "tin";

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_ZINC) {
			material = "zinc";

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}

		initDone = true;
	}

	protected static Fluid addFluid(String name, int density, int viscosity, int temperature, int luminosity) {
		MetalMaterial material = Materials.getMaterialByName(name);
		int tintColor = 0xFF000000;
		if (material != null) {
			tintColor = material.getTintColor();
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
		MetalMaterial material = Materials.getMaterialByName(name);
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
