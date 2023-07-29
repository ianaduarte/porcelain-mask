package one.ianthe.porcelain_mask.mixin.arm_posing_models;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import one.ianthe.porcelain_mask.model.BlockModelParentGetter;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({BlockModel.class})
public class BlockModelParentGetterImpl implements BlockModelParentGetter{
	@Shadow @Nullable protected BlockModel parent;
	
	@Override
	@Nullable
	public BlockModel getParent(){
		return parent;
	}
}
