package dev.creoii.greatbigworld.adventures.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

@Environment(EnvType.CLIENT)
public class ThinLeashKnotModel extends EntityModel<EntityRenderState> {
    private final ModelPart knot;

    public ThinLeashKnotModel(ModelPart modelPart) {
        super(modelPart);
        this.knot = modelPart.getChild("knot");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("knot", CubeListBuilder.create().texOffs(1, 1).addBox(-2.0F, -8.0F, -2.0F, 5.0F, 8.0F, 5.0F), PartPose.ZERO);
        return LayerDefinition.create(meshDefinition, 32, 32);
    }
}
