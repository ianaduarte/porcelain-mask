package dev.ianaduarte.porcelain_mask.model.pose;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ianaduarte.porcelain_mask.model.Transform;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ArmPose(float bobFactor, Transform transform){
	public static final ArmPose EMPTY = new ArmPose(1, Transform.EMPTY);
	
	public static final Codec<ArmPose> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			Codec.FLOAT.optionalFieldOf("bob_factor", 0.0f).forGetter(ArmPose::bobFactor),
			RecordCodecBuilder.of(ArmPose::transform, MapCodec.assumeMapUnsafe(Transform.CODEC))
		).apply(instance, ArmPose::new)
	);
	public static final StreamCodec<ByteBuf, ArmPose> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.FLOAT, ArmPose::bobFactor,
		Transform.STREAM_CODEC, ArmPose::transform,
		ArmPose::new
	);
	
	public boolean isEmpty() {
		return this == EMPTY || (this.bobFactor == 1 && this.transform.isEmpty());
	}
}
