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
				CharGenerator.readNext(); //Read the first char of the next token
				char curC = CharGenerator.curC;
				char nextC = CharGenerator.nextC;


				//Case 1 : nameToken / intToken
				//Case 1a: intToken
				//Case 1b: nameToken

				//Case 2 : numberToken

				//Case 3 : operatorToken

				//Case 4 : forToken

				//Case 5 : ifToken
				//Case 4a : Brackets
				//Case 4b :



				//}
				//illegal("Illegal symbol: '" + CharGenerator.curC + "'!");
			}
		}
		Log.noteToken();
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

	private static boolean isLetterAZ(char c) {
		//-- Must be changed in part 0:
		return Character.isLetter(c);
	}

	private static boolean isDigit(char c) {
		return Character.isDigit(c);
	}

	private static Token isNumericalOperator(char c) {
		switch (c) {
		case '+': return Token.addToken;
		case '-': return Token.subtractToken;
		case '/': return Token.divideToken;
		case '*': return Token.multiplyToken;
		default : return null;
		}
	}

	private static Token isComparisonOperator(char c1, char c2) {
		if (c1 == '=' && c2 == '=') return Token.equalToken;
		if (c1 == '!' && c2 == '=') return Token.notEqualToken;
		if (c1 == '>' && c2 == '=') return Token.greaterEqualToken;
		if (c1 == '<' && c2 == '=') return Token.lessEqualToken;
		return null;
	}

	private static Token isComparisonOperator (char c) {
		if (c == '<') return Token.lessToken;
		if (c == '>') return Token.greaterToken;
		return null;
	}

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
