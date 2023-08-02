package one.ianthe.porcelain_mask.model;

import com.google.gson.JsonObject;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import one.ianthe.porcelain_mask.PorcelainUtil;

public class ModelPartSwing{
	public static final ModelPartSwing DEFAULT_MAINHAND = new ModelPartSwing(
		ModelPartPose.EMPTY,
		new SwingAxis(EasingType.VANILLA,  1.2f, true, true, true, true),
		new SwingAxis(EasingType.VANILLA,    2f, true, true, true, true),
		new SwingAxis(EasingType.VANILLA, -0.4f, true, true, true, true)
	);
	public static final ModelPartSwing DEFAULT_OFFHAND = new ModelPartSwing(
		ModelPartPose.EMPTY,
		new SwingAxis(EasingType.NONE, 0f, false, false, false, false),
		new SwingAxis(EasingType.NONE, 0f, false, false, false, false),
		new SwingAxis(EasingType.NONE, 0f, false, false, false, false)
	);
	
	private final ModelPartPose POSE;

	private final SwingAxis X_AXIS;
	private final SwingAxis Y_AXIS;
	private final SwingAxis Z_AXIS;
	
	
	private ModelPartSwing(
		ModelPartPose pose, SwingAxis xAxis, SwingAxis yAxis, SwingAxis zAxis){
		this.POSE = pose;
		this.X_AXIS = xAxis;
		this.Y_AXIS = yAxis;
		this.Z_AXIS = zAxis;
	}
	
	public void swing(ModelPart rightArm, ModelPart leftArm, ModelPart head, ModelPart body, float delta, boolean isLeftArm){
		if(delta <= 0) return;
		
		float yFlip = isLeftArm? -1 : 1;
		float zFlip = isLeftArm? -1 : 1;
		float bodySwing = Mth.sin(Mth.sqrt(delta) * Mth.TWO_PI) * 0.2F;
		ModelPart partToSwing = isLeftArm? leftArm : rightArm;
		this.POSE.apply(partToSwing, head);
		
		if(X_AXIS.easing != EasingType.NONE || Y_AXIS.easing != EasingType.NONE || Z_AXIS.easing != EasingType.NONE){
			body.yRot += bodySwing * yFlip;
			
			rightArm.z = Mth.sin(body.yRot) * 5.0F;
			rightArm.x = -Mth.cos(body.yRot) * 5.0F;
			rightArm.yRot += body.yRot;
			
			leftArm.z = -Mth.sin(body.yRot) * 5.0F;
			leftArm.x = Mth.cos(body.yRot) * 5.0F;
			leftArm.yRot -= body.yRot;
		}
		
		switch(this.X_AXIS.easing){
			case SINE, FSINE, LINEAR, FLINEAR -> {
				float swingValue = this.X_AXIS.getValue(delta);
				partToSwing.xRot += swingValue;
			}
			case VANILLA -> {
				float f = 1.0F - delta;
				f = 1.0F - (f * f) * (f * f);
				float g = Mth.sin(f * Mth.PI);
				float h = Mth.sin(delta * Mth.PI) * -(head.xRot - 0.7F) * 0.75F;
				
				partToSwing.xRot -= (g * this.X_AXIS.amount + h);
			}
		}
		switch(this.Y_AXIS.easing){
			case SINE, FSINE, LINEAR, FLINEAR -> {
				float swingValue = this.Y_AXIS.getValue(delta);
				float swing = swingValue * this.Y_AXIS.amount;
				
				partToSwing.yRot += swing * yFlip;
			}
			case VANILLA -> partToSwing.yRot += (bodySwing * this.Y_AXIS.amount) * yFlip;
			
		}
		switch(this.Z_AXIS.easing){
			case SINE, FSINE, LINEAR, FLINEAR -> {
				float swingValue = this.Z_AXIS.getValue(delta);
				
				partToSwing.zRot += swingValue * zFlip;
			}
			case VANILLA -> partToSwing.zRot += (Mth.sin(delta * Mth.PI) * this.Z_AXIS.amount) * zFlip;
		}
	}
	
	public static ModelPartSwing fromJson(JsonObject json, boolean offhand){
		ModelPartPose pose = ModelPartPose.fromJson(json);
		//json.getAsJsonObject("swing_pitch")
		SwingAxis xAxis = SwingAxis.fromJson(GsonHelper.getAsJsonObject(json, "swing_pitch", new JsonObject()), offhand? "none" : "vanilla");
		SwingAxis yAxis = SwingAxis.fromJson(GsonHelper.getAsJsonObject(json, "swing_yaw", new JsonObject()), offhand? "none" : "vanilla");
		SwingAxis zAxis = SwingAxis.fromJson(GsonHelper.getAsJsonObject(json, "swing_roll", new JsonObject()), offhand? "none" : "vanilla");
		
		return new ModelPartSwing(pose, xAxis, yAxis, zAxis);
	}
	
	private static class SwingAxis{
		EasingType easing;
		float amount;
		boolean inverted;
		boolean invertedTwice;
		boolean squared;
		boolean squaredSquared;
		
		private SwingAxis(EasingType type, float amount, boolean inverted, boolean invertedTwice, boolean squared, boolean squaredSquared){
			this.easing = type;
			this.amount = amount;
			this.inverted = inverted;
			this.invertedTwice = invertedTwice;
			this.squared = squared;
			this.squaredSquared = squaredSquared;
		}
		
		float getValue(float delta){
			if(this.invertedTwice) delta = 1 - delta;
			if(this.squared) delta *= delta;
			if(this.squaredSquared) delta *= delta;
			if(this.inverted) delta = 1 - delta;
			
			return switch(this.easing){
				case SINE -> Mth.sin(delta * Mth.PI);
				case FSINE -> Mth.sin(delta * Mth.HALF_PI);
				case LINEAR -> PorcelainUtil.lsine(delta * Mth.PI);
				case FLINEAR -> PorcelainUtil.lsine(delta * Mth.HALF_PI);
				
				default -> 0.0f;
			} * this.amount;
		}
		
		static SwingAxis fromJson(JsonObject json, String defaultEasingType){
			EasingType type = EasingType.valueOf(GsonHelper.getAsString(json, "type", defaultEasingType).toUpperCase());
			float amount = GsonHelper.getAsFloat(json, "amount", 45.0f) * Mth.DEG_TO_RAD;
			boolean isInverted = GsonHelper.getAsBoolean(json, "inverted", false);
			boolean isInvertedTwice = GsonHelper.getAsBoolean(json, "inverted_twice", false);
			boolean isSquared = GsonHelper.getAsBoolean(json, "squared", false);
			boolean isSquaredTwice = GsonHelper.getAsBoolean(json, "squared_twice", false);
			
			return new SwingAxis(type, amount, isInverted, isInvertedTwice, isSquared, isSquaredTwice);
		}
	}
	
	public enum EasingType{
		NONE,
		SINE,
		FSINE,
		LINEAR,
		FLINEAR,
		VANILLA;
	}
}
