package one.ianthe.porcelain_mask.model;

import com.google.gson.JsonObject;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import one.ianthe.porcelain_mask.PorcelainUtil;

public class ModelPartPose{
	public static final ModelPartPose EMPTY = new ModelPartPose(false, 0, 0, 0, 0, 0);
	
	private final boolean OVERRIDE_TRANSFORMS;
	private final float X_ROT;
	private final float Y_ROT;
	private final float Z_ROT;
	
	private final float HEAD_X_ROT_FACTOR;
	private final float HEAD_Y_ROT_FACTOR;
	
	private ModelPartPose(boolean overrideTransforms,float xRot, float yRot, float zRot, float headXRotFactor, float headYRotFactor){
		this.OVERRIDE_TRANSFORMS = overrideTransforms;
		this.X_ROT = xRot;
		this.Y_ROT = yRot;
		this.Z_ROT = zRot;
		this.HEAD_X_ROT_FACTOR = headXRotFactor;
		this.HEAD_Y_ROT_FACTOR = headYRotFactor;
	}
	
	public void apply( ModelPart part, ModelPart head){
		float lookXRot = head.xRot * HEAD_X_ROT_FACTOR;
		float lookYRot = PorcelainUtil.normalizeAngle(head.yRot) * HEAD_Y_ROT_FACTOR;
		
		if(OVERRIDE_TRANSFORMS){
			part.xRot = this.X_ROT + lookXRot;
			part.yRot = this.Y_ROT + lookYRot;
			part.zRot = this.Z_ROT;
			return;
		}
		
		part.xRot += this.X_ROT + lookXRot;
		part.yRot += this.Y_ROT + lookYRot;
		part.zRot += this.Z_ROT;
	}
	
	public ModelPartPose mirrored(){
		return new ModelPartPose(
			this.OVERRIDE_TRANSFORMS,
			this.X_ROT, this.Y_ROT, -this.Z_ROT,
			this.HEAD_X_ROT_FACTOR, this.HEAD_Y_ROT_FACTOR
		);
	}
	public static ModelPartPose fromJson(JsonObject json){
		boolean overrideTransforms = GsonHelper.getAsBoolean(json, "override_transforms", false);
		
		float xRot = GsonHelper.getAsFloat(json, "pitch", 0f) * Mth.DEG_TO_RAD;
		float yRot = GsonHelper.getAsFloat(json, "yaw"  , 0f) * Mth.DEG_TO_RAD;
		float zRot = GsonHelper.getAsFloat(json, "roll" , 0f) * Mth.DEG_TO_RAD;
		
		float headXRotFactor = GsonHelper.getAsFloat(json, "head_pitch_factor" , 0f);
		float headYRotFactor = GsonHelper.getAsFloat(json, "head_yaw_factor" , 0f);
		
		return new ModelPartPose(overrideTransforms, xRot, yRot, zRot, headXRotFactor, headYRotFactor);
	}
}
