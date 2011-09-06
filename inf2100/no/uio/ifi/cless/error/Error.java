package no.uio.ifi.cless.error;

/*
 * module Error
 */

import no.uio.ifi.cless.log.Log;

/*
 * Print error messages.
 */
public class Error {
    public static void error(String where, String message) {
	//-- Must be changed in part 0:
	System.err.println("Error at line " + where + " : " + message);
	System.exit(1);
    }
	
    public static void error(String message) {
	error("", message);
    }
	
    public static void error(int lineNum, String message) {
	error((lineNum>0 ? "in line "+lineNum : ""), message);
    }

    public static void giveUsage() {
	System.err.println("Usage: cless [-c] [-log{B|P|S|T}] " +
			   "[-test{scanner|parser}] file");
	System.exit(2);
    }

    public static void init() {
	//-- Must be changed in part 0:
    }
    public static void finish() {
	//-- Must be changed in part 0:
    }
}
