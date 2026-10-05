package zone.moddev.mc.basemetals.client;

import zone.moddev.mc.basemetals.entity.MaterialProjectile;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public final class MaterialProjectileRenderer extends ArrowRenderer<MaterialProjectile> {
    private static final ResourceLocation ARROW_TEXTURE =
            new ResourceLocation("textures/entity/projectiles/arrow.png");

    public MaterialProjectileRenderer(EntityRendererManager manager) {
        super(manager);
    }

    @Override
    protected ResourceLocation getEntityTexture(MaterialProjectile projectile) {
        return ARROW_TEXTURE;
    }
}
