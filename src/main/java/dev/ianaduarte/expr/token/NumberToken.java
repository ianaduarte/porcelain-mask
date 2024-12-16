package dev.ianaduarte.expr.token;

public class NumberToken extends Token {
	public static final NumberToken EMPTY = new NumberToken(0.0);
	public final double value;
	
	public NumberToken(double value) {
		super(Type.NUMBER);
		this.value = value;
	}
	
	@Override
	public String toString() {
		return String.format("(NumberToken){ value: %f }", this.value);
	}
}
