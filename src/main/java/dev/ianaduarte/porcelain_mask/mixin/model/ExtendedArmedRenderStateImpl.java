package dev.ianaduarte.porcelain_mask.mixin.model;

import dev.ianaduarte.porcelain_mask.model.ArmPosingData;
import dev.ianaduarte.porcelain_mask.model.ExtendedArmedRenderState;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ArmedEntityRenderState.class)
public class ExtendedArmedRenderStateImpl implements ExtendedArmedRenderState {
	@Unique ArmPosingData leftData;
	@Unique ArmPosingData rightData;
	
	@Override
	public void setData(HumanoidArm arm, ArmPosingData data) {
		if(arm == HumanoidArm.RIGHT) {
			rightData = data;
		}
		else {
			leftData = data;
		}
	}
	@Override
	public ArmPosingData getData(HumanoidArm arm) {
		ArmPosingData posingData = (arm == HumanoidArm.RIGHT)? rightData : leftData;
		return (posingData == null)? ArmPosingData.EMPTY : posingData;
	}
}
