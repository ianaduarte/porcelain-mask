package one.ianthe.porcelain_mask.mixin.arm_posing_models;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import one.ianthe.porcelain_mask.model.ArmPose;
import one.ianthe.porcelain_mask.model.ArmPosingModel;
import one.ianthe.porcelain_mask.model.BlockModelParentGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({BlockModel.class, SimpleBakedModel.class})
public class ArmPosingModelImpl implements ArmPosingModel{
	@Unique private ArmPose inMainhandRight;
	@Unique private ArmPose inMainhandLeft;
	
	@Unique private ArmPose inOffhandRight;
	@Unique private ArmPose inOffhandLeft;
	
	@Unique Boolean mainhandSwingsBoth;
	@Unique Boolean offhandSwingsBoth;
	
	@Unique Boolean hidesOffhandItem;
	
	
	@Override
	public ArmPose getInMainhandRight(boolean leftHanded){
		if(leftHanded){
			ArmPose oppositePose = getInMainhandLeft(false);
			return oppositePose == null? null : oppositePose.mirror();
		}
		
		if(inMainhandRight == null && getParent() != null){
			return getParent().getInMainhandRight(false);
		}
		return inMainhandRight;
	}
	
	@Override
	public ArmPose getInMainhandLeft(boolean leftHanded){
		if(leftHanded){
			ArmPose oppositePose = getInMainhandRight(false);
			return oppositePose == null? null : oppositePose.mirror();
		}
		
		if(inMainhandLeft == null && getParent() != null){
			return getParent().getInMainhandLeft(false);
		}
		return inMainhandLeft;
	}
	
	@Override
	public ArmPose getInOffhandRight(boolean leftHanded){
		if(leftHanded){
			ArmPose oppositePose = getInOffhandLeft(false);
			return oppositePose == null? null : oppositePose.mirror();
		}
		
		if(inOffhandRight == null && getParent() != null){
			return getParent().getInOffhandRight(false);
		}
		return inOffhandRight;
	}
	
	@Override
	public ArmPose getInOffhandLeft(boolean leftHanded){
		if(leftHanded){
			ArmPose oppositePose = getInOffhandRight(false);
			return oppositePose == null? null : oppositePose.mirror();
		}
		
		if(inOffhandLeft == null && getParent() != null){
			return getParent().getInOffhandLeft(false);
		}
		return inOffhandLeft;
	}
	
	@Override
	public boolean mainhandSwingsBoth(){
		if(mainhandSwingsBoth == null && getParent() != null){
			return getParent().mainhandSwingsBoth();
		}
		return mainhandSwingsBoth != null && mainhandSwingsBoth;
	}
	
	@Override
	public boolean offhandSwingsBoth(){
		if(offhandSwingsBoth == null && getParent() != null){
			return getParent().offhandSwingsBoth();
		}
		return offhandSwingsBoth != null && offhandSwingsBoth;
	}
	
	@Override
	public boolean hidesOffhandItem(){
		if(hidesOffhandItem == null && getParent() != null){
			return getParent().hidesOffhandItem();
		}
		return hidesOffhandItem != null && hidesOffhandItem;
	}
	
	@Override
	public boolean hasPoses(boolean includeParents){
		if(includeParents){
			boolean parentHasPoses = getParent() != null && getParent().hasPoses(true);
			return hasPoses(false) || parentHasPoses;
		}
		return (inMainhandRight != null || inOffhandRight != null) || (inMainhandLeft != null || inOffhandLeft != null);
	}
	
	@Override
	public void fromJson(JsonObject jsonObject){
		this.hidesOffhandItem = jsonObject.has("hide_offhand_item") && jsonObject.get("hide_offhand_item").getAsBoolean();
		if(jsonObject.has("in_mainhand")){
			JsonObject mainHand = jsonObject.getAsJsonObject("in_mainhand");
			this.mainhandSwingsBoth = mainHand.has("offhand_swings") && mainHand.get("offhand_swings").getAsBoolean();
			
			if(mainHand.has("mainhand")) this.inMainhandRight = ArmPose.fromJson(mainHand.getAsJsonObject("mainhand"));
			if(mainHand.has("offhand" )) this.inMainhandLeft  = ArmPose.fromJson(mainHand.getAsJsonObject("offhand" ));
		}
		if(jsonObject.has("in_offhand")){
			JsonObject offhand = jsonObject.getAsJsonObject("in_offhand");
			this.offhandSwingsBoth = offhand.has("offhand_swings") && offhand.get("offhand_swings").getAsBoolean();
			
			if(offhand.has("mainhand")) this.inOffhandRight = ArmPose.fromJson(offhand.getAsJsonObject("mainhand"));
			if(offhand.has("offhand" )) this.inOffhandLeft  = ArmPose.fromJson(offhand.getAsJsonObject("offhand" ));
		}
	}
	
	@Override
	public void fromOther(ArmPosingModel other){
		inMainhandRight = other.getInMainhandRight(false);
		inMainhandLeft = other.getInMainhandLeft(false);
		inOffhandRight = other.getInOffhandRight(false);
		inOffhandLeft = other.getInOffhandLeft(false);
		mainhandSwingsBoth = other.mainhandSwingsBoth();
		offhandSwingsBoth = other.offhandSwingsBoth();
		hidesOffhandItem = other.hidesOffhandItem();
	}
	
	@Override
	public ArmPosingModel getParent(){
		ArmPosingModel _this = this;
		return _this instanceof BlockModel? (ArmPosingModel)((BlockModelParentGetter)_this).getParent() : null;
	}
}
