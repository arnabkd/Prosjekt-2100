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
		return false;
	}

	public static int curLineNum() {
		return (sourceFile == null ? 0 : sourceFile.getLineNumber());
	}

	public static void readNext() {
		curC = nextC;
		if (! isMoreToRead()) return;
		
		//-- Must be changed in part 0:
	}
}
