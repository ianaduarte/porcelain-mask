package dev.ianaduarte.expr;

import dev.ianaduarte.expr.ast.*;
import dev.ianaduarte.expr.token.CharToken;
import dev.ianaduarte.expr.token.NumberToken;
import dev.ianaduarte.expr.token.ReferenceToken;
import dev.ianaduarte.expr.token.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ExpressionCompiler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExpressionCompiler.class);
	private final Set<String> variables;
	private final Map<String, Double> constants;
	private final Map<String, Function> functions;
	
	private final char[] expression;
	private final int expressionLength;
	private int at = 0;
	private Token lastToken;
	
	public ExpressionCompiler(String expression) {
		this.expression = expression.trim().toCharArray();
		this.expressionLength = this.expression.length;
		
		variables = new HashSet<>(4);
		constants = new HashMap<>(4);
		functions = new HashMap<>(4);
	}
	
	public ExpressionCompiler registerVariables(Set<String> variables) {
		this.variables.addAll(variables);
		return this;
	}
	public ExpressionCompiler registerConstants(Map<String, Double> constants) {
		this.constants.putAll(constants);
		return this;
	}
	public ExpressionCompiler registerFunctions(Map<String, Function> functions) {
		this.functions.putAll(functions);
		return this;
	}
	
	public ExpressionCompiler registerVariable(String name) {
		this.variables.add(name);
		return this;
	}
	public ExpressionCompiler registerConstant(String name, double value) {
		this.constants.put(name, value);
		return this;
	}
	public ExpressionCompiler registerFunction(String name, Function function) {
		this.functions.put(name, function);
		return this;
	}
	
	
	public Expression compile() {
		for(String var : this.variables) {
			if(this.functions.containsKey(var) || this.constants.containsKey(var)) {
				LOGGER.error("Multiple declarations of identifier {}", var);
				return Expression.EMPTY;
			}
		}
		Map<String, Double> variables = new HashMap<>();
		this.variables.forEach((var) -> variables.put(var, 0.0));
		nextToken();
		
		Node expr = fold(parseExpr());
		return new Expression(this.functions, variables, expr.stringRep(), expr);
	}
	
	private Node fold(Node node) {
		return switch(node) {
			case BinopNode binop -> {
				Node lFolded = fold(binop.lhs);
				Node rFolded = fold(binop.rhs);
				
				if(lFolded instanceof NumNode lNum && rFolded instanceof NumNode rNum) {
					double folded = switch(binop.type) {
						case ADD -> lNum.value + rNum.value;
						case SUB -> lNum.value - rNum.value;
						case MUL -> lNum.value * rNum.value;
						case DIV -> lNum.value / rNum.value;
						case MOD -> lNum.value % rNum.value;
						case POW -> Math.pow(lNum.value, rNum.value);
						default -> {
							LOGGER.error("Unimplemented binary operator {}", binop.type);
							yield lNum.value;
						}
					};
					yield new NumNode(folded);
				}
				if(lFolded == binop.lhs && rFolded == binop.rhs) yield node;
				yield new BinopNode(binop.type, lFolded, rFolded);
			}
			case UnopNode unop -> {
				Node folded = fold(unop.value);
				
				if(folded instanceof NumNode num) yield switch(unop.type) {
					case POS -> new NumNode(+num.value);
					case NEG -> new NumNode(-num.value);
					default -> {
						LOGGER.error("Unimplemented unary operator {}", unop.type);
						yield num;
					}
				};
				yield new UnopNode(unop.type, folded);
			}
			case CallNode call -> {
				var foldedArgs = Arrays.stream(call.args).map(this::fold).toList().toArray(new Node[0]);
				var constArgs  = new double[foldedArgs.length];
				
				boolean allConst = true;
				for(int i = 0; i < foldedArgs.length; i++) {
					if(!(foldedArgs[i] instanceof NumNode)) {
						allConst = false;
						break;
					}
					constArgs[i] = ((NumNode)foldedArgs[i]).value;
				}
				if(allConst) yield new NumNode(call.func.apply(constArgs));
				yield new CallNode(call.func, call.id, foldedArgs);
			}
			default -> node;
		};
	}
	
	private Node parsePrimary() {
		Node prim;
		if(this.lastToken == null) return NumNode.ZERO;
		
		switch(this.lastToken.type) {
			case NUMBER -> {
				NumberToken num = (NumberToken)this.lastToken;
				prim = new NumNode(num.value);
				nextToken();
			}
			case CONSTANT -> {
				ReferenceToken ref = (ReferenceToken)this.lastToken;
				prim = new NumNode(this.constants.get(ref.id));
				nextToken();
			}
			case VARIABLE -> {
				ReferenceToken ref = (ReferenceToken)this.lastToken;
				prim = new VarNode(ref.id);
				nextToken();
			}
			case FUNCTION -> {
				ReferenceToken ref = (ReferenceToken)this.lastToken;
				List<Node> argv = new ArrayList<>();
				nextToken();
				expect("arglist for function " + ref.id, Token.Type.LPAREN);
				nextToken();
				
				while(!isEndOfExpression(at)) {
					if(match(Token.Type.RPAREN)) break;
					argv.add(parseExpr());
					
					expect("comma or end of arglist", Token.Type.COMMA, Token.Type.RPAREN);
					if(match(Token.Type.COMMA)) nextToken();
				}
				if(!expect("')' at end of arglist", Token.Type.RPAREN)) {
					prim = NumNode.ZERO;
					break;
				}
				nextToken();
				
				Function func = this.functions.get(ref.id);
				if(argv.size() < func.argc) {
					LOGGER.error(
						"Too few arguments for function {}, expected at least {}, but got {}",
						ref.id, func.argc, argv.size()
					);
					prim = NumNode.ZERO;
					break;
				}
				if(argv.size() > func.argc && !func.variadic) {
					LOGGER.error(
						"Too many arguments for function {}, expected at most {}, but got {}",
						ref.id, func.argc, argv.size()
					);
					prim = NumNode.ZERO;
					break;
				}
				
				prim = new CallNode(func, ref.id, argv.toArray(new Node[0]));
			}
			
			case LPAREN -> {
				nextToken();
				prim = parseExpr();
				expect("matching ')'", Token.Type.RPAREN);
				nextToken();
			}
			
			//prefix
			case OPERATOR -> {
				CharToken op = (CharToken)this.lastToken;
				nextToken();
				
				prim = new UnopNode(Node.OpType.unopFromChar(op.ch), parseExpr());
			}
			
			default -> {
				LOGGER.error("Expected primary expression, but got {}", this.lastToken.type);
				prim = NumNode.ZERO;
			}
		}
		return prim;
	}
	private Node parseBinop(Node lhs, int prec) {
		while(match(Token.Type.OPERATOR)) {
			CharToken ch = (CharToken)this.lastToken;
			Node.OpType op = Node.OpType.opFromChar(ch.ch);
			
			if(op.precedence < prec) return lhs;
			nextToken();
			
			Node rhs = parseBinop(parsePrimary(), op.precedence + (op.leftAssociative? 1 : 0));
			lhs = new BinopNode(op, lhs, rhs);
		}
		return lhs;
	}
	private Node parseExpr() {
		Node lhs = parsePrimary();
		return parseBinop(lhs, 0);
	}
	
	private boolean match(Token.Type expectedType) {
		return lastToken != null && lastToken.type == expectedType;
	}
	private boolean expect(String msg, Token.Type... expectedTypes) {
		if(this.lastToken == null) throw new RuntimeException();
		for(var expectedType : expectedTypes) {
			if(this.lastToken.type == expectedType) return true;
		}
		LOGGER.error("Expected {} but got {}", msg, this.lastToken.type);
		return false;
	}
	private void nextToken() {
		if(isEndOfExpression(at)) return;
		char ch = expression[at];
		while(ch == ' ') ch = expression[++at];
		
		if(Character.isDigit(ch) || ch == '.') {
			if(lastToken != null && (!lastToken.type.isOperator() && !lastToken.type.isOpening() && !lastToken.type.isComma())) {
				lastToken = new CharToken(Token.Type.OPERATOR, '*');
				return;
			}
			parseNumber();
			return;
		}
		if(Character.isAlphabetic(ch) || ch == '_') {
			if(lastToken != null && (!lastToken.type.isOperator() && !lastToken.type.isOpening() && !lastToken.type.isComma())) {
				lastToken = new CharToken(Token.Type.OPERATOR, '*');
				return;
			}
			parseReference();
			return;
		}
		switch(ch) {
			case ',' -> parseChar(Token.Type.COMMA);
			case '(' -> parseChar(Token.Type.LPAREN);
			case ')' -> parseChar(Token.Type.RPAREN);
			case '[' -> parseChar(Token.Type.LBRACE);
			case ']' -> parseChar(Token.Type.RBRACE);
			case '{' -> parseChar(Token.Type.LCURLY);
			case '}' -> parseChar(Token.Type.RCURLY);
			case '+', '-', '*', '/', '%', '^' -> parseChar(Token.Type.OPERATOR);
			default -> throw new IllegalArgumentException("Unable to parse char '" + ch + "' (Code:" + (int) ch + ") at [" + at + "]");
		}
	}
	
	private void parseChar(Token.Type type) {
		this.lastToken = new CharToken(type, this.expression[this.at++]);
	}
	private void parseReference() {
		int start = this.at;
		int len = 1;
		Token tk = null;
		String id = "";
		
		while(!isEndOfExpression(start + len - 1) && isIdentifier(expression[start + len - 1])) {
			id = new String(expression, start, len);
			
			if(constants != null && constants.containsKey(id)) {
				tk = new NumberToken(this.constants.get(id));
				break;
			}
			else if(variables != null && variables.contains(id)) {
				tk = new ReferenceToken(Token.Type.VARIABLE, id);
				break;
			}
			else if(functions != null && functions.containsKey(id)) {
				tk = new ReferenceToken(Token.Type.FUNCTION, id);
				break;
			}
			len++;
		}
		if(tk == null) LOGGER.error("Unknown identifier {}", id);
		
		this.at += len;
		this.lastToken = tk;
	}
	private void parseNumber() {
		int start = this.at;
		int length = 1;
		this.at++;
		
		while(!isEndOfExpression(start + length) && isNumeric(expression[start + length], isExp(expression[start + length - 1]))) {
			length++;
			this.at++;
		}
		if(isExp(expression[start + length - 1])) {
			length--;
			this.at--;
		}
		lastToken = new NumberToken(Double.parseDouble(String.valueOf(expression, start, length)));
	}
	
	private boolean isExp(char ch) {
		return ch == 'e' || ch == 'E';
	}
	private boolean isNumeric(char ch, boolean lastCharExp) {
		return Character.isDigit(ch) || isExp(ch) || ch == '.' || (lastCharExp && (ch == '-' || ch == '+'));
	}
	private boolean isIdentifier(char ch) {
		return Character.isAlphabetic(ch) || Character.isDigit(ch) || ch == '_';
	}
	private boolean isEndOfExpression(int offset) {
		return offset >= this.expressionLength;
	}
}
