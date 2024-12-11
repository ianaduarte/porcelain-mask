package dev.ianaduarte.expr;

import dev.ianaduarte.expr.ast.*;

import java.util.Map;

public class Expression {
	public static final Expression EMPTY = new Expression(null, null, "", NumNode.ZERO);
	private final Map<String, Function> functions;
	private final Map<String, Double> variables;
	
	public final String stringRep;
	private final Node op;
	
	public static Expression ofNumber(double n) {
		return new Expression(null, null, Double.toString(n), new NumNode(n));
	}
	public Expression(Map<String, Function> functions, Map<String, Double> variables, String stringRep, Node op) {
		this.functions = functions;
		this.variables = variables;
		this.stringRep = stringRep;
		this.op = op;
	}
	
	public double evaluate() {
		if(this.op == null) return 0;
		return this.op instanceof NumNode numOp? numOp.value : Expression.evaluate(this.variables, this.functions, this.op);
	}
	public static double evaluate(Map<String, Double> variables, Map<String, Function> functions, Node op) {
		return switch(op) {
			case BinopNode binop -> {
				double lValue = evaluate(variables, functions, binop.lhs);
				double rValue = evaluate(variables, functions, binop.rhs);
				
				yield switch(binop.type) {
					case ADD -> lValue + rValue;
					case SUB -> lValue - rValue;
					case MUL -> lValue * rValue;
					case DIV -> lValue / rValue;
					case MOD -> lValue % rValue;
					case POW -> Math.pow(lValue, rValue);
					default -> throw new RuntimeException();
				};
			}
			case UnopNode unop -> {
				double value = evaluate(variables, functions, unop.value);
				
				yield switch(unop.type) {
					case POS -> +value;
					case NEG -> -value;
					default -> throw new RuntimeException();
				};
			}
			case CallNode call -> {
				double[] args = new double[call.args.length];
				for(int i = 0; i < args.length; i++) args[i] = evaluate(variables, functions, call.args[i]);
				yield functions.get(call.id).apply(args);
			}
			case NumNode num -> num.value;
			case VarNode var -> variables.get(var.id);
			default -> throw new RuntimeException();
		};
	}
	
	public Expression setVariable(String name, double value) {
		this.variables.put(name, value);
		return this;
	}
	
	public String stringRep() {
		return this.stringRep == null? "" : this.stringRep;
	}
	
	public boolean isStatic() {
		return this.op instanceof NumNode;
	}
}
