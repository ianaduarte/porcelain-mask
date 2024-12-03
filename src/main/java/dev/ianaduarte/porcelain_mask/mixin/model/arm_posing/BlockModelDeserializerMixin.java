package dev.ianaduarte.porcelain_mask.mixin.model.arm_posing;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.block.model.BlockModel;
import dev.ianaduarte.porcelain_mask.model.ArmPosingModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockModel.Deserializer.class)
public class BlockModelDeserializerMixin{
	@ModifyReturnValue(method = "deserialize", at = @At("RETURN"))
	public BlockModel deserializeArmPoser(BlockModel original, @Local(ordinal = 0) JsonObject jsonObject){
		if(jsonObject.has("arm_poses")){
			JsonObject armPoses = jsonObject.getAsJsonObject("arm_poses");
			ArmPosingModel armPosing = (ArmPosingModel) original;
			
			armPosing.fromJson(armPoses);
		}
		
		return original;
	}
}
