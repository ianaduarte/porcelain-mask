package one.ianthe.porcelain_mask.mixin.arm_posing_models;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BakedModel;

import one.ianthe.porcelain_mask.model.IArmPosing;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockModel.class)
public abstract class BlockModelMixin{
	@ModifyReturnValue(
		method = "bake(Lnet/minecraft/client/resources/model/ModelBaker;Lnet/minecraft/client/renderer/block/model/BlockModel;Ljava/util/function/Function;Lnet/minecraft/client/resources/model/ModelState;Lnet/minecraft/resources/ResourceLocation;Z)Lnet/minecraft/client/resources/model/BakedModel;",
		at = @At(value = "RETURN", ordinal = 1)
	)
	public BakedModel porcelain_mask$bake(BakedModel original){
		IArmPosing armPosing = (IArmPosing) this;
		
		if(armPosing.hasPoses()){
			IArmPosing originalArmPosing = (IArmPosing) original;
			originalArmPosing.setFromOther(armPosing);
		}
		return original;
	}
}
