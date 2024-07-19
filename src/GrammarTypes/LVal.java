package GrammarTypes;

import Token.Token;

import java.util.StringJoiner;

public class LVal extends Node {
    // LVal → Ident
    // LVal → Ident '[' Exp ']'
    // LVal → Ident '[' Exp ']''[' Exp ']'

    private Token ident;
    private Token LBrack1;
    private Exp exp1;
    private Token RBrack1;
    private Token LBrack2;
    private Exp exp2;
    private Token RBrack2;

    public LVal(Token ident) {
        //普通变量
        this.ident = ident;
        this.LBrack1 = null;
        this.exp1 = null;
        this.RBrack1 = null;
        this.LBrack2 = null;
        this.exp2 = null;
        this.RBrack2 = null;
    }
    public LVal(Token ident, Token LBrack1, Exp exp1, Token RBrack1) {
        //一维数组
        this.ident = ident;
        this.LBrack1 = LBrack1;
        this.exp1 = exp1;
        this.RBrack1 = RBrack1;
        this.LBrack2 = null;
        this.exp2 = null;
        this.RBrack2 = null;
    }

    public LVal(Token ident, Token LBrack1, Exp exp1, Token RBrack1, Token LBrack2, Exp exp2, Token RBrack2) {
        //二维数组
        this.ident = ident;
        this.LBrack1 = LBrack1;
        this.exp1 = exp1;
        this.RBrack1 = RBrack1;
        this.LBrack2 = LBrack2;
        this.exp2 = exp2;
        this.RBrack2 = RBrack2;
    }

    public String toString() {
        return defString(ident, LBrack1, exp1, RBrack1, LBrack2,
                exp2, RBrack2, null, null, "LVal");

    }

    /*
    public String toString() {
        StringJoiner output = new StringJoiner("\n");
        output.add(ident.toString());
        if (LBrack1 != null) {
            output.add(LBrack1.toString());
            output.add(exp1.toString());
            output.add(RBrack1.toString());
        }
        if (LBrack2 != null) {
            output.add(LBrack2.toString());
            output.add(exp2.toString());
            output.add(RBrack2.toString());
        }

        output.add("<LVal>");
        return output.toString();
    }

     */

    @Override
    public Token getIdent() {
        return ident;
    }

    public Token getRParamType() {
        return ident;
    }

    public int getDimension() {
        if (LBrack1 == null) {
            return 0;
        } else if (LBrack2 == null) {
            return 1;
        } else {
            return 2;
        }
    }

    public Exp getExp1() {
        return exp1;
    }

    public Exp getExp2() {
        return exp2;
    }
}
