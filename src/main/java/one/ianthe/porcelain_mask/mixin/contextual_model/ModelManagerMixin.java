package one.ianthe.porcelain_mask.mixin.contextual_model;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import one.ianthe.porcelain_mask.model.IResourceLocationModelGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin({ModelManager.class})
public class ModelManagerMixin implements IResourceLocationModelGetter{
	@Shadow private Map<ResourceLocation, BakedModel> bakedRegistry;
	@Shadow private BakedModel missingModel;
	
	@Override
	public BakedModel getModel(ResourceLocation location) {
		return bakedRegistry.getOrDefault(location, missingModel);
	}
}
