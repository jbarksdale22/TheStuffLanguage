public class Environment implements Types{
	
	
	public static Lexeme createEnv(){
		return Lexeme.cons(ENV,Lexeme.cons(TABLE,null,null),null);
	}
	
	public static Lexeme insert (Lexeme env,Lexeme id, Lexeme val){
		env.car().setCar(Lexeme.cons(VARLIST,id,env.car().car()));
		env.car().setCdr(Lexeme.cons(VALLIST,val,env.car().cdr()));
		return val;
	}
	
	public static boolean sameVariable(Lexeme var1,Lexeme var2){
		return var1.word.equals(var2.word);
	}
	
	public static Lexeme lookup(Lexeme env, Lexeme id){
		Lexeme vars,vals,table;
		while(env != null){
			table = env.car();
			vars = table.car();
			vals = table.cdr();
			
			while(vars != null){
				if(sameVariable(id,vars.car()))
					return vals.car();
					
				vars = vars.cdr();
				vals = vals.cdr();
			}
			env = env.cdr();
		}
		System.out.println("variable " + id.word + " at line " + id.lineNum + " does not exist");
		System.out.println("Exiting program.");
		System.exit(1);
		return null;
	}
	
	public static Lexeme update(Lexeme env, Lexeme id, Lexeme val){
		Lexeme vars, vals, table;
		while(env != null){
			table = env.car();
			vars = table.car();
			vals = table.cdr();
			
			while(vars != null){
				if(sameVariable(id,vars.car())){
					vals.setCar(val);
					return null;
				}
				vars = vars.cdr();
				vals = vals.cdr();
			}
			env = env.cdr();
		}
		System.out.println("Variable " + id.word + " not defined");
		return null;
	}
	
	public static Lexeme extend(Lexeme vars, Lexeme vals, Lexeme env){
		return Lexeme.cons(ENV,Lexeme.cons(TABLE,vars,vals),env);
	}

	public static void displayCurrEnv(Lexeme env){
		Lexeme id, val;
		id = env.car().car();
		val = env.car().cdr();
		while (id != null){
			System.out.print("id is ");
			id.car().display();
			System.out.print("value is ");
			val.car().display();
			
			id = id.cdr();
			val = val.cdr();
		}
	}
	
	public static void displayEnv(Lexeme env){
		while (env != null){
			displayCurrEnv(env);
			env = env.cdr();
		}
	}
			
}