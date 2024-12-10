package dev.ianaduarte.porcelain_mask.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.renderer.item.BlockModelWrapper;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class ExtendedUnbakedCodec extends MapCodec<BlockModelWrapper.Unbaked> {
	@Override
	public <T> Stream<T> keys(DynamicOps<T> ops) {
		throw new RuntimeException("unimplemented");
	}
	//may god forgive my soul
	@Override
	public <T> DataResult<BlockModelWrapper.Unbaked> decode(DynamicOps<T> ops, MapLike<T> input) {
		JsonElement modelElement = (JsonElement)input.get("model");
		if(modelElement == null) return DataResult.error(() -> "AAAAAAAAAAAAAAAAAAAAAA");
		
		ResourceLocation model = ResourceLocation.parse(modelElement.getAsString());
		
		JsonElement tintsElement = (JsonElement)input.get("tints");
		AtomicReference<List<ItemTintSource>> tints = new AtomicReference<>(List.of());
		if(tintsElement != null) {
			ItemTintSources.CODEC.listOf().decode(JsonOps.INSTANCE, tintsElement).ifSuccess((p) -> tints.set(p.getFirst()));
		}
		BlockModelWrapper.Unbaked unbaked = new BlockModelWrapper.Unbaked(model, tints.get());
		
		JsonElement posesElement = (JsonElement)input.get("poses");
		AtomicReference<ArmPosingData> poses = new AtomicReference<>(ArmPosingData.EMPTY);
		if(posesElement != null) {
			ArmPosingData.CODEC.decode(JsonOps.INSTANCE, posesElement).ifSuccess((p) -> poses.set(p.getFirst()));
		}
		
		((ExtendedBlockModelWrapper)(Object)unbaked).setPoses(poses.get());
		return DataResult.success(unbaked);
	}
	
	@Override
	public <T> RecordBuilder<T> encode(BlockModelWrapper.Unbaked unbakedModel, DynamicOps<T> dynamicOps, RecordBuilder<T> recordBuilder) {
		recordBuilder.add("model", unbakedModel.model(), ResourceLocation.CODEC);
		recordBuilder.add("tints", unbakedModel.tints(), ItemTintSources.CODEC.listOf());
		recordBuilder.add("poses", ((ExtendedBlockModelWrapper)(Object)unbakedModel).getPoses(), ArmPosingData.CODEC);
		return recordBuilder;
	}
}
