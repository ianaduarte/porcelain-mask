package dev.ianaduarte.expr.ast;

public class VarNode extends Node {
	public final String id;
	
	public VarNode(String id) {
		this.id = id;
	}
	
	@Override
	public String stringRep() {
		return id;
	}
}
