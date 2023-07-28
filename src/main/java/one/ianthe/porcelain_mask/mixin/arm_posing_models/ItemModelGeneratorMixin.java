package one.ianthe.porcelain_mask.mixin.arm_posing_models;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import one.ianthe.porcelain_mask.model.IArmPosing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemModelGenerator.class)
public class ItemModelGeneratorMixin{
	@ModifyReturnValue(
		method = "generateBlockModel",
		at = @At(value = "RETURN", ordinal = 0)
	)
	public BlockModel porcelain_mask$generateBlockModel(BlockModel original, @Local(ordinal = 0) BlockModel model){
		IArmPosing armPosing = (IArmPosing) model;
		
		if(armPosing.hasPoses()){
			IArmPosing originalArmPosing = (IArmPosing) original;
			originalArmPosing.setFromOther(armPosing);
			
			return (BlockModel) originalArmPosing;
		}
		return original;
	}
}
