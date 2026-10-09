package zone.moddev.mc.basemetals.client;

import zone.moddev.mc.basemetals.entity.MaterialProjectile;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public final class MaterialProjectileRenderer extends ArrowRenderer<MaterialProjectile> {
    private static final ResourceLocation ARROW_TEXTURE =
            new ResourceLocation("textures/entity/projectiles/arrow.png");

    public MaterialProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(MaterialProjectile projectile) {
        return ARROW_TEXTURE;
    }
}
