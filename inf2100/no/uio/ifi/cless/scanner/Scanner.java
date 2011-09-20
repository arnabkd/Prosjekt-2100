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
		//-- Must be changed in part 0:
	    } else if (CharGenerator.curC == '<' && CharGenerator.nextC == '=') {
		nextNextToken = lessEqualToken;
		CharGenerator.readNext();  CharGenerator.readNext();
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
	    }else if (CharGenerator.curC == '(') {
		nextNextToken = leftBracketToken;
		CharGenerator.readNext();
	    }else if (CharGenerator.curC == ')') {
		nextNextToken = rightBracketToken;
		CharGenerator.readNext();
	    }else if (CharGenerator.curC == '{') {
		nextNextToken = leftCurlToken;
		CharGenerator.readNext();
	    }else if (CharGenerator.curC == '}') {
		nextNextToken = rightCurlToken;
		CharGenerator.readNext();
	    }else if (CharGenerator.curC == '[') {
		nextNextToken = leftParToken;
		CharGenerator.readNext();
	    }else if (CharGenerator.curC == ']') {
		nextNextToken = rightParToken;
		CharGenerator.readNext();
	    }else if (CharGenerator.curC == ';') {
		nextNextToken = semicolonToken;
		CharGenerator.readNext();
	    }else if (isLetterAZ(CharGenerator.curC)) {
		//Need to check for : intToken, ifToken, elseToken, forToken, whileToken and lastly nameToken
		String curString = "" + CharGenerator.curC;
		while(!isDelim(CharGenerator.curC)) {
		    curString = curString + CharGenerator.nextC;
		    CharGenerator.readNext();	    
		}
		System.out.println("ended loop");
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
		}
		nextNextToken = nameToken;
		nextNextName = curString;
		
		
	    }else if (isDigit(CharGenerator.curC) || (CharGenerator.curC == '-' && isDigit(CharGenerator.nextC))) {
		String curNumber = "" + CharGenerator.curC;
		while (isDigit(CharGenerator.nextC)) {
		    System.out.println("NEI");
		    curNumber = curNumber + CharGenerator.nextC;
		    CharGenerator.readNext();
		}
		nextNextToken = numberToken;
		nextNextNum = Integer.parseInt(curNumber);
	    }else{
		illegal("Illegal symbol: '" + CharGenerator.curC + "'!");
	    }
	}
	
	Log.noteToken();
    }


    /**Checks if character is delimeter aka. not a letter or digit
     */
    private static boolean isDelim(char c){ 
	//return (c == ' ') || (c == ';') || (c == '\n') || (c == '\t') || (c == '(') || (c == ')') || (c == '{') ;
	//	System.out.println ("Testing if -" + c +"- is delimiter");
	return !isLetterAZ(c) && !isDigit(c) && (c != '_');
    }



    //Must be changed so that it only matches [a-zA-Z]
    private static boolean isLetterAZ(char c) {
	//-- Must be changed in part 0:
	return (65 <= c || c <= 90) || (97 <= c || c <= 122);
    }

    private static boolean isDigit(char c) {
	return Character.isDigit(c);
    }


    private static Token getToken(String s){
	if (s.equals("+")) return Token.addToken;
	else if (s.equals("-")) return Token.subtractToken;
	else if (s.equals("/")) return Token.divideToken;
	else if (s.equals("*")) return Token.multiplyToken;
	else if (s.equals("==")) return Token.equalToken;
	else if (s.equals("!=")) return Token.notEqualToken;
	else if (s.equals(">=")) return Token.greaterEqualToken;
	else if (s.equals("<=")) return Token.lessEqualToken;
	else if (s.equals(">")) return Token.greaterToken;
	else if (s.equals("<")) return Token.lessToken;
	else if (s.equals(";")) return Token.semicolonToken;
	else if (s.equals(",")) return Token.commaToken;
	else if (s.equals("(")) return Token.leftBracketToken;
	else if (s.equals(")")) return Token.rightBracketToken;
	else if (s.equals("{")) return Token.leftCurlToken;
	else if (s.equals("}")) return Token.rightCurlToken;
	else if (s.equals("int")) return Token.intToken;
	else if (s.equals("if")) return Token.ifToken;
	else if (s.equals("else")) return Token.elseToken;
	else if (s.equals("for")) return Token.forToken;
	else if (s.equals("while")) return Token.whileToken;
	else if (s.matches("[a-z_A-Z][a-z_0-9A-Z]*")) return Token.nameToken;
	else if (s.matches("-?[0-9]+")) return Token.numberToken;
	else return null;
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
}



/*
  public static void readNext() {
  curToken = nextToken;	 nextToken = nextNextToken;
  curName = nextName;	 nextName = nextNextName;
  curNum = nextNum;	 nextNum = nextNextNum;
  curLine = nextLine;	 nextLine = nextNextLine;

  nextNextToken = null;

  char curC = CharGenerator.curC;
  char nextC = CharGenerator.nextC;
  String curString = "";

  while (nextNextToken == null) {
  nextNextLine = CharGenerator.curLineNum();


  if (! CharGenerator.isMoreToRead()) {
  nextNextToken = eofToken;
  //-- Must be changed in part 0:
  } else {
  curString = curString + curC;
  CharGenerator.readNext(); //Read the first char of the next token

  curC = CharGenerator.curC;
  nextC = CharGenerator.nextC;

  System.out.println("nextC	 : " +nextC + "	 -- curC : " + curC);

  //New token contained in curString
  //if the current character is not a delimiter and if the next
  //character is a delimiter
  if (!isDelim(curC) && isDelim(nextC)) {
  curString += curC;
  nextNextToken = getToken(curString);
  System.out.println("curString " + curString);
  curString = null;
  CharGenerator.readNext();

  if(nextC == ' ') {
  CharGenerator.readNext();
  break;
  }

  if (nextNextToken == null){
  illegal("Illegal symbol: '" + CharGenerator.curC + "'!");
  System.exit(1);
  }
  break;
  }




  //illegal("Illegal symbol: '" + CharGenerator.curC + "'!");
  }
  }
  Log.noteToken();
  }*/


/*private static String getNameToken() {
  String name = "";
  char nextC = CharGenerator.nextC;
  while (isLetterAZ (nextC)) {
  name += nextC;

  CharGenerator.readNext();
  nextC = CharGenerator.nextC;
  }
  return name;
  } */
