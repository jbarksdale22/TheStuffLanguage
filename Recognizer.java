// Written by Jeffery Barksdale
// CS 403 Dr.Lusth
// Recognizer

// A class that uses Lexeme and Lexer on a txt file to recognize proper syntax

public class Recognizer implements Types{
	Lexeme currentLexeme;
	Lexer lexer;
	
	Recognizer(String fileName){
		lexer = new Lexer(fileName);
		currentLexeme = lexer.lex();
	}
	
	private boolean check(String type){
		return currentLexeme.type.equals(type);
	}
	
	private void advance(){
		currentLexeme = lexer.lex();
	}
	
	private void match(String type){
		matchNoAdvance(type);
		advance();
	}
	
	private void matchNoAdvance(String type){
		if(!check(type)){
			System.out.println("illegal");
			System.out.println("Syntax ERROR at line " + currentLexeme.lineNum);
			System.out.println("Expected " + type + " have " + currentLexeme.type);
			System.exit(1);
		}
	}
	
	public void program(){
		definiton();
		if(programPending())
			program();	
	}
	
	public boolean programPending(){
		return definitonPending();
	}
	
	public boolean definitonPending(){
		return functDefPending() || varDefPending();
	}
		
	public void definiton(){
		if(functDefPending())
			functDef();
		else 
			varDef();
	}
	
	public boolean varDefPending(){
		return check(VAR) || check(ID);
	}
	
	public boolean functDefPending(){
		return check(FUNCTION);
	}
	
	public void varDef(){
		if(check(VAR)){
			match(VAR);
			match(ID);
			if(check(ASSIGN)){
				match(ASSIGN);
				expr();
			}
		}
		else{
			match(ID);
			match(ASSIGN);
			expr();
		}
		match(OCTOTHORPE);
	}
		
	public void functDef(){
		match(FUNCTION);
		match(ID);
		match(OPAREN);
		optParamList();
		match(CPAREN);
		block();
	}
			
	public void optParamList(){
		if(paramListPending())
			paramList();
	}
	
	public boolean paramListPending(){
		return check(ID);
	}
	
	public void paramList(){
		match(ID);
		if(check(COMMA)){
			match(COMMA);
			paramList();
		}
	}
	
	public void unary(){
		if(check(ID)){
			match(ID);
			if(check(OPAREN)){
				match(OPAREN);
				optExprList();
				match(CPAREN);
			
			}
		}
		else if(check(INTEGER))
			match(INTEGER);
		else if(check(STRING))
			match(STRING);
		else if (check(OPAREN)){
			match(OPAREN);
			expr();
			match(CPAREN);
		}
		else{
			match(MINUS);
			unary();
		}
	}
	
	public boolean unaryPending(){
		return check(ID) || check(INTEGER) || check(STRING) || check(OPAREN) 
				|| check(MINUS);
	}
	
	public void optExprList(){
		if(exprListPending())
			exprList();
	}
	
	public boolean exprListPending(){
		return exprPending();
	}
	
	public void exprList(){
		expr();
		if(check(COMMA)){
			match(COMMA);
			exprList();
		}
	}
	
	public boolean exprPending(){
		return unaryPending() ;
	}
	
	public void expr(){
		if(unaryPending()){
			unary();
			if(operatorPending()){
				operator();
				expr();
			}
		}
	}
	
	public boolean operatorPending(){
		return check(PLUS) || check(MINUS) || check(TIMES) || check(DIVIDES) 
				|| check(MOD) || check(NOT);
	}
	
	public void operator(){
		if(check(PLUS))
			match(PLUS);
		else if(check(MINUS))
			match(MINUS);
		else if(check(TIMES))
			match(TIMES);
		else if(check(DIVIDES))
			match(DIVIDES);
		else if(check(MOD))
			match(MOD);
		else
			match(NOT);
	}
	
	public boolean compareListPending(){
		return comparisonPending();
	}
	
	public boolean comparisonPending(){
		return exprPending();
	}
	
	public void compareList(){
		comparison();
		if(logOpPending()){
			logOp();
			compareList();
		}
	}
	
	public boolean logOpPending(){
		return check(OR) || check(AND) || check(NOT);
	}
	
	public void logOp(){
		if(check(AND))
			match(AND);
		else if(check(OR))
			match(OR);
		else
			match(NOT);
	}
	
	public void comparison(){
		expr();
		if(comparatorPending()){
			comparator();
			expr();
		}
	}
	
	public boolean comparatorPending(){
		return check(EQUALS) || check(NOTEQUAL) || check(GTHAN) || check(LTHAN)
				|| check(GTHANE) || check(LTHANE);
	}
	
	public void comparator(){
		if(check(EQUALS))
			match(EQUALS);
		else if(check(NOTEQUAL))
			match(NOTEQUAL);
		else if(check(GTHAN))
			match(GTHAN);
		else if(check(LTHAN))
			match(LTHAN);
		else if(check(GTHANE))
			match(GTHANE);
		else if(check(LTHANE))
			match(LTHANE);
	}
	
	public void block(){
		match(OBRACE);
		statements();
		match(CBRACE);
	}
	
	public void statements(){
		statement();
		if(statementPending())
			statements();
	}
	
	public boolean statementPending(){
		return loopStatementPending() || ifStatmentPending() || unaryPending()
				|| definitonPending();
	}
	
	public void statement(){
		if(loopStatementPending())
			loopStatement();
		else if(ifStatmentPending())
			ifStatment();
		else{
			if(check(VAR))
				varDef();
			else if(check(FUNCTION))
				functDef();
			else{
				match(ID);
				if(check(ASSIGN)){
					match(ASSIGN);
					expr();
				}
				else{
					match(OPAREN);
					optExprList();
					match(CPAREN);
				}
				match(OCTOTHORPE);
			}
		}
	}
		
	public boolean loopStatementPending(){
		return check(WHILE);
	}
	
	public boolean ifStatmentPending(){
		return check(IF);
	}
	
	public void loopStatement(){
		match(WHILE);
		match(OPAREN);
		compareList();
		match(CPAREN);
		block();
	}
	
	public void ifStatment(){
		match(IF);
		match(OPAREN);
		compareList();
		match(CPAREN);
		block();
		optIfElse();
	}
	
	public void optIfElse(){
		if(check(ELSE)){
			match(ELSE);
			if(ifStatmentPending())
				ifStatment();
			else
				block();
		}
	}
		
}