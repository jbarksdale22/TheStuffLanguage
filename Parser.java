/* Jeffery Barksdale
 * CS403 
 * Parser class
 *
 * Goes through and tell whether syntax is correct
 * also creates a parse tree
 * from a given source file.
 */


public class Parser implements Types{
	Lexeme currentLexeme, LexToReturn;
	Lexer lexer;
	Lexeme TopTree;
	
	Parser(String fileName){
		lexer = new Lexer(fileName);
		currentLexeme = lexer.lex();
	}
	
	public static void main(String[] args){
		Parser p = new Parser(args[0]);
		Lexeme tree = p.program();
	}
	
	private boolean check(String type){
		return currentLexeme.type.equals(type);
	}
	
	private void advance(){
		currentLexeme = lexer.lex();
	}
	
	private Lexeme match(String type){
		matchNoAdvance(type);
		Lexeme LexToReturn = currentLexeme;
		advance();
		return LexToReturn;
	}
	
	private void matchNoAdvance(String type){
		if(!check(type)){
			System.out.println("illegal");
			System.out.println("Syntax ERROR at line " + currentLexeme.lineNum);
			System.out.println("Expected " + type + " have " + currentLexeme.type);
			System.exit(1);
		}
	}
	
	public Lexeme program(){
		Lexeme d=null,p=null;
		d = definiton();
		if(programPending())
			p = program();
			
		return Lexeme.cons(PROG,d,p);
	}
	
	public boolean programPending(){
		return definitonPending();
	}
	
	public boolean definitonPending(){
		return functDefPending() || varDefPending();
	}
		
	public Lexeme definiton(){
		
		if(functDefPending())
			return functDef();
		else 
			return varDef();
		
	}
	
	public boolean varDefPending(){
		return check(VAR) || check(ID);
	}
	
	public boolean functDefPending(){
		return check(FUNCTION);
	}
	
	public Lexeme varDef(){
		Lexeme i = null,e = null;
		if(check(VAR)){
			match(VAR);
			i = match(ID);
			if(check(ASSIGN)){
				match(ASSIGN);
				e = expr();
			}
		}
		else{
			i = match(ID);
			match(ASSIGN);
			e = expr();
		}
		match(OCTOTHORPE);
		
		return Lexeme.cons(VARDEF,i,e);
	}
		
	public Lexeme functDef(){
		Lexeme i,p,b;
		match(FUNCTION);
		i = match(ID);
		match(OPAREN);
		p = optParamList();
		match(CPAREN);
		b = block();
		
		return Lexeme.cons(FUNCTDEF,i,Lexeme.cons(FUNCTDEFEXTRA,p,b));
	}

	public Lexeme lambda(){
		Lexeme p,b;
		match(LAMBDA);
		match(OPAREN);
		p = optParamList();
		match(CPAREN);
		b = block();
		return Lexeme.cons(LAMBDA,null,Lexeme.cons(FUNCTDEFEXTRA,p,b));

	}
			
	public Lexeme optParamList(){
		if(paramListPending())
			return paramList();
		return null;
	}
	
	public boolean paramListPending(){
		return check(ID);
	}
	
	public Lexeme paramList(){
		Lexeme i=null,p=null;
		i = match(ID);
		if(check(COMMA)){
			match(COMMA);
			p = paramList();
		}
		return Lexeme.cons(PARAMS,i,p);
	}
	
	public Lexeme unary(){
		Lexeme i=null,e=null, b = null;
		if(check(ID)){
			i = match(ID);
			if(check(OPAREN)){
				match(OPAREN);
				e = optExprList();
				match(CPAREN);
				return Lexeme.cons(CALL,i,e);
			}
			return Lexeme.cons(UNARY,i,e);
		}
		else if(check(INTEGER))
			return Lexeme.cons(UNARY,match(INTEGER),e);
		else if(check(STRING))
			return Lexeme.cons(UNARY,match(STRING),e);
		else if(check(LAMBDA)){
			return lambda();
		}
		else if (check(OPAREN)){
			match(OPAREN);
			e = expr();
			match(CPAREN);
			return Lexeme.cons(UNARY,e,null);
		}
		else{
			e = match(MINUS);
			i = unary();
			return Lexeme.cons(UNARY,Lexeme.cons(UMINUS,e,i),null);
		}
	}
	
	public boolean unaryPending(){
		return check(ID) || check(INTEGER) || check(STRING) || check(OPAREN) 
				|| check(LAMBDA) || check(MINUS);
	}
	
	public Lexeme optExprList(){
		if(exprListPending())
			return exprList();
		return null;
	}
	
	public boolean exprListPending(){
		return exprPending();
	}
	
	public Lexeme exprList(){
		Lexeme e, list=null;
		e = expr();
		if(check(COMMA)){
			list = match(COMMA);
			list = exprList();
		}
		return Lexeme.cons(EXPRLIST,e,list);
	}
	
	public boolean exprPending(){
		return unaryPending() ;
	}
	
	public Lexeme expr(){
		Lexeme u = null, o = null, e = null;
		if(unaryPending()){
			u = unary();
			if(operatorPending()){
				o = operator();
				e = expr();
				return Lexeme.cons(EXPR,u,Lexeme.cons(OPERATION,o,e));
				
			}
			return Lexeme.cons(EXPR,u,null);
		}
		return null;
	}
	
	public boolean operatorPending(){
		return check(PLUS) || check(MINUS) || check(TIMES) || check(DIVIDES) 
				|| check(MOD) || check(NOT);
	}
	
	public Lexeme operator(){
		if(check(PLUS))
			return match(PLUS);
		else if(check(MINUS))
			return match(MINUS);
		else if(check(TIMES))
			return match(TIMES);
		else if(check(DIVIDES))
			return match(DIVIDES);
		else if(check(MOD))
			return match(MOD);
		else
			return match(NOT);
	}
	
	public boolean compareListPending(){
		return comparisonPending();
	}
	
	public boolean comparisonPending(){
		return exprPending();
	}
	
	public Lexeme compareList(){
		Lexeme c = null, cl = null, op=null;
		c = comparison();
		if(logOpPending()){
			op = logOp();
			cl = compareList();
			return Lexeme.cons(COMPLIST,c,Lexeme.cons(COMPLISTEXTRA,op,cl));
		}
		return Lexeme.cons(COMPLIST,c,null);
	}
	
	public boolean logOpPending(){
		return check(OR) || check(AND) || check(NOT);
	}
	
	public Lexeme logOp(){
		if(check(AND))
			return match(AND);
		else if(check(OR))
			return match(OR);
		else
			return match(NOT);
	}
	
	public Lexeme comparison(){
		Lexeme e1 = null, c = null, e2 = null;
		e1 = expr();
		if(comparatorPending()){
			c = comparator();
			e2 = expr();
			return Lexeme.cons(COMP,e1,Lexeme.cons(COMPEXTRA,c,e2));
		}
		return Lexeme.cons(COMP,e1,null);
		
	}
	
	public boolean comparatorPending(){
		return  check(LTHAN) || check(EQUALS) || check(NOTEQUAL) || check(GTHAN)
				|| check(GTHANE) || check(LTHANE);
	}
	
	public Lexeme comparator(){
		if(check(EQUALS))
			return match(EQUALS);
		else if(check(NOTEQUAL))
			return match(NOTEQUAL);
		else if(check(GTHAN))
			return match(GTHAN);
		else if(check(LTHAN))
			return match(LTHAN);
		else if(check(GTHANE))
			return match(GTHANE);
		else
			return match(LTHANE);
	}
	
	public Lexeme block(){
		Lexeme s;
		match(OBRACE);
		s = statements();
		match(CBRACE);
		return Lexeme.cons(BLOCK,s,null);
	}
	
	public Lexeme statements(){
		Lexeme s = null, ss = null;
		s = statement();
		if(statementPending())
			ss = statements();
		return Lexeme.cons(STATEMENTS,s,ss);
	}
	
	public boolean statementPending(){
		return loopStatementPending() || ifStatmentPending() || unaryPending()
				|| definitonPending() || check(LAMBDA);
	}
	
	public Lexeme statement(){
		if(loopStatementPending())
			return loopStatement();
		else if(ifStatmentPending())
			return ifStatment();
		else if(check(LAMBDA))
			return lambda();
		else{
			if(check(VAR))
				return varDef();
			else if(check(FUNCTION))
				return functDef();
			// else if(exprPending()){
			// 	Lexeme e = expr();
			// 	match(OCTOTHORPE);
			// 	return e;
				
			// }
			else{
				Lexeme i = null, e = null, r= null;
				if(check(LAMBDA)){
					match(LAMBDA);
				}
				i = match(ID);
				if(check(ASSIGN)){
					match(ASSIGN);
					e = expr();
					match(OCTOTHORPE);
					return Lexeme.cons(REASSIGN,i,e);
				}
				else if(check(OPAREN)){
					match(OPAREN);
					e = optExprList();
					match(CPAREN);
					match(OCTOTHORPE);
					return Lexeme.cons(UNARY,Lexeme.cons(CALL,i,e),null);
				}
				else{
					match(OCTOTHORPE);
					return Lexeme.cons(UNARY,i,null);
				}				
			}
		}
	}
		
	public boolean loopStatementPending(){
		return check(WHILE);
	}
	
	public boolean ifStatmentPending(){
		return check(IF);
	}
	
	public Lexeme loopStatement(){
		Lexeme c,b;
		match(WHILE);
		match(OPAREN);
		c = compareList();
		match(CPAREN);
		b = block();
		return Lexeme.cons(WHILESTATEMENT,c,b);
	}
	
	public Lexeme ifStatment(){
		Lexeme c,b,o = null;
		match(IF);
		match(OPAREN);
		c = compareList();
		match(CPAREN);
		b = block();
		o = optIfElse();
		return Lexeme.cons(IFSTATEMENT,Lexeme.cons(IFEXTRA,c,b),o);
	}
	
	public Lexeme optIfElse(){
		Lexeme i = null, b = null, of = null;
		if(check(ELSE)){
			match(ELSE);
			if(ifStatmentPending())
				return ifStatment();
			else{
				b = block();
				return Lexeme.cons(ELSESTATEMENT,i,b);
			}
				
		}
		return null;
	}
		
}