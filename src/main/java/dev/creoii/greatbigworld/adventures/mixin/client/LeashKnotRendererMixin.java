package dev.creoii.greatbigworld.adventures.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.creoii.greatbigworld.adventures.client.AdventuresClient;
import dev.creoii.greatbigworld.adventures.client.LeashKnotRenderState;
import dev.creoii.greatbigworld.adventures.client.ThinLeashKnotModel;
import dev.creoii.greatbigworld.adventures.util.AdventuresTags;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LeashKnotRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LeashKnotRenderer.class)
public abstract class LeashKnotRendererMixin extends EntityRenderer<LeashFenceKnotEntity, LeashKnotRenderState> {
    @Unique
    private ThinLeashKnotModel thinModel;

    protected LeashKnotRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$initThinModel(EntityRendererProvider.Context context, CallbackInfo ci) {
        thinModel = new ThinLeashKnotModel(context.bakeLayer(AdventuresClient.THIN_LEASH_KNOT));
    }

    @WrapOperation(method = "submit", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IIILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"))
    private void gbw$renderThinModel(SubmitNodeCollector instance, Model model, Object o, PoseStack poseStack, RenderType renderType, int i, int j, int k, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, Operation<Void> original, @Local(argsOnly = true) EntityRenderState entityRenderState) {
        if (entityRenderState instanceof LeashKnotRenderState leashKnotRenderState && leashKnotRenderState.isThin()) {
            poseStack.pushPose();
            poseStack.translate(-.03125, 0f, -.03125);
            original.call(instance, thinModel, o, poseStack, renderType, i, j, k, crumblingOverlay);
            poseStack.popPose();
        } else original.call(instance, model, o, poseStack, renderType, i, j, k, crumblingOverlay);
    }

    @Inject(method = "createRenderState", at = @At("HEAD"), cancellable = true)
    private void gbw$createLeashKnotRenderState(CallbackInfoReturnable<LeashKnotRenderState> cir) {
        cir.setReturnValue(new LeashKnotRenderState());
    }

    @Override
    public void extractRenderState(LeashFenceKnotEntity entity, LeashKnotRenderState entityRenderState, float f) {
        super.extractRenderState(entity, entityRenderState, f);
        entityRenderState.setThin(entity.getBlockStateOn().is(AdventuresTags.THIN_LEASHABLE));
    }
}
