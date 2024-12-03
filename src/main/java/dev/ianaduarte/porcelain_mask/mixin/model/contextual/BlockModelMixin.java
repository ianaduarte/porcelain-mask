package dev.ianaduarte.porcelain_mask.mixin.model.contextual;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BakedModel;
import dev.ianaduarte.porcelain_mask.model.ContextualModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockModel.class)
public class BlockModelMixin{
	@ModifyReturnValue(method = "bake(Lnet/minecraft/client/resources/model/ModelBaker;Lnet/minecraft/client/renderer/block/model/BlockModel;Ljava/util/function/Function;Lnet/minecraft/client/resources/model/ModelState;Z)Lnet/minecraft/client/resources/model/BakedModel;", at = @At(value = "RETURN", ordinal = 1))
	public BakedModel bakeContextualModel(BakedModel original){
		ContextualModel contextual = (ContextualModel)this;
		
		if(contextual.hasOverrides(true)){
			ContextualModel originalContextual = (ContextualModel)original;
			originalContextual.contextualFromOther(contextual);
		}
		return original;
	}
}
