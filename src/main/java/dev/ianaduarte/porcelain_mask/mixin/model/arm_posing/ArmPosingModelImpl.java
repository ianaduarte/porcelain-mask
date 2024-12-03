package dev.ianaduarte.porcelain_mask.mixin.model.arm_posing;

import com.google.gson.JsonObject;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.util.GsonHelper;
import dev.ianaduarte.porcelain_mask.model.HoldingContext;
import dev.ianaduarte.porcelain_mask.model.ModelPartPose;
import dev.ianaduarte.porcelain_mask.model.ArmPosingModel;
import dev.ianaduarte.porcelain_mask.mixin.model.BlockModelAccessor;
import dev.ianaduarte.porcelain_mask.model.ModelPartSwing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({BlockModel.class, SimpleBakedModel.class})
public class ArmPosingModelImpl implements ArmPosingModel{
	@Unique ModelPartPose inMainhandRight;
	@Unique ModelPartPose inMainhandLeft;
	@Unique ModelPartPose inOffhandRight;
	@Unique ModelPartPose inOffhandLeft;
	
	@Unique ModelPartSwing inMainhandRightSwing;
	@Unique ModelPartSwing inMainhandLeftSwing;
	@Unique ModelPartSwing inOffhandRightSwing;
	@Unique ModelPartSwing inOffhandLeftSwing;
	
	@Unique Float inMainHandRightBobbingMultiplier;
	@Unique Float inMainHandLeftBobbingMultiplier;
	@Unique Float inOffhandRightBobbingMultiplier;
	@Unique Float inOffhandLeftBobbingMultiplier;
	
	@Unique Boolean hidesOffhandItem;
	
	@Override
	public ArmPosingModel getParent(){
		return ((ArmPosingModel)this) instanceof BlockModel? (ArmPosingModel)((BlockModelAccessor)this).getParent() : null;
	}
	
	@Override
	public ModelPartPose getPose(HoldingContext context, boolean leftHanded){
		if(leftHanded){
			context = context.opposite();
			ModelPartPose pose = getPose(context, false);
			return pose == null? null: pose.mirrored();
		}
		
		ModelPartPose pose = switch(context){
			case MAINHAND_RIGHT -> inMainhandRight;
			case MAINHAND_LEFT  -> inMainhandLeft;
			case OFFHAND_RIGHT  -> inOffhandRight;
			case OFFHAND_LEFT   -> inOffhandLeft;
		};
		if(pose == null && getParent() != null) return getParent().getPose(context,false);
		return pose;
	}
	@Override
	public ModelPartSwing getSwing(HoldingContext context, boolean leftHanded){
		if(leftHanded){
			context = context.opposite();
			return getSwing(context, false);
		}
		
		ModelPartSwing swing = switch(context){
			case MAINHAND_RIGHT -> inMainhandRightSwing;
			case MAINHAND_LEFT  -> inMainhandLeftSwing;
			case OFFHAND_RIGHT  -> inOffhandRightSwing;
			case OFFHAND_LEFT   -> inOffhandLeftSwing;
		};
		if(swing == null && getParent() != null) return getParent().getSwing(context,false);
		return swing;
	}
	@Override
	public Float getBobbingMultiplier(HoldingContext context, boolean leftHanded){
		if(leftHanded){
			context = context.opposite();
			return getBobbingMultiplier(context, false);
		}
		Float bob = switch(context){
			case MAINHAND_RIGHT -> inMainHandRightBobbingMultiplier;
			case MAINHAND_LEFT  -> inMainHandLeftBobbingMultiplier;
			case OFFHAND_RIGHT  -> inOffhandRightBobbingMultiplier;
			case OFFHAND_LEFT   -> inOffhandLeftBobbingMultiplier;
		};
		if(bob == null && getParent() != null) return getParent().getBobbingMultiplier(context,false);
		return bob;
	}
	
	
	@Override
	public boolean hasPoses(){
		return hasPoses(false);
	}
	@Override
	public boolean hasPoses(boolean includeAncestors){
		if(includeAncestors){
			boolean ancestorsHavePoses = getParent() != null && getParent().hasPoses(true);
			return hasPoses() || ancestorsHavePoses;
		}
		
		return (inMainhandRight != null || inMainhandLeft != null);
	}
	
	
	@Override
	public boolean hasCustomMainhandSwings(){
		return (inMainhandRightSwing != null || inMainhandLeftSwing != null);
	}
	@Override
	public boolean hasCustomOffhandSwing(){
		return (inOffhandRightSwing != null || inOffhandLeftSwing != null);
	}
	
	
	@Override
	public boolean hasCustomMainhandBobbing(){
		return (inMainHandRightBobbingMultiplier != null || inMainHandLeftBobbingMultiplier != null);
	}
	@Override
	public boolean hasCustomOffhandBobbing(){
		return (inOffhandRightBobbingMultiplier != null || inOffhandLeftBobbingMultiplier != null);
	}
	
	@Override
	public Boolean hidesOffhandItem(){
		if(hidesOffhandItem == null && getParent() != null) return getParent().hidesOffhandItem();
		if(hidesOffhandItem == null) return false;
		return hidesOffhandItem;
	}
	
	
	@Override
	public void fromJson(JsonObject json){
		hidesOffhandItem = (json.has("hide_offhand_item"))? json.get("hide_offhand_item").getAsBoolean() : null;
		
		if(json.has("in_mainhand")){
			JsonObject inMainHand = json.getAsJsonObject("in_mainhand");
			
			if(inMainHand.has("mainhand")){
				JsonObject mainHand = inMainHand.getAsJsonObject("mainhand");
				
				inMainhandRight      = ModelPartPose.fromJson(mainHand);
				inMainhandRightSwing = (mainHand.has("swing"))? ModelPartSwing.fromJson(mainHand.getAsJsonObject("swing"), false) : null;
				inMainHandRightBobbingMultiplier = (mainHand.has("bobbing_multiplier"))? mainHand.get("bobbing_multiplier").getAsFloat() : null;
			}
			if(inMainHand.has("offhand")){
				JsonObject offhand = inMainHand.getAsJsonObject("offhand");
				
				inMainhandLeft      = ModelPartPose.fromJson(offhand);
				inMainhandLeftSwing = (offhand.has("swing"))? ModelPartSwing.fromJson(offhand.getAsJsonObject("swing"), true) : null;
				inMainHandLeftBobbingMultiplier = (offhand.has("bobbing_multiplier"))? offhand.get("bobbing_multiplier").getAsFloat() : null;
			}
			
		}
		if(json.has("in_offhand")){
			JsonObject inOffhand = json.getAsJsonObject("in_offhand");
			if(inOffhand.has("mainhand")){
				JsonObject mainHand = inOffhand.getAsJsonObject("mainhand");
				
				inOffhandRight      = ModelPartPose.fromJson(mainHand);
				inOffhandRightSwing = (mainHand.has("swing"))? ModelPartSwing.fromJson(mainHand.getAsJsonObject("swing"), false) : null;
				inOffhandRightBobbingMultiplier = (mainHand.has("bobbing_multiplier"))? mainHand.get("bobbing_multiplier").getAsFloat() : null;
			}
			if(inOffhand.has("offhand")){
				JsonObject offhand = inOffhand.getAsJsonObject("offhand");
				
				inOffhandLeft      = ModelPartPose.fromJson(offhand);
				inOffhandLeftSwing = (offhand.has("swing"))? ModelPartSwing.fromJson(offhand.getAsJsonObject("swing"), true) : null;
				inOffhandLeftBobbingMultiplier = (offhand.has("bobbing_multiplier"))? offhand.get("bobbing_multiplier").getAsFloat() : null;
			}
		}
	}
	@Override
	public void fromOther(ArmPosingModel other){
		inMainhandRight = other.getPose(HoldingContext.MAINHAND_RIGHT,false);
		inMainhandLeft  = other.getPose(HoldingContext.MAINHAND_LEFT,false);
		
		inOffhandRight  = other.getPose(HoldingContext.OFFHAND_RIGHT,false);
		inOffhandLeft   = other.getPose(HoldingContext.OFFHAND_LEFT,false);
		
		inMainhandRightSwing = other.getSwing(HoldingContext.MAINHAND_RIGHT,false);
		inMainhandLeftSwing  = other.getSwing(HoldingContext.MAINHAND_LEFT,false);
		
		inOffhandRightSwing  = other.getSwing(HoldingContext.OFFHAND_RIGHT,false);
		inOffhandLeftSwing   = other.getSwing(HoldingContext.OFFHAND_LEFT,false);
		
		inMainHandRightBobbingMultiplier = other.getBobbingMultiplier(HoldingContext.MAINHAND_RIGHT,false);
		inMainHandLeftBobbingMultiplier  = other.getBobbingMultiplier(HoldingContext.MAINHAND_LEFT,false);
		
		inOffhandRightBobbingMultiplier  = other.getBobbingMultiplier(HoldingContext.OFFHAND_RIGHT,false);
		inOffhandLeftBobbingMultiplier   = other.getBobbingMultiplier(HoldingContext.OFFHAND_LEFT,false);
		
		hidesOffhandItem = other.hidesOffhandItem() != null && other.hidesOffhandItem();
	}
}
