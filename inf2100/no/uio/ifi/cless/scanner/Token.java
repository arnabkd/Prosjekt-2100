package no.uio.ifi.cless.scanner;

import no.uio.ifi.cless.code.Code;

/*
 * class Token
 */

/*
 * The different kinds of tokens read by Scanner.
 */
public enum Token {

    addToken, assignToken, commaToken, divideToken, elseToken,
    eofToken, equalToken, forToken, greaterEqualToken, greaterToken,
    ifToken, intToken, leftBracketToken, leftCurlToken, leftParToken,
    lessEqualToken, lessToken, multiplyToken, nameToken, notEqualToken,
    numberToken, rightBracketToken, rightCurlToken, rightParToken,
    returnToken, semicolonToken, subtractToken, whileToken;

    public static boolean isOperand(Token t) {
        return t == numberToken || t == nameToken || t == leftParToken;
    }

    public static boolean isOperator(Token t) {
        //-- Must be changed in part 0:
        return isNumericalOperator(t) || isComparisonOperator(t);
    }

    public static boolean isNumericalOperator(Token t) {
        if (t == addToken) {
            return true;
        }
        if (t == subtractToken) {
            return true;
        }
        if (t == multiplyToken) {
            return true;
        }
        if (t == divideToken) {
            return true;
        }
        return false;
    }

    public static boolean isComparisonOperator(Token t) {
        if (t == equalToken) {
            return true;
        }
        if (t == notEqualToken) {
            return true;
        }
        if (t == lessToken) {
            return true;
        }
        if (t == lessEqualToken) {
            return true;
        }
        if (t == greaterToken) {
            return true;
        }
        if (t == greaterEqualToken) {
            return true;
        }
        return false;
    }

    public String getOpString() {
        if (this == addToken) {
            return "+";
        } else if (this == subtractToken) {
            return "-";
        } else if (this == multiplyToken) {
            return "*";
        } else if (this == divideToken) {
            return "/";
        } else if (this == equalToken) {
            return "==";
        } else if (this == notEqualToken) {
            return "!=";
        } else if (this == lessToken) {
            return "<";
        } else if (this == lessEqualToken) {
            return "<=";
        } else if (this == greaterToken) {
            return ">";
        } else if (this == greaterEqualToken) {
            return ">=";
        } else {
            return null;
        }
    }

    public String[] getAssemblerCodeArith() {
        String[] line = new String[4];
        String op = getOpString();
        line[0] = "";
        line[2] = "%ecx,%eax";
        if (op == null) {
            return line;
        } else if (op.equals("+")) {
            line[1] = "addl";
            line[3] = "Adding";
        } else if (op.equals("-")) {
            line[1] = "subl";
            line[3] = "Subtracting";
        } else if (op.equals("*")) {
            line[1] = "imull";
            line[3] = "Multiplying";
        } else if (op.equals("/")) {
            Code.genInstr("", "cdq", "", "");
            line[1] = "idivl";
            line[2] = "%ecx";
            line[3] = "Dividing";
        }
        return line;
    }

    public String[] getAssemblerCodeComp() {
        String[] line = new String[4];
        String opString = getOpString();
        String assemblyCode = compToAssembly(opString);
        line[0] = "";
        line[1] = assemblyCode;
        line[2] = "%al";
        line[3] = "Compare operator : " + opString;
        return line;
    }

    private String compToAssembly(String opString) {
        String a = "";
        if (opString.equals("==")) {
            a = "sete";
        } else if (opString.equals("!=")) {
            a = "setne";
        } else if (opString.equals("<")) {
            a = "setl";
        } else if (opString.equals("<=")) {
            a = "setle";
        } else if (opString.equals(">")) {
            a = "setg";
        } else if (opString.equals(">=")) {
            a = "setge";
        }
        return a;
    }
}
