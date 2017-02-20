/**
 * 
 */
package com.mcmoddev.lib.integration.plugins.tinkers.traits;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.traits.AbstractTrait;
// import slimeknights.tconstruct.library.utils.ToolHelper;

/**
 * @author Daniel Hazelton &lt;dshadowwolf@gmail.com&gt;
 *
 */
public class TraitSoft extends AbstractTrait {

	public TraitSoft() {
		super("soft", 0xffffff);
	}

	@Override
	public int onToolDamage(ItemStack tool, int damage, int newDamage, EntityLivingBase entity) {
		newDamage += (int) (damage * 1.25f);
		return super.onToolDamage(tool, damage, newDamage, entity);
	}
}
