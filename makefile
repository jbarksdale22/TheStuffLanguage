run: Types.class Lexeme.class Lexer.class Parser.class Environment.class PrettyPrinter.class Evaluator.class

samples: arraysx conditionalsx recursionx iterationx functionsx lambdax objectsx problemx 

errors: error1x error2x error3x error4x error5x

error1:
	@cat program2.txt

error1x: run
	-@./run program2.txt 

error2: 
	@cat program5.txt

error2x: run
	-@./run program5.txt

error3:
	@cat error3.txt

error3x: run	
	-@./run error3.txt

error4: 
	@cat error4.txt

error4x: run
	-@./run error4.txt

error5:
	@cat error5.txt

error5x: run	
	-@./run error5.txt

arrays: run
	@cat arrays.stuff

arraysx: run
	@./run arrays.stuff

conditionals: run
	@cat conditionalTest.stuff

conditionalsx: run	
	@./run conditionalTest.stuff

recursion: run
	@cat recursionTest.stuff

recursionx: run	
	@./run recursionTest.stuff

iteration: run
	@cat iterationTest.stuff

iterationx: run	
	@./run iterationTest.stuff

functions: run
	@cat functionTest.stuff

functionsx: run	
	@./run functionTest.stuff

lambda: run
	@cat lambdaTest.stuff

lambdax: run
	@echo couldnt get lambda working but pp lambda works	
	@java PrettyPrinter lambdaTest.stuff

objects: run
	@cat objects.stuff

objectsx: run
	@./run objects.stuff

problem: 
	@cat problemTest.stuff

problemx: run	
	@./run problemTest.stuff nums.txt


Types.class: Types.java
	javac Types.java

Lexeme.class: Lexeme.java
	javac Lexeme.java

Lexer.class: Lexer.java
	javac Lexer.java

Environment.class: Environment.java
	javac Environment.java
	
Parser.class: Parser.java
	javac Parser.java

PrettyPrinter.class: PrettyPrinter.java
	javac PrettyPrinter.java

Evaluator.class: Evaluator.java
	javac Evaluator.java

clean:
	$(RM) *.class run
