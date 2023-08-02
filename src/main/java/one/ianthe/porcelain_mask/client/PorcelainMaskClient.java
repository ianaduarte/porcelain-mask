package one.ianthe.porcelain_mask.client;


import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;


public class PorcelainMaskClient implements ClientModInitializer{
	@Override
	public void onInitializeClient(ModContainer mod){
		ModelLoadingPlugin.register(new SpecialModelLoadingPlugin());
	}
}