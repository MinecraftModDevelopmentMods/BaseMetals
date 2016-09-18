package cyano.basemetals.init;

import cyano.basemetals.BaseMetals;
import cyano.basemetals.blocks.BlockMoltenFluid;
import cyano.basemetals.blocks.InteractiveFluidBlock;
import cyano.basemetals.fluids.CustomFluid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.*;

import java.util.HashMap;
import java.util.Map;

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

	private static final Map<Fluid, BlockFluidBase> fluidBlocks = new HashMap<>();
	private static final Map<BlockFluidBase, String> fluidBlockNames = new HashMap<>();

	private static final ResourceLocation dizzyPotionKey = new ResourceLocation("nausea");

	private static boolean initDone = false;

	/**
	 *
	 */
	public static void init() {
		if(initDone)
			return;


		// fluids
		fluidAdamantine = newFluid(BaseMetals.MODID, "adamantine", 2000, 10000, 330, 10, 0xFF000000);
		fluidAntimony = newFluid(BaseMetals.MODID, "antimony", 2000, 10000, 330, 10, 0xFF000000);
		fluidAquarium = newFluid(BaseMetals.MODID, "aquarium", 2000, 10000, 330, 10, 0xFF000000);
		fluidBismuth = newFluid(BaseMetals.MODID, "bismuth", 2000, 10000, 330, 10, 0xFF000000);
		fluidBrass = newFluid(BaseMetals.MODID, "brass", 2000, 10000, 330, 10, 0xFF000000);
		fluidBronze = newFluid(BaseMetals.MODID, "bronze", 2000, 10000, 330, 10, 0xFF000000);
		fluidColdIron = newFluid(BaseMetals.MODID, "coldiron", 2000, 10000, 330, 10, 0xFF000000);
		fluidCopper = newFluid(BaseMetals.MODID, "copper", 2000, 10000, 330, 10, 0xFF000000);
		fluidCupronickel = newFluid(BaseMetals.MODID, "cupronickel", 2000, 10000, 330, 10, 0xFF000000);
		fluidElectrum = newFluid(BaseMetals.MODID, "electrum", 2000, 10000, 330, 10, 0xFF000000);
		fluidInvar = newFluid(BaseMetals.MODID, "invar", 2000, 10000, 330, 10, 0xFF000000);
		fluidLead = newFluid(BaseMetals.MODID, "lead", 2000, 10000, 330, 10, 0xFF000000);

		fluidMercury = newFluid(BaseMetals.MODID, "mercury", 13594, 2000, 300, 0, 0xFFD8D8D8);
		
		fluidMithril = newFluid(BaseMetals.MODID, "mithril", 2000, 10000, 330, 10, 0xFF000000);
		fluidNickel = newFluid(BaseMetals.MODID, "nickel", 2000, 10000, 330, 10, 0xFF000000);
		fluidPewter = newFluid(BaseMetals.MODID, "pewter", 2000, 10000, 330, 10, 0xFF000000);
		fluidPlatinum = newFluid(BaseMetals.MODID, "platinum", 2000, 10000, 330, 10, 0xFF000000);
		fluidSilver = newFluid(BaseMetals.MODID, "silver", 2000, 10000, 330, 10, 0xFF000000);
		fluidStarSteel = newFluid(BaseMetals.MODID, "starsteel", 2000, 10000, 330, 10, 0xFF000000);
		fluidSteel = newFluid(BaseMetals.MODID, "steel", 2000, 10000, 330, 10, 0xFF000000);
		fluidTin = newFluid(BaseMetals.MODID, "tin", 2000, 10000, 330, 10, 0xFF000000);
		fluidZinc = newFluid(BaseMetals.MODID, "zinc", 2000, 10000, 330, 10, 0xFF000000);

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

		fluidBlockMercury = registerFluidBlock(fluidMercury, new InteractiveFluidBlock(
				fluidMercury, false, (World w, EntityLivingBase e)->{
					if(w.rand.nextInt(32) == 0) e.addPotionEffect(new PotionEffect(Potion.REGISTRY.getObject(dizzyPotionKey), 30 * 20, 2));
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

	/**
	 *
	 * @param modID
	 */
	@SideOnly(Side.CLIENT)
	public static void bakeModels(String modID) {
		for(final Fluid fluid : fluidBlocks.keySet()) {
			final BlockFluidBase block = fluidBlocks.get(fluid);
			final Item item = Item.getItemFromBlock(block);
			final ModelResourceLocation fluidModelLocation = new ModelResourceLocation(
					modID.toLowerCase() + ":" + fluidBlockNames.get(block), "fluid");
			ModelBakery.registerItemVariants(item);
			ModelLoader.setCustomMeshDefinition(item, stack -> fluidModelLocation);
			ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
				@Override
				protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
					return fluidModelLocation;
				}
			});
		}
	}

	private static Fluid newFluid(String modID, String name, int density, int viscosity, int temperature, int luminosity, int tintColor) {
		Fluid fluid = new CustomFluid(name, new ResourceLocation(modID + ":blocks/" + name + "_still"), new ResourceLocation(modID + ":blocks/" + name + "_flow"), tintColor);
		fluid.setDensity(density);
		fluid.setViscosity(viscosity);
		fluid.setTemperature(temperature);
		fluid.setLuminosity(luminosity);
		fluid.setUnlocalizedName(modID + "." + name);
		FluidRegistry.registerFluid(fluid);
		FluidRegistry.addBucketForFluid(fluid);
		return fluid;
	}

	private static BlockFluidBase registerFluidBlock(Fluid fluid, BlockFluidBase block, String name) {
		ResourceLocation location = new ResourceLocation(BaseMetals.MODID, name);
		block.setRegistryName(location);
		block.setUnlocalizedName(location.toString());
		GameRegistry.register(block);
		block.setCreativeTab(CreativeTabs.MISC);

		final ItemBlock itemBlock = new ItemBlock(block);
		itemBlock.setRegistryName(location);
		itemBlock.setUnlocalizedName(location.toString());
		GameRegistry.register(itemBlock);

		fluidBlocks.put(fluid, block);
		fluidBlockNames.put(block, name);
		return block;
	}
}
