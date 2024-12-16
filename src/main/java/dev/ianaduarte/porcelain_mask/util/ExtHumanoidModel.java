package dev.ianaduarte.porcelain_mask.util;

import dev.ianaduarte.porcelain_mask.model.pose.PosingData;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.InteractionHand;

public interface ExtHumanoidModel {
	ModelPart getHand(InteractionHand hand);
	PosingData getPosingData(InteractionHand hand);
}
