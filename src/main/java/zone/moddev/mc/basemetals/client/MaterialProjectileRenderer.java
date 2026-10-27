package zone.moddev.mc.basemetals.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.projectile.ArrowModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import zone.moddev.mc.basemetals.content.MaterialBacked;
import zone.moddev.mc.basemetals.entity.MaterialProjectile;

/** Vanilla arrow geometry, with the ammunition's colour kept in its own render state. */
public final class MaterialProjectileRenderer
        extends EntityRenderer<MaterialProjectile, MaterialProjectileRenderer.State> {
    private static final Identifier TEXTURE = Identifier.withDefaultNamespace(
            "textures/entity/projectiles/arrow.png");
    private final ArrowModel model;

    public MaterialProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        model = new ArrowModel(context.bakeLayer(ModelLayers.ARROW));
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(MaterialProjectile projectile, State state, float partialTicks) {
        super.extractRenderState(projectile, state, partialTicks);
        state.xRot = projectile.getXRot(partialTicks);
        state.yRot = projectile.getYRot(partialTicks);
        state.shake = projectile.shakeTime - partialTicks;
        state.colour = 0xFF000000 | (projectile.getItem().getItem() instanceof MaterialBacked backed
                ? backed.baseMetalsMaterial().colour() : 0xFFFFFF);
    }

    @Override
    public void submit(State state, PoseStack poses, SubmitNodeCollector collector, CameraRenderState camera) {
        poses.pushPose();
        poses.mulPose(Axis.YP.rotationDegrees(state.yRot - 90.0F));
        poses.mulPose(Axis.ZP.rotationDegrees(state.xRot));
        collector.submitModel(model, state, poses, RenderTypes.entityCutout(TEXTURE),
                state.lightCoords, OverlayTexture.NO_OVERLAY, state.colour, null,
                state.outlineColor, null);
        poses.popPose();

        super.submit(state, poses, collector, camera);
    }

    public static final class State extends ArrowRenderState {
        public int colour = 0xFFFFFFFF;
    }
}
