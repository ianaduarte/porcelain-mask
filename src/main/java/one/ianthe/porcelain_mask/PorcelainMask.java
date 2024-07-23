package one.ianthe.porcelain_mask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import net.minecraft.resources.ResourceLocation;

public class PorcelainMask {
	public static final String MOD_ID = "porcelain-mask";
	public static final Logger LOGGER = LoggerFactory.getLogger("PorcelainMask");

	public static ResourceLocation getLocation(String path){
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	public void initialize() {
	}
}