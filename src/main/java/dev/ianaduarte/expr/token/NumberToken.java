package dev.ianaduarte.expr.token;

public class NumberToken extends Token {
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
