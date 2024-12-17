package dev.ianaduarte.porcelain_mask.mixin.pose;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ModelPart.class)
public class ModelPartMixin {
	@Shadow public float x;
	@Shadow public float y;
	@Shadow public float z;
	
	@Shadow public float xRot;
	@Shadow public float yRot;
	@Shadow public float zRot;
	
	@Shadow public float xScale;
	@Shadow public float yScale;
	@Shadow public float zScale;
	
	/**
	 * @author ianaduarte
	 * @reason oh my god this shit was such a PAIN TO FIGURE OUT!!!
	 */
	@Overwrite
	public void translateAndRotate(PoseStack poseStack) {
		poseStack.translate(this.x / 16, this.y / 16, this.z / 16);
		if(this.xRot != 0 || this.yRot != 0.0F || this.zRot != 0.0F) poseStack.mulPose(new Quaternionf().rotationY(this.yRot).rotateX(this.xRot).rotateZ(this.zRot));
		if(this.xScale != 1 || this.yScale != 1 || this.zScale != 1) poseStack.scale(this.xScale, this.yScale, this.zScale);
	}
}
