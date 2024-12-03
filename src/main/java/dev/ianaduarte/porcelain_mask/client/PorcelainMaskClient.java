package dev.ianaduarte.porcelain_mask.client;


import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;


public class PorcelainMaskClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ModelLoadingPlugin.register(new SpecialModelLoadingPlugin());
	}
}