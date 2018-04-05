package com.mcmoddev.lib.client.renderer;

import com.mcmoddev.lib.entity.EntityCustomArrow;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class RenderCustomArrow extends Render<EntityCustomArrow> {

	/**
	 *
	 * @param renderManager
	 *            The render manager
	 */
	public RenderCustomArrow(final RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(final EntityCustomArrow entity, final double x, final double y,
			final double z, final float entityYaw, final float partialTicks) {
		this.bindEntityTexture(entity);
		RenderHelpers.doArrowOrBoltRender(entity, x, y, z, partialTicks, this.renderOutlines,
				this.getTeamColor(entity));
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	@Override
	protected ResourceLocation getEntityTexture(final EntityCustomArrow entity) {
		return new ResourceLocation("textures/entity/projectiles/arrow.png");
	}
}
