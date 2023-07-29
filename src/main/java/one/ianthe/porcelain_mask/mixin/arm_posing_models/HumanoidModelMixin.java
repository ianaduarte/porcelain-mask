package one.ianthe.porcelain_mask.mixin.arm_posing_models;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import one.ianthe.porcelain_mask.PorcelainUtil;
import one.ianthe.porcelain_mask.model.ArmPose;
import one.ianthe.porcelain_mask.model.ArmPosingModel;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<E extends Entity> extends EntityModel<E>{
	@Shadow @Final public ModelPart head;
	@Shadow @Final public ModelPart body;
	@Shadow @Final public ModelPart rightArm;
	@Shadow @Final public ModelPart leftArm;
	
	@Unique private float leftSwingYawFactor    = 1.0f;
	@Unique private float leftSwingPitchFactor  = 1.0f;
	@Unique private float leftSwingRollFactor   = 1.0f;
	@Unique private float rightSwingYawFactor   = 1.0f;
	@Unique private float rightSwingPitchFactor = 1.0f;
	@Unique private float rightSwingRollFactor  = 1.0f;
	
	@Unique private boolean leftIgnoresBobbing  = false;
	@Unique private boolean rightIgnoresBobbing = false;
	
	@Unique private ArmPosingModel mainHandModel;
	@Unique private ArmPosingModel offhandModel;
	
	@Inject(
		method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V",
		at = @At("HEAD")
	)
	private <T extends LivingEntity> void updateArmPosingModels(
		T entity,
		float limbSwing, float limbSwingAmount,
		float ageInTicks,
		float netHeadYaw, float headPitch,
		CallbackInfo ci)
	{
		ItemModelShaper models  = Minecraft.getInstance().getItemRenderer().getItemModelShaper();
		if(models.getItemModel(entity.getMainHandItem()) instanceof SimpleBakedModel){
			mainHandModel = (ArmPosingModel) models.getItemModel(entity.getMainHandItem());
		}else{
			mainHandModel = ArmPosingModel.EMPTY;
		}
		
		if(models.getItemModel(entity.getOffhandItem()) instanceof SimpleBakedModel){
			offhandModel = (ArmPosingModel) models.getItemModel(entity.getOffhandItem());
		}else{
			offhandModel = ArmPosingModel.EMPTY;
		}
	}
	
	//posing
	@Inject(
		method = "poseRightArm",
		at = @At("TAIL"),
		cancellable = true
	)
	private <T extends LivingEntity> void poseRightArmMixin(T livingEntity, CallbackInfo ci){
		if(mainHandModel.hasPoses(true) || offhandModel.hasPoses(true)){
			boolean leftHanded = PorcelainUtil.isLeftHanded(livingEntity);
			ArmPose pose = (mainHandModel.hasPoses(true))? mainHandModel.getInMainhandRight(leftHanded) : offhandModel.getInOffhandRight(leftHanded);
			
			if(pose != null){
				float xRot = head.xRot * pose.getHeadPitchFactor();
				float yRot = PorcelainUtil.angleDifference(body.yRot, head.yRot) * pose.getHeadYawFactor();
				rightArm.xRot = pose.getPitch() + xRot;
				rightArm.yRot = pose.getYaw() + yRot;
				rightArm.zRot = pose.getRoll();
				
				rightSwingYawFactor = pose.getSwingYawFactor();
				rightSwingPitchFactor = pose.getSwingPitchFactor();
				rightSwingRollFactor = pose.getSwingRollFactor();
				
				rightIgnoresBobbing = pose.ignoresBobbing();
				ci.cancel();
				return;
			}
		}
		
		rightSwingYawFactor   = 1.0f;
		rightSwingPitchFactor = 1.0f;
		rightSwingRollFactor  = 1.0f;
		rightIgnoresBobbing   = false;
	}
	@Inject(
		method = "poseLeftArm",
		at = @At("TAIL"),
		cancellable = true
	)
	private <T extends LivingEntity> void poseLeftArmMixin(T livingEntity, CallbackInfo ci){
		if(mainHandModel.hasPoses(true) || offhandModel.hasPoses(true)){
			boolean leftHanded = PorcelainUtil.isLeftHanded(livingEntity);
			ArmPose pose = (mainHandModel.hasPoses(true))? mainHandModel.getInMainhandLeft(leftHanded) : offhandModel.getInOffhandLeft(leftHanded);
			
			if(pose != null){
				float xRot = head.xRot * pose.getHeadPitchFactor();
				float yRot = PorcelainUtil.angleDifference(body.yRot, head.yRot) * pose.getHeadYawFactor();
				leftArm.xRot = pose.getPitch() + xRot;
				leftArm.yRot = pose.getYaw() + yRot;
				leftArm.zRot = pose.getRoll();
				
				leftSwingYawFactor = pose.getSwingYawFactor();
				leftSwingPitchFactor = pose.getSwingPitchFactor();
				leftSwingRollFactor = pose.getSwingRollFactor();
				
				leftIgnoresBobbing = pose.ignoresBobbing();
				ci.cancel();
				return;
			}
		}
		leftSwingYawFactor   = 1.0f;
		leftSwingPitchFactor = 1.0f;
		leftSwingRollFactor  = 1.0f;
		leftIgnoresBobbing   = false;
	}
	
	//swinging
	@Inject(
		method = "setupAttackAnimation",
		at = @At("TAIL"),
		cancellable = true
	)
	private <T extends LivingEntity> void doubleSwing(T livingEntity, float ageInTicks, CallbackInfo ci, @Local(ordinal = 1) float g, @Local(ordinal = 2) float h){
		if(this.attackTime <= 0.0F || !(mainHandModel.mainhandSwingsBoth() || offhandModel.offhandSwingsBoth())){
			ci.cancel();
			return;
		}
		
		ModelPart m = PorcelainUtil.isLeftHanded(livingEntity)? rightArm : leftArm;
		float xFactor = PorcelainUtil.isLeftHanded(livingEntity)? rightSwingPitchFactor : leftSwingPitchFactor;
		float yFactor = PorcelainUtil.isLeftHanded(livingEntity)? rightSwingYawFactor : leftSwingYawFactor;
		float zFactor = PorcelainUtil.isLeftHanded(livingEntity)? rightSwingRollFactor : leftSwingRollFactor;
		
		if((mainHandModel.mainhandSwingsBoth()) || (offhandModel.offhandSwingsBoth())){
			m.xRot -= (g * 1.2F + h) * xFactor;
			m.yRot += (body.yRot * 2.0F) * yFactor;
			m.zRot -= (Mth.sin(attackTime * (float) Math.PI) * 0.4F) * zFactor;
		}
	}
	
	@Redirect(
		method = "setupAttackAnimation",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/model/geom/ModelPart;xRot:F",
			opcode = Opcodes.PUTFIELD,
			ordinal = 1
		)
	)
	private <T extends LivingEntity> void attackXRotMixin(ModelPart instance, float value, @Local(ordinal = 0) T livingEntity, @Local(ordinal = 1) float g, @Local(ordinal = 2) float h){
		float factor = PorcelainUtil.isLeftHanded(livingEntity)? leftSwingPitchFactor : rightSwingPitchFactor;
		instance.xRot -= (g * 1.2F + h) * factor;
	}
	
	@Redirect(
		method = "setupAttackAnimation",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/model/geom/ModelPart;yRot:F",
			opcode = Opcodes.PUTFIELD,
			ordinal = 4
		)
	)
	private <T extends LivingEntity> void attackYRotMixin(ModelPart instance, float value, @Local(ordinal = 0) T livingEntity){
		float factor = PorcelainUtil.isLeftHanded(livingEntity)? leftSwingYawFactor : rightSwingYawFactor;
		instance.yRot += (body.yRot * 2.0F) * factor;
	}
	
	@Redirect(
		method = "setupAttackAnimation",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/model/geom/ModelPart;zRot:F",
			opcode = Opcodes.PUTFIELD,
			ordinal = 0
		)
	)
	private <T extends LivingEntity> void attackZRotMixin(ModelPart instance, float value, @Local(ordinal = 0) T livingEntity){
		float factor = PorcelainUtil.isLeftHanded(livingEntity)? leftSwingRollFactor : rightSwingRollFactor;
		instance.zRot -= (Mth.sin(attackTime * (float) Math.PI) * 0.4F) * factor;
	}
	
	//bobbing
	@Redirect(
		method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/model/AnimationUtils;bobModelPart(Lnet/minecraft/client/model/geom/ModelPart;FF)V",
			ordinal = 0
		)
	)
	private void disableRightArmBobbing(ModelPart arm, float ageInTicks, float multiplier){
		if(!rightIgnoresBobbing){
			AnimationUtils.bobModelPart(arm, ageInTicks, multiplier);
		}
	}
	
	@Redirect(
		method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/model/AnimationUtils;bobModelPart(Lnet/minecraft/client/model/geom/ModelPart;FF)V",
			ordinal = 1
		)
	)
	private void disableLeftArmBobbing(ModelPart arm, float ageInTicks, float multiplier){
		if(!leftIgnoresBobbing){
			AnimationUtils.bobModelPart(arm, ageInTicks, multiplier);
		}
	}
}
