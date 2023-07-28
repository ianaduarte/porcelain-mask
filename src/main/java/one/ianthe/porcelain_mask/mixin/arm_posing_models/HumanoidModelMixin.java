package one.ianthe.porcelain_mask.mixin.arm_posing_models;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import one.ianthe.porcelain_mask.model.ArmPose;
import one.ianthe.porcelain_mask.model.IArmPosing;
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
	
	@Unique private float leftSwingYawFactor = Float.NaN;
	@Unique private float leftSwingPitchFactor = Float.NaN;
	@Unique private float leftSwingRollFactor = Float.NaN;
	@Unique private float rightSwingYawFactor = Float.NaN;
	@Unique private float rightSwingPitchFactor = Float.NaN;
	@Unique private float rightSwingRollFactor = Float.NaN;
	
	@Unique private boolean leftIgnoresBobbing = false;
	@Unique private boolean rightIgnoresBobbing = false;
	
	@Inject(
		method = "poseRightArm",
		at = @At("TAIL"),
		cancellable = true
	)
	private <T extends LivingEntity> void porcelain_mask$poseRightArmMixin(T livingEntity, CallbackInfo ci){
		ItemModelShaper models = Minecraft.getInstance().getItemRenderer().getItemModelShaper();
		IArmPosing mainBakedModel = (IArmPosing) models.getItemModel(livingEntity.getMainHandItem());
		IArmPosing offBakedModel = (IArmPosing) models.getItemModel(livingEntity.getOffhandItem());
		
		if(mainBakedModel.hasPoses()){
			ArmPose pose = mainBakedModel.getMainPoses()[1];
			if(pose != null){
				float xRot = pose.followsView()? head.xRot : 0;
				float yRot = pose.followsView()? head.yRot : 0;
				rightArm.xRot = pose.getPitch() + xRot;
				rightArm.yRot = pose.getYaw() + yRot;
				rightArm.zRot = pose.getRoll();
				
				rightSwingYawFactor = pose.getSwingYawFactor();
				rightSwingPitchFactor = pose.getSwingPitchFactor();
				rightSwingRollFactor = pose.getSwingRollFactor();
				rightIgnoresBobbing = pose.getIgnoresBobbing();
				ci.cancel();
				return;
			}
		} else if(offBakedModel.hasPoses()){
			ArmPose pose = offBakedModel.getOffPoses()[1];
			
			if(pose != null){
				float xRot = pose.followsView()? head.xRot : 0;
				float yRot = pose.followsView()? head.yRot : 0;
				rightArm.xRot = pose.getPitch() + xRot;
				rightArm.yRot = pose.getYaw() + yRot;
				rightArm.zRot = pose.getRoll();
				
				rightSwingYawFactor = pose.getSwingYawFactor();
				rightSwingPitchFactor = pose.getSwingPitchFactor();
				rightSwingRollFactor = pose.getSwingRollFactor();
				rightIgnoresBobbing = pose.getIgnoresBobbing();
				ci.cancel();
				return;
			}
		}
		rightSwingYawFactor = Float.NaN;
		rightSwingPitchFactor = Float.NaN;
		rightSwingRollFactor = Float.NaN;
		rightIgnoresBobbing = false;
	}
	@Inject(
		method = "poseLeftArm",
		at = @At("TAIL"),
		cancellable = true
	)
	private <T extends LivingEntity> void porcelain_mask$poseLeftArmMixin(T livingEntity, CallbackInfo ci){
		ItemModelShaper models = Minecraft.getInstance().getItemRenderer().getItemModelShaper();
		IArmPosing mainBakedModel = (IArmPosing) models.getItemModel(livingEntity.getMainHandItem());
		IArmPosing offBakedModel = (IArmPosing) models.getItemModel(livingEntity.getOffhandItem());
		
		if(mainBakedModel.hasPoses()){
			ArmPose pose = mainBakedModel.getMainPoses()[0];
			if(pose != null){
				float xRot = pose.followsView()? head.xRot : 0;
				float yRot = pose.followsView()? head.yRot : 0;
				leftArm.xRot = pose.getPitch() + xRot;
				leftArm.yRot = pose.getYaw() + yRot;
				leftArm.zRot = pose.getRoll();
				leftSwingYawFactor = pose.getSwingYawFactor();
				leftSwingPitchFactor = pose.getSwingPitchFactor();
				leftSwingRollFactor = pose.getSwingRollFactor();
				leftIgnoresBobbing = pose.getIgnoresBobbing();
				ci.cancel();
				return;
			}
		} else if(offBakedModel.hasPoses()){
			ArmPose pose = offBakedModel.getOffPoses()[0];
			
			if(pose != null){
				float xRot = pose.followsView()? head.xRot : 0;
				float yRot = pose.followsView()? head.yRot : 0;
				leftArm.xRot = pose.getPitch() + xRot;
				leftArm.yRot = pose.getYaw() + yRot;
				leftArm.zRot = pose.getRoll();
				leftSwingYawFactor = pose.getSwingYawFactor();
				leftSwingPitchFactor = pose.getSwingPitchFactor();
				leftSwingRollFactor = pose.getSwingRollFactor();
				leftIgnoresBobbing = pose.getIgnoresBobbing();
				ci.cancel();
				return;
			}
		}
		leftSwingYawFactor = Float.NaN;
		leftSwingPitchFactor = Float.NaN;
		leftSwingRollFactor = Float.NaN;
		leftIgnoresBobbing = false;
	}
	
	
	//swinging
	@Redirect(
		method = "setupAttackAnimation",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/model/geom/ModelPart;xRot:F",
			opcode = Opcodes.PUTFIELD,
			ordinal = 1
		)
	)
	private <T extends LivingEntity> void porcelain_mask$attackXRotMixin(ModelPart instance, float value, @Local(ordinal = 0) T livingEntity, @Local(ordinal = 1) float g, @Local(ordinal = 2) float h){
		HumanoidArm humanoidArm = getAttackArm(livingEntity);
		float factor = (humanoidArm == HumanoidArm.LEFT)? leftSwingPitchFactor : rightSwingPitchFactor;
		if(!Float.isNaN(factor)){
			instance.xRot -= (g * 1.2F + h) * factor;
			return;
		}
		instance.xRot = value;
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
	private <T extends LivingEntity> void porcelain_mask$attackYRotMixin(ModelPart instance, float value, @Local(ordinal = 0) T livingEntity){
		HumanoidArm humanoidArm = getAttackArm(livingEntity);
		float factor = (humanoidArm == HumanoidArm.LEFT)? leftSwingYawFactor : rightSwingYawFactor;
		
		if(!Float.isNaN(factor)){
			instance.yRot += (body.yRot * 2.0F) * factor;
			return;
		}
		instance.yRot = value;
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
	private <T extends LivingEntity> void porcelain_mask$attackZRotMixin(ModelPart instance, float value, @Local(ordinal = 0) T livingEntity){
		HumanoidArm humanoidArm = getAttackArm(livingEntity);
		float factor = (humanoidArm == HumanoidArm.LEFT)? leftSwingRollFactor : rightSwingRollFactor;
		if(!Float.isNaN(factor)){
			instance.zRot -= (Mth.sin(attackTime * (float) Math.PI) * 0.4F) * factor;
			return;
		}
		instance.zRot = value;
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
	private void porcelain_mask$disableRightArmBobbing(ModelPart arm, float ageInTicks, float multiplier){
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
	private void porcelain_mask$disableLeftArmBobbing(ModelPart arm, float ageInTicks, float multiplier){
		if(!leftIgnoresBobbing){
			AnimationUtils.bobModelPart(arm, ageInTicks, multiplier);
		}
	}
	
	
	@Shadow
	protected abstract <T extends LivingEntity> HumanoidArm getAttackArm(T entity);
}
