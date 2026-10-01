package com.mcmoddev.basemetals.client.renderer;

import com.mcmoddev.lib.client.renderer.RenderCustomBolt;
import com.mcmoddev.lib.entity.EntityCustomBolt;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Uses the vanilla arrow texture for bolts; MMDLib's bolt texture does not exist.
 * The two projectiles share the same model and texture layout.
 */
@SideOnly(Side.CLIENT)
public final class RenderBaseMetalsBolt extends RenderCustomBolt {

	private static final ResourceLocation PROJECTILE_TEXTURE = new ResourceLocation("minecraft",
			"textures/entity/projectiles/arrow.png");

	public RenderBaseMetalsBolt(final RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(final EntityCustomBolt entity) {
		return PROJECTILE_TEXTURE;
	}
}
