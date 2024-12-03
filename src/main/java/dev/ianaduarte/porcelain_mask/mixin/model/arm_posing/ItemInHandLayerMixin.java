package dev.ianaduarte.porcelain_mask.mixin.model.arm_posing;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import dev.ianaduarte.porcelain_mask.PorcelainUtil;
import dev.ianaduarte.porcelain_mask.model.ArmPosingModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandLayer.class)
public abstract class ItemInHandLayerMixin<E extends LivingEntity, M extends EntityModel<E> & ArmedModel> extends RenderLayer<E, M>{
	
	public ItemInHandLayerMixin(RenderLayerParent<E, M> renderLayerParent){
		super(renderLayerParent);
	}
	@Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
	private void disableOffhandRendering(LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext displayContext, HumanoidArm arm, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci){
		boolean leftHand = arm == HumanoidArm.LEFT;
		boolean isLeftHanded = PorcelainUtil.isLeftHanded(livingEntity);
		
		boolean isUsingOffhand = livingEntity.isUsingItem() && livingEntity.getUsedItemHand() == InteractionHand.OFF_HAND;
		BakedModel mainModel = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(livingEntity.getMainHandItem());
		
		if(!isUsingOffhand && mainModel instanceof SimpleBakedModel){
			if(((ArmPosingModel)mainModel).hidesOffhandItem() && ((!isLeftHanded && leftHand) || (isLeftHanded && !leftHand))){
				ci.cancel();
			}
		}
	}
}
