package dev.ianaduarte.expr.ast;

public class BinopNode extends Node {
	public final Node.OpType type;
	public final Node lhs, rhs;
	
	public BinopNode(Node.OpType type, Node lhs, Node rhs) {
		switch(type) {
			case COND, POS, NEG -> throw new IllegalArgumentException("Expected a binary op type but got " + type);
		}
		this.type = type;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	
	@Override
	public String stringRep() {
		return String.format("(%s)%s(%s)", this.lhs.stringRep(), this.type.symbol, this.rhs.stringRep());
	}
}
