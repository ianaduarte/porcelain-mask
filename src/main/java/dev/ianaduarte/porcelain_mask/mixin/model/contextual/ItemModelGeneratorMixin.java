package dev.ianaduarte.porcelain_mask.mixin.model.contextual;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import dev.ianaduarte.porcelain_mask.model.ContextualModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemModelGenerator.class)
public class ItemModelGeneratorMixin{
	@ModifyReturnValue(method = "generateBlockModel", at = @At(value = "RETURN", ordinal = 0))
	public BlockModel generateContextualModel(BlockModel original, @Local(ordinal = 0) BlockModel model){
		ContextualModel contextual = (ContextualModel)model;
		
		if(contextual.hasOverrides(true)){
			ContextualModel originalContextual = (ContextualModel)original;
			originalContextual.contextualFromOther(contextual);
			
			return (BlockModel)originalContextual;
		}
		return original;
	}
}
