package dev.ianaduarte.porcelain_mask.mixin.pose;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ianaduarte.porcelain_mask.model.pose.PosingData;
import dev.ianaduarte.porcelain_mask.util.ExtModelWrapper;
import dev.ianaduarte.porcelain_mask.util.ExtUnbakedCodec;
import net.minecraft.client.renderer.item.BlockModelWrapper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Function;

@Mixin(BlockModelWrapper.Unbaked.class)
public class UnbakedWrapperMixin implements ExtModelWrapper {
	@Unique PosingData poses;
	@Override public void setPoses(PosingData poses) {
		this.poses = poses;
	}
	@Override public PosingData getPoses() {
		return this.poses;
	}
	
	@ModifyExpressionValue(method = "bake", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/item/ItemModel$BakingContext;bake(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/resources/model/BakedModel;"))
	private BakedModel guhh(BakedModel original) {
		if(original instanceof SimpleBakedModel simpleBakedModel) {
			((ExtModelWrapper)simpleBakedModel).setPoses(this.poses);
		}
		return original;
	}
	@Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;mapCodec(Ljava/util/function/Function;)Lcom/mojang/serialization/MapCodec;", opcode = Opcodes.INVOKESTATIC))
	private static MapCodec<BlockModelWrapper.Unbaked> extendCodec(Function<RecordCodecBuilder.Instance<BlockModelWrapper.Unbaked>, ? extends App<RecordCodecBuilder.Mu<BlockModelWrapper.Unbaked>, BlockModelWrapper.Unbaked>> builder) {
		return new ExtUnbakedCodec();
	}
}