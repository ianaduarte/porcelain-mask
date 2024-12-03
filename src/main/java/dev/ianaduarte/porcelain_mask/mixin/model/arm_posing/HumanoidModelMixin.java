package dev.ianaduarte.porcelain_mask.mixin.model.arm_posing;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import dev.ianaduarte.porcelain_mask.PorcelainMask;
import dev.ianaduarte.porcelain_mask.PorcelainUtil;
import dev.ianaduarte.porcelain_mask.model.ArmPosingModel;
import dev.ianaduarte.porcelain_mask.model.HoldingContext;
import dev.ianaduarte.porcelain_mask.model.ModelPartPose;
import dev.ianaduarte.porcelain_mask.model.ModelPartSwing;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<E extends LivingEntity> extends EntityModel<E>{
	@Shadow @Final public ModelPart head;
	@Shadow @Final public ModelPart body;
	@Shadow @Final public ModelPart rightArm;
	@Shadow @Final public ModelPart leftArm;
	
	@Shadow public HumanoidModel.ArmPose leftArmPose;
	@Shadow public HumanoidModel.ArmPose rightArmPose;
	
	@Unique ArmPosingModel mainhandModel;
	@Unique ArmPosingModel offhandModel;
	
	@Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("HEAD"))
	private void updateModels(E entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci){
		ItemModelShaper modelShaper = Minecraft.getInstance().getItemRenderer().getItemModelShaper();
		
		BakedModel modelInMainhand = modelShaper.getItemModel(entity.getMainHandItem());
		BakedModel modelInOffhand  = modelShaper.getItemModel(entity.getOffhandItem());
		
		mainhandModel = (modelInMainhand instanceof SimpleBakedModel)? (ArmPosingModel)modelInMainhand : ArmPosingModel.EMPTY;
		offhandModel  = (modelInOffhand  instanceof SimpleBakedModel)? (ArmPosingModel)modelInOffhand  : ArmPosingModel.EMPTY;
	}
	
	@Inject(method = "poseRightArm", at = @At("TAIL"))
	private void poseRightArm(E livingEntity, CallbackInfo ci){
		if(!(mainhandModel.hasPoses() || offhandModel.hasPoses())) return;
		
		boolean isLeftHanded = PorcelainUtil.isLeftHanded(livingEntity);
		HoldingContext context = (mainhandModel.hasPoses())? HoldingContext.MAINHAND_RIGHT : HoldingContext.OFFHAND_RIGHT;
		ModelPartPose pose = getPose(context, isLeftHanded);
		
		if(pose != null){
			//offset item holding angle
			if(rightArmPose == HumanoidModel.ArmPose.ITEM && !isLeftHanded) rightArm.xRot += Mth.PI / 10;
			
			pose.apply(rightArm, head);
		}
	}
	@Inject(method = "poseLeftArm", at = @At("TAIL"))
	private void poseLeftArm(E livingEntity, CallbackInfo ci){
		if(!(mainhandModel.hasPoses() || offhandModel.hasPoses())) return;
		
		boolean isLeftHanded = PorcelainUtil.isLeftHanded(livingEntity);
		HoldingContext context = (mainhandModel.hasPoses())? HoldingContext.MAINHAND_LEFT : HoldingContext.OFFHAND_LEFT;
		ModelPartPose pose = getPose(context, isLeftHanded);
		
		if(pose != null){
			//offset item holding angle
			if(leftArmPose == HumanoidModel.ArmPose.ITEM && isLeftHanded) leftArm.xRot += Mth.PI / 10;
			
			pose.apply(leftArm, head);
		}
	}

	@Inject(method = "setupAttackAnimation", at = @At("HEAD"), cancellable = true)
	private void attackAnimation(E livingEntity, float ageInTicks, CallbackInfo ci){
		if(attackTime >= 0 && (mainhandModel.hasCustomMainhandSwings() || offhandModel.hasCustomOffhandSwing())){
			boolean isLeftHanded = PorcelainUtil.isLeftHanded(livingEntity);
			boolean offHand = !mainhandModel.hasCustomMainhandSwings();
			
			HoldingContext rightContext = offHand? HoldingContext.OFFHAND_RIGHT : HoldingContext.MAINHAND_RIGHT;
			HoldingContext leftContext = offHand? HoldingContext.OFFHAND_LEFT : HoldingContext.MAINHAND_LEFT;
			
			ModelPartSwing rightSwing = getSwing(rightContext, isLeftHanded);
			ModelPartSwing leftSwing = getSwing(leftContext, isLeftHanded);
			
			if(rightSwing == null) rightSwing = (isLeftHanded)? ModelPartSwing.DEFAULT_OFFHAND : ModelPartSwing.DEFAULT_MAINHAND;
			if(leftSwing  == null) leftSwing = (isLeftHanded)? ModelPartSwing.DEFAULT_MAINHAND : ModelPartSwing.DEFAULT_OFFHAND;
			
			rightSwing.swing(rightArm, leftArm, head, body, attackTime, false);
			leftSwing.swing(rightArm, leftArm, head, body, attackTime, true);
			ci.cancel();
		}
	}
	
	@Redirect(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/AnimationUtils;bobModelPart(Lnet/minecraft/client/model/geom/ModelPart;FF)V", ordinal = 0))
	private void modifyRightBobbing(ModelPart modelPart, float ageInTicks, float multiplier, @Local(ordinal = 0) E entity){
		if(mainhandModel.hasCustomMainhandBobbing() || offhandModel.hasCustomOffhandBobbing()){
			boolean isLeftHanded = PorcelainUtil.isLeftHanded(entity);
			
			HoldingContext context = (mainhandModel.hasPoses())? HoldingContext.MAINHAND_RIGHT : HoldingContext.OFFHAND_RIGHT;
			Float bob = getBobbingMultiplier(context, isLeftHanded);
			
			if(bob != null){
				AnimationUtils.bobModelPart(modelPart, ageInTicks, bob);
				return;
			}
		}
		AnimationUtils.bobModelPart(modelPart, ageInTicks, multiplier);
	}
	@Redirect(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/AnimationUtils;bobModelPart(Lnet/minecraft/client/model/geom/ModelPart;FF)V", ordinal = 1))
	private void modifyLeftBobbing(ModelPart modelPart, float ageInTicks, float multiplier, @Local(ordinal = 0) E entity){
		if(mainhandModel.hasCustomMainhandBobbing() || offhandModel.hasCustomOffhandBobbing()){
			boolean isLeftHanded = PorcelainUtil.isLeftHanded(entity);
			
			HoldingContext context = (mainhandModel.hasPoses())? HoldingContext.MAINHAND_LEFT : HoldingContext.OFFHAND_LEFT;
			Float bob = getBobbingMultiplier(context, isLeftHanded);
			
			if(bob != null){
				AnimationUtils.bobModelPart(modelPart, ageInTicks, -bob);
				return;
			}
		}
		AnimationUtils.bobModelPart(modelPart, ageInTicks, multiplier);
	}
	
	@Unique
	private ModelPartPose getPose(HoldingContext context, boolean leftHanded){
		if(context.isOffhand()) return offhandModel.getPose(context, leftHanded);
		return mainhandModel.getPose(context, leftHanded);
	}
	@Unique
	private ModelPartSwing getSwing(HoldingContext context, boolean leftHanded){
		if(context.isOffhand()) return offhandModel.getSwing(context, leftHanded);
		return mainhandModel.getSwing(context, leftHanded);
	}
	@Unique
	private Float getBobbingMultiplier(HoldingContext context, boolean leftHanded){
		if(context.isOffhand()) return offhandModel.getBobbingMultiplier(context, leftHanded);
		return mainhandModel.getBobbingMultiplier(context, leftHanded);
	}
	
	@Shadow protected abstract HumanoidArm getAttackArm(E entity);
	@Shadow protected abstract ModelPart getArm(HumanoidArm side);
}
