// Written by Jeffery Barksdale
// CS 403 Dr.Lusth
// Lexer

// A class that uses Lexeme to Lex a txt file

import java.io.*;

public class Lexer implements Types{

  FileInputStream in;
  PushbackInputStream input;
  int lineNumber;

  // the number that is returned when there is no more
  // of the file to be read
  private static final int ENDINPUT = 0;

  // constructor reads takes a file name as an argument
  // and opens up a FileInputStream to read from
  Lexer(String readFile){
    try{
      in = new FileInputStream(readFile);
      input = new PushbackInputStream(in);
      lineNumber = 1;
    }
    catch(FileNotFoundException ex){
      System.out.println("no such file found");
    }
  }

  //uses the PushbackInputStream to read a character from the file
  private int charRead(){
    try{
      int c =  input.read();
      return c;
    }
    catch(IOException ex){
      return 0;
    }
  }
  // unreads a character from the PushbackInputStream
  private void charUnread(int c){
    try{
      input.unread(c);
    }
    catch(IOException ex){}
  }

  // skips whites space which includes lines
  // that begin with $ that determines comments
  private void skipWhiteSpace(){
          int c = charRead();
          while(Character.isWhitespace((char) c) || (char) c == '$'){
              if((char) c == '$'){
                while((char) c != '\n')
                  c = charRead();
              }
              if ((char) c == '\n')
                  lineNumber++;
              c = charRead();
          }
          charUnread(c);
  }

  // the core function of Lexer that determines what type
  // of Lexeme to return
  public Lexeme lex(){
    int c;
    skipWhiteSpace();
    c = charRead();
    try{
      if(input.available() == ENDINPUT) return new Lexeme(ENDofINPUT, lineNumber);
    }
    catch(IOException ex){}

    switch(c){
        case '(': return new Lexeme(OPAREN, lineNumber);
        case ')': return new Lexeme(CPAREN, lineNumber);
        case ',': return new Lexeme(COMMA, lineNumber);
        case '+': return new Lexeme(PLUS, lineNumber); 
        case '*': return new Lexeme(TIMES, lineNumber);
        case '-': return new Lexeme(MINUS, lineNumber);
        case '/': return new Lexeme(DIVIDES, lineNumber);
        case '<': return new Lexeme(LTHAN, lineNumber);
        case '>': return new Lexeme(GTHAN, lineNumber);
        case '=': return new Lexeme(ASSIGN, lineNumber);
        case '#': return new Lexeme(OCTOTHORPE, lineNumber);
        case '{': return new Lexeme(OBRACE, lineNumber);
        case '}': return new Lexeme(CBRACE, lineNumber);
        case '%': return new Lexeme(MOD, lineNumber);
        case '|': return new Lexeme(OR, lineNumber);
        case '!': return new Lexeme(NOT, lineNumber);

    default:
        //the defaults determine non single character Lexemes
        //that are INTEGERS, KEYWORDS, ID names, and STRINGS
         if(Character.isDigit(c)){
              charUnread(c);
              return lexNumber();
          }
          else if(Character.isLetter((char)c)){
              charUnread(c);
              return lexVariableOrKeyword();
          }
          else if(c=='\"')
            return lexString();
          else{
				String buffer = Character.toString((char) c);
				return new Lexeme(UNKNOWN,buffer,lineNumber);
		  }
    }

  }
  // returns an INTEGER Lexeme
  private Lexeme lexNumber(){
    int ch =  charRead();
    String buffer = "";
    while(Character.isDigit((char) ch)){
      buffer = buffer + (char) ch;
      ch = charRead();
    }
    charUnread(ch);

    return new Lexeme(INTEGER, Integer.parseInt(buffer), lineNumber);
  }
  // returns a ID name or Keyword Lexeme
  private Lexeme lexVariableOrKeyword(){
      int ch = charRead();
      String buffer = "";
      while(Character.isLetterOrDigit((char) ch)){
        buffer = buffer + (char) ch;
        ch = charRead();
      }
      charUnread(ch);

      if(buffer.equals(IF)) return new Lexeme(IF,lineNumber);
      else if(buffer.equals(ELSE)) return new Lexeme(ELSE, lineNumber);
      else if(buffer.equals(WHILE)) return new Lexeme(WHILE, lineNumber);
      else if(buffer.equals(FUNCTION)) return new Lexeme(FUNCTION, lineNumber);
      else if(buffer.equals(LAMBDA)) return new Lexeme(LAMBDA, lineNumber);
      else if(buffer.equals(VAR)) return new Lexeme(VAR, lineNumber);
	    else if(buffer.equals(EQUALS)) return new Lexeme(EQUALS, lineNumber);
	    else if(buffer.equals(OR)) return new Lexeme(OR, lineNumber);
	    else if(buffer.equals(AND)) return new Lexeme(AND, lineNumber);
	    else if(buffer.equals(NOTEQUAL)) return new Lexeme(NOTEQUAL, lineNumber);
      else if(buffer.equals(GTHAN)) return new Lexeme(GTHAN, lineNumber);
      else if(buffer.equals(GTHANE)) return new Lexeme(GTHANE, lineNumber);
      else if(buffer.equals(LTHAN)) return new Lexeme(LTHAN, lineNumber);
      else if(buffer.equals(LTHANE)) return new Lexeme(LTHANE, lineNumber);
      else return new Lexeme(ID, buffer, lineNumber);
  }
  // returns a String Lexeme
  private Lexeme lexString(){
    int ch = charRead();
    String buffer = "";
    while((char) ch != '\"'){
      buffer = buffer + (char) ch;
      ch = charRead();
    }
    buffer = buffer;
    return new Lexeme(STRING, buffer, lineNumber);

  }

}
