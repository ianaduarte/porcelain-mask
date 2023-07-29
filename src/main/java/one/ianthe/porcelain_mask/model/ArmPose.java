package one.ianthe.porcelain_mask.model;

import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;

public class ArmPose{
	public static final ArmPose EMPTY = new ArmPose(0f, 0f, 0f, 1f, 1f, 1f, 0f, 0f, false);

	private final float YAW;
	private final float PITCH;
	private final float ROLL;
	
	private final float SWING_YAW_FACTOR;
	private final float SWING_PITCH_FACTOR;
	private final float SWING_ROLL_FACTOR;
	
	private final float HEAD_YAW_FACTOR;
	private final float HEAD_PITCH_FACTOR;
	
	private final boolean IGNORE_BOBBING;
	
	public ArmPose(
		float yaw, float pitch, float roll,
		float swingYawFactor, float swingPitchFactor, float swingRollFactor,
		float headYawFactor, float headPitchFactor,
		boolean ignoreBobbing){
		this.YAW = yaw;
		this.PITCH = pitch;
		this.ROLL = roll;
		this.SWING_YAW_FACTOR = swingYawFactor;
		this.SWING_PITCH_FACTOR = swingPitchFactor;
		this.SWING_ROLL_FACTOR = swingRollFactor;
		this.HEAD_YAW_FACTOR = headYawFactor;
		this.HEAD_PITCH_FACTOR = headPitchFactor;
		this.IGNORE_BOBBING = ignoreBobbing;
	}

	
	public float getYaw(){
		return YAW;
	}
	public float getPitch(){
		return PITCH;
	}
	public float getRoll(){
		return ROLL;
	}
	
	public float getSwingYawFactor(){
		return SWING_YAW_FACTOR;
	}
	public float getSwingPitchFactor(){
		return SWING_PITCH_FACTOR;
	}
	public float getSwingRollFactor(){
		return SWING_ROLL_FACTOR;
	}
	
	public float getHeadYawFactor(){
		return HEAD_YAW_FACTOR;
	}
	public float getHeadPitchFactor(){
		return HEAD_PITCH_FACTOR;
	}
	
	public boolean ignoresBobbing(){
		return IGNORE_BOBBING;
	}
	
	public static ArmPose fromJson(JsonObject json){
		float yaw   = GsonHelper.getAsFloat(json, "yaw"  , 0.0f);
		float pitch = GsonHelper.getAsFloat(json, "pitch", 0.0f);
		float roll  = GsonHelper.getAsFloat(json, "roll" , 0.0f);
		
		float swingYawFactor   = GsonHelper.getAsFloat(json, "swing_yaw_factor"  , 1.0f);
		float swingPitchFactor = GsonHelper.getAsFloat(json, "swing_pitch_factor", 1.0f);
		float swingRollFactor  = GsonHelper.getAsFloat(json, "swing_roll_factor" , 1.0f);
		
		float headYawFactor   = GsonHelper.getAsFloat(json, "head_yaw_factor"  , 0.0f);
		float headPitchFactor = GsonHelper.getAsFloat(json, "head_pitch_factor", 0.0f);
		boolean ignoreBobbing = GsonHelper.getAsBoolean(json, "ignore_bobbing" , false);
		
		return new ArmPose(
			yaw,
			pitch,
			roll,
			swingYawFactor,
			swingPitchFactor,
			swingRollFactor,
			headYawFactor,
			headPitchFactor,
			ignoreBobbing
		);
	}
	public ArmPose mirror(){
		return new ArmPose(
			-YAW,
			PITCH,
			-ROLL,
			SWING_YAW_FACTOR,
			SWING_PITCH_FACTOR,
			SWING_ROLL_FACTOR,
			HEAD_YAW_FACTOR,
			HEAD_PITCH_FACTOR,
			IGNORE_BOBBING
		);
	}
	public ArmPose copy(){
		return new ArmPose(
			YAW,
			PITCH,
			ROLL,
			SWING_YAW_FACTOR,
			SWING_PITCH_FACTOR,
			SWING_ROLL_FACTOR,
			HEAD_YAW_FACTOR,
			HEAD_PITCH_FACTOR,
			IGNORE_BOBBING
		);
	}
}
