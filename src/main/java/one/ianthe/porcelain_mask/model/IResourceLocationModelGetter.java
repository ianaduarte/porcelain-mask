package one.ianthe.porcelain_mask.model;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;

public interface IResourceLocationModelGetter{
	BakedModel getModel(ResourceLocation location);
}
