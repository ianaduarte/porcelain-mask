package dev.ianaduarte.expr.token;

public class CharToken extends Token{
	public final char ch;
	
	public CharToken(Type type, char ch) {
		super(type);
		switch(type) {
			case OPERATOR, LPAREN, RPAREN, LBRACE, RBRACE, LCURLY, RCURLY, COMMA -> {}
			default -> throw new IllegalArgumentException(
				"Invalid type for CharToken? Got " + type
			);
		}
		this.ch = ch;
	}
	@Override
	public String toString() {
		return String.format("(CharToken){ ch: '%s' }", this.ch);
	}
}
