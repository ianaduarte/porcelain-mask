package dev.ianaduarte.porcelain_mask.registry;

import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpecialModelRegistry{
	private static final Set<ResourceLocation> modelLocations = new HashSet<>();
	
	public static void registerModel(ResourceLocation modelLocation){
		modelLocations.add(modelLocation);
	}
	public static Set<ResourceLocation> getModels(){
		return modelLocations;
	}
}
