package one.ianthe.porcelain_mask.mixin.contextual_model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import one.ianthe.porcelain_mask.item.IContextualModelProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin{
	@Shadow @Final private ItemModelShaper itemModelShaper;
	
	@ModifyVariable(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/resources/model/BakedModel;getTransforms()Lnet/minecraft/client/renderer/block/model/ItemTransforms;",
			ordinal = 0,
			shift = At.Shift.BEFORE
		
		),
		ordinal = 0,
		argsOnly = true
	)
	private BakedModel contextualModelHandling(BakedModel originalModel, ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay){
		if(itemStack.getItem() instanceof IContextualModelProvider modelProvider){
			ModelResourceLocation location = modelProvider.getModel(displayContext);
			if(location != null){
				originalModel = itemModelShaper.getModelManager().getModel(location);
			}
		}
		return originalModel;
	}
}
