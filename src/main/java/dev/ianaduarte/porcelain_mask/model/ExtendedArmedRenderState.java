package dev.ianaduarte.porcelain_mask.model;

import net.minecraft.world.entity.HumanoidArm;

public interface ExtendedArmedRenderState {
	void setData(HumanoidArm arm, ArmPosingData data);
	ArmPosingData getData(HumanoidArm arm);
}
