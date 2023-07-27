package one.ianthe.porcelain_mask.client;

import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;


public class PorcelainMaskClient implements ClientModInitializer{
	@Override
	public void onInitializeClient(ModContainer mod){
		ModelLoadingRegistry.INSTANCE.registerModelProvider(new ContextualModelProvider());
	}
}