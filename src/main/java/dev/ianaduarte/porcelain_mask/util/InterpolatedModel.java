package dev.ianaduarte.porcelain_mask.util;

import net.minecraft.world.entity.Entity;

public interface InterpolatedModel {
	void interpolate(Entity entity, float ticks);
}
