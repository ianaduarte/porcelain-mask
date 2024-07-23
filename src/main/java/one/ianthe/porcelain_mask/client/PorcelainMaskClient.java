package one.ianthe.porcelain_mask.client;


import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;


public class PorcelainMaskClient{
	public void initialize() {
		ModelLoadingPlugin.register(new SpecialModelLoadingPlugin());
	}
}