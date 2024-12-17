package dev.ianaduarte.porcelain_mask.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ianaduarte.expr.ExpressionCompiler;
import dev.ianaduarte.porcelain_mask.util.PorcelainMth;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Map;
import java.util.Set;

public sealed interface Transform {
	Set<String> EXPRESSION_VARIABLES = Set.of(
		"headYaw",
		"headPitch",
		"armYaw",
		"armPitch",
		"armRoll",
		"swingDelta",
		"swingAmount"
	);
	ExpressionCompiler EXPRESSION_COMPILER = new ExpressionCompiler(PorcelainMth.EXPRESSION_COMPILER).registerVariables(EXPRESSION_VARIABLES);
	
	Transform EMPTY = Static.EMPTY;
	Codec<Transform> CODEC = Dynamic.CODEC.xmap(
		dynamic -> dynamic.isStatic()? new Static(dynamic) : dynamic,
		transform -> { throw new RuntimeException("Unreachable, probably..."); }
	);
	StreamCodec<ByteBuf, Transform> STREAM_CODEC = StreamCodec.of(
		(buff, transform) -> { throw new RuntimeException("Unreachable"); },
		(buff)            -> { throw new RuntimeException("Unreachable"); }
	);
	
	boolean overridesTransforms();
	boolean isEmpty();
	Vector3f getPos(Map<String, Double> variables, boolean mirror);
	Vector3f getRot(Map<String, Double> variables, boolean mirror);
	void apply(ModelPart part, Map<String, Double> variables, boolean mirror);
	void apply(PoseStack poseStack, Map<String, Double> variables, boolean mirror);
	
	final class Static implements Transform {
		public static final Static EMPTY = new Static(false, Vec3.ZERO, Vec3.ZERO);
		
		private final boolean overridesTransforms;
		private final Vec3 position;
		private final Vec3 rotation;
		private final Quaternionf compositeRotation;
		private final Quaternionf compositeRotationMirror;
		
		public Static(Dynamic dynamic) {
			this(dynamic.overridesTransforms, dynamic.position.toVec3(), dynamic.rotation.toVec3(PorcelainMth::toRadians));
		}
		public Static(boolean overridesTransforms, Vec3 position, Vec3 rotation) {
			this.overridesTransforms = overridesTransforms;
			this.position = position;
			this.rotation = rotation;
			this.compositeRotation = new Quaternionf().rotationZ((float)rotation.z).rotateX((float)rotation.x).rotateY((float)rotation.y);
			this.compositeRotationMirror = new Quaternionf(this.compositeRotation);
			this.compositeRotationMirror.y *= -1;
			this.compositeRotationMirror.z *= -1;
		}
		
		@Override
		public boolean overridesTransforms() {
			return this.overridesTransforms;
		}
		@Override
		public boolean isEmpty() {
			return this == EMPTY || (!this.overridesTransforms && (this.position.lengthSqr() == 0) && (this.rotation.lengthSqr() == 0));
		}
		
		@Override
		public Vector3f getPos(Map<String, Double> variables, boolean mirror) {
			double mirrorFactor = mirror? -1 : 1;
			return new Vector3f(
				(float)(this.position.x * mirrorFactor),
				(float)(this.position.y),
				(float)(this.position.z)
			);
		}
		@Override
		public Vector3f getRot(Map<String, Double> variables, boolean mirror) {
			double mirrorFactor = mirror? -1 : 1;
			return new Vector3f(
				(float)(this.rotation.x),
				(float)(this.rotation.y * mirrorFactor),
				(float)(this.rotation.z * mirrorFactor)
			);
		}
		
		@Override
		public void apply(ModelPart part, Map<String, Double> variables, boolean mirror) {
			float mirrorFactor = mirror? -1 : 1;
			
			if(this.overridesTransforms) {
				part.setRotation(0, 0, 0);
				//part.resetPose();
			}
			part.xRot += (float)(this.rotation.x);
			part.yRot += (float)(this.rotation.y) * mirrorFactor;
			part.zRot += (float)(this.rotation.z) * mirrorFactor;
			
			part.x    += (float)(this.position.x) * mirrorFactor;
			part.y    += (float)(this.position.y);
			part.z    += (float)(this.position.z);
		}
		@Override
		public void apply(PoseStack poseStack, Map<String, Double> variables, boolean mirror) {
			double mirrorFactor = mirror? -1 : 1;
			poseStack.translate(
				(position.x * mirrorFactor) * PorcelainMth.INV_SIXTEEN,
				position.y * PorcelainMth.INV_SIXTEEN,
				position.z * PorcelainMth.INV_SIXTEEN
			);
			poseStack.mulPose(mirror? this.compositeRotationMirror : this.compositeRotation);
		}
	}
	final class Dynamic implements Transform {
		public static final Dynamic EMPTY = new Dynamic(false, Motion.EMPTY, Motion.EMPTY);
		public static final Codec<Dynamic> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
				Codec.BOOL.optionalFieldOf("override_transforms", false).forGetter(Dynamic::overridesTransforms),
				Motion.CODEC.optionalFieldOf("position", Motion.EMPTY).forGetter(Dynamic::position),
				Motion.CODEC.optionalFieldOf("rotation", Motion.EMPTY).forGetter(Dynamic::rotation)
			).apply(instance, Dynamic::new)
		);
		public static final StreamCodec<ByteBuf, Dynamic> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL, Dynamic::overridesTransforms,
			Motion.STREAM_CODEC, Dynamic::position,
			Motion.STREAM_CODEC, Dynamic::rotation,
			Dynamic::new
		);
		
		private final boolean overridesTransforms;
		private final Motion position;
		private final Motion rotation;
		
		public Dynamic(boolean overridesTransforms, Motion position, Motion rotation) {
			this.overridesTransforms = overridesTransforms;
			this.position = position;
			this.rotation = rotation;
		}
		
		@Override
		public boolean overridesTransforms() {
			return this.overridesTransforms;
		}
		@Override
		public boolean isEmpty() {
			return this == EMPTY || (!this.overridesTransforms && this.rotation.isEmpty() && this.position.isEmpty());
		}
		public boolean isStatic() {
			return this.rotation.isStatic() && this.position.isStatic();
		}
		public Motion position() {
			return this.position;
		}
		public Motion rotation() {
			return this.rotation;
		}
		
		@Override
		public Vector3f getPos(Map<String, Double> variables, boolean mirror) {
			double mirrorFactor = mirror? -1 : 1;
			double xPos = this.position.x().setVariables(variables).evaluate() * mirrorFactor;
			double yPos = this.position.y().setVariables(variables).evaluate();
			double zPos = this.position.z().setVariables(variables).evaluate();
			
			return new Vector3f(
				(float)xPos,
				(float)yPos,
				(float)zPos
			);
		}
		@Override
		public Vector3f getRot(Map<String, Double> variables, boolean mirror) {
			double mirrorFactor = mirror? -1 : 1;
			//maybe this should be done automatically when a dynamic transform is decoded?
			//the toRadians thing that is.
			double xRot = PorcelainMth.toRadians(this.rotation.x().setVariables(variables).evaluate());
			double yRot = PorcelainMth.toRadians(this.rotation.y().setVariables(variables).evaluate()) * mirrorFactor;
			double zRot = PorcelainMth.toRadians(this.rotation.z().setVariables(variables).evaluate()) * mirrorFactor;
			
			return new Vector3f(
				(float)xRot,
				(float)yRot,
				(float)zRot
			);
		}
		
		@Override
		public void apply(ModelPart part, Map<String, Double> variables, boolean mirror) {
			float mirrorFactor = mirror? -1 : 1;
			
			if(this.overridesTransforms) {
				part.setRotation(0, 0, 0);
				//part.resetPose();
			}
			//variables.put("armYaw", Math.toDegrees(part.yRot));
			//variables.put("armPitch", Math.toDegrees(part.xRot));
			//variables.put("armRoll", Math.toDegrees(part.zRot));
			part.xRot += (float)(PorcelainMth.toRadians(this.rotation.x().setVariables(variables).evaluate()));
			part.yRot += (float)(PorcelainMth.toRadians(this.rotation.y().setVariables(variables).evaluate())) * mirrorFactor;
			part.zRot += (float)(PorcelainMth.toRadians(this.rotation.z().setVariables(variables).evaluate())) * mirrorFactor;
			part.x    += (float)(this.position.x().setVariables(variables).evaluate()) * mirrorFactor;
			part.y    += (float)(this.position.y().setVariables(variables).evaluate());
			part.z    += (float)(this.position.z().setVariables(variables).evaluate());
		}
		@Override
		public void apply(PoseStack poseStack, Map<String, Double> variables, boolean mirror) {
			//TODO: override transforms on a matrix?
			double mirrorFactor = mirror? -1 : 1;
			double xPos = this.position.x().setVariables(variables).evaluate() * mirrorFactor;
			double yPos = this.position.y().setVariables(variables).evaluate();
			double zPos = this.position.z().setVariables(variables).evaluate();
			
			double xRot = Math.toRadians(this.rotation.x().setVariables(variables).evaluate());
			double yRot = Math.toRadians(this.rotation.y().setVariables(variables).evaluate()) * mirrorFactor;
			double zRot = Math.toRadians(this.rotation.z().setVariables(variables).evaluate()) * mirrorFactor;
			
			poseStack.translate(xPos * PorcelainMth.INV_SIXTEEN, yPos * PorcelainMth.INV_SIXTEEN, zPos * PorcelainMth.INV_SIXTEEN);
			poseStack.mulPose(
				new Quaternionf()
					.rotateZ((float)zRot)
					.rotateX((float)xRot)
					.rotateY((float)yRot)
			);
		}
	}
}
