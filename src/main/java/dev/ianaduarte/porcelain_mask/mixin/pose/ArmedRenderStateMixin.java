package dev.ianaduarte.porcelain_mask.mixin.pose;

import dev.ianaduarte.porcelain_mask.model.pose.PosingData;
import dev.ianaduarte.porcelain_mask.registry.DataComponents;
import dev.ianaduarte.porcelain_mask.util.ExtArmedRenderState;
import dev.ianaduarte.porcelain_mask.util.ExtModelWrapper;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmedEntityRenderState.class)
public class ArmedRenderStateMixin implements ExtArmedRenderState {
	@Shadow public HumanoidArm mainArm;
	@Unique private PosingData leftData;
	@Unique private PosingData rightData;
	
	@Override
	public void setData(HumanoidArm arm, PosingData data) {
		if(arm == HumanoidArm.LEFT) leftData = data;
		else rightData = data;
	}
	
	@Override
	public PosingData getData(InteractionHand hand) {
		PosingData posingData = (mainArm == HumanoidArm.LEFT)? ((hand == InteractionHand.MAIN_HAND)? leftData : rightData) : ((hand == InteractionHand.MAIN_HAND)? rightData : leftData);
		return (posingData == null)? PosingData.EMPTY : posingData;
	}
	
	@Override
	public PosingData getData(HumanoidArm arm) {
		PosingData posingData = (arm == HumanoidArm.RIGHT)? rightData : leftData;
		return (posingData == null)? PosingData.EMPTY : posingData;
	}
	
	@Inject(method = "extractArmedEntityRenderState", at = @At("TAIL"))
	private static void fetchInfo(LivingEntity livingEntity, ArmedEntityRenderState armedEntityRenderState, ItemModelResolver itemModelResolver, CallbackInfo ci) {
		ExtArmedRenderState extRenderState = (ExtArmedRenderState) armedEntityRenderState;
		ItemStack leftItem  = livingEntity.getItemHeldByArm(HumanoidArm.LEFT);
		ItemStack rightItem = livingEntity.getItemHeldByArm(HumanoidArm.RIGHT);
		
		ExtModelWrapper leftModel  = ((ExtModelWrapper)((StackRenderStateAccessor.LayerRenderStateAccessor)((StackRenderStateAccessor)armedEntityRenderState.leftHandItem).callFirstLayer()).getModel());
		ExtModelWrapper rightModel = ((ExtModelWrapper)((StackRenderStateAccessor.LayerRenderStateAccessor)((StackRenderStateAccessor)armedEntityRenderState.rightHandItem).callFirstLayer()).getModel());
		extRenderState.setData(HumanoidArm.LEFT, leftItem.getOrDefault(DataComponents.ARM_POSES, leftModel == null? PosingData.EMPTY : leftModel.getPoses()));
		extRenderState.setData(HumanoidArm.RIGHT, rightItem.getOrDefault(DataComponents.ARM_POSES, rightModel == null? PosingData.EMPTY : rightModel.getPoses()));
	}
}
