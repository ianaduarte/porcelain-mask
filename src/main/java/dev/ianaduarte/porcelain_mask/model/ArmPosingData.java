package dev.ianaduarte.porcelain_mask.model;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record ArmPosingData(PoseSet inMainhand, PoseSet inOffhand) {
	public static final ArmPosingData EMPTY = new ArmPosingData(PoseSet.EMPTY_MAIN, PoseSet.EMPTY_OFF);
	public static final Codec<ArmPosingData> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			PoseSet.CODEC.optionalFieldOf("in_mainhand", PoseSet.EMPTY_MAIN).forGetter(ArmPosingData::inMainhand),
			PoseSet.CODEC.optionalFieldOf("in_offhand", PoseSet.EMPTY_OFF).forGetter(ArmPosingData::inOffhand)
		).apply(instance, ArmPosingData::new)
	);
	public static final StreamCodec<ByteBuf, ArmPosingData> STREAM_CODEC = StreamCodec.composite(
		PoseSet.STREAM_CODEC, ArmPosingData::inMainhand,
		PoseSet.STREAM_CODEC, ArmPosingData::inOffhand,
		ArmPosingData::new
	);
	
	public record PoseSet(ArmPose mainhand, ArmPose offhand, ArmSwing mainhandSwing, ArmSwing offhandSwing) {
		public static PoseSet EMPTY_MAIN = new PoseSet(ArmPose.EMPTY, ArmPose.EMPTY, ArmSwing.DEFAULT_MAINHAND, ArmSwing.DEFAULT_OFFHAND);
		public static PoseSet EMPTY_OFF  = new PoseSet(ArmPose.EMPTY, ArmPose.EMPTY, ArmSwing.DEFAULT_OFFHAND, ArmSwing.DEFAULT_MAINHAND);
		public static final Codec<PoseSet> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
				ArmPose.CODEC.optionalFieldOf("mainhand", ArmPose.EMPTY).forGetter(PoseSet::mainhand),
				ArmPose.CODEC.optionalFieldOf("offhand", ArmPose.EMPTY).forGetter(PoseSet::offhand),
				ArmSwing.CODEC.optionalFieldOf("mainhand_swing", ArmSwing.DEFAULT_MAINHAND).forGetter(PoseSet::mainhandSwing),
				ArmSwing.CODEC.optionalFieldOf("offhand_swing", ArmSwing.DEFAULT_OFFHAND).forGetter(PoseSet::offhandSwing)
			).apply(instance, PoseSet::new)
		);
		public static final StreamCodec<ByteBuf, PoseSet> STREAM_CODEC = StreamCodec.composite(
			ArmPose.STREAM_CODEC, PoseSet::mainhand,
			ArmPose.STREAM_CODEC, PoseSet::offhand,
			ArmSwing.STREAM_CODEC, PoseSet::mainhandSwing,
			ArmSwing.STREAM_CODEC, PoseSet::offhandSwing,
			PoseSet::new
		);
	}
}
