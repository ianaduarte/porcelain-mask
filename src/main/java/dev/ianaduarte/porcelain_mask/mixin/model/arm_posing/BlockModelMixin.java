package dev.ianaduarte.porcelain_mask.mixin.model.arm_posing;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BakedModel;
import dev.ianaduarte.porcelain_mask.model.ArmPosingModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockModel.class)
public class BlockModelMixin{
	@ModifyReturnValue(method = "bake(Lnet/minecraft/client/resources/model/ModelBaker;Lnet/minecraft/client/renderer/block/model/BlockModel;Ljava/util/function/Function;Lnet/minecraft/client/resources/model/ModelState;Z)Lnet/minecraft/client/resources/model/BakedModel;", at = @At(value = "RETURN", ordinal = 1))
	public BakedModel bakePosingModel(BakedModel original){
		ArmPosingModel armPosing = (ArmPosingModel)this;
		
		if(armPosing.hasPoses(true)){
			ArmPosingModel originalArmPosing = (ArmPosingModel)original;
			originalArmPosing.fromOther(armPosing);
		}
		return original;
	}
}
