package one.ianthe.porcelain_mask;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;

public class PorcelainUtil{
	public static <T extends LivingEntity> boolean isLeftHanded(T livingEntity){
		return livingEntity.getMainArm() != HumanoidArm.RIGHT;
	}
	public static float angleDifference(float source, float target){
		return ((target - source) + Mth.PI) % Mth.TWO_PI - Mth.PI;
	}
	
}
