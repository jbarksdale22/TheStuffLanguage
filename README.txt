Stuff language read me

READ ME

The stuff language is an iterpreter language implemented through java.

Syntax:
Stuff has a c like syntax.
The file tag is .stuff but regular .txt files work as well


	Defining and assignment:
	A stuff program must have a main() function within the global scope.
	To define a function the form is
		FUNC functionName(){

		}
	For Functions to return a value it must be in a variable
	FUNC functionName(){
		VAR name = "STUFF"#
		name#
		}
	this function returns the value "STUFF".

	Variables are dynamic and are implemented with the form
		VAR x#
		or
		VAR x = 3#

	Function calls:
	Functions calls are done by the form function()# or function(arg1,arg2)# with any number of args.

	Iteration:
	Stuff has while loops. A while is implementented as
	while(a LTHAN 100){
		println(a)#
		a = a + 1#
	}
	The while loop boolean statement must compare 2 things with the comparison operatore
	LTHAN, GTHAN, EQUALS, LTE, GTE which are lessthan, greaterthan, equals, lessthanorequal, greaterthanorequal respectively.

	Expressions:
	Expressions can be done with numbers which can be implemented with literal numbers, variables, and function calls.
	VAR x = 1 + y + getNumber()#

	Objects:
	Stuff allows encapsulation.
	An object is defined as 
	FUNC obj(x,y){
		FUNC objFunction1(){

		}
		FUNC objFunction2(){

		}
		this#
	}
	
	To make a an object use 
	VAR o = obj(1,2)#

	This returns the objects environment and can be used with two different built in functions, 
		The field name must be quotes.
		getObjVal(object, "nameOfField") 
		setObjVal(object,"nameOfField", valToSet)

	Built-In Functions:
	Stuff has a few built-in functions for various uses.

	print() prints to current line
	println() prints to current line and addes a newline
	newArray(size) returns a new array of given size.
	setArray(array,index, val) sets an index of a given array to a given value.
	getArray(array,index) returns the value at the given index of the given array.
	getArgCount() returns the command line args count.
	getArg(index) return the command line argument at the given index  (index 0 is the name of the sorce file).
	openFileForReading(filename) opens a file with givename name and returns the file pointer
	readInt(filepointer) returns an integer from given file pointer.
	atFileEnd(filepointer) returns 1 if there are no more ints to be read if more ints returns 0.
	closeFile(filepointer) closes the given filePointer.


Getting ready to use Stuff
to compile necessary files use command: make 
	this will compile all files and make a run executable.

to run a program : run sourcefile
	runs the given program.

to use command line arguments: run sourcefile argument 
	runs the program with given argument

to delete .class files use command: make clean

To run given test cases
uses these commands

make arraysx
make conditionalsx
make recursionx
make objectsx
make iterattionx
make functionsx
make problemx

to see code of given problems use same commands without the x at end
	
		

