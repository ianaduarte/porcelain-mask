package one.ianthe.porcelain_mask.mixin.contextual_model;

import com.google.gson.JsonObject;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import one.ianthe.porcelain_mask.model.ContextualModel;
import one.ianthe.porcelain_mask.registry.SpecialModelRegistry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;

@Mixin({BlockModel.class, SimpleBakedModel.class})
public class ContextualModelImpl implements ContextualModel{
	@Unique
	HashMap<ItemDisplayContext, ResourceLocation> models;
	
	
	@Override
	@Nullable
	public ResourceLocation getModel(ItemDisplayContext context){
		return models.get(context);
	}
	
	@Override
	public HashMap<ItemDisplayContext, ResourceLocation> getAllModels(){
		return new HashMap<>(models);
	}
	
	@Override
	public void setContextualFromJson(JsonObject json){
		models = new HashMap<>();
		
		if(json.has("third_left" )) models.put(ItemDisplayContext.THIRD_PERSON_LEFT_HAND, new ResourceLocation(json.get("third_left").getAsString()));
		if(json.has("third_right")) models.put(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, new ResourceLocation(json.get("third_right").getAsString()));
		if(json.has("first_left" )) models.put(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, new ResourceLocation(json.get("first_left").getAsString()));
		if(json.has("first_right")) models.put(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, new ResourceLocation(json.get("first_right").getAsString()));
		if(json.has("head"       )) models.put(ItemDisplayContext.HEAD, new ResourceLocation(json.get("head").getAsString()));
		if(json.has("gui"        )) models.put(ItemDisplayContext.GUI, new ResourceLocation(json.get("gui").getAsString()));
		if(json.has("ground"     )) models.put(ItemDisplayContext.GROUND, new ResourceLocation(json.get("ground").getAsString()));
		if(json.has("fixed"      )) models.put(ItemDisplayContext.FIXED, new ResourceLocation(json.get("fixed").getAsString()));
		
		models.values().forEach(SpecialModelRegistry::registerModel);
	}
	
	@Override
	public void setContextualFromOther(ContextualModel other){
		this.models = other.getAllModels();
	}
	
	@Override
	public boolean isContextual(){
		return models != null && !models.values().isEmpty();
	}
}
