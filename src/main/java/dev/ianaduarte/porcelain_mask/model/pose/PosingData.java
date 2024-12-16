package dev.ianaduarte.porcelain_mask.model.pose;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record PosingData(PoseSet inMainhand, PoseSet inOffhand) {
	public static final PosingData EMPTY = new PosingData(PoseSet.EMPTY, PoseSet.EMPTY);
	
	public static final Codec<PosingData> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			PoseSet.CODEC.optionalFieldOf("in_mainhand", PoseSet.EMPTY).forGetter(PosingData::inMainhand),
			PoseSet.CODEC.optionalFieldOf("in_offhand", PoseSet.EMPTY).forGetter(PosingData::inOffhand)
		).apply(instance, PosingData::new)
	);
	public static final StreamCodec<ByteBuf, PosingData> STREAM_CODEC = StreamCodec.composite(
		PoseSet.STREAM_CODEC, PosingData::inMainhand,
		PoseSet.STREAM_CODEC, PosingData::inOffhand,
		PosingData::new
	);
	
	public boolean isEmpty() {
		return this == EMPTY || (this.inMainhand.isEmpty() && this.inOffhand.isEmpty());
	}
	
	//should probably use somethign else for the item's pose but whatever
	public record PoseSet(boolean hideOther, ArmPose mainhand, ArmPose offhand, ArmPose item) {
		public static PoseSet EMPTY = new PoseSet(false, ArmPose.EMPTY, ArmPose.EMPTY, ArmPose.EMPTY);
		
		public static final Codec<PoseSet> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
				Codec.BOOL.optionalFieldOf("hide_other", false).forGetter(PoseSet::hideOther),
				ArmPose.CODEC.optionalFieldOf("mainhand", ArmPose.EMPTY).forGetter(PoseSet::mainhand),
				ArmPose.CODEC.optionalFieldOf("offhand", ArmPose.EMPTY).forGetter(PoseSet::offhand),
				ArmPose.CODEC.optionalFieldOf("item", ArmPose.EMPTY).forGetter(PoseSet::offhand)
			).apply(instance, PoseSet::new)
		);
		public static final StreamCodec<ByteBuf, PoseSet> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL, PoseSet::hideOther,
			ArmPose.STREAM_CODEC, PoseSet::mainhand,
			ArmPose.STREAM_CODEC, PoseSet::offhand,
			ArmPose.STREAM_CODEC, PoseSet::item,
			PoseSet::new
		);
		
		public boolean isEmpty() {
			return this == EMPTY || (!this.hideOther && this.mainhand.isEmpty() && this.offhand.isEmpty() && this.item.isEmpty());
		}
	}
}
