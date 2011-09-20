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
			} else {
				illegal("Illegal symbol: '" + CharGenerator.curC + "'!");
			}
		}
		Log.noteToken();
	}

	/*
	public static void readNext() {
		curToken = nextToken;  nextToken = nextNextToken;
		curName = nextName;	 nextName = nextNextName;
		curNum = nextNum;  nextNum = nextNextNum;
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

				System.out.println("nextC  : " +nextC + "  -- curC : " + curC);

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

	private static boolean isDelim(char c){
		return (c == ' ') || (c == ';') || (c == '\n') || (c == '\t') || (c == '(') || (c == ')');
	}

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

	//Must be changed so that it only matches [a-zA-Z]
	private static boolean isLetterAZ(char c) {
		//-- Must be changed in part 0:
		return Character.isLetter(c);
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

	// private static Token numericalOperator(char c) {
	//	switch (c) {
	//	case '+': return Token.addToken;
	//	case '-': return Token.subtractToken;
	//	case '/': return Token.divideToken;
	//	case '*': return Token.multiplyToken;
	//	default : return null;
	//	}
	// }

	// private static Token comparisonOperator(char c1, char c2) {
	//	if (c1 == '=' && c2 == '=') return Token.equalToken;
	//	if (c1 == '!' && c2 == '=') return Token.notEqualToken;
	//	if (c1 == '>' && c2 == '=') return Token.greaterEqualToken;
	//	if (c1 == '<' && c2 == '=') return Token.lessEqualToken;
	//	return null;
	// }

	// private static Token comparisonOperator (char c) {
	//	if (c == '<') return Token.lessToken;
	//	if (c == '>') return Token.greaterToken;
	//	return null;
	// }

	// }
	// private static boolean isCommentStart(char c) {
	//	return
	// }

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
