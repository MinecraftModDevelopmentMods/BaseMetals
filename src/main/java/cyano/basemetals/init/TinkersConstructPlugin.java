package cyano.basemetals.init;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.init.Fluids;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import slimeknights.tconstruct.TinkerIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.traits.ITrait;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class TinkersConstructPlugin {

	private static final String TCONSTRUCT_MODID = "tconstruct";
	private static HashMap<String, MaterialCorrelation> correlation = new HashMap<>();
	private static boolean initDone = false;

//	private static List<MaterialIntegration> integrateList = Lists.newArrayList(); // List of materials needed to be integrated

	/**
	 *
	 */
	@SuppressWarnings("unused")
	public static void init() {
		if (initDone)
			return;

		if (Loader.isModLoaded(TCONSTRUCT_MODID)) {
			final Material adamantine = createTCMaterial("adamantine");
			final Material antimony = createTCMaterial("antimony");
			final Material aquarium = createTCMaterial("aquarium");
			final Material bismuth = createTCMaterial("bismuth");
			final Material brass = createTCMaterial("brass");
			// final Material bronze = createTCMaterial("bronze");
			final Material coldiron = createTCMaterial("coldiron");
			// final Material copper = createTCMaterial("copper");
			final Material cupronickel = createTCMaterial("cupronickel");
			// final Material electrum = createTCMaterial("electrum");
			final Material invar = createTCMaterial("invar");
			// final Material lead = createTCMaterial("lead");
//			final Material mercury = createTCMaterial("mercury"); // Crashes
			final Material mithril = createTCMaterial("mithril");
//			final Material nickel = createTCMaterial("nickel");
			final Material pewter = createTCMaterial("pewter");
			final Material platinum = createTCMaterial("platinum");
			// final Material silver = createTCMaterial("silver");
			final Material starsteel = createTCMaterial("starsteel");
			// final Material steel = createTCMaterial("steel");
			final Material tin = createTCMaterial("tin");
			final Material zinc = createTCMaterial("zinc");

			for(String name : correlation.keySet()) {
				if (null != correlation.get(name).getMeltFluid()) {
				// skip items with no declared melt fluid - eg Aluminum, Aluminum Brass
				// 		also skips items where the melt fluid process went awol, which is nice.
					setupTConSmeltAndParts(name);
				}
			}

			// registerAlloy("aluminumbrass", 2, new String[] { "aluminum", "brass" }, new int[] { 1, 1 }); // TCon already has Aluminum Brass alloy
			// registerAlloy("galvanizedsteel", 2, new String[] { "steel", "zinc" }, new int[] { 1, 1 });
			// registerAlloy("nichrome", 2, new String[] { "nickel", "chrome" }, new int[] { 1, 1 });
			// registerAlloy("stainlesssteel", 2, new String[] { "steel", "chrome" }, new int[] { 1, 1 });
			// registerAlloy("titanium", 2, new String[] { "rutile", "magnesium" }, new int[] { 1, 1 });
		}

		initDone = true;
	}

	/**
	 * Factory method to convert MetalMaterial statistics to Tinkers Construct statistics and create the TCon material. 
	 * @param name Material identifier
	 */
	protected static void setupTConSmeltAndParts(final String name) {
		final MaterialCorrelation metal = correlation.get(name);

		FMLLog.severe("correlation found for %s", name);
		registerFluid(metal.getMeltFluid(), true);
		metal.getMetal().getBaseAttackDamage();

		registerTinkerMaterial(metal.getMaterial(),
				// Craft at workbench: always false, craft at smeltery: always true
				false, true);
	}

	/**
	 * Creates a Tinkers Construct {@link slimeknights.tconstruct.library.materials.Material} and associates it with the link cyano.basemetals.Material.MetalMaterial and fluid net.minecraftforge.fluids.Fluid it goes with. This association is saved in the {@link #correlation} map so later we can just loop over the correlation for stuff. 
	 * @param name Material identifier
	 * @return
	 */
	protected static Material createTCMaterial(String name) {
		MetalMaterial bmmaterial = Materials.getMaterialByName(name);
		Fluid fluid = Fluids.getFluidByName(name);

		int tintColor = 0xFF000000;
		if (bmmaterial != null) {
			tintColor = bmmaterial.getTintColor();
		} else {
			tintColor = 0xFFD8D8D8; // Hack for Mercury as it doesn't have a metalMaterial
		}

		final Material tcmaterial = new Material(name, tintColor);
		correlation.put(name, new MaterialCorrelation(tcmaterial, bmmaterial, fluid));

		return tcmaterial;
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
			message.setString("ore", StringUtils.capitalize(fluid.getName()));
			message.setBoolean("toolforge", toolforge);
			// message.setTag("alloy", alloysTagList); // you can also send an alloy with the registration (see below)

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

			// if you have access to the fluid-instance you can also do FluidStack.writeToNBT
			NBTTagCompound fluid = new NBTTagCompound();
			fluid.setString("FluidName", outputName);
			fluid.setInteger("Amount", outputQty); // 144 = 1 ingot, 16 = 1 nugget
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

	protected static void registerTinkerMaterial(Material material, boolean craftable, boolean castable) {
		registerTinkerMaterial(material, craftable, castable, null);
	}

	protected static void registerTinkerMaterial(Material tcmaterial, boolean craftable, boolean castable, ITrait trait) {

		MetalMaterial material = Materials.getMaterialByName(tcmaterial.identifier);
		Fluid fluid = Fluids.getFluidByName(tcmaterial.identifier);

		HeadMaterialStats headStats = new HeadMaterialStats(material.getToolDurability(),
															material.magicAffinity * 3 / 2,
															material.getBaseAttackDamage() * 2,
															material.getToolHarvestLevel());

		HandleMaterialStats handleStats = new HandleMaterialStats((int) (material.hardness + material.magicAffinity * 2) / 9,
																  (int) (material.getToolDurability() / 6.8));

		ExtraMaterialStats extraStats = new ExtraMaterialStats((material.getToolDurability() / 10));

		TinkerRegistry.addMaterialStats(tcmaterial, headStats); // Sets stats for head
		TinkerRegistry.addMaterialStats(tcmaterial, handleStats); // Sets Stats for handle
		TinkerRegistry.addMaterialStats(tcmaterial, extraStats); // Sets stats for everything else

		if (trait != null) {
			String stats = "temporary placeholder"; // TODO: find out what goes here
			TinkerRegistry.addMaterialTrait(tcmaterial, trait, stats);
		}

		// Set fluid used, Set whether craftable, set whether castable, adds the
		// item with the value 144.
		tcmaterial.setFluid(fluid);
		tcmaterial.setCraftable(craftable);
		tcmaterial.setCastable(castable);
		tcmaterial.addItem(material.nugget, 1, Material.VALUE_Nugget);
		tcmaterial.addItem(material.ingot, 1, Material.VALUE_Ingot);
		tcmaterial.setRepresentativeItem(material.ingot); // Use this as the picture

//		TinkerIntegration.integrate(tcmaterial);
		TinkerIntegration.integrate(tcmaterial, fluid, StringUtils.capitalize(fluid.getName()));
	}
}