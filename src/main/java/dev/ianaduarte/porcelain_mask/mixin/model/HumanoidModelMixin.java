package dev.ianaduarte.porcelain_mask.mixin.model;

import com.llamalad7.mixinextras.sugar.Local;
import dev.ianaduarte.porcelain_mask.PorcelainUtil;
import dev.ianaduarte.porcelain_mask.model.ArmPose;
import dev.ianaduarte.porcelain_mask.model.ArmPosingData;
import dev.ianaduarte.porcelain_mask.model.ArmSwing;
import dev.ianaduarte.porcelain_mask.model.ExtendedArmedRenderState;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin<T extends HumanoidRenderState> {
	@Shadow @Final public ModelPart rightArm;
	@Shadow @Final public ModelPart leftArm;
	@Shadow @Final public ModelPart head;
	
	
	@Shadow @Final public ModelPart body;
	
	/*@Redirect(
			method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/AnimationUtils;bobModelPart(Lnet/minecraft/client/model/geom/ModelPart;FF)V", ordinal = 1)
		)
		private <T extends HumanoidRenderState> void changeLeftBob(ModelPart leftArm, float f, float g, @Local(argsOnly = true) T humanoidRenderState) {
			//ExtendedArmedRenderState extRenderState = (ExtendedArmedRenderState) humanoidRenderState;
			//AnimationUtils.bobModelPart(leftArm, humanoidRenderState.ageInTicks, -extRenderState.getPose(HumanoidArm.LEFT).bobFactor());
		}
		
		@Redirect(
			method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/AnimationUtils;bobModelPart(Lnet/minecraft/client/model/geom/ModelPart;FF)V", ordinal = 0)
		)
		private <T extends HumanoidRenderState> void changeRightBob(ModelPart rightArm, float f, float g, @Local(argsOnly = true) T humanoidRenderState) {
			//ExtendedArmedRenderState extRenderState = (ExtendedArmedRenderState) humanoidRenderState;
			//AnimationUtils.bobModelPart(rightArm, humanoidRenderState.ageInTicks, extRenderState.getPose(HumanoidArm.RIGHT).bobFactor());
		}*/
	@Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/HumanoidModel;setupAttackAnimation(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;F)V", shift = At.Shift.BEFORE))
	private void poseArms(T humanoidRenderState, CallbackInfo ci) {
		ExtendedArmedRenderState extRenderState = (ExtendedArmedRenderState) humanoidRenderState;
		boolean leftHanded = PorcelainUtil.isLeftHanded(humanoidRenderState);
		boolean isUsingItem = humanoidRenderState.isUsingItem;
		ModelPart mainhand, offhand;
		ArmPosingData mainhandData, offhandData;
		
		if(leftHanded) {
			mainhand = leftArm;
			offhand  = rightArm;
			mainhandData = extRenderState.getData(HumanoidArm.LEFT );
			offhandData  = extRenderState.getData(HumanoidArm.RIGHT);
		} else {
			mainhand = rightArm;
			offhand  = leftArm;
			mainhandData = extRenderState.getData(HumanoidArm.RIGHT);
			offhandData  = extRenderState.getData(HumanoidArm.LEFT );
		}
		if(!(isUsingItem && humanoidRenderState.useItemHand == InteractionHand.MAIN_HAND)) {
			if(mainhandData.inMainhand().mainhand() != ArmPose.EMPTY) mainhandData.inMainhand().mainhand().apply(mainhand, head);
			else if(offhandData.inOffhand().mainhand() != ArmPose.EMPTY) offhandData.inOffhand().mainhand().apply(offhand, head);
		}
		
		if(!(isUsingItem && humanoidRenderState.useItemHand == InteractionHand.OFF_HAND)) {
			if(mainhandData.inMainhand().offhand() != ArmPose.EMPTY) mainhandData.inMainhand().offhand().apply(offhand, head);
			else if(offhandData.inOffhand().offhand() != ArmPose.EMPTY) offhandData.inOffhand().offhand().apply(offhand, head);
		}
	}
	
	@Inject(method = "setupAttackAnimation", at = @At("HEAD"), cancellable = true)
	private void swingArms(T humanoidRenderState, float f, CallbackInfo ci) {
		ExtendedArmedRenderState extRenderState = (ExtendedArmedRenderState) humanoidRenderState;
		boolean leftHanded = PorcelainUtil.isLeftHanded(humanoidRenderState);
		boolean usingOffhand = humanoidRenderState.isUsingItem && humanoidRenderState.useItemHand == InteractionHand.OFF_HAND;
		float yFlip = (leftHanded)? (usingOffhand? 1 : -1) : (usingOffhand? -1 : 1);
		ModelPart mainhand, offhand;
		ArmPosingData mainhandData, offhandData;
		
		if(leftHanded) {
			mainhand = leftArm;
			offhand = rightArm;
			mainhandData = extRenderState.getData(HumanoidArm.LEFT );
			offhandData  = extRenderState.getData(HumanoidArm.RIGHT);
		} else {
			mainhand = rightArm;
			offhand = leftArm;
			mainhandData = extRenderState.getData(HumanoidArm.RIGHT);
			offhandData  = extRenderState.getData(HumanoidArm.LEFT );
		}
		boolean swung = false;
		
		if(!usingOffhand && mainhandData.inMainhand().mainhandSwing() != ArmSwing.DEFAULT_MAINHAND) {
			mainhandData.inMainhand().mainhandSwing().swing(mainhand, head, humanoidRenderState.attackTime, leftHanded);
			swung = true;
			
			if(mainhandData.inMainhand().offhandSwing() != ArmSwing.DEFAULT_OFFHAND) {
				mainhandData.inMainhand().offhandSwing().swing(offhand, head, humanoidRenderState.attackTime, !leftHanded);
			}
		} else if(usingOffhand && offhandData.inOffhand().mainhandSwing() != ArmSwing.DEFAULT_OFFHAND) {
			mainhandData.inMainhand().mainhandSwing().swing(mainhand, head, humanoidRenderState.attackTime, leftHanded);
			
			if(offhandData.inOffhand().offhandSwing() != ArmSwing.DEFAULT_MAINHAND) {
				mainhandData.inMainhand().offhandSwing().swing(offhand, head, humanoidRenderState.attackTime, !leftHanded);
			}
			swung = true;
		}
		
		if(swung) {
			float bodySwing = Mth.sin(Mth.sqrt(humanoidRenderState.attackTime) * Mth.TWO_PI) * 0.2F;
			body.yRot += bodySwing * yFlip;
			
			rightArm.z = Mth.sin(body.yRot) * 5.0F;
			rightArm.x = -Mth.cos(body.yRot) * 5.0F;
			rightArm.yRot += body.yRot;
			
			leftArm.z = -Mth.sin(body.yRot) * 5.0F;
			leftArm.x = Mth.cos(body.yRot) * 5.0F;
			leftArm.yRot -= body.yRot;
			ci.cancel();
		}
	}
}
