package one.ianthe.porcelain_mask.model;

import com.google.gson.JsonObject;

public interface IArmPosing{
	ArmPose[] getInMainhandPoses(boolean leftHanded);
	ArmPose[] getInOffhandPoses(boolean leftHanded);
	
	void setFromJson(JsonObject json);
	void setFromOther(IArmPosing other);
	boolean hasPoses();
}