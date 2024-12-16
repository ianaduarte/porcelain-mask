package dev.ianaduarte.porcelain_mask.registry;

import dev.ianaduarte.porcelain_mask.PorcelainMask;
import dev.ianaduarte.porcelain_mask.model.pose.PosingData;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import java.util.function.UnaryOperator;

public class DataComponents {
	public static final DataComponentType<PosingData> ARM_POSES = register(
		"arm_poses",
		builder -> builder.persistent(PosingData.CODEC).networkSynchronized(PosingData.STREAM_CODEC).cacheEncoding()
	);
	
	private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> unaryOperator) {
		PorcelainMask.LOGGER.info("registering component::{}", PorcelainMask.getLocation(id));
		return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, PorcelainMask.getLocation(id), (unaryOperator.apply(DataComponentType.builder())).build());
	}
	
	public static void registerComponents() {
		PorcelainMask.LOGGER.info("registering data components");
	}
}
