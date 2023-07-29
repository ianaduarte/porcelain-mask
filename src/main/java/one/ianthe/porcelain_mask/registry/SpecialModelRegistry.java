package one.ianthe.porcelain_mask.registry;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class SpecialModelRegistry{
	private static final List<ResourceLocation> modelLocations = new ArrayList<>();
	
	public static ResourceLocation registerModel(ResourceLocation modelLocation){
		modelLocations.add(modelLocation);
		return modelLocation;
	}
	public static List<ResourceLocation> getModels(){
		return modelLocations;
	}
}
