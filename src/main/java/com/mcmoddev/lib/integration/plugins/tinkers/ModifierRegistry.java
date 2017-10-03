package com.mcmoddev.lib.integration.plugins.tinkers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.integration.plugins.tinkers.modifiers.*;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.modifiers.Modifier;

/**
 * Contains static instances of modifiers for registering with TiC
 * 
 * @author Daniel Hazelton &lt;dshadowwolf@gmail.com&gt;
 *
 */

public class ModifierRegistry {

	private static final Map<String, Modifier> modifiers = new HashMap<>();

	private ModifierRegistry() {
		throw new IllegalAccessError("Not an instantiable class");
	}

	public static void setModifierRecipe(@Nonnull final String name, @Nonnull final ItemStack... ingredients) {
		final Modifier t = modifiers.get(name);
		if (t == null) {
			BaseMetals.logger.error("Trying to add a recipe to unknown modifier %s, ignoring.", name);
			return;
		}

		t.addRecipeMatch(new RecipeMatch.ItemCombination(1, ingredients));
		modifiers.put(name, t);
	}

	public static void setModifierItem(@Nonnull final String name, @Nonnull final ItemStack item) {
		setModifierItem(name, item.getItem());
	}

	public static void setModifierItem(@Nonnull final String name, @Nonnull final Item item) {
		final Modifier t = modifiers.get(name);
		if (t == null) {
			BaseMetals.logger.error("Trying to add an item to unknown modifier %s, ignoring.", name);
			return;
		}
		
		t.addItem(item);
		modifiers.put(name, t);
	}

	public static void initModifiers() {
		modifiers.put("toxic", new ModifierToxic());
		modifiers.put("plated", new ModifierLeadPlated());
		modifiers.put("fake-diamond", new ModifierFakeDiamond());
	}

	public static void registerModifiers() {
		for (final Entry<String, Modifier> ent : modifiers.entrySet()) {
			if( TinkerRegistry.getModifier(ent.getValue().getIdentifier()) == null ) {
				TinkerRegistry.registerModifier(ent.getValue());
				TinkerRegistry.registerModifierAlias(ent.getValue(),ent.getKey());
			}
		}
	}

	public static Map<String, String> getModifierDetails(@Nonnull final String name) {
		final Map<String, String> rv = new HashMap<>();
		if (modifiers.containsKey(name)) {
			Modifier t = modifiers.get(name);
			rv.put("name", t.getLocalizedName());
			rv.put("desc", t.getLocalizedDesc());
			return rv;
		} else {
			return Collections.emptyMap();
		}
	}	
}
