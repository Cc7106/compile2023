package GrammarTypes;

import Token.Token;


import java.util.StringJoiner;

public class ForStmt extends Node {
    //ForStmt → LVal '=' Exp
    private LVal lVal;

    private Token equal;
    private Exp exp;

    public ForStmt(LVal lVal, Token equal, Exp exp) {
        this.lVal = lVal;
        this.equal = equal;
        this.exp = exp;
    }

    public String toString() {
        StringJoiner output = new StringJoiner("\n");
        output.add(lVal.toString());
        output.add(equal.toString());
        output.add(exp.toString());
        output.add("<ForStmt>");
        return output.toString();
    }



}
