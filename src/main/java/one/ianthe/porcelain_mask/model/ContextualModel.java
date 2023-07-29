package one.ianthe.porcelain_mask.model;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public interface ContextualModel{
	@Nullable
	ResourceLocation getModel(ItemDisplayContext context);
	HashMap<ItemDisplayContext, ResourceLocation> getAllModels();
	
	void setContextualFromJson(JsonObject json);
	void setContextualFromOther(ContextualModel other);
	boolean isContextual();
}