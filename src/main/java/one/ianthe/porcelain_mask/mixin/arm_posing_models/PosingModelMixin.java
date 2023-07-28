package one.ianthe.porcelain_mask.mixin.arm_posing_models;

import com.google.gson.JsonObject;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import one.ianthe.porcelain_mask.model.ArmPose;
import one.ianthe.porcelain_mask.model.IArmPosing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({BlockModel.class, SimpleBakedModel.class})
public class PosingModelMixin implements IArmPosing{
	@Unique private ArmPose leftMainPose;
	@Unique private ArmPose leftOffPose;
	@Unique private ArmPose rightMainPose;
	@Unique private ArmPose rightOffPose;
	
	@Override
	public ArmPose[] getMainPoses(){
		return new ArmPose[]{leftMainPose, rightMainPose};
	}
	@Override
	public ArmPose[] getOffPoses(){
		return new ArmPose[]{leftOffPose, rightOffPose};
	}
	
	@Override
	public void setFromJson(JsonObject json){
		if(json.has("main_hand")){
			JsonObject mainHand = json.getAsJsonObject("main_hand");
			
			if(mainHand.has("left")) this.leftMainPose = ArmPose.fromJson(mainHand.getAsJsonObject("left"));
			if(mainHand.has("right")) this.rightMainPose = ArmPose.fromJson(mainHand.getAsJsonObject("right"));
		}
		if(json.has("offhand")){
			JsonObject offhand = json.getAsJsonObject("offhand");
			
			if(offhand.has("left")) this.leftOffPose = ArmPose.fromJson(offhand.getAsJsonObject("left"));
			if(offhand.has("right")) this.rightOffPose = ArmPose.fromJson(offhand.getAsJsonObject("right"));
		}
	}
	
	@Override
	public void setFromOther(IArmPosing other){
		ArmPose[] otherMain = other.getMainPoses();
		ArmPose[] otherOff = other.getOffPoses();
		this.leftMainPose = otherMain[0];
		this.leftOffPose = otherOff[0];
		this.rightMainPose = otherMain[1];
		this.rightOffPose = otherOff[1];
	}
	
	@Override
	public boolean hasPoses(){
		return (leftMainPose != null || leftOffPose != null) || (rightMainPose != null || rightOffPose != null);
	}
}
