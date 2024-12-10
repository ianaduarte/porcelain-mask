package dev.ianaduarte.porcelain_mask.mixin.model;

import dev.ianaduarte.porcelain_mask.model.ArmPosingData;
import dev.ianaduarte.porcelain_mask.model.ExtendedBlockModelWrapper;
import net.minecraft.client.resources.model.SimpleBakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SimpleBakedModel.class)
public class ModelWrapperMixin implements ExtendedBlockModelWrapper {
	@Unique ArmPosingData poses;
	
	@Override
	public void setPoses(ArmPosingData poses) {
		this.poses = poses;
	}
	
	@Override
	public ArmPosingData getPoses() {
		return this.poses;
	}
}
