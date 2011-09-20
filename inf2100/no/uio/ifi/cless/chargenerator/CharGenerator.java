package no.uio.ifi.cless.chargenerator;

/*
 * module CharGenerator
 */

import java.io.*;
import no.uio.ifi.cless.cless.CLess;
import no.uio.ifi.cless.error.Error;
import no.uio.ifi.cless.log.Log;

/*
 * Module for reading single characters.
 */
public class CharGenerator {
	public static char curC, nextC;

	private static LineNumberReader sourceFile = null;
	private static String sourceLine;
	private static int sourcePos;

	public static void init() {
		try {
			sourceFile = new LineNumberReader(new FileReader(CLess.sourceName));
		} catch (FileNotFoundException e) {
			Error.error("Cannot read " + CLess.sourceName + "!");
		}
		sourceLine = "";  sourcePos = 0;  curC = nextC = ' ';
		readNext();	 readNext();
	}

	public static void finish() {
		if (sourceFile != null) {
			try {
				sourceFile.close();
			} catch (IOException e) {
				Error.error("Could not close source file!");
			}
		}
	}

	public static boolean isMoreToRead() {
		//-- Must be changed in part 0:
		//Check if nextC is -1 (EOF)
		return nextC != -1;
	}

	public static boolean isComment() {
		return nextC == '#';
	}

	public static int curLineNum() {
		return (sourceFile == null ? 0 : sourceFile.getLineNumber());
	}

	private static boolean isNewLine(char c) { return (c=='\n' || c=='\r'); }

	public static void readNext() {
		curC = nextC;

		if(isNewLine(nextC)){

		}else if(isNewLine(curC)){
			sourceLine = "";
			sourcePos = 0;
		}else {
			sourceLine = sourceLine + curC;
			sourcePos++;
		}
		if (! isMoreToRead()) return;

		//-- Must be changed in part 0:
		try {
			//Update nextC :
			nextC = (char) sourceFile.read();
			//1. If current character is # , read past the rest of the line
			if (isComment())  {
				sourceFile.readLine();
				nextC = (char) sourceFile.read();
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}

