package one.ianthe.porcelain_mask;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;

public class PorcelainUtil{
	public static float QUARTER_PI = Mth.HALF_PI / 2;
	
	public static <T extends LivingEntity> boolean isLeftHanded(T livingEntity){
		return livingEntity.getMainArm() != HumanoidArm.RIGHT;
	}
	public static float normalizeAngle(float radians){
		float normRadians = radians % Mth.TWO_PI;
		if(normRadians >= Mth.PI) normRadians -= Mth.TWO_PI;
		if(normRadians < -Mth.PI) normRadians += Mth.TWO_PI;
		return normRadians;
	}
	public static float lsine(float x){
		float f = (x % Mth.HALF_PI) / Mth.HALF_PI;
		float h = (x % Mth.PI < Mth.HALF_PI)? f : 1 - f;
		return (x % Mth.TWO_PI < Mth.PI)? h : -h;
	}
}
