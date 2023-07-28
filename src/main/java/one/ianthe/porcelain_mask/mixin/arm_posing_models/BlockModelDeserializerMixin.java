package one.ianthe.porcelain_mask.mixin.arm_posing_models;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.block.model.BlockModel;
import one.ianthe.porcelain_mask.model.IArmPosing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockModel.Deserializer.class)
public class BlockModelDeserializerMixin{
	@ModifyReturnValue(
		method = "deserialize",
		at = @At("RETURN")
	)
	public BlockModel porcelain_mask$deserializeMixin(BlockModel original, @Local(ordinal = 0)JsonObject jsonObject){
		if(jsonObject.has("arm_poses")){
			JsonObject armPoses = jsonObject.getAsJsonObject("arm_poses");
			IArmPosing armPosing = (IArmPosing) original;
			armPosing.setFromJson(armPoses);
		}
		return original;
	}
}
