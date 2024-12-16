package dev.ianaduarte.expr.token;

public abstract class Token {
	public final Type type;
	
	public Token(Type type) {
		this.type = type;
	}
	@Override
	public abstract String toString();
	
	public enum Type {
		NUMBER,
		OPERATOR,
		FUNCTION,
		CONSTANT,
		VARIABLE,
		LPAREN,
		RPAREN,
		LBRACE,
		RBRACE,
		LCURLY,
		RCURLY,
		COMMA;
		
		public boolean isComma() {
			return this == COMMA;
		}
		public boolean isOperator() {
			return this == OPERATOR;
		}
		public boolean isOpening() {
			return switch(this) {
				case LPAREN, LBRACE, LCURLY -> true;
				default -> false;
			};
		}
		
		public Type opposite() {
			return switch(this) {
				case LPAREN -> RPAREN;
				case RPAREN -> LPAREN;
				case LBRACE -> RBRACE;
				case RBRACE -> LBRACE;
				case LCURLY -> RCURLY;
				case RCURLY -> LCURLY;
				default -> throw new RuntimeException();
			};
		}
	}
}
