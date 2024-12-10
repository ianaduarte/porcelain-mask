package dev.ianaduarte.porcelain_mask.registry;

import dev.ianaduarte.porcelain_mask.PorcelainMask;
import dev.ianaduarte.porcelain_mask.model.ArmPose;
import dev.ianaduarte.porcelain_mask.model.ArmPosingData;
import dev.ianaduarte.porcelain_mask.model.ArmSwing;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.function.UnaryOperator;

public class PorcelainDataComponents {
	public static final DataComponentType<ArmPose> ARM_POSE = register(
		"arm_pose",
		builder -> builder.persistent(ArmPose.CODEC).networkSynchronized(ArmPose.STREAM_CODEC).cacheEncoding()
	);
	public static final DataComponentType<ArmSwing> ARM_SWING = register(
		"arm_swing",
		builder -> builder.persistent(ArmSwing.CODEC).networkSynchronized(ArmSwing.STREAM_CODEC).cacheEncoding()
	);
	public static final DataComponentType<ArmPosingData> ARM_POSES = register(
		"arm_poses",
		builder -> builder.persistent(ArmPosingData.CODEC).networkSynchronized(ArmPosingData.STREAM_CODEC).cacheEncoding()
	);
	
	private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> unaryOperator) {
		PorcelainMask.LOGGER.info("registering component::{}", PorcelainMask.getLocation(id));
		return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, PorcelainMask.getLocation(id), (unaryOperator.apply(DataComponentType.builder())).build());
	}
	
	public static void registerComponents() {
		PorcelainMask.LOGGER.info("registering data components");
	}
}
