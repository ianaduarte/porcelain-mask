package one.ianthe.porcelain_mask.client;

import net.fabricmc.fabric.api.client.model.ExtraModelProvider;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import one.ianthe.porcelain_mask.registry.ContextualModelRegistry;

import java.util.function.Consumer;

public class ContextualModelProvider implements ExtraModelProvider{
	@Override
	public void provideExtraModels(ResourceManager manager, Consumer<ResourceLocation> out){
		for(ModelResourceLocation modelLocation : ContextualModelRegistry.getModels()){
			out.accept(modelLocation);
		}
	}
}
