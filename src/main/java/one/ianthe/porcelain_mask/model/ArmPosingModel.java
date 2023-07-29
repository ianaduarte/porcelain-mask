package one.ianthe.porcelain_mask.model;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;

public interface ArmPosingModel{
	ArmPosingModel EMPTY = new ArmPosingModel(){
		@Override public ArmPose getInMainhandRight(boolean leftHanded){return null;}
		
		@Override public ArmPose getInMainhandLeft(boolean leftHanded){return null;}
		
		@Override public ArmPose getInOffhandRight(boolean leftHanded){return null;}
		
		@Override public ArmPose getInOffhandLeft(boolean leftHanded){return null;}
		
		@Override public boolean mainhandSwingsBoth(){return false;}
		
		@Override public boolean offhandSwingsBoth(){return false;}
		
		@Override public boolean hidesOffhandItem(){return false;}
		
		@Override public boolean hasPoses(boolean includeParents){return false;}
		
		@Override public void fromJson(JsonObject jsonObject){}
		
		@Override public void fromOther(ArmPosingModel other){}
		
		@Override public ArmPosingModel getParent(){return null;}
	};
	
	ArmPose getInMainhandRight(boolean leftHanded);
	ArmPose getInMainhandLeft(boolean leftHanded);
	ArmPose getInOffhandRight(boolean leftHanded);
	ArmPose getInOffhandLeft(boolean leftHanded);
	
	boolean mainhandSwingsBoth();
	boolean offhandSwingsBoth();
	
	boolean hidesOffhandItem();
	boolean hasPoses(boolean includeParents);
	
	
	void fromJson(JsonObject jsonObject);
	void fromOther(ArmPosingModel other);
	
	ArmPosingModel getParent();
}
