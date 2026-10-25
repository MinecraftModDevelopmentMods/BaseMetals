package zone.moddev.mc.basemetals.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import zone.moddev.mc.basemetals.content.MaterialBacked;
import zone.moddev.mc.basemetals.entity.MaterialProjectile;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

/**
 * Uses vanilla's trajectory-aligned arrow geometry while tinting it from the
 * exact ammunition item retained by the shared projectile entity.
 */
public final class MaterialProjectileRenderer extends ArrowRenderer<MaterialProjectile> {
    private int colour = 0xFFFFFF;

    public MaterialProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(MaterialProjectile projectile, float yaw, float partialTicks,
            PoseStack poses, MultiBufferSource buffers, int packedLight) {
        colour = projectile.getItem().getItem() instanceof MaterialBacked backed
                ? backed.baseMetalsMaterial().colour()
                : 0xFFFFFF;
        super.render(projectile, yaw, partialTicks, poses, buffers, packedLight);
    }

    @Override
    public void vertex(PoseStack.Pose pose, VertexConsumer vertices,
            int x, int y, int z, float u, float v, int normalX, int normalY,
            int normalZ, int packedLight) {
        vertices.addVertex(pose, x, y, z)
                .setColor((colour >> 16) & 0xFF, (colour >> 8) & 0xFF, colour & 0xFF, 0xFF)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(pose, normalX, normalZ, normalY);
    }

    @Override
    public ResourceLocation getTextureLocation(MaterialProjectile projectile) {
        return TippableArrowRenderer.NORMAL_ARROW_LOCATION;
    }
}
