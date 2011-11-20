package no.uio.ifi.cless.scanner;

/*
 * module Scanner
 */

import no.uio.ifi.cless.chargenerator.CharGenerator;
import no.uio.ifi.cless.error.Error;
import no.uio.ifi.cless.log.Log;
import static no.uio.ifi.cless.scanner.Token.*;

/*
 * Module for forming characters into tokens.
 */
public class Scanner {
	public static Token curToken, nextToken, nextNextToken;
	public static String curName, nextName, nextNextName;
	public static int curNum, nextNum, nextNextNum;
	public static int curLine, nextLine, nextNextLine;

	public static void init() {
		//-- Must be changed in part 0:
        readNext();readNext();readNext();
	}

	public static void finish() {
		//-- Must be changed in part 0:
	}

	public static void readNext() {
		curToken = nextToken;  nextToken = nextNextToken;
		curName = nextName;	 nextName = nextNextName;
		curNum = nextNum;  nextNum = nextNextNum;
		curLine = nextLine;	 nextLine = nextNextLine;

		nextNextToken = null;

		while (nextNextToken == null) {
			nextNextLine = CharGenerator.curLineNum();



			if (! CharGenerator.isMoreToRead()) {
				nextNextToken = eofToken;
				Log.noteToken();
				return;
				//-- Must be changed in part 0:
			}


			if (CharGenerator.curC == '\r' || CharGenerator.curC == '\n' || CharGenerator.curC == '\t' || CharGenerator.curC == ' '){
				CharGenerator.readNext();
			}else if (CharGenerator.curC == '\''){
				CharGenerator.readNext();
				int val = (int) CharGenerator.curC;
				nextNextToken = numberToken;
				nextNextNum = val;
				CharGenerator.readNext(); CharGenerator.readNext();
			}else if(CharGenerator.curC == '/' && CharGenerator.nextC == '*'){
				CharGenerator.readNext(); CharGenerator.readNext();
				while(!(CharGenerator.curC == '*' && CharGenerator.nextC == '/')){
					CharGenerator.readNext();
				} //now curC = '*' and nextC = '/'
				CharGenerator.readNext();CharGenerator.readNext(); //read past the '*' and '/'
			}else if (CharGenerator.curC == '<' && CharGenerator.nextC == '=') {
				nextNextToken = lessEqualToken;
				CharGenerator.readNext();  CharGenerator.readNext();
			}else if(CharGenerator.curC == ','){
				nextNextToken = commaToken;
				CharGenerator.readNext();
			}else if (CharGenerator.curC == '<') {
				nextNextToken = lessToken;
				CharGenerator.readNext();
			}else if (CharGenerator.curC == '=' && CharGenerator.nextC == '=') {
				nextNextToken = equalToken;
				CharGenerator.readNext(); CharGenerator.readNext();
			}else if (CharGenerator.curC == '!' && CharGenerator.nextC == '=') {
				nextNextToken = notEqualToken;
				CharGenerator.readNext(); CharGenerator.readNext();
			}else if (CharGenerator.curC == '>' && CharGenerator.nextC == '=') {
				nextNextToken = greaterEqualToken;
				CharGenerator.readNext(); CharGenerator.readNext();
			}else if (CharGenerator.curC == '>') {
				nextNextToken = greaterToken;
				CharGenerator.readNext();
			}else if (CharGenerator.curC == '=') {
				nextNextToken = assignToken;
				CharGenerator.readNext();
			}else if (CharGenerator.curC == '+') {
				nextNextToken = addToken;
				CharGenerator.readNext();
			}else if (CharGenerator.curC == '-' && !isDigit(CharGenerator.nextC)) {
				nextNextToken = subtractToken;
				CharGenerator.readNext();
			}else if (CharGenerator.curC == '*') {
				nextNextToken = multiplyToken;
				CharGenerator.readNext();
			}else if (CharGenerator.curC == '/') {
				nextNextToken = divideToken;
				CharGenerator.readNext();
			}else if (CharGenerator.curC == '[') {
				nextNextToken = leftBracketToken;
				CharGenerator.readNext();
			}else if (CharGenerator.curC == ']') {
				nextNextToken = rightBracketToken;
				CharGenerator.readNext();
			}else if (CharGenerator.curC == '{') {
				nextNextToken = leftCurlToken;
				CharGenerator.readNext();
			}else if (CharGenerator.curC == '}') {
				nextNextToken = rightCurlToken;
				CharGenerator.readNext();
			}else if (CharGenerator.curC == '(') {
				nextNextToken = leftParToken;
				CharGenerator.readNext();
			}else if (CharGenerator.curC == ')') {
				nextNextToken = rightParToken;
				CharGenerator.readNext();
			}else if (CharGenerator.curC == ';') {
				nextNextToken = semicolonToken;
				CharGenerator.readNext();
			}else if (isLetterAZ(CharGenerator.curC)) {
				//Need to check for : intToken, ifToken, elseToken, forToken, whileToken and lastly nameToken
				String curString = "";
				while(isPartOfVarName(CharGenerator.curC)){//CharGenerator.isMoreToRead() && isPartOfVarName(CharGenerator.curC)){
					curString = curString + CharGenerator.curC;
					CharGenerator.readNext();
				}

				if(curString.equals("if")) {
					nextNextToken = ifToken;
				}else if (curString.equals("else")){
					nextNextToken = elseToken;
				}else if (curString.equals("int")) {
					nextNextToken = intToken;
				}else if (curString.equals("for")) {
					nextNextToken = forToken;
				}else if (curString.equals("while")) {
					nextNextToken = whileToken;
				}else if(curString.equals("return")){
					nextNextToken = returnToken;
				}else {
					nextNextToken = nameToken;
					nextNextName = curString;
				}
			}else if (isDigit(CharGenerator.curC) || (CharGenerator.curC == '-' && isDigit(CharGenerator.nextC))) {
				boolean negative = false;
				String curNumber ="";
				if(CharGenerator.curC == '-'){negative = true; CharGenerator.readNext();}
				while (isDigit(CharGenerator.curC)) {
					curNumber = curNumber + CharGenerator.curC;
					CharGenerator.readNext();
				}
				nextNextToken = numberToken;
				nextNextNum = Integer.parseInt(curNumber);
				if(negative) nextNextNum = 0 - nextNextNum;

			}else{
				illegal("Illegal symbol: '" + CharGenerator.curC + "'!");
			}
		}

		Log.noteToken();
	}




	/**Checks if character can be part of a variable
	 */
	private static boolean isPartOfVarName(char c){
		return isLetterAZ(c) || isDigit(c) || (c == '_');
	}

	//Must be changed so that it only matches [a-zA-Z]
	private static boolean isLetterAZ(char c) {
		//-- Must be changed in part 0:
		return (65 <= c && c <= 90) || (97 <= c && c <= 122);
	}

	private static boolean isDigit(char c) {
		return (c >= '0' && c<= '9');
	}




	// Various error reporting methods
	// (They are placed in this package because most of them include
	// information found here.)

	public static void illegal(String message) {
		Error.error(curLine, message);
	}

	public static void expected(String exp) {
		illegal(exp + " expected, but found a " + curToken + "!");
	}

	public static void check(Token t) {
		if (curToken != t)
			expected("A " + t);
	}

	public static void check(Token t1, Token t2) {
		if (curToken != t1 && curToken != t2)
			expected("A " + t1 + " or a " + t2);
	}

	public static void skip(Token t) {
		check(t);  readNext();
	}

	public static void skip(Token t1, Token t2) {
		check(t1,t2);  readNext();
	}

    public static void printDump(){
        System.err.println(String.format("Tokens : [%s][%s][%s]", curToken, nextToken, nextNextToken));
        System.err.println(String.format("Names  : [%s][%s][%s]", curName, nextName, nextNextName));
        System.err.println(String.format("Numbers: [%d][%d][%d]", curNum,nextNum, nextNextNum));
        System.err.println(String.format("Lines  : [%d][%d][%d]", curLine,nextLine, nextNextLine));
        System.err.println(String.format("CharGenerator.sourceline : %s", CharGenerator.sourceLine));
    }
}
