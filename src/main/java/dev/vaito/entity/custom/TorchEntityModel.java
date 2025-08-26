package dev.vaito.entity.custom;

import dev.vaito.LittleAdventure;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.util.Identifier;

public class TorchEntityModel extends EntityModel<EntityRenderState> {
    public static final EntityModelLayer TORCH = new EntityModelLayer(Identifier.of(LittleAdventure.MOD_ID, "torch"), "main");
    private final ModelPart torch;

    public TorchEntityModel(ModelPart root) {
        super(root);
        this.torch = root.getChild("torch");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData torch = modelPartData.addChild("torch", ModelPartBuilder.create(), ModelTransform.rotation(0.0F, 21.0F, 0.0F));

        torch.addChild("torch_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -7.0F, -1.0F, 2.0F, 9.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.0F, 0.0F, 0.0F, 0.0F, -3.1416F));
        return TexturedModelData.of(modelData, 16, 16);
    }
}
