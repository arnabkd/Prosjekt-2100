package no.uio.ifi.cless.syntax;

/*
* module Syntax
*/

import no.uio.ifi.cless.cless.CLess;
import no.uio.ifi.cless.code.Code;
import no.uio.ifi.cless.error.Error;
import no.uio.ifi.cless.log.Log;
import no.uio.ifi.cless.scanner.Scanner;
import no.uio.ifi.cless.scanner.Token;
import static no.uio.ifi.cless.scanner.Token.*;

/*
* Creates a syntax tree by parsing; checks it;
* generates executable code; also prints the parse tree (if requested).
*/
public class Syntax {
	static DeclList library;
	static Program program;

	public static void init() {
		//-- Must be changed in part 1:
	}

	public static void finish() {
		//-- Must be changed in part 1:
		//Maybe not
	}

	static void error(SyntaxUnit use, String message) {
		Error.error(use.lineNum, message);
	}

	public static void checkProgram() {
		program.check(library);
	}

	public static void genCode() {
		program.genCode(null);
	}

	public static void parseProgram() {
		program = new Program();
		program.parse();
	}

	public static void printProgram() {
		program.printTree();
	}
}


/*
* Master class for all syntactic units.
* (This class is not mentioned in the syntax diagrams.)
*/
abstract class SyntaxUnit {
	int lineNum;

	SyntaxUnit() {
		lineNum = Scanner.curLine;
	}

	abstract void check(DeclList curDecls);
	abstract void genCode(FuncDecl curFunc);
	abstract void parse();
	abstract void printTree();
}


/*
* A <program>
*/
class Program extends SyntaxUnit {
	DeclList progDecls;

	Program () {
		progDecls = new GlobalDeclList();
	}

	@Override void check(DeclList curDecls) {
		progDecls.check(curDecls);

		if (! CLess.noLink) {
			// Check that 'main' has been declared properly:
			//-- Must be changed in part 2:
		}
	}

	@Override void genCode(FuncDecl curFunc) {
		progDecls.genCode(null);
	}

	@Override void parse() {
		Log.enterParser("<program>");

		progDecls.parse();
		if (Scanner.curToken != eofToken)
		Scanner.expected("Declaration");

		Log.leaveParser("</program>");
	}

	@Override void printTree() {
		progDecls.printTree();
	}
}


/*
* A declaration list.
* (This class is not mentioned in the syntax diagrams.)
*/

abstract class DeclList extends SyntaxUnit {
	Declaration firstDecl = null;
	DeclList outerScope;

	DeclList () {
		//-- Must be changed in part 1:
	}

	@Override void check(DeclList curDecls) {
		outerScope = curDecls;

		Declaration dx = firstDecl;
		while (dx != null) {
			dx.check(this);
			dx = dx.nextDecl;
		}
	}

	@Override void printTree() {
		//-- Must be changed in part 1:
		Declaration currentDecl = firstDecl;
		while (currentDecl != null){
			currentDecl.printTree();
			currentDecl = currentDecl.nextDecl;
		}		
	}

	void addDecl(Declaration d) {
		//-- Must be changed in part 1:
		if (firstDecl == null) { firstDecl = d; return; } //If declList is empty, put d as firstDecl
				
		if (declExists(d)) {
			variableAlreadyDefinedError(d);
			return; 
		}
		
		//Declaration does not exist yet, add it to the declList
		d.nextDecl = firstDecl;
		firstDecl = d;
	}
	
	protected boolean declExists (Declaration d){
		if (d == null) return false; //declaration d does not exist if d is null
		
		Declaration currentDecl = firstDecl;
		
		while (currentDecl != null && currentDecl.name != null) {
			if (currentDecl.name.compareTo(d.name) == 0) return true; //A declaration with identical name is found
			currentDecl = currentDecl.nextDecl;
		}
		
		return false; //Declaration d does not exist yet
	}

	protected void variableAlreadyDefinedError (Declaration d){
		if (d == null || d.name == null) return;

		String errormessage = "Variable :";
		errormessage += d.name; // Add variable name to error message
		errormessage += " is already defined"; 
		
		Error.error(errormessage);
	}
	
	
	int dataSize() {
		Declaration dx = firstDecl;
		int res = 0;

		while (dx != null) {
			res += dx.dataSize();
			dx = dx.nextDecl;
		}
		return res;
	}

	Declaration findDecl(String name, SyntaxUnit usedIn) {
		//-- Must be changed in part 2:
		return null;
	}
}


/*
* A list of global declarations.
* (This class is not mentioned in the syntax diagrams.)
*/
class GlobalDeclList extends DeclList {
	@Override void genCode(FuncDecl curFunc) {
		//-- Must be changed in part 2:
	}

	@Override void parse() {
		while (Scanner.curToken == intToken) {
			if (Scanner.nextToken == nameToken) {
				if (Scanner.nextNextToken == leftParToken) { //Function declaration
					FuncDecl fd = new FuncDecl(Scanner.nextName);
					fd.parse();
					addDecl(fd);
				} else if (Scanner.nextNextToken == leftBracketToken) { //int array declaration
					GlobalArrayDecl gad = new GlobalArrayDecl(Scanner.nextName);
					gad.parse();
					addDecl(gad);
				} else { //int var;
					//-- Must be changed in part 1:
					GlobalSimpleVarDecl gsvd = new GlobalSimpleVarDecl (Scanner.nextName);
					gsvd.parse();
					addDecl(gsvd);
				}
			} else {
				Scanner.expected("Declaration");
			}
		}
	}
}


/*
* A list of local declarations.
* (This class is not mentioned in the syntax diagrams.)
*/
class LocalDeclList extends DeclList {
	@Override void genCode(FuncDecl curFunc) {
		//-- Must be changed in part 2:
	}

	@Override void parse() {
		//-- Must be changed in part 1:
	}
}


/*
* A list of parameter declarations.
* (This class is not mentioned in the syntax diagrams.)
*/
class ParamDeclList extends DeclList {
	@Override void genCode(FuncDecl curFunc) {
		//-- Must be changed in part 2:
	}

	@Override void parse() {
		//-- Must be changed in part 1:
	}
}


/*
* Any kind of declaration.
* (This class is not mentioned in the syntax diagrams.)
*/
abstract class Declaration extends SyntaxUnit {
	String name, assemblerName;
	boolean visible = false;
	Declaration nextDecl = null;

	Declaration(String n) {
		name = n;
	}

	abstract int dataSize();

	/**
	* checkWhetherArray: Utility method to check whether this Declaration is
	* really an array. The compiler must check that a name is used properly;
	* for instance, using an array name a in "a()" or in "x=a;" is illegal.
	* This is handled in the following way:
	* <ul>
	* <li> When a name a is found in a setting which implies that should be an
	*		array (i.e., in a construct like "a["), the parser will first
	*		search for a's declaration d.
	* <li> The parser will call d.checkWhetherArray(this).
	* <li> Every sub-class of Declaration will implement a checkWhetherArray.
	*		If the declaration is indeed an array, checkWhetherArray will do
	*		nothing, but if it is not, the method will give an error message
	*		and thus stop the compilation.
	* </ul>
	* Examples
	* <dl>
	*	<dt>GlobalArrayDecl.checkWhetherArray(this)</dt>
	*	<dd>will do nothing, as everything is all right.</dd>
	*	<dt>FuncDecl.checkWhetherArray(this)</dt>
	*	<dd>will give an error message.</dd>
	* </dl>
	*/
	abstract void checkWhetherArray(SyntaxUnit use);

	/**
	* checkWhetherFunction: Utility method to check whether this Declaration
	* is really a function.
	*
	* @param nParamsUsed Number of parameters used in the actual call.
	*					  (The method will give an error message if the
	*					  function was used with too many or too few parameters.)
	* @param use From where is the check performed?
	* @see	  checkWhetherArray
	*/
	abstract void checkWhetherFunction(int nParamsUsed, SyntaxUnit use);

	/**
	* checkWhetherSimpleVar: Utility method to check whether this
	* Declaration is really a simple variable.
	*
	* @see	  checkWhetherArray
	*/
	abstract void checkWhetherSimpleVar(SyntaxUnit use);
}


/*
* A <var decl>
*/
abstract class VarDecl extends Declaration {
	VarDecl(String n) {
		super(n);
	}

	@Override void checkWhetherFunction(int nParamsUsed, SyntaxUnit use) {
		Syntax.error(use, name + " is a variable and no function!");
	}

	@Override int dataSize() {
		return 4;
	}

	@Override void printTree() {
		Log.wTree("int " + name);
		Log.wTreeLn(";");
	}

	//-- Must be changed in part 1+2:
}


/*
* A global array declaration
*/
class GlobalArrayDecl extends VarDecl {
	int numElems;

	GlobalArrayDecl(String n) {
		super(n);
		assemblerName = (CLess.underscoredGlobals() ? "_" : "") + n;
	}

	@Override void check(DeclList curDecls) {
		visible = true;
		if (numElems < 0)
		Syntax.error(this, "Arrays cannot have negative size!");
	}

	@Override void checkWhetherArray(SyntaxUnit use) {
		/* OK */
	}

	@Override void checkWhetherSimpleVar(SyntaxUnit use) {
		Syntax.error(use, name + " is an array and no simple variable!");
	}

	@Override int dataSize() {
		return 4*numElems;
	}

	@Override void genCode(FuncDecl curFunc) {
		//-- Must be changed in part 2:
	}

	@Override void parse() {
		Log.enterParser("<var decl>");

		//-- Must be changed in part 1:

		Log.leaveParser("</var decl>");
	}

	@Override void printTree() {
		//-- Must be changed in part 1:
	}
}


/*
* A global simple variable declaration
*/
class GlobalSimpleVarDecl extends VarDecl {
	GlobalSimpleVarDecl(String n) {
		super(n);
		assemblerName = (CLess.underscoredGlobals() ? "_" : "") + n;
	}

	@Override void check(DeclList curDecls) {
		//-- Must be changed in part 2:
	}

	@Override void checkWhetherArray(SyntaxUnit use) {
		//-- Must be changed in part 2:
	}

	@Override void checkWhetherSimpleVar(SyntaxUnit use) {
		/* OK */
	}

	@Override void genCode(FuncDecl curFunc) {
		//-- Must be changed in part 2:
	}

	@Override void parse() {
		Log.enterParser("<var decl>");

		//-- Must be changed in part 1:

		Log.leaveParser("</var decl>");
	}
}


/*
* A local array declaration
*/
class LocalArrayDecl extends VarDecl {
	int numElems;

	LocalArrayDecl(String n) {
		super(n);
	}

	@Override void check(DeclList curDecls) {
		//-- Must be changed in part 2:
	}

	@Override void checkWhetherArray(SyntaxUnit use) {
		//-- Must be changed in part 2:
	}

	@Override void checkWhetherSimpleVar(SyntaxUnit use) {
		//-- Must be changed in part 2:
	}

	@Override int dataSize() {
		//-- Must be changed in part 2:
		return 0;
	}

	@Override void genCode(FuncDecl curFunc) {
		//-- Must be changed in part 2:
	}

	@Override void parse() {
		Log.enterParser("<var decl>");

		//-- Must be changed in part 1:

		Log.leaveParser("</var decl>");
	}

	@Override void printTree() {
		//-- Must be changed in part 1:
	}

}


/*
* A local simple variable declaration
*/
class LocalSimpleVarDecl extends VarDecl {
	LocalSimpleVarDecl(String n) {
		super(n);
	}

	@Override void check(DeclList curDecls) {
		//-- Must be changed in part 2:
	}

	@Override void checkWhetherArray(SyntaxUnit use) {
		//-- Must be changed in part 2:
	}

	@Override void checkWhetherSimpleVar(SyntaxUnit use) {
		//-- Must be changed in part 2:
	}

	@Override void genCode(FuncDecl curFunc) {
		//-- Must be changed in part 2:
	}
	@Override void parse() {
		Log.enterParser("<var decl>");

		//-- Must be changed in part 1:

		Log.leaveParser("</var decl>");
	}
}


/*
* A <param decl>
*/
class ParamDecl extends VarDecl {
	int paramNum = 0;

	ParamDecl(String n) {
		super(n);
	}

	@Override void check(DeclList curDecls) {
		//-- Must be changed in part 2:
	}

	@Override void checkWhetherArray(SyntaxUnit use) {
		//-- Must be changed in part 2:
	}

	@Override void checkWhetherSimpleVar(SyntaxUnit use) {
		//-- Must be changed in part 2:
	}

	@Override void genCode(FuncDecl curFunc) {
		//-- Must be changed in part 2:
	}

	@Override void parse() {
		Log.enterParser("<param decl>");

		//-- Must be changed in part 1:

		Log.leaveParser("</param decl>");
	}
}


/*
* A <func decl>
*/
class FuncDecl extends Declaration {
	//-- Must be changed in part 1+2:

	FuncDecl(String n) {
		// Used for user functions:

		super(n);
		assemblerName = (CLess.underscoredGlobals() ? "_" : "") + n;
		//-- Must be changed in part 1:
	}

	@Override void check(DeclList curDecls) {
		//-- Must be changed in part 2:
	}

	@Override void checkWhetherArray(SyntaxUnit use) {
		//-- Must be changed in part 2:
	}

	@Override void checkWhetherFunction(int nParamsUsed, SyntaxUnit use) {
		//-- Must be changed in part 2:
	}

	@Override void checkWhetherSimpleVar(SyntaxUnit use) {
		//-- Must be changed in part 2:
	}

	@Override int dataSize() {
		return 0;
	}

	@Override void genCode(FuncDecl curFunc) {
		Code.genInstr("", ".globl", assemblerName, "");
		Code.genInstr(assemblerName, "pushl", "%ebp", "Start function "+name);
		Code.genInstr("", "movl", "%esp,%ebp", "");
		//-- Must be changed in part 2:
	}

	@Override void parse() {
		//-- Must be changed in part 1:
	}

	@Override void printTree() {
		//-- Must be changed in part 1:
	}
}


/*
* A <statm list>.
*/
class StatmList extends SyntaxUnit {
	//-- Must be changed in part 1:

	@Override void check(DeclList curDecls) {
		//-- Must be changed in part 2:
	}

	@Override void genCode(FuncDecl curFunc) {
		//-- Must be changed in part 2:
	}

	@Override void parse() {
		Log.enterParser("<statm list>");

		Statement lastStatm = null;
		while (Scanner.curToken != rightCurlToken) {
			Log.enterParser("<statement>");
			//-- Must be changed in part 1:
			Log.leaveParser("</statement>");
		}

		Log.leaveParser("</statm list>");
	}

	@Override void printTree() {
		//-- Must be changed in part 1:
	}
}


/*
* A <statement>.
*/
abstract class Statement extends SyntaxUnit {
	Statement nextStatm = null;


	static Statement makeNewStatement() {
		Statement statm; 

		if (Scanner.curToken==nameToken && Scanner.nextToken==leftParToken) { //call-statm
			//-- Must be changed in part 1:
		} else if (Scanner.curToken == nameToken) { //assign-statm
			//-- Must be changed in part 1:
		} else if (Scanner.curToken == forToken) { //for-statm
			//-- Must be changed in part 1:
		} else if (Scanner.curToken == ifToken) { //if-statm
			return new IfStatm();
		} else if (Scanner.curToken == returnToken) { //return-statm
			//-- Must be changed in part 1:
		} else if (Scanner.curToken == whileToken) { //while-statm
			return new WhileStatm();
		} else if (Scanner.curToken == semicolonToken) { //empty-statm
			return new EmptyStatm();
		} else {
			Scanner.expected("Statement");
		}
		return null;  // Just to keep the Java compiler happy. :-)
	}
}




/*
* An <empty statm>.
*/
class EmptyStatm extends Statement {
	//-- Must be changed in part 1+2:

	@Override void check(DeclList curDecls) {
		//-- Must be changed in part 2:
	}

	@Override void genCode(FuncDecl curFunc) {
		//-- Must be changed in part 2:
	}

	@Override void parse() {
		//-- Must be changed in part 1:
		Log.enterParser("<empty statm>");
		Scanner.skip(semiColonToken);
		Log.leaveParser("</empty statm>");
		
	}

	@Override void printTree() {
		//-- Must be changed in part 1:
	}
}


/*
* A <for-statm>.
*/
//-- Must be changed in part 1+2:

/*
* An <if-statm>.
*/
class IfStatm extends Statement {
	//-- Must be changed in part 1+2:

	@Override void check(DeclList curDecls) {
		//-- Must be changed in part 2:
	}

	@Override void genCode(FuncDecl curFunc) {
		//-- Must be changed in part 2:
	}

	@Override void parse() {
		//-- Must be changed in part 1:
	}

	@Override void printTree() {
		//-- Must be changed in part 1:
	}
}


/*
* A <return-statm>.
*/
//-- Must be changed in part 1+2:


/*
* A <while-statm>.
*/
class WhileStatm extends Statement {
	Expression test = new Expression();
	StatmList body = new StatmList();

	@Override void check(DeclList curDecls) {
		test.check(curDecls);
		body.check(curDecls);
	}

	@Override void genCode(FuncDecl curFunc) {
		String testLabel = Code.getLocalLabel(),
		endLabel  = Code.getLocalLabel();

		Code.genInstr(testLabel, "", "", "Start while-statement");
		test.genCode(curFunc);
		Code.genInstr("", "cmpl", "$0,%eax", "");
		Code.genInstr("", "je", endLabel, "");
		body.genCode(curFunc);
		Code.genInstr("", "jmp", testLabel, "");
		Code.genInstr(endLabel, "", "", "End while-statement");
	}

	@Override void parse() {
		Log.enterParser("<while-statm>");

		Scanner.readNext();
		Scanner.skip(leftParToken);
		test.parse();
		Scanner.skip(rightParToken);
		Scanner.skip(leftCurlToken);
		body.parse();
		Scanner.skip(rightCurlToken);

		Log.leaveParser("</while-statm>");
	}

	@Override void printTree() {
		Log.wTree("while (");  test.printTree();  Log.wTreeLn(") {");
		Log.indentTree();
		body.printTree();
		Log.outdentTree();
		Log.wTreeLn("}");
	}
}


//-- Must be changed in part 1+2:


/*
* An <expression list>.
*/

class ExprList extends SyntaxUnit {
	Expression firstExpr = null;

	@Override void check(DeclList curDecls) {
		//-- Must be changed in part 2:
	}

	@Override void genCode(FuncDecl curFunc) {
		//-- Must be changed in part 2:
	}

	@Override void parse() {
		Expression lastExpr = null;

		Log.enterParser("<expr list>");

		//-- Must be changed in part 1:

		Log.leaveParser("</expr list>");
	}

	@Override void printTree() {
		//-- Must be changed in part 1:
	}

	int nExprs() {
		int n = 0;
		//-- Must be changed in part 1:
		return n;
	}
}


/*
* An <expression>
*/
class Expression extends Operand {
	Operand firstOp = null;
	Expression nextExpr = null;

	@Override void check(DeclList curDecls) {
		//-- Must be changed in part 2:
	}

	@Override void genCode(FuncDecl curFunc) {
		//-- Must be changed in part 2:
	}

	@Override void parse() {
		Log.enterParser("<expression>");

		//-- Must be changed in part 1:

		Log.leaveParser("</expression>");
	}

	@Override void printTree() {
		//-- Must be changed in part 1:
	}
}


/*
* An <operator>
*/
abstract class Operator extends SyntaxUnit {
	Operand secondOp;
	//-- Must be changed in part 1+2:
}


//-- Must be changed in part 1+2:


/*
* An <operand>
*/
abstract class Operand extends SyntaxUnit {
	Operator nextOperator = null;
}


/*
* A <function call>.
*/
class FunctionCall extends Operand {
	//-- Must be changed in part 1+2:

	@Override void check(DeclList curDecls) {
		//-- Must be changed in part 2:

		if (nextOperator != null) nextOperator.check(curDecls);
	}

	@Override void genCode(FuncDecl curFunc) {
		//-- Must be changed in part 2:
	}

	@Override void parse() {
		//-- Must be changed in part 1:
	}

	@Override void printTree() {
		//-- Must be changed in part 1:
	}

	//-- Must be changed in part 1+2:
}


/*
* A <number>.
*/
class Number extends Operand {
	int numVal;

	@Override void check(DeclList curDecls) {
		//-- Must be changed in part 2:
		if (nextOperator != null) nextOperator.check(curDecls);
	}

	@Override void genCode(FuncDecl curFunc) {
		Code.genInstr("", "movl", "$"+numVal+",%eax", ""+numVal);
	}

	@Override void parse() {
		//-- Must be changed in part 1:
	}

	@Override void printTree() {
		Log.wTree("" + numVal);
	}
}


/*
* A <variable>.
*/

class Variable extends Operand {
	String varName;
	VarDecl declRef = null;
	Expression index = null;

	@Override void check(DeclList curDecls) {
		Declaration d = curDecls.findDecl(varName,this);
		if (index == null) {
			d.checkWhetherSimpleVar(this);
		} else {
			d.checkWhetherArray(this);
			index.check(curDecls);
		}
		declRef = (VarDecl)d;

		if (nextOperator != null) nextOperator.check(curDecls);
	}

	@Override void genCode(FuncDecl curFunc) {
		//-- Must be changed in part 2:
	}

	@Override void parse() {
		Log.enterParser("<variable>");
		//-- Must be changed in part 1:
	}

	@Override void printTree() {
		//-- Must be changed in part 1:
	}
}
