package one.ianthe.porcelain_mask.mixin.model.contextual;

import com.google.gson.JsonObject;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import one.ianthe.porcelain_mask.PorcelainMask;
import one.ianthe.porcelain_mask.mixin.model.BlockModelAccessor;
import one.ianthe.porcelain_mask.model.ArmPosingModel;
import one.ianthe.porcelain_mask.model.ContextualModel;
import one.ianthe.porcelain_mask.registry.SpecialModelRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

@Mixin({BlockModel.class, SimpleBakedModel.class})
public class ContextualModelImpl implements ContextualModel{
	@Unique Map<ItemDisplayContext, ResourceLocation> overrides;
	
	@Override
	public ContextualModel getParent(){
		return ((ContextualModel)this) instanceof BlockModel? (ContextualModel)((BlockModelAccessor)this).getParent() : null;
	}
	
	@Override
	public ResourceLocation getModel(ItemDisplayContext context){
		if(overrides == null) return null;
		return overrides.get(context);
	}
	@Override
	public Map<ItemDisplayContext, ResourceLocation> getOverrides(){
		if(overrides == null && getParent() != null) return getParent().getOverrides();
		return this.overrides;
	}
	
	@Override
	public boolean hasOverrides(){
		return hasOverrides(false);
	}
	
	@Override
	public boolean hasOverrides(boolean includeAncestors){
		if(includeAncestors){
			boolean ancestorsHaveOverrides = getParent() != null && getParent().hasOverrides(true);
			return hasOverrides() || ancestorsHaveOverrides;
		}
		return overrides != null && !overrides.values().isEmpty();
	}
	
	@Override
	public void contextualFromJson(JsonObject json){
		this.overrides = new HashMap<>();
		for(ItemDisplayContext context : ItemDisplayContext.values()){
			String name = context.getSerializedName();
			
			if(json.has(name))overrides.put(context, ResourceLocation.parse(json.get(name).getAsString()));
		}
		overrides.values().forEach(SpecialModelRegistry::registerModel);
	}
	@Override
	public void contextualFromOther(ContextualModel other){
		this.overrides = new HashMap<>(other.getOverrides());
	}
}
