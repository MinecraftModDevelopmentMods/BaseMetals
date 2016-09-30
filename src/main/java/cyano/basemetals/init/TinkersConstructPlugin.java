package cyano.basemetals.init;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.init.Fluids;
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
import slimeknights.tconstruct.library.traits.ITrait;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class TinkersConstructPlugin {

	private static final String TCONSTRUCT_MODID = "tconstruct";
	private static HashMap<String, MaterialCorrelation> correlation = new HashMap<String, MaterialCorrelation>();
	private static boolean initDone = false;

	private static List<MaterialIntegration> integrateList = Lists.newArrayList(); // List of materials needed to be integrated

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

			final Material adamantine = createTCMaterial("adamantine", 0xFF000000, Fluids.fluidAdamantine);
			final Material antimony = createTCMaterial("antimony", 0xFF000000, Fluids.fluidAntimony);
			final Material aquarium = createTCMaterial("aqaurium", 0xFF000000, Fluids.fluidAquarium);
			final Material bismuth = createTCMaterial("bismuth", 0xFF000000, Fluids.fluidBismuth);
			final Material brass = createTCMaterial("brass", 0xFF000000, Fluids.fluidBrass);
			// final Material bronze = createTCMaterial("bronze", 0xFF000000, Fluids.fluidBronze);
			final Material coldiron = createTCMaterial("coldiron", 0xFF000000, Fluids.fluidColdIron);
			// final Material copper = createTCMaterial("copper", 0xFF000000, Fluids.fluidCopper);
			final Material cupronickel = createTCMaterial("cupronickel", 0xFF000000, Fluids.fluidCupronickel);
			// final Material electrum = createTCMaterial("electrum", 0xFF000000, Fluids.fluidElectrum);
			final Material invar = createTCMaterial("invar", 0xFF000000, Fluids.fluidInvar);
			// final Material lead = createTCMaterial("lead", 0xFF000000, Fluids.fluidLead);
			final Material mercury = createTCMaterial("mercury", 0xFF000000, Fluids.fluidMercury);
			final Material mithril = createTCMaterial("mithril", 0xFF000000, Fluids.fluidMithril);
			final Material nickel = createTCMaterial("nickel", 0xFF000000, Fluids.fluidNickel);
			final Material pewter = createTCMaterial("pewter", 0xFF000000, Fluids.fluidPewter);
			final Material platinum = createTCMaterial("platinum", 0xFF000000, Fluids.fluidPlatinum);
			// final Material silver = createTCMaterial("silver", 0xFF000000, Fluids.fluidSilver);
			final Material starsteel = createTCMaterial("starsteel", 0xFF000000, Fluids.fluidStarSteel);
			// final Material steel = createTCMaterial("steel", 0xFF000000, Fluids.fluidSteel);
			final Material tin = createTCMaterial("tin", 0xFF000000, Fluids.fluidTin);
			final Material zinc = createTCMaterial("zinc", 0xFF000000, Fluids.fluidZinc);

			for(String name : correlation.keySet()) {
				if (null != correlation.get(name).getMeltFluid()) {
				// skip items with no declared melt fluid - eg Aluminum, Aluminum Brass
				// 		also skips items where the melt fluid process went awol, which is nice.
					setupTConSmeltAndParts(name, d, s, a);
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
	 * @param durabilityFactor Overall scaling factor of toolpart durability
	 * @param speedFactor overall scaling factor of toolpart speed
	 * @param attackFactor overall scaling factor of toolpart damage
	 */
	private static void setupTConSmeltAndParts(final String name, double durabilityFactor, float speedFactor, float attackFactor) {
		final MaterialCorrelation metal = correlation.get(name);
		
		registerFluid(metal.getMeltFluid(), true);
		metal.getMetal().getBaseAttackDamage();
		
		registerTinkerMaterial(metal.getMaterial(), metal.getMeltFluid(), 
				(int) (metal.getMetal().getToolDurability()), 
				metal.getMetal().magicAffinity*3/2,
				metal.getMetal().getBaseAttackDamage()*4, 
				// Handle durability multiplier, handle durability
				(metal.getMetal().hardness+metal.getMetal().magicAffinity*2)/9, (int) (metal.getMetal().getToolDurability()/6.8), 
				// "Extra" - durability of non handle, non-head parts?
				(int) (metal.getMetal().getToolDurability()/10),
				// Mining level
				metal.getMetal().getToolHarvestLevel(),
				// Craft at workbench: always false, craft at smeltery: always true
				false, true);
	}

	/**
	 * Creates a Tinkers Construct {@link slimeknights.tconstruct.library.materials.Material} and associates it with the {@link cyano.basemetals.Material.MetalMaterial} and {@fluid net.minecraftforge.fluids.Fluid} it goes with. This association is saved in the {@link #correlation} map so later we can just loop over the correlation for stuff. 
	 * @param hexColor Color of the tool parts
	 * @param name Material identifier
	 * @param meltFluid fluid to be produced when this is melted in the smeltery
	 * @return
	 */
	private static Material createTCMaterial(String name, int hexColor, Fluid meltFluid) {
		final Material material = new Material(name,hexColor);
		correlation.put(name, new MaterialCorrelation(material, Materials.getMetalByName(name), meltFluid));
		
		return material;
	}

	/**
	 * Wrapper for {@link #createTCMaterial(String, int, Fluid) createTCMaterial(String name, int hexColor, Fluid meltFluid)} method, for when there is no fluid needed
	 * Creates a Tinkers Construct {@link slimeknights.tconstruct.library.materials.Material} and associates it with the {@link cyano.basemetals.Material.MetalMaterial} it goes with. This association is saved in the {@link #correlation} map so later we can just loop over the correlation for stuff. 
	 *
	 * @param hexColor Color of the tool parts
	 * @param name Material identifier
	 * @return
	 */
	private static Material createTCMaterial(String name, int hexColor) {
		return createTCMaterial(name, hexColor, null);
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

	private static void registerTinkerMaterial(Material material, Fluid fluid, int headDura, float headSpeed, float headAttack, float handleMod, int handleDura, int extra, int headLevel, boolean craft, boolean cast) {
		registerTinkerMaterial(material, fluid, headDura, headSpeed, headAttack, handleMod, handleDura, extra, headLevel, craft, cast, null);
	}

	private static void registerTinkerMaterial(Material material, Fluid fluid, int headDura, float headSpeed, float headAttack, float handleMod, int handleDura, int extra, int headLevel, boolean craft, boolean cast, ITrait trait) {
		final List<MaterialIntegration> integrateList = Lists.newArrayList(); // List of materials needed to be integrated

		TinkerRegistry.addMaterialStats(material, new HeadMaterialStats(headDura, headSpeed, headAttack, headLevel)); // Sets stats for head
		TinkerRegistry.addMaterialStats(material, new HandleMaterialStats(handleMod, handleDura)); // Sets Stats for handle
		TinkerRegistry.addMaterialStats(material, new ExtraMaterialStats(extra)); // Sets stats for everything else
		if (trait != null) {
			String stats = "temporary placeholder"; // TODO: find out what goes here
			TinkerRegistry.addMaterialTrait(material, trait, stats);
		}

		final Item item = Items.getItemByName(material.identifier + "_ingot"); // Why do we need to get an item here?

		// Set fluid used, Set whether craftable, set whether castable, adds the
		// item with the value 144.
		material.setFluid(fluid).setCraftable(craft).setCastable(cast).addItem(item, 1, Material.VALUE_Ingot);
		material.setRepresentativeItem(item); // Uses item as the picture?

		// Probably don't need this
		// proxy.setRenderInfo(material);
		final MaterialIntegration integration = new MaterialIntegration(material, fluid, StringUtils.capitalize(fluid.getName()));
		integration.integrate();
		integrateList.add(integration);
	}
}