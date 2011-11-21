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
    public static String sourceLine;
    private static int sourcePos;
    private static int nextCVal;

    public static void init() {
        try {
            sourceFile = new LineNumberReader(new FileReader(CLess.sourceName));
        } catch (FileNotFoundException e) {
            Error.error("Cannot read " + CLess.sourceName + "!");
        }
        sourceLine = "";
        sourcePos = 0;
        curC = nextC = ' ';
        nextCVal = 0;
        readNext();
        readNext();
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
        //System.out.println("Checking EOF : nextC is" + (int)nextC);
        return nextCVal != -1;
    }

    public static boolean isComment() {
        return curC == '#';
    }

    public static int curLineNum() {
        return (sourceFile == null ? 0 : sourceFile.getLineNumber());
    }

    private static boolean isNewLine(char c) {
        return (c == '\n' || c == '\r');
    }

    public static void readNext() {
        curC = nextC;
        if (!isMoreToRead()) {
            return;
        }

        if (isNewLine(curC)) {
            if (!sourceLine.equals("")) {
                Log.noteSourceLine(curLineNum(), sourceLine);
            }
            sourceLine = "";
            sourcePos = 0;
        } else {
            sourceLine = sourceLine + curC;
            sourcePos++;
        }

        //-- Must be changed in part 0:
        try {
            //Update nextC :
            nextCVal = sourceFile.read();
            nextC = (char) nextCVal;

            //1. If current character is # , read past the rest of the line
            if (isComment()) {
                sourceLine = sourceLine + nextC + sourceFile.readLine();
                if (!sourceLine.equals("")) {
                    Log.noteSourceLine(curLineNum(), sourceLine);
                }
                //sourceFile.readLine();
                sourceLine = "";
                sourcePos = 0;
                nextCVal = sourceFile.read();
                nextC = (char) nextCVal;
                readNext();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
