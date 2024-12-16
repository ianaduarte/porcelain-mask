package dev.ianaduarte.porcelain_mask.util;

import dev.ianaduarte.porcelain_mask.model.pose.PosingData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;

public interface ExtArmedRenderState {
	void setData(HumanoidArm arm, PosingData data);
	PosingData getData(InteractionHand hand);
	PosingData getData(HumanoidArm arm);
}