package dev.ianaduarte.expr.ast;

public class TernopNode extends Node {
	public final Node cond;
	public final Node a;
	public final Node b;
	
	public TernopNode(Node cond, Node a, Node b) {
		this.cond = cond;
		this.a = a;
		this.b = b;
	}
	
	@Override
	public String stringRep() {
		return String.format("(%s)?(%s):(%s)", this.cond.stringRep(), this.a.stringRep(), this.b.stringRep());
	}
}
