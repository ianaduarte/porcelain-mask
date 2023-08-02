package one.ianthe.porcelain_mask.model;

import com.google.gson.JsonObject;

public interface ArmPosingModel{
	ArmPosingModel EMPTY = new ArmPosingModel(){
		@Override public ArmPosingModel getParent(){return null;}
		
		@Override public ModelPartPose getPose(HoldingContext context, boolean leftHanded){return null;}
		@Override public ModelPartSwing getSwing(HoldingContext context, boolean leftHanded){return null;}
		
		@Override public Float getBobbingMultiplier(HoldingContext context, boolean leftHanded){return 0f;}
		
		@Override public boolean hasPoses(){return false;}
		@Override public boolean hasPoses(boolean includeAncestors){return false;}
		
		@Override public boolean hasCustomMainhandSwings(){return false;}
		@Override public boolean hasCustomOffhandSwing(){return false;}
		
		@Override public boolean hasCustomMainhandBobbing(){return false;}
		@Override public boolean hasCustomOffhandBobbing(){return false;}
		
		@Override public Boolean hidesOffhandItem(){return false;}
		
		@Override public void setFromJson(JsonObject json){}
		
		@Override public void setFromOther(ArmPosingModel other){}
	};
	
	ArmPosingModel getParent();
	
	ModelPartPose getPose(HoldingContext context, boolean leftHanded);
	ModelPartSwing getSwing(HoldingContext context, boolean leftHanded);
	Float getBobbingMultiplier(HoldingContext context, boolean leftHanded);
	
	boolean hasPoses();
	boolean hasPoses(boolean includeAncestors);
	
	boolean hasCustomMainhandSwings();
	boolean hasCustomOffhandSwing();
	
	boolean hasCustomMainhandBobbing();
	boolean hasCustomOffhandBobbing();
	
	Boolean hidesOffhandItem();
	
	void setFromJson(JsonObject json);
	void setFromOther(ArmPosingModel other);
}