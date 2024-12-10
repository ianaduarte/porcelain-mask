package dev.ianaduarte.porcelain_mask.mixin.model;

import dev.ianaduarte.porcelain_mask.model.ArmPosingData;
import dev.ianaduarte.porcelain_mask.model.ExtendedArmedRenderState;
import dev.ianaduarte.porcelain_mask.model.ExtendedBlockModelWrapper;
import dev.ianaduarte.porcelain_mask.registry.PorcelainDataComponents;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmedEntityRenderState.class)
public class ArmedRenderStateMixin {
	
	@Inject(method = "extractArmedEntityRenderState", at = @At("TAIL"))
	private static void fetchInfo(LivingEntity livingEntity, ArmedEntityRenderState armedEntityRenderState, ItemModelResolver itemModelResolver, CallbackInfo ci) {
		ExtendedArmedRenderState extRenderState = (ExtendedArmedRenderState)armedEntityRenderState;
		ItemStack leftItem  = livingEntity.getItemHeldByArm(HumanoidArm.LEFT);
		ItemStack rightItem = livingEntity.getItemHeldByArm(HumanoidArm.RIGHT);
		
		ExtendedBlockModelWrapper leftModel = ((ExtendedBlockModelWrapper)((ItemStackRenderStateAccessor.LayerRenderStateAccessor)((ItemStackRenderStateAccessor)armedEntityRenderState.leftHandItem).callFirstLayer()).getModel());
		ExtendedBlockModelWrapper rightModel = ((ExtendedBlockModelWrapper)((ItemStackRenderStateAccessor.LayerRenderStateAccessor)((ItemStackRenderStateAccessor)armedEntityRenderState.rightHandItem).callFirstLayer()).getModel());
		extRenderState.setData(HumanoidArm.LEFT, leftItem.getOrDefault(PorcelainDataComponents.ARM_POSES, leftModel == null? ArmPosingData.EMPTY : leftModel.getPoses()));
		extRenderState.setData(HumanoidArm.RIGHT, rightItem.getOrDefault(PorcelainDataComponents.ARM_POSES, rightModel == null? ArmPosingData.EMPTY : rightModel.getPoses()));
	}
}
