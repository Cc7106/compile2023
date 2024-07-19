package GrammarTypes;

import Token.Token;

import java.util.StringJoiner;

public class FuncFParam extends Node {
    // FuncFParam → BType Ident
    // FuncFParam → BType Ident '[' ']'
    // FuncFParam → BType Ident '[' ']''[' ConstExp ']'
    private Btype btype;
    private Token ident;
    private Token LBrack1;
    private Token RBrack1;

    private Token LBrack2;
    private ConstExp constExp;
    private Token RBrack2;

    public FuncFParam(Btype btype, Token ident) {
        //普通变量
        this.btype = btype;
        this.ident = ident;
        this.LBrack1 = null;
        this.RBrack1 = null;
        this.LBrack2 = null;
        this.constExp = null;
        this.RBrack2 = null;
    }

    public FuncFParam(Btype btype, Token ident, Token LBrack1, Token RBrack1) {
        //一维数组变量
        this.btype = btype;
        this.ident = ident;
        this.LBrack1 = LBrack1;
        this.RBrack1 = RBrack1;
        this.LBrack2 = null;
        this.constExp = null;
        this.RBrack2 = null;
    }
    public FuncFParam(Btype btype, Token ident, Token LBrack1, Token RBrack1, Token LBrack2, ConstExp constExp, Token RBrack2) {
        //二维数组变量
        this.btype = btype;
        this.ident = ident;
        this.LBrack1 = LBrack1;
        this.RBrack1 = RBrack1;
        this.LBrack2 = LBrack2;
        this.constExp = constExp;
        this.RBrack2 = RBrack2;
    }

    public String toString() {
        StringJoiner output = new StringJoiner("\n");
        output.add(btype.toString());
        output.add(ident.toString());
        if (LBrack1 != null) {
            output.add(LBrack1.toString());
            output.add(RBrack1.toString());
        }
        if (LBrack2 != null) {
            output.add(LBrack2.toString());
            output.add(constExp.toString());
            output.add(RBrack2.toString());
        }

        output.add("<FuncFParam>");
        return output.toString();
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

    @Override
    public Token getIdent() {
        return ident;
    }

    public ConstExp getConstExp() {
        return constExp;
    }
}
