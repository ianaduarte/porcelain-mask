package dev.ianaduarte.expr.token;

public class ReferenceToken extends Token {
	public final String id;
	
	public ReferenceToken(Type type, String id) {
		super(type);
		switch(type) {
			case VARIABLE, FUNCTION -> {}
			default -> throw new IllegalArgumentException(
				"Invalid type for ReferenceToken?" +
				"Expected either VARIABLE or FUNCTION, but got " + type
			);
		}
		this.id = id;
	}
	@Override
	public String toString() {
		return String.format("(ReferenceToken){ id: '%s' }", this.id);
	}
}
