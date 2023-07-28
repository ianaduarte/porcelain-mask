package one.ianthe.porcelain_mask.model;

import com.google.gson.JsonObject;

public interface IArmPosing{
	ArmPose[] getMainPoses();
	ArmPose[] getOffPoses();
	
	void setFromJson(JsonObject json);
	void setFromOther(IArmPosing other);
	boolean hasPoses();
}
