package dev.ianaduarte.porcelain_mask.mixin.interp;

import dev.ianaduarte.porcelain_mask.model.ModelPartState;
import dev.ianaduarte.porcelain_mask.util.ExtEntity;
import dev.ianaduarte.porcelain_mask.util.InterpolatedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;
import java.util.function.Function;

@Mixin(EntityModel.class)
public abstract class InterpolatedModelMixin extends Model implements InterpolatedModel {
	
	public InterpolatedModelMixin(ModelPart root, Function<ResourceLocation, RenderType> renderType) {
		super(root, renderType);
	}
	
	@Override
	public void interpolate(Entity entity, float ticks) {
		ExtEntity extEntity = (ExtEntity)entity;
		List<ModelPart> allParts = this.allParts();
		ModelPartState tmp = new ModelPartState();
		
		int offset = 0;
		float[] animData = extEntity.getData();
		if(animData == null) {
			extEntity.initializeData(allParts.size() * ModelPartState.OFFSET);
			animData = extEntity.getData();
			
			for(ModelPart part : allParts) {
				part.resetPose();
				tmp.fetch(part);
				tmp.set(animData, offset);
				offset += ModelPartState.OFFSET;
			}
			extEntity.setData(animData);
			return;
		}
		
		float tickDelta = (float)(ticks - Math.floor(ticks));
		
		for(ModelPart part : allParts) {
			//god forbid a language have proper structs
			tmp.fetch(animData, offset);
			part.x = Mth.lerp(0.25f, tmp.xPos, part.x);
			part.y = Mth.lerp(0.25f, tmp.yPos, part.y);
			part.z = Mth.lerp(0.25f, tmp.zPos, part.z);
			part.xRot = Mth.rotLerpRad(0.25f, tmp.xRot, part.xRot);
			part.yRot = Mth.rotLerpRad(0.25f, tmp.yRot, part.yRot);
			part.zRot = Mth.rotLerpRad(0.25f, tmp.zRot, part.zRot);
			
			tmp.fetch(part);
			tmp.set(animData, offset);
			offset += ModelPartState.OFFSET;
		}
		extEntity.setData(animData);
	}
}
