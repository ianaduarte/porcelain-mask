package dev.ianaduarte.expr.ast;

public class NumNode extends Node {
	public static final Node ZERO = new NumNode(0);
	public final double value;
	
	public NumNode(double value) {
		this.value = value;
	}
	
	@Override
	public String stringRep() {
		return String.format("%.16g", value).replaceAll("0+$", "").replaceAll("\\.$", "");
	}
}
