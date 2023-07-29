package one.ianthe.porcelain_mask.mixin.contextual_model;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.block.model.BlockModel;
import one.ianthe.porcelain_mask.model.ContextualModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockModel.Deserializer.class)
public class BlockModelDeserializerMixin{
	@ModifyReturnValue(
		method = "deserialize",
		at = @At("RETURN")
	)
	public BlockModel porcelain_mask$deserializeContextual(BlockModel original, @Local(ordinal = 0)JsonObject jsonObject){
		if(jsonObject.has("context_overrides")){
			JsonObject contextOverrides = jsonObject.getAsJsonObject("context_overrides");
			ContextualModel contextual = (ContextualModel) original;
			contextual.setContextualFromJson(contextOverrides);
		}
		return original;
	}
}
