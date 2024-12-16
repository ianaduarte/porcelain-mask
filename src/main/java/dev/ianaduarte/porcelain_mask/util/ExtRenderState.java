package dev.ianaduarte.porcelain_mask.util;

import net.minecraft.world.entity.Entity;

public interface ExtRenderState {
	void setOwner(Entity owner);
	Entity getOwner();
}
