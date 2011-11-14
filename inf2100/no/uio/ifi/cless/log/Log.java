package no.uio.ifi.cless.log;

/*
 * module Log
 */

import java.io.*;
import no.uio.ifi.cless.cless.CLess;
import no.uio.ifi.cless.error.Error;
import no.uio.ifi.cless.scanner.Scanner;
import static no.uio.ifi.cless.scanner.Token.*;

/*
 * Produce logging information.
 */
public class Log {
	public static boolean doLogBinding = false, doLogParser = false,
		doLogScanner = false, doLogTree = false;

	private static String logName, curTreeLine = "";
	private static int nLogLines = 0, parseLevel = 0;

    //Indicates the number of spaces to add whenever wTree(String s) is called
    private static int treeLevel = 0;


	public static void init() {
		logName = CLess.sourceBaseName + ".log";
	}

	public static void finish() {
		//-- Must be changed in part 0:
	}

	private static void writeLogLine(String data) {
		try {
			PrintWriter log = (nLogLines==0 ? new PrintWriter(logName) :
							   new PrintWriter(new FileOutputStream(logName,true)));
			log.println(data);	++nLogLines;
			log.close();
		} catch (FileNotFoundException e) {
			Error.error("Cannot open log file " + logName + "!");
		}
	}

	/*
	 * Make a note in the log file that an error has occured.
	 *
	 * @param message  The error message
	 */
	public static void noteError(String message) {
		if (nLogLines > 0)
			writeLogLine(message);
	}


	public static void enterParser(String symbol) {
		if (! doLogParser) return;

		//-- Must be changed in part 1:
        //System.err.println("Entering parser..");
        parseLevel++;

        String pLine = "Parser:" + stringRepeater("  ", parseLevel) + symbol;
        writeLogLine(pLine);
	}

	public static void leaveParser(String symbol) {
		if (! doLogParser) return;

		//-- Must be changed in part 1:
        String pLine = "Parser:" + stringRepeater("  ", parseLevel) + symbol;
        writeLogLine(pLine);

        parseLevel--;
        //System.err.println("Leaving parser..");
	}


    /**
     * @param String rep
     * @param int count : the number of times the character should be repeated
     * @return String s
     */
    public static String stringRepeater(String rep, int count){
        String s = "";
        for(int i = 0; i < count; i++)
            s += rep;
        return s;
    }


	/**
	 * Make a note in the log file that another source line has been read.
	 * This note is only made if the user has requested it.
	 *
	 * @param lineNum  The line number
	 * @param line	   The actual line
	 */
	public static void noteSourceLine(int lineNum, String line) {
		if (! doLogParser && ! doLogScanner) return;

		//-- Must be changed in part 0
		writeLogLine(lineNum + ": " + line);
	}

	/**
	 * Make a note in the log file that another token has been read
	 * by the Scanner module into Scanner.nextNextToken.
	 * This note will only be made if the user has requested it.
	 */
	public static void noteToken() {
		if (! doLogScanner) return;

		//-- Must be changed in part 0:
		String logline = "Scanner:  " + Scanner.nextNextToken + " ";

		if (Scanner.nextNextToken == nameToken)
			logline = logline + Scanner.nextNextName;
		else if (Scanner.nextNextToken == numberToken)
			logline = logline + Scanner.nextNextNum;

		writeLogLine(logline);
	}

	public static void noteBinding(String name, int lineNum, int useLineNum) {
		if (! doLogBinding) return;
		//-- Must be changed in part 2:
        
	}


	public static void wTree(String s) {
		if (curTreeLine.length() == 0) {
			for (int i = 1;	 i <= treeLevel;  ++i) curTreeLine += "	";
		}
		curTreeLine += s;
	}

	public static void wTreeLn() {
		writeLogLine("Tree:		" + curTreeLine);
		curTreeLine = "";
	}

	public static void wTreeLn(String s) {
		wTree(s);  wTreeLn();
	}

    /**
     * Increases the indentation of the tree by 1 level.
     */
	public static void indentTree() {
		//-- Must be changed in part 1:
        treeLevel++;
	}

    /**
     * Decreases the indentation of the tree by 1 level
     */
	public static void outdentTree() {
		//-- Must be changed in part 1:
        treeLevel--;
	}
}
