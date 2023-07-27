package one.ianthe.porcelain_mask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

import net.minecraft.resources.ResourceLocation;

public class PorcelainMask implements ModInitializer {
	public static final String MOD_ID = "porcelain-mask";
	public static final Logger LOGGER = LoggerFactory.getLogger("PorcelainMask");

	public static ResourceLocation getLocation(String path){
		return new ResourceLocation(MOD_ID, path);
	}

	@Override
	public void onInitialize(ModContainer mod) {
	}
}