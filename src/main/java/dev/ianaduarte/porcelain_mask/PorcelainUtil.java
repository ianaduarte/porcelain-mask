package dev.ianaduarte.porcelain_mask;

import com.google.common.collect.ImmutableMap;
import dev.ianaduarte.expr.Function;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PorcelainUtil{
	public static double QUARTER_PI = Mth.HALF_PI / 2;
	public static double HALF_PI = Math.PI / 2;
	public static Map<String, Function> DEFAULT_FUNCTIONS = Map.ofEntries(
		Map.entry("sin"  , new Function(1, false){ @Override public double apply(double... args){ return Math.sin(args[0]); }}),
		Map.entry("lsin" , new Function(1, false){ @Override public double apply(double... args){ return lsine(args[0]); }}),
		Map.entry("cos"  , new Function(1, false){ @Override public double apply(double... args){ return Math.cos(args[0]); }}),
		Map.entry("tan"  , new Function(1, false){ @Override public double apply(double... args){ return Math.tan(args[0]); }}),
		Map.entry("sqrt" , new Function(1, false){ @Override public double apply(double... args){ return Math.sqrt(args[0]); }}),
		Map.entry("cbrt" , new Function(1, false){ @Override public double apply(double... args){ return Math.cbrt(args[0]); }}),
		Map.entry("nrt"  , new Function(2, false){ @Override public double apply(double... args){ return Math.pow(args[0], 1 / args[0]); }}),
		Map.entry("pow"  , new Function(2, false){ @Override public double apply(double... args){ return Math.pow(args[0], args[1]); }}),
		Map.entry("sqr"  , new Function(1, false){ @Override public double apply(double... args){ return args[0] * args[0]; }}),
		Map.entry("floor", new Function(1, false){ @Override public double apply(double... args){ return Math.floor(args[0]); }}),
		Map.entry("round", new Function(1, false){ @Override public double apply(double... args){ return Math.round(args[0]); }}),
		Map.entry("ceil" , new Function(1, false){ @Override public double apply(double... args){ return Math.ceil(args[0]); }}),
		Map.entry("torad", new Function(1, false){ @Override public double apply(double... args){ return Math.toRadians(args[0]); }}),
		Map.entry("todeg", new Function(1, false){ @Override public double apply(double... args){ return Math.toDegrees(args[0]); }}),
		Map.entry("grad" , new Function(3, true ){ @Override public double apply(double... args){ return gradient(args[0], Arrays.copyOfRange(args, 1, args.length)); }}),
		Map.entry("wrap" , new Function(3, false){ @Override public double apply(double... args){ return wrap(args[0], args[1], args[2]); }})
	);
	public static Map<String, Double> DEFAULT_CONSTANTS = Map.ofEntries(
		Map.entry("QPI", Math.PI / 4),
		Map.entry("HPI", Math.PI / 2),
		Map.entry("PI", Math.PI),
		Map.entry("TAU", Math.TAU)
	);
	
	public static <T extends ArmedEntityRenderState> boolean isLeftHanded(T livingEntity){
		return livingEntity.mainArm != HumanoidArm.RIGHT;
	}
	public static float normalizeAngle(float radians){
		float normRadians = radians % Mth.TWO_PI;
		if(normRadians >= Mth.PI) normRadians -= Mth.TWO_PI;
		if(normRadians < -Mth.PI) normRadians += Mth.TWO_PI;
		return normRadians;
	}
	
	public static float toRadians(float degrees){
		return degrees * Mth.DEG_TO_RAD;
	}
	public static float toDegrees(float radians){
		return radians * Mth.RAD_TO_DEG;
	}
	public static float lsine(float x){
		float f = (x % Mth.HALF_PI) / Mth.HALF_PI;
		float h = (x % Mth.PI < Mth.HALF_PI)? f : 1 - f;
		return (x % Mth.TWO_PI < Mth.PI)? h : -h;
	}
	public static double lsine(double x){
		double f = (x % HALF_PI) / HALF_PI;
		double h = (x % Math.PI < HALF_PI)? f : 1 - f;
		return (x % Math.TAU < Math.PI)? h : -h;
	}
	public static double gradient(double delta, double... values) {
		if(values.length == 0) throw new IllegalArgumentException("Gradient array cannot be empty.");
		if(delta <= 0) return values[0];
		if(delta >= 1) return values[values.length - 1];
		
		int index = (int)(delta * (values.length - 1));
		double t = delta * (values.length - 1) - index;
		return Mth.lerp(t, values[index], values[index + 1]);
	}
	public static double wrap(double x, double min, double max) {
		double range = (Math.max(max, min) - Math.min(min, max));
		return ((x % range) + range) % range + min;
	}
}
