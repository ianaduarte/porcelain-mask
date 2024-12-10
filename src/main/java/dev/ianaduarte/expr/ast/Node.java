package dev.ianaduarte.expr.ast;

public abstract class Node {
	public abstract String stringRep();
	public enum OpType {
		COND("?", 1, false),
		EQ("==", 2, true),
		NEQ("!=", 2, true),
		AND("&", 3, true),
		OR("|", 4, true),
		GT(">", 5, true),
		LT("<", 5, true),
		GE(">=", 5, true),
		LE("<=", 5, true),
		ADD("+", 6, true),
		SUB("-", 6, true),
		MUL("*", 7, true),
		DIV("/", 7, true),
		MOD("%", 7, true),
		POW("^", 9, false),
		POS("+", 8, false),
		NEG("-", 8, false),
		INVALID("", -1, false);
		
		public final String symbol;
		public final int precedence;
		public final boolean leftAssociative;
		
		OpType(String symbol, int precedence, boolean leftAssociative) {
			this.symbol = symbol;
			this.precedence = precedence;
			this.leftAssociative = leftAssociative;
		}
		
		public static OpType opFromChar(char ch) {
			return switch(ch) {
				case '+' -> ADD;
				case '-' -> SUB;
				case '*' -> MUL;
				case '/' -> DIV;
				case '%' -> MOD;
				case '^' -> POW;
				default -> INVALID;
			};
		}
		public static OpType unopFromChar(char ch) {
			return switch(ch) {
				case '+' -> POS;
				case '-' -> NEG;
				default -> throw new IllegalArgumentException("Unexpected value: " + ch);
			};
		}
	}
}
