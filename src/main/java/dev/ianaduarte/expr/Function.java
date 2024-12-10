package dev.ianaduarte.expr;

public abstract class Function {
	public final int argc;
	public final boolean variadic;
	
	
	public Function(int argc, boolean variadic) {
		this.argc = argc;
		this.variadic = variadic;
	}
	
	public abstract double apply(double... args);
}
