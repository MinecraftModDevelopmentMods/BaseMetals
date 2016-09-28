package cyano.basemetals.asm;

import com.google.common.base.Optional;

import cyano.basemetals.items.IHorseArmor;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.HorseType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BaseMetalHooks {

	public static final DataParameter<Optional<ItemStack>> ARMOR_STACK = EntityDataManager.createKey(EntityHorse.class, DataSerializers.OPTIONAL_ITEM_STACK);

	/**
	 *
	 * @param entity
	 */
	public static void onInitHorse(EntityHorse entity) {
		entity.getDataManager().register(BaseMetalHooks.ARMOR_STACK, Optional.absent());
	}

	/**
	 *
	 * @param entity
	 * @param stack
	 */
	public static void setHorseArmorStack(EntityHorse entity, ItemStack stack) {
		entity.getDataManager().set(BaseMetalHooks.ARMOR_STACK, Optional.fromNullable(stack));
	}

	/**
	 *
	 * @param type
	 * @param entity
	 * @return
	 */
	@SideOnly(Side.CLIENT)
	public static String getTextureName(HorseType type, EntityHorse entity) {
		final ItemStack stack = entity.getDataManager().get(BaseMetalHooks.ARMOR_STACK).orNull();

		if ((stack != null) && (stack.getItem() instanceof IHorseArmor))
			return ((IHorseArmor) stack.getItem()).getArmorTexture(entity, stack);

		return type.getTexture().toString();
	}
}
