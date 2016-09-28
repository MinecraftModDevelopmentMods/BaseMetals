package cyano.basemetals.init;

import java.util.List;

import com.google.common.collect.Lists;

import cyano.basemetals.utils.StringUtilities;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import slimeknights.tconstruct.library.MaterialIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class TinkersConstructPlugin {

	private static final String TCONSTRUCT_MODID = "tconstruct";

	private static boolean initDone = false;

	/**
	 *
	 */
	@SuppressWarnings("unused")
	public static void init() {
		if (initDone)
			return;

		if (Loader.isModLoaded(TCONSTRUCT_MODID)) {
			final double d = 0; // durabilityFactorGeneral;
			// System.out.println("DURABILITY FACTOR" + d);
			final float s = 0;// (float) speedFactorGeneral;
			// System.out.println("SPEED FACTOR" + s);
			final float a = 0; // (float) attackFactorGeneral;
			// System.out.println("ATTACK FACTOR" + a);

			final Material adamantine = new Material("adamantine", 0xFF000000);
			final Material antimony = new Material("antimony", 0xFF000000);
			final Material aquarium = new Material("aqaurium", 0xFF000000);
			final Material bismuth = new Material("bismuth", 0xFF000000);
			final Material brass = new Material("brass", 0xFF000000);
			// final Material bronze = new Material("bronze", 0xFF000000);
			final Material coldiron = new Material("coldiron", 0xFF000000);
			// final Material copper = new Material("copper", 0xFF000000);
			final Material cupronickel = new Material("cupronickel", 0xFF000000);
			// final Material electrum = new Material("electrum", 0xFF000000);
			final Material invar = new Material("invar", 0xFF000000);
			// final Material lead = new Material("lead", 0xFF000000);
			final Material mercury = new Material("mercury", 0xFF000000);
			final Material mithril = new Material("mithril", 0xFF000000);
			final Material nickel = new Material("nickel", 0xFF000000);
			final Material pewter = new Material("pewter", 0xFF000000);
			final Material platinum = new Material("platinum", 0xFF000000);
			// final Material silver = new Material("silver", 0xFF000000);
			final Material starsteel = new Material("starsteel", 0xFF000000);
			// final Material steel = new Material("steel", 0xFF000000);
			final Material tin = new Material("tin", 0xFF000000);
			final Material zinc = new Material("zinc", 0xFF000000);

			registerFluid(Fluids.fluidAdamantine, true);
			// registerTinkerMaterial(adamantine, Fluids.fluidAdamantine, (int)
			// (235 * d), 5.33f * s, 3.80f * a, 1.15f, 17, 117, 1, false, true);
			registerFluid(Fluids.fluidAntimony, true);
			registerTinkerMaterial(antimony, Fluids.fluidAntimony, (int) (235 * d), 5.33f * s, 3.80f * a, 1.15f, 17,
					117, 1, false, true);
			registerFluid(Fluids.fluidAquarium, true);
			// registerTinkerMaterial(aquarium, Fluids.fluidAquarium, (int) (235
			// * d), 5.33f * s, 3.80f * a, 1.15f, 17, 117, 1, false, true);
			registerFluid(Fluids.fluidBismuth, true);
			registerTinkerMaterial(bismuth, Fluids.fluidBismuth, (int) (235 * d), 5.33f * s, 3.80f * a, 1.15f, 17, 117,
					1, false, true);
			registerFluid(Fluids.fluidBrass, true);
			registerTinkerMaterial(brass, Fluids.fluidBrass, (int) (235 * d), 5.33f * s, 3.80f * a, 1.15f, 17, 117, 1,
					false, true);
			// registerFluid(Fluids.fluidBronze, true);
			// registerTinkerMaterial(bronze, Fluids.fluidBronze, (int) (235 *
			// d), 5.33f * s, 3.80f * a, 1.15f, 17, 117, 1, false, true);
			registerFluid(Fluids.fluidColdIron, true);
			registerTinkerMaterial(coldiron, Fluids.fluidColdIron, (int) (235 * d), 5.33f * s, 3.80f * a, 1.15f, 17,
					117, 1, false, true);
			// registerFluid(Fluids.fluidCopper, true);
			// registerTinkerMaterial(copper, Fluids.fluidCopper, (int) (235 *
			// d), 5.33f * s, 3.80f * a, 1.15f, 17, 117, 1, false, true);
			registerFluid(Fluids.fluidCupronickel, true);
			registerTinkerMaterial(cupronickel, Fluids.fluidCupronickel, (int) (235 * d), 5.33f * s, 3.80f * a, 1.15f,
					17, 117, 1, false, true);
			// registerFluid(Fluids.fluidElectrum, true);
			// registerTinkerMaterial(electrum, Fluids.fluidElectrum, (int) (235
			// * d), 5.33f * s, 3.80f * a, 1.15f, 17, 117, 1, false, true);
			registerFluid(Fluids.fluidInvar, true);
			registerTinkerMaterial(invar, Fluids.fluidInvar, (int) (235 * d), 5.33f * s, 3.80f * a, 1.15f, 17, 117, 1,
					false, true);
			// registerFluid(Fluids.fluidLead, true);
			// registerTinkerMaterial(lead, Fluids.fluidLead, (int) (235 * d),
			// 5.33f * s, 3.80f * a, 1.15f, 17, 117, 1, false, true);
			registerFluid(Fluids.fluidMercury, true);
			registerTinkerMaterial(mercury, Fluids.fluidMercury, (int) (235 * d), 5.33f * s, 3.80f * a, 1.15f, 17, 117,
					1, false, true);
			registerFluid(Fluids.fluidMithril, true);
			registerTinkerMaterial(mithril, Fluids.fluidMithril, (int) (235 * d), 5.33f * s, 3.80f * a, 1.15f, 17, 117,
					1, false, true);
			registerFluid(Fluids.fluidNickel, true);
			registerTinkerMaterial(nickel, Fluids.fluidNickel, (int) (235 * d), 5.33f * s, 3.80f * a, 1.15f, 17, 117, 1,
					false, true);
			registerFluid(Fluids.fluidPewter, true);
			registerTinkerMaterial(pewter, Fluids.fluidPewter, (int) (235 * d), 5.33f * s, 3.80f * a, 1.15f, 17, 117, 1,
					false, true);
			registerFluid(Fluids.fluidPlatinum, true);
			registerTinkerMaterial(platinum, Fluids.fluidPlatinum, (int) (235 * d), 5.33f * s, 3.80f * a, 1.15f, 17,
					117, 1, false, true);
			// registerFluid(Fluids.fluidSilver, true);
			// registerTinkerMaterial(silver, Fluids.fluidSilver, (int) (235 *
			// d), 5.33f * s, 3.80f * a, 1.15f, 17, 117, 1, false, true);
			registerFluid(Fluids.fluidStarSteel, true);
			registerTinkerMaterial(starsteel, Fluids.fluidStarSteel, (int) (235 * d), 5.33f * s, 3.80f * a, 1.15f, 17,
					117, 1, false, true);
			// registerFluid(Fluids.fluidSteel, true);
			// registerTinkerMaterial(steel, Fluids.fluidSteel, (int) (235 * d),
			// 5.33f * s, 3.80f * a, 1.15f, 17, 117, 1, false, true);
			registerFluid(Fluids.fluidTin, true);
			registerTinkerMaterial(tin, Fluids.fluidTin, (int) (235 * d), 5.33f * s, 3.80f * a, 1.15f, 17, 117, 1,
					false, true);
			registerFluid(Fluids.fluidZinc, true);
			registerTinkerMaterial(zinc, Fluids.fluidZinc, (int) (235 * d), 5.33f * s, 3.80f * a, 1.15f, 17, 117, 1,
					false, true);

			// registerAlloy("aluminumbrass", 2, new String[] { "aluminum", 1,
			// "brass" }, new int[] { 1, 1 }); // TCon already has Aluminum
			// Brass alloy
			// registerAlloy("galvanizedsteel", 2, new String[] { "steel",
			// "zinc" }, new int[] { 1, 1 });
			// registerAlloy("nichrome", 2, new String[] { "nickel", "chrome" },
			// new int[] { 1, 1 });
			// registerAlloy("stainlesssteel", 2, new String[] { "steel",
			// "chrome" }, new int[] { 1, 1 });
			// registerAlloy("titanium", 2, new String[] { "rutile", "magnesium"
			// }, new int[] { 1, 1 });
		}

		initDone = true;
	}

	/**
	 *
	 * @param fluid
	 * @param toolforge
	 */
	public static void registerFluid(Fluid fluid, boolean toolforge) {
		if (Loader.isModLoaded(TCONSTRUCT_MODID)) {
			final NBTTagCompound message = new NBTTagCompound();
			message.setString("fluid", fluid.getName());
			message.setString("ore", StringUtilities.upperCaseFirst(fluid.getName()));
			message.setBoolean("toolforge", toolforge);
			// message.setTag("alloy", alloysTagList); // you can also send an
			// alloy with the registration (see below)

			// send the NBT to TCon
			FMLInterModComms.sendMessage(TCONSTRUCT_MODID, "integrateSmeltery", message);
		}
	}

	/**
	 *
	 * @param outputName
	 * @param outputQty
	 * @param inputName
	 * @param inputQty
	 */
	public static void registerAlloy(String outputName, int outputQty, String[] inputName, int[] inputQty) {
		if (Loader.isModLoaded(TCONSTRUCT_MODID)) {
			if (inputName.length != inputQty.length)
				throw new RuntimeException("Alloy must have the same amount of inputName and intQty");

			final NBTTagList tagList = new NBTTagList();

			// if you have access to the fluid-instance you can also do
			// FluidStack.writeToNBT
			NBTTagCompound fluid = new NBTTagCompound();
			fluid.setString("FluidName", outputName);
			fluid.setInteger("Amount", outputQty); // 144 = 1 ingot, 16 = 1
													// nugget
			tagList.appendTag(fluid);

			// alloy fluid
			for (int i = 0; i < inputName.length; i++) {
				fluid = new NBTTagCompound();
				fluid.setString("FluidName", inputName[i]);
				fluid.setInteger("Amount", inputQty[i]);
				tagList.appendTag(fluid);
			}

			final NBTTagCompound message = new NBTTagCompound();
			message.setTag("alloy", tagList);
			FMLInterModComms.sendMessage(TCONSTRUCT_MODID, "alloy", message);
		}
	}

	private static void registerTinkerMaterial(Material material, Fluid fluid, int headDura, float headSpeed,
			float headAttack, float handleMod, int handleDura, int extra, int headLevel, boolean craft, boolean cast) {

		final List<MaterialIntegration> integrateList = Lists.newArrayList(); // List
																				// of
																				// materials
																				// needed
																				// to
																				// be
																				// integrated

		TinkerRegistry.addMaterialStats(material, new HeadMaterialStats(headDura, headSpeed, headAttack, headLevel)); // Sets
																														// stats
																														// for
																														// head
		TinkerRegistry.addMaterialStats(material, new HandleMaterialStats(handleMod, handleDura)); // Sets
																									// Stats
																									// for
																									// handle
		TinkerRegistry.addMaterialStats(material, new ExtraMaterialStats(extra)); // Sets
																					// stats
																					// for
																					// everything
																					// else

		final Item item = Items.getItemByName(material.identifier + "_ingot"); // Why
																				// do
																				// we
																				// need
																				// to
																				// get
																				// an
																				// item
																				// here?

		// Set fluid used, Set whether craftable, set whether castable, adds the
		// item with the value 144.
		material.setFluid(fluid).setCraftable(craft).setCastable(cast).addItem(item, 1, Material.VALUE_Ingot);
		material.setRepresentativeItem(item); // Uses item as the picture?

		// Probably don't need this
		// proxy.setRenderInfo(material);
		final MaterialIntegration integration = new MaterialIntegration(material, fluid,
				StringUtilities.upperCaseFirst(fluid.getName()));
		integration.integrate();
		integrateList.add(integration);
	}
}