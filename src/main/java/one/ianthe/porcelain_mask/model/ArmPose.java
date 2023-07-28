package one.ianthe.porcelain_mask.model;

import com.google.gson.JsonObject;

public class ArmPose{
	private final float YAW;
	private final float PITCH;
	private final float ROLL;
	private final float SWING_YAW_FACTOR;
	private final float SWING_PITCH_FACTOR;
	private final float SWING_ROLL_FACTOR;
	private final boolean FOLLOW_VIEW;
	private final boolean IGNORE_BOBBING;
	
	public ArmPose(float yaw, float pitch, float roll, float swingYawFactor, float swingPitchFactor, float swingRollFactor, boolean followView, boolean ignoreBobbing){
		this.YAW = yaw;
		this.PITCH = pitch;
		this.ROLL = roll;
		this.SWING_YAW_FACTOR = swingYawFactor;
		this.SWING_PITCH_FACTOR = swingPitchFactor;
		this.SWING_ROLL_FACTOR = swingRollFactor;
		this.FOLLOW_VIEW = followView;
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
	
	public boolean followsView(){
		return FOLLOW_VIEW;
	}
	public boolean getIgnoresBobbing(){
		return IGNORE_BOBBING;
	}
	
	public static ArmPose fromJson(JsonObject json){
		float yaw   = (json.get("yaw")   != null)? json.get("yaw").getAsFloat()   : 0.0f;
		float pitch = (json.get("pitch") != null)? json.get("pitch").getAsFloat() : 0.0f;
		float roll  = (json.get("roll")  != null)? json.get("roll").getAsFloat()  : 0.0f;
		
		float swingYawFactor    = (json.get("swing_yaw_factor")   != null)? json.get("swing_yaw_factor").getAsFloat()   : 1.0f;
		float swingPitchFactor  = (json.get("swing_pitch_factor") != null)? json.get("swing_pitch_factor").getAsFloat() : 1.0f;
		float swingRollFactor   = (json.get("swing_roll_factor")  != null)? json.get("swing_roll_factor").getAsFloat()  : 1.0f;
		boolean followView = json.get("follow_view") != null && json.get("follow_view").getAsBoolean();
		boolean ignoreBobbing = json.get("ignore_bobbing") != null && json.get("ignore_bobbing").getAsBoolean();
		
		return new ArmPose(
			yaw,
			pitch,
			roll,
			swingYawFactor,
			swingPitchFactor,
			swingRollFactor,
			followView,
			ignoreBobbing
		);
	}
}
