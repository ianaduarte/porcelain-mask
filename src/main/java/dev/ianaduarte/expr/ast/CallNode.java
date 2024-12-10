package dev.ianaduarte.expr.ast;

import dev.ianaduarte.expr.Function;

public class CallNode extends Node {
	public final Function func;
	public final String id;
	public final Node[] args;
	
	public CallNode(Function func, String id, Node[] args) {
		this.func = func;
		this.id = id;
		this.args = args;
	}
	
	@Override
	public String stringRep() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.id);
		builder.append('(');
		for(int i = 0; i < this.args.length; i++) {
			Node arg = this.args[i];
			builder.append(arg.stringRep());
			
			if(i < this.args.length - 1) builder.append(',');
		}
		builder.append(')');
		return builder.toString();
	}
}
