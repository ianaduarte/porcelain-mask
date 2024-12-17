package dev.ianaduarte.porcelain_mask.model;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ianaduarte.expr.Expression;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.UnaryOperator;


public record Motion(Expression x, Expression y, Expression z) {
	public static final Motion EMPTY = new Motion(Expression.EMPTY, Expression.EMPTY, Expression.EMPTY);
	
	public boolean isStatic() {
		return x.isConstant() && y.isConstant() && z.isConstant();
	}
	public boolean isEmpty() {
		return this == EMPTY || (x.isEmpty() && y.isEmpty() && z.isEmpty());
	}
	public Vec3 toVec3(UnaryOperator<Double> transformer) {
		return new Vec3(
			transformer.apply(x.evaluate()),
			transformer.apply(y.evaluate()),
			transformer.apply(z.evaluate())
		);
	}
	public Vec3 toVec3() {
		return new Vec3(x.evaluate(), y.evaluate(), z.evaluate());
	}
	
	private static final Codec<Expression> AXIS_CODEC = Codec.withAlternative(
		Codec.STRING.xmap(Transform.EXPRESSION_COMPILER::compile, Expression::stringRep),
		Codec.DOUBLE, Expression::ofNumber
	);
	private static final StreamCodec<ByteBuf, Expression> AXIS_STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(
		Transform.EXPRESSION_COMPILER::compile,
		Expression::stringRep
	);
	
	private static final Codec<Motion> LIST_CODEC = Codec.list(AXIS_CODEC, 3, 3).xmap(
		expressions -> new Motion(expressions.get(0), expressions.get(1), expressions.get(2)),
		motion      -> List.of(motion.x, motion.y, motion.z)
	);
	private static final Codec<Motion> OBJ_CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			AXIS_CODEC.optionalFieldOf("x", Expression.EMPTY).forGetter(Motion::x),
			AXIS_CODEC.optionalFieldOf("y", Expression.EMPTY).forGetter(Motion::y),
			AXIS_CODEC.optionalFieldOf("z", Expression.EMPTY).forGetter(Motion::z)
		).apply(instance, Motion::new)
	);
	public static final Codec<Motion> CODEC = Codec.withAlternative(LIST_CODEC, OBJ_CODEC);
	public static final StreamCodec<ByteBuf, Motion> STREAM_CODEC = StreamCodec.composite(
		AXIS_STREAM_CODEC, Motion::x,
		AXIS_STREAM_CODEC, Motion::y,
		AXIS_STREAM_CODEC, Motion::z,
		Motion::new
	);
}
