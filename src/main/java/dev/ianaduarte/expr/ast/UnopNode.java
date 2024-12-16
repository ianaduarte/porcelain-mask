package dev.ianaduarte.expr.ast;

public class UnopNode extends Node {
	public final Node.OpType type;
	public final Node value;
	
	public UnopNode(OpType type, Node value) {
		switch(type) {
			case COND, POS, NEG -> {}
			default -> throw new IllegalArgumentException("Expected a unary op type but got " + type);
		}
		
		this.type = type;
		this.value = value;
	}
	
	@Override
	public String stringRep() {
		return String.format("%s(%s)", this.type.symbol, this.value.stringRep());
	}
}
