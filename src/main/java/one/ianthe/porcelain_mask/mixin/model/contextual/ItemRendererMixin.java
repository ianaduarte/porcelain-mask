package one.ianthe.porcelain_mask.mixin.model.contextual;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import one.ianthe.porcelain_mask.model.ContextualModel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin{
	@Shadow @Final private ItemModelShaper itemModelShaper;
	
	@ModifyVariable(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/BakedModel;getTransforms()Lnet/minecraft/client/renderer/block/model/ItemTransforms;", shift = At.Shift.BEFORE), ordinal = 0, argsOnly = true)
	private BakedModel contextualModelHandling(BakedModel originalModel, ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay){
		if(!(originalModel instanceof SimpleBakedModel)) return originalModel;
		
		ContextualModel contextual = (ContextualModel)originalModel;
		if(contextual.hasOverrides()){
			ResourceLocation location = contextual.getModel(displayContext);
			if(location != null) return itemModelShaper.getModelManager().getModel(location);
		}
		return originalModel;
	}
}
