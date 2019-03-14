package com.mcmoddev.basemetals.properties;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class MithrilProperty extends BMEPropertyBase {
	@Override
	public void apply(ItemStack stack) {
		if(hasEffect(stack)) {
			// only apply if there is an effect
			// we should always double check here :)
		}
	}

    @Override
    public void apply(ItemStack stack, EntityPlayer player) {
	if(hasEffect(stack, player)) {
	    if (hasFullSuit(player, MaterialNames.MITHRIL)) {
		final List<Potion> removeList = new LinkedList<>(); // needed to avoid concurrent
		// modification error
		final Iterator<PotionEffect> effectIterator = player.getActivePotionEffects()
		    .iterator();
		while (effectIterator.hasNext()) {
		    final PotionEffect pe = effectIterator.next();
		    final Potion p = pe.getPotion();
		    if (p.isBadEffect()) {
			removeList.add(p);
		    }
		}
		for (final Potion p : removeList) {
		    player.removePotionEffect(p);
		}
	    }
	}
    }

	@Override
	public void apply(ItemStack stack, EntityLivingBase ent) {
		if(hasEffect(stack, ent) && ent instanceof EntityPlayer) {
			apply(stack, (EntityPlayer)ent);
		}
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		return false; // no effect for just the stack
	}

	@Override
	public boolean hasEffect(ItemStack stack, EntityPlayer player) {
		MMDMaterial mat = Materials.getMaterialByName(MaterialNames.MITHRIL);
		boolean rv = (stackIsArmorMaterial(stack, mat) 
				&& ((stack.getItem() instanceof IMMDObject) && 
						(((IMMDObject)stack.getItem()).getMMDMaterial() == mat))) && 
				(countArmorPieces(Materials.getMaterialByName(MaterialNames.MITHRIL),player) > 0);
 
		return rv;
	}

	@Override
	public boolean hasEffect(ItemStack stack, EntityLivingBase ent) {
		return ent instanceof EntityPlayer?hasEffect(stack, (EntityPlayer)ent):false;
	}
}
