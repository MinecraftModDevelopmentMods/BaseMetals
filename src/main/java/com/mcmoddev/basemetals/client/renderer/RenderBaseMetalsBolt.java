package com.mcmoddev.basemetals.client.renderer;

import com.mcmoddev.lib.client.renderer.RenderCustomBolt;
import com.mcmoddev.lib.entity.EntityCustomBolt;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Renders MMDLib bolts with Minecraft's existing projectile texture.
 *
 * <p>MMDLib requests {@code minecraft:textures/entity/projectiles/bolt.png},
 * which does not exist in Minecraft 1.12.2. The bolt and arrow renderers use
 * the same geometry and UV layout, so the vanilla arrow texture is the
 * compatible replacement.</p>
 */
@SideOnly(Side.CLIENT)
public final class RenderBaseMetalsBolt extends RenderCustomBolt {

	private static final ResourceLocation PROJECTILE_TEXTURE =
			new ResourceLocation("minecraft", "textures/entity/projectiles/arrow.png");

	/**
	 * Creates the Base Metals bolt renderer.
	 *
	 * @param renderManager Minecraft's entity render manager
	 */
	public RenderBaseMetalsBolt(final RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(final EntityCustomBolt entity) {
		return PROJECTILE_TEXTURE;
	}
}
