package dev.ianaduarte.porcelain_mask.model;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ianaduarte.expr.Expression;
import dev.ianaduarte.expr.ExpressionCompiler;
import dev.ianaduarte.porcelain_mask.PorcelainUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public record ArmSwing(ArmPose pose, SwingAxis xAxis, SwingAxis yAxis, SwingAxis zAxis) {
	public static final ArmSwing DEFAULT_MAINHAND = new ArmSwing(
		ArmPose.EMPTY,
		new SwingAxis(EasingType.VANILLA, Expression.EMPTY,  1.2f),
		new SwingAxis(EasingType.VANILLA, Expression.EMPTY,    2f),
		new SwingAxis(EasingType.VANILLA, Expression.EMPTY, -0.4f)
	);
	public static final ArmSwing DEFAULT_OFFHAND = new ArmSwing(
		ArmPose.EMPTY,
		new SwingAxis(EasingType.NONE, Expression.EMPTY, 0f),
		new SwingAxis(EasingType.NONE, Expression.EMPTY, 0f),
		new SwingAxis(EasingType.NONE, Expression.EMPTY, 0f)
	);
	public static final Codec<ArmSwing> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			ArmPose.CODEC.optionalFieldOf("pose", ArmPose.EMPTY).forGetter(ArmSwing::pose),
			SwingAxis.CODEC.optionalFieldOf("x_axis", SwingAxis.EMPTY).forGetter(ArmSwing::xAxis),
			SwingAxis.CODEC.optionalFieldOf("y_axis", SwingAxis.EMPTY).forGetter(ArmSwing::yAxis),
			SwingAxis.CODEC.optionalFieldOf("z_axis", SwingAxis.EMPTY).forGetter(ArmSwing::zAxis)
		).apply(instance, ArmSwing::new)
	);
	public static final StreamCodec<ByteBuf, ArmSwing> STREAM_CODEC = StreamCodec.composite(
		ArmPose.STREAM_CODEC, ArmSwing::pose,
		SwingAxis.STREAM_CODEC, ArmSwing::xAxis,
		SwingAxis.STREAM_CODEC, ArmSwing::yAxis,
		SwingAxis.STREAM_CODEC, ArmSwing::zAxis,
		ArmSwing::new
	);
	
	public void swing(ModelPart partToSwing, ModelPart head, float delta, boolean isLeftArm){
		if(delta <= 0) return;
		
		float yFlip = isLeftArm? -1 : 1;
		float zFlip = isLeftArm? -1 : 1;
		float bodySwing = Mth.sin(Mth.sqrt(delta) * Mth.TWO_PI) * 0.2F;
		this.pose.apply(partToSwing, head);
		
		switch(this.xAxis.easingType){
			case null -> {}
			case VANILLA -> {
				//sin((1 -((1-x)^4)) * PI) * amount + sin(x * PI) * (0.525 - 0.75pitch)
				float f = 1.0F - delta;
				f = 1.0F - (f * f) * (f * f);
				float g = Mth.sin(f * Mth.PI);
				//float h = Mth.sin(delta * Mth.PI) * -(head.xRot - 0.7F) * 0.75F;
				float h = Mth.sin(delta * Mth.PI) * (0.525f - 0.75f * head.xRot);
				
				partToSwing.xRot -= (g * this.xAxis.swingAmount + h);
			}
			default -> {
				float swingValue = this.xAxis.getValue(delta, head.xRot, head.yRot);
				partToSwing.xRot += swingValue;
			}
		}
		switch(this.yAxis.easingType){
			case null -> {}
			case VANILLA -> partToSwing.yRot += (bodySwing * this.yAxis.swingAmount) * yFlip;
			default -> {
				float swingValue = this.yAxis.getValue(delta, head.xRot, head.yRot);
				partToSwing.yRot += swingValue * yFlip;
			}
		}
		switch(this.zAxis.easingType){
			case null -> {}
			case VANILLA -> partToSwing.zRot += (Mth.sin(delta * Mth.PI) * this.zAxis.swingAmount) * zFlip;
			default -> {
				float swingValue = this.zAxis.getValue(delta, head.xRot, head.yRot);
				partToSwing.zRot += swingValue * zFlip;
			}
		}
	}
	
	private record SwingAxis(EasingType easingType, Expression equation, float swingAmount) {
		public static final Codec<SwingAxis> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
				EasingType.CODEC.optionalFieldOf("easing", EasingType.NONE).forGetter(SwingAxis::easingType),
				Codec.STRING.optionalFieldOf("equation", "").xmap(SwingAxis::buildExpr, Expression::stringRep).forGetter(SwingAxis::equation),
				Codec.FLOAT.optionalFieldOf("swing_amount", 0f).xmap(PorcelainUtil::toRadians, PorcelainUtil::toDegrees).forGetter(SwingAxis::swingAmount)
			).apply(instance, SwingAxis::new)
		);
		
		public static final StreamCodec<ByteBuf, SwingAxis> STREAM_CODEC = StreamCodec.composite(
			EasingType.STREAM_CODEC, SwingAxis::easingType,
			ByteBufCodecs.STRING_UTF8.map(SwingAxis::buildExpr, Expression::stringRep), SwingAxis::equation,
			ByteBufCodecs.FLOAT, SwingAxis::swingAmount,
			SwingAxis::new
		);
		public static final SwingAxis EMPTY = new SwingAxis(EasingType.NONE, Expression.EMPTY, 0);
		
		static Expression buildExpr(String equation) {
			if(equation == null) return Expression.EMPTY;
			return new ExpressionCompiler(equation)
				       .registerFunctions(PorcelainUtil.DEFAULT_FUNCTIONS)
				       .registerConstants(PorcelainUtil.DEFAULT_CONSTANTS)
				       .registerVariable("x")
				       .registerVariable("headPitch")
				       .registerVariable("headYaw")
				       .compile();
		}
		float getValue(float delta, float pitch, float yaw){
			return this.swingAmount * switch(this.easingType) {
				case NONE     -> 0.0f;//dw about it
				case VANILLA  -> delta;//dw about it
				case INSTANT  -> 1.0f;
				case LINEAR   -> PorcelainUtil.lsine(delta * Mth.PI);
				case SINE     -> Mth.sin(delta * Mth.PI);
				case EQUATION -> (float)equation.setVariable("x", delta).setVariable("headPitch", pitch).setVariable("headYaw", yaw).evaluate();
			};
		}
	}
	public enum EasingType implements StringRepresentable {
		NONE(0, "none"),
		VANILLA(1, "vanilla"),
		INSTANT(2, "instant"),
		LINEAR(3, "linear"),
		SINE(4, "sine"),
		EQUATION(5, "equation");
		
		public static final IntFunction<EasingType> BY_ID = ByIdMap.continuous(easingType -> easingType.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
		public static final Codec<EasingType> CODEC = StringRepresentable.fromEnum(EasingType::values);
		public static final StreamCodec<ByteBuf, EasingType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, EasingType::getId);
		
		private final int id;
		private final String name;
		EasingType(int id, String name) {
			this.id = id;
			this.name = name;
		}
		
		public int getId() {
			return id;
		}
		@Override
		@NotNull
		public String getSerializedName() {
			return name;
		}
	}
}
