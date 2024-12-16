package dev.ianaduarte.porcelain_mask.util;

import dev.ianaduarte.expr.ExpressionCompiler;
import dev.ianaduarte.expr.Function;

import java.util.Arrays;
import java.util.Map;

public class PorcelainMth {
	public static final double INV_SIXTEEN = 1 / 16.0;
	public static final double QUARTER_PI  = Math.PI / 4.0;
	public static final double HALF_PI     = Math.PI / 2.0;
	public static final double PI          = Math.PI;
	public static final double TAU         = Math.TAU;
	public static final double DEG2RAD     = TAU / 360.0;
	public static final double RAG2DEG     = 360.0 / TAU;
	
	public static double toDegrees(double rad) { return rad * RAG2DEG; }
	public static double toRadians(double deg) { return deg * DEG2RAD; }
	
	public static double tsin(double t) {
		double f = (t % Math.PI) / HALF_PI;
		return (t % Math.TAU < Math.PI)? f : 1 - f;
	}
	public static double tcos(double t) {
		return tsin(t + HALF_PI);
	}
	public static double grad(double delta, double... values) {
		if(values.length == 0) throw new IllegalArgumentException("Gradient array cannot be empty.");
		if(delta <= 0) return values[0];
		if(delta >= 1) return values[values.length - 1];
		
		int index = (int)(delta * (values.length - 1));
		double t = delta * (values.length - 1) - index;
		double a = values[index];
		return a + (values[index + 1] - a) * t;
	}
	public static double wrap(double x, double min, double max) {
		double range = (Math.max(max, min) - Math.min(min, max));
		return ((x % range) + range) % range + min;
	}
	public static double sat(double x) {
		return ((x % 1) + 1) % 1;
	}
	
	
	public static final Map<String, Function> DEFAULT_EXPRESSSION_FUNCTIONS = Map.ofEntries(
		Map.entry("sin"  , new Function(1, false){ @Override public double apply(double... args){ return Math.sin(args[0]); }}),
		Map.entry("cos"  , new Function(1, false){ @Override public double apply(double... args){ return Math.cos(args[0]); }}),
		Map.entry("lsin" , new Function(1, false){ @Override public double apply(double... args){ return tsin(args[0]); }}),
		Map.entry("tcos" , new Function(1, false){ @Override public double apply(double... args){ return tcos(args[0]); }}),
		Map.entry("tan"  , new Function(1, false){ @Override public double apply(double... args){ return Math.tan(args[0]); }}),
		Map.entry("sqrt" , new Function(1, false){ @Override public double apply(double... args){ return Math.sqrt(args[0]); }}),
		Map.entry("cbrt" , new Function(1, false){ @Override public double apply(double... args){ return Math.cbrt(args[0]); }}),
		Map.entry("nrt"  , new Function(2, false){ @Override public double apply(double... args){ return Math.pow(args[0], 1 / args[0]); }}),
		Map.entry("pow"  , new Function(2, false){ @Override public double apply(double... args){ return Math.pow(args[0], args[1]); }}),
		Map.entry("sqr"  , new Function(1, false){ @Override public double apply(double... args){ return args[0] * args[0]; }}),
		Map.entry("floor", new Function(1, false){ @Override public double apply(double... args){ return Math.floor(args[0]); }}),
		Map.entry("round", new Function(1, false){ @Override public double apply(double... args){ return Math.round(args[0]); }}),
		Map.entry("ceil" , new Function(1, false){ @Override public double apply(double... args){ return Math.ceil(args[0]); }}),
		Map.entry("torad", new Function(1, false){ @Override public double apply(double... args){ return toRadians(args[0]); }}),
		Map.entry("todeg", new Function(1, false){ @Override public double apply(double... args){ return toDegrees(args[0]); }}),
		Map.entry("grad" , new Function(3, true ){ @Override public double apply(double... args){ return grad(args[0], Arrays.copyOfRange(args, 1, args.length)); }}),
		Map.entry("wrap" , new Function(3, false){ @Override public double apply(double... args){ return wrap(args[0], args[1], args[2]); }}),
		Map.entry("sat"  , new Function(1, false){ @Override public double apply(double... args){ return sat(args[0]); }})
	);
	public static Map<String, Double> DEFAULT_EXPRESSION_CONSTANTS = Map.ofEntries(
		Map.entry("QPI", QUARTER_PI),
		Map.entry("HPI", HALF_PI),
		Map.entry("PI", PI),
		Map.entry("TAU", TAU)
	);
	public static final ExpressionCompiler EXPRESSION_COMPILER = new ExpressionCompiler().registerFunctions(DEFAULT_EXPRESSSION_FUNCTIONS).registerConstants(DEFAULT_EXPRESSION_CONSTANTS);
}
