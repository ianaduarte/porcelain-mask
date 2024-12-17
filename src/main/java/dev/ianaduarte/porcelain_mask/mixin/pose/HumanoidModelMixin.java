package dev.ianaduarte.porcelain_mask.mixin.pose;

import dev.ianaduarte.porcelain_mask.PorcelainMask;
import dev.ianaduarte.porcelain_mask.model.pose.ArmPose;
import dev.ianaduarte.porcelain_mask.model.pose.ArmSwing;
import dev.ianaduarte.porcelain_mask.model.pose.PosingData;
import dev.ianaduarte.porcelain_mask.util.ExtArmedRenderState;
import dev.ianaduarte.porcelain_mask.util.ExtHumanoidModel;
import dev.ianaduarte.porcelain_mask.util.PorcelainMth;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin<T extends HumanoidRenderState> implements ExtHumanoidModel {
	@Shadow @Final public ModelPart rightArm;
	@Shadow @Final public ModelPart leftArm;
	@Shadow @Final public ModelPart head;
	@Shadow @Final public ModelPart body;
	
	@Unique boolean leftHanded;
	@Unique ModelPart mainhand, offhand;
	@Unique PosingData mainhandData, offhandData;
	
	@Override
	public ModelPart getHand(InteractionHand hand) {
		return hand == InteractionHand.MAIN_HAND? mainhand : offhand;
	}
	@Override
	public PosingData getPosingData(InteractionHand hand) {
		return hand == InteractionHand.MAIN_HAND? mainhandData : offhandData;
	}
	
	@Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At(value = "HEAD"))
	private void fetchInfo(T renderState, CallbackInfo ci) {
		ExtArmedRenderState extRenderState = (ExtArmedRenderState)renderState;
		
		mainhandData = extRenderState.getData(InteractionHand.MAIN_HAND);
		offhandData  = extRenderState.getData(InteractionHand.OFF_HAND);
		leftHanded = renderState.mainArm == HumanoidArm.LEFT;
		
		if(leftHanded) {
			mainhand = leftArm;
			offhand  = rightArm;
		} else {
			mainhand = rightArm;
			offhand  = leftArm;
		}
	}
	
	@Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At(value = "TAIL"))
	private void poseArms(T renderState, CallbackInfo ci) {
		if(mainhandData.isEmpty() && offhandData.isEmpty()) return;
		
		boolean leftHanded = renderState.mainArm != HumanoidArm.RIGHT;
		boolean usingMainhand = renderState.isUsingItem && renderState.useItemHand == InteractionHand.MAIN_HAND;
		boolean usingOffhand  = renderState.isUsingItem && renderState.useItemHand == InteractionHand.OFF_HAND;
		
		Map<String, Double> sharedVariables = Map.of(
			"headYaw", Math.toDegrees(head.yRot),
			"headPitch", Math.toDegrees(head.xRot),
			"swingDelta", (double)renderState.attackTime,
			"swingAmount", PorcelainMth.tsin(renderState.attackTime * Math.PI)
		);
		//these get modified inside apply and swing
		//Map<String, Double> mainhandVariables = new HashMap<>(sharedVariables);
		//Map<String, Double> offhandVariables = new HashMap<>(sharedVariables);
		
		
		ArmPose mainPose, offPose;
		if(renderState.attackTime <= 0) {
			mainPose = preferMain(this.mainhandData.inMainhand().mainhand(), this.offhandData.inOffhand().mainhand());
			offPose = preferMain(this.mainhandData.inMainhand().offhand(), this.offhandData.inOffhand().offhand());
			
		} else {
			mainPose = preferMain(usingOffhand, this.mainhandData.inMainhand().mainhandSwing(), this.offhandData.inOffhand().mainhandSwing());
			offPose  = preferMain(usingOffhand, this.mainhandData.inMainhand().offhandSwing(), this.offhandData.inOffhand().offhandSwing());
		}
		mainPose.transform().apply(mainhand, sharedVariables, leftHanded);
		offPose.transform().apply(offhand, sharedVariables, leftHanded);
		float mirrorFactor = leftHanded? -1 : 1;
		AnimationUtils.bobModelPart(mainhand, renderState.ageInTicks, mainPose.bobFactor() * mirrorFactor);
		AnimationUtils.bobModelPart(offhand, renderState.ageInTicks, offPose.bobFactor() * -mirrorFactor);
		/*ArmPose mainPose = preferMain(this.mainhandData.inMainhand().mainhand(), this.offhandData.inOffhand().mainhand());
		ArmPose   offPose = preferMain(this.mainhandData.inMainhand().offhand(), this.offhandData.inOffhand().offhand());
		mainPose.transform().apply(mainhand, mainhandVariables, leftHanded);
		offPose.transform().apply(offhand, offhandVariables, leftHanded);
		
		float mirrorFactor = leftHanded? -1 : 1;
		AnimationUtils.bobModelPart(mainhand, renderState.ageInTicks, mainPose.bobFactor() * mirrorFactor);
		AnimationUtils.bobModelPart(offhand, renderState.ageInTicks, offPose.bobFactor() * -mirrorFactor);
		
		if(renderState.attackTime < 0) return;
		ArmPose mainSwing = preferMain(usingOffhand, this.mainhandData.inMainhand().mainhandSwing(), this.offhandData.inOffhand().mainhandSwing());
		ArmPose offSwing  = preferMain(usingOffhand, this.mainhandData.inMainhand().offhandSwing(), this.offhandData.inOffhand().offhandSwing());
		
		mainSwing.apply(mainhand, renderState.attackTime, mainhandVariables, leftHanded);
		offSwing.apply(offhand, renderState.attackTime, offhandVariables, leftHanded);*/
	}
	@Unique
	ArmPose preferMain(ArmPose mainhand, ArmPose offhand) {
		return mainhand.isEmpty()? offhand : mainhand;
	}
	@Unique
	ArmPose preferMain(boolean usingOffhand, ArmPose mainhand, ArmPose offhand) {
		return (mainhand.isEmpty() || usingOffhand)? offhand : mainhand;
	}
}
