package com.mcmoddev.lib.integration.plugins.tinkers.modifiers;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.modifiers.IModifierDisplay;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;

public class ModifierToxic extends ModifierTrait implements IModifierDisplay {

	public ModifierToxic() {
		super("toxic-modifier", 0xFFFFFF, 1, 0);
	}

	@Override
	public void afterHit(@Nonnull final ItemStack tool, @Nonnull final EntityLivingBase player, @Nonnull final EntityLivingBase target, @Nonnull final float damageDealt, @Nonnull final boolean wasCritical, @Nonnull final boolean wasHit) {
		if (wasHit && target.isEntityAlive()) {
			target.addPotionEffect(new PotionEffect(MobEffects.POISON, 50, 1));
			target.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 10));
		}
	}

	@Override
	public boolean isHidden() {
		return false;
	}
	
	  @Override
	  public List<List<ItemStack>> getItems() {
	    ImmutableList.Builder<List<ItemStack>> builder = ImmutableList.builder();

	    for(RecipeMatch rm : items) {
	      List<ItemStack> in = rm.getInputs();
	      if(!in.isEmpty()) {
	        builder.add(in);
	      }
	    }

	    return builder.build();
	}
}
