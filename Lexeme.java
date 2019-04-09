// Written by Jeffery Barksdale
// CS 403 Dr.Lusth
// Lexeme



// A class uses to hold Lexeme data

import java.util.Scanner;
public class Lexeme implements Types{
	  String type;
	  String word;
	  int integer;
	  int lineNum;
	  Lexeme[] aval;
	  Scanner scanner;
	  Lexeme left, right;
      
	  Lexeme (String type){
		  this.type = type;
	  }

	  Lexeme(String type, int line){
		this.type = type;
		this.lineNum = line;
	  }

	  Lexeme(String type, int number,int line){
		  this.type = type;
		  this.integer = number;
      this.lineNum = line;
	  }

	  Lexeme(String type, String value, int line){
		  this.type = type;
		  this.word = value;
          this.lineNum = line;
	  }
	  
	  Lexeme(String type, Lexeme l, Lexeme r){
		  this.type = type;
		  this.left = l;
		  this.right = r;
	  }
	  
	  public static Lexeme cons(String type , Lexeme left, Lexeme right){
		return new Lexeme(type,left,right);
	  }
	  public Lexeme car(){
		  return this.left;
	  }
	  
	  public Lexeme cdr(){
		  return this.right;
	  }
	  
	  public void setCar(Lexeme newLexeme){
		  this.left = newLexeme;
	  }
	  
	  public void setCdr(Lexeme newLexeme){
		  this.right = newLexeme;
	  }
	
	public void displayValue(){
		if(type == INTEGER)
			System.out.print(integer);
		if(type == STRING)
			System.out.print(word);
	}
	public void display(){
		if(type == INTEGER)
			System.out.println(type + " " + integer);
		else if(type == STRING)
			System.out.println(type + " " + word);
		else if(type == ID)
			System.out.println(type + " " + word);
		else
			System.out.println(type);
	}

}
