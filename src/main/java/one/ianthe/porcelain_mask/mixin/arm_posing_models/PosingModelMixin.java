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
	public ArmPose[] getInMainhandPoses(boolean leftHanded){
		if(leftHanded){
			return new ArmPose[]{
				leftMainPose  != null? leftMainPose.mirror() : null,
				rightMainPose != null? rightMainPose.mirror() : null
			};
		}
		return new ArmPose[]{rightMainPose, leftMainPose};
	}
	@Override
	public ArmPose[] getInOffhandPoses(boolean leftHanded){
		if(leftHanded){
			return new ArmPose[]{
				(leftOffPose  != null)? leftOffPose.mirror()  : null,
				(rightOffPose != null)? rightOffPose.mirror() : null
			};
		}
		return new ArmPose[]{rightOffPose, leftOffPose};
	}
	
	@Override
	public void setFromJson(JsonObject json){
		if(json.has("in_mainhand")){
			JsonObject mainHand = json.getAsJsonObject("in_mainhand");
			
			if(mainHand.has("mainhand")) this.rightMainPose = ArmPose.fromJson(mainHand.getAsJsonObject("mainhand"));
			if(mainHand.has("offhand" )) this.leftMainPose  = ArmPose.fromJson(mainHand.getAsJsonObject("offhand" ));
		}
		if(json.has("in_offhand")){
			JsonObject offhand = json.getAsJsonObject("in_offhand");
			
			if(offhand.has("mainhand")) this.rightOffPose = ArmPose.fromJson(offhand.getAsJsonObject("mainhand"));
			if(offhand.has("offhand" )) this.leftOffPose  = ArmPose.fromJson(offhand.getAsJsonObject("offhand" ));
		}
	}
	
	@Override
	public void setFromOther(IArmPosing other){
		ArmPose[] otherMain = other.getInMainhandPoses(false);
		ArmPose[] otherOff  = other.getInOffhandPoses(false);
		this.rightMainPose  = otherMain[0];
		this.rightOffPose   = otherOff[0];
		this.leftMainPose   = otherMain[1];
		this.leftOffPose    = otherOff[1];
	}
	
	@Override
	public boolean hasPoses(){
		return (leftMainPose != null || leftOffPose != null) || (rightMainPose != null || rightOffPose != null);
	}
}
