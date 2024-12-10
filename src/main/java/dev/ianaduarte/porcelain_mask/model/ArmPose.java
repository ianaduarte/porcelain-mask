package dev.ianaduarte.porcelain_mask.model;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ianaduarte.porcelain_mask.PorcelainUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;

public record ArmPose(boolean overrideTransforms, boolean hideOffhand, float xRot, float yRot, float zRot, float pitchFactor, float yawFactor, float bobFactor) {
	public static final ArmPose EMPTY = new ArmPose(false, false, 0, 0, 0, 0, 0, 1);
	public static final Codec<ArmPose> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			Codec.BOOL.optionalFieldOf("override_transforms", false).forGetter(ArmPose::overrideTransforms),
			Codec.BOOL.optionalFieldOf("hide_offhand", false).forGetter(ArmPose::hideOffhand),
			Codec.FLOAT.optionalFieldOf("x_rot", 0f).xmap(PorcelainUtil::toRadians, PorcelainUtil::toDegrees).forGetter(ArmPose::xRot),
			Codec.FLOAT.optionalFieldOf("y_rot", 0f).xmap(PorcelainUtil::toRadians, PorcelainUtil::toDegrees).forGetter(ArmPose::yRot),
			Codec.FLOAT.optionalFieldOf("z_rot", 0f).xmap(PorcelainUtil::toRadians, PorcelainUtil::toDegrees).forGetter(ArmPose::zRot),
			Codec.FLOAT.optionalFieldOf("pitch_factor", 0f).forGetter(ArmPose::pitchFactor),
			Codec.FLOAT.optionalFieldOf("yaw_factor", 0f).forGetter(ArmPose::yawFactor),
			Codec.FLOAT.optionalFieldOf("bob_factor", 0f).forGetter(ArmPose::bobFactor)
		).apply(instance, ArmPose::new)
	);
	public static final StreamCodec<ByteBuf, ArmPose> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.BOOL , ArmPose::overrideTransforms,
		ByteBufCodecs.BOOL , ArmPose::hideOffhand,
		ByteBufCodecs.FLOAT, ArmPose::xRot,
		ByteBufCodecs.FLOAT, ArmPose::yRot,
		ByteBufCodecs.FLOAT, ArmPose::zRot,
		ByteBufCodecs.FLOAT, ArmPose::pitchFactor,
		ByteBufCodecs.FLOAT, ArmPose::yawFactor,
		ByteBufCodecs.FLOAT, ArmPose::bobFactor,
		ArmPose::new
	);
	
	public void apply(ModelPart part, ModelPart head){
		float lookXRot = head.xRot * pitchFactor;
		float lookYRot = head.yRot * yawFactor;
		
		if(overrideTransforms){
			part.xRot = this.xRot + lookXRot;
			part.yRot = this.yRot + lookYRot;
			part.zRot = this.zRot;
			return;
		}
		part.xRot += this.xRot + lookXRot;
		part.yRot += this.yRot + lookYRot;
		part.zRot += this.zRot;
	}
	public ArmPose mirrored(){
		return new ArmPose(
			this.overrideTransforms, this.hideOffhand,
			this.xRot, -this.yRot, -this.zRot,
			this.pitchFactor, this.yawFactor, this.bobFactor
		);
	}
}
