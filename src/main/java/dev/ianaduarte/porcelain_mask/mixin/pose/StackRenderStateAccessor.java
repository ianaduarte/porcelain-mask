package dev.ianaduarte.porcelain_mask.mixin.pose;

import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.BakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemStackRenderState.class)
public interface StackRenderStateAccessor {
	@Invoker
	ItemStackRenderState.LayerRenderState callFirstLayer();
	
	@Mixin(ItemStackRenderState.LayerRenderState.class)
	interface LayerRenderStateAccessor {
		@Accessor
		BakedModel getModel();
	}
}
