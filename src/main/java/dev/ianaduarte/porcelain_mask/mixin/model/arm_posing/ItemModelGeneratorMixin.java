package dev.ianaduarte.porcelain_mask.mixin.model.arm_posing;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import dev.ianaduarte.porcelain_mask.model.ArmPosingModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemModelGenerator.class)
public class ItemModelGeneratorMixin{
	@ModifyReturnValue(method = "generateBlockModel", at = @At(value = "RETURN", ordinal = 0))
	public BlockModel generatePosingModel(BlockModel original, @Local(ordinal = 0) BlockModel model){
		ArmPosingModel armPosing = (ArmPosingModel)model;
		
		if(armPosing.hasPoses(true)){
			ArmPosingModel originalArmPosing = (ArmPosingModel)original;
			originalArmPosing.fromOther(armPosing);
			
			return (BlockModel)originalArmPosing;
		}
		return original;
	}
}
