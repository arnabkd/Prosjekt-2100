package no.uio.ifi.cless.syntax;

/*
 * module Syntax
 */
import no.uio.ifi.cless.chargenerator.CharGenerator;
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
        library = new GlobalDeclList();
        addLibFunc("putchar", 1);
        addLibFunc("putint", 1);
        addLibFunc("exit", 1);

        addLibFunc("getchar", 0);
        addLibFunc("getint", 0);
    }

    public static void finish() {
        //-- Must be changed in part 1:
        //Maybe not
    }

    public static void addLibFunc(String funcName, int argc) {
        ParamDeclList parList = new ParamDeclList();
        FuncDecl f = new FuncDecl(funcName);
        for (int i = 0; i < argc; i++) {
            parList.addDecl(new ParamDecl("x" + i, i));
        }
        f.paramList = parList;
        library.addDecl(f);
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

    Program() {
        progDecls = new GlobalDeclList();
    }

    @Override
    void check(DeclList curDecls) {
        progDecls.check(curDecls);

        if (!CLess.noLink) {
            // Check that 'main' has been declared properly:
            //-- Must be changed in part 2:

            if (!progDecls.declExists("main")) {
                Error.error("No main method defined in file");
            }

        }
    }

    @Override
    void genCode(FuncDecl curFunc) {
        progDecls.genCode(null);
    }

    @Override
    void parse() {
        Log.enterParser("<program>");

        progDecls.parse();
        if (Scanner.curToken != eofToken) {
            Scanner.expected("Declaration");
        }

        Log.leaveParser("</program>");

    }

    @Override
    void printTree() {
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

    DeclList() {
        //-- Must be changed in part 1:
    }

    @Override
    void check(DeclList curDecls) {
        outerScope = curDecls;

        Declaration dx = firstDecl;
        while (dx != null) {
            dx.check(this);
            dx = dx.nextDecl;
        }
    }

    Declaration getLastDecl() {
        Declaration current = firstDecl;
        while (current.nextDecl != null) {
            current = current.nextDecl;
        }
        return current;
    }

    @Override
    void printTree() {
        //-- Must be changed in part 1:
        Declaration currentDecl = firstDecl;
        while (currentDecl != null) {
            currentDecl.printTree();
            currentDecl = currentDecl.nextDecl;
        }
    }

    void addDecl(Declaration d) {
        //-- Must be changed in part 1:
        if (firstDecl == null) {
            firstDecl = d;
            return;
        } //If declList is empty, put d as firstDecl

        if (declExists(d)) {
            variableAlreadyDefinedError(d);
            return;
        }
//        System.err.println("adding " + d.name +" to declaration list");
        //Declaration does not exist yet, add it to the declList
        Declaration last = getLastDecl();
        last.nextDecl = d;
    }

    protected boolean declExists(String name) {
        Declaration curDeclaration = firstDecl;
        while (curDeclaration != null) {
            if (curDeclaration.name.equals(name)) {
                return true;
            }
            curDeclaration = curDeclaration.nextDecl;
        }
        return false;
    }

    protected boolean declExists(Declaration d) {
        return declExists(d.name);
    }

    protected void variableAlreadyDefinedError(Declaration d) {
        if (d == null || d.name == null) {
            return;
        }

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

    void printList() {
        Declaration d = firstDecl;
        System.err.println("**Start of " + this);
        while (d != null) {
            System.err.println("- " + d.name);
            d = d.nextDecl;
        }
        System.err.println("**End of " + this);
    }

    Declaration findDecl(String name, SyntaxUnit usedIn) {
        //-- Must be changed in part 2:
        Declaration current = firstDecl;
        while (current != null) {
            if (current.comPareTo(name) == 0) {
                return current;
            }
            current = current.nextDecl;
        }
        if (outerScope == null) {
            Syntax.error(usedIn, name + " is not defined.");
        }
        return outerScope.findDecl(name, usedIn);
    }

    Declaration getDecl(String name) {
        Declaration currentDecl = firstDecl;

        while (currentDecl != null && currentDecl.name != null) {
            if (currentDecl.name.compareTo(name) == 0) {
                return currentDecl;
            }
            currentDecl = currentDecl.nextDecl;
        }

        return null; //Declaration d does not exist yet
    }
}

/*
 * A list of global declarations.
 * (This class is not mentioned in the syntax diagrams.)
 */
class GlobalDeclList extends DeclList {

    @Override
    void genCode(FuncDecl curFunc) {
        //-- Must be changed in part 2:
        Declaration curDeclaration = firstDecl;
        if (!(firstDecl instanceof FuncDecl)) {
            Code.genInstr("", ".data", "", "");
        }
        while (curDeclaration != null) {
            curDeclaration.genCode(curFunc);
            genCodeForMemoryChange(curDeclaration, curDeclaration.nextDecl);
            curDeclaration = curDeclaration.nextDecl;
        }
    }

    @Override
    void parse() {

        /* Every line at outermost level in a .cless file starts with an int
         * token (and is followed by a name token) : int func (...) or
         * int var or int arr[..] etc.
         * Case 1 : function declaration
         * Case 2 : array declaration
         * Case 3 : single variable declaration
         */

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
                    GlobalSimpleVarDecl gsvd = new GlobalSimpleVarDecl(Scanner.nextName);
                    gsvd.parse();
                    addDecl(gsvd);
                }
            } else {
                Scanner.expected("Declaration");
            }
        }
    }

    private void genCodeForMemoryChange(Declaration curDeclaration, Declaration nextDecl) {
        if (nextDecl == null) {
            return;
        }

        if (curDeclaration instanceof VarDecl && nextDecl instanceof FuncDecl) {
            Code.genInstr("", ".text", "", "");
        }

        if (curDeclaration instanceof FuncDecl && nextDecl instanceof VarDecl) {
            Code.genInstr("", ".data", "", "");
        }
    }
}


/*
 * A list of local declarations.
 * (This class is not mentioned in the syntax diagrams.)
 */
class LocalDeclList extends DeclList {

    @Override
    void genCode(FuncDecl curFunc) {
        //-- Must be changed in part 2:
        Declaration curDeclaration = firstDecl;
        int i = 0;
        while (curDeclaration != null) {
            //Beregner hvor i stakken deklarasjonen er
            i = i - curDeclaration.dataSize();
            curDeclaration.assemblerName = (i + "(%ebp)");

            curDeclaration.genCode(curFunc);
            curDeclaration = curDeclaration.nextDecl;
        }

    }

    @Override
    void parse() {
        //-- Must be changed in part 1: -- NOT COMPLETE

        //<decl list> int ..; int .... ; ...... ; </decl list>
        while (Scanner.curToken == intToken) {
            if (Scanner.nextToken == nameToken) {
                if (Scanner.nextNextToken == leftBracketToken) { //LocalArrayDecl
                    LocalArrayDecl lad = new LocalArrayDecl(Scanner.nextName);
                    addDecl(lad);
                    lad.parse();
                } else { //LocalSimpleVarDecl
                    LocalSimpleVarDecl lsvd = new LocalSimpleVarDecl(Scanner.nextName);
                    addDecl(lsvd);
                    lsvd.parse();
                }
            }
        }
    }
}

/*
 * A list of parameter declarations.
 * (This class is not mentioned in the syntax diagrams.)
 */
class ParamDeclList extends DeclList {

    @Override
    void genCode(FuncDecl curFunc) {
        //-- Must be changed in part 2:
        int i = 8;
        Declaration localDeclaration = this.firstDecl;
        while (localDeclaration != null) {
            localDeclaration.assemblerName = (i + "(%ebp)");
            localDeclaration.genCode(curFunc);
            i += 4;
            localDeclaration = localDeclaration.nextDecl;
        }
    }

    @Override
    void parse() {
        //-- Must be changed in part 1:
        int paramNum = 0;
        Log.enterParser("<paramDeclList>");
        while (Scanner.curToken != rightParToken) {
            //Skip a comma token if there is one here
            //(cannot be before first param)
            if (Scanner.curToken == commaToken && paramNum != 0) {
                Scanner.skip(commaToken);
            }
            ParamDecl param = new ParamDecl(Scanner.nextName, paramNum);
            addDecl(param);
            param.parse();
            paramNum++;
        }
        Log.leaveParser("</paramDeclList>");
    }

    @Override
    void printTree() {

        Declaration current = firstDecl;
        while (current != null) {
            Log.wTree("int " + current.name);
            current = current.nextDecl;
            if (current != null) {
                Log.wTree(",");
            }
        }

    }

    int size() {
        Declaration current = firstDecl;
        int i = 0;
        while (current != null) {
            i++;
            current = current.nextDecl;
        }
        return i;
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
        //System.err.println(this + " has name : " + name);
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

    int comPareTo(String name) {
        return this.name.compareTo(name);
    }
}
/*
 * A <var decl>
 */

abstract class VarDecl extends Declaration {

    VarDecl(String n) {
        super(n);
    }

    @Override
    void checkWhetherFunction(int nParamsUsed, SyntaxUnit use) {
        Syntax.error(use, name + " is a variable and no function!");
    }

    @Override
    int dataSize() {
        return 4; //Should be overloaded when creating array subtypes
    }

    @Override
    void printTree() {
        Log.wTreeLn("int " + name + ";");
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

    @Override
    void check(DeclList curDecls) {
        visible = true;
        if (numElems < 0) {
            Syntax.error(this, "Arrays cannot have negative size!");
        }
    }

    @Override
    void checkWhetherArray(SyntaxUnit use) {
        /* OK */
    }

    @Override
    void checkWhetherSimpleVar(SyntaxUnit use) {
        Syntax.error(use, name + " is an array and no simple variable!");
    }

    @Override
    int dataSize() {
        return 4 * numElems;
    }

    @Override
    void genCode(FuncDecl curFunc) {
        //-- Must be changed in part 2:
        Code.genInstr("", ".globl", assemblerName, "");
        Code.genInstr(assemblerName, ".fill", dataSize() + "", "");
    }

    @Override
    void parse() {
        //-- Must be changed in part 1:
        Log.enterParser("<var decl>");
        Scanner.skip(intToken);
        Scanner.skip(nameToken);
        Scanner.skip(leftBracketToken);
        numElems = Scanner.nextNum;
        Scanner.skip(numberToken);
        Scanner.skip(rightBracketToken);
        Scanner.skip(semicolonToken);
        Log.leaveParser("</var decl>");
    }

    @Override
    void printTree() {
        //-- Must be changed in part 1:
        Log.wTreeLn(String.format("int %s[%d];", name, numElems));
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

    @Override
    void check(DeclList curDecls) {
        //-- Must be changed in part 2

        visible = true;
    }

    @Override
    void checkWhetherArray(SyntaxUnit use) {
        //-- Must be changed in part 2:
        Syntax.error(use, name + " is a simple variable, not an array");
    }

    @Override
    void checkWhetherSimpleVar(SyntaxUnit use) {
        /* OK */
    }

    @Override
    void genCode(FuncDecl curFunc) {
        //-- Must be changed in part 2:
        Code.genInstr("", ".globl", assemblerName, "");
        Code.genInstr(assemblerName, ".fill", "4", "");
    }

    @Override
    void parse() {
        Log.enterParser("<var decl>");

        //-- Must be changed in part 1:
        Scanner.skip(intToken);
        Scanner.skip(nameToken);
        Scanner.skip(semicolonToken);

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

    @Override
    void check(DeclList curDecls) {
        //-- Must be changed in part 2:
        visible = true;
        if (numElems < 0) {
            Syntax.error(this, name + " cannot have negative number of elements");
        }
    }

    @Override
    void checkWhetherArray(SyntaxUnit use) {
        //-- Must be changed in part 2:
    }

    @Override
    void checkWhetherSimpleVar(SyntaxUnit use) {
        //-- Must be changed in part 2:
        Syntax.error(this, name + " is an array, not a simple variable");
    }

    @Override
    int dataSize() {
        //-- Must be changed in part 2:
        return numElems * 4;
    }

    @Override
    void genCode(FuncDecl curFunc) {
        //-- Must be changed in part 2:
        //no assembly code required
    }

    @Override
    void parse() {
        Log.enterParser("<var decl>");


        //-- Must be changed in part 1:
        Scanner.skip(intToken);
        Scanner.skip(nameToken);
        Scanner.skip(leftBracketToken);
        numElems = Scanner.nextNum;
        Scanner.skip(numberToken);
        Scanner.skip(rightBracketToken);
        Scanner.skip(semicolonToken);


        Log.leaveParser("</var decl>");
    }

    @Override
    void printTree() {
        //-- Must be changed in part 1:
        Log.wTreeLn(String.format("int %s[%d];", name, numElems));
    }
}


/*
 * A local simple variable declaration
 */
class LocalSimpleVarDecl extends VarDecl {

    LocalSimpleVarDecl(String n) {
        super(n);
    }

    @Override
    void check(DeclList curDecls) {
        //-- Must be changed in part 2:
        visible = true;
        //System.err.println("Setting " + this.name + " to visible");
    }

    @Override
    void checkWhetherArray(SyntaxUnit use) {
        //-- Must be changed in part 2:
        Syntax.error(this, name + " is a simple variable, not an array!");
    }

    @Override
    void checkWhetherSimpleVar(SyntaxUnit use) {
        //-- Must be changed in part 2:
        //no change required here
    }

    @Override
    void genCode(FuncDecl curFunc) {
        //-- Must be changed in part 2:
        //No assembly code required
    }

    @Override
    void parse() {
        //-- Must be changed in part 1:
        Log.enterParser("<var decl>");
        Scanner.skip(intToken);
        Scanner.skip(nameToken);
        Scanner.skip(semicolonToken);
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

    ParamDecl(String n, int paramNum) {
        super(n);
        this.paramNum = paramNum;
    }

    @Override
    void check(DeclList curDecls) {
        //-- Must be changed in part 2:
        visible = true;
    }

    @Override
    void checkWhetherArray(SyntaxUnit use) {
        //-- Must be changed in part 2:
        Syntax.error(this, name + " cannot be an array");
    }

    @Override
    void checkWhetherSimpleVar(SyntaxUnit use) {
        //-- Must be changed in part 2:
    }

    @Override
    void genCode(FuncDecl curFunc) {
        //-- Must be changed in part 2:
    }

    @Override
    void parse() {
        Log.enterParser("<param decl>");


        //-- Must be changed in part 1:
        Scanner.skip(intToken);
        Scanner.skip(nameToken);

        Log.leaveParser("</param decl>");
    }
}


/*
 * A <func decl>
 */
class FuncDecl extends Declaration {
    //-- Must be changed in part 1+2:

    ParamDeclList paramList;
    LocalDeclList localVarList;
    StatmList body;
    String exitname;

    FuncDecl(String n) {
        // Used for user functions:

        super(n);
        assemblerName = (CLess.underscoredGlobals() ? "_" : "") + n;
        exitname = ".exit$" + n;

        //-- Must be changed in part 1:
        body = new StatmList();
        localVarList = new LocalDeclList();

        paramList = new ParamDeclList();
    }

    @Override
    void check(DeclList curDecls) {
        //-- Must be changed in part 2:
        paramList.check(curDecls);
        visible = true;
        localVarList.check(paramList);
        body.check(localVarList);
    }

    @Override
    void checkWhetherArray(SyntaxUnit use) {
        //-- Must be changed in part 2:
        Syntax.error(this, this.name + " is a function, not an array");
    }

    @Override
    void checkWhetherFunction(int nParamsUsed, SyntaxUnit use) {
        //-- Must be changed in part 2:
        int size = paramList.size();
        if (size != nParamsUsed) {
            Syntax.error(this, "illegal number of arguments (Expected " + size
                    + " but got " + nParamsUsed + ")");
        }
    }

    @Override
    void checkWhetherSimpleVar(SyntaxUnit use) {
        //-- Must be changed in part 2:
        Syntax.error(this, " is not a variable");
    }

    @Override
    int dataSize() {
        return 0;
    }

    @Override
    void genCode(FuncDecl curFunc) {
        Code.genInstr("", ".globl", assemblerName, "");
        Code.genInstr(assemblerName, "pushl", "%ebp", "int " + name + ";");
        Code.genInstr("", "movl", "%esp,%ebp", "");
        int size = localVarList.dataSize();
        if (size > 0) {
            Code.genInstr("", "subl", "$" + size + ",%esp", "Allocate "
                    + size + " bytes");
        }
        paramList.genCode(this);
        localVarList.genCode(this);
        body.genCode(this);
        Code.genInstr(exitname, "", "", "");
        if (size > 0) {
            Code.genInstr("", "addl", "$" + size + ",%esp", "Free "
                    + size + " bytes");
        }
        Code.genInstr("", "popl", "%ebp", "");
        Code.genInstr("", "ret", "", "end " + assemblerName);
    }

    @Override
    void parse() {
        //-- Must be changed in part 1:
        Log.enterParser("<func decl>");

        //skip int, name, leftPar token - "int funcName("
        Scanner.skip(intToken);
        Scanner.skip(nameToken);
        Scanner.skip(leftParToken);
        paramList.parse();

        //skip rightPar, leftCurl token - "){"
        Scanner.skip(rightParToken);
        Scanner.skip(leftCurlToken);
        localVarList.parse();
        Log.enterParser("<func body>");
        body.parse();
        Log.leaveParser("</func body>");
        //skip rightCurl token - "}"
        Scanner.skip(rightCurlToken);
        Log.leaveParser("</func decl>");
    }

    @Override
    void printTree() {
        //-- Must be changed in part 1:

        //function name and leftParToken
        Log.wTree(String.format("int %s(", name));

        //paramlist and rightParToken
        paramList.printTree();
        Log.wTreeLn(")");

        //leftCurl -> body -> rightCurl
        Log.wTreeLn("{");
        Log.indentTree();
        localVarList.printTree();
        body.printTree();
        Log.outdentTree();
        Log.wTreeLn("}");
    }
}


/*
 * A <statm list>.
 */
class StatmList extends SyntaxUnit {
    //-- Must be changed in part 1:

    Statement first;

    @Override
    void check(DeclList curDecls) {
        //-- Must be changed in part 2:
        Statement curStatement = first;
        while (curStatement != null) {
            curStatement.check(curDecls);
            curStatement = curStatement.nextStatm;
        }
    }

    @Override
    void genCode(FuncDecl curFunc) {
        //-- Must be changed in part 2:
        Statement curStatement = first;
        while (curStatement != null) {
            curStatement.genCode(curFunc);
            curStatement = curStatement.nextStatm;
        }
    }

    @Override
    void parse() {
        Log.enterParser("<statm list>");

        Statement current = null;

        if (Scanner.curToken != rightCurlToken) {
            first = Statement.makeNewStatement();
            current = first;
        }
        while (Scanner.curToken != rightCurlToken && current != null) {
            //-- Must be changed in part 1:
            Log.enterParser("<Statement>");
            current.parse();
            Log.leaveParser("</Statement>");

            if (Scanner.curToken != rightCurlToken) {
                current.nextStatm = Statement.makeNewStatement();
                current = current.nextStatm;
            }
        }

        Log.leaveParser("</statm list>");
    }

    @Override
    void printTree() {
        //-- Must be changed in part 1:
        Statement current = first;
        while (current != null) {
            current.printTree();
            current = current.nextStatm;
        }
    }
}


/*
 * A <statement>.
 */
abstract class Statement extends SyntaxUnit {

    Statement nextStatm = null;

    static Statement makeNewStatement() {
        Statement statm = null;

        if (Scanner.curToken == nameToken && Scanner.nextToken == leftParToken) { //call-statm
            //-- Must be changed in part 1:
            statm = new CallStatm();
        } else if (Scanner.curToken == nameToken) { //assign-statm
            //-- Must be changed in part 1:
            statm = new AssignStatm();
        } else if (Scanner.curToken == forToken) { //for-statm
            //-- Must be changed in part 1:
            statm = new ForStatm();
        } else if (Scanner.curToken == ifToken) { //if-statm
            statm = new IfStatm();
        } else if (Scanner.curToken == returnToken) { //return-statm
            //-- Must be changed in part 1:
            statm = new ReturnStatm();
        } else if (Scanner.curToken == whileToken) { //while-statm
            statm = new WhileStatm();
        } else if (Scanner.curToken == semicolonToken) { //empty-statm
            statm = new EmptyStatm();
        } else {
            Scanner.expected("Statement");
        }
        return statm;  // Just to keep the Java compiler happy. :-)
    }
}

/*
 * An <empty statm>.
 */
class EmptyStatm extends Statement {
    //-- Must be changed in part 1+2:

    @Override
    void check(DeclList curDecls) {
        //-- Must be changed in part 2:
    }

    @Override
    void genCode(FuncDecl curFunc) {
        //-- Must be changed in part 2:
    }

    @Override
    void parse() {
        //-- Must be changed in part 1:
        Log.enterParser("<empty statm>");
        Scanner.skip(semicolonToken);
        Log.leaveParser("</empty statm>");
    }

    @Override
    void printTree() {
        //-- Must be changed in part 1:
        Log.wTreeLn(";");
    }
}

/*
 * <call statm>
 */
class CallStatm extends Statement {
    //part1 + part2

    FunctionCall func;

    @Override
    void check(DeclList curDecls) {
        //part2
        func.check(curDecls);
    }

    @Override
    void genCode(FuncDecl curFunc) {
        //part2
        func.genCode(curFunc);
    }

    @Override
    void parse() {
        //part1
        Log.enterParser("<call-statm>");
        func = new FunctionCall(Scanner.curName);
        func.parse();
        Scanner.skip(semicolonToken);
        Log.leaveParser("</call-statm>");
    }

    @Override
    void printTree() {
        //part1
        func.printTree();
        Log.wTreeLn(";");
    }
}

class Assignment extends SyntaxUnit {

    Variable var;
    Expression exp;

    @Override
    void check(DeclList curDecls) {
        var.check(curDecls);
        exp.check(curDecls);
    }

    @Override
    void genCode(FuncDecl curFunc) {
        if (var.isArrayVar()) {
            var.index.genCode(curFunc);
            Code.genInstr("", "pushl", "%eax", "");
        }


        exp.genCode(curFunc);
        var.genCodeForStoring(curFunc);
    }

    @Override
    void parse() {
        Log.enterParser("<assignment>");
        var = new Variable(Scanner.nextName);
        var.parse();
        Scanner.skip(assignToken);
        exp = new Expression();
        exp.parse();
        Log.leaveParser("</assignment>");
    }

    @Override
    void printTree() {
        var.printTree();
        Log.wTree(" = ");
        exp.printTree();
    }
}


/*
 * <assign stam>
 */
class AssignStatm extends Statement {
    //part1 + part2

//    Variable var;
//    Expression exps;
    Assignment assignment = new Assignment();

    @Override
    void check(DeclList curDecls) {
        //part 2
//        var.check(curDecls);
//        exps.check(curDecls);
        assignment.check(curDecls);
    }

    @Override
    void genCode(FuncDecl curFunc) {
        //part 2
//        if (var.isArrayVar()) {
//            var.index.genCode(curFunc);
//            Code.genInstr("", "pushl", "%eax", "");
//        }
//        exps.genCode(curFunc);
//        var.genCodeForStoring(curFunc);
        assignment.genCode(curFunc);
    }

    @Override
    void parse() {
        Log.enterParser("<assign-statm>");
//        Log.enterParser("<assignment>");
//        var = new Variable(Scanner.nextName);
//        var.parse();
//        Scanner.skip(assignToken);
//        exps = new Expression();
//        exps.parse();
//        Log.leaveParser("</assignment>");
        assignment.parse();
        Log.leaveParser("</assign-statm>");
        Scanner.skip(semicolonToken);
    }

    @Override
    void printTree() {
//        var.printTree();
//        Log.wTree(" = ");
//        exps.printTree();
        assignment.printTree();
        Log.wTreeLn(";");
    }
}


/*
 * A <for-statm>. ??
 */
//-- Must be changed in part 1+2:

/*
 * An <if-statm>.
 */
class IfStatm extends Statement {
    //-- Must be changed in part 1+2:

    Expression eks = new Expression();
    StatmList st = new StatmList();
    ElseStatm els = null;

    @Override
    void check(DeclList curDecls) {
        //-- Must be changed in part 2:
        eks.check(curDecls);
        st.check(curDecls);
        if (els != null) {
            els.check(curDecls);
        }
    }

    @Override
    void genCode(FuncDecl curFunc) {
        //-- Must be changed in part 2:
        String label = Code.getLocalLabel(),
                label2 = Code.getLocalLabel();

        //Code.genInstr(testLabel, "", "", "Start if-statement");

        Code.genInstr("", "", "", "Start if-statement");
        eks.genCode(curFunc);
        Code.genInstr("", "cmpl", "$0,%eax", "");
        Code.genInstr("", "je", label2, "");
        st.genCode(curFunc);
        if (els == null) {
            Code.genInstr(label2, "", "", "End if-statmement");
        } else {
            //String label2 = Code.getLocalLabel();
            Code.genInstr("", "jmp", label, "End if-statement");
            Code.genInstr(label2, "", "", "Start else-statement");
            els.genCode(curFunc);
            Code.genInstr(label, "", "", "End else-statement");
        }


    }

    @Override
    void parse() {
        //-- Must be changed in part 1:
        Log.enterParser("<if-statm>");
        //Skip and parse the if statement inside here
        Scanner.skip(ifToken);
        Scanner.skip(leftParToken);
        eks.parse();
        Scanner.skip(rightParToken);
        Scanner.skip(leftCurlToken);
        st.parse();
        Scanner.skip(rightCurlToken);

        Log.enterParser("</if-statm>");

        if (Scanner.curToken == elseToken) {
            els = new ElseStatm();
            els.parse();
        }
    }

    @Override
    void printTree() {
        //-- Must be changed in part 1:
        Log.wTree("if (");
        eks.printTree();
        Log.wTreeLn(")");
        Log.wTreeLn("{");
        Log.indentTree();
        st.printTree();
        Log.outdentTree();
        Log.wTreeLn("}");
        if (els != null) {
            els.printTree();
        }
    }
}

class ElseStatm extends Statement {

    StatmList st = new StatmList();

    @Override
    void parse() {
        Log.enterParser("<else-statm>");

        Scanner.skip(elseToken);
        Scanner.skip(leftCurlToken);
        st.parse();
        Scanner.skip(rightCurlToken);

        Log.leaveParser("</else-statm>");
    }

    @Override
    void printTree() {
        Log.wTreeLn("else");
        Log.wTreeLn("{");
        Log.indentTree();
        st.printTree();
        Log.outdentTree();
        Log.wTreeLn("}");
    }

    @Override
    void check(DeclList curDecls) {
        st.check(curDecls);
    }

    @Override
    void genCode(FuncDecl curFunc) {
        st.genCode(curFunc);
    }
}

/*
 * A <return-statm>.
 */
//-- Must be changed in part 1+2:
class ReturnStatm extends Statement {
    //part1 + part2

    Expression exp = null;

    @Override
    void check(DeclList curDecls) {
        //part 2
        if (exp != null) {
            exp.check(curDecls);
        }
    }

    @Override
    void genCode(FuncDecl curFunc) {
        //part2
        if (exp != null) {
            exp.genCode(curFunc);
        }
        Code.genInstr("", "jmp", ".exit$" + curFunc.assemblerName,
                "return-statement for " + curFunc.name);
    }

    @Override
    void parse() {
        //part1
        Log.enterParser("<return-statm>");

        Scanner.skip(returnToken);
        exp = new Expression();
        exp.parse();

        Scanner.skip(semicolonToken);

        Log.leaveParser("</return-statm>");
    }

    @Override
    void printTree() {
        //part1
        Log.wTree("return ");
        exp.printTree();
        Log.wTreeLn(";");
    }
}

/*
 * A <while-statm>.
 */
class WhileStatm extends Statement {

    Expression test = null;
    StatmList body = new StatmList();

    @Override
    void check(DeclList curDecls) {
        test.check(curDecls);
        body.check(curDecls);
    }

    @Override
    void genCode(FuncDecl curFunc) {
        String testLabel = Code.getLocalLabel(),
                endLabel = Code.getLocalLabel();

        Code.genInstr(testLabel, "", "", "Start while-statement");
        test.genCode(curFunc);
        Code.genInstr("", "cmpl", "$0,%eax", "");
        Code.genInstr("", "je", endLabel, "");
        body.genCode(curFunc);
        Code.genInstr("", "jmp", testLabel, "");
        Code.genInstr(endLabel, "", "", "End while-statement");
    }

    @Override
    void parse() {

        Log.enterParser("<while-statm>");

        Scanner.skip(whileToken);
        Scanner.skip(leftParToken);
        test = new Expression();
        test.parse();
        Scanner.skip(rightParToken);
        Scanner.skip(leftCurlToken);
        body.parse();
        Scanner.skip(rightCurlToken);


        Log.leaveParser("</while-statm>");
    }

    @Override
    void printTree() {
        Log.wTree("while (");
        test.printTree();
        Log.wTreeLn(") {");
        Log.indentTree();
        body.printTree();
        Log.outdentTree();
        Log.wTreeLn("}");
    }
}
/*
 * A <for-statm>.
 */  //for-statm, if statm, else-statm, call statm

class ForStatm extends Statement {

    //ExprList exps = new ExprList();
    ForControl cont = new ForControl();
    StatmList body = new StatmList();

    @Override
    void check(DeclList curDecls) {
        cont.check(curDecls);
        body.check(curDecls);
    }

    @Override
    void genCode(FuncDecl curFunc) {
        //-- Must be changed in part 2
        String label = Code.getLocalLabel(),
                label2 = Code.getLocalLabel();

        cont.genCode(curFunc);
        Code.genInstr(label, "", "", "Start for-statement");
        cont.eks.genCode(curFunc);
        Code.genInstr("", "cmpl", "$0,%eax", "");
        Code.genInstr("", "je", label2, "");
        body.genCode(curFunc);
        cont.a2.genCode(curFunc);
        Code.genInstr("", "jmp", label, "");
        Code.genInstr(label2, "", "", "End for-statement");
    }

    @Override
    void parse() {
        //-- Must be changed in part 1
        Log.enterParser("<for-statm>");

        Scanner.skip(forToken);
        Scanner.skip(leftParToken);
        cont.parse();
        Scanner.skip(rightParToken);
        Scanner.skip(leftCurlToken);
        body.parse();
        Scanner.skip(rightCurlToken);

        Log.leaveParser("</for-statm>");
    }

    @Override
    void printTree() {
        //-- Must be changed in part 1
        Log.wTree("for (");
        cont.printTree();
        Log.wTreeLn(") {");
        Log.indentTree();
        body.printTree();
        Log.outdentTree();
        Log.wTreeLn("}");
    }
}

class ForControl extends Statement {

    Assignment a1 = null;//new Assignment();
    Expression eks = null; //new Expression();
    Assignment a2 = null; //new Assignment();

    @Override
    void parse() {
        Log.enterParser("<for-control>");

        a1 = new Assignment();
        a1.parse();
        Scanner.skip(semicolonToken);
        eks = new Expression();
        eks.parse();
        Scanner.skip(semicolonToken);
        a2 = new Assignment();
        a2.parse();
        Log.leaveParser("</for-control>");
    }

    @Override
    void printTree() {

        a1.printTree();
        Log.wTree("; ");
        eks.printTree();
        Log.wTree("; ");
        a2.printTree();
    }

    @Override
    void genCode(FuncDecl curFunc) {
        a1.genCode(curFunc);
//		eks.genCode(curFunc);
//		a2.genCode(curFunc);
    }

    @Override
    void check(DeclList curDecls) {
        a1.check(curDecls);
        eks.check(curDecls);
        a2.check(curDecls);
    }
}
//class ForControl extends Statement {
//
//    Variable var = null;
//    Variable var2 = null;
//    Expression eks = null;
//    Expression eks2 = null;
//    Expression eks3 = null;
//
//    @Override
//    void parse() {
//        Log.enterParser("<for-control>");
//
//        var = new Variable(Scanner.nextName);
//        var.parse();
//        Scanner.skip(assignToken);
//        eks = new Expression();
//        eks.parse();
//        Scanner.skip(semicolonToken);
//        eks2 = new Expression();
//        eks2.parse();
//        Scanner.skip(semicolonToken);
//        var2 = new Variable(Scanner.nextName);
//        var2.parse();
//        Scanner.skip(assignToken);
//        eks3 = new Expression();
//        eks3.parse();
//
//        Log.leaveParser("</for-control>");
//    }
//
//    @Override
//    void printTree() {
//        var.printTree();
//        Log.wTree("=");
//        eks.printTree();
//        Log.wTree("; ");
//        eks2.printTree();
//        Log.wTree("; ");
//        var2.printTree();
//        Log.wTree("=");
//        eks3.printTree();
//    }
//
//    @Override
//    void genCode(FuncDecl curFunc) {
//
//    }
//
//    @Override
//    void check(DeclList curDecls) {
//
//    }
//}

//-- Must be changed in part 1+2:
/*
 * An <expression list>.
 */
class ExprList extends SyntaxUnit {

    Expression firstExpr = null;

    @Override
    void check(DeclList curDecls) {
        //-- Must be changed in part 2:
        Expression curExpression = this.firstExpr;

        while (curExpression != null) {
            curExpression.check(curDecls);
            curExpression = curExpression.nextExpr;
        }
    }

    @Override
    void genCode(FuncDecl curFunc) {
        //-- Must be changed in part 2:
    }

    @Override
    void parse() {
        //Special case if there are no arguments
        if (Scanner.curToken == rightParToken) {
            Log.enterParser("<expr list>");
            Log.leaveParser("</expr list>");
            return;
        }

        Expression lastExpr = null;
        firstExpr = new Expression();
        Expression currentExp = firstExpr;

        Log.enterParser("<expr list>");
        while (currentExp != null) {
            currentExp.parse();
            if (Scanner.curToken != rightParToken) {
                currentExp.nextExpr = new Expression();
            }
            if (currentExp.nextExpr != null) {
                Scanner.skip(commaToken);
            }
            currentExp = currentExp.nextExpr;

        }

        //-- Must be changed in part 1:

        Log.leaveParser("</expr list>");

    }

    @Override
    void printTree() {
        //-- Must be changed in part 1:
        Expression currentExp = firstExpr;
        while (currentExp != null) {
            currentExp.printTree();
            currentExp = currentExp.nextExpr;
            if (currentExp != null) {
                Log.wTree(",");
            }
        }
    }

    /**
     *@ returns n - the size of the expression list
     */
    int nExprs() {
        int i = 0;
        //-- Must be changed in part 1:
        Expression current = firstExpr;
        while (current != null) {
            i++;
            current = current.nextExpr;
        }
        return i;
    }

    int size() {
        int size = 0;
        Expression curExpression = firstExpr;
        while (curExpression != null) {
            size++;
            curExpression = curExpression.nextExpr;
        }
        return size;
    }

    Expression[] toArray() {
        Expression[] expTab = new Expression[size()];
        int i = 0;
        for (Expression e = firstExpr; e != null; e = e.nextExpr) {
            expTab[i] = e;
            i++;
        }
        return expTab;
    }
}


/*
 * An <expression>
 */
class Expression extends Operand {

    Operand firstOp = null;
    Expression nextExpr = null;

    @Override
    void check(DeclList curDecls) {
        //-- Must be changed in part 2:
        firstOp.check(curDecls);
        if(nextOperator != null){
            nextOperator.check(curDecls);
        }
    }

    @Override
    void genCode(FuncDecl curFunc) {
        //-- Must be changed in part 2:
        firstOp.genCode(curFunc);
        Operator curOperator = firstOp.nextOperator;
        while (curOperator != null) {
            Code.genInstr("", "pushl", "%eax", "");
            System.err.println(curOperator.secondOp);
            curOperator.secondOp.genCode(curFunc);
            curOperator.genCode(curFunc);
            curOperator = curOperator.secondOp.nextOperator;
        }
    }

    @Override
    void parse() {
        int level = 0;
        Log.enterParser("<expression>");
        if (Scanner.curToken == leftParToken) {
            Scanner.skip(leftParToken);
            level++;
        }
        
        firstOp = Operand.getOperand();

        if (firstOp != null) {
            firstOp.parse();
        }

        if (Scanner.curToken == rightParToken && level == 1) {
            Scanner.skip(rightParToken);
            level--;
        }

        //-- Must be changed in part 1:
        Log.leaveParser("</expression>");

        if (Token.isOperator(Scanner.curToken)) {
            nextOperator = Token.isComparisonOperator(Scanner.curToken)
                    ? new ComparisonOperator() : new ArithmeticOperator();
            nextOperator.parse();
        }
    }
    @Override
    void printTree(){
        if(firstOp.nextOperator == null){
            firstOp.printTree();
            return;
        }
        
        Operand curOperand = firstOp;
        
        while(curOperand!=null){
            Log.wTree("(");
            curOperand.printTree();
            Log.wTree(")");
            
            if(curOperand.nextOperator == null){
                return;
            }
            
            curOperand.nextOperator.printTree();
            curOperand = curOperand.nextOperator.secondOp;
        }
    }
    


    
}

//-- Must be changed in part 1+2:
/*
 * An <operator>
 */
abstract class Operator extends SyntaxUnit {

    Operand secondOp;
    Token token;

    @Override
    void check(DeclList curDecls) {
        secondOp.check(curDecls);
    }

    @Override
    void parse() {
        Log.enterParser("<operator>");
        token = Scanner.curToken;
        Scanner.skip(token);
        Log.leaveParser("</operator>");

        secondOp = Operand.getOperand();

        if (secondOp == null) {
            Scanner.expected("Operand");
        } else {
            secondOp.parse();
        }
    }

    @Override
    void printTree() {
        String s = token.getOpString();
        if (s != null) {
            Log.wTree(s);
        }
        /*    if (secondOp != null) {
        if (secondOp instanceof Expression) {
        Log.wTree("(");
        secondOp.printTree();
        Log.wTree(")");
        } else {
        secondOp.printTree();
        }
        }*/


    }
    //-- Must be changed in part 1+2:

    void genCodeBeforeNextOperand(FuncDecl curFunc) {
//        if (secondOp != null) {
//            secondOp.genCode(curFunc);
//        }
//        Code.genInstr("", "pushl", "%eax", "");
    }
}

class ComparisonOperator extends Operator {

    @Override
    void genCode(FuncDecl curFunc) {
        super.genCodeBeforeNextOperand(curFunc);

        Code.genInstr("", "popl", "%ecx", "");
        Code.genInstr("", "cmpl", "%eax,%ecx", "");

        String[] line = token.getAssemblerCodeComp();
        Code.genInstr(line[0], line[1], line[2], line[3]);
        Code.genInstr("", "movzbl", "%al,%eax", "");
    }
}

//-- Must be changed in part 1+2:
class ArithmeticOperator extends Operator {

    @Override
    void genCode(FuncDecl curFunc) {
        super.genCodeBeforeNextOperand(curFunc);

        Code.genInstr("", "movl", "%eax,%ecx", "");
        Code.genInstr("", "popl", "%eax", "");

        String[] line = token.getAssemblerCodeArith();
        Code.genInstr(line[0], line[1], line[2], line[3]);

    }
}

//-- Must be changed in part 1+2:
/*
 * An <operand>
 */
abstract class Operand extends SyntaxUnit {

    Operator nextOperator = null;

    public void setOperator(Operator next) {
        nextOperator = next;
    }

    public void checkNextOperator(DeclList curDecl) {
        if (nextOperator != null) {
            nextOperator.check(curDecl);
        }
    }

    public static Operand getOperand() {
        if (Scanner.curToken == nameToken
                && Scanner.nextToken != leftParToken) {
            return new Variable(Scanner.nextName);
        } else if (Scanner.curToken == numberToken) {
            return new Number(Scanner.nextNum);
        } else if (Scanner.curToken == leftParToken) {
            return new Expression();
        } else if (Scanner.curToken == nameToken
                && Scanner.nextToken == leftParToken) {
            return new FunctionCall(Scanner.nextName);
        } else {
            return null;
        }
    }
}
/*
 * A <function call>.
 */

class FunctionCall extends Operand {
    //-- Must be changed in part 1+2:

    String func_assemblerName;
    String varName;
    ExprList exps;

    public FunctionCall(String varName) {
        this.varName = varName;
    }

    @Override
    void check(DeclList curDecls) {
        //-- Must be changed in part 2:
        FuncDecl func = (FuncDecl) curDecls.findDecl(varName, this);
        func.checkWhetherFunction(exps.size(), this);
        func_assemblerName = func.assemblerName;
        exps.check(curDecls);

        if (nextOperator != null) {
            nextOperator.check(curDecls);
        }
    }

    @Override
    void genCode(FuncDecl curFunc) {
        //-- Must be changed in part 2:
        Expression[] expTab = exps.toArray();
        System.err.println("exptab has length: " + expTab.length);
        for (int i = exps.size() - 1; i >= 0; i--) {
            genParamCall(expTab[i], i, curFunc);
        }

        Code.genInstr("", "call", func_assemblerName, "call " + varName);

        int i = 0;
        for (Expression e = exps.firstExpr; e != null; e = e.nextExpr) {
            Code.genInstr("", "popl", "%ecx", "Pop param #" + i);
            i++;
        }

        //if (nextOperator != null) {
        //nextOperator.genCode(curFunc);
        //}
    }

    private void genParamCall(Expression e, int num, FuncDecl func) {
        e.genCode(func);
        Code.genInstr("", "pushl", "%eax", "Push parameter #" + num);
    }

    @Override
    void parse() {
        /*if (!Syntax.library.declExists(varName)
                && !Syntax.program.progDecls.declExists(varName)) {
            Error.error("Function " + varName + " is not defined :"
                    + "\nLine " + CharGenerator.curLineNum() + ": "
                    + CharGenerator.sourceLine);
        }*/


        Log.enterParser("<function call>");
        Scanner.skip(nameToken);
        Scanner.skip(leftParToken);
        exps = new ExprList();
        exps.parse();
        Scanner.skip(rightParToken);
        Log.leaveParser("</function call");


        if (Token.isOperator(Scanner.curToken)) {
            nextOperator = Token.isComparisonOperator(Scanner.curToken)
                    ? new ComparisonOperator() : new ArithmeticOperator();
        }
        if (nextOperator != null) {
            nextOperator.parse();
        }
        //-- Must be changed in part 1:
    }

    @Override
    void printTree() {
        //-- Must be changed in part 1:
        Log.wTree(varName + "(");
        exps.printTree();
        Log.wTree(")");
//        if (nextOperator != null) {
//            nextOperator.printTree();
//        }

    }
    //-- Must be changed in part 1+2:
}


/*
 * A <number>.
 */
class Number extends Operand {

    int numVal;

    Number(int num) {
        numVal = num;
    }

    @Override
    void check(DeclList curDecls) {
        //-- Must be changed in part 2:
        if (nextOperator != null) {
            nextOperator.check(curDecls);
        }
    }

    @Override
    void genCode(FuncDecl curFunc) {
        Code.genInstr("", "movl", "$" + numVal + ",%eax", "" + numVal);
        //if (nextOperator != null) {
        //nextOperator.genCode(curFunc);
        //}
    }

    @Override
    void parse() {
        Log.enterParser("<number>");
        //-- Must be changed in part 1:
        Scanner.skip(numberToken);
        Log.leaveParser("</number>");

        if (Token.isOperator(Scanner.curToken)) {
            nextOperator = Token.isComparisonOperator(Scanner.curToken)
                    ? new ComparisonOperator() : new ArithmeticOperator();
        }
        if (nextOperator != null) {
            nextOperator.parse();
        }
    }

    @Override
    void printTree() {
        Log.wTree("" + numVal);
//        if (nextOperator != null) {
//            nextOperator.printTree();
//        }
    }
}


/*
 * A <variable>.
 */
class Variable extends Operand {

    String varName;
    VarDecl declRef = null;
    Expression index = null;

    public Variable(String varName) {
        this.varName = varName;
    }

    @Override
    void check(DeclList curDecls) {
        Declaration d = curDecls.findDecl(varName, this);

        if (index == null) {
            d.checkWhetherSimpleVar(this);
        } else {
            d.checkWhetherArray(this);
            index.check(curDecls);
        }
       
        declRef = (VarDecl) d;
         System.err.println("Running check");
         System.err.println("assembler name " + declRef.assemblerName);
         System.err.println("is declref null? " + declRef == null);
         System.err.println("----------");
        if (nextOperator != null) {
            nextOperator.check(curDecls);
        }
    }

    @Override
    void parse() {
        Log.enterParser("<variable>");
        Scanner.skip(nameToken);
        Log.leaveParser("</variable>");

        if (Scanner.curToken == leftBracketToken) {
            Scanner.skip(leftBracketToken);
            index = new Expression();
            index.parse();
            Scanner.skip(rightBracketToken);
        }
        if (Token.isOperator(Scanner.curToken)) {
            nextOperator = Token.isComparisonOperator(Scanner.curToken)
                    ? new ComparisonOperator() : new ArithmeticOperator();
        }
        if (nextOperator != null) {
            nextOperator.parse();
        }
        //-- Must be changed in part 1:



    }

    @Override
    void printTree() {
        Log.wTree(varName);
        //-- Must be changed in part 1:
        if (index != null) {
            Log.wTree("[");
            index.printTree();
            Log.wTree("]");
        }
//        if (nextOperator != null) {
//            nextOperator.printTree();
//        }
    }

    void genCodeForStoring(FuncDecl curDecl) {
        if (index != null) {
//            Code.genInstr("", "pushl", "%eax", "");
            Code.genInstr("", "leal", declRef.assemblerName + ",%edx", varName
                    + "[index] being assigned");
            Code.genInstr("", "popl", "%ecx", "");
            Code.genInstr("", "movl", "%eax,(%edx,%ecx,4)", "");
        } else {
            Code.genInstr("", "movl", "%eax," + declRef.assemblerName,
                    varName + " being assigned");
        }
    }

    /** This method is ONLY for storing the variable in %eax
     *  (i.e this will be used when the value of this variable is returned).
     *  This will NOT be used for assigning a value to this variable. Index can
     *  be calculated for variables in arrays, but that'll maybe be done later.
     */
    @Override
    void genCode(FuncDecl curFunc) {

        if (isArrayVar()) {
            index.genCode(curFunc);
            Code.genInstr("", "leal", declRef.assemblerName + ",%edx",
                    "Putting" + varName + "[index] in %eax");
            Code.genInstr("", "movl", "(%edx,%eax,4),%eax", "");
        } else {
            Code.genInstr("", "movl", declRef.assemblerName + ",%eax",
                    "Putting " + varName + " in %eax");
        }
        //if (nextOperator != null) {
        //nextOperator.genCode(curFunc);
        //}
    }

    boolean isArrayVar() {
        return index != null;
    }
}
