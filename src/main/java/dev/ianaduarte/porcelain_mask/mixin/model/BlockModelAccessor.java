package dev.ianaduarte.porcelain_mask.mixin.model;

import net.minecraft.client.renderer.block.model.BlockModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockModel.class)
public interface BlockModelAccessor{
	@Accessor
	BlockModel getParent();
}