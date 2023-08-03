package one.ianthe.porcelain_mask.model;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;

import java.util.Map;

public interface ContextualModel{
	ContextualModel getParent();
	
	ResourceLocation getModel(ItemDisplayContext context);
	Map<ItemDisplayContext, ResourceLocation> getOverrides();
	
	boolean hasOverrides();
	boolean hasOverrides(boolean includeAncestors);
	
	void contextualFromJson(JsonObject json);
	void contextualFromOther(ContextualModel other);
}
