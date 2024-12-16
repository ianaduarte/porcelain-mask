package dev.ianaduarte.porcelain_mask.mixin.pose;

import dev.ianaduarte.porcelain_mask.model.pose.PosingData;
import dev.ianaduarte.porcelain_mask.util.ExtModelWrapper;
import net.minecraft.client.resources.model.SimpleBakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SimpleBakedModel.class)
public class ModelWrapperMixin implements ExtModelWrapper {
	@Unique PosingData poses;
	@Override public void setPoses(PosingData poses) {
		this.poses = poses;
	}
	@Override public PosingData getPoses() {
		return this.poses;
	}
}
