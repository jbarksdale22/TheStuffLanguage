/*
	Author: Jeffery Barksdale
	CS 403 Programming Languages Dr. Lusth
	Evaluator
	The Evaluator class evaluates given source code 
*/


import java.util.Scanner;
import java.io.*;

public class Evaluator implements Types{

	// the parse tree created from lexing the source
	private Lexeme tree;
	private Parser p;
	// Top level Environment
	Lexeme environment;

	// command line arg holders
	public static int countCL;
	public static String[] argsCL;

	// creates a parse tree for a given file
	Evaluator(String fileName){
		p = new Parser(fileName);
		tree = p.program();
	}

	public static void main(String[] args){
		// set args 
		countCL = args.length;
		argsCL = args;
		Evaluator e = new Evaluator(args[0]);
		e.environment = Environment.createEnv();
		e.eval(e.tree, e.environment);

		// uses to call the main function
		Lexeme callMain = Lexeme.cons(CALL, new Lexeme(ID,"main",0), new Lexeme(EXPRLIST,null,null));
		e.eval(callMain,e.environment);
	}

	//main eval function
	// returns various Lexemes
	public Lexeme eval(Lexeme tree, Lexeme env){
		switch(tree.type){
			case INTEGER: 		return tree;
			case ID: 			return Environment.lookup(env,tree);
			case STRING: 		return tree;
			case PROG: 			return evalProg(tree,env);
			case VARDEF: 		return evalVardef(tree, env);
			case LAMBDA:		return evalLambda(tree,env);
			case CALL:			return evalFunctCall(tree,env);
			case FUNCTDEF: 		return evalFunctDef(tree, env);
			case EXPR:			return evalExpr(tree,env);
			case UNARY:			return evalUnary(tree, env);
			case BLOCK: 		return evalBlock(tree,env);
			case STATEMENTS: 	return evalStatements(tree,env);
			case OPERATION:		return evalOperation(tree, env);
			case REASSIGN: 		return evalReassign(tree, env);
			case IFSTATEMENT:   return evalIf(tree, env);
			case ELSESTATEMENT: return evalElse(tree, env);
			case WHILESTATEMENT: return evalWhileLoop(tree, env);
			case COMPLIST:		return evalCompList(tree,env);
			case COMP:			return evalCompare(tree, env);
			
			default:
				System.out.println("Have not implemented " + tree.type);
		}
		return null;
	}

	//eval for global definitions
	public Lexeme evalProg(Lexeme tree, Lexeme env){
		//System.out.println("In evalProg");
		Lexeme a = null, b =null;
		a = eval(tree.car(),env);
		if(tree.cdr() != null)
			b = eval(tree.cdr(),env);
		return tree;
	}

	// eval a variable defintion
	public Lexeme evalVardef(Lexeme tree, Lexeme env){
		//System.out.println("In evalVarDef for Var: " + tree.car().word);
		Lexeme id = null, val = null;
		
		id = tree.car();
		if(tree.cdr()!=null)
			val = eval(tree.cdr(),env);
		Environment.insert(env,id,val);
		return val; 
	}

	// reassigns a variable defintion
	public Lexeme evalReassign(Lexeme reassign, Lexeme env){
		Lexeme id, val;
		id = reassign.car();
		val = eval(reassign.cdr(),env);
		Environment.update(env, id, val);
		//System.out.println("reassigned " + id.word + " to " + val.integer);
		//Environment.displayCurrEnv(env);
		return val;
	}

	// eval function defintion
	// makes a closure puts in in the local environment
	public Lexeme evalFunctDef(Lexeme tree, Lexeme env){
		//System.out.println("In evalFuncDef for Function: " + tree.car().word);
		Lexeme closure = Lexeme.cons(CLOSURE,env,tree);
		Environment.insert(env, tree.car(), closure);
		return closure;
	}
	// doesnt really work
	public Lexeme evalLambda(Lexeme lambda, Lexeme env){
		Lexeme closure =  Lexeme.cons(CLOSURE,env, tree);
		Environment.insert(env, tree.car(), tree);
		return closure;
	}
	// used for eval a function call
	public Lexeme evalFunctCall (Lexeme t, Lexeme env){
		Lexeme eargs = null;
		Lexeme name = getFucntCallName(t);
		Lexeme args = getFunctCallArgs(t);
		// if there are arguments evaluate them
		if(args != null){
			eargs = evalArgs(args,env);
		}

		// the various built in functions
		if(name.word.equals("getObjVal"))
			return evalGetObjVal(eargs);
		if(name.word.equals("setObjVal"))
			return evalSetObjVal(eargs);
		if(name.word.equals("print"))
			return evalPrint(eargs);
		if(name.word.equals("println"))
			return evalPrintln(eargs);
		if(name.word.equals("newArray"))
			return evalNewArray(eargs);
		if(name.word.equals("setArray"))
			return evalSetArray(eargs);
		if(name.word.equals("getArray"))
			return evalGetArray(eargs);
		if(name.word.equals("getArgCount"))
			return evalGetArgCount(eargs);
		if(name.word.equals("getArg"))
			return evalGetArg(eargs);
		if(name.word.equals("openFileForReading"))
			return evalOpenFileForReading(eargs);
		if(name.word.equals("readInteger"))
			return evalReadInteger(eargs);
		if(name.word.equals("atFileEnd"))
			return evalAtFileEnd(eargs);
		if(name.word.equals("closeFile"))
			return evalCloseFile(eargs);
	
		Lexeme closure = eval(name,env);
		Lexeme params = getClosureParams(closure);
		Lexeme body = getClosureBody(closure);
		Lexeme senv = getClosureEnvironment(closure);
		Lexeme xenv = extendedEnv(senv,params,eargs);

		Environment.insert(xenv,new Lexeme(ID,"this",0),xenv);

		return eval(body,xenv);

	}
	//var f = x.set;
	//f(q)
	public Lexeme evalSetObjVal(Lexeme args){
		Lexeme env = args.car();
		Lexeme var = new Lexeme(ID,args.cdr().car().word,0);
		Lexeme val = args.cdr().cdr().car();
		Environment.update(env, var, val);
		return env;
	}
	public Lexeme evalGetObjVal(Lexeme args){
		Lexeme env = args.car();
		Lexeme var = new Lexeme(ID,args.cdr().car().word,0);
		return eval(var,env);
	}
	public Lexeme getFucntCallName(Lexeme t){
		return t.car();
	}
	public Lexeme getFunctCallArgs(Lexeme t){
		return t.cdr();
	}
	public Lexeme getClosureParams(Lexeme closure){
		return closure.cdr().cdr().car();
	}
	public Lexeme getClosureBody(Lexeme closure){
		return closure.cdr().cdr().cdr();
	}
	public Lexeme getClosureEnvironment(Lexeme closure){
		return closure.car();
	}
	// iterator for testing
	// public void iterateList(Lexeme list){
	// 	while(list.car() != null){
	// 		System.out.println(list.car().type);
	// 		list = list.cdr();
	// 	}
	// }

	//evals all the argument in a function call
	public Lexeme evalArgs(Lexeme args, Lexeme env){
		Lexeme eargs = new Lexeme(EXPRLIST,0);
		if(args.car() != null)
			eargs.setCar(eval(args.car(),env));
		if(args.cdr() != null)
			eargs.setCdr(evalArgs(args.cdr(),env));
		return eargs;
	}

	// makes a new environment that extends the current one
	public Lexeme extendedEnv(Lexeme env, Lexeme params, Lexeme args){
		return Environment.extend(params,args,env);
	}

	// eval block returns the last statment
	public Lexeme evalBlock(Lexeme t, Lexeme env){
		Lexeme result=null;
		Lexeme statement = t.car();
		while(statement != null){
			result = eval(statement.car(),env);
			statement = statement.cdr();
		}
		return result;
	}

	//evals a statement
	public Lexeme evalStatements(Lexeme t, Lexeme env){
		return eval(t.car(),env);
	}

	//recusrsively evals an expression
	public Lexeme evalExpr(Lexeme expr, Lexeme env){
		Lexeme expr1 = null;
		Lexeme expr2 = null;

		expr1 = eval(expr.car(), env);

		if(expr.cdr() != null){
			expr2 = eval(expr.cdr().cdr(),env);
			return applyOp(expr1, expr2, expr.cdr().car());
		}
		return expr1;
	}

	// aplys the op in an expression
	public Lexeme applyOp(Lexeme expr1, Lexeme expr2, Lexeme op){
		int newNum;
		String newString;
		if(expr1.type.equals(INTEGER) && expr1.type.equals(INTEGER)){
			if(op.type.equals(PLUS))
				newNum = expr1.integer + expr2.integer;
			else if(op.type.equals(MINUS))
				newNum = expr1.integer - expr2.integer;
			else if(op.type.equals(TIMES))
				newNum = expr1.integer * expr2.integer;
			else
				newNum = expr1.integer / expr2.integer;

			return new Lexeme (INTEGER,newNum, 0);
					
		}
		else if(expr1.type.equals(STRING) && expr1.type.equals(STRING)){
			if(op.type.equals(PLUS)){
				newString = expr1.word + expr2.word;
				return  new Lexeme (STRING, newString, 0);
			}
			else{
				System.out.println("Incompatibale operator " + op.type + " for " + expr1.type + " and " + expr2.type);
				System.exit(1);
			}
			
		}
		else{
			System.out.println("Incompatibale types" + expr1.type + " and " + expr2.type);
			System.out.println("Exiting Program");
			System.exit(1);
		}

		return null;
			
	}

	// returns the operator
	public Lexeme evalOperation(Lexeme op, Lexeme env){
		op.setCdr(eval(op.cdr(),env));
		return op;
	}
	// returns an evaluated UNARY
	public Lexeme evalUnary(Lexeme unary, Lexeme env){
		return eval(unary.car(), env);
	}

	// evaluates a while Loop
	public Lexeme evalWhileLoop(Lexeme whileStmt, Lexeme env){
		Lexeme comparison = evalCompList(whileStmt.car(), env);
		Lexeme stmnt = null;
		while(comparison.type.equals(TRUE)){
			// System.out.println("in While");
			// Environment.displayCurrEnv(env);
			stmnt = eval(whileStmt.cdr(), env);
			comparison = evalCompList(whileStmt.car(), env);
		}
		return stmnt;
	}

	// reursively evaluates an if stament and else ifs
	public Lexeme evalIf(Lexeme ifStmnt, Lexeme env){
		if(eval(ifStmnt.car().car(),env).type.equals(TRUE))
			return eval(ifStmnt.car().cdr(),env);
		else if(ifStmnt.cdr() != null)
			return eval(ifStmnt.cdr(),env);
		return ifStmnt;
	}

	// evals the else
	public Lexeme evalElse(Lexeme elseStmt, Lexeme env){
		
		return eval(elseStmt.cdr(),env);
	}
	// built in print
	public Lexeme evalPrint(Lexeme evalArgsList){
		while(evalArgsList != null){
			evalArgsList.car().displayValue();
			evalArgsList = evalArgsList.right;
		}
		return evalArgsList;
	}
	// built in println
	public Lexeme evalPrintln(Lexeme evalArgsList){
		while(evalArgsList != null){
			evalArgsList.car().displayValue();
			evalArgsList = evalArgsList.right;
		}
		System.out.println("");
		return evalArgsList;
	}

	// evals a comparison 
	public Lexeme evalCompList(Lexeme list, Lexeme env){
		Lexeme expr1 = eval(list.car().car(), env);
		Lexeme expr2 = null;

		if(list.car().cdr() != null){
			expr2 = eval(list.car().cdr().cdr(), env);
			return eval(list.car(), env);
		}
		else return eval(list.car().car(),env);
	}

	public Lexeme evalCompare(Lexeme tree, Lexeme env){
		Lexeme expr1 = eval(tree.car(), env);
		Lexeme expr2 = eval(tree.cdr().cdr(),env);
		Lexeme comparator = tree.cdr().car();

		if(comparator.type.equals(GTHAN))
			if(expr1.integer > expr2.integer) return new Lexeme(TRUE,1,0);
		if(comparator.type.equals(LTHAN))
			if(expr1.integer < expr2.integer) return new Lexeme(TRUE,1,0);
		if(comparator.type.equals(GTHANE))
			if(expr1.integer >= expr2.integer) return new Lexeme(TRUE,1,0);
		if(comparator.type.equals(LTHANE))
			if(expr1.integer <= expr2.integer) return new Lexeme(TRUE,1,0);
		if(comparator.type.equals(EQUALS))
			if(expr1.integer == expr2.integer) return new Lexeme(TRUE,1,0);
		return new Lexeme(FALSE,0,0);
	}

	// built in for making a new arrays of given size
	public Lexeme evalNewArray(Lexeme eargs){
		if(length(eargs) != 1){
			System.out.println("newArray requires 1 arg");
			System.exit(1);
		}
		Lexeme size = eargs.car();
		assert size.type.equals(INTEGER): "arg is not in";
		Lexeme newArray = new Lexeme(ARRAY);
		newArray.aval = new Lexeme[size.integer];
		assert newArray.aval != null: "array is null";
		return newArray;
	}

	// gets array val at given index
	public Lexeme evalGetArray(Lexeme eargs){
		if(length(eargs) != 2){
			System.out.println("getArray requires two args");
			System.exit(1);
		}
		Lexeme array = eargs.car();
		Lexeme index = eargs.cdr().car();
		Lexeme val = array.aval[index.integer];
		return val;
	}
	// sets val at given index
	public Lexeme evalSetArray(Lexeme eargs){
		if(length(eargs) != 3){
			System.out.println("setArray requires three args");
			System.exit(1);
		}
		
		Lexeme array = eargs.car();
		Lexeme index = eargs.cdr().car();
		Lexeme val = eargs.cdr().cdr().car();
		array.aval[index.integer] = val;
		return val;
	}
	// gets length of arg list
	private int length(Lexeme argList){
		int ctr=0;
		while(argList != null){
			ctr++;
			argList = argList.cdr();
		}
		return ctr;
	}
	// builtin to get main arg count
	public Lexeme evalGetArgCount(Lexeme args){
		return new Lexeme(INTEGER,countCL,0);
	}
	// builtin to get arg at given index
	public Lexeme evalGetArg(Lexeme args){
		Lexeme index = args.car();
		return new Lexeme(STRING, argsCL[index.integer],0);
	}
	
	public Lexeme evalOpenFileForReading(Lexeme eargs){
		Lexeme fileName = eargs.car();
		Lexeme fp = new Lexeme(FILEREADER);
		try{
			fp.scanner = new Scanner(new File(fileName.word));
		}
		catch(FileNotFoundException ex){ System.out.println("no such file found");}
		return fp;
	}

	public Lexeme evalReadInteger(Lexeme eargs){
		Scanner fr = eargs.car().scanner;
		int num =  fr.nextInt();
		return new Lexeme(INTEGER, num,0);
	}

	public Lexeme evalAtFileEnd(Lexeme eargs){
		Scanner sc = eargs.car().scanner;
		if(sc.hasNextInt())
			return new Lexeme(FALSE,0,0);
		else 
			return new Lexeme(TRUE,1,0);
	}

	public Lexeme evalCloseFile(Lexeme eargs){
		Lexeme fp = eargs.car();
		fp.scanner.close();
		return new Lexeme(TRUE,1,0); 

	}
}