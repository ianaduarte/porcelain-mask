package one.ianthe.porcelain_mask.registry;

import net.minecraft.client.resources.model.ModelResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class ContextualModelRegistry{
	private static final List<ModelResourceLocation> modelLocations = new ArrayList<>();
	
	public static ModelResourceLocation registerModel(ModelResourceLocation modelLocation){
		modelLocations.add(modelLocation);
		return modelLocation;
	}
	public static List<ModelResourceLocation> getModels(){
		return modelLocations;
	}
}
