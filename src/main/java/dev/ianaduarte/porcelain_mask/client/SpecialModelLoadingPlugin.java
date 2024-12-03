package dev.ianaduarte.porcelain_mask.client;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import dev.ianaduarte.porcelain_mask.registry.SpecialModelRegistry;

import java.util.function.Consumer;

public class SpecialModelLoadingPlugin implements ModelLoadingPlugin{
	@Override
	public void onInitializeModelLoader(Context pluginContext){
		pluginContext.addModels(SpecialModelRegistry.getModels());
	}
}
