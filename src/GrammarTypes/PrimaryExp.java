package GrammarTypes;

import Token.*;

import java.util.StringJoiner;

public class PrimaryExp extends Node {
    // PrimaryExp → '(' Exp ')'
    // PrimaryExp → LVal
    // PrimaryExp → Number
    private Token lParent;
    private Exp exp;
    private Token rParent;

    private LVal LVal;

    private Number number;

    public PrimaryExp(Token lParent, Exp exp, Token rParent) {
        // '(' Exp ')'
        this.lParent = lParent;
        this.exp = exp;
        this.rParent = rParent;
        this.LVal = null;
        this.number = null;
    }

    public PrimaryExp(LVal LVal) {
        // LVal
        this.lParent = null;
        this.exp = null;
        this.rParent = null;
        this.LVal = LVal;
        this.number = null;
    }

    public PrimaryExp(Number number) {
        // Number
        this.lParent = null;
        this.exp = null;
        this.rParent = null;
        this.LVal = null;
        this.number = number;
    }

    public String toString() {
        StringJoiner output = new StringJoiner("\n");
        if (lParent != null) {
            output.add(lParent.toString());
            output.add(exp.toString());
            output.add(rParent.toString());
        } else if (LVal != null) {
            output.add(LVal.toString());
        } else {
            output.add(number.toString());
        }
        output.add("<PrimaryExp>");
        return output.toString();
    }

    public Token getRParamType() {
        if (lParent != null) {
            return exp.getRParamType();
        } else if (LVal != null) {
            return LVal.getRParamType();
        } else {
            return number.getRParamType();
        }
    }

    @Override
    public int getDimension() {
        if (lParent != null) {
            return exp.getDimension();
        } else if (LVal != null) {
            return LVal.getDimension();
        } else {
            //常数
            return 0;
        }
    }
}
