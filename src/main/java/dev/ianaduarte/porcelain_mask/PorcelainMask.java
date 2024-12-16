package dev.ianaduarte.porcelain_mask;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PorcelainMask implements ModInitializer {
	public static final String MOD_ID = "porcelain-mask";
	public static final Logger LOGGER = LoggerFactory.getLogger("PorcelainMask");
	
	public static ResourceLocation getLocation(String path){
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
	
	@Override
	public void onInitialize() {
	}
}