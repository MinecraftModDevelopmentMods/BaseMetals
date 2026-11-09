package zone.moddev.mc.basemetals.content;

import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.data.MaterialNames;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

/** Defines which material forms can be made in each content mode. */
public final class ContentPolicy {
	private static final ContentPolicy HIGH = new ContentPolicy(ContentMode.HIGH_FANTASY);
	private static final ContentPolicy LOW = new ContentPolicy(ContentMode.LOW_FANTASY);
	private static final Set<String> MATERIALS = set(
			MaterialNames.ADAMANTINE, MaterialNames.ANTIMONY, MaterialNames.AQUARIUM,
			MaterialNames.BISMUTH, MaterialNames.BRASS, MaterialNames.BRONZE,
			MaterialNames.CHARCOAL, MaterialNames.COAL, MaterialNames.COLDIRON,
			MaterialNames.COPPER, MaterialNames.CUPRONICKEL, MaterialNames.DIAMOND,
			MaterialNames.ELECTRUM, MaterialNames.EMERALD, MaterialNames.ENDER,
			MaterialNames.GOLD, MaterialNames.INVAR, MaterialNames.IRON,
			MaterialNames.LAPIS, MaterialNames.LEAD, MaterialNames.MERCURY,
			MaterialNames.MITHRIL, MaterialNames.NICKEL, MaterialNames.OBSIDIAN,
			MaterialNames.PEWTER, MaterialNames.PLATINUM, MaterialNames.PRISMARINE,
			MaterialNames.QUARTZ, MaterialNames.REDSTONE, MaterialNames.SILVER,
			MaterialNames.STARSTEEL, MaterialNames.STEEL, MaterialNames.STONE,
			MaterialNames.TIN, MaterialNames.WOOD, MaterialNames.ZINC);

	private static final Set<String> FULL_TOOLS = set(MaterialNames.ADAMANTINE,
			MaterialNames.AQUARIUM, MaterialNames.BRONZE, MaterialNames.COLDIRON,
			MaterialNames.COPPER, MaterialNames.CUPRONICKEL, MaterialNames.INVAR,
			MaterialNames.MITHRIL, MaterialNames.NICKEL, MaterialNames.STARSTEEL,
			MaterialNames.STEEL, MaterialNames.IRON);

	private static final Set<String> ARMOR = set(MaterialNames.ADAMANTINE,
			MaterialNames.AQUARIUM, MaterialNames.BRASS, MaterialNames.BRONZE,
			MaterialNames.COLDIRON, MaterialNames.COPPER, MaterialNames.CUPRONICKEL,
			MaterialNames.ELECTRUM, MaterialNames.INVAR, MaterialNames.MITHRIL,
			MaterialNames.NICKEL, MaterialNames.PLATINUM, MaterialNames.SILVER,
			MaterialNames.STARSTEEL, MaterialNames.STEEL);
	private static final Set<String> PROTECTIVE = union(ARMOR,
			set(MaterialNames.IRON, MaterialNames.GOLD, MaterialNames.DIAMOND));

	private static final Set<String> AMMUNITION = union(FULL_TOOLS,
			set(MaterialNames.SILVER, MaterialNames.OBSIDIAN, MaterialNames.DIAMOND));

	private static final Set<String> GEARS = set(MaterialNames.ADAMANTINE,
			MaterialNames.AQUARIUM, MaterialNames.BRASS, MaterialNames.BRONZE,
			MaterialNames.COLDIRON, MaterialNames.COPPER, MaterialNames.CUPRONICKEL,
			MaterialNames.INVAR, MaterialNames.MITHRIL, MaterialNames.NICKEL,
			MaterialNames.STARSTEEL, MaterialNames.STEEL, MaterialNames.IRON,
			MaterialNames.GOLD, MaterialNames.WOOD);

	private static final Set<MaterialForm> TOOLS = Collections.unmodifiableSet(
			new LinkedHashSet<>(Arrays.asList(MaterialForm.AXE, MaterialForm.CRACKHAMMER,
					MaterialForm.HOE, MaterialForm.PICKAXE, MaterialForm.SCYTHE,
					MaterialForm.SHEARS, MaterialForm.SHOVEL, MaterialForm.SWORD)));
	private static final Set<MaterialForm> ARMOR_FORMS = Collections.unmodifiableSet(
			new LinkedHashSet<>(Arrays.asList(MaterialForm.BOOTS, MaterialForm.CHESTPLATE,
					MaterialForm.HELMET, MaterialForm.LEGGINGS, MaterialForm.HORSE_ARMOR,
					MaterialForm.SHIELD)));
	private static final Set<MaterialForm> BUILDING_FORMS = Collections.unmodifiableSet(
			new LinkedHashSet<>(Arrays.asList(MaterialForm.ANVIL, MaterialForm.BARS,
					MaterialForm.BLOCK, MaterialForm.BOOKSHELF, MaterialForm.BUTTON,
					MaterialForm.DOOR, MaterialForm.DOUBLE_SLAB, MaterialForm.FENCE,
					MaterialForm.FENCE_GATE, MaterialForm.FLOWER_POT, MaterialForm.LADDER,
					MaterialForm.LEVER, MaterialForm.PLATE, MaterialForm.PRESSURE_PLATE,
					MaterialForm.SLAB, MaterialForm.STAIRS, MaterialForm.TRAPDOOR,
					MaterialForm.TRIPWIRE_HOOK, MaterialForm.WALL)));

	private final ContentMode mode;

	private ContentPolicy(final ContentMode mode) {
		this.mode = mode;
	}

	public static ContentPolicy forMode(final ContentMode mode) {
		return mode == ContentMode.LOW_FANTASY ? LOW : HIGH;
	}

	public static ContentPolicy active() {
		return forMode(zone.moddev.mc.basemetals.util.Config.getActiveContentMode());
	}

	public ContentMode mode() {
		return mode;
	}

	public boolean allowsToolParts(final String material) {
		return TOOLS.stream().anyMatch(form -> allows(material, form));
	}

	public boolean allows(final String material, final MaterialForm form) {
		final String name = normalize(material);

		if (mode == ContentMode.HIGH_FANTASY || material == null || form == null) {
			return true;
		}

		if (form == MaterialForm.OTHER) {
			return !MaterialNames.MERCURY.equals(name);
		}

		if (form == MaterialForm.BOW || form == MaterialForm.CROSSBOW
				|| form == MaterialForm.FISHING_ROD) {
			return false;
		}

		if (TOOLS.contains(form)) {
			return allowsTool(name, form);
		}

		if (ARMOR_FORMS.contains(form)) {
			return PROTECTIVE.contains(name);
		}

		if (form == MaterialForm.ARROW || form == MaterialForm.BOLT) {
			return AMMUNITION.contains(name);
		}

		if (form == MaterialForm.GEAR) {
			return GEARS.contains(name);
		}

		if (form == MaterialForm.ANVIL) {
			return MaterialNames.STONE.equals(name) || MaterialNames.STEEL.equals(name)
					|| MaterialNames.ADAMANTINE.equals(name);
		}

		if (MaterialNames.MERCURY.equals(name)) {
			return form == MaterialForm.ORE || form == MaterialForm.POWDER
					|| form == MaterialForm.SMALLPOWDER || form == MaterialForm.FLUID
					|| form == MaterialForm.INGOT;
		}

		if (BUILDING_FORMS.contains(form)) {
			return true;
		}

		return true;
	}

	public boolean allows(final ItemStack stack) {
		if (stack == null || stack.getItem() == null) {
			return true;
		}

		return allows(stack.getItem().getRegistryName());
	}

	public boolean allows(final ResourceLocation id) {
		if (id == null || !BaseMetals.MODID.equals(id.getResourceDomain())) {
			return true;
		}

		final String material = materialFromPath(id.getResourcePath());

		if (material == null) {
			return true;
		}

		MaterialForm form = MaterialForm.fromRegistryPath(id.getResourcePath());

		if (form == MaterialForm.OTHER && id.getResourcePath().equals(material)) {
			form = MaterialForm.FLUID;
		}

		return allows(material, form);
	}


	public static String materialFromPath(final String registryPath) {
		if (registryPath == null) {
			return null;
		}

		final String path = registryPath.toLowerCase(Locale.ROOT);

		for (final String material : MATERIALS) {
			if (path.equals(material) || path.startsWith(material + "_")
					|| path.startsWith("double_" + material + "_")) {
				return material;
			}
		}

		return null;
	}

	private boolean allowsTool(final String material, final MaterialForm form) {
		if (FULL_TOOLS.contains(material)) {
			return true;
		}

		if (MaterialNames.SILVER.equals(material)) {
			return form == MaterialForm.SWORD;
		}

		if (MaterialNames.OBSIDIAN.equals(material)) {
			return form == MaterialForm.AXE || form == MaterialForm.SWORD
					|| form == MaterialForm.SCYTHE;
		}

		if (MaterialNames.DIAMOND.equals(material)) {
			return form == MaterialForm.CRACKHAMMER || form == MaterialForm.SCYTHE
					|| form == MaterialForm.SHEARS;
		}

		if (MaterialNames.STONE.equals(material)) {
			return form == MaterialForm.CRACKHAMMER || form == MaterialForm.SCYTHE;
		}

		return MaterialNames.WOOD.equals(material) && form == MaterialForm.CRACKHAMMER;
	}

	private static String normalize(final String material) {
		return material == null ? "" : material.trim().toLowerCase(Locale.ROOT);
	}

	private static Set<String> set(final String... values) {
		return Collections.unmodifiableSet(new LinkedHashSet<>(Arrays.asList(values)));
	}

	private static Set<String> union(final Set<String> first, final Set<String> second) {
		final Set<String> values = new LinkedHashSet<>(first);

		values.addAll(second);

		return Collections.unmodifiableSet(values);
	}
}
