package cyano.basemetals.init;

import java.util.HashMap;
import java.util.Map;

import cyano.basemetals.BaseMetals;
import cyano.basemetals.blocks.BlockMoltenFluid;
import cyano.basemetals.blocks.InteractiveFluidBlock;
import cyano.basemetals.fluids.CustomFluid;
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
import net.minecraftforge.fml.common.FMLLog;
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

	private static final Map<String, BlockFluidBase> fluidBlockRegistry = new HashMap<>();
	private static final Map<BlockFluidBase, String> fluidBlockNames = new HashMap<>();

	private static final ResourceLocation dizzyPotionKey = new ResourceLocation("nausea");

	private static boolean initDone = false;

	/**
	 *
	 */
	public static void init() {
		if (initDone)
			return;

		// fluids
		fluidAdamantine = newFluid("adamantine", 2000, 10000, 330, 10, 0xFF53393F);
		fluidAntimony = newFluid("antimony", 2000, 10000, 330, 10, 0xFFD8E3DE);
		fluidAquarium = newFluid("aquarium", 2000, 10000, 330, 10, 0xFF000000);
		fluidBismuth = newFluid("bismuth", 2000, 10000, 330, 10, 0xFFDDD7CB);
		fluidBrass = newFluid("brass", 2000, 10000, 330, 10, 0xFFFFE374);
		fluidBronze = newFluid("bronze", 2000, 10000, 330, 10, 0xFFF7A54F);
		fluidColdIron = newFluid("coldiron", 2000, 10000, 330, 10, 0xFFC7CEF0);
		fluidCopper = newFluid("copper", 2000, 10000, 330, 10, 0xFFFF9F78);
		fluidCupronickel = newFluid("cupronickel", 2000, 10000, 330, 10, 0xFFC8AB6F);
		fluidElectrum = newFluid("electrum", 2000, 10000, 330, 10, 0xFFFFF2B3);
		fluidInvar = newFluid("invar", 2000, 10000, 330, 10, 0xFFD2CDB8);
		fluidLead = newFluid("lead", 2000, 10000, 330, 10, 0xFF7B7B7B);
		fluidMercury = newFluid("mercury", 13594, 2000, 300, 0, 0xFFD8D8D8);
		fluidMithril = newFluid("mithril", 2000, 10000, 330, 10, 0xFFF4FFFF);
		fluidNickel = newFluid("nickel", 2000, 10000, 330, 10, 0xFFEEFFEB);
		fluidPewter = newFluid("pewter", 2000, 10000, 330, 10, 0xFF92969F);
		fluidPlatinum = newFluid("platinum", 2000, 10000, 330, 10, 0xFFF2FFFF);
		fluidSilver = newFluid("silver", 2000, 10000, 330, 10, 0xFFFFFFFF);
		fluidStarSteel = newFluid("starsteel", 2000, 10000, 330, 10, 0xFF53393F);
		fluidSteel = newFluid("steel", 2000, 10000, 330, 10, 0xFFD5E3E5);
		fluidTin = newFluid("tin", 2000, 10000, 330, 10, 0xFFFFF7EE);
		fluidZinc = newFluid("zinc", 2000, 10000, 330, 10, 0xFFBCBCBC);

		// fluid blocks
		fluidBlockAdamantine = registerFluidBlock(fluidAdamantine, new BlockMoltenFluid(fluidAdamantine), "adamantine");
		fluidBlockAntimony = registerFluidBlock(fluidAntimony, new BlockMoltenFluid(fluidAntimony), "antimony");
		fluidBlockAquarium = registerFluidBlock(fluidAquarium, new BlockMoltenFluid(fluidAquarium), "aquarium");
		fluidBlockBismuth = registerFluidBlock(fluidBismuth, new BlockMoltenFluid(fluidBismuth), "bismuth");
		fluidBlockBrass = registerFluidBlock(fluidBrass, new BlockMoltenFluid(fluidBrass), "brass");
		fluidBlockBronze = registerFluidBlock(fluidBronze, new BlockMoltenFluid(fluidBronze), "bronze");
		fluidBlockColdIron = registerFluidBlock(fluidColdIron, new BlockMoltenFluid(fluidColdIron), "coldiron");
		fluidBlockCopper = registerFluidBlock(fluidCopper, new BlockMoltenFluid(fluidCopper), "copper");
		fluidBlockCupronickel = registerFluidBlock(fluidCupronickel, new BlockMoltenFluid(fluidCupronickel), "cupronickel");
		fluidBlockElectrum = registerFluidBlock(fluidElectrum, new BlockMoltenFluid(fluidElectrum), "electrum");
		fluidBlockInvar = registerFluidBlock(fluidInvar, new BlockMoltenFluid(fluidInvar), "invar");
		fluidBlockLead = registerFluidBlock(fluidLead, new BlockMoltenFluid(fluidLead), "lead");

		fluidBlockMercury = registerFluidBlock(fluidMercury,
				new InteractiveFluidBlock(fluidMercury, false, (World w, EntityLivingBase e) -> {
					if (w.rand.nextInt(32) == 0)
						e.addPotionEffect(new PotionEffect(Potion.REGISTRY.getObject(dizzyPotionKey), 30 * 20, 2));
				}), "liquid_mercury");

		fluidBlockMithril = registerFluidBlock(fluidMithril, new BlockMoltenFluid(fluidMithril), "mithril");
		fluidBlockNickel = registerFluidBlock(fluidNickel, new BlockMoltenFluid(fluidNickel), "nickel");
		fluidBlockPewter = registerFluidBlock(fluidPewter, new BlockMoltenFluid(fluidPewter), "pewter");
		fluidBlockPlatinum = registerFluidBlock(fluidPlatinum, new BlockMoltenFluid(fluidPlatinum), "platinum");
		fluidBlockSilver = registerFluidBlock(fluidSilver, new BlockMoltenFluid(fluidSilver), "silver");
		fluidBlockStarSteel = registerFluidBlock(fluidStarSteel, new BlockMoltenFluid(fluidStarSteel), "starsteel");
		fluidBlockSteel = registerFluidBlock(fluidSteel, new BlockMoltenFluid(fluidSteel), "steel");
		fluidBlockTin = registerFluidBlock(fluidTin, new BlockMoltenFluid(fluidTin), "tin");
		fluidBlockZinc = registerFluidBlock(fluidZinc, new BlockMoltenFluid(fluidZinc), "zinc");

		initDone = true;
	}

	private static Fluid newFluid(String name, int density, int viscosity, int temperature, int luminosity, int tintColor) {
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

	private static BlockFluidBase registerFluidBlock(Fluid fluid, BlockFluidBase block, String name) {
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
		fluidBlockNames.put(block, name);
		return block;
	}

	public static BlockFluidBase getFluidBlockByName(String name) {
		FMLLog.severe("getFluidBlockByName(): " + name);
		return fluidBlockRegistry.get(name);
	}

	public static String getNameOfFluidBlock(BlockFluidBase b) {
		FMLLog.severe("getNameOfFluidBlock(): " + b.getUnlocalizedName());
		return fluidBlockNames.get(b);
	}

	public static Map<String, BlockFluidBase> getFluidBlockRegistry() {
		FMLLog.severe("getFluidBlockRegistry(): ");
		return fluidBlockRegistry;
	}
}
