// Written by Jeffery Barksdale
// CS 403 Dr.Lusth
// Lexeme


//interface that defines multiple strings to be uses in Lexeme and Lexer
interface Types{

  public static final String OPAREN = "OPAREN";
  public static final String CPAREN = "CPAREN";
  public static final String OBRACE = "OBRACE";
  public static final String CBRACE = "CBRACE";
  public static final String OCTOTHORPE = "OCTOTHORPE";
  public static final String COMMA = "COMMA";
  public static final String BSLASH = "BACKSLASH";
  public static final String FSLASH = "FSLASH";

  public static final String PLUS = "PLUS";
  public static final String MINUS = "MINUS";
  public static final String TIMES = "TIMES";
  public static final String DIVIDES = "DIVIDES";
  public static final String LTHAN = "LTHAN";
  public static final String GTHAN = "GTHAN";
  public static final String ASSIGN = "ASSIGN";
  public static final String EQUALS = "EQUALS";
  public static final String NOT = "NOT";
  public static final String MOD = "MOD";
  public static final String OR = "OR";
  public static final String AND = "AND";
  public static final String NOTEQUAL = "NOTEQUAL";
  public static final String GTHANE = "GTE";
  public static final String LTHANE = "LTE";

  public static final String TRUE = "TRUE";
  public static final String FALSE = "FALSE";

  public static final String ARRAY= "ARRAY";
  public static final String FILEREADER = "FILEREADER";

  public static final String STRING = "STRING";
  public static final String INTEGER = "INTEGER";
  public static final String FUNCTION = "FUNC";
  public static final String VAR = "VAR";
  public static final String ID = "ID";

  public static final String IF ="if";
  public static final String ELSE = "else";
  public static final String WHILE = "while";
  public static final String ENDofINPUT = "ENDofINPUT";

  public static final String UNKNOWN = "UNKNOWN";
  public static final String ENV = "ENV";
  public static final String TABLE = "TABLE";
  public static final String VARLIST = "VARLIST";
  public static final String VALLIST = "VALLIST";
  public static final String PROG = "PROG";
  public static final String VARDEF = "VARDEF";
  public static final String FUNCTDEF = "FUNCTDEF";
  public static final String LAMBDA = "LAMBDA";
  public static final String FUNCTDEFEXTRA = "FUNCTDEFEXTRA";
  public static final String PARAMS = "PARAMS";
  public static final String CALL = "CALL";
  public static final String UNARY = "UNARY";
  public static final String UMINUS = "UMINUS";
  public static final String EXPRLIST = "EXPRLIST";
  public static final String EXPR = "EXPR";
  public static final String OPERATION = "OPERATION";
  public static final String COMPLIST = "COMPLIST";
  public static final String COMPLISTEXTRA = "COMPLISTEXTRA";
  public static final String COMP = "COMP";
  public static final String COMPEXTRA = "COMPEXTRA";
  public static final String BLOCK = "BLOCK";
  public static final String STATEMENTS = "STATEMENTS";
  public static final String WHILESTATEMENT = "WHILESTATEMENT";
  public static final String IFSTATEMENT = "IFSTATEMENT";
  public static final String IFEXTRA = "IFEXTRA";
  public static final String ELSESTATEMENT = "ELSESTATEMENT";
  public static final String REASSIGN = "REASSIGN";
  public static final String CLOSURE = "CLOSURE";
}
