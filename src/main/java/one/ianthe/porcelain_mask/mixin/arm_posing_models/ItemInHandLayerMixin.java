package one.ianthe.porcelain_mask.mixin.arm_posing_models;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import one.ianthe.porcelain_mask.PorcelainUtil;
import one.ianthe.porcelain_mask.model.ArmPosingModel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemInHandLayer.class)
public class ItemInHandLayerMixin{
	@Shadow @Final private ItemInHandRenderer itemInHandRenderer;
	
	@Redirect(
		method = "renderArmWithItem",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
		)
	)
	private void porcelain_mask$killRendering(ItemInHandRenderer instance, LivingEntity entity, ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int seed){
		boolean isLeftHanded = PorcelainUtil.isLeftHanded(entity);
		boolean isUsingOffhand = entity.isUsingItem() && entity.getUsedItemHand() == InteractionHand.OFF_HAND;
		BakedModel mainModel = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(entity.getMainHandItem());
		
		if(!isUsingOffhand && mainModel instanceof SimpleBakedModel && ((ArmPosingModel) mainModel).hidesOffhandItem()){
			if((!isLeftHanded && leftHand) || (isLeftHanded && !leftHand)) return;
		}
		
		instance.renderItem(entity, itemStack, displayContext, leftHand, poseStack, buffer, seed);
	}
}